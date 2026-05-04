import React from "react";
import { BrowserRouter, Routes, Route } from "react-router-dom";

import Login from "./pages/Login";
import Dashboard from "./pages/Dashboard";
import Tenants from "./pages/Tenants";
import Billing from "./pages/Billing";
import Reports from "./pages/Reports";
import Register from "./pages/Register";
import UserDashboard from "./pages/UserDashboard";
import Profile from "./pages/Profile";
import Support from "./pages/Support";
import RolePermissions from "./pages/RolePermissions";
import TenantPermissions from "./pages/TenantPermissions";
import ForgotPassword from "./pages/ForgotPassword";


import ProtectedRoute from "./components/ProtectedRoute";
import RoleRoute from "./components/RoleRoute";

function App() {
  return (
    <BrowserRouter>

      <Routes>

        <Route path="/" element={<Login />} />
        <Route path="/register" element={<Register />} />

        {/* ADMIN + SUPER ADMIN */}

        <Route path="/dashboard" element={
          <ProtectedRoute>
            <RoleRoute roles={["ADMIN","SUPER_ADMIN"]}>
              <Dashboard />
            </RoleRoute>
          </ProtectedRoute>
        } />

        <Route path="/tenants" element={
          <ProtectedRoute>
            <RoleRoute roles={["ADMIN","SUPER_ADMIN"]}>
              <Tenants />
            </RoleRoute>
          </ProtectedRoute>
        } />

<Route
path="/forgot-password"
element={<ForgotPassword/>}
/>


      <Route path="/tenant-permissions" element={
<ProtectedRoute>
<RoleRoute roles={["ADMIN"]}>
<TenantPermissions/>
</RoleRoute>
</ProtectedRoute>
}/>

        <Route path="/reports" element={
          <ProtectedRoute>
            <RoleRoute roles={["ADMIN","SUPER_ADMIN"]}>
              <Reports />
            </RoleRoute>
          </ProtectedRoute>
        } />

        {/* ALL USERS */}

        <Route path="/billing" element={
          <ProtectedRoute>
            <Billing />
          </ProtectedRoute>
        } />

        <Route path="/support" element={
          <ProtectedRoute>
            <Support />
          </ProtectedRoute>
        } />

        {/* TENANT */}

        <Route path="/user-dashboard" element={
          <ProtectedRoute>
            <RoleRoute roles={["TENANT"]}>
              <UserDashboard />
            </RoleRoute>
          </ProtectedRoute>
        } />

        <Route path="/profile" element={
  <ProtectedRoute>
    <Profile />
  </ProtectedRoute>
} />

        {/* SUPER ADMIN */}

        <Route path="/permissions" element={
          <ProtectedRoute>
            <RoleRoute roles={["SUPER_ADMIN"]}>
              <RolePermissions />
            </RoleRoute>
          </ProtectedRoute>
        } />

      </Routes>

    </BrowserRouter>
  );
}

export default App;