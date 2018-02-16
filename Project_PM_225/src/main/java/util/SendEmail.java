package util;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class SendEmail {
    public void sendEmail(String numeS, String email, String mesaj,String subiect) {
        final String username = "cataloglaborator@gmail.com";
        final String password = "12345qwer";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("cataloglaborator@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(email));
            message.setSubject(subiect);
            message.setText("Draga, "+numeS+"\n\n"
                    +mesaj+ "\n\n Acest mesaj a fost trimis automat.");
            Transport.send(message);
            System.out.println("S-a trimis email catre " + numeS);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

}
