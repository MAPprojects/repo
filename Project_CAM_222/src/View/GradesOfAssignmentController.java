package View;

import Domain.Nota;
import Domain.Student;
import Domain.Tema;
import Domain.ValidationException;
import Repository.RepositoryException;
import Service.Service;
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
import java.util.Optional;
import java.util.function.Predicate;

public class GradesOfAssignmentController {
    private Service service;
    private int rowsPerPage = 8;
    private Predicate<Student> filter = (x -> true);
    private String accountName;
    private String accountRole;
    private String accountPicturePath;
    private RootController rootController;
    private Tema tema;

    @FXML
    private TableView<Nota> studentsTableView;
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
    private TextField saptamanaTextField;
    @FXML
    private TextField observatiiTextField;
    @FXML
    private TextField gradeTextField;
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
    private Label assignmentNameLabel;

    private void createTable() {
        TableColumn<Nota, Integer> column1 = new TableColumn<>("ID");
        column1.setCellValueFactory(new PropertyValueFactory<>("idStudent"));
        column1.setMinWidth(60);
        column1.setMaxWidth(60);

        TableColumn<Nota, String> column2 = new TableColumn<>("Name");
        column2.setCellValueFactory(new PropertyValueFactory<>("numeStudent"));
        column2.setMinWidth(220);
        column2.setMaxWidth(220);

        TableColumn<Nota, Integer> column3 = new TableColumn<>("Class");
        column3.setCellValueFactory(new PropertyValueFactory<>("grupaStudent"));
        column3.setMinWidth(80);
        column3.setMaxWidth(80);

        TableColumn<Nota, String> column4 = new TableColumn<>("Email");
        column4.setCellValueFactory(new PropertyValueFactory<>("emailStudent"));
        column4.setMinWidth(220);
        column4.setMaxWidth(220);

        TableColumn<Nota, String> column5 = new TableColumn<>("Teacher");
        column5.setCellValueFactory(new PropertyValueFactory<>("profIndrumatorStudent"));
        column5.setMinWidth(220);
        column5.setMaxWidth(220);

        TableColumn<Nota, String> column6 = new TableColumn<>("Grade");
        column6.setCellValueFactory(new PropertyValueFactory<>("valoareTabel"));
        column6.setMinWidth(100);
        column6.setMaxWidth(100);

        studentsTableView.getColumns().addAll(column1, column2, column3, column4, column5, column6);
    }

    private Node createPage(int pageIndex) {
        int fromIndex = pageIndex * rowsPerPage;
        int toIndex = Math.min(fromIndex + rowsPerPage, service.getNoteCount());
        studentsTableView.setItems(FXCollections.observableArrayList(service.gradesOfAssignment(fromIndex, toIndex, filter, tema)));
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

        saptamanaTextField.setText("" + Service.weekOfYear);

        studentsTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        Student student = studentsTableView.getSelectionModel().getSelectedItem().getStudent();
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

    private void populateFields(Student student) {
        idTextField.setText(student.getId().toString());
        numeTextField.setText(student.getNume());
        grupaTextField.setText(student.getGrupa().toString());
        emailTextField.setText(student.getEmail());
        profIndrumatorTextField.setText(student.getProfIndrumator());
    }

    public void clearWorkspace(ActionEvent actionEvent) {
        idTextField.clear();
        numeTextField.clear();
        grupaTextField.clear();
        emailTextField.clear();
        profIndrumatorTextField.clear();
        saptamanaTextField.clear();
        observatiiTextField.clear();
        gradeTextField.clear();
        idCheckBox.setSelected(false);
        numeCheckBox.setSelected(false);
        grupaCheckBox.setSelected(false);
        emailCheckBox.setSelected(false);
        profIndrumatorCheckBox.setSelected(false);
        saptamanaTextField.setText("" + Service.weekOfYear);
        filtreaza();
    }
    
    public void clearTableViewSelection(KeyEvent keyEvent) {
        studentsTableView.getSelectionModel().clearSelection();
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public void setAccountRole(String accountRole) {
        this.accountRole = accountRole;
    }

    public void setAccountPicturePath(String accountPicturePath) {
        this.accountPicturePath = accountPicturePath;
    }

    public void setRootController(RootController rootController) {
        this.rootController = rootController;
    }

    public void setTema(Tema tema) {
        this.tema = tema;
        assignmentNameLabel.setText("" + tema.getId() + ". " + tema.getDescriere() + " - Deadline: Week " + tema.getDeadline());
    }

    public void addGrade(ActionEvent actionEvent) {
        if (Objects.equals(saptamanaTextField.getText(), ""))
            LoginController.showErrorMessage("The Handin Week Text Field must not be empty!");
        else {
            try {
                Optional<Student> s = service.findStudent(Integer.parseInt(idTextField.getText()));
                if (s.isPresent()) {
                    Student student = s.get();
                    String stringId = "" + student.getId() + "_" + tema.getId();
                    service.addNota(new Nota(stringId, student, tema, Integer.parseInt(gradeTextField.getText())),
                            observatiiTextField.getText(),Integer.parseInt(saptamanaTextField.getText()));
                    reloadTableView();
                    LoginController.showMessage(Alert.AlertType.INFORMATION, "Add", "The grade was successfully added!");
                }
                else
                    throw new ValidationException("Student ID is nonexistent!");
            } catch (ValidationException | RepositoryException e) {
                LoginController.showErrorMessage(e.getMessage());
            } catch (NumberFormatException e) {
                LoginController.showErrorMessage("The Student ID and Grade Value fields must contain natural numbers!");
            }
        }
    }
}
