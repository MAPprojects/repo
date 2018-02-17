package Service;

import Repository.DBRepositoryLogin;
import Validate.ValidationException;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

public class LoginService {
    private DBRepositoryLogin repository;

    public LoginService(DBRepositoryLogin repository) {
        this.repository = repository;
    }

    public boolean save(String email, String password, String table){
        try {
            return repository.save(email, password,table);
        } catch (ValidationException | FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete(String email, String table){
        try {
            return repository.delete(email, table);
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String get(String email, String table){
        return repository.getEntity(email, table);
    }

    public HashMap<String, String> getAll(String table){
        return repository.getAll(table);
    }
}
