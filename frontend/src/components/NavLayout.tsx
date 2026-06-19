import { useEffect, useState } from "react";
import { FaUser } from "react-icons/fa";
import { Outlet, useLocation, useNavigate } from "react-router";
import { initInfectedSSE } from "../sse";
import { useStore } from "../hooks/useStore";

export default function NavLayout() {
    const navigate = useNavigate();
    const location = useLocation();
    const [selected, setSelected] = useState<string>();
    const { loggedIn } = useStore();


    const style = (name: string) => {
        // check if the current route is equal to the name we keep track off
        if (name == selected) {
            return "bg-primary text-white rounded p-2";
        }

        return "";
    }

    useEffect(() => {
        console.log("Path", location.pathname)
        setSelected(location.pathname.split("/")[1]);
    }, [location]);

    return (
        <div className="w-screen h-screen flex flex-col">
            <div className="p-5 shadow-md flex justify-between">
                <div className="flex items-center gap-2">
                    <h1 className="text-2xl font-bold mr-3 cursor-pointer"
                        onClick={() => navigate("/dashboard")}
                    >SimCC</h1>
                    <h3 className={`font-medium cursor-pointer ${style("infected")}`}
                        onClick={() => navigate("/infected")}
                    >Infected</h3>
                    <h3 className={`font-medium cursor-pointer  ${style("settings")}`}
                        onClick={() => navigate("/settings")}
                    >Settings</h3>
                    <h3 className={`font-medium cursor-pointer  ${style("trojans")}`}
                        onClick={() => navigate("/trojans")}
                    >Trojans</h3>
                </div>
                <div className="avatar">
                    <div className="rounded-full p-2 border border-outline">
                        <FaUser className="text-2xl text-primary" />
                    </div>
                </div>
            </div>
            <div className="grow flex flex-col">
                <Outlet />
            </div>
        </div>
    )
}

