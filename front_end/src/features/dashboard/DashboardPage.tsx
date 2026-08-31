import React, {useState, useEffect} from "react";
import {useNavigate} from "react-router-dom";
import {planAPI, subscriptionAPI} from "../../services/api.js";
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

const videoCatalog = [
    {
        key: "java",
        label: "Java",
        tiers: [
            {
                id: "basic",
                label: "Basic",
                topics: [
                    {
                        id: "inheritance",
                        title: "Inheritance",
                        duration: "04:20",
                        src: "https://interactive-examples.mdn.mozilla.net/media/cc0-videos/flower.mp4",
                        description: "Learn how classes inherit behavior and data in Java.",
                    },
                    {
                        id: "classes",
                        title: "Classes",
                        duration: "03:45",
                        src: "https://interactive-examples.mdn.mozilla.net/media/cc0-videos/flower.mp4",
                        description: "Understand class structure, constructors, fields, and methods.",
                    },
                    {
                        id: "loops",
                        title: "Loops",
                        duration: "05:10",
                        src: "https://interactive-examples.mdn.mozilla.net/media/cc0-videos/flower.mp4",
                        description: "Practice iteration patterns with for and while loops.",
                    },
                ],
            },
            {
                id: "advanced",
                label: "Advanced",
                topics: [
                    {
                        id: "streams",
                        title: "Streams",
                        duration: "06:12",
                        src: "https://interactive-examples.mdn.mozilla.net/media/cc0-videos/flower.mp4",
                        description: "Use functional-style data processing to work with collections.",
                    },
                    {
                        id: "multithreading",
                        title: "Multi-threading",
                        duration: "05:40",
                        src: "https://interactive-examples.mdn.mozilla.net/media/cc0-videos/flower.mp4",
                        description: "Coordinate parallel tasks and manage shared resources safely.",
                    },
                    {
                        id: "designpatterns",
                        title: "Design Patterns",
                        duration: "07:05",
                        src: "https://interactive-examples.mdn.mozilla.net/media/cc0-videos/flower.mp4",
                        description: "Apply reusable patterns to build scalable Java systems.",
                    },
                ],
            },
            {
                id: "premium",
                label: "Premium",
                topics: [
                    {
                        id: "microservices",
                        title: "Microservices",
                        duration: "08:15",
                        src: "https://interactive-examples.mdn.mozilla.net/media/cc0-videos/flower.mp4",
                        description: "Design service boundaries and resilient distributed systems.",
                    },
                    {
                        id: "performance",
                        title: "Performance Tuning",
                        duration: "09:02",
                        src: "https://interactive-examples.mdn.mozilla.net/media/cc0-videos/flower.mp4",
                        description: "Profile bottlenecks and optimize Java runtime behavior.",
                    },
                    {
                        id: "cloudintegration",
                        title: "Cloud Integration",
                        duration: "07:40",
                        src: "https://interactive-examples.mdn.mozilla.net/media/cc0-videos/flower.mp4",
                        description: "Connect Java applications with cloud APIs and infrastructure.",
                    },
                ],
            },
        ],
    },
    {
        key: "javascript",
        label: "JavaScript",
        tiers: [
            {
                id: "basic",
                label: "Basic",
                topics: [
                    {
                        id: "variables",
                        title: "Variables",
                        duration: "03:36",
                        src: "https://interactive-examples.mdn.mozilla.net/media/cc0-videos/flower.mp4",
                        description: "Learn about var, let, const, and scope in JavaScript.",
                    },
                    {
                        id: "functions",
                        title: "Functions",
                        duration: "04:10",
                        src: "https://interactive-examples.mdn.mozilla.net/media/cc0-videos/flower.mp4",
                        description: "Build reusable logic with declarations and expressions.",
                    },
                    {
                        id: "arrays",
                        title: "Arrays",
                        duration: "03:58",
                        src: "https://interactive-examples.mdn.mozilla.net/media/cc0-videos/flower.mp4",
                        description: "Work with list data and array methods effectively.",
                    },
                ],
            },
            {
                id: "advanced",
                label: "Advanced",
                topics: [
                    {
                        id: "asyncawait",
                        title: "Async/Await",
                        duration: "06:08",
                        src: "https://interactive-examples.mdn.mozilla.net/media/cc0-videos/flower.mp4",
                        description: "Handle asynchronous workflows with cleaner syntax.",
                    },
                    {
                        id: "closures",
                        title: "Closures",
                        duration: "05:32",
                        src: "https://interactive-examples.mdn.mozilla.net/media/cc0-videos/flower.mp4",
                        description: "Master lexical scope and state retention in functions.",
                    },
                    {
                        id: "prototypes",
                        title: "Prototypes",
                        duration: "06:25",
                        src: "https://interactive-examples.mdn.mozilla.net/media/cc0-videos/flower.mp4",
                        description: "Understand inheritance chains and object behavior.",
                    },
                ],
            },
            {
                id: "premium",
                label: "Premium",
                topics: [
                    {
                        id: "dommanipulation",
                        title: "DOM Manipulation",
                        duration: "07:14",
                        src: "https://interactive-examples.mdn.mozilla.net/media/cc0-videos/flower.mp4",
                        description: "Interact with browser elements and update interfaces dynamically.",
                    },
                    {
                        id: "stateManagement",
                        title: "State Management",
                        duration: "08:00",
                        src: "https://interactive-examples.mdn.mozilla.net/media/cc0-videos/flower.mp4",
                        description: "Build predictable data flows for larger frontend applications.",
                    },
                    {
                        id: "performanceoptimization",
                        title: "Performance Optimization",
                        duration: "07:58",
                        src: "https://interactive-examples.mdn.mozilla.net/media/cc0-videos/flower.mp4",
                        description: "Reduce DOM work, improve rendering speed, and optimize bundles.",
                    },
                ],
            },
        ],
    },
];

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

    useEffect(() => {
        if (isTokenExpired()) {
            handleLogout();
        }
        loadDashboardData();
    }, []);

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

    const handleLogout = () => {
        localStorage.removeItem("token");
        localStorage.removeItem("user");
        setIsAuthenticated(false);
        navigate("/login");
    };

    const selectedLanguageData =
        videoCatalog.find((language) => language.key === selectedLanguage) ??
        videoCatalog[0];
    const selectedTierData =
        selectedLanguageData.tiers.find((tier) => tier.id === selectedTier) ??
        selectedLanguageData.tiers[0];
    const selectedTopicData =
        selectedTierData.topics.find((topic) => topic.id === selectedTopic) ??
        selectedTierData.topics[0];

    useEffect(() => {
        const currentTier =
            selectedLanguageData.tiers.find((tier) => tier.id === selectedTier) ??
            selectedLanguageData.tiers[0];

        if (!currentTier.topics.some((topic) => topic.id === selectedTopic)) {
            setSelectedTopic(currentTier.topics[0]?.id ?? "");
        }
    }, [selectedLanguage, selectedTier, selectedTopic]);

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
                                onClick={() => setActiveTab("videos")}
                            >
                                Videos
                            </button>
                        </div>

                        {activeTab === "videos" && (
                            <div className="sidebar-submenu">
                                {videoCatalog.map((language) => (
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
                                                    <span className="topic-duration">{topic.duration}</span>
                                                </button>
                                            ))}
                                        </div>
                                    </div>

                                    <div className="video-player-panel">
                                        <div className="video-player-card">
                                            <div className="video-meta">
                                                <span className="video-type">{selectedTierData.label}</span>
                                                <span className="video-topic">{selectedLanguageData.label}</span>
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
