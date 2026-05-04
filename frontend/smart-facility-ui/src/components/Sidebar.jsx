import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../services/api";

function Sidebar() {

  const navigate = useNavigate();
  const [menus, setMenus] = useState([]);

  const token = localStorage.getItem("token");

  let role = "";
  let payload = {};

  if (token) {
    payload = JSON.parse(atob(token.split(".")[1]));
    role = payload.role;
  }

  useEffect(() => {
    loadMenus();
  }, []);

  const loadMenus = async () => {

    try {

      // SUPER ADMIN
      if (role === "SUPER_ADMIN") {

        setMenus([
          { menuName: "Dashboard" },
          { menuName: "Tenants" },
          { menuName: "Billing" },
          { menuName: "Reports" },
          { menuName: "Support" },
          { menuName: "Permissions" },
          { menuName: "Profile" }
        ]);

        return;
      }

      // ADMIN
      if (role === "ADMIN") {

        const res =
          await api.get("/permissions/role/ADMIN");

        setMenus(
          res.data.filter(x => x.isEnabled)
        );

        return;
      }

      // TENANT
      if (role === "TENANT") {

        const orgId = payload.organizationId;

        const res =
          await api.get(
            "/permissions/effective/" +
            orgId +
            "/TENANT"
          );

        setMenus(
          res.data.filter(x => x.isEnabled)
        );
      }

    } catch (error) {
      console.log(error);
      loadDefaultMenus();
    }
  };

  const loadDefaultMenus = () => {

    if (role === "SUPER_ADMIN") {

      setMenus([
        { menuName: "Dashboard" },
        { menuName: "Tenants" },
        { menuName: "Billing" },
        { menuName: "Reports" },
        { menuName: "Support" },
        { menuName: "Permissions" },
        { menuName: "Profile" }
      ]);

    } else if (role === "ADMIN") {

      setMenus([
        { menuName: "Dashboard" },
        { menuName: "Tenants" },
        { menuName: "Billing" },
        { menuName: "Reports" },
        { menuName: "Support" },
        { menuName: "Tenant Permissions" },
        { menuName: "Profile" }
      ]);

    } else {

      setMenus([
        { menuName: "Dashboard" },
        { menuName: "Billing" },
        { menuName: "Profile" },
        { menuName: "Support" }
      ]);
    }
  };

  const goMenu = (menu) => {

    if (menu === "Dashboard")
      navigate(role === "TENANT"
        ? "/user-dashboard"
        : "/dashboard");

    if (menu === "Tenants")
      navigate("/tenants");

    if (menu === "Billing")
      navigate("/billing");

    if (menu === "Reports")
      navigate("/reports");

    if (menu === "Support")
      navigate("/support");

    if (menu === "Profile")
      navigate("/profile");

    if (menu === "Permissions")
      navigate("/permissions");

    if (menu === "Tenant Permissions")
      navigate("/tenant-permissions");
  };

  const logout = () => {
    localStorage.removeItem("token");
    navigate("/");
  };

  return (
    <div className="sidebar">

      <h2>🏢 SmartFacility</h2>

      <ul>

        {menus.map((m, i) => (
          <li key={i}
            onClick={() => goMenu(m.menuName)}>
            {m.menuName}
          </li>
        ))}

        <li onClick={logout}>
          Logout
        </li>

      </ul>

    </div>
  );
}

export default Sidebar;