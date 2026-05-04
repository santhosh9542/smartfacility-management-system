import React, { useEffect, useState } from "react";
import api from "../services/api";
import Layout from "../components/Layout";

function Support() {

  const [tickets, setTickets] = useState([]);
  const [search, setSearch] = useState("");

  const [form, setForm] = useState({
    subject: "",
    description: "",
    priority: "MEDIUM"
  });

  const token = localStorage.getItem("token");
  const payload = JSON.parse(atob(token.split(".")[1]));
  const role = payload.role;

  useEffect(() => {
    loadTickets();
  }, []);

  const loadTickets = async () => {
    try {
      const res = await api.get("/tickets");
      setTickets(res.data || []);
    } catch {
      setTickets([]);
    }
  };

  const createTicket = async () => {
    try {
      await api.post("/tickets", form);

      alert("Ticket Created");

      setForm({
        subject: "",
        description: "",
        priority: "MEDIUM"
      });

      loadTickets();

    } catch {
      alert("Failed");
    }
  };

  const updateStatus = async (id, status) => {

    let remarks = "";

    if (status === "RESOLVED") {
      remarks = prompt("Enter resolution remarks");

      if (!remarks) {
        alert("Remarks required");
        return;
      }
    }

    try {

      await api.put("/tickets/update-status/" + id, {
        status,
        remarks
      });

      loadTickets();

    } catch {
      alert("Update Failed");
    }
  };

  const searchTicket = async () => {
    try {
      const res = await api.get("/tickets/search/" + search);
      setTickets(res.data || []);
    } catch {
      alert("Search Failed");
    }
  };

  const resetSearch = () => {
    setSearch("");
    loadTickets();
  };

  const getStatusClass = (status) => {
    if (status === "OPEN") return "status-open";
    if (status === "IN_PROGRESS") return "status-progress";
    if (status === "RESOLVED") return "status-resolved";
    return "";
  };

  const getPriorityClass = (priority) => {
    if (priority === "HIGH") return "priority-high";
    if (priority === "MEDIUM") return "priority-medium";
    if (priority === "LOW") return "priority-low";
    return "";
  };

  return (
    <Layout>

      <div className="page-title">
        <h1>Support Center</h1>
      </div>

      {role === "TENANT" && (
        <div className="form-box">

          <input
            placeholder="Subject"
            value={form.subject}
            onChange={(e) =>
              setForm({ ...form, subject: e.target.value })
            }
          />

          <textarea
            placeholder="Description"
            value={form.description}
            onChange={(e) =>
              setForm({ ...form, description: e.target.value })
            }
          />

          <select
            value={form.priority}
            onChange={(e) =>
              setForm({ ...form, priority: e.target.value })
            }
          >
            <option>LOW</option>
            <option>MEDIUM</option>
            <option>HIGH</option>
          </select>

          <button onClick={createTicket}>
            Raise Ticket
          </button>

        </div>
      )}

      <div style={{ marginBottom: "15px" }}>
        <input
          placeholder="Search Ticket"
          value={search}
          onChange={(e) => setSearch(e.target.value)}
        />

        <button onClick={searchTicket}>Search</button>
        <button onClick={resetSearch}>Reset</button>
      </div>

      <div className="table-box">

        <table>

          <thead>
            <tr>
              <th>ID</th>
              <th>Subject</th>
              <th>Priority</th>
              <th>Status</th>
              <th>Remarks</th>
              <th>Action</th>
            </tr>
          </thead>

          <tbody>

            {tickets.length > 0 ? (
              tickets.map((t) => (
                <tr key={t.ticketId}>

                  <td>{t.ticketId}</td>
                  <td>{t.subject}</td>

                  <td>
                    <span className={getPriorityClass(t.priority)}>
                      {t.priority}
                    </span>
                  </td>

                  <td>
                    <span className={getStatusClass(t.status)}>
                      {t.status}
                    </span>
                  </td>

                  <td>{t.remarks}</td>

                  <td>

                    {(role === "ADMIN" || role === "SUPER_ADMIN") &&
                     t.status !== "RESOLVED" && (
                      <>
                        <button
                          onClick={() =>
                            updateStatus(
                              t.ticketId,
                              "IN_PROGRESS"
                            )
                          }
                        >
                          Start
                        </button>

                        <button
                          onClick={() =>
                            updateStatus(
                              t.ticketId,
                              "RESOLVED"
                            )
                          }
                        >
                          Resolve
                        </button>
                      </>
                    )}

                    {t.status === "RESOLVED" && "✔ Done"}

                  </td>

                </tr>
              ))
            ) : (
              <tr>
                <td colSpan="6">No Tickets Found</td>
              </tr>
            )}

          </tbody>

        </table>

      </div>

    </Layout>
  );
}

export default Support;