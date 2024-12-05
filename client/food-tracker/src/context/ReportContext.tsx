import { createContext, ReactNode, useContext } from "react";

interface ReportContextProps {}

const ReportContext = createContext<ReportContextProps | null>(null);

export const MealProvider: React.FC<{ children: ReactNode }> = ({
  children,
}) => {
  return <ReportContext.Provider value={{}}>{children}</ReportContext.Provider>;
};

export const useReport = (): ReportContextProps => {
  const context = useContext(ReportContext);
  if (!context) {
    throw new Error("useReport must be used within an AuthProvider");
  }
  return context;
};
