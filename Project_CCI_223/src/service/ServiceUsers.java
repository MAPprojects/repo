package service;

import domain.User;
import exceptii.EntityNotFoundException;
import exceptii.ValidationException;
import repository.Repository;
import sablonObserver.Observable;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import java.util.Optional;

public class ServiceUsers extends Observable{
    private Repository<User,String> repositoryUsers;

    public ServiceUsers(Repository<User, String> repositoryUsers) {
        this.repositoryUsers = repositoryUsers;
    }

    public ServiceUsers() {
    }

    public User getUserProfesorByName(String name){
        for (User user:repositoryUsers.findAll()){
            if (user.getProf_student().equals("profesor")&&user.getNume().equals(name))
                return user;
        }
        return null;
    }

    public Boolean verificaLogin(String username,String password){
        try {
            Optional<User> userOptional=repositoryUsers.findOne(username);
            if (userOptional.isPresent()){
                User user=userOptional.get();

                String bigInt = getHashCodePassword(password);

                if (user.getPassword().equals(bigInt)){
                    return true;
                }
            }
        } catch (EntityNotFoundException e) { }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String getHashCodePassword(String password) throws NoSuchAlgorithmException {
        MessageDigest messageDigest=MessageDigest.getInstance("MD5");
        messageDigest.reset();
        messageDigest.update(password.getBytes());
        byte[] digest=messageDigest.digest();
        return new BigInteger(1,digest).toString();
    }

    public String getIfStudentOrProfesor(String username){
        try{
            Optional<User> opUser=repositoryUsers.findOne(username);
            if (opUser.isPresent()){
                return opUser.get().getProf_student();
            }
        } catch (EntityNotFoundException e) { }
        return null;
    }

    public Boolean getIfRightOldPassword(String username,String oldPassword){
        try {
            Optional<User> userOptional=repositoryUsers.findOne(username);
            if (userOptional.isPresent()){
                return userOptional.get().getPassword().equals(getHashCodePassword(oldPassword));
            }
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void updateNewPassword(String username,String newPassword) throws ValidationException, EntityNotFoundException {
        try {
            Optional<User> userOptional=repositoryUsers.findOne(username);
            if (userOptional.isPresent()){
                User user=userOptional.get();
                user.setPassword(getHashCodePassword(newPassword));
                repositoryUsers.update(user);
                notifyObservers();
            }
        } catch (EntityNotFoundException e) {
            throw e;
        } catch (ValidationException e) {
            throw e;

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public Boolean getIfRightEmailForUsername(String username, String email) {
        try{
            Optional<User> userOptional=repositoryUsers.findOne(username);
            if (userOptional.isPresent()){
                return userOptional.get().getEmail().equals(email);
            }
        } catch (EntityNotFoundException e) {
        }
        return false;
    }

    public void addAccount(User user) throws ValidationException {
        try {
            user.setPassword(getHashCodePassword(user.getPassword()));
            repositoryUsers.save(user);
            notifyObservers();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public Optional<User> getUser(String username) throws EntityNotFoundException {
        return repositoryUsers.findOne(username);
    }

    public void updateUser(User user, Integer criptare) throws ValidationException, EntityNotFoundException {
        try {
            if (criptare==1)
            {
                user.setPassword(getHashCodePassword(user.getPassword()));
            }

            repositoryUsers.update(user);
            notifyObservers();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public void deleteAccount(String username) throws EntityNotFoundException {
        repositoryUsers.delete(username);
    }
}
