package controller;

import domain.Student;
import exceptii.EntityNotFoundException;
import exceptii.ValidationException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import service.Service;
import view_FXML.AlertMessage;

import java.util.Optional;

public class EditStudentController {
    @FXML
    private TextField textFieldNume;
    @FXML
    private TextField textFieldIdStudent;
    @FXML
    private TextField textFieldGrupa;
    @FXML
    private TextField textFieldEmail;
    @FXML
    private TextField textFieldProfesor;
    @FXML
    private Button buttonCancel;
    @FXML
    private Button buttonSave;

    private Service service;
    Student student;
    Stage dialogStage;
    Scene scene;

    public EditStudentController(){}

    @FXML
    public void initialize(){ }

    public void setService(Service service, Stage stage,Student student){
        this.service=service;
        this.dialogStage=stage;
        this.student=student;

        if (student!=null){
            setFields(student);
            textFieldIdStudent.setDisable(true);
        }
    }

    public void setScene(Scene scene) {
        this.scene=scene;

        scene.setOnKeyPressed(event -> {
            if (event.getCode()== KeyCode.S && event.isControlDown()){
                handleSave(new ActionEvent());
            }
        });
    }

    public void handleCancel(ActionEvent event){
        dialogStage.close();
    }

    public void handleSave(ActionEvent event) {
        try {
            Student stud = createStudentFromFields();

            if (student == null) {
                saveStudent(stud);
            } else {
                updateStudent(stud);
            }
        } catch (NumberFormatException e) {
            AlertMessage message = new AlertMessage();
            message.showMessage(dialogStage, Alert.AlertType.ERROR, "Eroare", "Ati introdus date invalide");
        }
    }

    private void updateStudent(Student stud) {
        try {
            service.updateStudent(stud.getId(),stud.getNume(),stud.getEmail(),stud.getCadru_didactic_indrumator_de_laborator(),stud.getGrupa());
            AlertMessage message=new AlertMessage();
            message.showMessage(dialogStage, Alert.AlertType.CONFIRMATION,"Modificare efectuata","Studentul a fost modificat cu succes");
            dialogStage.close();
        } catch (ValidationException e) {
            AlertMessage message=new AlertMessage();
            message.showMessage(dialogStage, Alert.AlertType.ERROR,"Eroare",e.toString());
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void saveStudent(Student stud) {
        try{
            Optional<Student> aux=service.addStudent(stud);
            if (!aux.isPresent()){
                AlertMessage message=new AlertMessage();
                message.showMessage(dialogStage, Alert.AlertType.CONFIRMATION,"Salvare efectuata","Studentul a fost salvat cu succes");
                dialogStage.close();
            }
            else{
                AlertMessage message=new AlertMessage();
                message.showMessage(dialogStage, Alert.AlertType.WARNING,"Warning","Id existent!");
            }

        } catch (ValidationException e) {
            AlertMessage message=new AlertMessage();
            message.showMessage(dialogStage, Alert.AlertType.ERROR,"Eroare",e.toString());
        }
    }

    private Student createStudentFromFields(){
        Student stud=new Student(Integer.parseInt(textFieldIdStudent.getText()),textFieldNume.getText(),Integer.parseInt(textFieldGrupa.getText()),textFieldEmail.getText(),textFieldProfesor.getText());
        return stud;
    }

    private void setFields(Student student) {
        textFieldIdStudent.setText(student.getId().toString());
        textFieldNume.setText(student.getNume());
        textFieldEmail.setText(student.getEmail());
        textFieldGrupa.setText(new Integer (student.getGrupa()).toString());
        textFieldProfesor.setText(student.getCadru_didactic_indrumator_de_laborator());
    }
}
