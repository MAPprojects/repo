package FXML;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

import Domain.Note;
import Domain.Studenti;
import Domain.StudentiNotePart;
import Repository.*;
import Service.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class StudentPartControllerFXML {


    StudentRepoSQL repoStudenti = new StudentRepoSQL(new StudentValidator());
    TemeRepoSQL repoTeme = new TemeRepoSQL(new TemeValidator());
    NoteRepoSQL noteRepo =new NoteRepoSQL(new NoteValidate());
   ServiceNote noteService= new ServiceNote(noteRepo);
    ServiceStudenti studentiService=new ServiceStudenti(repoStudenti);
    ServiceTeme temeService=new ServiceTeme(repoTeme);


    Service service=new Service(noteService,temeService,studentiService);

    String user;
    String pass;
    public List<StudentiNotePart> listaNoteStudent() throws SQLException {
        Studenti stud=repoStudenti.getByUsername(user);

        ObservableList<Note> list =FXCollections.observableArrayList();
        for (Note note: noteService.getAllNote())
            if(note.getIdStudent()==stud.getIdStudent())
                list.add(note);
        ObservableList<StudentiNotePart> listBuna =FXCollections.observableArrayList();
        for(Note note : list)
            listBuna.add(new StudentiNotePart(note.getNrTema(),note.getValoare(),repoTeme.getById(note.getNrTema()).getDeadline(),noteRepo.getSpatamnaPredare(note.getIdStudent(),note.getNrTema()),noteRepo.getObservatii(note.getIdStudent(),note.getNrTema())));



        return listBuna;
    }
    private final static int rowsPerPage = 20;

    public void setUser(String user,String pass){
        this.user = user;
        this.pass=pass;
    }

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private VBox vbox;

    @FXML
    private TableView<StudentiNotePart> table;

    @FXML
    private TableColumn<StudentiNotePart, Integer> c_idTema;

    @FXML
    private TableColumn<StudentiNotePart, Integer> c_nota;

    @FXML
    private TableColumn<StudentiNotePart, Integer> c_deadline;

    @FXML
    private TableColumn<StudentiNotePart, Integer> c_SaptamanPredare;

    @FXML
    private TableColumn<StudentiNotePart, String> c_obseratii;

    @FXML
    private Pagination paginator;

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
    private Button b_changePass;

    @FXML
    private Button b_logout;
    List<StudentiNotePart> lista=FXCollections.observableArrayList();
    @FXML
    void handleChangePass(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("ChangePassword.fxml"));

        Parent root =  loader.load();

        ChangePassword controller =loader.getController();
        controller.setuser(user,pass);


        Stage stage=new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(root));
        controller.setStage(stage);
        stage.show();
    }
    private Node createPage(int pageIndex) {

        int fromIndex = pageIndex * rowsPerPage;
        int toIndex = Math.min(fromIndex + rowsPerPage,lista.size());
        table.setItems(FXCollections.observableArrayList(lista.subList(fromIndex, toIndex)));

        return new BorderPane(table);
    }
    @FXML
    void handleLogout(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("LoginPage.fxml"));
        Scene loginScene=new Scene(root);
        Stage window=(Stage)((Node) event.getSource()).getScene().getWindow();
        window.close();
        window.setScene(loginScene);
        window.show();
        window.setResizable(false);

    }

    @FXML
    void initialize() {
        c_idTema.setCellValueFactory(new PropertyValueFactory<StudentiNotePart,Integer>("nrTema"));
        c_nota.setCellValueFactory(new PropertyValueFactory<StudentiNotePart,Integer>("nota"));
        c_deadline.setCellValueFactory(new PropertyValueFactory<StudentiNotePart,Integer>("deadline"));
        c_SaptamanPredare.setCellValueFactory(new PropertyValueFactory<StudentiNotePart,Integer>("saptamanaPredare"));
        c_obseratii.setCellValueFactory(new PropertyValueFactory<StudentiNotePart,String>("Observatii"));








        paginator.setPageCount(lista.size() / rowsPerPage + 1);
        paginator.setCurrentPageIndex(0);


        paginator.setPageFactory(this::createPage);
        table.setMinHeight(324);
        table.setMinWidth(568);
        vbox.setLayoutX(282);
        vbox.setLayoutY(105);
    }

    public void initData() throws SQLException {
        l_nume.setText(repoStudenti.getByUsername(user).getNume());
        l_id.setText(String.valueOf(repoStudenti.getByUsername(user).getIdStudent()));
        l_email.setText(repoStudenti.getByUsername(user).getEmail());
        l_nume.setText(repoStudenti.getByUsername(user).getNume());
        l_grupa.setText(String.valueOf(repoStudenti.getByUsername(user).getGrupa()));
        l_indrumator.setText(repoStudenti.getByUsername(user).getIndrumator());
        lista=listaNoteStudent();
        paginator.setPageCount(lista.size() / rowsPerPage + 1);
        paginator.setCurrentPageIndex(0);

        paginator.setPageFactory(this::createPage);
        table.setMinHeight(324);
        table.setMinWidth(568);
        vbox.setLayoutX(282);
        vbox.setLayoutY(105);
    }
}
