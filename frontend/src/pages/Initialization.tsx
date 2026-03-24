import React from "react"

export const Initialization: React.FC = () => {
    return (
            <div className={"flex items-center justify-center w-full min-h-screen"}>
                <div className={"card w-4/12 h-full bg-base-100 card-xs shadow-[0_-5px_10px_rgba(0,0,0,0.2),0_5px_10px_rgba(0,0,0,0.2)] "}>
                    <div className="card-body items-center text-center space-y-5">
                        <h2 className="card-title font-bold text-5xl">SimCC</h2>
                        <h2 className="card-title font-bold text-2xl">Set-Up</h2>
                        <input type={"text"} placeholder={"Type your username"} className={"input input-primary input-lg"}/>
                        <input type={"text"} placeholder={"Type your email"} className={"input input-primary input-lg"}/>
                        <input type={"text"} placeholder={"Type your password"} className={"input input-primary input-lg"}/>
                        <div className="card-actions">
                            <button className="btn btn-lg btn-primary mb-4">Set-Up</button>
                        </div>
                    </div>
                </div>
            </div>
    )
}