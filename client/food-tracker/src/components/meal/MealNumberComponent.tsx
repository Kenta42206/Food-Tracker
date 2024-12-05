import React from "react";
import { useMeal } from "../../context/MealContext";
import { formatTimeToHoursMinutes } from "../../utils/DateUtil";

const MealNumberComponent: React.FC = () => {
  const { formattedMeals, selectedDate } = useMeal();

  return (
    <div className="p-2">
      <div>
        <h2 className="text-xl font-semibold ml-1 mb-2">
          {selectedDate} の食事
        </h2>
      </div>
      {formattedMeals.map((mealGroup, i) => {
        // 合計値を初期化
        let totalProtein = 0;
        let totalCarbs = 0;
        let totalFat = 0;

        // 各mealGroup内でPFCを計算
        mealGroup.forEach((meal) => {
          totalProtein += meal.food.protein * (meal.quantity / 100);
          totalCarbs += meal.food.carbs * (meal.quantity / 100);
          totalFat += meal.food.fat * (meal.quantity / 100);
        });

        return (
          <div key={i} className="border px-4 pt-4">
            <div className="flex justify-between">
              <h3 className="font-semibold">Meal {i + 1}</h3>
              <p className="mt-2 text-gray-500">
                {formatTimeToHoursMinutes(mealGroup[0].consumedAt)}
              </p>
            </div>
            {mealGroup.map((meal, j) => (
              <div key={j} className="mt-2 flex justify-between items-center">
                <div className="flex space-x-2">
                  <p>{meal.food.name}</p>
                  <p>{meal.quantity}g</p>
                </div>
                <div className="flex space-x-3">
                  <p className="text-red-600">
                    P: {meal.food.protein * (meal.quantity / 100)}
                  </p>
                  <p className="text-blue-600">
                    F: {meal.food.fat * (meal.quantity / 100)}
                  </p>
                  <p className="text-yellow-600">
                    C: {meal.food.carbs * (meal.quantity / 100)}
                  </p>
                </div>
              </div>
            ))}

            {/* PFCの合計値を表示 */}
            <div className="flex justify-end space-x-3 items-center mt-2 border-t-2 py-2 text-gray-700">
              <p className="font-semibold">Total PFC</p>
              <p className="text-red-600">P: {totalProtein}g</p>
              <p className="text-blue-600">F: {totalFat}g</p>
              <p className="text-yellow-600">C: {totalCarbs}g</p>
            </div>
          </div>
        );
      })}
    </div>
  );
};

export default MealNumberComponent;
