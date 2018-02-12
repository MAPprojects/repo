package Utils;

import java.sql.Connection;
import java.sql.DriverManager;

public class Database {
    private static Database database = null;
    private static Connection conn = null;
    private Database() throws Exception {
        Class.forName(ResourceManager.getResourceMangager().getValue("SqlServerDriver"));
         conn = DriverManager.getConnection(ResourceManager.getResourceMangager().getValue("DatabaseUrl"),
                ResourceManager.getResourceMangager().getValue("DbUser"),
                ResourceManager.getResourceMangager().getValue("DbPassword"));
    }

    public static Connection getConnection()
    {
        if (database == null) {
            try {
                database = new Database();
            } catch (Exception ignored) {

            }
        }
        return conn;
    }
}
