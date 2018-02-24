package FXMLView;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.chart.*;

import Service.Service;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Arrays;

public class Charts {

    private Service service;

    public Charts(Service service){

        this.service = service;
    }


    public void initializeBarChart(BarChart<String, Number> barChart, CategoryAxis xAxis,  NumberAxis yAxis){
        xAxis.setCategories(FXCollections.<String>
                observableArrayList(Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10")));
        xAxis.setLabel("Grades");

        yAxis.setLabel("Number of Students");

        barChart.setTitle("Statistics about Grades/Students");

    }

    public void setDataToBarChart(BarChart<String, Number> barChart){

        barChart.getData().clear();

        int[] grades = service.getTheCountedGrades();

        XYChart.Series series1 = new XYChart.Series();
        series1.setName("Grades MAP");
        series1.getData().add(new XYChart.Data("1", grades[1]));
        series1.getData().add(new XYChart.Data("2", grades[2]));
        series1.getData().add(new XYChart.Data("3", grades[3]));
        series1.getData().add(new XYChart.Data("4", grades[4]));
        series1.getData().add(new XYChart.Data("5", grades[5]));
        series1.getData().add(new XYChart.Data("6", grades[6]));
        series1.getData().add(new XYChart.Data("7", grades[7]));
        series1.getData().add(new XYChart.Data("8", grades[8]));
        series1.getData().add(new XYChart.Data("9", grades[9]));
        series1.getData().add(new XYChart.Data("10", grades[10]));

        barChart.getData().addAll(series1);
    }

    public void setDataToBarChartHomeworks(BarChart<String, Number> barChart){

        barChart.getData().clear();

        double[] grades = service.getTheCountedGradesForAllHomeworks();

        XYChart.Series series1 = new XYChart.Series();
        series1.setName("Grades MAP");
        series1.getData().add(new XYChart.Data("H1", grades[1]));
        series1.getData().add(new XYChart.Data("H2", grades[2]));
        series1.getData().add(new XYChart.Data("H3", grades[3]));
        series1.getData().add(new XYChart.Data("H4", grades[4]));
        series1.getData().add(new XYChart.Data("H5", grades[5]));
        series1.getData().add(new XYChart.Data("H6", grades[6]));
        series1.getData().add(new XYChart.Data("H7", grades[7]));
        series1.getData().add(new XYChart.Data("H8", grades[8]));
        series1.getData().add(new XYChart.Data("H9", grades[9]));
        series1.getData().add(new XYChart.Data("H10", grades[10]));
        series1.getData().add(new XYChart.Data("H11", grades[10]));
        series1.getData().add(new XYChart.Data("H12", grades[10]));
        series1.getData().add(new XYChart.Data("H13", grades[10]));
        series1.getData().add(new XYChart.Data("H14", grades[10]));

        barChart.getData().addAll(series1);
    }

    public void initializeBarChartHomeworks(BarChart<String, Number> barChart, CategoryAxis xAxis,  NumberAxis yAxis){
        xAxis.setCategories(FXCollections.<String>
                observableArrayList(Arrays.asList("H1", "H2", "H3", "H4", "H5", "H6", "H7", "H8", "H9", "H10", "H11", "H12", "H13","H14")));
        xAxis.setLabel("Homeworks");

        yAxis.setLabel("Grades");

        barChart.setTitle("Statistics about Grades/Homeworks");
    }

    public void initializePieChart(PieChart pieChart, Stage stage){

        pieChart.getData().clear();
        int notPromoted = service.getNotPromotedPercentage();
        int promoted = 100-notPromoted;

        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                        new PieChart.Data("Promoted", promoted),
                        new PieChart.Data("Not Promoted", notPromoted));

        pieChart.setData(pieChartData);

        pieChart.setLabelLineLength(10);
        pieChart.setLegendSide(Side.LEFT);

        pieChartData.forEach(data ->
                data.nameProperty().bind(
                        Bindings.concat(
                                data.getName(), " ", data.pieValueProperty()
                        )
                )
        );

    }
}
