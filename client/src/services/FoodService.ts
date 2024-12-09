import axios from "axios";
import { FoodPage } from "../types/Food";

const API_URL = process.env.REACT_APP_API_BASE_URL + "/food";

export const getFoodsByKeyword = async (keyword: string, page: number) => {
  const token = localStorage.getItem("token");
  const url = `${API_URL}?name=${keyword}&page=${page}&size=10`;
  const res = await axios.get(url, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
  return res.data as FoodPage;
};
