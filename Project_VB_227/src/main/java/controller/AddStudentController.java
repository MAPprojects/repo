package controller;

import entities.Role;
import exceptions.AbstractValidatorException;
import exceptions.StudentServiceException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.hibernate.exception.ConstraintViolationException;
import services.StudentService;
import services.UserSerivce;
import thread_utils.NotifyingThread;
import thread_utils.ThreadCompleteListener;

import javax.mail.*;
import javax.xml.bind.ValidationException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class AddStudentController implements ThreadCompleteListener {

    private static final String MESAJ_EROARE_NUME = "Numele nu poate fi vid";
    private static final String MESAJ_EROARE_GRUPA = "Grupa nu poate fi vida";
    private static final String MESAJ_EROARE_EMAIL = "Email-ul nu poate fi vid";
    private static final String MESAJ_EROARE_PROF = "Numele profesroului nu poate fi vid";
    private GaussianBlur gaussianBlur;
    private Stage stage;
    private AnchorPane studentViewRootLayout;
    private AnchorPane rootLayout;
    private ImageView loadingImageView;
    private StudentService studentService;
    private UserSerivce userSerivce;
    private boolean isValidEmail;
    @FXML
    private Text mesajEroareNume;
    @FXML
    private Text mesajEroareGrupa;
    @FXML
    private Text mesajEroareEmail;
    @FXML
    private Text mesajEroareProf;
    @FXML
    private TextField studentNumeField;
    @FXML
    private TextField studentGrupaField;
    @FXML
    private TextField studentEmailField;
    @FXML
    private TextField studentProfField;
    @FXML
    private Button addStudentButton;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setStudentService(StudentService studentService) {
        this.studentService = studentService;
    }

    public void setRootLayout(AnchorPane rootLayout) {
        this.rootLayout = rootLayout;
    }

    public void setStudentViewRootLayout(AnchorPane studentViewRootLayout) {
        this.studentViewRootLayout = studentViewRootLayout;
    }

    @FXML
    public void initialize() {
        gaussianBlur = new GaussianBlur();
    }

    @FXML
    private void addStudent(MouseEvent mouseEvent) throws FileNotFoundException {
        clearWarningForInputs();
        if (validateInfo()) {
            String nume = studentNumeField.getText();
            String grupa = studentGrupaField.getText();
            String email = studentEmailField.getText();
            String prof = studentProfField.getText();
            //acum cream un user pentru studentul adaugat
            String parolaTemporara = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
            //loading the image and setting the image view
            FileInputStream loadingGifInputStream = new FileInputStream("src\\main\\resources\\views\\loadingScene\\loader.gif");
            Image loadingImage = new Image(loadingGifInputStream);
            loadingImageView = new ImageView(loadingImage);
            rootLayout.getChildren().add(loadingImageView);
            loadingImageView.setX(rootLayout.getWidth() / 2 - 80);
            loadingImageView.setY(rootLayout.getHeight() / 2 - 100);
            rootLayout.getChildren().forEach(node -> {
                if (node != loadingImageView) {
                    node.setDisable(true);
                    node.setEffect(gaussianBlur);
                }
            });
            NotifyingThread addUserThread = new NotifyingThread() {
                @Override
                public void doRun() {
                    try {
                        isValidEmail = true;
                        userSerivce.addUser(email, parolaTemporara, Role.STUDENT);
                        studentService.addStudent(nume, grupa, email, prof);
                        Platform.runLater(() -> {
                            stage.close();
                        });
                    } catch (ValidationException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (AbstractValidatorException e) {
                        e.printStackTrace();
                    } catch (MessagingException e) {
                        isValidEmail = false;
                        Platform.runLater(() -> {
                            studentViewRootLayout.getChildren().forEach(node -> {
                                node.setDisable(true);
                                node.setEffect(gaussianBlur);
                            });
                            Alert emailAlert = new Alert(Alert.AlertType.ERROR);
                            emailAlert.setTitle("Adresa de email inexistenta");
                            emailAlert.setHeaderText("Adresa de mail oferita nu exista.");
                            emailAlert.setContentText("Incercati sa introduceti o noua adresa in campul destinat.");
                            emailAlert.show();
                        });
                    } catch (StudentServiceException e) {
                        e.printStackTrace();
                    } catch (ConstraintViolationException e) {
                        System.out.println("trolll");
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    } catch (NoSuchFieldException e) {
                        isValidEmail = false;
                        Platform.runLater(() -> {
                            studentViewRootLayout.getChildren().forEach(node -> {
                                node.setDisable(true);
                                node.setEffect(gaussianBlur);
                            });
                            Alert emailAlert = new Alert(Alert.AlertType.ERROR);
                            emailAlert.setTitle("Adresa de mail oferita nu este valida");
                            emailAlert.setHeaderText("Exista deja un user cu acest email sau username");
                            emailAlert.setContentText("Incercati sa introduceti o noua adresa in campul destinat.");
                            emailAlert.show();
                        });
                    }
                }
            };
            addUserThread.addListener(this);
            addUserThread.start();
        }
    }

    public void setUserSerivce(UserSerivce userSerivce) {
        this.userSerivce = userSerivce;
    }

    @FXML
    private void exitDialogAddStudentHandler(MouseEvent mouseEvent) {
        studentViewRootLayout.getChildren().forEach(node -> {
            node.setDisable(false);
            node.setEffect(null);
        });
        stage.close();
    }

    @Override
    public void notifyOfThreadComplete(Thread thread) {
        Platform.runLater(() -> {
            rootLayout.getChildren().remove(loadingImageView);
            rootLayout.getChildren().forEach(node -> {
                node.setDisable(false);
                node.setEffect(null);
            });
            if (isValidEmail) {
                studentViewRootLayout.getChildren().forEach(node -> {
                    node.setDisable(false);
                    node.setEffect(null);
                });
            }
        });
    }

    private void clearWarningForInputs() {
        mesajEroareNume.setText("");
        mesajEroareGrupa.setText("");
        mesajEroareEmail.setText("");
        mesajEroareProf.setText("");
    }

    private boolean validateInfo() {
        boolean infoValide = true;
        String nume = studentNumeField.getText();
        String grupa = studentGrupaField.getText();
        String email = studentEmailField.getText();
        String prof = studentProfField.getText();
        if (nume.equals("") || nume.equals(MESAJ_EROARE_NUME)) {
            studentNumeField.setText(MESAJ_EROARE_NUME);
            studentNumeField.setStyle("-fx-text-inner-color: red; -fx-font-size: 14;");
            infoValide = false;
        }
        if (grupa.equals("") || grupa.equals(MESAJ_EROARE_GRUPA)) {
            studentGrupaField.setText(MESAJ_EROARE_GRUPA);
            studentGrupaField.setStyle("-fx-text-inner-color: red; -fx-font-size: 14;");
            infoValide = false;
        }
        if (email.equals("") || email.equals(MESAJ_EROARE_EMAIL)) {
            studentEmailField.setText(MESAJ_EROARE_EMAIL);
            studentEmailField.setStyle("-fx-text-inner-color: red; -fx-font-size: 14;");
            infoValide = false;
        }
        if (prof.equals("") || prof.equals(MESAJ_EROARE_PROF)) {
            studentProfField.setText(MESAJ_EROARE_PROF);
            studentProfField.setStyle("-fx-text-inner-color: red; -fx-font-size: 14;");
            infoValide = false;
        }
        return infoValide;
    }

    @FXML
    private void deleteErrorMessageGrupa(MouseEvent mouseEvent) {
        if (studentGrupaField.getText().equals(MESAJ_EROARE_GRUPA)) {
            studentGrupaField.setText("");
            studentGrupaField.setStyle("-fx-text-inner-color: black; -fx-font-size: 18;");
        }
    }

    @FXML
    private void deleteErrorMessageEmail(MouseEvent mouseEvent) {
        if (studentEmailField.getText().equals(MESAJ_EROARE_EMAIL)) {
            studentEmailField.setText("");
            studentEmailField.setStyle("-fx-text-inner-color: black; -fx-font-size: 18;");
        }
    }

    @FXML
    private void deleteErrorMessageProf(MouseEvent mouseEvent) {
        if (studentProfField.getText().equals(MESAJ_EROARE_PROF)) {
            studentProfField.setText("");
            studentProfField.setStyle("-fx-text-inner-color: black; -fx-font-size: 18;");
        }
    }

    @FXML
    private void deleteErrorMessageNume(MouseEvent mouseEvent) {
        if (studentNumeField.getText().equals(MESAJ_EROARE_NUME)) {
            studentNumeField.setText("");
            studentNumeField.setStyle("-fx-text-inner-color: black; -fx-font-size: 18;");
        }
    }

    @FXML
    private void changeMessageAfterErrorNume(KeyEvent inputMethodEvent) {
        studentNumeField.setStyle("-fx-text-inner-color: black; -fx-font-size: 18;");
    }

    @FXML
    private void changeMessageAfterErrorGrupa(KeyEvent inputMethodEvent) {
        studentGrupaField.setStyle("-fx-text-inner-color: black; -fx-font-size: 18;");
    }

    @FXML
    private void changeMessageAfterErrorEmail(KeyEvent inputMethodEvent) {
        studentEmailField.setStyle("-fx-text-inner-color: black; -fx-font-size: 18;");
    }

    @FXML
    private void changeFontAfterErrorProf(KeyEvent inputMethodEvent) {
        studentProfField.setStyle("-fx-text-inner-color: black; -fx-font-size: 18;");
    }
}
