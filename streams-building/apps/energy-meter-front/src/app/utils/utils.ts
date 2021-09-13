import { useEffect, useRef } from "react";

export function accessibleRouteChangeHandler() {
  return window.setTimeout(() => {
    const mainContainer = document.getElementById('primary-app-container');
    if (mainContainer) {
      mainContainer.focus();
    }
  }, 50);
}


export function usePrevious(value) {
	const ref = useRef();

	useEffect(() => {
    ref.current = value;
	});

	return ref.current;
}