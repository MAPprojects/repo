package sample;

import Domain.Globals;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;


public class EditSaptCurenta {

    public Label label1;
    public Label label2;
    public TextField txt1;
    public Button btnOK;

    @FXML
    private void initialize() {
        label1.setText("Saptamana curenta: " + Globals.getInstance().getSaptCurenta());
        label2.setText("All good so far. ");
    }

    @FXML
    private void updateSaptamanaCurenta() {
        if(txt1.getText().length() == 0) {
            label2.setText("Campul nu poate ramane gol! ");
            label2.setTextFill(Color.RED);
        }
        else {
            Integer newSaptCurenta = null;
            try {
                newSaptCurenta = Integer.parseInt(txt1.getText());
            } catch (NumberFormatException e) {
                label2.setText("Va rog introduceti un intreg! ");
                label2.setTextFill(Color.RED);
                txt1.setText("");
                return;
            }
            if(newSaptCurenta > 0 && newSaptCurenta < 15) {
                Globals.getInstance().setSaptCurenta(newSaptCurenta);
                label2.setText("Modificarile au fost salvate! ");
                label2.setTextFill(Color.CYAN);
                txt1.setText("");
                label1.setText("Saptamana curenta: " + Globals.getInstance().getSaptCurenta());
            }
            else {
                label2.setText("Saptamana: (1-14) ");
                label2.setTextFill(Color.RED);
                txt1.setText("");
            }
        }
    }

    @FXML
    private void reset() {
        txt1.setText("");
        label2.setText("All good so far. ");
        label2.setTextFill(Color.BLACK);
    }
}
