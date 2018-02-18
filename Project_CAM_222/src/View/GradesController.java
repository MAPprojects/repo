package View;

import Domain.Nota;
import Domain.Student;
import Domain.Tema;
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
import java.util.Optional;
import java.util.function.Predicate;

public class GradesController implements HyperlinkObserver {
    private Service service;
    private int rowsPerPage = 9;
    private Predicate<Nota> filter = (x -> true);
    private String accountName;
    private String accountRole;
    private String accountPicturePath;
    private RootController rootController;

    @FXML
    private TableView<Nota> gradesTableView;
    @FXML
    private Pagination gradesPagination;
    @FXML
    private TextField studentIdTextField;
    @FXML
    private TextField temaIdTextField;
    @FXML
    private TextField valoareTextField;
    @FXML
    private TextField observatiiTextField;
    @FXML
    private TextField saptamanaTextField;
    @FXML
    private CheckBox studentIdCheckBox;
    @FXML
    private CheckBox temaIdCheckBox;
    @FXML
    private CheckBox valoareCheckBox;
    @FXML
    private Button addGradeButton;
    @FXML
    private Button updateGradeButton;
    @FXML
    private Button deleteGradeButton;
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
        TableColumn<Nota, Integer> column1 = new TableColumn<>("Student ID");
        column1.setCellValueFactory(new PropertyValueFactory<>("idStudent"));
        column1.setMinWidth(150);
        column1.setMaxWidth(150);

        TableColumn<Nota, String> column2 = new TableColumn<>("Student Name");
        column2.setCellValueFactory(new PropertyValueFactory<>("numeStudent"));
        column2.setMinWidth(220);
        column2.setMaxWidth(220);

        TableColumn<Nota, String> column3 = new TableColumn<>("Assignment Description");
        column3.setCellValueFactory(new PropertyValueFactory<>("descriereTema"));
        column3.setMinWidth(300);
        column3.setMaxWidth(300);

        TableColumn<Nota, Integer> column4 = new TableColumn<>("Value");
        column4.setCellValueFactory(new PropertyValueFactory<>("valoare"));
        column4.setMinWidth(120);
        column4.setMaxWidth(120);

        TableColumn column5 = new TableColumn("Delete");
        column5.setCellFactory(tableCell -> new ViewGradesCell<>("Delete", this));
        column5.setMinWidth(90);
        column5.setMaxWidth(90);

        gradesTableView.getColumns().addAll(column1,column2,column3,column4,column5);
    }

    private Node createPage(int pageIndex) {
        if (!accountRole.equals("Standard User")) {
            int fromIndex = pageIndex * rowsPerPage;
            int toIndex = Math.min(fromIndex + rowsPerPage, service.getNoteCount());
            gradesTableView.setItems(FXCollections.observableArrayList(service.gradesSublist(fromIndex, toIndex, filter)));
            return new Pane();
        }
        else {
            int fromIndex = pageIndex * rowsPerPage;
            int toIndex = Math.min(fromIndex + rowsPerPage, service.getNoteCount());
            gradesTableView.setItems(FXCollections.observableArrayList(service.gradesSublist(fromIndex, toIndex, filter, accountName)));
            return new Pane();
        }
    }

    private void reloadTableView() {
        if (!accountRole.equals("Standard User")) {
            if (service.getNoteCount(filter) % rowsPerPage == 0)
                gradesPagination.setPageCount(service.getNoteCount(filter) / rowsPerPage);
            else
                gradesPagination.setPageCount(service.getNoteCount(filter) / rowsPerPage + 1);
            if (service.getNoteCount(filter) == 0)
                gradesPagination.setPageCount(1);
            //gradesPagination.setCurrentPageIndex(0);
            createPage(gradesPagination.getCurrentPageIndex());
        }
        else {
            if (service.getNoteCount(filter, accountName) % rowsPerPage == 0)
                gradesPagination.setPageCount(service.getNoteCount(filter, accountName) / rowsPerPage);
            else
                gradesPagination.setPageCount(service.getNoteCount(filter, accountName) / rowsPerPage + 1);
            if (service.getNoteCount(filter, accountName) == 0)
                gradesPagination.setPageCount(1);
            //gradesPagination.setCurrentPageIndex(0);
            createPage(gradesPagination.getCurrentPageIndex());
        }
    }

    public void setService(Service service) {
        this.service = service;

        createTable();
        reloadTableView();
        gradesPagination.setPageFactory(this::createPage);

        gradesTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        Nota nota = gradesTableView.getSelectionModel().getSelectedItem();
                        populateFields(nota);
                    }
                });
            }
        });

        saptamanaTextField.setText("" + Service.weekOfYear);

        studentIdTextField.textProperty().addListener((observable, oldValue, newValue)->filtreaza());
        temaIdTextField.textProperty().addListener((observable, oldValue, newValue)->filtreaza());
        valoareTextField.textProperty().addListener((observable, oldValue, newValue)->filtreaza());
        observatiiTextField.textProperty().addListener((observable, oldValue, newValue)->filtreaza());
        saptamanaTextField.textProperty().addListener((observable, oldValue, newValue)->filtreaza());

        if (accountRole.equals("Standard User")) {
            addGradeButton.setDisable(true);
            updateGradeButton.setDisable(true);
            deleteGradeButton.setDisable(true);
            clearWorkspaceButton.setDisable(false);
        }
        else if (accountRole.equals("Moderator")) {
            addGradeButton.setDisable(false);
            updateGradeButton.setDisable(false);
            deleteGradeButton.setDisable(false);
            clearWorkspaceButton.setDisable(false);
        }
        else if (accountRole.equals("Administrator")) {
            addGradeButton.setDisable(false);
            updateGradeButton.setDisable(false);
            deleteGradeButton.setDisable(false);
            clearWorkspaceButton.setDisable(false);
        }
    }

    public void filtreaza() {
        Predicate<Nota> predicate = (x -> true);
        if (studentIdCheckBox.isSelected()) {
            Predicate<Nota> aux = (x -> (x.getIdStudent().toString().equals(studentIdTextField.getText())));
            predicate = predicate.and(aux);
        }
        if (temaIdCheckBox.isSelected()) {
            Predicate<Nota> aux = (x -> (x.getIdTema().toString().equals(temaIdTextField.getText())));
            predicate = predicate.and(aux);
        }
        if (valoareCheckBox.isSelected()) {
            Predicate<Nota> aux = (x -> (x.getValoare().toString().equals(valoareTextField.getText())));
            predicate = predicate.and(aux);
        }

        filter = predicate;
        reloadTableView();
    }

    private Nota getNotaFromFields() {
        Student student = service.findStudent(Integer.parseInt(studentIdTextField.getText())).get();
        Tema tema = service.findTema(Integer.parseInt(temaIdTextField.getText())).get();
        String id = student.getId().toString() + "_" + tema.getId().toString();
        Integer valoare = Integer.parseInt(valoareTextField.getText());

        return new Nota(id, student, tema, valoare);
    }

    private void populateFields(Nota nota) {
        studentIdTextField.setText(nota.getStudent().getId().toString());
        temaIdTextField.setText(nota.getTema().getId().toString());
        valoareTextField.setText(nota.getValoare().toString());
    }

    @Override
    public void onHyperlinkClick(int tableRowIndex) {
        if (!accountRole.equals("Standard User")) {
            Nota nota = gradesTableView.getItems().get(tableRowIndex);
            service.deleteNota(nota.getId());
            reloadTableView();
            LoginController.showMessage(Alert.AlertType.INFORMATION, "Delete", "The grade was successfully deleted!");
        }
    }

    public void addGrade(ActionEvent actionEvent) {
        if (Objects.equals(studentIdTextField.getText(), "") ||
                Objects.equals(temaIdTextField.getText(), "") ||
                Objects.equals(valoareTextField.getText(), "") ||
                Objects.equals(saptamanaTextField.getText(), ""))
            LoginController.showErrorMessage("The Text Fields must not be empty!");
        else {
            try {
                Optional<Student> s = service.findStudent(Integer.parseInt(studentIdTextField.getText()));
                Optional<Tema> t = service.findTema(Integer.parseInt(temaIdTextField.getText()));
                if (s.isPresent() && t.isPresent()) {
                    Student student = s.get();
                    Tema tema = t.get();
                    String stringId = "" + student.getId() + "_" + tema.getId();
                    Optional<Nota> nota = service.findNota(stringId);
                    if (nota.isPresent())
                        throw new ValidationException("A grade already exists for that student and assignment!");
                    service.addNota(new Nota(stringId, student, tema, Integer.parseInt(valoareTextField.getText())),
                            observatiiTextField.getText(),Integer.parseInt(saptamanaTextField.getText()));
                    reloadTableView();
                    LoginController.showMessage(Alert.AlertType.INFORMATION, "Add", "The grade was successfully added!");
                }
                else
                    throw new ValidationException("Student & Assignment ID are nonexistent!");
            } catch (ValidationException | RepositoryException e) {
                LoginController.showErrorMessage(e.getMessage());
            } catch (NumberFormatException e) {
                LoginController.showErrorMessage("The Student ID, Assignment ID and Value fields must contain natural numbers!");
            }
        }
    }

    public void updateGrade(ActionEvent actionEvent) {
        if (Objects.equals(studentIdTextField.getText(), "") ||
                Objects.equals(temaIdTextField.getText(), "") ||
                Objects.equals(valoareTextField.getText(), "") ||
                Objects.equals(saptamanaTextField.getText(), ""))
            LoginController.showErrorMessage("The Text Fields must not be empty!");
        else {
            try {
                Optional<Student> s = service.findStudent(Integer.parseInt(studentIdTextField.getText()));
                Optional<Tema> t = service.findTema(Integer.parseInt(temaIdTextField.getText()));
                if (s.isPresent() && t.isPresent()) {
                    Student student = s.get();
                    Tema tema = t.get();
                    String stringId = "" + student.getId() + "_" + tema.getId();
                    Optional<Nota> nota = service.findNota(stringId);
                    if (!nota.isPresent())
                        throw new ValidationException("There is no grade for that student and assignment!");
                    service.addNota(new Nota(stringId, student, tema, Integer.parseInt(valoareTextField.getText())),
                            observatiiTextField.getText(),Integer.parseInt(saptamanaTextField.getText()));
                    reloadTableView();
                    LoginController.showMessage(Alert.AlertType.INFORMATION, "Modify", "The grade was successfully modified!");
                }
                else
                    throw new ValidationException("Student & Assignment ID are nonexistent!");
            } catch (ValidationException | RepositoryException e) {
                LoginController.showErrorMessage(e.getMessage());
            } catch (NumberFormatException e) {
                LoginController.showErrorMessage("The Student ID, Assignment ID and Value fields must contain natural numbers!");
            }
        }
    }

    public void deleteGrade(ActionEvent actionEvent) {
        if (Objects.equals(studentIdTextField.getText(), "") ||
                Objects.equals(temaIdTextField.getText(), "")) {
            LoginController.showErrorMessage("The Text Fields must not be empty!");
        }
        else {
            try {
                Optional<Student> s = service.findStudent(Integer.parseInt(studentIdTextField.getText()));
                Optional<Tema> t = service.findTema(Integer.parseInt(temaIdTextField.getText()));
                if (s.isPresent() && t.isPresent()) {
                    Student student = s.get();
                    Tema tema = t.get();
                    String stringId = "" + student.getId() + "_" + tema.getId();
                    Optional<Nota> nota = service.findNota(stringId);
                    if (!nota.isPresent())
                        throw new ValidationException("There is no grade for that student and assignment!");
                    service.deleteNota(stringId);
                    reloadTableView();
                    LoginController.showMessage(Alert.AlertType.INFORMATION, "Delete", "The grade was successfully deleted!");
                }
                else
                    throw new ValidationException("Student & Assignment ID are nonexistent!");
            } catch (ValidationException | RepositoryException e) {
                LoginController.showErrorMessage(e.getMessage());
            } catch (NumberFormatException e) {
                LoginController.showErrorMessage("The Student ID and Assignment ID fields must contain natural numbers!");
            }
        }
    }

    public void clearWorkspace(ActionEvent actionEvent) {
        studentIdTextField.clear();
        temaIdTextField.clear();
        valoareTextField.clear();
        observatiiTextField.clear();
        saptamanaTextField.setText("" + Service.weekOfYear);
        studentIdCheckBox.setSelected(false);
        temaIdCheckBox.setSelected(false);
        valoareCheckBox.setSelected(false);
        filtreaza();
    }

    public void clearTableViewSelection(KeyEvent keyEvent) {
        gradesTableView.getSelectionModel().clearSelection();
    }

    public void setRootController(RootController rootController) {
        this.rootController = rootController;
    }
}
