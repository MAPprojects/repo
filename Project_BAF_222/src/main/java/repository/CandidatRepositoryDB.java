package repository;

import entities.Candidat;
import validator.Validator;

import java.sql.*;

public class CandidatRepositoryDB extends AbstractRepository<Integer, Candidat> {

    private DataBaseConnection dbConnection;
    private Connection conn;
    public CandidatRepositoryDB(Validator<Candidat> validator,DataBaseConnection dbConnection) {
        super(validator);
        this.dbConnection = dbConnection;
        this.conn = this.dbConnection.getConnection();
        loadFromDB();
    }

    private void loadFromDB(){
        try {
            Statement st = conn.createStatement();
            String sql = "SELECT * FROM Candidat";
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()) {
                int id = Integer.parseInt(rs.getString(1));
                super.save(new Candidat(id,rs.getString(2),rs.getString(3),rs.getString(4)));
            }
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void save(Candidat c){
        super.save(c);
        try{
            Statement st = conn.createStatement();
            String sql = String.format("INSERT INTO Candidat VALUES (%d,'%s','%s','%s')", c.getID(), c.getNume(), c.getTelefon(), c.getEmail());
            st.execute(sql);
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }
    }

    @Override
    public Candidat delete(Integer id){
        Candidat c = super.delete(id);
        try{
            Statement st = conn.createStatement();
            String sql = String.format("DELETE FROM Candidat WHERE IdC = %d ", id);
            st.execute(sql);
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }
        return c;
    }

    @Override
    public void update(Integer id,Candidat c) {
        super.update(id,c);
        try{
            Statement st = conn.createStatement();
            String sql = String.format("UPDATE Candidat SET Nume = '%s' , Telefon = '%s' , Email = '%s' WHERE IdC = %d ", c.getNume(),c.getTelefon(),c.getEmail(),id);
            st.execute(sql);
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }
    }
}
