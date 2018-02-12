package Utils;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class MailSender {
    public MailSender(){}

    private Session setUpSender()
    {
        final String username = "fakemail9718@gmail.com";
        final String password = "fakepassword97";

        Properties props = new Properties();
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.socketFactory.port","465");
        props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback","false");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        return session;
    }
    //mail with attachment
    public void send(String adressesTo,String subject,String text,String attachmentPath,String attachmentName)
    {

        Session session=setUpSender();
        try {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress("maplabs@scs.ubbcluj.ro"));
        message.setRecipients(Message.RecipientType.CC,
                InternetAddress.parse(adressesTo));
        message.setSubject(subject);
        message.setText(text);

        MimeBodyPart messageBodyPart = new MimeBodyPart();
        Multipart multipart = new MimeMultipart();

        File att = new File(new File(attachmentPath), attachmentName);
        messageBodyPart.attachFile(att);

        DataSource source = new FileDataSource(att);
        messageBodyPart.setDataHandler(new DataHandler(source));
        messageBodyPart.setFileName(attachmentName);
        multipart.addBodyPart(messageBodyPart);
        message.setContent(multipart);

        System.out.println("Sending");
        Transport.send(message);
        System.out.println("Done");
    } catch (MessagingException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
        }
    }

    //mail without attachment
    public void send(String adressesTo,String subject,String text)
    {
        Session session=setUpSender();
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("maplabs@scs.ubbcluj.ro"));
            message.setRecipients(Message.RecipientType.CC,
                    InternetAddress.parse(adressesTo));
            message.setSubject(subject);
            message.setText(text);
            System.out.println("Sending");
            Transport.send(message);
            System.out.println("Done");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
