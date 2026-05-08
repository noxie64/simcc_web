import React, {useEffect, useState} from "react"
import api from "../api/baseUrl.ts";
import {useNavigate} from "react-router";

export const Initialization: React.FC = () => {
    const navigate = useNavigate();
    const [username, setUsername] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");

    const registerUser = async () => {
        try {
            const response = await api.post("/users/reg", {
                username: username,
                email: email,
                password: password
            })

            setUsername("");
            setEmail("");
            setPassword("");
            navigate("/twofa", {state: {username}});
            console.log(response);
        } catch (error) {
            console.error("Error: " + error);
        }
    }

    useEffect(() => {
        const checkAdmin = async () => {
            try{
                const response = await api.get("/users/check-if-exist");
                if (response.data === true){
                    navigate("/");
                }
            }catch (e) {
                console.log("Error: " + e);
            }
        };
        checkAdmin();
    }, [])

    return (
            <div className={"flex items-center justify-center w-full min-h-screen"}>
                <div className={"card w-4/14 h-full bg-base-100 card-xs shadow-[0_-5px_10px_rgba(0,0,0,0.2),0_5px_10px_rgba(0,0,0,0.2)] "}>
                    <div className="card-body items-center text-center space-y-5">
                        <h2 className="card-title font-bold text-5xl mt-4">SimCC</h2>
                        <h2 className="card-title font-bold text-2xl">Set-Up</h2>
                        <input type={"text"} placeholder={"Type your username"} className={"input input-lg bg-gray-100"} onChange={(e) => setUsername(e.target.value)} value={username} />
                        <input type={"email"} placeholder={"Type your email"} className={"input input-lg bg-gray-100"} onChange={(e) => setEmail(e.target.value)} value={email} />
                        <input type={"password"} placeholder={"Type your password"} className={"input input-lg bg-gray-100"} onChange={(e) => setPassword(e.target.value)} value={password} />
                        <div className="card-actions">
                            <button className="btn btn-lg btn-primary mb-4" onClick={registerUser}>Set-Up</button>
                        </div>
                    </div>
                </div>
            </div>
    )
}