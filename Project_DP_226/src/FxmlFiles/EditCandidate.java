package FxmlFiles;

import Entites.Candidate;
import Service.Service;
import Utils.Event;
import Utils.Observable;
import Utils.Observer;
import Validators.ValidationException;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class EditCandidate implements Observable<Event> {
    @FXML
    private TextField name;
    @FXML
    private TextField phoneNumber;

    Service service;
    private Stage editStage;
    private Candidate crtCandidate = null;
    private List<Observer<Event>> observers = new ArrayList<>();

    public EditCandidate() {
    }

    @FXML
    private void initialize() {

    }

    public void setService(Service service, Stage stage, Candidate candidate) {
        this.service = service;
        this.editStage = stage;
        this.crtCandidate = candidate;

        this.name.setText(candidate.getName());
        this.phoneNumber.setText(candidate.getPhoneNumber());

    }

    @FXML
    public void updateHandler() {
        String cName = name.getText();
        String cPhone = phoneNumber.getText();
        Candidate candidate = new Candidate(cName, cPhone);
        try {
            this.service.updateCandidate(crtCandidate.getID(), candidate);
            editStage.close();
        } catch (ValidationException e) {
            e.printStackTrace();
        }
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
