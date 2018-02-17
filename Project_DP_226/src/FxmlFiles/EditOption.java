package FxmlFiles;

import Entites.Option;
import Service.Service;
import Utils.DBConnection;
import Utils.Event;
import Utils.Observable;
import Utils.Observer;
import Validators.ValidationException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class EditOption implements Observable<Event> {
    @FXML
    private TextField idCandidate;
    @FXML
    private TextField idSection;
    @FXML
    private TextField priority;
    Service service;
    Stage editStage;
    Option crtOption = null;
    private List<Observer<Event>> observers = new ArrayList<>();

    public EditOption() {
    }

    @FXML
    private void initialize(){
    }

    public void setService(Service service, Stage stage, Option option) {
        this.service = service;
        this.editStage = stage;
        this.crtOption= option;

        this.idCandidate.setText(String.valueOf(option.getIdCandidate()));
        this.idSection.setText(String.valueOf(option.getIdSection()));
        this.priority.setText(String.valueOf(option.getPriority()));
    }

    @FXML
    public void updateHandler() {
        Integer idC = Integer.parseInt(idCandidate.getText());
        Integer idS = Integer.parseInt(idSection.getText());
        Integer pty = Integer.parseInt(priority.getText());
        Option option = new Option(idC,idS, pty);
        try {
            this.service.updateOption(crtOption.getID(), option);
            editStage.close();
        } catch (ValidationException  e) {
           showErrorMessage("Id invalid!");
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
        observers.stream().forEach(x -> x.notifyOnEvent(t));
    }
}

