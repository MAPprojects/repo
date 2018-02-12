package controller;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import services.ChangePasswordService;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class ChangePasswordController {
    private static final String MESAJ_EROARE_CAMPURI_OBLIGATORII = "Toate campurile sunt obligatorii";
    private static final String MESAJ_EROARE_PAROLE_DIFERITE = "Parola noua nu e identica in ambele campuri";
    private ChangePasswordService changePasswordService;
    private Stage changePasswordStage;
    private AnchorPane loginRootLayout;
    private Text loginPostIt;

    @FXML
    private Pane paneMesajEroare;
    @FXML
    private Text mesajEroare;
    @FXML
    private TextField fieldUserSauEmail;
    @FXML
    private PasswordField fieldParolaVeche;
    @FXML
    private PasswordField fieldParolaNoua1;
    @FXML
    private PasswordField fieldParolaNoua2;

    public void setChangePasswordService(ChangePasswordService changePasswordService) {
        this.changePasswordService = changePasswordService;
    }

    public void setChangePasswordStage(Stage changePasswordStage) {
        this.changePasswordStage = changePasswordStage;
    }

    public void setLoginRootLayout(AnchorPane loginRootLayout) {
        this.loginRootLayout = loginRootLayout;
    }

    public void setLoginPostIt(Text loginPostIt) {
        this.loginPostIt = loginPostIt;
    }

    @FXML
    public void initialize() {
        paneMesajEroare.setVisible(false);
        mesajEroare.setText("");
    }

    @FXML
    private void closeStage(MouseEvent mouseEvent) {
        loginRootLayout.getChildren().forEach(child -> {
            child.setEffect(null);
            child.setDisable(false);
        });
        changePasswordStage.close();
    }

    @FXML
    private void modificaParolaHandler(MouseEvent mouseEvent) {
        if (areFieldsValid()) {
            String userOrEmail = fieldUserSauEmail.getText();
            String oldPassword = fieldParolaVeche.getText();
            String newPAssword1 = fieldParolaNoua1.getText();
            String newPassword2 = fieldParolaNoua2.getText();
            try {
                changePasswordService.changePassword(userOrEmail, oldPassword, newPAssword1);
                loginRootLayout.getChildren().forEach(child -> {
                    child.setDisable(false);
                    child.setEffect(null);
                });
                loginPostIt.setText("Parola a fost schimbata cu succes");
                changePasswordStage.close();
            } catch (NoSuchFieldException e) {
                mesajEroare.setText(e.getMessage());
                mesajEroare.setStyle("-fx-font-size: 14;");
                paneMesajEroare.setVisible(true);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean areFieldsValid() {
        clearErrors();
        boolean infoValida = true;
        String userOrEmail = fieldUserSauEmail.getText();
        String oldPassword = fieldParolaVeche.getText();
        String newPAssword1 = fieldParolaNoua1.getText();
        String newPassword2 = fieldParolaNoua2.getText();
        if (userOrEmail.equals("") || oldPassword.equals("") || newPAssword1.equals("") || newPassword2.equals("")) {
            infoValida = false;
            mesajEroare.setText(MESAJ_EROARE_CAMPURI_OBLIGATORII);
            mesajEroare.setStyle("-fx-font-size: 18;");
            paneMesajEroare.setVisible(true);
        } else if (!newPAssword1.equals(newPassword2)) {
            infoValida = false;
            mesajEroare.setText(MESAJ_EROARE_PAROLE_DIFERITE);
            mesajEroare.setStyle("-fx-font-size: 18;");
            paneMesajEroare.setVisible(true);
        }
        return infoValida;
    }

    private void clearErrors() {
        mesajEroare.setText("");
        paneMesajEroare.setVisible(false);
    }

}
