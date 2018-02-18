package Controller;

import Domain.Student;
import Domain.Tema;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class UpdateHomeworkController {
    @FXML Button buttonCancelUpdate;
    @FXML TextField txtFieldIdUp;
    @FXML TextField txtFieldDescUp;
    @FXML TextField txtFieldDeadlineUp;
    @FXML Button buttonSaveUpdate;

    Service.Service service;
    //Pane paneTransparent;

    public UpdateHomeworkController(){}

    @FXML
    public void initialize() { }

    public void setService(Service.Service service)
    {
        this.service=service;
    }

    public void handleCancelUpdate(ActionEvent actionEvent) {
        Stage stage = (Stage) buttonCancelUpdate.getScene().getWindow();
        //paneTransparent.setVisible(false);
        stage.close();
    }

    public void setPane(Pane pane)
    {
        //paneTransparent=pane;
    }

    private void showError(String message) {
        Alert alerta=new Alert(Alert.AlertType.ERROR);
        alerta.setTitle("Mesaj eroare");
        alerta.setContentText(message);
        alerta.showAndWait();
    }

    public void setFieldsForUpdate(int  id,String description,int deadline)
    {

        txtFieldDescUp.setText(description);
        txtFieldDeadlineUp.setText(String.valueOf(deadline));
        txtFieldIdUp.setText(String.valueOf(id));
        txtFieldIdUp.setDisable(true);
    }

    public void handleUpdateHomework(ActionEvent actionEvent)
    {
        try
        {
            Tema tema=getHomeworkFromFields();
            service.modifyHomework(tema.getID(),tema.getDeadline(),tema.getDescriere());
            Stage stage = (Stage) buttonSaveUpdate.getScene().getWindow();
           // paneTransparent.setVisible(false);
            stage.close();

        }catch(Exception e)
        {
            showError(e.getMessage());
        }
    }

    private Tema getHomeworkFromFields()
    {

        Integer id=Integer.parseInt(txtFieldIdUp.getText());
        String description=txtFieldDescUp.getText();
        int deadline=Integer.parseInt(txtFieldDeadlineUp.getText());
        return new Tema(id,description,deadline);
    }
}
