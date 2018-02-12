package com.company.GUI;

import com.company.Domain.Globals;
import com.company.Repositories.SQLStudentRepository;
import com.company.Service.Service;
import com.itextpdf.text.zugferd.checkers.comfort.GlobalIdentifierCode;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PasswordChangeCtrl {

    public PasswordField oldP;
    public PasswordField newP;
    public PasswordField confP;
    public Service service;
    public Label oldPass;

    public void initialize()
    {
        stylise();
        if(Globals.getInstance().accessLevel==2)
            oldPass.setText("Id student");
    }

    public void apply(ActionEvent actionEvent) {

        if(oldP.getText().equals("") || newP.getText().equals("") || confP.getText().equals(""))
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setHeaderText("Completarea campurilor este obligatorie!");
            alert.showAndWait();
        }
        else
        {
            if(Globals.getInstance().accessLevel==1) {
                if (oldP.getText().equals(service.getAccounts().get(Globals.getInstance().nonAdminID))) {
                    if (newP.getText().equals(confP.getText())) {
                        Connection connection = SQLStudentRepository.getConnection();
                        String querry = "UPDATE Studenti SET parola = '" + newP.getText() + "' WHERE idStudent = " + Globals.getInstance().nonAdminID;

                        try {
                            PreparedStatement ps = connection.prepareStatement(querry);
                            ps.executeUpdate();
                            service.setAccountPassword(Globals.getInstance().nonAdminID, String.valueOf(newP));
                            Stage stage = (Stage) newP.getScene().getWindow();
                            stage.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error!");
                        alert.setHeaderText("Parola noua nu este identica cu cea confirmata!");
                        alert.showAndWait();
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error!");
                    alert.setHeaderText("Parola veche este gresita!");
                    alert.showAndWait();
                }
            }
            else
            {
                if (newP.getText().equals(confP.getText())) {
                    Connection connection = SQLStudentRepository.getConnection();
                    String querry = "UPDATE Studenti SET parola = '" + newP.getText() + "' WHERE idStudent = " + Integer.parseInt(oldP.getText());
                    try {
                        PreparedStatement ps = connection.prepareStatement(querry);
                        ps.executeUpdate();
                        service.setAccountPassword(Integer.parseInt(oldP.getText()), String.valueOf(newP));
                        Stage stage = (Stage) newP.getScene().getWindow();
                        stage.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error!");
                    alert.setHeaderText("Parola noua nu este identica cu cea confirmata!");
                    alert.showAndWait();
                }
            }
        }
    }

    public void stylise()
    {
        oldP.getParent().getParent().getStylesheets().add(getClass().getResource(Globals.getInstance().theme).toExternalForm());
        oldP.getParent().getParent().getStyleClass().add("pane");
    }
}
