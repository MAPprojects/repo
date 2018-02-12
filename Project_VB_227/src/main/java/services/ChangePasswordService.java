package services;

import entities.SystemUser;
import repository.UserRepository;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

public class ChangePasswordService {
    private UserRepository userRepository;

    public ChangePasswordService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void changePassword(String emailSauUser, String oldPassword, String newPassword) throws NoSuchFieldException, IOException, NoSuchAlgorithmException {
        Optional<SystemUser> systemUserOptional = userRepository.findOne(emailSauUser);
        if (!systemUserOptional.isPresent()) {
            throw new NoSuchFieldException("Nu exista niciun user cu acest username sau adresa de email");
        } else {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(newPassword.getBytes());
            byte[] newEncryptedPassword = messageDigest.digest();
            messageDigest.reset();
            messageDigest.update(oldPassword.getBytes());
            byte[] oldEncryptedPassword = messageDigest.digest();
            messageDigest.reset();
            if (!MessageDigest.isEqual(oldEncryptedPassword, systemUserOptional.get().getPassword())) {
                throw new NoSuchFieldException("Parola veche nu este corecta");
            }
            userRepository.update(emailSauUser, new SystemUser(emailSauUser, newEncryptedPassword, systemUserOptional.get().getRol()));
        }
    }
}
