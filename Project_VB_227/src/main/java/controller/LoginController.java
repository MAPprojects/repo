package controller;

import entities.LogInNotifier;
import entities.LogOutListener;
import entities.SystemUser;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import jdk.nashorn.internal.runtime.options.LoggingOption;
import services.ChangePasswordService;
import services.ForgotPasswordService;
import services.SystemConfigurationService;
import services.UserSerivce;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginController extends LogInNotifier implements LogOutListener {

    private GaussianBlur gaussianBlur;
    private Scene menuScene;
    private Scene loginScene;
    private Stage primaryStage;
    private UserSerivce userSerivce;
    private MainMenuController mainMenuController;
    private SystemConfigurationService systemConfigurationService;
    private AnchorPane loginRootLayout;
    private ForgotPasswordService forgotPasswordService;
    private ChangePasswordService changePasswordService;
    @FXML
    private Pane paneButoaneSendAndChangePass;
    @FXML
    private Pane loginPane;
    @FXML
    private Text textUserSauEmail;
    @FXML
    private Text textParola;
    @FXML
    private AnchorPane rootAnchorPane;
    @FXML
    private TextField loginMailUserField;
    @FXML
    private TextField loginPasswordField;
    @FXML
    private Text loginTextEroare;

    public void setUserSerivce(UserSerivce userSerivce) {
        this.userSerivce = userSerivce;
    }

    public void setMenuScene(Scene menuScene) {
        this.menuScene = menuScene;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setSystemConfigurationService(SystemConfigurationService systemConfigurationService) {
        this.systemConfigurationService = systemConfigurationService;
    }

    public void setMainMenuController(MainMenuController mainMenuController) {
        this.mainMenuController = mainMenuController;
    }

    public void setLoginRootLayout(AnchorPane loginRootLayout) {
        this.loginRootLayout = loginRootLayout;
    }

    public void setLoginScene(Scene loginScene) {
        this.loginScene = loginScene;
    }

    public void setForgotPasswordService(ForgotPasswordService forgotPasswordService) {
        this.forgotPasswordService = forgotPasswordService;
    }

    public void setChangePasswordService(ChangePasswordService changePasswordService) {
        this.changePasswordService = changePasswordService;
    }

    @FXML
    private void initialize() {
        rootAnchorPane.widthProperty().addListener((obs, oldVal, newVal) -> {
//            loginMailUserField.setLayoutX(rootAnchorPane.getWidth() / 2 - 111);
//            loginPasswordField.setLayoutX(rootAnchorPane.getWidth() / 2 - 111);
            loginPane.setLayoutX(rootAnchorPane.getWidth() / 2 - 151);
//            loginPane.setLayoutX(rootAnchorPane.getWidth() / 2 - 151);
            paneButoaneSendAndChangePass.setLayoutX(loginPane.getLayoutX() - 60);
        });
        gaussianBlur = new GaussianBlur();
    }

    @FXML
    private void submitInfohandler(MouseEvent mouseEvent) throws NoSuchAlgorithmException {
        if (validateInfo()) {
            SystemUser user = userSerivce.findOneUser(loginMailUserField.getText());
            if (user == null) {
                loginTextEroare.setText("Email sau parola gresite.");
            } else {
                if (user.getId().equals("administrator") && loginPasswordField.getText().equals("administrator")) {
                    try {
                        systemConfigurationService.setRoleOfCurrentUser(user.getRol());
                        notifyListenersOnLogIn(user.getRol());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    primaryStage.setScene(menuScene);
                } else {
                    MessageDigest messageDigest = MessageDigest.getInstance("MD5");
                    messageDigest.reset();
                    messageDigest.update(loginPasswordField.getText().getBytes());
                    byte[] encryptedPassword = messageDigest.digest();
                    messageDigest.reset();
                    if (MessageDigest.isEqual(encryptedPassword, user.getPassword())) {
                        try {
                            systemConfigurationService.setRoleOfCurrentUser(user.getRol());
                            notifyListenersOnLogIn(user.getRol());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        primaryStage.setScene(menuScene);
                    } else {
                        loginTextEroare.setText("Email sau parola gresite.");
                    }
                }
            }
        }

    }

    @FXML
    private void changePasswordHandler(MouseEvent mouseEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/login/changePassword.fxml"));
        AnchorPane changePasswordRootLayout = loader.load();
        ChangePasswordController changePasswordController = loader.getController();
        Scene forgotPasswordScene = new Scene(changePasswordRootLayout);
        Stage forgotPasswordStage = new Stage(StageStyle.TRANSPARENT);
        forgotPasswordStage.initStyle(StageStyle.TRANSPARENT);
        forgotPasswordStage.initOwner(primaryStage);
        forgotPasswordStage.initModality(Modality.APPLICATION_MODAL);
        forgotPasswordScene.setFill(Color.TRANSPARENT);
        forgotPasswordStage.setScene(forgotPasswordScene);
        changePasswordRootLayout.setLayoutY(loginRootLayout.getHeight() / 2 - 240);
        changePasswordController.setChangePasswordStage(forgotPasswordStage);
        changePasswordController.setChangePasswordService(changePasswordService);
        changePasswordController.setLoginRootLayout(loginRootLayout);
        changePasswordController.setLoginPostIt(loginTextEroare);
        loginRootLayout.getChildren().forEach(node -> {
            node.setEffect(gaussianBlur);
            node.setDisable(true);
        });
        forgotPasswordStage.show();
    }

    @FXML
    private void amUitatParolaHandler(MouseEvent mouseEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/login/forgotPasswordView.fxml"));
        AnchorPane forgotPasswordRootLayout = loader.load();
        ForgotPasswordController loginController = loader.getController();
        loginController.setLoginScene(loginScene);
        loginController.setLoginSceneRootLayout(loginRootLayout);
        loginController.setForgotPasswordService(forgotPasswordService);
        Scene forgotPasswordScene = new Scene(forgotPasswordRootLayout);
        Stage forgotPasswordStage = new Stage(StageStyle.TRANSPARENT);
        forgotPasswordStage.initStyle(StageStyle.TRANSPARENT);
        forgotPasswordStage.initOwner(primaryStage);
        forgotPasswordStage.initModality(Modality.APPLICATION_MODAL);
        forgotPasswordScene.setFill(Color.TRANSPARENT);
        forgotPasswordStage.setScene(forgotPasswordScene);
        forgotPasswordRootLayout.setLayoutY(loginRootLayout.getHeight() / 2 - 200);
        loginController.setForgotPasswordStage(forgotPasswordStage);
        loginController.setForgotPasswordRootLayout(forgotPasswordRootLayout);
        loginController.setLoginPostItText(loginTextEroare);
        loginRootLayout.getChildren().forEach(node -> {
            node.setEffect(gaussianBlur);
            node.setDisable(true);
        });
        forgotPasswordStage.show();
    }

    private boolean validateInfo() {
        boolean validInfo = true;
        StringBuilder mesajEroare = new StringBuilder();
        if (loginMailUserField.getText().equals("")) {
            validInfo = false;
            mesajEroare.append("Email-ul nu poate fi nul.\n");

        }
        if (loginPasswordField.getText().equals("")) {
            validInfo = false;
            mesajEroare.append("Parola nu poate fi vida.\n");
        }
        if (!mesajEroare.toString().equals("")) {
            loginTextEroare.setText(mesajEroare.toString());
        }
        return validInfo;
    }

    @Override
    public void updateOnLogOut() {
        loginPasswordField.setText("");
        loginTextEroare.setText("");
    }
}
