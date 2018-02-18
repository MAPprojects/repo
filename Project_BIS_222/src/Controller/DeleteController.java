package Controller;

import Domain.Student;
import Domain.Tema;
import Service.Service;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class DeleteController {
    public DeleteController()
    {

    }

    Service service;
    @FXML Button buttonYesDelete;
    @FXML Button buttonCancelDelete;
    @FXML Label labelDeleted;
    @FXML Label labelAreUSure;
    Pane paneTransparent;
    Student studentToDelete;
    Tema homeworkToDelete;

    public void setService(Service service)
    {
        this.service=service;
    }
    public void setPane(Pane paneTransparent)
    {
        this.paneTransparent=paneTransparent;
    }
    @FXML
    public void initialize()
    {

    }


    public void handleYesDelete(ActionEvent actionEvent) {
        service.deleteStudent(studentToDelete.getID());
        Stage stage = (Stage) buttonCancelDelete.getScene().getWindow();
        //paneTransparent.setVisible(false);
        stage.close();
    }


    public void setStudentToDelete(Student studentToDelete) {
        this.studentToDelete = studentToDelete;
        labelDeleted.setText(studentToDelete.getNume()+" from group "+studentToDelete.getGrupa());
    }

    public void handleCancelDelete(ActionEvent actionEvent) {
        Stage stage = (Stage) buttonCancelDelete.getScene().getWindow();
        //paneTransparent.setVisible(false);
        stage.close();
    }

    public void setHomeworkToDelete(Tema homeworkToDelete) {
        this.homeworkToDelete = homeworkToDelete;
        labelDeleted.setText(homeworkToDelete.getDescriere()+" with deadline in week"+homeworkToDelete.getDeadline());
    }

    public void handleYesDeleteHomework(ActionEvent actionEvent) {
        service.deleteTema(homeworkToDelete.getID());
        Stage stage = (Stage) buttonCancelDelete.getScene().getWindow();
        //paneTransparent.setVisible(false);
        stage.close();
    }


}
