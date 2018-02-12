package controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import entities.Candidate;
import entities.Option;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import services.StaffManager;
import utils.SendMail;
import validators.RepositoryException;
import validators.ValidationException;


public class canditatesController {
    StaffManager service;


    ObservableList<Candidate> lista;

    private IntegerProperty limit = new SimpleIntegerProperty(10);

    @FXML
    private JFXButton sendMail;

    @FXML
    private Pagination pagination;


    @FXML
    private JFXTextField textFieldSearch;

    @FXML
    private JFXTextField idCandidate;

    @FXML
    private JFXTextField nameCandidate;

    @FXML
    private JFXTextField phoneCandidate;

    @FXML
    private JFXTextField emailCandidate;

    @FXML
    private TableColumn<Candidate, Integer> idColumn;

    @FXML
    private TableColumn<Candidate, String> nameColumn;

    @FXML
    private TableColumn<Candidate, String> emailColumn;

    @FXML
    public TableView<Candidate> tableView;

    /**
     * Initialize elements
     */
    @FXML
    public void initialize() {
        sendMail.setDisable(true);
        lista = FXCollections.observableArrayList();

        idColumn.setCellValueFactory(new PropertyValueFactory<Candidate, Integer>("Id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<Candidate, String>("Name"));
        emailColumn.setCellValueFactory((new PropertyValueFactory<Candidate, String>("Email")));

        tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldvalue, newValue) -> {
            if (newValue == null){
                setFields(null);
                sendMail.setDisable(true);
            }
            else
            {
                setFields(newValue);
                sendMail.setDisable(false);
            }
        });

        tableView.setItems(lista);


        pagination.currentPageIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                changeTableView(newValue.intValue(), limit.get());
            }

        });

        limit.addListener(new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                changeTableView(pagination.getCurrentPageIndex(), newValue.intValue());
            }

        });

    }

    /**
     * Change content for table view according to number of max numbers of elements / page
     * @param index
     * @param limit
     */
    public void changeTableView(int index, int limit) {
        // int newIndex = index * limit;

        tableView.getItems().clear();
        tableView.setItems(null);
        lista.clear();

        service.showNextValuesCandidates(index,limit).forEach(lista::add);
        tableView.setItems(lista);

    }

    /**
     * Initialize table view when starting Candidates page
     */
    public void init() {
        resetPage();
        pagination.setCurrentPageIndex(0);
        changeTableView(0, limit.get());
    }

    /**
     * Resets curent number of pages according to size of repo
     */
    public void resetPage() {
        int size = (int)service.ShowSizeCandidates();
        int pageCount = size/limit.getValue() + 1;
        pagination.setPageCount(pageCount);
    }

    /**
     * Set fields according to current object
     * @param c
     */
    private void setFields(Candidate c) {
        if (c == null) {
            idCandidate.setText("");
            nameCandidate.setText("");
            phoneCandidate.setText("");
            emailCandidate.setText("");
        } else {
            idCandidate.setText(String.valueOf(c.getId()));
            nameCandidate.setText(c.getName());
            phoneCandidate.setText(c.getPhone());
            emailCandidate.setText(c.getEmail());
        }
    }


    /**
     * Load candidates in table view
     */
    public void setUpCandidates() {
        init();

    }


    /**
     * Set up controller for candidatesController
     * @param staff
     */
    public void seteaza(StaffManager staff) {
        this.service = staff;
        setUpCandidates();

    }

    /**
     * Handler for searching candidates
     * @param keyEvent
     */
    public void handleSearch(KeyEvent keyEvent) {

        ObservableList<Candidate> lista = FXCollections.observableArrayList();
        this.service.filterCandidates(textFieldSearch.getText()).forEach(lista::add);
        this.tableView.setItems(lista);

    }


    /**
     * Handler for adding a candidate
     * @param actionEvent
     */
    public void handleAddButton(ActionEvent actionEvent) {
        Integer id = 0;
        String name, phone, email;
        try {
            id = Integer.parseInt(idCandidate.getText());
            name = nameCandidate.getText();
            phone = phoneCandidate.getText();
            email = emailCandidate.getText();

            this.service.addCandidate(id, name, phone, email);
            setUpCandidates();
            showMessage(Alert.AlertType.CONFIRMATION,"Add","Candidate succesfully added ! ");


        } catch (NumberFormatException e) {
            showErrorMessage("Error Add Candidate", "ID must be integer");
        } catch (RepositoryException | ValidationException e) {
            showErrorMessage("Error Add Candidate", e.getMessage());
        }

    }


    /**
     * Handler for deleting a candidate
     * @param actionEvent
     */
    public void handleDeleteButton(ActionEvent actionEvent) {
        Integer id = 0;
        try {
            id = Integer.parseInt(idCandidate.getText());

            this.service.deleteCandidate(id);
            setUpCandidates();
            showMessage(Alert.AlertType.CONFIRMATION , "Delete" , "Candidate succesfully deleted !!");


        } catch (NumberFormatException e) {
            showErrorMessage("Error Delete Candidate", "ID must be integer");
        } catch (RepositoryException | ValidationException e) {
            showErrorMessage("Error Delete Candidate", e.getMessage());
        }
    }


    /**
     * Handler for updating a candidate
     * @param actionEvent
     */
    public void handleUpdateButton(ActionEvent actionEvent) {
        Integer id = 0;
        String name, phone, email;
        try {
            id = Integer.parseInt(idCandidate.getText());
            name = nameCandidate.getText();
            phone = phoneCandidate.getText();
            email = emailCandidate.getText();

            this.service.updateCandidate(id, name, phone, email);
            setUpCandidates();
            showMessage(Alert.AlertType.CONFIRMATION , "Update" , "Candidate succesfully updated !!");


        } catch (NumberFormatException e) {
            showErrorMessage("Error Update Candidate", "ID must be integer");
        } catch (RepositoryException | ValidationException e) {
            showErrorMessage("Error Update Candidate", e.getMessage());
        }

    }

    /**
     * Show an Error Alert with a given title and text
     * @param title
     * @param content
     */
    static void showErrorMessage(String title, String content) {
        Alert message = new Alert(Alert.AlertType.ERROR);
        message.setTitle(title);
        message.setContentText(content);
        message.showAndWait();
    }

    /**
     * Handler for changing pagination limit
     * @param actionEvent
     */
    public void changeLimit(ActionEvent actionEvent) {
        TextField txt = (TextField) actionEvent.getSource();
        if (txt != null) {
            try {
                int i = Integer.parseInt(txt.getText());
                if(i!=0) {
                    limit.set(i);

                    resetPage();
                }

            } catch (NumberFormatException nfe) {
                System.err.println("NFE error");
            }
        }

    }

    /**
     * Show an Alert of a given type
     * @param type
     * @param header
     * @param text
     */
    static void showMessage(Alert.AlertType type, String header, String text){
        Alert message=new Alert(type);
        DialogPane d = message.getDialogPane();
        message.setHeaderText(header);
        message.setContentText(text);
        message.showAndWait();
    }

    /**
     * Handler for sending emails to a certain candidate
     * @param actionEvent
     */
    public void handlerSendMail(ActionEvent actionEvent) {
       String rez = "Optiunile alese de dumneavoastra ce se afla  in baza noastra de date sunt urmatoarele: " + '\r' + '\r';
       int count  = 1;

       for(Option o : this.service.filterOptionIDC(Integer.parseInt(idCandidate.getText()))){
           rez = rez + count+ ". " + " ID Optiune :  " + o.getId() + " ,  Sectie : " + service.findDepartment(o.getIdDepartment()).get().getName() + " ,  Limba: " + o.getLanguage() + " ,  Prioritate : " + o.getPriority() + '\r';
           count = count + 1;
       }

        SendMail s = new SendMail(emailCandidate.getText(),"Enrollment options",rez);
    }
}
