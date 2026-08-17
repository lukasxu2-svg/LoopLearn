import React, {useEffect, useState} from "react";
import {planAPI} from "../../../../services/api";
import CancellationModal from "./Popups/CancellationModal";
import PaymentModal from "./Popups/PaymentModal";
import FreePlanModal from "./Popups/FreePlanModal";


interface PlanCatalogType {
    subscription,
    loading: boolean;
    setLoading: React.Dispatch<React.SetStateAction<boolean>>;
    showCancelModal: boolean;
    setShowCancelModal: React.Dispatch<React.SetStateAction<boolean>>;
    user
}


function PlanCatalog(
    {
        subscription,
        loading,
        setLoading,
        showCancelModal,
        setShowCancelModal,
        user
    }: PlanCatalogType) {

    const [plans, setPlans] = useState<any[]>([]);
    const [showFreePlanModal, setShowFreePlanModal] = useState(false);
    const [showPaymentModal, setShowPaymentModal] = useState(false);
    const [selectedPlan, setSelectedPlan] = useState<any | null>(null);
    const [selectedPaymentMethod, setSelectedPaymentMethod] = useState("paypal");

    useEffect(() => {
        loadPlans();
    }, [])


    const loadPlans = async () => {
        try {
            const plansResponse = await planAPI.getPlans();
            const sortedPlans = plansResponse.data.sort(
                (a: any, b: any) => a.rank - b.rank,
            );
            setPlans(sortedPlans);
        } catch (e) {
            //TODO
        }
    }

    return (
        <>
            <CancellationModal
                showCancelModal={showCancelModal}
                setShowCancelModal={setShowCancelModal}
                loading={loading}
                setLoading={setLoading}
            />

            <FreePlanModal
                subscription={subscription}
                showFreePlanModal={showFreePlanModal}
                setShowFreePlanModal={setShowFreePlanModal}
                modalLoading={loading}
                setModalLoading={setLoading}
            />

            <PaymentModal
                showPaymentModal={showPaymentModal}
                setShowPaymentModal={setShowPaymentModal}
                selectedPlan={selectedPlan}
                selectedPaymentMethod={selectedPaymentMethod}
                setSelectedPaymentMethod={setSelectedPaymentMethod}
                user={user}
            />

            {plans && plans.length > 0 && (
                <section className="plans-section">
                    <h2>Available Plans</h2>
                    <div className="plans-grid">
                        {plans.map((plan: any) => (
                            <div key={plan.id} className="plan-card">
                                <h3>{plan.planType}</h3>
                                <div className="plan-price">€{plan.monthlyPrice}/month</div>
                                <p className="plan-description">{plan.description}</p>

                                <button
                                    className="upgrade-button"
                                    disabled={
                                        (subscription?.planType === plan.planType &&
                                            subscription.cancelled)
                                    }
                                    onClick={() => {
                                        if (plan.planType === "FREE") {
                                            setShowFreePlanModal(true);
                                        } else if (subscription?.planType === plan.planType) {
                                            setShowCancelModal(true)
                                        } else {
                                            setSelectedPlan(plan)
                                            setShowPaymentModal(true)
                                        }
                                    }}
                                >
                                    {subscription?.planType === plan.planType
                                        ? subscription.cancelled
                                            ? "Cancelled"
                                            : "Cancel Subscription"
                                        : "Choose Plan"}
                                </button>
                            </div>
                        ))}
                    </div>
                </section>
            )}
        </>
    )
}

export default PlanCatalog;