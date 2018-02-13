package sample;

import Domain.Student;
import Domain.Tema;
import Service.Service;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.image.WritableImage;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class PromotedController implements Observer{
    @FXML
    private ListView<String> listPromovati;
    private Service service;
    @FXML
    private PieChart pieChart;
    private int promovati,respinsi=0;
    @FXML
    private Text easyLab;
    private PieChart pieChart2;
    private Scene scene;
    public PromotedController(){

    }

    public void setServices(Service service) {
        this.service = service;
        pieChart2=new PieChart();
        loadPromotedStudents();
        loadPieChart();
        getEasyLab();
    }

    private PieChart getForScene(){
        PieChart pieChart=new PieChart();
        ObservableList<PieChart.Data> pieChartData=FXCollections.observableArrayList(
                new PieChart.Data("Promovat",promovati),
                new PieChart.Data("Picat",respinsi));
        pieChart.setData(pieChartData);
        return pieChart;
    }

    public void loadPieChart(){

        ObservableList<PieChart.Data> pieChartData=FXCollections.observableArrayList(
                new PieChart.Data("Promovat",promovati),
                new PieChart.Data("Picat",respinsi));

        pieChart.setData(pieChartData);

        scene = new Scene(getForScene(), 600, 300);
        Stage newStage=new Stage();
        newStage.setScene(scene);


        saveAsPng(scene,"chartPie.png");
        newStage.close();

    }

    public void saveAsPng(Scene scene, String path) {
        WritableImage image = scene.snapshot(null);
        File file = new File(path);
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getEasyLab(){
        Tema tema=service.getEasy();
        if(tema.getDescriere()!=null) {
            easyLab.setText(tema.toString());
        }
        else{
            easyLab.setText("Nu exista!");
        }
    }

    public void loadPromotedStudents(){
        respinsi=0;
        Iterable<Student> list=service.getAllStud();
        List<String> inList=new ArrayList<>();
        for(Student s:list){
            if(service.getMedie(s.getId())>=4)
                inList.add("Nume:"+s.getNume()+"\nGrupa:"+s.getGrupa()+"\nMedia:"+service.getMedie(s.getId())+"\n");
        }
        promovati=inList.size();
        respinsi=service.fromIterableToList(service.getAllStud()).size()-promovati;
        listPromovati.setItems(FXCollections.observableArrayList(inList));

    }

    public void initialize(){

    }

    @Override
    public void update(Observable o, Object arg) {
        pieChart.getData().clear();
        loadPromotedStudents();
        loadPieChart();
        getEasyLab();
    }
}
