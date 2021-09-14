import { getRESTClient, ServiceName } from "./API";
import { WindTurbine } from "../models/WindTurbine";

const API = getRESTClient(ServiceName.BACKEND);

export function getWindTurbines(): Promise<WindTurbine[]> {
    return API.url("/turbines")
        .get()
        .json<WindTurbine[]>()
}

