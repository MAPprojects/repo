package com.company.gui;
import com.company.domain.Student;
import com.company.repository.RepositoryException;
import com.company.service.Service;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;


public class LogareController {
    public Service service;
    public ImageView imageLog;
    public PasswordField passwordF;


    public LogareController() {
    }

    @FXML
    public void initialize(){
        imageLog.setImage(new Image("com/company/image/welcome2.png"));
        System.out.println("INITIALIZE");
    }

    public void setService(Service s){
        this.service = s;
        System.out.println("SERVICE");
    }

    public void logare(){
        if (passwordF.getText().equals("parola")){
            try {
                logareProfesori();
            } catch (IOException e) {
                showInformation(Alert.AlertType.ERROR, "Eroare", "Eroare la logarea ca profesor!");
            }
        }
        else{
            try {
                String parola = passwordF.getText();
                int id = Integer.parseInt(parola);
                Student student = service.findStudentForId(id);
                logareStudenti(id);
            } catch (IOException e) {
                showInformation(Alert.AlertType.ERROR, "Eroare", "Parola incorecta!");
            } catch (RuntimeException e){
                showInformation(Alert.AlertType.ERROR, "Eroare", "Parola incorecta!");
            }
        }
        passwordF.setText("");

    }

    public void logareProfesori() throws IOException {
        FXMLLoader loader =new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/company/gui/View.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setOpacity(1);
        stage.setTitle("Sectiunea profesori");
        stage.setScene(new Scene(root));
        ViewController controller = loader.getController();
        controller.setService(service,stage);
        stage.showAndWait();
    }

    public void logareStudenti(int id) throws IOException {
        FXMLLoader loader =new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/company/gui/ViewStudenti.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setOpacity(1);
        stage.setTitle("Sectiunea studenti");
        stage.setScene(new Scene(root));
        ViewStudentiController controller = loader.getController();
        controller.setService(service,stage, id);
        stage.showAndWait();
    }

    private void showInformation(Alert.AlertType type, String titlu, String mesaj){
        Alert alert = new Alert(type);
        alert.setHeaderText(titlu);
        alert.setContentText(mesaj);
        alert.showAndWait();
    }


}
