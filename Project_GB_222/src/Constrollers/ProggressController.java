package Constrollers;

import Service.EmailSender;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.stage.Stage;
import Service.Service;
import java.util.ArrayList;
import javafx.concurrent.Task;
public class ProggressController
{
    @FXML ProgressIndicator progressIndicator;
    @FXML Button buttonProgress;
    @FXML Label labelMsg;

    Service service;
    public void setService(Service service) {
        this.service = service;
    }
    public ProggressController(){};

    public void handleCancelProgress(ActionEvent actionEvent) {
        Stage stage = (Stage) buttonProgress.getScene().getWindow();
        //paneTransparent.setVisible(false);
        stage.close();
    }
    @FXML
    public void initialize()
    {
        buttonProgress.setText("Ok");
        buttonProgress.setVisible(false);
        buttonProgress.setOnAction(this::handleCancelProgress);
    }

    public void setEmailsAndCheckBoxes(ArrayList<Integer> sendEmailTo)
    {

        Task<Void> runningTask=new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try
                {
                    int i=1;
                    int nr=sendEmailTo.size();
                    //progressIndicator.setProgress(i);
                    for (Integer id:sendEmailTo)
                    {
                        String email=service.findStudentById(id).get().getEmail();
                        String fileName="src\\Resources\\Student"+id+".txt";
                        EmailSender.send(email,fileName);
                        updateProgress(i,nr);
                        updateMessage(""+i+"/"+nr);
                        i=i+1;
                        //progressIndicator.setProgress(i);
                    }
                    buttonProgress.setVisible(true);


                }catch(Exception e)
                {
                    updateMessage("Canceled");
                    int nr=sendEmailTo.size();
                    updateProgress(0,nr);
                }
                return null;
            }};

        progressIndicator.progressProperty().bind(runningTask.progressProperty());
        labelMsg.textProperty().bind(runningTask.messageProperty());
        Thread th=new Thread(runningTask);
        th.setDaemon(true);
        th.start();

    }


}
