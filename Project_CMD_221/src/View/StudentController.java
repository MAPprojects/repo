package View;

import Controllers.ServiceObservable;
import Entities.Student;
import Entities.Teme;
import Entities.User;
import Repository.RepositoryException;
import Validators.ValidatorException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.sun.javafx.scene.control.skin.TableHeaderRow;
import com.sun.javafx.scene.control.skin.TableViewSkinBase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.control.Pagination;
import javafx.scene.Node;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Pair;
import utils.Event;
import utils.Observer;
import utils.StudentEvent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

public class StudentController implements Observer<Event> {
    private ServiceObservable so;
    private ObservableList<Student> model_s = FXCollections.observableArrayList();
    private ObservableList<User> model_u = FXCollections.observableArrayList();
    @FXML private TableView<Student> tableStudenti;
    @FXML private TextField idText, nameText, emailText, groupText, profText, primaLitera;
    @FXML private CheckBox checkBoxGrupa;
    @FXML private CheckBox checkBoxProf;
    @FXML private CheckBox checkBoxPrimaLitera;

    public StudentController() {}

    public void setSo(ServiceObservable so) {
        this.so = so;
        /*so.getAllStudents().forEach(x->model_s.add(x));
        tableStudenti.setItems(model_s);*/
        setTableViewItemsFancy();
    }

    public void initialize() {
        idText.setPromptText("idStudent");
        nameText.setPromptText("Nume");
        emailText.setPromptText("Email");
        groupText.setPromptText("Grupa");
        profText.setPromptText("Profesor");
        tableStudenti.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> showStudenti(newValue));
    }

    private void setTableViewItems(Iterator<Student> i) {
        ObservableList<Student> s = FXCollections.observableArrayList();
        if(i.hasNext())
            i.forEachRemaining((x)->s.add(x));
        tableStudenti.setItems(s);
    }

    private void setTableViewItemsFancy() {
        setTableViewItems(so.findAllStudenti().iterator());
    }

    @FXML
    private void scroll(ScrollEvent e) {
        if(e.getDeltaY() < 0) {
            so.fetchForwardS();
            setTableViewItemsFancy();
        }
        if(e.getDeltaY() > 0) {
            so.fetchBackwardS();
            setTableViewItemsFancy();
        }
    }

    @FXML
    private void showStudenti(Student st) {
        if(st == null)
            clearFields();
        else {
            idText.setText("" + st.getID());
            nameText.setText(st.getNume());
            emailText.setText(st.getEmail());
            groupText.setText(st.getGrupa());
            profText.setText(st.getProf());
        }
    }

    @FXML
    private void clearFields() {
        idText.setText("");
        nameText.setText("");
        emailText.setText("");
        groupText.setText("");
        profText.setText("");
    }

    @FXML
    private void image() {
        checkBoxGrupa.setSelected(true);
    }

    @FXML
    private void image2() {
        checkBoxPrimaLitera.setSelected(true);
    }

    @FXML
    private void addButton() {
        try {
            Integer id = Integer.parseInt(idText.getText());
            String name = nameText.getText();
            String email = emailText.getText();
            String group = groupText.getText();
            String prof = profText.getText();
            so.save_student(id, name, email, group, prof, 0, 0, 0.0, true, true);
            clearFields();
        }
        catch (NumberFormatException e) {
            showErrorMessage("Nu ati introdus date in niciun camp");
        }
        catch(RepositoryException e) {
            showErrorMessage(e.getMessage());
        }
        catch(ValidatorException e) {
            showErrorMessage(e.getMessage());
        }
    }

    @FXML
    private void updateButton() {
        Student s = tableStudenti.getSelectionModel().getSelectedItem();
        try {
            if(s != null) {
                int note = so.findStudent(tableStudenti.getSelectionModel().getSelectedItem().getID()).getNote();
                int nr = so.findStudent(tableStudenti.getSelectionModel().getSelectedItem().getID()).getNr();
                double medie = so.findStudent(tableStudenti.getSelectionModel().getSelectedItem().getID()).getMedie();
                boolean val = so.findStudent(tableStudenti.getSelectionModel().getSelectedItem().getID()).isVal();
                boolean fint = so.findStudent(tableStudenti.getSelectionModel().getSelectedItem().getID()).isFint();
                so.update_student(Integer.parseInt(idText.getText()), nameText.getText(), emailText.getText(), groupText.getText(), profText.getText(), note, nr, medie, val, fint);
                clearFields();
            }
            else
                showErrorMessage("Alegeti un student");
        }
        catch (NumberFormatException e) {
            showErrorMessage("Nu ati introdus date in niciun camp");
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

    @FXML
    private void deleteButton() {
        Student s = tableStudenti.getSelectionModel().getSelectedItem();
        try {
            if (s != null) {
                so.delete_student(Integer.parseInt(idText.getText()));
                for(Teme tema: so.getAllTeme()) {
                    Pair<Integer, Integer> id = new Pair<>(s.getIDStudent(), tema.getID());
                    if(so.findNota(id) != null)
                        so.deleteNota(id);
                }
            }
            else {
                showErrorMessage("Alegeti un student");
            }
            clearFields();
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

    public void notifyEvent(Event stevent) {
        model_s.clear();
        so.getAllStudents().forEach(x->model_s.add(x));
    }

    @FXML
    public void handleFilter1() {
        Iterable<Student> studenti = so.getAllStudents();
        List<Student> s = so.fromIterableToList(studenti);
        ObservableList<Student> sss = FXCollections.observableArrayList(s);
        if(checkBoxGrupa.isSelected()) {
            if(groupText.getText().isEmpty())
                showErrorMessage("Nu ati introdus nimic in campul grupa");
            sss = FXCollections.observableArrayList(so.filterStudentByGroup(sss, groupText.getText()));
            if(s.size() == 0)
                handleUnsuccessfulFilter();
            else
                handleSuccessfulFilter(sss);
        }
        if(checkBoxProf.isSelected()) {
            if(profText.getText().isEmpty())
                showErrorMessage("Nu ati introdus nimic in campul profesor");
            sss = FXCollections.observableArrayList(so.filterStudentByTeacher(sss, profText.getText()));
            if(s.size() == 0)
                handleUnsuccessfulFilter();
            else
                handleSuccessfulFilter(sss);
        }
        if(checkBoxPrimaLitera.isSelected()) {
            if(nameText.getText().isEmpty())
                showErrorMessage("Nu ati introdus nimic in campul Nume");
            sss = FXCollections.observableArrayList(so.filterByFirstLetter(sss, nameText.getText()));
            if(s.size() == 0)
                handleUnsuccessfulFilter();
            else
                handleSuccessfulFilter(sss);
        }
    }

    private void handleSuccessfulFilter(List<Student> s) {
        model_s.clear();
        s.forEach(x->model_s.add(x));
        tableStudenti.setItems(model_s);
    }

    private void handleUnsuccessfulFilter() {
        Alert message = new Alert(Alert.AlertType.INFORMATION);
        message.setTitle("Informatie");
        message.setContentText("Nu exista nicio data corespunzatoare");
        message.showAndWait();
    }

    Stage primaryStage, appStage;

    @FXML
    private MediaPlayer playMusic() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        File file = fileChooser.showOpenDialog(primaryStage);
        String path;
        if(file == null)
            return null;
        else
            path = file.getAbsolutePath();
        path.replace("\\", "/");
        Media media = new Media(new File(path).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setAutoPlay(true);
        return mediaPlayer;
    }

    @FXML
    public void createPDFExamen() throws Exception {
        try {
            String dest = "D:/Faculta_Mark/Anul 2/MAP/LaboratorStudenti - Copy/LaboratorStudentsExamen.pdf";
            Document doc = new Document();
            PdfWriter.getInstance(doc, new FileOutputStream(dest));
            doc.open();
            doc.addTitle("Rapoarte");
            doc.add(new Paragraph("Studentii care pot intra in examen"));
            Iterable<Student> studenti = so.getAllStudents();
            List<Student> studentList = so.fromIterableToList(studenti);
            List<Student> studentListNote = new ArrayList<>();
            Comparator<Student> compare2 = (Student st, Student st2) -> {
                return Boolean.compare(st2.isVal(), st.isVal());
            };
            for(Student st: studentList)
                if(st.getNr() != 0)
                    studentListNote.add(st);
            if(studentListNote.size() == 0)
                return;
            Collections.sort(studentListNote, compare2);
            int i = 1;
            boolean b = studentListNote.get(0).isVal();
            while(!(i + 1 > studentListNote.size()) && b == studentListNote.get(i).isVal()) {
                String par = "Studentul cu id-ul " + studentListNote.get(i).getID() + " si numele " + studentListNote.get(i).getNume() + " poate intra in examen";
                doc.add(new Paragraph(par));
                i++;
            }
            String par = "Studentul cu id-ul " + studentListNote.get(0).getID() + " si numele " + studentListNote.get(0).getNume() + " poate intra in examen";
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

    @FXML
    public void createPDFPredari() throws Exception {
        try {
            String dest = "D:/Faculta_Mark/Anul 2/MAP/LaboratorStudenti - Copy/LaboratorStudentsPredari.pdf";
            Document doc = new Document();
            PdfWriter.getInstance(doc, new FileOutputStream(dest));
            doc.open();

            doc.addTitle("Rapoarte");

            doc.add(new Paragraph("Studentii care au predat la timp toate temele"));
            Iterable<Student> studenti = so.getAllStudents();
            List<Student> studentList = so.fromIterableToList(studenti);
            List<Student> studentListNote = new ArrayList<>();
            Comparator<Student> compare3 = (Student st, Student st2) -> {
                return Boolean.compare(st2.isFint(), st.isFint());
            };
            for(Student st: studentList)
                if(st.getNr() != 0)
                    studentListNote.add(st);
            if(studentListNote.size() == 0)
                return;
            Collections.sort(studentListNote, compare3);
            int j = 1;
            boolean bool = studentListNote.get(0).isFint();
            while(!(j + 1 > studentListNote.size()) && bool == studentListNote.get(j).isFint()) {
                String paragraph = "Studentul cu id-ul " + studentListNote.get(j).getID() + " si numele " + studentListNote.get(j).getNume() + " a predat la timp toate temele";
                doc.add(new Paragraph(paragraph));
                j++;
            }
            String paragraph = "Studentul cu id-ul " + studentListNote.get(0).getID() + " si numele " + studentListNote.get(0).getNume() + " a predat la timp toate temele";
            doc.add(new Paragraph(paragraph));

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

    @Override
    public void notifyOnEvent(Event e) {
        notifyEvent(e);
    }
}
