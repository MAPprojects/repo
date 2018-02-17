package com.company.gui;

import com.company.domain.Nota;
import com.company.domain.Student;
import com.company.service.Service;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.util.List;

public class FiltersNotaController {
    public Service service;
    public Stage stage;
    public TableView noteTableFilters;
    public TableColumn idCNFilter;
    public TableColumn idSCNFilter;
    public TableColumn idTCNFilter;
    public TableColumn valoareCNFilter;
    public TableColumn observatiCNFilter;
    public ComboBox comboNNotaFilter;
    public ComboBox comboNIdSFilter;
    public ComboBox comboNIdTFilter;
    public CheckBox checkNota;
    public CheckBox checkIdS;
    public CheckBox checkIdT;
    public Button btnClose;

    ObservableList<Nota> notaObservableList;
    public FiltersNotaController() {
    }

    @FXML
    private void initialize(){
        idCNFilter.setCellValueFactory(new PropertyValueFactory<Nota, Integer>("Id"));
        idSCNFilter.setCellValueFactory(new PropertyValueFactory<Nota, Integer>("idStudent"));
        idTCNFilter.setCellValueFactory(new PropertyValueFactory<Nota, Integer>("idTema"));
        valoareCNFilter.setCellValueFactory(new PropertyValueFactory<Nota, Integer>("valoare"));
        observatiCNFilter.setCellValueFactory(new PropertyValueFactory<Nota, String>("observatie"));
    }

    public void setService(Service s, Stage stage) {
        this.service = s;
        this.stage = stage;

        ObservableList<Integer> note = FXCollections.observableArrayList(service.getAllNota());
        comboNNotaFilter.setItems(note);

        ObservableList<Integer> ids = FXCollections.observableArrayList(service.getAllIdStudenta());
        comboNIdSFilter.setItems(ids);

        ObservableList<Integer> idt = FXCollections.observableArrayList(service.getAllIdTema());
        comboNIdTFilter.setItems(idt);
    }

    public void filterByNota(){
        try {
            if (comboNNotaFilter.getValue() == null){
                checkNota.setSelected(false);
                throw new Exception("Nu este selectata nici o nota");
            }
            int nota = (int) comboNNotaFilter.getValue();
            List<Nota> notas = service.filtreazaNoteDupaNota(nota);
            if (notas.size() == 0)
                throw new Exception("Nu exista date pentru filtrarea dorita");
            notaObservableList = FXCollections.observableList(notas);
            noteTableFilters.setItems(notaObservableList);
            checkNota.setSelected(false);

        } catch (Exception e) {
            showInformation(Alert.AlertType.WARNING, "Eroare", e.getMessage());
        }
    }

    public void filterByIdS(){
        try {
            if (comboNIdSFilter.getValue() == null){
                checkIdS.setSelected(false);
                throw new Exception("Nu este selectata nici un id");
            }
            int id = (int) comboNIdSFilter.getValue();
            List<Nota> notas = service.filtreazaNoteDupaStudent(id);
            if (notas.size() == 0)
                throw new Exception("Nu exista date pentru filtrarea dorita");
            notaObservableList = FXCollections.observableList(notas);
            noteTableFilters.setItems(notaObservableList);
            checkIdS.setSelected(false);

        } catch (Exception e) {
            showInformation(Alert.AlertType.WARNING, "Eroare", e.getMessage());
        }
    }

    public void filterByIdT(){
        try {
            if (comboNIdTFilter.getValue() == null){
                checkIdT.setSelected(false);
                throw new Exception("Nu este selectata nici un id");
            }
            int id = (int) comboNIdTFilter.getValue();
            List<Nota> notas = service.filtreazaNoteDupaNrTema(id);
            if (notas.size() == 0)
                throw new Exception("Nu exista date pentru filtrarea dorita");
            notaObservableList = FXCollections.observableList(notas);
            noteTableFilters.setItems(notaObservableList);
            checkIdT.setSelected(false);

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
