import React, {createContext, useContext, useState} from "react";

interface NotificationContextValue {
    successMessage: string;
    errorMessage: string;
    setSuccessMessage: (message: string) => void;
    setErrorMessage: (message: string) => void;
    clearNotifications: () => void;
}

const NotificationContext = createContext<NotificationContextValue | undefined>(
    undefined,
);

export function NotificationProvider({
                                         children,
                                     }: {
    children: React.ReactNode;
}) {
    const [successMessage, setSuccessMessageState] = useState("");
    const [errorMessage, setErrorMessageState] = useState("");

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
        <NotificationContext.Provider
            value={{
                successMessage,
                errorMessage,
                setSuccessMessage,
                setErrorMessage,
                clearNotifications,
            }}
        >
            {children}
        </NotificationContext.Provider>
    );
}

export function useNotifications() {
    const context = useContext(NotificationContext);

    if (!context) {
        throw new Error(
            "useNotifications must be used within NotificationProvider",
        );
    }

    return context;
}
