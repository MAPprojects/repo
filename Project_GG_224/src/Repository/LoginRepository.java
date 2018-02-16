package Repository;

import Domain.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LoginRepository {
    private List<User> list;

    public LoginRepository() {
        this.list=new ArrayList<>();
        load();
    }

    private Connection getConnection(){
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        String url="jdbc:jtds:sqlserver://localhost:1433/java;instance=SQLEXPRESS";
        String user="java";
        String password="whatever";
        Connection connection=null;
        try {
            connection= DriverManager.getConnection(url);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
    void load(){
        Connection connection=getConnection();
        String query= "SELECT * FROM users";
        try {
            PreparedStatement statement=connection.prepareStatement(query);
            ResultSet rez=statement.executeQuery();
            while (rez.next()){
                String account=rez.getString("account");
                String pass=rez.getString("pass");
                int profesor=rez.getInt("profesor");
                save(new User(account,pass,profesor));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void save(User entity){
        list.add(entity);
    }
    public List<User> getAll() {
        return list;
    }

    public User usersExist(String account){
        for (User user:getAll()) {
            if(user.getAccount().equals(account))
                return user;
        }
        return null;
    }

}
