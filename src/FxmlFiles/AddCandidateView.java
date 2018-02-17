package FxmlFiles;

import Entites.Candidate;
import Service.Service;
import Utils.DBConnection;
import Utils.Event;
import Utils.Observable;
import Utils.Observer;
import Validators.ValidationException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AddCandidateView implements Observable<Event> {

    @FXML
    private TextField name;
    @FXML
    private TextField phoneNumber;
    private Stage editStage;
    Service service;
    private Stage addStage;
    private List<Observer<Event>> observers = new ArrayList<>();

    public AddCandidateView() {
    }

    @FXML
    private void initialize() {

    }

    public void setService(Service service, Stage stage) {
        this.service = service;
        this.addStage = stage;
        this.name.setText("");
        this.phoneNumber.setText("");
    }

    @FXML
    public void addHandler() {
        String cName = name.getText();
        String cPhone = phoneNumber.getText();
        Candidate candidate = new Candidate(cName, cPhone);
        try {
            this.service.addCandidate(candidate);
            addStage.close();
        } catch (ValidationException e) {
            showErrorMessage("");
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
