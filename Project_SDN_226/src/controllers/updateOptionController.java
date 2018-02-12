package controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import entities.Candidate;
import entities.Option;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import services.StaffManager;
import utils.ListEvent;
import utils.ListEventType;
import utils.Observable;
import utils.Observer;
import validators.RepositoryException;
import validators.ValidationException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class updateOptionController implements Observable<Option> {

    protected ArrayList<Observer<Option>> observersList = new ArrayList<>();
    StaffManager service;
    @FXML
    private TableColumn<Option, String> idColumn;

    @FXML
    private TableColumn<Option, String> nameColumn;

    @FXML
    private TableColumn<Option, Integer> idCandidateColumn;

    @FXML
    private TableColumn<Option, Integer> idDepartmentColumn;

    @FXML
    private TableColumn<Option, String> departmentColumn;


    @FXML
    private TableColumn<Option, String> languageColumn;


    @FXML
    private TableColumn<Option, Integer> priorityColumn;

    @FXML
    private TableView<Option> tableView;

    @FXML
    private ComboBox<Candidate> candidatescombo;

    @FXML
    private JFXButton up;

    @FXML
    private JFXButton down;

    @FXML
    private JFXButton change;

    @FXML
    private JFXTextField priorityText;

    @FXML
    public void initialize() {

        idColumn.setCellValueFactory(new PropertyValueFactory<Option, String>("Id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<Option, String>("Name"));
        departmentColumn.setCellValueFactory(new PropertyValueFactory<Option, String>("Department"));
        languageColumn.setCellValueFactory(new PropertyValueFactory<Option, String>("Language"));
        priorityColumn.setCellValueFactory(new PropertyValueFactory<Option, Integer>("Priority"));
        idCandidateColumn.setCellValueFactory(new PropertyValueFactory<Option, Integer>("IdCandidate"));
        idDepartmentColumn.setCellValueFactory(new PropertyValueFactory<Option, Integer>("IdDepartment"));

        tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldvalue, newValue) -> {
            if (newValue == null)
                priorityText.setText("");
            else
                priorityText.setText(newValue.getPriority().toString());
        });

        Callback<ListView<Candidate>, ListCell<Candidate>> factory = lv -> new ListCell<Candidate>() {

            @Override
            protected void updateItem(Candidate item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.getId() + " - " + item.getName());

            }

        };

        candidatescombo.setCellFactory(factory);
        candidatescombo.setButtonCell(factory.call(null));

    }


    /**
     * Load data in table
     */
    public void setUpOptions() {


        ObservableList<Option> options = FXCollections.observableArrayList();
        this.service.filterOptionIDC(candidatescombo.getSelectionModel().getSelectedItem().getId()).forEach(options::add);
        Collections.sort(options, Comparator.comparing(Option::getPriority));
        tableView.setItems(options);


        for (int i = 0; i < options.size(); i++) {
            Integer DepartmentID = options.get(i).getIdDepartment();
            String department = service.findDepartment(DepartmentID).get().getName();

            Option item = options.get(i);
            item.setDepartment(department);

            options.set(i, item);
        }


    }

    /**
     * Set up things for optionsController
     *
     * @param staff
     */
    public void seteaza(StaffManager staff) {
        this.service = staff;

        ObservableList<Candidate> candidates = FXCollections.observableArrayList();
        this.service.showCandidates().forEach(candidates::add);
        candidatescombo.setItems(candidates);
        candidatescombo.getSelectionModel().selectFirst();


        setUpOptions();


    }


    public void handlerUP(ActionEvent actionEvent) {
        if (!tableView.getItems().isEmpty() && tableView.getSelectionModel().getSelectedItem() != null) {
            Option o = tableView.getItems().get(0);
            if (tableView.getSelectionModel().getSelectedItem() != o) {
                Option o1 = tableView.getSelectionModel().getSelectedItem();
                Integer index = tableView.getSelectionModel().getSelectedIndex();

                Option o2 = tableView.getItems().get(index - 1);

                service.updateOption(o2.getIdCandidate(), o2.getIdDepartment(), o2.getLanguage(), -1);
                service.updateOption(o1.getIdCandidate(), o1.getIdDepartment(), o1.getLanguage(), o2.getPriority());
                service.updateOption(o2.getIdCandidate(), o2.getIdDepartment(), o2.getLanguage(), o1.getPriority());

                setUpOptions();

                tableView.getSelectionModel().select(index - 1);
                ListEvent<Option> ev = createEvent(ListEventType.ADD, null, this.service.showOptions());
                notifyObservers(ev);
            }
        }

    }

    public void handlerDOWN(ActionEvent actionEvent) {
        if (!tableView.getItems().isEmpty() && tableView.getSelectionModel().getSelectedItem() != null) {
            int last = tableView.getItems().size() - 1;
            Option o = tableView.getItems().get(last);
            if (tableView.getSelectionModel().getSelectedItem() != o) {
                Option o1 = tableView.getSelectionModel().getSelectedItem();
                Integer index = tableView.getSelectionModel().getSelectedIndex();

                Option o2 = tableView.getItems().get(index + 1);

                service.updateOption(o2.getIdCandidate(), o2.getIdDepartment(), o2.getLanguage(), -1);
                service.updateOption(o1.getIdCandidate(), o1.getIdDepartment(), o1.getLanguage(), o2.getPriority());
                service.updateOption(o2.getIdCandidate(), o2.getIdDepartment(), o2.getLanguage(), o1.getPriority());

                setUpOptions();
                tableView.getSelectionModel().select(index + 1);
                ListEvent<Option> ev = createEvent(ListEventType.ADD, null, this.service.showOptions());
                notifyObservers(ev);
            }
        }


    }

    public void handleCombo(ActionEvent actionEvent) {
        setUpOptions();
    }

    public void handleChange(ActionEvent actionEvent) {
        try {
            if (tableView.getSelectionModel().getSelectedItem() != null && priorityText.getText() != null && priorityText.getText() != "") {
                service.updateOption(tableView.getSelectionModel().getSelectedItem().getIdCandidate(), tableView.getSelectionModel().getSelectedItem().getIdDepartment(), tableView.getSelectionModel().getSelectedItem().getLanguage(), Integer.parseInt(priorityText.getText()));
                setUpOptions();
                ListEvent<Option> ev = createEvent(ListEventType.ADD, null, this.service.showOptions());
                notifyObservers(ev);
            }
        } catch (NumberFormatException e) {
            showErrorMessage("Error Update Option", "ID / Priority must be integers");

        } catch (RepositoryException | ValidationException e) {
            showErrorMessage("Error Update Option", e.getMessage());
        }


    }


    static void showErrorMessage(String title, String content) {
        Alert message = new Alert(Alert.AlertType.ERROR);
        message.setTitle(title);
        message.setContentText(content);
        message.showAndWait();
    }

    @Override
    public void addObserver(Observer<Option> o) {
        observersList.add(o);
    }

    /**
     * Remove an observer
     *
     * @param o the observer
     */
    @Override
    public void removeObserver(Observer<Option> o) {
        observersList.remove(o);
    }

    /**
     * Notify all observers
     *
     * @param event
     */
    @Override
    public void notifyObservers(ListEvent<Option> event) {
        observersList.forEach(x -> x.notifyEvent(event));
    }

    /**
     * Create an event
     *
     * @param type
     * @param elem
     * @param l
     * @param <E>
     * @return
     */
    private <E> ListEvent<E> createEvent(ListEventType type, final E elem, final Iterable<E> l) {
        return new ListEvent<E>(type) {
            @Override
            public Iterable<E> getList() {
                return l;
            }

            @Override
            public E getElement() {
                return elem;
            }
        };
    }

}
