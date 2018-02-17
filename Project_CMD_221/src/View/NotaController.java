package View;

import Controllers.ControllerException;
import Controllers.ServiceObservable;
import Entities.Nota;
import Entities.Student;
import Entities.Teme;
import Entities.User;
import Repository.RepositoryException;
import Validators.ValidatorException;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.stage.Stage;
import javafx.util.Pair;
import utils.Event;
import utils.NotaEvent;
import utils.Observer;
import utils.TemeEvent;

import java.awt.event.ActionListener;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import java.io.FileOutputStream;

import static sun.plugin.javascript.navig.JSType.Document;

public class NotaController implements Observer<Event> {
    private ServiceObservable so;
    private ObservableList<Nota> model_n = FXCollections.observableArrayList();
    private ObservableList<User> model_u = FXCollections.observableArrayList();
    @FXML private TableView<Nota> tableNote;
    @FXML private TextField idsText, numeText, nrtText, valText;
    @FXML private ComboBox<String> comboBoxNote = new ComboBox<String>();

    public NotaController() {}

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
        numeText.setPromptText("NumeStudent");
        nrtText.setPromptText("NrTema");
        valText.setPromptText("Nota");
        comboBoxNote.setItems(FXCollections.observableArrayList("Filtru dupa note", "Filtru dupa student", "Filtru dupa tema"));
        tableNote.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> showStudenti(newValue));
    }

    @FXML
    private void showStudenti(Nota nota) {
        if(nota == null)
            clearFields();
        else {
            idsText.setText("" + nota.getID().getKey());
            numeText.setText("" + nota.getNume());
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

    LocalDate startingDate = LocalDate.of(2017, Month.OCTOBER, 3 );
    LocalDate endDate = LocalDate.now();
    private long saptCurent = ChronoUnit.WEEKS.between(startingDate, endDate) - 1;

    @FXML
    private void addButton() {
        try {
            Integer ids = Integer.parseInt(idsText.getText());
            Student st = so.findStudent(ids);
            Integer nrt = Integer.parseInt(nrtText.getText());
            Integer val = Integer.parseInt(valText.getText());
            Pair<Integer,Integer> id = new Pair<>(ids,nrt);
            so.saveNota(id, st.getNume(), val, (int)saptCurent);
            clearFields();
        }
        catch (NumberFormatException e) {
            showErrorMessage("Nu ati introdus date in niciun camp");
        }
        catch(RepositoryException e) {
            showErrorMessage(e.getMessage());
        }
        catch(ControllerException e) {
            showErrorMessage(e.getMessage());
        }
        catch(ValidatorException e) {
            showErrorMessage(e.getMessage());
        }
    }

    @FXML
    private void updateButton() {
        Nota nota = tableNote.getSelectionModel().getSelectedItem();
        Pair<Integer,Integer> id;
        try {
            if(nota != null) {
                id = new Pair<>(Integer.parseInt(idsText.getText()), Integer.parseInt(nrtText.getText()));
                Student st = so.findStudent(id.getKey());
                so.updateNota(id, st.getNume(), Integer.parseInt(valText.getText()), (int) saptCurent);
                clearFields();
            }
            else
                showErrorMessage("Selectati o nota");
        }
        catch (NumberFormatException e) {
            showErrorMessage("Nu ati introdus date in niciun camp");
        }
        catch(RepositoryException e) {
            showErrorMessage(e.getMessage());
            return;
        }
        catch(ControllerException e) {
            showErrorMessage(e.getMessage());
            return;
        }
        catch(ValidatorException v) {
            showErrorMessage(v.getMessage());
            return;
        }
    }

    @FXML
    private void deleteButton() {
        Nota nota = tableNote.getSelectionModel().getSelectedItem();
        Pair<Integer, Integer> id;
        try {
            if (nota != null) {
                id = new Pair<>(Integer.parseInt(idsText.getText()), Integer.parseInt(nrtText.getText()));
                so.deleteNota(id);
            }
            else
                showErrorMessage("Alegeti o nota");
        }
        catch(RepositoryException e) {
            showErrorMessage(e.getMessage());
            return;
        }
        catch(ValidatorException v) {
            showErrorMessage(v.getMessage());
            return;
        }
    }

    Stage appStage;

    @FXML
    void logout(MouseEvent event) throws IOException {
        if(setUser().isOk1() == 0) {
            so.update_user(setUser().getID(), 0, 0);
            Parent blah = FXMLLoader.load(getClass().getResource("/View/RootUser.fxml"));
            Scene scene = new Scene(blah, 950, 500);
            appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            appStage.setScene(scene);
            appStage.show();
        }
        else {
            so.update_user(setUser().getID(), 1, 1);
            Parent blah = FXMLLoader.load(getClass().getResource("/View/RootUser.fxml"));
            Scene scene = new Scene(blah, 950, 500);
            appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            appStage.setScene(scene);
            appStage.show();
        }
    }

    void showErrorMessage(String text) {
        Alert message = new Alert(Alert.AlertType.ERROR);
        message.setTitle("Eroare");
        message.setContentText(text);
        message.showAndWait();
    }

    @FXML
    public void handleFilter() {
        try {
            comboBoxNote.setValue("Filtru dupa note");
            List<Nota> note = so.filterByValoare(Integer.parseInt(valText.getText()));
            if (note.size() == 0)
                handleUnsuccessfulFilter();
            else
                handleSuccessfulFilter(note);
        }
        catch(NumberFormatException e) {
        showErrorMessage("Nu ati introdus date");
    }
}

    @FXML
    public void handleFilter2() {
        try {
            comboBoxNote.setValue("Filtru dupa student");
            List<Nota> grades = so.filterByStudent(Integer.parseInt(idsText.getText()));
            if (grades.size() == 0)
                handleUnsuccessfulFilter();
            else
                handleSuccessfulFilter(grades);
        }
        catch(NumberFormatException e) {
            showErrorMessage("Nu ati introdus date");
        }
    }

    @FXML
    public void handleFilter3() {
        try {
            comboBoxNote.setValue("Filtru dupa tema");
            List<Nota> grade = so.filterByNrHomework(Integer.parseInt(nrtText.getText()));
            if (grade.size() == 0)
                handleUnsuccessfulFilter();
            else
                handleSuccessfulFilter(grade);
        }
        catch(NumberFormatException e) {
            showErrorMessage("Nu ati introdus date");
        }
    }

    private void handleSuccessfulFilter(List<Nota> grades) {
        model_n.clear();
        grades.forEach(x->model_n.add(x));
        tableNote.setItems(model_n);
    }

    private void handleUnsuccessfulFilter() {
        Alert message = new Alert(Alert.AlertType.INFORMATION);
        message.setTitle("Informatie");
        message.setContentText("Nu exista nicio data corespunzatoare");
        message.showAndWait();
    }

    @FXML
    public void createPDFMedii() throws Exception {
        try {
            String dest = "D:/Faculta_Mark/Anul 2/MAP/LaboratorStudenti - Copy/LaboratorStudentsMedii.pdf";
            Document doc = new Document();
            PdfWriter.getInstance(doc, new FileOutputStream(dest));
            doc.open();

            doc.addTitle("Rapoarte");

            doc.add(new Paragraph("Media studentilor"));
            Iterable<Student> studenti = so.getAllStudents();
            List<Student> studentList = so.fromIterableToList(studenti);
            if(studentList.size() == 0)
                return;
            studentList.forEach(x-> {
                try {
                    String paragraf = x.getID() + " " + x.getNume() + " " + x.getMedie();
                    doc.add(new Paragraph(paragraf));
                }
                catch(DocumentException e) {
                    e.printStackTrace();
                }
            });
            doc.close();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        Alert message = new Alert(Alert.AlertType.INFORMATION);
        message.setTitle("Informatie");
        message.setContentText("Raport efectuat cu succes");
        message.showAndWait();
    }

    public void notifyEvent(Event nevent) {
        model_n.clear();
        so.getAllNote().forEach(x->model_n.add(x));
    }

    @Override
    public void notifyOnEvent(Event e) {
        notifyEvent(e);
    }
}