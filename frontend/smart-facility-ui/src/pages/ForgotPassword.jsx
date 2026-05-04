import React, { useState } from "react";
import api from "../services/api";
import { useNavigate } from "react-router-dom";

function ForgotPassword() {

 const [email,setEmail] = useState("");
 const [otp,setOtp] = useState("");
 const [password,setPassword] = useState("");

 const navigate = useNavigate();

 const sendOtp = async () => {
   try{
     const res = await api.post(
       "/auth/forgot-password",
       { email }
     );

     alert(res.data);

   }catch(error){
     alert("Failed to send OTP");
   }
 };

 const reset = async () => {
   try {

     const res = await api.post(
       "/auth/reset-password",
       { email, otp, password }
     );

     alert(res.data);

     setTimeout(() => {
       navigate("/");
     }, 1000);

   } catch (error) {

     if(error.response){
       alert(error.response.data);
     }else{
       alert("Reset Failed");
     }
   }
 };

 return (
 <div className="login-page">
   <div className="login-box">

     <h2>Forgot Password</h2>

     <input
       placeholder="Email"
       onChange={(e)=>setEmail(e.target.value)}
     />

     <button onClick={sendOtp}>
       Send OTP
     </button>

     <input
       placeholder="OTP"
       onChange={(e)=>setOtp(e.target.value)}
     />

     <input
       type="password"
       placeholder="New Password"
       onChange={(e)=>setPassword(e.target.value)}
     />

     <button onClick={reset}>
       Reset Password
     </button>

   </div>
 </div>
 );
}

export default ForgotPassword;