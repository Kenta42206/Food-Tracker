import React, { useState } from "react";
import { useAuth } from "../../context/AuthContext";
import { useNavigate } from "react-router-dom";

const SignupForm: React.FC = () => {
  const { signup } = useAuth();
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [email, setEmail] = useState("");
  const [error, setError] = useState<string | null>(null);
  const navigate = useNavigate();

  const handleSignup = async () => {
    try {
      await signup(username, password, email);
      navigate("/meals"); // サインアップ後にタスクページへリダイレクト
    } catch (err) {
      setError("サインアップに失敗しました。再度お試しください。");
    }
  };

  return (
    <div className="relative flex flex-col bg-white shadow-sm border border-slate-200 w-96 rounded-lg my-6">
      <div className="relative m-2.5 items-center flex justify-center text-white h-24 rounded-md bg-slate-800">
        <h3 className="text-2xl">Sign In</h3>
      </div>
      <div className="flex flex-col gap-4 p-6">
        <div className="w-full max-w-sm min-w-[200px]">
          <label className="block mb-2 text-sm text-slate-600">Username</label>
          <input
            type="text"
            placeholder="ユーザー名"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            className="w-full bg-transparent placeholder:text-slate-400 text-slate-700 text-sm border border-slate-200 rounded-md px-3 py-2 transition duration-300 ease focus:outline-none focus:border-slate-400 hover:border-slate-300 shadow-sm focus:shadow"
          />
        </div>

        <div className="w-full max-w-sm min-w-[200px]">
          <label className="block mb-2 text-sm text-slate-600">Password</label>
          <input
            type="password"
            placeholder="パスワード"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            className="w-full bg-transparent placeholder:text-slate-400 text-slate-700 text-sm border border-slate-200 rounded-md px-3 py-2 transition duration-300 ease focus:outline-none focus:border-slate-400 hover:border-slate-300 shadow-sm focus:shadow"
          />
        </div>

        <div className="w-full max-w-sm min-w-[200px]">
          <label className="block mb-2 text-sm text-slate-600">Password</label>
          <input
            type="email"
            placeholder="メールアドレス"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            className="w-full bg-transparent placeholder:text-slate-400 text-slate-700 text-sm border border-slate-200 rounded-md px-3 py-2 transition duration-300 ease focus:outline-none focus:border-slate-400 hover:border-slate-300 shadow-sm focus:shadow"
          />
        </div>

        <div className="p-6 pt-0">
          <button
            onClick={handleSignup}
            className="w-full rounded-md bg-slate-800 py-2 px-4 border border-transparent text-center text-sm text-white transition-all shadow-md hover:shadow-lg focus:bg-slate-700 focus:shadow-none active:bg-slate-700 hover:bg-slate-700 active:shadow-none disabled:pointer-events-none disabled:opacity-50 disabled:shadow-none"
            type="button"
          >
            Sign In
          </button>
          <p className="flex justify-center mt-6 text-sm text-slate-600">
            Do you have an account?
            <a
              onClick={() => {
                navigate("/login");
              }}
              className="ml-1 text-sm font-semibold text-slate-700 underline"
            >
              Log in
            </a>
          </p>
        </div>
        {error && <p>{error}</p>}
      </div>
    </div>
  );
};

export default SignupForm;
