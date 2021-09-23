import { ServiceName, getSSEClient } from "./API";
import { WindTurbineStats } from "../models/WindTurbineStats";

const SSE = getSSEClient(ServiceName.BACKEND);

export function subscribeToStatsEvents(onEvent: (p: WindTurbineStats) => void): void {
    SSE.open<WindTurbineStats>("/turbines/measurements-count", onEvent);
}

