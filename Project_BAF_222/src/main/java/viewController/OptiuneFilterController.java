package viewController;

import entities.CheieOptiune;
import entities.Optiune;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import observer.Observer;
import service.AbstractService;
import service.OptiuneService;

import java.util.List;
import java.util.function.Predicate;

import static java.lang.StrictMath.max;

public class OptiuneFilterController implements Observer{

    @FXML
    private TextField candidatNameFilterTextField, sectieNameFilterTextField;

    @FXML
    private CheckBox candidatNameFilterCheckBox, sectieNameFilterCheckBox,numberOfOptionsFilterCheckBox;

    @FXML
    private Slider numberOfOptionsSlider;


    private AbstractService<Optiune, CheieOptiune> service;
    private Stage stage;
    private OptiuneTableController tableController;


    private Predicate<Optiune> filterByCandidatNameContains=x->{OptiuneService auxService=(OptiuneService) service;
                                                        return auxService.candidatService.getEntity(x.getIdCandidat()).getNume().toLowerCase().contains(candidatNameFilterTextField.getText());
    };
    private Predicate<Optiune> filterBySectieNameConstains=x->{

        OptiuneService auxService=(OptiuneService) service;
        return auxService.sectieService.getEntity(x.getIdSectie()).getNume().toLowerCase().contains(sectieNameFilterTextField.getText());
    };

    private Predicate<Optiune> filterByNumberOfOptions=x->{
        int maxi=0;
        for(Optiune o:service.getAllEntities()){
            if(x.getIdCandidat()==o.getIdCandidat())
                maxi=max(maxi,o.getPrioritate());
        }
        return maxi==numberOfOptionsSlider.getValue();
    };



    public void setService(OptiuneService optiuneService, Stage dialogStage, OptiuneTableController tableController) {
        this.service=optiuneService;
        this.stage=dialogStage;
        this.tableController=tableController;
        int maxi=0;
        for(Optiune o: service.getAllEntities()){
            if(o.getPrioritate()>maxi)
                maxi=o.getPrioritate();
        }
        numberOfOptionsSlider.setMax(maxi);
    }

    @FXML
    private void handleOk(){
        boolean ok=true;
        Predicate<Optiune> auxPred=x->true;
        if(candidatNameFilterCheckBox.isSelected()){
            if(candidatNameFilterTextField.getText().equals("")) {
                candidatNameFilterTextField.setPromptText("Dati nume!!!");
                ok=false;
            }
            else {
                auxPred = filterByCandidatNameContains.and(auxPred);
            }
        }
        if(sectieNameFilterCheckBox.isSelected()){
            if(sectieNameFilterTextField.getText()==null){
                sectieNameFilterTextField.setPromptText("Dati sectie!!!");
                ok=false;
            }
            else {
                auxPred = filterBySectieNameConstains.and(auxPred);
            }
        }
        if(numberOfOptionsFilterCheckBox.isSelected()){
            auxPred = filterByNumberOfOptions.and(auxPred);

        }
        if(ok==true) {
            List<Optiune> filteredList = service.filterAndSorter(service.getAllEntities(), auxPred, Optiune.compOptiuneByCandidatIdAndPriority);
            tableController.setTableValues(filteredList);
            stage.close();
        }
    }

    @FXML
    private void handleCancel(){
        stage.close();
    }

    @FXML
    private void handleReset(){

        candidatNameFilterTextField.setPromptText("");
        sectieNameFilterTextField.setPromptText("");
        candidatNameFilterCheckBox.setSelected(false);
        sectieNameFilterCheckBox.setSelected(false);
        numberOfOptionsFilterCheckBox.setSelected(false);
        candidatNameFilterTextField.setText("");
        sectieNameFilterTextField.setText("");
        service.notifyObservers();
        stage.close();
    }

    @Override
    public void update() {
        //to do
    }
}
