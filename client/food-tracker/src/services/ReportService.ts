import axios from "axios";

const API_URL = process.env.REACT_APP_API_BASE_URL + "/report";

export const getDailyReport = async (date: Date) => {
  const token = localStorage.getItem("token");
  const url = API_URL + date;
  const res = await axios.get(url, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
  return res.data;
};
