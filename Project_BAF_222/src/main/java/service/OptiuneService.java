package service;

import entities.Candidat;
import entities.CheieOptiune;
import entities.Optiune;
import entities.Sectie;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import repository.AbstractRepository;
import repository.DataBaseConnection;
import repository.Repository;
import repository.RepositoryException;

import javax.persistence.criteria.CriteriaBuilder;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import static java.lang.StrictMath.max;

public class OptiuneService extends AbstractService<Optiune,CheieOptiune> {

    public AbstractService<Candidat, Integer> candidatService;
    public AbstractService<Sectie, Integer> sectieService;

    public OptiuneService(AbstractRepository<CheieOptiune, Optiune> repo, AbstractService<Candidat, Integer> candidatService, AbstractService<Sectie, Integer> sectieService) {
        super(repo);
        this.candidatService = candidatService;
        this.sectieService = sectieService;
    }

    @Override
    public void addEntity(Optiune optiune) {
        candidatService.getEntity(optiune.getIdCandidat());
        sectieService.getEntity(optiune.getIdSectie());
        int maxim = 0;
        for (Optiune aux : getAllEntities()) {
            if (aux.getIdCandidat() == optiune.getIdCandidat())
                maxim = max(maxim, aux.getPrioritate());
        }
        optiune.setPrioritate(maxim + 1);
        super.addEntity(optiune);
    }

    @Override
    public Optiune deleteEntity(CheieOptiune id) {
        for (Optiune optiune : getAllEntities())
            if (optiune.getID().equals(id)) {
                Optiune optiune1 = repo.delete(optiune);
                int prioritate = optiune1.getPrioritate();
                boolean merge = true;
                while (merge) {
                    merge = false;
                    for (Optiune optiune3 : getAllEntities())
                        if (optiune3.getIdCandidat() == optiune1.getIdCandidat() && optiune3.getPrioritate() == prioritate + 1) {
                            Optiune copyOptiune = new Optiune(optiune3.getIdOptiune(), optiune3.getIdCandidat(), optiune3.getIdSectie());
                            copyOptiune.setPrioritate(prioritate);
                            updateEntity(copyOptiune);
                            prioritate = optiune3.getPrioritate();
                            merge = true;
                            break;
                        }
                }
                notifyObservers();
                return optiune1;
            }
        throw new RepositoryException("ID-ul " + id + " nu exista!");
    }

    public ObservableList<PieChart.Data> getNumberOfEntriesPerSectie() {
        try {
            ArrayList<PieChart.Data> dataList = new ArrayList<>();
            Statement st = DataBaseConnection.getConnection().createStatement();
            String sql = "SELECT IdS, COUNT(*) " +
                    "FROM Optiune " +
                    "WHERE Prioritate = 1 " +
                    "GROUP BY IdS ";
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                int idSectie = Integer.parseInt(rs.getString(1));
                int numberOfRegistrations = Integer.parseInt(rs.getString(2));
                Sectie s = sectieService.getEntity(idSectie);
                dataList.add(new PieChart.Data(s.getNume(), numberOfRegistrations));
            }
            return FXCollections.observableArrayList(dataList);
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public ObservableList<PieChart.Data> getNumberOfCandidatsGroupedByNumberOfOptions() {
        try {
            ArrayList<PieChart.Data> dataList = new ArrayList<>();
            Statement st = DataBaseConnection.getConnection().createStatement();
            String sql = "SELECT Prioritate, COUNT(*) " +
                 "FROM Optiune " +
                 "GROUP BY Prioritate";
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                int prioritate = Integer.parseInt(rs.getString(1));
                int numberOfRegistrations = Integer.parseInt(rs.getString(2));
                dataList.add(new PieChart.Data("Candidati cu "+prioritate +" optiuni", numberOfRegistrations));
            }
            return FXCollections.observableArrayList(dataList);
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public ObservableList<PieChart.Data> getSectiiAndNumberOfRelativeRemainingEmptySlots() {
        try {
            ArrayList<PieChart.Data> dataList = new ArrayList<>();
            Statement st = DataBaseConnection.getConnection().createStatement();
            String sql = "SELECT s.IdS,(s.NumarLocuri- x.Inregistrari)\n " +
                    "FROM Sectie s,(SELECT IdS, COUNT(*) as Inregistrari \n " +
                    "\t\t\t\tFROM Optiune\n " +
                    "\t\t\t\tWHERE Prioritate = 1\n " +
                    "\t\t\t\tGROUP BY IdS) x\n " +
                    "WHERE s.IdS = x.IdS ";
            ArrayList<Integer> sectii = new ArrayList<>();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                int idSectie = Integer.parseInt(rs.getString(1));
                int numarLocuri = Integer.parseInt(rs.getString(2));
                dataList.add(new PieChart.Data(sectieService.getEntity(idSectie).getNume(), numarLocuri));
                sectii.add(idSectie);
            }


            for(Sectie s: sectieService.getAllEntities())
                if(!sectii.contains(s.getID())){
                    dataList.add(new PieChart.Data(s.getNume(), s.getNrLoc()));
                }
            return FXCollections.observableArrayList(dataList);
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public ObservableList<Optiune> getOptiuniForCandidat(Candidat c) {
        try {
            ArrayList<Optiune> dataList = new ArrayList<>();
            Statement st = DataBaseConnection.getConnection().createStatement();
            String sql = String.format("SELECT * FROM Optiune WHERE IdC = %d ",c.getID());
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                int idOptiune = Integer.parseInt(rs.getString(1));
                int idCandidat = Integer.parseInt(rs.getString(2));
                int idSectie = Integer.parseInt(rs.getString(3));
                int prioritate = Integer.parseInt(rs.getString(4));
                Optiune o = new Optiune(idOptiune,idCandidat,idSectie);
                o.setPrioritate(prioritate);
                dataList.add(o);
            }
            return FXCollections.observableArrayList(dataList);
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }


    public int getMaxId(){
        try {
            Statement st = DataBaseConnection.getConnection().createStatement();
            String sql = "SELECT MAX(IdO) FROM Optiune";
            ResultSet rs = st.executeQuery(sql);
            if(rs.next())
                return Integer.parseInt(rs.getString(1));
            else return 1;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1;
        }
    }
}
