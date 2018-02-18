package GUI;
import Domain.GrupeTop;
import Domain.NoteDTO;
import Domain.TemeTop;
import Service.Service;

import Utils.ListEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Controller {

    @FXML
    GridPane mainView;
    @FXML private ToggleGroup listButtonsGroup;
    @FXML private RadioButton studentiBtn, noteBtn, temeBtn,backBtn,statisticsButton;
    @FXML
    private AnchorPane studentPane;
    @FXML
    private AnchorPane temaPane;
    @FXML
    private AnchorPane notaPane;

    StudentiController studentController;
    TemeController temaController;
    NoteController notaController;

    Service service;
    Stage stage;

    public Controller(){}
    public Service getService(){return service;}


    @FXML
    private void initialize(){
        loadFXMLFiles();

        addActionOnButtons();
        studentiBtn.fire();
    }

    private void loadFXMLFiles(){
        try{
            FXMLLoader studentLoader=new FXMLLoader(getClass().getResource("/GUI/StudentiController.fxml"));
            studentPane=studentLoader.load();
            studentController = studentLoader.getController();

            FXMLLoader temaLoader=new FXMLLoader(getClass().getResource("/GUI/TemeController.fxml"));
            temaPane=temaLoader.load();
            temaController = temaLoader.getController();

            FXMLLoader notaLoader=new FXMLLoader(getClass().getResource("/GUI/NoteController.fxml"));
            notaPane=notaLoader.load();
            notaController = notaLoader.getController();

            mainView.add(studentPane,0,2);
            mainView.add(temaPane,0,2);
            mainView.add(notaPane,0,2);

            GridPane.setColumnSpan(studentPane,3);
            GridPane.setColumnSpan(temaPane,3);
            GridPane.setColumnSpan(notaPane,3);

        }catch (IOException e){
            e.printStackTrace();
        }
    }





    private void addActionOnButtons() {
        studentiBtn.getStyleClass().remove("radio-button");
        studentiBtn.getStyleClass().add("button");

        temeBtn.getStyleClass().remove("radio-button");
        temeBtn.getStyleClass().add("button");

        noteBtn.getStyleClass().remove("radio-button");
        noteBtn.getStyleClass().add("button");

        backBtn.getStyleClass().remove("radio-button");
        backBtn.getStyleClass().add("button");

        statisticsButton.getStyleClass().remove("radio-button");
        statisticsButton.getStyleClass().add("button");


        statisticsButton.setOnAction(event->{
            final Stage dialog = new Stage();
            dialog.initOwner(this.stage);
            dialog.initModality(Modality.APPLICATION_MODAL);
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("StatisticsButtons.fxml"));
            try {
                Parent root = loader.load();
                dialog.setTitle("Aplicatie");
                Scene scene=new Scene(root);
                scene.getStylesheets().add("Viper.css");
                dialog.setScene(scene);
                dialog.show();
                StatisticsButtonsController ctrl= loader.getController();

                ctrl.setService(service,dialog);
            }catch (Exception e) {
                System.out.println(e);
            }
        });

        backBtn.setOnAction(event -> {
            try{
                FXMLLoader loader = new FXMLLoader();
                //loader.setLocation(getClass().getResource("GUI\\FXMLProiect.fxml"));
                loader.setLocation(getClass().getResource("LoginU.fxml"));
                Parent root = loader.load();
                Scene scene=new Scene(root);
                LoginController ctrl= loader.getController();
                ctrl.setService(service,stage);
                stage.setScene(scene);
            }catch (Exception e){
                showErrorMessage(e.toString());
            }

        });

        studentiBtn.setOnAction(event -> {
            makeVisible(studentPane);
            makeInVisible(temaPane);
            makeInVisible(notaPane);

            //change the fillter values



        });

        temeBtn.setOnAction(event -> {
            makeInVisible(studentPane);
            makeVisible(temaPane);
            makeInVisible(notaPane);


        });

        noteBtn.setOnAction(event -> {
            try{
                FXMLLoader notaLoader=new FXMLLoader(getClass().getResource("/GUI/NoteController.fxml"));
                notaPane=notaLoader.load();
                notaController = notaLoader.getController();
                notaController.setService(service,stage);
                mainView.add(notaPane,0,2);
                GridPane.setColumnSpan(notaPane,3);}
            catch (IOException e){
                System.err.println(e);
            }
            makeInVisible(studentPane);
            makeInVisible(temaPane);
            makeVisible(notaPane);



        });
    }

    private void makeVisible(Node p) { p.setVisible(true); }

    private void makeInVisible(Node p) {
        p.setVisible(false);
    }

    private void showErrorMessage(String text) {
        Alert message = new Alert(Alert.AlertType.ERROR);
        message.setTitle("Error Message");
        message.initOwner(stage);
        message.initModality(Modality.APPLICATION_MODAL);
        message.setContentText(text);
        message.showAndWait();
    }

    public void setService(Service service,Stage stage) {
        this.stage=stage;
        this.service = service;
        studentController.setService(service,stage);
        temaController.setService(service,stage);
        notaController.setService(service,stage);
    }

    private void showMessage(Alert.AlertType information, String s) {
        Alert message = new Alert(information);
        message.setHeaderText("");
        message.setContentText(s);
        message.showAndWait();
    }
}
