package repository;

import entities.Candidat;
import entities.Sectie;
import validator.Validator;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SectieRepositoryDB extends AbstractRepository<Integer, Sectie> {

    private DataBaseConnection dbConnection;
    private Connection conn;

    public SectieRepositoryDB(Validator<Sectie> validator,DataBaseConnection dbConnection) {
        super(validator);
        this.dbConnection = dbConnection;
        this.conn = this.dbConnection.getConnection();
        loadFromDB();
    }

    private void loadFromDB(){
        try {
            Statement st = conn.createStatement();
            String sql = "SELECT * FROM Sectie";
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()) {
                int id = Integer.parseInt(rs.getString(1));
                int nrLoc = Integer.parseInt(rs.getString(3));
                super.save(new Sectie(id,rs.getString(2),nrLoc));
            }
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void save(Sectie s){
        super.save(s);
        try{
            Statement st = conn.createStatement();
            String sql = String.format("INSERT INTO Sectie VALUES (%d,'%s', %d ,'%s')", s.getID(), s.getNume(), s.getNrLoc());
            st.execute(sql);
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }
    }

    @Override
    public Sectie delete(Integer id){
        Sectie s = super.delete(id);
        try{
            Statement st = conn.createStatement();
            String sql = String.format("DELETE FROM Sectie WHERE IdS = %d ", id);
            st.execute(sql);
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }
        return s;
    }

    @Override
    public void update(Integer id,Sectie s) {
        super.update(id,s);
        try{
            Statement st = conn.createStatement();
            String sql = String.format("UPDATE Sectie SET Nume = '%s' , NumarLocuri = %d WHERE IdS = %d ", s.getNume(),s.getNrLoc(),id);
            st.execute(sql);
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }
    }

}
