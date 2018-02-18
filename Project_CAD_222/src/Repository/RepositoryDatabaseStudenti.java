package Repository;

import Domain.Student;
import Domain.ValidationException;
import Domain.Validator;
import MVC.MainController;

import java.sql.*;

public class RepositoryDatabaseStudenti extends AbstractRepositoryDatabase<Integer, Student> {
    public RepositoryDatabaseStudenti(Validator<Student> validator) throws ValidationException {
        super(validator);
        readFromDatabase();
    }

    @Override
    public void readFromDatabase() throws ValidationException{
        databaseConnection();
        try {
            Connection conn = DriverManager.getConnection(url);
            String query = "SELECT * FROM Studenti";
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                try {
                    Integer idStudent = rs.getInt("idStudent");
                    String nume = rs.getString("nume");
                    Integer grupa = rs.getInt("grupa");
                    String email = rs.getString("email");
                    String profIndrumator = rs.getString("profIndrumator");
                    Student student = new Student(idStudent,nume,grupa,email,profIndrumator);
                    super.saveWithNoInsert(student);
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
    public void insertIntoDatabase(Student student){
        databaseConnection();
        try {
            Connection conn = DriverManager.getConnection(url);
            String insertTableSQL="INSERT INTO Studenti(idStudent, nume, grupa, email, profIndrumator) VALUES (?,?,?,?,?)";
            PreparedStatement statement = conn.prepareStatement(insertTableSQL);
            statement.setInt(1,student.getId());
            statement.setString(2,student.getNume());
            statement.setInt(3,student.getGrupa());
            statement.setString(4,student.getEmail());
            statement.setString(5,student.getProfIndrumator());
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            e.getMessage();
        }
    }

    @Override
    public void updateDatabase(Student student){
        databaseConnection();
        try {
            Connection conn = DriverManager.getConnection(url);
            String updateTableSQL="UPDATE Studenti SET idStudent=?,nume=?,grupa=?,email=?,profIndrumator=? WHERE idStudent=?";
            PreparedStatement statement = conn.prepareStatement(updateTableSQL);
            statement.setInt(1,student.getId());
            statement.setString(2,student.getNume());
            statement.setInt(3,student.getGrupa());
            statement.setString(4,student.getEmail());
            statement.setString(5,student.getProfIndrumator());
            statement.setInt(6,student.getId());
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            e.getMessage();
        }
    }

    @Override
    public void deleteFromDatabase(Integer idStudent){
        databaseConnection();
        try {
            Connection conn = DriverManager.getConnection(url);
            String deleteSQL="DELETE FROM Studenti WHERE idStudent=?";
            PreparedStatement statement = conn.prepareStatement(deleteSQL);
            statement.setInt(1,idStudent);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
