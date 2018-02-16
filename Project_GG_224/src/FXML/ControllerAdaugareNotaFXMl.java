package FXML;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

import Domain.Note;
import Domain.Studenti;
import Domain.Teme;
import Repository.*;
import Service.*;
import Utils.ListEvent;
import Utils.ListEventType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class ControllerAdaugareNotaFXMl {

//    StudentRepoSQL repoStudenti = new StudentRepoSQL(new StudentValidator());
//    TemeRepoSQL repoTeme = new TemeRepoSQL(new TemeValidator());
//    NoteRepoSQL noteRepo =new NoteRepoSQL(new NoteValidate());
//    Service service1=new Service(noteRepo,repoTeme,repoStudenti);


    StudentRepoSQL repoStudenti = new StudentRepoSQL(new StudentValidator());
    TemeRepoSQL repoTeme = new TemeRepoSQL(new TemeValidator());
    NoteRepoSQL noteRepo =new NoteRepoSQL(new NoteValidate());
    ServiceNote noteService= new ServiceNote(noteRepo);
    ServiceStudenti studentiService=new ServiceStudenti(repoStudenti);
  ServiceTeme temeService=new ServiceTeme(repoTeme);

    Stage stage;
    public void setStage(Stage stage){
        this.stage=stage;
    }
    Service service;
    private ObservableList options= FXCollections.observableArrayList();
    private Studenti student;
    private NotaControllerFXML notaController;
    public void setService(Service service){
        this.service = service;
    }
    public void setCotnroller(NotaControllerFXML notaController){
        this.notaController=notaController;
    }
    public void setStudent(Studenti student){
        this.student = student;
    }
    public void initData(Studenti stud){
        tf_id.setText(String.valueOf(stud.getIdStudent()));
        tf_id.setDisable(true);

    }
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField tf_valoare;

    @FXML
    private Button bt_saveNota;

    @FXML
    private TextField tf_Obs;

    @FXML
    private TextField tf_saptamana;

    @FXML
    private TextField tf_id;

    @FXML
    private ComboBox<Teme> combobox;

    private Note extractNota() {
        String id = tf_id.getText();
        String nrTema = String.valueOf(combobox.getSelectionModel().getSelectedItem().getNrTema());

        String valoare = tf_valoare.getText();

        return new Note(Integer.parseInt(id),Integer.parseInt(nrTema),Integer.parseInt(valoare));
    }

    TableColumn medie;
    public void setCell(TableColumn<Studenti, Float> medie){
        this.medie=medie;

    }

    @FXML
    void handleSaveNota(ActionEvent event) throws ValidationException, SQLException {
        try {
            Note toAdd = extractNota();

            if(Integer.parseInt(tf_saptamana.getText()) <0 && Integer.parseInt(tf_saptamana.getText())>15)
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,"Nu a fost adaugat !", "A aparut o eroare !");

            Note nota = noteService.save(toAdd.getIdStudent(), toAdd.getNrTema(), toAdd.getValoare(), tf_Obs.getText(), Integer.parseInt(tf_saptamana.getText()));
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,"Adaugat !", "Nota a fost adaugata cu succes !");
            float medie=0;
            int contor=0;
            for(Note nota2 : noteService.getAllNote())
                if(nota.getIdStudent()==nota2.getIdStudent()) {
                    medie += nota.getValoare();
                    contor+=1;
                }
            medie=medie/contor;
            notaController.UpdateTable(medie);
        }catch (Exception e){
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,"Nu a fost adaugat !", "A aparut o eroare !");

        }



    }

     public void fillComboBox(Studenti stud){


//         for (Teme tema:temeService.getAllTeme())
//             options.add(String.valueOf(tema.getNrTema()));
//
//        for (Teme tema:temeService.getAllTeme())
//            for (Note nota :noteService.getAllNote())
//                if(nota.getIdStudent()==stud.getIdStudent())
//                    if (tema.getNrTema()==nota.getNrTema()) {
//                        options.remove(String.valueOf(tema.getNrTema()));
//
//                    }


         for (Teme tema:temeService.getAllTeme())
             options.add(tema);

        for (Teme tema:temeService.getAllTeme())
            for (Note nota :noteService.getAllNote())
                if(nota.getIdStudent()==stud.getIdStudent())
                    if (tema.getNrTema()==nota.getNrTema()) {
                        options.remove(tema);

                    }


        combobox.setItems(options);



    }


    @FXML
    void initialize() {

        combobox.setCellFactory(param -> {
            return new ListCell<Teme>() {

                @Override
                public void updateItem(Teme item, boolean empty) {
                    super.updateItem(item, empty);

                    if (item != null) {
                        setText(String.valueOf(item.getNrTema()));


                        Tooltip tt = new Tooltip(repoTeme.getById(item.getNrTema()).getDescriere());
                        String tooltip="Dedline-ul temei"+String.valueOf(item.getDeadline())+"DESCRIERE"+item.getDescriere();
                        tt.setText(tooltip);
                        tt.setFont(Font.font(20));

                        setTooltip(tt);
                    } else {
                        setText(null);
                        setTooltip(null);
                    }
                }
            };
        });
    }
}
