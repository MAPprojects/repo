import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

import java.io.IOException;

public class Main {

    public static void main (String args[]) throws IOException {

        //Creating PDF document object
        PDDocument document = new PDDocument();

        //Saving the document
        PDPage my_page = new PDPage();
        document.addPage(my_page);
        String currentDirectory = System.getProperty("user.dir");
        String finalPath = currentDirectory + "/myPdf";
        document.save(finalPath);

        System.out.println("PDF created");

        //Closing the document
        document.close();

    }
}