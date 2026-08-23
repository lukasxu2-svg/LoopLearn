import React from "react";
import {subscriptionAPI} from "../../../../services/api";
import {useNotifications} from "../../../../context/NotificationContext";

interface CancelNextModalProps {
    showCancelNextModal: boolean;
    setShowCancelNextModal: React.Dispatch<React.SetStateAction<boolean>>;
    loading: boolean;
    setLoading: React.Dispatch<React.SetStateAction<boolean>>;
}

function CancelNextModal({
                             showCancelNextModal,
                             setShowCancelNextModal,
                             loading,
                             setLoading,
                         }: CancelNextModalProps) {
    const {setSuccessMessage, setErrorMessage} = useNotifications();

    const handleCancelNextSubscription = async () => {
        setLoading(true);
        try {
            const response = await subscriptionAPI.cancelNextSubscription();

            const approvalUrl = response.data.links.find(
                (link: any) => link.rel === "approve",
            ).href;

            window.open(approvalUrl, "_blank");

            setSuccessMessage("Next subscription cancelled successfully");
            setTimeout(() => {
                setLoading(false);
                setShowCancelNextModal(false);
            }, 3000);
        } catch (err: any) {
            setErrorMessage("Failed to cancel next subscription");
        }
    };

    return (
        <>
            {showCancelNextModal && (
                <div
                    className="modal-overlay"
                    onClick={() => setShowCancelNextModal(false)}
                >
                    <div className="modal" onClick={(e) => e.stopPropagation()}>
                        <h2>Cancel Subscription</h2>
                        <p>Are you sure you want to cancel your incoming subscription?</p>
                        <div className="modal-actions">
                            <button
                                className="modal-button cancel"
                                onClick={() => setShowCancelNextModal(false)}
                                disabled={loading}
                            >
                                Keep Subscription
                            </button>
                            <button
                                className="modal-button confirm"
                                onClick={handleCancelNextSubscription}
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

export default CancelNextModal;
