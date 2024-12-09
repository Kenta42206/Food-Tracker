import React, { useState } from "react";
import { useAuth } from "../../context/AuthContext";
import { useNavigate } from "react-router-dom";

const LoginForm: React.FC = () => {
  const { login } = useAuth(); // AuthContextからloginメソッドを取得
  const [username, setUsername] = useState<string>("");
  const [password, setPassword] = useState<string>("");
  const [error, setError] = useState<string | null>(null);
  const navigate = useNavigate();

  const handleLogin = async () => {
    if (!username || !password) {
      setError("ユーザー名とパスワードを入力してください。");
      return; // 入力がない場合は処理を中止
    }

    try {
      await login(username, password); // loginメソッドを呼び出す
      // ログイン成功後の処理（リダイレクトなど）
      setError(null);
      navigate("/meals"); // タスク一覧ページにリダイレクト
    } catch (err) {
      setError(`ログインに失敗しました ${err}`); // エラーメッセージの設定
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

        <div className="p-6 pt-0">
          <button
            onClick={handleLogin}
            className="w-full rounded-md bg-slate-800 py-2 px-4 border border-transparent text-center text-sm text-white transition-all shadow-md hover:shadow-lg focus:bg-slate-700 focus:shadow-none active:bg-slate-700 hover:bg-slate-700 active:shadow-none disabled:pointer-events-none disabled:opacity-50 disabled:shadow-none"
            type="button"
          >
            Sign In
          </button>
          <p className="flex justify-center mt-6 text-sm text-slate-600">
            Don&apos;t have an account?
            <a
              onClick={() => {
                navigate("/signup");
              }}
              className="ml-1 text-sm font-semibold text-slate-700 underline"
            >
              Sign up
            </a>
          </p>
        </div>
        {error && <p>{error}</p>}
      </div>
    </div>
  );
};

export default LoginForm;
