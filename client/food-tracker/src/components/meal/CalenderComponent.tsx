import React, { useState } from "react";
import Calendar from "react-calendar";
import "../../CustomCalender.css";
import "react-calendar/dist/Calendar.css";
import { useMeal } from "../../context/MealContext";
import { formatDate } from "../../utils/DateUtil";

const CalenderComponent = () => {
  const { selectedDate, handleCalenderClick } = useMeal();

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
