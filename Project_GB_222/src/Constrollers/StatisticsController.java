package Constrollers;

import Service.Service;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.print.PrinterJob;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.common.filespecification.PDFileSpecification;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.graphics.image.PDInlineImage;
import org.apache.pdfbox.pdmodel.graphics.state.RenderingMode;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotation;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationFileAttachment;
import org.apache.pdfbox.tools.imageio.ImageIOUtil;
import org.apache.pdfbox.util.Matrix;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.Annotation;
import java.util.*;

public class StatisticsController implements Observer
{
    Service service;

    @FXML
    AnchorPane mainAnchor;
    @FXML
    Button buttonExport;
    @FXML
    HBox hboxCharts;
    @FXML
    LineChart<String,Number> lineChart;
    @FXML
    CategoryAxis xAxis2;
    @FXML
    NumberAxis yAxis2;
    @FXML
    PieChart pieChart;

    @FXML
    VBox verticalBox;

    @FXML CategoryAxis CategoryAxisBarChart;
    @FXML NumberAxis NumberAxisBarChart;
    @FXML BarChart barChart;

    public StatisticsController() {
    }

    public void setService(Service service)
    {
        this.service=service;
        loadchart1();
        initChart();
    }

    private void initChart()
    {
        barChart.setTitle("Nota medie per tema");
        CategoryAxisBarChart.setLabel("Tema");
        NumberAxisBarChart.setLabel("Medie");

        XYChart.Series series1 = new XYChart.Series();

        HashMap<Integer,Float> groups=service.getMedie();
        for (Map.Entry group:groups.entrySet())
        {
            series1.getData().add(new XYChart.Data(group.getKey().toString(),group.getValue()));
        }

        barChart.getData().addAll(series1);
        setMaxBarWidth(40, 10);
        barChart.widthProperty().addListener((obs,b,b1)->{
            Platform.runLater(()->setMaxBarWidth(40, 10));
        });

    }

    private void setMaxBarWidth(double maxBarWidth, double minCategoryGap){
        double barWidth=0;
        do{
            double catSpace = CategoryAxisBarChart.getCategorySpacing();
            double avilableBarSpace = catSpace - (barChart.getCategoryGap() + barChart.getBarGap());
            barWidth = (avilableBarSpace / barChart.getData().size()) - barChart.getBarGap();
            if (barWidth >maxBarWidth){
                avilableBarSpace=(maxBarWidth + barChart.getBarGap())* barChart.getData().size();
                barChart.setCategoryGap(catSpace-avilableBarSpace-barChart.getBarGap());
            }
        } while(barWidth>maxBarWidth);

        do{
            double catSpace = CategoryAxisBarChart.getCategorySpacing();
            double avilableBarSpace = catSpace - (minCategoryGap + barChart.getBarGap());
            barWidth = Math.min(maxBarWidth, (avilableBarSpace / barChart.getData().size()) - barChart.getBarGap());
            avilableBarSpace=(barWidth + barChart.getBarGap())* barChart.getData().size();
            barChart.setCategoryGap(catSpace-avilableBarSpace-barChart.getBarGap());
        } while(barWidth < maxBarWidth && barChart.getCategoryGap()>minCategoryGap);
    }

    private void loadchart1()
    {
        ArrayList<String> homeworks=new ArrayList<>();
        HashMap<String,Integer> intarziere=service.studPerHomework();
        for (String nr:intarziere.keySet())
        {
            homeworks.add("Tema "+nr);
        }
        ObservableList<XYChart.Data> myList = FXCollections.observableArrayList();
        //final CategoryAxis xAxis2 = new CategoryAxis();
        //final NumberAxis yAxis2 = new NumberAxis();
        xAxis2.setLabel("Teme");
        xAxis2.setCategories(FXCollections.<String> observableArrayList(homeworks));
        yAxis2.setLabel("Note sub 5");
        XYChart.Series XYSeries2 = new XYChart.Series(myList);
        int i=0;
        for (String key:intarziere.keySet())
        {
            myList.add(new XYChart.Data(homeworks.get(i),intarziere.get(key)));
            i=i+1;
        }
        XYSeries2.setName("Teme");

        lineChart.getData().add(XYSeries2);
    }

    @FXML
    private void initialize()
    {

        //lineChart=new LineChart<>(23,yAxis2);
        lineChart.setTitle("Dificultate teme");
        lineChart.setPrefWidth(250);

        buttonExport.setOnAction(this::ExportPDF);
        //hboxCharts.getChildren().add(lineChart);
    }

    @Override
    public void update(Observable o, Object arg) {

    }

//    @FXML
    public void ExportPDF(ActionEvent event) {


        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose location");
        FileChooser.ExtensionFilter extFilter =
                new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showSaveDialog(new Stage());

        if (file != null)
        {
            WritableImage nodeshot = mainAnchor.snapshot(new SnapshotParameters(), null);

            File fileImg = new File("src/chart.png");
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(nodeshot, null), "png", fileImg);
            } catch (IOException e) {

            }

            PDDocument doc = new PDDocument();


            PDImageXObject pdimage;
            PDPageContentStream content;
            try {

                pdimage = PDImageXObject.createFromFile("src/chart.png", doc);
                PDPage page = new PDPage(new PDRectangle(pdimage.getWidth(), pdimage.getHeight()));
                doc.addPage(page);
                content = new PDPageContentStream(doc, page);
                content.drawImage(pdimage, 0, 0);
                content.close();
                doc.save(file.toString());
                doc.close();
                fileImg.delete();
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }



        }
    }
}
