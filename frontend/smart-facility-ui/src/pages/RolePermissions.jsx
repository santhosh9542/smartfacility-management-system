import React, { useEffect, useState } from "react";
import api from "../services/api";
import Layout from "../components/Layout";

function RolePermissions() {

  const [data, setData] = useState([]);

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    const res = await api.get("/permissions");
    setData(res.data);
  };

  const menus = [...new Set(data.map(x => x.menuName))];

  const getValue = (menu, role) => {
    return data.find(
      x => x.menuName === menu && x.roleName === role
    );
  };

  const updatePermission = async (item) => {
    await api.put("/permissions/" + item.id, {
      isEnabled: !item.isEnabled
    });

    loadData();
  };

  return (
    <Layout>

      <div className="topbar">
        <h1>Permission Matrix</h1>
      </div>

      <div className="table-box">

        <table>
          <thead>
            <tr>
              <th>Menu</th>
              <th>ADMIN</th>
              <th>TENANT</th>
            </tr>
          </thead>

          <tbody>

            {menus.map(menu => {

              const admin =
                getValue(menu, "ADMIN");

              const tenant =
                getValue(menu, "TENANT");

              return (
                <tr key={menu}>
                  <td>{menu}</td>

                  <td>
                    <input
                      type="checkbox"
                      checked={admin?.isEnabled || false}
                      onChange={() =>
                        updatePermission(admin)
                      }
                    />
                  </td>

                  <td>
                    <input
                      type="checkbox"
                      checked={tenant?.isEnabled || false}
                      onChange={() =>
                        updatePermission(tenant)
                      }
                    />
                  </td>

                </tr>
              );
            })}

          </tbody>
        </table>

      </div>

    </Layout>
  );
}

export default RolePermissions;