package Constrollers;

import Domain.Student;
import ExceptionsAndValidators.AbstractException;
import Service.Service;
import Views.StudentView;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class StudentController implements Observer
{
    Service service;
    StudentView stView;
    ObservableList<Student> modelStudent;
    public StudentController() {
    }




    public StudentController(Service service)
    {
        this.service = service;
        this.modelStudent = FXCollections.observableArrayList(service.iterableToArrayList(service.getStudenti()));
    }



    public void setView(StudentView view) {
        this.stView = view;
    }

    public ObservableList<Student> getModelStudent()
    {
        return modelStudent;
    }


    public void fillFields(Student newValue) {
        if(newValue!=null)
        {
            stView.textID.setText(newValue.getID().toString());
            stView.textID.setDisable(true);
            stView.textNume.setText(newValue.getNume());
            stView.textGrupa.setText(""+newValue.getGrupa());
            stView.textEmail.setText(newValue.getEmail());
            stView.textCadruDidactic.setText(newValue.getCadruDidactic());
        }
    }

    public void handleClear(ActionEvent actionEvent) {
        stView.textID.clear();
        stView.textCadruDidactic.clear();
        stView.textEmail.clear();
        stView.textGrupa.clear();
        stView.textNume.clear();
        stView.textID.setDisable(false);
        modelStudent.setAll(FXCollections.observableArrayList(service.iterableToArrayList(service.getStudenti())));

    }

    public void handleAdd(ActionEvent actionEvent) {
        Integer id = new Integer(stView.textID.getText());
        String nume = stView.textNume.getText();
        Integer grupa = new Integer(stView.textGrupa.getText());
        String email = stView.textEmail.getText();
        String cadruDidactic = stView.textCadruDidactic.getText();

        try {
            service.addStudent(id, nume, grupa, email, cadruDidactic);
            showMessage("Adaugarea");
        }
        catch(AbstractException ex)
        {
            showError(ex.getMessage());
        }

    }

    public void handleDelete(ActionEvent actionEvent)
    {
        Integer id = new Integer(stView.textID.getText());
        try
        {
            service.delete(id);
            showMessage("Stergerea ");
        }
        catch(AbstractException ex)
        {
            showError(ex.getMessage());
        }
    }

    private void showError(String msg)
    {
        Alert al = new Alert(Alert.AlertType.ERROR);
        al.setTitle("Eroare");
        al.setContentText(msg);
        al.showAndWait();
    }

    private void showMessage(String msg)
    {
        Alert al = new Alert(Alert.AlertType.INFORMATION);
        al.setTitle("Succes");
        al.setContentText(msg + " a fost realizata!");
        al.show();
    }


    public void handleUpdate(ActionEvent actionEvent)
    {
        Integer id = new Integer(stView.textID.getText());
        String nume = stView.textNume.getText();
        Integer grupa = new Integer(stView.textGrupa.getText());
        String email = stView.textEmail.getText();
        String cadruDidactic = stView.textCadruDidactic.getText();

        try {
            service.updateStudent(id, nume, grupa, email, cadruDidactic);
            showMessage("Modificarea");
        }
        catch(AbstractException ex)
        {
            showError(ex.getMessage());
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        modelStudent.setAll(FXCollections.observableArrayList(service.iterableToArrayList(service.getStudenti())));
    }

    public void handleFilter(ActionEvent actionEvent) {
        List<Student> temp=new ArrayList<>();
        try {
            switch (stView.cmbFilt.getValue()) {
                case "Nume":
                    temp = service.filterNume(service.iterableToArrayList(service.getStudenti()),stView.txtFilt.getText());
                    break;
                case "Grupa":
                    temp = service.filterGrupa(service.iterableToArrayList(service.getStudenti()),new Integer(stView.txtFilt.getText()));
                    break;
                case "Cadru Didactic":
                    temp = service.filterCadruDidactic(service.iterableToArrayList(service.getStudenti()),stView.txtFilt.getText());
                    break;
            }
            modelStudent.setAll(FXCollections.observableArrayList(temp));
        }
        catch(AbstractException ex)
        {
            showError(ex.getMessage());
        }
    }



}
