package sample.ui.adminUI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
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
import sample.service.CandidatService;
import sample.service.GeneralService;
import sample.service.OpțiuneService;
import sample.service.SecțieService;
import sample.utils.Observer;
import sample.utils.OpțiuneEvent;

import java.io.IOException;
import java.util.Objects;
import java.util.Vector;

public class OpțiuneController_Secție implements Observer<OpțiuneEvent> {
    private CandidatService candidatService;
    private SecțieService secțieService;
    private GeneralService generalService;
    private OpțiuneService opțiuneService;
    private ObservableList<Secție> toateSecțiile;
    private Integer nrSecții;

    // Toți candidații care au optat pentru secția X
    private ObservableList<Secție> secțiiGăsite_SecțiiAlese;
    private ObservableList<Candidat> candidați_SecțiiAlese;
    private Secție secțieAleasă;
    @FXML public BorderPane borderPaneCandidațiiDinSecțiaX;
    @FXML public TextField câmpSearch_secțiiOptate;
    @FXML public ImageView x_search_secțiiOptate;
    @FXML public ImageView lupaSearch_secțiiOptate;
    @FXML public TableView<Secție> tabelSelectare_secțiiOptate;
    @FXML public TableColumn<Secție,String> tabelSelectare_secțiiOptate_ID;
    @FXML public TableColumn<Secție,String> tabelSelectare_secțiiOptate_Nume;
    @FXML public TableColumn<Secție,String> tabelSelectare_secțiiOptate_limba;
    @FXML public TableColumn<Secție,FormăDeFinanțare> tabelSelectare_secțiiOptate_finanțare;
    @FXML public TableView<Candidat> tabelCandidați_secțiiOptate;
    @FXML public TableColumn<Candidat,String> tabelCandidațiID_SecțiiCăutate;
    @FXML public TableColumn<Candidat,String> tabelCandidațiNume_SecțiiCăutate;
    @FXML public TableColumn<Candidat,String> tabelCandidațiPrenume_SecțiiCăutate;
    @FXML public Label labelNumeSecțieAleasă;
    // Numărul de optări pentru fiecare secție, în funcție de prioritate
    @FXML public BorderPane borderpaneCeleMaiDoriteSecții;
    @FXML BarChart<String,Integer> chartSelecțieSecții;
    @FXML public CheckBox checkBoxDoarSecțiileOptate;
    // Cele mai dorite N secții
    ObservableList<Secție> celeMaiDoriteNsecții;
    @FXML public BorderPane borderPaneChart;
    @FXML public ChoiceBox choiceBox_NrSecții;
    @FXML public ChoiceBox choiceBox_Prioritate;
    @FXML public TableView<Secție> tabelCeleMaiDoriteNSecții;
    @FXML public TableColumn<Secție,String> tabelCeleMaiDoriteNSecții_ID;
    @FXML public TableColumn<Secție,String> tabelCeleMaiDoriteNSecții_Nume;
    @FXML public TableColumn<Secție,String> tabelCeleMaiDoriteNSecții_Limba;
    @FXML public TableColumn<Secție,FormăDeFinanțare> tabelCeleMaiDoriteNSecții_Finanțare;
    @FXML public TableColumn<Secție,Integer> tabelCeleMaiDoriteNSecții_NrOptări;

    @FXML
    public BorderPane mainBorderPane;


    public OpțiuneController_Secție() {
        this.generalService = new GeneralService();
        candidatService = generalService.getCandidatService();
        secțieService = generalService.getSecțieService();
        opțiuneService = generalService.getOpțiuneService();
        secțieAleasă = new Secție();
    }


    public BorderPane getMainBorderPane() {
        return mainBorderPane;
    }

    @FXML
    private void initialize() {
        x_search_secțiiOptate.setOpacity(0);
        toateSecțiile = FXCollections.observableArrayList();
        reloadSecții();
        /** Secții alese */
        // tabelul de căutare a secțiilor alese
        secțiiGăsite_SecțiiAlese = FXCollections.observableArrayList();
        tabelSelectare_secțiiOptate_ID.setCellValueFactory(new PropertyValueFactory<Secție,String>("ID"));
        tabelSelectare_secțiiOptate_Nume.setCellValueFactory(new PropertyValueFactory<Secție,String>("Nume"));
        tabelSelectare_secțiiOptate_limba.setCellValueFactory(new PropertyValueFactory<Secție,String>("LimbaDePredare"));
        tabelSelectare_secțiiOptate_finanțare.setCellValueFactory(new PropertyValueFactory<Secție,FormăDeFinanțare>("FormăDeFinanțare"));
        tabelSelectare_secțiiOptate.setItems(toateSecțiile);

        // tabelul candidaților ce au optat pentru secție
        candidați_SecțiiAlese = FXCollections.observableArrayList();
        tabelCandidațiID_SecțiiCăutate.setCellValueFactory(new PropertyValueFactory<Candidat, String>("ID"));
        tabelCandidațiNume_SecțiiCăutate.setCellValueFactory(new PropertyValueFactory<Candidat, String>("Nume"));
        tabelCandidațiPrenume_SecțiiCăutate.setCellValueFactory(new PropertyValueFactory<Candidat, String>("Prenume"));

        // chart-ul cu toate secțiile
        reloadChartSecții();

        // tabelul celor mai dorite N secții
        celeMaiDoriteNsecții = FXCollections.observableArrayList();
        tabelCeleMaiDoriteNSecții_ID.setCellValueFactory(new PropertyValueFactory<Secție, String>("ID"));
        tabelCeleMaiDoriteNSecții_Nume.setCellValueFactory(new PropertyValueFactory<Secție, String>("Nume"));
        tabelCeleMaiDoriteNSecții_Limba.setCellValueFactory(new PropertyValueFactory<Secție, String>("LimbaDePredare"));
        tabelCeleMaiDoriteNSecții_Finanțare.setCellValueFactory(new PropertyValueFactory<Secție,FormăDeFinanțare>("FormăDeFinanțare"));
        //tabelCeleMaiDoriteNSecții_NrOptări.setCellValueFactory(column-> new ReadOnlyObjectWrapper<Integer>(tabelCeleMaiDoriteNSecții.getItems().indexOf(column.getValue()) + 1));
        reloadNrSecții();
        loadCeleMaiDoriteNsecții(); // by default: cea mai dorită secție (orice prioritate)

        // setarea listenerilor
        seteazăListeneri();
    }

    private void seteazăListeneri() {
        // tabelul de căutare a secțiilor alese
        tabelSelectare_secțiiOptate.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    actualizeazăLabelSecție_SecțiiAlese(newValue);
                    actualizeazăSecțieAleasă(newValue);
                    reloadTabelCandidați_SecțiiAlese(newValue);
                }
        );

        // tabelul celor mai dorite N secții
        choiceBox_NrSecții.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> loadCeleMaiDoriteNsecții()
        );
        choiceBox_Prioritate.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> loadCeleMaiDoriteNsecții()
        );
    }

    private void reloadSecții() {
        Vector<Secție> secțieVector = (Vector<Secție>) secțieService.findAll();
        toateSecțiile.clear();
        toateSecțiile.addAll(secțieVector);
    }

    @Override
    public void notifyOnEvent(OpțiuneEvent event) {
        notifyOnEvent();
    }
    public void notifyOnEvent() {
        reloadSecții();
        // Raport: toți candidații care au optat pentru secție X
        cautăSecție_SecțiiAlese();
        //reloadCandidați_SecțiiAlese();
        reloadTabelCandidați_SecțiiAlese(secțieAleasă);

        // Raport: Numărul de optări pentru fiecare secție
        reloadChartSecții();

        // Raport: Cele mai dorite N secții
        reloadNrSecții();
    }

    public void setOpțiuneService(OpțiuneService opțiuneService) {
        this.opțiuneService = opțiuneService;
    }

    public void setGeneralService(GeneralService generalService) {
        this.generalService = generalService;
        candidatService = generalService.getCandidatService();
        secțieService = generalService.getSecțieService();
        opțiuneService = generalService.getOpțiuneService();
        reloadSecții();
        reloadChartSecții();
        reloadNrSecții();
    }

    /** Raport: toți candidații care au optat pentru secție X */

    private void actualizeazăLabelSecție_SecțiiAlese(Object newValue) {
        try {
            Secție secție = (Secție) newValue;
            labelNumeSecțieAleasă.setText(secție.getNume());
        } catch (Exception ignored) {}
    }

//    private void reloadCandidați_SecțiiAlese() {
//        try {
//            Candidat candidat = tabelCandidați_secțiiOptate.getSelectionModel().getSelectedItem();
//            reloadTabelCandidați_SecțiiAlese(candidat);
//        } catch (Exception ignored) {}
//    }

    private void actualizeazăSecțieAleasă(Object newValue) {
        Secție secție = (Secție) newValue;
        if (secție != null)
            secțieAleasă = secție;
    }

    /**
     * A fost selectată o secție din tabelul tabelSelectare_SecțiiOptate
     * Actualizăm tabelul cu candidații care au optat pentru această secție.
     * @param newValue - secția selectată
     */
    private void reloadTabelCandidați_SecțiiAlese(Object newValue) {
        Secție secție = (Secție) newValue;
        if (secție != null)
            reloadTabelCandidați_SecțiiAlese(secție);
    }
    private void reloadTabelCandidați_SecțiiAlese(Secție secție) {
        try {
            Vector<Candidat> candidatVector = generalService.filtrareOpțiuni_secție(secție.getID());
            candidați_SecțiiAlese.clear();
            candidați_SecțiiAlese.addAll(candidatVector);
            tabelCandidați_secțiiOptate.setItems(candidați_SecțiiAlese);

        } catch (Exception ignored) {}
    }


    @FXML
    public void cautăSecție_SecțiiAlese(KeyEvent keyEvent) {
        cautăSecție_SecțiiAlese();
    }
    public void cautăSecție_SecțiiAlese() {
        secțiiGăsite_SecțiiAlese.clear();
        lupaSearch_secțiiOptate.setOpacity(0);
        x_search_secțiiOptate.setOpacity(0.5);
        String secție = câmpSearch_secțiiOptate.getText();
        if (secție.length() == 0) {
            lupaSearch_secțiiOptate.setOpacity(1);
            x_search_secțiiOptate.setOpacity(0);
            tabelSelectare_secțiiOptate.setItems(toateSecțiile);
            return;
        }

        cautăSecție_SecțiiAlese(secție);
        lupaSearch_secțiiOptate.setOpacity(0);
        tabelSelectare_secțiiOptate.setItems(secțiiGăsite_SecțiiAlese);
    }

    private void cautăSecție_SecțiiAlese(String idSecție) {
        Vector<Secție> secțiiActuale = new Vector<>(toateSecțiile);
        Vector<Secție> secțieVector = secțieService.filtrare_id(idSecție, secțiiActuale);
        secțiiGăsite_SecțiiAlese.clear();
        secțiiGăsite_SecțiiAlese.addAll(secțieVector);
    }

    @FXML
    public void închideSearch_SecțiiAlese(MouseEvent mouseEvent) {
        câmpSearch_secțiiOptate.setText("");
        reloadSecții();
        tabelSelectare_secțiiOptate.setItems(toateSecțiile);
        x_search_secțiiOptate.setOpacity(0);
    }

    @FXML
    public void setOpacityX_SecțiiAlese_100(MouseEvent mouseEvent) {
        if (!Objects.equals(câmpSearch_secțiiOptate.getText(), ""))
            x_search_secțiiOptate.setOpacity(1);
    }
    @FXML
    public void setOpacityX_SecțiiAlese_50(MouseEvent mouseEvent) {
        if (!Objects.equals(câmpSearch_secțiiOptate.getText(), ""))
            x_search_secțiiOptate.setOpacity(0.5);
    }

    @FXML
    public void ascundeLupa_SecțiiAlese(MouseEvent mouseEvent) {
        lupaSearch_secțiiOptate.setOpacity(0);
    }
    @FXML
    public void aratăLupa_SecțiiAlese(MouseEvent mouseEvent) {
        if (câmpSearch_secțiiOptate.getText().equals(""))
            lupaSearch_secțiiOptate.setOpacity(1);
    }




    /** Raport: Numărul de optări pentru fiecare secție, în funcție de prioritate */

    private void reloadChartSecții() {
        chartSelecțieSecții.getData().clear();
        chartSelecțieSecții.setTitle("Raportul de selecție a tuturor secțiilor");
        XYChart.Series optăriTotale = new XYChart.Series();
        XYChart.Series primaOpțiune = new XYChart.Series();
        XYChart.Series douaOpțiune = new XYChart.Series();
        XYChart.Series treiaOpțiune = new XYChart.Series();
        optăriTotale.setName("Optări totale");
        primaOpțiune.setName("Prima opțiune");
        douaOpțiune.setName("A doua opțiune");
        treiaOpțiune.setName("A treia opțiune");

        Vector<Secție> secții = new Vector<>();
        if (checkBoxDoarSecțiileOptate.isSelected())
            secții =  generalService.getCeleMaiOptateSecții();
        else
            secții.addAll(toateSecțiile);

        for (Secție secție : secții) {
            Integer nrOptăriTotale = generalService.getNrOptări(secție.getID());
            Integer nrOptări_1 = generalService.getNrOptăriPrioritate(secție.getID(), 1);
            Integer nrOptări_2 = generalService.getNrOptăriPrioritate(secție.getID(), 2);
            Integer nrOptări_3 = generalService.getNrOptăriPrioritate(secție.getID(), 3);
            optăriTotale.getData().add(new XYChart.Data(secție.toString(), nrOptăriTotale));
            primaOpțiune.getData().add(new XYChart.Data(secție.toString(), nrOptări_1));
            douaOpțiune.getData().add(new XYChart.Data(secție.toString(), nrOptări_2));
            treiaOpțiune.getData().add(new XYChart.Data(secție.toString(), nrOptări_3));
        }
        chartSelecțieSecții.getData().addAll(optăriTotale, primaOpțiune, douaOpțiune, treiaOpțiune);
    }

    public void handlerCheckBoxDoarSecțiileOptate(MouseEvent mouseEvent) {
        reloadChartSecții();
    }




    /** Raport: Cele mai dorite N secții */
    private void reloadNrSecții() {
        nrSecții = generalService.getSecții().size();
        reloadChoiceBoxNrSecții();
        reloadChoiceBoxPrioritate();
    }
    private void reloadChoiceBoxNrSecții() {
        choiceBox_NrSecții.getItems().clear();
        for (int i=1; i <= nrSecții; i++)
            choiceBox_NrSecții.getItems().add(i);

        choiceBox_NrSecții.getSelectionModel().selectFirst();
    }
    private void reloadChoiceBoxPrioritate() {
        choiceBox_Prioritate.getItems().clear();
        choiceBox_Prioritate.getItems().add("oricare");
        for (int i=1; i <= nrSecții; i++)
            choiceBox_Prioritate.getItems().add(String.valueOf(i));

        choiceBox_Prioritate.getSelectionModel().selectFirst();
    }

    private void loadCeleMaiDoriteNsecții() {
        try {
            Integer nrSecții = (Integer) choiceBox_NrSecții.getSelectionModel().getSelectedItem();
            String prioritate = (String) choiceBox_Prioritate.getSelectionModel().getSelectedItem();

            if (nrSecții == null || prioritate == null)
                return;

            if (prioritate.equals("oricare")) {
                Vector<Secție> secțieVector = generalService.getCeleMaiOptateSecții();
                Vector<Secție> secțieVector_2 = new Vector<>();
                for (int i=0; i < nrSecții && i < secțieVector.size(); i++)
                    secțieVector_2.add(secțieVector.elementAt(i));
                celeMaiDoriteNsecții.clear();
                celeMaiDoriteNsecții.addAll(secțieVector_2);
                tabelCeleMaiDoriteNSecții.setItems(celeMaiDoriteNsecții);
            }

            else {
                Integer prioritateNr = Integer.valueOf(prioritate);
                Vector<Secție> secțieVector = generalService.getCeleMaiDoriteSecțiiPrioritate(prioritateNr);
                Vector<Secție> secțieVector_2 = new Vector<>();
                for (int i=0; i<nrSecții && i<secțieVector.size(); i++)
                    secțieVector_2.add(secțieVector.elementAt(i));
                celeMaiDoriteNsecții.clear();
                celeMaiDoriteNsecții.addAll(secțieVector_2);
                tabelCeleMaiDoriteNSecții.setItems(celeMaiDoriteNsecții);
            }
        } catch (Exception e) {
            celeMaiDoriteNsecții.clear();
        }
    }


    public void printeazăCandidațiiDinSecțiaX(ActionEvent actionEvent) {
        try {
            FXMLLoader loaderPrintare = new FXMLLoader();
            loaderPrintare.setLocation(getClass().getClassLoader().getResource("sample/ui/adminUI/PrintareGenerală.fxml"));
            Parent rootPrintare = loaderPrintare.load();
            PrintareGeneralăController printareGeneralăController = loaderPrintare.getController();
            printareGeneralăController.setPrintBorderPane(borderPaneCandidațiiDinSecțiaX, "Candidații înscriși la secția " + labelNumeSecțieAleasă.getText());

            Stage stage = new Stage();
            stage.setTitle("Candidații înscriși la secția " + labelNumeSecțieAleasă.getText());
            stage.setScene(new Scene(rootPrintare));
            stage.show();
        }
        catch (IOException e) {
            afișareMesaj(e.getMessage(), false);
        }
    }

    public void printeazăBarChart(ActionEvent actionEvent) {
        try {
            FXMLLoader loaderPrintare = new FXMLLoader();
            loaderPrintare.setLocation(getClass().getClassLoader().getResource("sample/ui/adminUI/PrintareGenerală.fxml"));
            Parent rootPrintare = loaderPrintare.load();
            PrintareGeneralăController printareGeneralăController = loaderPrintare.getController();
            printareGeneralăController.setPrintBorderPane(borderPaneChart, "Graficul alegerii secților");

            Stage stage = new Stage();
            stage.setTitle("Graficul alegerii secților");
            stage.setScene(new Scene(rootPrintare));
            stage.show();
        }
        catch (IOException e) {
            afișareMesaj(e.getMessage(), false);
        }
    }

    public void printeazăCeleMaiDoriteSecții(ActionEvent actionEvent) {
        try {
            FXMLLoader loaderPrintare = new FXMLLoader();
            loaderPrintare.setLocation(getClass().getClassLoader().getResource("sample/ui/adminUI/PrintareGenerală.fxml"));
            Parent rootPrintare = loaderPrintare.load();
            PrintareGeneralăController printareGeneralăController = loaderPrintare.getController();
            printareGeneralăController.setPrintBorderPane(borderpaneCeleMaiDoriteSecții, "Cele mai dorite "+ choiceBox_NrSecții.getSelectionModel().getSelectedItem() + " secții cu prioritatea " + choiceBox_Prioritate.getSelectionModel().getSelectedItem());

            Stage stage = new Stage();
            stage.setTitle("Cele mai dorite "+ choiceBox_NrSecții.getSelectionModel().getSelectedItem() + " secții cu prioritatea " + choiceBox_Prioritate.getSelectionModel().getSelectedItem());
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
