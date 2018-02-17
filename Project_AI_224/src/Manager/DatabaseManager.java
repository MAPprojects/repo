package Manager;

import java.sql.*;
public class DatabaseManager {

    private final String HOST = "jdbc:postgresql://localhost:5432/";
    private final String DB_NAME = "postgres";
    private final String DB_USER = "postgres";
    private final String DB_PASSWORD = "javalogin";

    public Connection con = null;
    Statement stmt = null;
    ResultSet rs = null;

    public DatabaseManager() {
        try {
            Class.forName("org.postgresql.Driver");
            String connectionString = HOST+DB_NAME;
            con = DriverManager.getConnection(connectionString, DB_USER, DB_PASSWORD);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
