package Graphics.Login;

import Domain.Category;
import Domain.User;
import Graphics.CreateAccountWindow;
import Graphics.MainGui.MainController;
import Repository.UsersRepository;
import Services.UsersService;
import Utilities.MD5Encrypter;
import Utilities.TextFieldFormatter;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class LoginController {

    private MainController rootController;
    @FXML
    BorderPane mainPane;
    @FXML
    Button loginButton;
    @FXML
    TextField userField;
    @FXML
    PasswordField passwordField;
    @FXML
    Label signup;
    private UsersService users;

    public LoginController() {}

    @FXML
    public void initialize() {
        loginButton.setTooltip(new Tooltip("admin:admin?"));
        userField.setTooltip(new Tooltip("admin:admin?"));
        passwordField.setTooltip(new Tooltip("admin:admin?"));
        TextFieldFormatter.formatEmailField(userField);
        TextFieldFormatter.formatPasswordField(passwordField);
        loginButton.setOnAction(event -> {
            handleLogin();
        });

        mainPane.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER) {
                    handleLogin();
                }
            }
        });

    }

    private void handleLogin() {
        String username = userField.getText();
        if (users.find(username)) {
            User user = users.get(username);
            if (user.getPassword().equals(MD5Encrypter.getHash(passwordField.getText()))) {
                //check user category
                if (user.getCategory().equals(Category.STUDENT)) {
                    //loginButton.setText("logged in as student");
                    rootController.loadStudentsView(username);
                }

                if (user.getCategory().equals(Category.PROFESSOR)) {
                    //loginButton.setText("logged in as professor");
                    rootController.loadTeacherView(username);
                }
            }
        }
        userField.setText("");
        passwordField.setText("");
    }

    @FXML
    public void createAccount() {
        CreateAccountWindow window = new CreateAccountWindow(users);
        window.show();
    }

    public void setRootController(MainController rootController) {
        this.rootController = rootController;
    }

    public void setService(UsersService service) {
        this.users = service;
    }

    public UsersService getUsers() {
        return users;
    }

    public void addTextFieldsListeners() {
        final BooleanProperty firstSelection = new SimpleBooleanProperty(true);
        userField.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue && firstSelection.get()) {
                    mainPane.requestFocus();
                    firstSelection.setValue(false);
                }
            }
        });
    }

}
