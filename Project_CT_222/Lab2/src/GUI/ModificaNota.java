package GUI;

import Domain.Nota;
import Domain.NoteDTO;
import Domain.Studenti;
import Domain.Teme;
import Service.Service;
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

public class ModificaNota {
        @FXML
        TextField idNota,idStudent,Valoare,SaptamanaPredarii;

        @FXML
        TextArea Observatii;
        @FXML
        Button modificaButton;

        @FXML
        public ComboBox teme;



    public void onEnterSaptamanaPredarii(ActionEvent ae){
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                Observatii.requestFocus();
            }
        });
    }

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

    public void onEnterObservatii(ActionEvent ae){
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                modButtonHandler();
            }
        });
    }

        private Service service;
        private Stage editStage;
        public void setService(Studenti student, Service service){
            Observatii.setPromptText("Observatii");
            this.service=service;
            idNota.setPromptText("Id: only numbers");
            idStudent.setPromptText("Student id: only numbers");
            Valoare.setPromptText("Valoare: only numbers");
            SaptamanaPredarii.setPromptText("Saptamana predarii: only numbers");
            idNota.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue,
                                    String newValue) {
                    if (!newValue.matches("\\d*")) {
                        idNota.setText(newValue.replaceAll("[^\\d]", ""));
                    }
                }
            });
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
            idNota.setDisable(true);
            idStudent.setText(String.valueOf(student.getId()));
            idStudent.setDisable(true);
            List<Teme> tema=service.getTemeInLista();
            List<String> stringuri=new ArrayList<>();
            for(NoteDTO nota:service.getAllNoteDTO())
            {
                if(nota.getStudent().getId()==student.getId())
                    for(Teme t: service.getTemeInLista())
                    {
                        if(nota.getGrades().get(t.getId())!=null)
                            stringuri.add(String.valueOf(t.getId()));}

            }
            ObservableList<String> listCombo1 = FXCollections.observableArrayList(stringuri);
            teme.setItems(listCombo1);
            teme.setVisibleRowCount(3);
        }

        public void comboSelectionHandler(){
            String temaId=(String)teme.getSelectionModel().getSelectedItem();
            for(Nota nota: service.getAllNote()){
                if(Integer.parseInt(idStudent.getText())==nota.getIdStudent() && nota.getIdTema()==Integer.parseInt(temaId)){
                    Integer notaCautata=nota.getId();
                    idNota.setText(String.valueOf(notaCautata));
                    break;
                }
            }

        }
        public void modButtonHandler(){
            if(idNota.getText().equals("")||idStudent.getText().equals("")||idStudent.getText().equals("")||Observatii.getText().equals("")){

                showErrorMessage("Completati toate fieldurile");


            }
            else{
            try {
                String idTema=(String)teme.getSelectionModel().getSelectedItem();

                service.updateNota(Integer.parseInt(idNota.getText()), Integer.parseInt(Valoare.getText()), Integer.parseInt(SaptamanaPredarii.getText()),Observatii.getText());
                Stage stage = (Stage) modificaButton.getScene().getWindow();
                // do what you have to do
                stage.close();
            } catch (Exception e) {
                showErrorMessage(e.toString());
            }}
        }
        private void showErrorMessage(String text) {
            Alert message = new Alert(Alert.AlertType.ERROR);
            message.setTitle("Error Message");
            message.initOwner(editStage);
            message.setContentText(text);
            message.showAndWait();
        }
    }
