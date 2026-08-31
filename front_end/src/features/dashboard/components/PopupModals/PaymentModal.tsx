import "../../styles/Popup.css";
import {subscriptionAPI} from "../../../../services/api.js";
import React from "react";
import {useDashboardContext} from "../../context/DashboardContext";
import {User} from "../../DashboardPage";


interface PaymentModalProps {
    showPaymentModal: boolean;
    setShowPaymentModal: (v: boolean) => void;
    showFreePlanModal: boolean;
    setShowFreePlanModal: (v: boolean) => void;
    selectedPlan: any;
    selectedPaymentMethod: string;
    setSelectedPaymentMethod: (m: string) => void;
    user: User;
    loading: boolean;
    setLoading: React.Dispatch<React.SetStateAction<boolean>>;
}

function PaymentModal({
                          showPaymentModal,
                          setShowPaymentModal,
                          showFreePlanModal,
                          setShowFreePlanModal,
                          selectedPlan,
                          selectedPaymentMethod,
                          setSelectedPaymentMethod,
                          user,
                          loading,
                          setLoading
                      }: PaymentModalProps) {
    const {subscription, loadSubscriptionData, setSuccessMessage, setErrorMessage} = useDashboardContext();


    const handlePayment = async () => {
        setLoading(true);
        try {
            const body = {
                simplePlanId: selectedPlan.id,
                subscriber: {
                    email_address: user.email,
                    name: {
                        given_name: user.firstName,
                        surname: user.lastName,
                    },
                },
            };
            let response;

            if (!isNaN(Number(subscription.rank)) && subscription?.rank !== selectedPlan.rank) {
                response = await reviseSubscription(body);
            } else {
                response = await createSubscription(body);
            }

            const approvalUrl = response.data?.links?.find(
                (link: any) => link.rel === "approve",
            ).href;

            if (approvalUrl) {
                window.open(approvalUrl, "_blank");
            }
            await loadSubscriptionData();
            setSuccessMessage("Subscription creation processed. May take a minute");
        } catch (err: any) {
            setErrorMessage(err.message || "Payment failed");
        } finally {
            setLoading(false);
            setShowPaymentModal(false);
            setShowFreePlanModal(false);
        }
    };

    const reviseSubscription = async (body) => {
        try {
            return await subscriptionAPI.reviseSubscription(body);
        } catch (e: any) {
            throw new Error("Upgrading/Downgrading subscription failed");
        }
    }

    const createSubscription = async (body) => {
        try {
            return await subscriptionAPI.createSubscription(body);
        } catch (e: any) {
            throw new Error("Creating subscription failed");
        }
    }

    return (
        <>
            {showPaymentModal && (
                <div className="overlay" onClick={() => setShowPaymentModal(false)}>
                    <div className="popupContainer" onClick={(e) => e.stopPropagation()}>
                        <div className="popupHeader">
                            <div>
                                <p className="popupEyebrow">Secure checkout</p>
                                <h2>Choose your payment method</h2>
                            </div>
                            <button
                                className="close-popup-button"
                                onClick={() => setShowPaymentModal(false)}
                            >
                                ✕
                            </button>
                        </div>

                        <div className="popupLayout">
                            <div className="popupPaymentMethod">
                                <h3>Payment options</h3>
                                <div className="payment-options">
                                    <label
                                        className={`payment-option ${selectedPaymentMethod === "paypal" ? "active" : ""}`}
                                    >
                                        <input
                                            type="radio"
                                            name="paymentMethod"
                                            value="paypal"
                                            checked={selectedPaymentMethod === "paypal"}
                                            onChange={() => setSelectedPaymentMethod("paypal")}
                                        />
                                        <span>
                                          <strong>PayPal</strong>
                                          <small>Pay with your PayPal account</small>
                                        </span>
                                    </label>
                                </div>
                                <p className="popupNote">
                                    Upgrading/Downgrading takes place at the next billing cycle.
                                    Your subscription renews monthly unless you cancel before the
                                    next billing date.
                                </p>
                            </div>

                            <div className="popupBill">
                                <h3 className="popupBillHeader">Order summary</h3>
                                <div className="popupBillRows">
                                    <div className="popupBillRow">
                                        <span>Plan</span>
                                        <strong>{selectedPlan?.planType || "Selected plan"}</strong>
                                    </div>
                                    <div className="popupBillRow">
                                        <span>Monthly price</span>
                                        <strong>
                                            {Number(selectedPlan?.monthlyPrice || 0).toFixed(2)}€
                                        </strong>
                                    </div>
                                    <div className="popupBillRow total">
                                        <span>Total to pay</span>
                                        <strong>
                                            {Number(selectedPlan?.monthlyPrice || 0).toFixed(2)}€
                                        </strong>
                                    </div>
                                </div>
                                <button
                                    className="popupConfirmButton"
                                    disabled={loading}
                                    onClick={() => {
                                        handlePayment();
                                    }}
                                >
                                    {loading ? "Processing ..." : "Confirm payment"}
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            )}

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
                                    disabled={loading}
                                >
                                    Cancel
                                </button>
                                <button
                                    className="modal-button confirm"
                                    onClick={() => handlePayment()}
                                    disabled={loading}
                                >
                                    {loading ? "Processing..." : "Yes, Choose Free Plan"}
                                </button>
                            </div>
                        )}
                    </div>
                </div>
            )}
        </>
    );
}

export default PaymentModal;
