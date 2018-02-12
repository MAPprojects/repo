package com.company.GUI;

import com.company.Domain.Globals;
import com.company.Exceptions.ServiceException;
import com.company.Service.Service;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class UpdateTemaWindowCtrl {
    public TextField idField;
    public TextField descField;
    public TextField deadlineField;
    public Service service;

    public void initialize()
    {
        stylise();
    }

    public void stylise()
    {
        idField.getParent().getParent().getStylesheets().add(getClass().getResource(Globals.getInstance().theme).toExternalForm());
        idField.getParent().getParent().getStyleClass().add("anchor-pane");
    }

    public void update(MouseEvent mouseEvent) {
        try {
            service.modificareTermen(Integer.parseInt(idField.getText()),Integer.parseInt(deadlineField.getText()));
        } catch (ServiceException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setHeaderText(e.getMessage());
            alert.showAndWait();
        }
        catch (NumberFormatException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setHeaderText("La campurile Id si Deadline treubie introduse valori numerice!");
            alert.showAndWait();
        }
        finally {
            Stage stage = (Stage) idField.getScene().getWindow();
            stage.close();
        }
    }

    public void initialize(String id, String nume, String grupa) {
        idField.setText(id);
        descField.setText(nume);
        deadlineField.setText(grupa);
    }
}
