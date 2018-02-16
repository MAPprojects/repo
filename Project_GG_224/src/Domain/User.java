package Domain;

public class User {
    private String account;
    private String pass;
    private int profesor;

    public User(String account, String pass, int profesor) {
        this.account = account;
        this.pass = pass;
        this.profesor = profesor;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }


    public int getProfesor() {
        return profesor;
    }

    public void setProfesor(int profesor) {
        this.profesor = profesor;
    }
}
