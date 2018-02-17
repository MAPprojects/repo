package View;

import Controllers.ControllerException;
import Controllers.ServiceObservable;
import Entities.Nota;
import Entities.Student;
import Entities.User;
import Repository.RepositoryException;
import Validators.ValidatorException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.stage.Stage;
import javafx.util.Pair;
import utils.Event;
import utils.Observer;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.Iterator;
import java.util.List;

public class NotaControllerS implements Observer<Event> {
    private ServiceObservable so;
    private ObservableList<Nota> model_n = FXCollections.observableArrayList();
    private ObservableList<User> model_u = FXCollections.observableArrayList();
    @FXML
    private TableView<Nota> tableNote;
    @FXML private TextField idsText, nrtText, valText;

    public NotaControllerS() {}

    public User setUser() {
        so.getAllUsers().forEach(x->model_u.add(x));
        for(User user: model_u)
            if(user.isOk2() == 2)
                return user;
        return null;
    }

    public void setSo(ServiceObservable so) {
        this.so = so;
        //so.getAllNote().forEach(x->model_n.add(x));
        //tableNote.setItems(model_n);
        setTableViewItemsFancy();
    }

    public void initialize() {
        idsText.setPromptText("idStudent");
        nrtText.setPromptText("NrTema");
        valText.setPromptText("Nota");
        tableNote.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> showStudenti(newValue));
    }

    @FXML
    private void showStudenti(Nota nota) {
        if(nota == null)
            clearFields();
        else {
            idsText.setText("" + nota.getID().getKey());
            nrtText.setText("" + nota.getID().getValue());
            valText.setText("" + nota.getValoare());
        }
    }

    @FXML
    private void clearFields() {
        idsText.setText("");
        nrtText.setText("");
        valText.setText("");
    }

    private void setTableViewItems(Iterator<Nota> i) {
        ObservableList<Nota> s = FXCollections.observableArrayList();
        ObservableList<Nota> ss = FXCollections.observableArrayList();
        if(i.hasNext())
            i.forEachRemaining((x)->s.add(x));
        for(Nota nota: s) {
            Student st = so.findStudent(nota.getIDStudent());
            if(st.getEmail().compareTo(setUser().getUsername()) == 0) {
                ss.add(nota);
                tableNote.setItems(ss);
            }
        }
        if(ss.isEmpty() && setUser().isOk1() == 0)
            tableNote.setItems(s);
    }

    private void setTableViewItemsFancy() {
        setTableViewItems(so.findAllNote().iterator());
    }

    @FXML
    private void scroll(ScrollEvent e) {
        if(e.getDeltaY() < 0) {
            so.fetchForwardN();
            setTableViewItemsFancy();
        }
        if(e.getDeltaY() > 0) {
            so.fetchBackwardN();
            setTableViewItemsFancy();
        }
    }

    Stage appStage;

    @FXML
    void logout(MouseEvent event) throws IOException {
        so.update_user(setUser().getID(), 1, 1);
        Parent blah = FXMLLoader.load(getClass().getResource("/View/RootUser.fxml"));
        Scene scene = new Scene(blah, 950, 500);
        appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        appStage.setScene(scene);
        appStage.show();
    }


    public void notifyEvent(utils.Event nevent) {
        model_n.clear();
        so.getAllNote().forEach(x->model_n.add(x));
    }

    @Override
    public void notifyOnEvent(utils.Event e) {
        notifyEvent(e);
    }
}
