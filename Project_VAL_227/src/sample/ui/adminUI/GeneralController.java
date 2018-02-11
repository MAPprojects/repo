package sample.ui.adminUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import sample.service.CandidatService;
import sample.service.GeneralService;
import sample.service.OpțiuneService;
import sample.service.SecțieService;

import java.io.IOException;


public class GeneralController {
    /**** VARIABIBLE ****/
    private CandidatService candidatService;
    private SecțieService secțieService;
    private OpțiuneService opțiuneService;
    private GeneralService generalService;
    private CandidatController candidatController;
    private SecțieController secțieController;
    private OpțiuneController_Candidat opțiuneControllerCandidat;
    private OpțiuneController_Secție opțiuneControllerSecție;
    @FXML
    public TabPane tabPane;
    @FXML
    public TabPane tabPaneOpțiune;
    @FXML
    public Tab tabCandidați;
    @FXML
    public Tab tabSecții;
    @FXML
    public Tab tabOpțiuni_Candidat;
    @FXML
    public Tab tabOpțiuni_Secție;

    Stage stage;

    /**** METODE ****/
    public GeneralController() {
        this.candidatService = new CandidatService("candidați.txt", "candidați.xml", "./src/sample/data/");
        this.secțieService = new SecțieService("secții.txt", "secții.xml", "./src/sample/data/");
        this.opțiuneService = new OpțiuneService("opțiuni.txt", "opțiuni.xml", "./src/sample/data/", "log_Opțiuni.txt");
        this.generalService = new GeneralService(candidatService, secțieService, opțiuneService);
        // simulare Observer
        candidatService.addOpțiuneControllerCandidat(opțiuneControllerCandidat);
        secțieService.addOpțiuneControllerCandidat(opțiuneControllerCandidat);

        this.candidatController = new CandidatController(candidatService);
        this.secțieController = new SecțieController(secțieService);
        this.opțiuneControllerCandidat = new OpțiuneController_Candidat(generalService);
    }


    public GeneralController(CandidatController candidatController, SecțieController secțieController, OpțiuneController_Candidat opțiuneControllerCandidat) {
        this.candidatController = candidatController;
        this.secțieController = secțieController;
        this.opțiuneControllerCandidat = opțiuneControllerCandidat;
    }

    public void setCandidatService(CandidatService candidatService) {
        this.candidatService = candidatService;
        candidatService.addObserver(candidatController);
        //candidatService.addOpțiuneControllerCandidat(opțiuneControllerCandidat);
        //candidatService.addOpțiuneControllerSecție(opțiuneControllerSecție);
    }
    public void setSecțieService(SecțieService secțieService) {
        this.secțieService = secțieService;
        secțieService.addObserver(secțieController);
        //secțieService.addOpțiuneControllerCandidat(opțiuneControllerCandidat);
        //secțieService.addOpțiuneControllerSecție(opțiuneControllerSecție);
    }
    public void setOpțiuneService(OpțiuneService opțiuneService) {
        this.opțiuneService = opțiuneService;
        opțiuneService.addObserver(opțiuneControllerCandidat);
        opțiuneService.addObserver(opțiuneControllerSecție);
        //opțiuneService.addOpțiuneControllerSecție(opțiuneControllerSecție);
    }
    public void setGeneralService(GeneralService generalService) {
        this.generalService = generalService;
        setCandidatService(generalService.getCandidatService());
        setSecțieService(generalService.getSecțieService());
        setOpțiuneService(generalService.getOpțiuneService());
    }

    @FXML
    private void initialize() {
    }


    public void setCandidatController(CandidatController candidatController) {
        this.candidatController = candidatController;
    }
    public void setSecțieController(SecțieController secțieController) {
        this.secțieController = secțieController;
    }
    public void setOpțiuneControllerCandidat(OpțiuneController_Candidat opțiuneControllerCandidat) {
        this.opțiuneControllerCandidat = opțiuneControllerCandidat;
    }
    public void setOpțiuneControllerSecție(OpțiuneController_Secție opțiuneControllerSecție) {
        this.opțiuneControllerSecție = opțiuneControllerSecție;
    }

    /**
     * A se apela această funcție după afișarea ferestrei.
     */
    public void modificăriFereastră() {
        /* redimensionarea fereastrei */
        stage = (Stage) tabPane.getScene().getWindow();
        stage.setMinHeight(500);
    }


    private void afișareMesaj(String mesaj, Boolean succes) {
        Alert messageBox;
        if (succes){
            messageBox = new Alert(Alert.AlertType.INFORMATION);
            messageBox.setTitle("Bine ai venit !");
        }
        else {
            messageBox = new Alert(Alert.AlertType.ERROR);
            messageBox.setTitle("Eroare");
        }

        messageBox.setContentText(mesaj);
        messageBox.setResizable(true);
        messageBox.getDialogPane().setMinWidth(900);
        messageBox.showAndWait();
    }

    public void setInTabCandidați(BorderPane borderPane) {
        this.tabCandidați.setContent(borderPane);
    }
    public void setInTabSecții(BorderPane borderPane) {
        this.tabSecții.setContent(borderPane);
    }
    public void setInTabOpțiuni_Candidat(BorderPane borderPane) {
        this.tabOpțiuni_Candidat.setContent(borderPane);
    }
    public void setInTabOpțiuni_Secție(BorderPane borderPane) {
        this.tabOpțiuni_Secție.setContent(borderPane);
    }

    public void creareFereastră(MouseEvent mouseEvent) {
        Tab tabCurent = tabPane.getSelectionModel().getSelectedItem();
        if (tabCurent == tabCandidați)
            creareFereastrăCandidat();

        if (tabCurent == tabSecții)
            creareFereastrăSecție();
    }
    public void creareFereastrăOpțiune(MouseEvent mouseEvent) {
        Tab tabCurent = tabPaneOpțiune.getSelectionModel().getSelectedItem();
        if (tabCurent == tabOpțiuni_Candidat)
            creareFereastrăOpțiune_Candidat();

        if (tabCurent == tabOpțiuni_Secție)
            creareFereastrăOpțiune_Secție();
    }

    private void creareFereastrăCandidat() {
        try {
            FXMLLoader loaderCandidat = new FXMLLoader();
            loaderCandidat.setLocation(getClass().getClassLoader().getResource("sample/ui/adminUI/CandidatUI.fxml"));
            Parent rootCandidat = loaderCandidat.load();
            CandidatController candidatController = loaderCandidat.getController();
            candidatController.setGeneralService(this.generalService);

            Stage stage = new Stage();
            stage.setTitle("Candidați");
            stage.setScene(new Scene(rootCandidat));
            stage.show();
            candidatController.modificăriFereastră();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void creareFereastrăSecție() {
        try {
            FXMLLoader loaderSecție = new FXMLLoader();
            loaderSecție.setLocation(getClass().getClassLoader().getResource("sample/ui/adminUI/SecțieUI.fxml"));
            Parent rootSecție = loaderSecție.load();
            SecțieController secțieController = loaderSecție.getController();
            secțieController.setGeneralService(this.generalService);

            Stage stage = new Stage();
            stage.setTitle("Secții");
            stage.setScene(new Scene(rootSecție));
            stage.show();

            secțieController.modificăriFereastră();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void creareFereastrăOpțiune_Candidat() {
        try {
            FXMLLoader loaderOpțiune_Candidat = new FXMLLoader();
            loaderOpțiune_Candidat.setLocation(getClass().getClassLoader().getResource("sample/ui/adminUI/OpțiuneUI_Candidat.fxml"));
            Parent rootOpțiune_Candidat = loaderOpțiune_Candidat.load();
            OpțiuneController_Candidat opțiuneController_Candidat = loaderOpțiune_Candidat.getController();
            opțiuneController_Candidat.setOpțiuneService(this.opțiuneService);
            opțiuneController_Candidat.setGeneralService(this.generalService);
            // simulare Observer
            candidatService.addOpțiuneControllerCandidat(opțiuneController_Candidat);
            secțieService.addOpțiuneControllerCandidat(opțiuneController_Candidat);

            Stage stage = new Stage();
            stage.setTitle("Opțiuni candidați");
            stage.setScene(new Scene(rootOpțiune_Candidat));
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void creareFereastrăOpțiune_Secție() {
        try {
            FXMLLoader loaderOpțiune_Secție = new FXMLLoader();
            loaderOpțiune_Secție.setLocation(getClass().getClassLoader().getResource("sample/ui/adminUI/OpțiuneUI_Secție.fxml"));
            Parent rootOpțiune_Secție = loaderOpțiune_Secție.load();
            OpțiuneController_Secție opțiuneController_Secție = loaderOpțiune_Secție.getController();
            opțiuneController_Secție.setOpțiuneService(this.opțiuneService);
            opțiuneController_Secție.setGeneralService(this.generalService);
            // simulare Observer
            candidatService.addOpțiuneControllerSecție(opțiuneController_Secție);
            secțieService.addOpțiuneControllerSecție(opțiuneController_Secție);
            opțiuneService.addOpțiuneControllerSecție(opțiuneController_Secție);

            Stage stage = new Stage();
            stage.setTitle("Rapoarte secții");
            stage.setScene(new Scene(rootOpțiune_Secție));
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openWindow_repartizareCandidați(ActionEvent actionEvent) {
        try {
            FXMLLoader loaderRepartizare = new FXMLLoader();
            loaderRepartizare.setLocation(getClass().getClassLoader().getResource("sample/ui/adminUI/RepartizareUI.fxml"));
            Parent rootRepartizare = loaderRepartizare.load();
            RepartizareController repartizareController = loaderRepartizare.getController();
            repartizareController.setGeneralService(this.generalService);

            Stage stage = new Stage();
            stage.setTitle("Repartizarea candidaților");
            stage.setScene(new Scene(rootRepartizare));
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void close(ActionEvent actionEvent) {
        Stage fereastra = (Stage) tabPane.getScene().getWindow();
        fereastra.close();
    }

    public void help(ActionEvent actionEvent) {
        afișareHelp();
    }
    private void afișareHelp() {
        String mesaj = "Adăugarea/modificarea/ștergerea candidaților:\n"
                + "În tabul „Candidați” vei găsi\n"
                + "     - pe partea stângă: tabelul tuturor candidaților\n"
                + "     - pe partea dreaptă: filtrele de căutare ale candidaților și câmpurile de completare ale datelor fiecărui candidat, cu butoanele aferente\n"
                + "\n------------------------------\n"
                + "Adăugarea/modificarea/ștergerea secților:\n"
                + "În tabul „Secții” vei găsi \n"
                + "     - pe partea stângă: tabelul tuturor secților\n"
                + "     - pe partea dreaptă: filtrele de căutare ale secților și câmpurile de completare ale caracteristicilor fiecărei secții, cu butoanele aferente\n"
                + "ID-ul unei secții se generează automat astfel:\n"
                + "prima literă din fiecare cuvânt din nume  +  litera cu care începe limba de predare  +  _  +  prima literă a formei de finațare\n"
                + "La adăugarea unei noi secții, dacă ID-ul generat automat pentru o secție nouă este identic cu ID-ul unei secții existente, se poate dezactiva opțiunea AutoID și să se introducă un ID unic pentrus ecția nouă.\n"
                + "\n------------------------------\n"
                + "Înscrierea/modificarea/eliminarea unui candidat de la o secție:\n"
                + "În tabul „Opțiuni” la „Înscriere la secții” vei găsi\n"
                + "Pe partea stângă:\n"
                + "     - sus: tabelul cu candidatul ales\n"
                + "     - jos: tabelul cu secțiile alese de candidatul ales\n"
                + "Pe partea dreaptă:\n"
                + "     - sus: filtrele de căutare ale candidaților și ale secților\n"
                + "     - jos: câmpurile de completare automată ale caracteristicilor secției selectate, cu butoanele aferente\n"
                + "Instrucțiuni pas cu pas:\n"
                + "1. Caută candidatul în câmpul de search (după CNP sau după nume candidat). Datele candidatului vor fi completate în stânga sus. În tabelul din stânga jos vor apărea secțiile la care este înscris în ordinea priorității.\n"
                + "2. a) Înscrierea la o secție nouă: Caută secția în câmpul de search (după ID sau nume secție). Dând clic pe secția găsită, aceasta va fi automat adăugată în lista de opțiuni a candidatului pe ultima poziție (ultima prioritate).\n"
                + "   b) Modificarea priorității unei secții: Dă click pe secția vizată din tabelul secțiilor alese de candidat. Prioritatea secției se poate modifica din ChoiceBox-ul din dreapta. Modificarea se face pe loc la schimbarea valorii din ChoiceBox.\n"
                + "   c) Eliminarea de la o secție: Dă click pe secția vizată din tabelul secțiilor alese de candidat apoi pe butonul „Șterge”.\n"
                + "\n------------------------------\n"
                + "Vizualizarea înscrierii candidaților la secții (vizualizarea rapoartelor):\n"
                + "În tabul „Opțiuni” la „Rapoarte” vei găsi\n"
                + "Pe partea stângă:\n"
                + "     - sus: tabelul cu toate secțile\n"
                + "     - jos: tabelul cu toți candidații care s-au înscris la secția selectată din tabelul de sus\n"
                + "Pe centru:\n"
                + "     - o diagramă cu numărul de optări pentru fiecare secție în funcție de prioritate\n"
                + "Pe partea dreaptă:\n"
                + "     - sus: filtrele de căutare ale celor mai dorite secții\n"
                + "     - jos: cele mai dorite secții afișate în funcție de filtrele alese\n"
                + "\n------------------------------\n"
                + "Generarea unui contract:\n"
                + "Mergi în tabul „Opțiuni” la „Înscriere la secții” și dă click pe butonul „Generare contract”. Poți salva contractul în PDF și/sau să i-l trimiți pe e-mail.";

        afișareMesaj(mesaj, true);
    }


}
