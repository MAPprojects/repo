package view_FXML;

import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class AlertMessage {
    /**
     * Creates a message box with the information given as parameters
     * @param owner Stage a window
     * @param type Alert.AlertType
     * @param header String
     * @param text String
     */
    public void showMessage(Stage owner, Alert.AlertType type, String header, String text) {
        Alert message = new Alert(type);
        message.setTitle(header);
        message.initOwner(owner);
        message.setContentText(text);
        message.showAndWait();
    }

}
