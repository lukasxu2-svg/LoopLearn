import React from "react";
import {subscriptionAPI} from "../../../../services/api";
import {useNotifications} from "../../../../context/NotificationContext";

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
    const {setSuccessMessage, setErrorMessage} = useNotifications();

    const handleCancelSubscription = async () => {
        setLoading(true);
        try {
            await subscriptionAPI.cancelSubscription();

            setSuccessMessage("Subscription cancelled successfully");

            setShowCancelModal(false);
            setTimeout(() => {
                setLoading(false);
                setShowCancelModal(false);
            }, 3000);
        } catch (err: any) {
            setErrorMessage("Failed to cancel subscription");
        } finally {
            window.location.reload();
        }
    };

    return (
        <>
            {showCancelModal && (
                <div className="modal-overlay" onClick={() => setShowCancelModal(false)}>
                    <div className="modal" onClick={(e) => e.stopPropagation()}>
                        <h2>Cancel Subscription</h2>
                        <p>
                            Are you sure you want to cancel your subscription? Incoming subscriptions will also be
                            cancelled. You have
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