package viewController;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.controlsfx.control.decoration.Decorator;
import org.controlsfx.control.decoration.StyleClassDecoration;
import org.controlsfx.validation.Severity;
import org.controlsfx.validation.ValidationResult;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import org.controlsfx.validation.decoration.StyleClassValidationDecoration;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.imageio.ImageIO;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.swing.*;

import java.io.File;
import java.io.IOException;
import java.util.Properties;


public class MailController {

    @FXML
    JFXButton sendButton,cancelButton;

    @FXML
    JFXTextField destinationTextField,subjectTextField;

    @FXML
    JFXTextArea contentTextArea;

    @FXML
    JFXProgressBar mailSendingProgressBar;

    @FXML
    Label mailSendingLabel,destinationErrorLabel,subjectErrorLabel;

    Stage stage;
    String destination,subject,content,fileName;
    private Task<Void> messageSender;




    void setDefaults(String destination,String subject,String content, Stage stage,String fileName){
        this.destination=destination;
        this.subject=subject;
        this.content=content;
        this.stage=stage;
        this.fileName=fileName;
        if(destination != null){
            destinationTextField.setText(destination);
            destinationTextField.setDisable(true);
        }
        if(subject != null) {
            subjectTextField.setText(subject);
            subjectTextField.setDisable(true);
        }
        if(content != null) {
            contentTextArea.setText(content);
            contentTextArea.setDisable(true);
        }
        mailSendingProgressBar.setProgress(0f);
    }



    @FXML
    private void handleSend(){

          Boolean validation=true;
          if(subjectTextField.getText().equals(""))
          {
              subjectErrorLabel.setVisible(true);
              validation=false;
          }
          if(destinationTextField.getText().equals("")){
              destinationErrorLabel.setVisible(true);
              validation=false;
          }
          if(validation) {
              subjectErrorLabel.setVisible(false);
              destinationErrorLabel.setVisible(false);
              if(destination==null)
                  destination=destinationTextField.getText();
              MailSender mailSender = new MailSender(destination, subjectTextField.getText(), contentTextArea.getText(),fileName);
              Thread th = new Thread(mailSender);
              th.setDaemon(true);
              mailSendingLabel.setText("Mailul se trimite...");
              mailSendingLabel.setTextFill(Color.WHITE);
              mailSendingProgressBar.setProgress(-1.0f);
              mailSender.setOnSucceeded((Event event) -> {
                  mailSendingLabel.setText("Mailul a fost trimis cu succes!");
                  mailSendingProgressBar.setProgress(1.0f);
              });
              mailSender.setOnFailed((Event event) -> {
                  mailSendingLabel.setText("Eroare la trimiterea mailului!");
                  mailSendingLabel.setTextFill(Color.RED);
                  mailSendingProgressBar.setProgress(0f);
              });
              th.start();
          }
    }

    @FXML
    private void handleCancel(){
        stage.close();
    }

    public void handleSendButtonGlow(MouseEvent mouseEvent) {
        BoxBlur blur = new BoxBlur();
        blur.setWidth(3);
        blur.setHeight(3);
        sendButton.setEffect(blur);
    }

    public void handleSendButtonUnglow(MouseEvent mouseEvent) {
        sendButton.setEffect(null);
    }

    public void handleCancelButtonGlow(MouseEvent mouseEvent) {
        BoxBlur blur = new BoxBlur();
        blur.setWidth(3);
        blur.setHeight(3);
        cancelButton.setEffect(blur);
    }

    public void handleCancelButtonUnglow(MouseEvent mouseEvent) {
        cancelButton.setEffect(null);
    }
}

class MailSender extends Task{

    private Session session;
    private String destination,subject,content,fileName;


    public MailSender(String destination,String subject,String content,String fileName) {

        final String username = "inscriere.ubb@gmail.com";
        final String password = "inscrierelaubbcluj";
        this.destination=destination;
        this.subject=subject;
        this.content=content;
        this.fileName=fileName;

        Properties props = new Properties();
        props.put("mail.smtp.user", "inscriere.ubb@gmail.com");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", 587);
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.socketFactory.port", 587);
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");


        session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
    }

    @Override
    protected Object call() throws Exception {
        sendMail();
        return null;
    }



    public void sendMail() {
        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("inscriere.ubb@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(destination));
            message.setSubject(subject);
            message.setText(content);

            if(fileName!=null){
                MimeBodyPart messageBodyPart = new MimeBodyPart();

                Multipart multipart = new MimeMultipart();

                messageBodyPart = new MimeBodyPart();
                String file = "C:\\Users\\Alexandru\\IdeaProjects\\Tema Lab 6\\"+fileName;
                String fName = fileName;
                DataSource source = new FileDataSource(file);
                messageBodyPart.setDataHandler(new DataHandler(source));
                messageBodyPart.setFileName(fName);
                multipart.addBodyPart(messageBodyPart);

                message.setContent(multipart);
            }

            Transport.send(message);

            System.out.println("Done");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
