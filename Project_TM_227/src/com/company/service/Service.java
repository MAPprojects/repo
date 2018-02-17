package com.company.service;

import com.company.domain.Nota;
import com.company.domain.Student;
import com.company.domain.Tema;
import com.company.repository.*;

import com.google.common.collect.Ordering;
import com.google.common.collect.TreeMultimap;
import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.sun.mail.util.MailConnectException;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import java.awt.*;
import java.io.UnsupportedEncodingException;
import javax.mail.*;
import javax.mail.internet.*;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import java.io.*;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.util.*;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;


public class Service {
    private int saptamana = 1;
    private RepositoryStudent repositoryStudent;
    private RepositoryTema repositoryTema;
    private RepositoryNota repositoryNota;

    public Service(RepositoryStudent repositoryStudent, RepositoryTema repositoryTema, RepositoryNota repositoryNota) {
        this.repositoryStudent = repositoryStudent;
        this.repositoryTema = repositoryTema;
        this.repositoryNota = repositoryNota;
    }

    public void setSaptamana(int saptamana){
        this.saptamana = saptamana;
    }

    public int getSaptamana(){
        return this.saptamana;
    }

    public Iterable<Student> getStudents() {
        return repositoryStudent.findAll();
        }

    public Iterable<Tema> getTeme() {
        return repositoryTema.findAll();
    }

    public Iterable<Nota> getNote() {
        return repositoryNota.findAll();
    }


    public void addStudent(int idStudent, String nume, int grupa, String email, String cadru_didactic) throws Exception {
        Student s = new Student(idStudent, nume, grupa, email, cadru_didactic);
        repositoryStudent.save(s);
    }

    public void deleteStudent(int idStudent){
        repositoryStudent.delete(idStudent);
        repositoryStudent.saveStream();
        deleteNoteForStudent(idStudent);
        String filename = idStudent+ ".txt";
        File fw = new File(filename);
        fw.delete();
    }

    private void deleteNoteForStudent(int idStudent) {
        int id =repositoryNota.findNotaForStudent(idStudent);
        while (id!=0){
            repositoryNota.delete(id);
            id = repositoryNota.findNotaForStudent(idStudent);
        }
    }

    public Student findStudentForId(int idStudent){
        Student s = repositoryStudent.findOne(idStudent);
        return s;
    }


    public void updateStudent(int idStudent, String nume, int grupa, String email, String cadru_didactic) throws Exception {
        Student s = repositoryStudent.findOne(idStudent);
        s.setNume(nume);
        s.setGrupa(grupa);
        s.setEmail(email);
        s.setCadru_didactic(cadru_didactic);
        repositoryStudent.update(s.getId(),s);
    }

    public void addTema(int id_tema, String cerinta, int deadline) throws Exception {
        Tema t = new Tema(id_tema, cerinta, deadline);
        repositoryTema.save(t);
    }

    public void updateDeadline(int idTema, int newDeadline)throws Exception {
        Tema t = repositoryTema.findOne(idTema);
        if (newDeadline <1 || newDeadline>14)
            throw new Exception("Deadline-ul nu respecta intervalul");
        else
            if (t.getDeadline() - getSaptamana() < 1)
                throw new Exception("Prea tarziu pentru a modifica termenul limita");
        else
            if (newDeadline > t.getDeadline()) {
                t.setDeadline(newDeadline);
                repositoryTema.update(t.getId(), t);
            }
        else
            throw new Exception("Noul termen este mai mic sau egal decat cel vechi " + t.getDeadline());
    }

    public void sendMail(String email, String file){
        // Recipient's email ID needs to be mentioned.
        String to = email;

        // Sender's email ID needs to be mentioned
        String from = "trifamaria96@gmail.com";

        // Assuming you are sending email from localhost
        String host = "smtp.gmail.com";

        // Get system properties
        Properties properties = System.getProperties();

        // Setup mail server
        //properties.setProperty("mail.smtp.host", host);
        //properties.put("mail.smtp.port", "888");


        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.starttls.enable", "true");
        properties.setProperty("mail.smtp.host", "smtp.gmail.com");
        properties.setProperty("mail.smtp.port", "587");
        properties.setProperty("mail.smtp.user", "trifamaria96@gmail.com");
        properties.setProperty("mail.smtp.password", "ingernebun96");

        // Get the default Session object.
        Session session = Session.getDefaultInstance(properties);

        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from, "MAP"));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));

            // Set Subject: header field
            message.setSubject("Situatie note MAP");

            // Create the message part
            BodyPart messageBodyPart = new MimeBodyPart();

            // Fill the message
            messageBodyPart.setText("Documentul atasat contine situatia dumneavoastra la MAP. Va rog sa consultati documentul.");

            // Create a multipar message
            Multipart multipart = new MimeMultipart();

            // Set text message part
            multipart.addBodyPart(messageBodyPart);

            // Part two is attachment
            messageBodyPart = new MimeBodyPart();
           // String filename = file;
            DataSource source = new FileDataSource(file);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(file);
            multipart.addBodyPart(messageBodyPart);

            // Send the complete message parts
            message.setContent(multipart );

            Transport transport = session.getTransport("smtp");
            String SMTP_OUT_SERVER = "smtp.gmail.com";
            String USER = "trifamaria96@gmail.com"; // godaddy domain
            String PASSWORD = "ingernebun96";
            transport.connect(SMTP_OUT_SERVER, USER, PASSWORD);
            transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
            transport.close();


            System.out.println("Mail-ul a fost trimis cu succes");
        } catch (MailConnectException e){
            System.out.println("Eroare la conectare gmail");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    public void addNota(int idStudent, int idTema, int valoare, String observatie) throws Exception {
        Student s = repositoryStudent.findOne(idStudent);
        Tema t = repositoryTema.findOne(idTema);

        if (repositoryNota.findNota(idStudent, idTema) != null)
            throw new Exception("Acest student are deja nota la aceasta tema!");

        int idNota = repositoryNota.getIdNota();
        int newValoare;
        if (t.getDeadline() == getSaptamana() || getSaptamana() < t.getDeadline())
            newValoare = valoare;
        else if (getSaptamana() - t.getDeadline() == 1) {
            newValoare = valoare - 2;
            observatie = observatie + " O saptamana intarziere. ";
        }
        else if (getSaptamana() - t.getDeadline() == 2){
            newValoare = valoare - 4;
            observatie = observatie + " Doua saptamani intarziere. ";
        }
        else {
            newValoare = 1;
            observatie = observatie + " Trei saptamani intarziere. ";
        }

        Nota n = new Nota(idNota, idStudent, idTema, newValoare, observatie);
        repositoryNota.save(n);

        String filename = s.getId() + ".txt";

        File fw = new File(filename);
        fw.createNewFile();
        BufferedWriter bw = new BufferedWriter(new FileWriter(filename, true));
        String line = "Adaugare nota" + " Id tema " + t.getId() + " Valoare " + newValoare + " Deadline " + t.getDeadline() + " Saptamana predarii " + getSaptamana() + " Observatii " + observatie +  "\n";
        bw.append(line);
        bw.close();

        Thread thread = new Thread(() -> {
            sendMail(s.getEmail(), filename);
        });
        thread.start();
    }

    public void changeNota(int idStudent, int idTema, int valoare, String observatie) throws Exception {
        Student s = repositoryStudent.findOne(idStudent);
        Tema t = repositoryTema.findOne(idTema);

        Nota n = repositoryNota.findNota(idStudent, idTema);
        if (n == null)
            throw new Exception("Acest student nu are nota la aceasta tema!");

        int newValoare;
        if (t.getDeadline() == getSaptamana() || getSaptamana() < t.getDeadline())
            newValoare = valoare;
        else if (getSaptamana() - t.getDeadline() == 1){
            newValoare = valoare - 2;
            observatie = observatie + " O saptamana intarziere. ";
        }
        else if (getSaptamana() - t.getDeadline() == 2){
            newValoare = valoare - 4;
            observatie = observatie + " Doua saptamani intarziere. ";
        }
        else{
            newValoare = 1;
            observatie = observatie + " Trei saptamani intarziere. ";
        }
        if (newValoare < n.getValoare())
            throw new Exception("Noua nota optinuta este mai mica decat cea veche!");

        n.setValoare(newValoare);
        n.setObservatie(observatie);
        repositoryNota.update(n.getId(), n);

        String filename = s.getId() + ".txt";

        FileWriter fw = new FileWriter(filename, true);
        BufferedWriter bw = new BufferedWriter(fw);
        String line = "Modificare nota" + " Id tema " + t.getId() + " Valoare " + newValoare + " Deadline " + t.getDeadline() + " Saptamana predarii " + getSaptamana() + " Observatii " + observatie +  "\n";
        bw.append(line);
        bw.close();
        fw.close();

        Thread thread = new Thread(() -> {
            sendMail(s.getEmail(), filename);
        });
        thread.start();
    }

    public long getNrTeme(){
        return repositoryTema.size();
    }

    private static void insertCell(PdfPTable table, String text, int align, int colspan, Font font) {

        //create a new cell with the specified Text and Font
        PdfPCell cell = new PdfPCell(new Phrase(text.trim(), font));
        //set the cell alignment
        cell.setHorizontalAlignment(align);
        //set the cell column span in case you want to merge two or more cells
        cell.setColspan(colspan);
        //in case there is no text and you wan to create an empty row
        cell.setMinimumHeight(10f);
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        //add the call to the table
        table.addCell(cell);
    }

    public void savePDFStud(TreeMultimap<Double, Student> map) throws FileNotFoundException {
        Font bfBold12 = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD, new BaseColor(160, 0, 0));
        Font bf12 = new Font(Font.FontFamily.TIMES_ROMAN, 12);
        Font bf12r = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL, new BaseColor(200, 0, 0));
        Font titlu = new Font(Font.FontFamily.TIMES_ROMAN, 20, Font.BOLD, new BaseColor(200, 0, 0));
        DecimalFormat df = new DecimalFormat("0.00");

        Document document = new Document();
        try
        {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("RaportStudenti.pdf"));
            document.open();
            Paragraph title = new Paragraph("Raport privind media fiecarui student:",titlu);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            title.setFont(new Font(Font.FontFamily.TIMES_ROMAN, 50, Font.BOLDITALIC));
            document.add(title);

            Paragraph paragraph = new Paragraph();
            float[] columnWidths = {1f, 2f, 1f, 2f, 1f};
            PdfPTable table = new PdfPTable(columnWidths);
            insertCell(table, "Id Student", Element.ALIGN_CENTER, 1, bfBold12);
            insertCell(table, "Nume", Element.ALIGN_CENTER, 1, bfBold12);
            insertCell(table, "Grupa", Element.ALIGN_CENTER, 1, bfBold12);
            insertCell(table, "Email", Element.ALIGN_CENTER, 1, bfBold12);
            insertCell(table, "Medie", Element.ALIGN_CENTER, 1, bfBold12);
            table.setHeaderRows(1);

            for (double key : map.keySet()){
                if (key>=5)
                    for (Student s : map.get(key)) {
                        insertCell(table, String.valueOf(s.getId()), Element.ALIGN_CENTER, 1, bf12);
                        insertCell(table, s.getNume(), Element.ALIGN_CENTER, 1, bf12);
                        insertCell(table, String.valueOf(s.getGrupa()), Element.ALIGN_CENTER,1, bf12);
                        insertCell(table, s.getEmail(), Element.ALIGN_CENTER, 1, bf12);
                        insertCell(table, String.valueOf(df.format(key)), Element.ALIGN_CENTER,1, bf12);
                    }
                    else
                        for (Student s : map.get(key)) {
                            insertCell(table, String.valueOf(s.getId()), Element.ALIGN_CENTER, 1, bf12);
                            insertCell(table, s.getNume(), Element.ALIGN_CENTER, 1, bf12);
                            insertCell(table, String.valueOf(s.getGrupa()), Element.ALIGN_CENTER,1, bf12);
                            insertCell(table, s.getEmail(), Element.ALIGN_CENTER, 1, bf12);
                            insertCell(table, String.valueOf(df.format(key)), Element.ALIGN_CENTER,1, bf12r);
                        }
            }

            paragraph.add(table);
            document.add(paragraph);

            document.close();
            writer.close();
        } catch (FileNotFoundException e) {
            throw (e);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    public void raportStudenti() throws FileNotFoundException {
        Iterable<Student> students = repositoryStudent.findAll();
        Iterable<Nota> notas = repositoryNota.findAll();
        Comparator<Student> c = (y, z) -> {
            return y.getId().compareTo(z.getId());
        };
        TreeMultimap<Double, Student> map = TreeMultimap.create(Ordering.natural().reverse(), c);
        for(Student s:students){
            double medie = 0;
            for (Nota n : notas){
                if (s.getId() == n.getIdStudent())
                    medie += n.getValoare();
            }
            medie = medie/repositoryTema.size();

            map.put(medie, s);
        }
        savePDFStud(map);
    }

    public void savePDFTem(TreeMultimap<Long, Tema> map, long nr) throws FileNotFoundException {
        Font bfBold12 = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD, new BaseColor(160, 0, 0));
        Font bf12 = new Font(Font.FontFamily.TIMES_ROMAN, 12);
        Font bf12r = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL, new BaseColor(200, 0, 0));
        Font titlu = new Font(Font.FontFamily.TIMES_ROMAN, 20, Font.BOLD, new BaseColor(200, 0, 0));
        DecimalFormat df = new DecimalFormat("0.00");

        Document document = new Document();
        try
        {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("RaportTeme.pdf"));
            document.open();
            Paragraph title = new Paragraph("Raport privind gradul de predare a fiecarei teme:", titlu);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            title.setFont(new Font(Font.FontFamily.TIMES_ROMAN, 50, Font.BOLDITALIC));
            document.add(title);

            Paragraph paragraph = new Paragraph();
            float[] columnWidths = {1f, 2f, 1f, 2f, 2f};
            PdfPTable table = new PdfPTable(columnWidths);
            insertCell(table, "Id Tema", Element.ALIGN_CENTER, 1, bfBold12);
            insertCell(table, "Cerinta", Element.ALIGN_CENTER, 1, bfBold12);
            insertCell(table, "Deadline", Element.ALIGN_CENTER, 1, bfBold12);
            insertCell(table, "Nr stud care au predat", Element.ALIGN_CENTER, 1, bfBold12);
            insertCell(table, "Nr stud care nu au predat", Element.ALIGN_CENTER, 1, bfBold12);
            table.setHeaderRows(1);

            for (long key : map.keySet()){
                for (Tema t : map.get(key)) {
                    insertCell(table, String.valueOf(t.getId()), Element.ALIGN_CENTER, 1, bf12);
                    insertCell(table, t.getCerinta(), Element.ALIGN_CENTER, 1, bf12);
                    insertCell(table, String.valueOf(t.getDeadline()), Element.ALIGN_CENTER,1, bf12);
                    insertCell(table, String.valueOf(key), Element.ALIGN_CENTER, 1, bf12);
                    insertCell(table, String.valueOf(nr-key), Element.ALIGN_CENTER,1, bf12);
                    }
            }

            paragraph.add(table);
            document.add(paragraph);

            document.close();
            writer.close();
        } catch (FileNotFoundException e) {
            throw (e);
        } catch (DocumentException e) {
            e.printStackTrace();
        }

    }

    public void raportTeme() throws FileNotFoundException {
        Iterable<Tema> temas = repositoryTema.findAll();
        Iterable<Nota> notas = repositoryNota.findAll();
        Comparator<Tema> c = (y, z) -> {
            return y.getId().compareTo(z.getId());
        };
        TreeMultimap<Long, Tema> map = TreeMultimap.create(Ordering.natural().reverse(), c);
        long nr;
        for (Tema t : temas){
            nr = 0;
            for (Nota n : notas){
                if (t.getId() == n.getIdTema())
                    nr += 1;
            }
            map.put(nr, t);
        }
        nr = repositoryStudent.size();
        savePDFTem(map, nr);
    }

    public void savePDFNot(Map<Integer, Long> map) throws FileNotFoundException {
        Document document = new Document();
        try
        {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("RaportNote.pdf"));
            document.open();
            Paragraph title = new Paragraph();
            final DefaultCategoryDataset dataset = new DefaultCategoryDataset( );


            for (Integer key : map.keySet())
            {
                dataset.addValue(map.get(key), "note", key);
            }

            JFreeChart barChart = ChartFactory.createBarChart3D(
                    "Grafic note",
                    "Nota",
                    "Numar studenti",
                    dataset,
                    PlotOrientation.VERTICAL,
                    true, true, false);

            int width = 540; /* Width of the image */
            int height = 480; /* Height of the image */
            File barChart3D = new File( "barChart3D.jpeg" );
            ChartUtilities.saveChartAsJPEG( barChart3D, barChart, width, height);

            Image image = Image.getInstance("barChart3D.jpeg");
            title.add(image);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            document.close();
            writer.close();
        } catch (FileNotFoundException e) {
            throw (e);
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void raportNote() throws FileNotFoundException{
        Iterable<Nota> notas = repositoryNota.findAll();
        Map<Integer, Long> map = new HashMap<>();
        for (Nota n : notas){
            int key = n.getValoare();
            if (map.containsKey(key)){
                long values = map.get(key) + 1;
                map.put(key, values);
            }
            else
                map.put(key, (long) 1);
        }
        savePDFNot(map);
    }

    public List<Nota> getNoteStudent(int idStudent) {
        Student student = repositoryStudent.findOne(idStudent);
        Iterable<Nota> notas = repositoryNota.findAll();
        List<Nota> note = new ArrayList<Nota>();
        for (Nota n : notas) {
            if (student.getId() == n.getIdStudent()) {
                note.add(n);
            }
        }
        return note;
    }



    public  <E> List<E> filterList(List<E> l, Predicate<E> pr, Comparator<E> c) {
        return l.stream().filter(pr).sorted(c).collect(Collectors.toList());
    }

    public <E> List<E> toList(CRUDRepository<Integer,E> r){
        List<E> l = new ArrayList<>();
        for(E el:r.findAll()){
            l.add(el);
        }
        return l;
    }

    public List<Student> filtreazaStudentiDupaNume(String nume) {
        Predicate<Student> pr = x -> x.getNume().equals(nume);
        Comparator<Student> c = (y, z) -> {
            if (y.getId() < z.getId())
                return -1;
            else if (y.getId() > z.getId())
                return 1;
            else
                return 0;
        };
        List<Student> l = toList(repositoryStudent);
        return filterList(l, pr, c);
    }

    public List<Student> filtreazaStudentiDupaCadru(String nume) {
        Predicate<Student> pr = x -> x.getCadru_didactic().equals(nume);
        Comparator<Student> c = (y, z) -> {
            return y.getNume().compareTo(z.getNume());
        };
        List<Student> l = toList(repositoryStudent);
        return filterList(l, pr, c);
    }

    public List<Student> filtreazaStudentiDupaGrupa(int grupa) {
        Predicate<Student> pr = x -> x.getGrupa()==grupa;
        Comparator<Student> c = (y, z) -> {
            return y.getNume().compareTo(z.getNume());
        };
        List<Student> l = toList(repositoryStudent);
        return filterList(l, pr, c);
    }

    public List<Tema> filtreazaTemaDupaCerinta(String nume){
        List<Tema> li = toList(repositoryTema);
        List<Tema> l=filterList(li,x->x.getCerinta().equals(nume),(y,z)->{return y.getCerinta().compareTo(z.getCerinta());});
        return l;
    }
    public List<Tema> filtreazaTemaDupaDeadline(int deadline){
        List<Tema> li = toList(repositoryTema);
        List<Tema> l=filterList(li,x->x.getDeadline()==deadline,(y,z)->{return y.getCerinta().compareTo(z.getCerinta());});
        return l;
    }
    public List<Tema> filtreazaTemaDupaCerintaSiDeadline(String nume,int deadline){

        List<Tema> li = toList(repositoryTema);
        Predicate<Tema> pr=x->x.getCerinta().equals(nume);
        List<Tema> l=filterList(li,pr.and(x->x.getDeadline()==deadline),(y,z)->{return y.getCerinta().compareTo(z.getCerinta());});
        return l;
    }

    public List<Nota> filtreazaNoteDupaNota(int nota){

        List<Nota> li = toList(repositoryNota);
        Comparator<Nota> c=(x,y)->{if(x.getValoare()<y.getValoare()) return 1; else if(x.getValoare()>y.getValoare()) return-1; else return 0; };
        List<Nota> l=filterList(li,x->x.getValoare()==nota,c);
        return l;
    }

    public List<Nota> filtreazaNoteDupaStudent(int id){

        List<Nota> li = toList(repositoryNota);
        Comparator<Nota> c=(x,y)->{if(x.getIdStudent()<y.getIdStudent()) return 1; else if(x.getIdStudent()>y.getIdStudent()) return-1; else return 0; };
        List<Nota> l=filterList(li,x->x.getIdStudent()==id,c);
        return l;
    }

    public List<Nota> filtreazaNoteDupaNrTema(int id){

        List<Nota> li = toList(repositoryNota);
        Comparator<Nota> c=(x,y)->{if(x.getIdTema()<y.getIdTema()) return 1; else if(x.getIdTema()>y.getIdTema()) return-1; else return 0; };
        List<Nota> l=filterList(li,x->x.getIdTema()==id,c);
        return l;
    }

    public ArrayList geaAllGrupe(){
        ArrayList<Student> students = (ArrayList<Student>) toList(repositoryStudent);
        ArrayList grupe = new ArrayList<>();
        students.forEach(s -> {if (!grupe.contains(s.getGrupa())) grupe.add(s.getGrupa());});
        return grupe;
    }

    public ArrayList getAllDeadline(){
        ArrayList<Tema> temas = (ArrayList<Tema>) toList(repositoryTema);
        ArrayList deadline = new ArrayList<>();
        temas.forEach(t->{if (!deadline.contains(t.getDeadline())) deadline.add(t.getDeadline());});
        return deadline;
    }

    public ArrayList getAllNota(){
        ArrayList<Nota> notas = (ArrayList<Nota>) toList(repositoryNota);
        ArrayList rez = new ArrayList<>();
        notas.forEach(n->{if (!rez.contains(n.getValoare())) rez.add(n.getValoare());});
        return rez;
    }

    public ArrayList getAllIdStudenta(){
        ArrayList<Nota> notas = (ArrayList<Nota>) toList(repositoryNota);
        ArrayList rez = new ArrayList<>();
        notas.forEach(n->{if (!rez.contains(n.getIdStudent())) rez.add(n.getIdStudent());});
        return rez;
    }

    public ArrayList getAllIdTema(){
        ArrayList<Nota> notas = (ArrayList<Nota>) toList(repositoryNota);
        ArrayList rez = new ArrayList<>();
        notas.forEach(n->{if (!rez.contains(n.getIdTema())) rez.add(n.getIdTema());});
        return rez;
    }
}

