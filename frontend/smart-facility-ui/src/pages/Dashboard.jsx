import React, { useEffect, useState } from "react";
import api from "../services/api";
import Layout from "../components/Layout";

import {
  BarChart,
  Bar,
  XAxis,
  YAxis,
  Tooltip,
  ResponsiveContainer,
  PieChart,
  Pie,
  Cell,
  Legend
} from "recharts";

function Dashboard() {

  const [tenantCount, setTenantCount] = useState(0);
  const [billCount, setBillCount] = useState(0);
  const [pendingAmount, setPendingAmount] = useState(0);
  const [revenue, setRevenue] = useState(0);

  const [chartData, setChartData] = useState([]);
  const [pieData, setPieData] = useState([]);

  const token = localStorage.getItem("token");

  let role = "";
  let userName = "";

  if (token) {
    const payload = JSON.parse(atob(token.split(".")[1]));
    role = payload.role;
    userName = payload.sub;
  }

  useEffect(() => {
    loadDashboard();
  }, []);

  const loadDashboard = async () => {
    try {

      const tenantRes = await api.get("/tenants");
      const billRes = await api.get("/bills");

      const tenants = tenantRes.data || [];
      const bills = billRes.data || [];

      setTenantCount(tenants.length);
      setBillCount(bills.length);

      let pending = 0;
      let paid = 0;
      let paidCount = 0;
      let unpaidCount = 0;

      const monthly = {};

      bills.forEach((bill) => {

        const amount = Number(bill.totalAmount || 0);

        if (bill.status === "UNPAID") {
          pending += amount;
          unpaidCount++;
        } else {
          paid += amount;
          paidCount++;
        }

        const month = bill.billingMonth || "Unknown";

        if (!monthly[month]) {
          monthly[month] = 0;
        }

        monthly[month] += amount;
      });

      setPendingAmount(pending);
      setRevenue(paid);

      const chart = Object.keys(monthly).map((key) => ({
        month: key,
        revenue: monthly[key]
      }));

      setChartData(chart);

      setPieData([
        { name: "Paid", value: paidCount },
        { name: "Unpaid", value: unpaidCount }
      ]);

    } catch (error) {
      console.log("Dashboard Error:", error);
    }
  };

  const formatMoney = (value) => {
    return Number(value).toLocaleString("en-IN");
  };

  const getTitle = () => {
    if (role === "SUPER_ADMIN") return "Super Admin Dashboard";
    if (role === "ADMIN") return "Admin Dashboard";
    return "User Dashboard";
  };

  return (
    <Layout>

      <div className="topbar">
        <div>
          <h1>{getTitle()}</h1>
          <p style={{ color: "white", marginTop: "8px" }}>
            Welcome, {userName}
          </p>
        </div>
      </div>

      {/* Cards */}

      <div className="cards">

        <div className="card">
          <h3>Total Tenants</h3>
          <p>{tenantCount}</p>
        </div>

        <div className="card">
          <h3>Total Bills</h3>
          <p>{billCount}</p>
        </div>

        <div className="card">
          <h3>Pending Amount</h3>
          <p>₹{formatMoney(pendingAmount)}</p>
        </div>

        <div className="card">
          <h3>Revenue</h3>
          <p>₹{formatMoney(revenue)}</p>
        </div>

      </div>

      {/* Charts */}

      <div className="chart-grid">

        <div className="chart-box">
          <h2>Monthly Revenue</h2>

          <ResponsiveContainer width="100%" height={280}>
            <BarChart data={chartData}>
              <XAxis dataKey="month" />
              <YAxis />
              <Tooltip />
              <Bar
                dataKey="revenue"
                fill="#38bdf8"
                radius={[8, 8, 0, 0]}
              />
            </BarChart>
          </ResponsiveContainer>

        </div>

        <div className="chart-box">
          <h2>Payment Status</h2>

          <ResponsiveContainer width="100%" height={280}>
            <PieChart>

              <Pie
                data={pieData}
                dataKey="value"
                outerRadius={95}
                label
              >
                <Cell fill="#16a34a" />
                <Cell fill="#dc2626" />
              </Pie>

              <Tooltip />
              <Legend />

            </PieChart>
          </ResponsiveContainer>

        </div>

      </div>

    </Layout>
  );
}

export default Dashboard;