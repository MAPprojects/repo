package sample.ui.loginWindow;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import sample.domain.Candidat;
import sample.domain.UserType;
import sample.service.*;
import sample.ui.adminUI.*;
import sample.ui.userUI.UserController;


public class LoginWindowController {
    private FXMLLoader loaderAdminUI;
    private FXMLLoader loaderUserUI;
    private Stage stage;

    private ContService contService;
    private GeneralService generalService;
    /** fereastra de login */
    @FXML public BorderPane borderPaneLogin;
    @FXML public TextField câmpLogin_CNP;
    @FXML public PasswordField câmpLogin_Parolă;
    @FXML public Button butonLogin;
    @FXML public Label labelCreeazăContNou;
    @FXML public Label labelEroriLogin;
    /** fereastra creării unui cont nou */
    @FXML public BorderPane borderPaneCreareCont;
    @FXML public TextField câmpCC_CNP;
    @FXML public TextField câmpCC_Nume;
    @FXML public TextField câmpCC_Prenume;
    @FXML public TextField câmpCC_Telefon;
    @FXML public TextField câmpCC_Email;
    @FXML public PasswordField câmpCC_Parolă;
    @FXML public PasswordField câmpCC_ConfirmareParolă;
    @FXML public Label labelConfirmareParolă;
    @FXML public Button butonCreeazăCont;
    @FXML public Label labelÎnapoiLaLogin;
    @FXML public Label labelEroriCreareCont;

    private boolean afișezMesajLogin;
    private boolean afișezMesajCC;



    public LoginWindowController() {
        generalService = new GeneralService();
        contService = generalService.getContService();
        afișezMesajLogin = false;
        afișezMesajCC = false;
    }

    public void setGeneralService(GeneralService generalService) {
        this.generalService = generalService;
        contService = generalService.getContService();
    }

    public void setLoaderAdminUI(FXMLLoader loaderAdminUI) {
        this.loaderAdminUI = loaderAdminUI;
    }
    public void setLoaderUserUI(FXMLLoader loaderUserUI) {
        this.loaderUserUI = loaderUserUI;
    }
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void initialize() {
        borderPaneLogin.setVisible(true);
        borderPaneCreareCont.setVisible(false);
        labelConfirmareParolă.setText("Confirmare\nparolă");
    }


    /** fereastra de login */

    /**
     * Utilizatorul a dat click pe butonul Login sau a tastat Enter.
     * @param keyEvent
     */
    @FXML
    public void tryLogin(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.ENTER))
            login();
    }
    @FXML
    public void login(ActionEvent actionEvent) {
        login();
    }
    private void login() {
        String cnp = câmpLogin_CNP.getText();
        String parolă = câmpLogin_Parolă.getText();
        try {
            UserType tipUser = generalService.login(cnp, parolă);
            if (tipUser == UserType.ADMINISTRATOR)
                openAdminUI();
            else
                openUserUI(generalService.getCandidat(cnp));
        } catch (Exception e) {
            afișareMesajSmart(labelEroriLogin, e.getMessage(), false);
        }
    }


    /**
     * S-a introdus ID-ul și parola administratorului.
     * Se deschide fereastra administratorului.
     */
    private void openAdminUI() {
        try {
            /** CandidatController */
            //Parent root = FXMLLoader.load(getClass().getResource("ui/GeneralUI.fxml"));
            FXMLLoader loaderCandidat = new FXMLLoader();
            loaderCandidat.setLocation(getClass().getClassLoader().getResource("sample/ui/adminUI/CandidatUI.fxml"));
            Parent rootCandidat = loaderCandidat.load();
            CandidatController candidatController = loaderCandidat.getController();

            /** SecțieController */
            FXMLLoader loaderSecție = new FXMLLoader();
            loaderSecție.setLocation(getClass().getClassLoader().getResource("sample/ui/adminUI/SecțieUI.fxml"));
            Parent rootSecție = loaderSecție.load();
            SecțieController secțieController = loaderSecție.getController();

            /** OpțiuneController_Candidat */
            FXMLLoader loaderOpțiuneCandidat = new FXMLLoader();
            loaderOpțiuneCandidat.setLocation(getClass().getClassLoader().getResource("sample/ui/adminUI/OpțiuneUI_Candidat.fxml"));
            Parent rootOpțiune = loaderOpțiuneCandidat.load();
            OpțiuneController_Candidat opțiuneControllerCandidat = loaderOpțiuneCandidat.getController();

            /** OpțiuneController_Secție */
            FXMLLoader loaderOpțiuneSecție = new FXMLLoader();
            loaderOpțiuneSecție.setLocation(getClass().getClassLoader().getResource("sample/ui/adminUI/OpțiuneUI_Secție.fxml"));
            Parent rootOpțiuneSecție = loaderOpțiuneSecție.load();
            OpțiuneController_Secție opțiuneControllerSecție = loaderOpțiuneSecție.getController();

            /** GeneralController */
            FXMLLoader loaderAdmin = new FXMLLoader();
            loaderAdmin.setLocation(getClass().getClassLoader().getResource("sample/ui/adminUI/GeneralUI.fxml"));
            Parent rootAdmin = loaderAdmin.load();
            GeneralController generalController = loaderAdmin.getController();


            /** Service */
            CandidatService candidatService = generalService.getCandidatService();
            SecțieService secțieService = generalService.getSecțieService();
            OpțiuneService opțiuneService = generalService.getOpțiuneService();
            // simulare Observer
            candidatService.addOpțiuneControllerCandidat(opțiuneControllerCandidat);
            candidatService.addOpțiuneControllerSecție(opțiuneControllerSecție);
            secțieService.addOpțiuneControllerCandidat(opțiuneControllerCandidat);
            secțieService.addOpțiuneControllerSecție(opțiuneControllerSecție);
            opțiuneService.addOpțiuneControllerSecție(opțiuneControllerSecție);

            /** setarea controller-elor și a service-urilor */
            // CandidatController
            candidatController.setGeneralService(generalService);
            generalController.setCandidatController(candidatController);
            generalController.setInTabCandidați(candidatController.getMainBorderPane());
            // SecțieController
            secțieController.setGeneralService(generalService);
            generalController.setSecțieController(secțieController);
            generalController.setInTabSecții(secțieController.getMainBorderPane());
            // OpțiuneController_Candidat
            opțiuneControllerCandidat.setOpțiuneService(opțiuneService);
            opțiuneControllerCandidat.setGeneralService(generalService);
            generalController.setOpțiuneControllerCandidat(opțiuneControllerCandidat);
            generalController.setInTabOpțiuni_Candidat(opțiuneControllerCandidat.getMainBorderPane());
            // OpțiuneController_Secție
            opțiuneControllerSecție.setOpțiuneService(opțiuneService);
            opțiuneControllerSecție.setGeneralService(generalService);
            generalController.setOpțiuneControllerSecție(opțiuneControllerSecție);
            generalController.setInTabOpțiuni_Secție(opțiuneControllerSecție.getMainBorderPane());
            // GeneralController
            generalController.setGeneralService(generalService);

            Stage stage = new Stage();
            stage.setTitle("Administrator");
            stage.setScene(new Scene(rootAdmin));
            stage.show();

            candidatController.modificăriFereastră();
            secțieController.modificăriFereastră();
            generalController.modificăriFereastră();
        }
        catch (Exception e) {
            afișareMesajSmart(labelEroriLogin, e.getMessage(), false);
        }
    }

    /**
     * S-a introdus CNP-ul și parola unui candidat.
     * Se deschide fereastra candidatului.
     */
    private void openUserUI(Candidat candidat) {
        try {
            /** USER UI */
            FXMLLoader loaderUser = new FXMLLoader();
            loaderUser.setLocation(getClass().getClassLoader().getResource("sample/ui/userUI/UserUI.fxml"));
            Parent rootUser = loaderUser.load();
            UserController userController = loaderUser.getController();
            userController.setGeneralService(generalService);
            userController.setCandidat(candidat);

            Stage stage = new Stage();
            stage.setTitle(candidat.getNume() + " " + candidat.getPrenume());
            stage.setScene(new Scene(rootUser));
            stage.show();

            userController.modificăriFereastră();
        }
        catch (Exception e) {
            afișareMesajSmart(labelEroriLogin, e.getMessage(), false);
        }
    }

    @FXML
    public void goToCreeazăContNou(MouseEvent mouseEvent) {
        goToCreeazăContNou();
    }
    public void goToCreeazăContNou() {
        borderPaneLogin.setVisible(false);
        borderPaneCreareCont.setVisible(true);
    }

    @FXML
    public void evidențiazăButonLogin(MouseEvent mouseEvent) {
        butonLogin.setStyle("-fx-background-color: rgb(0, 59, 119);");
    }
    @FXML
    public void reseteazăButonLogin(MouseEvent mouseEvent) {
        butonLogin.setStyle("-fx-background-color: rgb(0, 34, 68);");
    }

    @FXML
    public void evidențiazăLabelCCN(MouseEvent mouseEvent) {
        labelCreeazăContNou.setTextFill(Color.web("#FFFFFF"));
    }
    @FXML
    public void resetLabelCCN(MouseEvent mouseEvent) {
        labelCreeazăContNou.setTextFill(Color.web("#323232"));
    }



    /** fereastra creării unui cont nou */

    /**
     * Utilizatorul a dat click pe butonul CreeazăCont sau a tastat Enter.
     */
    @FXML
    public void tryCreeazăCont(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.ENTER))
            creeazăCont();
    }
    @FXML
    public void creeazăCont(MouseEvent mouseEvent) {
        creeazăCont();
    }
    private void creeazăCont() {
        String cnp = câmpCC_CNP.getText();
        String nume = câmpCC_Nume.getText();
        String prenume = câmpCC_Prenume.getText();
        String telefon = câmpCC_Telefon.getText();
        String email = câmpCC_Email.getText();

        String parolă = câmpCC_Parolă.getText();
        String confirmareParolă = câmpCC_ConfirmareParolă.getText();

        try {
            generalService.adaugăCont(new Candidat(cnp, nume, prenume, telefon, email), parolă, confirmareParolă);
            openUserUI(generalService.getCandidat(cnp));
        }
        catch (Exception e) {
            afișareMesajSmart(labelEroriCreareCont, e.getMessage(), false);
        }
    }

    @FXML
    public void goToLogin(MouseEvent mouseEvent) {
        goToLogin();
    }
    public void goToLogin() {
        borderPaneLogin.setVisible(true);
        borderPaneCreareCont.setVisible(false);
    }

    @FXML
    public void evidențiazăButonCreeazăCont(MouseEvent mouseEvent) {
        butonCreeazăCont.setStyle("-fx-background-color: rgb(0, 59, 119);");
    }
    @FXML
    public void reseteazăButonCreeazăCont(MouseEvent mouseEvent) {
        butonCreeazăCont.setStyle("-fx-background-color: rgb(0, 34, 68);");
    }

    @FXML
    public void evidențiazăLabelÎLL(MouseEvent mouseEvent) {
        labelÎnapoiLaLogin.setTextFill(Color.web("#FFFFFF"));
    }
    @FXML
    public void resetLabelÎLL(MouseEvent mouseEvent) {
        labelÎnapoiLaLogin.setTextFill(Color.web("#323232"));
    }




    private void afișareMesajSmart(Label label, String mesaj, boolean succes) {
        if (afișezMesajLogin && label == labelEroriLogin)
            return;
        if (afișezMesajCC && label == labelEroriCreareCont)
            return;

        if (succes)
            label.setTextFill(Color.web("#11ff00"));
        else
            label.setTextFill(Color.web("#ff0000"));

        label.setText(mesaj);

        Thread thread = new Thread() {
            public void run() {
                if (label == labelEroriLogin)
                    afișezMesajLogin = true;
                if (label == labelEroriCreareCont)
                    afișezMesajCC = true;

                try {
                    label.setOpacity(1);
                    Thread.sleep(2000);

                    for (Double opacity = 1.0; opacity >=0; opacity = opacity - 0.01) {
                        label.setOpacity(opacity);
                        Thread.sleep(10);
                    }

                    if (label == labelEroriLogin)
                        afișezMesajLogin = false;
                    if (label == labelEroriCreareCont)
                        afișezMesajCC = false;

                } catch(InterruptedException e) {
                    System.out.println(e);
                }
            }
        };
        thread.start();
    }


    private void afișareMesajEroare(String eroare) {
        Alert mesaj = new Alert(Alert.AlertType.ERROR);
        mesaj.setTitle("Eroare");
        mesaj.setContentText(eroare);
        mesaj.showAndWait();
    }

}
