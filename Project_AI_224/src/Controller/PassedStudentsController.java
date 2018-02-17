package Controller;

import Domain.Student;
import Service.ApplicationService;
import Utils.AlertMessage;
import Utils.FXMLUtil;
import Utils.PDFTableGenerator;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;

public class PassedStudentsController {

    private ApplicationService service;

    public PassedStudentsController(){
    }

    @FXML
    private StackPane stackPaneGradeReport;
    @FXML
    private TableView tableView;
    @FXML
    private TableColumn columnCodMatricol;
    @FXML
    private TableColumn columnName;
    @FXML
    private TableColumn columnGroup;
    @FXML
    private TableColumn columnTeacher;
    @FXML
    private TableColumn columnFinalGrade;

    public void setService(ApplicationService applicationService){
        this.service = applicationService;
        this.tableView.setItems(FXCollections.observableArrayList(this.service.getPassedStudents().keySet()));
    }

    public void handleSavePDF(){
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(new Stage());
        if(selectedDirectory == null){
            AlertMessage.showMessage(Alert.AlertType.ERROR, "Error", "No directory selcted!");
        }else {
            PDFTableGenerator pdfTableGenerator = new PDFTableGenerator(this.service);
            pdfTableGenerator.createPDF(selectedDirectory.toString(), "Passed");
            AlertMessage.showMessage(Alert.AlertType.CONFIRMATION, "Action completed", "File has been saved!");
        }
    }

    @FXML
    public void initialize(){
        FXMLUtil.initializeStudentColumns(columnCodMatricol, columnName, columnGroup, columnTeacher);
        this.columnFinalGrade.setCellValueFactory(new PropertyValueFactory<Student, Integer>("finalGrade"));
    }
}
