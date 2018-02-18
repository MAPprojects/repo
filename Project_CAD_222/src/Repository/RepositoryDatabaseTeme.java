package Repository;

import Domain.Tema;
import Domain.ValidationException;
import Domain.Validator;
import MVC.MainController;
import com.sun.org.apache.xalan.internal.xsltc.runtime.ErrorMessages;

import java.sql.*;

public class RepositoryDatabaseTeme extends AbstractRepositoryDatabase<Integer, Tema> {

    public RepositoryDatabaseTeme(Validator<Tema> validator) throws ValidationException{
        super(validator);
        readFromDatabase();
    }

    @Override
    public void readFromDatabase() throws ValidationException{
        databaseConnection();
        try {
            Connection conn = DriverManager.getConnection(url);
            String query = "SELECT * FROM Teme";
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                try {
                    Integer nrTemaLaborator = rs.getInt("nrTemaLaborator");
                    String descriere = rs.getString("descriere");
                    Integer deadline = rs.getInt("deadline");
                    Tema tema = new Tema(nrTemaLaborator, descriere, deadline);
                    super.saveWithNoInsert(tema);
                } catch (NumberFormatException | ValidationException e) {
                    MainController.showErrorMessage(e.getMessage());
                }
            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            e.getMessage();
        }
    }

    @Override
    public void insertIntoDatabase(Tema tema){
        databaseConnection();
        try {
            Connection conn = DriverManager.getConnection(url);
            String insertTableSQL="INSERT INTO Teme(nrTemaLaborator, descriere, deadline) VALUES (?,?,?)";
            PreparedStatement statement = conn.prepareStatement(insertTableSQL);
            statement.setInt(1,tema.getId());
            statement.setString(2,tema.getDescriere());
            statement.setInt(3,tema.getDeadline());
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            e.getMessage();
        }
    }

    @Override
    public void updateDatabase(Tema tema){
        databaseConnection();
        try {
            Connection conn = DriverManager.getConnection(url);
            String updateTableSQL="UPDATE Teme SET nrTemaLaborator=?,descriere=?,deadline=? WHERE nrTemaLaborator=?";
            PreparedStatement statement = conn.prepareStatement(updateTableSQL);
            statement.setInt(1,tema.getId());
            statement.setString(2,tema.getDescriere());
            statement.setInt(3,tema.getDeadline());
            statement.setInt(4,tema.getId());
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            e.getMessage();
        }
    }

    @Override
    public void deleteFromDatabase(Integer nrTemaLaborator){
        databaseConnection();
        try {
            Connection conn = DriverManager.getConnection(url);
            String deleteSQL="DELETE FROM Teme WHERE nrTemaLaborator=?";
            PreparedStatement statement = conn.prepareStatement(deleteSQL);
            statement.setInt(1,nrTemaLaborator);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
