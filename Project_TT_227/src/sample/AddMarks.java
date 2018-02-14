package sample;

import Domain.Nota;
import Exceptions.RepositoryException;
import Service.Service;
import Utils.NotaEvent;
import Utils.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import java.util.Vector;

public class AddMarks {

    public Label label2;
    public TextField txtIDStudent;
    public TextField txtNrTema;
    public TextField txtNota;
    public Button btnAdd;
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
    private void addMark() {

        String idStudent = txtIDStudent.getText();
        String nrTema = txtNrTema.getText();
        String nota = txtNota.getText();

        try {
            Integer id = Integer.valueOf(idStudent);
            Integer tema = Integer.valueOf(nrTema);
            Integer vnota = Integer.valueOf(nota);
            service.adaugaNota(id,tema,vnota);
            label2.setText("Nota a fost adaugata! ");
            label2.setTextFill(Color.CYAN);
            clearFields();
        } catch (NumberFormatException e) {
            label2.setText("All fields - Integers ");
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
        txtIDStudent.setText("");
        txtNrTema.setText("");
        txtNota.setText("");
    }
}
