import React, {createContext, useContext, useState} from "react";
import {subscriptionAPI} from "../../../services/api";

interface DashboardContextValue {
    subscription;
    successMessage: string;
    errorMessage: string;
    loadSubscriptionData;
    setSuccessMessage: (message: string) => void;
    setErrorMessage: (message: string) => void;
    clearNotifications: () => void;
}

const DashboardContext = createContext<DashboardContextValue | undefined>(
    undefined,
);

export function NotificationProvider({
                                         children,
                                     }: {
    children: React.ReactNode;
}) {
    const [successMessage, setSuccessMessageState] = useState("");
    const [errorMessage, setErrorMessageState] = useState("");
    const [subscription, setSubscription] = useState("")

    const setSuccessMessage = (message: string) => {
        setSuccessMessageState(message);
        setErrorMessageState("");

        setTimeout(() => {
            clearNotifications();
        }, 3000);
    };

    const setErrorMessage = (message: string) => {
        setErrorMessageState(message);
        setSuccessMessageState("");

        setTimeout(() => {
            clearNotifications();
        }, 3000);
    };

    const clearNotifications = () => {
        setSuccessMessageState("");
        setErrorMessageState("");
    };

    const loadSubscriptionData = async () => {
        try {
            const subResponse = await subscriptionAPI.getSubscription();
            setSubscription(subResponse.data);
        } catch (err) {
            throw new Error("Failed to load subscription data");
        }
    }

    return (
        <DashboardContext.Provider
            value={{
                subscription,
                successMessage,
                errorMessage,
                loadSubscriptionData,
                setSuccessMessage,
                setErrorMessage,
                clearNotifications,
            }}
        >
            {children}
        </DashboardContext.Provider>
    );
}

export function useDashboardContext() {
    const context = useContext(DashboardContext);

    if (!context) {
        throw new Error(
            "useDashboardContext must be used within NotificationProvider",
        );
    }

    return context;
}
