import { useTitle } from "../hooks/useTitle";

export default function Trojans() {
    useTitle("Trojans");

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

