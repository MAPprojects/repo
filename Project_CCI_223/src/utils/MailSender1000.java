package utils;

import javafx.scene.control.Alert;
import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailSender1000 {
    public void send(String subiect, String mesaj, String destinatar) {
        Properties props = new Properties();

        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");

        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.host", "www.scs.ubbcluj.ro");
        props.put("mail.smtp.port", "465");

        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("ccir2082","iuliu hatieganu");
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("ccir2082@scs.ubbcluj.ro"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(destinatar));
            message.setSubject(subiect);
            message.setText(mesaj);

            Transport.send(message);

            System.out.println(subiect + " " + mesaj + " " + destinatar );

            showSucces("Mailul a fost transmis cu succes");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    private void showSucces(String message)
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Succes");
        alert.setContentText(message);
        alert.showAndWait();
    }
}

