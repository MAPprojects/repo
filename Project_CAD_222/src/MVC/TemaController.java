package MVC;

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
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;

import java.util.function.Predicate;

import static MVC.MainController.showErrorMessage;
import static MVC.MainController.showMessage;

public class TemaController {
    private boolean admin;
    private Service service;
    private Integer nrRanduriPePagina=16;
    private Predicate<Tema> filtru =  (x -> true);

    @FXML
    private TableView<Tema> temaTableView;
    @FXML
    private Pagination pagination;

    @FXML
    TextField idTextField;
    @FXML
    TextField descriereTextField;
    @FXML
    TextField deadlineTextField;
    @FXML
    private CheckBox idCheckBox;
    @FXML
    private CheckBox descriptionCheckBox;
    @FXML
    private CheckBox deadlineCheckBox;

    public void setService(Service service) {
        this.service = service;

        reloadTableView();
        pagination.setPageFactory(this::createPage);

        temaTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                Platform.runLater(() -> {
                    Tema tema = temaTableView.getSelectionModel().getSelectedItem();
                    idTextField.setText(tema.getId().toString());
                    descriereTextField.setText(tema.getDescriere());
                    deadlineTextField.setText(tema.getDeadline().toString());
                });
            }
        });

        idTextField.textProperty().addListener((observable, oldValue, newValue)->updateFilter());
        descriereTextField.textProperty().addListener((observable, oldValue, newValue)->updateFilter());
        deadlineTextField.textProperty().addListener((observable, oldValue, newValue)->updateFilter());
    }

    private void reloadTableView() {
        if (service.getNrTeme(filtru) % nrRanduriPePagina == 0)
            pagination.setPageCount(service.getNrTeme(filtru) / nrRanduriPePagina);
        else
            pagination.setPageCount(service.getNrTeme(filtru) / nrRanduriPePagina + 1);
        if (service.getNrTeme(filtru) == 0)
            pagination.setPageCount(1);
        createPage(pagination.getCurrentPageIndex());
    }

    private Node createPage(int pageIndex) {
        int indexInceput = pageIndex * nrRanduriPePagina;
        int indexFinal = Math.min(indexInceput + nrRanduriPePagina, service.getNrTeme());
        temaTableView.setItems(FXCollections.observableArrayList(service.sublistaTeme(indexInceput, indexFinal, filtru)));
        return new Pane();
    }

    public void clearFields(ActionEvent actionEvent) {
        temaTableView.getSelectionModel().clearSelection();
        idTextField.clear();
        descriereTextField.clear();
        deadlineTextField.clear();
        idCheckBox.setSelected(false);
        descriptionCheckBox.setSelected(false);
        deadlineCheckBox.setSelected(false);
        updateFilter();
    }

    public void handleKeyPressed(KeyEvent keyEvent) {
        temaTableView.getSelectionModel().clearSelection();
    }

    @FXML
    private void updateFilter() {
        Predicate<Tema> p = (x -> true);
        if (idCheckBox.isSelected()) {
            Predicate<Tema> pNou = (x -> x.getId().toString().contains(idTextField.getText()));
            p = p.and(pNou);
        }

        if (descriptionCheckBox.isSelected()) {
            Predicate<Tema> pNou = (x -> x.getDescriere().contains(descriereTextField.getText()));
            p = p.and(pNou);
        }

        if (deadlineCheckBox.isSelected()) {
            Predicate<Tema> pNou = (x -> x.getDeadline().toString().contains(deadlineTextField.getText()));
            p = p.and(pNou);
        }

        filtru = p;
        reloadTableView();
    }

    public Tema getTemaFromFields() {
        Integer id = Integer.parseInt(idTextField.getText());
        String descriere = descriereTextField.getText();
        Integer deadline = Integer.parseInt(deadlineTextField.getText());
        return new Tema(id, descriere, deadline);
    }

    public void addTema(ActionEvent actionEvent) {
        try {
            if (!admin){
                throw new ValidationException("Nu aveti drepturile necesare!");
            }
            service.adaugaTema(getTemaFromFields());
            reloadTableView();
            showMessage(Alert.AlertType.CONFIRMATION, "Operatiune completata cu succes", "Tema a fost adaugata!");
        } catch (ValidationException | RepositoryException | NumberFormatException exception) {
            showErrorMessage(exception.getMessage());
        }
    }

    public void updateTema(ActionEvent actionEvent) {
        try {
            if (!admin){
                throw new ValidationException("Nu aveti drepturile necesare!");
            }
            service.modificaTema(getTemaFromFields());
            reloadTableView();
            showMessage(Alert.AlertType.CONFIRMATION, "Operatiune completata cu succes", "Tema a fost modificata!");
        } catch (ValidationException | RepositoryException | NumberFormatException exception) {
            showErrorMessage(exception.getMessage());
        }
    }

    public void updateDeadline(ActionEvent actionEvent) {
        try {
            if (!admin){
                throw new ValidationException("Nu aveti drepturile necesare!");
            }
            Integer id=Integer.parseInt(idTextField.getText());
            Integer deadline=Integer.parseInt(deadlineTextField.getText());
            service.modifyingDeadline(id,deadline);
            reloadTableView();
            showMessage(Alert.AlertType.CONFIRMATION, "Operatiune completata cu succes", "Deadline-ul a fost modificat!");
        } catch (ValidationException | RepositoryException | NumberFormatException exception) {
            showErrorMessage(exception.getMessage());
        }
    }

    public void deleteTema(ActionEvent actionEvent) {
        try {
            if (!admin){
                throw new ValidationException("Nu aveti drepturile necesare!");
            }
            Integer id = Integer.parseInt(idTextField.getText());
            service.stergeTema(id);
            reloadTableView();
            showMessage(Alert.AlertType.CONFIRMATION, "Operatiune completata cu succes", "Tema a fost stearsa!");
        } catch (ValidationException | RepositoryException | NumberFormatException exception) {
            showErrorMessage(exception.getMessage());
        }
    }

    public void setDrepturi(boolean admin){
        this.admin=admin;
    }
}
