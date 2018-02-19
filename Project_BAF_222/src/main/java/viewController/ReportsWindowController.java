package viewController;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.parser.Path;
import entities.Candidat;
import entities.CheieOptiune;
import entities.Optiune;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.effect.Glow;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Transform;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import service.AbstractService;
import service.OptiuneService;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;


public class ReportsWindowController {


    @FXML
    private Pagination pagination;

    @FXML
    private PieChart pieChart;

    @FXML
    private Label numberLabel;

    private AbstractService<Optiune, CheieOptiune> service;
    private Stage stage;


    public void setService(AbstractService<Optiune, CheieOptiune> service,Stage stage){
        this.service = service;
        this.stage = stage;
        pagination.getStylesheets().add("paginationStyle.css");
        pieChart.getStylesheets().add("/pieChartStyle.css");
        setPagination();
    }

    private void setPagination() {

        pagination.setPageCount(3);
        pagination.setPageFactory(new Callback<Integer, Node>() {
            @Override
            public Node call(Integer pageIndex) {
                if (pageIndex > 3) {
                    return null;
                } else {
                    setPieChart(pagination.getCurrentPageIndex());
                    return pieChart;
                }
            }
        });

    }

    @FXML
    void handleOK(){
        stage.close();
    }

    private static ImageView createScaledView(Node node, int scale) {
        final Bounds bounds = node.getLayoutBounds();

        final WritableImage image = new WritableImage(
                (int) Math.round(bounds.getWidth() * scale),
                (int) Math.round(bounds.getHeight() * scale));

        final SnapshotParameters spa = new SnapshotParameters();
        spa.setTransform(javafx.scene.transform.Transform.scale(scale, scale));

        final ImageView view = new ImageView(node.snapshot(spa, image));
        //WritableImage img = node.snapshot(spa, image);

        view.setFitWidth(bounds.getWidth());
        view.setFitHeight(bounds.getHeight());

        return view;
    }

    void addPieChartMouseEvents(PieChart pieChart){

        for(PieChart.Data data : pieChart.getData()){
            numberLabel.getStyleClass().addAll("chart-line-symbol", "chart-series-line");
            numberLabel.setStyle("-fx-font-size: 12; -fx-font-weight: bold;");
            Node node = data.getNode();
            node.setOnMouseEntered(new EventHandler<MouseEvent>() {

                @Override
                public void handle(MouseEvent arg0) {
                    node.setEffect(new Glow());
                    numberLabel.setTranslateX(arg0.getX()+295);
                    numberLabel.setTranslateY(arg0.getY()+100);
                    numberLabel.setText(""+(int)data.getPieValue()+" inregistrari");
                    numberLabel.setVisible(true);
                    numberLabel.toFront();
                   // String styleString = "-fx-border-color: white; -fx-border-width: 3; -fx-border-style: dashed;";
                  //  node.setStyle(styleString);
                   // tooltip.setText(String.valueOf(data.getName() + "\n" + (int)data.getPieValue()) );
                }
            });
            node.setOnMouseMoved(new EventHandler<MouseEvent>() {

                @Override
                public void handle(MouseEvent arg0) {
                    numberLabel.setTranslateX(arg0.getX()+295);
                    numberLabel.setTranslateY(arg0.getY()+100);
                }
            });


            node.setOnMouseExited(new EventHandler<MouseEvent>() {

                @Override
                public void handle(MouseEvent arg0) {
                    node.setEffect(null);
                    numberLabel.setVisible(false);
                }
            });
        }
    }

    @FXML
    void handlePDF(){
        try {
            ImageView scaledImage = createScaledView(pieChart,3);
            javafx.scene.image.Image image = scaledImage.getImage();

            int reportNumber = pagination.getCurrentPageIndex() + 1;
            String pdfName = "Raport" + reportNumber + ".pdf";

            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(pdfName));
            document.open();


            ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
            ImageIO.write( SwingFXUtils.fromFXImage( image, null ), "png", byteOutput );
            com.itextpdf.text.Image  graph;
            graph = com.itextpdf.text.Image.getInstance( byteOutput.toByteArray() );
            graph.scaleAbsolute(600,350);
            graph.setAbsolutePosition(0,430);
            document.add(graph);

            PdfContentByte over = writer.getDirectContent();
            over.saveState();
            float sinus = (float)Math.sin(Math.PI / 60);
            float cosinus = (float)Math.cos(Math.PI / 60);
            BaseFont bf = BaseFont.createFont();
            over.beginText();
            over.setTextRenderingMode(PdfContentByte.TEXT_RENDER_MODE_FILL_STROKE);
            over.setLineWidth(1.0f);
            //over.setRGBColorStroke(0xFF, 0x00, 0x00);
            //over.setRGBColorFill(0xFF, 0xFF, 0xFF);
            over.setFontAndSize(bf, 16);
            //over.setTextMatrix(cosinus, sinus, -sinus, cosinus, 50, 324);
            over.setTextMatrix(60,360);
            if(pagination.getCurrentPageIndex()==0)
                over.showText("Numarul de inregistrari pentru fiecare sectie:");
            else if(pagination.getCurrentPageIndex()==1)
                over.showText("Numarul de candidati grupati dupa numarul de inregistrari:");
            else if(pagination.getCurrentPageIndex()==2)
                over.showText("Numarul de locuri ramas pentru fiecare sectie:");
            over.setLineWidth(0.5f);
            over.setFontAndSize(bf, 14);
            int y=320;
            for(PieChart.Data data : pieChart.getData())
            {
                over.setTextMatrix(70,y);
                over.showText(data.getName()+" "+(int)data.getPieValue());
                y-=20;
            }

            over.endText();
            over.restoreState();


            document.close();


        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }

    }



    public void setPieChart(int currentPie) {
        OptiuneService newService = (OptiuneService) service;
        switch (currentPie){
            case 0:
                pieChart.setTitle("Alegerea primei optiuni in functie de sectie");
                pieChart.setData(newService.getNumberOfEntriesPerSectie());
                break;
            case 1:
                pieChart.setTitle("Gruparea candidatilor dupa numarul de optiuni");
                pieChart.setData(newService.getNumberOfCandidatsGroupedByNumberOfOptions());
                break;
            case 2:
                pieChart.setTitle("Numarul de locuri libere ramase la fiecare sectie");
                pieChart.setData(newService.getSectiiAndNumberOfRelativeRemainingEmptySlots());
                break;
            default:
                pieChart.setTitle("Blabla");
                break;
        }
        addPieChartMouseEvents(pieChart);
    }

    @FXML
    public void handleMail(){
        try {
            handlePDF();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(DetailedCandidatController.class.getResource("/mailComposer.fxml"));
            AnchorPane root = (AnchorPane) loader.load();


            Stage dialogStage = new Stage();
            dialogStage.setTitle("");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            //dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            MailController mailController = loader.getController();

            int reportNumber = pagination.getCurrentPageIndex() + 1;
            String pdfName = "Raport" + reportNumber + ".pdf";
            mailController.setDefaults(null,null,null,dialogStage,pdfName);

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
