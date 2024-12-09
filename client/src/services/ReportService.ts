import axios from "axios";
import { Report } from "../types/Report";

const API_URL = process.env.REACT_APP_API_BASE_URL + "/report/";

export const getDailyReportService = async (date: string) => {
  const token = localStorage.getItem("token");
  const url = API_URL + date;
  const res = await axios.get(url, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
  return res.data as Report;
};
