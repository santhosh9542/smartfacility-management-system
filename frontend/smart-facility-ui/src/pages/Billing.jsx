import React, { useEffect, useState } from "react";
import api from "../services/api";
import Layout from "../components/Layout";

function Billing() {

  const [bills, setBills] = useState([]);

  useEffect(() => {
    loadBills();
  }, []);

  const loadBills = async () => {
    try {
      const res = await api.get("/bills");
      setBills(res.data);
    } catch (error) {
      console.log(error);
    }
  };

const generateBills = async()=>{

 try{
   await api.post("/bills/generate-monthly");
   alert("Bills Generated");
 }catch(error){
   alert("Failed");
 }

};
const token = localStorage.getItem("token");
const payload = JSON.parse(atob(token.split(".")[1]));
const role = payload.role;

const downloadPdf = async (id) => {
  try {
    const res = await api.get("/bills/invoice/" + id, {
      responseType: "blob"
    });

    const file = new Blob([res.data], {
      type: "application/pdf"
    });

    const url = window.URL.createObjectURL(file);

    const link = document.createElement("a");
    link.href = url;
    link.download = "invoice_" + id + ".pdf";
    link.click();

    window.URL.revokeObjectURL(url);

  } catch (error) {
    alert("PDF Download Failed");
  }
};


const payBill = async(id)=>{

 const method =
 prompt("Enter Payment Method:\nUPI / CARD / CASH");

 if(!method) return;

 try{

   await api.post(
   "/bills/pay-simulate/" + id,
   {
     paymentMethod: method
   });

   alert("Payment Success");
   loadBills();

 }catch(error){
   alert("Payment Failed");
 }
};

  return (
    <Layout>

      <div className="topbar">
        <h1>Billing Management</h1>
      </div>

      <div className="table-box">

        <table>
          <thead>
            <tr>
              <th>ID</th>
              <th>Unit</th>
              <th>Month</th>
              <th>Total</th>
              <th>Status</th>
              <th>Action</th>
            </tr>
          </thead>

          <tbody>

            {bills.map((bill) => (
              <tr key={bill.billId}>
                <td>{bill.billId}</td>
                <td>{bill.unitNo}</td>
                <td>{bill.billingMonth}</td>
                <td>₹{bill.totalAmount}</td>

                <td>
                  {bill.status === "PAID" ? (
                    <span className="paid">PAID</span>
                  ) : (
                    <span className="unpaid">UNPAID</span>
                  )}
                </td>

               <td>

{bill.status === "UNPAID" && (
<button onClick={() =>
payBill(bill.billId)}>
Pay
</button>
)}

<button onClick={() => downloadPdf(bill.billId)}>
PDF
</button>

<button
onClick={async()=>{

 await api.post(
 "/bills/send-invoice/" + bill.billId
 );

 alert("Invoice Sent");
}}
>
Email
</button>

</td>

              </tr>
            ))}

          </tbody>
        </table>
{(role === "ADMIN" || role === "SUPER_ADMIN") && (
  <button onClick={generateBills}>
    Generate Monthly Bills
  </button>
)}
      </div>

    </Layout>
  );
}

export default Billing;