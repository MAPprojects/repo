package StudentController;

import domain.Student;
import service.ServiceStudent;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;

public class StudentFilterController_FXML {

    @FXML
    private RadioButton radioButtonAn;

    @FXML
    private RadioButton radioButtonProfesor;

    @FXML
    private RadioButton radioButtonLiteraDinNume;

    @FXML
    private TextField dataField;

    private ObservableList<Student> model;

    private ServiceStudent service;
    Stage dialogStage;

    @FXML
    private void initialize() {}

    public void setService(ServiceStudent service, Stage dialogStage, ObservableList<Student> model)
    {
        this.service = service;
        this.dialogStage = dialogStage;
        this.model = model;
    }

    @FXML
    public void FilterHandler(ActionEvent ev)
    {
        if (!this.radioButtonAn.isSelected() && !this.radioButtonProfesor.isSelected() &&! this.radioButtonLiteraDinNume.isSelected()) {
        }else
        {
            try
            {
                List<Student> filtered = (ArrayList<Student>) this.service.returnall();
                if (this.radioButtonAn.isSelected())
                    filtered = this.service.studentiPeAni(Integer.parseInt(this.dataField.getText()));
                else if (this.radioButtonLiteraDinNume.isSelected())
                    filtered = this.service.studentiNumeSauPrenumeCuA(this.dataField.getText());
                else
                    filtered=this.service.tataLor(this.dataField.getText());
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
