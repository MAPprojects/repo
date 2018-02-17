package com.company.gui;

import com.company.service.Service;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SettingsServiceController {
    public TextField sapF;
    public Button btnSetare;
    public Button btnCancel;
    public Service service;
    public Stage stage;

    public SettingsServiceController() {
    }

    @FXML
    private void initialize(){
    }

    public void setService(Service s, Stage stage) {
        this.service = s;
        this.stage = stage;
    }

    public void setareSaptamana(){
        try {
            int sap = Integer.parseInt(sapF.getText().trim());
            if (sap<1 || sap>14)
                throw new Exception("Saptamana trebuie sa fie intre 1-14");
            service.setSaptamana(sap);
            showInformation(Alert.AlertType.INFORMATION, "Setat", "Saptamana a fost setata");
            cancelSaptamana();
        }
        catch (NumberFormatException ex){
            showInformation(Alert.AlertType.WARNING, "Eroare", "Saptamana trebuie sa fie intreg");
        } catch (Exception e) {
            showInformation(Alert.AlertType.WARNING, "Eroare", e.getMessage());
        }
    }

    public void cancelSaptamana(){
        stage.close();
    }


    private void showInformation(Alert.AlertType type, String titlu, String mesaj){
        Alert alert = new Alert(type);
        alert.setHeaderText(titlu);
        alert.setContentText(mesaj);
        alert.showAndWait();
    }
}
