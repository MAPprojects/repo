package View;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class HelpSignInController {
    private LoginController loginController;
    private Stage primaryStage;

    @FXML
    private Button closeButton;
    @FXML
    private Label labelDescription;

    @FXML
    private void initialize() {
        String s = "";
        s = s + "        The account list contains every single account of the ";
        s = s + "application, and they can be filtered by their user role using ";
        s = s + "the checkboxes. Selecting an account from the account list fills ";
        s = s + "the 'Username' text field. If your account is not in that list ";
        s = s + "(which means you do not have an account), please create a new ";
        s = s + "account clicking the button in the bottom left. If you can not ";
        s = s + "remember your password, please contact one of the administrators ";
        s = s + "from the account list.";

        labelDescription.setText(s);

        labelDescription.setTextAlignment(TextAlignment.JUSTIFY);
        labelDescription.setWrapText(true);
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setOnCloseRequest(event -> loginController.helpSignInStage = null);
    }

    public void closeButtonHandler(ActionEvent actionEvent) {
        loginController.helpSignInStage = null;
        primaryStage.close();
    }

    public void setLoginController(LoginController loginController) {
        this.loginController = loginController;
    }
}
