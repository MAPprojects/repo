package sample;

import Domain.Globals;
import Repositories.SQLNotaRepo;
import Repositories.SQLStudentRepo;
import Repositories.SQLTemaRepo;
import Service.Service;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Rapoarte {

    private Service service;
    public Button btnExport;
    public Accordion accordion;
    public PieChart pieChart;
    public Label lblPercentage;
    public ListView<String> listView1;
    public ListView<String> listView2;
    public ListView<String> listView3;

    public void setService(Service service) {
        this.service = service;
        pieChart.setTitle("Situatie note");
        Integer noOfStudents = service.noOfStudents();
        ArrayList<String> finalMarks = service.finalMarks();
        Integer noOfPassedStudents = service.studentiPromovati(finalMarks).size();
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Admisi",noOfPassedStudents),
                new PieChart.Data("Respinsi",noOfStudents-noOfPassedStudents)
        );
        pieChart.setData(pieChartData);

        pieChart.getData().stream().forEach(
                data -> {
                    data.getNode().addEventHandler(MouseEvent.ANY, e-> {
                        String text = "Procentaj: " + String.format("%.1f%%", (data.getPieValue()*100/noOfStudents));
                        lblPercentage.setText(text);
                    });
                }
        );
        ObservableList<String> listViewItems = FXCollections.observableArrayList();
        String s;
        for(int i=0;i<finalMarks.size()-1;i+=2) {
            if(!Double.isNaN(Double.valueOf(finalMarks.get(i+1))))
                s = finalMarks.get(i) + " are medie de laborator " + finalMarks.get(i+1) + ".";
            else s = finalMarks.get(i) + " nu are inca o medie de laborator.";
            listViewItems.add(s);
        }
        listView1.setItems(listViewItems);
        ObservableList<String> listViewItems1 = FXCollections.observableArrayList();
        ArrayList<String> studentiLaZi = service.studentiLaZi();
        listViewItems1.addAll(studentiLaZi);
        listView2.setItems(listViewItems1);
        ObservableList<String> listViewItems2 = FXCollections.observableArrayList();
        ArrayList<String> mostDifficultHomeworks = service.mostDifficultHomeworks();
        Collections.sort(mostDifficultHomeworks, (o1, o2) -> -o1.split(" ")[1].compareTo(o2.split(" ")[1]));
        ArrayList<String> mostDifficultHomeworksAUX = new ArrayList<>();
        for(String st : mostDifficultHomeworks) {
            mostDifficultHomeworksAUX.add("La tema \"" + st.split(" ")[0] + "\" au fost penalizati " + st.split(" ")[1] + " studenti.");
        }
        listViewItems2.addAll(mostDifficultHomeworksAUX);
        listView3.setItems(listViewItems2);
    }

    @FXML
    private void closeAction() {
        Platform.exit();
        System.exit(0);
    }

    @FXML
    private void exportButtonActivation() {
        if(accordion.getPanes().get(0).isExpanded() || accordion.getPanes().get(1).isExpanded() || accordion.getPanes().get(2).isExpanded() || accordion.getPanes().get(3).isExpanded())
            btnExport.setDisable(false);
        else btnExport.setDisable(true);
    }

    @FXML
    private void FiltersWindow() {
        Parent FiltersWindow = null;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FiltersWindow.fxml"));
        try {
            FiltersWindow = loader.load();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR,"Something went wrong :(");
            alert.setTitle("");
            alert.setHeaderText("");
            alert.showAndWait();
        }
        FiltersWindow filtersWindow = loader.getController();
        filtersWindow.setService(service);
        Scene FiltersScene = new Scene(FiltersWindow);
        Stage FiltersStage = (Stage)btnExport.getScene().getWindow();
        FiltersStage.setTitle("Filtrari");
        FiltersScene.getStylesheets().add("Theme.css");
        FiltersScene.getStylesheets().add("scrollbar.css");
        FiltersStage.setScene(FiltersScene);
        FiltersStage.show();
    }

    @FXML
    private void goBackToCRUD() {
        Parent CRUDWindow = null;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainWindow.fxml"));
        try {
            CRUDWindow = loader.load();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR,"Something went wrong :(");
            alert.setTitle("");
            alert.setHeaderText("");
            alert.showAndWait();
        }
        MainWindow mainWindow = loader.getController();
        mainWindow.setService(service);
        SQLStudentRepo sqlStudentRepo = service.getStudentRepository();
        sqlStudentRepo.addObserver(mainWindow);
        SQLTemaRepo sqlTemaRepo = service.getTemaRepository();
        sqlTemaRepo.addObserver(mainWindow);
        SQLNotaRepo sqlNotaRepo = service.getNotaRepository();
        sqlNotaRepo.addObserver(mainWindow);
        Globals.getInstance().addObserver(mainWindow);
        Scene CRUDScene = new Scene(CRUDWindow);
        Stage CRUDStage = (Stage)btnExport.getScene().getWindow();
        //CRUDStage.setTitle("Lab6 - Main Window");
        CRUDStage.setScene(CRUDScene);
        CRUDScene.getStylesheets().add("Theme.css");
        CRUDScene.getStylesheets().add("scrollbar.css");
        CRUDStage.show();
    }

    @FXML
    private void exportToPDF() {
        FileChooser fc = new FileChooser();
        fc.setTitle("Alegeti locatia");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf");
        fc.getExtensionFilters().add(extFilter);
        File file = fc.showSaveDialog(btnExport.getScene().getWindow());
        if(file != null) {
            try {
                Document document = new Document();
                PdfWriter.getInstance(document, new FileOutputStream(file));
                document.open();
                if(accordion.getPanes().get(0).isExpanded()) {
                    for(String s : listView1.getItems())
                        document.add(new Paragraph(s));
                }
                else if(accordion.getPanes().get(1).isExpanded()) {
                    for(String s : listView3.getItems())
                        document.add(new Paragraph(s));
                    document.add(new Paragraph("Cea mai grea tema este: " + listView3.getItems().get(0).split(" ")[2]));
                }
                else if(accordion.getPanes().get(2).isExpanded()) {
                    ArrayList<String> finalMarks = service.finalMarks();
                    document.add(new Paragraph("Studentii care pot intra in examen sunt: " + service.studentiPromovati(finalMarks)));
                    String procentaj = String.format("%.1f%%",service.studentiPromovati(finalMarks).size()*100.0/service.noOfStudents());
                    document.add(new Paragraph("Procentajul studentilor care pot intra in examen este: " + procentaj));
                    procentaj = String.format("%.1f%%",100.0-service.studentiPromovati(finalMarks).size()*100.0/service.noOfStudents());
                    document.add(new Paragraph("Procentajul studentilor care NU pot intra in examen este: " + procentaj));
                    document.add(new Paragraph("Numar total studenti: " + service.noOfStudents()));
                }
                else if(accordion.getPanes().get(3).isExpanded()) {
                    for(String s : listView2.getItems())
                        document.add(new Paragraph(s));
                }
                document.close();
                Alert alert = new Alert(Alert.AlertType.INFORMATION,"Fisierul PDF a fost creat!");
                alert.setTitle("");
                alert.setHeaderText("");
                alert.showAndWait();
            } catch (DocumentException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR,"Eroare PDF");
                alert.setHeaderText("");
                alert.setTitle("");
                alert.showAndWait();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
