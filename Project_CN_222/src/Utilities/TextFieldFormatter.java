package Utilities;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;

public class TextFieldFormatter {

    public static void formatNameField(TextField textField) {
        formatField(textField,"[A-Za-z- ]*");
    }

    public static void formatEmailField(TextField textField) {
        formatField(textField, "[A-Za-z@-_0-9.]*");
    }

    public static void formatPasswordField(TextField textField) {
        formatXMLSafeField(textField);
    }

    public static void formatXMLSafeField(TextField textField) {
        formatField(textField, "[^<>]*");
    }

    public static void formatNumericField(TextField textField) {
        formatField(textField, "([1-9][0-9]*)?");
    }

    public static void formatField(TextField textField, String regex) {
        textField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue.matches(regex)) {
                    textField.setText(newValue);
                } else {
                    textField.setText(oldValue);
                }
            }
        });
    }
}
