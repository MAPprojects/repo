package db_stuff;

import java.sql.*;

public class DBConnect {
    public Connection con;
    public PreparedStatement stmt;
    public ResultSet rs;
    public DBConnect() {
        try{
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/registration","root","");

        }catch(Exception ex){
            System.out.println("Err :" + ex);
            System.exit(0);

        }
    }

    /**
     * Check if an user has matching name and password
     * @param _name
     * @param _password
     * @return
     */
    public boolean init(String _name , String _password){
        try{

            stmt = con.prepareStatement("SELECT * FROM admins WHERE name=? AND password=?");
            stmt.setString(1, _name);
            stmt.setString(2, _password);
            rs = stmt.executeQuery();


            if(!rs.first()){
                return false;
            }
          }catch(Exception ex){
            System.out.println("Err :" + ex);
        }

        return true;
    }

    /**
     * Get user level access
     * @param _name
     * @return
     */
    public Integer getAccesLevel(String _name){
        try{

            stmt = con.prepareStatement("SELECT acceslevel FROM admins WHERE name=?");
            stmt.setString(1, _name);
            rs = stmt.executeQuery();

            if(rs.first()){
                return rs.getInt("acceslevel");

            }
            else {
                return 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Close DB Connection
     */
    public void close() {

            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) { /* ignored */}
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) { /* ignored */}
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) { /* ignored */}
            }
        }

    /**
     * Get number of candidates registrations in a certain period
      * @param day
     * @param month
     * @param year
     * @return
     */
    public Integer getCandidatesRegisteredinMonth(Integer day  , Integer month , Integer year){
        Integer count = 0;
        try {
            stmt = con.prepareStatement("SELECT * FROM candidates WHERE YEAR(date_registered) = ? and MONTH(date_registered) = ? and DAY(date_registered) = ?");
            stmt.setInt(1,year);
            stmt.setInt(2,month);
            stmt.setInt(3,day);

            rs = stmt.executeQuery();
            while (rs.next()) {
                count++;
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }
}
