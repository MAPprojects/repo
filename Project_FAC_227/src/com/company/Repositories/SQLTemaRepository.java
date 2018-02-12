package com.company.Repositories;

import com.company.Domain.Tema;
import com.company.Exceptions.RepositoryException;

import java.sql.*;
import java.util.Optional;

public class SQLTemaRepository extends TemaRepository {

    public String quote = "\'";

    public SQLTemaRepository()
    {
        super();
        load();
    }

    @Override
    public Tema save(Tema entitate) throws RepositoryException {
        Tema tema = super.save(entitate);
        //store();
        try {
            Connection connection = getConnection();
            String querry = "INSERT INTO Teme(nrTema,descriere,deadline) VALUES(" + tema.getID() + "," + quote + tema.getDescriere() + quote + "," + tema.getDeadline() + ")";
            PreparedStatement insert = connection.prepareStatement(querry);
            insert.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tema;
    }

    @Override
    public Optional<Tema> delete(Integer id_entitate) throws RepositoryException {
        Optional<Tema> tema = super.delete(id_entitate);
        //store();
        try {
            Connection connection = getConnection();
            String querry = "DELETE Teme WHERE nrTema =" + id_entitate;
            PreparedStatement deleteQ = connection.prepareStatement(querry);
            deleteQ.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tema;
    }

    @Override
    public Tema update(Integer nrTema, String Descriere, int deadline) throws RepositoryException {
        Tema tema = super.update(nrTema,Descriere,deadline);
        //store();
        try{
            Connection connection = getConnection();
            String querry = "UPDATE Teme SET descriere = '" +tema.getDescriere() + "', deadline = " + tema.getDeadline() + " WHERE nrTema = " + tema.getID();
            PreparedStatement updateQ = connection.prepareStatement(querry);
            updateQ.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tema;
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
        int nrTema = 0;
        String descriere = null;
        int deadline = 0;
        try {
            for(Tema tema: findAll())
            {
                nrTema=tema.getID();
                descriere = tema.getDescriere();
                deadline = tema.getDeadline();
                String querry = "INSERT INTO Teme(nrTema,descriere,deadline) VALUES(" + nrTema +","+quote + descriere + quote+"," + deadline + ")";
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
        String querry = "SELECT * FROM Teme";
        try{
            PreparedStatement getData = connection.prepareStatement(querry);
            ResultSet resultSet = getData.executeQuery();

            while(resultSet.next())
            {
                super.save(new Tema(resultSet.getInt("nrTema"),resultSet.getString("descriere"),resultSet.getInt("deadline")));
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
        String querry = "DELETE FROM Teme";
        try {
            PreparedStatement clearTable = connection.prepareStatement(querry);
            clearTable.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
