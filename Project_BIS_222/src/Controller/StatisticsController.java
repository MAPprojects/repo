package Controller;

import Service.Service;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

;


public class StatisticsController implements Observer
{
    Service service;

    @FXML
    AnchorPane mainAnchor;
    @FXML HBox hboxCharts;
    @FXML LineChart<String,Number> lineChart;
    @FXML CategoryAxis xAxis2;
    @FXML NumberAxis yAxis2;
    @FXML PieChart pieChart;
    @FXML VBox verticalBox;

    @FXML CategoryAxis CategoryAxisBarChart;
    @FXML NumberAxis NumberAxisBarChart;
    @FXML BarChart barChart;



    public StatisticsController(){}

    private void initChart()
    {
        ArrayList<String> homeworks=new ArrayList<>();
        HashMap<Integer,Integer> intarziere=service.getIntarzieri();
        for (Integer nr:intarziere.keySet())
        {
            homeworks.add("Homework "+nr);
        }
        ObservableList<XYChart.Data> myList = FXCollections.observableArrayList();
        xAxis2.setLabel("Homeworks");
        xAxis2.setCategories(FXCollections.<String> observableArrayList(homeworks));
        yAxis2.setLabel("Delays");
        XYChart.Series XYSeries2 = new XYChart.Series(myList);
        int i=0;
        for (Integer key:intarziere.keySet())
        {
            myList.add(new XYChart.Data(homeworks.get(i),intarziere.get(key)));
            i=i+1;
        }

        XYSeries2.setName("Homeworks");

        lineChart.setTitle("Homeworks Difficulty");
        lineChart.setPrefWidth(250);
        lineChart.getData().add(XYSeries2);

    }

    public void initPieChart()
    {
        ArrayList<String> grades=new ArrayList<>();
        HashMap<Integer,Integer> allGrades=service.getNoteIntre();

        ObservableList<PieChart.Data> pieChartData1;
        pieChartData1 = FXCollections.observableArrayList();
        for (Integer grade:allGrades.keySet())
        {
            pieChartData1.add(new PieChart.Data("Grade "+grade, allGrades.get(grade)));
        }

        pieChart.setData(pieChartData1);
        pieChart.setAnimated(true);
        pieChart.setPrefWidth(200);
        pieChart.setTitle("Grades");

        final Label caption = new Label("");
        caption.setTextFill(Color.DARKORANGE);
        caption.setStyle("-fx-font: 24 arial;");


        for (final PieChart.Data data : pieChart.getData()) {
            data.getNode().addEventHandler(MouseEvent.MOUSE_PRESSED,
                    new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent e) {
                            caption.setTranslateX(e.getSceneX());
                            caption.setTranslateY(e.getSceneY());
                            caption.setText(String.valueOf(data.getPieValue()) + "%");
                        }
                    });
        }

        pieChartData1.stream().forEach(pieData -> {
            pieData.getNode().addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
//                caption.setTranslateX(event.getSceneX());
//                caption.setTranslateY(event.getSceneY());
//                caption.setText(String.valueOf(pieData.getPieValue()) + "%");

                Bounds b1 = pieData.getNode().getBoundsInLocal();
                double r=pieData.getPieValue();
                caption.setTranslateX(event.getSceneX());
                caption.setTranslateY(event.getSceneY());
                caption.setText(String.valueOf(r) + "%");

                double newX = (b1.getWidth()) / 2 + b1.getMinX();
                double newY = (b1.getHeight()) / 2 + b1.getMinY();
                // Make sure pie wedge location is reset
                pieData.getNode().setTranslateX(0);
                pieData.getNode().setTranslateY(0);
                TranslateTransition tt = new TranslateTransition(
                        Duration.millis(1500), pieData.getNode());
                tt.setByX(newX);
                tt.setByY(newY);
                tt.setAutoReverse(true);
                tt.setCycleCount(2);
                tt.play();
            });
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
    public void initBarChart()
    {

        barChart.setTitle("Grade average per group");
        CategoryAxisBarChart.setLabel("Group");
        NumberAxisBarChart.setLabel("Average");

        XYChart.Series series1 = new XYChart.Series();

        ArrayList<Integer> groups=service.getAllGroups();
        for (Integer group:groups)
        {
            series1.getData().add(new XYChart.Data(group.toString(),service.getAvgPerGroup(group)));
        }

        barChart.getData().addAll(series1);
        setMaxBarWidth(40, 10);
        barChart.widthProperty().addListener((obs,b,b1)->{
            Platform.runLater(()->setMaxBarWidth(40, 10));
        });
    }

    @FXML
    public void initialize()
    {

    }

    public void setService(Service service)
    {
        this.service = service;
        initChart();
        initPieChart();
        initBarChart();
    }

    @Override
    public void update(Observable o, Object arg) {

    }

    @FXML
    public void handleExportPdf(ActionEvent event) {


        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose location");
        FileChooser.ExtensionFilter extFilter =
                new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showSaveDialog(new Stage());

        if (file != null)
        {
            WritableImage nodeshot = verticalBox.snapshot(new SnapshotParameters(), null);
            File fileImg = new File("chart.png");

            try {
                ImageIO.write(SwingFXUtils.fromFXImage(nodeshot, null), "png", fileImg);
            } catch (IOException e) {

            }

            PDDocument doc = new PDDocument();

            PDImageXObject pdimage;
            PDPageContentStream content;
            try {
                pdimage = PDImageXObject.createFromFile("chart.png", doc);
                PDPage page = new PDPage(new PDRectangle(pdimage.getWidth(),pdimage.getHeight()));
                content = new PDPageContentStream(doc, page);
                content.drawImage(pdimage, 0, 0);
                content.close();
                doc.addPage(page);
                doc.save(file.toString());
                doc.close();
                fileImg.delete();
            } catch (IOException ex) {
                System.out.println("err export pdf");

            }

        }
    }
}
