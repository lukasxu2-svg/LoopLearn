import React, {useEffect, useState} from "react";
import {planAPI} from "../../../../services/api";
import {useDashboardContext} from "../../context/DashboardContext";

interface PlanCatalogType {
    plans;
    subscription;
    setShowCancelModal: React.Dispatch<React.SetStateAction<boolean>>;
    setShowFreePlanModal: React.Dispatch<React.SetStateAction<boolean>>;
    setShowPaymentModal: React.Dispatch<React.SetStateAction<boolean>>;
    setSelectedPlan: React.Dispatch<React.SetStateAction<any | null>>;
    user;
}

function PlanCatalog({
                         plans,
                         subscription,
                         setShowCancelModal,
                         setShowFreePlanModal,
                         setShowPaymentModal,
                         setSelectedPlan,
                     }: PlanCatalogType) {

    const showText = (plan): String => {
        if (subscription === null) {
            return "Choose plan"
        }

        if (subscription.rank > plan.rank) {
            return "Downgrade plan"
        }

        if (subscription.rank < plan.rank) {
            return "Upgrade Plan"
        }
        return "Choose plan";
    }

    return (
        <>
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
                                        subscription?.planType === plan.planType ||
                                        subscription?.cancelled ||
                                        subscription?.nextSubscription?.planType === plan.planType
                                    }
                                    onClick={() => {
                                        setSelectedPlan(plan);
                                        if (plan.planType === "FREE") {
                                            setShowFreePlanModal(true);
                                        } else if (subscription?.planType === plan.planType) {
                                            setShowCancelModal(true);
                                        } else {
                                            setShowPaymentModal(true);
                                        }
                                    }}
                                >
                                    {showText(plan)}
                                </button>
                            </div>
                        ))}
                    </div>
                </section>
            )}
        </>
    );
}

export default PlanCatalog;
