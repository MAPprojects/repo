package NotaController;

import domain.Nota;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import service.ServiceNota;

import java.util.ArrayList;
import java.util.List;

public class NotaFilterController_FXML {

    @FXML
    private RadioButton radioButtonNote1;

    @FXML
    private RadioButton radioButtonNoteStudent;

    @FXML
    private RadioButton radioButtonNote10;

    @FXML
    private TextField dataField;

    private ObservableList<Nota> model;

    private ServiceNota service;
    Stage dialogStage;

    @FXML
    private void initialize() {}

    public void setService(ServiceNota service, Stage dialogStage, ObservableList<Nota> model)
    {
        this.service = service;
        this.dialogStage = dialogStage;
        this.model = model;
    }

    @FXML
    public void FilterHandler(ActionEvent ev)
    {
        if (!this.radioButtonNote1.isSelected() && !this.radioButtonNoteStudent.isSelected() &&! this.radioButtonNote10.isSelected()) {
        }else
        {
            try
            {
                List<Nota> filtered = (ArrayList<Nota>) this.service.returnall();
                if (this.radioButtonNote1.isSelected())
                    filtered = this.service.noteDe1();
                else if (this.radioButtonNote10.isSelected())
                    filtered = this.service.noteDe10();
                else if (this.radioButtonNoteStudent.isSelected())
                    filtered=this.service.noteleUnuiStudentInOrdine(Integer.parseInt(this.dataField.getText()));
                this.model.setAll(filtered);
                dialogStage.close();
            }
            catch (Exception ex) {
            }
        }
    }

    @FXML
    public void CancelHandler(ActionEvent ev) { dialogStage.close(); }

}
