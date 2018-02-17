package FxmlFiles;

import Entites.Section;
import Service.Service;
import Utils.DBConnection;
import Utils.Event;
import Utils.Observable;
import Utils.Observer;
import Validators.ValidationException;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EditSection implements Observable<Event> {
    @FXML
    private TextField section;
    @FXML
    private TextField number;

    Service service;
    Stage editStage;
    Section crtSection = null;
    private List<Observer<Event>> observers = new ArrayList<>();

    public EditSection() {
    }

    @FXML
    private void initialize() {
    }

    public void setService(Service service, Stage stage, Section section) {
        this.service = service;
        this.editStage = stage;
        this.crtSection = section;

        this.section.setText(section.getName());
        this.number.setText(String.valueOf(section.getNumber()));

    }

    @FXML
    public void updateHandler() {
        String sName = section.getText();
        Integer sNumber = Integer.parseInt(number.getText());
        Section section = new Section(sName, sNumber);
        try {
            this.service.updateSection(crtSection.getID(), section);
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
