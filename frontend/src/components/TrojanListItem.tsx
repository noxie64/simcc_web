import { useState } from "react";
import type { Trojan } from "../types/trojan"
import { FaVirusCovid } from "react-icons/fa6";

export default function TrojanListItem(props: {
    trojan: Trojan,
    handleBuild: () => void
}) {

    return (
        <div className="flex flex-row items-center justify-between gap-2 border border-slate-200 p-2 rounded">
            <div className="flex flex-row items-center gap-2">
                <FaVirusCovid className="text-3xl" />
                <div className="flex flex-col">
                    <p className="text-lg font-semibold">{props.trojan.name}</p>
                    <p className="text-slate-500 italic">
                        last built {
                            props.trojan.lastBuilt
                                ? props.trojan.lastBuilt.format("HH:mm DD/MM/YYYY")
                                : 'never'
                        }
                    </p>
                </div>
            </div>
            <div className="flex gap-2">
                {
                    props.trojan.building
                        ? <>
                            <button className="btn btn-disabled">
                                <span>Building</span>
                                <span className="loading loading-spinner loading-md"></span>
                            </button>
                            <button className="btn btn-disabled">
                                Download
                            </button>
                        </>
                        : <>
                            <button className="btn btn-primary" onClick={() => {
                                props.handleBuild();
                            }}>
                                Build
                            </button>
                            <button className={`btn ${
                                props.trojan.lastBuilt
                                ? 'btn-primary'
                                : 'btn-disabled'
                            }`} onClick={() => {
                                window.location.href = `/api/trojan/download/${props.trojan.ccid}`
                            }}>
                                Download
                            </button>
                        </>}
            </div>
        </div>
    )
}

