package Constrollers;

import Service.Service;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class MenuController{

    @FXML
    private AnchorPane extendableSearchPane;

    @FXML
    private BorderPane borderPane;

    @FXML private Button studButton;
    @FXML private Button temaButton;
    @FXML private Button notaButton;
    @FXML private Button statButton;

    private Rectangle clipRect;

    public void setService(Service service) {
        this.service = service;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/Students.fxml"));
        try {
            AnchorPane root = (AnchorPane)loader.load();
            StudentControllerV2 ctrl = loader.getController();
            ctrl.setService(service);
            service.addObserver(ctrl);
            borderPane.setCenter(root);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Service service;

    @FXML
    void initialize() {
        double widthInitial = 107;
        double heightInitial = 365;
        clipRect = new Rectangle();
        clipRect.setWidth(0);
        clipRect.setHeight(heightInitial);
        clipRect.translateXProperty().set(widthInitial);
        extendableSearchPane.setClip(clipRect);
        extendableSearchPane.translateXProperty().set(-widthInitial);
        extendableSearchPane.prefWidthProperty().set(0);

        studButton.getStyleClass().add("standard");
        studButton.setOnAction(ev -> LoadStudentsView(ev));
        temaButton.getStyleClass().add("standard");
        temaButton.setOnAction(ev -> LoadTemaView(ev));
        notaButton.getStyleClass().add("standard");
        notaButton.setOnAction(ev -> LoadNotaView(ev));
        statButton.getStyleClass().add("standard");
        statButton.setOnAction(ev -> LoadStatView(ev));



    }



    @FXML
    public void toggleExtendableSearch() {

        clipRect.setWidth(extendableSearchPane.getWidth());
        clipRect.setHeight(extendableSearchPane.getHeight());

        if (clipRect.widthProperty().get() != 0) {

            // Animation for scroll up.
            Timeline timelineUp = new Timeline();

            // Animation of sliding the search pane up, implemented via
            // clipping.
            final KeyValue kvUp1 = new KeyValue(clipRect.widthProperty(), 0);
            final KeyValue kvUp2 = new KeyValue(clipRect.translateXProperty(), extendableSearchPane.getWidth());

            // The actual movement of the search pane. This makes the table
            // grow.
            final KeyValue kvUp4 = new KeyValue(extendableSearchPane.prefWidthProperty(), 0);
            final KeyValue kvUp3 = new KeyValue(extendableSearchPane.translateXProperty(), extendableSearchPane.getWidth());

            final KeyFrame kfUp = new KeyFrame(Duration.millis(200), kvUp1, kvUp2, kvUp3, kvUp4);
            timelineUp.getKeyFrames().add(kfUp);
            timelineUp.play();
        } else {

            // Animation for scroll down.
            Timeline timelineDown = new Timeline();

            // Animation for sliding the search pane down. No change in size,
            // just making the visible part of the pane
            // bigger.
            final KeyValue kvDwn1 = new KeyValue(clipRect.widthProperty(), 107);
            final KeyValue kvDwn2 = new KeyValue(clipRect.translateXProperty(), 0);

            // Growth of the pane.
            final KeyValue kvDwn4 = new KeyValue(extendableSearchPane.prefWidthProperty(), 107);
            final KeyValue kvDwn3 = new KeyValue(extendableSearchPane.translateXProperty(), 0);

            final KeyFrame kfDwn = new KeyFrame(Duration.millis(200), kvDwn1, kvDwn2,
                    kvDwn3, kvDwn4);
            timelineDown.getKeyFrames().add(kfDwn);

            timelineDown.play();
        }
    }

    @FXML
    public void LoadStudentsView(ActionEvent ev)
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/Students.fxml"));
        try {
            AnchorPane root = (AnchorPane)loader.load();
            StudentControllerV2 ctrl = loader.getController();
            ctrl.setService(service);
            service.addObserver(ctrl);
            borderPane.setCenter(root);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void LoadTemaView(ActionEvent ev)
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/Assignments.fxml"));
        try {
            AnchorPane root = (AnchorPane)loader.load();
            AssignmentController ctrl = loader.getController();
            ctrl.setService(service);
            service.addObserver(ctrl);
            borderPane.setCenter(root);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void LoadNotaView(ActionEvent ev)
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/Grades.fxml"));
        try {
            AnchorPane root = (AnchorPane)loader.load();
            GradeController ctrl = loader.getController();
            ctrl.setService(service);
            service.addObserver(ctrl);
            borderPane.setCenter(root);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void LoadStatView(ActionEvent ev)
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/Statistics.fxml"));
        try {
            AnchorPane root = (AnchorPane)loader.load();
            StatisticsController ctrl = loader.getController();
            ctrl.setService(service);
            service.addObserver(ctrl);
            borderPane.setCenter(root);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
