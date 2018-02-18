package Repository;

import Domain.*;
import MVC.MainController;

import java.sql.*;
import java.util.Optional;

public class RepositoryDatabaseNote extends AbstractRepositoryDatabase<String, Nota> {
    private IRepository<Integer, Student> repoStudenti;
    private IRepository<Integer, Tema> repoTeme;
    public RepositoryDatabaseNote(Validator<Nota> validator,
                                  IRepository<Integer,Student> rs,
                                  IRepository<Integer,Tema> rt) throws ValidationException{
        super(validator);
        repoStudenti = rs;
        repoTeme = rt;
        readFromDatabase();
    }

    @Override
    public void readFromDatabase() throws ValidationException{
        databaseConnection();
        try {
            Connection conn = DriverManager.getConnection(url);
            String query = "SELECT * FROM Note";
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                try {
                    String id = rs.getString("id");
                    Integer idStudent = rs.getInt("idStudent");
                    Integer nrTemaLaborator = rs.getInt("nrTemaLaborator");
                    Integer valoare = rs.getInt("valoare");
                    Optional<Student> student=repoStudenti.findOne(idStudent);
                    Optional<Tema> tema=repoTeme.findOne(nrTemaLaborator);
                    if (student.isPresent() && tema.isPresent()){
                        Nota nota = new Nota(id,student.get(),tema.get(),valoare);
                        super.saveWithNoInsert(nota);
                    }
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
    public void insertIntoDatabase(Nota nota){
        databaseConnection();
        try {
            Connection conn = DriverManager.getConnection(url);
            String insertTableSQL="INSERT INTO Note(id,idStudent,nrTemaLaborator,valoare) VALUES (?,?,?,?)";
            PreparedStatement statement = conn.prepareStatement(insertTableSQL);
            statement.setString(1,nota.getId());
            statement.setInt(2,nota.getIdStudent());
            statement.setInt(3,nota.getIdTema());
            statement.setInt(4,nota.getValoare());
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            e.getMessage();
        }
    }

    @Override
    public void updateDatabase(Nota nota){
        databaseConnection();
        try {
            Connection conn = DriverManager.getConnection(url);
            String updateTableSQL="UPDATE Note SET id=?,idStudent=?,nrTemaLaborator=?,valoare? WHERE id=?";
            PreparedStatement statement = conn.prepareStatement(updateTableSQL);
            statement.setString(1,nota.getId());
            statement.setInt(2,nota.getIdStudent());
            statement.setInt(3,nota.getIdTema());
            statement.setInt(4,nota.getValoare());
            statement.setString(5,nota.getId());
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
            String deleteSQL="DELETE FROM Note WHERE id=?";
            PreparedStatement statement = conn.prepareStatement(deleteSQL);
            statement.setString(1,id);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
