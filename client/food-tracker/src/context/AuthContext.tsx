import { createContext, ReactNode, useContext, useState } from "react";
import { User } from "../types/User";
import { loginService, signupService } from "../services/AuthService";

interface AuthContextProps {
  isAuthenticated: boolean;
  loginUser: User | null;
  login: (username: string, password: string) => Promise<void>;
  signup: (username: string, password: string, email: string) => Promise<void>;
  logout: () => void;
}

const AuthContext = createContext<AuthContextProps | null>(null);

export const AuthProvider: React.FC<{ children: ReactNode }> = ({
  children,
}) => {
  const [isAuthenticated, setIsAuthenticated] = useState<boolean>(false);
  const [loginUser, setLoginUser] = useState<User | null>(null);

  const login = async (username: string, password: string) => {
    try {
      const data = await loginService(username, password);
      localStorage.setItem("token", data.token);
      setLoginUser(data.user);
      setIsAuthenticated(true);
    } catch (err) {
      console.log(err);
      setLoginUser(null);
      setIsAuthenticated(false);
    }
  };

  const signup = async (username: string, password: string, email: string) => {
    try {
      const data = await signupService(username, password, email);
      await login(username, password);
    } catch (err) {
      console.log(err);
      setLoginUser(null);
      setIsAuthenticated(false);
    }
  };

  const logout = () => {
    localStorage.removeItem("token");
    setLoginUser(null);
    setIsAuthenticated(false);
  };

  return (
    <AuthContext.Provider
      value={{ isAuthenticated, loginUser, login, signup, logout }}
    >
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = (): AuthContextProps => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error("useAuth must be used within an AuthProvider");
  }
  return context;
};
