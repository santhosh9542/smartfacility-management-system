import React, { useState } from "react";
import api from "../services/api";
import { useNavigate } from "react-router-dom";

function Register(){

 const nav = useNavigate();

 const [form,setForm] = useState({
   organizationName:"",
   adminName:"",
   email:"",
   phone:"",
   password:"",
   plan:"TRIAL"
 });

 const save = async()=>{

   await api.post(
    "/auth/register-admin",
    form
   );

   alert("Registered Successfully");

   nav("/");
 };

 return(
 <div>

   <h1>Register Admin</h1>

   <input placeholder="Organization"
   onChange={(e)=>
   setForm({...form,
   organizationName:e.target.value})}/>

   <input placeholder="Admin Name"
   onChange={(e)=>
   setForm({...form,
   adminName:e.target.value})}/>

   <input placeholder="Email"
   onChange={(e)=>
   setForm({...form,
   email:e.target.value})}/>

   <input placeholder="Phone"
   onChange={(e)=>
   setForm({...form,
   phone:e.target.value})}/>

   <input type="password"
   placeholder="Password"
   onChange={(e)=>
   setForm({...form,
   password:e.target.value})}/>

   <button onClick={save}>
   Register
   </button>

 </div>
 );
}

export default Register;