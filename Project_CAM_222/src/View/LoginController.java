package View;

import Domain.*;
import Repository.*;
import Service.Service;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LoginController {
    private Stage primaryStage;
    public Stage helpSignInStage = null;
    public Stage createNewAccountStage = null;

    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField passwordField;
    @FXML
    public CheckBox rememberCheckBox;
    @FXML
    private ImageView logoImageView;
    @FXML
    private CheckBox administratorsCheckBox;
    @FXML
    private CheckBox moderatorsCheckBox;
    @FXML
    private CheckBox standardUsersCheckBox;
    @FXML
    private ListView<HBox> accountListView;

    @FXML
    private void initialize() {
        // Loading and preparing the top-left logo image.
        Image image = new Image("Resources/Images/LoginLogo.png");
        logoImageView.setImage(image);
        logoImageView.setFitHeight(50);
        logoImageView.setFitWidth(50);
        logoImageView.setSmooth(true);

        // Checking all the account checkboxes at the beginning
        administratorsCheckBox.setSelected(true);
        moderatorsCheckBox.setSelected(true);
        standardUsersCheckBox.setSelected(true);
        accountListCheckboxHandler(null);

        // "rememberCheckBox" checkbox state checking
        loadRememberCheckBoxState();
    }

    public void loadRememberCheckBoxState() {
        try {
            Path path = Paths.get("./src/Resources/Login.enc");
            Stream<String> lines;
            lines = Files.lines(path);
            String[] l = lines.collect(Collectors.toList()).get(0).split("[;]",-1);
            if (l[0].equals("1")) {
                rememberCheckBox.setSelected(true);
                usernameTextField.setText(l[1]);
                passwordField.setText(l[2]);
            }
            else {
                rememberCheckBox.setSelected(false);
            }
        }
        catch (IOException e) {
            System.err.println("Eroare IO: " + e);
            System.exit(1);
        }
    }

    public void saveRememberCheckBoxState() {
        try (PrintWriter pw = new PrintWriter("./src/Resources/Login.enc")) {
            String s = "";
            if (rememberCheckBox.isSelected())
                s = s + "1;";
            else
                s = s + "0;";
            s = s + usernameTextField.getText() + ";" + passwordField.getText();
            pw.println(s);
        } catch (IOException e) {
            System.err.println("Eroare IO: " + e);
            System.exit(1);
        }
    }

    public void accountListCheckboxHandler(ActionEvent actionEvent) {
        accountListView.getItems().clear();
        try {
            Path path = Paths.get("./src/Resources/Accounts.enc");
            Stream<String> lines;
            lines = Files.lines(path);
            lines.forEach(s -> {
                String[] l = s.split("[;]");
                if ((administratorsCheckBox.isSelected() && Objects.equals(l[3], "Administrator")) ||
                    (moderatorsCheckBox.isSelected() && Objects.equals(l[3], "Moderator")) ||
                    (standardUsersCheckBox.isSelected() && Objects.equals(l[3], "Standard User"))) {
                    // construim HBox-ul mare
                    HBox hbox = new HBox();
                    hbox.setSpacing(6);

                    // punem imaginea in stanga
                    Image image;
                    try {
                        image = new Image(l[2]);
                    } catch (IllegalArgumentException e) {
                        image = new Image("Resources/Images/Missing.jpg");
                    }
                    ImageView accountImageView = new ImageView();
                    accountImageView.setImage(image);
                    accountImageView.setFitHeight(45);
                    accountImageView.setFitWidth(30);
                    accountImageView.setSmooth(true);
                    hbox.getChildren().add(accountImageView);

                    // construim inca un vbox, ce contine rolul si mailul userului
                    VBox vbox = new VBox();

                    // rolul userului
                    Text text = new Text(l[3]);
                    text.setFont(new Font("Segoe UI",14));
                    if (Objects.equals(l[3], "Administrator"))
                        text.setFill(Color.web("#FF0000"));
                    if (Objects.equals(l[3], "Moderator"))
                        text.setFill(Color.web("#0000FF"));
                    if (Objects.equals(l[3], "Standard User"))
                        text.setFill(Color.web("#FFFFFF"));
                    vbox.getChildren().add(text);

                    // mailul userului
                    Label label = new Label(l[0]);
                    label.setTranslateY(6);
                    vbox.getChildren().add(label);

                    // adaugam vbox-ul in hbox
                    hbox.getChildren().add(vbox);

                    // adaugam hbox-ul la lista de conturi
                    accountListView.getItems().add(hbox);
                }
            });
        }
        catch (IOException e) {
            System.err.println("Eroare IO: " + e);
            System.exit(1);
        }
    }

    public void accountSelectHandler() {
        if (accountListView.getSelectionModel().getSelectedItem() != null) {
            VBox vbox = (VBox) accountListView.getSelectionModel().getSelectedItem().getChildren().get(1);
            Label label = (Label) vbox.getChildren().get(1);
            usernameTextField.setText(label.getText());
        }
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setOnCloseRequest(event -> saveRememberCheckBoxState());
        primaryStage.getScene().setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case ENTER:
                    loginHandler(null);
                    break;
            }
        });
    }

    public void closeButtonHandler(ActionEvent actionEvent) {
        saveRememberCheckBoxState();
        primaryStage.close();
    }

    public void helpSignInHandler(ActionEvent actionEvent) {
        if (helpSignInStage == null)
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("HelpSignInView.fxml"));
                Scene scene = new Scene(fxmlLoader.load());

                HelpSignInController helpSignInController = fxmlLoader.getController();
                helpSignInController.setLoginController(this);
                Stage stage = new Stage();
                helpSignInController.setPrimaryStage(stage);
                stage.setTitle("Help Signing In");
                stage.setScene(scene);
                stage.setResizable(false);
                helpSignInStage = stage;
                stage.show();
            } catch (IOException e) {
                System.err.println("Eroare IO: " + e);
                System.exit(1);
            }
        else {
            helpSignInStage.toFront();
        }
    }

    public void createNewAccountHandler(ActionEvent actionEvent) {
        if (createNewAccountStage == null)
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CreateNewAccountView.fxml"));
                Scene scene = new Scene(fxmlLoader.load());

                CreateNewAccountController createNewAccountController = fxmlLoader.getController();
                createNewAccountController.setLoginController(this);
                Stage stage = new Stage();
                createNewAccountController.setPrimaryStage(stage);
                stage.setTitle("Create A New Account");
                stage.setScene(scene);
                stage.setResizable(false);
                createNewAccountStage = stage;
                stage.show();
            } catch (IOException e) {
                System.err.println("Eroare IO: " + e);
                System.exit(1);
            }
        else {
            createNewAccountStage.toFront();
        }
    }

    public String getRole(String accountName) {
        try {
            Path path = Paths.get("./src/Resources/Accounts.enc");
            Stream<String> lines;
            lines = Files.lines(path);
            for (String line : lines.collect(Collectors.toList())) {
                String[] l = line.split("[;]");
                if (l[0].equals(accountName))
                    return l[3];
            }
        } catch (IOException e) {
            System.err.println("Eroare IO: " + e);
            System.exit(1);
        }
        return "";
    }

    public String getProfilePicturePath(String accountName) {
        try {
            Path path = Paths.get("./src/Resources/Accounts.enc");
            Stream<String> lines;
            lines = Files.lines(path);
            for (String line : lines.collect(Collectors.toList())) {
                String[] l = line.split("[;]");
                if (l[0].equals(accountName))
                    return l[2];
            }
        } catch (IOException e) {
            System.err.println("Eroare IO: " + e);
            System.exit(1);
        }
        return "";
    }

    public void loginHandler(ActionEvent actionEvent) {
        class AtomicBoolean {
            private boolean valoare;

            private AtomicBoolean() {
                valoare = false;
            }

            public void setValoare(boolean valoare) {
                this.valoare = valoare;
            }

            public boolean getValoare() {
                return valoare;
            }
        }

        try {
            // Verificam daca exista contul & parola scrise in textField-uri
            Path path = Paths.get("./src/Resources/Accounts.enc");
            Stream<String> lines;
            lines = Files.lines(path);
            AtomicBoolean contExistent = new AtomicBoolean();
            lines.forEach(s -> {
                String[] l = s.split("[;]");
                if (l[0].equals(usernameTextField.getText()) && l[1].equals(hashPassword(passwordField.getText())))
                    contExistent.setValoare(true);
            });

            // Daca exista, ne conectam; altfel, afisam o eroare
            if (!contExistent.getValoare()) {
                showErrorMessage("   You have entered your password or account name incorrectly. " +
                        "Please check your password and account name and try again.");
            }
            else {
                saveRememberCheckBoxState();
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("RootView.fxml"));
                    Scene scene = new Scene(fxmlLoader.load());

                    RootController rootController = fxmlLoader.getController();
                    rootController.setAccountName(usernameTextField.getText());
                    rootController.setAccountRole(getRole(usernameTextField.getText()));
                    rootController.setAccountPicturePath(getProfilePicturePath(usernameTextField.getText()));
                    Stage stage = new Stage();
                    stage.setTitle("Lab Manager™ v1.0.0");
                    stage.setScene(scene);
                    stage.setResizable(false);
                    stage.getIcons().add(new Image(getClass().getResourceAsStream("../Resources/Images/LoginLogo.png")));
                    rootController.setPrimaryStage(stage);

                    IRepository<Integer, Student> repositoryStudenti =
                            new RepositoryStudentiXML(new ValidatorRepositoryStudenti(), "src/Resources/Students.xml");
                    IRepository<Integer, Tema> repositoryTeme =
                            new RepositoryTemeXML(new ValidatorRepositoryTeme(), "src/Resources/Assignments.xml");
                    IRepository<String, Nota> repositoryNote =
                            new RepositoryNoteXML(new ValidatorRepositoryNote(), "src/Resources/Grades.xml", repositoryStudenti, repositoryTeme);
                    Service service = new Service(repositoryStudenti, repositoryTeme, repositoryNote);
                    rootController.setService(service);

                    primaryStage.close();
                    stage.show();
                } catch (IOException e) {
                    System.err.println("Eroare IO: " + e);
                    System.exit(1);
                }
            }
        }
        catch (IOException e) {
            System.err.println("Eroare IO: " + e);
            System.exit(1);
        }
    }

    public static void showMessage(Alert.AlertType type, String header, String text) {
        Alert message = new Alert(type);
        message.setHeaderText(header);
        message.setContentText(text);
        message.getDialogPane().getStylesheets().add("View/DarkTheme.css");
        message.showAndWait();
    }

    public static void showErrorMessage(String text) {
        showMessage(Alert.AlertType.WARNING, "Warning!", text);
    }

    public static String hashPassword(String password) {
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());
            byte[] bytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte aByte : bytes)
                sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
            generatedPassword = sb.toString();
            return generatedPassword;
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
