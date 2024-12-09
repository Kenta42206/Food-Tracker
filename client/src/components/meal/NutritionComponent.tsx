import RatioComponent from "./RatioComponent";
import { useMeal } from "../../context/MealContext";

const NutritionComponent = () => {
  const { selectedDate } = useMeal();

  return (
    <div className="p-2">
      <div>
        <h2 className="text-xl font-semibold ml-1 mb-2">
          {selectedDate} のPFC
        </h2>
      </div>
      <div className="grid grid-cols-3 gap-6">
        {/* ratio */}
        <div className="">
          <RatioComponent nutritionName="Protein" />
        </div>
        {/* end ratio */}
        {/* ratio */}
        <div className="">
          <RatioComponent nutritionName="Carbs" />
        </div>
        {/* end ratio */}
        {/* ratio */}
        <div className="">
          <RatioComponent nutritionName="Fat" />
        </div>
        {/* end ratio */}
      </div>
    </div>
  );
};

export default NutritionComponent;
