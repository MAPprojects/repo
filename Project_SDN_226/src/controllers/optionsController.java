package controllers;

import com.jfoenix.controls.JFXTextField;
import entities.Option;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import services.StaffManager;
import utils.ListEvent;
import utils.Observer;
import utils.pdf;
import validators.RepositoryException;
import validators.ValidationException;

import java.io.IOException;


public class optionsController implements  Observer<Option>{
    StaffManager service;

    private IntegerProperty limit = new SimpleIntegerProperty(10);
    ObservableList<Option> lista;

    @FXML
    private Pagination pagination;

    @FXML
    private Label depTxt;

    @FXML
    private Label candTxt;

    @FXML
    private JFXTextField searchField;

    @FXML
    private JFXTextField idTxt;

    @FXML
    private JFXTextField nameTxt;

    @FXML
    private JFXTextField departmentTxt;

    @FXML
    private JFXTextField languageTxt;

    @FXML
    private JFXTextField priorityTxt;


    @FXML
    private TableColumn<Option, String> idColumn;

    @FXML
    private TableColumn<Option, String> nameColumn;

    @FXML
    private TableColumn<Option , Integer> idCandidateColumn;

    @FXML
    private TableColumn<Option , Integer> idDepartmentColumn;

    @FXML
    private TableColumn<Option, String> departmentColumn;


    @FXML
    private TableColumn<Option, String> languageColumn;


    @FXML
    private TableColumn<Option, Integer> priorityColumn;

    @FXML
    private TableView<Option> tableView;

    @FXML
    private RadioButton nameRadio;

    @FXML
    private RadioButton departmentRadio;

    @FXML
    public void  initialize(){
        lista = FXCollections.observableArrayList();

        idColumn.setCellValueFactory(new PropertyValueFactory<Option, String>("Id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<Option, String>("Name"));
        departmentColumn.setCellValueFactory(new PropertyValueFactory<Option, String>("Department"));
        languageColumn.setCellValueFactory(new PropertyValueFactory<Option, String>("Language"));
        priorityColumn.setCellValueFactory(new PropertyValueFactory<Option, Integer>("Priority"));
        idCandidateColumn.setCellValueFactory(new PropertyValueFactory<Option, Integer>("IdCandidate"));
        idDepartmentColumn.setCellValueFactory(new PropertyValueFactory<Option, Integer>("IdDepartment"));
        tableView.getSelectionModel().selectedItemProperty().addListener((observable,oldvalue,newValue)->{
            if (newValue == null)
                setFields(null);
            else
                setFields(newValue);
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

        tableView.setItems(lista);
    }

    /**
     * Reinitialize table for current page
     * @param index
     * @param limit
     */
    public void changeTableView(int index, int limit) {
        // int newIndex = index * limit;

        tableView.getItems().clear();
        tableView.setItems(null);
        lista.clear();

        service.showNextValuesOptions(index,limit).forEach(lista::add);
        //System.out.println(lista.size());

        for(int i = 0;i < lista.size();i++){
            Integer CandidateID = lista.get(i).getIdCandidate();
            String name = this.service.findCandidate(CandidateID).get().getName();


            Integer DepartmentID = lista.get(i).getIdDepartment();
            String department = service.findDepartment(DepartmentID).get().getName();

            Option item = lista.get(i);
            item.setName(name);
            item.setDepartment(department);

            lista.set(i,item);
        }
        tableView.setItems(lista);
    }

    /**
     * Load paginated data in tabel
     */
    public void init() {
        resetPage();
        pagination.setCurrentPageIndex(0);
        changeTableView(0, limit.get());
    }


    /**
     * Resets page count
     */
    public void resetPage() {
        int size = (int)service.ShowSizeOptions();
        int pageCount = size/limit.getValue() + 1;
        pagination.setPageCount(pageCount);
    }

    /**
     * Set fields with data
     * @param c
     */
    private void setFields(Option c) {
        if (c == null){
            idTxt.setText("");
            nameTxt.setText("");
            departmentTxt.setText("");
            languageTxt.setText("");
            priorityTxt.setText("");
            candTxt.setText("-");
            depTxt.setText("-");

        }
        else
        {
            idTxt.setText(String.valueOf(c.getId()));
            nameTxt.setText(this.service.findCandidate(c.getIdCandidate()).get().getName());
            departmentTxt.setText(this.service.findDepartment(c.getIdDepartment()).get().getName());
            languageTxt.setText(c.getLanguage());
            priorityTxt.setText(String.valueOf(c.getPriority()));
            candTxt.setText(String.valueOf(c.getIdCandidate()));
            depTxt.setText(String.valueOf(c.getIdDepartment()));
        }
    }

    /**
     *
     * Load data in table
     */
    public void setUpOptions() {

    init();

    }

    /**
     * Set up things for optionsController
     * @param staff
     */
    public void seteaza(StaffManager staff) {
        this.service = staff;
        setUpOptions();

    }

    /**
     * Handler for adding option
     * @param actionEvent
     */
    public void handleAddButton(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/administrator/addOption.fxml"));
        Parent root = null;
        try {
            root = (Parent) loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        addOptionController dc = loader.getController();
        dc.seteaza(service);
        dc.addObserver(this);


        Stage newStage = new Stage();
        newStage.getIcons().add(new Image("images\\icon.png"));
        newStage.initModality(Modality.APPLICATION_MODAL);
        Scene newScene = new Scene(root);
        newStage.initOwner(this.tableView.getScene().getWindow());


        newStage.setScene(newScene);
        newStage.show();

    }

    /**
     * handler for deleting option
     * @param actionEvent
     */
    public void handleDeleteButton(ActionEvent actionEvent) {
        Integer idCandidate=0 , idDepartment=0;
        String lang;
        try {

            idCandidate = Integer.parseInt(candTxt.getText());
            idDepartment = Integer.parseInt(depTxt.getText());
            lang = languageTxt.getText();

            this.service.deleteOption(idCandidate,idDepartment,lang);
            setUpOptions();
            showMessage(Alert.AlertType.CONFIRMATION,"Delete" , "Option succesfully deleted !");

        }catch(NumberFormatException e){
            showErrorMessage("Error Delete Option", "ID / Priority must be integers");

        }
        catch (RepositoryException | ValidationException e )
        {
            showErrorMessage( "Error Delete Option" , e.getMessage());
        }
    }

    /**
     * Handler for updating options
     * @param actionEvent
     */
    public void handleUpdateButton(ActionEvent actionEvent) {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/administrator/updateOptions.fxml"));
        Parent root = null;
        try {
            root = (Parent) loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        updateOptionController dc = loader.getController();
        dc.seteaza(service);
        dc.addObserver(this);



        Stage newStage = new Stage();
        newStage.getIcons().add(new Image("images\\icon.png"));
        newStage.initModality(Modality.APPLICATION_MODAL);
        Scene newScene = new Scene(root);
        newStage.initOwner(this.tableView.getScene().getWindow());


        newStage.setScene(newScene);
        newStage.show();



    }

    /**
     * Handler for searching options
     * @param keyEvent
     */
    public void handleSearchField(KeyEvent keyEvent) {
        ObservableList<Option> lista_aux = FXCollections.observableArrayList();
        String input = searchField.getText();

        if(nameRadio.isSelected()) {
            this.service.filterOptionName(input).forEach(lista_aux::add);
        }
        else
        {
            this.service.filterOptionDepartment(input).forEach(lista_aux::add);
        }

        for(int i = 0;i < lista_aux.size();i++){
            Integer CandidateID = lista_aux.get(i).getIdCandidate();
            String name = this.service.findCandidate(CandidateID).get().getName();


            Integer DepartmentID = lista_aux.get(i).getIdDepartment();
            String department = service.findDepartment(DepartmentID).get().getName();

            Option item = lista_aux.get(i);
            item.setName(name);
            item.setDepartment(department);

            lista_aux.set(i,item);
        }
        tableView.setItems(lista_aux);
    }

    /**
     * Handler for searching options by Name
     * @param actionEvent
     */
    public void handleNameRadio(ActionEvent actionEvent) {
        departmentRadio.setSelected(false);

        if(!nameRadio.isSelected())
            nameRadio.setSelected(true);
        else
            setUpOptions();
        searchField.clear();

    }

    /**
     * Handler for searching options by department
     * @param actionEvent
     */
    public void handleDepartmentRadio(ActionEvent actionEvent) {
        nameRadio.setSelected(false);

        if(!departmentRadio.isSelected())
            departmentRadio.setSelected(true);
        else
            setUpOptions();

        searchField.clear();

    }

    /**
     * Show an Alert Error
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
     * Reload table when event occurs
     * @param e
     */
    @Override
    public void notifyEvent(ListEvent<Option> e) {
        setUpOptions();
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
     * Handler for generating a pdf
     * @param actionEvent
     */
    public void handleGenerate(ActionEvent actionEvent) {
        pdf report = new pdf("a.pdf",service);
        try {
            report.makePDF();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * Show a Message Alert
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

}
