import axios from "axios";
import { User } from "../types/User";

const API_URL = process.env.REACT_APP_API_BASE_URL + "/auth";

export const loginService = async (username: string, password: string) => {
  const login_url = API_URL + "/login";
  const res = await axios.post(login_url, { username, password });
  return res.data;
};

export const signupService = async (
  username: string,
  password: string,
  email: string
) => {
  const signup_url = API_URL + "/signup";
  const res = await axios.post(signup_url, {
    username,
    password,
    email,
  });
  return res.data as User;
};
