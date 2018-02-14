package sample;

import Domain.Student;
import Exceptions.RepositoryException;
import Service.Service;
import Utils.Observer;
import Utils.StudentEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import java.util.Vector;

public class UpdateStudents {

    public Label label2;
    public TextField txtID;
    public TextField txtName;
    public TextField txtGrupa;
    public TextField txtEmail;
    public TextField txtProf;
    public Button btnUpdate;
    public Button btnClear;
    private Service service;
    private ObservableList<Student> students;

    public void setService(Service service)
    {
        this.service = service;
        this.students = FXCollections.observableArrayList();
        reloadStudents();
    }

    @FXML
    private void initialize() {
        label2.setText("All good so far. ");
    }

    public ObservableList<Student> reloadStudents() {
        Iterable<Student> it = service.findAllStudents();
        Vector<Student> allStudents = new Vector<>();
        for(Student student : it)
            allStudents.add(student);
        students.clear();
        allStudents.forEach(candidat -> students.add(candidat));
        return students;
    }

    @FXML
    private void updateStudent() {
        String id = txtID.getText();
        String nume = txtName.getText();
        String grupa = txtGrupa.getText();
        String email = txtEmail.getText();
        String profLab = txtProf.getText();

        try {
            Integer idStudent = Integer.valueOf(id);
            Integer gr = Integer.valueOf(grupa);
            service.updateStudent(idStudent,nume,gr,email,profLab);
            label2.setText("Studentul a fost actualizat! ");
            label2.setTextFill(Color.CYAN);
            clearFields();
        } catch (NumberFormatException e) {
            label2.setText("ID/Grupa - Integers ");
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
        txtProf.setText("");
        txtEmail.setText("");
        txtGrupa.setText("");
        txtName.setText("");
        if(!txtID.isDisabled())
            txtID.setText("");
    }
}
