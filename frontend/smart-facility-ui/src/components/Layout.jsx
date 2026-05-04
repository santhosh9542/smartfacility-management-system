import React from "react";
import Sidebar from "./Sidebar";
import UserMenu from "./UserMenu";

function Layout({ children }) {
  return (
    <div className="layout">

      <Sidebar />

      <div className="main-content">

        <div className="top-navbar">

          <div className="header-left">
            <h2 className="top-title">
              SmartFacility
            </h2>

            <input
              className="header-search"
              placeholder="Search..."
            />
          </div>

          <div className="header-right">

            <div className="notify-bell">
              🔔
            </div>

            <UserMenu />

          </div>

        </div>

        <div className="page-body">
          {children}
        </div>

      </div>

    </div>
  );
}

export default Layout;