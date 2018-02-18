package mainpackage.repository;

import mainpackage.domain.Student;
import mainpackage.exceptions.MyException;
import mainpackage.validator.Validator;

import java.sql.*;

public class StudentDbRepo extends StudentMemoryRepository{

    public static String db_name = "monitorizare_studenti.db";

    public StudentDbRepo(Validator<Student> validator) {
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
            ResultSet rs = stmt.executeQuery( "SELECT * FROM STUDENTS;" );

            while ( rs.next() ) {
                int id = rs.getInt("id");
                String  name = rs.getString("name");
                String  email = rs.getString("email");
                String  teacher = rs.getString("teacher");
                String  group = rs.getString("grp");

                try {
                    super.save(new Student(name, id, email, teacher, group));
                } catch (MyException e) {
                    System.out.println("Validation error when loading db! " + e.getMessage());
                }
            }
            rs.close();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("LOAD done successfully");
    }

    @Override
    public void save(Student entity) throws MyException {
        super.save(entity);
        insert_into_db(entity);
    }

    @Override
    public Student delete(Integer integer) {
        Student old_student = super.delete(integer);
        delete_from_db(integer);
        return old_student;
    }

    @Override
    public Student update(Student entity) throws MyException {
        Student updated_student = super.update(entity);
        update_db_entity(entity);
        return updated_student;
    }

    private void update_db_entity(Student st) {
        Connection c = null;
        PreparedStatement stmt = null;

        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:"+db_name);
            c.setAutoCommit(false);
            System.out.println("Opened database successfully for update");
            stmt = c.prepareStatement("UPDATE STUDENTS SET NAME = ?, EMAIL = ?, TEACHER = ?, GRP = ? WHERE ID = ?");
            stmt.setString(1, st.getName());
            stmt.setString(2, st.getEmail());
            stmt.setString(3, st.getTeacher());
            stmt.setString(4, st.getGroup());
            stmt.setString(5, String.valueOf(st.getId()));
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

    private void insert_into_db(Student st) {
        Connection c = null;
        PreparedStatement stmt = null;

        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:"+db_name);
            c.setAutoCommit(false);
            System.out.println("Opened database successfully for insert");
            stmt = c.prepareStatement("INSERT INTO STUDENTS (ID,NAME,EMAIL,TEACHER,GRP) VALUES (?,?,?,?,?);");
            stmt.setString(1, String.valueOf(st.getId()));
            stmt.setString(2, st.getName());
            stmt.setString(3, st.getEmail());
            stmt.setString(4, st.getTeacher());
            stmt.setString(5, st.getGroup());
            stmt.executeUpdate();
            c.commit();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
        System.out.println("Student inserted into db!");
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
            String sql = String.format("DELETE from STUDENTS where ID=%d;",id);
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