package MVC;

import Service.Service;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;


public class MeniuController {
    private Service service;
    private boolean admin;
    private String mailAnumitUtilizator;

    private TemaController temaController;
    private TemaAnumitStudentController temaAnumitStudentController;
    private StudentController studentController;
    private StudentAnumitStudentController studentAnumitStudentController;
    private NotaController notaController;
    private NotaAnumitStudentController notaAnumitStudentController;
    private RapoarteController rapoarteController;

    @FXML
    private Pane content;

    public void setService(Service service) {
        this.service = service;
        afisareTeme(null);
    }

    public void setDrepturi(boolean admin){
        this.admin = admin;
    }

    public void setMailAnumitUtilizator(String mailAnumitUtilizator){
        this.mailAnumitUtilizator=mailAnumitUtilizator;
    }

    public static void showMessage(Alert.AlertType type, String header, String text) {
        Alert message = new Alert(type);
        message.setHeaderText(header);
        message.setContentText(text);
        message.showAndWait();
    }

    public static void showErrorMessage(String text) {
        showMessage(Alert.AlertType.WARNING, "Warning!", text);
    }

    @FXML
    private void afisareTeme(ActionEvent actionEvent) {
        content.getChildren().clear();
        try {
            if (!admin){
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("TemaAnumitStudentView.fxml"));
                content.getChildren().add(fxmlLoader.load());
                temaAnumitStudentController = fxmlLoader.getController();
                temaAnumitStudentController.setService(service);
                Stage stage = (Stage) content.getScene().getWindow();
                stage.setTitle("Meniu teme");
            }
            else {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("TemaView.fxml"));
                content.getChildren().add(fxmlLoader.load());
                temaController = fxmlLoader.getController();
                temaController.setService(service);
                temaController.setDrepturi(admin);
                Stage stage = (Stage) content.getScene().getWindow();
                stage.setTitle("Meniu teme");
            }
        } catch (IOException e) {
            showErrorMessage("Nu s-a putut deschide TemaView.fxml!");
        }
    }

    @FXML
    private void afisareStudenti(ActionEvent actionEvent) {
        content.getChildren().clear();
        try {
            if (!admin){
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("StudentAnumitStudentView.fxml"));
                content.getChildren().add(fxmlLoader.load());
                studentAnumitStudentController = fxmlLoader.getController();
                studentAnumitStudentController.setService(service);
                Stage stage = (Stage) content.getScene().getWindow();
                stage.setTitle("Meniu studenti");
            }
            else {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("StudentView.fxml"));
                content.getChildren().add(fxmlLoader.load());
                studentController = fxmlLoader.getController();
                studentController.setService(service);
                studentController.setDrepturi(admin);
                Stage stage = (Stage) content.getScene().getWindow();
                stage.setTitle("Meniu studenti");
            }
        } catch (IOException e) {
            showErrorMessage("Nu s-a putut deschide StudentView.fxml!");
        }
    }

    @FXML
    private void afisareNote(ActionEvent actionEvent) {
        content.getChildren().clear();
        try {
            if (!admin){
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("NotaAnumitStudentView.fxml"));
                content.getChildren().add(fxmlLoader.load());
                notaAnumitStudentController = fxmlLoader.getController();
                notaAnumitStudentController.setService(service);
                notaAnumitStudentController.setMailUtilizator(mailAnumitUtilizator);
                Stage stage = (Stage) content.getScene().getWindow();
                stage.setTitle("Meniu note");
            }
            else {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("NotaView.fxml"));
                content.getChildren().add(fxmlLoader.load());
                notaController = fxmlLoader.getController();
                notaController.setService(service);
                notaController.setDrepturi(admin);
                Stage stage = (Stage) content.getScene().getWindow();
                stage.setTitle("Meniu note");
            }
        } catch (IOException e) {
            showErrorMessage("Nu s-a putut deschide NotaView.fxml!");
        }
    }

    @FXML
    private void afisareRapoarte(ActionEvent actionEvent) {
        if (admin)
            content.getChildren().clear();
        try {
            if (admin) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("RapoarteView.fxml"));
                content.getChildren().add(fxmlLoader.load());
                rapoarteController = fxmlLoader.getController();
                rapoarteController.setService(service);
                Stage stage = (Stage) content.getScene().getWindow();
                stage.setTitle("Rapoarte");
            }
            else {
                MainController.showErrorMessage("Rapoartele nu pot fi vizualizate de catre studenti!");
            }
        } catch (IOException e) {
            showErrorMessage("Nu s-a putut deschide RapoarteView.fxml!");
        }
    }
}
