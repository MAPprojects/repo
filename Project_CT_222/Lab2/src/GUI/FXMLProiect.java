package GUI;

import Domain.*;
import Utils.ListEvent;
import Utils.Observer;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import Service.Service;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class FXMLProiect implements Observer<ListEvent> {

    @FXML
    public TableView studentiView;
    public TableView temeView;
    public TableView noteView;

    public TableColumn<Studenti, String> colNume;
    public TableColumn<Studenti, Integer> colGrupa;
    public TableColumn<Studenti, String> colEmail;
    public TableColumn<Studenti, String> colCadruDidactic;

    public TableColumn<Teme,Integer> colNrLaborator;
    public TableColumn<Teme,String> colCerinta ;
    public TableColumn<Teme,Integer> colDeadline ;

    public TableColumn<Nota,Integer> colNota;
    public TableColumn<Nota,String> colStudent;
    public TableColumn<Nota,Integer> colTema;

    public TextField id, nume, grupa, email, profesor, filter,filter2, filter3;

    public TextField idTema,cerinta,deadline;

    public TextField idNota,idStudent,NrLaborator,Valoare,Observatii,SaptamanaPredare;
    @FXML
    public Button adauga1,adauga2,adauga3,sterge1,sterge2,modifica1,modifica2,modifica3,goleste1,goleste2,goleste3;

    public Label LidStud,Lnume,Lgrupa,Lemail,LcadruDidactic,LidTema,LCerinta,LDeadline,LidNota,LidStudent,LidNrLaborator,LValoare,LObservatii,LSaptamanaPredare;
    public Label Lfiltrare1,Lfiltrare2,Lfiltrare3;

    public ComboBox comboStudenti,comboTeme, comboNote;
    private Service service;
    private Stage editStage;


    private ObservableList<Studenti> model;

    private ObservableList<Teme> model2;

    private ObservableList<ObiectNota> model3;

    public void setService(Service service) {
        this.service = service;
        this.service.addObserver(this);
        loadDataHandler();
    }

    @FXML
    private void initialize() {
        studentiView.setVisible(false);
        temeView.setVisible(false);
        noteView.setVisible(false);

        id.setVisible(false);
        nume.setVisible(false);
        grupa.setVisible(false);
        email.setVisible(false);
        profesor.setVisible(false);
        filter.setVisible(false);
        comboStudenti.setVisible(false);
        adauga1.setVisible(false);
        sterge1.setVisible(false);
        modifica1.setVisible(false);
        goleste1.setVisible(false);
        LidStud.setVisible(false);
        Lnume.setVisible(false);
        Lgrupa.setVisible(false);
        Lemail.setVisible(false);
        LcadruDidactic.setVisible(false);
        Lfiltrare1.setVisible(false);

        idTema.setVisible(false);
        cerinta.setVisible(false);
        deadline.setVisible(false);
        filter2.setVisible(false);
        comboTeme.setVisible(false);
        adauga2.setVisible(false);
        sterge2.setVisible(false);
        modifica2.setVisible(false);
        goleste2.setVisible(false);
        Lfiltrare2.setVisible(false);
        LidTema.setVisible(false);
        LCerinta.setVisible(false);
        LDeadline.setVisible(false);


        NrLaborator.setVisible(false);
        idStudent.setVisible(false);
        idNota.setVisible(false);
        Observatii.setVisible(false);
        SaptamanaPredare.setVisible(false);
        Valoare.setVisible(false);
        filter3.setVisible(false);
        comboNote.setVisible(false);
        adauga3.setVisible(false);
        modifica3.setVisible(false);
        goleste3.setVisible(false);
        Lfiltrare3.setVisible(false);
        LidNota.setVisible(false);
        LidStudent.setVisible(false);
        LidNrLaborator.setVisible(false);
        LValoare.setVisible(false);
        LObservatii.setVisible(false);
        LSaptamanaPredare.setVisible(false);


        colNume.setCellValueFactory(new PropertyValueFactory<>("nume"));
        colGrupa.setCellValueFactory(new PropertyValueFactory<>("grupa"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colCadruDidactic.setCellValueFactory(new PropertyValueFactory<>("CadruDidactic"));
        studentiView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showStudentiDetails((Studenti) newValue));


        colNrLaborator.setCellValueFactory(new PropertyValueFactory<>("NumarLaborator"));
        colCerinta.setCellValueFactory(new PropertyValueFactory<>("cerinta"));
        colDeadline.setCellValueFactory(new PropertyValueFactory<>("deadline"));
        temeView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showTemeDetails((Teme) newValue));

        colNota.setCellValueFactory(new PropertyValueFactory<>("value"));
        colStudent.setCellValueFactory(new PropertyValueFactory<>("nume"));
        colTema.setCellValueFactory(new PropertyValueFactory<>("laborator"));
        noteView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showNoteDetails((ObiectNota) newValue));

        ObservableList<String> listCombo1 = FXCollections.observableArrayList("grupa","email","cadru didactic","niciunul");
        comboStudenti.setItems(listCombo1);

        ObservableList<String> listCombo2 = FXCollections.observableArrayList("cerinta","deadline","deadline between","niciunul");
        comboTeme.setItems(listCombo2);

        ObservableList<String> listCombo3 = FXCollections.observableArrayList("idStudent","idTema","note mai mici decat","niciunul");
        comboNote.setItems(listCombo3);

    }

    public void Studentihandler(){
        if(id.isVisible()==true){
            studentiView.setVisible(false);
        id.setVisible(false);
        nume.setVisible(false);
        grupa.setVisible(false);
        email.setVisible(false);
        profesor.setVisible(false);
        filter.setVisible(false);
        comboStudenti.setVisible(false);
        adauga1.setVisible(false);
        sterge1.setVisible(false);
        modifica1.setVisible(false);
        goleste1.setVisible(false);
        LidStud.setVisible(false);
        Lnume.setVisible(false);
        Lgrupa.setVisible(false);
        Lemail.setVisible(false);
        LcadruDidactic.setVisible(false);
        Lfiltrare1.setVisible(false);}
        else{
            studentiView.setVisible(true);
            id.setVisible(true);
            nume.setVisible(true);
            grupa.setVisible(true);
            email.setVisible(true);
            profesor.setVisible(true);
            filter.setVisible(true);
            comboStudenti.setVisible(true);
            adauga1.setVisible(true);
            sterge1.setVisible(true);
            modifica1.setVisible(true);
            goleste1.setVisible(true);
            LidStud.setVisible(true);
            Lnume.setVisible(true);
            Lgrupa.setVisible(true);
            Lemail.setVisible(true);
            LcadruDidactic.setVisible(true);
            Lfiltrare1.setVisible(true);
        }
    }

    public void Temehandler(){
        if(idTema.isVisible()==true){
            idTema.setVisible(false);
            cerinta.setVisible(false);
            deadline.setVisible(false);
            filter2.setVisible(false);
            comboTeme.setVisible(false);
            adauga2.setVisible(false);
            sterge2.setVisible(false);
            modifica2.setVisible(false);
            goleste2.setVisible(false);
            Lfiltrare2.setVisible(false);
            LidTema.setVisible(false);
            LCerinta.setVisible(false);
            LDeadline.setVisible(false);
            temeView.setVisible(false);
        }
        else{
            idTema.setVisible(true);
            cerinta.setVisible(true);
            deadline.setVisible(true);
            filter2.setVisible(true);
            comboTeme.setVisible(true);
            adauga2.setVisible(true);
            sterge2.setVisible(true);
            modifica2.setVisible(true);
            goleste2.setVisible(true);
            Lfiltrare2.setVisible(true);
            LidTema.setVisible(true);
            LCerinta.setVisible(true);
            LDeadline.setVisible(true);
            temeView.setVisible(true);
        }
    }

    public void Notehandler(){
        if(idNota.isVisible()==true){
            NrLaborator.setVisible(false);
            idStudent.setVisible(false);
            idNota.setVisible(false);
            Observatii.setVisible(false);
            SaptamanaPredare.setVisible(false);
            Valoare.setVisible(false);
            filter3.setVisible(false);
            comboNote.setVisible(false);
            adauga3.setVisible(false);
            modifica3.setVisible(false);
            goleste3.setVisible(false);
            Lfiltrare3.setVisible(false);
            LidNota.setVisible(false);
            LidStudent.setVisible(false);
            LidNrLaborator.setVisible(false);
            LValoare.setVisible(false);
            LObservatii.setVisible(false);
            LSaptamanaPredare.setVisible(false);
            noteView.setVisible(false);
        }
        else{
            NrLaborator.setVisible(true);
            idStudent.setVisible(true);
            idNota.setVisible(true);
            Observatii.setVisible(true);
            SaptamanaPredare.setVisible(true);
            Valoare.setVisible(true);
            filter3.setVisible(true);
            comboNote.setVisible(true);
            adauga3.setVisible(true);
            modifica3.setVisible(true);
            goleste3.setVisible(true);
            Lfiltrare3.setVisible(true);
            LidNota.setVisible(true);
            LidStudent.setVisible(true);
            LidNrLaborator.setVisible(true);
            LValoare.setVisible(true);
            LObservatii.setVisible(true);
            LSaptamanaPredare.setVisible(true);
            noteView.setVisible(true);
        }
    }

    private void showTemeDetails(Teme newValue) {
        if (newValue == null)
            clearFieldsT();
        else {
            idTema.setText(String.valueOf(newValue.getId()));
            cerinta.setText(newValue.getCerinta());
            deadline.setText(String.valueOf(newValue.getDeadline()));
        }
    }

    private void showNoteDetails(ObiectNota newValue) {
        if (newValue == null)
            clearFieldsN();
        else {
            Nota not=service.cautaNota(newValue.getIDNota());
            idNota.setText(String.valueOf(not.getId()));
            idStudent.setText(String.valueOf(not.getIdStudent()));
            NrLaborator.setText(String.valueOf(not.getIdTema()));
            Valoare.setText(String.valueOf(not.getValoare()));
        }
    }

    private void showStudentiDetails(Studenti newValue) {
        if (newValue == null)
            clearFieldsS();
        else {
            id.setText(String.valueOf(newValue.getId()));
            nume.setText(newValue.getNume());
            grupa.setText(String.valueOf(newValue.getGrupa()));
            email.setText(newValue.getEmail());
            profesor.setText(newValue.getCadruDidactic());
        }
    }

    //!!!

//    private ChangeListener<Studenti> tableItemListener(){ene
//        return (observable, oldValue, newValue)->showStudentiDetails(newValue);
//    }

   /* private void fillWithData(Studenti student){
        if(student==null){
            nume.setText("");
            email.setText("");
            grupa.setText("");
            profesor.setText("");
            id.setText("");
        }
        else{
            nume.setText(student.getNume());
            profesor.setText(student.getCadruDidactic());
            email.setText(student.getEmail());
            grupa.setText(String.valueOf(student.getGrupa()));
            id.setText(String.valueOf(student.getId()));
        }
    }*/

    @FXML
    private void clearFieldsS() {
        id.setText("");
        nume.setText("");
        grupa.setText("");
        email.setText("");
        profesor.setText("");
    }

    @FXML
    private void clearFieldsT(){
        idTema.setText("");
        cerinta.setText("");
        deadline.setText("");
    }

    @FXML
    private void clearFieldsN(){
        idNota.setText("");
        NrLaborator.setText("");
        Observatii.setText("");
        SaptamanaPredare.setText("");
        idStudent.setText("");
        Valoare.setText("");
    }

    private void loadStudenti() {
        List<Studenti> list = service.getStudInLista();
        model = FXCollections.observableArrayList(list);
        studentiView.setItems(model);
    }

    private void loadTeme() {
        List<Teme> list = service.getTemeInLista();

        model2 = FXCollections.observableArrayList(list);
        temeView.setItems(model2);
    }

    private void loadNote() {
        List<ObiectNota> list2=new ArrayList<>();
        List<Nota> list = service.getNotaInLista();
        for(Nota n:list){
            list2.add(new ObiectNota(n.getId(),n.getValoare(),service.cautaStudent(n.getIdStudent()).getNume(),n.getIdTema()));
        }
        model3 = FXCollections.observableArrayList(list2);
        noteView.setItems(model3);
    }


    private void loadDataHandler() {
        loadStudenti();
        loadTeme();
        loadNote();
    }

    public void addButtonHandler() {
        try {
            service.addStudent( nume.getText(), Integer.parseInt(grupa.getText()), email.getText(), profesor.getText());
            // loadStudenti();
        } catch (Exception e) {
            showErrorMessage(e.toString());
        }
    }

    public void addButtonHandlerTeme() {
        try {
            service.addTema(Integer.parseInt(idTema.getText()),cerinta.getText(),Integer.parseInt(deadline.getText()));
            // loadStudenti();
        } catch (Exception e) {
            showErrorMessage(e.toString());
        }
    }

    public void addButtonHandlerNote() {
        try {
            service.adaugareNota(Integer.parseInt(idStudent.getText()),Integer.parseInt(NrLaborator.getText()),Integer.parseInt(Valoare.getText()),Integer.parseInt(SaptamanaPredare.getText()),Observatii.getText());
            // loadStudenti();
        } catch (Exception e) {
            showErrorMessage(e.toString());
        }
    }

    public void deleteButtonHandler() {

        Studenti student = (Studenti) studentiView.getSelectionModel().getSelectedItem();
        int index = studentiView.getSelectionModel().getFocusedIndex();
        try {
            service.removeStudent(student.getId());
            // studentiView.getSelectionModel().select(index==model.size() ? index-1 : index);
            showMessage(Alert.AlertType.INFORMATION, "Ati sters studentul: " + student);
        } catch (Exception e) {
            showErrorMessage(e.toString());
        }
    }

    public void deleteButtonHandlerTeme() {

        Teme tema = (Teme) temeView.getSelectionModel().getSelectedItem();
        int index = temeView.getSelectionModel().getFocusedIndex();
        try {
            service.removeTema(tema.getId());
            // studentiView.getSelectionModel().select(index==model.size() ? index-1 : index);
            showMessage(Alert.AlertType.INFORMATION, "Ati sters tema: " + tema);
        } catch (Exception e) {
            showErrorMessage(e.toString());
        }
    }



    public void filtrareStudHandller(){
        if(!(filter.getText().equals(""))) {
            List<Studenti> rezultat ;
            if (comboStudenti.getSelectionModel().getSelectedItem().equals("grupa")) {
                rezultat = service.filterGrupa(Integer.parseInt(filter.getText()));
            }
            else if(comboStudenti.getSelectionModel().getSelectedItem().equals("email")) {
                rezultat = service.filterEmail(filter.getText());
            }
            else if(comboStudenti.getSelectionModel().getSelectedItem().equals("cadru didactic")) {
                rezultat = service.filterCadruDidactic(filter.getText());
            }
            else{
                rezultat=service.getStudInLista();
            }


            model.setAll(rezultat);
        }
        else
            showErrorMessage("Filtrul este gol!");
    }

    public void filtrareTemeHandller(){
        if(!(filter2.getText().equals(""))) {
            List<Teme> rezultat ;
            if (comboTeme.getSelectionModel().getSelectedItem().equals("cerinta")) {
                rezultat = service.filterCerinta(filter2.getText());
            }
            else if(comboTeme.getSelectionModel().getSelectedItem().equals("deadline")) {
                rezultat = service.filterDeadline(Integer.parseInt(filter2.getText()));
            }
            else if(comboTeme.getSelectionModel().getSelectedItem().equals("deadline between")) {
                String[] s=filter2.getText().split(";");
                rezultat = service.filterDeadlineBetween(Integer.parseInt(s[0]),Integer.parseInt(s[1]));
            }
            else{
                rezultat=service.getTemeInLista();
            }


            model2.setAll(rezultat);
        }
        else
            showErrorMessage("Filtrul este gol!");
    }

    public void filtrareNoteHandller(){
        if(!(filter3.getText().equals(""))) {
            List<Nota> rezultat ;
            if (comboNote.getSelectionModel().getSelectedItem().equals("idStudent")) {
                rezultat = service.filterIdStudent(Integer.parseInt(filter3.getText()));
            }
            else if(comboNote.getSelectionModel().getSelectedItem().equals("idTema")) {
                rezultat = service.filterIdTema(Integer.parseInt(filter3.getText()));
            }
            else if(comboNote.getSelectionModel().getSelectedItem().equals("note mai mici decat")) {
                rezultat = service.filterSubNota(Integer.parseInt(filter3.getText()));
            }
            else{
                rezultat=service.getNotaInLista();
            }
            List<ObiectNota> list=new ArrayList<>();
            for(Nota n:rezultat){
                list.add(new ObiectNota(n.getId(),n.getValoare(),service.cautaStudent(n.getIdStudent()).getNume(),n.getIdTema()));
            }
            model3.setAll(list);
        }
        else
            showErrorMessage("Filtrul este gol!");
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
        message.setContentText(text);
        message.showAndWait();
    }

    public void updateButtonHandler() {
        Studenti student = (Studenti) studentiView.getSelectionModel().getSelectedItem();
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
    }

    public void updateButtonHandlerTeme() {
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
    }

    public void updateButtonHandlerNote() {
        Nota nota = (Nota) noteView.getSelectionModel().getSelectedItem();
        try {
            if (nota != null) {
                String val = Valoare.getText();
                String obs = Observatii.getText();
                String sp = SaptamanaPredare.getText();
                service.updateNota(nota.getId(), Integer.parseInt(val), Integer.parseInt(sp), obs);
            } else {
                showErrorMessage("Field-urile sunt goale!");
            }
        }catch (Exception e){
            showErrorMessage(e.toString());
        }
    }


    @Override
    public void notifyEvent(ListEvent e) {
        List<Studenti> list = service.getStudInLista();
        model.setAll(list);
        List<Teme> list2=service.getTemeInLista();
        model2.setAll(list2);
        List<ObiectNota> list31=new ArrayList<>();
        List<Nota> list3=service.getNotaInLista();
        for(Nota n:list3){
            list31.add(new ObiectNota(n.getId(),n.getValoare(),service.cautaStudent(n.getIdStudent()).getNume(),n.getIdTema()));
        }
        model3.setAll(list31);
    }
}
