import React, { useEffect, useState } from "react";
import api from "../services/api";
import Layout from "../components/Layout";

import {
  ResponsiveContainer,
  BarChart,
  Bar,
  XAxis,
  YAxis,
  Tooltip,
  PieChart,
  Pie,
  Cell,
  Legend
} from "recharts";

function Reports() {

  const [summary, setSummary] = useState({});
  const [status, setStatus] = useState({});
  const [monthly, setMonthly] = useState([]);

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {

    try {

      const s1 = await api.get("/reports/summary");
      const s2 = await api.get("/reports/status");
      const s3 = await api.get("/reports/monthly");

      setSummary(s1.data);
      setStatus(s2.data);
      setMonthly(s3.data);

    } catch (error) {
      console.log(error);
    }
  };

  const pieData = [
    { name: "Paid", value: status.paid || 0 },
    { name: "Unpaid", value: status.unpaid || 0 }
  ];

  return (
    <Layout>

      <div className="topbar">
        <h1>Reports & Analytics</h1>
      </div>

      {/* CARDS */}

      <div className="cards">

        <div className="card">
          <h3>Total Bills</h3>
          <p>{summary.totalBills || 0}</p>
        </div>

        <div className="card">
          <h3>Revenue</h3>
          <p>₹{summary.revenue || 0}</p>
        </div>

        <div className="card">
          <h3>Pending</h3>
          <p>₹{summary.pending || 0}</p>
        </div>

        <div className="card">
          <h3>Paid Bills</h3>
          <p>{status.paid || 0}</p>
        </div>

        <div className="card">
          <h3>Unpaid Bills</h3>
          <p>{status.unpaid || 0}</p>
        </div>

      </div>

      {/* CHARTS */}

      <div className="chart-grid">

        <div className="chart-box">

          <h2>Monthly Revenue</h2>

          <ResponsiveContainer
            width="100%"
            height={300}
          >
            <BarChart data={monthly}>
              <XAxis dataKey="month" />
              <YAxis />
              <Tooltip />
              <Bar
                dataKey="revenue"
                fill="#3b82f6"
              />
            </BarChart>
          </ResponsiveContainer>

        </div>

        <div className="chart-box">

          <h2>Payment Status</h2>

          <ResponsiveContainer
            width="100%"
            height={300}
          >
            <PieChart>

              <Pie
                data={pieData}
                dataKey="value"
                outerRadius={100}
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

export default Reports;