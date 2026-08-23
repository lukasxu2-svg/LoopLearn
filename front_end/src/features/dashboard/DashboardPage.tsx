import React, {useState, useEffect} from "react";
import {useNavigate} from "react-router-dom";
import {planAPI, subscriptionAPI} from "../../services/api.js";
import "./styles/Dashboard.css";
import {isTokenExpired} from "../../auth/authUtils.js";
import PlanCatalog from "./components/PlanCatalog/PlanCatalog";
import SubscriptionModal from "./components/SubscriptionModal/SubscriptionModal";
import CancellationModal from "./components/PopupModals/CancellationModal";
import CancelNextModal from "./components/PopupModals/CancelNextModal";
import FreePlanModal from "./components/PopupModals/FreePlanModal";
import PaymentModal from "./components/PopupModals/PaymentModal";
import {useNotifications} from "../../context/NotificationContext";

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

    const [plans, setPlans] = useState<any[]>([]);
    const [subscription, setSubscription] = useState<any>(null);
    const [loading, setLoading] = useState(true);
    const {errorMessage, successMessage, setSuccessMessage, setErrorMessage, clearNotifications} =
        useNotifications();

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

            setSuccessMessage("Successfully loaded dashboard data")
        } catch (err) {
            setErrorMessage("Failed to load dashboard data");
        } finally {
            setLoading(false);

        }
    };

    const loadSubscriptionData = async () => {
        try {
            const subResponse = await subscriptionAPI.getSubscription();
            setSubscription(subResponse.data);
        } catch (err) {
            setErrorMessage("Failed to load subscription data");
        }
    }

    const loadPlans = async () => {
        try {
            const plansResponse = await planAPI.getPlans();
            const sortedPlans = plansResponse.data.sort(
                (a: any, b: any) => a.rank - b.rank,
            );
            setPlans(sortedPlans);
        } catch (err) {
            setErrorMessage("Failed to load plan data");
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

                <FreePlanModal
                    subscription={subscription}
                    showFreePlanModal={showFreePlanModal}
                    setShowFreePlanModal={setShowFreePlanModal}
                    modalLoading={modalLoading}
                    setModalLoading={setModalLoading}
                    user={user}
                    selectedPlan={selectedPlan}
                />

                <PaymentModal
                    showPaymentModal={showPaymentModal}
                    setShowPaymentModal={setShowPaymentModal}
                    selectedPlan={selectedPlan}
                    selectedPaymentMethod={selectedPaymentMethod}
                    setSelectedPaymentMethod={setSelectedPaymentMethod}
                    user={user}
                    loading={modalLoading}
                    setLoading={setModalLoading}
                />

                <div className="dashboard-content">
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
                </div>
            </div>
        </>
    );
}

export default DashboardPage;
