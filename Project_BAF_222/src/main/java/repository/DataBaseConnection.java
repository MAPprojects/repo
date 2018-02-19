package repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseConnection {
    private String connectionUrl = "jdbc:sqlserver://localhost\\SQLEXPRESS:1433;database=TemaSuplimentara;integratedSecurity=true";
    private static Connection connection = null;

    public DataBaseConnection(){
        try{
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            connection = DriverManager.getConnection(connectionUrl);
        }
        catch (ClassNotFoundException | SQLException ex)
        {
            ex.printStackTrace();
            throw new RepositoryException("Nu se poate conecta la db");
        }
    }

    public static Connection getConnection() {
        return connection;
    }
}
