package controller;

import entities.AlertBox;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import services.ForgotPasswordService;
import thread_utils.NotifyingThread;
import thread_utils.ThreadCompleteListener;

import javax.mail.MessagingException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class ForgotPasswordController implements ThreadCompleteListener {

    private static final String MESAJ_EROARE_EMAIL = "Email invalid";
    private static final String EMAIL_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private Stage forgotPasswordStage;
    private Scene loginScene;
    private AnchorPane loginSceneRootLayout;
    private AnchorPane forgotPasswordRootLayout;
    private ForgotPasswordService forgotPasswordService;
    private GaussianBlur gaussianBlur;
    private ImageView loadingImageView;
    private boolean successfulEmailSending = false;
    @FXML
    public Pane forgotPasswordPAneEroare;
    @FXML
    public Text mesajEroareForgotPassword;
    private Text loginPostItText;
    @FXML
    private TextField fieldEmail;

    public void setForgotPasswordStage(Stage forgotPasswordStage) {
        this.forgotPasswordStage = forgotPasswordStage;
    }

    public void setLoginScene(Scene loginScene) {
        this.loginScene = loginScene;
    }

    public void setLoginSceneRootLayout(AnchorPane loginSceneRootLayout) {
        this.loginSceneRootLayout = loginSceneRootLayout;
    }

    public void setForgotPasswordService(ForgotPasswordService forgotPasswordService) {
        this.forgotPasswordService = forgotPasswordService;
    }

    public void setForgotPasswordRootLayout(AnchorPane forgotPasswordRootLayout) {
        this.forgotPasswordRootLayout = forgotPasswordRootLayout;
    }

    public void setLoginPostItText(Text loginPostItText) {
        this.loginPostItText = loginPostItText;
    }

    @FXML
    public void initialize() {
        forgotPasswordPAneEroare.setVisible(false);
        mesajEroareForgotPassword.setText("");
    }

    @FXML
    private void exitToLoginScene(MouseEvent mouseEvent) {
        loginSceneRootLayout.getChildren().forEach(node -> {
            node.setDisable(false);
            node.setEffect(null);
        });
        forgotPasswordStage.close();
        gaussianBlur = new GaussianBlur();
    }

    @FXML
    private void sendEmailWithPassword(MouseEvent mouseEvent) {
        String email = fieldEmail.getText();
        if (validateEmailField()) {
            //adaugam loading image
            FileInputStream loadingGifInputStream = null;
            try {
                loadingGifInputStream = new FileInputStream("src\\main\\resources\\views\\loadingScene\\loader.gif");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Image loadingImage = new Image(loadingGifInputStream);
            loadingImageView = new ImageView(loadingImage);
            forgotPasswordRootLayout.getChildren().add(loadingImageView);
            loadingImageView.setX(forgotPasswordRootLayout.getWidth() / 2 - 80);
            loadingImageView.setY(forgotPasswordRootLayout.getHeight() / 2 - 100);
            //bluram fiecare element, mai putin loading image
            forgotPasswordRootLayout.getChildren().forEach((child) -> {
                if (child != loadingImageView) {
                    child.setDisable(true);
                    child.setEffect(gaussianBlur);
                }
            });
            NotifyingThread notifyingThread = new NotifyingThread() {
                @Override
                public void doRun() {
                    try {
                        forgotPasswordService.sendPasswordToEmail(email);
                        successfulEmailSending = true;
                    } catch (NoSuchFieldException e) {
                        successfulEmailSending = false;
                        Platform.runLater(() -> {
                            forgotPasswordRootLayout.getChildren().remove(loadingImageView);
                            forgotPasswordRootLayout.getChildren().forEach(child -> {
                                child.setDisable(false);
                                child.setEffect(null);
                            });
                            forgotPasswordPAneEroare.setVisible(true);
                            mesajEroareForgotPassword.setText("Niciun user inregistrat nu are acest email");
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (MessagingException e) {
                        successfulEmailSending = false;
                        Platform.runLater(() -> {
                            forgotPasswordRootLayout.getChildren().remove(loadingImageView);
                            forgotPasswordRootLayout.getChildren().forEach(child -> {
                                child.setDisable(false);
                                child.setEffect(null);
                            });
                            forgotPasswordPAneEroare.setVisible(true);
                            mesajEroareForgotPassword.setText("Trimiterea nu s-a putut efectua cu succes");
                        });
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }
                }
            };
            notifyingThread.addListener(this);
            notifyingThread.start();
        }

    }

    private boolean validateEmailField() {
        boolean validInfo = true;
        String email = fieldEmail.getText();
        if (email.equals("") || !email.matches(EMAIL_REGEX)) {
            validInfo = false;
            fieldEmail.setText(MESAJ_EROARE_EMAIL);
            fieldEmail.setStyle("-fx-text-fill: red;");
        }
        return validInfo;
    }

    @FXML
    private void removeErrorMEssageOnCLick(MouseEvent mouseEvent) {
        if (fieldEmail.getText().equals(MESAJ_EROARE_EMAIL)) {
            fieldEmail.setText("");
            fieldEmail.setStyle("-fx-text-fill: black;");
        }
    }

    @FXML
    private void deleteErrorMEssageOnKeyPressed(KeyEvent keyEvent) {
        fieldEmail.setStyle("-fx-text-fill: black;");
    }

    @Override
    public void notifyOfThreadComplete(Thread thread) {
        if (successfulEmailSending) {
            Platform.runLater(() -> {
                loginSceneRootLayout.getChildren().forEach(child -> {
                    child.setEffect(null);
                    child.setDisable(false);
                });
                forgotPasswordPAneEroare.setVisible(false);
                mesajEroareForgotPassword.setText("");
                forgotPasswordStage.close();
                loginPostItText.setText("S-a trimis un email cu noua parola");
            });
        }
    }
}
