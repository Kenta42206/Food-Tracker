import { Food } from "./Food";

export interface Meal {
  id: number;
  food: Food;
  quantity: number;
  mealNumber: number;
  consumedAt: string;
}

export interface MealRequestProps {
  food: Food;
  quantity: number;
  mealNumber: number;
  consumedAt: string;
}
