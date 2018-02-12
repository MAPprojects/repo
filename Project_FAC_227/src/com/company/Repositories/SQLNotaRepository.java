package com.company.Repositories;

import com.company.Domain.Nota;
import com.company.Exceptions.RepositoryException;

import java.sql.*;
import java.util.Optional;

public class SQLNotaRepository extends NotaRepository {

    public String quote = "\'";

    public SQLNotaRepository()
    {
        super();
        load();
    }

    @Override
    public Nota save(Nota entitate) throws RepositoryException {
        Nota nota = super.save(entitate);
        //store();
        try{
            Connection connection = getConnection();
            String querry = "INSERT INTO Note(idStudent,nrTema,nota) VALUES (" + nota.getIdStudent() +"," + nota.getNrTema() + "," + nota.getNota() + ")";
            PreparedStatement updateQ = connection.prepareStatement(querry);
            updateQ.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nota;
    }

    @Override
    public Optional<Nota> delete(Integer id_entitate) throws RepositoryException {
        Optional<Nota> nota = super.delete(id_entitate);
        //store();
        try{
            Connection connection = getConnection();
            String querry = "DELETE Note WHERE idStudent = " + nota.get().getIdStudent() + " AND nrTema = " + nota.get().getNrTema();
            PreparedStatement updateQ = connection.prepareStatement(querry);
            updateQ.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nota;
    }

    @Override
    public Nota update(int idStudent, int nrTema, int vnota) throws RepositoryException {
        Nota nota = super.update(idStudent,nrTema,vnota);
        //store();
        try{
            Connection connection = getConnection();
            String querry = "UPDATE Note SET nota = " +nota.getNota() + " WHERE idStudent = " + nota.getIdStudent() + " AND nrTema =" + nota.getNrTema();
            PreparedStatement updateQ = connection.prepareStatement(querry);
            updateQ.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nota;
    }

    public static Connection getConnection() {
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
        int idStudent = 0;
        int nrTema = 0;
        int nota = 0;
        try {
            for(Nota n: findAll())
            {
                idStudent=n.getIdStudent();
                nrTema=n.getNrTema();
                nota=n.getNota();
                String querry = "INSERT INTO Note(idStudent,nrTema,nota) VALUES(" + idStudent +"," + nrTema + "," + nota + ")";
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
        String querry = "SELECT * FROM Note";
        try{
            PreparedStatement getData = connection.prepareStatement(querry);
            ResultSet resultSet = getData.executeQuery();

            while(resultSet.next())
            {
                super.save(new Nota(resultSet.getInt("idStudent"),resultSet.getInt("nrTema"),resultSet.getInt("nota")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (RepositoryException e) {
            e.printStackTrace();
        }
    }

    public void clearDatabase()
    {
        Connection connection = getConnection();
        String querry = "DELETE FROM Note";
        try {
            PreparedStatement clearTable = connection.prepareStatement(querry);
            clearTable.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
