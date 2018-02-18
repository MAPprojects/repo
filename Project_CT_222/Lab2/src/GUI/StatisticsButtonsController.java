package GUI;
import Service.Service;
import Domain.GrupeTop;
import Domain.NoteDTO;
import Domain.TemeTop;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StatisticsButtonsController {
    @FXML
    Button studenti;

    @FXML
    Button teme;

    @FXML
    Button grupe;
    Service service;
    Stage editStage;

    public void setService(Service service,Stage stage){
        this.service=service;
        this.editStage=stage;
    }
    public void initialize(){

        studenti.setOnAction(event->{
        try{
            PDFont font = PDType1Font.HELVETICA;
            float fontSize = 14;
            float fontHeight = fontSize;
            float leading = 20;
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
            Date date = new Date();

            PDDocument doc = new PDDocument();
            PDPage page = new PDPage();
            doc.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(doc, page);
            contentStream.setFont(font, fontSize);

            final float[] yCordinate = {page.getCropBox().getUpperRightY() - 30};
            float startX = page.getCropBox().getLowerLeftX() + 30;
            float endX = page.getCropBox().getUpperRightX() - 30;

            contentStream.beginText();
            contentStream.newLineAtOffset(startX, yCordinate[0]);
            contentStream.showText("Top Students");
            yCordinate[0] -= fontHeight;
            contentStream.newLineAtOffset(0, -leading);
            yCordinate[0] -= leading;
            contentStream.showText("Data: " + dateFormat.format(date));
            yCordinate[0] -= fontHeight;
            contentStream.endText(); // End of text mode

            contentStream.moveTo(startX, yCordinate[0]);
            contentStream.lineTo(endX, yCordinate[0]);
            contentStream.stroke();
            yCordinate[0] -= leading;
            contentStream.beginText();
            contentStream.newLineAtOffset(startX, yCordinate[0]);
            contentStream.showText("Creator of this statistic: Cazac Toma");
            yCordinate[0] -= fontHeight;
            contentStream.newLineAtOffset(0, -leading);
            yCordinate[0] -= leading;


            yCordinate[0] -= leading;
            List<NoteDTO> sectionList = new ArrayList<>();
            sectionList = service.getTopStudents();
            if(sectionList.size()>=10)
                sectionList.subList(0,10);
            sectionList.forEach(x -> {
                try {
                    contentStream.newLineAtOffset(0, -leading);
                    contentStream.showText("ID: "+x.getStudent().getId().toString() + "     " + "Name: "+x.getNume()+"     "+"CurrentPerformance: " + service.calculateAverage(x));
                    yCordinate[0] -= leading;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            contentStream.endText();

            contentStream.close();
            doc.save("raport.pdf");
            showMessage(Alert.AlertType.INFORMATION, "Students hierarchy has been stored in 'raportStudents.pdf'");
        } catch (IOException e) {
            e.printStackTrace();
        }}
        );

        grupe.setOnAction(event->{
        try{
            PDFont font = PDType1Font.HELVETICA;
            float fontSize = 14;
            float fontHeight = fontSize;
            float leading = 20;
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
            Date date = new Date();

            PDDocument doc = new PDDocument();
            PDPage page = new PDPage();
            doc.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(doc, page);
            contentStream.setFont(font, fontSize);

            final float[] yCordinate = {page.getCropBox().getUpperRightY() - 30};
            float startX = page.getCropBox().getLowerLeftX() + 30;
            float endX = page.getCropBox().getUpperRightX() - 30;

            contentStream.beginText();
            contentStream.newLineAtOffset(startX, yCordinate[0]);
            contentStream.showText("Top Groups");
            yCordinate[0] -= fontHeight;
            contentStream.newLineAtOffset(0, -leading);
            yCordinate[0] -= leading;
            contentStream.showText("Data: " + dateFormat.format(date));
            yCordinate[0] -= fontHeight;
            contentStream.endText(); // End of text mode

            contentStream.moveTo(startX, yCordinate[0]);
            contentStream.lineTo(endX, yCordinate[0]);
            contentStream.stroke();
            yCordinate[0] -= leading;
            contentStream.beginText();
            contentStream.newLineAtOffset(startX, yCordinate[0]);
            contentStream.showText("Creator of this statistic: Cazac Toma");
            yCordinate[0] -= fontHeight;
            contentStream.newLineAtOffset(0, -leading);
            yCordinate[0] -= leading;


            yCordinate[0] -= leading;
            List<GrupeTop> sectionList = new ArrayList<>();
            sectionList = service.getTopGrupe();
            if(sectionList.size()>=10)
                sectionList.subList(0,10);
            sectionList.forEach(x -> {
                try {
                    contentStream.newLineAtOffset(0, -leading);
                    contentStream.showText("Grupa: "+x.getGrupa().toString() + "     " + "Medie: "+x.getMedie());
                    yCordinate[0] -= leading;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            contentStream.endText();

            contentStream.close();
            doc.save("raportGroups.pdf");
            showMessage(Alert.AlertType.INFORMATION, "Projects hierarchy has been stored in 'raportGroups.pdf' ");
        } catch (IOException e) {
            e.printStackTrace();
        }

            Scene scene = new Scene(new Group());
            editStage.setTitle("Top groups");
            editStage.setWidth(500);
            editStage.setHeight(500);


            List<PieChart.Data> piechart=new ArrayList<>();
            List<GrupeTop> sectionList = new ArrayList<>();
            sectionList = service.getTopGrupe();
            if(sectionList.size()>=10)
                sectionList.subList(0,10);
            sectionList.forEach(x -> {

                    piechart.add(new PieChart.Data(""+x.getGrupa()+": "+x.getMedie(),x.getMedie()));


                });


            ObservableList<PieChart.Data> pieChartData =
                    FXCollections.observableArrayList(
                            piechart);
            final PieChart chart = new PieChart(pieChartData);
            chart.setTitle("Groups");
            final Label caption = new Label("");
            caption.setTextFill(Color.DARKORANGE);
            caption.setStyle("-fx-font: 16 arial;");



            final Button pushtoExport=new Button("Export");
            pushtoExport.setOnAction(event1->{
                WritableImage nodeshot = chart.snapshot(new SnapshotParameters(), null);
                File file = new File("chart.png");

                try {
                    ImageIO.write(SwingFXUtils.fromFXImage(nodeshot, null), "png", file);
                } catch (IOException e) {

                }

                PDDocument doc    = new PDDocument();
                PDPage page = new PDPage();
                PDImageXObject pdimage;
                PDPageContentStream content;
                try {
                    pdimage = PDImageXObject.createFromFile("chart.png",doc);
                    content = new PDPageContentStream(doc, page);
                    content.drawImage(pdimage, 100, 100);
                    content.close();
                    doc.addPage(page);
                    doc.save("pdf_file.pdf");
                    doc.close();
                    file.delete();
                    editStage.close();
                } catch (IOException ex) {
                    Logger.getLogger(StatisticsButtonsController.class.getName()).log(Level.SEVERE, null, ex);
                }

            });

            ((Group) scene.getRoot()).getChildren().add(chart);
            ((Group) scene.getRoot()).getChildren().add(pushtoExport);
            editStage.setScene(scene);
            editStage.show();
        });


       teme.setOnAction(event->{
        try{
            PDFont font = PDType1Font.HELVETICA;
            float fontSize = 14;
            float fontHeight = fontSize;
            float leading = 20;
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
            Date date = new Date();

            PDDocument doc = new PDDocument();
            PDPage page = new PDPage();
            doc.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(doc, page);
            contentStream.setFont(font, fontSize);

            final float[] yCordinate = {page.getCropBox().getUpperRightY() - 30};
            float startX = page.getCropBox().getLowerLeftX() + 30;
            float endX = page.getCropBox().getUpperRightX() - 30;

            contentStream.beginText();
            contentStream.newLineAtOffset(startX, yCordinate[0]);
            contentStream.showText("Top E-H Projects");
            yCordinate[0] -= fontHeight;
            contentStream.newLineAtOffset(0, -leading);
            yCordinate[0] -= leading;
            contentStream.showText("Data: " + dateFormat.format(date));
            yCordinate[0] -= fontHeight;
            contentStream.endText(); // End of text mode

            contentStream.moveTo(startX, yCordinate[0]);
            contentStream.lineTo(endX, yCordinate[0]);
            contentStream.stroke();
            yCordinate[0] -= leading;
            contentStream.beginText();
            contentStream.newLineAtOffset(startX, yCordinate[0]);
            contentStream.showText("Creator of this statistic: Cazac Toma");
            yCordinate[0] -= fontHeight;
            contentStream.newLineAtOffset(0, -leading);
            yCordinate[0] -= leading;


            yCordinate[0] -= leading;
            List<TemeTop> sectionList = new ArrayList<>();
            sectionList = service.getTopTeme();
            if(sectionList.size()>=10)
                sectionList.subList(0,10);
            sectionList.forEach(x -> {
                try {
                    contentStream.newLineAtOffset(0, -leading);
                    contentStream.showText("Project: "+x.getTemaNr().toString() + "     " + "Average: "+x.getMedie());
                    yCordinate[0] -= leading;


                } catch (IOException e) {
                    e.printStackTrace();
                }
            });



            contentStream.endText();

            contentStream.close();
            doc.save("raportProjects.pdf");
            showMessage(Alert.AlertType.INFORMATION, "Groups hierarchy has been stored in 'raportProjects.pdf'");
        } catch (IOException e) {
            e.printStackTrace();
        }



    });

    }
    private void showMessage(Alert.AlertType information, String s) {
        Alert message = new Alert(information);
        message.setHeaderText("");
        message.setContentText(s);
        message.showAndWait();
    }
}
