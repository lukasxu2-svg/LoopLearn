import React, {useState, useEffect, use} from "react";
import {useNavigate} from "react-router-dom";
import {planAPI, subscriptionAPI} from "../../services/api.js";
import "./styles/Dashboard.css";
import {isTokenExpired} from "../../auth/authUtils.js";
import PaymentModal from "./components/PlanCatalog/Popups/PaymentModal";
import PlanCatalog from "./components/PlanCatalog/PlanCatalog";

interface User {
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
    const [subscription, setSubscription] = useState<any>(null);
    const [plans, setPlans] = useState<any[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    const [successMessage, setSuccessMessage] = useState("");

    const [showCancelModal, setShowCancelModal] = useState(false);
    const [modalLoading, setModalLoading] = useState(false);

    const [showPopup, setShowPopup] = useState(false);

    const [showFreePlanModal, setShowFreePlanModal] = useState(false);

    useEffect(() => {
        if (isTokenExpired()) {
            handleLogout();
        }
        loadDashboardData();
    }, []);

    const loadDashboardData = async () => {
        setLoading(true);
        setError("");
        try {
            const userData = localStorage.getItem("user");
            if (userData) {
                setUser(JSON.parse(userData));
            }

            const subResponse = await subscriptionAPI.getSubscription();
            setSubscription(subResponse.data);
        } catch (err) {
            setError("Failed to load subscription data");
            console.error("Error loading dashboard:", err);
        } finally {
            setLoading(false);
        }
    };

    const handleUpgradePlan = async (planId: any) => {
        try {
            const currentSubscription = await subscriptionAPI.getSubscription();
            const plan = await planAPI.getPlanById(planId);

            const rank = plan.data.rank;

            setSuccessMessage("Plan upgraded successfully");
            setTimeout(() => {
                loadDashboardData();
                setSuccessMessage("");
            }, 2000);
        } catch (err: any) {
            setError(err.response?.data?.message || "Failed to upgrade plan");
            console.error("Upgrade error:", err);
        }
    };

    const handleLogout = () => {
        localStorage.removeItem("token");
        localStorage.removeItem("user");
        setIsAuthenticated(false);
        navigate("/login");
    };

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

                {error && <div className="alert alert-error">{error}</div>}
                {successMessage && (
                    <div className="alert alert-success">{successMessage}</div>
                )}

                <div className="dashboard-content">
                    <section className="subscription-section">
                        <h2>Your Subscription</h2>

                        {subscription ? (
                            <div className="subscription-card">
                                <div className="subscription-info">
                                    <div className="info-item">
                                        <label>Plan Name</label>
                                        <p className="plan-name">{subscription.planType}</p>
                                    </div>

                                    <div className="info-item">
                                        <label>Status</label>
                                        <p
                                            className={`status ${subscription.status?.toLowerCase()}`}
                                        >
                                            {subscription.status}
                                        </p>
                                    </div>

                                    <div className="info-item">
                                        <label>Active from</label>
                                        <p>{subscription.periodStart.slice(0, 10)}</p>
                                    </div>

                                    <div className="info-item">
                                        <label>Active till</label>
                                        <p>{subscription.periodEnd.slice(0, 10)}</p>
                                    </div>

                                    <div className="info-item">
                                        <label>Price</label>
                                        <p className="price">€{subscription.cost} / month</p>
                                    </div>
                                </div>

                                <div className="subscription-actions">
                                    <button
                                        className="cancel-button"
                                        onClick={() => setShowCancelModal(true)}
                                        disabled={subscription.cancelled}
                                    >
                                        {subscription.cancelled
                                            ? "Cancelled"
                                            : "Cancel Subscription"}
                                    </button>
                                </div>
                            </div>
                        ) : (
                            <div className="no-subscription">
                                <p>You don't have an active subscription</p>
                                <p className="note">Choose a plan below to get started</p>
                            </div>
                        )}
                    </section>

                    <PlanCatalog
                        subscription={subscription}
                        loading={modalLoading}
                        setLoading={setModalLoading}
                        showCancelModal={showCancelModal}
                        setShowCancelModal={setShowCancelModal}
                    />
                </div>
            </div>
        </>
    );
}

export default DashboardPage;
