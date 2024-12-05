import React from "react";

interface RatioComponentProps {
  nutritionName: string;
}

const RatioComponent: React.FC<RatioComponentProps> = ({ nutritionName }) => {
  const colorMap: Record<string, string> = {
    Protein: "text-red-500 dark:text-red-400",
    Carbs: "text-yellow-600 dark:text-yellow-500",
    Fat: "text-blue-500 dark:text-blue-400",
  };

  const ratioColor =
    colorMap[nutritionName] || "text-blue-600 dark:text-blue-500";
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
            stroke-dasharray="37.5 100"
            stroke-linecap="round"
          ></circle>
        </svg>

        <div className="absolute top-1/2 start-1/2 transform -translate-x-1/2 -translate-y-1/2 text-center">
          <span className={`text-3xl font-bold ${ratioColor}`}>50 / 100</span>
          <span className={`${ratioColor} block`}>{nutritionName}</span>
        </div>
      </div>
    </div>
  );
};

export default RatioComponent;
