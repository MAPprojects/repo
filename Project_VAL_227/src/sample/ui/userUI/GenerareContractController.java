package sample.ui.userUI;

import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import sample.domain.Candidat;
import sample.domain.FormăDeFinanțare;
import sample.domain.Secție;
import sample.service.GeneralService;
import sample.ui.AnimatedZoomOperator;

import javax.imageio.ImageIO;
import java.io.File;
import java.util.Vector;



public class GenerareContractController {
    private GeneralService generalService;
    private Candidat candidat;
    private ObservableList<Secție> secțiiAlese;
    private double dimensiuneInițială;
    private double lastZoom;

    @FXML public BorderPane mainBorderPane;
    @FXML public BorderPane borderPaneA4;
    @FXML public Label labelPrenume;
    @FXML public Label labelNume;
    @FXML public Label labelCNP;
    @FXML public Label labelTelefon;
    @FXML public Label labelEmail;
    @FXML public TableView<Secție> tabelSecțiiAlese;
    @FXML public TableColumn<Secție,Integer> tabelSecțiiAlese_prioritate;
    @FXML public TableColumn<Secție,Integer> tabelSecțiiAlese_ID;
    @FXML public TableColumn<Secție,String> tabelSecțiiAlese_nume;
    @FXML public TableColumn<Secție,String> tabelSecțiiAlese_limba;
    @FXML public TableColumn<Secție,FormăDeFinanțare> tabelSecțiiAlese_finanțare;
    @FXML public TableColumn<Secție,Integer> tabelSecțiiAlese_nrLocuri;
    @FXML public VBox vBoxLoading;
    @FXML public ProgressIndicator progressIndicator;
    @FXML public Label labelProcentZoom;
    AnimatedZoomOperator animatedZoomOperator;


    public GenerareContractController() {
        generalService = new GeneralService();
        secțiiAlese = FXCollections.observableArrayList();
        animatedZoomOperator = new AnimatedZoomOperator();
    }
    public GenerareContractController(GeneralService generalService) {
        this.generalService = generalService;
        secțiiAlese = FXCollections.observableArrayList();
        animatedZoomOperator = new AnimatedZoomOperator();
    }
    public void setGeneralService(GeneralService generalService) {
        this.generalService = generalService;
        reloadSecții();
    }
    public void setCandidat(Candidat candidat) {
        this.candidat = candidat;
        reloadSecții();
        completareDateCandidat();
    }
    public BorderPane getBorderPaneA4() {
        return borderPaneA4;
    }
    public BorderPane getMainBorderPane() {
        return mainBorderPane;
    }
    public void setMainBorderPane(BorderPane borderPane) {
        this.mainBorderPane = borderPane;
    }

    @FXML
    private void initialize() {
        lastZoom = 100;
        vBoxLoading.setVisible(false);

        tabelSecțiiAlese_prioritate.setCellValueFactory(column-> new ReadOnlyObjectWrapper<Integer>(tabelSecțiiAlese.getItems().indexOf(column.getValue()) + 1));
        tabelSecțiiAlese_ID.setCellValueFactory(new PropertyValueFactory<Secție,Integer>("ID"));
        tabelSecțiiAlese_nume.setCellValueFactory(new PropertyValueFactory<Secție,String>("Nume"));
        tabelSecțiiAlese_limba.setCellValueFactory(new PropertyValueFactory<Secție,String>("LimbaDePredare"));
        tabelSecțiiAlese_finanțare.setCellValueFactory(new PropertyValueFactory<Secție,FormăDeFinanțare>("FormăDeFinanțare"));
        tabelSecțiiAlese_nrLocuri.setCellValueFactory(new PropertyValueFactory<Secție,Integer>("NrLocuri"));

        dimensiuneInițială = 595;

        reloadSecții();
        seteazăListeneri();
    }

    private void seteazăListeneri() {

    }

    private void completareDateCandidat() {
        if (candidat == null)
            return;
        labelCNP.setText(candidat.getID());
        labelNume.setText(candidat.getNume());
        labelPrenume.setText(candidat.getPrenume());
        labelTelefon.setText(candidat.getTelefon());
        labelEmail.setText(candidat.getE_mail());
    }

    private void reloadSecții() {
        if (candidat == null || generalService == null)
            return;

        Vector<Secție> secțieVector = generalService.getSecțiiAlese(candidat.getID());
        secțiiAlese.clear();
        secțiiAlese.addAll(secțieVector);
        tabelSecțiiAlese.setItems(secțiiAlese);
    }


    @FXML
    public void salveazăPNG(ActionEvent actionEvent) {
        Thread thread_loading = new Thread() {
            public void run() {
                showLoading();
            }
        };
        thread_loading.start();

        Alert mesaj;
        mesaj = new Alert(Alert.AlertType.INFORMATION);
        mesaj.show();
        mesaj.close();

        Thread thread_save = new Thread() {
            public void run() {
                salveazăPNG();
            }
        };
        thread_save.start();
    }
    public void salveazăPNG() {
        Platform.runLater(new Runnable() {
                    @Override
                    public void run() {

                        Thread thread = new Thread() {
                            public void run() {

                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            WritableImage image = borderPaneA4.snapshot(new SnapshotParameters(), null);
                                            // TODO: probably use a file chooser here
                                            File file = new File(candidat.getNume() + " " + candidat.getPrenume() + " " + candidat.getID() + ".png");
                                            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);

                                            // dezactivăm loading status
                                            vBoxLoading.setVisible(false);

                                            afișareMesaj("Imaginea a fost salvată în folderul aplicației.", true);

                                        } catch (Exception e) {
                                            afișareMesaj(e.getMessage(), false);
                                        }
                                    }
                                });
                            }
                        };
                        thread.start();
                    }
                });

    }

    @FXML
    public void printeazăPDF(ActionEvent actionEvent) {
        Thread thread_loading = new Thread() {
            public void run() {
                showLoading();
            }
        };
        thread_loading.start();

        Alert mesaj;
        mesaj = new Alert(Alert.AlertType.INFORMATION);
        mesaj.show();
        mesaj.close();

        Thread thread_save = new Thread() {
            public void run() {
                printeazăPDF();
            }
        };
        thread_save.start();

    }
    public void printeazăPDF() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    FXMLLoader loaderPrintareOpțiuni = new FXMLLoader();
                    loaderPrintareOpțiuni.setLocation(getClass().getClassLoader().getResource("sample/ui/userUI/GenerareContract.fxml"));
                    Parent rootPrintareOpțiuni = loaderPrintareOpțiuni.load();
                    GenerareContractController generareContractController = loaderPrintareOpțiuni.getController();
                    generareContractController.setGeneralService(generalService);
                    generareContractController.setCandidat(candidat);

                    Stage stage = new Stage();
                    stage.setTitle(candidat.getNume() + " " + candidat.getPrenume() + " - secții optate");
                    stage.setScene(new Scene(rootPrintareOpțiuni));
                    generareContractController.setMainBorderPane(borderPaneA4);
                    BorderPane borderPane = generareContractController.getMainBorderPane();


                    NodeToPDF nodeToPDF = new NodeToPDF();
                    nodeToPDF.print(borderPaneA4, candidat.getNume() + " " + candidat.getPrenume() + " " + candidat.getID());

                    // dezactivăm loading status
                    vBoxLoading.setVisible(false);

                    afișareMesaj("Documentul PDF a fost salvat în folderul aplicației în PrintFolder.", true);
                }
                catch (Exception e) {
                    afișareMesaj(e.getMessage(), false);
                }

            }
        });
    }


    private void showLoading() {
        vBoxLoading.setVisible(true);
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

    @FXML
    public void zoomMinus(ActionEvent actionEvent) {
        try {
            Integer procent = Integer.valueOf(labelProcentZoom.getText());
            procent = procent / 2;
            labelProcentZoom.setText(procent.toString());
        } catch (Exception ignored) {
            labelProcentZoom.setText("100");
        }

        animatedZoomOperator.zoom(borderPaneA4, 0.5, 2, 2);
    }

    @FXML
    public void zoomPlus(ActionEvent actionEvent) {
        try {
            Integer procent = Integer.valueOf(labelProcentZoom.getText());
            procent = procent * 2;
            labelProcentZoom.setText(procent.toString());
        } catch (Exception ignored) {
            labelProcentZoom.setText("100");
        }

        animatedZoomOperator.zoom(borderPaneA4, 2, 2, 2);
    }


    public void trimiteEmail(ActionEvent actionEvent) {
        afișareMesaj("Ne pare rău, dar acest serviciu este indisponibil momentan.", false);
    }
}
