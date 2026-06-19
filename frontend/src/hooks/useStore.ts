import { create } from 'zustand'
import type { Infected } from '../types/infected'

interface SIMCCState {
    infected: Infected[],
    loggedIn: boolean,
    setLoggedIn: (val: boolean) => void,
    setInfected: (infected: Infected[] | ((prev: Infected[]) => Infected[])) => void,
    findByIID: (iid: string) => Infected | undefined,
}

export const useStore = create<SIMCCState>((set, get) => ({
    infected: [],
    loggedIn: false,
    setLoggedIn: (loggedIn) => set({ loggedIn }),
    setInfected: (infected) =>
        set((state) => ({
            infected: typeof (infected) === 'function'
                ? (infected as (prev: Infected[]) => Infected[])(state.infected)
                : infected
        })),
    findByIID: (iid: string) => get().infected.find(i => i.iid === iid)
}))
