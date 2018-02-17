package Repository;

import Manager.DatabaseManager;
import Validate.ValidationException;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class DBRepositoryLogin {
    private DatabaseManager databaseManager;
    private Statement stmt = null;
    private ResultSet rs = null;

    public DBRepositoryLogin(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public boolean save(String email, String password, String table) throws ValidationException, FileNotFoundException, UnsupportedEncodingException {
        String SQL = "INSERT INTO " + table + " VALUES ('" + email + "', '" + password + "');";
        try {
            stmt = databaseManager.con.createStatement();
            stmt.executeUpdate(SQL);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete(String email, String table) throws FileNotFoundException, UnsupportedEncodingException {
        String SQL = "DELETE FROM " + table + " where email='" + email + "';";
        if(this.getEntity(email, table).compareTo("")==0){
            return false;
        }
        try {
            stmt = databaseManager.con.createStatement();
            stmt.executeUpdate(SQL);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getEntity(String email, String table) {
        String SQL = "SELECT * FROM " + table + " where email='" +email +"';";
        try {
            stmt = databaseManager.con.createStatement();
            rs = stmt.executeQuery(SQL);
            if(rs.next()){
                return rs.getString(2);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

    public HashMap<String, String> getAll(String table) {
        HashMap<String, String> loggedinStudents = new HashMap<>();
        String SQL = "SELECT * FROM " + table + ";";
        try {
            stmt = databaseManager.con.createStatement();
            rs = stmt.executeQuery(SQL);
            while (rs.next()) {
                loggedinStudents.put(rs.getString(1), rs.getString(2));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return loggedinStudents;
    }

}
