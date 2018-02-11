package sample.ui.userUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import sample.domain.Candidat;
import sample.service.GeneralService;

public class DatePersonaleController {
    private GeneralService generalService;
    private Candidat candidat;

    @FXML public VBox vBoxPrincipal;
    @FXML public TextField câmpCNP;
    @FXML public TextField câmpNume;
    @FXML public TextField câmpPrenume;
    @FXML public TextField câmpTelefon;
    @FXML public TextField câmpEmail;
    @FXML public Button butonModificăDatele;
    @FXML public Label labelMesajModificăDatele;
    @FXML public Label labelSchimbăParola;
    @FXML public VBox vboxSchimbareParolă;
    @FXML public PasswordField câmpParolaVeche;
    @FXML public PasswordField câmpParolaNouă;
    @FXML public PasswordField câmpConfirmareParolaNouă;
    @FXML public Button butonSchimbăParola;
    @FXML public Label labelMesajSchimbăParola;
    private boolean afișezMesajModificăDate;
    private boolean afișezMesajSchimbareParolă;




    public DatePersonaleController() {
        generalService = new GeneralService("fișier");
        candidat = new Candidat();
    }

    public DatePersonaleController(GeneralService generalService) {
        this.generalService = generalService;
        candidat = new Candidat();
    }

    public void setGeneralService(GeneralService generalService) {
        this.generalService = generalService;
    }

    public void setCandidat(Candidat candidat) {
        this.candidat = candidat;
        completareDatePersonale();
    }


    @FXML
    private void initialize() {
        vBoxPrincipal.getChildren().remove(vboxSchimbareParolă);
        completareDatePersonale();


        afișezMesajModificăDate = false;
        afișezMesajSchimbareParolă = false;
    }


    /** Schimbarea datelor personale */

    private void completareDatePersonale() {
        câmpCNP.setText(candidat.getID());
        câmpNume.setText(candidat.getNume());
        câmpPrenume.setText(candidat.getPrenume());
        câmpTelefon.setText(candidat.getTelefon());
        câmpEmail.setText(candidat.getE_mail());
    }

    @FXML
    public void handlerCâmpuriDate(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.ENTER))
            modificăCandidat();
        ///activeazăDezactiveazăButonModificăCandidat();
    }

    @FXML
    public void modificăDatele(ActionEvent actionEvent) {
        modificăCandidat();
    }
    private void modificăCandidat() {
        String nume = câmpNume.getText();
        String prenume = câmpPrenume.getText();
        String telefon = câmpTelefon.getText();
        String email = câmpEmail.getText();

        try {
            candidat = generalService.modificăCandidat(candidat.getID(), nume, prenume, telefon, email);
            afișareMesajSmart(labelMesajModificăDatele, "Datele au fost modificate.", true);
        }
        catch (Exception e) {
            afișareMesajSmart(labelMesajModificăDatele, e.getMessage(), false);
        }
    }

    @FXML
    public void evidențiazăButonModificăDatele(MouseEvent mouseEvent) {
        butonModificăDatele.setStyle("-fx-background-color: rgb(0, 59, 119);");
    }
    @FXML
    public void reseteazăButonModificăDatele(MouseEvent mouseEvent) {
        butonModificăDatele.setStyle("-fx-background-color: rgb(0, 34, 68);");
    }

    @FXML
    public void evidențiazăLabelSchimbăParola(MouseEvent mouseEvent) {
        labelSchimbăParola.setTextFill(Color.web("#FFFFFF"));
    }
    @FXML
    public void reseteazăLabelSchimbăParola(MouseEvent mouseEvent) {
        labelSchimbăParola.setTextFill(Color.web("#323232"));
    }

    @FXML
    public void aratăAscundeModificareParolă(MouseEvent mouseEvent) {
        if (vBoxPrincipal.getChildren().contains(vboxSchimbareParolă)) {
            vBoxPrincipal.getChildren().remove(vboxSchimbareParolă);
            labelSchimbăParola.setText("Schimbă parola");
        }
        else {
            vBoxPrincipal.getChildren().add(vboxSchimbareParolă);
            labelSchimbăParola.setText("Închide schimbarea parolei");
            câmpParolaVeche.setText("");
            câmpParolaNouă.setText("");
            câmpConfirmareParolaNouă.setText("");
        }
    }


    /** Schimbarea parolei */
    @FXML
    public void evidențiazăButonschimbăParola(MouseEvent mouseEvent) {
        butonSchimbăParola.setStyle("-fx-background-color: rgb(0, 59, 119);");
    }
    @FXML
    public void reseteazăButonschimbăParola(MouseEvent mouseEvent) {
        butonSchimbăParola.setStyle("-fx-background-color: rgb(0, 34, 68);");
    }

    @FXML
    public void schimbăParola(ActionEvent actionEvent) {
        schimbăParola();
    }
    @FXML
    public void handlerCâmpuriParolă(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.ENTER))
            schimbăParola();
    }
    private void schimbăParola() {
        String parolaVeche = câmpParolaVeche.getText();
        String parolăNouă = câmpParolaNouă.getText();
        String confirmareParolăNouă = câmpConfirmareParolaNouă.getText();
        try {
            generalService.modificăParolă(candidat.getID(), parolaVeche, parolăNouă, confirmareParolăNouă);
            afișareMesajSmart(labelMesajSchimbăParola, "Parola a fost schimbată.", true);
        }
        catch (Exception e) {
            afișareMesajSmart(labelMesajSchimbăParola, e.getMessage(), false);
        }
    }




    private void afișareMesajSmart(Label label, String mesaj, boolean succes) {
        if (afișezMesajModificăDate && label == labelMesajModificăDatele)
            return;
        if (afișezMesajSchimbareParolă && label == labelMesajSchimbăParola)
            return;

        if (succes)
            label.setTextFill(Color.web("#11ff00"));
        else
            label.setTextFill(Color.web("#ff0000"));

        label.setText(mesaj);

        Thread thread = new Thread() {
            public void run() {
                if (label == labelMesajModificăDatele)
                    afișezMesajModificăDate = true;
                if (label == labelMesajSchimbăParola)
                    afișezMesajSchimbareParolă = true;

                try {
                    label.setOpacity(1);
                    Thread.sleep(2000);

                    for (Double opacity = 1.0; opacity >=0; opacity = opacity - 0.01) {
                        label.setOpacity(opacity);
                        Thread.sleep(10);
                    }

                    if (label == labelMesajModificăDatele)
                        afișezMesajModificăDate = false;
                    if (label == labelMesajSchimbăParola)
                        afișezMesajSchimbareParolă = false;

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
