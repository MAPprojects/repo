package FXML;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;

import java.util.List;
import java.util.ResourceBundle;


import Domain.Teme;
import Repository.*;
import Service.*;
import Utils.ListEvent;
import Utils.Observer;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.converter.IntegerStringConverter;

public class TemaControllerFXML implements Observer<Teme> {
    StudentRepoSQL repoStudenti = new StudentRepoSQL(new StudentValidator());
    TemeRepoSQL repoTeme = new TemeRepoSQL(new TemeValidator());
    NoteRepoSQL noteRepo =new NoteRepoSQL(new NoteValidate());
    ServiceNote noteService= new ServiceNote(noteRepo);
    ServiceStudenti studentiService=new ServiceStudenti(repoStudenti);
    ServiceTeme temeService=new ServiceTeme(repoTeme);


    Service service=new Service(noteService,temeService,studentiService);
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button bt_saveStudent;

    @FXML
    private Button bt_deleteStudent;

    @FXML
    private Button bt_updateStudent;

    @FXML
    private TableView<Teme> tf_table;

    @FXML
    private TableColumn<Teme, Integer> c_id;

    @FXML
    private TableColumn<Teme, String> c_Descriere;

    @FXML
    private TableColumn<Teme, Integer> c_Deadline;


    @FXML
    private TableColumn<Teme, String> c_Delete;

    @FXML
    private TextField tf_searchID;

    @FXML
    private TextField tf_searchDescriere;

    @FXML
    private TextField tf_SearchDeadline;


    @FXML
    private Pagination paginator;
    @FXML
    private VBox vbox;


    private final static int rowsPerPage = 10;

    public ObservableList<Teme> listaCuMedie=  FXCollections.observableArrayList();
    @FXML
    void handleSaveTema(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("SaveTema.fxml"));

        Parent root = loader.load();
        Stage stage=new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(root));
        SaveTemaFXML saveCtrl = loader.getController();
        saveCtrl.setService(temeService);
        saveCtrl.setStage(stage);
        stage.close();
        stage.show();

    }

    private Node createPage(int pageIndex) {

        int fromIndex = pageIndex * rowsPerPage;
        int toIndex = Math.min(fromIndex + rowsPerPage, temeService.getAllTeme().size());
        tf_table.setItems(FXCollections.observableArrayList(temeService.getAllTeme().subList(fromIndex, toIndex)));

        return new BorderPane(tf_table);
    }


    @FXML
    void initialize() {
        temeService.addObserver(this);
        c_id.setCellValueFactory(new PropertyValueFactory<Teme,Integer>("nrTema"));
        c_Deadline.setCellValueFactory(new PropertyValueFactory<Teme,Integer>("Deadline"));
        c_Descriere.setCellValueFactory(new PropertyValueFactory<Teme,String>("Descriere"));





        Callback<TableColumn<Teme, String>, TableCell<Teme, String>> cellFactory
                = //
                new Callback<TableColumn<Teme, String>, TableCell<Teme, String>>() {
                    @Override
                    public TableCell call(final TableColumn<Teme, String> param) {
                        final TableCell<Teme, String> cell = new TableCell<Teme, String>() {

                            final ImageView image = new ImageView(new Image("FXML/img/delete-button.png"));
                            Image imageOk = new Image(getClass().getResourceAsStream("img/delete-button.png"));
                            ImageView imageView = new ImageView(imageOk);
                            final Button btn = new Button("",imageView);


                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                } else {

                                    btn.setOnAction(event -> {
                                        Teme tema = getTableView().getItems().get(getIndex());

                                        try {
                                            temeService.stergeTema(tema.getNrTema());
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,"Sters !", "Candidat sters cu succes !");

                                    });

                                    imageView.setFitHeight(15);
                                    imageView.setFitWidth(15);
                                    setGraphic(btn);
                                    setText(null);
                                }
                            }
                        };
                        return cell;
                    }
                };

        c_Delete.setCellFactory(cellFactory);
        tf_table.setEditable(true);

        paginator.setPageCount(temeService.getAllTeme().size() / rowsPerPage + 1);
        paginator.setCurrentPageIndex(0);


        paginator.setPageFactory(this::createPage);
        tf_table.setMinHeight(501);
        tf_table.setMinWidth(632);
        vbox.setLayoutX(148);
        vbox.setLayoutY(104);

        c_Deadline.setOnEditCommit(
                (TableColumn.CellEditEvent<Teme, Integer> t) ->
                        ( t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setDeadline(t.getNewValue())
        );
        c_Deadline.setOnEditCommit(
                (TableColumn.CellEditEvent<Teme, Integer> t) -> {
                    try {
                        handleUpdateDeadline(t.getNewValue());
                    } catch (ValidationException e) {
                        e.printStackTrace();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                }
        );


        c_Deadline.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        c_Descriere.setCellFactory(TextFieldTableCell.forTableColumn());

        c_Descriere.setOnEditCommit(
                (TableColumn.CellEditEvent<Teme, String> t) ->
                        ( t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setDescriere(t.getNewValue())
        );
        c_Descriere.setOnEditCommit(
                (TableColumn.CellEditEvent<Teme, String> t) -> {
                    try {
                        handleUpdateDescriere(t.getNewValue());
                    } catch (ValidationException e) {
                        e.printStackTrace();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                }
        );

    }

    private void handleUpdateDeadline(Integer newValue) throws ValidationException, SQLException {
        Teme selectedTema=tf_table.getSelectionModel().getSelectedItem();
        temeService.updateTema(selectedTema.getNrTema(),selectedTema.getNrTema(),newValue,selectedTema.getDescriere());
    }

    private void handleUpdateDescriere(String newValue) throws ValidationException, SQLException {
        Teme selectedTema=tf_table.getSelectionModel().getSelectedItem();
        temeService.updateTema(selectedTema.getNrTema(),selectedTema.getNrTema(),selectedTema.getDeadline(),newValue);
    }


    @FXML
    void handleDataFilter(ActionEvent event) {
        FilteredList<Teme> filteredList=new FilteredList<>(FXCollections.observableArrayList(temeService.getAllTeme()));
        filteredList.predicateProperty().bind((Bindings.createObjectBinding(() ->
                        teme -> String.valueOf(teme.getNrTema()).contains(tf_searchID.getText())
                                && String.valueOf(teme.getDeadline()).contains(tf_SearchDeadline.getText())
                                && teme.getDescriere().contains((tf_searchDescriere.getText())),


                tf_searchID.textProperty(),
                tf_searchDescriere.textProperty(),
                tf_SearchDeadline.textProperty()

        )));



        tf_table.setItems(filteredList);
    }


    @Override
    public void notifyEvent(ListEvent<Teme> e) throws SQLException {
        tf_table.getItems().setAll(e.getList());
        int index = paginator.getCurrentPageIndex();
            paginator.setPageCount(temeService.getAllTeme().size() / rowsPerPage + 1);
        paginator.setPageFactory(this::createPage);
        paginator.setCurrentPageIndex(index);
    }
}

