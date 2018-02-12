package controller;

import entities.LogInListener;
import entities.LogOutListener;
import entities.LogOutNotifier;
import entities.Role;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.Reflection;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import repository.StudentRepository;
import services.StudentService;
import services.SystemConfigurationService;
import validator.StudentValidator;

import javax.tools.Tool;
import java.awt.*;
import java.io.IOException;

public class MainMenuController extends LogOutNotifier implements LogInListener {

    private Stage primaryStage;

    private static Role roleOfCurrentUser;
    private SystemConfigurationService systemConfigurationService;
    private StatisticsController statisticsController;
    private NotaController notaController;
    private SystemConfigurationController systemConfigurationController;

    private Scene studentScene;
    private Scene temaScene;
    private Scene notaScene;
    private Scene systemConfigScene;
    private Scene statisticsScene;
    private Scene logInScene;

    @FXML
    private StackPane setariProfSiStudenti;
    @FXML
    private CheckBox checkBoxProfSiStudenti;
    @FXML
    private StackPane setariMenuStackPane;
    @FXML
    private StackPane studentiMenuStackPane;
    @FXML
    private Button exitMenuButton;
    @FXML
    private Button statisticiMEnuButton;
    @FXML
    private Button notaMenuButton;
    @FXML
    private Button temaMenuButton;
    @FXML
    private AnchorPane anchorMenuPane;
    @FXML
    private Pane menuButtonsPane;
    @FXML
    private Button studentMenuButton;

    public static void setRoleOfCurrentUserFromDb(Role currentRoleOfUser) {
        roleOfCurrentUser = currentRoleOfUser;
    }

//    public void setRoleOfCurrentUserFromDb(Role roleOfCurrentUser) {
//        this.roleOfCurrentUser = roleOfCurrentUser;
//    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setTemaRootLayout(Scene temaRootLayout) {
        this.temaScene = temaRootLayout;
    }

    public void setStudentRootLayout(Scene studentScene) {
        this.studentScene = studentScene;
    }

    public void setNotaRootLayout(Scene notaRootLayout) {
        this.notaScene = notaRootLayout;
    }

    public void setSystemConfigScene(Scene systemConfigScene) {
        this.systemConfigScene = systemConfigScene;
    }

    public void setStatisticsScene(Scene statisticsScene) {
        this.statisticsScene = statisticsScene;
    }

    public void setStatisticsController(StatisticsController statisticsController) {
        this.statisticsController = statisticsController;
    }

    public void setLogInScene(Scene logInScene) {
        this.logInScene = logInScene;
    }

    public void setNotaController(NotaController notaController) {
        this.notaController = notaController;
    }

    public void setSystemConfigurationController(SystemConfigurationController systemConfigurationController) {
        this.systemConfigurationController = systemConfigurationController;
    }

    @FXML
    public void initialize() {
        addReflectionToButtons();
        setariProfSiStudenti.setVisible(false);
        anchorMenuPane.widthProperty().addListener((obs, oldVal, newVal) -> {
            menuButtonsPane.setLayoutX(anchorMenuPane.getWidth() / 2 - 430);
        });
        anchorMenuPane.heightProperty().addListener((obs, oldVal, newVal) -> {
            if (oldVal.doubleValue() != 0) {
                if (newVal.doubleValue() - oldVal.doubleValue() > 0) {
                    menuButtonsPane.setLayoutY(menuButtonsPane.getLayoutY() + (newVal.doubleValue() - oldVal.doubleValue() + 75));
                } else {
                    menuButtonsPane.setLayoutY(menuButtonsPane.getLayoutY() + (newVal.doubleValue() - oldVal.doubleValue() - 75));
                }
            }
        });
        addTooltipForButtons();
    }

    public void setSystemConfigurationService(SystemConfigurationService systemConfigurationService) {
        this.systemConfigurationService = systemConfigurationService;
    }

    public void setNodesVisibilityDependingOnRole(Role rol) {
        if (rol.equals(Role.STUDENT)) {
            studentiMenuStackPane.setVisible(false);
        }
    }

    @FXML
    private void studentButtonHandler() throws IOException {
        double actualHeight, actualWidth;
        actualHeight = primaryStage.getHeight();
        actualWidth = primaryStage.getWidth();
        primaryStage.setScene(studentScene);
        primaryStage.setHeight(actualHeight);
        primaryStage.setWidth(actualWidth);
    }

    @FXML
    private void temaButtonHandler(MouseEvent mouseEvent) {
        double actualHeight, actualWidth;
        actualHeight = primaryStage.getHeight();
        actualWidth = primaryStage.getWidth();
        primaryStage.setScene(temaScene);
        primaryStage.setHeight(actualHeight);
        primaryStage.setWidth(actualWidth);
    }

    @FXML
    private void showNotaMenu(MouseEvent mouseEvent) {
        double actualHeight, actualWidth;
        actualHeight = primaryStage.getHeight();
        actualWidth = primaryStage.getWidth();
        primaryStage.setScene(notaScene);
        primaryStage.setHeight(actualHeight);
        primaryStage.setWidth(actualWidth);
    }

    @FXML
    private void setSystemConfigurationScene(MouseEvent mouseEvent) {
        if (roleOfCurrentUser.equals(Role.ADMINISTRATOR)) {
            double actualHeight, actualWidth;
            actualHeight = primaryStage.getHeight();
            actualWidth = primaryStage.getWidth();
            primaryStage.setScene(systemConfigScene);
            primaryStage.setHeight(actualHeight);
            primaryStage.setWidth(actualWidth);
        } else {
            setariProfSiStudenti.setVisible(true);
        }
    }

    @FXML
    private void showStatisticsScene(MouseEvent mouseEvent) {
        double actualHeight, actualWidth;
        actualHeight = primaryStage.getHeight();
        actualWidth = primaryStage.getWidth();
        primaryStage.setScene(statisticsScene);
        primaryStage.setHeight(actualHeight);
        primaryStage.setWidth(actualWidth);
//        statisticsController.refreshPage();
    }

    @FXML
    private void logOutHandler(MouseEvent mouseEvent) throws IOException {
        double actualHeight, actualWidth;
        actualHeight = primaryStage.getHeight();
        actualWidth = primaryStage.getWidth();
        systemConfigurationService.updateEnableAuthentication(true);
        notifyListenersOnLogOut();
        primaryStage.setScene(logInScene);
        primaryStage.setHeight(actualHeight);
        primaryStage.setWidth(actualWidth);
        setariProfSiStudenti.setVisible(false);
//        statisticsController.refreshPage();
    }

    @FXML
    private void enableAuthenticationHandler(MouseEvent mouseEvent) throws IOException {
        boolean isAuthenticationEnabled = checkBoxProfSiStudenti.selectedProperty().getValue();
        systemConfigurationService.updateEnableAuthentication(isAuthenticationEnabled);
    }

    @FXML
    private void closeSettingForProfAndStudents(MouseEvent mouseEvent) {
        if (roleOfCurrentUser.equals(Role.PROFESOR) || roleOfCurrentUser.equals(Role.STUDENT)) {
            if (mouseEvent.getPickResult().getIntersectedNode().getId() != null) {
                if (!mouseEvent.getPickResult().getIntersectedNode().getId().equals("settingsImageMenu") &&
                        !mouseEvent.getPickResult().getIntersectedNode().getId().equals("setariProfSiStudenti")) {
                    setariProfSiStudenti.setVisible(false);
                }
            }
        }
    }

    private void addTooltipForButtons() {
        Tooltip tooltipStudenti = new Tooltip("Studenti");
        Tooltip.install(studentiMenuStackPane, tooltipStudenti);
        Tooltip tooltipSetari = new Tooltip("Setari");
        Tooltip.install(setariMenuStackPane, tooltipSetari);
    }

    private void addReflectionToButtons() {
        Reflection reflection = new Reflection();
        reflection.setFraction(0.9);
        temaMenuButton.setEffect(reflection);
        notaMenuButton.setEffect(reflection);
        statisticiMEnuButton.setEffect(reflection);
        exitMenuButton.setEffect(reflection);
    }

    @Override
    public void updateOfLogIn(Role roleOfTheUser) {
        checkBoxProfSiStudenti.setSelected(systemConfigurationService.isEnabledAuthentication());
        if (roleOfTheUser.equals(Role.STUDENT)) {
            studentiMenuStackPane.setVisible(false);
            roleOfCurrentUser = roleOfTheUser;
        } else if (roleOfTheUser.equals(Role.ADMINISTRATOR)) {
            studentiMenuStackPane.setVisible(true);
            roleOfCurrentUser = roleOfTheUser;
        } else if (roleOfTheUser.equals(Role.PROFESOR)) {
            studentiMenuStackPane.setVisible(true);
            roleOfCurrentUser = roleOfTheUser;
        }
    }

    @FXML
    private void exitApplication(MouseEvent mouseEvent) {
        Platform.exit();
    }
}
