import axios from "axios";
import { Meal, MealRequestProps } from "../types/Meal";

const API_URL = process.env.REACT_APP_API_BASE_URL + "/mealhistory/";

export const getDailyMealhistoryListService = async (date: string) => {
  const token = localStorage.getItem("token");
  const url = API_URL + date;
  const res = await axios.get(url, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
  return res.data as Meal[];
};

export const createMealhistoriesService = async (
  mealhistories: MealRequestProps[]
) => {
  const token = localStorage.getItem("token");
  const url = API_URL + "batch";
  const res = await axios.post(url, mealhistories, {
    headers: {
      Authorization: `Bearer ${token}`,
      "Content-Type": "application/json",
    },
  });
  return res.data as Meal[];
};
