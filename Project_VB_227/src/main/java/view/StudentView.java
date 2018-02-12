package view;

import controller.StudentController;
import entities.Student;
import exceptions.AbstractValidatorException;
import exceptions.StudentServiceException;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.io.IOException;


public class StudentView extends BorderPane {

    private TableView<Student> tableView;
    private StudentController studentController;
    private GridPane gridPane;
    private TextField fieldId;
    private TextField fieldNume;
    private TextField fieldGrupa;
    private TextField fieldEmail;
    private TextField fieldProf;

    public StudentView(StudentController studentController) {
        super();
        this.studentController = studentController;
        initComponents();
    }

    private void initComponents() {
        initTableView();
        initGridPane();
        initButtons();
    }

    private void initButtons() {
        HBox hBox = new HBox();
        Button addButton = new Button("Add");
        Button removeButton = new Button("Remove");
        Button updateButton = new Button("Update");
        hBox.getChildren().addAll(addButton, removeButton, updateButton);
        gridPane.addRow(5, hBox);
        //functionalitate pentru butoane
        addButton.setOnAction(x -> addButton());
        removeButton.setOnAction(x -> removeButton());
        updateButton.setOnAction(x -> updateButton());
    }

    private void updateButton() {
        String id = fieldId.getText();
        String nume = fieldNume.getText();
        String email = fieldEmail.getText();
        String grupa = fieldGrupa.getText();
        String prof = fieldProf.getText();
        try {
            studentController.updateStudent(id, nume, grupa, email, prof);
        } catch (IOException e) {
            errorAlertMessage(e.getMessage());
        } catch (StudentServiceException e) {
            errorAlertMessage(e.getMessage());
        } catch (AbstractValidatorException e) {
            errorAlertMessage(e.getMessage());
        }

    }

    private void addButton() {
        try {
            String nume = fieldNume.getText();
            String email = fieldEmail.getText();
            String grupa = fieldGrupa.getText();
            String prof = fieldProf.getText();
            studentController.addStudent(nume, grupa, email, prof);
        } catch (IOException e) {
            errorAlertMessage(e.getMessage());
        } catch (AbstractValidatorException e) {
            errorAlertMessage(e.getMessage());
        } catch (StudentServiceException e) {
            errorAlertMessage(e.getMessage());
        } catch (NumberFormatException e) {
            errorAlertMessage(e.getMessage());
        }
    }

    private void removeButton() {
        Student student = tableView.getSelectionModel().getSelectedItem();
        try {
            if (student != null) {
                studentController.removeStudent(student);
            }
        } catch (IOException e) {
            errorAlertMessage(e.getMessage());
        } catch (AbstractValidatorException e) {
            errorAlertMessage(e.getMessage());
        } catch (StudentServiceException e) {
            errorAlertMessage(e.getMessage());
        }
    }

    private void initGridPane() {
        gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.add(new Label("Id student"), 0, 0);
        gridPane.add(new Label("Nume student"), 0, 1);
        gridPane.add(new Label("Grupa student"), 0, 2);
        gridPane.add(new Label("Email student"), 0, 3);
        gridPane.add(new Label("Profesor laborator"), 0, 4);
        fieldId = new TextField();
        fieldNume = new TextField();
        fieldGrupa = new TextField();
        fieldEmail = new TextField();
        fieldProf = new TextField();
        gridPane.add(fieldId, 1, 0);
        gridPane.add(fieldNume, 1, 1);
        gridPane.add(fieldGrupa, 1, 2);
        gridPane.add(fieldEmail, 1, 3);
        gridPane.add(fieldProf, 1, 4);
        super.setRight(gridPane);
    }

    private void initTableView() {
        tableView = new TableView<>();
        TableColumn<Student, Integer> coloanaId = new TableColumn<>("Id");
        TableColumn<Student, String> coloanaNume = new TableColumn<>("Nume");
        TableColumn<Student, String> coloanaGrupa = new TableColumn<>("Grupa");
        TableColumn<Student, String> coloanaEmail = new TableColumn<>("Email");
        TableColumn<Student, String> coloanaProf = new TableColumn<>("Profesor laborator");
        //coloanaId.setCellValueFactory(new PropertyValueFactory<>("idStudent"));
        coloanaId.setCellValueFactory(new PropertyValueFactory<Student, Integer>("id"));
        coloanaNume.setCellValueFactory(new PropertyValueFactory<>("nume"));
        coloanaGrupa.setCellValueFactory(new PropertyValueFactory<>("grupa"));
        coloanaEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        coloanaProf.setCellValueFactory(new PropertyValueFactory<>("cadruDidacticIndrumator"));
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.getColumns().addAll(coloanaId, coloanaNume, coloanaGrupa, coloanaEmail, coloanaProf);
        tableView.setItems(studentController.getStudentModel());
        //modificam text field-urile sa ne afiseze detaliile despre fiecare obiect selectat
        tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                fieldId.setText(newValue.getId().toString());
                fieldNume.setText(newValue.getNume());
                fieldGrupa.setText(newValue.getGrupa());
                fieldEmail.setText(newValue.getEmail());
                fieldProf.setText(newValue.getCadruDidacticIndrumator());
            }
        });
        super.setCenter(tableView);
    }

    private void errorAlertMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("You really drop the ball mate!");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
