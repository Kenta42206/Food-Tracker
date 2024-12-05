import { Divider } from "@blueprintjs/core";
import React from "react";
import RatioComponent from "./RatioComponent";

const NutritionComponent = () => {
  return (
    <>
      <h1>栄養</h1>
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
