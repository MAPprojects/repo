package controller;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import domain.StudentMedia;
import domain.TemaLaborator;
import domain.TemaPenalizari;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArraySet;

public class ControllerCeleMaiGreleTeme extends ControllerStatisticaTip<TemaPenalizari>{

    public ControllerCeleMaiGreleTeme(){
        super();
    }

    @FXML
    private TableColumn tableColumnNrTema;
    @FXML
    private TableColumn tableColumnCerinta;
    @FXML
    private TableColumn tableColumnDeadline;
    @FXML
    private TableColumn tableColumnNrStudenti;
    @FXML
    private Label labelItemsPerPage;
    @FXML
    private AnchorPane anchorPanePage;

    private ObservableList<BarChart.Data> dataPieChart= FXCollections.observableArrayList();
    @FXML
    private BarChart barChart;

    @Override
    protected void setTableViewColumns() {
        tableColumnCerinta.setCellValueFactory(new PropertyValueFactory<TemaPenalizari,String>("cerinta"));
        tableColumnDeadline.setCellValueFactory(new PropertyValueFactory<TemaPenalizari,Integer>("deadline"));
        tableColumnNrTema.setCellValueFactory(new PropertyValueFactory<TemaPenalizari,Integer>("idTema"));
        tableColumnNrStudenti.setCellValueFactory(new PropertyValueFactory<TemaPenalizari,Integer>("nrStudentiPenalizati"));
    }

    @Override
    protected String getGraficImage() {

        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        BarChart chartForPdf=new BarChart(xAxis,yAxis);

        XYChart.Series set=new XYChart.Series<>();
        List<TemaPenalizari> list=service.getStatisticaCeleMaiGreleTeme(listaStudenti);
        set.getData().removeAll();

        for (TemaPenalizari tema:list){
            set.getData().add(new XYChart.Data<String,Integer>(tema.getIdTema().toString(),tema.getNrStudentiPenalizati()));
        }

        chartForPdf.getData().setAll(set);

        Scene scene=new Scene(chartForPdf);
        WritableImage snapShot=scene.snapshot(null);
        String path="D:\\MAP\\GestiuneStudentiLaboratareNote\\src\\grafice\\"+contor+".png";
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(snapShot, null), "png", new File(path));
            contor++;
            return path;

        } catch (IOException e) {
            contor++;
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(snapShot, null), "png", new File(path));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            contor++;
            return path;
        }
    }

    @Override
    public PdfPTable getPdfPTable() throws DocumentException {
        PdfPTable table=new PdfPTable(4);
        table.setWidthPercentage(105);
        table.setSpacingBefore(11f);
        table.setSpacingAfter(11f);

        float[] colwidth={2f,2f,2f,2f};
        table.setWidths(colwidth);
        PdfPCell cellNrTema=new PdfPCell(new Paragraph("Nr Tema"));
        PdfPCell cellCerinta=new PdfPCell(new Paragraph("Cerinta"));
        PdfPCell cellDeadline=new PdfPCell(new Paragraph("Deadline"));
        PdfPCell cellNrStudenti=new PdfPCell(new Paragraph("NrStudentiIntarziati"));

        table.addCell(cellNrTema);
        table.addCell(cellCerinta);
        table.addCell(cellDeadline);
        table.addCell(cellNrStudenti);

        List<TemaPenalizari> listaTemePenalizari=new ArrayList<>();
        for (int i=0;i<data.size();i++){
            listaTemePenalizari.add((TemaPenalizari) data.get(i));
        }

        cellNrTema.setRowspan(listaStudenti.size());
        cellCerinta.setRowspan(listaStudenti.size());
        cellDeadline.setRowspan(listaStudenti.size());
        cellNrStudenti.setRowspan(listaStudenti.size());

        listaTemePenalizari.forEach(temaPenalizare -> {
            table.addCell(temaPenalizare.getIdTema().toString());
            table.addCell(temaPenalizare.getCerinta());
            table.addCell(temaPenalizare.getDeadline().toString());
            table.addCell(temaPenalizare.getNrStudentiPenalizati().toString());
        });
        return table;
    }

    @Override
    public List<TemaPenalizari> getStatistica() {
        return service.getStatisticaCeleMaiGreleTeme(listaStudenti);
    }

    @FXML
    public void handleGrafic(ActionEvent event){
        tableView.setVisible(false);
        pagination.setVisible(false);
        comboBoxItemsPerPage.setVisible(false);
        labelItemsPerPage.setVisible(false);
        barChart.setVisible(true);
        initBarChart();
    }

    @FXML
    public void handleTabel(ActionEvent event){
        tableView.setVisible(true);
        pagination.setVisible(true);
        comboBoxItemsPerPage.setVisible(true);
        labelItemsPerPage.setVisible(true);
        barChart.setVisible(false);
    }

    private void initBarChart() {

        XYChart.Series set=new XYChart.Series<>();
        List<TemaPenalizari> list=service.getStatisticaCeleMaiGreleTeme(listaStudenti);
        set.getData().removeAll();

        for (TemaPenalizari tema:list){
            set.getData().add(new XYChart.Data<String,Integer>(tema.getIdTema().toString(),tema.getNrStudentiPenalizati()));
        }

        barChart.getData().setAll(set);
    }

    @Override
    public void initialize(){
        super.initialize();
        barChart.setVisible(false);
        barChart.setTitle("Cele Mai Grele Teme");
        barChart.getData().setAll(new XYChart.Series<>());
    }

    @Override
    protected void initGrafic() {
        initBarChart();
    }

    @Override
    public void update(){
        super.update();
        initBarChart();
    }
}