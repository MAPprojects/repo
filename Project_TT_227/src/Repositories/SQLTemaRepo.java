package Repositories;

import Domain.Student;
import Domain.Tema;
import Exceptions.RepositoryException;

import javax.swing.text.html.Option;
import java.sql.*;
import java.util.ArrayList;
import java.util.Optional;

public class SQLTemaRepo extends TemaRepository {

    public SQLTemaRepo() {
        super();
        load();
    }

    private void load() {
        Connection connection = getConnection();
        String selectQuery = "SELECT * FROM Homeworks";
        try {
            PreparedStatement statement = connection.prepareStatement(selectQuery);
            ResultSet result = statement.executeQuery();
            while(result.next()) {
                Integer nrTema = result.getInt("nrTema");
                String descriere = result.getString("descriere");
                Integer deadline = result.getInt("deadline");
                save(new Tema(nrTema, descriere, deadline));
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
        String deleteQuery = "DELETE FROM Homeworks";
        Iterable<Tema> it = findAll();
        ArrayList<Tema> allHomeworks = new ArrayList<>();
        for(Tema t : it)
            allHomeworks.add(t);
        String addQuery = "";
        String quote = "\'";
        try {
            statement = connection.prepareStatement(deleteQuery);
            statement.executeUpdate();
            for(Tema t : allHomeworks) {
                addQuery="INSERT INTO Homeworks VALUES(" + t.getID() + "," + quote + t.getDescriere() + quote + "," + t.getDeadline() + ")";
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
    public Optional<Tema> save(Tema entitate) throws RepositoryException {
        Optional<Tema> returnedValue = super.save(entitate);
        store();
        return returnedValue;
    }

    @Override
    public Optional<Tema> delete(Integer id_entitate) throws RepositoryException {
        Optional<Tema> returnedValue = super.delete(id_entitate);
        store();
        return returnedValue;
    }

    @Override
    public Optional<Tema> update(Integer id_entitate, Tema entitate) throws RepositoryException {
        Optional<Tema> returnedValue = super.update(id_entitate, entitate);
        store();
        return returnedValue;
    }
}
