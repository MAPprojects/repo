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

public class GradesOfStudentController {
    private Service service;
    private int rowsPerPage = 8;
    private Predicate<Tema> filter = (x -> true);
    private String accountName;
    private String accountRole;
    private String accountPicturePath;
    private RootController rootController;
    private Student student;

    @FXML
    private TableView<Nota> assignmentsTableView;
    @FXML
    private Pagination assignmentsPagination;
    @FXML
    private TextField idTextField;
    @FXML
    private TextField descriereTextField;
    @FXML
    private TextField deadlineTextField;
    @FXML
    private TextField saptamanaTextField;
    @FXML
    private TextField observatiiTextField;
    @FXML
    private TextField gradeTextField;
    @FXML
    private CheckBox idCheckBox;
    @FXML
    private CheckBox descriereCheckBox;
    @FXML
    private CheckBox deadlineCheckBox;
    @FXML
    private Label studentNameLabel;

    private void createTable() {
        TableColumn<Nota, Integer> column1 = new TableColumn<>("ID");
        column1.setCellValueFactory(new PropertyValueFactory<>("idTema"));
        column1.setMinWidth(60);
        column1.setMaxWidth(60);

        TableColumn<Nota, String> column2 = new TableColumn<>("Description");
        column2.setCellValueFactory(new PropertyValueFactory<>("descriereTema"));
        column2.setMinWidth(420);
        column2.setMaxWidth(420);

        TableColumn<Nota, Integer> column3 = new TableColumn<>("Deadline");
        column3.setCellValueFactory(new PropertyValueFactory<>("deadlineStringTema"));
        column3.setMinWidth(120);
        column3.setMaxWidth(120);

        TableColumn<Nota, String> column4 = new TableColumn<>("Grade");
        column4.setCellValueFactory(new PropertyValueFactory<>("valoareTabel"));
        column4.setMinWidth(100);
        column4.setMaxWidth(100);

        assignmentsTableView.getColumns().addAll(column1, column2, column3, column4);
    }

    private Node createPage(int pageIndex) {
        int fromIndex = pageIndex * rowsPerPage;
        int toIndex = Math.min(fromIndex + rowsPerPage, service.getNoteCount());
        assignmentsTableView.setItems(FXCollections.observableArrayList(service.gradesOfStudent(fromIndex, toIndex, filter, student)));
        return new Pane();
    }

    private void reloadTableView() {
        if (service.getTemeCount(filter) % rowsPerPage == 0)
            assignmentsPagination.setPageCount(service.getTemeCount(filter) / rowsPerPage);
        else
            assignmentsPagination.setPageCount(service.getTemeCount(filter) / rowsPerPage + 1);
        if (service.getTemeCount(filter) == 0)
            assignmentsPagination.setPageCount(1);
        //assignmentsPagination.setCurrentPageIndex(0);
        createPage(assignmentsPagination.getCurrentPageIndex());
    }

    public void setService(Service service) {
        this.service = service;

        createTable();
        reloadTableView();
        assignmentsPagination.setPageFactory(this::createPage);

        saptamanaTextField.setText("" + Service.weekOfYear);

        assignmentsTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        Tema tema = assignmentsTableView.getSelectionModel().getSelectedItem().getTema();
                        populateFields(tema);
                    }
                });
            }
        });

        idTextField.textProperty().addListener((observable, oldValue, newValue)->filtreaza());
        descriereTextField.textProperty().addListener((observable, oldValue, newValue)->filtreaza());
        deadlineTextField.textProperty().addListener((observable, oldValue, newValue)->filtreaza());
    }

    public void filtreaza() {
        Predicate<Tema> predicate = (x -> true);
        if (idCheckBox.isSelected()) {
            Predicate<Tema> aux = (x -> (x.getId().toString().equals(idTextField.getText())));
            predicate = predicate.and(aux);
        }
        if (descriereCheckBox.isSelected()) {
            Predicate<Tema> aux = (x -> (x.getDescriere().contains(descriereTextField.getText())));
            predicate = predicate.and(aux);
        }
        if (deadlineCheckBox.isSelected()) {
            Predicate<Tema> aux = (x -> (x.getDeadline().toString().equals(deadlineTextField.getText())));
            predicate = predicate.and(aux);
        }

        filter = predicate;
        reloadTableView();
    }

    public void populateFields(Tema tema) {
        idTextField.setText(tema.getId().toString());
        descriereTextField.setText(tema.getDescriere());
        deadlineTextField.setText(tema.getDeadline().toString());
    }

    public void clearWorkspace(ActionEvent actionEvent) {
        idTextField.clear();
        descriereTextField.clear();
        deadlineTextField.clear();
        saptamanaTextField.clear();
        observatiiTextField.clear();
        gradeTextField.clear();
        idCheckBox.setSelected(false);
        descriereCheckBox.setSelected(false);
        deadlineCheckBox.setSelected(false);
        saptamanaTextField.setText("" + Service.weekOfYear);
        filtreaza();
    }

    public void addGrade(ActionEvent actionEvent) {
        if (Objects.equals(saptamanaTextField.getText(), ""))
            LoginController.showErrorMessage("The Handin Week Text Field must not be empty!");
        else {
            try {
                Optional<Tema> t = service.findTema(Integer.parseInt(idTextField.getText()));
                if (t.isPresent()) {
                    Tema tema = t.get();
                    String stringId = "" + student.getId() + "_" + tema.getId();
                    service.addNota(new Nota(stringId, student, tema, Integer.parseInt(gradeTextField.getText())),
                            observatiiTextField.getText(),Integer.parseInt(saptamanaTextField.getText()));
                    reloadTableView();
                    LoginController.showMessage(Alert.AlertType.INFORMATION, "Add", "The grade was successfully added!");
                }
                else
                    throw new ValidationException("Assignment ID is nonexistent!");
            } catch (ValidationException | RepositoryException e) {
                LoginController.showErrorMessage(e.getMessage());
            } catch (NumberFormatException e) {
                LoginController.showErrorMessage("The Assignment ID and Grade Value fields must contain natural numbers!");
            }
        }
    }

    public void clearTableViewSelection(KeyEvent keyEvent) {
        assignmentsTableView.getSelectionModel().clearSelection();
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

    public void setStudent(Student student) {
        this.student = student;
        studentNameLabel.setText("" + student.getId() + ". " + student.getNume() + ", Group " + student.getGrupa());
    }
}
