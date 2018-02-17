package Repository;

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

public class DBRepositoryStudent extends DBAbstractRepository<UUID, Student> {

    private DatabaseManager databaseManager;

    private Statement stmt = null;
    private ResultSet rs = null;

    public DBRepositoryStudent(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    @Override
    public Optional<Student> save(Student entity) throws ValidationException, FileNotFoundException, UnsupportedEncodingException {
        String SQL = "INSERT INTO Student VALUES ('" + entity.getID().toString() + "', '" + entity.getCodMatricol() + "', '" + entity.getName() + "', "
                + entity.getGroup() + ", '" + entity.getEmail() + "', '" + entity.getTeacher() + "');";
        try {
            stmt = databaseManager.con.createStatement();
            stmt.executeUpdate(SQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<Student> delete(UUID uuid) throws FileNotFoundException, UnsupportedEncodingException {
        String SQL = "DELETE FROM Student where id='" + uuid.toString() + "';";
        Optional<Student> toRemove = this.getEntity(uuid);
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
    public Optional<Student> update(Student entity) throws FileNotFoundException, UnsupportedEncodingException, ValidationException {
        String SQL = "UPDATE Student " +
                " set registrationNumber = '" + entity.getCodMatricol() + "', studentname='" + entity.getName() + "', studentgroup=" + entity.getGroup()
                + ", email = '"+ entity.getEmail() + "', teacher = '" + entity.getTeacher() + "' where id='" + entity.getID()+ "';";
        Optional<Student> toRemove = this.getEntity(entity.getID());
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
    public Optional<Student> getEntity(UUID uuid) {
        String SQL = "SELECT * FROM Student where id='" + uuid.toString() +"';";
        try {
            stmt = databaseManager.con.createStatement();
            rs = stmt.executeQuery(SQL);
            if(rs.next()){
                return Optional.of(new Student(java.util.UUID.fromString(rs.getString(1)), rs.getString(2), rs.getString(3), Integer.valueOf(rs.getString(4)), rs.getString(5), rs.getString(6)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Student> getAll() {
        List<Student> students = new ArrayList<>();
        String SQL = "SELECT * FROM Student;";
        try {
            stmt = databaseManager.con.createStatement();
            rs = stmt.executeQuery(SQL);
            while (rs.next()) {
                students.add(new Student(java.util.UUID.fromString(rs.getString(1)), rs.getString(2), rs.getString(3), Integer.valueOf(rs.getString(4)), rs.getString(5), rs.getString(6)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }

    @Override
    public List<Student> getPage(int pageNumber) {
        List<Student> students = new ArrayList<>();
        String SQL = "SELECT * FROM Student OFFSET " + pageNumber*10  + "LIMIT " + 10 +";";
        try {
            stmt = databaseManager.con.createStatement();
            rs = stmt.executeQuery(SQL);
            while (rs.next()) {
                students.add(new Student(java.util.UUID.fromString(rs.getString(1)), rs.getString(2), rs.getString(3), Integer.valueOf(rs.getString(4)), rs.getString(5), rs.getString(6)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }

    @Override
    public long size() {
        String SQL = "SELECT COUNT(*) FROM Student;";
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
