import React, { useState } from "react";
import Calendar from "react-calendar";
import "../../CustomCalender.css";
import "react-calendar/dist/Calendar.css";
import { useMeal } from "../../context/MealContext";

const CalenderComponent = () => {
  const { handleCalenderClick } = useMeal();

  return (
    <div className="w-full">
      <Calendar
        onClickDay={(e) => handleCalenderClick(e)}
        className="w-full h-full"
      />
    </div>
  );
};

export default CalenderComponent;
