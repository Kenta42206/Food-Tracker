import React, { useState } from "react";
import { useMeal } from "../../context/MealContext";
import { MealRequestProps } from "../../types/Meal";

const MealCreateComponent = () => {
  const {
    rows,
    selectedDate,
    selectedMealNumber,
    selectedMealNumsConsumedTime,
    setRows,
    setSelectedMealNumber,
    setSelectedMealNumsConsumedTime,
    createMealhistories,
  } = useMeal();

  const handleInputChange = (id: number, field: string, value: string) => {
    setRows((prevRows) =>
      prevRows.map((row) =>
        row.foodId === id ? { ...row, [field]: value } : row
      )
    );
  };

  const handleRemoveRow = (id: number) => {
    setRows((prevRows) =>
      prevRows.map((row) =>
        row.foodId === id ? { ...row, deleteFlg: !row.deleteFlg } : row
      )
    );
  };

  const handleCreateButtonClick = () => {
    const newMeals: MealRequestProps[] = rows.map((row) => ({
      id: row.id,
      foodId: row.foodId,
      mealNumber: selectedMealNumber,
      quantity: parseInt(row.quantity),
      consumedAt: `${selectedDate}T${selectedMealNumsConsumedTime}:00`,
      deleteFlg: row.deleteFlg,
    }));

    createMealhistories(newMeals);
    setRows([]);
    setSelectedMealNumber(0);
  };

  console.log(rows);

  return (
    <div className=" p-2">
      <div>
        <h2 className="text-xl font-semibold ml-1 mb-2">
          {selectedDate}: {selectedMealNumber != 0 ? selectedMealNumber : "-"}
          食目
        </h2>
      </div>

      {rows.length == 0 ? (
        <>
          <div>
            <p className="text-slate-500">
              右の検索からFoodを検索し、Addボタンで追加してください！
            </p>
          </div>
        </>
      ) : (
        <>
          <div>
            <h2 className="text-xl font-semibold ml-1 mb-2">
              食事時間:{" "}
              <input
                type="time"
                value={selectedMealNumsConsumedTime}
                onChange={(e) =>
                  setSelectedMealNumsConsumedTime(e.target.value)
                }
                required
              />
            </h2>
          </div>
          <div className="space-y-4">
            {rows.map((row) => (
              <div
                key={row.id}
                className={`space-y-2 ${row.deleteFlg ? "opacity-50" : ""}`}
              >
                <div className="flex space-x-4 items-center">
                  <input
                    type="text"
                    value={row.foodName}
                    readOnly
                    onChange={(e) =>
                      handleInputChange(row.foodId, "foodName", e.target.value)
                    }
                    placeholder="Food Name"
                    className="w-full border border-gray-300 rounded px-2 py-1 focus:outline-none focus:ring focus:ring-blue-300"
                  />
                  <input
                    type="text"
                    pattern="^[+-]?([1-9][0-9]*|0)$"
                    value={row.quantity}
                    onChange={(e) => {
                      const newValue = e.target.value;
                      if (/^-?\d*$/.test(newValue)) {
                        handleInputChange(
                          row.foodId,
                          "quantity",
                          e.target.value
                        );
                      }
                    }}
                    placeholder="Quantity (g)"
                    className={`w-24 border border-gray-300 rounded px-2 py-1 focus:outline-none focus:ring ${
                      row.deleteFlg ? "line-through" : ""
                    }`}
                    disabled={row.deleteFlg}
                    required
                  />
                  <button
                    onClick={() => handleRemoveRow(row.foodId)}
                    className="bg-red-500 text-white px-2 py-1 rounded hover:bg-red-600"
                  >
                    －
                  </button>
                </div>
              </div>
            ))}
            <div className="mt-2 flex justify-center">
              <button onClick={handleCreateButtonClick}>作成</button>
            </div>
          </div>
        </>
      )}
    </div>
  );
};
export default MealCreateComponent;
