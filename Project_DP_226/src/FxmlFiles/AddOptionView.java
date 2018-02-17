package FxmlFiles;

import Entites.Candidate;
import Entites.Option;
import Entites.Section;
import Service.Service;
import Utils.Event;
import Utils.Observable;
import Utils.Observer;
import Validators.ValidationException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;

public class AddOptionView implements Observable<Event> {

    @FXML
    ComboBox<String> comboCandidate;
    @FXML
    ComboBox<String> comboSection;

    @FXML
    private TextField priority;
    private Stage editStage;
    Service service;
    Stage addStage;
    private List<Observer<Event>> observers = new ArrayList<>();

    public AddOptionView() {
    }



    public void setService(Service service, Stage stage) {
        this.service = service;
        this.addStage = stage;
        ObservableList<String> listCombo = FXCollections.observableArrayList();
        for(Candidate candidate:service.findAllCandidates())
            listCombo.add(candidate.getName());
        comboCandidate.setItems(listCombo);
        ObservableList<String> listCombo2 = FXCollections.observableArrayList();
        for(Section section:service.findAllSections())
            listCombo2.add(section.getName());
        comboSection.setItems(listCombo2);
        this.priority.setText("");
    }
    @FXML
    private void initialize() {

    }

    @FXML
    public void addHandler() {
        //comboSorter.getSelectionModel().getSelectedItem()
        Integer idC = service.findCandidate(comboCandidate.getSelectionModel().getSelectedItem()).getID();
        Integer idS = service.findSection(comboSection.getSelectionModel().getSelectedItem()).getID();
        Integer pry = Integer.parseInt(priority.getText());
        Option option = new Option(idC, idS, pry);
        try {
           this.service.addOption(option);
            addStage.close();
        } catch (ValidationException e) {
           //showErrorMessage("Invalid id!");
            e.printStackTrace();
        }

    }
    private void showErrorMessage(String text) {
        Alert message = new Alert(Alert.AlertType.ERROR);
        message.setTitle("Error Message");
        message.initOwner(editStage);
        message.setContentText(text);
        message.showAndWait();
    }
    @Override
    public void addObserver(Observer<Event> e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer<Event> e) {

    }

    @Override
    public void notifyObservers(Event t) {

    }
}


