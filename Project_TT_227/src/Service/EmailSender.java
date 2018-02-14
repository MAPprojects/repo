package Service;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

public class EmailSender {

    private String host;
    private String user;
    private String password;
    private String to;
    private String from;
    private String subject;
    private String message;

    public EmailSender(String host, String user, String password, String to, String from, String subject, String message) {
        this.host = host;
        this.user = user;
        this.password = password;
        this.to = to;
        this.from = from;
        this.subject = subject;
        this.message = message;
    }

    public void send() {
        try {
            boolean sessionDebug = false;

            Properties props = System.getProperties();

            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", this.host);
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.required", "true");

            //java.security.Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
            Session mailSession = Session.getDefaultInstance(props, null);
            mailSession.setDebug(sessionDebug);
            Message msg = new MimeMessage(mailSession);
            msg.setFrom(new InternetAddress(this.from));
            InternetAddress[] address = {new InternetAddress(this.to)};
            msg.setRecipients(Message.RecipientType.TO, address);
            msg.setSubject(this.subject);
            msg.setSentDate(new Date());
            msg.setText(this.message);

            Transport transport = mailSession.getTransport("smtp");
            transport.connect(this.host, this.user, this.password);
            transport.sendMessage(msg, msg.getAllRecipients());
            transport.close();

            System.out.println("E-mail trimis cu succes!");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}