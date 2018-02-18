package MVC;

import Domain.Student;
import Domain.ValidationException;
import Repository.RepositoryException;
import Service.Service;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static MVC.MainController.showErrorMessage;
import static MVC.MainController.showMessage;

public class StudentController {
    private boolean admin;
    private Service service;
    private Integer nrRanduriPePagina=16;
    private Predicate<Student> filtru = (x -> true);

    @FXML
    private TableView<Student> studentTableView;
    @FXML
    private Pagination pagination;
    @FXML
    AutocompletionTextField idTextField;
    @FXML
    AutocompletionTextField numeTextField;
    @FXML
    AutocompletionTextField grupaTextField;
    @FXML
    AutocompletionTextField emailTextField;
    @FXML
    AutocompletionTextField profIndrumatorTextField;
    @FXML
    private CheckBox nameCheckBox;
    @FXML
    private CheckBox classCheckBox;
    @FXML
    private CheckBox teacherCheckBox;

    public void setService(Service service) {
        this.service = service;

        reloadTableView();
        pagination.setPageFactory(this::createPage);

        studentTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                Platform.runLater(() -> {
                    Student student = studentTableView.getSelectionModel().getSelectedItem();
                    idTextField.setText(student.getId().toString());
                    numeTextField.setText(student.getNume());
                    grupaTextField.setText(student.getGrupa().toString());
                    emailTextField.setText(student.getEmail());
                    profIndrumatorTextField.setText(student.getProfIndrumator());
                    idTextField.hideEntries();
                    numeTextField.hideEntries();
                    grupaTextField.hideEntries();
                    emailTextField.hideEntries();
                    profIndrumatorTextField.hideEntries();
                });
            }
        });

        fillTextFields();

        idTextField.textProperty().addListener((observable, oldValue, newValue)->updateFilter());
        numeTextField.textProperty().addListener((observable, oldValue, newValue)->updateFilter());
        grupaTextField.textProperty().addListener((observable, oldValue, newValue)->updateFilter());
        emailTextField.textProperty().addListener((observable, oldValue, newValue)->updateFilter());
        profIndrumatorTextField.textProperty().addListener((observable, oldValue, newValue)->updateFilter());
    }

    private Node createPage(int pageIndex) {
        int fromIndex = pageIndex * nrRanduriPePagina;
        int toIndex = Math.min(fromIndex + nrRanduriPePagina, service.getNrStudenti());
        studentTableView.setItems(FXCollections.observableArrayList(service.sublistaStudenti(fromIndex, toIndex, filtru)));
        return new Pane();
    }

    private void reloadTableView() {
        if (service.getNrStudenti(filtru) % nrRanduriPePagina == 0)
            pagination.setPageCount(service.getNrStudenti(filtru) / nrRanduriPePagina);
        else
            pagination.setPageCount(service.getNrStudenti(filtru) / nrRanduriPePagina + 1);
        if (service.getNrStudenti(filtru) == 0)
            pagination.setPageCount(1);
        createPage(pagination.getCurrentPageIndex());
    }

    public void updateFilter() {
        Predicate<Student> predicate = (x -> true);
        if (nameCheckBox.isSelected()) {
            Predicate<Student> aux = (x -> (x.getNume().contains(numeTextField.getText())));
            predicate = predicate.and(aux);
        }
        if (classCheckBox.isSelected()) {
            Predicate<Student> aux = (x -> (x.getGrupa().toString().contains(grupaTextField.getText())));
            predicate = predicate.and(aux);
        }
        if (teacherCheckBox.isSelected()) {
            Predicate<Student> aux = (x -> (x.getProfIndrumator().contains(profIndrumatorTextField.getText())));
            predicate = predicate.and(aux);
        }

        filtru = predicate;
        reloadTableView();
    }

    private Student getStudentFromFields() {
        Integer id = Integer.parseInt(idTextField.getText());
        String nume = numeTextField.getText();
        Integer grupa = Integer.parseInt(grupaTextField.getText());
        String email = emailTextField.getText();
        String profIndrumator = profIndrumatorTextField.getText();

        return new Student(id, nume, grupa, email, profIndrumator);
    }

    public void clearFields(ActionEvent actionEvent) {
        studentTableView.getSelectionModel().clearSelection();
        idTextField.clear();
        numeTextField.clear();
        grupaTextField.clear();
        emailTextField.clear();
        profIndrumatorTextField.clear();
        nameCheckBox.setSelected(false);
        classCheckBox.setSelected(false);
        teacherCheckBox.setSelected(false);
        updateFilter();
    }

    public void fillTextFields(){
        idTextField.getEntries().clear();
        numeTextField.getEntries().clear();
        grupaTextField.getEntries().clear();
        emailTextField.getEntries().clear();
        profIndrumatorTextField.getEntries().clear();

        List<String> stringList1 = new ArrayList<>();
        stringList1.addAll(service.getNumeStudenti());
        numeTextField.getEntries().addAll(stringList1);


        List<String> stringList2 = new ArrayList<>();
        stringList2.addAll(service.getGrupaStudenti());
        grupaTextField.getEntries().addAll(stringList2);


        List<String> stringList3 = new ArrayList<>();
        stringList3.addAll(service.getProfIndrumator());
        profIndrumatorTextField.getEntries().addAll(stringList3);
    }

    public void handleKeyPressed(KeyEvent keyEvent) {
        studentTableView.getSelectionModel().clearSelection();
    }

    public void addStudent(ActionEvent actionEvent) {
        try {
            if (!admin){
                throw new ValidationException("Nu aveti drepturile necesare!");
            }
            service.adaugaStudent(getStudentFromFields());
            reloadTableView();
            showMessage(Alert.AlertType.CONFIRMATION, "Operatiune completa", "Student adaugat cu succes!");
        } catch (ValidationException | RepositoryException | NumberFormatException exception) {
            showErrorMessage(exception.getMessage());
        }
    }

    public void updateStudent(ActionEvent actionEvent) {
        try {
            if (!admin){
                throw new ValidationException("Nu aveti drepturile necesare!");
            }
            service.modificaStudent(getStudentFromFields());
            reloadTableView();
            showMessage(Alert.AlertType.CONFIRMATION, "Operatiune completa", "Student modificat cu succes!");
        } catch (ValidationException | RepositoryException | NumberFormatException exception) {
            showErrorMessage(exception.getMessage());
        }
    }

    public void deleteStudent(ActionEvent actionEvent) {
        try {
            if (!admin){
                throw new ValidationException("Nu aveti drepturile necesare!");
            }
            Integer id = Integer.parseInt(idTextField.getText());
            service.stergeStudent(id);
            reloadTableView();
            showMessage(Alert.AlertType.CONFIRMATION, "Operatiune completa!", "Student sters cu succes!");
        } catch (ValidationException | RepositoryException | NumberFormatException exception) {
            showErrorMessage(exception.getMessage());
        }
    }

    public void setDrepturi(boolean admin){
        this.admin=admin;
    }
}
