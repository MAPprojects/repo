package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.service.*;
import sample.ui.loginWindow.LoginWindowController;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
//        /** CandidatController */
//        //Parent root = FXMLLoader.load(getClass().getResource("ui/GeneralUI.fxml"));
//        FXMLLoader loaderCandidat = new FXMLLoader();
//        loaderCandidat.setLocation(Main.class.getResource("ui/adminUI/CandidatUI.fxml"));
//        Parent rootCandidat = loaderCandidat.load();
//        CandidatController candidatController = loaderCandidat.getController();
//
//        /** SecțieController */
//        FXMLLoader loaderSecție = new FXMLLoader();
//        loaderSecție.setLocation(Main.class.getResource("ui/adminUI/SecțieUI.fxml"));
//        Parent rootSecție = loaderSecție.load();
//        SecțieController secțieController = loaderSecție.getController();
//
//        /** OpțiuneController_Candidat */
//        FXMLLoader loaderOpțiuneCandidat = new FXMLLoader();
//        loaderOpțiuneCandidat.setLocation(Main.class.getResource("ui/adminUI/OpțiuneUI_Candidat.fxml"));
//        Parent rootOpțiune = loaderOpțiuneCandidat.load();
//        OpțiuneController_Candidat opțiuneControllerCandidat = loaderOpțiuneCandidat.getController();
//
//        /** OpțiuneController_Secție */
//        FXMLLoader loaderOpțiuneSecție = new FXMLLoader();
//        loaderOpțiuneSecție.setLocation(Main.class.getResource("ui/adminUI/OpțiuneUI_Secție.fxml"));
//        Parent rootOpțiuneSecție = loaderOpțiuneSecție.load();
//        OpțiuneController_Secție opțiuneControllerSecție = loaderOpțiuneSecție.getController();
//
//        /** GeneralController */
//        FXMLLoader loaderAdmin = new FXMLLoader();
//        loaderAdmin.setLocation(Main.class.getResource("ui/adminUI/GeneralUI.fxml"));
//        Parent rootAdmin = loaderAdmin.load();
//        GeneralController generalController = loaderAdmin.getController();
//
//
        /** Service */
        CandidatService candidatService = new CandidatService("candidați.txt", "candidați.xml",  "./src/sample/data/");
        SecțieService secțieService = new SecțieService("secții.txt", "secții.xml", "./src/sample/data/");
        OpțiuneService opțiuneService = new OpțiuneService("opțiuni.txt", "opțiuni.xml", "./src/sample/data/", "log_Opțiuni.txt");
        ContService contService = new ContService("conturi.txt", "conturi.xml", "./src/sample/data/");
        GeneralService generalService = new GeneralService(candidatService, secțieService, opțiuneService, contService);
//        // simulare Observer
//        candidatService.addOpțiuneControllerCandidat(opțiuneControllerCandidat);
//        candidatService.addOpțiuneControllerSecție(opțiuneControllerSecție);
//        secțieService.addOpțiuneControllerCandidat(opțiuneControllerCandidat);
//        secțieService.addOpțiuneControllerSecție(opțiuneControllerSecție);
//        opțiuneService.addOpțiuneControllerSecție(opțiuneControllerSecție);
//
//        /** setarea controller-elor și a service-urilor */
//        // CandidatController
//        candidatController.setCandidatService(candidatService);
//        generalController.setCandidatController(candidatController);
//        generalController.setInTabCandidați(candidatController.getMainBorderPane());
//        // SecțieController
//        secțieController.setSecțieService(secțieService);
//        generalController.setSecțieController(secțieController);
//        generalController.setInTabSecții(secțieController.getMainBorderPane());
//        // OpțiuneController_Candidat
//        opțiuneControllerCandidat.setOpțiuneService(opțiuneService);
//        opțiuneControllerCandidat.setGeneralService(generalService);
//        generalController.setOpțiuneControllerCandidat(opțiuneControllerCandidat);
//        generalController.setInTabOpțiuni_Candidat(opțiuneControllerCandidat.getMainBorderPane());
//        // OpțiuneController_Secție
//        opțiuneControllerSecție.setOpțiuneService(opțiuneService);
//        opțiuneControllerSecție.setGeneralService(generalService);
//        generalController.setOpțiuneControllerSecție(opțiuneControllerSecție);
//        generalController.setInTabOpțiuni_Secție(opțiuneControllerSecție.getMainBorderPane());
//        // GeneralController
//        generalController.setGeneralService(generalService);
//
//
//
//
//        /** USER UI */
//        FXMLLoader loaderUser = new FXMLLoader();
//        loaderUser.setLocation(Main.class.getResource("ui/userUI/UserUI.fxml"));
//        Parent rootUser = loaderUser.load();
//        UserController userController = loaderUser.getController();
//        userController.setGeneralService(generalService);
//        userController.setCandidat(new Candidat("1", "Vaida", "Andrei", "123", "a@yaho.com"));


        /** Login window */
        FXMLLoader loaderLoginWindow = new FXMLLoader();
        loaderLoginWindow.setLocation(Main.class.getResource("ui/loginWindow/LoginWindow.fxml"));
        Parent rootLoginWindow = loaderLoginWindow.load();
        LoginWindowController loginWindowController = loaderLoginWindow.getController();
        loginWindowController.setGeneralService(generalService);
        loginWindowController.setStage(primaryStage);
//        loginWindowController.setLoaderAdminUI(loaderAdmin);
//        loginWindowController.setLoaderUserUI(loaderUser);


        /** lansarea aplicației */
        primaryStage.setTitle("Login | Facultatea de Matematică și Informatică");
        Scene scene = new Scene(rootLoginWindow);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
