package viewController;

import entities.CheieOptiune;
import entities.HybridOptiune;
import entities.Optiune;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import service.AbstractService;
import service.OptiuneService;

import java.util.ArrayList;
import java.util.List;


public class OptiuneTableController extends AbstractTableController<Optiune,CheieOptiune>{

    @FXML
    private TableColumn<HybridOptiune, Integer> optiuneIdColumn;
    @FXML
    private TableColumn<HybridOptiune, Integer> candidatIdColumn;
    @FXML
    private TableColumn<HybridOptiune, String> candidatNameColumn;
    @FXML
    private TableColumn<HybridOptiune, Integer> sectieIdColumn;
    @FXML
    private TableColumn<HybridOptiune, String> sectieNameColumn;
    @FXML
    private TableColumn<HybridOptiune, Integer> priorityColumn;

    OptiuneService newService;

    @FXML
    protected void initialize() {

        optiuneIdColumn.setCellValueFactory(new PropertyValueFactory<HybridOptiune, Integer>("IdOptiune"));
        candidatIdColumn.setCellValueFactory(new PropertyValueFactory<HybridOptiune, Integer>("IdCandidat"));
        candidatNameColumn.setCellValueFactory(new PropertyValueFactory<HybridOptiune, String>("numeCandidat"));
        sectieIdColumn.setCellValueFactory(new PropertyValueFactory<HybridOptiune, Integer>("IdSectie"));
        sectieNameColumn.setCellValueFactory(new PropertyValueFactory<HybridOptiune, String>("numeSectie"));
        priorityColumn.setCellValueFactory(new PropertyValueFactory<HybridOptiune, Integer>("prioritate"));
        super.initialize();
    }

    @Override
    public void setService(AbstractService service, MainWindowController mainWindowsController){
        super.service=service;
        newService=(OptiuneService) service;
        super.mainWindowsController=mainWindowsController;
        ArrayList<HybridOptiune> hybridList=new ArrayList<>();
        for(Object o:service.getAllEntities()){
            HybridOptiune hybridOptiune=new HybridOptiune(((Optiune) o).getIdOptiune(),((Optiune) o).getIdCandidat(),((Optiune) o).getIdSectie(),newService.candidatService.getEntity(((Optiune) o).getIdCandidat()).getNume(),newService.sectieService.getEntity(((Optiune) o).getIdSectie()).getNume());
            hybridOptiune.setPrioritate(((Optiune) o).getPrioritate());
            hybridList.add(hybridOptiune);
        }
        super.initPagination(0,hybridList);
    }


    @Override
    public void setTableValues(List<Optiune> newList){
        ArrayList<HybridOptiune> hybridList=new ArrayList<>();
        for(Object o:newList){
            HybridOptiune hybridOptiune=new HybridOptiune(((Optiune) o).getIdOptiune(),((Optiune) o).getIdCandidat(),((Optiune) o).getIdSectie(),newService.candidatService.getEntity(((Optiune) o).getIdCandidat()).getNume(),newService.sectieService.getEntity(((Optiune) o).getIdSectie()).getNume());
            hybridOptiune.setPrioritate(((Optiune) o).getPrioritate());
            hybridList.add(hybridOptiune);
        }
        super.initPagination(0,hybridList);
    }

    @Override
    public void update() {
        ArrayList<HybridOptiune> hybridList=new ArrayList<>();
        for(Object o:service.getAllEntities()){
            HybridOptiune hybridOptiune=new HybridOptiune(((Optiune) o).getIdOptiune(),((Optiune) o).getIdCandidat(),((Optiune) o).getIdSectie(),newService.candidatService.getEntity(((Optiune) o).getIdCandidat()).getNume(),newService.sectieService.getEntity(((Optiune) o).getIdSectie()).getNume());
            hybridOptiune.setPrioritate(((Optiune) o).getPrioritate());
            hybridList.add(hybridOptiune);
        }
        super.initPagination(pagination.getCurrentPageIndex(),hybridList);
        table.refresh();
    }




}
