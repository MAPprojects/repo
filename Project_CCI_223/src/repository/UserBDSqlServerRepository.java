package repository;

import domain.User;
import exceptii.EntityNotFoundException;
import exceptii.ValidationException;
import validator.Validator;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

public class UserBDSqlServerRepository extends UserRepository {
    /**
     * Constructor
     *
     * @param val -validator.Validator<E></E>
     */
    public UserBDSqlServerRepository(Validator<User> val) {
        super(val);
        loadFromBD();
    }

    private void loadFromBD() {
        try {
            ArrayList<User> list=UserBDOperations.searchUsers();
            for (User user:list){
                super.save(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<User> save(User user) throws ValidationException {
        //verificam ca username-ul si emailul sa fie unice
        Boolean ok=verificaUnicitateEmailUsername(user);
        if (ok) {
            Optional<User> aux = super.save(user);
            try {
                UserBDOperations.insertUser(user.getUsername(), user.getPassword(), user.getProf_student(), user.getNume(), user.getEmail());
                return aux;
            } catch (SQLException e) {
                ArrayList<String> erori = new ArrayList<>();
                erori.add(e.getMessage());
                throw new ValidationException("Eroare la save User", erori);
            }
        }
        ArrayList<String> erori=new ArrayList<>();
        erori.add("Emailul si usernameul trebuie sa fie unice");
        throw new ValidationException("Eroare unicitate",erori);
    }

    private Boolean verificaUnicitateEmailUsername(User user) {
        Boolean ok1=verificaUnicitateEmail(user);
        Boolean ok2=verificaUnicitateUsername(user);
        return ok1 && ok2;
    }

    private Boolean verificaUnicitateUsername(User user) {
        for (User u:super.findAll()){
            if (u.getUsername().equals(user.getUsername())){
                return false;
            }
        }
        return true;
    }

    private Boolean verificaUnicitateEmail(User user) {
        for (User u:super.findAll()){
            if (u.getEmail().equals(user.getEmail())){
                return false;
            }
        }
        return true;
    }

    @Override
    public void update(User user) throws ValidationException, EntityNotFoundException {
        Boolean ok=verificaUnicitateEmailPtUpdate(user);
        if (ok) {
            super.update(user);
            ArrayList<String> erori=new ArrayList<>();
            try {
                UserBDOperations.updateUser(user);
            } catch (SQLException e) {
                erori.add(e.getMessage());
                throw new ValidationException("Eroare SQL",erori);
            }
        }

    }

    private Boolean verificaUnicitateEmailPtUpdate(User user) {
        //unul dintre emailuri poate fi egal cu emailul de la userul dat ca parametru
        for (User u:super.findAll()){
            if (u.getEmail().equals(user.getEmail())&&user.getUsername().equals(u.getUsername())){
                return true;
            }
            else if(u.getEmail().equals(user.getEmail())){
                return false;
            }
        }
        return true;
    }

    @Override
    public Optional<User> delete(String idUser) throws EntityNotFoundException {
        Optional<User> user=super.findOne(idUser);
        try {
            UserBDOperations.deleteUserByUsername(idUser);
            super.delete(idUser);
        } catch (SQLException e) {
            throw new EntityNotFoundException(e.getMessage());
        }

        return user;
    }

}
