package Repository;

import Domain.Intarziere;
import Domain.ValidationException;
import Domain.Validator;
import MVC.MainController;

import java.sql.*;
import java.util.Optional;

public class RepositoryDatabaseIntarzieri extends AbstractRepositoryDatabase<String, Intarziere>{
    public RepositoryDatabaseIntarzieri(Validator<Intarziere> validator) throws ValidationException {
        super(validator);
        readFromDatabase();
    }

    @Override
    public void readFromDatabase() throws ValidationException{
        databaseConnection();
        try {
            Connection conn = DriverManager.getConnection(url);
            String query = "SELECT * FROM Intarzieri";
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                try {
                    String id = rs.getString("id");
                    Integer idStudent = rs.getInt("idStudent");
                    Integer nrTemaLaborator = rs.getInt("nrTemaLaborator");
                    Integer nrSaptamani = rs.getInt("nrSaptamani");
                    Intarziere intarziere=new Intarziere(idStudent,nrTemaLaborator,nrSaptamani);
                    super.saveWithNoInsert(intarziere);

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
    public void insertIntoDatabase(Intarziere intarziere){
        databaseConnection();
        try {
            Connection conn = DriverManager.getConnection(url);
            String insertTableSQL="INSERT INTO Intarzieri(id,idStudent,nrTemaLaborator,nrSaptamani) VALUES (?,?,?,?)";
            PreparedStatement statement = conn.prepareStatement(insertTableSQL);
            statement.setString(1,intarziere.getId());
            statement.setInt(2,intarziere.getIdStudent());
            statement.setInt(3,intarziere.getNrTemaLaborator());
            statement.setInt(4,intarziere.getNrSaptamani());
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            e.getMessage();
        }
    }

    @Override
    public void updateDatabase(Intarziere intarziere){
        databaseConnection();
        try {
            Connection conn = DriverManager.getConnection(url);
            String updateTableSQL="UPDATE Intarzieri SET id=?,idStudent=?,nrTemaLaborator=?,nrSaptamani? WHERE id=?";
            PreparedStatement statement = conn.prepareStatement(updateTableSQL);
            statement.setString(1,intarziere.getId());
            statement.setInt(2,intarziere.getIdStudent());
            statement.setInt(3,intarziere.getNrTemaLaborator());
            statement.setInt(4,intarziere.getNrSaptamani());
            statement.setString(5,intarziere.getId());
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            e.getMessage();
        }
    }

    @Override
    public void deleteFromDatabase(String id){
        databaseConnection();
        try {
            Connection conn = DriverManager.getConnection(url);
            String deleteSQL="DELETE FROM Inatrzieri WHERE id=?";
            PreparedStatement statement = conn.prepareStatement(deleteSQL);
            statement.setString(1,id);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
