import React, {useEffect, useState} from "react";
import {useNavigate, Link} from "react-router-dom";
import {authAPI} from "../../services/api.js";
import "./styles/AuthPages.css";
import {isTokenExpired} from "./utils/authUtils.js";

interface Props {
    setIsAuthenticated: (v: boolean) => void;
}

function LoginPage({setIsAuthenticated}: Props) {
    const navigate = useNavigate();
    const [formData, setFormData] = useState({
        email: "",
        password: "",
    });
    const [error, setError] = useState("");
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        if (isTokenExpired()) {
            localStorage.clear();
        }
    });

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const {name, value} = e.target;
        setFormData((prev) => ({
            ...prev,
            [name]: value,
        }));
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setError("");
        setLoading(true);

        try {
            const response = await authAPI.login(formData);
            const {accessToken, tokenType, expiresIn, user} = response.data;

            const exp = new Date(Date.now() + expiresIn * 1000).toISOString();

            localStorage.setItem("token", accessToken);
            localStorage.setItem("user", JSON.stringify(user));
            localStorage.setItem("expiresIn", exp);

            setIsAuthenticated(true);
            navigate("/dashboard");
        } catch (err: any) {
            setError(
                err.response?.data?.message || "Login failed. Please try again.",
            );
            console.error("Login error:", err);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="auth-container">
            <div className="auth-card">
                <h1>Welcome Back</h1>
                <p className="auth-subtitle">Sign in to your account</p>

                {error && <div className="error-message">{error}</div>}

                <form onSubmit={handleSubmit}>
                    <div className="form-group">
                        <label htmlFor="email">Email</label>
                        <input
                            type="email"
                            id="email"
                            name="email"
                            value={formData.email}
                            onChange={handleChange}
                            required
                            placeholder="Enter your email"
                        />
                    </div>

                    <div className="form-group">
                        <label htmlFor="password">Password</label>
                        <input
                            type="password"
                            id="password"
                            name="password"
                            value={formData.password}
                            onChange={handleChange}
                            required
                            placeholder="Enter your password"
                        />
                    </div>

                    <button type="submit" className="auth-button" disabled={loading}>
                        {loading ? "Signing in..." : "Sign In"}
                    </button>
                </form>

                <p className="auth-link">
                    Don't have an account? <Link to="/signup">Create one</Link>
                </p>
            </div>
        </div>
    );
}

export default LoginPage;
