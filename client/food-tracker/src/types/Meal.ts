import { Food } from "./Food";

export interface Meal {
  id: number;
  food: Food;
  quantity: number;
  mealNumber: number;
  consumedAt: string;
  deleteFlg: boolean;
}

export interface MealRequestProps {
  id?: number;
  foodId: number;
  mealNumber: number;
  quantity: number;
  consumedAt: string;
  deleteFlg: boolean;
}
