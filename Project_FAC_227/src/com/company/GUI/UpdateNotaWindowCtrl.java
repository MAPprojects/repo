package com.company.GUI;

import com.company.Domain.Globals;
import com.company.Exceptions.ServiceException;
import com.company.Service.Service;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class UpdateNotaWindowCtrl {
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
        studentField.getParent().getParent().getStyleClass().add("anchor-pane");
    }

    public void update(MouseEvent mouseEvent) {
        try {
            service.modificareNota(Integer.parseInt(studentField.getText()),Integer.parseInt(temaField.getText()),Integer.parseInt(notaField.getText()));
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
            Stage stage = (Stage) studentField.getScene().getWindow();
            stage.close();
        }
    }

    public void initialize(String st, String tema, String nota) {
        studentField.setText(st);
        temaField.setText(tema);
        notaField.setText(nota);
    }
}
