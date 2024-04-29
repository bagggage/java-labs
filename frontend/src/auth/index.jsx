import apiRequest from "../api";
import Cookies from "js-cookie";

export function isPassCorrect(password) {
  return password.length >= 8 && (/[\W\s]+/.test(password) === false);
};

export function isLoginCorrect(login) {
  return login.length >= 4 && (/[\s]/.test(login) === false);
};

async function isAuthCheck() {
  const responce = await apiRequest("/auth/verify");

  return responce.ok;
}

export function isAuth() {
  if (Cookies.get("authToken") === undefined) return false;

  isAuthCheck().then((result) => {
    if (result === false) {
      Cookies.remove("authToken", { path: '' });
    }
  });

  return true;
}