package controller;

import entities.Student;
import exceptions.AbstractValidatorException;
import exceptions.StudentServiceException;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import services.StudentService;

import java.io.IOException;

public class UpdateStudentController {

    private static final String MESAJ_EROARE_NUME_VID = "Numele nu poate fi vid";
    private static final String MESAJ_EROARE_GRUPA_VIDA = "Grupa nu poate fi vida";
    private static final String MESAJ_EROARE_PROF_VID = "Numele profesorului nu poate fi vid";
    private Stage stage;
    private Student studentToBeUpdated;
    private StudentService service;
    private String oldStudentsId;
    private AnchorPane studentRootLayout;
    @FXML
    private Text mesajEroareNume;
    @FXML
    private Text mesajEroareGrupa;
    @FXML
    private Text mesajEroareEmail;
    @FXML
    private Text mesajEroareProf;
    @FXML
    private TextField studentNumeField;
    @FXML
    private TextField studentGrupaField;
    @FXML
    private TextField studentEmailField;
    @FXML
    private TextField studentProfField;

    public void setService(StudentService service) {
        this.service = service;
    }

    public void setOldStudentsId(String oldStudentsId) {

        this.oldStudentsId = oldStudentsId;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setStudentToBeUpdated(Student studentToBeUpdated) {
        this.studentToBeUpdated = studentToBeUpdated;
    }

    public void setStudentRootLayout(AnchorPane studentRootLayout) {
        this.studentRootLayout = studentRootLayout;
    }

    @FXML
    public void initialize() {
    }

    public void initializeFIeldsWithOldValues() {
        studentEmailField.setText(studentToBeUpdated.getEmail());
        studentGrupaField.setText(studentToBeUpdated.getGrupa());
        studentNumeField.setText(studentToBeUpdated.getNume());
        studentProfField.setText(studentToBeUpdated.getCadruDidacticIndrumator());
        studentEmailField.setDisable(true);
    }

    @FXML
    private void updateStudent(MouseEvent mouseEvent) throws IOException, AbstractValidatorException, StudentServiceException {
        clearWarningForInputs();
        if (validateInfo()) {
            String nume = studentNumeField.getText();
            String grupa = studentGrupaField.getText();
            String email = studentEmailField.getText();
            String prof = studentProfField.getText();
            service.updateStudent(oldStudentsId, nume, grupa, email, prof);
            studentRootLayout.getChildren().forEach(node -> {
                node.setDisable(false);
                node.setEffect(null);
            });
            stage.close();
        }
    }

    private boolean validateInfo() {
        boolean infoValide = true;
        String nume = studentNumeField.getText();
        String grupa = studentGrupaField.getText();
        String email = studentEmailField.getText();
        String prof = studentProfField.getText();
        if (nume.equals("") || nume.equals(MESAJ_EROARE_NUME_VID)) {
//            mesajEroareNume.setText("Numele nu poate fi vid.");
            studentNumeField.setText(MESAJ_EROARE_NUME_VID);
            studentNumeField.setStyle("-fx-text-inner-color: red; -fx-font-size: 14;");
            infoValide = false;
        }
        if (grupa.equals("") || grupa.equals(MESAJ_EROARE_GRUPA_VIDA)) {
//            mesajEroareGrupa.setText("Grupa nu poate fi vida.");
            studentGrupaField.setText(MESAJ_EROARE_GRUPA_VIDA);
            studentGrupaField.setStyle("-fx-text-inner-color: red; -fx-font-size: 14;");
            infoValide = false;
        }
        if (prof.equals("") || prof.equals(MESAJ_EROARE_PROF_VID)) {
//            mesajEroareProf.setText("Numele profesorului nu poate fi vid.");
            studentProfField.setText(MESAJ_EROARE_PROF_VID);
            studentProfField.setStyle("-fx-text-inner-color: red; -fx-font-size: 14;");
            infoValide = false;
        }
        return infoValide;
    }

    @FXML
    private void exitDialogUpdateStudentHandler(MouseEvent mouseEvent) {
        stage.close();
        studentRootLayout.getChildren().forEach(node -> {
            node.setDisable(false);
            node.setEffect(null);
        });
    }

    private void clearWarningForInputs() {
        mesajEroareNume.setText("");
        mesajEroareGrupa.setText("");
        mesajEroareEmail.setText("");
        mesajEroareProf.setText("");
    }

    @FXML
    private void deleteErrorMessageOnClickNume(MouseEvent mouseEvent) {
        if (studentNumeField.getText().equals(MESAJ_EROARE_NUME_VID)) {
            studentNumeField.setText("");
            studentNumeField.setStyle("-fx-text-fill: black; -fx-font-size: 18;");
        }
    }

    @FXML
    private void deleteErrorMessageOnClickGrupa(MouseEvent mouseEvent) {
        if (studentGrupaField.getText().equals(MESAJ_EROARE_GRUPA_VIDA)) {
            studentGrupaField.setText("");
            studentGrupaField.setStyle("-fx-text-fill: black; -fx-font-size: 18");
        }
    }

    @FXML
    private void deleteErrorMessageOnClickProf(MouseEvent mouseEvent) {
        if (studentProfField.getText().equals(MESAJ_EROARE_PROF_VID)) {
            studentProfField.setText("");
            studentProfField.setStyle("-fx-text-fill: black; -fx-font-size: 18;");
        }
    }

    @FXML
    private void changeFontOnKeyPressedNume(KeyEvent keyEvent) {
        studentNumeField.setStyle("-fx-text-fill: black; -fx-font-size: 18;");
    }

    @FXML
    private void changeFontOnKeyPressedGrupa(KeyEvent keyEvent) {
        studentGrupaField.setStyle("-fx-text-fill: black; -fx-font-size: 18");
    }

    @FXML
    private void changeFontOnKeyPressedProf(KeyEvent keyEvent) {
        studentProfField.setStyle("-fx-text-fill: black; -fx-font-size: 18;");
    }
}
