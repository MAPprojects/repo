package controller;

import entities.Role;
import exceptions.AbstractValidatorException;
import exceptions.StudentServiceException;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import services.StudentService;
import services.UserSerivce;
import thread_utils.NotifyingThread;
import thread_utils.ThreadCompleteListener;

import javax.mail.MessagingException;
import javax.xml.bind.ValidationException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class AddUserController implements ThreadCompleteListener {

    private static final String MESAJ_EROARE_EMAIL_VID = "Email-ul nu poate fi vid.";
    private static final String MESAJ_EROARE_PAROLA_VIDA = "Parola nu poate fi vida.";
    private static final String MESAJ_EROARE_NUME_VID = "Numele nu poate fi vid.";
    private static final String MESAJ_EROARE_GRUPA_VIDA = "Grupa nu poate fi vida.";
    private static final String MESAJ_EROARE_PROF_VID = "Numele profesroului este vid.";
    private static final String MESAJ_EROARE_ROL_VID = "Niciun rol ales";
    private ImageView loadingImageView;
    private AnchorPane sysConfigRootLayout;
    private GaussianBlur gaussianBlur;
    private AnchorPane rootLayout;
    private boolean isValidEmailForStudentUser;
    private Stage addUserStage;
    private UserSerivce userSerivce;
    private StudentService studentService;
    @FXML
    private Pane noRolAlesWarning;
    @FXML
    private TextField userPArolaField;
    @FXML
    private TextField userEmailField;
    @FXML
    private ComboBox comboRol;
    @FXML
    private Pane detaliiStudentUser;
    @FXML
    private Text textEroareFieldEmail;
    @FXML
    private Text mesajEroareFieldGrupa;
    @FXML
    private Text mesajEroareFieldProfesor;
    @FXML
    private Text mesajEroareNume;
    @FXML
    private Text mesajEroareFieldParola;
    @FXML
    private TextField fieldUSerNumeStudent;
    @FXML
    private TextField fieldUSerGRupaStudent;
    @FXML
    private TextField fieldUserProfCoordonator;
    @FXML
    private Text mesajEroareComboRol;

    @FXML
    public void initialize() {
        ObservableList<Role> roles = FXCollections.observableArrayList();
        roles.setAll(Role.STUDENT, Role.ADMINISTRATOR, Role.PROFESOR);
        comboRol.setItems(roles);
        detaliiStudentUser.setVisible(false);
        clearErrorFields();
        gaussianBlur = new GaussianBlur();
        noRolAlesWarning.setVisible(false);
        mesajEroareComboRol.setText("Niciun rol ales.");
        String parola = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        userPArolaField.setText(parola);
    }

    public void setAddUserStage(Stage addUserStage) {
        this.addUserStage = addUserStage;
    }

    public void setUserSerivce(UserSerivce userSerivce) {
        this.userSerivce = userSerivce;
    }

    public void setStudentService(StudentService studentService) {
        this.studentService = studentService;
    }

    public void setRootLayout(AnchorPane rootLayout) {
        this.rootLayout = rootLayout;
    }

    public void setSysConfigRootLayout(AnchorPane sysConfigRootLayout) {
        this.sysConfigRootLayout = sysConfigRootLayout;
    }

    @FXML
    private void addUser(MouseEvent mouseEvent) throws FileNotFoundException {
        Role rol = (Role) comboRol.getSelectionModel().getSelectedItem();
        if (rol != null) {
            if (!rol.equals(Role.STUDENT)) {
                if (validateFieldForAdminOrTeacher()) {
                    String email = userEmailField.getText();
                    String parola = userPArolaField.getText();
                    sysConfigRootLayout.getChildren().forEach(node -> {
                        node.setDisable(false);
                        node.setEffect(null);
                    });
                    addUserStage.close();
                    try {
                        userSerivce.addUser(email, parola, rol);
                        addUserStage.close();
                    } catch (ValidationException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (AbstractValidatorException e) {
                        e.printStackTrace();
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    } catch (NoSuchFieldException e) {
                        Alert emailAlert = new Alert(Alert.AlertType.ERROR);
                        emailAlert.setTitle("Adresa de mail oferita nu exista");
                        emailAlert.setHeaderText("Adresa de mail oferita nu exista.");
                        emailAlert.setContentText("Incercati sa introduceti o noua adresa in campul destinat.");
                        emailAlert.show();
                    }
                }
            } else {
                if (validateFieldsForStudent()) {
                    isValidEmailForStudentUser = true;
                    String email = userEmailField.getText();
                    String parola = userPArolaField.getText();
                    String nume = fieldUSerNumeStudent.getText();
                    String grupa = fieldUSerGRupaStudent.getText();
                    String prof = fieldUserProfCoordonator.getText();
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
                    NotifyingThread notifyingThread = new NotifyingThread() {
                        @Override
                        public void doRun() {
                            try {
                                userSerivce.addUser(email, parola, rol);
                                studentService.addStudent(nume, grupa, email, prof);
                            } catch (ValidationException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (AbstractValidatorException e) {
                                e.printStackTrace();
                            } catch (MessagingException e) {
                                isValidEmailForStudentUser = false;
                                Platform.runLater(() -> {
                                    rootLayout.getChildren().forEach(node -> {
                                        node.setDisable(false);
                                        node.setEffect(null);
                                    });
                                    Alert emailAlert = new Alert(Alert.AlertType.ERROR);
                                    emailAlert.setTitle("Adresa de mail oferita nu este valida");
                                    emailAlert.setHeaderText("Exista deja un user cu acest email sau username");
                                    emailAlert.setContentText("Incercati sa introduceti o noua adresa in campul destinat.");
                                    emailAlert.show();
                                });
                            } catch (StudentServiceException e) {
                                e.printStackTrace();
                            } catch (NoSuchAlgorithmException e) {
                                e.printStackTrace();
                            } catch (NoSuchFieldException e) {
                                Alert emailAlert = new Alert(Alert.AlertType.ERROR);
                                emailAlert.setTitle("Adresa de mail oferita nu este valida");
                                emailAlert.setHeaderText("Exista deja un user cu acest email sau username");
                                emailAlert.setContentText("Incercati sa introduceti o noua adresa in campul destinat.");
                                emailAlert.show();
                            }
                        }
                    };
                    notifyingThread.addListener(this);
                    notifyingThread.start();
                }
            }
        } else {
            noRolAlesWarning.setVisible(true);
        }
    }

    @FXML
    private void handleDetaliiStudentUser(MouseEvent mouseEvent) {
        Role selectedRole = (Role) comboRol.getSelectionModel().getSelectedItem();
        if (selectedRole != null) {
            if (selectedRole.equals(Role.STUDENT)) {
                detaliiStudentUser.setVisible(true);
            } else {
                detaliiStudentUser.setVisible(false);
            }
        }
    }

    private boolean validateFieldsForStudent() {
        clearErrorFields();
        boolean infoVaide = true;
        String email = userEmailField.getText();
        if (email.equals("") || email.equals(MESAJ_EROARE_EMAIL_VID)) {
            userEmailField.setText(MESAJ_EROARE_EMAIL_VID);
            userEmailField.setStyle("-fx-text-inner-color: red;");
            infoVaide = false;
        }
        if (userPArolaField.getText().equals("") || userPArolaField.getText().equals(MESAJ_EROARE_PAROLA_VIDA)) {
            userPArolaField.setText(MESAJ_EROARE_PAROLA_VIDA);
            userPArolaField.setStyle("-fx-text-inner-color: red;");
            infoVaide = false;
        }
        if (fieldUSerNumeStudent.getText().equals("") || fieldUSerNumeStudent.getText().equals(MESAJ_EROARE_NUME_VID)) {
            fieldUSerNumeStudent.setText(MESAJ_EROARE_NUME_VID);
            fieldUSerNumeStudent.setStyle("-fx-text-inner-color: red;");
            infoVaide = false;
        }
        if (fieldUSerGRupaStudent.getText().equals("") || fieldUSerGRupaStudent.getText().equals(MESAJ_EROARE_GRUPA_VIDA)) {
            fieldUSerGRupaStudent.setText(MESAJ_EROARE_GRUPA_VIDA);
            fieldUSerGRupaStudent.setStyle("-fx-text-inner-color: red;");
            infoVaide = false;
        }
        if (fieldUserProfCoordonator.getText().equals("") || fieldUserProfCoordonator.getText().equals(MESAJ_EROARE_PROF_VID)) {
            fieldUserProfCoordonator.setText(MESAJ_EROARE_PROF_VID);
            fieldUserProfCoordonator.setStyle("-fx-text-inner-color: red;");
            infoVaide = false;
        }
        return infoVaide;
    }

    private boolean validateFieldForAdminOrTeacher() {
        clearErrorFields();
        boolean infoVaide = true;
        String email = userEmailField.getText();
        if (email.equals("") || email.equals(MESAJ_EROARE_EMAIL_VID)) {
            userEmailField.setText(MESAJ_EROARE_EMAIL_VID);
            userEmailField.setStyle("-fx-text-inner-color: red;");
            infoVaide = false;
        }
        if (userPArolaField.getText().equals("") || userPArolaField.getText().equals(MESAJ_EROARE_PAROLA_VIDA)) {
            userPArolaField.setText(MESAJ_EROARE_PAROLA_VIDA);
            userPArolaField.setStyle("-fx-text-inner-color: red;");
            infoVaide = false;
        }
        return infoVaide;
    }

    @FXML
    private void comboRolClickHandler(MouseEvent mouseEvent) {
        noRolAlesWarning.setVisible(false);
    }

    private void clearErrorFields() {
        mesajEroareFieldGrupa.setText("");
        mesajEroareFieldParola.setText("");
        mesajEroareFieldProfesor.setText("");
        textEroareFieldEmail.setText("");
        mesajEroareNume.setText("");
        mesajEroareFieldGrupa.setText("");
        textEroareFieldEmail.setText("");
        mesajEroareComboRol.setText("");
    }

    @FXML
    private void closeAddUserStage(MouseEvent mouseEvent) {
        sysConfigRootLayout.getChildren().forEach(node -> {
            node.setDisable(false);
            node.setEffect(null);
        });
        addUserStage.close();
    }

    @Override
    public void notifyOfThreadComplete(Thread thread) {
        Platform.runLater(() -> {
            rootLayout.getChildren().remove(loadingImageView);
            rootLayout.getChildren().forEach(node -> {
                node.setDisable(false);
                node.setEffect(null);
            });
            if (isValidEmailForStudentUser) {
                sysConfigRootLayout.getChildren().forEach(node -> {
                    node.setDisable(false);
                    node.setEffect(null);
                });
                addUserStage.close();
            }
        });
    }

    @FXML
    private void delteErrorOnKeyEMail(KeyEvent keyEvent) {
        userEmailField.setStyle("-fx-text-inner-color: black;");
    }

    @FXML
    private void removeErrorEmailOnClick(MouseEvent mouseEvent) {
        if (userEmailField.getText().equals(MESAJ_EROARE_EMAIL_VID)) {
            userEmailField.setText("");
            userEmailField.setStyle("-fx-text-inner-color: black;");
        }
    }

    @FXML
    private void delteErrorOnKeyPArola(KeyEvent keyEvent) {
        userPArolaField.setStyle("-fx-text-inner-color: black;");
    }

    @FXML
    private void removeErrorOnClickParola(MouseEvent mouseEvent) {
        if (userPArolaField.getText().equals(MESAJ_EROARE_PAROLA_VIDA)) {
            userPArolaField.setText("");
            userPArolaField.setStyle("-fx-text-inner-color: black;");
        }
    }

    @FXML
    private void delteErrorOnKeyEMailNume(KeyEvent keyEvent) {
        fieldUSerNumeStudent.setStyle("-fx-text-inner-color: black;");
    }

    @FXML
    private void removeErrorOnClickNume(MouseEvent mouseEvent) {
        if (fieldUSerNumeStudent.getText().equals(MESAJ_EROARE_NUME_VID)) {
            fieldUSerNumeStudent.setText("");
            fieldUSerNumeStudent.setStyle("-fx-text-inner-color: black;");
        }
    }

    @FXML
    private void delteErrorOnKeyGrupa(KeyEvent keyEvent) {
        fieldUSerGRupaStudent.setStyle("-fx-text-inner-color: black;");
    }

    @FXML
    private void removeErrorOnClickGrupa(MouseEvent mouseEvent) {
        if (fieldUSerGRupaStudent.getText().equals(MESAJ_EROARE_GRUPA_VIDA)) {
            fieldUSerGRupaStudent.setText("");
            fieldUSerGRupaStudent.setStyle("-fx-text-inner-color: black;");
        }
    }

    @FXML
    private void delteErrorOnKeyProf(KeyEvent keyEvent) {
        fieldUserProfCoordonator.setStyle("-fx-text-inner-color: black;");
    }

    @FXML
    private void removeErrorOnClickProf(MouseEvent mouseEvent) {
        if (fieldUserProfCoordonator.getText().equals(MESAJ_EROARE_PROF_VID)) {
            fieldUserProfCoordonator.setText("");
            fieldUserProfCoordonator.setStyle("-fx-text-inner-color: black;");
        }
    }
}
