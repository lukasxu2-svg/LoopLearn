import React, {useState, useEffect} from "react";
import {useNavigate} from "react-router-dom";
import {planAPI, videoAPI} from "../../services/api.js";
import "./styles/Dashboard.css";
import {isTokenExpired} from "../auth/utils/authUtils.js";
import PlanCatalog from "./components/PlanCatalog/PlanCatalog";
import SubscriptionModal from "./components/SubscriptionModal/SubscriptionModal";
import CancellationModal from "./components/PopupModals/CancellationModal";
import CancelNextModal from "./components/PopupModals/CancelNextModal";
import PaymentModal from "./components/PopupModals/PaymentModal";
import {useDashboardContext} from "./context/DashboardContext";

export interface User {
    email?: string;
    firstName?: string;
    lastName?: string;
}

function DashboardPage({
                           setIsAuthenticated,
                       }: {
    setIsAuthenticated: (v: boolean) => void;
}) {
    const navigate = useNavigate();
    const [user, setUser] = useState<User>({
        email: "",
        firstName: "",
        lastName: "",
    });

    const [activeTab, setActiveTab] = useState<"subscription" | "videos">(
        "subscription",
    );
    const [selectedLanguage, setSelectedLanguage] = useState<string>("java");
    const [selectedTier, setSelectedTier] = useState<string>("basic");
    const [selectedTopic, setSelectedTopic] = useState<string>("inheritance");

    const [plans, setPlans] = useState<any[]>([]);
    const [loading, setLoading] = useState(true);

    const {
        subscription,
        errorMessage,
        successMessage,
        loadSubscriptionData,
        setSuccessMessage,
        setErrorMessage,
        clearNotifications,
    } = useDashboardContext();

    const [showCancelModal, setShowCancelModal] = useState(false);
    const [showCancelNextModal, setShowCancelNextModal] = useState(false);
    const [modalLoading, setModalLoading] = useState(false);

    const [showFreePlanModal, setShowFreePlanModal] = useState(false);
    const [showPaymentModal, setShowPaymentModal] = useState(false);
    const [selectedPlan, setSelectedPlan] = useState<any | null>(null);
    const [selectedPaymentMethod, setSelectedPaymentMethod] = useState("paypal");

    const [videoCatalog, setVideoCatalog] = useState<any[]>([]);

    useEffect(() => {
        const load = async () => {
            if (isTokenExpired()) {
                handleLogout();
                return;
            }

            await loadDashboardData();
        };

        load();
    }, []);

    useEffect(() => {
        if (!selectedLanguageData || selectedLanguageData.tiers === undefined) {
            return;
        }
        const currentTier =
            selectedLanguageData.tiers.find((tier) => tier.id === selectedTier) ??
            selectedLanguageData.tiers[0];

        if (!currentTier.topics.some((topic) => topic.id === selectedTopic)) {
            setSelectedTopic(currentTier.topics[0]?.id ?? "");
        }
    }, [selectedLanguage, selectedTier, selectedTopic]);

    const loadDashboardData = async () => {
        setLoading(true);
        clearNotifications();
        try {
            const userData = localStorage.getItem("user");
            if (userData) {
                setUser(JSON.parse(userData));
            }
            await loadSubscriptionData();
            await loadPlans();
            await loadVideoCatalog();

            setSuccessMessage("Successfully loaded dashboard data");
        } catch (err) {
            setErrorMessage(err.message || "Failed to load dashboard data");
        } finally {
            setLoading(false);
        }
    };

    const loadPlans = async () => {
        try {
            const plansResponse = await planAPI.getPlans();
            const sortedPlans = plansResponse.data.sort(
                (a: any, b: any) => a.rank - b.rank,
            );
            setPlans(sortedPlans);
        } catch (err) {
            throw new Error("Failed to load plan data");
        }
    };

    const loadVideoCatalog = async () => {
        try {
            const videoCatalog = await videoAPI.getVideoCatalog();
            setVideoCatalog(videoCatalog.data.videoCatalog);
        } catch (err) {
            throw new Error("Failed to video catalog");
        }
    };

    const handleLogout = () => {
        localStorage.removeItem("token");
        localStorage.removeItem("user");
        setIsAuthenticated(false);
        navigate("/login");
    };

    const selectedLanguageData =
        videoCatalog.length > 0
            ? videoCatalog.find((language) => language.key === selectedLanguage)
            : [];

    const selectedTierData =
        selectedLanguageData?.tiers == undefined
            ? []
            : selectedLanguageData?.tiers?.find((tier) => tier.id === selectedTier);

    const selectedTopicData =
        selectedTierData?.topics == undefined
            ? []
            : selectedTierData?.topics?.find((topic) => topic.id === selectedTopic);

    if (loading) {
        return <div className="dashboard-loading">Loading your dashboard...</div>;
    }

    return (
        <>
            <div className="dashboard-container">
                <header className="dashboard-header">
                    <div className="header-content">
                        <div>
                            <h1>Dashboard</h1>
                            <p>Welcome, {user?.firstName}!</p>
                        </div>
                        <button className="logout-button" onClick={handleLogout}>
                            Logout
                        </button>
                    </div>
                </header>

                {errorMessage && (
                    <div className="alert alert-error">{errorMessage}</div>
                )}
                {successMessage && (
                    <div className="alert alert-success">{successMessage}</div>
                )}

                <CancelNextModal
                    showCancelNextModal={showCancelNextModal}
                    setShowCancelNextModal={setShowCancelNextModal}
                    loading={modalLoading}
                    setLoading={setModalLoading}
                />

                <CancellationModal
                    showCancelModal={showCancelModal}
                    setShowCancelModal={setShowCancelModal}
                    loading={modalLoading}
                    setLoading={setModalLoading}
                />

                <PaymentModal
                    showPaymentModal={showPaymentModal}
                    setShowPaymentModal={setShowPaymentModal}
                    showFreePlanModal={showFreePlanModal}
                    setShowFreePlanModal={setShowFreePlanModal}
                    selectedPlan={selectedPlan}
                    selectedPaymentMethod={selectedPaymentMethod}
                    setSelectedPaymentMethod={setSelectedPaymentMethod}
                    user={user}
                    loading={modalLoading}
                    setLoading={setModalLoading}
                />

                <div className="dashboard-shell">
                    <aside className="dashboard-sidebar">
                        <div className="sidebar-group">
                            <button
                                className={`sidebar-tab ${activeTab === "subscription" ? "active" : ""}`}
                                onClick={() => setActiveTab("subscription")}
                            >
                                Subscription
                            </button>
                            <button
                                className={`sidebar-tab ${activeTab === "videos" ? "active" : ""}`}
                                disabled={videoCatalog.length === 0}
                                onClick={() => {
                                    setActiveTab("videos")
                                }}
                            >
                                Videos
                            </button>
                        </div>

                        {activeTab === "videos" && (
                            <div className="sidebar-submenu">
                                {videoCatalog.length > 0 &&
                                    videoCatalog.map((language) => (
                                        <div key={language.key} className="language-group">
                                            <button
                                                className={`language-header ${selectedLanguage === language.key ? "active" : ""}`}
                                                onClick={() => {
                                                    setSelectedLanguage(language.key);
                                                    const firstTier = language.tiers[0];
                                                    setSelectedTier(firstTier.id);
                                                    setSelectedTopic(firstTier.topics[0]?.id ?? "");
                                                }}
                                            >
                                                {language.label}
                                            </button>

                                            <div className="tier-list">
                                                {language.tiers.map((tier) => (
                                                    <button
                                                        key={`${language.key}-${tier.id}`}
                                                        className={`tier-tag ${selectedLanguage === language.key && selectedTier === tier.id ? "active" : ""}`}
                                                        onClick={() => {
                                                            setSelectedLanguage(language.key);
                                                            setSelectedTier(tier.id);
                                                            setSelectedTopic(tier.topics[0]?.id ?? "");
                                                        }}
                                                    >
                                                        {tier.label}
                                                    </button>
                                                ))}
                                            </div>
                                        </div>
                                    ))}
                            </div>
                        )}
                    </aside>

                    <main className="dashboard-content">
                        {activeTab === "subscription" ? (
                            <>
                                <SubscriptionModal
                                    title={"Current Subscription"}
                                    subscription={subscription}
                                    setShowCancelModal={setShowCancelModal}
                                />

                                {subscription?.nextSubscription && (
                                    <SubscriptionModal
                                        title={"Next Subscription"}
                                        subscription={subscription.nextSubscription}
                                        setShowCancelModal={setShowCancelNextModal}
                                    />
                                )}

                                <PlanCatalog
                                    plans={plans}
                                    subscription={subscription}
                                    setShowCancelModal={setShowCancelModal}
                                    user={user}
                                    setShowFreePlanModal={setShowFreePlanModal}
                                    setShowPaymentModal={setShowPaymentModal}
                                    setSelectedPlan={setSelectedPlan}
                                />
                            </>
                        ) : !subscription ? (
                            <section className="video-library-panel subscription-required-message">
                                <p>Subscribe first to access the video library.</p>
                            </section>
                        ) : (
                            <section className="video-library-panel">
                                <div className="video-library-header">
                                    <div>
                                        <p className="eyebrow">Learning path</p>
                                        <h2>
                                            {selectedLanguageData.label} · {selectedTierData.label}
                                        </h2>
                                    </div>
                                </div>

                                <div className="video-content-layout">
                                    <div className="topic-list-panel">
                                        <h3>Topics</h3>
                                        <div className="topic-list">
                                            {selectedTierData.topics.map((topic) => (
                                                <button
                                                    key={topic.id}
                                                    className={`topic-item ${selectedTopic === topic.id ? "active" : ""}`}
                                                    onClick={() => setSelectedTopic(topic.id)}
                                                >
                                                    <span className="topic-title">{topic.title}</span>
                                                    <span className="topic-duration">
                                                        {topic.duration}
                                                    </span>
                                                </button>
                                            ))}
                                        </div>
                                    </div>

                                    <div className="video-player-panel">
                                        <div className="video-player-card">
                                            <div className="video-meta">
                                                <span className="video-type">
                                                  {selectedTierData.label}
                                                </span>
                                                <span className="video-topic">
                                                  {selectedLanguageData.label}
                                                </span>
                                            </div>

                                            <video
                                                key={selectedTopicData.id}
                                                className="video-player"
                                                controls
                                                preload="metadata"
                                                src={selectedTopicData.src}
                                            >
                                                Your browser does not support the video tag.
                                            </video>

                                            <div className="video-info">
                                                <h3>{selectedTopicData.title}</h3>
                                                <p>{selectedTopicData.description}</p>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </section>
                        )}
                    </main>
                </div>
            </div>
        </>
    );
}

export default DashboardPage;
