package Controller;

import Domain.Homework;
import Service.StatisticsService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.print.*;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;

public class StatisticsController implements Initializable {

    @FXML
    public PieChart piechart;
    public LineChart<String, Integer> linechart;
    public CategoryAxis xAxis;
    public NumberAxis yAxis;
    public Label max_label;
    public TableView<Object> tableEligibili;
    public TableView tableRespecta;
    public TableColumn<Object, Object> idColumn2;
    public TableColumn<Object, Object> nameColumn2;
    public TableColumn<Object, Object> grupaColumn2;
    public TableColumn<Object, Object> emailColumn2;
    public TableColumn<Object, Object> profesorColumn2;
    public TableColumn<Object, Object> idColumn1;
    public TableColumn<Object, Object> nameColumn1;
    public TableColumn<Object, Object> grupaColumn1;
    public TableColumn<Object, Object> emailColumn1;
    public TableColumn<Object, Object> profesorColumn1;

    private StatisticsService statisticsService;


    public StatisticsController() {

    }

    private void reloadAvg() {
        Map<String, Integer> grades = this.statisticsService.media();
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        grades.forEach((x, y) -> pieChartData.add(new PieChart.Data(x, y)));

        piechart.setTitle("Students Grades");
        piechart.setData(pieChartData);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (tableEligibili != null) {
            idColumn1.setCellValueFactory(new PropertyValueFactory<>("Id"));
            nameColumn1.setCellValueFactory(new PropertyValueFactory<>("nume"));
            emailColumn1.setCellValueFactory(new PropertyValueFactory<>("email"));
            grupaColumn1.setCellValueFactory(new PropertyValueFactory<>("grupa"));
            profesorColumn1.setCellValueFactory(new PropertyValueFactory<>("profesor"));
        }
        if (tableRespecta != null) {
            idColumn2.setCellValueFactory(new PropertyValueFactory<>("Id"));
            nameColumn2.setCellValueFactory(new PropertyValueFactory<>("nume"));
            emailColumn2.setCellValueFactory(new PropertyValueFactory<>("email"));
            grupaColumn2.setCellValueFactory(new PropertyValueFactory<>("grupa"));
            profesorColumn2.setCellValueFactory(new PropertyValueFactory<>("profesor"));
        }
    }

    public void setStatisticsService(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    public void handleActionAvg() {
        //Logger.getLogger().display(new Message("Load data ...", 2000));
        reloadAvg();
    }

    private void reloadHomeworks() {
        Map<Homework, Integer> homeworks = this.statisticsService.homeworks();

        linechart.getData().clear();
        xAxis.getCategories().clear();
        AtomicReference<Homework> maxHomework = new AtomicReference<>();
        int max = -1;

        if (homeworks.size() == 0)
            max_label.setText("There was no penalties");

        xAxis.setLabel("Homework Id");
        yAxis.setLabel("No. Of Penalties");

        linechart.setTitle("Homework Penalties");
        XYChart.Series<String, Integer> series = new XYChart.Series<>();
        linechart.setLegendVisible(false);
        linechart.getData().add(series);
        homeworks.forEach((key, value) -> {
            if (value > max)
                maxHomework.set(key);
            series.getData().add(new XYChart.Data<>(key.getId().toString(), value));
        });
        if (homeworks.size() > 0)
            max_label.setText(String.format("The most difficult was homework %d (%s) ", maxHomework.get().getId(),
                    maxHomework.get().getTask().length() > 10 ? maxHomework.get().getTask().substring(0, 10) + " ..." :
                            maxHomework.get().getTask()
            ));
    }

    public void handleActionHomeworks() {
        reloadHomeworks();
    }

    private void reloadStudents1() {
        tableEligibili.getItems().clear();
        statisticsService.studentiEligibili().forEach((tableEligibili.getItems()::add));

    }

    //studenti eligibili
    public void handleActionStudents1() {
        reloadStudents1();
    }

    private void reloadStudents2() {
        tableRespecta.getItems().clear();
        statisticsService.studentiFaraPenalitati().forEach((tableRespecta.getItems()::add));

    }

    //Studentii care au predat la timp toate temele
    public void handleActionStudents2() {
        reloadStudents2();
    }

    public void SaveToPDF(ActionEvent actionEvent) {
        PrinterJob job = PrinterJob.createPrinterJob();
        Printer printer = javafx.print.Printer.getDefaultPrinter();
        PageLayout layout = printer.createPageLayout(Paper.A4, PageOrientation.LANDSCAPE,
                Printer.MarginType.EQUAL_OPPOSITES);
        if (job != null) {
            //job.showPrintDialog(((Button) actionEvent.getTarget()).getScene().getWindow()); // Window must be your main Stage
            if (piechart != null)
                job.printPage(layout, piechart);
            if (linechart != null) {
                job.printPage(layout, linechart);
            }
            if (tableRespecta != null)
                job.printPage(layout, tableRespecta);
            if (tableEligibili != null)
                job.printPage(layout, tableEligibili);
            job.endJob();
        }
    }
}
