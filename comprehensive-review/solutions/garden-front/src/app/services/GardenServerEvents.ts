import { GardenEvent } from "@app/models/GardenEvent";
import { SensorMeasurement } from "@app/models/SensorMeasurement";
import { GardenStatus } from "../models/GardenStatus";
import { ServiceName, getSSEClient } from "./API";

const sse = getSSEClient(ServiceName.BACKEND);

export function subscribeToGardenStatuses(onEvent: (p: GardenStatus) => void): void {
    sse.open<GardenStatus>("/garden/statuses", onEvent);
}

export function subscribeToGardenEvents(onEvent: (p: GardenEvent) => void): void {
    sse.open<GardenEvent>("/garden/events", onEvent);
}

export function subscribeToSensorMeasurements(onEvent: (p: SensorMeasurement) => void): void {
    sse.open<SensorMeasurement>("/sensor/measurements/enriched", onEvent);
}


