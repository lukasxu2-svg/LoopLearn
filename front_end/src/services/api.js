import axios from "axios";

const API_BASE_URL = import.meta.env.VITE_API_URL;

const api = axios.create({
    baseURL: API_BASE_URL,
    headers: {
        "Content-Type": "application/json",
    },
});

// Add token to requests
api.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem("token");
        if (token) {
            // @ts-ignore
            config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    },
);

// Handle responses
api.interceptors.response.use(
    (response) => response,
    (error) => {
        if (error.response?.status === 401) {
            localStorage.removeItem("token");
            localStorage.removeItem("user");
            window.location.href = "/login";
        }
        return Promise.reject(error);
    },
);

// Auth endpoints
export const authAPI = {
    signup: (data) => api.post("/auth/signup", data),
    login: (data) => api.post("/auth/login", data),
};

// Subscription endpoints
export const subscriptionAPI = {
    getSubscription: () => api.get("/subscription/current"),
    cancelSubscription: () => api.post(`/subscription/current/cancel`),
    cancelNextSubscription: () => api.post(`/subscription/next/cancel`),
    createSubscription: (data) => api.post(`/subscription`, data),
    createFreeSubscription: (data) => api.post(`/subscription/free`, data),
    reviseSubscription: (data) => api.post(`/subscription/revise`, data)
};

export const planAPI = {
    getPlanById: (id) => api.get(`/plans/${id}`),
    getPlans: () => api.get(`/plans`),
};

export default api;
