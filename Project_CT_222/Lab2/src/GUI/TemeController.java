package GUI;

import Domain.Studenti;
import Domain.Teme;
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
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.util.List;

public class TemeController implements Observer<ListEvent> {
    @FXML
    public TableView temeView;

    @FXML
    public TableColumn<Teme,Integer> colNrLaborator;
    public TableColumn<Teme,String> colCerinta ;
    public TableColumn<Teme,Integer> colDeadline ;
    public TableColumn deleteColumn;
    @FXML
    public TextField idTema,deadline,filter;
    public TextArea cerinta;

    @FXML
    public Button addButton;
    public Button clearButton;
    public Button updateButton;

    @FXML
    public ComboBox comboTeme;

    @FXML
    public Pagination pagination=new Pagination(5,0);

    @FXML
    private final static int rowsPerPage=14;

    @FXML
    private Service service;
    private Stage editStage;




    private ObservableList<Teme> model;

    public void onEnterIdTema(ActionEvent ae){
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                deadline.requestFocus();
            }
        });
    }
    public void onEnterDeadline(ActionEvent ae){
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                cerinta.requestFocus();
            }
        });
    }


    public void setService(Service service,Stage stage) {
        this.editStage=stage ;
        this.service = service;
        this.service.addObserver(this);
        loadDataHandler();
    }
    private Node createPage(int pageIndex){
        int fromIndex=pageIndex*rowsPerPage;
        int toIndex=Math.min(fromIndex+rowsPerPage,model.size());
        temeView.setItems(FXCollections.observableArrayList(model.subList(fromIndex,toIndex)));
        temeView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        return new BorderPane(temeView);
    }
    @FXML
    public void initialize(){
        idTema.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    idTema.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        deadline.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    deadline.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        idTema.setPromptText("Numar Tema: only numbers");
        deadline.setPromptText("Deadline: only numbers");
        cerinta.setPromptText("Cerinta");
        filter.setPromptText("Search");
        colNrLaborator.setCellValueFactory(new PropertyValueFactory<>("NumarLaborator"));
        colCerinta.setCellValueFactory(new PropertyValueFactory<>("cerinta"));
        colDeadline.setCellValueFactory(new PropertyValueFactory<>("deadline"));
        addDeleteButtons(deleteColumn);
        temeView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showTemeDetails((Teme) newValue));
        ObservableList<String> listCombo2 = FXCollections.observableArrayList("Tip search","cerinta","deadline","numar");
        comboTeme.setItems(listCombo2);
        comboTeme.getSelectionModel().selectFirst();


        filter.setOnKeyReleased(event -> {
            List<Teme> rezultat;
            if(comboTeme.getSelectionModel().getSelectedItem().equals("Tip search"))
                rezultat = service.getTemeInLista();
            else{
                if(!filter.getText().equals("")){
                    if (comboTeme.getSelectionModel().getSelectedItem().equals("numar")) {
                        try{
                            rezultat = service.filterNrTema(Integer.parseInt(filter.getText()));
                        }catch (Exception e){
                            rezultat = service.getTemeInLista();
                        }
                    } else if (comboTeme.getSelectionModel().getSelectedItem().equals("deadline")) {
                        rezultat = service.filterDeadline(Integer.parseInt(filter.getText()));

                    } else if (comboTeme.getSelectionModel().getSelectedItem().equals("cerinta")) {
                        rezultat = service.filterCerinta(filter.getText());
                    }
                    else
                        rezultat = service.getTemeInLista();
                }
                else {
                    rezultat = service.getTemeInLista();
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
            model.setAll(service.getTemeInLista());

        });}

    private void addDeleteButtons(TableColumn col){
        col.setCellFactory(new Callback<TableColumn,TableCell>(){
            @Override
            public TableCell call(TableColumn param){
                TemeButtonCell btn=new TemeButtonCell(service,TemeController.this);
                return btn;
            }
        });
    }
    private void showTemeDetails(Teme newValue) {
        if (newValue == null)
            clearFields();
        else {
            idTema.setText(String.valueOf(newValue.getId()));
            cerinta.setText(newValue.getCerinta());
            deadline.setText(String.valueOf(newValue.getDeadline()));
        }
    }

    @FXML
    public void clearFields(){
        idTema.setText("");
        cerinta.setText("");
        deadline.setText("");
    }

    @FXML
    private void clearFieldsT(){
        idTema.setText("");
        cerinta.setText("");
        deadline.setText("");
    }

    private void loadDataHandler() {
        List<Teme> list = service.getTemeInLista();

        model = FXCollections.observableArrayList(list);
        if(model.size()%14==0)
            pagination.setPageCount(model.size()/rowsPerPage);
        else
            pagination.setPageCount(model.size()/rowsPerPage+1);
        pagination.setPageFactory(this::createPage);
    }

    public void addButtonHandler() {

        if(idTema.getText().equals("")||deadline.getText().equals("")||cerinta.getText().equals("")) {
            showErrorMessage("Completati toate fieldurile");

        }
        else{
            if(!idTema.getText().equals("")&&!deadline.getText().equals("")&&!cerinta.getText().equals("")){
        try {

            service.addTema(Integer.parseInt(idTema.getText()),cerinta.getText(),Integer.parseInt(deadline.getText()));
            // loadStudenti();
        } catch (Exception e) {
            showErrorMessage(e.toString());
        }}
        }
    }


    public void filtrareTemeHandller(){
        List<Teme> rezultat ;
        if(comboTeme.getSelectionModel().getSelectedItem().equals("niciunul"))
            rezultat = service.getTemeInLista();
        else{
            if(!filter.getText().equals("")){
                if (comboTeme.getSelectionModel().getSelectedItem().equals("numar")) {
                    try{
                        rezultat = service.filterNrTema(Integer.parseInt(filter.getText()));
                    }catch (Exception e){
                        rezultat = service.getTemeInLista();
                    }
                } else if (comboTeme.getSelectionModel().getSelectedItem().equals("deadline")) {
                    rezultat = service.filterDeadline(Integer.parseInt(filter.getText()));

                } else if (comboTeme.getSelectionModel().getSelectedItem().equals("cerinta")) {
                    rezultat = service.filterCerinta(filter.getText());
                }
                else
                    rezultat = service.getTemeInLista();
            }
            else {
                rezultat = service.getTemeInLista();
            }
        }

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
        Teme tema = (Teme) temeView.getSelectionModel().getSelectedItem();
        try{
            if (tema != null) {
                String cer = cerinta.getText();
                String dead = deadline.getText();
                service.updateTeme(tema.getId(), "cerinta", cer);
                service.updateTeme(tema.getId(), "deadline", dead);
            } else {
                showErrorMessage("Field-urile sunt goale!");
            }
        }catch (Exception e){
            showErrorMessage(e.toString());
        }
        temeView.refresh();
    }

    public void notifyEvent(ListEvent e) {

        List<Teme> list = service.getTemeInLista();
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

class TemeButtonCell extends TableCell<Teme,Boolean>{
    final Button cellButton=new Button("Delete");
    private Service service;
    private TemeController temeController;
    TemeButtonCell(Service service,TemeController ctrl){
        this.service=service;
        temeController=ctrl;
        cellButton.getStyleClass().add("button");
        cellButton.setOnAction(new EventHandler<ActionEvent>(){
            public void handle(ActionEvent t){
                Teme current=TemeButtonCell.this.getTableView().getItems().get(TemeButtonCell.this.getIndex());
                temeController.clearFields();
                service.removeTema(current.getId());
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


