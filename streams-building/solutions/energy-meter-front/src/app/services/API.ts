import wretch from "wretch";

export enum ServiceName {
    BACKEND
}

// these environment variables are only evaluated/available at build time
const serviceUrlMap: { [key in ServiceName]: string } = {
    [ServiceName.BACKEND]: process.env.BACKEND ?? "http://localhost:8080/",
};
console.log("Backend URL:", serviceUrlMap);

export function getRESTClient(serviceName: ServiceName) {
    // `wretch` is a thin wrapper around the `fetch` API available in most modern browsers
    return wretch(serviceUrlMap[serviceName]);
}

// wretch().catcher(...) can't handle rejections due to no response from server
window.addEventListener("unhandledrejection", (event) => {
    const message = `caught error: ${event.reason}`;
    console.error(message);
});

