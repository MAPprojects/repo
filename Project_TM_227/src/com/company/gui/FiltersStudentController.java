package com.company.gui;

import com.company.domain.Student;
import com.company.service.Service;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;


public class FiltersStudentController {
    public Service service;
    public Stage stage;
    public TableColumn idCSFilter;
    public TableColumn numeCSFilter;
    public TableColumn grupaCSFilter;
    public TableColumn emailCSFilter;
    public TableColumn cadruCSFilter;
    public TextField numeFSFilter;
    public TextField cadruFSFilter;
    public ComboBox comboSFilter;
    public Button btnDupaNumeS;
    public Button btnDupaCadruS;
    public Button btnDupaGrupaS;
    public Button btnCloseS;
    public TableView studentiTabelFilter;

    ObservableList<Student> studentObservableList;
    public FiltersStudentController() {
    }

    @FXML
    private void initialize(){
        idCSFilter.setCellValueFactory(new PropertyValueFactory<Student, Integer>("Id"));
        numeCSFilter.setCellValueFactory(new PropertyValueFactory<Student, String>("nume"));
        grupaCSFilter.setCellValueFactory(new PropertyValueFactory<Student, Integer>("grupa"));
        emailCSFilter.setCellValueFactory(new PropertyValueFactory<Student, String>("email"));
        cadruCSFilter.setCellValueFactory(new PropertyValueFactory<Student, Integer>("cadru_didactic"));
    }

    public void setService(Service s, Stage stage) {
        this.service = s;
        this.stage = stage;

        ObservableList<Integer> grupe = FXCollections.observableArrayList(service.geaAllGrupe());
        comboSFilter.setItems(grupe);

    }

    public void filterByNume(){
        try {
            String nume = numeFSFilter.getText().trim();
            List<Student> students = service.filtreazaStudentiDupaNume(nume);
            if (students.size() == 0)
                throw new Exception("Nu exista date pentru filtrarea dorita");
            studentObservableList = FXCollections.observableList(students);
            studentiTabelFilter.setItems(studentObservableList);

        } catch (Exception e) {
            showInformation(Alert.AlertType.WARNING, "Eroare", e.getMessage());
        }
    }

    public void filterByCadru(){
        try {
            String cadru = cadruFSFilter.getText().trim();
            List<Student> students = service.filtreazaStudentiDupaCadru(cadru);
            if (students.size() == 0)
                throw new Exception("Nu exista date pentru filtrarea dorita");
            studentObservableList = FXCollections.observableList(students);
            studentiTabelFilter.setItems(studentObservableList);

        } catch (Exception e) {
            showInformation(Alert.AlertType.WARNING, "Eroare", e.getMessage());
        }
    }

    public void filterByGrupa(){
        try {
            if (comboSFilter.getValue() == null)
                throw new Exception("Nu este selectata nici o grupa");
            int grupa = (int) comboSFilter.getValue();
            List<Student> students = service.filtreazaStudentiDupaGrupa(grupa);
            if (students.size() == 0)
                throw new Exception("Nu exista date pentru filtrarea dorita");
            studentObservableList = FXCollections.observableList(students);
            studentiTabelFilter.setItems(studentObservableList);

        } catch (Exception e) {
            showInformation(Alert.AlertType.WARNING, "Eroare", e.getMessage());
        }
    }


    public void closeFilters(){
        stage.close();
    }


    private void showInformation(Alert.AlertType type, String titlu, String mesaj){
        Alert alert = new Alert(type);
        alert.setHeaderText(titlu);
        alert.setContentText(mesaj);
        alert.showAndWait();
    }


}
