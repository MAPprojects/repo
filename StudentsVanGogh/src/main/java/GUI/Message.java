package GUI;

import javafx.scene.control.Alert;

public class Message {
    public static void showMessage(Alert.AlertType type, String header, String text) {
        Alert message = new Alert(type);
        message.setHeaderText(header);
        message.setContentText(text);

        message.showAndWait();
    }
}
