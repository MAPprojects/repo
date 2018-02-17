package Graphics;

import Domain.Category;
import Domain.User;
import Exceptions.RepositoryException;
import Exceptions.ValidationException;
import Services.UsersService;
import Utilities.MD5Encrypter;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class CreateAccountWindow {
    private UsersService service;

    public CreateAccountWindow(UsersService service) {
        this.service = service;
    }

    public void show() {
        Stage stage = new Stage();
        Pane prompt = new AnchorPane();
        prompt.setPrefHeight(170d);
        prompt.setPrefWidth(200d);
        TextField username = new TextField();
        PasswordField password = new PasswordField();
        PasswordField confirmPassword = new PasswordField();
        PasswordField code = new PasswordField();
        Button signupBtn = new Button("signup");

        username.setPromptText("username");
        password.setPromptText("password");
        confirmPassword.setPromptText("confirm password");
        code.setPromptText("secret code");
        VBox box = new VBox();
        box.getChildren().addAll(username, password, confirmPassword, code, signupBtn);
        prompt.getChildren().addAll(box);
        signupBtn.setOnAction(event -> {
            try {
                if (password.getText().equals(confirmPassword.getText())) {
                    if (getSecretCodes().contains(MD5Encrypter.getHash(code.getText()))) {
                        service.add(username.getText(), MD5Encrypter.getHash(password.getText()), Category.PROFESSOR);
                        AlertMessage.show("success","user success","account created successfully", Alert.AlertType.CONFIRMATION);
                        stage.close();
                    } else {
                        throw new ValidationException("secret code error");
                    }
                } else {
                    throw new ValidationException("passwords do not match");
                }
            } catch (RepositoryException| ValidationException e) {
                AlertMessage.show("error", "user error", e.getMessage(), Alert.AlertType.ERROR);
            }
        });
        addStyle(prompt);
        stage.setResizable(false);
        stage.setScene(new Scene(prompt));
        stage.showAndWait();
    }

    private List<String> getSecretCodes() {
        List<String> res = new ArrayList<>();
        try (InputStream input = new FileInputStream("secretcodes.xml")) {
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            XMLStreamReader reader = inputFactory.createXMLStreamReader(input);

            String elem;
            while (reader.hasNext()) {
                int event = reader.next();
                switch (event) {
                    case XMLStreamReader.START_ELEMENT:
                        if (reader.getLocalName().equals("code")) {
                            elem = readCodeFromXML(reader);
                            res.add(elem);
                        }
                        break;
                }
            }

        } catch (IOException | XMLStreamException e) {
            e.printStackTrace();
        }
        return res;
    }

    private String readCodeFromXML(XMLStreamReader reader) throws XMLStreamException{
        String currentPropertyValue = "";
        while (reader.hasNext()) {
            int event = reader.next();
            switch (event) {
                case XMLStreamReader.CHARACTERS:
                    currentPropertyValue = reader.getText().trim();
                    break;
                case XMLStreamReader.END_ELEMENT:
                    if (reader.getLocalName().equals("code")) {
                        return currentPropertyValue;
                    } else {
                        currentPropertyValue = "";
                    }
                    break;
            }
        }
        throw new XMLStreamException("code could not be read from file");
    }

    private void addStyle(Pane pane) {
        pane.getStylesheets().add("/Graphics/Styles/changePwd.css");
    }

}
