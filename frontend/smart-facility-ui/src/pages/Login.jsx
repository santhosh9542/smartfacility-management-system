import React, { useState } from "react";
import api from "../services/api";
import { useNavigate } from "react-router-dom";

function Login() {

  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const navigate = useNavigate();

  const login = async () => {
  try {
    // clear old token before fresh login
    localStorage.removeItem("token");

    const res = await api.post("/auth/login", {
      email,
      password
    });

   if (res.data.token) {

  localStorage.setItem("token", res.data.token);

  const payload = JSON.parse(atob(res.data.token.split(".")[1]));

  if (payload.role === "SUPER_ADMIN") {
   navigate("/dashboard");
}
else if (payload.role === "ADMIN") {
   navigate("/dashboard");
}
else if (payload.role === "TENANT") {
   navigate("/user-dashboard");
}
else {
   navigate("/");
}
} else {
      alert("Invalid Login");
    }

  } catch (error) {
  localStorage.removeItem("token");
  setPassword("");
  alert("Invalid Login");
}
};

  return (
    <div className="login-page">

      <div className="login-box">
        <h2>SmartFacility Login</h2>

        <input
          placeholder="Email"
          onChange={(e) => setEmail(e.target.value)}
        />

        <input
          type="password"
          placeholder="Password"
          onChange={(e) => setPassword(e.target.value)}
        />

        <button onClick={login}>Login</button>

<p
className="register-link"
onClick={()=>navigate("/forgot-password")}
>
Forgot Password?
</p>

<p
 className="register-link"
 onClick={()=>navigate("/register")}
>
Create Admin Account
</p>
      </div>

    </div>
  );
}

export default Login;