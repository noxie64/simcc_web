import React, { useEffect, useState } from "react"
import { useNavigate } from "react-router";
import api from "../api/baseUrl.ts";
import { useTitle } from "../hooks/useTitle.ts";
import { useStore } from "../hooks/useStore.ts";

export const Login: React.FC = () => {
    useTitle("Login");
    const [email, setEmail] = useState<string>("");
    const [password, setPassword] = useState<string>("");
    const navigate = useNavigate();
    /**
     * isCredentialRight -> variable that checks whether the login credentials are right
     * is2FA -> variable which is used in order to know whether the website should show a 2fa screen or login screen
     * isValid -> variable that checks whether the 2fa code is right
     */
    const { setLoggedIn, loggedIn } = useStore();
    const [isCredentialRight, setIsCredentialRight] = useState<boolean>();
    const [is2FA, setIs2FA] = useState<boolean>();
    const [isValid, setIsValid] = useState<boolean>();
    const [code, setCode] = useState<string>("");


    const forgotPassword = () => {
        navigate("/");
    }

    /**
     * withCredentials -> tells the browser, that it is secure to send and obtain cookies
     */
    const login = async () => {
        try {
            const response = await api.post("/users/login-user", {
                email: email,
                password: password
            }, { withCredentials: true });

            setPassword("");
            setIsCredentialRight(response.data);
            setIs2FA(response.data);
        } catch (error) {
            setPassword("");
            setIsCredentialRight(false);
            setIs2FA(false);
        }
    }

    const verifyCode = async () => {
        if (code.length != 0) {
            const response = await api.post("/users/verify-2fa-after-login", {
                email: email,
                code: code
            }, { withCredentials: true });

            if (response.data === true) {
                setLoggedIn(true);
                navigate("/infected");
            } else {
                setIsValid(false);
            }
        }
        else {
            setIsValid(false);
        }
    }

    useEffect(() => {
        if (isValid) {
            navigate("/infected", { state: true });
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

                                isCredentialRight == false &&
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
            {
                is2FA == true &&
                <div className={"flex flex-col items-center justify-center w-full min-h-screen space-y-6"}>
                    <div className={"card w-4/14 h-full bg-base-100 card-xs shadow-[0_-5px_10px_rgba(0,0,0,0.2),0_5px_10px_rgba(0,0,0,0.2)] "}>
                        <div className="card-body items-center text-center space-y-5">
                            <h2 className="card-title font-bold text-5xl mt-4">SimCC</h2>
                            <h2 className="card-title font-bold text-2xl">2FA</h2>
                            <input type={"password"} placeholder={"Type your code"} className={"input input-lg bg-gray-100"} onChange={(e) => setCode(e.target.value)} value={code} />
                            {
                                /**
                                 * If the code is wrong it will show an error message
                                 */

                                isValid == false &&
                                <>
                                    <p className="font-semibold text-lg text-red-600 border border-red-600 rounded-lg p-2 bg-red-200">The 2FA code is wrong</p>
                                </>
                            }
                            <div className="card-actions flex items-center justify-center w-full px-2">
                                <button className="btn btn-lg btn-primary mb-4" onClick={verifyCode}>Verify</button>
                            </div>
                        </div>
                    </div>
                </div>
            }
        </>
    )
}
