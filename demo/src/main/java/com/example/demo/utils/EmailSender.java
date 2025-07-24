package com.example.demo.utils;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;

public class EmailSender {

    public static void sendEmail(String to, String subject, String body) {
        final String from = "ratatouilletpo@gmail.com";
        final String password = "lpke siyx ztfz tsds"; // use environment variable or configuration instead

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        properties.put("mail.smtp.ssl.trust", "*"); // <- ACEPTA TODOS LOS CERTIFICADOS (inseguro)

        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(body);
            Transport.send(message);
            System.out.println("Email sent successfully");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
