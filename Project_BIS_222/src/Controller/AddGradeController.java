package Controller;

import Domain.DTOgrades;
import Domain.Nota;
import Domain.Student;
import Domain.Tema;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class AddGradeController {
    @FXML
    Button buttonCancelAddGrade;
    @FXML
    Button buttonAddGrade;
    @FXML
    TextField txtFieldStudent;
    @FXML TextField txtFieldGroup;
    @FXML TextField txtFieldHomework;
    @FXML TextField txtFieldHandedIn;
    @FXML TextField txtFieldValue;
    @FXML TextArea txtAreaObservations;

    Service.Service service;
    private DTOgrades dto;
    //Pane paneTransparent;

    public AddGradeController(){}

    @FXML
    public void initialize()
    {
        txtFieldHomework.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
            {

                try {
                    int nrTema=Integer.parseInt(newValue);
                    Nota nota = service.getGradeByNrAndIdStudent(nrTema, dto.getStudent().getID());
                    txtFieldValue.setText(String.valueOf(nota.getValoare()));
                }catch (Exception ex) {txtFieldValue.setText(""); }
            }
        });
    }

    public void setService(Service.Service service)
    {
        this.service=service;
    }

    public void handleCancelAddGrade(ActionEvent actionEvent) {
        Stage stage = (Stage) buttonCancelAddGrade.getScene().getWindow();
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

    public void handleAddGrade(ActionEvent actionEvent)
    {
        try
        {
            Student student=dto.getStudent();
            int nrTema=Integer.parseInt(txtFieldHomework.getText());
            int valoare=Integer.parseInt(txtFieldValue.getText());
            int sapt_predare=Integer.parseInt(txtFieldHandedIn.getText());
            service.addNota(student.getID(),nrTema,sapt_predare,valoare,txtAreaObservations.getText());

            Stage stage = (Stage) buttonAddGrade.getScene().getWindow();
            // paneTransparent.setVisible(false);
            stage.close();

        }catch(Exception e)
        {
            showError(e.getMessage());
        }
    }


    public void setDTO(DTOgrades DTO) {
        this.dto = DTO;
        initStudentFields();
    }

    private void initStudentFields() {
        txtFieldStudent.setText(dto.getName());
        txtFieldStudent.setDisable(true);
        txtFieldGroup.setText(String.valueOf(dto.getStudent().getGrupa()));
        txtFieldGroup.setDisable(true);
    }

    public void handleUpdateGrade(ActionEvent actionEvent)
    {

        try {
            int nrTema = Integer.parseInt(txtFieldHomework.getText());
            int value = Integer.parseInt(txtFieldValue.getText());
            String obs = txtAreaObservations.getText();
            int sapt = Integer.parseInt(txtFieldHandedIn.getText());
            service.modifyNota(dto.getStudent().getID(), nrTema, sapt, value, obs);
            Stage stage = (Stage) buttonAddGrade.getScene().getWindow();
            // paneTransparent.setVisible(false);
            stage.close();
        }catch (Exception ex)
        {
            showError(ex.getMessage());
        }
    }
}
