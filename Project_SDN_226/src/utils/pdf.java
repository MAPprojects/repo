package utils;

import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.UnitValue;
import entities.Candidate;
import entities.Department;
import services.StaffManager;
import java.io.File;
import java.util.ArrayList;


public class pdf {
    private  String DEST = "a.pdf";
    private StaffManager service;

    public pdf(String DEST, StaffManager service) {
        this.DEST = DEST;
        this.service = service;
    }

    public void makePDF() throws Exception {
        File file = new File(DEST);
        this.manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        pdfDoc.setDefaultPageSize(PageSize.A4);
        Document doc = new Document(pdfDoc);

        Paragraph p = new Paragraph("Top 3 departments");
        p.setBold();
        p.setHorizontalAlignment(HorizontalAlignment.CENTER);
        p.setPaddingLeft(200f);
        p.setPaddingBottom(50f);
        doc.add(p);

        Table table = new Table(3);
        table.addHeaderCell("Name");
        table.addHeaderCell("Size");
        table.addHeaderCell("Number of Choices");
        table.getHeader().setBold();
        table.getHeader().setBackgroundColor(Color.GRAY,0.5f);

        ArrayList<Object> res = new ArrayList<>();

        res = service.findTop3Departments();

        for(int i = 0; i<res.size() ; i=i+2){
            table.addCell(((Department)res.get(i+1)).getName());
            table.addCell(((Department)res.get(i+1)).getNrLoc().toString());
            table.addCell(res.get(i).toString());
        }
        doc.add(table);


        Paragraph p2 = new Paragraph("Top 15 students with most choices ");
        p2.setBold();
        p2.setHorizontalAlignment(HorizontalAlignment.CENTER);
        p2.setPaddingLeft(200f);
        p2.setPaddingTop(50f);
        p2.setPaddingBottom(50f);
        doc.add(p2);


        Table table2 = new Table(4);
        table2.getColumnWidth(0).setValue(10f);
        table2.getColumnWidth(3).setValue(10f);

        table2.addHeaderCell("ID");
        table2.addHeaderCell("Name");
        table2.addHeaderCell("Email");
        table2.addHeaderCell("Number of Choices");
        table2.getHeader().setBold();
        table2.getHeader().setBackgroundColor(Color.GRAY,0.5f);


        ArrayList<Object> res2 = new ArrayList<>();

        res2 = service.findTop15Candidates();

        for(int i = 0; i<res2.size() ; i=i+2){
            table2.addCell(((Candidate)res2.get(i+1)).getId().toString());
            table2.addCell(((Candidate)res2.get(i+1)).getName());
            table2.addCell(((Candidate)res2.get(i+1)).getEmail());
            table2.addCell(res2.get(i).toString());
        }


        doc.add(table2);


        Paragraph p3 = new Paragraph("Top 15 students with least choices ");
        p3.setBold();
        p3.setHorizontalAlignment(HorizontalAlignment.CENTER);
        p3.setPaddingLeft(200f);
        p3.setPaddingTop(50f);
        p3.setPaddingBottom(80f);
        doc.add(p3);


        Table table3 = new Table(4);
        table3.getColumnWidth(0).setValue(10f);
        table3.getColumnWidth(3).setValue(10f);
        table3.addHeaderCell("ID");
        table3.addHeaderCell("Name");
        table3.addHeaderCell("Email");
        table3.addHeaderCell("Number of Choices");
        table3.getHeader().setBold();
        table3.getHeader().setBackgroundColor(Color.GRAY,0.5f);



        ArrayList<Object> res3 = new ArrayList<>();

        res3 = service.findTop15CandidatesLeast();


        for(int i = 0; i<res3.size() ; i=i+2){
            table3.addCell(((Candidate)res3.get(i+1)).getId().toString());
            table3.addCell(((Candidate)res3.get(i+1)).getName());
            table3.addCell(((Candidate)res3.get(i+1)).getEmail());
            table3.addCell(res3.get(i).toString());
        }

        doc.add(table3);



        doc.close();
    }
}
