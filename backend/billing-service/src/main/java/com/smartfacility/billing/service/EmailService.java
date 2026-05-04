package com.smartfacility.billing.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.*;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

 @Autowired
 private JavaMailSender sender;

 public void sendInvoice(
 String to,
 byte[] pdf
 ) throws Exception {

   MimeMessage msg =
   sender.createMimeMessage();

   MimeMessageHelper helper =
   new MimeMessageHelper(msg,true);

   helper.setTo(to);
   helper.setSubject("SmartFacility Invoice");
   helper.setText(
   "Dear Customer,\n\nPlease find attached invoice.\n\nThank You."
   );

   helper.addAttachment(
   "invoice.pdf",
   new ByteArrayResource(pdf)
   );

   sender.send(msg);
 }
}