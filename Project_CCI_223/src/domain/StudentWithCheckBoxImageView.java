package domain;

import controller.ControllerFisierLog;
import controller.ControllerTabelLogProf;
import javafx.application.HostServices;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import service.Service;
import service.ServiceUsers;
import view_FXML.DropShadowForImage;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class StudentWithCheckBoxImageView extends StudentWithCheckBox {
    private Button button;
    private Service service;
    private ServiceUsers serviceUsers;
    private User currentUser;
    private HostServices hostServices;
    private Stage stage;
    private Stage mainStage;

    /**
     * Constructor
     *
     * @param idStudent                              int nr matricol al studentului
     * @param nume                                   String
     * @param grupa                                  int
     * @param email                                  String
     * @param cadru_didactic_indrumator_de_laborator String
     */
    public StudentWithCheckBoxImageView(int idStudent, String nume, int grupa, String email, String cadru_didactic_indrumator_de_laborator,Service service) {
        super(idStudent, nume, grupa, email, cadru_didactic_indrumator_de_laborator);
        this.service=service;
        button=new Button();
        setButtonStyle();
        setButtonAction();
    }

    private void setButtonAction() {
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                initLogTable();
            }
        });
    }

    private void initLogTable() {
        FXMLLoader loader=new FXMLLoader();
        loader.setLocation(getClass().getResource("/view_FXML/tabelLogProf.fxml"));
        try{
            BorderPane root=loader.load();
            Stage stage=new Stage();
            Scene scene=new Scene(root);
            stage.setTitle("Fisier LOG");
            stage.setScene(scene);
            stage.initModality(Modality.WINDOW_MODAL);

            ControllerTabelLogProf controller=loader.getController();
            controller.setCurrentUser(new User("student",nume,email,"a",ServiceUsers.getHashCodePassword("a")));
            controller.setStage(stage);
            controller.setService(service);

            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    protected void setButtonStyle() {
        File file=new File("D:\\MAP\\GestiuneStudentiLaboratareNote\\src\\view_FXML\\login\\doc.png");
        Image image=new Image(file.toURI().toString());
        ImageView imageView=new ImageView(image);
        imageView.setX(5d);
        imageView.setY(5d);
        imageView.setFitHeight(20d);
        imageView.setFitWidth(20d);
        button.setGraphic(imageView);
        button.setStyle("-fx-border-style: none;\n" +
                "    -fx-border-width: 0;\n" +
                "    -fx-border-insets: 0;\n");
        button.setPrefSize(2d,2d);
        button.setCursor(Cursor.HAND);
    }

    public Button getButton() {
        return button;
    }

    public void setButton(Button button) {
        this.button = button;
    }


}
