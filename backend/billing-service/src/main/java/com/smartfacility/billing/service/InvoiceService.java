package com.smartfacility.billing.service;

import java.io.ByteArrayOutputStream;

import org.springframework.stereotype.Service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import com.smartfacility.billing.entity.Bill;

@Service
public class InvoiceService {

 public byte[] generate(Bill bill) throws Exception {

   ByteArrayOutputStream out =
   new ByteArrayOutputStream();

   Document doc = new Document();
   PdfWriter.getInstance(doc, out);

   doc.open();

   Font title =
   new Font(Font.HELVETICA,20,Font.BOLD);

   Paragraph head =
   new Paragraph(
   "SmartFacility Invoice",
   title
   );

   head.setAlignment(Element.ALIGN_CENTER);

   doc.add(head);
   doc.add(new Paragraph(" "));

   PdfPTable table =
   new PdfPTable(2);

   table.setWidthPercentage(100);

   table.addCell("Invoice ID");
   table.addCell(
   String.valueOf(bill.getBillId()));

   table.addCell("Tenant Email");
   table.addCell(bill.getTenantEmail());

   table.addCell("Unit No");
   table.addCell(bill.getUnitNo());

   table.addCell("Billing Month");
   table.addCell(bill.getBillingMonth());

   table.addCell("Maintenance");
   table.addCell(
   "₹"+bill.getMaintenanceCharge());

   table.addCell("Water");
   table.addCell(
   "₹"+bill.getWaterCharge());

   table.addCell("Penalty");
   table.addCell(
   "₹"+bill.getPenalty());

   table.addCell("Total");
   table.addCell(
   "₹"+bill.getTotalAmount());

   table.addCell("Status");
   table.addCell(bill.getStatus());

   doc.add(table);

   doc.add(new Paragraph(" "));
   doc.add(new Paragraph(
   "Thank you for using SmartFacility."
   ));

   doc.close();

   return out.toByteArray();
 }
}