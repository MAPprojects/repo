package FXML;



import java.awt.geom.Rectangle2D;
import java.io.FileNotFoundException;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.awt.*;
import java.io.File;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;


import Domain.Note;
import Domain.Studenti;

import Domain.Teme;
import Repository.*;

import Service.*;
import Service.Service;
import Utils.ListEvent;
import Utils.Observer;
import com.itextpdf.text.*;
import com.itextpdf.text.Font;


import com.itextpdf.text.pdf.*;
import javafx.beans.binding.Bindings;

import javafx.collections.FXCollections;


import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;


import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

public class NotaControllerFXML implements Observer<Studenti> {
    Stage stage1;
    AnchorPane pane;
    public void setStage(Stage stage1){
        this.stage1=stage1;
    }
    public void setPane(AnchorPane pane){
        this.pane=pane;
    }

    StudentRepoSQL repoStudenti = new StudentRepoSQL(new StudentValidator());
    TemeRepoSQL repoTeme = new TemeRepoSQL(new TemeValidator());
    NoteRepoSQL noteRepo = new NoteRepoSQL(new NoteValidate());
    ServiceNote noteService = new ServiceNote(noteRepo);
    ServiceStudenti studentiService = new ServiceStudenti(repoStudenti);
    ServiceTeme temeService = new ServiceTeme(repoTeme);

    private static List<Teme> listaTeme;
    private static List<Note> listaNote;
    private static  Studenti studentSelect;
    Service service = new Service(noteService, temeService, studentiService);

    private Studenti studentSelected;
    private NotaControllerFXML notaController = this;

    private final static int rowsPerPage = 6;
    private static String FILE = "D:\\MyUni\\An2 sem1\\java\\Lab3-6\\src\\PDF\\1.pdf";
    private static String FILE1 = "D:\\MyUni\\An2 sem1\\java\\Lab3-6\\src\\PDF\\2.pdf";
    private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
            Font.BOLD);
    private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.NORMAL, BaseColor.RED);
    private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16,
            Font.BOLD);
    private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.BOLD);
    @FXML
    private Label labelPen;
    @FXML
    private VBox vbox;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableView<Studenti> table;

    @FXML
    private TableColumn<Studenti, String> c_numeStudent;

    @FXML
    private TableColumn<Studenti, Integer> c_Grupa;

    @FXML
    private TableColumn<Studenti, String> c_butonAdauga;

    @FXML
    private TableColumn<Studenti, String> c_viewButton;
    @FXML
    private Pagination paginator;

    @FXML
    private TableColumn<Studenti, Float> c_medie;
    @FXML
    private Button buttonPDF;
    @FXML
    private TextField tf_searchNume;

    @FXML
    private TableColumn<Studenti, String> c_SendEmail;

    @FXML
    private TextField tf_searchGrupa;

    @FXML
    private Label labelPen1;
    @FXML
    void handleStudentiExamen(ActionEvent event) {

    }

    private void createTable(Section subCatPart) throws BadElementException, SQLException {
        PdfPTable table = new PdfPTable(4);
        PdfPCell c1 = new PdfPCell(new Phrase(""));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Nota"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Deadline"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);
        table.setHeaderRows(1);

        c1 = new PdfPCell(new Phrase("Predare"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);
        table.setHeaderRows(1);
        for(Teme teme :temeService.getAllTeme()) {

            c1 = new PdfPCell(new Phrase("Tema" + String.valueOf(teme.getNrTema())));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);
            boolean contor=false;
            for (Note nota : noteService.getAllNote()) {

                if (nota.getNrTema()==teme.getNrTema()) {
                    if(nota.getIdStudent() == studentSelect.getIdStudent()) {
                        table.addCell(String.valueOf(nota.getValoare()));
                        table.addCell(String.valueOf(String.valueOf(teme.getDeadline())));
                        table.addCell(String.valueOf(noteRepo.getSpatamnaPredare(nota.getIdStudent(), nota.getNrTema())));
                    }


                }



            }
        }



        subCatPart.add(table);

    }

    private static void createList(Section subCatPart) {
        com.itextpdf.text.List list = new com.itextpdf.text.List(true, false, 10);

    }

    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }


    private  void addContent(Document document) throws DocumentException, SQLException {
        Anchor anchor = new Anchor("Note", catFont);
        anchor.setName("1p");

        Chapter catPart = new Chapter(new Paragraph(anchor), 1);

        Paragraph subPara = new Paragraph("Notele dumneavoastra", subFont);
        Section subCatPart = catPart.addSection(subPara);


        // add a list
        createList(subCatPart);
        Paragraph paragraph = new Paragraph();
        addEmptyLine(paragraph, 5);
        subCatPart.add(paragraph);

        // add a table
        createTable(subCatPart);

        document.add(catPart);

        // Next section
        anchor = new Anchor("Second Chapter", catFont);
        anchor.setName("Second Chapter");

        // Second parameter is the number of the chapter
        catPart = new Chapter(new Paragraph(anchor), 1);

        subPara = new Paragraph("Subcategory", subFont);
        subCatPart = catPart.addSection(subPara);
        subCatPart.add(new Paragraph("This is a very important message"));

        // now add all this to the document
        document.add(catPart);

    }



    /*RAPOARTE*?

     */

    private void createTablerRapotExamen(Section subCatPart) throws BadElementException, SQLException {
        PdfPTable table = new PdfPTable(4);
        PdfPCell c1 = new PdfPCell(new Phrase("Nume si prenume"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Grupa"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);
        table.setHeaderRows(1);
        c1 = new PdfPCell(new Phrase("Indrumator"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);
        table.setHeaderRows(1);
        c1 = new PdfPCell(new Phrase("Medie"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);
        table.setHeaderRows(1);

        for (Studenti studenti : service.studentiCarePotIntraInExamen()) {
            table.addCell(String.valueOf(studenti.getNume()));
            table.addCell(String.valueOf(String.valueOf(studenti.getGrupa())));
            table.addCell(studenti.getIndrumator());
            table.addCell(String.valueOf(studenti.getMedie()));
        }


        subCatPart.add(table);

    }

    private void createTablerRapotNepenalizati(Section subCatPart) throws BadElementException, SQLException {
        PdfPTable table = new PdfPTable(3);
        PdfPCell c1 = new PdfPCell(new Phrase("Nume si prenume"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Grupa"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);
        table.setHeaderRows(1);
        c1 = new PdfPCell(new Phrase("Indrumator"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);
        table.setHeaderRows(1);


        for (Studenti studenti : service.predateLaTimp()) {
            if (service.areToateTemelePredate(studenti)) {
                table.addCell(String.valueOf(studenti.getNume()));
                table.addCell(String.valueOf(String.valueOf(studenti.getGrupa())));
                table.addCell(studenti.getIndrumator());
            }
        }


        subCatPart.add(table);

    }
    private void createTablerRapotTop10(Section subCatPart) throws BadElementException, SQLException {
        PdfPTable table = new PdfPTable(4);
        PdfPCell c1 = new PdfPCell(new Phrase("Nume si prenume"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Grupa"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);
        table.setHeaderRows(1);
        c1 = new PdfPCell(new Phrase("Indrumator"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);
        table.setHeaderRows(1);
        c1 = new PdfPCell(new Phrase("Media"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);
        table.setHeaderRows(1);



        for (Studenti studenti : service.primii10Studenti()) {
            if (service.areToateTemelePredate(studenti)) {
                table.addCell(String.valueOf(studenti.getNume()));
                table.addCell(String.valueOf(String.valueOf(studenti.getGrupa())));
                table.addCell(studenti.getIndrumator());
                table.addCell(String.valueOf(studenti.getMedie()));
            }
        }


        subCatPart.add(table);

    }



    private  void addContentRaportNepenalzati(Document document) throws DocumentException, SQLException {
        Anchor anchor = new Anchor("Rapoarte studenti", catFont);
        anchor.setName("1p");

        Chapter catPart = new Chapter(new Paragraph(anchor), 1);

        Paragraph subPara = new Paragraph("", subFont);
        Section subCatPart = catPart.addSection(subPara);
        subCatPart.add(new Paragraph("Toti studentii care nu au fost penalizati"));



        // add a table
        createTablerRapotNepenalizati(subCatPart);
        createList(subCatPart);
        Paragraph paragraph = new Paragraph();
        addEmptyLine(paragraph, 5);
        subCatPart.add(paragraph);


        createTablerRapotTop10(catPart);

        document.add(catPart);
//        // Next section
//        anchor = new Anchor("Second Chapter", catFont);
//        anchor.setName("Second Chapter");
//
//        // Second parameter is the number of the chapter
//        catPart = new Chapter(new Paragraph(anchor), 1);
//
//        subPara = new Paragraph("Subcategory", subFont);
//        subCatPart = catPart.addSection(subPara);
//        subCatPart.add(new Paragraph("This is a very important message"));
//
//        // now add all this to the document
//        document.add(catPart);

    }
    private  void addContentRaportExamen(Document document) throws DocumentException, SQLException {
        Anchor anchor = new Anchor("Rapoarte Studenti", catFont);
        anchor.setName("2p");

        Chapter catPart = new Chapter(new Paragraph(anchor), 1);

        Paragraph subPara = new Paragraph("", subFont);
        Section subCatPart = catPart.addSection(subPara);
        subCatPart.add(new Paragraph("Studenti care pot intra in examen"));

        // add a list
        createList(subCatPart);
        Paragraph paragraph = new Paragraph();
        addEmptyLine(paragraph, 5);
        subCatPart.add(paragraph);

        // add a table
        createTablerRapotExamen(subCatPart);

        document.add(catPart);



//        // Next section
//        anchor = new Anchor("Second Chapter", catFont);
//        anchor.setName("Second Chapter");
//
//        // Second parameter is the number of the chapter
//        catPart = new Chapter(new Paragraph(anchor), 1);
//
//        subPara = new Paragraph("Subcategory", subFont);
//        subCatPart = catPart.addSection(subPara);
//        subCatPart.add(new Paragraph("This is a very important message"));
//
//        // now add all this to the document
//        document.add(catPart);

    }
    @FXML
    void handlePDFCreateStatistics(ActionEvent event) throws IOException, DocumentException {

        writeChartToPDF(generatePieChart(), 400, 400, FILE1);



        File myFile = new File(FILE1);
        Desktop.getDesktop().open(myFile);
    }

    @FXML
    void handlePDFCreate(ActionEvent event) throws DocumentException, IOException, SQLException {


        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(FILE));
        document.open();

        addContentRaportNepenalzati(document);
        document.close();
        File myFile = new File(FILE);
        Desktop.getDesktop().open(myFile);

    }
    @FXML
    void handleFilterData(KeyEvent event) {

        FilteredList<Studenti> filteredList = new FilteredList<>(FXCollections.observableArrayList(studentiService.getAllStudents()));
        filteredList.predicateProperty().bind((Bindings.createObjectBinding(() ->
                        studenti -> studenti.getNume().toLowerCase().contains(tf_searchNume.getText()) &&
                                Integer.toString(studenti.getGrupa()).contains(tf_searchGrupa.getText()),

                tf_searchNume.textProperty(),
                tf_searchGrupa.textProperty()


        )));


        table.setItems(filteredList);

    }


    @FXML
    void handlePDFCreateExamen(ActionEvent event) throws DocumentException, IOException, SQLException {


        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(FILE));
        document.open();

        addContentRaportExamen(document);
        document.close();
        File myFile = new File(FILE);
        Desktop.getDesktop().open(myFile);

    }






    Callback<TableColumn<Studenti, String>, TableCell<Studenti, String>> cellFactorySendMail =   new Callback<TableColumn<Studenti, String>, TableCell<Studenti, String>>() {
        @Override
        public TableCell call(final TableColumn<Studenti, String> param) {
            final TableCell<Studenti, String> cell = new TableCell<Studenti, String>() {
                final ImageView image = new ImageView(new Image("FXML/img/gmail.png"));

                final Button btn = new Button("",image);


                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        btn.setOnAction(event -> {
                            Studenti student = getTableView().getItems().get(getIndex());
                            studentSelect=student;
                            final String username = "students.cs.ubbcluj@gmail.com";
                            final String password = "scs.ubbcluj.ro";

                            Properties props = new Properties();
                            props.put("mail.smtp.auth", "true");
                            props.put("mail.smtp.starttls.enable", "true");
                            props.put("mail.smtp.host", "smtp.gmail.com");
                            props.put("mail.smtp.port", "587");

                            Session session = Session.getInstance(props,
                                    new javax.mail.Authenticator() {
                                        protected PasswordAuthentication getPasswordAuthentication() {
                                            return new PasswordAuthentication(username, password);
                                        }
                                    });

                            try {

                                Message message = new MimeMessage(session);
                                message.setFrom(new InternetAddress("students.cs.ubbcluj@gmail.com"));
                                message.setRecipients(Message.RecipientType.TO,
                                        InternetAddress.parse(student.getEmail()));
                                message.setSubject("Note.pdf");
                                message.setText("Acestea sunt notele dumneavoastra");

                                MimeBodyPart messageBodyPart = new MimeBodyPart();

                                Multipart multipart = new MimeMultipart();


                                Document document = new Document();
                                PdfWriter.getInstance(document, new FileOutputStream(FILE));
                                document.open();

                                addContent(document);
                                document.close();
                                File myFile = new File(FILE);
                                Desktop.getDesktop().open(myFile);
                                messageBodyPart = new MimeBodyPart();
                                String file =FILE;
                                String fileName = "1.pdf";
                                DataSource source = new FileDataSource(file);
                                messageBodyPart.setDataHandler(new DataHandler(source));
                                messageBodyPart.setFileName(fileName);
                                multipart.addBodyPart(messageBodyPart);

                                message.setContent(multipart);

                                System.out.println("Sending");
                                Transport.send(message);
                                System.out.println("Done");



                            } catch (MessagingException e) {
                                throw new RuntimeException(e);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (DocumentException e) {
                                e.printStackTrace();

                            } catch (SQLException e) {
                                e.printStackTrace();
                            }

                        });

                        image.setFitHeight(15);
                        image.setFitWidth(20);
                        setGraphic(btn);
                        setText(null);
                    }
                }
            };
            return cell;
        }
    };





    private Node createPage(int pageIndex) {

        int fromIndex = pageIndex * rowsPerPage;
        int toIndex = Math.min(fromIndex + rowsPerPage, service.createStudentiMedie().size());
        table.setItems(FXCollections.observableArrayList(service.createStudentiMedie().subList(fromIndex, toIndex)));

        return new BorderPane(table);
    }

        Callback<TableColumn<Studenti, String>, TableCell<Studenti, String>> cellFactoryADD =   new Callback<TableColumn<Studenti, String>, TableCell<Studenti, String>>() {
                    @Override
                    public TableCell call(final TableColumn<Studenti, String> param) {
                        final TableCell<Studenti, String> cell = new TableCell<Studenti, String>() {
                            final ImageView image = new ImageView(new Image("FXML/img/add-plus-button.png"));

                            final Button btn = new Button("",image);


                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                } else {
                                    btn.setOnAction(event -> {
                                        Studenti student = getTableView().getItems().get(getIndex());
                                        index1=getIndex();
                                        try {
                                        FXMLLoader loader = new FXMLLoader();
                                        loader.setLocation(getClass().getResource("AdaugareNota.fxml"));

                                        Parent root =  loader.load();
                                        ControllerAdaugareNotaFXMl controller=loader.getController();
                                        controller.setService(service);
                                        controller.initData(student);
                                        controller.fillComboBox(student);
                                        controller.setCotnroller(notaController);
                                        controller.setCell(c_medie);


                                        studentSelected=student;

                                        Stage stage=new Stage();
                                        stage.initModality(Modality.APPLICATION_MODAL);
                                        stage.setScene(new Scene(root));
                                        controller.setStage(stage);
                                        stage.show();


                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    });

                                    image.setFitHeight(15);
                                    image.setFitWidth(20);
                                    setGraphic(btn);
                                    setText(null);
                                }
                            }
                        };
                        return cell;
                    }
                };
    Callback<TableColumn<Studenti, String>, TableCell<Studenti, String>> cellFactoryView =   new Callback<TableColumn<Studenti, String>, TableCell<Studenti, String>>() {
        @Override
        public TableCell call(final TableColumn<Studenti, String> param) {
            final TableCell<Studenti, String> cell = new TableCell<Studenti, String>() {
                final ImageView image = new ImageView(new Image("FXML/img/test-results.png"));
                final Button btn = new Button("",image);


                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {

                        btn.setOnAction(event -> {
                            Studenti student = getTableView().getItems().get(getIndex());

                            try {
                                FXMLLoader loader = new FXMLLoader();
                                loader.setLocation(getClass().getResource("ViewNote.fxml"));

                                Parent root =  loader.load();
                                ControllerViewNoteFXMl controller=loader.getController();
                                controller.initData(student);

                                Stage stage=new Stage();
                                stage.initModality(Modality.APPLICATION_MODAL);
                                stage.setScene(new Scene(root));

                                stage.show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            table.refresh();
                        });

                        image.setFitHeight(15);
                        image.setFitWidth(20);
                        setGraphic(btn);
                        setText(null);
                    }
                }
            };
            return cell;
        }
    };




    @FXML
    void initialize() {
        studentiService.addObserver(this);


        listaTeme=repoTeme.getAll();
        listaNote=noteRepo.getAll();
        c_numeStudent.setCellValueFactory(new PropertyValueFactory<Studenti,String>("Nume"));
        c_Grupa.setCellValueFactory(new PropertyValueFactory<Studenti,Integer>("Grupa"));
        c_medie.setCellValueFactory(new PropertyValueFactory<>("medie"));


        c_butonAdauga.setCellFactory( cellFactoryADD);
        c_viewButton.setCellFactory(cellFactoryView);
        c_SendEmail.setCellFactory(cellFactorySendMail);

        paginator.setPageCount(service.createStudentiMedie().size() / rowsPerPage + 1);
        paginator.setCurrentPageIndex(0);


        paginator.setPageFactory(this::createPage);
        table.setMinHeight(369);
        table.setMinWidth(431);
        vbox.setLayoutX(39);
        vbox.setLayoutY(104);



    }
    private int index1;

    public void UpdateTable(float medie) {

    }



    @Override
    public void notifyEvent(ListEvent<Studenti> e) throws SQLException {

        table.getItems().setAll(e.getList());
        int index = paginator.getCurrentPageIndex();
        paginator.setPageCount(service.createStudentiMedie().size() / rowsPerPage + 1);
        paginator.setPageFactory(this::createPage);
        paginator.setCurrentPageIndex(index);


        table.setMinHeight(369);
        table.setMinWidth(431);
        vbox.setLayoutX(39);
        vbox.setLayoutY(104);
    }




    public  JFreeChart generatePieChart() {
        DefaultPieDataset dataSet = new DefaultPieDataset();

        List<Studenti> list=FXCollections.observableArrayList();
        List<Studenti> listNepredat=FXCollections.observableArrayList();
        for(Studenti studenti : repoStudenti.getAll())
            if (service.areToateTemelePredate(studenti))
                list.add(studenti);
            else
                listNepredat.add(studenti);
        dataSet.setValue("Integral",list.size());
        dataSet.setValue("Partial",listNepredat.size());
        JFreeChart chart = ChartFactory.createPieChart(
                "Studenti care au predat temele ", dataSet, true, true, false);

        return chart;
    }



    public  void writeChartToPDF(JFreeChart chart, int width, int height, String fileName) throws IOException, DocumentException {
        PdfWriter writer = null;

        Document document = new Document();



        try {
            writer = PdfWriter.getInstance(document, new FileOutputStream(
                    fileName));
            document.open();
            PdfContentByte contentByte = writer.getDirectContent();
            PdfTemplate template = contentByte.createTemplate(width, height);
            Graphics2D graphics2d = template.createGraphics(width, height,
                    new DefaultFontMapper());
            Rectangle2D rectangle2d = new Rectangle2D.Double(0, 0, width,
                    height);

            chart.draw(graphics2d, rectangle2d);

            graphics2d.dispose();
//            contentByte.addTemplate(template, 0, 0);
            com.itextpdf.text.Image chartImage = com.itextpdf.text.Image.getInstance(template);




            document.add(chartImage);



        } catch (Exception e) {
            e.printStackTrace();
        }


        document.close();
    }


    }





