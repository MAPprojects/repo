package Repository;

import Domain.Profesor;
import Domain.Studenti;
import Domain.User;
import Utils.ListEvent;
import Utils.ListEventType;
import Utils.Observabel;
import Utils.Observer;


import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepo implements Observabel<User> {

    ArrayList<Observer<User>> userObserver=new ArrayList<>();
    private List<User> list;

    public UserRepo() {
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

    @Override
    public void addObserver(Observer<User> o) {
        userObserver.add(o);

    }

    @Override
    public void removeObserver(Observer<User> o) {
        userObserver.remove(o);

    }

    @Override
    public void notifyObservers(ListEvent<User> event) throws SQLException {
        userObserver.forEach(x -> {
            try {
                x.notifyEvent(event);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }



    public  <E> ListEvent<E> createEvent(ListEventType type, final E elem, final List<E> l) {
        return new ListEvent<E>(type) {
            @Override
            public List<E> getList() {
                return l;
            }

            @Override
            public E getElement() {
                return elem;
            }
        };
    }
    public List<User> getAllUsers() {
        return list;
    }

    public String createUser(String nume, int id){
        String output="";

        output=nume.replaceAll("\\s+","").toLowerCase()+String.valueOf(id);
        return output;
    }
    public static String getMD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger number = new BigInteger(1, messageDigest);
            String hashtext = number.toString(16);
            // Now we need to zero pad it if you actually want the full 32 chars.
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveProfesor(Profesor t) throws ValidationException, SQLException {
        if (t != null) {
            Connection connection=getConnection();
            String query = "INSERT INTO Profesori(id,nume,account) " +"values(?,?,?)";
            PreparedStatement preparedStmt = connection.prepareStatement(query);
            preparedStmt.setInt(1,t.getId());
            preparedStmt.setString(2,t.getNume());
            preparedStmt.setString(3,createUser(t.getNume().toLowerCase(),t.getId()));
            preparedStmt.execute();
            String query1 = "INSERT INTO users(account,pass,profesor) " +"values(?,?,?);";
            PreparedStatement preparedStmt1 = connection.prepareStatement(query1);
            preparedStmt1.setString(1,createUser(t.getNume().toLowerCase(),t.getId()));
            preparedStmt1.setString(2,getMD5(createUser(t.getNume().toLowerCase(),t.getId())));
            preparedStmt1.setInt(3,1);
            preparedStmt1.execute();
        }
        ListEvent<User> ev = createEvent(ListEventType.ADD, new User(createUser(t.getNume().toLowerCase(),t.getId()),createUser(t.getNume().toLowerCase(),t.getId()),1), list);
        notifyObservers(ev);
    }
}
