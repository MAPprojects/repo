package com.company.gui;

import com.company.domain.Student;
import com.company.domain.Tema;
import com.company.service.Service;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.util.List;

public class FiltersTemaController {
    public Service service;
    public TableColumn idCTFilter;
    public TableColumn cerintaCTFilter;
    public TableColumn deadlineCTFilter;
    public ChoiceBox choiceTFilter;
    public TextField cerintaFTFilter;
    public ToggleGroup filtrari;
    public Button btnFiltreazaT;
    public Button btnCloseT;
    public TableView temeTableFilter;
    public RadioButton dupaCerintaT;
    public RadioButton dupaDeadlineT;
    public RadioButton dupaCerintaDeadlineT;
    Stage stage;
    ObservableList<Tema> temaObservableList;
    public FiltersTemaController() {
    }

    @FXML
    private void initialize(){
        idCTFilter.setCellValueFactory(new PropertyValueFactory<Tema, Integer>("Id"));
        cerintaCTFilter.setCellValueFactory(new PropertyValueFactory<Tema, String>("cerinta"));
        deadlineCTFilter.setCellValueFactory(new PropertyValueFactory<Tema, Integer>("deadline"));
    }

    public void setService(Service s, Stage stage) {
        this.service = s;
        this.stage = stage;

        ObservableList<Tema> deadline = FXCollections.observableArrayList(service.getAllDeadline());
        choiceTFilter.setItems(deadline);
    }

    public void filterByDeadline(){
        try {
            if (choiceTFilter.getValue() == null)
                throw new Exception("Nu este selectat nici un deadline");
            int deadline = (int) choiceTFilter.getValue();
            List<Tema> teme = service.filtreazaTemaDupaDeadline(deadline);
            if (teme.size() == 0)
                throw new Exception("Nu exista date pentru filtrarea dorita");
            temaObservableList = FXCollections.observableList(teme);
            temeTableFilter.setItems(temaObservableList);

        } catch (Exception e) {
            showInformation(Alert.AlertType.WARNING, "Eroare", e.getMessage());
        }
    }

    public void filterByCerinta(){
        try {
            String cerinta = cerintaFTFilter.getText().trim();
            List<Tema> teme = service.filtreazaTemaDupaCerinta(cerinta);
            if (teme.size() == 0)
                throw new Exception("Nu exista date pentru filtrarea dorita");
            temaObservableList = FXCollections.observableList(teme);
            temeTableFilter.setItems(temaObservableList);

        } catch (Exception e) {
            showInformation(Alert.AlertType.WARNING, "Eroare", e.getMessage());
        }
    }

    public void filterByDeadlineCerinat(){
        try {
            if (choiceTFilter.getValue() == null)
                throw new Exception("Nu este selectat nici un deadline");
            String cerinta = cerintaFTFilter.getText().trim();
            int deadline = (int) choiceTFilter.getValue();
            List<Tema> teme = service.filtreazaTemaDupaCerintaSiDeadline(cerinta,deadline);
            if (teme.size() == 0)
                throw new Exception("Nu exista date pentru filtrarea dorita");
            temaObservableList = FXCollections.observableList(teme);
            temeTableFilter.setItems(temaObservableList);

        } catch (Exception e) {
            showInformation(Alert.AlertType.WARNING, "Eroare", e.getMessage());
        }
    }

    public void filter(){
        if (dupaDeadlineT.isSelected())
            filterByDeadline();
        else
            if (dupaCerintaT.isSelected())
                filterByCerinta();
        else
            filterByDeadlineCerinat();
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
