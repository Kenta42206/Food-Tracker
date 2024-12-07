import { Divider } from "@blueprintjs/core";
import React from "react";
import RatioComponent from "./RatioComponent";
import { useReport } from "../../context/ReportContext";
import { useMeal } from "../../context/MealContext";
import { formatDateTimeStringToDateString } from "../../utils/DateUtil";

const NutritionComponent = () => {
  const { getDailyReport } = useReport();
  const { selectedDate } = useMeal();
  const handleRefreshButtonClick = () => {
    getDailyReport(formatDateTimeStringToDateString(selectedDate));
  };
  return (
    <>
      <p onClick={handleRefreshButtonClick}>refresh</p>
      <div className="grid grid-cols-3 gap-6">
        {/* ratio */}
        <div className="">
          <RatioComponent nutritionName="Protein" />
        </div>
        {/* end ratio */}
        {/* ratio */}
        <div className="">
          <RatioComponent nutritionName="Carbs" />
        </div>
        {/* end ratio */}
        {/* ratio */}
        <div className="">
          <RatioComponent nutritionName="Fat" />
        </div>
        {/* end ratio */}
      </div>
    </>
  );
};

export default NutritionComponent;
