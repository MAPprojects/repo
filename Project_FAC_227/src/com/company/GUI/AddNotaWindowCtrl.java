package com.company.GUI;

import com.company.Domain.Globals;
import com.company.Exceptions.ServiceException;
import com.company.Service.Service;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class AddNotaWindowCtrl {
    public TextField studentField;
    public TextField temaField;
    public TextField notaField;
    public Service service;

    public void initialize()
    {
        stylise();
    }

    public void stylise()
    {
        studentField.getParent().getParent().getStylesheets().add(getClass().getResource(Globals.getInstance().theme).toExternalForm());
        studentField.getParent().getParent().getStyleClass().add("pane");
    }

    public void add(MouseEvent mouseEvent) {
        try {
            service.adaugaNota(Integer.parseInt(studentField.getText()),Integer.parseInt(temaField.getText()),Integer.parseInt(notaField.getText()));
        } catch (ServiceException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setHeaderText(e.getMessage());
            alert.showAndWait();
        }
        catch (NumberFormatException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setHeaderText("La campurile Id si Nota treubie introduse valori numerice!");
            alert.showAndWait();
        }
        finally {
            Stage stage = (Stage) studentField.getScene().getWindow();
            stage.close();
        }
    }
}
