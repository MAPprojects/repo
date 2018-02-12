package entities;

import javafx.scene.control.ContentDisplay;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableCell;

public class PasswordFieldCell extends TableCell<SystemUser, String> {
    private PasswordField passwordField;

    public PasswordFieldCell() {
        passwordField = new PasswordField();
        passwordField.setEditable(false);
        this.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        this.setGraphic(null);
    }

    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (!isEmpty()) {
            passwordField.setText(item);
            setGraphic(passwordField);
        } else {
            setGraphic(null);
        }
    }
}
