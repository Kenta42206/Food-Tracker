import React, { useState } from "react";

const MealCreateComponent = () => {
  const [rows, setRows] = useState([
    { id: Date.now(), foodName: "", quantity: "" },
  ]);

  const handleAddRow = () => {
    setRows([...rows, { id: Date.now(), foodName: "", quantity: "" }]);
  };

  const handleInputChange = (id: number, field: string, value: string) => {
    setRows((prevRows) =>
      prevRows.map((row) => (row.id === id ? { ...row, [field]: value } : row))
    );
  };

  const handleRemoveRow = (id: number) => {
    if (rows.length > 1) {
      setRows((prevRows) => prevRows.filter((row) => row.id !== id));
    }
  };

  console.log(rows.length);

  return (
    <div className="space-y-4 p-2">
      {rows.map((row, index) => (
        <div key={row.id} className="space-y-2">
          <div className="flex space-x-4 items-center">
            <input
              type="text"
              value={row.foodName}
              onChange={(e) =>
                handleInputChange(row.id, "foodName", e.target.value)
              }
              placeholder="Food Name"
              className="w-full border border-gray-300 rounded px-2 py-1 focus:outline-none focus:ring focus:ring-blue-300"
            />
            <input
              type="number"
              value={row.quantity}
              onChange={(e) =>
                handleInputChange(row.id, "quantity", e.target.value)
              }
              placeholder="Quantity (g)"
              className="w-24 border border-gray-300 rounded px-2 py-1 focus:outline-none focus:ring focus:ring-blue-300"
            />
            <button
              onClick={() => handleRemoveRow(row.id)}
              className="bg-red-500 text-white px-2 py-1 rounded hover:bg-red-600"
            >
              －
            </button>
          </div>

          {index === rows.length - 1 && (
            <div className="flex justify-center">
              <button
                onClick={handleAddRow}
                className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600"
              >
                ＋
              </button>
            </div>
          )}
        </div>
      ))}
    </div>
  );
};

export default MealCreateComponent;
