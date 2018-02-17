package FxmlFiles;


import Entites.Section;
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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AddSectionView implements Observable<Event> {

    @FXML
    private TextField name;
    @FXML
    private TextField number;
    private Stage editStage;
    Service service;
    Stage addStage;
    private List<Observer<Event>> observers = new ArrayList<>();
    public AddSectionView() {
    }

    @FXML
    private void initialize() {

    }

    public void setService(Service service, Stage stage) {
        this.service = service;
        this.addStage = stage;
        this.name.setText("");
        this.number.setText("");
    }
    @FXML
    public void addHandler() {
        String sName = name.getText();
        String sNumber = number.getText();
        try {
            Section section = new Section(sName, Integer.parseInt(sNumber));
            this.service.addSection(section);
            addStage.close();
        } catch (Exception  e) {
            showErrorMessage("Number must be integer!");
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
