package GUI.ReportsUI;

import Domain.Grade;
import Domain.Student;
import Domain.User;
import GUI.AbstractController;
import GUI.InitView;
import Main.StartApplication;
import Repository.RepositoryException;
import Service.AssignmentService;
import Service.GeneralService;
import Service.GradeService;
import Service.StudentService;
import Utils.ListEvent;
import Utils.Observer;
import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Rpt1Ctrl extends AbstractController implements Observer<Student> {
    private StudentService studentService;
    private GradeService gradeService;
    private AssignmentService assignmentService;
    private GeneralService generalService;
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    ObservableList<Student> model = FXCollections.observableArrayList();

    ObservableList<XYChart.Series<String,Number>> modelSeries = FXCollections.observableArrayList();


    @FXML
    AnchorPane contentStd;

    @FXML
    ImageView imageViewStd;

    @FXML
    JFXButton buttonBackRpt1;

    @FXML
    JFXButton buttonDownload;

    @FXML
    BarChart<String,Number> barChart;

    @FXML
    CategoryAxis xAxis;

    @FXML
    NumberAxis yAxis;

    private Node reportsView;

    public void setStudentService(StudentService studentService) throws Exception {
        this.studentService = studentService;


    }

    public void setGradeService(GradeService gradeService) {
        this.gradeService = gradeService;
    }

    public void setAssignmentService(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    public void setGeneralService(GeneralService generalService) throws Exception {
        this.generalService = generalService;

        try {
            model.setAll(studentService.getAllStudents());
        } catch (RepositoryException e) {
            e.printStackTrace();
        }

        barChart.setData(modelSeries);

        xAxis = new CategoryAxis();
        yAxis = new NumberAxis();
        barChart = new BarChart<String, Number>(xAxis,yAxis);
        barChart.setTitle("final grade for all students");
        xAxis.setLabel("Name");
        yAxis.setLabel("Grade");

        for (Student student : model) {
            XYChart.Series<String,Number> series = new XYChart.Series<String, Number>();
            series.setName(student.getName());
            try {
                series.getData().add(new XYChart.Data<String, Number>(student.getName(), generalService.getFinalGrades().get(student.getIdStudent())));
            } catch (Exception e) {
                e.printStackTrace();
            }
            modelSeries.add(series);
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        File file = new File("5.jpg");
        Image image = new Image(file.toURI().toString());
        imageViewStd.setImage(image);


    }

    public Rpt1Ctrl(StudentService studentService, GradeService gradeService, AssignmentService assignmentService, GeneralService generalService) {
        this.studentService = studentService;
        this.gradeService = gradeService;
        this.assignmentService = assignmentService;
        this.generalService = generalService;
    }

    public void handleBackRpt1() throws IOException {
        reportsView  = InitView.initRptView(studentService,assignmentService,gradeService,generalService,user);
        contentStd.getChildren().setAll(reportsView);

        AnchorPane.setTopAnchor(reportsView, 0d);
        AnchorPane.setBottomAnchor(reportsView, 0d);
        AnchorPane.setLeftAnchor(reportsView, 0d);
        AnchorPane.setRightAnchor(reportsView, 0d);
    }

    public void handleDownload() throws Exception {
        PrinterJob job = PrinterJob.createPrinterJob();
        if(job != null){
            job.showPrintDialog(StartApplication.getStage()); // Window must be your main Stage
            barChart.setData(modelSeries);
            job.printPage(barChart);
            job.endJob();
        }

        contentStd.getChildren().setAll(InitView.initRpt1View(studentService,assignmentService,gradeService,generalService,user));

    }



    public Rpt1Ctrl() {

    }

    @Override
    public void notifyEvent(ListEvent<Student> listEvent) {
        try {
            model.setAll(studentService.getAllStudents());
        } catch (RepositoryException e) {
            e.printStackTrace();
        }
    }

}
