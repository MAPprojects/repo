package repository;

import domain.User;
import utils.BDUtil;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserBDOperations {
    /***
     * Select * from Useri where username='...'
      * @param username String
     * @return User
     * @throws SQLException
     */
    public static User searchUserByUserName(String username) throws SQLException {
        String statement="Select * from Useri where username='"+username+"'";
        try{
            ResultSet resultSetUser= BDUtil.bdExecuteQuery(statement);
            User user=getUserFromResultSet(resultSetUser);
            return user;
        } catch (SQLException e) {
            System.out.println("Searching for a user"+e);
            throw e;
        }
    }

    private static User getUserFromResultSet(ResultSet resultSetUser) throws SQLException {
        User user=null;
        if (resultSetUser.next()){
            user=new User();
            user.setNume(resultSetUser.getString("nume"));
            user.setEmail(resultSetUser.getString("email"));
            user.setProf_student(resultSetUser.getString("prof_student"));
            user.setUsername(resultSetUser.getString("username"));
            user.setPassword(resultSetUser.getString("password"));
        }
        return user;
    }

    /**
     * select * from Useri
     * @return List<User></User>
     */
    public static ArrayList<User> searchUsers() throws SQLException {
        String statement="Select * from userBD.db_datareader.Useri";
        try{
            ResultSet resultSet=BDUtil.bdExecuteQuery(statement);
            ArrayList<User> list=getUserListFromResultSet(resultSet);
            return list;
        } catch (SQLException e) {
            System.out.println("select * failed "+e);
            throw e;
        }
    }

    private static ArrayList<User> getUserListFromResultSet(ResultSet resultSet) throws SQLException {
        ArrayList<User> list=new ArrayList<>();

        while(resultSet.next()){
            User user=new User();
            user.setPassword(resultSet.getString("password"));
            user.setUsername(resultSet.getString("username"));
            user.setProf_student(resultSet.getString("prof_student"));
            user.setEmail(resultSet.getString("email"));
            user.setNume(resultSet.getString("nume"));

            list.add(user);
        }
        return list;
    }

    public static void updateUserEmail(String username,String email) throws SQLException {
        String statement= "Update Useri\n" +
                "set email='"+email+"'\n" +
                "where username='"+username+"'\n";
        try {
            BDUtil.bdExecuteUpdate(statement);
        } catch (SQLException e) {
            System.out.println("Update email operation"+e);
            throw e;
        }
    }


    public static void updateUser(User user) throws SQLException {
        String statement= "Update userBD.db_datareader.Useri\n" +
                "set email='"+user.getEmail()+"'\n" +
                "where username='"+user.getUsername()+"'\n" +
                "Update userBD.db_datareader.Useri\n" +
                "set [prof_student]='"+user.getProf_student()+"'\n" +
                "where username='"+user.getUsername()+"'\n" +
                "Update userBD.db_datareader.Useri\n" +
                "set password='"+user.getPassword()+"'\n" +
                "where username='"+user.getUsername()+"'\n" +
                "Update userBD.db_datareader.Useri\n" +
                "set nume='"+user.getNume()+"'\n" +
                "where username='"+user.getUsername()+"'\n";
        try {
            BDUtil.bdExecuteUpdate(statement);
        } catch (SQLException e) {
            System.out.println("Update email operation"+e);
            throw e;
        }
    }

    /***
     * Deletes a user by its username
     * @param username String
     * @throws SQLException if deletion fails
     */
    public static void deleteUserByUsername(String username) throws SQLException {
        String statement="DELETE from userBD.db_datareader.Useri\n" +
                "Where username='"+username+"'";
        try{
            BDUtil.bdExecuteUpdate(statement);
        } catch (SQLException e) {
            System.out.println("Delete failed "+e);
            throw e;
        }
    }

    /**
     * INSERT a User in the database
     * @param username String
     * @param password BigInteger
     * @param prof_student String
     * @param nume String
     * @param email String
     * @throws SQLException if insert is not valid
     */
    public static void insertUser(String username,String password,String prof_student,String nume,String email) throws SQLException {
        String statement="INSERT INTO userBD.db_datareader.Useri\n" +
                "(username,password,[prof_student],nume,email)\n" +
                "VALUES\n" +
                "('"+username+"','"+password+"','"+prof_student+"','"+nume+"','"+email+"')";
        try{
            BDUtil.bdExecuteUpdate(statement);
        } catch (SQLException e) {
            System.out.println("Error on update operation "+e);
            throw e;
        }
    }


}
