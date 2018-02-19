package viewController;

import entities.Sectie;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import observer.Observer;
import service.AbstractService;
import service.SectieService;

import java.util.List;
import java.util.function.Predicate;

public class SectieFilterController implements Observer{

    @FXML
    private TextField nameFilterTextField, idFilterTextField;

    @FXML
    private Slider numberOfPlacesSlider;

    @FXML
    private RadioButton idFilterMoreRadioButton,idFilterLessRadioButton;

    @FXML
    private CheckBox nameFilterCheckBox,numberOfPlacesFilterCheckBox,idFieldCheckBox;

    @FXML
    private Label numberOfPlacesLabel;


    private AbstractService<Sectie,Integer> service;
    private Stage stage;
    private Predicate<Sectie> filterByNameContains=x->x.getNume().toLowerCase().contains(nameFilterTextField.getText().toLowerCase());
    private Predicate<Sectie> filterByMailConstains=x->x.getNrLoc()<Math.round(numberOfPlacesSlider.getValue());


    private Predicate<Sectie> filterByIdMoreOrLess= x->((idFilterMoreRadioButton.isSelected() && x.getID()>Integer.parseInt(idFilterTextField.getText())) || (idFilterLessRadioButton.isSelected() && x.getID()<Integer.parseInt(idFilterTextField.getText())));
    private SectieTableController tableController;


    public void setService(SectieService sectieService, Stage dialogStage, SectieTableController tableController) {
        this.service=sectieService;
        this.stage=dialogStage;
        this.tableController=tableController;
        update();

    }

    @FXML
    private void handleOk(){
        boolean ok=true;
        Predicate<Sectie> auxPred=x->true;
        if(nameFilterCheckBox.isSelected()){
            if(nameFilterTextField.getText().equals("")) {
                nameFilterTextField.setPromptText("Dati secventa!!!");
                ok=false;
            }
            else {
                auxPred = filterByNameContains.and(auxPred);
            }
        }
        if(numberOfPlacesFilterCheckBox.isSelected()){
            auxPred = filterByMailConstains.and(auxPred);
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
            List<Sectie> filteredList = service.filterAndSorter(service.getAllEntities(), auxPred, Sectie.compSectieById);
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
        numberOfPlacesSlider.setValue(0);
        idFilterTextField.setPromptText("");
        nameFilterCheckBox.setSelected(false);
        numberOfPlacesFilterCheckBox.setSelected(false);
        idFieldCheckBox.setSelected(false);
        nameFilterTextField.setText("");
        idFilterTextField.setText("");
        idFilterMoreRadioButton.setSelected(true);
        numberOfPlacesLabel.setText("0");
        service.notifyObservers();
        stage.close();
    }

    @FXML
    private void handleSliderValueChange(){
        numberOfPlacesLabel.setText(""+Math.round(numberOfPlacesSlider.getValue()));
    }

    @Override
    public void update() {
        int maxi=0;
        for(Sectie c: service.getAllEntities()){
            maxi=Integer.max(maxi,c.getNrLoc());
        }
        numberOfPlacesSlider.setMax(maxi);
    }
}
