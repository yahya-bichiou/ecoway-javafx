package services;

import jakarta.mail.*;
import jakarta.mail.internet.*;

import java.util.Properties;

public class MailSender {

    public static void sendMail(String recipient, int id) throws MessagingException {
        String host = "smtp.gmail.com";
        String senderEmail = "azizkadri435@gmail.com";
        String senderPassword = "oegkiniepejwegwf";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(senderEmail));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
        message.setSubject("Test Email");
        message.setText("The depot " + id + " is full please empty it");

        Transport.send(message);
        System.out.println("Email sent successfully!");
    }
}

