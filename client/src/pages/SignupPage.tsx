import React from "react";
import SignupForm from "../components/auth/SignupForm";

const SignupPage: React.FC = () => {
  return (
    <div className="h-screen flex justify-center items-center">
      <SignupForm />
    </div>
  );
};

export default SignupPage;
