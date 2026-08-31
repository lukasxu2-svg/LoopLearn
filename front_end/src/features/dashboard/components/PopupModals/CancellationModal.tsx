import React from "react";
import {subscriptionAPI} from "../../../../services/api";
import {useDashboardContext} from "../../context/DashboardContext";

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
    const {loadSubscriptionData, setSuccessMessage, setErrorMessage} = useDashboardContext();

    const handleCancelSubscription = async () => {
        setLoading(true);
        try {
            await subscriptionAPI.cancelSubscription();

            await loadSubscriptionData();

            setSuccessMessage("Cancellation processed. Might take some time");
        } catch (err: any) {
            setErrorMessage("Failed to cancel subscription");
        } finally {
            setShowCancelModal(false);
            setLoading(false);
        }
    };

    return (
        <>
            {showCancelModal && (
                <div className="modal-overlay" onClick={() => setShowCancelModal(false)}>
                    <div className="modal" onClick={(e) => e.stopPropagation()}>
                        <h2>Cancel Current Subscription</h2>
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