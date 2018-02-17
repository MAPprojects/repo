package controller;

import domain.DetaliiLog;
import domain.Student;
import domain.User;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import service.Service;
import view_FXML.AlertMessage;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class ControllerTabelLogProf extends ControllerTablePagination <DetaliiLog>  {
    protected Service service;
    protected User currentUser;
    protected Stage stage;

    @FXML
    protected TableColumn columnOperatiune;
    @FXML
    protected TableColumn columnNrTema;
    @FXML
    protected TableColumn columnNota;
    @FXML
    protected TableColumn columnDeadline;
    @FXML
    protected TableColumn columnSaptamanaPredarii;
    @FXML
    protected TableColumn columnIntarziere;
    @FXML
    protected TableColumn columnGreseli;

    public ControllerTabelLogProf() {
    }

    public void setStage(Stage stage) {
        this.stage = stage;
        stage.getIcons().setAll(new Image("/view_FXML/login/people.png"));    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    @Override
    public List<DetaliiLog> setareDataInit() {
        try {
            return service.getDetaliiLogForStudent(getStudentForUser().getId());
        } catch (FileNotFoundException e) {
            AlertMessage message = new AlertMessage();
            message.showMessage(stage, Alert.AlertType.ERROR, "Eroare", "Nu s-a gasit fisierul de log corespunzator!");
        }
        return new ArrayList<>();
    }

    public void setService(Service service) {
        this.service = service;
        if (getStudentForUser()==null){
            AlertMessage message=new AlertMessage();
            message.showMessage(stage, Alert.AlertType.ERROR,"Eroare","Studentul nu e inregistrat in sistem.");
            stage.close();
        }
        else {
            update();
        }
    }

    public Student getStudentForUser() {
        return service.getStudentByEmail(currentUser.getEmail());
    }

    @FXML
    public void initialize() {
        super.initialize();

        tableView.setEditable(false);
        columnOperatiune.setCellValueFactory(new PropertyValueFactory<DetaliiLog, String>("operatiune"));
        columnNrTema.setCellValueFactory(new PropertyValueFactory<DetaliiLog, Integer>("nrTema"));
        columnNota.setCellValueFactory(new PropertyValueFactory<DetaliiLog, Float>("valoareNota"));
        columnDeadline.setCellValueFactory(new PropertyValueFactory<DetaliiLog, Integer>("deadline"));
        columnSaptamanaPredarii.setCellValueFactory(new PropertyValueFactory<DetaliiLog, Integer>("saptamana_predarii"));
        columnIntarziere.setCellValueFactory(new PropertyValueFactory<DetaliiLog, String>("intarzieri"));
        columnGreseli.setCellValueFactory(new PropertyValueFactory<DetaliiLog, String>("greseli"));

    }

    @Override
    public void update(){
        if (getStudentForUser()==null){
            AlertMessage message=new AlertMessage();
            message.showMessage(stage, Alert.AlertType.ERROR,"Eroare","Student nu are fisier de log.");
            stage.close();
        }
        super.update();
    }
}
