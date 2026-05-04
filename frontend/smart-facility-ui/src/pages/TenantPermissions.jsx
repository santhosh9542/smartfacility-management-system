import React, { useEffect, useState } from "react";
import api from "../services/api";
import Layout from "../components/Layout";

function TenantPermissions(){

 const [menus,setMenus]=useState([]);

 const token = localStorage.getItem("token");
 const payload =
 JSON.parse(atob(token.split(".")[1]));

 const orgId = payload.organizationId;

 useEffect(()=>{
   loadData();
 },[]);

 const loadData = async()=>{

   const res =
   await api.get(
   "/permissions/organization/" +
   orgId +
   "/TENANT"
   );

   setMenus(res.data);
 };

 const toggle = async(item)=>{

   await api.put(
   "/permissions/organization/" +
   item.id,
   {
     isEnabled: !item.isEnabled
   });

   loadData();
 };

 return(
 <Layout>

 <div className="topbar">
   <h1>Tenant Permissions</h1>
 </div>

 <div className="table-box">

 <table>
 <thead>
 <tr>
   <th>Menu</th>
   <th>Enabled</th>
 </tr>
 </thead>

 <tbody>

 {menus.map((m)=>(
 <tr key={m.id}>
   <td>{m.menuName}</td>

   <td>
   <input
    type="checkbox"
    checked={m.isEnabled}
    onChange={()=>toggle(m)}
   />
   </td>

 </tr>
 ))}

 </tbody>
 </table>

 </div>

 </Layout>
 );
}

export default TenantPermissions;