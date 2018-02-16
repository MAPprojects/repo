package Repository;

import Domain.Studenti;


import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;

public class StudentRepoSQL extends StudentRepo {
    public StudentRepoSQL(Validator<Studenti> vali) {
        super(vali);
        load();
    }
    public String createUser(String nume, int id){
        String output="";

        for (String s : nume.split(" "))
            output+=s.charAt(0);
        output+=String.valueOf(id);
        return output;
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
        String query= "SELECT * FROM Student";
        try {
            PreparedStatement statement=connection.prepareStatement(query);
            ResultSet rez=statement.executeQuery();
            while (rez.next()){
                int idStudent=rez.getInt("idStudent");
                String nume=rez.getString("nume");
                int grupa=rez.getInt("grupa");
                String email=rez.getString("email");
                String indrumator=rez.getString("indrumator");
                super.save(new Studenti(idStudent,nume,grupa,email,indrumator));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void save(Studenti t) throws ValidationException, SQLException {
        super.save(t);
        if (t != null) {
            Connection connection=getConnection();
            String query = "INSERT INTO Student(idStudent, nume, grupa, email, indrumator,cont) " +"values(?,?,?,?,?,?);";
            PreparedStatement preparedStmt = connection.prepareStatement(query);
            preparedStmt.setInt(1,t.getIdStudent());
            preparedStmt.setString(2,t.getNume());
            preparedStmt.setInt(3,t.getGrupa());
            preparedStmt.setString(4,t.getEmail());
            preparedStmt.setString(5,t.getIndrumator());
            preparedStmt.setString(6,createUser(t.getNume().toLowerCase(),t.getIdStudent()));
            preparedStmt.execute();
            String query1 = "INSERT INTO users(account,pass,profesor) " +"values(?,?,?);";
            PreparedStatement preparedStmt1 = connection.prepareStatement(query1);
            preparedStmt1.setString(1,createUser(t.getNume().toLowerCase(),t.getIdStudent()));
            preparedStmt1.setString(2,getMD5(createUser(t.getNume().toLowerCase(),t.getIdStudent())));
            preparedStmt1.setInt(3,0);
            preparedStmt1.execute();
        }
    }
    @Override
    public  void update(Studenti t ,Studenti t1) throws ValidationException, SQLException {
//        super.update(t,t1);
        Connection connection=getConnection();
        String query = "UPDATE Student SET nume = ? , "
                + "grupa=? ,"
                +"email=? ,"
                +" indrumator = ? "
                + "WHERE idStudent = ?" ;
        PreparedStatement preparedStmt = connection.prepareStatement(query);
        preparedStmt.setString(1,t1.getNume());
        preparedStmt.setInt(2,t1.getGrupa());
        preparedStmt.setString(3,t1.getEmail());
        preparedStmt.setString(4,t1.getIndrumator());
        preparedStmt.setInt(5,t1.getIdStudent());
        preparedStmt.execute();
        String query1 = "UPDATE  users SET account=?,"
                        +"pass=?" +
                        "WHERE  account=?";
        PreparedStatement preparedStmt1 = connection.prepareStatement(query1);
        preparedStmt1.setString(1,createUser(t1.getNume().toLowerCase(),t.getIdStudent()));
        preparedStmt1.setString(2,getMD5(createUser(t1.getNume().toLowerCase(),t.getIdStudent())));
        preparedStmt1.setString(3,createUser(t.getNume().toLowerCase(),t.getIdStudent()));

        preparedStmt1.executeUpdate();

    }
    @Override
    public  void delete(Studenti t) throws SQLException {
        super.delete(t);
        Connection connection=getConnection();
        String query = "delete FROM Student where idStudent=?";
        PreparedStatement preparedStmt = connection.prepareStatement(query);
        preparedStmt.setInt(1,t.getIdStudent());
        preparedStmt.execute();
        String queryNota = "delete FROM Nota where idStudent=?";
        PreparedStatement preparedStmt1 = connection.prepareStatement(queryNota);
        preparedStmt1.setInt(1,t.getIdStudent());
        preparedStmt1.execute();
        String queryUser = "delete FROM users where account=?";
        PreparedStatement preparedStmt2 = connection.prepareStatement(queryUser);
        preparedStmt2.setString(1,createUser(t.getNume().toLowerCase(),t.getIdStudent()));
        preparedStmt2.execute();
        String queryNota1 = "delete FROM Penalizare where idStudent=?";
        PreparedStatement preparedStmt3 = connection.prepareStatement(queryNota1);
        preparedStmt3.setInt(1,t.getIdStudent());
        preparedStmt3.executeUpdate();

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
    public void changePassword(String username, String pass) throws SQLException {
        Connection connection=getConnection();
        String query = "UPDATE users SET pass = ? "+
                        "WHERE account=?";

        PreparedStatement preparedStmt = connection.prepareStatement(query);
        preparedStmt.setString(1,getMD5(pass));
        preparedStmt.setString(2,username);
        preparedStmt.execute();
    }


    public Studenti getByUsername(String username) {
        Connection connection = getConnection();
        String query = "SELECT * FROM Student WHERE cont=?";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            ResultSet rez = statement.executeQuery();
            while (rez.next()) {
                int idStudent = rez.getInt("idStudent");
                String nume = rez.getString("nume");
                int grupa = rez.getInt("grupa");
                String email = rez.getString("email");
                String indrumator = rez.getString("indrumator");
                return new Studenti(idStudent, nume, grupa, email, indrumator);
            }

        } catch (SQLException e) {
            e.printStackTrace();

        }
        return null;
    }
}
