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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.stage.Stage;
import javafx.util.Pair;
import utils.Event;
import utils.Observer;
import utils.TemeEvent;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class TemaController implements Observer<Event> {
    private ServiceObservable so;
    private ObservableList<Teme> model_t = FXCollections.observableArrayList();
    private ObservableList<User> model_u = FXCollections.observableArrayList();
    @FXML private TableView<Teme> tableTeme;
    @FXML private TextField idText, deadlineText, descText;
    @FXML private RadioButton radioButtonFilter1, radioButtonFilter2, radioButtonFilter3;

    public TemaController() {}

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

    @FXML
    private void addButton() {
        try {
            Integer id = Integer.parseInt(idText.getText());
            Integer deadline = Integer.parseInt(deadlineText.getText());
            String desc = descText.getText();
            so.save_tema(id, deadline, desc, 0);
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
    private void updateDeadlineButton() {
        int index = tableTeme.getSelectionModel().getFocusedIndex();
        try {
            so.update_Deadline(Integer.parseInt(idText.getText()), Integer.parseInt(deadlineText.getText()));
            tableTeme.getSelectionModel().select(index);
            clearFields();
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
        Teme tema = tableTeme.getSelectionModel().getSelectedItem();
        try {
            if (tema != null) {
                so.delete_tema(Integer.parseInt(idText.getText()));
                for(Student st: so.getAllStudents()) {
                    Pair<Integer, Integer> id = new Pair<>(st.getIDStudent(), tema.getID());
                    if(so.findNota(id) != null)
                        so.deleteNota(id);
                }
            }
            else {
                showErrorMessage("Alegeti o tema");
            }
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

    void showErrorMessage(String text) {
        Alert message = new Alert(Alert.AlertType.ERROR);
        message.setTitle("Eroare");
        message.setContentText(text);
        message.showAndWait();
    }

    @FXML
    public void handleFilter1() {
        try {
            radioButtonFilter1.setSelected(true);
            List<Teme> teme = so.filterByDeadline(Integer.parseInt(deadlineText.getText()));
            if (teme.size() == 0)
                handleUnsuccessfulFilter();
            else
                handleSuccessfulFilter(teme);
        }
        catch(NumberFormatException e) {
            showErrorMessage("Nu ati introdus nimic in campul Deadline");
        }
    }

    @FXML
    public void handleFilter2() {
        try {
            radioButtonFilter2.setSelected(true);
            List<Teme> teme = so.filterByLowerDeadline(Integer.parseInt(deadlineText.getText()));
            if (teme.size() == 0)
                handleUnsuccessfulFilter();
            else
                handleSuccessfulFilter(teme);
        }
        catch(NumberFormatException e) {
            showErrorMessage("Nu ati introdus nimic in campul Deadline");
        }
    }

    @FXML
    public void handleFilter3() {
        try {
            radioButtonFilter3.setSelected(true);
            List<Teme> teme = so.filterByHigherDeadline(Integer.parseInt(deadlineText.getText()));
            if (teme.size() == 0)
                handleUnsuccessfulFilter();
            else
                handleSuccessfulFilter(teme);
        }
        catch(NumberFormatException e) {
            showErrorMessage("Nu ati introdus nimic in campul Deadline");
        }
    }

    private void handleSuccessfulFilter(List<Teme> teme) {
        model_t.clear();
        teme.forEach(x->model_t.add(x));
        tableTeme.setItems(model_t);
    }

    private void handleUnsuccessfulFilter() {
        Alert message = new Alert(Alert.AlertType.INFORMATION);
        message.setTitle("Informatie");
        message.setContentText("Nu exista nicio data corespunzatoare");
        message.showAndWait();
    }

    private Stage appStage;

    @FXML
    public void createPDFTeme() throws Exception {
        try {
            String dest = "D:/Faculta_Mark/Anul 2/MAP/LaboratorStudenti - Copy/LaboratorStudentsTeme.pdf";
            Document doc = new Document();
            PdfWriter.getInstance(doc, new FileOutputStream(dest));
            doc.open();
            doc.addTitle("Rapoarte");
            doc.add(new Paragraph("Tema cea mai grea"));
            Iterable<Teme> teme = so.getAllTeme();
            List<Teme> temeList = so.fromIterableToList(teme);
            if(temeList.size() == 0)
                return;
            Comparator<Teme> compare = (Teme tema, Teme tema2) -> {
                boolean b = tema.getDif() > tema2.getDif();
                return b ? -1 : 1;
            };
            Collections.sort(temeList, compare);
            int dif = temeList.get(0).getDif();
            int next = 1;
            while(!(next + 1 > temeList.size()) && dif == temeList.get(next).getDif()) {
                String message = "Tema cea mai grea este cea cu id-ul " + temeList.get(next).getIDTema() + " si are " + temeList.get(next).getDif() + " intarzieri.";
                doc.add(new Paragraph(message));
                next++;
            }
            String par = "Tema cea mai grea este cea cu id-ul " + temeList.get(0).getIDTema() + " si are " + dif + " intarzieri.";
            doc.add(new Paragraph(par));

            doc.close();
            Alert message = new Alert(Alert.AlertType.INFORMATION);
            message.setTitle("Informatie");
            message.setContentText("Raport efectuat cu succes");
            message.showAndWait();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public User setUser() {
        so.getAllUsers().forEach(x->model_u.add(x));
        for(User user: model_u)
            if(user.isOk2() == 2)
                return user;
        return null;
    }

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

    public void notifyEvent(Event tevent) {
        model_t.clear();
        so.getAllTeme().forEach(x->model_t.add(x));
    }

    @Override
    public void notifyOnEvent(Event e) {
        notifyEvent(e);
    }
}
