import LaboratorController.LaboratorController_FXML;
import NotaController.NotaController_FXML;
import StudentController.StudentAddController_FXML;
import StudentController.StudentController_FXML;
import com.itextpdf.text.DocumentException;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import repository.RepoNota;
import repository.RepoStudent;
import repository.RepoTema;
import service.ServiceLaborator;
import service.ServiceNota;
import service.ServiceStudent;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MainController_FXML {
//    private ServiceNota serviceNota;
//    private ServiceStudent serviceStudent;
//    private ServiceLaborator serviceLaborator;

    RepoStudent studentRepository = new RepoStudent();
    RepoTema temaRepository = new RepoTema();
    RepoNota notaRepo = new RepoNota();

    ServiceStudent studentService = new ServiceStudent(studentRepository, notaRepo);
    ServiceLaborator laboratorService = new ServiceLaborator(temaRepository, studentRepository);
    ServiceNota notaService = new ServiceNota(studentRepository, notaRepo, temaRepository);

    @FXML
    public Tab student;

    @FXML
    public Tab laborator;
    @FXML
    public Tab nota;

    @FXML
    public Tab rapoarte;

    @FXML
    public Button raportBut1;

    @FXML
    public Button raportBut2;

    @FXML
    public Button raportBut3;

    public MainController_FXML() throws Exception {
    }


    public void setService() {
//        this.serviceLaborator = laboratorService;
//        this.serviceNota = notaService;
//        this.serviceStudent =studentService;
    }


    @FXML
    public void studentInterface(Event event) throws Exception {
        if (student.isSelected()) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/studentView/StudentView_FXML.fxml"));
            AnchorPane root = (AnchorPane) loader.load();

            StudentController_FXML studentController = loader.getController();
            studentController.setService(this.studentService);
            this.studentService.addObserver(studentController);
            student.setContent(root);

        }

    }


    @FXML
    public void laboratorInterface(Event event) throws Exception {
        if (laborator.isSelected()) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/laboratorView/LaboratorView_FXML.fxml"));
            AnchorPane root = (AnchorPane) loader.load();
            LaboratorController_FXML laboratorController = loader.getController();
            laboratorController.setService(this.laboratorService);
            this.laboratorService.addObserver(laboratorController);
            laborator.setContent(root);
        }
    }

    @FXML
    public void notaInterface(Event event) throws Exception {
        if (nota.isSelected()) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/notaView/NotaView_FXML.fxml"));
            AnchorPane root = (AnchorPane) loader.load();

            NotaController_FXML notaController = loader.getController();
            notaController.setService(this.notaService);
            this.notaService.addObserver(notaController);
            nota.setContent(root);
        }
    }


    public void onActionRaportBut1(ActionEvent actionEvent) throws IOException, IllegalAccessException, DocumentException, NoSuchFieldException {
        File file = new File(this.notaService.studentiCareIntraInExamen());
        Desktop.getDesktop().open(file);
    }

    public void onActionRaportBut2(ActionEvent actionEvent) throws IOException, NoSuchFieldException, DocumentException, IllegalAccessException {
        File file = new File(this.notaService.generarePdfPentruMediePonderata());
        Desktop.getDesktop().open(file);

    }

    public void onActionRaportBut3(ActionEvent actionEvent) throws IOException, NoSuchFieldException, DocumentException, IllegalAccessException {
        File file = new File(this.notaService.procentStudentiCuMediiPeste7());
        Desktop.getDesktop().open(file);

    }
}
