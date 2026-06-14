import type { Dayjs } from "dayjs";

export interface Trojan {
    ccid: string,
    name: string,
    lastBuilt: Dayjs | null,
    building: boolean
}
