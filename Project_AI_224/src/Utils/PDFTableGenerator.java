package Utils;

import Domain.Student;
import Domain.TableProject;
import Service.ApplicationService;
import com.itextpdf.awt.DefaultFontMapper;
import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;
import org.jfree.chart.JFreeChart;

import java.awt.*;
import java.io.FileOutputStream;
import java.util.*;
import java.util.List;

public class PDFTableGenerator {
    private ApplicationService service;

    public PDFTableGenerator(ApplicationService service) {
        this.service = service;
    }

    public void createPDF(String filename, String title) {
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(filename+"\\"+title+".pdf"));
            document.open();
            document.addTitle(title);
            addTitlePage(document, title);
            generateTable(document, title);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createChartPDF(String filename, String title, JFreeChart jChart){
        try {
            Document document = new Document(new Rectangle(700, 700));
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filename+"\\"+title+".pdf"));
            document.open();
            document.addTitle(title);
            PdfContentByte cb = writer.getDirectContent();
            PdfTemplate tp = cb.createTemplate(500, 500);
            Graphics2D g2d = tp.createGraphics(500, 500, new DefaultFontMapper());
            java.awt.Rectangle r2d = new java.awt.Rectangle(0, 0, 500, 500);
            jChart.draw(g2d, r2d);
            g2d.dispose();
            cb.addTemplate(tp, 0, 0);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void generateTable(Document document, String title) throws DocumentException {
        switch (title){
            case "Passed":
                generatePassedTable(document);
                break;
            case "HardestProjects":
                generateHardestProjectsTable(document);
                break;
            case "InTime":
                generateInTimeTable(document);
                break;
        }
    }

    private void generateInTimeTable(Document document) throws DocumentException {
        PdfPTable table = new PdfPTable(4);
        PdfPCell c1 = new PdfPCell(new Phrase("Cod Matricol"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Name"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Group"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Teacher"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        List<Student> inTimeStudents = this.service.getInTimeStudents();
        if(inTimeStudents.size()==0){
            return;
        }



        for(Student student : inTimeStudents){
            table.addCell(student.getCodMatricol());
            table.addCell(student.getName());
            table.addCell(String.valueOf(student.getGroup()));
            table.addCell(student.getTeacher());
        }

        Paragraph paragraph = new Paragraph();
        paragraph.add(table);

        document.add(new Paragraph(" "));

        document.add(paragraph);

    }

    private void generatePassedTable(Document document) throws DocumentException {
        PdfPTable table = new PdfPTable(5);
        PdfPCell c1 = new PdfPCell(new Phrase("Cod Matricol"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Name"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Group"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Teacher"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Final grade"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        HashMap<Student, Integer> passedStudents = this.service.getPassedStudents();
        List<Student> students = this.service.studentService.getAllStudents();
        for(Student student : students){
            if(passedStudents.get(student)!=null){
                table.addCell(student.getCodMatricol());
                table.addCell(student.getName());
                table.addCell(String.valueOf(student.getGroup()));
                table.addCell(student.getTeacher());
                table.addCell(String.valueOf(passedStudents.get(student)));
            }
        }

        Paragraph paragraph = new Paragraph();
        paragraph.add(table);

        document.add(new Paragraph(" "));

        document.add(paragraph);
    }

    private void generateHardestProjectsTable(Document document) throws DocumentException {
        PdfPTable table = new PdfPTable(3);
        PdfPCell c1 = new PdfPCell(new Phrase("Project Description"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Deadline"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Number of delayed assignements"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        HashMap<TableProject, Integer> hardestProjects = service.getHardestProjects();
        Iterator it = hardestProjects.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            TableProject project = (TableProject) pair.getKey();
            table.addCell(project.getDescription());
            table.addCell(String.valueOf(project.getDeadline()));
            table.addCell(String.valueOf(hardestProjects.get(project)));
            it.remove();
        }

        Paragraph paragraph = new Paragraph();
        paragraph.add(table);

        document.add(new Paragraph(" "));

        document.add(paragraph);

    }

    private static void addTitlePage(Document document, String title)
            throws DocumentException {
        Paragraph preface = new Paragraph();

        preface.add(new Paragraph());

        preface.add(new Paragraph(title, new Font(Font.FontFamily.TIMES_ROMAN, 15)));

        preface.add(new Paragraph());

        preface.add(new Paragraph(
                "Report generated at: " + new Date(),
                new Font(Font.FontFamily.COURIER, 12, Font.BOLD)));

        preface.add(new Paragraph());

        document.add(preface);
    }



}

