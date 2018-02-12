package controllers;

import db_stuff.DBConnect;
import javafx.fxml.FXML;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import services.StaffManager;

import java.util.ArrayList;
import java.util.Calendar;

public class homeController {
    StaffManager service;

    @FXML
    private AreaChart<?, ?> areaChart;

    @FXML
    private Label registeredCount;

    @FXML
    private Label departmentCount;

    public void seteaza(StaffManager staff) {
        this.service = staff;
        registeredCount.setText(String.valueOf(service.ShowSizeCandidates()));
        departmentCount.setText(String.valueOf(service.ShowSizeDepartments()));

        initChart();


    }

    /**
    * Initializare grafic din pagina Home
    * */
    private void initChart() {


        XYChart.Series series = new XYChart.Series();
        Calendar cal = Calendar.getInstance();
        int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH) + 1;
        int year = cal.get(Calendar.YEAR);

        DBConnect db = new DBConnect();

        for (Integer i = 1; i <= dayOfMonth; i++) {
            series.getData().add(new XYChart.Data(i.toString(), db.getCandidatesRegisteredinMonth(i, month, year)));
        }

        areaChart.getData().add(series);
        db.close();
    }
}
