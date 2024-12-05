import React from "react";
import CalenderComponent from "../components/meal/CalenderComponent";
import NutritionComponent from "../components/meal/NutritionComponent";
import MealNumberComponent from "../components/meal/MealNumberComponent";
import MealCreateComponent from "../components/meal/MealCreateComponent";
import { FoodSearchComponent } from "../components/meal/FoodSearchComponent";

const MealPage = () => {
  return (
    <div className="grid grid-cols-6 gap-6 ">
      <div className="col-span-2 border rounded bg-white">
        <CalenderComponent />
      </div>
      <div className="col-span-4  rounded bg-white">
        <NutritionComponent />
      </div>
      <div className="col-span-2  rounded bg-white">
        <MealNumberComponent />
      </div>
      <div className="col-span-2  rounded bg-white min-h-32">
        <MealCreateComponent />
      </div>
      <div className="col-span-2  rounded bg-white min-h-32">
        <FoodSearchComponent />
      </div>
    </div>
  );
};

export default MealPage;
