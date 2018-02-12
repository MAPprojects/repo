package com.company.GUI;

import com.company.Domain.Globals;
import com.company.Exceptions.ServiceException;
import com.company.Service.Service;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.lang.reflect.Method;

public class UpdateWindowCtrl {
    public TextField idField;
    public TextField profField;
    public TextField emailField;
    public TextField grupaField;
    public TextField nameField;
    public Service service;
    public Button updBtn;
    FXMLLoader parentLoader;

    public void initialize(String id, String nume, String grupa, String email, String prof)
    {
        idField.setText(id);
        nameField.setText(nume);
        grupaField.setText(grupa);
        emailField.setText(email);
        profField.setText(prof);
        stylise();
        //this.parentLoader = parentLoader;
    }

    public void stylise()
    {
        idField.getParent().getParent().getStylesheets().add(getClass().getResource(Globals.getInstance().theme).toExternalForm());
        updBtn.getParent().getParent().getStyleClass().add("anchor-pane");

    }

    public void updateStudent(MouseEvent mouseEvent) {
        try {
            service.updateStudent(Integer.parseInt(idField.getText()),nameField.getText(),Integer.parseInt(grupaField.getText()),emailField.getText(),profField.getText());
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
}
