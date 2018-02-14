package Repositories;

import Domain.Nota;
import Domain.Tema;
import Exceptions.RepositoryException;

import javax.swing.text.html.Option;
import java.sql.*;
import java.util.ArrayList;
import java.util.Optional;

public class SQLNotaRepo extends NotaRepository {

    public SQLNotaRepo() {
        super();
        load();
    }

    private void load() {
        Connection connection = getConnection();
        String selectQuery = "SELECT * FROM Marks";
        try {
            PreparedStatement statement = connection.prepareStatement(selectQuery);
            ResultSet result = statement.executeQuery();
            while(result.next()) {
                Integer idStudent = result.getInt("idStudent");
                Integer nrTema = result.getInt("nrTema");
                Integer nota = result.getInt("nota");
                save(new Nota(idStudent, nrTema, nota));
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
        String deleteQuery = "DELETE FROM Marks";
        Iterable<Nota> it = findAll();
        ArrayList<Nota> allMarks = new ArrayList<>();
        for(Nota n : it)
            allMarks.add(n);
        String addQuery = "";
        try {
            statement = connection.prepareStatement(deleteQuery);
            statement.executeUpdate();
            for(Nota n : allMarks) {
                addQuery="INSERT INTO Marks VALUES(" + n.getID() + "," + n.getIdStudent() + "," + n.getNrTema() + "," + n.getNota() + ")";
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
    public Optional<Nota> save(Nota entitate) throws RepositoryException {
        Optional<Nota> returnedValue = super.save(entitate);
        store();
        return returnedValue;
    }

    @Override
    public Optional<Nota> delete(Integer id_entitate) throws RepositoryException {
        Optional<Nota> returnedValue = super.delete(id_entitate);
        store();
        return returnedValue;
    }

    @Override
    public Optional<Nota> update(Integer id_entitate, Nota entitate) throws RepositoryException {
        Optional<Nota> returnedValue = super.update(id_entitate,entitate);
        store();
        return returnedValue;
    }
}
