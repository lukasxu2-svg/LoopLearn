import React from "react";
import {subscriptionAPI} from "../../../../../services/api";

interface CancellationModalProps {
    showCancelModal: boolean;
    setShowCancelModal: React.Dispatch<React.SetStateAction<boolean>>;
    loading: boolean;
    setLoading: React.Dispatch<React.SetStateAction<boolean>>;
};

function CancellationModal(
    {
        showCancelModal,
        setShowCancelModal,
        loading,
        setLoading
    }: CancellationModalProps
) {

    const handleCancelSubscription = async () => {
        //if (!subscription || subscription.cancelled) return;

        setLoading(true);
        try {
            await subscriptionAPI.cancelSubscription();
            //setSuccessMessage("Subscription cancelled successfully");
            setShowCancelModal(false);
        } catch (err: any) {
            //setError(err.response?.data?.message || "Failed to cancel subscription");
            console.error("Cancel error:", err);
        } finally {
            setLoading(false);
        }
    };

    return (
        <>
            {showCancelModal && (
                <div className="modal-overlay" onClick={() => setShowCancelModal(false)}>
                    <div className="modal" onClick={(e) => e.stopPropagation()}>
                        <h2>Cancel Subscription</h2>
                        <p>
                            Are you sure you want to cancel your subscription? You have
                            access to the features till the cancellation date
                        </p>
                        <div className="modal-actions">
                            <button
                                className="modal-button cancel"
                                onClick={() => setShowCancelModal(false)}
                                disabled={loading}
                            >
                                Keep Subscription
                            </button>
                            <button
                                className="modal-button confirm"
                                onClick={handleCancelSubscription}
                                disabled={loading}
                            >
                                {loading ? "Cancelling..." : "Yes, Cancel"}
                            </button>
                        </div>
                    </div>
                </div>
            )}
        </>
    );
}

export default CancellationModal;