package Entities;

import javafx.util.Pair;

public class User implements HasID<Pair<String, String>> {
    private String username, password;
    private Pair<String, String> IDUser;
    private int ok1, ok2;

    public User(String username, String password, int ok1, int ok2) {
        this.username = username;
        this.password = password;
        IDUser = new Pair<>(username, password);
        this.ok1 = ok1;
        this.ok2 = ok2;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int isOk1() {
        return ok1;
    }

    public void setOk1(int ok1) {
        this.ok1 = ok1;
    }

    public int isOk2() {
        return ok2;
    }

    public void setOk2(int ok2) {
        this.ok2 = ok2;
    }

    @Override
    public Pair<String, String> getID() {
        return this.IDUser;
    }

    @Override
    public void setID(Pair<String, String> user) {
        this.IDUser = user;
    }
}
