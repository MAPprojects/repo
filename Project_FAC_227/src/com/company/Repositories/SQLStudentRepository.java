package com.company.Repositories;

import com.company.Domain.Student;
import com.company.Exceptions.RepositoryException;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class SQLStudentRepository extends StudentRepository {

    public String quote = "\'";
    private Map<Integer,String> passwords;

    public SQLStudentRepository()
    {
        super();
        passwords = new HashMap<>();
        load();

    }

    @Override
    public Student save(Student entitate) throws RepositoryException {
        Student student = super.save(entitate);
        String studentFileName = "Data\\" + student.getID() + ".txt";
        File studentFile = new File(studentFileName);
        try {
            studentFile.createNewFile();
        } catch (IOException e) {
            throw new RepositoryException("Nu s-a putut crea fisierul studentului!");
        }
        //store();
        try {
            Connection connection = getConnection();
            String querry = "INSERT INTO Studenti(idStudent,nume,grupa,email,prof,parola) VALUES(" + student.getID() + "," + quote + student.getNume() + quote + "," + student.getGrupa() + "," + quote + student.getEmail() + quote + "," + quote + student.getProfLab() + quote + "," + "'parola'" +")";
            PreparedStatement insert = connection.prepareStatement(querry);
            insert.executeUpdate();

            accountRead();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return student;
    }

    @Override
    public Optional<Student> delete(Integer id_entitate) throws RepositoryException {
        Optional<Student> student = super.delete(id_entitate);
        //store();
        try {
            Connection connection = getConnection();
            String querry = "DELETE Studenti WHERE idStudent =" + id_entitate;
            PreparedStatement deleteQ = connection.prepareStatement(querry);
            deleteQ.executeUpdate();

            String studentFileName = "Data\\" + student.get().getID() + ".txt";
            File studentFile = new File(studentFileName);
            studentFile.delete();

            passwords.remove(id_entitate);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return student;
    }

    @Override
    public Student update(Student student) throws RepositoryException {
        Student student1 = super.update(student);
        //store();
        try{
            Connection connection = getConnection();
            String querry = "UPDATE Studenti SET nume = '" +student1.getNume() + "', grupa = " + student1.getGrupa() + ", email = '" + student1.getEmail() + "', prof = '" + student1.getProfLab() + "' WHERE idStudent = " + student1.getID();
            PreparedStatement updateQ = connection.prepareStatement(querry);
            updateQ.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return student1;
    }

    public static Connection getConnection(){
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            //Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        String url = "jdbc:jtds:sqlserver://localhost:1433/mapDatabase";
        String usr = "Betmen";
        String pass = "betmen";
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url,usr,pass);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public void store()
    {
        clearDatabase();
        Connection connection = getConnection();
        int id = 0;
        String nume = null;
        int grupa = 0;
        String email = null;
        String prof = null;
        String parola = null;
        try {
            for(Student student: findAll())
            {
                id=student.getID();
                nume = student.getNume();
                grupa = student.getGrupa();
                email = student.getEmail();
                prof = student.getProfLab();
                parola = "parola";
                String querry = "INSERT INTO Studenti(idStudent,nume,grupa,email,prof,parola) VALUES(" + id +","+quote + nume + quote+"," + grupa + ","+quote + email + quote+","+quote + prof + quote+ "," + quote + parola + quote +")";
                PreparedStatement insert = connection.prepareStatement(querry);
                insert.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void load()
    {
        Connection connection = getConnection();
        String querry = "SELECT * FROM Studenti";
        try{
            PreparedStatement getData = connection.prepareStatement(querry);
            ResultSet resultSet = getData.executeQuery();

            while(resultSet.next())
            {
                super.save(new Student(resultSet.getInt("idStudent"),resultSet.getString("nume"),resultSet.getInt("grupa"),resultSet.getString("email"),resultSet.getString("prof")));
            }

            accountRead();

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (RepositoryException e) {
            e.printStackTrace();
        }
    }

    public void clearDatabase()
    {
        Connection connection = getConnection();
        String querry = "DELETE FROM Studenti";
        try {
            PreparedStatement clearTable = connection.prepareStatement(querry);
            clearTable.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void accountRead()
    {
        passwords.clear();
        Connection connection = getConnection();
        String querry = "SELECT idStudent,parola FROM Studenti";
        try{
            PreparedStatement getPasswords = connection.prepareStatement(querry);
            ResultSet resultSet = getPasswords.executeQuery();

            while (resultSet.next())
            {
                passwords.put(resultSet.getInt("idStudent"),resultSet.getString("parola"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getPassword(int idStudent)
    {
        return passwords.get(idStudent);
    }

    public void setPassword(int idStudent, String newPassword)
    {
        passwords.replace(idStudent,newPassword); accountRead();
    }

    public Map<Integer,String> getAllPasswords()
    {
        return passwords;
    }




}
