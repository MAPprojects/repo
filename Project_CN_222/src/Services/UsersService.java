package Services;

import Domain.Category;
import Domain.User;
import Repository.GenericRepository;
import Utilities.ListEvent;
import Utilities.ListEventType;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public class UsersService extends AbstractService<User> {
    private GenericRepository<String, User> repository;

    public UsersService(GenericRepository<String, User> repo) {
        this.repository = repo;
    }

    public void add(String username, String password, Category category) {
        User u = new User(username,password,category);
        repository.add(u);
    }

    public User remove(String id) {
        return repository.delete(id);
    }

    public boolean find(String id) {
        return repository.find(id);
    }

    public User get(String id) {
        return repository.get(id);
    }

    public void update(String username, String newPassword) {
        User newUser = get(username);
        newUser.setPassword(newPassword);
        repository.update(username, newUser);
    }

    public void updateUser(String username, String newUser) {
        User newU = get(username);
        newU.setId(newUser);
        repository.update(username, newU);
    }



}
