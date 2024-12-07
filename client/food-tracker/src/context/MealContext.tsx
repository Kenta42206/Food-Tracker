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
  formatDateTimeStringToDateString,
  formatDateTimeToDateString,
  getToday,
} from "../utils/DateUtil";
import { getFoodsByKeyword } from "../services/FoodService";
import { Food } from "../types/Food";
import { showToast } from "../utils/ToastUtil";

interface MealContextProps {
  mealList: Meal[];
  selectedDate: string;
  rows: Row[];
  selectedMealNumber: number;
  selectedMealNumsConsumedTime: string;
  formattedMeals: Meal[][];
  totalResultPage: number;
  currentResultPage: number;
  resultPage: Food[];
  setRows: React.Dispatch<React.SetStateAction<Row[]>>;
  setSelectedMealNumber: React.Dispatch<React.SetStateAction<number>>;
  setSelectedMealNumsConsumedTime: React.Dispatch<React.SetStateAction<string>>;
  addFoodToMeal: (foodId: number, foodName: string) => void;
  handleSeach: (keyword: string, page: number) => void;
  handleChangeCurrentPage: (page: number) => void;
  getDailyMealhistoryList: (date: string) => void;
  createMealhistories: (mealhistories: MealRequestProps[]) => void;
  handleCalenderClick: (date: Date) => void;
}

export interface Row {
  id?: number;
  foodId: number;
  foodName: string;
  quantity: string;
  deleteFlg: boolean;
}

const MealContext = createContext<MealContextProps | null>(null);

export const MealProvider: React.FC<{ children: ReactNode }> = ({
  children,
}) => {
  const [mealList, setMealList] = useState<Meal[]>([]);
  const [formattedMeals, setFormattedMeals] = useState<Meal[][]>([]);
  const [selectedDate, setSelectedDate] = useState<string>(getToday);
  const [rows, setRows] = useState<Row[]>([]);
  const [selectedMealNumber, setSelectedMealNumber] = useState<number>(0);
  const [selectedMealNumsConsumedTime, setSelectedMealNumsConsumedTime] =
    useState<string>("");

  const getDailyMealhistoryList = async (date: string) => {
    try {
      const meals = await getDailyMealhistoryListService(date);
      setMealList(meals);
    } catch (e) {
      console.log(e);
      setMealList([]);
      showToast("error", "failed to get mealhitories");
    }
  };

  const createMealhistories = async (mealhistories: MealRequestProps[]) => {
    try {
      const createdMeals = await createMealhistoriesService(mealhistories);
      console.log("Meals successfully created:", createdMeals);
      if (createdMeals) {
        getDailyMealhistoryList(
          formatDateTimeStringToDateString(createdMeals[0].consumedAt)
        );
      }
      setSelectedMealNumsConsumedTime("");
      showToast("success", "New mealhistory is successfully saved");
    } catch (e) {
      showToast("error", "failed to create mealhitories");
      console.log(e);
    }
  };

  const addFoodToMeal = (foodId: number, foodName: string) => {
    if (rows.some((food) => food.foodId === foodId)) {
      console.log("すでに登録済みです。");
      return;
    } else {
      console.log("登録されていないです。");
      console.log(foodId);
    }
    if (rows.length != 0) {
      setRows((prevRows) => [
        ...prevRows,
        { foodId, foodName, quantity: "", deleteFlg: false },
      ]);
    } else {
      setRows([{ foodId, foodName, quantity: "", deleteFlg: false }]);
      setSelectedMealNumsConsumedTime("");
    }
  };

  const handleCalenderClick = (date: Date) => {
    setSelectedDate(formatDate(date));
    setRows([]);
    setSelectedMealNumber(0);
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

  // 選択した日にちの食事を～食目ごとに成型する
  useEffect(() => {
    const nMap = new Map<number, Meal[]>();

    mealList.forEach((meal) => {
      if (nMap.has(meal.mealNumber)) {
        nMap.get(meal.mealNumber)?.push(meal);
      } else {
        nMap.set(meal.mealNumber, [meal]);
      }
    });

    console.log(nMap);

    const formatedMeals = Array.from(nMap.values());
    setFormattedMeals(formatedMeals);
    setSelectedMealNumber(formatedMeals.length + 1);
  }, [mealList]);

  console.log(formattedMeals);

  return (
    <MealContext.Provider
      value={{
        mealList,
        selectedDate,
        rows,
        selectedMealNumber,
        selectedMealNumsConsumedTime,
        formattedMeals,
        totalResultPage,
        currentResultPage,
        resultPage,
        setRows,
        setSelectedMealNumber,
        setSelectedMealNumsConsumedTime,
        addFoodToMeal,
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
