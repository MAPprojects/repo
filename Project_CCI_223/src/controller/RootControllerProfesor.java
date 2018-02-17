package controller;

import Main.StartApplication;
import domain.User;
import exceptii.EntityNotFoundException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;
import service.Service;
import java.io.IOException;
import java.util.Optional;

public class RootControllerProfesor extends RootController {

    @FXML
    private AnchorPane anchorPaneStudenti;

    @Override
    public void setSaptamana(Integer saptamana){
        super.setSaptamana(saptamana);
        initStudentPane(service,anchorPaneStudenti);
    }

    void initStudentPane(Service service, AnchorPane anchorPaneStudenti) {
        try {
            FXMLLoader loaderSt = new FXMLLoader();
            loaderSt.setLocation(StartApplication.class.getResource("/view_FXML/student.fxml"));
            AnchorPane root = (AnchorPane) loaderSt.load();

            ControllerStudent ctrlStudent = loaderSt.getController();
            try {
                Optional<User> userOptional = serviceUsers.getUser(currentUser.getUsername());
                if (userOptional.isPresent())
                    ctrlStudent.setCurrentUser(userOptional.get());
            } catch (EntityNotFoundException e) {
                handleIesireCont();
            }
            ctrlStudent.setService(service);
            service.addObserver(ctrlStudent);

            anchorPaneStudenti.getChildren().setAll(root);
            AnchorPane.setRightAnchor(root, 5d);
            AnchorPane.setLeftAnchor(root, 5d);
            AnchorPane.setTopAnchor(root, 5d);
            AnchorPane.setBottomAnchor(root, 5d);

        } catch (IOException e) {
        }
    }

    @FXML
    public void handleOpenTabStudenti(){
        Tab stTab=new Tab();
        tabPane.getTabs().add(stTab);
        stTab.setText("Studenti");
        AnchorPane anchorPane=new AnchorPane();
        stTab.setContent(anchorPane);
        initStudentPane(service,anchorPane);
    }

    @Override
    public void update(){
        super.update();
        anchorPaneStudenti.getChildren().removeAll();
        initStudentPane(service,anchorPaneStudenti);
    }

    @Override
    public void handleOpenAllTabs(ActionEvent event){
        super.handleOpenAllTabs(event);
        handleOpenTabStudenti();
    }
}
