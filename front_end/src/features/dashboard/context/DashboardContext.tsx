import React, {createContext, useContext, useState} from "react";

interface DashboardContextValue {
    subscription;
    successMessage: string;
    errorMessage: string;
    setSubscription;
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

    return (
        <DashboardContext.Provider
            value={{
                subscription,
                successMessage,
                errorMessage,
                setSubscription,
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
