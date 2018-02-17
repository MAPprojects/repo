package Repository;

import Domain.Grade;
import Domain.Project;
import Domain.Student;
import Manager.DatabaseManager;
import Validate.ValidationException;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class DBRepositoryGrade extends DBAbstractRepository<UUID, Grade> {
    private DatabaseManager databaseManager;

    private Statement stmt = null;
    private ResultSet rs = null;

    private DBAbstractRepository<UUID, Student> studentRepository;
    private DBAbstractRepository<UUID, Project> projectRepository;

    public DBRepositoryGrade(DatabaseManager databaseManager, DBAbstractRepository<UUID, Student> studentRepository, DBAbstractRepository<UUID, Project> projectRepository) {
        this.databaseManager = databaseManager;
        this.studentRepository = studentRepository;
        this.projectRepository = projectRepository;
    }

    @Override
    public Optional<Grade> save(Grade entity) throws ValidationException, FileNotFoundException, UnsupportedEncodingException {
        int isLate = 0;
        if(entity.isDelay()){
            isLate=1;
        }
        String SQL = "INSERT INTO Grade VALUES ('" + entity.getID().toString() + "', '" + entity.getStudent().getID() + "', '" + entity.getProject().getID()
                + "', "+ entity.getGrade() + ", " + entity.getWeek() + ", CAST(" + isLate + " AS BIT));";
        try {
            stmt = databaseManager.con.createStatement();
            stmt.executeUpdate(SQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<Grade> delete(UUID uuid) throws FileNotFoundException, UnsupportedEncodingException {
        String SQL = "DELETE FROM Grade where id='" + uuid.toString() + "';";
        Optional<Grade> toRemove = this.getEntity(uuid);
        try {
            stmt = databaseManager.con.createStatement();
            stmt.executeUpdate(SQL);
            return toRemove;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<Grade> update(Grade entity) throws FileNotFoundException, UnsupportedEncodingException, ValidationException {
        int isLate = 0;
        if(entity.isDelay()){
            isLate=1;
        }
        String SQL = "UPDATE Grade set grade = " + entity.getGrade() + ", weekAssigned=" + entity.getWeek() + ", isLate=CAST(" + isLate + " AS BIT)" + " where id='" + entity.getID() + "';";
        Optional<Grade> toRemove = this.getEntity(entity.getID());
        try {
            stmt = databaseManager.con.createStatement();
            stmt.executeUpdate(SQL);
            return toRemove;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<Grade> getEntity(UUID uuid) {
        String SQL = "SELECT * FROM Grade where id='" + uuid.toString() + "';";
        try {
            stmt = databaseManager.con.createStatement();
            rs = stmt.executeQuery(SQL);
            boolean isLate = false;
            if(rs.next()){
                if(Integer.valueOf(rs.getString(6))==1){
                    isLate=true;
                }
                return Optional.of(new Grade(java.util.UUID.fromString(rs.getString(1)), studentRepository.getEntity(java.util.UUID.fromString(rs.getString(2))).get(), projectRepository.getEntity(java.util.UUID.fromString(rs.getString(3))).get(), Integer.parseInt(rs.getString(4)), Integer.parseInt(rs.getString(5)), isLate));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Iterable<Grade> getAll() {
        List<Grade> grades = new ArrayList<>();
        String SQL = "SELECT * FROM Grade;";
        try {
            stmt = databaseManager.con.createStatement();
            rs = stmt.executeQuery(SQL);
            while (rs.next()) {
                boolean isLate = false;
                if(Integer.valueOf(rs.getString(6))==1){
                    isLate=true;
                }
                grades.add(new Grade(java.util.UUID.fromString(rs.getString(1)), studentRepository.getEntity(java.util.UUID.fromString(rs.getString(2))).get(), projectRepository.getEntity(java.util.UUID.fromString(rs.getString(3))).get(), Integer.parseInt(rs.getString(4)), Integer.parseInt(rs.getString(5)), isLate));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return grades;
    }

    @Override
    public List<Grade> getPage(int pageNumber) {
        List<Grade> grades = new ArrayList<>();
        String SQL = "SELECT * FROM Grade OFFSET " + pageNumber*10  + "LIMIT " + 10 +";";
        try {
            stmt = databaseManager.con.createStatement();
            rs = stmt.executeQuery(SQL);
            while (rs.next()) {
                boolean isLate = false;
                if(Integer.valueOf(rs.getString(6))==1){
                    isLate=true;
                }
                grades.add(new Grade(java.util.UUID.fromString(rs.getString(1)), studentRepository.getEntity(java.util.UUID.fromString(rs.getString(2))).get(), projectRepository.getEntity(java.util.UUID.fromString(rs.getString(3))).get(), Integer.parseInt(rs.getString(4)), Integer.parseInt(rs.getString(5)), isLate));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return grades;
    }

    @Override
    public long size() {
        String SQL = "SELECT COUNT(*) FROM Grade;";
        try {
            stmt = databaseManager.con.createStatement();
            rs = stmt.executeQuery(SQL);
            if(rs.next()){
                return Integer.valueOf(rs.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
