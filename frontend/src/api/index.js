import Cookies from "js-cookie";

export const apiUrl = "http://localhost:8080/api";

export default async function apiRequest(url = "", method = "GET", data = null, isAuthorized = true) {
  const response = await fetch(apiUrl + url, {
    credentials: "include",
    method: method,
    headers: {
      "Content-Type": "application/json",
      "Authorization": isAuthorized ? Cookies.get("authToken") : null 
    },
    body: data !== null ? (method !== "GET" ? JSON.stringify(data) : null) : null
  });

  return response;
}