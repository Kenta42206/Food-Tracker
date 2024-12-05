export interface Food {
  id: number;
  name: string;
  protein: number;
  fat: number;
  carbs: number;
  calories: number;
}

export interface FoodPage {
  totalItems: number;
  foods: Food[];
  totalPages: number;
  currentPage: number;
}
