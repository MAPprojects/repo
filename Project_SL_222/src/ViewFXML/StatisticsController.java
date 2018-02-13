package ViewFXML;

import Domain.Nota;
import Domain.Student;
import Domain.Tema;
import Service.NotaService;
import Service.StudentService;
import Service.TemaService;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.*;
import com.itextpdf.kernel.pdf.PdfDocument;
//import com.itextpdf.kernel.pdf.PdfWriter;
//import com.itextpdf.layout.Document;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.awt.*;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.stream.Collectors;

import java.io.FileOutputStream;
import java.io.IOException;

//import com.itextpdf.text.Document;
//import com.itextpdf.text.pdf.PdfWriter;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import javafx.scene.layout.AnchorPane;

public class StatisticsController {
    NotaService serviceNota;
    StudentService serviceStudent;
    TemaService serviceTema;
    ObservableList<Double> averages=FXCollections.observableArrayList();
    ObservableList<String> names=FXCollections.observableArrayList();
    ObservableList<String> teme=FXCollections.observableArrayList();
    ObservableList<Double> penalizari=FXCollections.observableArrayList();

    ObservableList<Student> model1;
    ObservableList<Tema> model2;
    ObservableList<Nota> model3;
    @FXML
    AnchorPane anchor;
    @FXML
    MenuItem item1;
    @FXML
    MenuItem item2;
    @FXML
    MenuItem item3;
    @FXML
    MenuItem item4;
    @FXML
    BarChart<String,Double> barChart;
    @FXML
    CategoryAxis x;
    @FXML
    NumberAxis y;

    @FXML
    private Button export;
    @FXML
    private MenuButton charts;

    XYChart.Series<String,Double> series = new XYChart.Series<>();

    public void setService(NotaService service,StudentService service2,TemaService service3) {
        this.serviceNota=service;
        this.serviceStudent=service2;
        this.serviceTema=service3;
        this.model1=FXCollections.observableArrayList(service2.getAllS());
        this.model2=FXCollections.observableArrayList(service3.getAllT());
        this.model3= FXCollections.observableArrayList(service.getAllN());
    }

    @FXML
    public void initialize(){
        barChart.setLegendVisible(false);
        String iconPath2="if_pdf_272705.png";
        Image icon2=new Image(getClass().getResourceAsStream(iconPath2),30,35,false,false);
        ImageView img2=new ImageView(icon2);
        img2.fitHeightProperty();
        img2.fitWidthProperty();
        export.setGraphic(new ImageView(icon2));
        String iconPath3="if_pie_chart_61654.png";
        Image icon3=new Image(getClass().getResourceAsStream(iconPath3),30,32,false,false);
        ImageView img3=new ImageView(icon3);
        img3.fitHeightProperty();
        img3.fitWidthProperty();
        charts.setGraphic(new ImageView(icon3));
    }
    @FXML
    public void handleClearChart(){
        barChart.getData().clear();
        series.getData().clear();
        averages.clear();
        names.clear();
        teme.clear();
        penalizari.clear();
    }
    @FXML
    public void handleAverageStudents(){
        //averageStudents();
        //barChart.setTitle("Average of grades for each student");
        series.getData().clear();
        barChart.getData().clear();
        averages.clear();
        names.clear();
        x.setLabel("ID of students");
        y.setLabel("Average");
        double sum,media;
        for(Student stud:model1) {
            sum=0;
            for (Nota nota : model3) {
                if (stud.getID() == nota.getIdStudent())
                    sum = sum + nota.getValoare();
            }
            media=sum/serviceTema.getAllT().size();
            averages.add(media);
            names.add(stud.getID().toString());
        }
        x.setCategories(names);
        for(int i=0;i<names.size();i++)
            series.getData().add(new XYChart.Data<>(names.get(i),averages.get(i)));
        barChart.getData().add(series);
    }
    @FXML
    public void handleAssignment(){
        //barChart.setTitle("Number of students with grade under 10 for each assignment");
        series.getData().clear();
        barChart.getData().clear();
        teme.clear();
        penalizari.clear();
        x.setLabel("Assignment name");
        y.setLabel("Number of students");
        double studs;
        for(Tema tema:model2){
            studs=0;
            for(Nota nota:model3){
                if(tema.getID()==nota.getNrTema() && nota.getValoare()!=10)
                    studs++;
            }
            teme.add(tema.getDescriere());
            penalizari.add(studs);
        }
        x.setCategories(teme);
        XYChart.Series<String,Double> series = new XYChart.Series<>();
        for(int i=0;i<teme.size();i++)
            series.getData().add(new XYChart.Data<>(teme.get(i),penalizari.get(i)));
        barChart.getData().add(series);
    }

    @FXML
    public void handleExam(){
        //barChart.setTitle("Students with the average over or equal with 4");
        series.getData().clear();
        barChart.getData().clear();
        averages.clear();
        names.clear();
        x.setLabel("ID of students");
        y.setLabel("Average");
        double sum,media;
        for(Student stud:model1) {
            sum=0;
            for (Nota nota : model3) {
                if (stud.getID() == nota.getIdStudent())
                    sum = sum + nota.getValoare();
            }
            media=sum/serviceTema.getAllT().size();
            if(media>=4) {
                averages.add(media);
                names.add(stud.getID().toString());
            }
        }
        x.setCategories(names);
        for(int i=0;i<names.size();i++)
            series.getData().add(new XYChart.Data<>(names.get(i),averages.get(i)));
        barChart.getData().add(series);
    }

    @FXML
    public void handle3Averages(){
        series.getData().clear();
        barChart.getData().clear();
        averages.clear();
        names.clear();
        x.setLabel("ID of students");
        y.setLabel("Average");
        double sum,media;
        for(Student stud:model1) {
            sum=0;
            for (Nota nota : model3)
                if (stud.getID() == nota.getIdStudent())
                    sum = sum + nota.getValoare();
            media=sum/serviceTema.getAllT().size();
            averages.add(media);
            names.add(stud.getID().toString());
        }
        x.setCategories(names);
        if(averages.size()<=3) {
            for (int i = 0; i < names.size(); i++)
                series.getData().add(new XYChart.Data<>(names.get(i), averages.get(i)));
        }
        else {
            Double max1 = Double.MIN_VALUE, max2 = Double.MIN_VALUE, max3 = Double.MIN_VALUE;
            int i;
            String id1 = "", id2 = "", id3 = "";
            for (i = 0; i < averages.size(); i++) {
                if (averages.get(i) > max1) {
                    max3 = max2;
                    max2 = max1;
                    max1 = averages.get(i);
                    id3=id2;
                    id2=id1;
                    id1 = names.get(i);
                } else if (averages.get(i) > max2) {
                    max3 = max2;
                    max2 = averages.get(i);
                    id3 = id2;
                    id2 = names.get(i);
                } else if (averages.get(i) > max3) {
                    max3 = averages.get(i);
                    id3 = names.get(i);
                }
            }
            series.getData().addAll(new XYChart.Data<>(id1,max1),new XYChart.Data<>(id2,max2),new XYChart.Data<>(id3,max3));
        }
        barChart.getData().add(series);
    }

    @FXML
    public void writeChartToPDF() {
        String fileName="/Users/laurascurtu/IdeaProjects/Lab5GUI/MyPDF.pdf";
        try {

            PdfDocument pdfDoc;
            Document doc;
            Image img = barChart.snapshot(null, null);
            ImageData imgData = ImageDataFactory.create(SwingFXUtils.fromFXImage(img, null), null);
            com.itextpdf.layout.element.Image pdfImg = new com.itextpdf.layout.element.Image(imgData);


            PdfWriter writer = new PdfWriter(new FileOutputStream(fileName));
            pdfDoc = new PdfDocument(writer);
            doc = new Document(pdfDoc);

            doc.add(pdfImg);
            doc.close();
            MessageAlert.showInfoMessage(null, "Chart exported to pdf successfully!");
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }
}
