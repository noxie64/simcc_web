import { useEffect, useRef, useState } from "react";
import { useTitle } from "../hooks/useTitle";
import api from "../api/baseUrl";
import { type Trojan } from "../types/trojan";
import TrojanListItem from "../components/TrojanListItem";
import dayjs from "dayjs";

interface FilledTrojanSetting {
    defaultValue: any,
    value: any
}

export default function Trojans() {
    useTitle("Trojans");
    const creatorModalRef: any = useRef(null);
    const [buildSettings, setBuildSettings] = useState<Record<string, FilledTrojanSetting>>({});
    const [name, setName] = useState<string>('');
    const [trojans, setTrojans] = useState<Trojan[]>([]);

    const loadSettings = async () => {
        const response = await api.get("/trojan/defaults/build");
        setBuildSettings(Object.fromEntries(
            Object.entries(response.data).map(([key, value]): [string, FilledTrojanSetting] => {
                return [
                    key,
                    {
                        defaultValue: value,
                        value: ""
                    } as FilledTrojanSetting
                ]
            })
        ));
    }

    const loadTrojans = async () => {
        const res = await api.get("/trojan/");
        console.log(res.data);
        setTrojans(
            (await res.data as Trojan[]).map((trojan: Trojan) => ({
                ...trojan,
                lastBuilt: trojan.lastBuilt
                    ? dayjs(trojan.lastBuilt)
                    : null
            }))
        );
    }

    const handleModalClose = () => {
        setName('');
        setBuildSettings(prev => Object.fromEntries(
            Object.entries(prev)
                .map(([key, value]): [string, FilledTrojanSetting] => ([
                    key,
                    {
                        ...value,
                        value: ''
                    }
                ]))
        ));
    }

    const handleCreate = async () => {
        await api.post('/trojan/create', {
            name,
            buildConfig: Object.fromEntries(
                Object.entries(buildSettings)
                    .filter(([_, value]: [string, FilledTrojanSetting]) => value.value != '')
                    .map(([key, value]: [string, FilledTrojanSetting]) => [
                        key, value.value
                    ]))
        });
        await loadTrojans();
    }

    const handleBuild = async (ccid: string) => {
        setTrojans(prev => prev.map(trojan =>
            trojan.ccid === ccid ? { ...trojan, building: true } : trojan
        ));
        await api.post(`/trojan/build/${ccid}`);
    }

    useEffect(() => {
        const sse = new EventSource("/api/trojan/sse/build");
        sse.addEventListener("build.completed", (e) => {
            const data = JSON.parse(e.data);
            console.log("Build complete:", data.ccid, data.message);
            setTrojans(prev => prev.map(trojan => trojan.ccid == data.ccid
                ? { ...trojan, building: false, lastBuilt: dayjs() }
                : trojan
            ));
        });

        sse.addEventListener("build.failed", (e) => {
            const data = JSON.parse(e.data);
            console.error("Build failed:", data.ccid, data.message);
        });

        sse.onopen = () => console.log("SSE connected");
        sse.onerror = (e) => console.error("SSE error", e);

        loadSettings();
        loadTrojans();

        creatorModalRef.current.addEventListener('close', handleModalClose)

        return () => {
            if (creatorModalRef.current) {
                creatorModalRef.current.removeEventListener('close', handleModalClose);
            }
            sse.close();
        }
    }, []);

    return (
        <div className="flex flex-col p-4">
            <div>
                <button className="btn btn-primary" onClick={() => creatorModalRef.current.showModal()}>Add</button>
            </div>
            <div className="flex flex-col pt-3 pb-3 gap-2">
                {
                    trojans.map(trojan => <TrojanListItem
                        trojan={trojan}
                        key={trojan.ccid}
                        handleBuild={() => handleBuild(trojan.ccid)}
                    />)
                }
            </div>
            <dialog ref={creatorModalRef} className="modal">
                <div className="modal-box">
                    <h3 className="font-bold text-lg">Trojan-Creator</h3>
                    <div className="mb-2">
                        <h4 className="font-semibold">Name</h4>
                        <input
                            value={name}
                            onChange={e => setName(e.target.value)}
                            type="text" className="input focus:outline-1" placeholder="Trojan 01" />
                    </div>
                    <dl className="grid grid-cols-[1fr_auto] gap-x-8 gap-y-1">
                        {Object.entries(buildSettings).map(([key, value]) => (
                            <>
                                <dt key={`k-${key}`} className="font-semibold truncate">{key}</dt>
                                <input type={
                                    (() => {
                                        if (["number", "bingint"].includes(typeof (value.defaultValue))) {
                                            return "number";
                                        }

                                        return "text";
                                    })()
                                }
                                    value={buildSettings[key].value}
                                    onChange={(e) => {
                                        setBuildSettings(prev => ({
                                            ...prev,
                                            [key]: {
                                                defaultValue: value.defaultValue,
                                                value: e.target.value
                                            }
                                        }))
                                    }}
                                    placeholder={value.defaultValue} className="input focus:outline-1" />
                            </>
                        ))}
                    </dl>
                    <div className="modal-action">
                        <form method="dialog">
                            <button className="btn btn-primary" onClick={handleCreate}>Create</button>
                        </form>
                    </div>
                </div>
            </dialog>
        </div>
    )
}

