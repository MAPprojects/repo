package LaboratorController;

import domain.Laborator;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import service.ServiceLaborator;

import java.util.ArrayList;
import java.util.List;

public class LaboratorFilterController_FXML {

    @FXML
    private RadioButton radioButtonNumere;

    @FXML
    private RadioButton radioButtonDupaDeadline;

    @FXML
    private RadioButton radioButtonSecondHalfOfSemester;

    @FXML
    private TextField dataField;

    private ObservableList<Laborator> model;

    private ServiceLaborator service;
    Stage dialogStage;

    @FXML
    private void initialize() {}

    public void setService(ServiceLaborator service, Stage dialogStage, ObservableList<Laborator> model)
    {
        this.service = service;
        this.dialogStage = dialogStage;
        this.model = model;
    }

    @FXML
    public void FilterHandler(ActionEvent ev)
    {
        if (!this.radioButtonNumere.isSelected() && !this.radioButtonDupaDeadline.isSelected() &&! this.radioButtonSecondHalfOfSemester.isSelected()) {
        }else
        {
            try
            {
                List<Laborator> filtered = (ArrayList<Laborator>) this.service.returnall();
                if (this.radioButtonNumere.isSelected())
                    filtered = this.service.laboratoareCarePrelucreazaNumere();
                else if (this.radioButtonSecondHalfOfSemester.isSelected())
                    filtered = this.service.secondHalfOfSemester();
                else if (this.radioButtonDupaDeadline.isSelected())
                    filtered=this.service.temeOrdonateDupaDeadline();
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
