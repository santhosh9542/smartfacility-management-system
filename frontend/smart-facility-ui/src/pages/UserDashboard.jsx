import React, { useEffect, useState } from "react";
import api from "../services/api";
import Layout from "../components/Layout";

function UserDashboard() {

  const [bills, setBills] = useState([]);
  const [pending, setPending] = useState(0);

  useEffect(() => {
    loadBills();
  }, []);

  const loadBills = async () => {
    try {
      const res = await api.get("/bills");

      setBills(res.data);

      let total = 0;

      res.data.forEach((bill) => {
        if (bill.status === "UNPAID") {
          total += bill.totalAmount;
        }
      });

      setPending(total);

    } catch (error) {
      console.log(error);
    }
  };

  return (
    <Layout>

      <h1>User Dashboard</h1>

      <div className="card">
        <h3>Total Bills</h3>
        <p>{bills.length}</p>
      </div>

      <div className="card">
        <h3>Pending Amount</h3>
        <p>₹{pending}</p>
      </div>

    </Layout>
  );
}

export default UserDashboard;