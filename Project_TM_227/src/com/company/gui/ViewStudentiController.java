package com.company.gui;

import com.company.service.Service;
import javafx.beans.binding.Bindings;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
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

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


public class ViewStudentiController {
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
    public Label medieL;
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

    public PieChart chart;


    private ObservableList<Student> studentObservableList;
    private ObservableList<Tema> temaObservableList;
    private ObservableList<Nota> notaObservableList;
    public Service service;
    public int idStudent;
    private final static int rowsPerPage = 20;


    public ViewStudentiController() {
    }

    @FXML
    private void initialize(){
        idCT.setCellValueFactory(new PropertyValueFactory<Tema, Integer>("Id"));
        cerintaCT.setCellValueFactory(new PropertyValueFactory<Tema, String>("cerinta"));
        deadlineCT.setCellValueFactory(new PropertyValueFactory<Tema, Integer>("deadline"));


        idCN.setCellValueFactory(new PropertyValueFactory<Nota, Integer>("Id"));
        idSCN.setCellValueFactory(new PropertyValueFactory<Nota, Integer>("idStudent"));
        idTCN.setCellValueFactory(new PropertyValueFactory<Nota, Integer>("idTema"));
        valoareCN.setCellValueFactory(new PropertyValueFactory<Nota, Integer>("valoare"));
        observatiCN.setCellValueFactory(new PropertyValueFactory<Nota, String>("observatie"));

    }


    public void setService(Service s, Stage st, int id){
        this.service = s;
        this.stage = st;
        this.idStudent = id;
        loadData();

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
        numeStudentF.setTooltip(new Tooltip("Campul nu poate fii editat"));
    }

    public void loadData(){
        DecimalFormat df = new DecimalFormat("0.00");
        temaObservableList = FXCollections.observableArrayList();
        for(Tema el : service.getTeme()){
            temaObservableList.add(el);
        }
        temeTabel.setItems(temaObservableList);

        notaObservableList = FXCollections.observableArrayList();
        double medie = 0;
        List<Nota> notas = service.getNoteStudent(idStudent);
        for(Nota el : notas){
            notaObservableList.add(el);
            medie +=el.getValoare();
        }
        long predate = notas.size();
        long teme = service.getNrTeme();
        long nepedate = teme-predate;

        medieL.setText(String.valueOf(df.format(medie/teme)));
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                        new PieChart.Data("Predate", predate),
                        new PieChart.Data("Nepredate", nepedate));

        chart.setData(pieChartData);

        pieChartData.forEach(data ->
                data.nameProperty().bind(
                        Bindings.concat(
                                data.getName(), " ", data.pieValueProperty()
                        )
                )
        );


        noteTabel.setItems(notaObservableList);
    }

    private void showTema(Tema t) {
        if(t != null) {
            idFT.setText(String.valueOf(t.getId()));
            cerintaFT.setText(t.getCerinta());
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


    private void showInformation(Alert.AlertType type, String titlu, String mesaj){
        Alert alert = new Alert(type);
        alert.setHeaderText(titlu);
        alert.setContentText(mesaj);
        alert.showAndWait();
    }
}
