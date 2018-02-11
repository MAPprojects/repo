package sample.ui.adminUI;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
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
import sample.domain.Candidat;
import sample.filterTypes.FilterTypesCandidat;
import sample.service.CandidatService;
import sample.service.GeneralService;
import sample.utils.CandidatEvent;
import sample.utils.Observer;

import java.util.Objects;
import java.util.Vector;

public class CandidatController implements Observer<CandidatEvent> {
    /** VARIABIBLE */
    private CandidatService candidatService;
    private GeneralService generalService;
    private ObservableList<Candidat> candidați;
    private ObservableList<Candidat> candidațiParțiali;
    private ObservableList<Candidat> candidațiGăsiți;
    private ObservableList<FilterTypesCandidat> opțiuniCăutareCandidat;
    private FilterTypesCandidat tipFiltru;
    private String filtru;
    private Candidat candidatSelectat;
    private Stage stage;
    @FXML
    public BorderPane mainBorderPane;
    // Tabel
    @FXML
    public TableView<Candidat> tabelCandidați;
    @FXML
    public TableColumn<Candidat,String> tabelCandidați_CNP;
    @FXML
    public TableColumn<Candidat,String> tabelCandidați_Nume;
    @FXML
    public TableColumn<Candidat,String> tabelCandidați_Prenume;
    @FXML
    public TableColumn<Candidat,String> tabelCandidați_Telefon;
    @FXML
    public TableColumn<Candidat,String> tabelCandidați_Email;
    @FXML
    public CheckBox checkBoxAfișarePaginată;
    @FXML
    public Pagination paginare;
    // Câmpuri + butoane
    @FXML
    public TextField câmpCandidatID;
    @FXML
    public TextField câmpCandidatNume;
    @FXML
    public TextField câmpCandidatPrenume;
    @FXML
    public TextField câmpCandidatTelefon;
    @FXML
    public TextField câmpCandidatEmail;
    @FXML
    public Button butonCandidatAdaugă;
    @FXML
    public Button butonCandidatModifică;
    @FXML
    public Button butonCandidatȘterge;
    // Search
    @FXML
    public TextField câmpSearchCandidat;
    @FXML
    public ListView<Candidat> listViewSearchCandidat;
    @FXML
    public ChoiceBox candidatSearchOptions;
    @FXML
    public ImageView lupaSearchCandidat;
    @FXML
    public ImageView x_searchCandidat;
    @FXML
    public VBox vBoxMesajeSmart;


    /** METODE */
    public CandidatController() {
        this.candidatService = new CandidatService("candidați.txt", "candidați.xml", "./src/sample/data/");
        candidatService.addObserver(this);
        this.generalService = new GeneralService();
        generalService.setCandidatService(candidatService);
        this.candidați = FXCollections.observableArrayList();
        this.candidațiGăsiți = FXCollections.observableArrayList();
        this.candidațiParțiali = FXCollections.observableArrayList();
        candidatSelectat = new Candidat();
        reloadCandidați();
    }

    public CandidatController(CandidatService candidatService) {
        this.candidatService = candidatService;
        candidatService.addObserver(this);
        this.generalService = new GeneralService();
        generalService.setCandidatService(candidatService);
        this.candidați = FXCollections.observableArrayList();
        this.candidațiGăsiți = FXCollections.observableArrayList();
        this.candidațiParțiali = FXCollections.observableArrayList();
        reloadCandidați();
    }

    @FXML
    private void initialize() {
        /* CANDIDAT */
        // tabel
        tabelCandidați_CNP.setCellValueFactory(new PropertyValueFactory<Candidat, String>("ID"));
        tabelCandidați_Nume.setCellValueFactory(new PropertyValueFactory<Candidat, String>("Nume"));
        tabelCandidați_Prenume.setCellValueFactory(new PropertyValueFactory<Candidat,String>("Prenume"));
        tabelCandidați_Telefon.setCellValueFactory(new PropertyValueFactory<Candidat, String>("Telefon"));
        tabelCandidați_Email.setCellValueFactory(new PropertyValueFactory<Candidat, String>("E_mail"));

        candidați = getCandidați();
        tabelCandidați.setItems(candidați);

        // lista de search
        listViewSearchCandidat.setOpacity(0);
        x_searchCandidat.setOpacity(0);
        candidațiGăsiți = getCandidațiCăutați();

        opțiuniCăutareCandidat = FXCollections.observableArrayList(
                FilterTypesCandidat.NUME, FilterTypesCandidat.PRENUME, FilterTypesCandidat.TELEFON, FilterTypesCandidat.EMAIL);
        candidatSearchOptions.setItems(opțiuniCăutareCandidat);
        candidatSearchOptions.getSelectionModel().selectFirst();
        candidatSearchOptions.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue)-> cautăCandidat()
        );

        // activarea listenerilor
        seteazăListeneri();

        disableButonAdaugă();
        disableButonModifică();
        disableButonȘterge();

        checkBoxAfișarePaginată.setSelected(true);
    }


    public BorderPane getMainBorderPane() {
        return mainBorderPane;
    }
//    public void setCandidatService(CandidatService candidatService) {
//        this.candidatService = candidatService;
//        candidatService.addObserver(this);
//    }
    public void setGeneralService(GeneralService generalService) {
        this.generalService = generalService;
        candidatService = generalService.getCandidatService();
        candidatService.addObserver(this);
    }

    private ObservableList<Candidat> getCandidați() {
        return candidați;
    }

    private ObservableList<Candidat> getCandidațiCăutați() {
        return candidațiGăsiți;
    }


    /**
     * Repopulează tabelul de candidați cu toți candidații din repository.
     */
    private void reloadCandidați() {
        Vector<Candidat> candidatVector = (Vector<Candidat>) candidatService.findAll();
        candidați.clear();
        candidatVector.forEach(candidat -> candidați.add(candidat));
    }


    private void adaugăCandidat(String cnp, String nume, String prenume, String telefon, String email) throws Exception {
        try {
            candidatSelectat = candidatService.add(new Candidat(cnp, nume, prenume, telefon, email));
        }
        catch (NumberFormatException e) { throw new NumberFormatException("ID-ul trebuie să fie număr."); }
        // excepțiile aruncate de service se vor prinde în View
    }

    private void modificăCandidat(String cnp, String nume, String prenume, String telefon, String email) throws Exception {
        try {
            //Integer id = Integer.valueOf(id_str);
            candidatSelectat = candidatService.update(new Candidat(cnp, nume, prenume, telefon, email));
        }
        catch (NumberFormatException e) { throw new NumberFormatException("ID-ul trebuie să fie număr."); }
        // excepțiile aruncate de service se vor prinde în View
    }

    private Candidat ștergeCandidat(String cnp) throws Exception {
        try {
            return generalService.ștergeCandidatSafe(cnp);
        }
        catch (NumberFormatException e) { throw new NumberFormatException("ID-ul trebuie să fie număr."); }
        // excepțiile aruncate de service se vor prinde în View
    }


    private void cautăNumeCandidat(String numeCandidat) {
        this.tipFiltru = FilterTypesCandidat.NUME;
        this.filtru = numeCandidat;

        Vector<Candidat> candidațiVector = candidatService.filtrare_nume(numeCandidat);
        candidațiGăsiți.clear();
        candidațiGăsiți.addAll(candidațiVector);
    }

    private void cautăPrenumeCandidat(String prenumeCandidat) {
        this.tipFiltru = FilterTypesCandidat.PRENUME;
        this.filtru = prenumeCandidat;

        Vector<Candidat> candidațiVector = candidatService.filtrare_prenume(prenumeCandidat);
        candidațiGăsiți.clear();
        candidațiGăsiți.addAll(candidațiVector);
    }

    private void filtrareTelefonCandidat(String telefonCandidat) {
        this.tipFiltru = FilterTypesCandidat.TELEFON;
        this.filtru = telefonCandidat;

        Vector<Candidat> candidațiVector = candidatService.filtrare_telefon(telefonCandidat);
        candidațiGăsiți.clear();
        candidațiGăsiți.addAll(candidațiVector);
    }

    private void filtrareEmailCandidat(String emailCandidat) {
        this.tipFiltru = FilterTypesCandidat.EMAIL;
        this.filtru = emailCandidat;
        Vector<Candidat> candidațiVector = candidatService.filtrare_email(emailCandidat);
        candidațiGăsiți.clear();
        candidațiGăsiți.addAll(candidațiVector);
    }


    @Override
    public void notifyOnEvent(CandidatEvent event) {
        reloadCandidați();
        recalculareNrCandidațiTabel();
        if (tipFiltru == FilterTypesCandidat.NUME)
            cautăNumeCandidat(filtru);
        if (tipFiltru == FilterTypesCandidat.PRENUME)
            cautăPrenumeCandidat(filtru);
        if (tipFiltru == FilterTypesCandidat.TELEFON)
            filtrareTelefonCandidat(filtru);
        if (tipFiltru == FilterTypesCandidat.EMAIL)
            filtrareEmailCandidat(filtru);
        // pentru optimizare, am putea verifica tipul de eveniment și să actualizăm doar elementul adăugat/șters/modificat

        activeazăDezactiveazăButoane();
    }


    private void afișareMesajEroare(String eroare) {
        Alert mesaj = new Alert(Alert.AlertType.ERROR);
        mesaj.setTitle("Eroare");
        mesaj.setContentText(eroare);
        mesaj.showAndWait();
    }


    private void seteazăListeneri() {
        /* candidat */
        listViewSearchCandidat.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue)-> completareCâmpuriCandidat(newValue)
        );
        tabelCandidați.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue)-> {
                    completareCâmpuriCandidat(newValue);
                    actualizeazăCandidatCurent();
                    activeazăDezactiveazăButoane();
                }
        );

        paginare.currentPageIndexProperty().addListener((observable, oldValue, newValue) -> recalculareNrCandidațiTabel());
    }

    /**
     * A se apela această funcție după afișarea ferestrei.
     */
    public void modificăriFereastră() {
        /* redimensionarea fereastrei */
        stage = (Stage) tabelCandidați.getScene().getWindow();
        stage.heightProperty().addListener((obs, oldVal, newVal) -> {
            recalculareNrCandidațiTabel();
        });

        stage.maximizedProperty().addListener((obs, oldVal, newVal) -> {
            Thread thread = new Thread() {
                public void run() {
                    try { Thread.sleep(100); }
                    catch (InterruptedException ignored) {}
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            recalculareNrCandidațiTabel();
                        }
                    });
                }
            };
            thread.start();
        });

        stage.setMinHeight(250);

        recalculareNrCandidațiTabel();
    }

    private void completareCâmpuriCandidat(Candidat candidat) {
        if (candidat == null)
            return;

        câmpCandidatID.setText(String.valueOf(candidat.getID()));
        câmpCandidatNume.setText(candidat.getNume());
        câmpCandidatPrenume.setText(candidat.getPrenume());
        câmpCandidatTelefon.setText(candidat.getTelefon());
        câmpCandidatEmail.setText(candidat.getE_mail());
    }

    @FXML
    public void adaugăCandidat(ActionEvent actionEvent) {
        adaugăCandidat();
        activeazăDezactiveazăButoane();
    }
    private void adaugăCandidat() {
        String id = câmpCandidatID.getText();
        String nume = câmpCandidatNume.getText();
        String prenume = câmpCandidatPrenume.getText();
        String telefon = câmpCandidatTelefon.getText();
        String email = câmpCandidatEmail.getText();
        try {
            adaugăCandidat(id,nume,prenume,telefon,email);
            afișareMesajSmart("Candidatul " + nume + " " + prenume + " a fost adăugat cu succes.", true);
        } catch (Exception e) {
            afișareMesajEroare(e.getMessage());
            afișareMesajSmart("Candidatul " + nume + " " + prenume + " nu a fost adăugat.", false);
        }
        activeazăDezactiveazăButoane();
    }

    @FXML
    public void modificăCandidat(ActionEvent actionEvent) {
        modificăCandidat();
    }
    private void modificăCandidat() {
        String id = câmpCandidatID.getText();
        String nume = câmpCandidatNume.getText();
        String prenume = câmpCandidatPrenume.getText();
        String telefon = câmpCandidatTelefon.getText();
        String email = câmpCandidatEmail.getText();

        try {
            modificăCandidat(id, nume, prenume, telefon, email);
            try {candidatSelectat = new Candidat(id,nume,prenume,telefon,email); } catch (Exception ignored) {}
            activeazăDezactiveazăButoane();
            afișareMesajSmart("Candidatul " + nume + " " + prenume + " a fost modificat cu succes.", true);
        }
        catch (Exception e) {
            afișareMesajEroare(e.getMessage());
            try {
                Candidat candidat = candidatService.findOne(id);
                afișareMesajSmart("Candidatul " + candidat.toString() + " nu a fost modificat.", false);
            }
            catch (Exception ignored) {
                afișareMesajSmart("Candidatul " + nume + " " + prenume + " nu a fost modificat.", false);
            }
        }
    }

    @FXML
    public void ștergeCandidat(ActionEvent actionEvent) {
        ștergeCandidat();
    }
    private void ștergeCandidat() {
        String id = câmpCandidatID.getText();

        try {
            Candidat candidat = ștergeCandidat(id);
            afișareMesajSmart("Candidatul " + candidat.toString() + " a fost șters cu succes.", true);
        }
        catch (Exception e) {
            afișareMesajEroare(e.getMessage());
            try {
                Candidat candidat = candidatService.findOne(id);
                afișareMesajSmart("Candidatul " + candidat.toString() + " nu a fost șters.", false);
            }
            catch (Exception ignored) {
                String nume = câmpCandidatNume.getText();
                String prenume = câmpCandidatPrenume.getText();
                afișareMesajSmart("Candidatul " + nume + " " + prenume + " nu a fost șters.", false);
            }
        }
    }


    @FXML
    public void cautăCandidat(Event inputMethodEvent) {
        cautăCandidat();
    }
    public void cautăCandidat() {
        candidațiGăsiți.clear();
        lupaSearchCandidat.setOpacity(0);
        x_searchCandidat.setOpacity(0.5);
        String candidat = câmpSearchCandidat.getText();
        if (candidat.length() == 0) {
            ascundeListaSearchCandidat();   // nu mai e necesar decât pentru lupă
            x_searchCandidat.setOpacity(0);
            recalculareNrCandidațiTabel();
            return;
        }

        FilterTypesCandidat tipCăutare = (FilterTypesCandidat) candidatSearchOptions.getSelectionModel().getSelectedItem();
        if (tipCăutare.equals(FilterTypesCandidat.NUME)) {
            cautăNumeCandidat(candidat);
        }
        if (tipCăutare.equals(FilterTypesCandidat.PRENUME)) {
            cautăPrenumeCandidat(candidat);
        }
        if (tipCăutare.equals(FilterTypesCandidat.TELEFON)) {
            filtrareTelefonCandidat(candidat);
        }
        if (tipCăutare.equals(FilterTypesCandidat.EMAIL)) {
            filtrareEmailCandidat(candidat);
        }

        lupaSearchCandidat.setOpacity(0);
        tabelCandidați.setItems(candidațiGăsiți);
        recalculareNrCandidațiTabel();
    }


    @FXML
    private void ascundeListaSearchCandidat() {
        candidațiGăsiți.clear();
        listViewSearchCandidat.setOpacity(0);
        lupaSearchCandidat.setOpacity(1);
    }

    @FXML
    public void închideSearchCandidat() {
        câmpSearchCandidat.setText("");
        ascundeListaSearchCandidat();
        recalculareNrCandidațiTabel();
        x_searchCandidat.setOpacity(0);
    }

    @FXML
    public void completareCâmpuri_SearchCandidat(MouseEvent mouseEvent) {
//        listViewSearchCandidat.getSelectionModel().selectedItemProperty().addListener(
//                (observable, oldValue, newValue)-> completareCâmpuriCandidat(newValue)
//        );
    }

    @FXML
    public void ștergeSearchCandidat(MouseEvent mouseEvent) {
        câmpSearchCandidat.setText("");
        listViewSearchCandidat.setOpacity(0);
        lupaSearchCandidat.setOpacity(1);
    }


    @FXML
    public void handlerTextFielduri(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.ENTER))
            adaugăModificăCandidat();
        activeazăDezactiveazăButoane();
    }

    /**
     * Utilizatorul a tastat ENTER pe un texfield al candidatului. Se adaugă/actualizează candidatul cu datele introduse, dacă se poate.
     */
    private void adaugăModificăCandidat() {
        String cnp = câmpCandidatID.getText();
        String nume = câmpCandidatNume.getText();
        String prenume = câmpCandidatPrenume.getText();
        String telefon = câmpCandidatTelefon.getText();
        String email = câmpCandidatEmail.getText();
        try {
            // încercăm să modificăm candidatul
             modificăCandidat(cnp,nume,prenume,telefon,email);
             candidatSelectat = candidatService.findOne(cnp);
            afișareMesajSmart("Candidatul " + nume + " " + prenume + " a fost modificat cu succes.", true);
        } catch (Exception e) {
            try{
                // încercăm să adăugăm candidatul
                adaugăCandidat(cnp,nume,prenume,telefon,email);
                candidatSelectat = candidatService.findOne(cnp);
                afișareMesajSmart("Candidatul " + nume + " " + prenume + " a fost adăugat cu succes.", true);
            } catch (Exception e2) {
                afișareMesajEroare(e2.getMessage());
                afișareMesajSmart("Candidatul " + nume + " " + prenume + " nu a fost adăugat/modificat.", false);
            }
        }
    }

    /**
     * Utilizatorul a tastat ceva într-un textfield al candidatului.
     * Butonul ADD      se activează dacă candidatul nu există.
     * Butonul UPDATE   se activează dacă i s-a modificat un câmp candidatului.
     * Butonul DELETE   se activează dacă există candidatul.
     * Butoanele se dezactivează în caz contrar.
     */
    private void activeazăDezactiveazăButoane() {
        try {
            //Integer idCandidat = Integer.valueOf(câmpCandidatID.getText());
            String cnp = câmpCandidatID.getText();
            if (cnp.equals(""))
                throw new NumberFormatException();  // doar ca să fie prinsă mai jos

            try {
                // ADD + DELETE
                candidatService.findOne(cnp);
                // => candidatul există
                disableButonAdaugă();
                enableButonȘterge();

                // UPDATE
                String nume = câmpCandidatNume.getText();
                String prenume = câmpCandidatPrenume.getText();
                String telefon = câmpCandidatTelefon.getText();
                String email = câmpCandidatEmail.getText();
                Candidat candidat = new Candidat(cnp, nume, prenume, telefon, email);
                if (candidatSelectat.equals(candidat))
                    disableButonModifică();
                else
                    enableButonModifică();

            } catch (Exception e) {
                // => candidatul nu există
                enableButonAdaugă();
                disableButonModifică();
                disableButonȘterge();
            }

        } catch (NumberFormatException e) {
            // => ID-ul introdus nu e număr
            disableButonAdaugă();
            disableButonModifică();
            disableButonȘterge();
        }
    }

    private void actualizeazăCandidatCurent() {
        Candidat c = tabelCandidați.getSelectionModel().getSelectedItem();
        if (c != null)
            candidatSelectat = c;
    }

    @FXML
    public void ascundeLupaCandidat(MouseEvent mouseEvent) {
        lupaSearchCandidat.setOpacity(0);
    }

    @FXML
    public void aratăLupaCandidat(MouseEvent mouseEvent) {
        if (Objects.equals(câmpSearchCandidat.getText(), ""))
            lupaSearchCandidat.setOpacity(1);
    }

    @FXML
    public void setOpacityXcandidat_100(MouseEvent mouseEvent) {
        if (!Objects.equals(câmpSearchCandidat.getText(), ""))
            x_searchCandidat.setOpacity(1);
    }

    @FXML
    public void setOpacityXcandidat_50(MouseEvent mouseEvent) {
        if (!Objects.equals(câmpSearchCandidat.getText(), ""))
            x_searchCandidat.setOpacity(0.5);
    }

    private void disableButonAdaugă() {
        butonCandidatAdaugă.setDisable(true);
    }
    private void disableButonModifică() {
        butonCandidatModifică.setDisable(true);
    }
    private void disableButonȘterge() {
        butonCandidatȘterge.setDisable(true);
    }
    private void disableCâmpID() { câmpCandidatID.setDisable(true); }
    private void enableButonAdaugă() {
        butonCandidatAdaugă.setDisable(false);
    }
    private void enableButonModifică() {
        butonCandidatModifică.setDisable(false);
    }
    private void enableButonȘterge() {
        butonCandidatȘterge.setDisable(false);
    }
    private void enableCâmpID() { câmpCandidatID.setDisable(false); }


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

    private void recalculareNrCandidațiTabel() {
        // verific dacă utilizatorul a selectat butonul de afișare paginată
        if (!checkBoxAfișarePaginată.isSelected()) {
            if (câmpSearchCandidat.getText().length() > 0)
                tabelCandidați.setItems(candidațiGăsiți);
            else
                tabelCandidați.setItems(candidați);
            return;
        }

        Double heightWindow = tabelCandidați.getScene().getWindow().getHeight(); // 190 de pixeli de sus până la tabel
        Double heightTabel = tabelCandidați.getHeight();
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

        Vector<Candidat> candidatVector = new Vector<>();
        if (câmpSearchCandidat.getText().length() > 0)
            candidatVector.addAll(candidațiGăsiți);
        else
            candidatVector.addAll(generalService.getCandidați());

        setCandidațiÎnTabel(nrLinii, candidatVector);
        resizePaginationBar(nrLinii, candidatVector.size());
    }

    private void resizePaginationBar(Integer nrLinii, int nrCandidați) {
        if (nrLinii == 0)
            nrLinii = 1;
        Integer ultimaPagină = nrCandidați / nrLinii;

        if (nrCandidați % nrLinii == 0)
            ultimaPagină--;             // candidații intră fix pe toate paginile => nu se va mai crea o pagină nouă goală

        paginare.setPageCount(ultimaPagină + 1);
        paginare.setMaxPageIndicatorCount(ultimaPagină + 1);
    }

    private void setCandidațiÎnTabel(Integer nrCandidați, Vector<Candidat> candidați) {
        Integer pagină = paginare.getCurrentPageIndex();
        Integer primul = pagină * nrCandidați;
        Integer ultimul = primul + nrCandidați;
        if (primul > candidați.size())
            primul = candidați.size();
        if (ultimul > candidați.size())
            ultimul = candidați.size();

        candidațiParțiali.clear();
        candidațiParțiali.addAll(candidați.subList(primul, ultimul));
        tabelCandidați.setItems(candidațiParțiali);
    }

    private void setTabel_afișarePaginată() {
        paginare.setDisable(false);
        paginare.setVisible(true);
        recalculareNrCandidațiTabel();
    }
    private void setTabel_afișareNormală() {
        paginare.setDisable(true);
        paginare.setVisible(false);
        reloadCandidați();
        if (!checkBoxAfișarePaginată.isSelected()) {
            if (câmpSearchCandidat.getText().length() > 0)
                tabelCandidați.setItems(candidațiGăsiți);
            else
                tabelCandidați.setItems(candidați);
        }
    }


}
