import { useEffect, useRef, useState } from "react";
import { useTitle } from "../hooks/useTitle";
import api from "../api/baseUrl";

export default function Trojans() {
    useTitle("Trojans");
    const creatorModal: any = useRef(null);
    const [buildSettings, setBuildSettings] = useState<Record<string, any>>({});

    const loadSettings = async () => {
        const response = await api.get("/trojan/defaults/build");
        setBuildSettings(response.data)
    }

    useEffect(() => {
        const sse = new EventSource("/trojan/sse/build");
        sse.addEventListener("build.completed", (e) => {
            const data = JSON.parse(e.data);
            console.log("Build complete:", data.buildId, data.message);
        });

        sse.addEventListener("build.failed", (e) => {
            const data = JSON.parse(e.data);
            console.error("Build failed:", data.buildId, data.error);
        });

        sse.onopen = () => console.log("SSE connected");
        sse.onerror = (e) => console.error("SSE error", e);

        loadSettings();

        return () => sse.close()
    }, []);

    return (
        <div className="flex flex-col p-4">
            <div>
                <button className="btn btn-primary" onClick={() => creatorModal.current.showModal()}>Add</button>
            </div>
            <dialog ref={creatorModal} className="modal">
                <div className="modal-box">
                    <h3 className="font-bold text-lg">Trojan-Creator</h3>
                    <div className="mb-2">
                        <h4 className="font-semibold">Name</h4>
                        <input type="text" className="input focus:outline-1" placeholder="Trojan 01" />
                    </div>
                    <dl className="grid grid-cols-[1fr_auto] gap-x-8 gap-y-1">
                        {Object.entries(buildSettings).map(([key, value]) => (
                            <>
                                <dt key={`k-${key}`} className="font-semibold truncate">{key}</dt>
                                <input type={
                                    (() => {
                                        if (["number", "bingint"].includes(typeof(value))) {
                                            return "number";
                                        }

                                        return "text";
                                    })()
                                } placeholder={value} className="input focus:outline-1"/>
                            </>
                        ))}
                    </dl>
                    <div className="modal-action">
                        <form method="dialog">
                            <button className="btn btn-primary">Close</button>
                        </form>
                    </div>
                </div>
            </dialog>
        </div>
    )
}

