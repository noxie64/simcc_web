import React, {useEffect, useState} from "react"
import {useLocation, useNavigate} from "react-router";
import type {Infected} from "../types/infected.ts";
import api from "../api/baseUrl.ts";
import {InfectedCard} from "../components/InfectedCard.tsx";
import {useTitle} from "../hooks/useTitle.ts";

export const InfectedPage: React.FC = () => {
    const navigate = useNavigate();
    const [infected, setInfected] = useState<Infected[]>();
    const location = useLocation();
    useTitle("InfectedPage");

    const obtainAllInfectedSystems = async () => {
        const response = await api.get("/infected/allInfected");
        setInfected(response.data as Infected[]);
    }

    const command = (infected: Infected) => {
        console.log(infected.currentIpAddress);
    }

    /**
     * Checks if the user made login, before entering this page
     */

    useEffect(() => {
        if (!location.state) {
            navigate("/login");
            return;
        }
        obtainAllInfectedSystems();
    }, []);

    return (
        <>
            <div className="flex flex-row flex-wrap gap-4 p-4">
                {infected?.map((io) =>
                    (<InfectedCard infected = {io} command = {() => command(io)}/>))}
            </div>
        </>
    )
}