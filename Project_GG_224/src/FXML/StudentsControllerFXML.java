package FXML;


import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;

import java.util.ResourceBundle;

import Domain.Studenti;
import Repository.*;
import Service.*;
import Utils.ListEvent;
import Utils.Observer;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.converter.IntegerStringConverter;


public class StudentsControllerFXML implements Observer<Studenti>{


    ProfesorRepoSQL profesorRepoSQL=new ProfesorRepoSQL();
    StudentRepoSQL repoStudenti = new StudentRepoSQL(new StudentValidator());
    TemeRepoSQL repoTeme = new TemeRepoSQL(new TemeValidator());
    NoteRepoSQL noteRepo =new NoteRepoSQL(new NoteValidate());
    ServiceNote noteService= new ServiceNote(noteRepo);
    ServiceStudenti studentiService=new ServiceStudenti(repoStudenti);
    ServiceTeme temeService=new ServiceTeme(repoTeme);


    Service service=new Service(noteService,temeService,studentiService);

    private final static int rowsPerPage = 10;




    private final List<Studenti> data =repoStudenti.getAll();


    @FXML
    private TableColumn<Studenti, String> c_Action;
    @FXML
    private VBox tf_vbox;
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
    private TableView<Studenti> tf_table;

    @FXML
    private TableColumn<Studenti, Integer> c_id;

    @FXML
    private TableColumn<Studenti, String> c_Nume;

    @FXML
    private TableColumn<Studenti, Integer> c_Grupa;

    @FXML
    private TableColumn<Studenti, String> c_indrumator;

    @FXML
    private TableColumn<Studenti, String> c_Email;

    @FXML
    private Pagination paginator;
    @FXML
    private TextField tf_search;

    @FXML
    private TextField tf_searchID;

    @FXML
    private TextField tf_searchNume;

    @FXML
    private TextField tf_searchGrupa;

    @FXML
    private TextField tf_searchEmail;

    @FXML
    private ComboBox tf_searchIndrumator;


    @FXML
    void handleSaveStudent(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("SaveStudent.fxml"));

        Parent root = loader.load();
        Stage stage=new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(root));
        SaveStudentFXML saveCtrl = loader.getController();
        saveCtrl.setService(studentiService);
        saveCtrl.setStage(stage);
        stage.close();
        stage.show();






    }



    private Node createPage(int pageIndex) {

        int fromIndex = pageIndex * rowsPerPage;
        int toIndex = Math.min(fromIndex + rowsPerPage, studentiService.getAllStudents().size());
        tf_table.setItems(FXCollections.observableArrayList(studentiService.getAllStudents().subList(fromIndex, toIndex)));

        return new BorderPane(tf_table);
    }

    @FXML
    void handleFilterData(KeyEvent event) {
        FilteredList<Studenti> filteredList=new FilteredList<>(FXCollections.observableArrayList(studentiService.getAllStudents()));

        if (tf_searchIndrumator.getSelectionModel().getSelectedItem()!=null){
        filteredList.predicateProperty().bind((Bindings.createObjectBinding(() ->
                        student -> String.valueOf(student.getIdStudent()).contains(tf_searchID.getText())
                                && student.getNume().toLowerCase().contains(tf_searchNume.getText())
                                && student.getEmail().toLowerCase().contains((tf_searchEmail.getText()))
                                && Integer.toString(student.getGrupa()).contains(tf_searchGrupa.getText())
                                && student.getIndrumator().contains(String.valueOf(tf_searchIndrumator.getSelectionModel().getSelectedItem())),

                tf_searchID.textProperty(),
                tf_searchNume.textProperty(),
                tf_searchEmail.textProperty(),
                tf_searchGrupa.textProperty(),
                tf_searchEmail.textProperty(),
                tf_searchIndrumator.itemsProperty()

        ))); tf_table.setItems(filteredList);}
        else{
            filteredList.predicateProperty().bind((Bindings.createObjectBinding(() ->
                            student -> String.valueOf(student.getIdStudent()).contains(tf_searchID.getText())
                                    && student.getNume().toLowerCase().contains(tf_searchNume.getText())
                                    && student.getEmail().toLowerCase().contains((tf_searchEmail.getText()))
                                    && Integer.toString(student.getGrupa()).contains(tf_searchGrupa.getText()),


                    tf_searchID.textProperty(),
                    tf_searchNume.textProperty(),
                    tf_searchEmail.textProperty(),
                    tf_searchGrupa.textProperty(),
                    tf_searchEmail.textProperty())));
            tf_table.setItems(filteredList);

        }






    }


    ObservableList<String> listaComboBox= FXCollections.observableArrayList();

    void handlecomboBoxEdit(){
        for (String str : profesorRepoSQL.numeProfesori()) {
            listaComboBox.add(str);

        }
        listaComboBox.add(null);

    }
    @FXML
    void initialize() {
        handlecomboBoxEdit();

        tf_searchIndrumator.setItems(listaComboBox);
        studentiService.addObserver(this);
        c_id.setCellValueFactory(new PropertyValueFactory<Studenti,Integer>("idStudent"));
        c_Nume.setCellValueFactory(new PropertyValueFactory<Studenti,String>("Nume"));
        c_Grupa.setCellValueFactory(new PropertyValueFactory<Studenti,Integer>("Grupa"));
        c_Email.setCellValueFactory(new PropertyValueFactory<Studenti,String>("Email"));
        c_indrumator.setCellValueFactory(new PropertyValueFactory<Studenti,String>("Indrumator"));
        c_indrumator.setCellFactory(ComboBoxTableCell.forTableColumn(listaComboBox));



        Callback<TableColumn<Studenti, String>, TableCell<Studenti, String>> cellDelete
                = //
                new Callback<TableColumn<Studenti, String>, TableCell<Studenti, String>>() {
                    @Override
                    public TableCell call(final TableColumn<Studenti, String> param) {
                        final TableCell<Studenti, String> cell = new TableCell<Studenti, String>() {

                            final ImageView image = new ImageView(new Image("FXML/img/delete-button.png"));

                            final Button btn = new Button("",image);


                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                } else {

                                    btn.setOnAction(event -> {
                                        Studenti person = getTableView().getItems().get(getIndex());

                                        try {
                                            studentiService.stergeStudent(person.getIdStudent());
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,"Sters !", "Student sters cu succes !");

                                    });

                                    image.setFitHeight(15);
                                    image.setFitWidth(15);

                                    setGraphic(btn);
                                    setText(null);
                                }
                            }
                        };
                        return cell;
                    }
                };



        c_Action.setCellFactory(cellDelete);
        tf_table.setEditable(true);



        paginator.setPageCount(studentiService.getAllStudents().size() / rowsPerPage + 1);
        paginator.setCurrentPageIndex(0);


        paginator.setPageFactory(this::createPage);
        tf_table.setMinHeight(515);
        tf_table.setMinWidth(689);
        tf_vbox.setLayoutX(71);
        tf_vbox.setLayoutY(170);




//        c_indrumator.setOnEditCommit(
//                (TableColumn.CellEditEvent<Studenti, String> t) ->
//                        ( t.getTableView().getItems().get(
//                                t.getTablePosition().getRow())
//                        ).setIndrumator(t.getNewValue())
//        );
//        c_indrumator.setOnEditCommit(
//                (TableColumn.CellEditEvent<Studenti, String> t) -> {
//                    try {
//                        handleUpdateIndrumator(t.getNewValue());
//                    } catch (ValidationException e) {
//                        e.printStackTrace();
//                    } catch (SQLException e) {
//                        e.printStackTrace();
//                    }
//
//                }
//        );
        c_indrumator.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Studenti, String>>() {
                                         @Override
                                         public void handle(TableColumn.CellEditEvent<Studenti, String> event) {
                                             try {
                                                 handleUpdateIndrumator(event.getNewValue());
                                             } catch (ValidationException e) {
                                                 e.printStackTrace();
                                             } catch (SQLException e) {
                                                 e.printStackTrace();
                                             }
                                         }
                                     }

        );
//        tf_searchIndrumator.setEditable(true);




        c_Nume.setOnEditCommit(
                (TableColumn.CellEditEvent<Studenti, String> t) ->
                        ( t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setNume(t.getNewValue())
        );
        c_Nume.setOnEditCommit(
                (TableColumn.CellEditEvent<Studenti, String> t) -> {
                    try {
                        handleUpdateNume(t.getNewValue());
                    } catch (ValidationException e) {
                        e.printStackTrace();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                }
        );
        c_Email.setOnEditCommit(
                (TableColumn.CellEditEvent<Studenti, String> t) ->
                        ( t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setEmail(t.getNewValue())
        );
        c_Email.setOnEditCommit(
                (TableColumn.CellEditEvent<Studenti, String> t) -> {
                    try {
                        handleUpdateEmail(t.getNewValue());
                    } catch (ValidationException e) {
                        e.printStackTrace();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                }
        );
        c_Email.setCellFactory(TextFieldTableCell.forTableColumn());

        c_Nume.setCellFactory(TextFieldTableCell.forTableColumn());
//        c_indrumator.setCellFactory(TextFieldTableCell.forTableColumn());



        c_Grupa.setOnEditCommit(
                (TableColumn.CellEditEvent<Studenti, Integer> t) ->
                        ( t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setGrupa(t.getNewValue())
        );
        c_Grupa.setOnEditCommit(
                (TableColumn.CellEditEvent<Studenti, Integer> t) -> {
                    try {
                        handleUpdateGrupa(t.getNewValue());
                    } catch (ValidationException e) {
                        e.printStackTrace();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                }
        );
        c_Grupa.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
    }

    private void handleUpdateIndrumator(String newValue) throws ValidationException, SQLException {
        Studenti selectedStudent =tf_table.getSelectionModel().getSelectedItem();

        repoStudenti.update(selectedStudent,new Studenti(selectedStudent.getIdStudent(),selectedStudent.getNume(),selectedStudent.getGrupa(),selectedStudent.getEmail(),newValue));

    }


    private void handleUpdateGrupa(Integer newValue) throws ValidationException, SQLException {
        Studenti selectedStudent =tf_table.getSelectionModel().getSelectedItem();

        repoStudenti.update(selectedStudent,new Studenti(selectedStudent.getIdStudent(),selectedStudent.getNume(),newValue,selectedStudent.getEmail(),selectedStudent.getIndrumator()));

    }

    private void handleUpdateEmail(String newValue) throws ValidationException, SQLException {
        Studenti selectedStudent =tf_table.getSelectionModel().getSelectedItem();

        repoStudenti.update(selectedStudent,new Studenti(selectedStudent.getIdStudent(),selectedStudent.getNume(),selectedStudent.getGrupa(),newValue,selectedStudent.getIndrumator()));

    }



    public void notifyEvent(ListEvent<Studenti> e) {
        tf_table.getItems().setAll(e.getList());
        int index = paginator.getCurrentPageIndex();
        paginator.setPageCount(studentiService.getAllStudents().size() / rowsPerPage + 1);
        paginator.setPageFactory(this::createPage);
        paginator.setCurrentPageIndex(index);

    }
    void handleUpdateNume(String str) throws ValidationException, SQLException {
        Studenti selectedStudent =tf_table.getSelectionModel().getSelectedItem();
        repoStudenti.update(selectedStudent,new Studenti(selectedStudent.getIdStudent(),str,selectedStudent.getGrupa(),selectedStudent.getEmail(),selectedStudent.getIndrumator()));


    }


}
