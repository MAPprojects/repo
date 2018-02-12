package controller;

import entities.Role;
import exceptions.TemaServiceException;
import exceptions.TemaValidatorException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import services.TemaService;
import thread_utils.NotifyingThread;
import thread_utils.ThreadCompleteListener;

import java.io.FileInputStream;
import java.io.IOException;

public class AddTemaController implements ThreadCompleteListener {

    private static final String MESAJ_EROARE_CERINTA_VIDA = "Cerinta nu poate fi vida.";
    private static final String MESAJ_EROARE_TERMEN_VID = "Termenul nu poate fi vid.";
    private static final String MESAJ_EROARE_TERMEN_INVALID = "TERMEN INVALID (intre 1-14).";
    private AnchorPane temaViewRootLayout;
    private GaussianBlur gaussianBlur;
    private ImageView loadingImageView;
    private AnchorPane rootLayout;
    private Stage addTemaDialogStage;
    private TemaService temaService;
    @FXML
    private TextArea cerintaTemaField;
    @FXML
    private TextField termenPredareTemaField;
    @FXML
    private Text mesajEroareCerinta;
    @FXML
    private Text mesajEroareTermenPredare;

    public void setAddTemaDialogStage(Stage addTemaDialogStage) {
        this.addTemaDialogStage = addTemaDialogStage;
    }

    public void setTemaService(TemaService temaService) {
        this.temaService = temaService;
    }

    public void setRootLayout(AnchorPane rootLAyout) {
        this.rootLayout = rootLAyout;
    }

    public void setTemaViewRootLayout(AnchorPane temaViewRootLayout) {
        this.temaViewRootLayout = temaViewRootLayout;
    }

    @FXML
    public void initialize() {
        gaussianBlur = new GaussianBlur();
    }

    public void addTema(MouseEvent mouseEvent) throws IOException, TemaValidatorException, TemaServiceException {
        clearWarningForInputs();
        String cerinta = cerintaTemaField.getText();
        String termenPredare = termenPredareTemaField.getText();
        if (validateInfo()) {
            //loading the image and setting the image view
            FileInputStream loadingGifInputStream = new FileInputStream("src\\main\\resources\\views\\loadingScene\\loader.gif");
            Image loadingImage = new Image(loadingGifInputStream);
            loadingImageView = new ImageView(loadingImage);
            rootLayout.getChildren().add(loadingImageView);
            loadingImageView.setX(rootLayout.getWidth() / 2 - 80);
            loadingImageView.setY(rootLayout.getHeight() / 2 - 100);
            rootLayout.getChildren().forEach(node -> {
                if (node != loadingImageView) {
                    node.setDisable(true);
                    node.setEffect(gaussianBlur);
                }
            });
            NotifyingThread addUserThread = new NotifyingThread() {
                @Override
                public void doRun() {
                    try {
                        temaService.addTema(cerinta, Integer.parseInt(termenPredare));
                        Platform.runLater(() -> {
                            addTemaDialogStage.close();
                        });
                    } catch (TemaServiceException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (TemaValidatorException e) {
                        e.printStackTrace();
                    }
                }
            };
            addUserThread.addListener(this);
            addUserThread.start();
        }
    }

    public void exitDialogAddTemaHandler(MouseEvent mouseEvent) {
        temaViewRootLayout.getChildren().forEach(node -> {
            node.setDisable(false);
            node.setEffect(null);
        });
        addTemaDialogStage.close();
    }

    private boolean validateInfo() {
        String cerinta = cerintaTemaField.getText();
        String termenPredare = termenPredareTemaField.getText();
        boolean infoValida = true;
        if (cerinta.equals("") || cerinta.equals(MESAJ_EROARE_CERINTA_VIDA)) {
            infoValida = false;
            cerintaTemaField.setText(MESAJ_EROARE_CERINTA_VIDA);
            cerintaTemaField.setStyle("-fx-text-inner-color: red; -fx-font-size: 14;");
        }
        if (termenPredare.equals("") || termenPredare.equals(MESAJ_EROARE_TERMEN_VID)) {
            infoValida = false;
            termenPredareTemaField.setText(MESAJ_EROARE_TERMEN_VID);
            termenPredareTemaField.setStyle("-fx-text-inner-color: red; -fx-font-size: 14;");
        } else {
            try {
                Integer termenPredareNumber = Integer.parseInt(termenPredareTemaField.getText());
                if (termenPredareNumber < 1 || termenPredareNumber > 14) {
                    infoValida = false;
                    termenPredareTemaField.setText(MESAJ_EROARE_TERMEN_INVALID);
                    termenPredareTemaField.setStyle("-fx-text-inner-color: red; -fx-font-size: 14;");
                }
            } catch (NumberFormatException e) {
                infoValida = false;
                termenPredareTemaField.setText(MESAJ_EROARE_TERMEN_INVALID);
                termenPredareTemaField.setStyle("-fx-text-inner-color: red; -fx-font-size: 14;");
            }
        }
        return infoValida;
    }

    private void clearWarningForInputs() {
        mesajEroareCerinta.setText("");
        mesajEroareTermenPredare.setText("");
    }

    @Override
    public void notifyOfThreadComplete(Thread thread) {
        Platform.runLater(() -> {
            rootLayout.getChildren().remove(loadingImageView);
            rootLayout.getChildren().forEach(node -> {
                node.setDisable(false);
                node.setEffect(null);
            });
            temaViewRootLayout.getChildren().forEach(node -> {
                if (node != loadingImageView) {
                    node.setDisable(false);
                    node.setEffect(null);
                }
            });
        });
    }

    @FXML
    private void deleteErrorMessageCerinta(MouseEvent mouseEvent) {
        if (cerintaTemaField.getText().equals(MESAJ_EROARE_CERINTA_VIDA)) {
            cerintaTemaField.setText("");
            cerintaTemaField.setStyle("-fx-text-inner-color: black; -fx-font-size: 14;");
        }
    }

    @FXML
    private void deleteErrorMessageTermen(MouseEvent mouseEvent) {
        if ((termenPredareTemaField.getText().equals(MESAJ_EROARE_TERMEN_VID)) ||
                (termenPredareTemaField.getText().equals(MESAJ_EROARE_TERMEN_INVALID))) {
            termenPredareTemaField.setText("");
            termenPredareTemaField.setStyle("-fx-text-inner-color: black; -fx-font-size: 18;");
        }
    }

    @FXML
    private void deleteErrorOnTabTermen(KeyEvent keyEvent) {
        termenPredareTemaField.setStyle("-fx-text-inner-color: black; -fx-font-size: 18;");
    }

    @FXML
    private void deleteErrorOnTabCerinta(KeyEvent keyEvent) {
        cerintaTemaField.setStyle("-fx-text-inner-color: black; -fx-font-size: 14;");
    }
}

