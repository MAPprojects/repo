package repository;

import entities.Candidat;
import entities.CheieOptiune;
import entities.Optiune;
import validator.Validator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static java.lang.Integer.max;

public class OptiuneRepositoryDB extends AbstractRepository<CheieOptiune, Optiune> {

    private DataBaseConnection dbConnection;
    private Connection conn;

    public OptiuneRepositoryDB(Validator<Optiune> validator,DataBaseConnection dbConnection) {
        super(validator);
        this.dbConnection = dbConnection;
        this.conn = this.dbConnection.getConnection();
        loadFromDB();
    }

    private void loadFromDB(){
        try {
            Statement st = conn.createStatement();
            String sql = "SELECT * FROM Optiune";
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()) {
                int idOptiune = Integer.parseInt(rs.getString(1));
                int idCandidat = Integer.parseInt(rs.getString(2));
                int idSectie = Integer.parseInt(rs.getString(3));
                int prioritate = Integer.parseInt(rs.getString(4));
                Optiune o = new Optiune(idOptiune,idCandidat,idSectie);
                o.setPrioritate(prioritate);
                super.save(o);
            }
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }
    }

    private void writeToLogFile(String mesajInitial, Optiune o, String mesajFinal){
        String numeFisierCandidat=""+o.getIdCandidat()+".txt";
        try(PrintWriter pr=new PrintWriter(new FileOutputStream(new File(numeFisierCandidat), true))){

            pr.append(mesajInitial+" optiune la sectia cu ID-ul "+o.getIdSectie());
            if(mesajInitial.equals("Adaugare")){
                pr.append(" cu prioritatea "+o.getPrioritate());
                if(o.getPrioritate()==1){
                    pr.append(" aceasta fiind prima optiune a candidatului.");
                }
                else{
                    pr.append(" prioritate determinata de optiunea anterioara.");
                }
            }
            if(mesajInitial.equals("Update")){
                pr.append(" "+mesajFinal);
            }
            if(mesajInitial.equals("Stergere")){
                pr.append(" cu prioritatea "+o.getPrioritate()+".");
            }
            pr.append(System.lineSeparator());

        } catch (IOException e) {
            System.err.println(e);
        }
    }

    @Override
    public void save(Optiune o){
        super.save(o);
        try{
            Statement st = conn.createStatement();
            String sql = String.format("INSERT INTO Optiune VALUES (%d,%d,%d, %d)", o.getIdOptiune(), o.getIdCandidat(), o.getIdSectie(), o.getPrioritate());
            st.execute(sql);
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }
        writeToLogFile("Adaugare",o,"");
    }

    private void updateRec(CheieOptiune c, Optiune o, int directieUpdate){
        Optiune aux2=dict.get(c);
        for (Optiune aux : getAll()) {
            if (aux.getIdCandidat() == o.getIdCandidat() && aux.getPrioritate() == aux2.getPrioritate() - directieUpdate) {
                Optiune aux3 = new Optiune(aux.getIdOptiune(), aux.getIdCandidat(), aux.getIdSectie());
                aux3.setPrioritate(aux.getPrioritate() + directieUpdate);
                updateRec(aux3, aux3,directieUpdate);
                break;
            }
        }
        writeToLogFile("Update",o,"din prioritatea "+aux2.getPrioritate()+" in prioritatea "+o.getPrioritate()+".");
        super.update(c,o);
        try{
            Statement st = conn.createStatement();
            String sql = String.format("UPDATE Optiune SET Prioritate = %d WHERE IdO = %d AND IdC = %d AND IdS = %d ",o.getPrioritate(),o.getIdOptiune(),o.getIdCandidat(),o.getIdSectie());
            st.execute(sql);
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void update(CheieOptiune c, Optiune o){
        Optiune aux2=dict.get(c);
        if(aux2.getPrioritate()<o.getPrioritate()) {
            int maxim = 0;
            for (Optiune aux : getAll()) {
                if (aux.getIdCandidat() == o.getIdCandidat()) {
                    maxim = max(maxim, aux.getPrioritate());
                }
            }
            if (o.getPrioritate() > maxim)
                o.setPrioritate(maxim);
            updateRec(c, o, -1);
        }
        else if(aux2.getPrioritate()>o.getPrioritate()) {
            updateRec(c, o, 1);
        }
    }

    @Override
    public Optiune delete(CheieOptiune c){
        Optiune o=super.delete(c);
        try{
            Statement st = conn.createStatement();
            String sql = String.format("DELETE FROM Optiune WHERE IdO = %d AND IdC = %d AND IdS = %d", c.getIdOptiune(), c.getIdCandidat(), c.getIdSectie());
            st.execute(sql);
        }
        catch (SQLException ex){
            throw new RepositoryException("Candidatul are adaugate optiuni. Stergeti optiunile inainte sa stergeti candidatul.");
        }
        writeToLogFile("Stergere",o,"");
        return o;
    }
}
