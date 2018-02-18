package MVC;

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
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import static MVC.MainController.showErrorMessage;
import static MVC.MainController.showMessage;

public class NotaController {
    private boolean admin;
    private Service service;
    private Integer nrRanduriPePagina=16;
    private Predicate<Nota> filtru =  (x -> true);

    @FXML
    private TableView<Nota> notaTableView;
    @FXML
    private Pagination pagination;

    @FXML
    AutocompletionTextField studentNameTextField;
    @FXML
    AutocompletionTextField descriptionTextField;
    @FXML
    AutocompletionTextField valoareTextField;
    @FXML
    AutocompletionTextField observatiiTextField;
    @FXML
    AutocompletionTextField saptamanaTextField;
    @FXML
    AutocompletionTextField grupaTextField;
    @FXML
    private CheckBox studentNameCheckBox;
    @FXML
    private CheckBox descriptionCheckBox;
    @FXML
    private CheckBox classCheckBox;
    @FXML
    private CheckBox gradeGreaterThanCheckBox;
    @FXML
    private CheckBox gradeLessThanCheckBox;


    public void setService(Service service) {
        this.service = service;

        reloadTableView();
        pagination.setPageFactory(this::createPage);

        notaTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                Platform.runLater(() -> {
                    Nota nota = notaTableView.getSelectionModel().getSelectedItem();
                    descriptionTextField.setText(nota.getTema().getDescriere());
                    valoareTextField.setText(nota.getValoare().toString());
                    studentNameTextField.setText(nota.getNumeStudent());
                    grupaTextField.setText(nota.getStudent().getGrupa().toString());
                    descriptionTextField.hideEntries();
                    valoareTextField.hideEntries();
                    studentNameTextField.hideEntries();
                    grupaTextField.hideEntries();
                });
            }
        });

        fillTextFields();

        studentNameTextField.textProperty().addListener((observable, oldValue, newValue)->updateFilter());
        descriptionTextField.textProperty().addListener((observable, oldValue, newValue)->updateFilter());
        grupaTextField.textProperty().addListener((observable, oldValue, newValue)->updateFilter());
        valoareTextField.textProperty().addListener((observable, oldValue, newValue)->updateFilter());
    }

    private void reloadTableView() {
        if (service.getNrNote(filtru) % nrRanduriPePagina == 0)
            pagination.setPageCount(service.getNrNote(filtru) / nrRanduriPePagina);
        else
            pagination.setPageCount(service.getNrNote(filtru) / nrRanduriPePagina + 1);
        if (service.getNrNote(filtru) == 0)
            pagination.setPageCount(1);
        createPage(pagination.getCurrentPageIndex());
    }

    private Node createPage(int pageIndex) {
        int indexInceput = pageIndex * nrRanduriPePagina;
        int indexFinal = Math.min(indexInceput + nrRanduriPePagina, service.getNrNote());
        notaTableView.setItems(FXCollections.observableArrayList(service.sublistaNote(indexInceput, indexFinal, filtru)));
        return new Pane();
    }

    public void fillTextFields(){
        studentNameTextField.getEntries().clear();
        descriptionTextField.getEntries().clear();
        valoareTextField.getEntries().clear();
        observatiiTextField.getEntries().clear();
        saptamanaTextField.getEntries().clear();
        grupaTextField.getEntries().clear();

        List<String> stringList1 = new ArrayList<>();
        stringList1.addAll(service.getNumeStudenti());
        studentNameTextField.getEntries().addAll(stringList1);


        List<String> stringList2 = new ArrayList<>();
        stringList2.addAll(service.getGrupaStudenti());
        grupaTextField.getEntries().addAll(stringList2);


        List<String> stringList3 = new ArrayList<>();
        stringList3.addAll(service.getDescriereTeme());
        descriptionTextField.getEntries().addAll(stringList3);
    }

    public void clearFields(ActionEvent actionEvent) {
        notaTableView.getSelectionModel().clearSelection();
        studentNameTextField.clear();
        descriptionTextField.clear();
        valoareTextField.clear();
        observatiiTextField.clear();
        saptamanaTextField.clear();
        grupaTextField.clear();
        studentNameCheckBox.setSelected(false);
        descriptionCheckBox.setSelected(false);
        classCheckBox.setSelected(false);
        gradeGreaterThanCheckBox.setSelected(false);
        gradeLessThanCheckBox.setSelected(false);
        updateFilter();
    }

    public void handleKeyPressed(KeyEvent keyEvent) {
        notaTableView.getSelectionModel().clearSelection();
    }


    @FXML
    private void updateFilter() {
        Predicate<Nota> p = (x -> true);
        if (studentNameCheckBox.isSelected()) {
            Predicate<Nota> pNou = (x -> x.getNumeStudent().contains(studentNameTextField.getText()));
            p = p.and(pNou);
        }

        if (descriptionCheckBox.isSelected()) {
            Predicate<Nota> pNou = (x -> x.getDescriereTema().contains(descriptionTextField.getText()));
            p = p.and(pNou);
        }

        if (classCheckBox.isSelected()) {
            Predicate<Nota> pNou = (x -> x.getStudent().getGrupa().toString().contains(grupaTextField.getText()));
            p = p.and(pNou);
        }

        if (gradeGreaterThanCheckBox.isSelected()) {
            try {
                Predicate<Nota> pNou = (x -> {
                    try {
                        return x.getValoare() >= Integer.parseInt(valoareTextField.getText());
                    }
                    catch (NumberFormatException ignored) {
                        return true;
                    }
                });
                p = p.and(pNou);
            } catch (NumberFormatException ignored) {
            }
        }

        if (gradeLessThanCheckBox.isSelected()) {
            try {
                Predicate<Nota> pNou = (x -> {
                    try {
                        return x.getValoare() <= Integer.parseInt(valoareTextField.getText());
                    }
                    catch (NumberFormatException ignored) {
                        return true;
                    }
                });
                p = p.and(pNou);
            } catch (NumberFormatException ignored) {
            }
        }

        filtru = p;
        reloadTableView();
    }

    public void addNota(ActionEvent actionEvent) {
        try {
            if (!admin){
                throw new ValidationException("Nu aveti drepturile necesare!");
            }
            Student student = service.findStudentByName(studentNameTextField.getText());
            Tema tema = service.findHomeworkByDescription(descriptionTextField.getText());
            if (tema!=null && student != null) {
                String stringId = "" + student.getId() + "-" + tema.getId();
                Optional<Nota> nota = service.findNota(stringId);
                if (nota.isPresent())
                    throw new ValidationException("Exista deja o nota la aceasta tema pentru acest student!");
                service.adaugaNota(new Nota(stringId, student, tema, Integer.parseInt(valoareTextField.getText())),
                        Integer.parseInt(saptamanaTextField.getText()), observatiiTextField.getText());
                reloadTableView();
                showMessage(Alert.AlertType.INFORMATION, "Operatiune completa!", "Nota a fost adaugata!");
            } else
                throw new ValidationException("Nume student sau id tema invalid!");
        } catch (ValidationException | RepositoryException e) {
            showErrorMessage(e.getMessage());
        } catch (NumberFormatException e) {
            showErrorMessage("Id-ul temei, nota si saptamana trebuie sa fie numere naturale!");
        }
    }

    public void updateNota(ActionEvent actionEvent) {
        try {
            if (!admin){
                throw new ValidationException("Nu aveti drepturile necesare!");
            }
            Student student = service.findStudentByName(studentNameTextField.getText());
            Tema tema = service.findHomeworkByDescription(descriptionTextField.getText());
            if (tema!=null && student != null) {
                String stringId = "" + student.getId() + "-" + tema.getId();
                Optional<Nota> nota = service.findNota(stringId);
                if (!nota.isPresent())
                    throw new ValidationException("Nota cautata pentru studentul cu tema respectiva, nu exista!");
                service.adaugaNota(new Nota(stringId, student, tema, Integer.parseInt(valoareTextField.getText())),
                        Integer.parseInt(saptamanaTextField.getText()), observatiiTextField.getText());
                reloadTableView();
                showMessage(Alert.AlertType.INFORMATION, "Operatiune completa!", "Nota a fost modificata!");
            } else
                throw new ValidationException("Nume student sau id tema invalid!");
        } catch (ValidationException | RepositoryException e) {
            showErrorMessage(e.getMessage());
        } catch (NumberFormatException e) {
            showErrorMessage("Id-ul temei, nota si saptamana trebuie sa fie numere naturale!");
        }
    }

    public void deleteNota(ActionEvent actionEvent) {
        try {
            if (!admin){
                throw new ValidationException("Nu aveti drepturile necesare!");
            }
            Student student = service.findStudentByName(studentNameTextField.getText());
            Tema tema = service.findHomeworkByDescription(descriptionTextField.getText());
            if (tema!=null && student != null) {
                String stringId = "" + student.getId() + "-" + tema.getId();
                Optional<Nota> nota = service.findNota(stringId);
                if (!nota.isPresent())
                    throw new ValidationException("Nota pentru acest student la aceasta tema nu exista!");
                service.stergeNota(stringId);
                reloadTableView();
                showMessage(Alert.AlertType.INFORMATION, "Operatiune completa!", "Nota a fost stearsa!");
            } else
                throw new ValidationException("Nume student sau id tema invalid!");
        } catch (ValidationException | RepositoryException e) {
            showErrorMessage(e.getMessage());
        } catch (NumberFormatException e) {
            showErrorMessage("Id-ul temei, nota si saptamana trebuie sa fie numere naturale!");
        }
    }

    public void setDrepturi(boolean admin){
        this.admin=admin;
    }
}
