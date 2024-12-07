import React from "react";
import { useAuth } from "../../context/AuthContext";
import { useReport } from "../../context/ReportContext";

interface RatioComponentProps {
  nutritionName: string;
}

const RatioComponent: React.FC<RatioComponentProps> = ({ nutritionName }) => {
  const { loginUser } = useAuth();
  const { totalNut } = useReport();

  const colorMap: Record<string, string> = {
    Protein: "text-red-500 dark:text-red-400",
    Carbs: "text-yellow-600 dark:text-yellow-500",
    Fat: "text-blue-500 dark:text-blue-400",
  };
  const nutMap: Record<string, string> = {
    Protein: loginUser?.goalProtein.toString() || "0",
    Carbs: loginUser?.goalCarbs.toString() || "0",
    Fat: loginUser?.goalFat.toString() || "0",
  };

  const ratioColor =
    colorMap[nutritionName] || "text-blue-600 dark:text-blue-500";

  const goalNum = nutMap[nutritionName];
  const totalNum = totalNut[nutritionName];

  // 表示する円の割合を求める
  const ratio = (totalNum / parseInt(goalNum)) * 100;
  const strokeDasharrayValue = ratio <= 100 ? (ratio / 100) * 75 : 75;

  return (
    <div className=" ">
      <div className="relative m-auto size-64">
        <svg
          className="rotate-[135deg] size-full"
          viewBox="0 0 36 36"
          xmlns="http://www.w3.org/2000/svg"
        >
          <circle
            cx="18"
            cy="18"
            r="16"
            fill="none"
            className="stroke-current text-gray-200 dark:text-neutral-700"
            stroke-width="1.5"
            stroke-dasharray="75 100"
            stroke-linecap="round"
          ></circle>

          <circle
            cx="18"
            cy="18"
            r="16"
            fill="none"
            className={`stroke-current ${ratioColor}`}
            stroke-width="1.5"
            strokeDasharray={`${strokeDasharrayValue} 100`}
            stroke-linecap="round"
          ></circle>
        </svg>

        <div className="absolute top-1/2 start-1/2 transform -translate-x-1/2 -translate-y-1/2 text-center">
          <span className={`text-3xl font-bold ${ratioColor}`}>
            {totalNum} / {goalNum}
          </span>
          <span className={`${ratioColor} block`}>{nutritionName}</span>
        </div>
      </div>
    </div>
  );
};

export default RatioComponent;
