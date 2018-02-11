package sample.ui.adminUI;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import sample.domain.FormăDeFinanțare;
import sample.domain.Secție;
import sample.filterTypes.FilterTypesSecție;
import sample.service.GeneralService;
import sample.service.SecțieService;
import sample.utils.Observer;
import sample.utils.SecțieEvent;

import java.util.Objects;
import java.util.Vector;

public class SecțieController implements Observer<SecțieEvent> {
    /** VARIABIBLE */
    private SecțieService secțieService;
    private GeneralService generalService;
    private ObservableList<Secție> secții;
    private ObservableList<Secție> secțiiGăsite;
    private ObservableList<FilterTypesSecție> opțiuniCăutareSecții;
    private ObservableList<FormăDeFinanțare> formeDeFinanțare;
    private ObservableList<Secție> secțiiParțiale;
    private ObservableList<String> limbiDePredare;
    private String filtru;
    private FilterTypesSecție tipFiltru;
    private Secție secțieSelectată;
    @FXML
    BorderPane mainBorderPane;
    // Tabel
    @FXML
    public TableView<Secție> tabelSecții;
    @FXML
    public TableColumn<Secție,String> tabelSecții_ID;
    @FXML
    public TableColumn<Secție,String> tabelSecții_Nume;
    @FXML
    public TableColumn<Secție,String> tabelSecții_LimbaDePredare;
    @FXML
    public TableColumn<Secție,FormăDeFinanțare> tabelSecții_FormaDeFinanțare;
    @FXML
    public TableColumn<Secție,Integer> tabelSecții_NrLocuri;
    // Câmpuri + butoane
    @FXML
    public TextField câmpSecțieID;
    @FXML
    public CheckBox checkBoxAutoID;
    @FXML
    public TextField câmpSecțieNume;
    @FXML
    public TextField câmpSecțieLimbaDePredare;
    @FXML
    public ChoiceBox<FormăDeFinanțare> câmpSecțieFormaDeFinanțare;
    @FXML
    public TextField câmpSecțieNrLocuri;
    @FXML
    public Button butonSecțieAdaugă;
    @FXML
    public Button butonSecțieModifică;
    @FXML
    public Button butonSecțieȘterge;
    // Search
    @FXML
    public TextField câmpSearchSecție;
    @FXML
    public ListView<Secție> listViewSearchSecție;
    @FXML
    public ChoiceBox secțieSearchOptions;
    @FXML
    public ChoiceBox<String> choiceBoxLimbaDePredare;
    @FXML
    public ImageView lupaSearchSecție;
    @FXML
    public ImageView x_searchSecție;
    @FXML
    public VBox vBoxMesajeSmart;
    // Paginare
    public Pagination paginare;
    public CheckBox checkBoxAfișarePaginată;
    private Stage stage;


    /** METODE */
    public SecțieController() {
        this.secțieService = new SecțieService("secții.txt", "secții.xml", "./src/sample/data/");
        this.generalService = new GeneralService();
        generalService.setSecțieService(secțieService);
        secțieService.addObserver(this);
        this.secții = FXCollections.observableArrayList();
        this.secțiiGăsite = FXCollections.observableArrayList();
        reloadSecții();
        secțieSelectată = new Secție();
    }

    public SecțieController(SecțieService secțieService) {
        this.secțieService = secțieService;
        this.generalService = new GeneralService();
        generalService.setSecțieService(secțieService);
        secțieService.addObserver(this);
        this.secții = FXCollections.observableArrayList();
        this.secțiiGăsite = FXCollections.observableArrayList();
        reloadSecții();
    }


    @FXML
    private void initialize() {
        /* SECȚIE */
        secțiiParțiale = FXCollections.observableArrayList();
        // tabel
        tabelSecții_ID.setCellValueFactory(new PropertyValueFactory<Secție, String>("ID"));
        tabelSecții_Nume.setCellValueFactory(new PropertyValueFactory<Secție, String>("Nume"));
        tabelSecții_LimbaDePredare.setCellValueFactory(new PropertyValueFactory<Secție, String>("LimbaDePredare"));
        tabelSecții_FormaDeFinanțare.setCellValueFactory(new PropertyValueFactory<Secție, FormăDeFinanțare>("FormăDeFinanțare"));
        tabelSecții_NrLocuri.setCellValueFactory(new PropertyValueFactory<Secție, Integer>("NrLocuri"));

        secții = getSecții();
        tabelSecții.setItems(secții);

        // lista de search
        listViewSearchSecție.setOpacity(0);
        x_searchSecție.setOpacity(0);
        secțiiGăsite = getSecțiiCăutate();

        // opțiunile de search
        opțiuniCăutareSecții = FXCollections.observableArrayList(FilterTypesSecție.NUME, FilterTypesSecție.NrMaximLocuri, FilterTypesSecție.NrMinimLocuri);
        secțieSearchOptions.setItems(opțiuniCăutareSecții);
        secțieSearchOptions.getSelectionModel().selectFirst();

        limbiDePredare = FXCollections.observableArrayList();
        reloadListaLimbilor();

        // câmpuri
        formeDeFinanțare = FXCollections.observableArrayList();
        formeDeFinanțare.addAll(FormăDeFinanțare.BUGET, FormăDeFinanțare.TAXĂ);
        câmpSecțieFormaDeFinanțare.setItems(formeDeFinanțare);

        // altele
        seteazăListeneri();
        disableButonAdaugă();
        disableButonModifică();
        disableButonȘterge();

        checkBoxAfișarePaginată.setSelected(true);
        checkBoxAutoID.setSelected(true);
        câmpSecțieID.setEditable(false);
    }

    private void reloadListaLimbilor() {
        // verificăm dacă există s-au adăugat sau șters limbi
        Vector<String> limbiNoi = generalService.getSecțieService().getLimbileDePredare();
        if (limbiNoi.size() == limbiDePredare.size() - 1) {
            Integer nrGăsite = 0;
            for (String limbăVeche : limbiDePredare)
                if (limbiNoi.contains(limbăVeche))
                    nrGăsite++;
            // nu s-a schimbat nicio limbă, deci nu mai actualizăm lista din choiceBox => nu se va actualiza tabelul cu toate secțiile
            if (nrGăsite == limbiDePredare.size() - 1)
                return;
        }

        limbiDePredare.clear();
        limbiDePredare.add("orice limbă");
        limbiDePredare.addAll(secțieService.getLimbileDePredare());
        choiceBoxLimbaDePredare.setItems(limbiDePredare);
        choiceBoxLimbaDePredare.getSelectionModel().selectFirst();
    }

    private void seteazăListeneri() {
        tabelSecții.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue)-> {
                    // verificăm dacă secția din tabel (deja existentă) are ID generat automat sau nu
                    Secție secție = (Secție) newValue;
                    if (secție != null)
                        if (secție.equals(new Secție(secție.getNume(), secție.getLimbaDePredare(), secție.getFormăDeFinanțare(), secție.getNrLocuri()))) {
                            checkBoxAutoID.setSelected(true);
                            câmpSecțieID.setEditable(false);
                        }
                        else {
                            checkBoxAutoID.setSelected(false);
                            câmpSecțieID.setEditable(true);
                        }


                    completareCâmpuriSecție(newValue);
                    actualizeazăSecțiaCurentă();
                    activeazăDezactiveazăButoane();
                }
        );

        secțieSearchOptions.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue)-> {
                    secțiiGăsite.clear();
                    secțiiGăsite.addAll(secții);
                    cautăSecție();
                }
        );

        choiceBoxLimbaDePredare.getSelectionModel().selectedItemProperty().addListener(
                ((observable, oldValue, newValue) -> {
                    cautăSecție();
                })
        );

        listViewSearchSecție.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue)-> completareCâmpuriSecție(newValue)
        );

        câmpSecțieFormaDeFinanțare.getSelectionModel().selectedItemProperty().addListener(
                (observable ->  {
                    // generăm ID-ul automat și completăm câmpul de ID
                    if (checkBoxAutoID.isSelected())
                        generareAutoID();

                    // dacă există secția cu ID-ul creat, atunci actualizăm secția selectată
                    try { secțieSelectată = secțieService.findOne(câmpSecțieID.getText()); }
                    catch (Exception ignored) {}

                    // acum activăm și dezactivăm butoanele
                    activeazăDezactiveazăButoane();
                })
        );

        paginare.currentPageIndexProperty().addListener((observable, oldValue, newValue) -> recalculareNrSecțiiTabel());
    }

    /**
     * A se apela această funcție după afișarea ferestrei.
     */
    public void modificăriFereastră() {
        /* redimensionarea fereastrei */
        stage = (Stage) tabelSecții.getScene().getWindow();
        stage.heightProperty().addListener((obs, oldVal, newVal) -> {
            recalculareNrSecțiiTabel();
        });

        stage.maximizedProperty().addListener((obs, oldVal, newVal) -> {
            Thread thread = new Thread() {
                public void run() {
                    try { Thread.sleep(100); }
                    catch (InterruptedException ignored) {}
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            recalculareNrSecțiiTabel();
                        }
                    });
                }
            };
            thread.start();
        });

        stage.setMinHeight(360);

        recalculareNrSecțiiTabel();
    }

    public BorderPane getMainBorderPane() {
        return mainBorderPane;
    }

    public ObservableList<Secție> getSecții() {
        return secții;
    }

    private ObservableList<Secție> getSecțiiCăutate() {
        return secțiiGăsite;
    }

//    public void setSecțieService(SecțieService secțieService) {
//        this.secțieService = secțieService;
//        secțieService.addObserver(this);
//        generalService.setSecțieService(secțieService);
//    }
    public void setGeneralService(GeneralService generalService) {
        this.generalService = generalService;
        secțieService = generalService.getSecțieService();
        secțieService.addObserver(this);
    }

    private void reloadSecții() {
        Vector<Secție> candidatVector = (Vector<Secție>) secțieService.findAll();
        secții.clear();
        candidatVector.forEach(candidat -> secții.add(candidat));
    }


    private Secție adaugăSecție(String id, String nume, String limbaDePredare, FormăDeFinanțare formăDeFinanțare, String nrLocuri_str) throws Exception {
        try {
            Integer nrLocuri = Integer.valueOf(nrLocuri_str);
            return secțieService.add(new Secție(id, nume, limbaDePredare, formăDeFinanțare, nrLocuri));
        }
        catch (NumberFormatException e) { throw new NumberFormatException("Numărul de locuri trebuie să fie număr."); }
        // excepțiile aruncate de service se vor prinde în View
    }

    private Secție modificăSecție(String id, String nume, String limbaDePredare, FormăDeFinanțare formăDeFinanțare, String nrLocuri_str) throws Exception {
        try {
            Integer nrLocuri = Integer.valueOf(nrLocuri_str);
            return secțieService.update(new Secție(id, nume, limbaDePredare, formăDeFinanțare, nrLocuri));
        }
        catch (NumberFormatException e) { throw new NumberFormatException("ID-ul și numărul de locuri trebuie să fie număr."); }
        // excepțiile aruncate de service se vor prinde în View
    }

    private Secție ștergeSecție(String id) throws Exception {
        try {
            return generalService.ștergeSecțieSafe(id);
        }
        catch (NumberFormatException e) { throw new NumberFormatException("ID-ul trebuie să fie număr."); }
        // excepțiile aruncate de service se vor prinde în View
    }


    private Vector<Secție> cautăSecție(String numeSecție, Vector<Secție> secțiiActuale) {
        this.tipFiltru = FilterTypesSecție.NUME;
        this.filtru = numeSecție;
        return secțieService.filtrare_nume(numeSecție, secțiiActuale);
    }

    private Vector<Secție> filtrareLimbaDePredare(String limbaDePredare, Vector<Secție> secțiiActuale) {
        return secțieService.filtrare_limbaDePredare(limbaDePredare, secțiiActuale);
    }

    /**
     * Aruncă excepție dacă nrLocuriSecție_str nu poate fi număr !
     */
    private Vector<Secție> filtrareNrMinimLocuriSecție(String nrLocuriSecție_str, Vector<Secție> secțiiActuale) {
        this.tipFiltru = FilterTypesSecție.NrMinimLocuri;
        this.filtru = nrLocuriSecție_str;
        try {
            Integer nrLocuriSecție = Integer.valueOf(nrLocuriSecție_str);
            return secțieService.filtrare_nrMinimLocuri(nrLocuriSecție, secțiiActuale);
        }
        catch (NumberFormatException e) { throw new NumberFormatException("Trebuie să dai un număr."); }
        // excepțiile aruncate de service se vor prinde în View
    }

    /**
     * Aruncă excepție dacă nrLocuriSecție_str nu poate fi număr !
     */
    private Vector<Secție> filtrareNrMaximLocuriSecție(String nrLocuriSecție_str, Vector<Secție> secțiiActuale) {
        this.tipFiltru = FilterTypesSecție.NrMaximLocuri;
        this.filtru = nrLocuriSecție_str;
        try {
            Integer nrLocuriSecție = Integer.valueOf(nrLocuriSecție_str);
            return secțieService.filtrare_nrMaximLocuri(nrLocuriSecție, secțiiActuale);
        }
        catch (NumberFormatException e) { throw new NumberFormatException("Trebuie să dai un număr."); }
        // excepțiile aruncate de service se vor prinde în View
    }


    @Override
    public void notifyOnEvent(SecțieEvent event) {
        reloadListaLimbilor();
        reloadSecții();
        recalculareNrSecțiiTabel();
        cautăSecție();
        activeazăDezactiveazăButoane();
    }


    private void afișareMesajEroare(String eroare) {
        Alert mesaj = new Alert(Alert.AlertType.ERROR);
        mesaj.setTitle("Eroare");
        mesaj.setContentText(eroare);
        mesaj.showAndWait();
    }


    private void completareCâmpuriSecție(Secție secție) {
        if (secție == null)
            return;

        câmpSecțieID.setText(String.valueOf(secție.getID()));
        câmpSecțieNume.setText(secție.getNume());
        câmpSecțieLimbaDePredare.setText(secție.getLimbaDePredare());
        câmpSecțieFormaDeFinanțare.getSelectionModel().select(secție.getFormăDeFinanțare());
        câmpSecțieNrLocuri.setText(secție.getNrLocuri().toString());
    }

    public void handlerCheckBoxAutoID(ActionEvent actionEvent) {
        if (checkBoxAutoID.isSelected()) {
            câmpSecțieID.setEditable(false);
            generareAutoID();
        }
        else {
            câmpSecțieID.setEditable(true);
            activeazăDezactiveazăButoane();
        }
        activeazăDezactiveazăButoane();
    }

    private void generareAutoID() {
        String nume = câmpSecțieNume.getText();
        String limbaDePredare = câmpSecțieLimbaDePredare.getText();
        FormăDeFinanțare formăDeFinanțare = câmpSecțieFormaDeFinanțare.getSelectionModel().getSelectedItem();
        Integer nrLocuri = 0;
        try { nrLocuri = Integer.valueOf(câmpSecțieNrLocuri.getText()); }
        catch (NumberFormatException ignored) {}

        Secție secție = new Secție(nume, limbaDePredare, formăDeFinanțare, nrLocuri);
        String idAutomat = secție.getID();
        câmpSecțieID.setText(idAutomat);
    }


    @FXML
    public void adaugăSecție(ActionEvent actionEvent) {
        String id = câmpSecțieID.getText();
        String nume = câmpSecțieNume.getText();
        String limbaDePredare = câmpSecțieLimbaDePredare.getText();
        FormăDeFinanțare formăDeFinanțare = câmpSecțieFormaDeFinanțare.getSelectionModel().getSelectedItem();
        String nrLocuri = câmpSecțieNrLocuri.getText();
        try {
            Secție secție = adaugăSecție(id, nume, limbaDePredare, formăDeFinanțare, nrLocuri);
            afișareMesajSmart("Secția " + secție.toString() + "\na fost adăugată cu succes.", true);
        } catch (Exception e) {
            afișareMesajEroare(e.getMessage());
            afișareMesajSmart("Secția " + nume + " nu a fost adăugată.", false);
        }
        activeazăDezactiveazăButoane();
    }

    @FXML
    public void modificăSecție(ActionEvent actionEvent) {
        String id = câmpSecțieID.getText();
        String nume = câmpSecțieNume.getText();
        String limbaDePredare = câmpSecțieLimbaDePredare.getText();
        FormăDeFinanțare formăDeFinanțare = câmpSecțieFormaDeFinanțare.getSelectionModel().getSelectedItem();
        String nrLocuri = câmpSecțieNrLocuri.getText();

        try {
            Secție secție = modificăSecție(id, nume, limbaDePredare, formăDeFinanțare, nrLocuri);
            secțieSelectată = secțieService.findOne(id);
            afișareMesajSmart("Secția " + secție.toString() + "\na fost modificată cu succes.", true);
            activeazăDezactiveazăButoane();
        }
        catch (Exception e) {
            afișareMesajSmart("Secția " + nume + " nu a fost modificată.", false);
            afișareMesajEroare(e.getMessage());
        }
    }

    @FXML
    public void ștergeSecție(ActionEvent actionEvent) {
        String id = câmpSecțieID.getText();

        try {
            Secție secție = ștergeSecție(id);
            afișareMesajSmart("Secția " + secție.toString() + "\na fost ștearsă cu succes.", true);
        }
        catch (Exception e) {
            afișareMesajEroare(e.getMessage());
            afișareMesajSmart("Secția " + id + " nu a fost ștearsă.", false);
        }
    }


    @FXML
    public void cautăSecție(KeyEvent inputMethodEvent) {
        cautăSecție();
    }
    public void cautăSecție() {
        secțiiGăsite.clear();
        lupaSearchSecție.setOpacity(0);
        x_searchSecție.setOpacity(0.5);

        String secție = câmpSearchSecție.getText();
        String limbăDePredare = choiceBoxLimbaDePredare.getSelectionModel().getSelectedItem();
        if (limbăDePredare == null)
            return;
        if (secție.length() == 0 && limbăDePredare.equals("orice limbă")) {
            ascundeListaSearchSecție();   // nu mai e necesar decât pentru lupă
            x_searchSecție.setOpacity(0);
            recalculareNrSecțiiTabel();
            return;
        }

        Vector<Secție> secțiiActuale = new Vector<>(secții);

        // filtrul 1
        if (câmpSearchSecție.getText().length() > 0) {
            FilterTypesSecție tipCăutare = (FilterTypesSecție) secțieSearchOptions.getSelectionModel().getSelectedItem();
            if (tipCăutare.equals(FilterTypesSecție.NUME)) {
                secțiiActuale = cautăSecție(secție, secțiiActuale);
            }
            else if (tipCăutare.equals(FilterTypesSecție.NrMaximLocuri)) {
                try {
                    secțiiActuale = filtrareNrMaximLocuriSecție(secție, secțiiActuale);
                }
                catch (NumberFormatException ignored) {
                    secțiiActuale.clear();
                }
                catch (Exception ignored) {}
            }
            else if (tipCăutare.equals(FilterTypesSecție.NrMinimLocuri)) {
                try {
                    secțiiActuale = filtrareNrMinimLocuriSecție(secție, secțiiActuale);
                }
                catch (NumberFormatException ignored) {
                    secțiiActuale.clear();
                }
                catch (Exception ignored) {}
            }
        }

        // filtrul 2
        if (!limbăDePredare.equals("orice limbă"))
            secțiiActuale = filtrareLimbaDePredare(limbăDePredare, secțiiActuale);

        // setarea rezultatului în tabel
        secțiiGăsite.addAll(secțiiActuale);
        lupaSearchSecție.setOpacity(0);
        recalculareNrSecțiiTabel();
    }

    @FXML
    private void ascundeListaSearchSecție() {
        secțiiGăsite.clear();
        listViewSearchSecție.setOpacity(0);
        lupaSearchSecție.setOpacity(1);
    }

    @FXML
    public void închideSearchSecție() {
        câmpSearchSecție.setText("");
        ascundeListaSearchSecție();
        choiceBoxLimbaDePredare.getSelectionModel().selectFirst();
        tabelSecții.setItems(secții);
        recalculareNrSecțiiTabel();
        x_searchSecție.setOpacity(0);
    }

    @FXML
    public void completareCâmpuri_SearchSecție(MouseEvent mouseEvent) {
//        listViewSearchSecție.getSelectionModel().selectedItemProperty().addListener(
//                (observable, oldValue, newValue)-> completareCâmpuriSecție(newValue)
//        );
    }

    @FXML
    public void ștergeSearchSecție(MouseEvent mouseEvent) {
        câmpSearchSecție.setText("");
        listViewSearchSecție.setOpacity(0);
        lupaSearchSecție.setOpacity(1);
    }


    @FXML
    public void handlerTextFielduri(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.ENTER))
            adaugăModificăSecție();

        if (checkBoxAutoID.isSelected())
            generareAutoID();
        activeazăDezactiveazăButoane();
    }

    /**
     * Utilizatorul a tastat ENTER pe un texfield al secției. Se adaugă/actualizează secția cu datele introduse, dacă se poate.
     */
    public void adaugăModificăSecție() {
        String id = câmpSecțieID.getText();
        String nume = câmpSecțieNume.getText();
        String limbaDePredare = câmpSecțieLimbaDePredare.getText();
        FormăDeFinanțare formăDeFinanțare = câmpSecțieFormaDeFinanțare.getSelectionModel().getSelectedItem();
        String nrLocuri = câmpSecțieNrLocuri.getText();
        try {
            // încercăm să modificăm secția
            Secție secție = modificăSecție(id, nume, limbaDePredare, formăDeFinanțare, nrLocuri);
            secțieSelectată = secțieService.findOne(id);
            afișareMesajSmart("Secția " + secție.toString() + "\na fost modificată cu succes.", true);
        } catch (Exception e) {
            try{
                // încercăm să adăugăm secția
                Secție secție = adaugăSecție(id, nume, limbaDePredare, formăDeFinanțare, nrLocuri);
                secțieSelectată = secțieService.findOne(id);
                afișareMesajSmart("Secția " + secție.toString() + "\na fost adăugată cu succes.", true);
            } catch (Exception e2) {
                afișareMesajEroare(e2.getMessage());
                afișareMesajSmart("Secția " + nume + " nu a fost adăugată/modificată.", false);
            }
        }
    }

    /**
     * Utilizatorul a tastat ceva într-un textfield al secției.
     * Butonul ADD      se activează dacă secție nu există.
     * Butonul UPDATE   se activează dacă i s-a modificat un câmp secției.
     * Butonul DELETE   se activează dacă există secția.
     * Butoanele se dezactivează în caz contrar.
     */
    private void activeazăDezactiveazăButoane() {
        String idSecție = câmpSecțieID.getText();
        if (idSecție.length() == 0) {
            disableButonAdaugă();
            disableButonModifică();
            disableButonȘterge();
            return;
        }

        try {
            // ADD + DELETE
            secțieService.findOne(idSecție);
            Secție secțiaTentată = secțieSelectată; //secțieService.findOne(idSecție);
            // => secția există
            disableButonAdaugă();
            enableButonȘterge();

            // UPDATE
            try {
                String nume = câmpSecțieNume.getText();
                String limbaDePredare = câmpSecțieLimbaDePredare.getText();
                FormăDeFinanțare formăDeFinanțare = câmpSecțieFormaDeFinanțare.getSelectionModel().getSelectedItem();
                Integer nrLocuri = Integer.valueOf(câmpSecțieNrLocuri.getText());
                Secție secție = new Secție(idSecție, nume, limbaDePredare, formăDeFinanțare, nrLocuri);
                if (secțiaTentată.equals(secție))
                    disableButonModifică();
                else
                    enableButonModifică();

            } catch (NumberFormatException ignored) {
                disableButonModifică();
            }

        } catch (Exception e) {
            // => secția nu există
            enableButonAdaugă();
            disableButonModifică();
            disableButonȘterge();
        }

    }

    private void actualizeazăSecțiaCurentă() {
        Secție s = tabelSecții.getSelectionModel().getSelectedItem();
        if (s != null)
            secțieSelectată = s;
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


    private void disableButonAdaugă() {
        butonSecțieAdaugă.setDisable(true);
    }
    private void disableButonModifică() {
        butonSecțieModifică.setDisable(true);
    }
    private void disableButonȘterge() {
        butonSecțieȘterge.setDisable(true);
    }
    private void enableButonAdaugă() {
        butonSecțieAdaugă.setDisable(false);
    }
    private void enableButonModifică() {
        butonSecțieModifică.setDisable(false);
    }
    private void enableButonȘterge() {
        butonSecțieȘterge.setDisable(false);
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
                    Thread.sleep(1000);

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



    /** Afișarea paginată a tabelului */

    @FXML
    public void changeTabel_afișarePaginată(ActionEvent actionEvent) {
        if (checkBoxAfișarePaginată.isSelected())
            setTabel_afișarePaginată();
        else
            setTabel_afișareNormală();
    }

    private void recalculareNrSecțiiTabel() {
        if (!checkBoxAfișarePaginată.isSelected()) {
            if (câmpSearchSecție.getText().length() > 0 || !choiceBoxLimbaDePredare.getSelectionModel().getSelectedItem().equals("orice limbă"))
                tabelSecții.setItems(secțiiGăsite);
            else
                tabelSecții.setItems(secții);
            return;
        }

        Double heightWindow = tabelSecții.getScene().getWindow().getHeight(); // 190 de pixeli de sus până la tabel
        Double heightTabel = tabelSecții.getHeight();
        //Integer înălțimeFereastră = heightWindow.intValue();
        Integer înălțimeTabel = heightTabel.intValue();
        Integer înălțimeLinie = 26;

        Integer nrLinii;
        nrLinii = înălțimeTabel / înălțimeLinie;
//        if (înălțimeTabel < înălțimeFereastră-190)
//            nrLinii = înălțimeTabel/înălțimeLinie;
//        else
//            nrLinii = (înălțimeFereastră - 190)/înălțimeLinie;

        if (nrLinii < 0)
            nrLinii = 0;

        Vector<Secție> secțieVector = new Vector<>();
        if (câmpSearchSecție.getText().length() > 0 || !choiceBoxLimbaDePredare.getSelectionModel().getSelectedItem().equals("orice limbă"))
            secțieVector.addAll(secțiiGăsite);
        else
            secțieVector.addAll(generalService.getSecții());

        setSecțiiÎnTabel(nrLinii, secțieVector);
        resizePaginationBar(nrLinii, secțieVector.size());
    }

    private void resizePaginationBar(Integer nrLinii, int nrSecții) {
        if (nrLinii == 0)
            nrLinii = 1;
        Integer ultimaPagină = nrSecții / nrLinii;

        if (nrSecții % nrLinii == 0)
            ultimaPagină--;             // secțiile intră fix pe toate paginile => nu se va mai crea o pagină nouă goală

        paginare.setPageCount(ultimaPagină + 1);
        paginare.setMaxPageIndicatorCount(ultimaPagină + 1);
    }

    private void setSecțiiÎnTabel(Integer nrSecții, Vector<Secție> secții) {
        Integer pagină = paginare.getCurrentPageIndex();
        Integer primul = pagină * nrSecții;
        Integer ultimul = primul + nrSecții;
        if (primul > secții.size())
            primul = secții.size();
        if (ultimul > secții.size())
            ultimul = secții.size();

        secțiiParțiale.clear();
        secțiiParțiale.addAll(secții.subList(primul, ultimul));
        tabelSecții.setItems(secțiiParțiale);
    }

    private void setTabel_afișarePaginată() {
        paginare.setDisable(false);
        paginare.setVisible(true);
        recalculareNrSecțiiTabel();
    }
    private void setTabel_afișareNormală() {
        paginare.setDisable(true);
        paginare.setVisible(false);
        reloadSecții();
        if (!checkBoxAfișarePaginată.isSelected()) {
            if (câmpSearchSecție.getText().length() > 0 || !choiceBoxLimbaDePredare.getSelectionModel().getSelectedItem().equals("orice limbă"))
                tabelSecții.setItems(secțiiGăsite);
            else
                tabelSecții.setItems(secții);
        }
    }

}
