package view_FXML;

import controller.ControllerTemeLaborator;
import domain.TemaLaborator;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import service.Service;


public class EditingCellInteger extends EditingCell<TemaLaborator,Integer>{

    @Override
    public Integer covertToType(String text) {
        return Integer.parseInt(text);
    }
}
