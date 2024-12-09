import React, { useEffect, useState } from "react";
import { useMeal } from "../../context/MealContext";
import { Pagination, Table } from "flowbite-react";

export const FoodSearchComponent = () => {
  const {
    totalResultPage,

    resultPage,
    addFoodToMeal,
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

      <div className="mt-3">
        <Table hoverable>
          <Table.Head>
            <Table.HeadCell>Name</Table.HeadCell>
            <Table.HeadCell>P</Table.HeadCell>
            <Table.HeadCell>F</Table.HeadCell>
            <Table.HeadCell>C</Table.HeadCell>
            <Table.HeadCell>
              <span className="sr-only">Edit</span>
            </Table.HeadCell>
          </Table.Head>

          {resultPage.map((food) => (
            <Table.Body className="divide-y">
              <Table.Row className="bg-white dark:border-gray-700 dark:bg-gray-800">
                <Table.Cell className="whitespace-nowrap font-medium text-gray-900 dark:text-white">
                  {food.name.length > 13
                    ? food.name.substring(0, 12) + "..."
                    : food.name}
                </Table.Cell>
                <Table.Cell>{food.protein}</Table.Cell>
                <Table.Cell>{food.fat}</Table.Cell>
                <Table.Cell>{food.carbs}</Table.Cell>
                <Table.Cell>
                  <p
                    onClick={() => addFoodToMeal(food.id, food.name)}
                    className="font-medium text-cyan-600 hover:underline dark:text-cyan-500"
                  >
                    Add
                  </p>
                </Table.Cell>
              </Table.Row>
            </Table.Body>
          ))}
        </Table>
      </div>

      <div className="flex overflow-x-auto sm:justify-center">
        <Pagination
          currentPage={currentActivePage}
          totalPages={totalResultPage}
          onPageChange={onPageChange}
          showIcons
        />
      </div>
    </div>
  );
};
