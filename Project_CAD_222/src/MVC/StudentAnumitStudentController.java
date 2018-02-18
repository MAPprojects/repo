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

import java.util.function.Predicate;

import static MVC.MainController.showErrorMessage;
import static MVC.MainController.showMessage;

public class StudentAnumitStudentController{
    private Service service;
    private Integer nrRanduriPePagina=16;
    private Predicate<Student> filtru = (x -> true);

    @FXML
    private TableView<Student> studentAnumitStudentTableView;
    @FXML
    private Pagination pagination;
    @FXML
    TextField idTextField;
    @FXML
    TextField numeTextField;
    @FXML
    TextField grupaTextField;
    @FXML
    TextField emailTextField;
    @FXML
    TextField profIndrumatorTextField;
    @FXML
    private CheckBox nameCheckBox;
    @FXML
    private CheckBox classCheckBox;
    @FXML
    private CheckBox teacherCheckBox;

    private Node createPage(int pageIndex) {
        int fromIndex = pageIndex * nrRanduriPePagina;
        int toIndex = Math.min(fromIndex + nrRanduriPePagina, service.getNrStudenti());
        studentAnumitStudentTableView.setItems(FXCollections.observableArrayList(service.sublistaStudenti(fromIndex, toIndex, filtru)));
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

    public void setService(Service service) {
        this.service = service;

        reloadTableView();
        pagination.setPageFactory(this::createPage);

        studentAnumitStudentTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                Platform.runLater(() -> {
                    Student student = studentAnumitStudentTableView.getSelectionModel().getSelectedItem();
                    idTextField.setText(student.getId().toString());
                    numeTextField.setText(student.getNume());
                    grupaTextField.setText(student.getGrupa().toString());
                    emailTextField.setText(student.getEmail());
                    profIndrumatorTextField.setText(student.getProfIndrumator());
                });
            }
        });

        idTextField.textProperty().addListener((observable, oldValue, newValue)->updateFilter());
        numeTextField.textProperty().addListener((observable, oldValue, newValue)->updateFilter());
        grupaTextField.textProperty().addListener((observable, oldValue, newValue)->updateFilter());
        emailTextField.textProperty().addListener((observable, oldValue, newValue)->updateFilter());
        profIndrumatorTextField.textProperty().addListener((observable, oldValue, newValue)->updateFilter());
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

    public void handleKeyPressed(KeyEvent keyEvent) {
        studentAnumitStudentTableView.getSelectionModel().clearSelection();
    }
}
