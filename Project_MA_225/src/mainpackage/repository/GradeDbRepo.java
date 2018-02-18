package mainpackage.repository;
import javafx.util.Pair;
import mainpackage.domain.Grade;
import mainpackage.domain.Homework;
import mainpackage.domain.Student;
import mainpackage.exceptions.MyException;
import mainpackage.validator.Validator;

import java.sql.*;

public class GradeDbRepo extends AbstractRepository<Grade, Pair<Integer,Integer>>{

    public static String db_name = "monitorizare_studenti.db";

    public GradeDbRepo(Validator<Grade> validator) {
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
            ResultSet rs = stmt.executeQuery( "SELECT * FROM GRADES;" );

            while ( rs.next() ) {
                int ids = rs.getInt("id_student");
                int idh = rs.getInt("id_homework");
                float value = rs.getFloat("value");

                try {
                    super.save(new Grade(ids, idh, value));
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
    public void save(Grade entity) throws MyException {
        super.save(entity);
        insert_into_db(entity);
    }

    @Override
    public Grade update(Grade entity) throws MyException {
        Grade updated_grade = super.update(entity);
        update_db_entity(entity);
        return updated_grade;
    }

    @Override
    public Grade delete(Pair<Integer, Integer> id) {
        Grade old_student = super.delete(id);
        delete_from_db(id);
        return old_student;
    }
    private void delete_from_db(Pair<Integer, Integer> id) {
        Connection c = null;
        Statement stmt = null;

        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:"+db_name);
            c.setAutoCommit(false);
            System.out.println("Opened database successfully for delete");

            stmt = c.createStatement();
            String sql = String.format("DELETE from GRADES where ID_STUDENT=%d AND ID_HOMEWORK=%d;",id.getKey(),id.getValue());
            stmt.executeUpdate(sql);
            c.commit();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
        System.out.println("Operation done successfully");
    }

    private void update_db_entity(Grade g) {
        Connection c = null;
        PreparedStatement stmt = null;

        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:"+db_name);
            c.setAutoCommit(false);
            System.out.println("Opened database successfully for update");
            stmt = c.prepareStatement("UPDATE GRADES SET VALUE = ? WHERE ID_STUDENT = ? AND ID_HOMEWORK = ?");
            stmt.setFloat(1, g.get_value());
            stmt.setInt(2, g.get_idStudent());
            stmt.setInt(3, g.get_idHomework());
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

    private void insert_into_db(Grade hw) {
        Connection c = null;
        PreparedStatement stmt = null;

        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:"+db_name);
            c.setAutoCommit(false);
            System.out.println("Opened database successfully for insert");
            stmt = c.prepareStatement("INSERT INTO GRADES (ID_STUDENT, ID_HOMEWORK, VALUE) VALUES (?,?,?);");
            stmt.setInt(1,hw.get_idStudent());
            stmt.setInt(2, hw.get_idHomework());
            stmt.setFloat(3, hw.get_value());
            stmt.executeUpdate();
            c.commit();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
        System.out.println("Homework inserted into db!");
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
