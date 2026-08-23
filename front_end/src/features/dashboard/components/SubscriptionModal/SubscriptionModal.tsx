import React from "react";

interface SubscriptionModalProps {
    title: string;
    subscription: any;
    setShowCancelModal: React.Dispatch<React.SetStateAction<boolean>>;
}

function SubscriptionModal({
                               title,
                               subscription,
                               setShowCancelModal,
                           }: SubscriptionModalProps) {

    return (
        <section className="subscription-section">
            <h2>{title}</h2>

            {subscription ? (
                <div className="subscription-card">
                    <div className="subscription-info">
                        <div className="info-item">
                            <label>Plan Name</label>
                            <p className="plan-name">{subscription.planType}</p>
                        </div>

                        <div className="info-item">
                            <label>Status</label>
                            <p className={`status ${subscription.status?.toLowerCase()}`}>
                                {subscription.status}
                            </p>
                        </div>

                        <div className="info-item">
                            <label>Active from</label>
                            <p>
                                {subscription.planType === "FREE"
                                    ? "--"
                                    : subscription.periodStart?.slice(0, 10)}
                            </p>
                        </div>

                        <div className="info-item">
                            <label>Active till</label>
                            <p>
                                {subscription.planType === "FREE"
                                    ? "--"
                                    : subscription.periodEnd?.slice(0, 10)}
                            </p>
                        </div>

                        <div className="info-item">
                            <label>Price</label>
                            <p className="price">€{subscription?.cost} / month</p>
                        </div>

                    </div>

                    <div className="subscription-actions">
                        <button
                            className="cancel-button"
                            onClick={() => setShowCancelModal(true)}
                            disabled={subscription.cancelled}
                        >
                            {subscription.cancelled ? "Cancelled" : "Cancel Subscription"}
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
    );
}

export default SubscriptionModal;
