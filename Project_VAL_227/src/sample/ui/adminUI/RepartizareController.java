package sample.ui.adminUI;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import sample.domain.Candidat;
import sample.domain.FormăDeFinanțare;
import sample.domain.Secție;
import sample.service.GeneralService;

import java.io.IOException;
import java.util.Objects;
import java.util.Vector;

public class RepartizareController {
    private GeneralService generalService;
    private ObservableList<Secție> secții;
    private ObservableList<Secție> secțiiGăsite;
    private  ObservableList<Candidat> candidați;
    private Secție secțieSelectată;

    @FXML public TextField câmpSearchSecție;
    @FXML public ImageView lupaSearchSecție;
    @FXML public ImageView x_searchSecție;
    @FXML public TableView<Secție> tabelSecții;
    @FXML public TableColumn<Secție, String> tabelSecții_ID;
    @FXML public TableColumn<Secție, String> tabelSecții_Nume;
    @FXML public TableColumn<Secție, String> tabelSecții_LimbaDePredare;
    @FXML public TableColumn<Secție, FormăDeFinanțare> tabelSecții_FormaDeFinanțare;
    @FXML public TableColumn<Secție, Integer> tabelSecții_NrLocuri;
    @FXML public BorderPane borderPaneCandidați;
    @FXML public Label labelNumeSecție;
    @FXML public TableView<Candidat> tabelCandidați;
    @FXML public TableColumn<Candidat, Integer> tabelCandidați_Nr;
    @FXML public TableColumn<Candidat, String> tabelCandidați_CNP;
    @FXML public TableColumn<Candidat, String> tabelCandidați_Nume;
    @FXML public TableColumn<Candidat, String> tabelCandidați_Prenume;



    public RepartizareController() {
        generalService = new GeneralService("fișier");
    }

    public void setGeneralService(GeneralService generalService) {
        this.generalService = generalService;
        reloadSecții();
        generalService.repartizeazăCadidați();
    }

    @FXML
    private void initialize() {
        /* SECȚII */
        secții = FXCollections.observableArrayList();
        secțiiGăsite = FXCollections.observableArrayList();
        // tabel
        tabelSecții_ID.setCellValueFactory(new PropertyValueFactory<Secție, String>("ID"));
        tabelSecții_Nume.setCellValueFactory(new PropertyValueFactory<Secție, String>("Nume"));
        tabelSecții_LimbaDePredare.setCellValueFactory(new PropertyValueFactory<Secție, String>("LimbaDePredare"));
        tabelSecții_FormaDeFinanțare.setCellValueFactory(new PropertyValueFactory<Secție, FormăDeFinanțare>("FormăDeFinanțare"));
        tabelSecții_NrLocuri.setCellValueFactory(new PropertyValueFactory<Secție, Integer>("NrLocuri"));

        secții = getSecții();
        tabelSecții.setItems(secții);

        // lista de search
        x_searchSecție.setOpacity(0);
        secțiiGăsite = getSecțiiCăutate();


        /* Candidați */
        candidați = FXCollections.observableArrayList();

        tabelCandidați_Nr.setCellValueFactory(column-> new ReadOnlyObjectWrapper<Integer>(tabelCandidați.getItems().indexOf(column.getValue()) + 1));
        tabelCandidați_CNP.setCellValueFactory(new PropertyValueFactory<Candidat, String>("ID"));
        tabelCandidați_Nume.setCellValueFactory(new PropertyValueFactory<Candidat, String>("Nume"));
        tabelCandidați_Prenume.setCellValueFactory(new PropertyValueFactory<Candidat,String>("Prenume"));
    }


    private void reloadSecții() {
        Vector<Secție> candidatVector = (Vector<Secție>) generalService.getSecțieService().findAll();
        secții.clear();
        candidatVector.forEach(candidat -> secții.add(candidat));
    }

    public ObservableList<Secție> getSecții() {
        return secții;
    }

    private ObservableList<Secție> getSecțiiCăutate() {
        return secțiiGăsite;
    }



    @FXML
    public void cautăSecție(KeyEvent inputMethodEvent) {
        cautăSecție();
    }
    public void cautăSecție() {
        secțiiGăsite.clear();
        lupaSearchSecție.setOpacity(0);
        x_searchSecție.setOpacity(0.5);

        String idSecție = câmpSearchSecție.getText();
        if (idSecție.length() == 0) {
            închideSearchSecție();
            return;
        }

        Vector<Secție> secțieVector = new Vector<>(secții);
        secțieVector = generalService.getSecțieService().filtrare_id(idSecție, secțieVector);

        // setarea rezultatului în tabel
        secțiiGăsite.addAll(secțieVector);
        tabelSecții.setItems(secțiiGăsite);
        lupaSearchSecție.setOpacity(0);
    }

    @FXML
    private void ascundeListaSearchSecție() {
        secțiiGăsite.clear();
        lupaSearchSecție.setOpacity(1);
    }

    @FXML
    public void ascundeLupaSecție(MouseEvent mouseEvent) {
        lupaSearchSecție.setOpacity(0);
    }

    @FXML
    public void aratăLupaSecție(MouseEvent mouseEvent) {
        if (Objects.equals(câmpSearchSecție.getText(), ""))
            lupaSearchSecție.setOpacity(1);
    }

    @FXML
    public void setOpacityXsecție_100(MouseEvent mouseEvent) {
        if (!Objects.equals(câmpSearchSecție.getText(), ""))
            x_searchSecție.setOpacity(1);
    }

    @FXML
    public void setOpacityXsecție_50(MouseEvent mouseEvent) {
        if (!Objects.equals(câmpSearchSecție.getText(), ""))
            x_searchSecție.setOpacity(0.5);
    }

    @FXML
    public void închideSearchSecție() {
        câmpSearchSecție.setText("");
        ascundeListaSearchSecție();
        tabelSecții.setItems(secții);
        x_searchSecție.setOpacity(0);
    }

    @FXML
    public void handlerTabelSecții(MouseEvent mouseEvent) {
        Secție secție = tabelSecții.getSelectionModel().getSelectedItem();
        if (secție == null)
            return;

        secțieSelectată = secție;
        labelNumeSecție.setText(secție.getNume() + " " + secție.getLimbaDePredare() + " [" + secție.getFormăDeFinanțare() + "]");
        repartizeazăCandidați(secție);
    }

    private void repartizeazăCandidați(Secție secție) {
        Vector<Candidat> candidatVector = generalService.getRepartiție(secție);
        candidați.clear();
        candidați.addAll(candidatVector);
        tabelCandidați.setItems(candidați);
    }

    @FXML
    public void refresh(MouseEvent mouseEvent) {
        reloadSecții();
        cautăSecție();
        generalService.repartizeazăCadidați();

        if (secțieSelectată != null)
            repartizeazăCandidați(secțieSelectată);
    }

    @FXML
    public void printează(ActionEvent actionEvent) {
        try {
            FXMLLoader loaderPrintare = new FXMLLoader();
            loaderPrintare.setLocation(getClass().getClassLoader().getResource("sample/ui/adminUI/PrintareGenerală.fxml"));
            Parent rootPrintare = loaderPrintare.load();
            PrintareGeneralăController printareGeneralăController = loaderPrintare.getController();
            printareGeneralăController.setPrintBorderPane(borderPaneCandidați, "Repartiția candidaților la secția " + labelNumeSecție.getText());

            Stage stage = new Stage();
            stage.setTitle("Repartiția candidaților la secția " + labelNumeSecție.getText());
            stage.setScene(new Scene(rootPrintare));
            stage.show();
        }
        catch (IOException e) {
            afișareMesaj(e.getMessage(), false);
        }
    }

    private void afișareMesaj(String mesaj, Boolean succes) {
        Alert text;
        if (succes){
            text = new Alert(Alert.AlertType.INFORMATION);
            text.setTitle("Succes");
        }
        else {
            text = new Alert(Alert.AlertType.ERROR);
            text.setTitle("Eroare");
        }

        text.setContentText(mesaj);
        text.showAndWait();
    }
}
