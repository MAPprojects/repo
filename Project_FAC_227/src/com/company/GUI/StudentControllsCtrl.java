package com.company.GUI;

import com.company.Domain.Globals;
import com.company.Domain.Student;
import com.company.Exceptions.ServiceException;
import com.company.Service.Service;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class StudentControllsCtrl {
    public Service service;
    public TextField nameField;
    public TextField grupaField;
    public TextField emailField;
    public TextField profField;
    public TextField idField;
    public Button addBtn;
    public Button updBtn;
    public Button delBtn;

    public void initialize()
    {
        if(Globals.getInstance().accessLevel==1)
        {
            addBtn.setDisable(true);
            updBtn.setDisable(true);
            delBtn.setDisable(true);
        }

        stylise();
    }

    public void stylise()
    {
        addBtn.getParent().getParent().getStylesheets().add(getClass().getResource(Globals.getInstance().theme).toExternalForm());
        addBtn.getParent().getParent().getStyleClass().add("pane");
    }

    public void search()
    {
        Integer id = Integer.parseInt(idField.getText());
        for (Student st:service.getStudents())
        {
            if(st.getID()==id) {
                nameField.setText(st.getNume());
                grupaField.setText("" + st.getGrupa());
                emailField.setText(st.getEmail());
                profField.setText(st.getProfLab());
                break;
            }
        }
    }

    public void autoComplete(KeyEvent keyEvent) {
        try {
            Boolean gasit = false;
            if (idField.getText().length() > 0) {
                Integer id = Integer.parseInt(idField.getText());
                for (Student st : service.getStudents()) {
                    if (st.getID() == id) {
                        nameField.setText(st.getNume());
                        grupaField.setText("" + st.getGrupa());
                        emailField.setText(st.getEmail());
                        profField.setText(st.getProfLab());
                        gasit = true;
                        break;
                    }
                }
            }
            if (!gasit) {
                nameField.clear();
                grupaField.clear();
                emailField.clear();
                profField.clear();
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

    public void fillSelected(Student st) {
        if(st != null){
            idField.setText("" + st.getID());
            nameField.setText(st.getNume());
            grupaField.setText("" + st.getGrupa());
            emailField.setText(st.getEmail());
            profField.setText(st.getProfLab());
        }
    }

    public void add()
    {
        FXMLLoader loaderAddWindow = new FXMLLoader(getClass().getResource("AddWindow.fxml"));
        try {
            Parent root = loaderAddWindow.load();
            loaderAddWindow.<AddWindowCtrl>getController().service=service;
            Stage addWindow = new Stage();
            addWindow.setScene(new Scene(root));
            addWindow.show();
            clearAll();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update()
    {
        if(nameField.getText().length() >0) {
            FXMLLoader loaderUpdateWindow = new FXMLLoader(getClass().getResource("UpdateWindow.fxml"));
            try {
                Parent root = loaderUpdateWindow.load();
                loaderUpdateWindow.<UpdateWindowCtrl>getController().initialize(idField.getText(), nameField.getText(), grupaField.getText(), emailField.getText(), profField.getText());
                loaderUpdateWindow.<UpdateWindowCtrl>getController().service = service;
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

    public void delete()
    {
        if(nameField.getText().length() >0) {
            try {
                service.deleteStudent(Integer.parseInt(idField.getText()));
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
        nameField.clear();
        grupaField.clear();
        emailField.clear();
        profField.clear();
    }
}
