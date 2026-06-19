import { useStore } from "../hooks/useStore";
import { Outlet, useNavigate } from "react-router";
import { useEffect, useState } from "react";
import api from "../api/baseUrl";
import { initInfectedSSE } from "../sse";

export const EnsureLoggedIn = () => {
    const { setLoggedIn } = useStore();
    const navigate = useNavigate();
    const [checking, setChecking] = useState(true);

    useEffect(() => {
        let cleanup: () => void;
        let cancelled = false;

        const check = async () => {
            try {
                await api.get('/users/me');
                setLoggedIn(true);
                const sseCleanup = initInfectedSSE();
                if (cancelled) {
                    sseCleanup?.();
                } else {
                    cleanup = sseCleanup;
                }
            } catch (error) {
                setLoggedIn(false);
                navigate("/login");
            } finally {
                setChecking(false);
            }
        };

        check();

        return () => {
            cancelled = true;
            cleanup?.();
        };
    }, []);

    if (checking) {
        return <div>Loading...</div>;
    }

    return <Outlet />;
}
