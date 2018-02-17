package Utils;

import javafx.scene.control.Alert;

public class AlertMessage {
    public static void showMessage(Alert.AlertType type, String title, String text){
        Alert message=new Alert(type);
        message.setTitle(title);
        message.setContentText(text);
        message.showAndWait();
    }

}
