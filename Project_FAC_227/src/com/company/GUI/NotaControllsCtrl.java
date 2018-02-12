package com.company.GUI;

import com.company.Domain.ExtendedNota;
import com.company.Domain.Globals;
import com.company.Domain.Nota;
import com.company.Exceptions.ServiceException;
import com.company.Service.Service;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class NotaControllsCtrl {

    public TextField studentField;
    public TextField temaField;
    public TextField notaField;
    public Service service;
    public Button addBtn;

    public void initialize()
    {
        stylise();

    }

    public void stylise()
    {
        addBtn.getParent().getParent().getStylesheets().add(getClass().getResource(Globals.getInstance().theme).toExternalForm());
        addBtn.getParent().getParent().getStyleClass().add("pane");
    }

    public void fillSelected(ExtendedNota extendedNota) {
        if(extendedNota != null) {
            studentField.setText("" + extendedNota.getNota().getIdStudent());
            temaField.setText("" + extendedNota.getNota().getNrTema());
            notaField.setText("" + extendedNota.getNota().getNota());
        }
    }

    public void autoComplete(KeyEvent keyEvent) {
        try {
            Boolean gasit = false;
            if (studentField.getText().length() > 0 && temaField.getText().length() > 0) {
                Integer id1 = Integer.parseInt(studentField.getText());
                Integer id2 = Integer.parseInt(temaField.getText());
                for (Nota n : service.getNote()) {
                    if (n.getIdStudent() == id1 && n.getNrTema() == id2) {
                        notaField.setText("" + n.getNota());
                        gasit = true;
                        break;
                    }
                }
            }
            if (!gasit) {
                notaField.clear();
            }
        }
        catch (NumberFormatException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setHeaderText("La campurile Id si Deadline treubie introduse valori numerice!");
            alert.showAndWait();
            studentField.clear();
            temaField.clear();
        }
    }

    public void add(MouseEvent mouseEvent) {
        FXMLLoader loaderAddWindow = new FXMLLoader(getClass().getResource("AddNotaWindow.fxml"));
        try {
            Parent root = loaderAddWindow.load();
            loaderAddWindow.<AddNotaWindowCtrl>getController().service=service;
            Stage addWindow = new Stage();
            addWindow.setScene(new Scene(root));
            addWindow.show();
            clearAll();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update(MouseEvent mouseEvent) {
        if(notaField.getText().length() >0) {
            FXMLLoader loaderUpdateWindow = new FXMLLoader(getClass().getResource("UpdateNotaWindow.fxml"));
            try {
                Parent root = loaderUpdateWindow.load();
                loaderUpdateWindow.<UpdateNotaWindowCtrl>getController().initialize(studentField.getText(), temaField.getText(), notaField.getText());
                loaderUpdateWindow.<UpdateNotaWindowCtrl>getController().service = service;
                Stage updateWindow = new Stage();
                updateWindow.setScene(new Scene(root));
                updateWindow.show();
                clearAll();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setHeaderText("Nu exista selectie!");
            alert.showAndWait();
        }
    }

    public void delete(MouseEvent mouseEvent) {

        if(notaField.getText().length() >0) {
            try {
                service.deleteNota(Integer.parseInt(studentField.getText()),Integer.parseInt(temaField.getText()));
                clearAll();
            } catch (ServiceException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error!");
                alert.setHeaderText(e.getMessage());
                alert.showAndWait();
            }
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setHeaderText("Nu exista selectie!");
            alert.showAndWait();
        }

    }

    public void clearAll()
    {
        studentField.clear();
        temaField.clear();
        notaField.clear();
    }
}
