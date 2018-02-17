package utils;


import com.sun.rowset.CachedRowSetImpl;

import java.sql.*;

public class BDUtil {
    private static String JDBC_jTds_DRIVER="net.sourceforge.jtds.jdbc.Driver";

    private static Connection connection=null;
    //username=oana password=oana
    private static final String connectionStr="jdbc:sqlserver://localhost:1433;"+"databaseName=testBD";

    public static void bdConnect() throws ClassNotFoundException, SQLException {
        try{
            Class.forName(JDBC_jTds_DRIVER);
        } catch (ClassNotFoundException e) {
            System.out.println("You don't have the jTds jdbc DRIVER!");
            e.printStackTrace();
            throw e;
        }

        try{
            connection= DriverManager.getConnection(connectionStr,"oana","oana");

        } catch (SQLException e) {
            System.out.println("Connection failed!");
            e.printStackTrace();
            throw e;
        }
    }

    public static void bdDisconnect() throws SQLException {
        try {
            if (connection!=null && !connection.isClosed()){
                connection.close();
            }
        } catch (SQLException e) {
            throw e;
        }
    }

    /***
     * for SELECT
     * @param query String
     * @return ResultSet (the result of the execution of the query)
     * @throws SQLException if the executeQuery has errors
     */
    public static ResultSet bdExecuteQuery(String query) throws SQLException {
        Statement statement=null;
        ResultSet resultSet=null;
        CachedRowSetImpl cachedRowSet=null;

        try{
            bdConnect();
            statement=connection.createStatement();
            resultSet=statement.executeQuery(query);

            cachedRowSet=new CachedRowSetImpl();
            cachedRowSet.populate(resultSet);
        } catch (SQLException e) {
            System.out.println("Error at query operation!");
            throw e;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (resultSet!=null){
                resultSet.close();
            }
            if (statement!=null){
                statement.close();
            }
            bdDisconnect();
        }
        return cachedRowSet;
    }


    /**
     * For Delete,Insert,Update table in sql server
     * @param queryStatement String
     * @throws SQLException if executeUpdate has errors
     */
    public static void bdExecuteUpdate(String queryStatement) throws SQLException {
        Statement statement=null;
        try{
            bdConnect();
            statement=connection.createStatement();
            statement.executeUpdate(queryStatement);

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }finally {
            if (statement!=null){
                statement.close();
            }
            bdDisconnect();
        }
    }

}
