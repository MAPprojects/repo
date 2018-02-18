package Domain;

public class User implements HasID<String>
{
    String email;
    String password;
    String rol;

    public User(String email, String password,String rol) {
        this.email = email;
        this.password = password;
        this.rol=rol;
    }

    public String getRol() {
        return rol;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String getID() {
        return email;
    }

    @Override
    public void setID(String  email) {
        this.email=email;

    }
}
