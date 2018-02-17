package controller;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import domain.Student;
import domain.StudentMedia;
import domain.TemaPenalizari;
import javafx.application.HostServices;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.controlsfx.control.CheckComboBox;
import sablonObserver.Observable;
import sablonObserver.Observer;
import service.Service;
import view_FXML.AlertMessage;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ControllerStatisticaStudentiiCareIntraInExamen extends ControllerStatisticaStudentMedia{

    @FXML
    private ComboBox comboBoxNotaFiltru;
    @FXML
    private PieChart pieChart;
    @FXML
    private Label labelItemsPerPage;


    @Override
    public void initialize(){
        super.initialize();
        setComboBoxNotaFiltruList();

        comboBoxNotaFiltru.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
               updateTableView();
               initGrafic();
            }
        });

    }

    @Override
    protected void initGrafic() {
        initPieChart();
    }

    @Override
    protected String getGraficImage() {
        PieChart pieChart2 = getPieChartForPdf();

        Scene scene=new Scene(pieChart2);
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

    private PieChart getPieChartForPdf() {
        PieChart pieChart2=new PieChart();
        pieChart2.setTitle("Studentii care intra in Examen");

        ObservableList<PieChart.Data> set= FXCollections.observableArrayList();

        set.removeAll();
        List<StudentMedia> list=getStatistica();

        Integer studentiIntrati=list.size();
        Integer studentiRespinsi=service.getListStudenti().size()-studentiIntrati;

        set.add(new PieChart.Data("Studentii care intra in examen",studentiIntrati));
        set.add((new PieChart.Data("Studentii restantieri", studentiRespinsi)));

        pieChart2.getData().setAll(set);
        return pieChart2;
    }

    public Integer getNotaFiltru(){
        Integer nota=(Integer) comboBoxNotaFiltru.getValue();
        if (nota==null)
            return 0;
        else return nota;
    }

    public ControllerStatisticaStudentiiCareIntraInExamen() {
        super();
    }

    private void setComboBoxNotaFiltruList(){
        ObservableList<Integer> list=FXCollections.observableArrayList();
        list.addAll(0,1,2,3,4,5,6,7,8,9,10);
        comboBoxNotaFiltru.getItems().setAll(list);
    }


    @Override
    public List<StudentMedia> getStatistica() {
        return service.getStatisticaStudentiiCarePotIntraInExamen(ponderi,listaStudenti,getNotaFiltru());
    }

    @FXML
    public void handleTabel(ActionEvent event){
        tableView.setVisible(true);
        pagination.setVisible(true);
        comboBoxItemsPerPage.setVisible(true);
        labelItemsPerPage.setVisible(true);
        pieChart.setVisible(false);
    }
    
    @FXML
    public void handleGrafic(ActionEvent event){
        tableView.setVisible(false);
        pagination.setVisible(false);
        comboBoxItemsPerPage.setVisible(false);
        labelItemsPerPage.setVisible(false);

        pieChart.setVisible(true);
        pieChart.setTitle("Studentii care intra in Examen");
        pieChart.setLegendSide(Side.BOTTOM);
        pieChart.setLabelsVisible(true);
        initPieChart();
    }

    private void initPieChart() {
        ObservableList<PieChart.Data> details= FXCollections.observableArrayList();
        details.removeAll();
        List<StudentMedia> list=getStatistica();
        Integer studentiIntrati=list.size();
        Integer studentiRespinsi=service.getListStudenti().size()-studentiIntrati;

        details.add(new PieChart.Data("Studentii care intra in examen",studentiIntrati));
        details.add((new PieChart.Data("Studentii restatieri", studentiRespinsi)));

        pieChart.setData(details);
    }

    @Override
    public void update(){
        super.update();
        initPieChart();
    }
}
