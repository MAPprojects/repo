package Repository;



import Domain.Note;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;

public class NoteRepoSQL extends NoteRepository {

    TemeRepoSQL repoTeme= new TemeRepoSQL(new TemeValidator());

    public NoteRepoSQL(Validator<Note> vali) {
        super(vali);
        load();
    }





    private Connection getConnection(){
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        String url="jdbc:jtds:sqlserver://localhost:1433/java;instance=SQLEXPRESS";
        String user="java";
        String password="whatever";
        Connection connection=null;
        try {
            connection= DriverManager.getConnection(url);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
    void load(){
        Connection connection=getConnection();
        String query= "SELECT * FROM Nota";
        try {
            PreparedStatement statement=connection.prepareStatement(query);
            ResultSet rez=statement.executeQuery();
            while (rez.next()){
                int nrTema=rez.getInt("nrTema");
                int idStudent=rez.getInt("idStudent");
                int valoare=rez.getInt("valoare");
                int saptamanaPredare=rez.getInt("saptamanaPredare");
                String observatii=rez.getString("observatii");

                super.save(new Note(idStudent,nrTema,valoare));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }
    private void saveToFile(Note nota, String observatii, int saptamanaPredare, String status){
        String filename = Integer.toString(nota.getIdStudent()) + ".txt";
        //saveToCatalaog(nota);
        try(BufferedWriter out = new BufferedWriter(new FileWriter(filename,true))){
            out.write(status+";");
            out.write(Integer.toString(nota.getIdStudent()) +";"+ Integer.toString((nota.getNrTema())) +";"+  Integer.toString(nota.getValoare())+";" +  Integer.toString(repoTeme.getById(nota.getNrTema()).getDeadline()) +";"+ Integer.toString(saptamanaPredare)+";");

            if (!observatii.isEmpty()){
                out.write("Observatii: " + observatii + ";");
                out.newLine();
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public void update(Note nota,Note notaNou, String observatii, int saptamanaPredare) throws ValidationException, SQLException {
        saveToFile(nota,observatii,saptamanaPredare,"Modificare nota");
//        delete(nota);
//        save(notaNou,observatii,saptamanaPredare);
//        super.update(nota,notaNou);
        Connection connection=getConnection();
        String query = "UPDATE Nota SET valoare = ? , "
                + "observatii=? ,"
                +"saptamanaPredare=? "
                + "WHERE idStudent = ? AND nrTema=?" ;
        PreparedStatement preparedStmt = connection.prepareStatement(query);
        preparedStmt.setInt(1,notaNou.getValoare());
        preparedStmt.setString(2,observatii);
        preparedStmt.setInt(3,saptamanaPredare);
        preparedStmt.setInt(4,nota.getIdStudent());
        preparedStmt.setInt(5,nota.getNrTema());
        preparedStmt.execute();



    }

    public void save(Note nota, String observatii, int saptamanaPredare) throws ValidationException, SQLException {
        super.save(nota);
        saveToFile(nota,observatii,saptamanaPredare,"Adaugare nota");

        if (nota != null) {
            Connection connection=getConnection();
            String query = "INSERT INTO Nota(idStudent, nrTema, valoare, observatii, saptamanaPredare) " +"values(?,?,?,?,?);";
            PreparedStatement preparedStmt = connection.prepareStatement(query);
            preparedStmt.setInt(1,nota.getIdStudent());
            preparedStmt.setInt(2,nota.getNrTema());
            preparedStmt.setInt(3,nota.getValoare());
            preparedStmt.setString(4,observatii);
            preparedStmt.setInt(5,saptamanaPredare);
            preparedStmt.execute();

        }
    }
    public void delete(Note nota) throws SQLException {
        super.delete(nota);
        Connection connection=getConnection();
        String queryNota = "delete FROM Nota where idStudent=? AND nrTema=?";
        PreparedStatement preparedStmt1 = connection.prepareStatement(queryNota);
        preparedStmt1.setInt(1,nota.getIdStudent());
        preparedStmt1.setInt(2,nota.getNrTema());
        preparedStmt1.executeUpdate();
        String queryNota1 = "delete FROM Penalizare where idStudent=? AND nrTema=?";
        PreparedStatement preparedStmt2 = connection.prepareStatement(queryNota1);
        preparedStmt2.setInt(1,nota.getIdStudent());
        preparedStmt2.setInt(2,nota.getNrTema());
        preparedStmt2.executeUpdate();

    }

    public int getSpatamnaPredare(int idStudent, int nrTema) throws SQLException {
        Connection connection=getConnection();
        String query = "SELECT saptamanaPredare FROM Nota WHERE idStudent=? AND nrTema=?";
        PreparedStatement preparedStmt = connection.prepareStatement(query);
        preparedStmt.setInt(1,idStudent);
        preparedStmt.setInt(2,nrTema);
        ResultSet rez= preparedStmt.executeQuery();

        while (rez.next())
            return  rez.getInt("saptamanaPredare");
        return 0;



    }
    public String getObservatii(int idStudent, int nrTema) throws SQLException {
        Connection connection=getConnection();
        String query = "SELECT observatii FROM Nota WHERE idStudent=? AND nrTema=?";
        PreparedStatement preparedStmt = connection.prepareStatement(query);
        preparedStmt.setInt(1,idStudent);
        preparedStmt.setInt(2,nrTema);
        ResultSet rez= preparedStmt.executeQuery();
        while (rez.next())
            return rez.getString("observatii");
        return  null;
    }
}
