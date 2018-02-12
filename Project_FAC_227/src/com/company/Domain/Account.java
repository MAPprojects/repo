package com.company.Domain;

public class Account {
    String username;
    String password;
    int accessLevel;

    public Account(String username, String password, int accessLevel) {
        this.username = username;
        this.password = password;
        this.accessLevel = accessLevel;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getAccessLevel() {
        return accessLevel;
    }
}
