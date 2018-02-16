package FXML;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

import Domain.NotaMare;
import Domain.Note;
import Domain.Studenti;
import Repository.*;
import Service.*;
import Utils.ListEvent;
import Utils.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

public class ControllerViewNoteFXMl implements Observer<Note> {

//    StudentRepoSQL repoStudenti = new StudentRepoSQL(new StudentValidator());
//    TemeRepoSQL repoTeme = new TemeRepoSQL(new TemeValidator());
//    NoteRepoSQL noteRepo =new NoteRepoSQL(new NoteValidate());
//    Service service=new Service(noteRepo,repoTeme,repoStudenti);


    StudentRepoSQL repoStudenti = new StudentRepoSQL(new StudentValidator());
    TemeRepoSQL repoTeme = new TemeRepoSQL(new TemeValidator());
    NoteRepoSQL noteRepo =new NoteRepoSQL(new NoteValidate());
    ServiceNote noteService= new ServiceNote(noteRepo);
    ServiceStudenti studentiService=new ServiceStudenti(repoStudenti);
    ServiceTeme temeService=new ServiceTeme(repoTeme);


    Service service=new Service(noteService,temeService,studentiService);

    List<NotaMare> lista=FXCollections.observableArrayList();
    private final static int rowsPerPage = 20;
    @FXML
    private Pagination paginator;

    @FXML
    private VBox vbox;

    @FXML
    private TableView<Note> table;

    @FXML
    private TableColumn<Note, Integer> c_idTema;

    @FXML
    private TableColumn<Note, Integer> c_nota;

    @FXML
    private TableColumn<Note, Integer> c_deadline;

    @FXML
    private TableColumn<Note, String> c_butonSterge;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private GridPane gridpane;

    @FXML
    private Label l_grupa;

    @FXML
    private Label l_id;

    @FXML
    private Label l_nume;

    @FXML
    private Label l_email;

    @FXML
    private Label l_indrumator;

    @FXML
    private TableColumn<Note, String> b_coloanaUpdate;

    private Studenti selectedStudent;
    public void initData(Studenti student) throws SQLException {
        selectedStudent=student;
        l_nume.setText(selectedStudent.getNume());
        l_grupa.setText(String.valueOf(selectedStudent.getGrupa()));
        l_email.setText(selectedStudent.getEmail());
        l_indrumator.setText(selectedStudent.getIndrumator());
        lista=listaNoteStudent(student);

        if (lista.size()%2==0)
            paginator.setPageCount(lista.size() / rowsPerPage );
        else
            paginator.setPageCount(lista.size() / rowsPerPage +1);
        paginator.setCurrentPageIndex(0);


        paginator.setPageFactory(this::createPage);
        table.setMinHeight(348);
        table.setMinWidth(396);
        table.setLayoutX(349);
        table.setLayoutY(58);


    }

    public void notifyEvent(ListEvent<Note> e) throws SQLException {
        table.getItems().setAll(e.getList());
        int index = paginator.getCurrentPageIndex();
        lista=listaNoteStudent(selectedStudent);
        paginator.setPageCount(listaNoteStudent(selectedStudent).size() / rowsPerPage + 1);
        paginator.setPageFactory(this::createPage);
        paginator.setCurrentPageIndex(index);


    }
    public List<NotaMare> listaNoteStudent(Studenti stud) throws SQLException {
        ObservableList<NotaMare> list =FXCollections.observableArrayList();
        for (Note note: noteService.getAllNote())
            if(note.getIdStudent()==stud.getIdStudent())
                list.add(new NotaMare(note.getIdStudent(),note.getNrTema(),note.getValoare(),noteRepo.getSpatamnaPredare(note.getIdStudent(),note.getNrTema())));

        return list;
    }


    private Node createPage(int pageIndex) {

        int fromIndex = pageIndex * rowsPerPage;
        int toIndex = Math.min(fromIndex + rowsPerPage, lista.size());
        table.setItems(FXCollections.observableArrayList(lista.subList(fromIndex, toIndex)));

        return new BorderPane(table);
    }


    Callback<TableColumn<Note, String>, TableCell<Note, String>> cellFactoryView =   new Callback<TableColumn<Note, String>, TableCell<Note, String>>() {
        @Override
        public TableCell call(final TableColumn<Note, String> param) {
            final TableCell<Note, String> cell = new TableCell<Note, String>() {
                final ImageView image = new ImageView(new Image("FXML/img/delete-button.png"));
                final Button btn = new Button("", image);

                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {

                        btn.setOnAction(event -> {
                            Note nota = getTableView().getItems().get(getIndex());

                            try {
                                noteService.stergeNota(nota.getIdStudent(), nota.getNrTema());


                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Sters !", "Noata a fost stearsa cu succes !");
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



    Callback<TableColumn<Note, String>, TableCell<Note, String>> cellUpdate =   new Callback<TableColumn<Note, String>, TableCell<Note, String>>() {
        @Override
        public TableCell call(final TableColumn<Note, String> param) {
            final TableCell<Note, String> cell = new TableCell<Note, String>() {
                final ImageView image = new ImageView(new Image("FXML/img/rotate.png"));
                final Button btn = new Button("", image);

                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {

                        btn.setOnAction(event -> {
                            Note nota = getTableView().getItems().get(getIndex());
                            FXMLLoader loader = new FXMLLoader();
                            loader.setLocation(getClass().getResource("UpdateNota.fxml"));

                            Parent root = null;
                            try {
                                root = loader.load();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Stage stage=new Stage();
                            stage.initModality(Modality.APPLICATION_MODAL);
                            stage.setScene(new Scene(root));
                            UpdateNota updateCotnroller = loader.getController();
                            updateCotnroller.setService(noteService);
                            updateCotnroller.initData(nota);
                            updateCotnroller.setStage(stage);
                            stage.close();
                            stage.show();

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

        @FXML
        void initialize() {
            noteService.addObserver(this);

            c_nota.setCellValueFactory(new PropertyValueFactory<Note, Integer>("valoare"));
            c_idTema.setCellValueFactory(new PropertyValueFactory<Note, Integer>("nrTema"));
            c_deadline.setCellValueFactory(new PropertyValueFactory<Note, Integer>("saptamanaPredare"));

            c_butonSterge.setCellFactory(cellFactoryView);
            b_coloanaUpdate.setCellFactory(cellUpdate);
            if (lista.size() % 2 == 0)
                paginator.setPageCount(lista.size() / rowsPerPage);
            else
                paginator.setPageCount(lista.size() / rowsPerPage + 1);
            paginator.setCurrentPageIndex(0);


            paginator.setPageFactory(this::createPage);
            table.setMinHeight(348);
            table.setMinWidth(396);
            table.setLayoutX(349);
            table.setLayoutY(58);
        }




}
