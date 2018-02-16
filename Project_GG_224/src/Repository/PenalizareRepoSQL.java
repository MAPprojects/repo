package Repository;



import Domain.Penalizare;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PenalizareRepoSQL {
    private List<Penalizare> list;

    public PenalizareRepoSQL() {
        this.list = new ArrayList<>();
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
        String query= "SELECT * FROM Penalizare";
        try {
            PreparedStatement statement=connection.prepareStatement(query);
            ResultSet rez=statement.executeQuery();
            while (rez.next()){
                int nrTema=rez.getInt("nrTema");
                int idStudent=rez.getInt("idStudent");

                saveDatabse(new Penalizare(nrTema,idStudent));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveDatabse(Penalizare entity) throws SQLException {
        list.add(entity);}
    public void save(Penalizare entity) throws SQLException {
        list.add(entity);
        if (entity != null) {
            Connection connection=getConnection();
            String query = "INSERT INTO Penalizare(nrTema, idStudent) " +"values(?,?);";
            PreparedStatement preparedStmt = connection.prepareStatement(query);
            preparedStmt.setInt(1,entity.getNrTema());
            preparedStmt.setInt(2,entity.getIdStudent());
            preparedStmt.execute();

        }
    }

    public List<Penalizare> getAll() {
        return list;
    }


}
