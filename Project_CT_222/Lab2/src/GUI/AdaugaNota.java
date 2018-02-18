package GUI;

import Domain.Nota;
import Domain.NoteDTO;
import Domain.Studenti;
import Domain.Teme;
import Service.Service;
import Utils.ListEvent;
import Utils.Observer;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class AdaugaNota {
    @FXML
    private TextField idStudent;
    @FXML
    private TextField Valoare;
    @FXML
    private TextField SaptamanaPredarii;
    @FXML
    private TextArea Observatii;
    @FXML
    private Button adaugaButton;

    @FXML
    public ComboBox teme;

    public void onEnterIdStud(ActionEvent ae){
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                Valoare.requestFocus();
            }
        });
    }
    public void onEnterValoare(ActionEvent ae){
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                SaptamanaPredarii.requestFocus();
            }
        });
    }

    public void onEnterSaptamanaPredarii(ActionEvent ae){
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                Observatii.requestFocus();
            }
        });
    }

    public void onEnterObservatii(ActionEvent ae){
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                addButtonHandler();
            }
        });
    }


    Studenti student;
    private Service service;
    private Stage editStage;

    public void setService(Studenti student,Service service){
        this.student=student;

        this.service=service;
        List<String> stringuri=new ArrayList<>();
        for(NoteDTO nota:service.getAllNoteDTO())
        {
            if(nota.getStudent().getId()==student.getId())
                for(Teme t: service.getTemeInLista())
                {
                    if(nota.getGrades().get(t.getId())==null)
                        stringuri.add(String.valueOf(t.getId()));}

        }
        ObservableList<String> listCombo1 = FXCollections.observableArrayList(stringuri);
        teme.setItems(listCombo1);
        teme.setVisibleRowCount(3);
        idStudent.setText(String.valueOf(student.getId()));
        idStudent.setDisable(true);


        Observatii.setPromptText("Observatii");

        idStudent.setPromptText("Student id: only numbers");
        Valoare.setPromptText("Valoare: only numbers");
        SaptamanaPredarii.setPromptText("Saptamana predarii: only numbers");

        RestrictFields();




    }

    public void RestrictFields(){

        idStudent.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    idStudent.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        Valoare.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    Valoare.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        SaptamanaPredarii.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    SaptamanaPredarii.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
    }





    public void addButtonHandler(){
        if(idStudent.getText().equals("")||idStudent.getText().equals("")||Observatii.getText().equals("")){

                showErrorMessage("Completati toate fieldurile");


        }
        else{
        {
        try {
            String idTema=(String)teme.getSelectionModel().getSelectedItem();

            service.adaugareNota( Integer.parseInt(idStudent.getText()), Integer.parseInt(idTema), Integer.parseInt(Valoare.getText()), Integer.parseInt(SaptamanaPredarii.getText()),Observatii.getText());
            Stage stage = (Stage) adaugaButton.getScene().getWindow();
            // do what you have to do
            stage.close();
        } catch (Exception e) {
            showErrorMessage(e.toString());
        }}}
    }
    private void showErrorMessage(String text) {
        Alert message = new Alert(Alert.AlertType.ERROR);
        message.setTitle("Error Message");
        message.initOwner(editStage);
        message.setContentText(text);
        message.showAndWait();
    }
}
