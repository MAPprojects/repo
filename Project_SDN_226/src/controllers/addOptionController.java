package controllers;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import entities.Candidate;
import entities.Department;
import entities.Option;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import services.StaffManager;
import utils.ListEvent;
import utils.ListEventType;
import utils.Observable;
import utils.Observer;
import validators.RepositoryException;
import validators.ValidationException;
import java.util.ArrayList;

public class addOptionController implements Observable<Option>{
    protected ArrayList<Observer<Option>> observersList=new ArrayList<>();
    StaffManager service;

    @FXML
    private JFXComboBox<Candidate> candidateCombo;


    @FXML
    private JFXComboBox<Department> departmentCombo;


    @FXML
    private JFXComboBox<String> languageCombo;


    @FXML
    private JFXTextField priorityTxt;

    /**
     * Initialize elements from fxml
     */
    @FXML
    public void initialize(){
        Callback<ListView<Candidate>, ListCell<Candidate>> factory = lv -> new ListCell<Candidate>() {

            @Override
            protected void updateItem(Candidate item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.getId()+ " - "+item.getName());
            }

        };

        candidateCombo.setCellFactory(factory);
        candidateCombo.setButtonCell(factory.call(null));


        Callback<ListView<Department>, ListCell<Department>> factory2 = lv -> new ListCell<Department>() {

            @Override
            protected void updateItem(Department item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" :item.getId()+ " - " + item.getName());
            }

        };

        departmentCombo.setCellFactory(factory2);
        departmentCombo.setButtonCell(factory2.call(null));



    }

    /**
     * Set up things for addOptionController
     * @param staff
     */
    public void seteaza(StaffManager staff) {
        this.service = staff;
        setUp();

    }

    /**
     * Load data in page
     */
    private void setUp() {
        ObservableList<Candidate> candidates = FXCollections.observableArrayList();
        this.service.showCandidates().forEach(candidates::add);
        candidateCombo.setItems(candidates);

        ObservableList<Department> departments = FXCollections.observableArrayList();
        this.service.showDepartments().forEach(departments::add);
        departmentCombo.setItems(departments);

        ObservableList<String> languages = FXCollections.observableArrayList();
        languages.addAll("romanian" ,"english" , "german" , "hungarian");
        languageCombo.setItems(languages);

        languageCombo.getSelectionModel().selectFirst();
        candidateCombo.getSelectionModel().selectFirst();
        departmentCombo.getSelectionModel().selectFirst();



    }

    /**
     * Handler for adding an option.
     * @param actionEvent
     */
    public void handleAddButton(ActionEvent actionEvent) {
        String  language;
        Integer priority=0 , idCandidate=0 , idDepartment=0;

        try {

           idCandidate = candidateCombo.getSelectionModel().getSelectedItem().getId();
           idDepartment= departmentCombo.getSelectionModel().getSelectedItem().getId();
           language = languageCombo.getSelectionModel().getSelectedItem();
           priority = Integer.parseInt(priorityTxt.getText());
           this.service.addOption(idCandidate,idDepartment,language,priority);
           showMessage(Alert.AlertType.CONFIRMATION,"Add" , "Option succesfully added !");

        }catch(NumberFormatException e){
            showErrorMessage("Error Add Option", "ID / Priority must be integers");

        }
        catch (RepositoryException | ValidationException e )
        {
            showErrorMessage( "Error Add Option" , e.getMessage());
        }
        ListEvent<Option> ev = createEvent(ListEventType.ADD, null, this.service.showOptions());
        notifyObservers(ev);
    }

    /**
     *  Show an Error Alert with a given title and content
     *
     * @param title
     * @param content
     */
    static void showErrorMessage(String title,String content){
        Alert message=new Alert(Alert.AlertType.ERROR);
        message.setTitle(title);
        message.setContentText(content);
        message.showAndWait();
    }

    /**
     * Show a Message Alert with a given type , header and text.
     *
     * @param type
     * @param header
     * @param text
     */
    static void showMessage(Alert.AlertType type, String header, String text){
        Alert message=new Alert(type);
        message.setHeaderText(header);
        message.setContentText(text);
        message.showAndWait();
    }


    /**
     * Add an observer
     * @param o the observer
     */
    @Override
    public void addObserver(Observer<Option> o) {
        observersList.add(o);
    }

    /**
     * Remove an observer
     * @param o the observer
     */
    @Override
    public void removeObserver(Observer<Option> o) {
        observersList.remove(o);
    }

    /**
     * Notify all observers
     * @param event
     */
    @Override
    public void notifyObservers(ListEvent<Option> event) {
        observersList.forEach(x->x.notifyEvent(event));
    }

    /**
     * Create an event
     * @param type
     * @param elem
     * @param l
     * @param <E>
     * @return
     */
    private <E> ListEvent<E> createEvent(ListEventType type, final E elem, final Iterable<E> l){
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
