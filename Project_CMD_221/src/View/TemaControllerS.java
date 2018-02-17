package View;

import Controllers.ControllerException;
import Controllers.ServiceObservable;
import Entities.Student;
import Entities.Teme;
import Entities.User;
import Repository.RepositoryException;
import Validators.ValidatorException;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.stage.Stage;
import javafx.util.Pair;
import utils.Event;
import utils.Observer;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class TemaControllerS implements Observer<Event> {
    private ServiceObservable so;
    private ObservableList<Teme> model_t = FXCollections.observableArrayList();
    private ObservableList<User> model_u = FXCollections.observableArrayList();
    @FXML
    private TableView<Teme> tableTeme;
    @FXML private TextField idText, deadlineText, descText;

    public TemaControllerS() {}

    public void setSo(ServiceObservable so) {
        this.so = so;
        /*so.getAllTeme().forEach(x->model_t.add(x));
        tableTeme.setItems(model_t);*/
        setTableViewItemsFancy();
    }

    public void initialize() {
        idText.setPromptText("NrTema");
        deadlineText.setPromptText("Deadline");
        descText.setPromptText("Descriere");
        tableTeme.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> showStudenti(newValue));
    }

    @FXML
    private void showStudenti(Teme tema) {
        if(tema == null)
            clearFields();
        else {
            idText.setText("" + tema.getID());
            deadlineText.setText("" + tema.getDeadline());
            descText.setText(tema.getDescriere());
        }
    }

    @FXML
    private void clearFields() {
        idText.setText("");
        deadlineText.setText("");
        descText.setText("");
    }

    private void setTableViewItems(Iterator<Teme> i) {
        ObservableList<Teme> s = FXCollections.observableArrayList();
        if(i.hasNext())
            i.forEachRemaining((x)->s.add(x));
        tableTeme.setItems(s);
    }

    private void setTableViewItemsFancy() {
        setTableViewItems(so.findAllTeme().iterator());
    }

    @FXML
    private void scroll(ScrollEvent e) {
        if(e.getDeltaY() < 0) {
            so.fetchForwardT();
            setTableViewItemsFancy();
        }
        if(e.getDeltaY() > 0) {
            so.fetchBackwardT();
            setTableViewItemsFancy();
        }
    }

    private Stage appStage;

    public User setUser() {
        so.getAllUsers().forEach(x->model_u.add(x));
        for(User user: model_u)
            if(user.isOk2() == 2)
                return user;
        return null;
    }

    @FXML
    void logout(MouseEvent event) throws IOException {
        so.update_user(setUser().getID(), 1, 1);
        Parent blah = FXMLLoader.load(getClass().getResource("/View/RootUser.fxml"));
        Scene scene = new Scene(blah, 950, 500);
        appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        appStage.setScene(scene);
        appStage.show();
    }

    public void notifyEvent(Event tevent) {
        model_t.clear();
        so.getAllTeme().forEach(x->model_t.add(x));
    }

    @Override
    public void notifyOnEvent(Event e) {
        notifyEvent(e);
    }
}

