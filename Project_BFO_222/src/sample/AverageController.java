package sample;

import Domain.Student;
import Service.Service;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.element.Paragraph;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.print.*;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.*;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1CFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import sun.font.FontFamily;

import javax.imageio.ImageIO;
import javax.print.*;
import javax.sql.rowset.serial.SerialException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.xml.bind.annotation.XmlAnyAttribute;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import javafx.print.PrinterJob;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.function.DoubleBinaryOperator;
import java.util.stream.Collectors;




public class AverageController implements Observer {
    @FXML
    private  BarChart<Double,Integer> barChart;
    @FXML
    private CategoryAxis categoryAxis;
    @FXML
    private NumberAxis numberAxis;
    @FXML
    private ListView<String> listView;
    private ListView<String> listPromovati;
    @FXML
    private PieChart pieChart;

    private Scene scene;

    public static final String TEXT
            = "1.txt";
    public static final String DEST
            = "statistica.pdf";

    private Service service;
    public AverageController(){

    }

    public void loadStudents(){
        Iterable<Student> list=service.getAllStud();
        List<String> inList=new ArrayList<>();
        for(Student s:list){
            if(service.getMedie(s.getId())>0)
            inList.add("Nume:"+s.getNume()+"\nGrupa:"+s.getGrupa()+"\nMedia:"+service.getMedie(s.getId())+"\n");
        }
        listView.setItems(FXCollections.observableArrayList(inList));
    }

    public BarChart<Double,Integer> getForScene(){
        Axis xAxis,yAxis;
        xAxis=new CategoryAxis();
        yAxis=new NumberAxis();
        xAxis.setLabel("Medie");
        yAxis.setLabel("NumberOf");

        BarChart<Double,Integer> barChart2=new BarChart<Double, Integer>(xAxis,yAxis);
        HashMap<Double,Integer> medii=new HashMap<>();
        XYChart.Series set=new XYChart.Series();
        for(Double i=1.0;i<=10;i=i+1){
            medii.put(i,0);
        }

        for(Student student:service.getAllStud()){
            double medie=service.getMedie(student.getId());
            if(medii.containsKey(medie)){
                medii.put(medie,medii.get(medie)+1);
            }else{
                medii.put(medie,1);
            }
        }


        if(medii.keySet().size()>0) {
            List<Double> keys=service.fromIterableToList(medii.keySet()).stream().sorted((x,y)->{
                if(x<y)
                    return -1;
                else if(x>y)
                    return 1;
                else
                    return 0;
            }).collect(Collectors.toList());
            for (Double value : keys) {
                if(value!=0) {
                    set.getData().add(new XYChart.Data(value.toString(), medii.get(value)));
                }
            }
        }

        barChart2.getData().addAll(set);
        barChart2.setAnimated(false);
        return barChart2;
    }
    public void loadBarChart(){
        HashMap<Double,Integer> medii=new HashMap<>();
        XYChart.Series set=new XYChart.Series();
        for(Double i=1.0;i<=10;i=i+1){
            medii.put(i,0);
        }

        for(Student student:service.getAllStud()){
            double medie=service.getMedie(student.getId());
            if(medii.containsKey(medie)){
                medii.put(medie,medii.get(medie)+1);
            }else{
                medii.put(medie,1);
            }
        }


        if(medii.keySet().size()>0) {
            List<Double> keys=service.fromIterableToList(medii.keySet()).stream().sorted((x,y)->{
                if(x<y)
                    return -1;
                else if(x>y)
                    return 1;
                    else
                        return 0;
            }).collect(Collectors.toList());
            for (Double value : keys) {
                if(value!=0) {
                    set.getData().add(new XYChart.Data(value.toString(), medii.get(value)));
                }
            }
        }

        barChart.getData().addAll(set);

        AnchorPane anchorPane=new AnchorPane(getForScene());
        scene = new Scene(anchorPane, 600, 400);
        Stage newStage=new Stage();


        saveAsPng(scene,"barChart.png");

    }


    public void saveAsPng(Scene scene, String path) {
        WritableImage image = scene.snapshot(null);
        File file = new File(path);
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public PDPage createNewPage(PDDocument document,String grupa) throws  IOException{
        try {
            PDPage page = new PDPage();
            PDPageContentStream content = new PDPageContentStream(document, page);
            content.beginText();
            content.setFont(PDType1Font.TIMES_BOLD,24);
            content.moveTextPositionByAmount(250,750);
            content.showText("Grupa "+grupa);
            content.endText();
            int startP=700;
            content.setFont(PDType1Font.TIMES_ROMAN, 16);
            for(Student s:service.getAllStud()) {
                if (s.getGrupa().equals(grupa)) {
                    content.beginText();
                    content.moveTextPositionByAmount(20, startP);
                    if (service.getMedie(s.getId()) >= 4.0) {
                        content.setNonStrokingColor(Color.GREEN);
                    } else {
                        content.setNonStrokingColor(Color.RED);
                    }
                    String textul="Nume:'"+s.getNume()+"'  "+"Email:'"+s.getEmail()+"'  "+"Cadru:'"+s.getCadru()+"'  "+"Medie:" + service.getMedie(s.getId());
                    content.showText(textul);
                    content.setLeading(14.5f);
                    content.newLineAtOffset(20, startP);
                    content.newLine();
                    startP = startP - 18;
                    content.endText();
                }
            }
            document.addPage(page);
            content.close();
            return page;
        }catch (IOException ioe){
            throw ioe;
        }
    }

//    public void printNode(final Node node) throws NoSuchMethodException, InstantiationException, IllegalAccessException {
//        Printer printer = Printer.getDefaultPrinter();
//        PageLayout pageLayout= printer.createPageLayout(Paper.A4, PageOrientation.LANDSCAPE, Printer.MarginType.HARDWARE_MINIMUM);
//        PrinterAttributes attr = printer.getPrinterAttributes();
//        PrinterJob job = PrinterJob.createPrinterJob();
////        double scaleX
////                = pageLayout.getPrintableWidth() / node.getBoundsInParent().getWidth();
////        double scaleY
////                = pageLayout.getPrintableHeight() / node.getBoundsInParent().getHeight();
////        Scale scale = new Scale(scaleX, scaleY);
//
//        Scale scale = new Scale(1, 0.616);
//
//
//        node.getTransforms().add(scale);
//
//        if (job != null && job.showPrintDialog(node.getScene().getWindow())) {
//            boolean success = job.printPage(pageLayout, node);
//            if (success) {
//                job.endJob();
//
//            }
//        }
//        node.getTransforms().remove(scale);
//    }


    @FXML
    public void exportPDF(ActionEvent actionEvent){
        try {
            String fileName = "statistica.pdf";
            PDDocument doc = new PDDocument();
            PDPage page = new PDPage();
            doc.addPage(page);
            PDPageContentStream content = new PDPageContentStream(doc, page);

            content.beginText();
            content.setFont(PDType1Font.TIMES_BOLD,26);
            content.moveTextPositionByAmount(120,750);
            content.showText("Statistica privind media studentilor");
            content.endText();

            int startP=700;
//            try {
//                printNode((Node) barChart);
//            }catch (NoSuchMethodException | InstantiationException |  IllegalAccessException c){

//            }
//
            PDImageXObject imag=PDImageXObject.createFromFile("barChart.png",doc);
            content.drawImage(imag,10,startP-370);
            startP=startP-370;
            PDImageXObject imag2=PDImageXObject.createFromFile("chartPie.png",doc);
            content.drawImage(imag2,10,startP-310);

            content.close();

            createNewPage(doc,"221");
            createNewPage(doc,"222");
            createNewPage(doc,"223");
            createNewPage(doc,"224");
            createNewPage(doc,"225");
            createNewPage(doc,"226");
            createNewPage(doc,"227");





            doc.save(fileName);
            doc.close();
            System.out.println("Your file created in: "+ System.getProperty("user.dir"));

        }catch (IOException ioe){
            System.out.println("Nu a reusit exportul!");
        }

    }


    public void setServices(Service service){
        this.service=service;
        loadBarChart();
        loadStudents();
    }

    public void initialize(){
    }

    @Override
    public void update(Observable o, Object arg) {
        barChart.getData().clear();
        loadBarChart();
        loadStudents();
    }
}
