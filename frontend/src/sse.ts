export interface InfectedStatus {
    online: boolean,
    iid: string
}

export function initInfectedSSE() {
    const sse = new EventSource("/api/infected/sse/status");

    sse.addEventListener("status.updated", (e) => {
        window.dispatchEvent(new CustomEvent('infected-status', {
            detail: JSON.parse(e.data) as InfectedStatus
        }))
    })

    sse.onopen = () => console.log("SSE connected");
    sse.onerror = (e) => console.error("SSE error", e);

    return () => sse.close();
}
