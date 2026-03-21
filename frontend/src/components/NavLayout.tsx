import { FaUser } from "react-icons/fa";
import { Outlet, useNavigate } from "react-router";

export default function NavLayout() {
    const navigate = useNavigate();

    return (
        <div className="w-screen h-screen flex flex-col">
            <div className="p-5 shadow-md flex justify-between">
                <div className="flex items-center gap-2">
                    <h1 className="text-2xl font-bold mr-3 cursor-pointer"
                        onClick={() => navigate("/")}
                    >SimCC</h1>
                    <h3 className="font-medium cursor-pointer">Infected</h3>
                    <h3 className="font-medium cursor-pointer">Trojan</h3>
                    <h3 className="font-medium cursor-pointer">Settings</h3>
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

