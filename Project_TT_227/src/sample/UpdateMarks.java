package sample;

import Domain.Nota;
import Exceptions.RepositoryException;
import Service.Service;
import Utils.NotaEvent;
import Utils.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;

import java.util.Optional;
import java.util.Vector;

public class UpdateMarks {

    public Label label2;
    public TextField txtID;
    public TextField txtNota;
    public Button btnUpdate;
    public Button btnClear;
    private Service service;
    private ObservableList<Nota> marks;

    public void setService(Service service)
    {
        this.service = service;
        this.marks = FXCollections.observableArrayList();
        reloadMarks();
    }

    @FXML
    private void initialize() {
        label2.setText("All good so far. ");
    }

    public ObservableList<Nota> reloadMarks() {
        Iterable<Nota> it = service.findAllMarks();
        Vector<Nota> allMarks = new Vector<>();
        for(Nota nota : it)
            allMarks.add(nota);
        marks.clear();
        allMarks.forEach(candidat -> marks.add(candidat));
        return marks;
    }

    @FXML
    private void updateMark() {
        String id = txtID.getText();
        String nota = txtNota.getText();
        boolean sendEmail;

        ButtonType btnYes = new ButtonType("Da",ButtonBar.ButtonData.YES);
        ButtonType btnNo = new ButtonType("Nu",ButtonBar.ButtonData.NO);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Doriti sa trimiteti un e-mail studentului pentru a-l instiinta de modificarile facute?",btnYes,btnNo);
        alert.setHeaderText("");
        alert.setTitle("");

        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == btnYes)
            sendEmail = true;
        else sendEmail = false;
        System.out.println(sendEmail);
        try {
            Integer idNota = Integer.valueOf(id);
            Integer vnota = Integer.valueOf(nota);
            service.modificareNota(idNota,vnota,sendEmail);
            label2.setText("Nota a fost actualizata! ");
            label2.setTextFill(Color.CYAN);
            clearFields();
        } catch (NumberFormatException e) {
            label2.setText("ID/Nota - Integers ");
            label2.setTextFill(Color.RED);
            clearFields();
            return;
        } catch (RepositoryException e) {
            label2.setText(e.getMessage() + " ");
            label2.setTextFill(Color.RED);
            clearFields();
            return;
        }
    }

    @FXML
    private void clearFields() {
        if(!txtID.isDisabled())
            txtID.setText("");
        txtNota.setText("");
    }
}
