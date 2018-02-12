package controllers;

import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import services.StaffManager;
import views.formDecorator;

import java.io.IOException;


public class dashController {
    StaffManager service;
    Integer acces;

    @FXML
    private Label exitApp;

    @FXML
    private AnchorPane rightWindow;

    @FXML
    public void initialize() {
    }

    /**
     * Set up controller for Dashboard Page
     * @param staffService
     * @param accesLevel
     */
    public void seteaza(StaffManager staffService, Integer accesLevel) {
        this.service = staffService;
        this.acces = accesLevel;

        rightWindow.getChildren().clear();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/general/home.fxml"));
        AnchorPane root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        homeController cd = loader.getController();
        cd.seteaza(service);


        rightWindow.getChildren().add(root);
    }

    /**
     * Handler for Students Page
     * @param mouseEvent
     */
    public void handleLoadStudents(MouseEvent mouseEvent) {
        rightWindow.getChildren().clear();
        FXMLLoader loader = null;
        AnchorPane root = null;

        switch (acces) {
            case 1:
                loader = new FXMLLoader(getClass().getResource("/views/user/candidates.fxml"));
                break;
            case 5:
                loader = new FXMLLoader(getClass().getResource("/views/secretary/candidates.fxml"));
                break;
            case 9:
                loader = new FXMLLoader(getClass().getResource("/views/administrator/candidates.fxml"));
                break;
            default:
                break;
        }

        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        canditatesController cd = loader.getController();
        cd.seteaza(service);

        rightWindow.getChildren().add(root);

    }

    /**
     *
     * @param mouseEvent
     * Handler for Departments page
     */
    public void handleLoadDepartments(MouseEvent mouseEvent) {
        rightWindow.getChildren().clear();

        FXMLLoader loader = null;
        AnchorPane root = null;

        switch (acces) {
            case 1:
                loader = new FXMLLoader(getClass().getResource("/views/user/departments.fxml"));
                break;
            case 5:
                loader = new FXMLLoader(getClass().getResource("/views/secretary/departments.fxml"));
                break;
            case 9:
                loader = new FXMLLoader(getClass().getResource("/views/administrator/departments.fxml"));
                break;
            default:
                break;
        }

        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        departmentController cd = loader.getController();
        cd.seteaza(service);


        rightWindow.getChildren().add(root);
    }

    /**
     * Handler for Options Page
     * @param mouseEvent
     */
    public void handleLoadOptions(MouseEvent mouseEvent) {
        rightWindow.getChildren().clear();

        FXMLLoader loader = null;
        AnchorPane root = null;

        switch (acces) {
            case 1:
                loader = new FXMLLoader(getClass().getResource("/views/user/options.fxml"));
                break;
            case 5:
                loader = new FXMLLoader(getClass().getResource("/views/secretary/options.fxml"));
                break;
            case 9:
                loader = new FXMLLoader(getClass().getResource("/views/administrator/options.fxml"));
                break;
            default:
                break;
        }

        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        optionsController cd = loader.getController();
        cd.seteaza(service);


        rightWindow.getChildren().add(root);
    }

    /**
     * Handler for Logout Application
     * @param mouseEvent
     */
    public void handleLogout(MouseEvent mouseEvent) {
        Stage st = (Stage) exitApp.getScene().getWindow();
        st.close();
        this.service.saveData();


        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/views/general/login.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage primaryStage = new Stage();
        primaryStage.getIcons().add(new Image("images\\icon.png"));

        primaryStage.initStyle(StageStyle.TRANSPARENT);
        Scene scene = new Scene(new formDecorator(primaryStage, root));
        scene.setFill(null);


        primaryStage.setScene(scene);
        primaryStage.show();

    }

    /**
     * Handler for Exit Application
     * @param mouseEvent
     */
    public void handleExit(MouseEvent mouseEvent) {

        Stage st = (Stage) exitApp.getScene().getWindow();
        st.close();
        Platform.exit();
    }

    /**
     * Handler for Home Page
     * @param mouseEvent
     */
    public void handleLoadHome(MouseEvent mouseEvent) {
        rightWindow.getChildren().clear();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/general/home.fxml"));
        Parent root = null;
        try {
            root = (AnchorPane) loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        homeController cd = loader.getController();
        cd.seteaza(service);
        rightWindow.getChildren().add(root);

    }

}

