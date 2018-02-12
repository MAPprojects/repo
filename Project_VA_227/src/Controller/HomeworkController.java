package Controller;

import Domain.Action;
import Domain.Homework;
import Service.HomeworkService;
import Utils.Utils;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;
import Utils.ResourceManager;

public class HomeworkController extends AbstractController<Integer, Homework>{

    @FXML
    public TableColumn<Object, Object> idColumn;
    @FXML
    public TableColumn<Object, Object> taskColumn;
    @FXML
    public TableColumn<Object, Object> deadlineColumn;
    @FXML
    public TextField taskText, deadlineText;

    public HomeworkController()
    {
        super();
    }

    private Integer SelectedId = null;

    @Override
    protected void showDetails(Homework newValue) {
        if (newValue == null) {
            clearDetails();
            return;
        }
        SelectedId = newValue.getId();
        taskText.setText(newValue.getTask());
        deadlineText.setText(newValue.getDeadline().toString());
    }

    private void clearDetails() {
        SelectedId = null;
        taskText.clear();
        deadlineText.clear();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.model = FXCollections.observableArrayList();
        this.table.setItems(model);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldvalue, newValue) -> showDetails(newValue)
        );

        idColumn.setCellValueFactory(new PropertyValueFactory<>("Id"));
        taskColumn.setCellValueFactory(new PropertyValueFactory<>("task"));
        deadlineColumn.setCellValueFactory(new PropertyValueFactory<>("deadline"));
    }

    @FXML
    public void handleAction() {
        if (actionsChoose.getValue() == null) {
            showError("Select an action");
            return;
        }
        if ((actionsChoose.getValue().equals("Remove") || actionsChoose.getValue().equals("Update")) &&
                SelectedId == null)
        {
            showError("Select a howemork");
            return;
        }
        HomeworkService homeworkService = (HomeworkService) service;
        switch (actionsChoose.getValue()) {
            case "Add":
                try {
                    this.add(new Homework(-1, taskText.getText(),
                            Integer.parseInt(deadlineText.getText())));
                    if (this.user.getMailOption() == 1)
                        MainMenuController.sendToAll(new Homework(-1, taskText.getText(),
                            Integer.parseInt(deadlineText.getText())), Action.ADD);
                    clearDetails();
                } catch (Exception exception) {
                    showError(exception.getMessage());
                }
                break;
            case "Remove":
                try {
                    this.delete(SelectedId);
                    clearDetails();
                } catch (Exception exception) {
                    showError(exception.getMessage());
                }
                break;
            case "Update":
                try {
                    int oldDeadline = homeworkService.find(SelectedId).getDeadline();
                    Homework homework = new Homework(SelectedId, taskText.getText(), oldDeadline);
                    homework.setDeadline(Integer.parseInt(deadlineText.getText()));
                    this.update(homework);
                    if (oldDeadline != homework.getDeadline())
                        MainMenuController.sendToAll(homework, Action.MODIFY);
                    clearDetails();
                } catch (Exception exception) {
                    showError(exception.getMessage());
                }
                break;
            case "Get All":
                this.populateList(true);
                break;
            case "Filter":
                try {
                    this.populateList(homeworkService.filter(Utils.tryParseInt(deadlineText.getText()),
                            Utils.NullOrString(taskText.getText())));
                } catch (Exception exception) {
                    showError(exception.getMessage());
                }
                break;
        }
    }
}
