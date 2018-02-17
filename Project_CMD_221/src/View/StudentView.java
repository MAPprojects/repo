package View;

import Controller.StudentController;
import Entities.Student;
import Repository.RepositoryException;
import Validators.ValidatorException;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class StudentView {
    TextField StudentID, nume, email, grupa, prof;
    TableView<Student> table;
    StudentController ctrl;

    public StudentView(StudentController ctrl) {
        super();
        this.ctrl = ctrl;
        table = new TableView<>();
    }

    public BorderPane getView() {
        BorderPane pane = new BorderPane();
        pane.setRight(createStudent());
        pane.setCenter(createTable());
        return pane;
    }

    protected StackPane createTable() {
        StackPane pane = new StackPane();
        initStudentView();
        pane.getChildren().add(table);
        return pane;
    }

    private void initStudentView() {
        TableColumn<Student, String> idCol = new TableColumn<>("ID Student");
        TableColumn<Student, String> numeCol = new TableColumn<>("Nume");
        TableColumn<Student, String> emailCol = new TableColumn<>("Email");
        TableColumn<Student, String> grupaCol = new TableColumn<>("Grupa");
        TableColumn<Student, String> profCol = new TableColumn<>("Profesor");

        table.getColumns().addAll(idCol, numeCol, emailCol, grupaCol, profCol);

        idCol.setCellValueFactory(new PropertyValueFactory<Student, String>("iDStudent"));
        idCol.setStyle("-fx-alignment: CENTER;");
        numeCol.setCellValueFactory(new PropertyValueFactory<Student, String>("nume"));
        numeCol.setStyle("-fx-alignment: CENTER;");
        emailCol.setCellValueFactory(new PropertyValueFactory<Student, String>("email"));
        emailCol.setStyle("-fx-alignment: CENTER;");
        grupaCol.setCellValueFactory(new PropertyValueFactory<Student, String>("grupa"));
        grupaCol.setStyle("-fx-alignment: CENTER;");
        profCol.setCellValueFactory(new PropertyValueFactory<Student, String>("prof"));
        profCol.setStyle("-fx-alignment: CENTER;");

        table.setItems(ctrl.getStudentModel());

        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        table.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> showStudentDetails(newValue));
    }

    private void showStudentDetails(Student st) {
        if(st == null)
            clearFields();
        else {
            StudentID.setText(String.valueOf(st.getID()));
            StudentID.setEditable(false);
            nume.setText(st.getNume());
            email.setText(st.getEmail());
            grupa.setText(st.getGrupa());
            prof.setText(st.getProf());
        }
    }

    protected GridPane createStudent() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(15, 15, 15, 15));

        Label id_lbl = new Label("ID");
        grid.add(id_lbl, 0, 1);
        StudentID = new TextField();
        grid.add(StudentID, 1, 1);
        Label nume_lbl = new Label("Nume");
        grid.add(nume_lbl, 0, 2);
        nume = new TextField();
        grid.add(nume, 1, 2);
        Label email_lbl = new Label("Email");
        grid.add(email_lbl, 0, 3);
        email = new TextField();
        grid.add(email, 1, 3);
        Label grupa_lbl = new Label("Grupa");
        grid.add(grupa_lbl, 0, 4);
        grupa = new TextField();
        grid.add(grupa, 1, 4);
        Label prof_lbl = new Label("Profesor");
        grid.add(prof_lbl, 0, 5);
        prof = new TextField();
        grid.add(prof, 1, 5);

        Button addStudent = new Button("Adauga");
        Button updateStudent = new Button("Modifica");
        Button deleteStudent = new Button("Sterge");
        Button clear = new Button("Clear");

        HBox hbtn = new HBox(10);
        hbtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbtn.getChildren().add(addStudent);
        addStudent.setOnAction(x->addButton());
        hbtn.getChildren().add(updateStudent);
        updateStudent.setOnAction(x->updateButton());
        hbtn.getChildren().add(deleteStudent);
        deleteStudent.setOnAction(x->deleteButton());
        hbtn.getChildren().add(clear);
        clear.setOnAction(x->clearButton());

        grid.add(hbtn, 0, 6, 2, 1);
        return grid;
    }

    private void addButton() {
        try {
            Integer id = Integer.parseInt(StudentID.getText());
            String name = nume.getText();
            String emaill = email.getText();
            String group = grupa.getText();
            String profesor = prof.getText();
            ctrl.addStudent(id, name, emaill, group, profesor, 0, 0, 0.0, true, true);
            showMessage(Alert.AlertType.CONFIRMATION, "Adaugare", "Ati adaugat un student cu succes");
        }
        catch(NumberFormatException ex) {
            showErrorMessage("ID-ul trebuie sa fie un numar intreg pozitiv.");
            return;
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

    private void updateButton() {
        int index = table.getSelectionModel().getFocusedIndex();
        try {
            ctrl.updateStudent(Integer.parseInt(StudentID.getText()), nume.getText(), email.getText(), grupa.getText(), prof.getText(), 0, 0, 0.0, true, true);
            table.getSelectionModel().select(index);
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

    private void deleteButton() {
        int index = table.getSelectionModel().getFocusedIndex();
        try {
            ctrl.deleteStudent(Integer.parseInt(StudentID.getText()));
            table.getSelectionModel().select(index == ctrl.getStudentModel().size() ? index - 1 : index);
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

    private void clearButton() {
        table.getSelectionModel().clearSelection();
        clearFields();
    }

    private void clearFields() {
        StudentID.setText("");
        nume.setText("");
        email.setText("");
        grupa.setText("");
        prof.setText("");
    }

    static void showMessage(Alert.AlertType type, String header, String text) {
        Alert message = new Alert(type);
        message.setHeaderText(header);
        message.setContentText(text);
        message.showAndWait();
    }

    static void showErrorMessage(String text) {
        Alert message = new Alert(Alert.AlertType.ERROR);
        message.setTitle("Eroare");
        message.setContentText(text);
        message.showAndWait();
    }
}
