package com.company.GUI;

import com.company.Domain.Globals;
import com.company.Domain.Student;
import com.company.Domain.Tema;
import com.company.Service.Service;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfImage;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.transform.Transform;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import javax.print.Doc;
import java.awt.event.ActionEvent;
import java.io.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RaportsCtrl {
    public BorderPane borderPane;
    public Service service;
    public int lastRaportType =0;
    public Chart chart;

    public void initialize()
    {
        borderPane.getStylesheets().clear();
        lastRaportType = 0;
    }

    public void mediaChart()
    {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        BarChart<String,Number> barChart = new BarChart<String,Number>(xAxis,yAxis);
        barChart.setPrefSize(400,400);
        barChart.setMaxSize(400,300);
        xAxis.setLabel("Name");
        yAxis.setLabel("Media");
        XYChart.Series series = new XYChart.Series();
        ArrayList<Double> info = service.raportMediePonderata();
        ArrayList<Student> students = new ArrayList<>();
        students.addAll((Collection<? extends Student>) service.getStudents());
        for(int i=0;i<2*students.size();i+=2)
        {
            Student student = null;
            for(Student st:students)
            {
                if(st.getID() == info.get(i).intValue()) {
                    student = st;
                    break;
                }
            }
            series.getData().add(new XYChart.Data(student.getNume(), info.get(i+1)));
        }
        barChart.getData().add(series);
        barChart.setLegendVisible(false);
        borderPane.setCenter(barChart);
        barChart.getParent().setStyle("-fx-background-color:white");
        Pane pane = (Pane) ((BorderPane)barChart.getParent()).getRight();
        pane.getStyleClass().add("pane");
        barChart.getParent().getStyleClass().clear();
        barChart.setStyle("-fx-background-color: white");
        lastRaportType=1;
        barChart.setStyle(null);
    }

    public void hardest3()
    {
        ArrayList<Tema> hardest = service.hardest3(new ArrayList<Integer>());
        FXMLLoader loaderTop = new FXMLLoader(getClass().getResource("TopLabels.fxml"));
        try {
            Parent parent = loaderTop.load();
            TopLabelsCtrl ctrl = loaderTop.getController();
            AnchorPane pane = new AnchorPane();
            if(hardest.get(0)!=null)
            {
                ctrl.nr1.setText("1# " + hardest.get(0).getDescriere());
                ctrl.nr1.setFont(new Font(40));
                ctrl.nr1.setTextFill(Color.GOLD);

                ctrl.nr1.setVisible(true);
            }
            if(hardest.get(1)!=null)
            {
                ctrl.nr2.setText("2# " + hardest.get(1).getDescriere());
                ctrl.nr2.setFont(new Font(30));
                ctrl.nr2.setTextFill(Color.SILVER);

                ctrl.nr2.setVisible(true);
            }
            if(hardest.get(2)!=null)
            {
                ctrl.nr3.setText("3# " + hardest.get(2).getDescriere());
                ctrl.nr3.setFont(new Font(20));
                ctrl.nr3.setTextFill(Color.BROWN);

                ctrl.nr3.setVisible(true);
            }

            borderPane.setCenter(parent);
            parent.getParent().getStyleClass().clear();
            parent.setStyle("-fx-background-color: white");
            ctrl.nr1.setStyle("-fx-text-fill: gold");
            ctrl.nr2.setStyle("-fx-text-fill: silver");
            ctrl.nr3.setStyle("-fx-text-fill: sandybrown");
        } catch (IOException e) {
            e.printStackTrace();
        }
        lastRaportType=2;
    }

    public void inExamenChart()
    {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        BarChart<String,Number> barChart = new BarChart<String,Number>(xAxis,yAxis);
        barChart.setPrefSize(400,400);
        barChart.setMaxSize(400,300);
        xAxis.setLabel("Name");
        yAxis.setLabel("Media");
        XYChart.Series series = new XYChart.Series();
        ArrayList<Double> info = service.raportMediePonderata();
        ArrayList<Student> students = new ArrayList<>();
        students.addAll((Collection<? extends Student>) service.getStudents());
        for(int i=0;i<2*students.size();i+=2)
        {
            Student student = null;
            for(Student st:students)
            {
                if(st.getID() == info.get(i).intValue()) {
                    student = st;
                    break;
                }
            }
            XYChart.Data data = new XYChart.Data(student.getNume(),info.get(i+1));
            data.nodeProperty().addListener(new ChangeListener<Node>() {
                @Override public void changed(ObservableValue<? extends Node> ov, Node oldNode, Node newNode) {
                    if (newNode != null) {
                        if (Double.valueOf((Double) data.getYValue()) > 4 ) {
                            newNode.setStyle("-fx-bar-fill: springgreen;");
                        }
                        else {
                            newNode.setStyle("-fx-bar-fill: firebrick;");
                        }
                    }
                }
            });
            series.getData().add(data);

        }
        barChart.getData().add(series);
        barChart.setLegendVisible(false);
        borderPane.setCenter(barChart);
        barChart.getParent().getStyleClass().clear();
        barChart.setStyle("-fx-background-color: white");
        chart=barChart;
        lastRaportType=3;
    }


    public void noPenality()
    {
        ListView<String> listView = new ListView<>();
        ArrayList<String> students = new ArrayList<>();
        for (Student st : service.noPenalityStudents()) {
            students.add("" + st.getID() + ": " + st.getNume() + "  " + st.getGrupa() + "  " + st.getEmail() + "  " + st.getProfLab());
        }
        ObservableList<String> obsList = FXCollections.observableList(students);
        listView.getItems().addAll(obsList);
        borderPane.setCenter(listView);
        lastRaportType=4;
    }


    public void exportImage() {
        FileChooser fc = new FileChooser();
        fc.setInitialDirectory(new File(System.getProperty("user.home") + "\\Desktop"));
        File savingFile = fc.showSaveDialog(null);

        if(lastRaportType!=0)
        {
            SnapshotParameters param = new SnapshotParameters();
            param.setDepthBuffer(true);
            param.setFill(Color.WHITE);

            SnapshotParameters sp = new SnapshotParameters();
            sp.setTransform(Transform.scale(1.45,1.45));
            WritableImage image = borderPane.getCenter().snapshot(sp,null);
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(image,null),"png",new File("Data\\chart.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }

            Document document = new Document();
            try {
                //PdfWriter.getInstance(document, new FileOutputStream("Data\\Raport" + raportNumber + ".pdf"));
                PdfWriter.getInstance(document, new FileOutputStream(savingFile + ".pdf"));

                document.open();
                Paragraph par1 = new Paragraph();
                par1.setFont(new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.COURIER, 20, com.itextpdf.text.Font.BOLD));
                par1.add("Grades Management Application Raport");
                par1.setAlignment(Paragraph.ALIGN_CENTER);
                document.add(par1);

                Paragraph space = new Paragraph();
                space.add("\n");
                document.add(space);

                if(lastRaportType==1) {
                    Paragraph p = new Paragraph();
                    p.add("Media ponderata a studentilor:");
                    document.add(p);
                    document.add(space);
                    media(document);
                }
                if(lastRaportType==2) {
                    Paragraph p = new Paragraph();
                    p.add("Cele mai grele 3 teme:");
                    document.add(p);
                    document.add(space);
                }
                if(lastRaportType==3) {
                    Paragraph p = new Paragraph();
                    p.add("Studentii trecuti in examen:");
                    document.add(p);
                    document.add(space);
                }
                if(lastRaportType==4) {
                    Paragraph p = new Paragraph();
                    p.add("Studentii fara penalizare:");
                    document.add(p);
                    document.add(space);
                }

                if(lastRaportType!=4) {
                    try {
                        Image imagePdf = Image.getInstance("Data\\chart.png");
                        PdfImage imageToPdf = new PdfImage(imagePdf, "", null);
                        imagePdf.setAlignment(Image.ALIGN_CENTER);
                        document.add(imagePdf);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if(lastRaportType==2)
                    hardestText(document);
                if(lastRaportType==3)
                    inExamenText(document);
                if(lastRaportType==4)
                    noPenalityText(document);

                document.close();
            } catch (DocumentException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void media(Document document)
    {
        ArrayList<Double> info = service.raportMediePonderata();
        ArrayList<Student> students = new ArrayList<>();
        students.addAll((Collection<? extends Student>) service.getStudents());
        NumberFormat format = new DecimalFormat("#0.00");
        for(int i=0;i<2*students.size();i+=2)
        {
            Student student = null;
            for(Student st:students)
            {
                if(st.getID() == info.get(i).intValue()) {
                    student = st;
                    break;
                }
            }
            Paragraph studP = new Paragraph(student + "\nMedia:" + format.format(info.get(i+1)));
            try {
                document.add(studP);
            } catch (DocumentException e) {
                e.printStackTrace();
            }
        }
    }

    public void hardestText(Document document)
    {
        List<Integer> nr = new ArrayList<>();
        ArrayList<Tema> rez = service.hardest3(nr);
        ArrayList<Student> students = new ArrayList<Student>((Collection<? extends Student>)(service.getStudents()));

        try {
            NumberFormat format = new DecimalFormat("#0.00");
            Paragraph par = new Paragraph();
            par.add(rez.get(0) + " este cea mai grea tema cu " + nr.get(0) + " studenti penalizati pana in prezent, ceea ce inseamna " +format.format((double)((double)nr.get(0)*100/students.size())) + "% din studenti." );
            document.add(par);

            par = new Paragraph();
            par.add(rez.get(1) + " este a doua cea mai grea tema cu " + nr.get(1) + " studenti penalizati pana in prezent, ceea ce inseamna  " + format.format((double)((double)nr.get(1)*100/students.size())) + "% din studenti.");
            document.add(par);

            par = new Paragraph();
            par.add(rez.get(2) + " este a doua cea mai grea tema cu " + nr.get(2) + " studenti penalizati pana in prezent, ceea ce inseamna  " + format.format((double)((double)nr.get(2)*100/students.size())) + "% din studenti.");
            document.add(par);
        } catch (DocumentException e) {
            e.printStackTrace();
        }

    }

    public void inExamenText(Document document)
    {
        ArrayList<Double> info = service.raportMediePonderata();
        ArrayList<Student> students = new ArrayList<>();
        students.addAll((Collection<? extends Student>) service.getStudents());
        for(int i=0;i<2*students.size();i+=2) {
            Student student = null;
            for (Student st : students) {
                if (st.getID() == info.get(i).intValue()) {
                    student = st;
                    break;
                }
            }

            Paragraph par = new Paragraph();
            par.add(""+student);
            com.itextpdf.text.Font font;
            font = par.getFont();
            if(info.get(i+1) > 4)
                font.setColor(BaseColor.GREEN);
            else
                font.setColor(BaseColor.RED);
            try {
                document.add(par);
            } catch (DocumentException e) {
                e.printStackTrace();
            }
        }
    }

    public void noPenalityText(Document document)
    {
        ArrayList<Student> students = service.noPenalityStudents();
        for(Student student : students) {
            Paragraph paragraph = new Paragraph();
            paragraph.add(""+student);
            try {
                document.add(paragraph);
            } catch (DocumentException e) {
                e.printStackTrace();
            }
        }
    }

}
