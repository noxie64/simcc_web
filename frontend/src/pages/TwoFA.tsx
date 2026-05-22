import React, {useEffect, useState} from "react"
import {useLocation, useNavigate} from "react-router";
import api from "../api/baseUrl.ts";
export const TwoFA: React.FC = () => {
    const [code, setCode] = useState<number>();
    const [isVerified, setIsVerified] = useState<boolean>();
    const [url, setUrl] = useState<string>();
    /**
     * Retrieves the navigation state passed from the previous page
     */
    const location = useLocation();
    const {username} = location.state;
    const navigate = useNavigate();

    const verifyCode = async () => {
        const response = await api.get("/users/verify-2fa", {params: {code, username}});
        setIsVerified(response.data)
        setCode(undefined)
    }

    function settingCode(input:string) {
        const parse = Number.parseInt(input);

        if (!isNaN(parse)) {
            setCode(Number.parseInt(input));
        }else {
            setCode(undefined)
        }
    }

    useEffect(() => {
        const getQrUrl = async () => {
            try{
                const response = await api.get("/users/obtain-qr-url",
                    {params: {username: username}});
                setUrl(response.data);
                console.log(response.data);
            }catch (e) {
                console.log("Error: " + e);
            }
        };
        console.log(code);
        getQrUrl();
    }, [])

    useEffect(() => {
        const verification = async () => {
            if (isVerified){
                navigate("/login");
            }
        };
        verification();
    }, [isVerified])

    return (
        <>
            <div className={"flex items-center justify-center w-full min-h-screen"}>
                <div className={"card w-4/14 h-full bg-base-100 card-xs shadow-[0_-5px_10px_rgba(0,0,0,0.2),0_5px_10px_rgba(0,0,0,0.2)] "}>
                    <div className="card-body items-center text-center space-y-5">
                        <h2 className="card-title font-bold text-5xl mt-4">SimCC</h2>
                        <h2 className="card-title font-bold text-2xl">2FA</h2>
                        <img src={`data:image/png;base64,${url}`} alt="QR Code" className="w-64 h-64" />
                        <div className="flex flex-row gap-2 p-2">
                            <input type={"text"} placeholder={"Type your code"} className={"input input-lg bg-gray-100"} onChange={(e) => settingCode(e.target.value)} value={code ?? ""} />
                            <div className="card-actions">
                                <button className="btn btn-lg btn-primary mb-4" onClick={verifyCode}>Verify</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </>
    )
}