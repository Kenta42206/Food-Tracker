import React, { useState } from "react";
import { useMeal } from "../context/MealContext";

const SearchComponent = () => {
  const { handleSeach, currentResultPage } = useMeal();
  const [keyword, setKeyword] = useState<string>("");

  const handleSearchKeywordChange = (
    e: React.ChangeEvent<HTMLInputElement>
  ) => {
    setKeyword(e.target.value);
  };

  return (
    <div className="flex items-center border-1 rounded-lg overflow-hidden ">
      <input
        type="search"
        onChange={handleSearchKeywordChange}
        placeholder="Search..."
        className={`px-4 py-1 border w-full border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 `}
      />
      <button onClick={() => handleSeach(keyword, currentResultPage)}>
        Search
      </button>
    </div>
  );
};

export default SearchComponent;
