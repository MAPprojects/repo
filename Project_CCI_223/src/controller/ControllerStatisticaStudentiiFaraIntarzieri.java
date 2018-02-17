package controller;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import domain.StudentiIntarziati;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import view_FXML.AlertMessage;

import java.util.ArrayList;
import java.util.List;

public class ControllerStatisticaStudentiiFaraIntarzieri extends ControllerStatisticaTip<StudentiIntarziati> {
    @FXML
    private TableColumn tableColumnIdStudent;
    @FXML
    private TableColumn tableColumnNume;
    @FXML
    private TableColumn tableColumnGrupa;
    @FXML
    private TableColumn tableColumnProfesor;
    @FXML
    private TableColumn tableColumnEmail;
    @FXML
    private TableColumn tableColumnNrTeme;
    @FXML
    private TableColumn tableColumnIntarzieri;

    @FXML
    private TextField textFieldTemeNepredate;
    @FXML
    private TextField textFieldIntarziere;


    @Override
    protected void setTableViewColumns() {
        tableColumnEmail.setCellValueFactory(new PropertyValueFactory<StudentiIntarziati,String>("email"));
        tableColumnGrupa.setCellValueFactory(new PropertyValueFactory<StudentiIntarziati,Integer>("grupa"));
        tableColumnIdStudent.setCellValueFactory(new PropertyValueFactory<StudentiIntarziati,Integer>("idStudent"));
        tableColumnNume.setCellValueFactory(new PropertyValueFactory<StudentiIntarziati,String>("nume"));
        tableColumnIntarzieri.setCellValueFactory(new PropertyValueFactory<StudentiIntarziati,Integer>("nrIntarzieri"));
        tableColumnNrTeme.setCellValueFactory(new PropertyValueFactory<StudentiIntarziati,Integer>("nrTemeNepredate"));
        tableColumnProfesor.setCellValueFactory(new PropertyValueFactory<StudentiIntarziati,String>("profesor"));

    }

    @Override
    protected String getGraficImage() {
        return null;
    }

    @Override
    public PdfPTable getPdfPTable() throws DocumentException {
        PdfPTable table=new PdfPTable(7);
        table.setWidthPercentage(105);
        table.setSpacingBefore(11f);
        table.setSpacingAfter(11f);

        float[] colwidth={1f,1.5f,1f,1.5f,2f,1f,1f};
        table.setWidths(colwidth);
        PdfPCell cellIdStudent=new PdfPCell(new Paragraph("IdStudent"));
        PdfPCell cellNume=new PdfPCell(new Paragraph("Nume"));
        PdfPCell cellGrupa=new PdfPCell(new Paragraph("Grupa"));
        PdfPCell cellProfesor=new PdfPCell(new Paragraph("Profesor"));
        PdfPCell cellEmail=new PdfPCell(new Paragraph("Email"));
        PdfPCell cellTemeNepredate=new PdfPCell(new Paragraph("Nr Teme Nepredate"));
        PdfPCell cellIntarzieri=new PdfPCell(new Paragraph("Intarzieri (nr de saptamani)"));


        table.addCell(cellIdStudent);
        table.addCell(cellNume);
        table.addCell(cellGrupa);
        table.addCell(cellProfesor);
        table.addCell(cellEmail);
        table.addCell(cellTemeNepredate);
        table.addCell(cellIntarzieri);

        List<StudentiIntarziati> studentiIntarziatiList=new ArrayList<>();
        for (int i=0;i<data.size();i++){
            studentiIntarziatiList.add((StudentiIntarziati) data.get(i));
        }

        cellIdStudent.setRowspan(studentiIntarziatiList.size());
        cellNume.setRowspan(studentiIntarziatiList.size());
        cellGrupa.setRowspan(studentiIntarziatiList.size());
        cellProfesor.setRowspan(studentiIntarziatiList.size());
        cellEmail.setRowspan(studentiIntarziatiList.size());
        cellTemeNepredate.setRowspan(studentiIntarziatiList.size());
        cellIntarzieri.setRowspan(studentiIntarziatiList.size());


        studentiIntarziatiList.forEach(studentIntarziat -> {
            table.addCell(studentIntarziat.getIdStudent().toString());
            table.addCell(studentIntarziat.getNume());
            table.addCell(studentIntarziat.getGrupa().toString());
            table.addCell(studentIntarziat.getProfesor());
            table.addCell(studentIntarziat.getEmail());
            table.addCell(studentIntarziat.getNrTemeNepredate().toString());
            table.addCell(studentIntarziat.getNrIntarzieri().toString());
        });
        return table;
    }

    @Override
    public void initialize(){
        super.initialize();

        textFieldIntarziere.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                updateTableView();
            }
        });

        textFieldTemeNepredate.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                updateTableView();
            }
        });
    }

    @Override
    protected void initGrafic() {
        //TODO
    }

    @Override
    public List<StudentiIntarziati> getStatistica() {
        String intarzieri;
        String temeNePredate;
        if (!textFieldTemeNepredate.getText().isEmpty() && !textFieldIntarziere.getText().isEmpty()) {
            intarzieri = textFieldIntarziere.getText();
            temeNePredate = textFieldTemeNepredate.getText();

            try {
                Integer intarzieriNr = Integer.parseInt(intarzieri);
                if (intarzieriNr < 0)
                    throw new NumberFormatException();
                try {
                    Integer nrTemeNePredate = Integer.parseInt(temeNePredate);
                    if (nrTemeNePredate < 0)
                        throw new NumberFormatException();

                    return service.getStatisticaStudentiIntarziatiCuTemeNepredate(listaStudenti, nrTemeNePredate, intarzieriNr);

                } catch (NumberFormatException e) {
                    AlertMessage message = new AlertMessage();
                    message.showMessage(stage, Alert.AlertType.ERROR, "Eroare!!!", "Numarul de teme nepredate trebuie sa fie un numar natural nenul");
                }
            } catch (NumberFormatException e) {
                AlertMessage message = new AlertMessage();
                message.showMessage(stage, Alert.AlertType.ERROR, "Eroare!!!", "Numarul de intarzieri (numarul de saptamani intarziate) trebuie sa fie un numar natural nenul");

            }
        }
        return service.getStatisticaStudentiIntarziatiCuTemeNepredate(listaStudenti,0,0);
    }
}
