package Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class RunnableSendEmail implements Runnable {
    String mesaj;
    String mail;

    public RunnableSendEmail(String mail, String mesaj) {
        this.mesaj = mesaj;
        this.mail = mail;
    }

    @Override
    public void run() {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("contulMeu@gmail.com",parola);
                    }
                });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("contulMeu@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(mail));
            message.setSubject("Notificare MAP");
            message.setText(mesaj);

            Transport.send(message);
        } catch (MessagingException e) {
        }
    }
    String parola="parolaMea";
}
