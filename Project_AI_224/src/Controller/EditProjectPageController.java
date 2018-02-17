package Controller;

import Domain.Project;
import Service.ApplicationService;
import Utils.AlertMessage;
import Validate.ValidationException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

public class EditProjectPageController implements ScreenController {
    MainController mainController;

    @FXML
    private Button btnAdd;
    @FXML
    private TextField txtID;
    @FXML
    private TextField txtDesc;
    @FXML
    private TextField txtDeadline;

    private ApplicationService service;

    private Project project;
    private Stage dialogStage;
    private String action;

    @FXML
    private Label labelStudProf;

    public void setService(ApplicationService service, Stage dialogStage, Project project, String action) {
        this.service = service;
        this.dialogStage = dialogStage;
        this.project = project;
        this.action = action;
        this.btnAdd.setText(action);
        setFields(project, action);
    }

    private void setFields(Project project, String action){
        if(action.compareTo("ADD")==0){
            this.labelStudProf.setText("Add new project");
        }
        if(project==null){
            return;
        }
        this.txtID.setText(String.valueOf(project.getID()));
        this.txtDesc.setText(project.getDescription());
        this.txtDeadline.setText(String.valueOf(project.getDeadline()));
    }

    @FXML
    private void initialize(){ }

    @FXML
    private void handleSave() throws FileNotFoundException, UnsupportedEncodingException, ValidationException {
        String id = this.txtID.getText();
        String desc = this.txtDesc.getText();
        String deadline = this.txtDeadline.getText();
        switch(this.action){
            case "ADD": {
                addProject(desc, deadline);
                break;
            }
            case "UPDATE": {
                updateProject(id, deadline);
                break;
            }
        }
    }

    private void addProject(String description, String deadline){
        try {
            service.projectService.saveProject(description, deadline);
            AlertMessage.showMessage(Alert.AlertType.CONFIRMATION, "Action completed", "Project has been saved!");
            dialogStage.close();
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ValidationException e) {
            AlertMessage.showMessage(Alert.AlertType.ERROR, "Error", e.getMessage());
        }
    }

    private void updateProject(String ID, String deadline){
        try {
            service.projectService.updateProject(UUID.fromString(ID), deadline);
            AlertMessage.showMessage(Alert.AlertType.CONFIRMATION, "Action completed", "Project has been updated!");
            dialogStage.close();
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ValidationException e) {
            AlertMessage.showMessage(Alert.AlertType.ERROR, "Error", e.getMessage());
        }

    }

    @Override
    public void setScreenParent(MainController controller) {
        mainController = controller;
    }
}
