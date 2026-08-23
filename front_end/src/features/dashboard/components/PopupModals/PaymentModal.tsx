import "../../styles/Popup.css";
import {subscriptionAPI} from "../../../../services/api.js";
import React from "react";
import {useNotifications} from "../../../../context/NotificationContext";
import {User} from "../../DashboardPage";


interface PaymentModalProps {
    showPaymentModal: boolean;
    setShowPaymentModal: (v: boolean) => void;
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
                          selectedPlan,
                          selectedPaymentMethod,
                          setSelectedPaymentMethod,
                          user,
                          loading,
                          setLoading
                      }: PaymentModalProps) {
    const {setSuccessMessage, setErrorMessage} = useNotifications();


    const confirmPayment = async () => {
        setLoading(true);
        try {
            const body = {
                simplePlanId: selectedPlan.id,
                subscriber: {
                    email_address: user?.email,
                    name: {
                        given_name: user?.firstName,
                        surname: user?.lastName,
                    },
                },
            };
            const response = await subscriptionAPI.createSubscription(body);

            const approvalUrl = response.data.links.find(
                (link: any) => link.rel === "approve",
            ).href;

            window.open(approvalUrl, "_blank");

            setTimeout(() => {
                setLoading(false);
                setShowPaymentModal(false);
            }, 3000);
        } catch (e: any) {
            setErrorMessage("Payment failed");
        }
    };

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
                                        confirmPayment();
                                    }}
                                >
                                    {loading ? "Processing ..." : "Confirm payment"}
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            )}
        </>
    );
}

export default PaymentModal;
