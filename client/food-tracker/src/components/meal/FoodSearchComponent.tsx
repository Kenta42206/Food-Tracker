import React, { useEffect, useState } from "react";

import { useMeal } from "../../context/MealContext";
import { Pagination } from "flowbite-react";
import { ArrowRightIcon, ArrowLeftIcon } from "@heroicons/react/24/outline";
import { Button } from "@blueprintjs/core";

export const FoodSearchComponent = () => {
  const {
    totalResultPage,
    currentResultPage,
    resultPage,
    handleChangeCurrentPage,
    handleSeach,
  } = useMeal();
  const [currentActivePage, setCurrentActivePage] = useState<number>(1);
  const [keyword, setKeyword] = useState<string>("");

  const handleSearchKeywordChange = (
    e: React.ChangeEvent<HTMLInputElement>
  ) => {
    setKeyword(e.target.value);
  };

  const handleSearchButtonClick = () => {
    handleChangeCurrentPage(0);
    setCurrentActivePage(1);
    handleSeach(keyword, 0);
  };

  const onPageChange = (page: number) => setCurrentActivePage(page);

  useEffect(() => {
    handleSeach(keyword, currentActivePage - 1);
  }, [currentActivePage]);

  return (
    <div className="p-2">
      <div className="flex items-center border-1 rounded-lg overflow-hidden ">
        <input
          type="search"
          onChange={handleSearchKeywordChange}
          placeholder="Search..."
          className={`px-4 py-1 border w-full border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 `}
        />
        <button onClick={handleSearchButtonClick}>Search</button>
      </div>

      <div>
        {resultPage.map((food) => (
          <div>
            <p>{food.id}</p>
            <p>{food.name}</p>
          </div>
        ))}
      </div>

      <div className="flex overflow-x-auto sm:justify-center">
        <Pagination
          currentPage={currentActivePage}
          totalPages={totalResultPage}
          onPageChange={onPageChange}
          className="flex"
        />
      </div>
    </div>
  );
};
