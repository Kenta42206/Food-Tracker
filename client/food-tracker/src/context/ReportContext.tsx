import { createContext, ReactNode, useContext, useState } from "react";
import { getDailyReportService } from "../services/ReportService";

interface ReportContextProps {
  totalNut: { [key: string]: number };
  getDailyReport: (date: string) => void;
}

const ReportContext = createContext<ReportContextProps | null>(null);

export const ReportProvider: React.FC<{ children: ReactNode }> = ({
  children,
}) => {
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

  return (
    <ReportContext.Provider value={{ totalNut, getDailyReport }}>
      {children}
    </ReportContext.Provider>
  );
};

export const useReport = (): ReportContextProps => {
  const context = useContext(ReportContext);
  if (!context) {
    throw new Error("useReport must be used within an AuthProvider");
  }
  return context;
};
