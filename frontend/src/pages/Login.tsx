import React, {useEffect, useState} from "react"
import {useNavigate} from "react-router";
import api from "../api/baseUrl.ts";

export const Login: React.FC = () => {
    const [email, setEmail] = useState<string>("");
    const [password, setPassword] = useState<string>("");
    const navigate = useNavigate();
    const [isValid, setIsValid] = useState<boolean>();
    const [is2FA, setIs2FA] = useState<boolean>();

    const forgotPassword = () => {
        navigate("/dummy");
    }

    const login = async () => {
        const response = await api.get("/users/login-user", {params: {
            email: email,
            password: password
        }});

        setEmail("");
        setPassword("");
        setIsValid(response.data)
    }

    useEffect(()=>{
        if(isValid){
            navigate("/dummy");
        }
    }, [isValid])

    return (
        <>
            {
                /**
                 * Checks either the user typed his credentials and must be redirected to the screen where he needs
                 * to type his 2fa code, or he still needs to type his credentials
                 */
            }
            {
                is2FA != true &&
                <div className={"flex flex-col items-center justify-center w-full min-h-screen space-y-6"}>
                    <div className={"card w-4/14 h-full bg-base-100 card-xs shadow-[0_-5px_10px_rgba(0,0,0,0.2),0_5px_10px_rgba(0,0,0,0.2)] "}>
                        <div className="card-body items-center text-center space-y-5">
                            <h2 className="card-title font-bold text-5xl mt-4">SimCC</h2>
                            <h2 className="card-title font-bold text-2xl">Login</h2>
                            <input type={"email"} placeholder={"Type your email"} className={"input input-lg bg-gray-100"} onChange={(e) => setEmail(e.target.value)} value={email} />
                            <input type={"password"} placeholder={"Type your password"} className={"input input-lg bg-gray-100"} onChange={(e) => setPassword(e.target.value)} value={password} />
                            {
                                /**
                                 * If the password or email is wrong, or account doesn't exist it will show
                                 * an error message
                                 */

                                isValid == false &&
                                <>
                                    <p className="font-semibold text-lg text-red-600 border border-red-600 rounded-lg p-2 bg-red-200">Password or Email is wrong</p>
                                </>
                            }
                            <div className="card-actions flex justify-between items-center w-full px-2">
                                <a className={"link link-primary text-lg"} onClick={forgotPassword}>Forgot your password?</a>
                                <button className="btn btn-lg btn-primary mb-4" onClick={login}>Login</button>
                            </div>
                        </div>
                    </div>
                </div>
            }
        </>
    )
}