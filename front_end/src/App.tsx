import {
    BrowserRouter as Router,
    Routes,
    Route,
    Navigate,
} from "react-router-dom";
import {useState, useEffect, JSX} from "react";
import LoginPage from "./features/auth/LoginPage";
import SignupPage from "./features/auth/SignupPage";
import HomePage from "./features/homepage/HomePage";
import DashboardPage from "./features/dashboard/DashboardPage";
import PrivateRoute from "./routes/PrivateRoute";
import {NotificationProvider} from "./features/dashboard/context/DashboardContext";
import "./styles/App.css";

function App(): JSX.Element {
    const [isAuthenticated, setIsAuthenticated] = useState(false);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const token = localStorage.getItem("token");
        setIsAuthenticated(!!token);
        setLoading(false);
    }, []);

    if (loading) {
        return <div className="loading">Loading...</div>;
    }

    return (
        <Router>
            <Routes>
                <Route
                    path="/login"
                    element={<LoginPage setIsAuthenticated={setIsAuthenticated}/>}
                />
                <Route
                    path="/signup"
                    element={<SignupPage setIsAuthenticated={setIsAuthenticated}/>}
                />
                <Route
                    path="/dashboard"
                    element={
                        <PrivateRoute isAuthenticated={isAuthenticated}>
                            <NotificationProvider>
                                <DashboardPage setIsAuthenticated={setIsAuthenticated}/>
                            </NotificationProvider>
                        </PrivateRoute>
                    }
                />
                <Route
                    path="/"
                    element={
                        isAuthenticated ? <Navigate to="/dashboard"/> : <HomePage/>
                    }
                />
                <Route path="*" element={<Navigate to="/"/>}/>
            </Routes>
        </Router>
    );
}

export default App;
