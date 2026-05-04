import React, { useEffect, useState } from "react";
import api from "../services/api";
import Layout from "../components/Layout";

function Tenants() {

  const [tenants, setTenants] = useState([]);

  const [editId, setEditId] = useState(null);

  const [form, setForm] = useState({
    tenantName: "",
    email: "",
    phone: "",
    planType: "",
    password: "",
    status: "ACTIVE"
  });

  useEffect(() => {
    loadTenants();
  }, []);

  const loadTenants = async () => {
    try {
      const res = await api.get("/tenants");
      setTenants(res.data);
    } catch (error) {
      console.log(error);
    }
  };

  const saveTenant = async () => {
    try {

      if (editId) {
        await api.put("/tenants/" + editId, form);
        alert("Tenant Updated Successfully");
      } else {
        await api.post("/tenants", form);
        alert("Tenant + Login User Created Successfully");
      }

      clearForm();
      loadTenants();

    } catch (error) {
      console.log(error);
      alert("Operation Failed");
    }
  };

  const editTenant = (tenant) => {

    setEditId(tenant.tenantId);

    setForm({
      tenantName: tenant.tenantName,
      email: tenant.email,
      phone: tenant.phone,
      planType: tenant.planType,
      password: "",
      status: tenant.status
    });
  };

const deleteTenant = async (id) => {
  const ok = window.confirm("Delete this tenant?");
  if (!ok) return;

  try {
    await api.delete("/tenants/" + id);
    alert("Deleted Successfully");
    loadTenants();
  } catch (error) {
    alert("Delete Failed");
  }
};

  const resetPassword = async (email) => {

    const newPassword = prompt("Enter New Password");

    if (!newPassword) return;

    try {
      await api.post("/tenants/reset-password", {
        email: email,
        password: newPassword
      });

      alert("Password Reset Successfully");

    } catch (error) {
      console.log(error);
      alert("Password Reset Failed");
    }
  };

  const clearForm = () => {

    setEditId(null);

    setForm({
      tenantName: "",
      email: "",
      phone: "",
      planType: "",
      password: "",
      status: "ACTIVE"
    });
  };

  return (
    <Layout>

      <h1>Tenant Management</h1>

      <div className="form-box">

        <input
          placeholder="Tenant Name"
          value={form.tenantName}
          onChange={(e) =>
            setForm({ ...form, tenantName: e.target.value })
          }
        />

        <input
          placeholder="Email"
          value={form.email}
          onChange={(e) =>
            setForm({ ...form, email: e.target.value })
          }
        />

        <input
          placeholder="Phone"
          value={form.phone}
          onChange={(e) =>
            setForm({ ...form, phone: e.target.value })
          }
        />

        <input
          placeholder="Plan Type"
          value={form.planType}
          onChange={(e) =>
            setForm({ ...form, planType: e.target.value })
          }
        />

        <input
          placeholder="Password"
          type="password"
          value={form.password}
          onChange={(e) =>
            setForm({ ...form, password: e.target.value })
          }
        />

        <select
          value={form.status}
          onChange={(e) =>
            setForm({ ...form, status: e.target.value })
          }
        >
          <option value="ACTIVE">ACTIVE</option>
          <option value="INACTIVE">INACTIVE</option>
        </select>

        <button onClick={saveTenant}>
          {editId ? "Update Tenant" : "Save Tenant"}
        </button>

        {editId && (
          <button onClick={clearForm}>
            Cancel Edit
          </button>
        )}

      </div>

      <br />

      <table border="1" width="100%">
        <thead>
          <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Email</th>
            <th>Phone</th>
            <th>Plan</th>
            <th>Status</th>
            <th>Action</th>
          </tr>
        </thead>

        <tbody>
          {tenants.map((t) => (
            <tr key={t.tenantId}>
              <td>{t.tenantId}</td>
              <td>{t.tenantName}</td>
              <td>{t.email}</td>
              <td>{t.phone}</td>
              <td>{t.planType}</td>
              <td>{t.status}</td>

              <td>
  <button
    onClick={() => editTenant(t)}
    className="edit-btn"
  >
    Edit
  </button>

  <button
    onClick={() => resetPassword(t.email)}
    className="reset-btn"
  >
    ResetPw
  </button>

  <button
    onClick={() => deleteTenant(t.tenantId)}
    className="delete-btn"
  >
    Delete
  </button>
</td>

            </tr>
          ))}
        </tbody>
      </table>

    </Layout>
  );
}

export default Tenants;