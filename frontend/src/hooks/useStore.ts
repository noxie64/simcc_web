import { create } from 'zustand'
import type { Infected } from '../types/infected'

interface SIMCCState {
    infected: Infected[],
    setInfected: (infected: Infected[]) => void,
    findByIID: (iid: string) => Infected | undefined
}

export const useStore = create<SIMCCState>((set, get) => ({
    infected: [],
    setInfected: (infected) => set({ infected }),
    findByIID: (iid: string) => get().infected.find(i => i.iid === iid)
}))
