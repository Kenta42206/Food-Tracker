import "./App.css";
import "./index.css";
import "./CustomCalender.css";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import SignupPage from "./pages/SignupPage";
import LoginPage from "./pages/LoginPage";
import ReportPage from "./pages/ReportPage";
import MealPage from "./pages/MealPage";
import SettingPage from "./pages/SettingPage";
import Dashboard from "./components/layout/Dashboard";
import PrivateRoute from "./components/auth/PrivateRoute";
import { AuthProvider } from "./context/AuthContext";
import { MealProvider } from "./context/MealContext";

function App() {
  return (
    <AuthProvider>
      <MealProvider>
        <Router>
          <Routes>
            <Route path="/login" element={<LoginPage />} />
            <Route path="/signup" element={<SignupPage />} />
            <Route path="/" element={<PrivateRoute element={<Dashboard />} />}>
              <Route path="reports" element={<ReportPage />} />
              <Route path="meals" element={<MealPage />} />
              <Route path="settings" element={<SettingPage />} />
            </Route>
          </Routes>
        </Router>
      </MealProvider>
    </AuthProvider>
  );
}

export default App;
