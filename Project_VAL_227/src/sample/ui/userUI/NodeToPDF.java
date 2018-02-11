package sample.ui.userUI;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;


public class NodeToPDF {
    String path;

    public NodeToPDF() {
        path = "./PrintFolder/";
    }

    /**
     * @param borderPane - fereastra de printat
     * @param fileName - numele fișierului FĂRĂ extensia .pdf (extensia e adăugată automat)
     */
    public void print(BorderPane borderPane, String fileName) {
        PDDocument document = getDocument(borderPane);
        if (document != null) {
            try {
                Path fullPath = Paths.get(path + fileName + ".pdf");
                OutputStream outputStream = Files.newOutputStream(fullPath);
                document.save(outputStream);
                document.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

//        WritableImage nodeshot = borderPane.snapshot(new SnapshotParameters(), null);
//        File file = new File("chart.png");
//
//        try {
//            ImageIO.write(SwingFXUtils.fromFXImage(nodeshot, null), "png", file);
//        } catch (IOException ignored) {}
//
//
//        PDDocument doc = new PDDocument();
//        PDPage page = new PDPage();
//        PDImageXObject pdimage;
//        PDPageContentStream content;
//        try {
//            pdimage = PDImageXObject.createFromFile("chart.png", doc);
//            content = new PDPageContentStream(doc, page);
//            content.drawImage(pdimage, 0, 0);
//            content.close();
//            doc.addPage(page);
//            doc.save(fileName + ".pdf");
//            doc.close();
//            //file.delete();
//        } catch (IOException ex) {
//            Logger.getLogger(NodeToPDF.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    /**
     * @param borderPane - fereastra de printat
     * @return documentul PDF creat
     * A se închide ducumentul după primire !
     */
    public PDDocument getDocument(BorderPane borderPane) {
        WritableImage nodeshot = borderPane.snapshot(new SnapshotParameters(), null);
        File file = new File("chart.png");

        try {
            ImageIO.write(SwingFXUtils.fromFXImage(nodeshot, null), "png", file);
        } catch (IOException ignored) {}


        float width = (float) nodeshot.getWidth();
        float height = (float) nodeshot.getHeight();
        PDDocument doc = new PDDocument();
        PDPage page = new PDPage(new PDRectangle(width, height));
        PDImageXObject pdimage;
        PDPageContentStream content;
        try {
            pdimage = PDImageXObject.createFromFile("chart.png", doc);
            content = new PDPageContentStream(doc, page);;
            content.drawImage(pdimage, 0, 0);
            content.close();
            doc.addPage(page);
            file.delete();
            //doc.close();
            return doc;
        } catch (IOException ex) {
            Logger.getLogger(NodeToPDF.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
