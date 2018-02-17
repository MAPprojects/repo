package controller;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import domain.DetaliiLog;
import domain.Student;
import domain.StudentMedia;
import domain.User;
import exceptii.EntityNotFoundException;
import javafx.application.HostServices;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sablonObserver.Observer;
import service.Service;
import service.ServiceUsers;
import view_FXML.AlertMessage;
import view_FXML.DropShadowForImage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;


public class ControllerFisierLog extends ControllerTabelLogProf{

    private ServiceUsers serviceUsers;
    private HostServices hostServices;
    private Stage mainStage;

    @FXML
    private ImageView imagePdf;

    @FXML
    private Label labelDetaliiStudent;

    @FXML
    public void handleImageEnterPdf(MouseEvent event){
        imagePdf.setEffect(DropShadowForImage.dropShadow(10d,10d,1d,1d,2d));
    }
    @FXML
    public void handleImageExitPdf(MouseEvent ev){
        imagePdf.setEffect(null);
    }

    public void handleSaveAsPdf(MouseEvent event){
        FileChooser fileChooser=new FileChooser();

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PDF","*.pdf")
        );
        fileChooser.setInitialFileName("log.pdf");
        fileChooser.setTitle("Save As PDF");
        File file=fileChooser.showSaveDialog(stage);
        if (file!=null){
            String path=file.getAbsolutePath();
            createPdf(path);
        }
    }

    private void createPdf(String path) {
        Document document=new Document();
        try{
            PdfWriter writer=PdfWriter.getInstance(document,new FileOutputStream(path));
            document.open();

            document.addTitle("Student Managerment System");
            document.addAuthor("Coste Claudia-Ioana");
            document.addCreationDate();
            document.addSubject("Fisier LOG");
            document.resetPageCount();
            document.setPageCount(2);

            Paragraph title=new Paragraph("Student Managerment System");

            Font font=new Font();
            font.setColor(BaseColor.RED);
            font.setSize(30f);
            font.setFamily("Times New Roman");
            title.setFont(font);

            document.add(title);

            PdfPTable table = getPdfPTable();

            document.add(table);

            document.close();
            writer.close();

            File file=new File(path);
            hostServices.showDocument(file.getAbsolutePath());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ControllerFisierLog() {
    }

    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }

    private void setDetaliiLabel() {
        String text = "";
        Student student = getStudentForUser();
        text = "ID: " + student.getId() + " Nume: " + student.getNume() + "; Email: " + student.getEmail() + "; Grupa: " + student.getGrupa() + "; Profesor: " + student.getCadru_didactic_indrumator_de_laborator() + "; Email profesor: ";
        User profesor = serviceUsers.getUserProfesorByName(student.getCadru_didactic_indrumator_de_laborator());
        if (profesor != null) {
            text = text + profesor.getEmail();
        }
        labelDetaliiStudent.setText(text);
        labelDetaliiStudent.setStyle("-fx-text-fill: darkcyan");
        labelDetaliiStudent.setStyle("-fx-font-weight: bold");
    }

    public void setServiceUsers(ServiceUsers serviceUsers) {
        this.serviceUsers = serviceUsers;
    }

    public void setHostServices(HostServices hostServices) {
        this.hostServices = hostServices;
    }

    @Override
    public void initialize(){
        super.initialize();
    }

    @Override
    public void update() {
        if (getStudentForUser()==null){
            AlertMessage message=new AlertMessage();
            message.showMessage(stage, Alert.AlertType.ERROR,"Eroare","Nu sunteti inregistrati in sistem, va rugam sa vorbiti cu profesorul coordonator.");
            stage.close();
        }
        super.update();
        setDetaliiLabel();
    }

    public User getCurrentUser() {
        try {
            return serviceUsers.getUser(currentUser.getUsername()).get();
        } catch (EntityNotFoundException e) {
            stage.close();
        }
        return new User();
    }

    @Override
    public void setService(Service service){
        super.setService(service);
        if (getStudentForUser()!=null){
            setDetaliiLabel();
        }
    }

    public PdfPTable getPdfPTable() throws DocumentException {
        PdfPTable table=new PdfPTable(7);
        table.setWidthPercentage(105);
        table.setSpacingBefore(11f);
        table.setSpacingAfter(11f);

        float[] colwidth={1f,0.5f,0.8f,0.8f,0.8f,1f,1f};
        table.setWidths(colwidth);
        PdfPCell cellOperatiune=new PdfPCell(new Paragraph("Operatiune"));
        PdfPCell cellNrTema=new PdfPCell(new Paragraph("Nr Tema"));
        PdfPCell cellNota=new PdfPCell(new Paragraph("Nota"));
        PdfPCell cellDeadline=new PdfPCell(new Paragraph("Deadline"));
        PdfPCell cellSaptPredarii=new PdfPCell(new Paragraph("Sapt. predarii"));
        PdfPCell cellIntarzieri=new PdfPCell(new Paragraph("Intarzieri"));
        PdfPCell cellGreseli=new PdfPCell(new Paragraph("Greseli"));

        table.addCell(cellOperatiune);
        table.addCell(cellNrTema);
        table.addCell(cellNota);
        table.addCell(cellDeadline);
        table.addCell(cellSaptPredarii);
        table.addCell(cellIntarzieri);
        table.addCell(cellGreseli);

        List<DetaliiLog> lista=new ArrayList<>();
        for (int i=0;i<data.size();i++){
            lista.add((DetaliiLog) data.get(i));
        }

        cellOperatiune.setRowspan(lista.size());
        cellNrTema.setRowspan(lista.size());
        cellNota.setRowspan(lista.size());
        cellDeadline.setRowspan(lista.size());
        cellSaptPredarii.setRowspan(lista.size());
        cellIntarzieri.setRowspan(lista.size());
        cellGreseli.setRowspan(lista.size());

        lista.forEach(detalii -> {
            table.addCell(detalii.getOperatiune());
            table.addCell(detalii.getNrTema().toString());
            table.addCell(detalii.getValoareNota().toString());
            table.addCell(detalii.getDeadline().toString());
            table.addCell(detalii.getSaptamana_predarii().toString());
            table.addCell(detalii.getIntarzieri().toString());
            table.addCell(detalii.getGreseli().toString());
        });
        return table;
    }
}
