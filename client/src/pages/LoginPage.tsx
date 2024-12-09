import React from "react";
import LoginForm from "../components/auth/LoginForm";

const LoginPage: React.FC = () => {
  return (
    <div className="h-screen flex justify-center items-center">
      <LoginForm />
    </div>
  );
};

export default LoginPage;
