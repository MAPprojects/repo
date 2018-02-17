package Controller;

import Domain.Student;
import Service.ApplicationService;
import Utils.AlertMessage;
import Utils.FXMLUtil;
import Utils.PDFTableGenerator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;

public class InTimeReportController {
    ApplicationService service;
    ObservableList<Student> model;

    @FXML
    private javafx.scene.control.TableView tableView;
    @FXML
    private TableColumn columnCodMatricol;
    @FXML
    private TableColumn columnName;
    @FXML
    private TableColumn columnGroup;
    @FXML
    private TableColumn columnTeacher;

    public InTimeReportController(){}

    public void setService(ApplicationService service){
        this.service = service;
        this.model = FXCollections.observableArrayList(this.service.getInTimeStudents());
        this.tableView.setItems(model);
    }

    @FXML
    public void initialize(){
        FXMLUtil.initializeStudentColumns(columnCodMatricol, columnName, columnGroup, columnTeacher);
    }

    public void handleSavePDF(){
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(new Stage());
        if(selectedDirectory == null){
            AlertMessage.showMessage(Alert.AlertType.ERROR, "Error", "No directory selcted!");
        }else{
            PDFTableGenerator pdfTableGenerator = new PDFTableGenerator(this.service);
            pdfTableGenerator.createPDF(selectedDirectory.toString(), "InTime");
            AlertMessage.showMessage(Alert.AlertType.CONFIRMATION, "Action completed", "File has been saved!");
        }
    }

}
