package services;

import entities.MailCredentials;
import entities.SystemUser;
import repository.UserRepository;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import java.util.Properties;
import java.util.UUID;

public class ForgotPasswordService {
    private static final String MESAJ_EROARE_LIPSA_USER = "Nu exista niciun user cu acest email";
    private static final String SUBIECT_EMAIL = "Notificare parola";
    private UserRepository userRepository;

    public ForgotPasswordService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * In cazul in care un user isi uita parola o sa i se modifice si il va notifica prin email cu noua parola
     *
     * @param email - Mail-ul userului
     * @throws NoSuchFieldException Arunca exceptie daca nu exista niciun user cu email-ul dat
     */
    public void sendPasswordToEmail(String email) throws NoSuchFieldException, IOException, MessagingException, NoSuchAlgorithmException {
        Optional<SystemUser> optionalUser = userRepository.findOne(email);
        if (!optionalUser.isPresent()) {
            throw new NoSuchFieldException(MESAJ_EROARE_LIPSA_USER);
        }
        //acum trimit mail-ul cu parola user-ului dupa ce o schimb
        String parolaNoua = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        SystemUser user = optionalUser.get();
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.reset();
        messageDigest.update(parolaNoua.getBytes());
        byte[] parolaNouaEncrypted = messageDigest.digest();
        messageDigest.reset();
        userRepository.update(email, new SystemUser(user.getId(), parolaNouaEncrypted, user.getRol()));
        String bodyEmail = "Folositi aceasta parola pentru a va conecta la aplicatie(o puteti schimba mai tarziu): "
                + parolaNoua;
        sendMailToStudent(email, SUBIECT_EMAIL, bodyEmail);
    }

    /**
     * Trimite un mesaj la un student
     *
     * @param studentAddress - String - mail-ul studentului
     * @param subject        - Subiectul mesajului
     * @param messageBody    - Continutul mesajului
     * @throws MessagingException - Arunca exceptie daca apare vreo problema in timpul trimiterii
     */
    private void sendMailToStudent(String studentAddress, String subject, String messageBody) throws MessagingException {
        String username = MailCredentials.getApplicationEmail();
        String password = MailCredentials.getApplicationPassword();
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        Session session = Session.getInstance(properties,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(username));
        message.setRecipients(Message.RecipientType.TO,
                InternetAddress.parse(studentAddress));
        message.setSubject(subject);
        message.setText(messageBody);
        Transport.send(message);
    }
}
