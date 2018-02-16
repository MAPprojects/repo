package Repository;

import Domain.Teme;


import java.sql.*;

public class TemeRepoSQL extends TemeRepo {
    public TemeRepoSQL(Validator<Teme> vali) {
        super(vali);
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
        String query= "SELECT * FROM Tema";
        try {
            PreparedStatement statement=connection.prepareStatement(query);
            ResultSet rez=statement.executeQuery();
            while (rez.next()){
                int nrTema=rez.getInt("nrTema");
                String descriere=rez.getString("descriere");
                int deadline=rez.getInt("deadline");
                super.save(new Teme(nrTema,deadline,descriere));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save(Teme t) throws ValidationException, SQLException {
        super.save(t);
        if (t != null) {
            Connection connection=getConnection();
            String query = "INSERT INTO Tema(nrTema,deadline,descriere) " +"values(?,?,?);";
            PreparedStatement preparedStmt = connection.prepareStatement(query);
            preparedStmt.setInt(1,t.getNrTema());
            preparedStmt.setInt(2,t.getDeadline());
            preparedStmt.setString(3,t.getDescriere());
            preparedStmt.execute();

        }
    }
    @Override
    public  void update(Teme t ,Teme t1) throws ValidationException, SQLException {
        super.update(t,t1);
        Connection connection=getConnection();
        String query = "UPDATE Tema SET deadline = ? , "
                + "descriere=? "
                + "WHERE nrTema = ?" ;
        PreparedStatement preparedStmt = connection.prepareStatement(query);
        preparedStmt.setInt(1,t1.getDeadline());
        preparedStmt.setString(2,t1.getDescriere());
        preparedStmt.setInt(3,t1.getNrTema());
        preparedStmt.execute();


    }
    @Override
    public  void delete(Teme t) throws SQLException {
        super.delete(t);
        Connection connection=getConnection();
        String query = "delete FROM Tema where nrTema=?";
        PreparedStatement preparedStmt = connection.prepareStatement(query);
        preparedStmt.setInt(1,t.getNrTema());
        preparedStmt.execute();
        String queryNota = "delete FROM Nota where nrTema=?";
        PreparedStatement preparedStmt1 = connection.prepareStatement(queryNota);
        preparedStmt1.setInt(1,t.getNrTema());
        preparedStmt1.execute();
        String queryNota1 = "delete FROM Penalizare where nrTema=?";
        PreparedStatement preparedStmt2 = connection.prepareStatement(queryNota1);

        preparedStmt2.setInt(1,t.getNrTema());
        preparedStmt2.executeUpdate();


    }


}
