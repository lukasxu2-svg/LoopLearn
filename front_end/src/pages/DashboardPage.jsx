import {useState, useEffect} from 'react'
import {useNavigate} from 'react-router-dom'
import {planAPI, subscriptionAPI} from '../services/api.js'
import '../styles/Dashboard.css'
import '../styles/Popup.css'
import {isTokenExpired} from "../auth/authUtils.js";

function DashboardPage({setIsAuthenticated}) {
    const navigate = useNavigate()
    const [user, setUser] = useState(null)
    const [subscription, setSubscription] = useState(null)
    const [plans, setPlans] = useState([])
    const [loading, setLoading] = useState(true)
    const [error, setError] = useState('')
    const [showCancelModal, setShowCancelModal] = useState(false)
    const [cancelLoading, setCancelLoading] = useState(false)
    const [successMessage, setSuccessMessage] = useState('')
    const [showPopup, setShowPopup] = useState(false)
    const [selectedPlan, setSelectedPlan] = useState(null)
    const [selectedPaymentMethod, setSelectedPaymentMethod] = useState('card')

    useEffect(() => {
        if (isTokenExpired()) {
            handleLogout();
        }
        loadDashboardData()
    }, [])

    const loadDashboardData = async () => {
        setLoading(true)
        setError('')
        try {
            const userData = localStorage.getItem('user')
            if (userData) {
                setUser(JSON.parse(userData))
            }

            // Load subscription data
            const subResponse = await subscriptionAPI.getSubscription()
            setSubscription(subResponse.data)

            // Load available plans
            try {
                const plansResponse = await planAPI.getPlans()
                setPlans(plansResponse.data)
            } catch (err) {
                console.log('Could not load plans')
            }
        } catch (err) {
            setError('Failed to load subscription data')
            console.error('Error loading dashboard:', err)
        } finally {
            setLoading(false)
        }
    }

    const handleCancelSubscription = async () => {
        if (!subscription?.id) return

        setCancelLoading(true)
        try {
            await subscriptionAPI.cancelSubscription(subscription.id)
            setSuccessMessage('Subscription cancelled successfully')
            setShowCancelModal(false)
            // Reload data
            setTimeout(() => {
                loadDashboardData()
                setSuccessMessage('')
            }, 2000)
        } catch (err) {
            setError(err.response?.data?.message || 'Failed to cancel subscription')
            console.error('Cancel error:', err)
        } finally {
            setCancelLoading(false)
        }
    }

    const handleUpgradePlan = async (planId) => {
        try {
            const currentSubscription = await subscriptionAPI.getSubscription()
            const plan = await planAPI.getPlanById(planId)

            if (!currentSubscription.data == null) {

            }
            const rank = plan.data.rank
            if (currentSubscription.data.plan.rank > plan.data.rank) {

            } else if (currentSubscription.data.plan.rank < plan.data.rank) {

            } else {

            }
            await subscriptionAPI.changeSubscription(planId)
            setSuccessMessage('Plan upgraded successfully')
            setTimeout(() => {
                loadDashboardData()
                setSuccessMessage('')
            }, 2000)
        } catch (err) {
            setError(err.response?.data?.message || 'Failed to upgrade plan')
            console.error('Upgrade error:', err)
        }
    }

    const handleLogout = () => {
        localStorage.removeItem('token')
        localStorage.removeItem('user')
        setIsAuthenticated(false)
        navigate('/login')
    }

    if (loading) {
        return <div className="dashboard-loading">Loading your dashboard...</div>
    }

    return (
        <>
            {showPopup && (
                <div className="overlay" onClick={() => setShowPopup(false)}>
                    <div className="popupContainer" onClick={(e) => e.stopPropagation()}>
                        <div className="popupHeader">
                            <div>
                                <p className="popupEyebrow">Secure checkout</p>
                                <h2>Choose your payment method</h2>
                            </div>
                            <button className="close-popup-button" onClick={() => setShowPopup(false)}>
                                ✕
                            </button>
                        </div>

                        <div className="popupLayout">
                            <div className="popupPaymentMethod">
                                <h3>Payment options</h3>
                                <div className="payment-options">
                                    <label className={`payment-option ${selectedPaymentMethod === 'card' ? 'active' : ''}`}>
                                        <input
                                            type="radio"
                                            name="paymentMethod"
                                            value="card"
                                            checked={selectedPaymentMethod === 'card'}
                                            onChange={() => setSelectedPaymentMethod('card')}
                                        />
                                        <span>
                                            <strong>Credit / Debit Card</strong>
                                            <small>Fast and secure checkout</small>
                                        </span>
                                    </label>

                                    <label className={`payment-option ${selectedPaymentMethod === 'paypal' ? 'active' : ''}`}>
                                        <input
                                            type="radio"
                                            name="paymentMethod"
                                            value="paypal"
                                            checked={selectedPaymentMethod === 'paypal'}
                                            onChange={() => setSelectedPaymentMethod('paypal')}
                                        />
                                        <span>
                                            <strong>PayPal</strong>
                                            <small>Pay with your PayPal account</small>
                                        </span>
                                    </label>

                                    <label className={`payment-option ${selectedPaymentMethod === 'bank' ? 'active' : ''}`}>
                                        <input
                                            type="radio"
                                            name="paymentMethod"
                                            value="bank"
                                            checked={selectedPaymentMethod === 'bank'}
                                            onChange={() => setSelectedPaymentMethod('bank')}
                                        />
                                        <span>
                                            <strong>Bank Transfer</strong>
                                            <small>Ideal for manual invoices</small>
                                        </span>
                                    </label>
                                </div>
                                <p className="popupNote">
                                    Your subscription renews monthly unless you cancel before the next billing date.
                                </p>
                            </div>

                            <div className="popupBill">
                                <h3 className="popupBillHeader">Order summary</h3>
                                <div className="popupBillRows">
                                    <div className="popupBillRow">
                                        <span>Plan</span>
                                        <strong>{selectedPlan?.planType || 'Selected plan'}</strong>
                                    </div>
                                    <div className="popupBillRow">
                                        <span>Monthly price</span>
                                        <strong>€{Number(selectedPlan?.monthlyPrice || 0).toFixed(2)}</strong>
                                    </div>
                                    <div className="popupBillRow">
                                        <span>Tax</span>
                                        <strong>€0.00</strong>
                                    </div>
                                    <div className="popupBillRow total">
                                        <span>Total to pay</span>
                                        <strong>€{Number(selectedPlan?.monthlyPrice || 0).toFixed(2)}</strong>
                                    </div>
                                </div>
                                <button className="popupConfirmButton" onClick={() => setShowPopup(false)}>
                                    Confirm payment
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            )}
            <div className="dashboard-container">
                {/* Header */}
                <header className="dashboard-header">
                    <div className="header-content">
                        <div>
                            <h1>Dashboard</h1>
                            <p>Welcome, {user?.firstname}!</p>
                        </div>
                        <button className="logout-button" onClick={handleLogout}>
                            Logout
                        </button>
                    </div>
                </header>

                {/* Messages */}
                {error && <div className="alert alert-error">{error}</div>}
                {successMessage && <div className="alert alert-success">{successMessage}</div>}

                <div className="dashboard-content">
                    {/* Subscription Card */}
                    <section className="subscription-section">
                        <h2>Your Subscription</h2>

                        {subscription ? (
                            <div className="subscription-card">
                                <div className="subscription-info">
                                    <div className="info-item">
                                        <label>Plan Name</label>
                                        <p className="plan-name">{subscription.planName || 'Free Plan'}</p>
                                    </div>

                                    <div className="info-item">
                                        <label>Status</label>
                                        <p className={`status ${subscription.status?.toLowerCase()}`}>
                                            {subscription.status || 'Active'}
                                        </p>
                                    </div>

                                    <div className="info-item">
                                        <label>Start Date</label>
                                        <p>{subscription.startDate ? new Date(subscription.startDate).toLocaleDateString() : 'N/A'}</p>
                                    </div>

                                    <div className="info-item">
                                        <label>Renewal Date</label>
                                        <p>{subscription.renewalDate ? new Date(subscription.renewalDate).toLocaleDateString() : 'N/A'}</p>
                                    </div>

                                    <div className="info-item">
                                        <label>Price</label>
                                        <p className="price">${subscription.price || '0.00'} / month</p>
                                    </div>
                                </div>

                                <div className="subscription-actions">
                                    <button
                                        className="cancel-button"
                                        onClick={() => setShowCancelModal(true)}
                                        disabled={subscription.status === 'CANCELLED'}
                                    >
                                        Cancel Subscription
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

                    {/* Available Plans */}
                    {plans && plans.length > 0 && (
                        <section className="plans-section">
                            <h2>Available Plans</h2>
                            <div className="plans-grid">
                                {plans.map((plan) => (
                                    <div key={plan.id} className="plan-card">
                                        <h3>{plan.planType}</h3>
                                        <div className="plan-price">€{plan.monthlyPrice}/month</div>
                                        <p className="plan-description">{plan.description}</p>

                                        <ul className="plan-features">
                                            {plan.features && plan.features.map((feature, idx) => (
                                                <li key={idx}>✓ {feature}</li>
                                            ))}
                                        </ul>

                                        <button
                                            className="upgrade-button"
                                            onClick={() => {
                                                setSelectedPlan(plan)
                                                setSelectedPaymentMethod('card')
                                                setShowPopup(true)
                                                handleUpgradePlan(plan.id)
                                            }}
                                            disabled={subscription?.planId === plan.id}
                                        >
                                            {subscription?.planId === plan.id ? 'Current Plan' : 'Choose Plan'}
                                        </button>
                                    </div>
                                ))}
                            </div>
                        </section>
                    )}
                </div>

                {/* Cancel Confirmation Modal */}
                {showCancelModal && (
                    <div className="modal-overlay">
                        <div className="modal">
                            <h2>Cancel Subscription</h2>
                            <p>Are you sure you want to cancel your subscription? You will lose access to all premium
                                features.</p>
                            <div className="modal-actions">
                                <button
                                    className="modal-button cancel"
                                    onClick={() => setShowCancelModal(false)}
                                    disabled={cancelLoading}
                                >
                                    Keep Subscription
                                </button>
                                <button
                                    className="modal-button confirm"
                                    onClick={handleCancelSubscription}
                                    disabled={cancelLoading}
                                >
                                    {cancelLoading ? 'Cancelling...' : 'Yes, Cancel'}
                                </button>
                            </div>
                        </div>
                    </div>
                )}
            </div>
        </>
    )
}

export default DashboardPage
