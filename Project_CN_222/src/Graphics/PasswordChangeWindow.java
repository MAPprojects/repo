package Graphics;

import Domain.User;
import Exceptions.RepositoryException;
import Exceptions.ValidationException;
import Services.UsersService;
import Utilities.MD5Encrypter;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class PasswordChangeWindow {
    private UsersService service;

    public PasswordChangeWindow(UsersService service) {
        this.service = service;
    }

    public void show(String username) {
        Stage stage = new Stage();
        Pane prompt = new AnchorPane();
        prompt.setPrefHeight(100d);
        prompt.setPrefWidth(200d);
        PasswordField currentPwd = new PasswordField();
        PasswordField newPwd = new PasswordField();
        Button changeBtn = new Button("change password");

        currentPwd.setPromptText("current password");
        newPwd.setPromptText("new password");
        VBox box = new VBox();
        box.getChildren().addAll(currentPwd, newPwd, changeBtn);
        prompt.getChildren().addAll(box);
        changeBtn.setOnAction(event -> {
            try {
                User user = service.get(username);
//                System.out.println(currentPwd.getText());
//                System.out.println(user.getPassword());
                if (!(MD5Encrypter.getHash(currentPwd.getText()).equals(user.getPassword()))) {
//                    System.out.println(currentPwd.getText());
//                    System.out.println(user.getPassword());
                    throw new ValidationException("user password does not match");
                }
                service.update(username, MD5Encrypter.getHash(newPwd.getText()));
                stage.close();
            } catch (RepositoryException | ValidationException e) {
                AlertMessage.show("error","error ocurred", e.getMessage(), Alert.AlertType.ERROR);
            }
        });
        addStyle(prompt);
        stage.setResizable(false);
        stage.setScene(new Scene(prompt));
        stage.showAndWait();
    }

    private void addStyle(Pane pane) {
        pane.getStylesheets().add("/Graphics/Styles/passwordChange.css");
        pane.getStyleClass().add("pane");
    }
}
