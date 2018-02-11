package sample.ui.userUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import sample.domain.Cont;
import sample.service.GeneralService;

public class ȘtergeContController {
    private GeneralService generalService;
    private String cnpCandidat;
    private Stage fereastraUser;

    @FXML public VBox vBoxGeneral;
    @FXML public VBox vBoxConfirmareȘtergere;
    @FXML public PasswordField câmpParolă;
    @FXML public Button butonContinuăEliminarea;



    public ȘtergeContController() {
        generalService = new GeneralService();
    }

    public ȘtergeContController(GeneralService generalService, String cnpCandidat) {
        this.generalService = generalService;
        this.cnpCandidat = cnpCandidat;
    }

    public void setGeneralService(GeneralService generalService) {
        this.generalService = generalService;
    }
    public void setCnpCandidatȘiFereastraUser(String cnpCandidat, Stage fereastraUser) {
        this.cnpCandidat = cnpCandidat;
        this.fereastraUser = fereastraUser;
    }

    @FXML
    private void initialize() {
        vBoxGeneral.getChildren().remove(vBoxConfirmareȘtergere);
        butonContinuăEliminarea.setDisable(true);
    }

    public void handlerCâmpParolă(KeyEvent keyEvent) {
        String parola = câmpParolă.getText();
        if (parola.length() > 0)
            butonContinuăEliminarea.setDisable(false);
        else
            butonContinuăEliminarea.setDisable(true);
    }

    @FXML
    public void continuăEliminarea(ActionEvent actionEvent) {
        try {
            Cont cont = generalService.getContService().findOne(cnpCandidat);
            String parola = câmpParolă.getText();
            if (cont.getParola().equals(parola))
                vBoxGeneral.getChildren().add(vBoxConfirmareȘtergere);
        } catch (Exception ignored) {}
    }

    public void handlerButonDa(ActionEvent actionEvent) {
        try {
            String parola = câmpParolă.getText();
            generalService.ștergeContSmart(cnpCandidat, parola);
            fereastraUser.close();
            închideFereastră();
            afișareMesaj("Ai fost eliminat de la toate secțiile.\n"
                    + "Contul tău a fost șters.", true);
        }
        catch (Exception e) {
            afișareMesaj(e.getMessage(), false);
        }

    }

    public void handlerButonNu(ActionEvent actionEvent) {
        închideFereastră();
    }
    private void închideFereastră() {
        Stage stage = (Stage) vBoxGeneral.getScene().getWindow();
        stage.close();
    }



    private void afișareMesaj(String mesaj, Boolean succes) {
        Alert text;
        if (succes){
            text = new Alert(Alert.AlertType.INFORMATION);
            text.setTitle("Contul a fost eliminat !");
        }
        else {
            text = new Alert(Alert.AlertType.ERROR);
            text.setTitle("Eroare");
        }

        text.setContentText(mesaj);
        text.showAndWait();
    }


}
