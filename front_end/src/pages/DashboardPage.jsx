import {useState, useEffect} from 'react'
import {useNavigate} from 'react-router-dom'
import {subscriptionAPI} from '../services/api.js'
import '../styles/Dashboard.css'

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

    useEffect(() => {
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
                const plansResponse = await subscriptionAPI.getPlans()
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
            await subscriptionAPI.upgradePlan(planId)
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
                                    <h3>{plan.name}</h3>
                                    <div className="plan-price">${plan.price}/month</div>
                                    <p className="plan-description">{plan.description}</p>

                                    <ul className="plan-features">
                                        {plan.features && plan.features.map((feature, idx) => (
                                            <li key={idx}>✓ {feature}</li>
                                        ))}
                                    </ul>

                                    <button
                                        className="upgrade-button"
                                        onClick={() => handleUpgradePlan(plan.id)}
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
    )
}

export default DashboardPage
