package Domain;

public class LoginObject implements HasID<String> {

    private String user;
    private String password;
    private String email;
    private Integer priority;

    public LoginObject() {
    }

    public LoginObject(String email, String u,String passwd,Integer priority) {
        this.setUser(u);
        this.setPassword(passwd);
        this.setId(email);
        this.setPriority(priority);
    }


    public void setId(String id) {
        this.email=id;
    }

    public String getId(){
        return email;
    }

    public void setUser(String user) {
        this.user = user;
    }


    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword(){
        return password;
    }
    public String getUser(){
        return user;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Integer getPriority(){
        return priority;
    }
}
