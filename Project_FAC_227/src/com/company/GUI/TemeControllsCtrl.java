package com.company.GUI;

import com.company.Domain.Globals;
import com.company.Domain.Tema;
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

public class TemeControllsCtrl {
    public TextField idField;
    public TextField descField;
    public TextField deadlineField;
    public Service service;
    public Button addBtn;
    public Button updBtn;
    public Button delBtn;

    public void initialize()
    {
        if(Globals.getInstance().accessLevel==1){
        addBtn.setDisable(true);
        updBtn.setDisable(true);
        delBtn.setDisable(true);}

        stylise();

    }

    public void stylise()
    {
        addBtn.getParent().getParent().getStylesheets().add(getClass().getResource(Globals.getInstance().theme).toExternalForm());
        addBtn.getParent().getParent().getStyleClass().add("pane");
    }

    public void fillSelected(Tema tema) {
        if(tema != null) {
            idField.setText("" + tema.getID());
            descField.setText(tema.getDescriere());
            deadlineField.setText("" + tema.getDeadline());
        }
    }

    public void autoComplete(KeyEvent keyEvent) {
        try {
            Boolean gasit = false;
            if (idField.getText().length() > 0) {
                Integer id = Integer.parseInt(idField.getText());
                for (Tema t : service.getTeme()) {
                    if (t.getID() == id) {
                        descField.setText(t.getDescriere());
                        deadlineField.setText("" + t.getDeadline());
                        gasit = true;
                        break;
                    }
                }
            }
            if (!gasit) {
                descField.clear();
                deadlineField.clear();
            }
        }
        catch (NumberFormatException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setHeaderText("La campurile Id si Deadline treubie introduse valori numerice!");
            alert.showAndWait();
            idField.clear();
        }
    }

    public void add(MouseEvent mouseEvent) {
        FXMLLoader loaderAddWindow = new FXMLLoader(getClass().getResource("AddTemaWindow.fxml"));
        try {
            Parent root = loaderAddWindow.load();
            loaderAddWindow.<AddTemaWindowCtrl>getController().service=service;
            Stage addWindow = new Stage();
            addWindow.setScene(new Scene(root));
            addWindow.show();
            clearAll();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update(MouseEvent mouseEvent) {
        if(descField.getText().length() >0) {
            FXMLLoader loaderUpdateWindow = new FXMLLoader(getClass().getResource("UpdateTemaWindow.fxml"));
            try {
                Parent root = loaderUpdateWindow.load();
                loaderUpdateWindow.<UpdateTemaWindowCtrl>getController().initialize(idField.getText(), descField.getText(), deadlineField.getText());
                loaderUpdateWindow.<UpdateTemaWindowCtrl>getController().service = service;
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

        if(descField.getText().length() >0) {
            try {
                service.deleteTema(Integer.parseInt(idField.getText()));
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
        idField.clear();
        deadlineField.clear();
        descField.clear();
    }
}
