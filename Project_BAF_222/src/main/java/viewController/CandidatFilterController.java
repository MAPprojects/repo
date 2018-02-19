package viewController;


import entities.Candidat;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import service.AbstractService;
import service.CandidatService;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class CandidatFilterController {

    @FXML
    private TextField nameFilterTextField, idFilterTextField;

    @FXML
    private ComboBox emailFilterComboBox;

    @FXML
    private RadioButton idFilterMoreRadioButton,idFilterLessRadioButton;

    @FXML
    private CheckBox nameFilterCheckBox,emailFilterCheckBox,idFieldCheckBox;


    private AbstractService<Candidat,Integer> service;
    private Stage stage;
    private Predicate<Candidat> filterByNameContains=x->x.getNume().toLowerCase().contains(nameFilterTextField.getText().toLowerCase());
    private Predicate<Candidat> filterByMailConstains=x->{
        String domain=x.getEmail().split("@")[1];
        String emailProvider=domain.substring(0,domain.lastIndexOf('.'));
        return emailProvider.equals((String)emailFilterComboBox.getValue());
        };

    private Predicate<Candidat> filterByIdMoreOrLess=x->((idFilterMoreRadioButton.isSelected() && x.getID()>Integer.parseInt(idFilterTextField.getText())) || (idFilterLessRadioButton.isSelected() && x.getID()<Integer.parseInt(idFilterTextField.getText())));
    CandidatTableController tableController;


    public void setService(CandidatService candidatService, Stage dialogStage, CandidatTableController tableController) {
        this.service=candidatService;
        this.stage=dialogStage;
        this.tableController=tableController;
        ArrayList<String> emailProviders=new ArrayList<>();
        for(Candidat c: service.getAllEntities()){
            String domain=c.getEmail().split("@")[1];
            String emailProvider=domain.substring(0,domain.lastIndexOf('.'));
            if(!emailProviders.contains(emailProvider)) {
                emailProviders.add(emailProvider);
            }
        }
        emailFilterComboBox.setItems(FXCollections.observableArrayList(emailProviders));
    }

    @FXML
    private void handleOk(){
        boolean ok=true;
        Predicate<Candidat> auxPred=x->true;
        if(nameFilterCheckBox.isSelected()){
            if(nameFilterTextField.getText().equals("")) {
                nameFilterTextField.setPromptText("Dati secventa!!!");
                ok=false;
            }
            else {
                auxPred = filterByNameContains.and(auxPred);
            }
        }
        if(emailFilterCheckBox.isSelected()){
            if(emailFilterComboBox.getValue()==null){
                emailFilterComboBox.setPromptText("Dati val.!!!");
                ok=false;
            }
            else {
                auxPred = filterByMailConstains.and(auxPred);
            }
        }
        if(idFieldCheckBox.isSelected()){
            if(idFilterTextField.getText().equals("")) {
                idFilterTextField.setPromptText("Dati id!!!");
                ok=false;
            }
            else {
                auxPred = filterByIdMoreOrLess.and(auxPred);
            }
        }
        if(ok==true) {
            List<Candidat> filteredList = service.filterAndSorter(service.getAllEntities(), auxPred, Candidat.compCandidatById);
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
        nameFilterTextField.setPromptText("");
        emailFilterComboBox.setPromptText("");
        idFilterTextField.setPromptText("");
        nameFilterCheckBox.setSelected(false);
        emailFilterCheckBox.setSelected(false);
        idFieldCheckBox.setSelected(false);
        nameFilterTextField.setText("");
        idFilterTextField.setText("");
        idFilterMoreRadioButton.setSelected(true);
        service.notifyObservers();
        stage.close();
    }


}
