import { ServiceName, getSSEClient } from "./API";
import { WindTurbineProduction } from "../models/WindTurbineProduction";

const SSE = getSSEClient(ServiceName.BACKEND);

export function subscribeToPowerEvents(onEvent: (p: WindTurbineProduction) => void): void {
    SSE.open<WindTurbineProduction>("/turbines/generated-power", onEvent);
}

