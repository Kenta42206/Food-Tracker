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
  getToday,
} from "../utils/DateUtil";
import { getFoodsByKeyword } from "../services/FoodService";
import { Food } from "../types/Food";
import { showToast } from "../utils/ToastUtil";
import { useAuth } from "./AuthContext";
import { getDailyReportService } from "../services/ReportService";

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
  totalNut: { [key: string]: number };
  getDailyReport: (date: string) => void;
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
  const { isAuthenticated } = useAuth();

  const getDailyMealhistoryList = async (date: string) => {
    try {
      const meals = await getDailyMealhistoryListService(date);
      setMealList(meals);
    } catch (e) {
      setMealList([]);
      showToast("error", `食事記録の取得に失敗しました。 ${e}`);
    }
  };

  const createMealhistories = async (mealhistories: MealRequestProps[]) => {
    try {
      const createdMeals = await createMealhistoriesService(mealhistories);

      if (createdMeals) {
        getDailyMealhistoryList(
          formatDateTimeStringToDateString(createdMeals[0].consumedAt)
        );
      }
      setSelectedMealNumsConsumedTime("");
      showToast("success", "食事記録を登録しました！");
    } catch (e) {
      showToast("error", `食事記録の登録に失敗しました。 ${e}`);
      console.log(e);
    }
  };

  const addFoodToMeal = (foodId: number, foodName: string) => {
    if (rows.some((food) => food.foodId === foodId)) {
      showToast("error", "すでに登録済みです。");
      return;
    }

    if (rows.length !== 0) {
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
    getDailyReport(formatDate(date));
  };

  const [totalResultPage, setTotalResultPage] = useState<number>(0);
  const [currentResultPage, setCurrentResultPage] = useState<number>(0);
  const [resultPage, setResultPage] = useState<Food[]>([]);

  const handleSeach = async (keyword: string, page: number) => {
    try {
      const result = await getFoodsByKeyword(keyword, page);
      setCurrentResultPage(result.currentPage);
      setTotalResultPage(result.totalPages);
      setResultPage(result.foods);
    } catch (e) {
      showToast("error", `検索に失敗しました。 ${e}`);
    }
  };

  const handleChangeCurrentPage = (page: number) => {
    setCurrentResultPage(page);
  };

  const [totalNut, setTotalNut] = useState<{ [key: string]: number }>({
    Protein: 0,
    Carbs: 0,
    Fat: 0,
  });

  const getDailyReport = async (date: string) => {
    try {
      const report = await getDailyReportService(date);
      setTotalNut({
        ...totalNut,
        Protein: report.totalProtein,
        Carbs: report.totalCarbs,
        Fat: report.totalFat,
      });
    } catch (e) {
      console.log(e);
    }
  };

  useEffect(() => {
    if (isAuthenticated) {
      getDailyMealhistoryList(selectedDate);
    }
  }, [isAuthenticated, selectedDate]);

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

    const formatedMeals = Array.from(nMap.values());
    setFormattedMeals(formatedMeals);
    setSelectedMealNumber(formatedMeals.length + 1);
  }, [mealList]);

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
        totalNut,
        getDailyReport,
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
