package Domain;

public class User implements HasId<String> {
    private String username, password;
    private Category category;

    public User(String username, String password, Category category) {
        this.username = username;
        this.password = password;
        this.category = category;
    }

    public User() {}

    public String getId() {
        return username;
    }

    public void setId(String name) {
        this.username = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
