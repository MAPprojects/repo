package sample.ui.userUI;


import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import sample.domain.Candidat;
import sample.domain.FormăDeFinanțare;
import sample.domain.Opțiune;
import sample.domain.Secție;
import sample.service.GeneralService;
import sample.service.OpțiuneService;
import sample.utils.Observer;
import sample.utils.OpțiuneEvent;

import java.util.Vector;

public class UserController implements Observer<OpțiuneEvent> {
    private Candidat candidat;
    private OpțiuneService opțiuneService;
    private GeneralService generalService;
    private ObservableList<Secție> toateSecțiile;
    private ObservableList<Secție> secțiiAlese;
    private ObservableList<Secție> secțiiGăsite;
    private ObservableList<String> limbiDePredare;
    private ObservableList<Integer> prioritățiSecție;
    private ObservableList<Secție> secțiiParțiale;
    private ObservableList<PieChart.Data> pieChartData;
    private Secție secțieSelectată;
    private Secție secțieSelectată_dinToateSecțiile;
    private Secție secțieSelectată_dinSecțiiAlese;
    private boolean modificDinCod;
    private Stage stage;

    @FXML public TextField câmpSearchSecție;
    @FXML public ImageView lupaSearchSecție;
    @FXML public ImageView x_searchSecție;
    @FXML public ChoiceBox<String> choiceBoxLimbaDePredare;
    @FXML public TableView<Secție> tabelToateSecțiile;
    @FXML public TableColumn<Secție,String> tabelToateSecțiile_ID;
    @FXML public TableColumn<Secție,String> tabelToateSecțiile_Nume;
    @FXML public TableColumn<Secție,String> tabelToateSecțiile_LimbaDePredare;
    @FXML public TableColumn<Secție,FormăDeFinanțare> tabelToateSecțiile_FormaDeFinanțare;
    @FXML public TableView<Secție> tabelSecțiiAlese;
    @FXML public TableColumn<Secție,Integer> tabelSecțiiAlese_prioritate;
    @FXML public TableColumn<Secție,String> tabelSecțiiAlese_ID;
    @FXML public TableColumn<Secție,String> tabelSecțiiAlese_nume;
    @FXML public TableColumn<Secție,String> tabelSecțiiAlese_limbaPredare;
    @FXML public TableColumn<Secție,FormăDeFinanțare> tabelSecțiiAlese_formaFinanțare;
    @FXML public TableColumn<Secție,Integer> tabelSecțiiAlese_nrLocuri;
    @FXML public HBox hBoxSecțieSelectată;
    @FXML public TextField câmpSecție_ID;
    @FXML public TextField câmpSecție_nume;
    @FXML public TextField câmpSecție_limbaDePredare;
    @FXML public TextField câmpSecție_FormaDeFinanțare;
    @FXML public TextField câmpSecție_nrLocuri;
    @FXML public ChoiceBox choiceBoxPrioritateSecție;
    @FXML public Button butonAdaugăSecție;
    @FXML public Button butonȘtergeSecție;
    @FXML public VBox vBoxMesajeSmart;
    @FXML public PieChart pieChart_optăriSecție;
    @FXML private Label labelNrOptări;
    @FXML public Pagination paginare;
    @FXML public CheckBox checkBoxAfișarePaginată;


    public UserController() {
        candidat = new Candidat();
        opțiuneService = new OpțiuneService("opțiuni.txt", "opțiuni.xml", "./src/sample/data/", "log_Opțiuni.txt");
        opțiuneService.addObserver(this);
        generalService = new GeneralService(opțiuneService);

        toateSecțiile = FXCollections.observableArrayList();
        secțiiAlese = FXCollections.observableArrayList();
        secțiiGăsite = FXCollections.observableArrayList();
        prioritățiSecție = FXCollections.observableArrayList();
        pieChartData = FXCollections.observableArrayList();
        secțiiParțiale = FXCollections.observableArrayList();
        limbiDePredare = FXCollections.observableArrayList();
        modificDinCod = false;
    }

    public void setCandidat(Candidat candidat) {
        this.candidat = candidat;
        reloadSecțiiAlese();
        if (secțiiAlese.size() == 0) {
            hBoxSecțieSelectată.setStyle("-fx-background-color:   rgb(0, 27, 35)");
            coloreazăButoane("-fx-background-color: rgb(0, 71, 67)");
        }
    }
    public void setOpțiuneService(OpțiuneService opțiuneService) {
        this.opțiuneService = opțiuneService;
        opțiuneService.addObserver(this);
        reloadToateSecțiile();
        reloadSecțiiAlese();
    }
    public void setGeneralService(GeneralService generalService) {
        this.generalService = generalService;
        opțiuneService = generalService.getOpțiuneService();
        opțiuneService.addObserver(this);
        generalService.getSecțieService().addUserController(this);
        reloadToateSecțiile();
        reloadSecțiiAlese();
    }

    @FXML
    private void initialize(){
        // search
        limbiDePredare = FXCollections.observableArrayList();
        reloadListaLimbilor();

        // tabelul cu toate secțiile
        tabelToateSecțiile_ID.setCellValueFactory(new PropertyValueFactory<Secție,String>("ID"));
        tabelToateSecțiile_Nume.setCellValueFactory(new PropertyValueFactory<Secție,String>("Nume"));
        tabelToateSecțiile_LimbaDePredare.setCellValueFactory(new PropertyValueFactory<Secție,String>("LimbaDePredare"));
        tabelToateSecțiile_FormaDeFinanțare.setCellValueFactory(new PropertyValueFactory<Secție,FormăDeFinanțare>("FormăDeFinanțare"));
        reloadToateSecțiile();
        tabelToateSecțiile.setItems(toateSecțiile);

        // tabelul cu secțiile alese
        tabelSecțiiAlese_prioritate.setCellValueFactory(column-> new ReadOnlyObjectWrapper<Integer>(tabelSecțiiAlese.getItems().indexOf(column.getValue()) + 1));
        tabelSecțiiAlese_ID.setCellValueFactory(new PropertyValueFactory<Secție,String>("ID"));
        tabelSecțiiAlese_nume.setCellValueFactory(new PropertyValueFactory<Secție,String>("Nume"));
        tabelSecțiiAlese_limbaPredare.setCellValueFactory(new PropertyValueFactory<Secție,String>("LimbaDePredare"));
        tabelSecțiiAlese_formaFinanțare.setCellValueFactory(new PropertyValueFactory<Secție,FormăDeFinanțare>("FormăDeFinanțare"));
        tabelSecțiiAlese_nrLocuri.setCellValueFactory(new PropertyValueFactory<Secție,Integer>("NrLocuri"));
        reloadSecțiiAlese();
        tabelSecțiiAlese.setItems(secțiiAlese);

        // choice box prioritate
        choiceBoxPrioritateSecție.setItems(prioritățiSecție);

        seteazăListeneri();

        disableButonAdaugă();
        disableButonȘterge();
        x_searchSecție.setOpacity(0);
        checkBoxAfișarePaginată.setSelected(true);
    }

    private void seteazăListeneri() {
        choiceBoxLimbaDePredare.getSelectionModel().selectedItemProperty().addListener(
                ((observable, oldValue, newValue) -> {
                    cautăSecții();
                })
        );

        tabelToateSecțiile.getSelectionModel().selectedItemProperty().addListener(
                ((observable, oldValue, newValue) -> handlerTabelToateSecțiile())
        );
        tabelSecțiiAlese.getSelectionModel().selectedItemProperty().addListener(
                ((observable, oldValue, newValue) -> handlerTabelSecțiiAlese())
        );

        choiceBoxPrioritateSecție.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (modificDinCod || secțieSelectată == null)
                        return;
                    modificăPrioritateaSecției();
                    activeazăDezactiveazăButoane();
                }
        );

        paginare.currentPageIndexProperty().addListener((observable, oldValue, newValue) -> recalculareNrToateSecțiiTabel());
    }

    /**
     * A se apela această funcție după afișarea ferestrei.
     */
    public void modificăriFereastră() {
        /* redimensionarea fereastrei */
        stage = (Stage) tabelToateSecțiile.getScene().getWindow();
        stage.heightProperty().addListener((obs, oldVal, newVal) -> {
            recalculareNrToateSecțiiTabel();
        });

        stage.maximizedProperty().addListener((obs, oldVal, newVal) -> {
            Thread thread = new Thread() {
                public void run() {
                    try { Thread.sleep(100); }
                    catch (InterruptedException ignored) {}
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            recalculareNrToateSecțiiTabel();
                        }
                    });
                }
            };
            thread.start();
        });

        stage.setMinHeight(600);

        recalculareNrToateSecțiiTabel();

        if (generalService.getContService().contNou(candidat.getID()))
            afișareHelp();

        if (generalService.getContService().contNouDefault(candidat.getID()))
            afișareMesajSchimbăParola();
    }


    @FXML
    public void afișareHelp(ActionEvent actionEvent) {
        afișareHelp();
    }
    private void afișareHelp() {
        afișareMesaj("Bine ai venit, " + candidat.getPrenume() + " ! \n"
                + "Te rugăm să îți alegi secțiile la care dorești să te înscri și apoi să printezi alegerea făcută pentru a o aduce la secretariat. \n\n"
                + "Secțiile disponibile le găsești în tabelul din stânga, iar în tabelul din dreapta sunt secțile alese de tine. \n"
                + "Te rugăm să fi atent la ordinea de setare a priorităților. \n\n"
                + "Pentru a printa opțiunile, dă clic pe butonul „Unelte” din bara de meniu.", true);
    }
    private void afișareMesajSchimbăParola() {
        afișareMesaj("Din motive de securitate, te rugăm să îți schimbi parola ce ți-a fost generată automat.\n\n"
                + "Pentru a face acest lucru, intră în „Contul meu” din bara meniului, apoi la „Date personale”.\n", true);
    }

    private void reloadToateSecțiile() {
        Vector<Secție> secțieVector = generalService.getSecții();
        toateSecțiile.clear();
        toateSecțiile.addAll(secțieVector);
        tabelToateSecțiile.setItems(toateSecțiile);
    }

    private void reloadSecțiiAlese() {
        Vector<Secție> secțieVector = generalService.getSecțiiAlese(candidat.getID());
        secțiiAlese.clear();
        secțiiAlese.addAll(secțieVector);
        tabelSecțiiAlese.setItems(secțiiAlese);
    }


    private void completareCâmpuriSecție(Secție secție) {
        if (secție == null) {
            câmpSecție_ID.setText("");
            câmpSecție_nume.setText("");
            câmpSecție_limbaDePredare.setText("");
            câmpSecție_FormaDeFinanțare.setText("");
            câmpSecție_nrLocuri.setText("");
            return;
        }
        câmpSecție_ID.setText(String.valueOf(secție.getID()));
        câmpSecție_nume.setText(secție.getNume());
        câmpSecție_limbaDePredare.setText(secție.getLimbaDePredare());
        câmpSecție_FormaDeFinanțare.setText(secție.getFormăDeFinanțare().toString());
        câmpSecție_nrLocuri.setText(String.valueOf(secție.getNrLocuri()));
    }

    private void adaugăSecție() {
        try {
            generalService.adaugăOpțiune(candidat.getID(), secțieSelectată.getID());
            afișareMesajSmart("Te-ai înscris la secția " + secțieSelectată.toString() + ".", true);
        }
        catch (Exception e) {
            afișareMesajSmart(e.getMessage(), false);
        }
    }

    private void ștergeSecție() {
        try {
            generalService.ștergeOpțiune(candidat.getID(), secțieSelectată.getID());
            afișareMesajSmart("Ai renunțat la secția " + secțieSelectată.toString() + ".", true);
            prioritățiSecție.clear();
        }
        catch (Exception e) {
            afișareMesajSmart(e.getMessage(), false);
        }
    }

    private void actualizeazăPrioritatea(Secție secție) {
        modificDinCod = true;
        try {
            Opțiune opțiune = generalService.getOpțiune(candidat.getID());
            Integer prioritate = generalService.getPrioritate(candidat.getID(), secție.getID());

            prioritățiSecție.clear();
            for (int i = 1; i <= opțiune.getIdSecții().size(); i++)
                prioritățiSecție.add(i);

            choiceBoxPrioritateSecție.setItems(prioritățiSecție);
            choiceBoxPrioritateSecție.getSelectionModel().select(prioritate-1);
        }
        catch (Exception e) {
            prioritățiSecție.clear();
        }
        modificDinCod = false;
    }

    public void modificăPrioritateaSecției() {
        if (secțieSelectată == null) {
            afișareMesajSmart("Selectează o secție.", false);
            return;
        }

        Integer prioritate = (Integer) choiceBoxPrioritateSecție.getSelectionModel().getSelectedItem();
        if (prioritate == null) {
            afișareMesajSmart("Nu ești înscris la această secție.", false);
            return;
        }

        try {
            // verificăm dacă chiar a fost schimbată prioritatea
            if (prioritate.equals(generalService.getPrioritate(candidat.getID(), secțieSelectată.getID())))
                return;

            // prioritatea a fost schimbată
            generalService.modificăOpțiune_Prioritate(candidat.getID(), secțieSelectată.getID(), prioritate);
            afișareMesajSmart("Prioritatea secției " + secțieSelectată.getNume() + "\n"
                    + "a fost schimbată cu succes la " + prioritate + ".", true);
        }
        catch (Exception e) {
            afișareMesajSmart(e.getMessage(), false);
            //afișareMesajSmart("Prioritatea secției " + secțieSelectată.getNume() + " nu a fost schimbată.", false);
        }
    }

    private void reloadPieChart(Secție secție) {
        pieChartData.clear();
        labelNrOptări.setText("");
        if (secție == null)
            return;

        Integer nrPriorități = 3;
        if (nrPriorități <= secțiiAlese.size())
            nrPriorități = secțiiAlese.size() + 1;
        for (int i = 1; i <= nrPriorități; i++) {
            Integer nrOptări = generalService.getNrOptăriPrioritate(secție.getID(), i);
            if (nrOptări > 0)
                pieChartData.add(new PieChart.Data("Prioritatea " + i, nrOptări));
        }
        pieChart_optăriSecție.setData(pieChartData);

        // setez listener pentru a afișa numărul de alegeri ale secției atunci când mausul este pe pie chart
        for (PieChart.Data data : pieChart_optăriSecție.getData()) {
            data.getNode().addEventHandler(MouseEvent.MOUSE_ENTERED,
                    new EventHandler<MouseEvent>() {
                        @Override public void handle(MouseEvent e) {
//                            labelNrOptări.setTranslateX(e.getSceneX());
//                            labelNrOptări.setTranslateY(e.getSceneY());

                            Double nrOptări_d = data.getPieValue();
                            Integer nrOptări = Integer.valueOf(nrOptări_d.intValue());
                            if (nrOptări == 1)
                                labelNrOptări.setText(nrOptări + " optare");
                            else
                                labelNrOptări.setText(nrOptări + " optări");
                        }
                    });
            data.getNode().addEventHandler(MouseEvent.MOUSE_EXITED,
                    new EventHandler<MouseEvent>() {
                        @Override public void handle(MouseEvent e) {
                            labelNrOptări.setText("");
                        }
                    });
        }
    }

    private void actualizeazăSecția(Secție secție) {
        if (secție == null)
            return;

        secțieSelectată = secție;
        completareCâmpuriSecție(secție);
        actualizeazăPrioritatea(secție);
        activeazăDezactiveazăButoane();
        reloadPieChart(secție);
    }

    /**
     * Utilizatorul a dat clic pe o secție din tabelul cu toate secțiile.
     */
    @FXML
    public void handlerTabelToateSecțiile(MouseEvent mouseEvent) {
        // avem setat un listener, așa că apelăm funcția doar dacă s-a dat clic pe o secție deja selectată
        Secție secție = tabelToateSecțiile.getSelectionModel().getSelectedItem();
        if (secție != null && secție.equals(secțieSelectată_dinToateSecțiile))
            handlerTabelToateSecțiile();
    }
    public void handlerTabelToateSecțiile() {
        if (modificDinCod)
            return;
        Secție secție = tabelToateSecțiile.getSelectionModel().getSelectedItem();
        actualizeazăSecția(secție);
        secțieSelectată_dinToateSecțiile = secție;
    }

    /**
     * Utilizatorul a dat clic pe o secție din tabelul cu secțiile lui.
     */
    @FXML
    public void handlerTabelSecțiiAlese(MouseEvent mouseEvent) {
        // avem setat un listener, așa că apelăm funcția doar dacă s-a dat clic pe o secție deja selectată
        Secție secție = tabelSecțiiAlese.getSelectionModel().getSelectedItem();
        if (secție != null && secție.equals(secțieSelectată_dinSecțiiAlese))
            handlerTabelSecțiiAlese();
    }
    public void handlerTabelSecțiiAlese() {
        Secție secție = tabelSecțiiAlese.getSelectionModel().getSelectedItem();
        actualizeazăSecția(secție);
        secțieSelectată_dinSecțiiAlese = secție;
    }

    @FXML
    public void modificăSecție(MouseEvent mouseEvent) {
        modificăPrioritateaSecției();
    }

    @FXML
    public void adaugăSecție(ActionEvent actionEvent) {
        adaugăSecție();
    }

    @FXML
    public void ștergeSecție(ActionEvent actionEvent) {
        ștergeSecție();
    }


    @FXML
    public void cautăSecții(KeyEvent keyEvent) {
        cautăSecții();
    }
    public void cautăSecții() {
        secțiiGăsite.clear();
        lupaSearchSecție.setOpacity(0);
        x_searchSecție.setOpacity(0.5);

        String numeSecție = câmpSearchSecție.getText();
        String limbăDePredare = choiceBoxLimbaDePredare.getSelectionModel().getSelectedItem();
        if (limbăDePredare == null)
            return;
        if (numeSecție.length() == 0 && limbăDePredare.equals("orice limbă")) {
            lupaSearchSecție.setOpacity(1);
            x_searchSecție.setOpacity(0);
            recalculareNrToateSecțiiTabel();
            return;
        }

        Vector<Secție> secțiiActuale = new Vector<>(toateSecțiile);

        // filtrul 1
        if (numeSecție.length() > 0) {
            secțiiActuale = generalService.filtrareSecții_nume(numeSecție, secțiiActuale);
        }

        if (!limbăDePredare.equals("orice limbă"))
            secțiiActuale = generalService.filtrareSecții_limbaDePredare(limbăDePredare, secțiiActuale);

        secțiiGăsite.addAll(secțiiActuale);
        recalculareNrToateSecțiiTabel();
    }

    @FXML
    public void ascundeLupa(MouseEvent mouseEvent) {
        lupaSearchSecție.setOpacity(0);
    }

    @FXML
    public void aratăLupa(MouseEvent mouseEvent) {
        if (câmpSearchSecție.getText().length() == 0)
            lupaSearchSecție.setOpacity(1);
    }

    public void închideSearch(MouseEvent mouseEvent) {
        câmpSearchSecție.setText("");
        choiceBoxLimbaDePredare.getSelectionModel().selectFirst();
        secțiiGăsite.clear();
        x_searchSecție.setOpacity(0);
        lupaSearchSecție.setOpacity(1);
        recalculareNrToateSecțiiTabel();
    }

    @FXML
    public void setOpacityX_100(MouseEvent mouseEvent) {
        if (câmpSearchSecție.getText().length() > 0)
            x_searchSecție.setOpacity(1);
    }

    @FXML
    public void setOpacity_50(MouseEvent mouseEvent) {
        if (câmpSearchSecție.getText().length() > 0)
            x_searchSecție.setOpacity(0.5);
    }

    private void disableButonAdaugă() {
        butonAdaugăSecție.setDisable(true);
    }
    private void disableButonȘterge() {
        butonȘtergeSecție.setDisable(true);
    }
    private void enableButonAdaugă() {
        butonAdaugăSecție.setDisable(false);
    }
    private void enableButonȘterge() {
        butonȘtergeSecție.setDisable(false);
    }

    /**
     * Utilizatorul a selectat o secție.
     * Butonul ADD      se activează dacă secție nu există în opțiunile candidatului.
     * Butonul DELETE   se activează dacă există secția în opțiunile candidatului.
     * Butoanele se dezactivează în caz contrar.
     */
    private void activeazăDezactiveazăButoane() {
        if (secțieSelectată == null) {
            disableButonAdaugă();
            disableButonȘterge();
            return;
        }

        try {
            // ADD + DELETE
            generalService.getOpțiune(candidat.getID(), secțieSelectată.getID());
            // => secția a fost optată de candidat
            disableButonAdaugă();
            enableButonȘterge();
            hBoxSecțieSelectată.setStyle("-fx-background-color:  rgb(0, 26, 51)");
            coloreazăButoane("-fx-background-color: rgb(0, 59, 119)");

        } catch (Exception e) {
            // => secția nu a fost optată de candidat
            enableButonAdaugă();
            disableButonȘterge();
            prioritățiSecție.clear();
            hBoxSecțieSelectată.setStyle("-fx-background-color:   rgb(0, 27, 35)");
            coloreazăButoane("-fx-background-color: rgb(0, 71, 67)");
        }
    }

    private void coloreazăButoane(String style) {
        butonAdaugăSecție.setStyle(style);
        butonȘtergeSecție.setStyle(style);
    }

    private void afișareMesaj(String mesaj, Boolean succes) {
        Alert text;
        if (succes){
            text = new Alert(Alert.AlertType.INFORMATION);
            text.setTitle("Bine ai venit !");
        }
        else {
            text = new Alert(Alert.AlertType.ERROR);
            text.setTitle("Eroare");
        }

        text.setContentText(mesaj);
        text.showAndWait();
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

    @Override
    public void notifyOnEvent(OpțiuneEvent event) {
        notifyOnEvent();
    }
    public void notifyOnEvent() {
        modificDinCod = true;
        reloadToateSecțiile();
        reloadSecțiiAlese();
        recalculareNrToateSecțiiTabel();
        cautăSecții();
        activeazăDezactiveazăButoane();
        reloadListaLimbilor();
        actualizeazăPrioritatea(secțieSelectată);
        reloadPieChart(secțieSelectată);
        modificDinCod = false;
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
        limbiDePredare.addAll(generalService.getSecțieService().getLimbileDePredare());
        choiceBoxLimbaDePredare.setItems(limbiDePredare);
        choiceBoxLimbaDePredare.getSelectionModel().selectFirst();
    }


    @FXML
    public void openWindowDatePersonale(ActionEvent actionEvent) {
        try {
            FXMLLoader loaderDatePersonale = new FXMLLoader();
            loaderDatePersonale.setLocation(getClass().getClassLoader().getResource("sample/ui/userUI/DatePersonale.fxml"));
            Parent rootDatePersonale = loaderDatePersonale.load();
            DatePersonaleController datePersonaleController = loaderDatePersonale.getController();
            datePersonaleController.setGeneralService(generalService);
            datePersonaleController.setCandidat(candidat);

            Stage stage = new Stage();
            stage.setTitle(candidat.getNume() + " " + candidat.getPrenume() + " - date personale");
            stage.setScene(new Scene(rootDatePersonale));
            stage.show();
        }
        catch (Exception e) {
            afișareMesaj(e.getMessage(), false);
        }
    }


    @FXML
    public void logOut(ActionEvent actionEvent) {
        Stage stage = (Stage) tabelToateSecțiile.getScene().getWindow();
        stage.close();
    }


    @FXML
    public void openWindowStatisticaAlegerilor(ActionEvent actionEvent) {
        try {
            FXMLLoader loaderStatisticaAlegerilor = new FXMLLoader();
            loaderStatisticaAlegerilor.setLocation(getClass().getClassLoader().getResource("sample/ui/userUI/StatisticaAlegerilor.fxml"));
            Parent rootStatisticaAlegerilor = loaderStatisticaAlegerilor.load();
            StatisticaAlegerilorController statisticaAlegerilorController = loaderStatisticaAlegerilor.getController();
            statisticaAlegerilorController.setGeneralService(generalService);
            statisticaAlegerilorController.setCandidat(candidat);

            Stage stage = new Stage();
            stage.setTitle("Statistica alegerilor secțiilor de către toți candidații");
            stage.setScene(new Scene(rootStatisticaAlegerilor));
            stage.show();
        }
        catch (Exception e) {
            afișareMesaj(e.getMessage(), false);
        }
    }

    @FXML
    public void openWindowȘtergeCont(ActionEvent actionEvent) {
        try {
            FXMLLoader loaderȘtergeCont = new FXMLLoader();
            loaderȘtergeCont.setLocation(getClass().getClassLoader().getResource("sample/ui/userUI/ȘtergeCont.fxml"));
            Parent rootȘtergeCont = loaderȘtergeCont.load();
            ȘtergeContController ștergeContController = loaderȘtergeCont.getController();
            ștergeContController.setGeneralService(generalService);
            Stage fereastraUser = (Stage) tabelSecțiiAlese.getScene().getWindow();
            ștergeContController.setCnpCandidatȘiFereastraUser(candidat.getID(), fereastraUser);

            Stage stage = new Stage();
            stage.setTitle("Ștergere cont - " + candidat.getNume() + " " + candidat.getPrenume());
            stage.setScene(new Scene(rootȘtergeCont));
            stage.show();
        }
        catch (Exception e) {
            afișareMesaj(e.getMessage(), false);
        }
    }

    @FXML
    public void openWindowGenerareContract(ActionEvent actionEvent) {
        try {
            FXMLLoader loaderPrintareOpțiuni = new FXMLLoader();
            loaderPrintareOpțiuni.setLocation(getClass().getClassLoader().getResource("sample/ui/userUI/GenerareContract.fxml"));
            Parent rootPrintareOpțiuni = loaderPrintareOpțiuni.load();
            GenerareContractController generareContractController = loaderPrintareOpțiuni.getController();
            generareContractController.setGeneralService(generalService);
            generareContractController.setCandidat(candidat);

            Stage stage = new Stage();
            stage.setTitle(candidat.getNume() + " " + candidat.getPrenume() + " - printare secții optate");
            stage.setScene(new Scene(rootPrintareOpțiuni));
            stage.show();
        }
        catch (Exception e) {
            afișareMesaj(e.getMessage(), false);
        }
    }



    /** Afișarea paginată a tabelului */

    @FXML
    public void changeTabel_afișarePaginată(ActionEvent actionEvent) {
        if (checkBoxAfișarePaginată.isSelected())
            setTabel_afișarePaginată();
        else
            setTabel_afișareNormală();
    }

    private void recalculareNrToateSecțiiTabel() {
        if (!checkBoxAfișarePaginată.isSelected()) {
            if (câmpSearchSecție.getText().length() > 0 || !choiceBoxLimbaDePredare.getSelectionModel().getSelectedItem().equals("orice limbă"))
                tabelToateSecțiile.setItems(secțiiGăsite);
            else
                tabelToateSecțiile.setItems(toateSecțiile);
            return;
        }

        Double heightWindow = tabelToateSecțiile.getScene().getWindow().getHeight(); // 150 de pixeli de sus până la tabel
        Double heightTabel = tabelToateSecțiile.getHeight();
        //Integer înălțimeFereastră = heightWindow.intValue();
        Integer înălțimeTabel = heightTabel.intValue();
        Integer înălțimeLinie = 26;

        Integer nrLinii;
        nrLinii = înălțimeTabel / înălțimeLinie;
//        if (înălțimeTabel < înălțimeFereastră-150)
//            nrLinii = înălțimeTabel/înălțimeLinie;
//        else
//            nrLinii = (înălțimeFereastră - 150)/înălțimeLinie;

        if (nrLinii < 0)
            nrLinii = 0;

        Vector<Secție> secțieVector = new Vector<>();
        if (câmpSearchSecție.getText().length() > 0  || !choiceBoxLimbaDePredare.getSelectionModel().getSelectedItem().equals("orice limbă"))
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
        tabelToateSecțiile.setItems(secțiiParțiale);
    }

    private void setTabel_afișarePaginată() {
        paginare.setDisable(false);
        paginare.setVisible(true);
        recalculareNrToateSecțiiTabel();
    }
    private void setTabel_afișareNormală() {
        paginare.setDisable(true);
        paginare.setVisible(false);
        reloadToateSecțiile();
        if (!checkBoxAfișarePaginată.isSelected()) {
            if (câmpSearchSecție.getText().length() > 0 || !choiceBoxLimbaDePredare.getSelectionModel().getSelectedItem().equals("orice limbă"))
                tabelToateSecțiile.setItems(secțiiGăsite);
            else
                tabelToateSecțiile.setItems(toateSecțiile);
        }
    }


}
