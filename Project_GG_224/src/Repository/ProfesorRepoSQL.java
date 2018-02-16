package Repository;

import Domain.Profesor;
import Domain.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProfesorRepoSQL {
    private List<Profesor> list;

    public ProfesorRepoSQL() {
        this.list = new ArrayList<>();
        load();
    }

    private Connection getConnection() {
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        String url = "jdbc:jtds:sqlserver://localhost:1433/java;instance=SQLEXPRESS";
        String user = "java";
        String password = "whatever";
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    void load() {
        Connection connection = getConnection();
        String query = "SELECT * FROM Profesori";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rez = statement.executeQuery();
            while (rez.next()) {
                String account = rez.getString("account");
                String nume = rez.getString("nume");
                int id = rez.getInt("id");
                save(new Profesor(1, nume, account));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void save(Profesor entity) {
        list.add(entity);
    }

    public List<Profesor> getAll() {
        return list;
    }

    public void changePassword(String username, String pass) throws SQLException {
        Connection connection = getConnection();
        String query = "UPDATE users SET pass = ? " +
                "WHERE account=?";

        PreparedStatement preparedStmt = connection.prepareStatement(query);
        preparedStmt.setString(1, pass);
        preparedStmt.setString(2, username);
        preparedStmt.execute();
    }

    public List<String> numeProfesori() {

    List list=new ArrayList();
    Connection connection = getConnection();

    String query = "SELECT nume FROM Profesori";
        try

    {
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet rez = statement.executeQuery();
        while (rez.next()) {
            String account = rez.getString("nume");
            list.add(account);
        }
    } catch(
    SQLException e)

    {
        e.printStackTrace();
    }
    return list;
}




    public int numeProfesoriElevi(String nume) throws SQLException {

        int nr=0;
        Connection connection = getConnection();
        System.out.println(nume);
        String query = "SELECT Count(*) AS nrStudenti FROM Student WHERE indrumator=?";


            PreparedStatement statement = connection.prepareStatement(query);

            statement.setString(1, nume);

            ResultSet rez = statement.executeQuery();
        while (rez.next()) {
            int numar = rez.getInt("nrStudenti");
            nr=numar;
        }


        return nr;

    }

    }
