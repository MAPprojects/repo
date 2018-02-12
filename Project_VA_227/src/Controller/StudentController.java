package Controller;

import Domain.Student;
import Domain.Tip;
import Domain.User;
import Service.StudentService;
import Utils.Message;
import Utils.Utils;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class StudentController extends AbstractController<Integer, Student> {

    @FXML
    public TextField nameText, emailText, grupaText, profesorText;
    @FXML
    public TableColumn<Object, Object> idColumn;
    @FXML
    public TableColumn<Object, Object> nameColumn;
    @FXML
    public TableColumn<Object, Object> emailColumn;
    @FXML
    public TableColumn<Object, Object> profesorColumn;
    @FXML
    public TableColumn<Object, Object> grupaColumn;

    public StudentController() {
        super();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.model = FXCollections.observableArrayList();
        this.table.setItems(model);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldvalue, newValue) -> showDetails(newValue)
        );

        idColumn.setCellValueFactory(new PropertyValueFactory<>("Id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("nume"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        grupaColumn.setCellValueFactory(new PropertyValueFactory<>("grupa"));
        profesorColumn.setCellValueFactory(new PropertyValueFactory<>("profesor"));
    }

    private Integer selectedId = null;

    @Override
    protected void showDetails(Student newValue) {
        if (newValue == null) {
            clearDetails();
            return;
        }
        selectedId = newValue.getId();
        nameText.setText(newValue.getNume());
        grupaText.setText(newValue.getGrupa().toString());
        profesorText.setText(newValue.getProfesor());
        emailText.setText(newValue.getEmail());
    }

    private void clearDetails() {
        nameText.clear();
        grupaText.clear();
        profesorText.clear();
        emailText.clear();
        selectedId = null;
    }

    @FXML
    public void handleAction() {
        if (actionsChoose.getValue() == null) {
            showError("Select an action");
            return;
        }
        if ((actionsChoose.getValue().equals("Remove") || actionsChoose.getValue().equals("Update")) &&
                selectedId == null)
        {
            showError("Select a student");
            return;
        }
        StudentService studentService = (StudentService) this.service;
        switch (actionsChoose.getValue()) {
            case "Add":
                try {
                    this.add(new Student(-1, nameText.getText(),
                            Integer.parseInt(grupaText.getText()), emailText.getText(), profesorText.getText()));
                    clearDetails();
                } catch (Exception exception) {
                    showError(exception.getMessage());
                }
                break;
            case "Remove":
                try {
                    this.delete(selectedId);
                    clearDetails();
                } catch (Exception exception) {
                    showError(exception.getMessage());
                }
                break;
            case "Update":
                try {
                    this.update(new Student(selectedId, nameText.getText(),
                            Integer.parseInt(grupaText.getText()), emailText.getText(), profesorText.getText()));
                    clearDetails();
                } catch (Exception exception) {
                    showError(exception.getMessage());
                }
                break;
            case "Get All":
                this.populateList(true);
                break;
            case "Filter":
                try {
                    this.populateList(studentService.filter(Utils.NullOrString(nameText.getText()),
                            Utils.tryParseInt(grupaText.getText()), Utils.NullOrString(profesorText.getText())));
                } catch (Exception exception) {
                    showError(exception.getMessage());
                }
                break;
        }
    }
}
