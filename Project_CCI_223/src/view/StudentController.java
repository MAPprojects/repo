package view;

import domain.Student;
import exceptii.EntityNotFoundException;
import exceptii.ValidationException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import service.Service;
import utils.ListEvent;
import utils.Observer;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


public class StudentController  implements Observer<Student> {
    private Service service;
    private ObservableList<Student> model;
    private StudentView view;

    /**
     * Getter for the model
     * @return ObservableList<Student></Student>
     */
    public ObservableList<Student> getModel() {
        return model;
    }

    /**
     * Setter for the view
     * @param view StudentView
     */
    public void setView(StudentView view) {
        this.view = view;
    }

    /**
     * Constructor
     * @param service Service
     */
    public StudentController(Service service) {
        this.service = service;
        model = FXCollections.observableArrayList(service.getListStudenti());
    }

    /**
     * Handles for the saveEvent
     * @param event ActionEvent
     */
    public void handleAddStudent(ActionEvent event) {
        Student student = createStudentFromFields();
        if (student!=null) {
            try {
                if (!service.addStudent(student).isPresent()) {
                    showMessage("Studentul a fost salvat cu succes!");
                } else showWarningMessage("Studentul nu a putut fi salvat (ID existent)");

            } catch (ValidationException e) {
                showErrorMessage(e.toString());
            }
        }
    }

    /**
     * Shows a new window with an error message and with the text given as a parameter
     * @param text String
     */
    private void showErrorMessage(String text) {
        Alert message = new Alert(Alert.AlertType.ERROR);
        message.setTitle("Eroare");
        message.setContentText(text);
        message.showAndWait();
    }

    /**
     * Shows an information box with the text given as a parameter
     * @param text String
     */
    private void showMessage(String text) {
        Alert message = new Alert(Alert.AlertType.INFORMATION);
        message.setTitle("Information");
        message.setContentText(text);
        message.showAndWait();
    }

    /**
     * Shows a warning message with the text given as a parameter
     * @param text String
     */
    private void showWarningMessage(String text) {
        Alert message = new Alert(Alert.AlertType.WARNING);
        message.setTitle("Warning");
        message.setContentText(text);
        message.showAndWait();
    }

    /**
     * Creates a student from textfields
     * @return Student
     */
    private Student createStudentFromFields() {
        Integer id, grupa;
        try {
            id = Integer.parseInt(view.fieldID.getText());
            grupa = Integer.parseInt(view.fieldGrupa.getText());
            Student st = new Student(id, view.fieldNume.getText(), grupa, view.fieldEmail.getText(), view.fieldCadruDidactic.getText());
            return st;
        } catch (NumberFormatException e) {
            showErrorMessage("Idul si grupa studentului trebuie sa fie un numar intreg!!!");
            return null;
        }

    }

    /**
     * Handles the updateEvent for student
     * @param event ActionEvent
     */
    public void handleUpdateStudent(ActionEvent event){
        try {

            Student st = createStudentFromFields();
            if (st!=null){
                service.updateStudent(st.getId(), st.getNume(), st.getEmail(), st.getCadru_didactic_indrumator_de_laborator(), st.getGrupa());
            }
            handleClearAll(event);
        } catch (EntityNotFoundException e) {
            showErrorMessage("Entitatea cu idul dorit nu exista in repository");
        } catch (ValidationException e) {
            showErrorMessage(e.toString());
        }
    }

    /**
     * Handles the deleteEvent for student
     * @param event ActionEvent
     */
    public void handleDeleteStudent(ActionEvent event){
        try {
            Integer idStudent=Integer.parseInt(view.fieldID.getText());
            if (service.deleteStudent(idStudent).isPresent()){
                showMessage("Studentul a fost sters!");
                handleClearAll(event);
            }
            else showWarningMessage("Studentul nu a putut fi sters (are note asignate)");
        } catch (EntityNotFoundException e) {
            showErrorMessage(e.toString());
        }catch (NumberFormatException e){
            showErrorMessage("Trebuie selectat un student din tabel");
        }
    }

    /**
     * Handles the clearAllButton Event
     * @param event ActionEvent
     */
    public void handleClearAll(ActionEvent event) {
        view.fieldID.clear();
        view.fieldNume.clear();
        view.fieldGrupa.clear();
        view.fieldEmail.clear();
        view.fieldCadruDidactic.clear();
        view.fieldID.setDisable(false);

    }

    /**
     * Notify the events from ListEvent<Student>></>
     * @param events ListEvent<Student> </E>
     */
    @Override
    public void notifyEvents(ListEvent<Student> events) {
        model.setAll(StreamSupport.stream(events.getList().spliterator(), false).collect(Collectors.toList()));
    }

    /**
     * Completes the textFields with the details of a student selected from the tableView
     * @param newStudent Student
     */
    public void showStudentDetails(Student newStudent) {
        if (newStudent != null) {
            view.fieldID.setText(newStudent.getId().toString());
            view.fieldID.setDisable(true);
            view.fieldNume.setText(newStudent.getNume());
            view.fieldGrupa.setText(new Integer(newStudent.getGrupa()).toString());
            view.fieldEmail.setText(newStudent.getEmail());
            view.fieldCadruDidactic.setText(newStudent.getCadru_didactic_indrumator_de_laborator());
        }
    }
}
