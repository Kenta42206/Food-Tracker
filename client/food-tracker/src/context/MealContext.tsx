import {
  createContext,
  ReactNode,
  useContext,
  useEffect,
  useState,
} from "react";
import { Meal, MealRequestProps } from "../types/Meal";
import {
  createMealhistoriesService,
  getDailyMealhistoryListService,
} from "../services/MealService";
import {
  formatDate,
  formatDateTimeToDateString,
  getToday,
} from "../utils/DateUtil";
import { getFoodsByKeyword } from "../services/FoodService";
import { Food } from "../types/Food";

interface MealContextProps {
  mealList: Meal[];
  selectedDate: string;
  formattedMeals: Meal[][];
  totalResultPage: number;
  currentResultPage: number;
  resultPage: Food[];
  handleSeach: (keyword: string, page: number) => void;
  handleChangeCurrentPage: (page: number) => void;
  getDailyMealhistoryList: (date: string) => void;
  createMealhistories: (mealhistories: MealRequestProps[]) => void;
  handleCalenderClick: (date: Date) => void;
}

const MealContext = createContext<MealContextProps | null>(null);

export const MealProvider: React.FC<{ children: ReactNode }> = ({
  children,
}) => {
  const [mealList, setMealList] = useState<Meal[]>([]);
  const [formattedMeals, setFormattedMeals] = useState<Meal[][]>([]);
  const [selectedDate, setSelectedDate] = useState<string>(getToday);

  const getDailyMealhistoryList = async (date: string) => {
    try {
      const meals = await getDailyMealhistoryListService(date);
      setMealList(meals);
    } catch (e) {
      console.log(e);
      setMealList([]);
    }
  };

  const createMealhistories = async (mealhistories: MealRequestProps[]) => {
    try {
      const createdMeals = await createMealhistoriesService(mealhistories);
      // if (
      //   formatDateTimeToDateString(createdMeals[0].consumedAt) == selectedDate
      // ) {
      //   setMealList((prevMealList) => [...prevMealList, ...createdMeals]);
      // }
      console.log("Meals successfully created:", createdMeals);
    } catch (e) {
      console.log(e);
    }
  };

  const handleCalenderClick = (date: Date) => {
    setSelectedDate(formatDate(date));
  };

  const [totalResultPage, setTotalResultPage] = useState<number>(0);
  const [currentResultPage, setCurrentResultPage] = useState<number>(0);
  const [resultPage, setResultPage] = useState<Food[]>([]);

  const handleSeach = async (keyword: string, page: number) => {
    try {
      const result = await getFoodsByKeyword(keyword, page);
      setCurrentResultPage(result.currentPage);
      console.log(currentResultPage);
      setTotalResultPage(result.totalPages);
      setResultPage(result.foods);
    } catch (e) {
      console.log(e);
    }
  };

  const handleChangeCurrentPage = (page: number) => {
    setCurrentResultPage(page);
  };

  useEffect(() => {
    getDailyMealhistoryList(selectedDate);
  }, [selectedDate]);

  useEffect(() => {
    const nMap = new Map<number, Meal[]>();

    mealList.forEach((meal) => {
      if (nMap.has(meal.mealNumber)) {
        nMap.get(meal.mealNumber)?.push(meal);
      } else {
        nMap.set(meal.mealNumber, [meal]);
      }
    });

    const formatedMeals = Array.from(nMap.values());
    setFormattedMeals(formatedMeals);
  }, [mealList]);

  console.log(formattedMeals);

  return (
    <MealContext.Provider
      value={{
        mealList,
        selectedDate,
        formattedMeals,
        totalResultPage,
        currentResultPage,
        resultPage,
        handleSeach,
        handleChangeCurrentPage,
        getDailyMealhistoryList,
        createMealhistories,
        handleCalenderClick,
      }}
    >
      {children}
    </MealContext.Provider>
  );
};

export const useMeal = (): MealContextProps => {
  const context = useContext(MealContext);
  if (!context) {
    throw new Error("useMeal must be used within an AuthProvider");
  }
  return context;
};
