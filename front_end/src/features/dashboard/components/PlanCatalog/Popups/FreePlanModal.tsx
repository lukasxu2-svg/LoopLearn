import React from "react";
import {subscriptionAPI} from "../../../../../services/api";

interface FreePlanModalProps {
    subscription
    showFreePlanModal: boolean,
    setShowFreePlanModal: React.Dispatch<React.SetStateAction<boolean>>,
    modalLoading: boolean,
    setModalLoading: React.Dispatch<React.SetStateAction<boolean>>,
}

function FreePlanModal(
    {
        subscription,
        showFreePlanModal,
        setShowFreePlanModal,
        modalLoading,
        setModalLoading
    }: FreePlanModalProps) {

    const handleFreePlan = async () => {
        if (subscription) {
            return;
        }
        setModalLoading(true);
        try {
            const response = subscriptionAPI.createFreeSubscription();

            setShowFreePlanModal(false);
        } catch (e) {

        } finally {
            setModalLoading(false);
        }
    }

    return (
        <>
            {showFreePlanModal && (
                <div className="modal-overlay" onClick={() => setShowFreePlanModal(false)}>
                    <div className="modal" onClick={(e) => e.stopPropagation()}>
                        <h2>Choose Free Plan</h2>
                        <p>
                            {!subscription ? "Acknowledge to select the free plan? By agreeing you get access to the free plan" :
                                "Ongoing subscription exists. It will automatically revert to the free plan after the duration of the paid subscription"}
                        </p>
                        {!subscription && (
                            <div className="modal-actions">
                                <button
                                    className="modal-button cancel"
                                    onClick={() => setShowFreePlanModal(false)}
                                    disabled={modalLoading}
                                >
                                    Cancel
                                </button>
                                <button
                                    className="modal-button confirm"
                                    onClick={() => handleFreePlan()}
                                    disabled={modalLoading}
                                >
                                    {modalLoading ? "Processing..." : "Yes, Choose Free Plan"}
                                </button>
                            </div>
                        )}
                    </div>
                </div>
            )}
        </>
    )
}

export default FreePlanModal;