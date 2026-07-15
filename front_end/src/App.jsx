import {BrowserRouter as Router, Routes, Route, Navigate} from 'react-router-dom'
import {useState, useEffect} from 'react'
import LoginPage from './pages/LoginPage.jsx'
import SignupPage from './pages/SignupPage.jsx'
import DashboardPage from './pages/DashboardPage.jsx'
import PrivateRoute from './components/PrivateRoute.jsx'
import './App.css'

function App() {
    const [isAuthenticated, setIsAuthenticated] = useState(false)
    const [loading, setLoading] = useState(true)

    useEffect(() => {
        // Check if user is authenticated (token exists in localStorage)
        const token = localStorage.getItem('token')
        setIsAuthenticated(!!token)
        setLoading(false)
    }, [])

    if (loading) {
        return <div className="loading">Loading...</div>
    }

    return (
        <Router>
            <Routes>
                <Route path="/login" element={<LoginPage setIsAuthenticated={setIsAuthenticated}/>}/>
                <Route path="/signup" element={<SignupPage setIsAuthenticated={setIsAuthenticated}/>}/>
                <Route
                    path="/dashboard"
                    element={
                        <PrivateRoute isAuthenticated={isAuthenticated}>
                            <DashboardPage setIsAuthenticated={setIsAuthenticated}/>
                        </PrivateRoute>
                    }
                />
                <Route path="/" element={isAuthenticated ? <Navigate to="/dashboard"/> : <Navigate to="/login"/>}/>
                <Route path="*" element={<Navigate to="/"/>}/>
            </Routes>
        </Router>
    )
}

export default App
