package GUI;

import Domain.Nota;
import Domain.NoteDTO;
import Domain.Studenti;
import Domain.Teme;
import Service.Service;
import Utils.ListEvent;
import Utils.Observer;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.util.ArrayList;
import java.util.List;

public class NoteController implements Observer<ListEvent> {
    public Service service;

    @FXML
    private TableView<NoteDTO> tableView;

    @FXML
    private ObservableList<NoteDTO> model;
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
    private final static int rowsPerPage=11;

    @FXML
    public Pagination pagination=new Pagination(5,0);


    public void setService(Service service,Stage stage){
        this.service=service;
        this.editStage=stage;
        this.service.addObserver(this);
        makeColumns(service.getTemeInLista().size(),tableView);

        loadDataHandler();
    }

    private Node createPage(int pageIndex){
        int fromIndex=pageIndex*rowsPerPage;
        int toIndex=Math.min(fromIndex+rowsPerPage,model.size());

        tableView.setItems(FXCollections.observableArrayList(model.subList(fromIndex,toIndex)));
        if(service.getTemeInLista().size()<=5)
            tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        return new BorderPane(tableView);
    }

    @FXML
    public void initialize(){

        filter.setPromptText("Search");
        colStudent.setCellValueFactory(new PropertyValueFactory<NoteDTO,String>("nume"));
        colGrupa.setCellValueFactory(new PropertyValueFactory<NoteDTO,Integer>("grupa"));
        addDeleteButtons(deleteColumn);
        ObservableList<String> listCombo1 = FXCollections.observableArrayList("tip search","nume","grupa");
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
            if(model.size()%11==0)
                pagination.setPageCount(model.size()/rowsPerPage);
            else
                pagination.setPageCount(model.size()/rowsPerPage+1);
            pagination.setPageFactory(this::createPage);
            // pagination.setCurrentPageIndex(lol);
            model.setAll(service.getAllNoteDTO());

        });

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
        if(model.size()%11==0)
            pagination.setPageCount(model.size()/rowsPerPage);
        else
            pagination.setPageCount(model.size()/rowsPerPage+1);
        pagination.setPageFactory(this::createPage);
    }

    private void addDeleteButtons(TableColumn col){
        col.setCellFactory(new Callback<TableColumn,TableCell>(){
            @Override
            public TableCell call(TableColumn param){
                NoteButtonCell btn=new NoteButtonCell(service,NoteController.this);
                return btn;
            }

        });

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
        if(model.size()%11==0)
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
    public void notifyEvent(ListEvent e) {

        List<NoteDTO> list = service.getAllNoteDTO();
        model.setAll(list);
        Integer lol=pagination.getCurrentPageIndex();
        if(model.size()%11==0)
            pagination.setPageCount(model.size()/rowsPerPage);
        else
            pagination.setPageCount(model.size()/rowsPerPage+1);
        pagination.setPageFactory(this::createPage);
        pagination.setCurrentPageIndex(lol);

    }



}
class NoteButtonCell extends TableCell<NoteDTO,Boolean> {
    final Button ucellButton=new Button("Update");
    final Button acellButton=new Button("Add");
    final HBox box=new HBox(ucellButton,acellButton);
    private Service service;
    private NoteController noteController;

    NoteButtonCell(Service service,NoteController ctrl){
        this.service=service;
        noteController=ctrl;
        ucellButton.getStyleClass().add("button");
        acellButton.getStyleClass().add("button");

        acellButton.setOnAction(new EventHandler<ActionEvent>(){
            public void handle(ActionEvent t){
                if(service.getTemeInLista().size()!=0){
                final Stage dialog = new Stage();
                dialog.initOwner(noteController.editStage);
                dialog.initModality(Modality.APPLICATION_MODAL);
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("AdaugaNota.fxml"));
                try {
                    Parent root = loader.load();
                    dialog.setTitle("Aplicatie");
                    Scene scene=new Scene(root);
                    scene.getStylesheets().add("Viper.css");
                    dialog.setScene(scene);
                    dialog.show();
                    AdaugaNota ctrl= loader.getController();

                    ctrl.setService(NoteButtonCell.this.getTableView().getItems().get(NoteButtonCell.this.getIndex()).getStudent(),noteController.service);
                }catch (Exception e) {
                    System.out.println(e);
                }}
                else
                    showErrorMessage("There are no projects you can grade");
            }
        });
        ucellButton.setOnAction(new EventHandler<ActionEvent>(){

            public void handle(ActionEvent t){
                if(service.getNotaInLista().size()!=0) {
                    final Stage dialog = new Stage();
                    dialog.initOwner(noteController.editStage);
                    dialog.initModality(Modality.APPLICATION_MODAL);
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("ModificaNota.fxml"));
                    try {
                        Parent root = loader.load();
                        dialog.setTitle("Aplicatie");
                        Scene scene = new Scene(root);
                        scene.getStylesheets().add("Viper.css");
                        dialog.setScene(scene);
                        dialog.show();
                        ModificaNota ctrl = loader.getController();

                        ctrl.setService(NoteButtonCell.this.getTableView().getItems().get(NoteButtonCell.this.getIndex()).getStudent(), noteController.service);
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                }
                else
                    showErrorMessage("There are no grades that you can update");

            }
        });

    }
    @Override
    protected void updateItem(Boolean t,boolean empty){
        super.updateItem(t,empty);
        if(!empty){
            setGraphic(box);
        }
        else
        {
            setGraphic(null);
        }
    }
    private void showErrorMessage(String text) {
        Alert message = new Alert(Alert.AlertType.ERROR);
        message.setTitle("Error Message");
        message.initOwner(noteController.editStage);
        message.initModality(Modality.APPLICATION_MODAL);
        message.setContentText(text);
        message.showAndWait();
    }
}