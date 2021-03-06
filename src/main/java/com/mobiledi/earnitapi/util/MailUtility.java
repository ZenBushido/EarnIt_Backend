package com.mobiledi.earnitapi.util;

import static com.mobiledi.earnitapi.util.MessageConstants.MAIL_PASSWORD;
import static com.mobiledi.earnitapi.util.MessageConstants.MAIL_USERNAME;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MailUtility {

    public static boolean sendMail(String emailTo, String subject, String body) {
        String smtp = "smtp.superb.net";
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", smtp);
        props.put("mail.smtp.ssl.trust", smtp);
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(MAIL_USERNAME, MAIL_PASSWORD);
                    }
                });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(MAIL_USERNAME));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailTo));
            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);

            log.debug("Mail sent to " + emailTo);
            return true;
        } catch (MessagingException e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }
}
