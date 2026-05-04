import React from "react";
import { Navigate } from "react-router-dom";

function RoleRoute({ roles, children }) {

 const token = localStorage.getItem("token");

 if(!token) return <Navigate to="/" />;

 const payload =
   JSON.parse(atob(token.split(".")[1]));

 if(!roles.includes(payload.role)){
   return <Navigate to="/" />;
 }

 return children;
}

export default RoleRoute;