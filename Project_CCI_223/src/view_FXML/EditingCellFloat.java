package view_FXML;

import domain.Nota;
import domain.TemaLaborator;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class EditingCellFloat extends EditingCell<Nota,Float> {

    @Override
    public Float covertToType(String text) {
        return Float.parseFloat(text);
    }
}

