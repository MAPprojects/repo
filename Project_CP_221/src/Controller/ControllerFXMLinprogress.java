package Controller;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

public class ControllerFXMLinprogress {

    @FXML
    private Button buttonCancelTask;
    @FXML
    protected Label labelInProgressTask;
    @FXML
    protected ProgressBar progressBar;
    public ControllerFXMLinprogress(){}
    @FXML
    public void initialize()
    {
    }

    protected void bindComponents(Task<Void> task)
    {
        labelInProgressTask.textProperty().bind(task.messageProperty());
        progressBar.progressProperty().bind(task.progressProperty());
    }

    protected void unbindProgressBar()
    {
        progressBar.progressProperty().unbind();
    }

    public void setLabelInProgressTask(String text)
    {
        labelInProgressTask.setText(text);
        System.out.println("am setat text pe buton");
    }

}
