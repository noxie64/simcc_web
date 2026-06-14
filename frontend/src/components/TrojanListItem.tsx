import type { Trojan } from "../types/trojan"
import { FaVirusCovid } from "react-icons/fa6";

export default function TrojanListItem(props: {
    building: boolean,
    trojan: Trojan
}) {
    return (
        <div className="flex flex-row items-center justify-between gap-2 border border-slate-200 p-2 rounded">
            <div className="flex flex-row items-center gap-2">
                <FaVirusCovid className="text-xl" />
                <h3 className="text-lg font-semibold">{props.trojan.name}</h3>
                <p className="text-slate-500 italic">
                    last built {
                        props.trojan.lastBuilt
                            ? props.trojan.lastBuilt.format("HH:mm DD/MM/YYYY")
                            : 'never'
                    }
                </p>
            </div>
            <div className="flex gap-2">
                {
                    props.building
                        ? <>
                            <button className="btn btn-primary">
                                Build
                            </button>
                            <button className="btn btn-primary">
                                Download
                            </button>
                        </>
                        : <>
                            <button className="btn btn-disabled">
                                <span>Building</span>
                                <span className="loading loading-spinner loading-md"></span>
                            </button>
                            <button className="btn btn-disabled">
                                Download
                            </button>
                        </>}
            </div>
        </div>
    )
}

