package Repositories;

import Domain.Student;
import Exceptions.RepositoryException;

import java.sql.*;
import java.util.ArrayList;
import java.util.Optional;

public class SQLStudentRepo extends StudentRepository {

    public SQLStudentRepo() {
        super();
        load();
    }

    private void load() {
        Connection connection = getConnection();
        String selectQuery = "SELECT * FROM Students";
        try {
            PreparedStatement statement = connection.prepareStatement(selectQuery);
            ResultSet result = statement.executeQuery();
            while(result.next()) {
                Integer idStudent = result.getInt("idStudent");
                String nume = result.getString("nume");
                Integer grupa = result.getInt("grupa");
                String email = result.getString("email");
                String profLab = result.getString("profLab");
                save(new Student(idStudent, nume, grupa, email, profLab));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (RepositoryException e) {
            System.out.println(e.getMessage());
        }
    }

    private void store() {
        Connection connection = getConnection();
        PreparedStatement statement;
        String deleteQuery = "DELETE FROM Students";
        Iterable<Student> it = findAll();
        ArrayList<Student> allStudents = new ArrayList<>();
        for(Student st : it)
            allStudents.add(st);
        String addQuery = "";
        String quote = "\'";
        try {
            statement = connection.prepareStatement(deleteQuery);
            statement.executeUpdate();
            for(Student st : allStudents) {
                addQuery="INSERT INTO Students VALUES(" + st.getID() + "," + quote + st.getNume() + quote + "," + st.getGrupa() + "," + quote + st.getEmail() + quote + "," + quote + st.getProfLab() + quote + ")";
                statement = connection.prepareStatement(addQuery);
                statement.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Connection getConnection() {
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        String url = "jdbc:jtds:sqlserver://localhost:1433/javaProject";
        String user = "java";
        String passwd = "whatever";
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url,user,passwd);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    @Override
    public Optional<Student> save(Student entitate) throws RepositoryException {
        Optional<Student> returnedValue =  super.save(entitate);
        store();
        return returnedValue;
    }

    @Override
    public Optional<Student> delete(Integer id_entitate) throws RepositoryException {
        Optional<Student> returnedValue =  super.delete(id_entitate);
        store();
        return returnedValue;
    }

    @Override
    public Optional<Student> update(Integer id_entitate, Student entitate) throws RepositoryException {
        Optional<Student> returnedValue =  super.update(id_entitate,entitate);
        store();
        return returnedValue;
    }
}
