package mainpackage.repository;

import mainpackage.domain.Homework;
import mainpackage.domain.Student;
import mainpackage.exceptions.MyException;
import mainpackage.validator.Validator;

import java.sql.*;

public class HomeworkDbRepo extends HomeworkMemoryRepository{

    public static String db_name = "monitorizare_studenti.db";

    public HomeworkDbRepo(Validator<Homework> validator) {
        super(validator);
        load_from_db();
    }

    private void load_from_db() {
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:"+db_name);
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT * FROM HOMEWORKS;" );

            while ( rs.next() ) {
                int id = rs.getInt("id");
                String  description = rs.getString("description");
                int deadline = rs.getInt("deadline");

                try {
                    super.save(new Homework(id, description, deadline));
                } catch (MyException e) {
                    System.out.println("Validation error when loading db! " + e.getMessage());
                }
            }
            rs.close();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
        System.out.println("LOAD done successfully");
    }

    @Override
    public void save(Homework entity) throws MyException {
        super.save(entity);
        insert_into_db(entity);
    }

    @Override
    public Homework delete(Integer integer) {
        Homework old_hw = super.delete(integer);
        delete_from_db(integer);
        return old_hw;
    }

    @Override
    public Homework update(Homework entity) throws MyException {
        Homework updated_homework = super.update(entity);
        update_db_entity(entity);
        return updated_homework;
    }

    private void update_db_entity(Homework st) {
        Connection c = null;
        PreparedStatement stmt = null;

        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:"+db_name);
            c.setAutoCommit(false);
            System.out.println("Opened database successfully for update");
            stmt = c.prepareStatement("UPDATE HOMEWORKS SET DESCRIPTION = ?, DEADLINE = ? WHERE ID = ?");
            stmt.setString(1, st.getDescription());
            stmt.setInt(2, st.getDeadline());
            stmt.setInt(3, st.getId());
            System.out.println(stmt.toString());
            stmt.executeUpdate();
            c.commit();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
        System.out.println("Operation done successfully");
    }

    private void insert_into_db(Homework hw) {
        Connection c = null;
        PreparedStatement stmt = null;

        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:"+db_name);
            c.setAutoCommit(false);
            System.out.println("Opened database successfully for insert");
            stmt = c.prepareStatement("INSERT INTO HOMEWORKS (ID,DESCRIPTION,DEADLINE) VALUES (?,?,?);");
            stmt.setString(1, String.valueOf(hw.getId()));
            stmt.setString(2, hw.getDescription());
            stmt.setInt(3, hw.getDeadline());
            stmt.executeUpdate();
            c.commit();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
        System.out.println("Homework inserted into db!");
    }

    private void delete_from_db(Integer id) {
        Connection c = null;
        Statement stmt = null;

        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:"+db_name);
            c.setAutoCommit(false);
            System.out.println("Opened database successfully for delete");

            stmt = c.createStatement();
            String sql = String.format("DELETE from HOMEWORKS where ID=%d;",id);
            stmt.executeUpdate(sql);
            c.commit();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }

        System.out.println("Operation done successfully");
    }

    private static void create_tables()
    {
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:monitorizare_studenti.db");
            System.out.println("Opened database successfully");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }
        try {
            stmt = c.createStatement();
            String sql = "CREATE TABLE STUDENTS" +
                    "(ID INT PRIMARY KEY         NOT NULL," +
                    " NAME           CHAR(50)    NOT NULL, " +
                    " EMAIL          CHAR(50)    NOT NULL, " +
                    " TEACHER        CHAR(50)    NOT NULL," +
                    " GRP          CHAR(50)    NOT NULL)";
            stmt.executeUpdate(sql);
            stmt.close();
            System.out.println("Table created successfully");
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }

        try {
            stmt = c.createStatement();
            String sql = "CREATE TABLE HOMEWORKS" +
                    "(ID INT PRIMARY KEY         NOT NULL," +
                    " DESCRIPTION       CHAR(50)    NOT NULL, " +
                    " DEADLINE          INT    NOT NULL) ";
            stmt.executeUpdate(sql);
            stmt.close();
            System.out.println("Table created successfully");
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }

        try {
            stmt = c.createStatement();
            String sql = "CREATE TABLE GRADES" +
                    "(ID_STUDENT INT         NOT NULL," +
                    " ID_HOMEWORK       INT    NOT NULL, " +
                    " VALUE          FLOAT    NOT NULL,PRIMARY KEY(`ID_STUDENT`,`ID_HOMEWORK`)) ";
            stmt.executeUpdate(sql);
            stmt.close();
            System.out.println("Table created successfully");
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
    }

    public void test_connection()
    {
        Connection c = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:"+db_name);
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("Opened database successfully");
    }
}
