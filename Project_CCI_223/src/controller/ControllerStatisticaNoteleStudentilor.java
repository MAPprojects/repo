package controller;

import domain.StudentMedia;
import domain.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.image.WritableImage;
import service.Service;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class ControllerStatisticaNoteleStudentilor extends ControllerStatisticaStudentMedia {

    @FXML
    private PieChart pieChart;
    @FXML
    private Label labelItemsPerPage;

    @Override
    public List<StudentMedia> getStatistica() {
        return service.getStstisticaNoteleTuturorStudentilor(ponderi,listaStudenti);
    }

    @FXML
    public void handleGrafic(ActionEvent event){
        tableView.setVisible(false);
        pagination.setVisible(false);
        comboBoxItemsPerPage.setVisible(false);
        labelItemsPerPage.setVisible(false);

        pieChart.setVisible(true);
        pieChart.setTitle("Mediile Studentilor");
        pieChart.setLegendSide(Side.BOTTOM);
        pieChart.setLabelsVisible(true);

        initPieChart();
    }

    private void initPieChart() {
        ObservableList<PieChart.Data> details= FXCollections.observableArrayList();
        details.removeAll();

        for (int i=1;i<=10;i++){
            List<StudentMedia> list=service.getStatisticaCuNotePeCategorii(i);
            details.add(new PieChart.Data(i-1+"-"+i,list.size()));
        }
        pieChart.setData(details);

    }

    @FXML 
    public void handleTabel(ActionEvent event){
        tableView.setVisible(true);
        pagination.setVisible(true);
        comboBoxItemsPerPage.setVisible(true);
        labelItemsPerPage.setVisible(true);
        pieChart.setVisible(false);
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

    @Override
    public void update(){
        super.update();
        initPieChart();
    }

    public PieChart getPieChartForPdf() {
        PieChart pieChartPdf=new PieChart();
        pieChartPdf.setTitle("Mediile Studentilor");
        ObservableList<PieChart.Data> details= FXCollections.observableArrayList();
        details.removeAll();

        for (int i=1;i<=10;i++){
            List<StudentMedia> list=service.getStatisticaCuNotePeCategorii(i);
            details.add(new PieChart.Data(i-1+"-"+i,list.size()));
        }
        pieChartPdf.setData(details);
        return pieChartPdf;
    }

    @Override
    public void setService(Service service){
        super.setService(service);
        if (user.getProf_student().equals("student"))
            imagePdf.setVisible(false);
    }
}
