package Repository;

import Domain.Project;
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

public class DBRepositoryProject extends DBAbstractRepository<UUID, Project> {
    private DatabaseManager databaseManager;

    private Statement stmt = null;
    private ResultSet rs = null;

    public DBRepositoryProject(DatabaseManager databaseManager){
        this.databaseManager = databaseManager;
    }


    @Override
    public Optional<Project> save(Project entity) throws ValidationException, FileNotFoundException, UnsupportedEncodingException {
        String SQL = "INSERT INTO Project VALUES ('" + entity.getID().toString() + "', '" + entity.getDescription() + "', " + entity.getDeadline() + ");";
        try {
            stmt = databaseManager.con.createStatement();
            stmt.executeUpdate(SQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<Project> delete(UUID uuid) throws FileNotFoundException, UnsupportedEncodingException {
        String SQL = "DELETE FROM Project where id='" + uuid.toString() + "';";
        Optional<Project> toRemove = this.getEntity(uuid);
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
    public Optional<Project> update(Project entity) throws FileNotFoundException, UnsupportedEncodingException, ValidationException {
        String SQL = "UPDATE Project set description ='" + entity.getDescription() + "', deadline =" + entity.getDeadline() + "where id='" + entity.getID() + "';";
        Optional<Project> toRemove = this.getEntity(entity.getID());
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
    public Optional<Project> getEntity(UUID uuid) {
        String SQL = "SELECT * FROM Project where id='" + uuid.toString() + "';";
        try {
            stmt = databaseManager.con.createStatement();
            rs = stmt.executeQuery(SQL);
            if(rs.next()){
                return Optional.of(new Project(java.util.UUID.fromString(rs.getString(1)), rs.getString(2), Integer.valueOf(rs.getString(3))));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Iterable<Project> getAll() {
        List<Project> projects = new ArrayList<>();
        String SQL = "SELECT * FROM Project;";
        try {
            stmt = databaseManager.con.createStatement();
            rs = stmt.executeQuery(SQL);
            while (rs.next()) {
                projects.add(new Project(java.util.UUID.fromString(rs.getString(1)), rs.getString(2), Integer.valueOf(rs.getString(3))));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return projects;
    }

    @Override
    public List<Project> getPage(int pageNumber) {
        List<Project> projects = new ArrayList<>();
        String SQL = "SELECT * FROM Project OFFSET " + pageNumber*10  + "LIMIT " + 10 +";";
        try {
            stmt = databaseManager.con.createStatement();
            rs = stmt.executeQuery(SQL);
            while (rs.next()) {
                projects.add(new Project(java.util.UUID.fromString(rs.getString(1)), rs.getString(2), Integer.valueOf(rs.getString(3))));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return projects;
    }

    @Override
    public long size() {
        String SQL = "SELECT COUNT(*) FROM Project;";
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
