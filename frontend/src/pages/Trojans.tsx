import { useEffect } from "react";
import { useTitle } from "../hooks/useTitle";

export default function Trojans() {
    useTitle("Trojans");

    useEffect(() => {
        const sse = new EventSource("/api/trojan/sse/build");
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

        return () => sse.close()
    }, []);

    return (
        <div className="flex flex-col">
            <div className="modal-action">
                <form method="dialog">
                    <button className="btn">Close</button>
                </form>
            </div>
        </div>
    )
}

