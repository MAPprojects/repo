package service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Map;

public class GeneratePDF {
    public <T> void genereatePDf(Map<String, String> map, String header1, String header2, String filename) throws FileNotFoundException, DocumentException, NoSuchFieldException, IllegalAccessException {
        Document document = new Document();
        String FILE = "./src/main/resources/" + filename + ".pdf";
        PdfWriter.getInstance(document, new FileOutputStream(FILE));
        document.setPageCount(2);
        document.open();
        addContent(document, map, header1, header2);
        document.close();
    }

    private <T> void addContent(Document document, Map<String, String> map, String header1, String header2) throws DocumentException, NoSuchFieldException, IllegalAccessException {
        document.newPage();
        Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
                Font.BOLD);
        Anchor anchor = new Anchor("Raport", catFont);
        anchor.setName("Raport");

        Chapter catPart = new Chapter(new Paragraph(anchor), 1);

        Paragraph subPara = new Paragraph("", catFont);
        Section subCatPart = catPart.addSection(subPara);

        createTable(subCatPart, map, header1, header2);
        document.add(anchor);
        document.add(subCatPart);
        document.add(subPara);
    }

    private <T> void createTable(Section subCatPart, Map<String, String> map, String header1, String header2)
            throws BadElementException, NoSuchFieldException, IllegalAccessException {
        PdfPTable table = new PdfPTable(2);
        PdfPCell c1 = new PdfPCell(new Phrase(header1));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase(header2));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);


        table.setHeaderRows(1);
        for (String key : map.keySet()) {
            table.addCell(key);
            table.addCell(map.get(key));
        }
        subCatPart.add(table);

    }

}
