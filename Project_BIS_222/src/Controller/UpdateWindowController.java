package Controller;

import Domain.Student;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.List;

public class UpdateWindowController {
    @FXML Button buttonCancelUpdate;
    @FXML TextField txtFieldIdUp;
    @FXML TextField txtFieldNameUp;
    @FXML TextField txtFieldEmailUp;
    @FXML TextField txtFieldGroupUp;
    @FXML ComboBox comboBoxTeacherUp;
    @FXML Button buttonSaveUpdate;

    Service.Service service;
    Pane paneTransparent;
    public UpdateWindowController(){}

    public void setService(Service.Service service)
    {
        this.service=service;
    }

    @FXML
    public void initialize()
    {
        comboBoxTeacherUp.getItems().addAll("Camelia Serban","Cojocaru Grigoreta");
    }

    public void handleCancelUpdate(ActionEvent actionEvent) {
        Stage stage = (Stage) buttonCancelUpdate.getScene().getWindow();
        //paneTransparent.setVisible(false);
        stage.close();

    }

    public void setPane(Pane pane)
    {
        paneTransparent=pane;
    }

    private void showError(String message) {
        Alert alerta=new Alert(Alert.AlertType.ERROR);
        alerta.setTitle("Mesaj eroare");
        alerta.setContentText(message);
        alerta.showAndWait();
    }

    public void setFieldsForUpdate(int  id,String nume,int grupa,String email,String prof)
    {
        txtFieldEmailUp.setText(email);
        txtFieldNameUp.setText(nume);
        txtFieldGroupUp.setText(String.valueOf(grupa));
        txtFieldIdUp.setText(String.valueOf(id));
        txtFieldIdUp.setDisable(true);
        comboBoxTeacherUp.getSelectionModel().selectFirst();
        while(!(comboBoxTeacherUp.getSelectionModel().getSelectedItem().toString().toLowerCase().equals(prof.toLowerCase())))
        {
            comboBoxTeacherUp.getSelectionModel().selectNext();
        }
    }

    private Student getStudentFromFields()
    {

        Integer id=Integer.parseInt(txtFieldIdUp.getText());
        String nume=txtFieldNameUp.getText();
        int grupa=Integer.parseInt(txtFieldGroupUp.getText());
        String email=txtFieldEmailUp.getText();
        String prof=comboBoxTeacherUp.getSelectionModel().getSelectedItem().toString();
        return new Student(id,nume,grupa,email,prof);
    }

    public void handleUpdateStudent(ActionEvent actionEvent)
    {
        try
        {
            Student student=getStudentFromFields();
            service.updateStudent(student.getID(),student.getNume(),student.getGrupa(),student.getEmail(),student.getCadruDidactic());
            Stage stage = (Stage) buttonSaveUpdate.getScene().getWindow();
            //paneTransparent.setVisible(false);
            stage.close();

        }catch(Exception e)
        {
            showError(e.getMessage());
        }
    }
}
