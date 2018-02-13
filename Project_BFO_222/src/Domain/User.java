package Domain;

public class User {
    private String username;
    private String password;
    private String categorie;
    public User(){

    }
    public User(String username,String password,String categorie){
        this.password=password;
        this.username=username;
        this.categorie=categorie;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getCategorie(){
        return this.categorie;
    }

    public void setUsername(String username){
        this.username=username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }
}
