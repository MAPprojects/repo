package com.company.GUI;

import com.company.Domain.Globals;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class ConfigCtrl {

    @FXML
    public Button saveChangesBtn;
    @FXML
    public TextField saptField;

    public void initialize()
    {
        saptField.setText("" + Globals.getInstance().getSaptCurenta());
        stylise();
    }

    public void stylise()
    {
        saptField.getParent().getStylesheets().add(getClass().getResource(Globals.getInstance().theme).toExternalForm());
    }

    public void saveChanges(MouseEvent mouseEvent) {
        String sapt = saptField.getText();
        if(Integer.parseInt(sapt)>0 && Integer.parseInt(sapt)<=14) {
            Globals.setSaptCurenta(Integer.parseInt(sapt));
            //System.out.println(Globals.getInstance().getSaptCurenta());
            Stage stage = (Stage) saveChangesBtn.getScene().getWindow();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Successful!");
            alert.setHeaderText("Saptamana curenta:" + sapt);
            alert.showAndWait();
            stage.close();
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setHeaderText("Saptamana invalida!");
            alert.showAndWait();
        }
    }

}
