package sample.ui.adminUI;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import sample.ui.AnimatedZoomOperator;
import sample.ui.userUI.NodeToPDF;

import javax.imageio.ImageIO;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PrintareGeneralăController {
    private String path;
    private String nume;
    private AnimatedZoomOperator animatedZoomOperator;
    @FXML public Label labelNumePrintare;
    @FXML public Label labelProcentZoom;
    @FXML public Label labelProcentZoom1;
    @FXML public BorderPane printBorderPane;

    public PrintareGeneralăController() {
        animatedZoomOperator = new AnimatedZoomOperator();
        path = "./PrintFolder/";
    }

    public void setPrintBorderPane(BorderPane borderPane, String nume) {
        this.printBorderPane = borderPane;
        this.nume = nume;
        labelNumePrintare.setText(nume);
    }

    public void setPath(String path) {
        this.path = path;
    }


    @FXML
    public void printeazăPDF(ActionEvent actionEvent) {
        printeazăPDF();
    }

    public void printeazăPDF() {
        if (printBorderPane == null || nume == null) {
            afișareMesaj("Ce să printez ?", false);
            return;
        }

        try {
            NodeToPDF nodeToPDF = new NodeToPDF();
            nodeToPDF.print(printBorderPane, nume);
            afișareMesaj("Documentul PDF a fost salvat în folderul aplicației în PrintFolder.", true);
        }
        catch (Exception e) {
            afișareMesaj(e.getMessage(), false);
        }
    }


    @FXML
    public void salveazăPNG(ActionEvent actionEvent) {
        salveazăPNG();
    }

    public void salveazăPNG() {
        if (printBorderPane == null || nume == null) {
            afișareMesaj("Ce să printez ?", false);
            return;
        }

        try {
            WritableImage image = printBorderPane.snapshot(new SnapshotParameters(), null);
            // TODO: probably use a file chooser here
//            JFileChooser f = new JFileChooser();
//            f.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
//            f.showSaveDialog(null);
//            File director = f.getCurrentDirectory();
//            File file = f.getSelectedFile();
            Path fullPath = Paths.get(path + nume);
            File file = new File(fullPath + ".png");
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);

            afișareMesaj("Imaginea a fost salvată în folderul aplicației.", true);

        } catch (Exception e) {
            afișareMesaj(e.getMessage(), false);
        }
    }


    @FXML
    public void trimiteEmail(ActionEvent actionEvent) {
        trimiteEmail();
    }

    public void trimiteEmail() {
        if (printBorderPane == null || nume == null) {
            afișareMesaj("Ce să printez ?", false);
            return;
        }
        afișareMesaj("Ne pare rău, dar acest serviciu este indisponibil momentan.", false);
    }


    @FXML
    public void zoomMinus(ActionEvent actionEvent) {
        zoomMinus();
    }
    private void zoomMinus() {
        if (printBorderPane == null) {
            afișareMesaj("Nu am la ce face zoom", false);
            return;
        }

        try {
            Integer procent = Integer.valueOf(labelProcentZoom.getText());
            procent = procent / 2;
            labelProcentZoom.setText(procent.toString());
        } catch (Exception ignored) {
            labelProcentZoom.setText("100");
        }

        animatedZoomOperator.zoom(printBorderPane, 0.5, 2, 2);
    }

    @FXML
    public void zoomPlus(ActionEvent actionEvent) {
        zoomPlus();
    }
    private void zoomPlus() {
        if (printBorderPane == null) {
            afișareMesaj("Nu am la ce face zoom", false);
            return;
        }

        try {
            Integer procent = Integer.valueOf(labelProcentZoom.getText());
            procent = procent * 2;
            labelProcentZoom.setText(procent.toString());
        } catch (Exception ignored) {
            labelProcentZoom.setText("100");
        }

        animatedZoomOperator.zoom(printBorderPane, 2, 2, 2);
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
