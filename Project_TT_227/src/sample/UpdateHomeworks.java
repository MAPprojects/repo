package sample;

import Domain.Tema;
import Exceptions.RepositoryException;
import Service.Service;
import Utils.Observer;
import Utils.TemaEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import java.util.Vector;

public class UpdateHomeworks {

    public Label label2;
    public TextField txtID;
    public TextField txtDeadline;
    public Button btnUpdate;
    public Button btnClear;
    private Service service;
    private ObservableList<Tema> homeworks;

    public void setService(Service service)
    {
        this.service = service;
        this.homeworks = FXCollections.observableArrayList();
        reloadHomeworks();
    }

    @FXML
    private void initialize() {
        label2.setText("All good so far. ");
    }

    public ObservableList<Tema> reloadHomeworks() {
        Iterable<Tema> it = service.findAllHomeworks();
        Vector<Tema> allHomeworks = new Vector<>();
        for(Tema tema : it)
            allHomeworks.add(tema);
        homeworks.clear();
        allHomeworks.forEach(candidat -> homeworks.add(candidat));
        return homeworks;
    }

    @FXML
    private void updateHomework() {
        String id = txtID.getText();
        String deadline = txtDeadline.getText();

        try {
            Integer nrTema = Integer.valueOf(id);
            Integer deadln = Integer.valueOf(deadline);
            service.modificareTermen(nrTema,deadln,false);
            label2.setText("Termenul a fost actualizat! ");
            label2.setTextFill(Color.CYAN);
            clearFields();
        } catch (NumberFormatException e) {
            label2.setText("Numar/Deadline - Integers ");
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
        txtDeadline.setText("");
    }
}
