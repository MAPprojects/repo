package FXMLView;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.WritableImage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class PDF {

    public void createPDFWithImage(WritableImage image) throws IOException {

        //Creating PDF document object
        PDDocument document = new PDDocument();


        //Saving the document
        PDPage my_page = new PDPage();
        document.addPage(my_page);
        String currentDirectory = System.getProperty("user.dir");
        String finalPath = currentDirectory + "/myPdf";
        String imagePath = currentDirectory+ "/chart.png";
        File file = new File(imagePath);

        ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);

        PDImageXObject pdImage = PDImageXObject.createFromFile(imagePath, document);

        PDPageContentStream content = new PDPageContentStream(document, my_page);

        content.drawImage(pdImage, 50, 250);

        content.close();

        document.save(finalPath);

        System.out.println("PDF created");

        //Closing the document
        document.close();

    }

}
