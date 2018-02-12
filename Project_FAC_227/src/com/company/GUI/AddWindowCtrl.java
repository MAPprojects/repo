package com.company.GUI;

import com.company.Domain.Globals;
import com.company.Exceptions.ServiceException;
import com.company.Service.Service;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class AddWindowCtrl {
    public Service service;
    public TextField idField;
    public TextField nameField;
    public TextField grupaField;
    public TextField emailField;
    public TextField profField;

    public void initialize()
    {
        stylise();
    }

    public void stylise()
    {
        idField.getParent().getParent().getStylesheets().add(getClass().getResource(Globals.getInstance().theme).toExternalForm());
        idField.getParent().getParent().getStyleClass().add("pane");
    }

    public void addStudent(MouseEvent mouseEvent) {
        try {
            service.addStudent(Integer.parseInt(idField.getText()),nameField.getText(),Integer.parseInt(grupaField.getText()),emailField.getText(),profField.getText());
        } catch (ServiceException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setHeaderText(e.getMessage());
            alert.showAndWait();
        } catch (NumberFormatException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setHeaderText("La campurile Id si Grupa treubie introduse valori numerice!");
            alert.showAndWait();
        }
        finally {
            Stage stage = (Stage) idField.getScene().getWindow();
            stage.close();
        }
    }
}
