package services;

import entities.MailCredentials;
import entities.Role;
import entities.SystemUser;
import exceptions.AbstractValidatorException;
import observer_utils.*;
import repository.AbstractCrudRepo;
import validator.Validator;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.xml.bind.ValidationException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class UserSerivce extends AbstractObservable<SystemUserEvent> {
    private AbstractCrudRepo<SystemUser, String> userRepository;
    private Validator<SystemUser> userValidator;
//    private AbstractCrudRepo<Student,String> studentRepository;

    public UserSerivce(AbstractCrudRepo<SystemUser, String> userRepository, Validator<SystemUser> userValidator) {
        this.userRepository = userRepository;
        this.userValidator = userValidator;
    }

    public SystemUser addUser(String email, String password, Role rol) throws ValidationException, IOException, AbstractValidatorException, MessagingException, NoSuchAlgorithmException, NoSuchFieldException {
        List<SystemUser> users = (List<SystemUser>) userRepository.findAll();
        for (SystemUser u : users) {
            if (u.getId().equals(email)) {
                throw new NoSuchFieldException("Exista deja un user cu acest email.");
            }
        }
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.update(password.getBytes());
        byte[] encryptedPassword = messageDigest.digest();
        messageDigest.reset();
        SystemUser user = new SystemUser(email, encryptedPassword, rol);
        if (user.getRol().equals(Role.STUDENT)) {
            //trimitem un mail la student cu noua parola(trimitem doar daca este student)
            String mesaj = "S-a creat contul pentru userul " + email + " cu parola: " + password;
            sendMailToStudent(email, "Parola cont", mesaj);
        }
        userValidator.validate(user);
        userRepository.save(user);
        notifyAll(new SystemUserEvent(SystemUserEventType.ADD, user));
        return user;
    }

    public void deleteUser(String email) throws IOException {
        SystemUser user = userRepository.findOne(email).get();
        userRepository.delete(email);
        notifyAll(new SystemUserEvent(SystemUserEventType.REMOVE, user));
    }

    public List<SystemUser> findAll() {
        return (List<SystemUser>) userRepository.findAll();
    }

    public SystemUser findOneUser(String email) {
        Optional<SystemUser> userOptional = userRepository.findOne(email);
        if (!userOptional.isPresent()) {
            return null;
        } else {
            return userOptional.get();
        }
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
