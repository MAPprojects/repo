package Utilities;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDTrueTypeFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.encoding.WinAnsiEncoding;

import java.io.IOException;
import java.util.List;

public class PdfExporter {

    public PdfExporter() {}

    public void export(String filename, String header, List<String> content) {
        try {
            PDDocument document = new PDDocument();
            PDPage page = new PDPage();
            document.addPage(page);
            PDFont font = PDType1Font.HELVETICA;

            PDPageContentStream contents = new PDPageContentStream(document, page);
            contents.setLeading(16.0);
            contents.beginText();
            contents.setFont(font, 25);
            contents.newLineAtOffset(50, 750);
            contents.showText(header);
            contents.endText();

            contents.beginText();
            contents.setFont(font, 15);
            contents.newLineAtOffset(50,700);
            //System.out.println(content);
            for (String str : content) {
                str = remove(str);
                contents.showText(str);
                contents.newLine();
            }


            contents.endText();
            contents.close();
            document.save(filename);
            document.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String remove(String test) {
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < test.length(); i++) {
            if (WinAnsiEncoding.INSTANCE.contains(test.charAt(i))) {
                b.append(test.charAt(i));
            }
        }
        return b.toString();
    }

}
