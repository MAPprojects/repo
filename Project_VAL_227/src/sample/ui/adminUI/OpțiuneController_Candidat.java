package sample.ui.adminUI;

import javafx.application.Platform;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import sample.domain.Candidat;
import sample.domain.FormăDeFinanțare;
import sample.domain.Opțiune;
import sample.domain.Secție;
import sample.filterTypes.FilterTypesOpțiune;
import sample.service.GeneralService;
import sample.service.OpțiuneService;
import sample.ui.userUI.GenerareContractController;
import sample.utils.Observer;
import sample.utils.OpțiuneEvent;

import java.util.Objects;
import java.util.Vector;

public class OpțiuneController_Candidat implements Observer<OpțiuneEvent> {
    /** VARIABIBLE */
    private OpțiuneService opțiuneService;  // ATENȚIE: folosim GeneralService pentru ADD, DELETE, UPDATE și filtrări opțiune
    private GeneralService generalService;
    private ObservableList<Opțiune> opțiuni;
    private ObservableList<Opțiune> opțiuniGăsite;
    private ObservableList<Candidat> candidațiGăsiți;
    private ObservableList<Candidat> candidat_OpțiuneCandidat;
    private ObservableList<Secție> secțiiGăsite;
    private ObservableList<Secție> secții_OpțiuneCandidat;
    private String filtru;
    private FilterTypesOpțiune tipFiltru;
    private Candidat candidatSelectat;
    private Secție secțieSelectată;
    @FXML
    public BorderPane mainBorderPane;
    private ObservableList<Candidat> candidațiGăsiți_OpțiuneCandidat;
    private ObservableList<Secție> secțiiGăsite_OpțiuneCandidat;
    private ObservableList<FilterTypesOpțiune> opțiuniCăutare_OpțiuniCandidat;
    private ObservableList<Integer> prioritățiSecții;
    // Tabel candidat
    @FXML
    public TableView<Candidat> tabelOpțiuniCandidat_candidat;
    @FXML
    public TableColumn<Candidat,String> tabelOpțiuniCandidat_candidat_CNP;
    @FXML
    public TableColumn<Candidat,String> tabelOpțiuniCandidat_candidat_nume;
    @FXML
    public TableColumn<Candidat,String> tabelOpțiuniCandidat_candidat_prenume;
    @FXML
    public TableColumn<Candidat,String> tabelOpțiuniCandidat_candidat_telefon;
    @FXML
    public TableColumn<Candidat,String> tabelOpțiuniCandidat_candidat_email;
    // Tabel secții alese
    @FXML
    public TableView<Secție> tabelSecții;
    @FXML
    public TableColumn<Secție,Integer> tabelOpțiuniCandidat_secții_prioritate;
    @FXML
    public TableColumn<Secție,Integer> tabelOpțiuniCandidat_secții_ID;
    @FXML
    public TableColumn<Secție,String> tabelOpțiuniCandidat_secții_nume;
    @FXML
    public TableColumn<Secție,String> tabelOpțiuniCandidat_secții_limbaDePredare;
    @FXML
    public TableColumn<Secție,FormăDeFinanțare> tabelOpțiuniCandidat_secții_formaDeFinanțare;
    @FXML
    public TableColumn<Secție,Integer> tabelOpțiuniCandidat_secții_nrLocuri;
    // Câmpuri + butoane
    @FXML
    public ChoiceBox<Integer> choiceBoxPrioritateSecție;
    @FXML
    public TextField câmpOpțiuneCandidat_IDsecție;
    @FXML
    public TextField câmpOpțiuneCandidat_numeSecție;
    @FXML
    public TextField câmpOpțiuneCandidat_LimbăPredare;
    @FXML
    public TextField câmpOpțiuneCandidat_FormăFinanțare;
    @FXML
    public TextField câmpOpțiuneCandidat_nrLocuri;
    @FXML
    public Button butonOpțiuneCandidat_Adaugă;
    @FXML
    public Button butonOpțiuneCandidat_Modifică;
    @FXML
    public Button butonOpțiuneCandidat_Șterge;
    @FXML
    public Button butonGenerareContract;
    // Search
    @FXML
    public TextField câmpSearchOpțiuneCandidat;
    @FXML
    public ListView<Candidat> listViewSearchOpțiuneCandidat_candidați;
    @FXML
    public ListView<Secție> listViewSearchOpțiuneCandidat_secții;
    @FXML
    public ChoiceBox<FilterTypesOpțiune> opțiuneCandidatSearchOptions;
    @FXML
    public ImageView lupaSearchOpțiuneCandidat;
    @FXML
    public ImageView x_searchOpțiuneCandidat;
    @FXML
    public VBox vBoxMesajeSmart;

    private boolean modificDinCod;


    /** METODE */
    public OpțiuneController_Candidat() {
        this.opțiuneService = new OpțiuneService("opțiuni.txt", "opțiuni.xml",  "./src/sample/data/", "log_Opțiuni.txt");
        opțiuneService.addObserver(this);
        generalService = new GeneralService(opțiuneService);
        this.opțiuni = FXCollections.observableArrayList();
        this.opțiuniGăsite = FXCollections.observableArrayList();
        this.candidațiGăsiți = FXCollections.observableArrayList();
        this.secțiiGăsite = FXCollections.observableArrayList();
        this.candidat_OpțiuneCandidat = FXCollections.observableArrayList();
        this.secții_OpțiuneCandidat = FXCollections.observableArrayList();
        reloadOpțiuni();
        //candidatSelectat = new Candidat();
        //secțieSelectată = new Secție();
        modificDinCod = false;
    }

    public OpțiuneController_Candidat(OpțiuneService opțiuneService) {
        this.opțiuneService = opțiuneService;
        opțiuneService.addObserver(this);
        generalService = new GeneralService(opțiuneService);
        this.opțiuni = FXCollections.observableArrayList();
        this.opțiuniGăsite = FXCollections.observableArrayList();
        this.candidațiGăsiți = FXCollections.observableArrayList();
        this.secțiiGăsite = FXCollections.observableArrayList();
        this.candidat_OpțiuneCandidat = FXCollections.observableArrayList();
        this.secții_OpțiuneCandidat = FXCollections.observableArrayList();
        reloadOpțiuni();
    }

    // A se folosi acest constructor
    public OpțiuneController_Candidat(GeneralService generalService) {
        this.generalService = generalService;
        opțiuneService = generalService.getOpțiuneService();
        opțiuneService.addObserver(this);
        this.opțiuni = FXCollections.observableArrayList();
        this.opțiuniGăsite = FXCollections.observableArrayList();
        this.candidațiGăsiți = FXCollections.observableArrayList();
        this.secțiiGăsite = FXCollections.observableArrayList();
        this.candidat_OpțiuneCandidat = FXCollections.observableArrayList();
        this.secții_OpțiuneCandidat = FXCollections.observableArrayList();
        reloadOpțiuni();
    }

    @FXML
    private void initialize() {
        /* OPȚIUNE */
        candidațiGăsiți_OpțiuneCandidat = FXCollections.observableArrayList();
        // tabel candidat
        tabelOpțiuniCandidat_candidat_CNP.setCellValueFactory(new PropertyValueFactory<Candidat, String>("ID"));
        tabelOpțiuniCandidat_candidat_nume.setCellValueFactory(new PropertyValueFactory<Candidat, String>("Nume"));
        tabelOpțiuniCandidat_candidat_prenume.setCellValueFactory(new PropertyValueFactory<Candidat, String>("Prenume"));
        tabelOpțiuniCandidat_candidat_telefon.setCellValueFactory(new PropertyValueFactory<Candidat, String>("Telefon"));
        tabelOpțiuniCandidat_candidat_email.setCellValueFactory(new PropertyValueFactory<Candidat, String>("E_mail"));
        // tabel secție
        tabelOpțiuniCandidat_secții_prioritate.setCellValueFactory(column-> new ReadOnlyObjectWrapper<Integer>(tabelSecții.getItems().indexOf(column.getValue()) + 1));
        tabelOpțiuniCandidat_secții_ID.setCellValueFactory(new PropertyValueFactory<Secție, Integer>("ID"));
        tabelOpțiuniCandidat_secții_nume.setCellValueFactory(new PropertyValueFactory<Secție, String>("Nume"));
        tabelOpțiuniCandidat_secții_limbaDePredare.setCellValueFactory(new PropertyValueFactory<Secție,String>("LimbaDePredare"));
        tabelOpțiuniCandidat_secții_formaDeFinanțare.setCellValueFactory(new PropertyValueFactory<Secție,FormăDeFinanțare>("FormăDeFinanțare"));
        tabelOpțiuniCandidat_secții_nrLocuri.setCellValueFactory(new PropertyValueFactory<Secție, Integer>("NrLocuri"));

        // lista de search
        listViewSearchOpțiuneCandidat_candidați.setOpacity(0);
        listViewSearchOpțiuneCandidat_secții.setOpacity(0);
        x_searchOpțiuneCandidat.setOpacity(0);

        opțiuniCăutare_OpțiuniCandidat = FXCollections.observableArrayList(
                FilterTypesOpțiune.CNP_CANDIDAT, FilterTypesOpțiune.NUME_CANDIDAT, FilterTypesOpțiune.ID_SECȚIE, FilterTypesOpțiune.NUME_SECȚIE);
        opțiuneCandidatSearchOptions.setItems(opțiuniCăutare_OpțiuniCandidat);
        opțiuneCandidatSearchOptions.getSelectionModel().selectFirst();

        prioritățiSecții = FXCollections.observableArrayList();
        choiceBoxPrioritateSecție.setItems(prioritățiSecții);
        //choiceBoxPrioritateSecție.getSelectionModel().selectFirst();

        candidațiGăsiți_OpțiuneCandidat = getCandidațiCăutați();
        candidat_OpțiuneCandidat = getCandidatAles();
        secțiiGăsite_OpțiuneCandidat = getSecțiiCăutate();
        secții_OpțiuneCandidat = getSecțiiAlese();

        listViewSearchOpțiuneCandidat_candidați.toFront();

        seteazăListeneri();

        disableButonAdaugă();
        disableButonModifică();
        disableButonȘterge();
        butonGenerareContract.setDisable(true);
    }

    private void seteazăListeneri() {
        opțiuneCandidatSearchOptions.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue)-> {
                    cautăOpțiuneCandidat();
                    //activeazăDezactiveazăButoane();
                }
        );
//        câmpSearchOpțiuneCandidat.textProperty().addListener(
//                ((observable, oldValue, newValue) -> {
//                    cautăOpțiuneCandidat();
//                })
//        );

//        listViewSearchOpțiuneCandidat_candidați.getSelectionModel().selectedItemProperty().addListener(
//                (observable, oldValue, newValue) -> {
//                    completareTabelCandidat(newValue);
//                    actualizeazăCandidatulCurent(newValue);
//                    ștergeSearch_OpțiuneCandidat();
//                    /// activeazăDezactiveazăButoane();
//                }
//        );
//        listViewSearchOpțiuneCandidat_secții.getSelectionModel().selectedItemProperty().addListener(
//                (observable, oldValue, newValue) -> {
//                    actualizeazăSecțiaCurentă(newValue);
//                    completareCâmpuriSecție(newValue);
//                    încearcăAdaugăOpțiuneCandidat();
//                    modificDinCod = true;
//                    tabelSecții.getSelectionModel().select(newValue);
//                    modificDinCod = false;
//                    ștergeSearch_OpțiuneCandidat();
//                }
//        );
        choiceBoxPrioritateSecție.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (modificDinCod)
                        return;
                    Secție secție = tabelSecții.getSelectionModel().getSelectedItem();
                    modificăOpțiuneCandidat();
                    selecteazăSecția(secție);
                    activeazăDezactiveazăButoane();
                }
        );
        tabelSecții.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue)-> {
                    if (modificDinCod)
                        return;
                    actualizeazăSecțiaCurentă(newValue);
                    Secție secție = newValue;
                    completareCâmpuriSecție(newValue);
                    actualizeazăPrioritatea(newValue);
                    selecteazăSecția(secție);
                    activeazăDezactiveazăButoane();
                }
        );
    }

    public void setOpțiuneService(OpțiuneService opțiuneService) {
        this.opțiuneService = opțiuneService;
        opțiuneService.addObserver(this);
    }
    public void setGeneralService(GeneralService generalService) {
        this.generalService = generalService;
        this.generalService.setCandidatService(generalService.getCandidatService());
        this.generalService.setSecțieService(generalService.getSecțieService());
        this.generalService.setOpțiuneService(generalService.getOpțiuneService());

        opțiuneService.addObserver(this);
        generalService.setOpțiuneControllerCandidat_înCandidatService(this);
        generalService.setOpțiuneControllerCandidat_înSecțieService(this);
    }

    public ObservableList<Opțiune> getOpțiuni() {
        return opțiuni;
    }

    public ObservableList<Opțiune> getOpțiuniCăutate() {
        return opțiuniGăsite;
    }

    public ObservableList<Candidat> getCandidațiCăutați() {
        return candidațiGăsiți;
    }

    public ObservableList<Secție> getSecțiiCăutate() {
        return secțiiGăsite;
    }

    public Vector<Secție> getSecțiiAlese(String cnpCandidat) {
        return generalService.getSecțiiAlese(cnpCandidat);
    }

    public ObservableList<Candidat> getCandidatAles() {
        return candidat_OpțiuneCandidat;
    }

    public ObservableList<Secție> getSecțiiAlese() {
        return secții_OpțiuneCandidat;
    }

    public void reloadOpțiuni() {
        Vector<Opțiune> opțiuneVector = (Vector<Opțiune>) opțiuneService.findAll();
        opțiuni.clear();
        opțiuneVector.forEach(opțiune -> opțiuni.add(opțiune));
    }


    public Opțiune getOpțiune(String idCandidat) throws Exception {
        return generalService.getOpțiune(idCandidat);
    }

    public Integer getPrioritate(String idCandidat, String idSecție) throws Exception {
        return generalService.getPrioritate(idCandidat, idSecție);
    }

    public void adaugăOpțiune(String idCandidat, String idSecție) throws Exception {
        try {
            generalService.adaugăOpțiune(idCandidat, idSecție);
        }
        catch (NumberFormatException e) { throw new NumberFormatException("ID-ul trebuie să fie număr."); }
        // excepțiile aruncate de service se vor prinde în View
    }

    public void modificăOpțiune_Secția(String idCandidat, String idSecție, String idSecțieNouă) throws Exception {
        try {
            generalService.modificăOpțiune(idCandidat, idSecție, idSecțieNouă);
        }
        catch (NumberFormatException e) { throw new NumberFormatException("ID-ul trebuie să fie număr."); }
        // excepțiile aruncate de service se vor prinde în View
    }

    public void modificăOpțiune_Prioritatea(String idCandidat, String idSecție, String prioritateNouă_str) throws Exception {
        try {
            Integer prioritateNouă = Integer.valueOf(prioritateNouă_str);
            generalService.modificăOpțiune_Prioritate(idCandidat, idSecție, prioritateNouă);
        }
        catch (NumberFormatException e) { throw new NumberFormatException("ID-ul și prioritatea trebuie să fie număr."); }
        // excepțiile aruncate de service se vor prinde în View
    }

    public void ștergeOpțiune(String idCandidat, String idSecție) throws Exception {
        try {
            generalService.ștergeOpțiune(idCandidat, idSecție);
        }
        catch (NumberFormatException e) { throw new NumberFormatException("ID-ul trebuie să fie număr."); }
        // excepțiile aruncate de service se vor prinde în View
    }
    public void ștergeOpțiune(String idCandidat_str) throws Exception {
        try {
            //Integer idCandidat = Integer.valueOf(idCandidat_str);
            generalService.ștergeOpțiune(idCandidat_str);
        }
        catch (NumberFormatException e) { throw new NumberFormatException("ID-ul trebuie să fie număr."); }
        // excepțiile aruncate de service se vor prinde în View
    }


    /**
     * Aruncă excepție dacă idCandidat_str nu poate fi număr !
     */
    public void cautăCandidatID(String idCandidat_str) throws Exception {
        this.tipFiltru = FilterTypesOpțiune.CNP_CANDIDAT;
        this.filtru = idCandidat_str;
        //Integer idCandidat = Integer.valueOf(idCandidat_str);
        candidațiGăsiți.clear();
        try {
            Candidat candidat = generalService.getCandidat(idCandidat_str);
            candidațiGăsiți.add(candidat);
        }
        catch (Exception e) {}
    }

    public void cautăCandidatNume(String numeCandidat) {
        this.tipFiltru = FilterTypesOpțiune.NUME_CANDIDAT;
        this.filtru = numeCandidat;
        Vector<Candidat> candidatVector = generalService.filtrareCandidați_nume(numeCandidat);
        candidațiGăsiți.clear();
        candidațiGăsiți.addAll(candidatVector);
    }

    /**
     * Aruncă excepție dacă idSecție_str nu poate fi număr !
     */
    public void cautăSecțieID(String idSecție) throws Exception {
        this.tipFiltru = FilterTypesOpțiune.ID_SECȚIE;
        this.filtru = idSecție;
        secțiiGăsite.clear();
        Vector<Secție> secțiiActuale = new Vector<>(generalService.getSecții());
        Vector<Secție> secțieVector = generalService.getSecțieService().filtrare_id(idSecție, secțiiActuale);
        secțiiGăsite.addAll(secțieVector);
    }

    public void cautăSecțieNume(String numeSecție) {
        this.tipFiltru = FilterTypesOpțiune.NUME_SECȚIE;
        this.filtru = numeSecție;
        secțiiGăsite.clear();
        Vector<Secție> secțiiActuale = new Vector<>(generalService.getSecții());
        Vector<Secție> secțieVector = generalService.filtrareSecții_nume(numeSecție, secțiiActuale);
        secțiiGăsite.addAll(secțieVector);
    }

    public Candidat getCandidatAles_OpțiuneCandidat() {
        try {
            return candidat_OpțiuneCandidat.get(0);
        }
        catch (Exception e) {
            return null;
        }
    }

    private void reloadCandidatAles(String id) {
        try {
            Candidat candidat = generalService.getCandidat(id);
            candidat_OpțiuneCandidat.clear();
            candidat_OpțiuneCandidat.add(candidat);
        } catch (Exception ignored) {}
    }

    public void reloadSecțiiAlese(Candidat candidat) {
        Vector<Secție> secțieVector = getSecțiiAlese(candidat.getID());
        secții_OpțiuneCandidat.clear();
        secții_OpțiuneCandidat.addAll(secțieVector);

        tabelSecții.setItems(secții_OpțiuneCandidat);
    }

    @Override
    public void notifyOnEvent(OpțiuneEvent event) {
        notifyOnEvent();
    }
    public void notifyOnEvent() {
        reloadOpțiuni();
        try {
            reloadCandidatAles(getCandidatAles_OpțiuneCandidat().getID());
            reloadSecțiiAlese(getCandidatAles_OpțiuneCandidat());
        } catch (Exception ignored) {}

        if (tipFiltru == FilterTypesOpțiune.CNP_CANDIDAT)
            try { cautăCandidatID(filtru); }
            catch (Exception ignored) {}
        if (tipFiltru == FilterTypesOpțiune.NUME_CANDIDAT)
            cautăCandidatNume(filtru);
        if (tipFiltru == FilterTypesOpțiune.ID_SECȚIE)
            try {cautăSecțieID(filtru); }
            catch (Exception ignored) {}
        if (tipFiltru == FilterTypesOpțiune.NUME_SECȚIE)
            cautăSecțieNume(filtru);

        // pentru optimizare, am putea verifica tipul de eveniment și să actualizăm doar elementul adăugat/șters/modificat

        activeazăDezactiveazăButoane();
    }


    public BorderPane getMainBorderPane() {
        return mainBorderPane;
    }

    // completez tabelul de candidați cu candidatul găsit
    private void completareTabelCandidat(Candidat candidat) {
        if (candidat == null)
            return;

        candidat_OpțiuneCandidat.clear();
        candidat_OpțiuneCandidat.add(candidat);
        tabelOpțiuniCandidat_candidat.setItems(candidat_OpțiuneCandidat);
        reloadSecțiiAlese(candidat);
    }

    // completez câmpurile cu detaliile secției selectate în lista de search
    private void completareCâmpuriSecție(Secție secție) {
        if (secție == null)
            return;

        câmpOpțiuneCandidat_IDsecție.setText(String.valueOf(secție.getID()));
        câmpOpțiuneCandidat_numeSecție.setText(secție.getNume());
        câmpOpțiuneCandidat_LimbăPredare.setText(secție.getLimbaDePredare());
        câmpOpțiuneCandidat_FormăFinanțare.setText(secție.getFormăDeFinanțare().toString());
        câmpOpțiuneCandidat_nrLocuri.setText(String.valueOf(secție.getNrLocuri()));
        //animeazăCâmpurile();
    }

    private void animeazăCâmpurile() {
        choiceBoxPrioritateSecție.setStyle("-fx-background-color: rgb(0, 34, 68)");
        try { Thread.sleep(100); }
        catch (Exception ignored) { }
        choiceBoxPrioritateSecție.setStyle("");
    }

    private void actualizeazăPrioritatea(Secție secție) {
        modificDinCod = true;
        Candidat candidat = getCandidatAles_OpțiuneCandidat();
        // dacă a fost ales candidatul
        if (candidat != null) {
            try {
                Opțiune opțiune = getOpțiune(candidat.getID());
                Integer prioritate = getPrioritate(candidat.getID(), secție.getID());

                prioritățiSecții.clear();
                for (int i = 1; i <= opțiune.getIdSecții().size(); i++)
                    prioritățiSecții.add(i);

                choiceBoxPrioritateSecție.setItems(prioritățiSecții);
                choiceBoxPrioritateSecție.getSelectionModel().select(prioritate-1);
            }
            catch (Exception ignored) {
                prioritățiSecții.clear();
                //prioritățiSecții.add(1);
                //choiceBoxPrioritateSecție.getSelectionModel().selectFirst();
            }
        }
        // acum selectăm secția în tabel
        tabelSecții.getSelectionModel().select(secție);
        modificDinCod = false;
    }

    @FXML
    public void adaugăOpțiuneCandidat(ActionEvent actionEvent) {
        adaugăOpțiuneCandidat();
        activeazăDezactiveazăButoane();
    }
    public void adaugăOpțiuneCandidat() {
        Candidat candidat = getCandidatAles_OpțiuneCandidat();
        if (candidat == null) {
            afișareMesajEroare("Selectează un candidat.");
            return;
        }

        String idSecție = câmpOpțiuneCandidat_IDsecție.getText();
        if (Objects.equals(idSecție, "")) {
            afișareMesajEroare("Selectează o secție.");
            return;
        }

        try {
            adaugăOpțiune(String.valueOf(candidat.getID()), idSecție);
            actualizeazăPrioritatea(secțieSelectată);
            afișareMesajSmart("Candidatul " + candidat.getNume() + " a fost înscris cu succes la secția " + idSecție + ".", true);
        } catch (Exception e) {
            afișareMesajEroare(e.getMessage());
            afișareMesajSmart("Candidatul " + candidat.getNume() + " nu a fost înscris la secția " + idSecție + ".", false);
        }
    }

    public void încearcăAdaugăOpțiuneCandidat() {
        Candidat candidat = getCandidatAles_OpțiuneCandidat();
        if (candidat == null) {
            return;
        }

        String idSecție = câmpOpțiuneCandidat_IDsecție.getText();
        if (idSecție == null || Objects.equals(idSecție, "")) {
            return;
        }

        try {
            adaugăOpțiune(String.valueOf(candidat.getID()), idSecție);
            //actualizeazăPrioritatea(secțieSelectată);
            afișareMesajSmart("Candidatul " + candidat.getNume() + " a fost înscris cu succes la secția " + idSecție + ".", true);

        } catch (Exception ignored) {
            afișareMesajSmart("Candidatul " + candidat.getNume() + " este înscris la secția " + idSecție + ".", true);
        }
    }

    /**
     * Modifică prioritatea secției.
     */
    @FXML
    public void modificăOpțiuneCandidat(ActionEvent actionEvent) {
        modificăOpțiuneCandidat();
    }
    @FXML
    public void modificăOpțiuneCandidat_doarPrioritatea(MouseEvent mouseEvent) {

        //modificăOpțiuneCandidat();
    }
    public void modificăOpțiuneCandidat() {
        Candidat candidat = getCandidatAles_OpțiuneCandidat();
        if (candidat == null) {
            afișareMesajSmart("Selectează un candidat.", false);
            return;
        }

        String idSecție = câmpOpțiuneCandidat_IDsecție.getText();
        if (idSecție == null || Objects.equals(idSecție, "")) {
            afișareMesajSmart("Selectează secția.", false);
            return;
        }

        Integer prioritate = (Integer) choiceBoxPrioritateSecție.getSelectionModel().getSelectedItem();
        if (prioritate == null) {
            //afișareMesajSmart("Selectează prioritatea", false);
            return;
        }

        try {
            modificăOpțiune_Prioritatea(String.valueOf(candidat.getID()), idSecție, prioritate.toString());
            afișareMesajSmart("Prioritatea secției " + secțieSelectată.getNume() + "\n"
                    + "a fost schimbată cu succes la " + prioritate + ".", true);
        }
        catch (Exception e) {
            afișareMesajEroare(e.getMessage());
            afișareMesajSmart("Prioritatea secției " + secțieSelectată.getNume() + " nu a fost schimbată.", false);
        }
    }

    @FXML
    public void ștergeOpțiune_secția(ActionEvent actionEvent) {
        Candidat candidat = getCandidatAles_OpțiuneCandidat();
        if (candidat == null) {
            afișareMesajEroare("Selectează un candidat.");
            return;
        }

        String idSecție = câmpOpțiuneCandidat_IDsecție.getText();
        if (Objects.equals(idSecție, "")) {
            afișareMesajEroare("Selectează o secție.");
            return;
        }

        try {
            ștergeOpțiune(candidat.getID().toString(), idSecție);
            afișareMesajSmart("Candidatul " + candidat.getNume() + " a fost eliminat cu succes de la secția " + idSecție + ".", true);
        }
        catch (Exception e) {
            afișareMesajEroare(e.getMessage());
            afișareMesajSmart("Candidatul " + candidat.getNume() + " nu a fost eliminat de la secția " + idSecție + ".", false);
        }
    }

    /**
     * Utilizatorul a selectat o secție.
     * Butonul ADD      se activează dacă secție nu există în opțiunile candidatului.
     * Butonul UPDATE   se activează dacă i s-a modificat prioritatea secției alese.
     * Butonul DELETE   se activează dacă există secția în opțiunile candidatului.
     * Butoanele se dezactivează în caz contrar.
     */
    private void activeazăDezactiveazăButoane() {
        if (candidatSelectat == null || secțieSelectată == null) {
            disableButonAdaugă();
            disableButonModifică();
            disableButonȘterge();
            return;
        }

        try {
            // ADD + DELETE
            generalService.getOpțiune(candidatSelectat.getID(), secțieSelectată.getID());
            // => secția a fost optată de candidat
            disableButonAdaugă();
            enableButonȘterge();

            // UPDATE
            try {
                Integer prioritate = choiceBoxPrioritateSecție.getSelectionModel().getSelectedItem();
                if (prioritate == null) {
                    actualizeazăPrioritatea(secțieSelectată);
                    return;
                }
                if (prioritate.equals(generalService.getPrioritate(candidatSelectat.getID(), secțieSelectată.getID())))
                    disableButonModifică();
                else
                    enableButonModifică();
            } catch (Exception ignored) {   /// NumberFormatException era înainte
                disableButonModifică();
            }

        } catch (Exception e) {
            // => secția nu a fost optată de candidat
            enableButonAdaugă();
            disableButonModifică();
            disableButonȘterge();
        }
    }

    private void actualizeazăCandidatulCurent(Object object) {
        Candidat c = (Candidat) object;
        if (c != null) {
            candidatSelectat = c;
            butonGenerareContract.setDisable(false);
        }
    }
    private void actualizeazăSecțiaCurentă() {
        Secție s = tabelSecții.getSelectionModel().getSelectedItem();
        if (s != null)
            secțieSelectată = s;
    }
    private void actualizeazăSecțiaCurentă(Object object) {
        Secție s = (Secție) object;
        if (s != null)
            secțieSelectată = s;
    }

    /**
     * S-a ales un candidat din lista de search.
     * Se completază câmpurile tabelului cu detaliile candidatului ales.
     *
     * SAU
     *
     * S-a ales o secție din lista de search.
     * Se completează câmpurile cu detaliile secției.
     * Se adaugă secția în lista de secții ale candidatului (dacă nu există).
     */
    @FXML
    public void completareTabelCandidat(MouseEvent mouseEvent) {
        FilterTypesOpțiune tipFiltrare = (FilterTypesOpțiune) opțiuneCandidatSearchOptions.getSelectionModel().getSelectedItem();

        if (tipFiltrare == FilterTypesOpțiune.CNP_CANDIDAT || tipFiltrare == FilterTypesOpțiune.NUME_CANDIDAT) {
            listViewSearchOpțiuneCandidat_candidați.toFront();
        }

        if (tipFiltrare == FilterTypesOpțiune.ID_SECȚIE || tipFiltrare == FilterTypesOpțiune.NUME_SECȚIE) {
            listViewSearchOpțiuneCandidat_secții.toFront();
//            listViewSearchOpțiuneCandidat_secții.getSelectionModel().selectedItemProperty().addListener(
//                    (observable, oldValue, newValue) -> {
//                        completareCâmpuriSecție(newValue);
//                        try {                                               // se execută de 4 ori, a cincea oară dă eroare
//                            încearcăAdaugăOpțiuneCandidat();        // AICI DĂ EROARE   ///////////////////////////
//                        } catch (Exception ignored) {}                      // dar programul merge corect
//                    }
//            );
        }
    }

    /**
     * Selectează secția primită în tabel (dacă există).
     * @param object = Secție
     */
    private void selecteazăSecția(Object object) {
        Secție secție = (Secție) object;
        if (secție == null)
            return;

        //modificDinCod = true;
        tabelSecții.getSelectionModel().select(secție);
        //modificDinCod = false;
    }

//    @FXML
//    public void cautăOpțiuneCandidat(Event inputMethodEvent) {
//        cautăOpțiuneCandidat();
//    }
    public void cautăOpțiuneCandidat() {
        lupaSearchOpțiuneCandidat.setOpacity(0);
        x_searchOpțiuneCandidat.setOpacity(0.5);
        candidațiGăsiți_OpțiuneCandidat.clear();
        secțiiGăsite_OpțiuneCandidat.clear();


        String filtru = câmpSearchOpțiuneCandidat.getText();
        if (filtru == null || filtru.length() == 0) {
            ascundeListaSearch_OpțiuneCandidat();
            x_searchOpțiuneCandidat.setOpacity(0);
            return;
        }

        FilterTypesOpțiune tipCăutare = (FilterTypesOpțiune) opțiuneCandidatSearchOptions.getSelectionModel().getSelectedItem();
        String obiect = "";
        if (tipCăutare.equals(FilterTypesOpțiune.CNP_CANDIDAT)) {
            obiect = "CANDIDAT";
            try { cautăCandidatID(filtru); }
            catch (Exception ignored) {}
        }
        if (tipCăutare.equals(FilterTypesOpțiune.NUME_CANDIDAT)) {
            obiect = "CANDIDAT";
            cautăCandidatNume(filtru);
        }
        if (tipCăutare.equals(FilterTypesOpțiune.ID_SECȚIE)) {
            obiect = "SECȚIE";
            try { cautăSecțieID(filtru); }
            catch (Exception ignored) {}
        }
        if (tipCăutare.equals(FilterTypesOpțiune.NUME_SECȚIE)) {
            obiect = "SECȚIE";
            cautăSecțieNume(filtru);
        }

//        if (candidatVector.isEmpty())
//            return;

        if (Objects.equals(obiect, "CANDIDAT")) {
            // candidați găsiți
            listViewSearchOpțiuneCandidat_candidați.toFront();
            lupaSearchOpțiuneCandidat.setOpacity(0);
            listViewSearchOpțiuneCandidat_candidați.setOpacity(1);
            listViewSearchOpțiuneCandidat_candidați.setItems(candidațiGăsiți_OpțiuneCandidat);
        }

        if (Objects.equals(obiect, "SECȚIE")) {
            // secții găsite
            listViewSearchOpțiuneCandidat_secții.toFront();
            listViewSearchOpțiuneCandidat_secții.setOpacity(1);
            listViewSearchOpțiuneCandidat_secții.setItems(secțiiGăsite_OpțiuneCandidat);
        }
    }

    @FXML
    private void ascundeListaSearch_OpțiuneCandidat() {
        candidațiGăsiți_OpțiuneCandidat.clear();
        secțiiGăsite_OpțiuneCandidat.clear();
        listViewSearchOpțiuneCandidat_candidați.setOpacity(0);
        listViewSearchOpțiuneCandidat_secții.setOpacity(0);
        lupaSearchOpțiuneCandidat.setOpacity(1);
        //x_searchOpțiuneCandidat.setOpacity(0);
    }

    @FXML
    public void închideSearch_OpțiuneCandidat() {
        câmpSearchOpțiuneCandidat.setText("");
        ascundeListaSearch_OpțiuneCandidat();
        x_searchOpțiuneCandidat.setOpacity(0);
    }

    /**
     * S-a ales o secție din lista de secții ale candidatului.
     * Se completează câmpurile cu detaliile secției.
     */
    @FXML
    public void completareCâmpuriSecție_OpțiuneCandidat(MouseEvent mouseEvent) {
//        tabelSecții.getSelectionModel().selectedItemProperty().addListener(
//                (observable, oldValue, newValue)-> completareCâmpuriSecție(newValue)
//        );
    }

    @FXML
    public void ștergeSearch_OpțiuneCandidat(MouseEvent mouseEvent) {
        ștergeSearch_OpțiuneCandidat();
    }
    public void ștergeSearch_OpțiuneCandidat() {
        câmpSearchOpțiuneCandidat.setText("");
        listViewSearchOpțiuneCandidat_candidați.setOpacity(0);
        listViewSearchOpțiuneCandidat_secții.setOpacity(0);
        lupaSearchOpțiuneCandidat.setOpacity(1);
        x_searchOpțiuneCandidat.setOpacity(0.0);
    }


    @FXML
    public void ascundeLupaOpțiuneCandidat(MouseEvent mouseEvent) {
        lupaSearchOpțiuneCandidat.setOpacity(0);
    }

    @FXML
    public void aratăLupaOpțiuneCandidat(MouseEvent mouseEvent) {
        if (Objects.equals(câmpSearchOpțiuneCandidat.getText(), ""))
            lupaSearchOpțiuneCandidat.setOpacity(1);
    }

    @FXML
    public void setOpacityXopțiuneCandidat_100(MouseEvent mouseEvent) {
        if (!Objects.equals(câmpSearchOpțiuneCandidat.getText(), ""))
            x_searchOpțiuneCandidat.setOpacity(1);
    }

    @FXML
    public void setOpacityXopțiuneCandidat_50(MouseEvent mouseEvent) {
        if (!Objects.equals(câmpSearchOpțiuneCandidat.getText(), ""))
            x_searchOpțiuneCandidat.setOpacity(0.5);
    }

    private void afișareMesajEroare(String eroare) {
        Alert mesaj = new Alert(Alert.AlertType.ERROR);
        mesaj.setTitle("Eroare");
        mesaj.setContentText(eroare);
        mesaj.showAndWait();
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


    private void disableButonAdaugă() {
        butonOpțiuneCandidat_Adaugă.setDisable(true);
    }
    private void disableButonModifică() {
        butonOpțiuneCandidat_Modifică.setDisable(true);
    }
    private void disableButonȘterge() {
        butonOpțiuneCandidat_Șterge.setDisable(true);
    }
    private void enableButonAdaugă() {
        butonOpțiuneCandidat_Adaugă.setDisable(false);
    }
    private void enableButonModifică() {
        butonOpțiuneCandidat_Modifică.setDisable(false);
    }
    private void enableButonȘterge() {
        butonOpțiuneCandidat_Șterge.setDisable(false);
    }


    private void afișareMesajSmart(String mesaj, boolean succes) {
        Label labelMesajSmart = new Label(mesaj);
        if (succes)
            labelMesajSmart.setTextFill(Color.web("#11ff00"));
        else
            labelMesajSmart.setTextFill(Color.web("#ff0000"));
        vBoxMesajeSmart.getChildren().add(labelMesajSmart);

        Thread thread = new Thread() {
            public void run() {
                try {
                    labelMesajSmart.setOpacity(1);
                    Thread.sleep(2000);

                    for (Double opacity = 1.0; opacity >=0; opacity = opacity - 0.01) {
                        labelMesajSmart.setOpacity(opacity);
                        Thread.sleep(10);
                    }

                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            vBoxMesajeSmart.getChildren().remove(labelMesajSmart);
                        }
                    });

                } catch(InterruptedException e) {
                    System.out.println(e);
                }
            }
        };
        thread.start();
    }


    @FXML
    public void handlerListaSearchCandidat(MouseEvent mouseEvent) {
        Candidat candidat = listViewSearchOpțiuneCandidat_candidați.getSelectionModel().getSelectedItem();
        completareTabelCandidat(candidat);
        Candidat copieCandidatSelectat = candidatSelectat;
        actualizeazăCandidatulCurent(candidat);

        if (copieCandidatSelectat == null || candidat == null || !candidat.getID().equals(copieCandidatSelectat.getID()))
            activeazăDezactiveazăButoane();

        ștergeSearch_OpțiuneCandidat();
    }

    public void handlerListaSearchSecție(MouseEvent mouseEvent) {
        Secție secție = listViewSearchOpțiuneCandidat_secții.getSelectionModel().getSelectedItem();

        actualizeazăSecțiaCurentă(secție);
        completareCâmpuriSecție(secție);
        încearcăAdaugăOpțiuneCandidat();
        tabelSecții.getSelectionModel().select(secție);
        ștergeSearch_OpțiuneCandidat();
    }


    public void openWindowGenerareContract(ActionEvent actionEvent) {
        if (candidatSelectat == null)
            afișareMesaj("Selectează un candidat", false);
        try {
            FXMLLoader loaderPrintareOpțiuni = new FXMLLoader();
            loaderPrintareOpțiuni.setLocation(getClass().getClassLoader().getResource("sample/ui/userUI/GenerareContract.fxml"));
            Parent rootPrintareOpțiuni = loaderPrintareOpțiuni.load();
            GenerareContractController generareContractController = loaderPrintareOpțiuni.getController();
            generareContractController.setGeneralService(generalService);
            generareContractController.setCandidat(candidatSelectat);

            Stage stage = new Stage();
            stage.setTitle(candidatSelectat.getNume() + " " + candidatSelectat.getPrenume() + " - generare contract");
            stage.setScene(new Scene(rootPrintareOpțiuni));
            stage.show();
        }
        catch (Exception e) {
            afișareMesaj(e.getMessage(), false);
        }
    }
}
