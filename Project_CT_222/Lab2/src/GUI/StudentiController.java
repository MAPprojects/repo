package GUI;
import Domain.Studenti;
import Service.Service;
import Utils.ListEvent;
import Utils.Observer;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.skin.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class StudentiController implements Observer<ListEvent> {
private Service service;

    @FXML
    private TableColumn deleteColumn;
    @FXML
    private TableView<Studenti> tableView;

    @FXML
    private ObservableList<Studenti> model;

    @FXML
    private TextField id;
    @FXML
    private TextField nume;
    @FXML
    private TextField email;
    @FXML
    private TextField grupa;
    @FXML
    private TextField profesor;
    @FXML
    private TextField filter;

    @FXML
    private Stage editStage;

    @FXML
    private Button addButton;

    @FXML
    private Button updateButton;

    @FXML
    private Button clearButton;


    @FXML
    private final static int rowsPerPage=14;


    @FXML
    public TableColumn<Studenti, String> colNume;
    public TableColumn<Studenti, Integer> colGrupa;
    public TableColumn<Studenti, String> colEmail;
    public TableColumn<Studenti, String> colCadruDidactic;

    @FXML
    public Pagination pagination=new Pagination(5,0);

    public ComboBox comboStudenti,comboTeme, comboNote;
    public StudentiController(){}

    public void onEnterNume(ActionEvent ae){
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                grupa.requestFocus();
            }
        });
    }
    public void onEnterGrupa(ActionEvent ae){
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                email.requestFocus();
            }
        });
    }

    public void onEnterEmail(ActionEvent ae){
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                profesor.requestFocus();
            }
        });
    }



    public void setService(Service service,Stage stage) {
        this.service = service;
        this.editStage=stage;
        this.service.addObserver(this);
        loadDataHandler();
    }
    private Node createPage(int pageIndex){
        int fromIndex=pageIndex*rowsPerPage;
        int toIndex=Math.min(fromIndex+rowsPerPage,model.size());
        tableView.setItems(FXCollections.observableArrayList(model.subList(fromIndex,toIndex)));
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        return new BorderPane(tableView);
    }

    public void initialize(){
        id.setDisable(true);
        id.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    id.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        grupa.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    grupa.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        id.setPromptText("Id: only numbers");
        nume.setPromptText("Nume");
        grupa.setPromptText("Grupa: only numbers");
        email.setPromptText("Email");
        profesor.setPromptText("Profesor");
        filter.setPromptText("Search");
        colNume.setCellValueFactory(new PropertyValueFactory<>("nume"));
        colGrupa.setCellValueFactory(new PropertyValueFactory<>("grupa"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colCadruDidactic.setCellValueFactory(new PropertyValueFactory<>("CadruDidactic"));
        addDeleteButtons(deleteColumn);
        tableView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showStudentiDetails((Studenti) newValue));

        comboStudenti.getSelectionModel().selectFirst();

        filter.setOnKeyReleased(event -> {
                    List<Studenti> rezultat;
                    if(comboStudenti.getSelectionModel().getSelectedItem().equals("tip search"))
                        rezultat = service.getStudInLista();
                    else{
                    if(!filter.getText().equals("")){
                    if (comboStudenti.getSelectionModel().getSelectedItem().equals("grupa")) {
                        try{
                        rezultat = service.filterGrupa(Integer.parseInt(filter.getText()));
                        }catch (Exception e){
                            rezultat = service.getStudInLista();
                        }
                    } else if (comboStudenti.getSelectionModel().getSelectedItem().equals("email")) {
                        rezultat = service.filterEmail(filter.getText());

                    } else if (comboStudenti.getSelectionModel().getSelectedItem().equals("cadru didactic")) {
                        rezultat = service.filterCadruDidactic(filter.getText());
                    }
                    else if(comboStudenti.getSelectionModel().getSelectedItem().equals("nume")){
                        rezultat=service.filterNume(filter.getText());
                    }
                        else
                        rezultat = service.getStudInLista();
                    }
                    else {
                        rezultat = service.getStudInLista();
                    }
                    }


                    model.setAll(rezultat);
                    //Integer lol=pagination.getCurrentPageIndex();
                    if(model.size()%14==0)
                        pagination.setPageCount(model.size()/rowsPerPage);
                    else
                        pagination.setPageCount(model.size()/rowsPerPage+1);
                    pagination.setPageFactory(this::createPage);
                   // pagination.setCurrentPageIndex(lol);
                    model.setAll(service.getStudInLista());

        });

        }

    private void addDeleteButtons(TableColumn col){
        col.setCellFactory(new Callback<TableColumn,TableCell>(){
            @Override
            public TableCell call(TableColumn param){
                StudentiButtonCell btn=new StudentiButtonCell(service,StudentiController.this);
                return btn;
            }
        });
        ObservableList<String> listCombo1 = FXCollections.observableArrayList("tip search","nume","grupa","email","cadru didactic");
        comboStudenti.setItems(listCombo1);
    }

    private void showStudentiDetails(Studenti newValue) {
        if (newValue == null)
            clearFields();
        else {
            id.setText(String.valueOf(newValue.getId()));
            nume.setText(newValue.getNume());
            grupa.setText(String.valueOf(newValue.getGrupa()));
            email.setText(newValue.getEmail());
            profesor.setText(newValue.getCadruDidactic());
        }
    }

    public void clearFields() {
        id.setText("");
        nume.setText("");
        grupa.setText("");
        email.setText("");
        profesor.setText("");
    }

    @FXML
    private void clearTFields() {
        id.setText("");
        nume.setText("");
        grupa.setText("");
        email.setText("");
        profesor.setText("");
    }

    @FXML
    private void loadDataHandler() {
        List<Studenti> list = service.getStudInLista();
        model = FXCollections.observableArrayList(list);
        if(model.size()%14==0)
            pagination.setPageCount(model.size()/rowsPerPage);
        else
            pagination.setPageCount(model.size()/rowsPerPage+1);
        pagination.setPageFactory(this::createPage);
    }


    public void addButtonHandler() {
        if(nume.getText().equals("")||email.getText().equals("")||grupa.getText().equals("")||profesor.getText().equals("")) {
            showErrorMessage("Completati toate fieldurile");

        }
        else{
        try {
            service.addStudent( nume.getText(), Integer.parseInt(grupa.getText()), email.getText(), profesor.getText());
            // loadStudenti();
        } catch (Exception e) {
            showErrorMessage(e.toString());
        }}

    }

    @FXML
    public void filtrareStudHandler(){
        List<Studenti> rezultat ;
        if(!(filter.getText().equals(""))) {

            if (comboStudenti.getSelectionModel().getSelectedItem().equals("grupa")) {
                try{
                    rezultat = service.filterGrupa(Integer.parseInt(filter.getText()));
                }catch (Exception e){
                    rezultat = service.getStudInLista();
                }
            }
            else if(comboStudenti.getSelectionModel().getSelectedItem().equals("email")) {
                rezultat = service.filterEmail(filter.getText());
            }
            else if(comboStudenti.getSelectionModel().getSelectedItem().equals("cadru didactic")) {
                rezultat = service.filterCadruDidactic(filter.getText());
            }
            else if(comboStudenti.getSelectionModel().getSelectedItem().equals("nume")){
                rezultat=service.filterNume(filter.getText());
            }
            else{
                rezultat=service.getStudInLista();
            }
        }
        else
            rezultat=service.getStudInLista();

            model.setAll(rezultat);
        Integer lol=pagination.getCurrentPageIndex();
        if(model.size()%14==0)
            pagination.setPageCount(model.size()/rowsPerPage);
        else
            pagination.setPageCount(model.size()/rowsPerPage+1);
        pagination.setPageFactory(this::createPage);
        pagination.setCurrentPageIndex(lol);

    }

    private void showMessage(Alert.AlertType information, String s) {
        Alert message = new Alert(information);
        message.setHeaderText("");
        message.setContentText(s);
        message.showAndWait();
    }

    private void showErrorMessage(String text) {
        Alert message = new Alert(Alert.AlertType.ERROR);
        message.setTitle("Error Message");
        message.initOwner(editStage);
        message.initModality(Modality.APPLICATION_MODAL);
        message.setContentText(text);
        message.showAndWait();
    }

    public void updateButtonHandler() {
        Studenti student = (Studenti) tableView.getSelectionModel().getSelectedItem();

        try{
            if (student != null) {
                String gr=grupa.getText();
                String n=nume.getText();
                String c=profesor.getText();
                String e=email.getText();
                // if (Integer.parseInt(grupa.getText()) != student.getGrupa())
                // service.updateStudent(student.getId(), "grupa", grupa.getText());
                service.updateStudent(student.getId(), "grupa", gr);
                //if (!(nume.getText().equals(student.getNume())))
                //    service.updateStudet(student.getId(), "nume", nume.getText());
                service.updateStudent(student.getId(), "nume", n);
                // if (!(email.getText().equals(student.getEmail())))
                service.updateStudent(student.getId(), "e-mail", e);
                // service.updateStudent(student.getId(), "e-mail", email.getText());
                // if (!(profesor.getText().equals(student.getCadruDidactic())))
                // service.updateStudent(student.getId(), "cadru didactic", profesor.getText());
                service.updateStudent(student.getId(), "cadru didactic", c);
            } else {
                showErrorMessage("Field-urile sunt goale!");
            }
        }catch (Exception e){
            showErrorMessage(e.toString());
        }
        model.setAll(service.getStudInLista());
        Integer lol=pagination.getCurrentPageIndex();
        if(model.size()%14==0)
            pagination.setPageCount(model.size()/rowsPerPage);
        else
            pagination.setPageCount(model.size()/rowsPerPage+1);
        pagination.setPageFactory(this::createPage);
        pagination.setCurrentPageIndex(lol);
        tableView.refresh();
    }

    @Override
    public void notifyEvent(ListEvent e) {
        List<Studenti> list = service.getStudInLista();
        model.setAll(list);
        Integer lol=pagination.getCurrentPageIndex();
        if(model.size()%14==0)
            pagination.setPageCount(model.size()/rowsPerPage);
        else
            pagination.setPageCount(model.size()/rowsPerPage+1);
        pagination.setPageFactory(this::createPage);
        pagination.setCurrentPageIndex(lol);


    }
}

class StudentiButtonCell extends TableCell<Studenti,Boolean>{
    final Button cellButton=new Button("Delete");
    private Service service;
    private StudentiController studentiController;

    StudentiButtonCell(Service service,StudentiController ctrl){
        this.service=service;
        studentiController=ctrl;
        cellButton.getStyleClass().add("button");
        cellButton.setOnAction(new EventHandler<ActionEvent>(){
            public void handle(ActionEvent t){
                Studenti current=StudentiButtonCell.this.getTableView().getItems().get(StudentiButtonCell.this.getIndex());
                studentiController.clearFields();
                service.removeStudent(current.getId());
            }
        });
    }
    @Override
    protected void updateItem(Boolean t,boolean empty){
        super.updateItem(t,empty);
        if(!empty){
            setGraphic(cellButton);
        }
        else
        {

            setGraphic(null);
        }
    }
}
