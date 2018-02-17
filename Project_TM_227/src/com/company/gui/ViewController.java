package com.company.gui;

import com.company.domain.*;
import com.company.service.Service;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ViewController {
    public Tab studentiTab;
    public TableView studentiTabel;
    public TableColumn idCS;
    public TableColumn numeCS;
    public TableColumn grupaCS;
    public TableColumn emailCS;
    public TableColumn cadruCS;
    public Tab temeTab;
    public TableView temeTabel;
    public TableColumn idCT;
    public TableColumn cerintaCT;
    public TableColumn deadlineCT;
    public Tab noteTab;
    public TableView noteTabel;
    public TableColumn idCN;
    public TableColumn idSCN;
    public TableColumn idTCN;
    public TableColumn valoareCN;
    public TableColumn observatiCN;
    public TextField idFS;
    public TextField numeFS;
    public TextField grupaFS;
    public TextField emailFS;
    public TextField cadruFS;
    public TextField idFT;
    public TextField cerintaFT;
    public TextField deadlineFT;
    public TextField idFN;
    public TextField idSFN;
    public TextField idTFN;
    public TextField valoareFN;
    public TextField observatiFN;
    public Button btnAddS;
    public Button btnDeleteS;
    public Button btnUpdateS;
    public Button btnClearS;
    public Button btnAddT;
    public Button btnUpdateT;
    public Button btnClearT;
    public Button btnAddN;
    public Button btnUpdateN;
    public Button btnClearN;
    public Button btnSetSapt;
    public Label saptamanaL;
    public Button btnFiltersS;
    public ImageView imageStudent;
    public ImageView imageTema;
    public ImageView imageNote;
    public Button btnFiltersT;
    public Button btnFiltersN;
    public TextField numeStudentF;
    public BorderPane borderS;
    public BorderPane borderT;
    public BorderPane borderN;

    public Pagination paginationT;
    public Pagination paginationS;
    public Pagination paginationN;

    public Button btnRapS;
    public Button btnRapT;
    public Button btnRapN;

    public Stage stage;


    private ObservableList<Student> studentObservableList;
    private ObservableList<Tema> temaObservableList;
    private ObservableList<Nota> notaObservableList;
    public Service service;
    private final static int rowsPerPage = 20;

    public ViewController() {
    }

    private Node createPageS(int pageIndex) {
        int fromIndex = pageIndex * rowsPerPage;
        int toIndex = Math.min(fromIndex + rowsPerPage, studentObservableList.size());
        studentiTabel.refresh();
        studentiTabel.setItems(FXCollections.observableArrayList(studentObservableList.subList(fromIndex, toIndex)));
        return new BorderPane(studentiTabel);
    }


    private Node createPageT(int pageIndex) {
        int fromIndex = pageIndex * rowsPerPage;
        int toIndex = Math.min(fromIndex + rowsPerPage, temaObservableList.size());
        temeTabel.refresh();
        temeTabel.setItems(FXCollections.observableArrayList(temaObservableList.subList(fromIndex, toIndex)));
        return new BorderPane(temeTabel);
    }


    private Node createPageN(int pageIndex) {
        int fromIndex = pageIndex * rowsPerPage;
        int toIndex = Math.min(fromIndex + rowsPerPage, notaObservableList.size());
        noteTabel.refresh();
        noteTabel.setItems(FXCollections.observableArrayList(notaObservableList.subList(fromIndex, toIndex)));
        return new BorderPane(noteTabel);
    }

    public void paginatS(){
        studentObservableList = FXCollections.observableArrayList();
        //studentObservableList.clear();
        for(Student el : service.getStudents()){
            studentObservableList.add(el);
        }
        int numOfPages = 1;
        if (studentObservableList.size() % rowsPerPage == 0) {
            numOfPages = studentObservableList.size() / rowsPerPage;
        } else if (studentObservableList.size() > rowsPerPage) {
            numOfPages = studentObservableList.size() / rowsPerPage + 1;
        }
        paginationS = new Pagination((numOfPages), 0);
        paginationS.setPageFactory(this::createPageS);
        borderS.setCenter(paginationS);

    }

    public void paginatT(){
        temaObservableList = FXCollections.observableArrayList();
        //temaObservableList.clear();
        for(Tema el : service.getTeme()){
            temaObservableList.add(el);
        }
        int numOfPages = 1;
        if (temaObservableList.size() % rowsPerPage == 0) {
            numOfPages = temaObservableList.size() / rowsPerPage;
        } else if (temaObservableList.size() > rowsPerPage) {
            numOfPages = temaObservableList.size() / rowsPerPage + 1;
        }
        paginationT = new Pagination((numOfPages), 0);
        paginationT.setPageFactory(this::createPageT);
        borderT.setCenter(paginationT);
    }

    public void paginatN(){
        notaObservableList = FXCollections.observableArrayList();
        //notaObservableList.clear();
        for(Nota el : service.getNote()){
            notaObservableList.add(el);
        }
        int numOfPages = 1;
        if (notaObservableList.size() % rowsPerPage == 0) {
            numOfPages = notaObservableList.size() / rowsPerPage;
        } else if (notaObservableList.size() > rowsPerPage) {
            numOfPages = notaObservableList.size() / rowsPerPage + 1;
        }
        paginationN = new Pagination((numOfPages), 0);
        paginationN.setPageFactory(this::createPageN);
        borderN.setCenter(paginationN);
    }

    @FXML
    private void initialize(){

        idCS.setCellValueFactory(new PropertyValueFactory<Student, Integer>("Id"));
        numeCS.setCellValueFactory(new PropertyValueFactory<Student, String>("nume"));
        grupaCS.setCellValueFactory(new PropertyValueFactory<Student, Integer>("grupa"));
        emailCS.setCellValueFactory(new PropertyValueFactory<Student, String>("email"));
        cadruCS.setCellValueFactory(new PropertyValueFactory<Student, Integer>("cadru_didactic"));

        idCT.setCellValueFactory(new PropertyValueFactory<Tema, Integer>("Id"));
        cerintaCT.setCellValueFactory(new PropertyValueFactory<Tema, String>("cerinta"));
        deadlineCT.setCellValueFactory(new PropertyValueFactory<Tema, Integer>("deadline"));


        idCN.setCellValueFactory(new PropertyValueFactory<Nota, Integer>("Id"));
        idSCN.setCellValueFactory(new PropertyValueFactory<Nota, Integer>("idStudent"));
        idTCN.setCellValueFactory(new PropertyValueFactory<Nota, Integer>("idTema"));
        valoareCN.setCellValueFactory(new PropertyValueFactory<Nota, Integer>("valoare"));
        observatiCN.setCellValueFactory(new PropertyValueFactory<Nota, String>("observatie"));

        imageStudent.setImage(new Image("com/company/image/students.gif"));
        imageTema.setImage(new Image("com/company/image/homeworks3.gif"));
        imageNote.setImage(new Image("com/company/image/grades.gif"));
    }

    public void setService(Service s, Stage st){
        this.service = s;
        this.stage = st;
        paginatS();
        paginatT();
        paginatN();

        studentiTabel.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Student>() {
            @Override
            public void changed(ObservableValue<? extends Student> observable, Student oldValue, Student newValue) {
                showStudent(newValue);
            }
        });

        temeTabel.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tema>() {
            @Override
            public void changed(ObservableValue<? extends Tema> observable, Tema oldValue, Tema newValue) {
                showTema(newValue);

            }
        });

        noteTabel.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Nota>() {
            @Override
            public void changed(ObservableValue<? extends Nota> observable, Nota oldValue, Nota newValue) {
                try {
                    showNota(newValue);
                } catch (Exception e) {
                    showInformation(Alert.AlertType.ERROR, "Eroare", "Nu exista acest student");
                }
            }
        });
        idFN.setTooltip(new Tooltip("Campul nu poate fii editat"));
        saptamanaL.setText(String.valueOf(service.getSaptamana()));
        numeStudentF.setTooltip(new Tooltip("Campul nu poate fii editat"));
    }

    public void loadData(){
        studentObservableList = FXCollections.observableArrayList();
        for(Student el : service.getStudents()){
            studentObservableList.add(el);
        }
        studentiTabel.setItems(studentObservableList);

        temaObservableList = FXCollections.observableArrayList();
        for(Tema el : service.getTeme()){
            temaObservableList.add(el);
        }
        temeTabel.setItems(temaObservableList);

        notaObservableList = FXCollections.observableArrayList();
        for(Nota el : service.getNote()){
            notaObservableList.add(el);
        }
        noteTabel.setItems(notaObservableList);
    }


    private void showStudent(Student s){
        if(s != null) {
            idFS.setText(String.valueOf(s.getId()));
            idFS.setDisable(true);
            numeFS.setText(s.getNume());
            grupaFS.setText(String.valueOf(s.getGrupa()));
            emailFS.setText(s.getEmail());
            cadruFS.setText(s.getCadru_didactic());
        }

    }

    @FXML
    private void clearStudent(){
        idFS.setText("");
        idFS.setDisable(false);
        numeFS.setText("");
        grupaFS.setText("");
        emailFS.setText("");
        cadruFS.setText("");
        studentiTabel.getSelectionModel().clearSelection();
    }


    private void showTema(Tema t) {
        if(t != null) {
            idFT.setText(String.valueOf(t.getId()));
            idFT.setDisable(true);
            cerintaFT.setText(t.getCerinta());
            cerintaFT.setDisable(true);
            deadlineFT.setText(String.valueOf(t.getDeadline()));
        }
    }

    @FXML
    private void clearTema(){
        idFT.setText("");
        idFT.setDisable(false);
        cerintaFT.setText("");
        cerintaFT.setDisable(false);
        deadlineFT.setText("");
        temeTabel.getSelectionModel().clearSelection();
    }


    private void showNota(Nota n) throws Exception {
        if(n != null) {
            idFN.setText(String.valueOf(n.getId()));
            idSFN.setText(String.valueOf(n.getIdStudent()));
            idTFN.setText(String.valueOf(n.getIdTema()));
            valoareFN.setText(String.valueOf(n.getValoare()));
            observatiFN.setText(String.valueOf(n.getObservatie()));
            Student s = service.findStudentForId(n.getIdStudent());
            numeStudentF.setText(s.getNume());

        }
    }

    @FXML
    private void clearNota(){
        idFN.setText("");
        idSFN.setText("");
        idTFN.setText("");
        valoareFN.setText("");
        observatiFN.setText("");
        numeStudentF.setText("");
        noteTabel.getSelectionModel().clearSelection();
    }



    public void addStudent(){

        try {
            int id = Integer.parseInt(idFS.getText().trim());
            int grupa = Integer.parseInt(grupaFS.getText().trim());
            service.addStudent(id,numeFS.getText().trim(),grupa,emailFS.getText().trim(),cadruFS.getText().trim());
            studentObservableList.clear();
            paginatS();
            showInformation(Alert.AlertType.INFORMATION, "Adaugare", "Studentul a fost adaugat");
        }
        catch (NumberFormatException ex){
            showInformation(Alert.AlertType.WARNING, "Eroare", "Id-ul si grupa trebuie sa fie de tip intreg");
        }
        catch (Exception e) {
            showInformation(Alert.AlertType.ERROR, "Eroare", e.getMessage());
        }
    }

    public void deleteStudent(){
        try {
            int id = Integer.parseInt(idFS.getText().trim());
            service.deleteStudent(id);
            studentObservableList.clear();
            notaObservableList.clear();
            paginatS();
            paginatN();
            showInformation(Alert.AlertType.INFORMATION, "Stergere", "Studentul a fost sters");
        }
        catch (NumberFormatException ex){
            showInformation(Alert.AlertType.WARNING, "Eroare", "Id-ul trebuie sa fie de tip intreg");
        }
        catch (Exception e) {
            showInformation(Alert.AlertType.ERROR, "Eroare", e.getMessage());
        }
    }

    public void updateStudent(){
        try {
            int id = Integer.parseInt(idFS.getText().trim());
            int grupa = Integer.parseInt(grupaFS.getText().trim());
            service.updateStudent(id, numeFS.getText().trim(), grupa, emailFS.getText().trim(), cadruFS.getText().trim());
            studentObservableList.clear();
            paginatS();
            showInformation(Alert.AlertType.INFORMATION, "Modificare", "Datele studentului au fost modificate");
        }
        catch (NumberFormatException ex){
            showInformation(Alert.AlertType.WARNING, "Eroare", "Id-ul trebuie sa fie de tip intreg");
        }
        catch (Exception e) {
            showInformation(Alert.AlertType.ERROR, "Eroare", e.getMessage());
        }
    }

    public void addTema(){
        try {
            int id = Integer.parseInt(idFT.getText().trim());
            int deadline = Integer.parseInt(deadlineFT.getText().trim());
            service.addTema(id,cerintaFT.getText().trim(),deadline);
            temaObservableList.clear();
            paginatT();
            showInformation(Alert.AlertType.INFORMATION, "Adaugare", "Tema a fost adaugata");
        }
        catch (NumberFormatException ex){
            showInformation(Alert.AlertType.WARNING, "Eroare", "Id-ul si deadline-ul trebuie sa fie de tip intreg");
        }
        catch (Exception e) {
            showInformation(Alert.AlertType.ERROR, "Eroare", e.getMessage());
        }
    }

    public void updateTema(){
        try {
            int id = Integer.parseInt(idFT.getText().trim());
            int deadline = Integer.parseInt(deadlineFT.getText().trim());
            service.updateDeadline(id, deadline);
            temaObservableList.clear();
            paginatT();
            showInformation(Alert.AlertType.INFORMATION, "Modificare", "Deadline-ul a fost modificat");
        }
        catch (NumberFormatException ex){
            showInformation(Alert.AlertType.WARNING, "Eroare", "Id-ul si deadline-ul trebuie sa fie de tip intreg");
        }
        catch (Exception e) {
            showInformation(Alert.AlertType.ERROR, "Eroare", e.getMessage());
        }
    }

    public void addNota(){
        try {
            int idS = Integer.parseInt(idSFN.getText().trim());
            int idT = Integer.parseInt(idTFN.getText().trim());
            int valoare =  Integer.parseInt(valoareFN.getText().trim());
            service.addNota(idS,idT,valoare,observatiFN.getText().trim());
            notaObservableList.clear();
            paginatN();
            showInformation(Alert.AlertType.INFORMATION, "Adaugare", "Nota a fost adaugata");
        }
        catch (NumberFormatException ex){
            showInformation(Alert.AlertType.WARNING, "Eroare", "Id-ul studentului, id-ul temei si valoarea trebuie sa fie de tip intreg");
        }
        catch (Exception e) {
            showInformation(Alert.AlertType.ERROR, "Eroare", e.getMessage());
        }
    }

    public void updateNota(){
        try {
            int idS = Integer.parseInt(idSFN.getText().trim());
            int idT = Integer.parseInt(idTFN.getText().trim());
            int valoare =  Integer.parseInt(valoareFN.getText().trim());
            service.changeNota(idS,idT,valoare,observatiFN.getText().trim());
            notaObservableList.clear();
            paginatN();
            showInformation(Alert.AlertType.INFORMATION, "Modificare", "Nota a fost modificata");
        }
        catch (NumberFormatException ex){
            showInformation(Alert.AlertType.WARNING, "Eroare", "Id-ul studentului, id-ul temei si valoarea trebuie sa fie de tip intreg");
        }
        catch (Exception e) {
            showInformation(Alert.AlertType.ERROR, "Eroare", e.getMessage());
        }
    }

    public void setSaptamana() throws Exception{
        FXMLLoader loader =new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/company/gui/SettingsService.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setOpacity(1);
        stage.setTitle("Setare saptamana curenta");
        stage.setScene(new Scene(root));
        SettingsServiceController controller = loader.getController();
        controller.setService(service,stage);
        stage.showAndWait();
        saptamanaL.setText(String.valueOf(service.getSaptamana()));
    }


    public void filtersStudent() throws IOException {
        FXMLLoader loader =new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/company/gui/FiltersStudent.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setOpacity(1);
        stage.setTitle("Filtrari student");
        stage.setScene(new Scene(root));
        FiltersStudentController controller = loader.getController();
        controller.setService(service,stage);
        stage.showAndWait();
    }

    public void filtersTema() throws IOException {
        FXMLLoader loader =new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/company/gui/FiltersTema.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setOpacity(1);
        stage.setTitle("Filtrari teme");
        stage.setScene(new Scene(root));
        FiltersTemaController controller = loader.getController();
        controller.setService(service,stage);
        stage.showAndWait();
    }

    public void filtersNota() throws IOException {
        FXMLLoader loader =new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/company/gui/FiltersNota.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setOpacity(1);
        stage.setTitle("Filtrari Nota");
        stage.setScene(new Scene(root));
        FiltersNotaController controller = loader.getController();
        controller.setService(service,stage);
        stage.showAndWait();
    }

    public void rapStudenti(){
        try {
            service.raportStudenti();
        } catch (FileNotFoundException e) {
            showInformation(Alert.AlertType.ERROR, "Eroare", "Documentul pe care doriti sa il modificati este deschis in alta parte");
        }
    }

    public void rapTeme(){
        try {
            service.raportTeme();
        } catch (FileNotFoundException e) {
            showInformation(Alert.AlertType.ERROR, "Eroare", "Documentul pe care doriti sa il modificati este deschis in alta parte");
        }
    }

    public void rapNote(){
        try {
            service.raportNote();
        } catch (FileNotFoundException e) {
            showInformation(Alert.AlertType.ERROR, "Eroare", "Documentul pe care doriti sa il modificati este deschis in alta parte");
        }

    }


    private void showInformation(Alert.AlertType type, String titlu, String mesaj){
        Alert alert = new Alert(type);
        alert.setHeaderText(titlu);
        alert.setContentText(mesaj);
        alert.setResizable(true);
        alert.showAndWait();
    }
}
