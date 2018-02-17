package Controller;

import Domain.Grade;
import Domain.TableProject;
import Service.ApplicationService;
import Utils.AlertMessage;
import Utils.PDFTableGenerator;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.layout.StackPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class HardestProjectController {
    ApplicationService service;
    ObservableList<TableProject> model;

    public HardestProjectController(){
    }

    @FXML
    private StackPane stackPaneGradeReport;

    @FXML
    public void initialize(){
    }

    public void setService(ApplicationService applicationService){
        this.service = applicationService;

        this.showChart();
    }

    public void handleSavePDF(){
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(new Stage());
        if(selectedDirectory == null){
            AlertMessage.showMessage(Alert.AlertType.ERROR, "Error", "No directory selcted!");
        }else{
            PDFTableGenerator pdfTableGenerator = new PDFTableGenerator(this.service);
            pdfTableGenerator.createChartPDF(selectedDirectory.toString(), "Final grades", this.getBarChart() );
            AlertMessage.showMessage(Alert.AlertType.CONFIRMATION, "Action completed", "File has been saved!");
        }
    }

    private void setGrades(HashMap<Integer, Integer> finalGrades){
        HashMap<UUID, List<Grade>> grades = service.gradeService.createGradesHashMap();
        for(Integer i=1;i<=10;i++){
            finalGrades.put(i, 0);
        }

        Collection<List<Grade>> values = grades.values();
        for (List<Grade> gradesToCalculate: values) {
            int sum = 0;
            for(Grade grade : gradesToCalculate){
                sum+=grade.getGrade();
            }
            if(gradesToCalculate.size()!=0){
                Integer avg = Math.round(sum / gradesToCalculate.size());
                if(avg!=0){
                    finalGrades.put(avg, finalGrades.get(avg)+1);
                }
            }
        }
    }

    public JFreeChart getBarChart(){
        HashMap<Integer, Integer> finalGrades = new HashMap<>();
        this.setGrades(finalGrades);

        DefaultCategoryDataset series1 = new DefaultCategoryDataset();
        series1.setValue(finalGrades.get(1), "Number of students", "1");
        series1.setValue(finalGrades.get(2), "Number of students", "2");
        series1.setValue(finalGrades.get(3),  "Number of students", "3");
        series1.setValue(finalGrades.get(4),  "Number of students", "4");
        series1.setValue(finalGrades.get(5), "Number of students", "5");
        series1.setValue(finalGrades.get(6),  "Number of students", "6");
        series1.setValue(finalGrades.get(7),  "Number of students", "7");
        series1.setValue(finalGrades.get(8),  "Number of students", "8");
        series1.setValue(finalGrades.get(9),  "Number of students", "9");
        series1.setValue(finalGrades.get(10),  "Number of students", "10");

        return ChartFactory.createBarChart("Students grades", "Grades", "Number of students", series1, PlotOrientation.VERTICAL, true, true, false);
    }

    public void showChart(){
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Grades");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Students");

        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Students' grades");

        XYChart.Series<String, Number> series1 = new XYChart.Series<>();
        series1.setName("Number of students");

        HashMap<Integer, Integer> finalGrades = new HashMap<>();
        this.setGrades(finalGrades);

        series1.getData().add(new XYChart.Data<>("1", finalGrades.get(1)));
        series1.getData().add(new XYChart.Data<>("2", finalGrades.get(2)));
        series1.getData().add(new XYChart.Data<>("3", finalGrades.get(3)));
        series1.getData().add(new XYChart.Data<>("4", finalGrades.get(4)));
        series1.getData().add(new XYChart.Data<>("5", finalGrades.get(5)));
        series1.getData().add(new XYChart.Data<>("6", finalGrades.get(6)));
        series1.getData().add(new XYChart.Data<>("7", finalGrades.get(7)));
        series1.getData().add(new XYChart.Data<>("8", finalGrades.get(8)));
        series1.getData().add(new XYChart.Data<>("9", finalGrades.get(9)));
        series1.getData().add(new XYChart.Data<>("10", finalGrades.get(10)));

        barChart.getData().addAll(series1);
        Group root = new Group(barChart);

        stackPaneGradeReport.getChildren().addAll(root);
    }

}
