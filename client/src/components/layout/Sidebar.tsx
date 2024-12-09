import React, { useEffect, useRef, useState } from "react";
import { NavLink, useNavigate } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";
import { Menu, MenuItem } from "@blueprintjs/core";

const Sidebar = () => {
  const navigate = useNavigate();
  const { loginUser, logout } = useAuth();
  const [isUserMenuOpen, setIsUserMenuOpen] = useState<boolean>(false);
  const userMenuRef = useRef<HTMLDivElement>(null);

  const handleLogout = () => {
    logout();
    navigate("/login");
  };

  const toggleUserMenu = () => {
    setIsUserMenuOpen((prev) => !prev);
  };

  useEffect(() => {
    const handleClickOutside = (e: MouseEvent) => {
      if (
        userMenuRef.current &&
        !userMenuRef.current.contains(e.target as Node)
      ) {
        setIsUserMenuOpen(false);
      }
    };

    document.addEventListener("mousedown", handleClickOutside);
    return () => {
      document.removeEventListener("mousedown", handleClickOutside);
    };
  }, []);

  return (
    <div className="flex flex-col h-full w-64 bg-white text-slate-600 p-4 overflow-y-auto">
      <h2 className="text-2xl font-bold mb-6">Food Tracker</h2>
      <nav className="flex flex-col space-y-2 ">
        <NavLink
          to="/reports"
          className={({ isActive }) =>
            `px-3 py-2 rounded hover:bg-slate-100 transition-all duration-75 ${
              isActive ? "bg-slate-100" : ""
            }`
          }
        >
          ダッシュボード
        </NavLink>
        <NavLink
          to="/meals"
          className={({ isActive }) =>
            `px-3 py-2 rounded hover:bg-slate-100 transition-all duration-75 ${
              isActive ? "bg-slate-100" : ""
            }`
          }
        >
          食事管理
        </NavLink>
        <NavLink
          to="/settings"
          className={({ isActive }) =>
            `px-3 py-2 rounded hover:bg-slate-100 transition-all duration-75 ${
              isActive ? "bg-slate-100" : ""
            }`
          }
        >
          設定
        </NavLink>
      </nav>
      <div className="mt-auto" ref={userMenuRef}>
        {isUserMenuOpen && (
          <div className="absolute w-60 bottom-20 left-2 border-2 bg-white rounded p-4 z-50 transition-all duration-75">
            <ul className="flex flex-col space-y-2">
              <li className="cursor-pointer px-4 py-2 rounded hover:bg-slate-100 text-gray-800">
                <NavLink to="/userInfo">ユーザー情報</NavLink>
              </li>
              <li
                className="cursor-pointer px-4 py-2 rounded hover:bg-slate-100 text-red-600"
                onClick={handleLogout}
              >
                ログアウト
              </li>
            </ul>
          </div>
        )}
        <div
          onClick={toggleUserMenu}
          className=" flex items-center space-x-4 p-2 rounded hover:bg-slate-100 transition-all duration-75"
        >
          <div>
            <p className="text-lg font-semibold">{loginUser?.username}</p>
            <p className="text-sm text-gray-400">{loginUser?.email}</p>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Sidebar;
