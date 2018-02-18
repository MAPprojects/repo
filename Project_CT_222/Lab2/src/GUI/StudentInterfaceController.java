package GUI;

import Domain.NoteDTO;
import Service.Service;
import Utils.ListEvent;
import Utils.Observer;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.util.List;

public class StudentInterfaceController {
    public Service service;

    @FXML
    private TableView<NoteDTO> tableView;

    @FXML
    private ObservableList<NoteDTO> model;

    @FXML
    private Button login;
    @FXML
    public TableColumn<NoteDTO,String> colStudent;
    public TableColumn<NoteDTO,Integer> colGrupa ;
    public TableColumn deleteColumn;

    @FXML
    public Stage editStage;

    @FXML
    public TextField filter;

    @FXML
    public ComboBox<String> comboNote;

    @FXML
    private final static int rowsPerPage=14;

    @FXML
    public Pagination pagination=new Pagination(5,0);


    public void setService(Service service,Stage stage){
        this.service=service;
        this.editStage=stage;
        makeColumns(service.getTemeInLista().size(),tableView);

        loadDataHandler();
    }

    private Node createPage(int pageIndex){
        int fromIndex=pageIndex*rowsPerPage;
        int toIndex=Math.min(fromIndex+rowsPerPage,model.size());
        tableView.setItems(FXCollections.observableArrayList(model.subList(fromIndex,toIndex)));
        if(service.getTemeInLista().size()<=9)
            tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        return new BorderPane(tableView);
    }
    @FXML
    public void initialize() {

        filter.setPromptText("Search");
        colStudent.setCellValueFactory(new PropertyValueFactory<NoteDTO, String>("nume"));
        colGrupa.setCellValueFactory(new PropertyValueFactory<NoteDTO, Integer>("grupa"));
        ObservableList<String> listCombo1 = FXCollections.observableArrayList("tip search", "nume", "grupa");
        comboNote.setItems(listCombo1);
        comboNote.getSelectionModel().selectFirst();


        filter.setOnKeyReleased(event -> {
            List<NoteDTO> rezultat;
            if(comboNote.getSelectionModel().getSelectedItem().equals("tip search"))
                rezultat = service.getAllNoteDTO();
            else{
                if(!filter.getText().equals("")){
                    if (comboNote.getSelectionModel().getSelectedItem().equals("grupa")) {
                        try{
                            rezultat = service.filterDTOGrupa(Integer.parseInt(filter.getText()));
                        }catch (Exception e){
                            rezultat = service.getAllNoteDTO();
                        }

                    }
                    else if(comboNote.getSelectionModel().getSelectedItem().equals("nume")){
                        rezultat=service.filterDTONume(filter.getText());
                    }
                    else
                        rezultat = service.getAllNoteDTO();
                }
                else {
                    rezultat = service.getAllNoteDTO();
                }
            }


            model.setAll(rezultat);
            //Integer lol=pagination.getCurrentPageIndex();
            if(model.size()%12==0)
                pagination.setPageCount(model.size()/rowsPerPage);
            else
                pagination.setPageCount(model.size()/rowsPerPage+1);
            pagination.setPageFactory(this::createPage);
            // pagination.setCurrentPageIndex(lol);
            model.setAll(service.getAllNoteDTO());

        });

    }

    public void handleLogin(){
        try {
            FXMLLoader loader = new FXMLLoader();
            //loader.setLocation(getClass().getResource("GUI\\FXMLProiect.fxml"));
            loader.setLocation(getClass().getResource("LoginU.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            LoginController ctrl = loader.getController();
            ctrl.setService(service, editStage);
            editStage.setScene(scene);
        } catch (Exception e) {
            showErrorMessage(e.toString());
        }
    }
    public void makeColumns(int count, TableView<NoteDTO> tableView)
    {
        for (int m = 0; m < count; m++)
        {
            TableColumn<NoteDTO, String> column = new TableColumn<>("Tema: "+service.getTemeInLista().get(m).getId());
            Integer i=service.getTemeInLista().get(m).getId();
            column.setCellValueFactory(c->new SimpleStringProperty(Lambda(c.getValue(),i)));
            tableView.getColumns().add(column);
        }
    }
    public String Lambda(NoteDTO c,Integer i){
        if(c.getGrades().get(i)==null){
            return "";
        }
        else
            return " "+c.getGrades().get(i);
    }

    private void loadDataHandler() {

        List<NoteDTO> list = service.getAllNoteDTO();

        model = FXCollections.observableArrayList(list);
        if(model.size()%14==0)
            pagination.setPageCount(model.size()/rowsPerPage);
        else
            pagination.setPageCount(model.size()/rowsPerPage+1);
        pagination.setPageFactory(this::createPage);
    }


    public void filtrareNotaHandler(){
        List<NoteDTO> rezultat;
        if(comboNote.getSelectionModel().getSelectedItem().equals("tip filtrare"))
            rezultat = service.getAllNoteDTO();
        else{
            if(!filter.getText().equals("")){
                if (comboNote.getSelectionModel().getSelectedItem().equals("grupa")) {
                    try{
                        rezultat = service.filterDTOGrupa(Integer.parseInt(filter.getText()));
                    }catch (Exception e){
                        rezultat = service.getAllNoteDTO();
                    }

                }
                else if(comboNote.getSelectionModel().getSelectedItem().equals("nume")){
                    rezultat=service.filterDTONume(filter.getText());
                }
                else
                    rezultat = service.getAllNoteDTO();
            }
            else {
                rezultat = service.getAllNoteDTO();
            }
        }


        model.setAll(rezultat);
        //Integer lol=pagination.getCurrentPageIndex();
        if(model.size()%14==0)
            pagination.setPageCount(model.size()/rowsPerPage);
        else
            pagination.setPageCount(model.size()/rowsPerPage+1);
        pagination.setPageFactory(this::createPage);
        //pagination.setCurrentPageIndex(lol);
        model.setAll(service.getAllNoteDTO());

    }

    public void updateTable(){
        tableView.setColumnResizePolicy(new Callback<TableView.ResizeFeatures, Boolean>() {
            @Override
            public Boolean call(TableView.ResizeFeatures p) {
                return true;
            }
        });
    }

    private void showErrorMessage(String text) {
        Alert message = new Alert(Alert.AlertType.ERROR);
        message.setTitle("Error Message");
        message.initOwner(editStage);
        message.initModality(Modality.APPLICATION_MODAL);
        message.setContentText(text);
        message.showAndWait();
    }

}
