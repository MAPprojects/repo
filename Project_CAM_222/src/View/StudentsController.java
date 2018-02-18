package View;

import Domain.Student;
import Domain.ValidationException;
import Repository.RepositoryException;
import Service.Service;
import Utils.HyperlinkObserver;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;

import java.util.Objects;
import java.util.function.Predicate;

public class StudentsController implements HyperlinkObserver {
    private Service service;
    private int rowsPerPage = 9;
    private Predicate<Student> filter = (x -> true);
    private String accountName;
    private String accountRole;
    private String accountPicturePath;
    private RootController rootController;

    @FXML
    private TableView<Student> studentsTableView;
    @FXML
    private Pagination studentsPagination;
    @FXML
    private TextField idTextField;
    @FXML
    private TextField numeTextField;
    @FXML
    private TextField grupaTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    private TextField profIndrumatorTextField;
    @FXML
    private CheckBox idCheckBox;
    @FXML
    private CheckBox numeCheckBox;
    @FXML
    private CheckBox grupaCheckBox;
    @FXML
    private CheckBox emailCheckBox;
    @FXML
    private CheckBox profIndrumatorCheckBox;
    @FXML
    private Button addStudentButton;
    @FXML
    private Button updateStudentButton;
    @FXML
    private Button deleteStudentButton;
    @FXML
    private Button clearWorkspaceButton;

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public void setAccountRole(String accountRole) {
        this.accountRole = accountRole;
    }

    public void setAccountPicturePath(String accountPicturePath) {
        this.accountPicturePath = accountPicturePath;
    }

    private void createTable() {
        TableColumn<Student, Integer> column1 = new TableColumn<>("ID");
        column1.setCellValueFactory(new PropertyValueFactory<>("id"));
        column1.setMinWidth(60);
        column1.setMaxWidth(60);

        TableColumn<Student, String> column2 = new TableColumn<>("Name");
        column2.setCellValueFactory(new PropertyValueFactory<>("nume"));
        column2.setMinWidth(220);
        column2.setMaxWidth(220);

        TableColumn<Student, Integer> column3 = new TableColumn<>("Class");
        column3.setCellValueFactory(new PropertyValueFactory<>("grupa"));
        column3.setMinWidth(80);
        column3.setMaxWidth(80);

        TableColumn<Student, String> column4 = new TableColumn<>("Email");
        column4.setCellValueFactory(new PropertyValueFactory<>("email"));
        column4.setMinWidth(220);
        column4.setMaxWidth(220);

        TableColumn<Student, String> column5 = new TableColumn<>("Teacher");
        column5.setCellValueFactory(new PropertyValueFactory<>("profIndrumator"));
        column5.setMinWidth(220);
        column5.setMaxWidth(220);

        studentsTableView.getColumns().addAll(column1,column2,column3,column4,column5);

        if (!accountRole.equals("Standard User")) {
            TableColumn column6 = new TableColumn("Grades");
            column6.setCellFactory(tableCell -> new ViewGradesCell<>("View", this));
            column6.setMinWidth(100);
            column6.setMaxWidth(100);
            studentsTableView.getColumns().addAll(column6);
        }
        else {
            studentsTableView.setMinWidth(studentsTableView.getMinWidth() - 100);
            studentsTableView.setMaxWidth(studentsTableView.getMaxWidth() - 100);
            studentsTableView.setTranslateX(20);
        }
    }

    private Node createPage(int pageIndex) {
        int fromIndex = pageIndex * rowsPerPage;
        int toIndex = Math.min(fromIndex + rowsPerPage, service.getStudentiCount());
        studentsTableView.setItems(FXCollections.observableArrayList(service.studentsSublist(fromIndex, toIndex, filter)));
        return new Pane();
    }

    private void reloadTableView() {
        if (service.getStudentiCount(filter) % rowsPerPage == 0)
            studentsPagination.setPageCount(service.getStudentiCount(filter) / rowsPerPage);
        else
            studentsPagination.setPageCount(service.getStudentiCount(filter) / rowsPerPage + 1);
        if (service.getStudentiCount(filter) == 0)
            studentsPagination.setPageCount(1);
        //studentsPagination.setCurrentPageIndex(0);
        createPage(studentsPagination.getCurrentPageIndex());
    }

    public void setService(Service service) {
        this.service = service;

        createTable();
        reloadTableView();
        studentsPagination.setPageFactory(this::createPage);

        studentsTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        Student student = studentsTableView.getSelectionModel().getSelectedItem();
                        populateFields(student);
                    }
                });
            }
        });

        idTextField.textProperty().addListener((observable, oldValue, newValue)->filtreaza());
        numeTextField.textProperty().addListener((observable, oldValue, newValue)->filtreaza());
        grupaTextField.textProperty().addListener((observable, oldValue, newValue)->filtreaza());
        emailTextField.textProperty().addListener((observable, oldValue, newValue)->filtreaza());
        profIndrumatorTextField.textProperty().addListener((observable, oldValue, newValue)->filtreaza());

        if (accountRole.equals("Standard User")) {
            addStudentButton.setDisable(true);
            updateStudentButton.setDisable(true);
            deleteStudentButton.setDisable(true);
            clearWorkspaceButton.setDisable(false);
        }
        else if (accountRole.equals("Moderator")) {
            addStudentButton.setDisable(true);
            updateStudentButton.setDisable(true);
            deleteStudentButton.setDisable(true);
            clearWorkspaceButton.setDisable(false);
        }
        else if (accountRole.equals("Administrator")) {
            addStudentButton.setDisable(false);
            updateStudentButton.setDisable(false);
            deleteStudentButton.setDisable(false);
            clearWorkspaceButton.setDisable(false);
        }
    }

    public void filtreaza() {
        Predicate<Student> predicate = (x -> true);
        if (idCheckBox.isSelected()) {
            Predicate<Student> aux = (x -> (x.getId().toString().equals(idTextField.getText())));
            predicate = predicate.and(aux);
        }
        if (numeCheckBox.isSelected()) {
            Predicate<Student> aux = (x -> (x.getNume().contains(numeTextField.getText())));
            predicate = predicate.and(aux);
        }
        if (grupaCheckBox.isSelected()) {
            Predicate<Student> aux = (x -> (x.getGrupa().toString().equals(grupaTextField.getText())));
            predicate = predicate.and(aux);
        }
        if (emailCheckBox.isSelected()) {
            Predicate<Student> aux = (x -> (x.getEmail().contains(emailTextField.getText())));
            predicate = predicate.and(aux);
        }
        if (profIndrumatorCheckBox.isSelected()) {
            Predicate<Student> aux = (x -> (x.getProfIndrumator().contains(profIndrumatorTextField.getText())));
            predicate = predicate.and(aux);
        }

        filter = predicate;
        reloadTableView();
    }

    @Override
    public void onHyperlinkClick(int tableRowIndex) {
        rootController.showGradesOfStudent(studentsTableView.getItems().get(tableRowIndex));
    }

    private Student getStudentFromFields() {
        Integer id = Integer.parseInt(idTextField.getText());
        String nume = numeTextField.getText();
        Integer grupa = Integer.parseInt(grupaTextField.getText());
        String email = emailTextField.getText();
        String profIndrumator = profIndrumatorTextField.getText();

        return new Student(id, nume, grupa, email, profIndrumator);
    }

    private void populateFields(Student student) {
        idTextField.setText(student.getId().toString());
        numeTextField.setText(student.getNume());
        grupaTextField.setText(student.getGrupa().toString());
        emailTextField.setText(student.getEmail());
        profIndrumatorTextField.setText(student.getProfIndrumator());
    }

    public void addStudent(ActionEvent actionEvent) {
        if (Objects.equals(idTextField.getText(), "") ||
                Objects.equals(numeTextField.getText(), "") ||
                Objects.equals(grupaTextField.getText(), "") ||
                Objects.equals(emailTextField.getText(), "") ||
                Objects.equals(profIndrumatorTextField.getText(), ""))
            LoginController.showErrorMessage("The Text Fields must not be empty!");
        else {
            try {
                service.addStudent(getStudentFromFields());
                reloadTableView();
                LoginController.showMessage(Alert.AlertType.INFORMATION, "Add", "The student was successfully added!");
            } catch (ValidationException | RepositoryException e) {
                LoginController.showErrorMessage(e.getMessage());
            } catch (NumberFormatException e) {
                LoginController.showErrorMessage("The ID and Group Text Fields must contain natural numbers!");
            }
        }
    }

    public void updateStudent(ActionEvent actionEvent) {
        if (Objects.equals(idTextField.getText(), "") ||
                Objects.equals(numeTextField.getText(), "") ||
                Objects.equals(grupaTextField.getText(), "") ||
                Objects.equals(emailTextField.getText(), "") ||
                Objects.equals(profIndrumatorTextField.getText(), ""))
            LoginController.showErrorMessage("The Text Fields must not be empty!");
        else {
            try {
                service.modifyStudent(getStudentFromFields());
                reloadTableView();
                LoginController.showMessage(Alert.AlertType.INFORMATION, "Modify", "The student was successfully modified!");
            } catch (ValidationException | RepositoryException e) {
                LoginController.showErrorMessage(e.getMessage());
            } catch (NumberFormatException e) {
                LoginController.showErrorMessage("The ID and Group Text Fields must contain natural numbers!");
            }
        }
    }

    public void deleteStudent(ActionEvent actionEvent) {
        if (Objects.equals(idTextField.getText(), ""))
            LoginController.showErrorMessage("The ID Text Field must not be empty!");
        else {
            try {
                Integer id = Integer.parseInt(idTextField.getText());
                service.deleteStudent(id);
                reloadTableView();
                LoginController.showMessage(Alert.AlertType.INFORMATION, "Delete", "The student was successfully deleted!");
            } catch (ValidationException | RepositoryException e) {
                LoginController.showErrorMessage(e.getMessage());
            } catch (NumberFormatException e) {
                LoginController.showErrorMessage("The ID Text Field must contain a natural number!");
            }
        }
    }

    public void clearWorkspace(ActionEvent actionEvent) {
        idTextField.clear();
        numeTextField.clear();
        grupaTextField.clear();
        emailTextField.clear();
        profIndrumatorTextField.clear();
        idCheckBox.setSelected(false);
        numeCheckBox.setSelected(false);
        grupaCheckBox.setSelected(false);
        emailCheckBox.setSelected(false);
        profIndrumatorCheckBox.setSelected(false);
        filtreaza();
    }

    public void clearTableViewSelection(KeyEvent keyEvent) {
        studentsTableView.getSelectionModel().clearSelection();
    }

    public void setRootController(RootController rootController) {
        this.rootController = rootController;
    }
}
