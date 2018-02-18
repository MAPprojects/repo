package MVC;

import Domain.Nota;
import Domain.Student;
import Domain.Tema;
import Service.Service;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.util.Pair;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static MVC.MainController.showErrorMessage;

public class RapoarteController {
    private Service service;

    private String statisticsTitle = "";
    private String listaSelectata = "lista1";
    private List<Pair<Student, Float>> lista1;
    private List<Pair<Tema, Integer>> lista2;
    private List<Pair<Student, Float>> lista3;

    @FXML
    public TextField averageTextField;
    @FXML
    private PieChart pieChart;
    @FXML
    private TableView tableView;
    @FXML
    private Button saveAsPDFButton;

    public void setService(Service service) {
        this.service = service;

        saveAsPDFButton.setDisable(true);

        pieChart.setTitle("Statistici");
        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                        new PieChart.Data("0", 0),
                        new PieChart.Data("1", 0),
                        new PieChart.Data("2", 0),
                        new PieChart.Data("3", 0),
                        new PieChart.Data("4", 0),
                        new PieChart.Data("5", 0),
                        new PieChart.Data("6", 0),
                        new PieChart.Data("7", 0),
                        new PieChart.Data("8", 0),
                        new PieChart.Data("9", 0),
                        new PieChart.Data("10", 0));
        pieChart.setData(pieChartData);
    }

    public void mediaFiecaruiStudent(ActionEvent actionEvent) {
        statisticsTitle = "Media ponderata a notelor pentru fieacare student";
        listaSelectata = "lista1";

        List<Pair<Student, Float>> lista = new ArrayList<>();
        for (Student student : service.getStudenti()) {
            float medie = 0;
            int nrNote = service.getNrTeme();
            for (Tema tema : service.getTeme()) {
                String id = "" + student.getId() + "_" + tema.getId();
                Optional<Nota> notaOptional = service.findNota(id);
                if (notaOptional.isPresent())
                    medie+=notaOptional.get().getValoare();
                else
                    medie+=1;
            }
            if (nrNote > 0)
                medie = medie / nrNote;
            lista.add(new Pair<>(student, medie));
        }
        lista1 = lista;

        List<Pair<String, Float>> listaPieChart = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            listaPieChart.add(new Pair<>(Integer.toString(i), 0.0f));
        }
        for (int i = 0; i < lista.size(); i++) {
            Pair<String, Float> pereche = new Pair<>(listaPieChart.get(Math.round(lista.get(i).getValue())).getKey(),
                    listaPieChart.get(Math.round(lista.get(i).getValue())).getValue() + 1);
            listaPieChart.set(Math.round(lista.get(i).getValue()), pereche);
        }
        List<PieChart.Data> listaPieChart2 = new ArrayList<>();
        for (Pair<String, Float> pair : listaPieChart) {
            if (pair.getValue() > 0)
                listaPieChart2.add(new PieChart.Data(pair.getKey(), pair.getValue()));
        }
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(listaPieChart2);
        pieChart.setData(pieChartData);

        tableView.getItems().clear();
        tableView.getColumns().clear();

        TableColumn<StudentAverageTableRow, Integer> column1 = new TableColumn<>("ID");
        column1.setCellValueFactory(new PropertyValueFactory<>("idStudent"));
        column1.setMinWidth(60);
        column1.setMaxWidth(60);

        TableColumn<StudentAverageTableRow, String> column2 = new TableColumn<>("Nume");
        column2.setCellValueFactory(new PropertyValueFactory<>("numeStudent"));
        column2.setMinWidth(260);
        column2.setMaxWidth(260);

        TableColumn<StudentAverageTableRow, Integer> column3 = new TableColumn<>("Grupa");
        column3.setCellValueFactory(new PropertyValueFactory<>("grupaStudent"));
        column3.setMinWidth(80);
        column3.setMaxWidth(80);

        TableColumn<StudentAverageTableRow, Integer> column4 = new TableColumn<>("Medie");
        column4.setCellValueFactory(new PropertyValueFactory<>("medie"));
        column4.setMinWidth(130);
        column4.setMaxWidth(130);

        tableView.getColumns().addAll(column1, column2, column3, column4);

        for (Pair<Student, Float> pair : lista) {
            tableView.getItems().add(new StudentAverageTableRow(pair.getKey(), pair.getValue()));
        }

        saveAsPDFButton.setDisable(false);
    }

    public void celeMaiGreleTeme(ActionEvent actionEvent){
        //temele la care au fost penalizati cei mai multi studenti
        statisticsTitle = "Cele mai grele teme (multe intarzieri in predarea acestora)";
        listaSelectata = "lista2";

        List<Pair<Tema, Integer>> lista= new ArrayList<>();
        lista=service.raportCeleMaiGreleTeme();
        lista2 = lista;

        // piechart
        List<Pair<String, Integer>> listaPieChart = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            listaPieChart.add(new Pair<>(Integer.toString(i), 0));
        }
        for (int i = 0; i < lista.size(); i++) {
            Pair<String, Integer> pereche = new Pair<>(listaPieChart.get(Math.round(lista.get(i).getValue())).getKey(),
                    listaPieChart.get(Math.round(lista.get(i).getValue())).getValue() + 1);
            listaPieChart.set(Math.round(lista.get(i).getValue()), pereche);
        }
        List<PieChart.Data> listaPieChart2 = new ArrayList<>();
        for (Pair<String, Integer> pair : listaPieChart) {
            if (pair.getValue() > 0)
                listaPieChart2.add(new PieChart.Data(pair.getKey(), pair.getValue()));
        }
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(listaPieChart2);
        pieChart.setData(pieChartData);

        //tabel
        tableView.getItems().clear();
        tableView.getColumns().clear();

        TableColumn<celeMaiGreleTemeTableRow, Integer> column1 = new TableColumn<>("ID");
        column1.setCellValueFactory(new PropertyValueFactory<>("idTema"));
        column1.setMinWidth(60);
        column1.setMaxWidth(60);

        TableColumn<celeMaiGreleTemeTableRow, String> column2 = new TableColumn<>("Descriere");
        column2.setCellValueFactory(new PropertyValueFactory<>("descriereTema"));
        column2.setMinWidth(240);
        column2.setMaxWidth(240);

        TableColumn<celeMaiGreleTemeTableRow, Integer> column3 = new TableColumn<>("Numar penalizari");
        column3.setCellValueFactory(new PropertyValueFactory<>("intarziere"));
        column3.setMinWidth(150);
        column3.setMaxWidth(150);

        tableView.getColumns().addAll(column1, column2, column3);

        for (Pair<Tema, Integer> pair : lista) {
            tableView.getItems().add(new celeMaiGreleTemeTableRow(pair.getKey(), pair.getValue()));
        }

        saveAsPDFButton.setDisable(false);
    }

    public void studentiCarePotIntraInExamen(ActionEvent actionEvent){
        statisticsTitle = "Studenti care pot intra in examen";
        listaSelectata = "lista3";
        List<Pair<Student, Float>> lista;
        lista=service.raportStudentiCarePotIntraInExamen();
        lista3 = lista;

        //piechart
        List<PieChart.Data> listaPieChart = new ArrayList<>();
        int valueCount = Math.round(((float) lista.size() / (float) service.getNrStudenti()) * 100);
        listaPieChart.add(new PieChart.Data("#Intra", valueCount));
        listaPieChart.add(new PieChart.Data("#NU intra", 100 - valueCount));
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(listaPieChart);
        pieChart.setData(pieChartData);

        //tabel
        tableView.getItems().clear();
        tableView.getColumns().clear();

        TableColumn<StudentAverageTableRow, Integer> column1 = new TableColumn<>("ID");
        column1.setCellValueFactory(new PropertyValueFactory<>("idStudent"));
        column1.setMinWidth(60);
        column1.setMaxWidth(60);

        TableColumn<StudentAverageTableRow, String> column2 = new TableColumn<>("Nume");
        column2.setCellValueFactory(new PropertyValueFactory<>("numeStudent"));
        column2.setMinWidth(260);
        column2.setMaxWidth(260);

        TableColumn<StudentAverageTableRow, Integer> column3 = new TableColumn<>("Grupa");
        column3.setCellValueFactory(new PropertyValueFactory<>("grupaStudent"));
        column3.setMinWidth(80);
        column3.setMaxWidth(80);

        TableColumn<StudentAverageTableRow, Integer> column4 = new TableColumn<>("Medie");
        column4.setCellValueFactory(new PropertyValueFactory<>("medie"));
        column4.setMinWidth(130);
        column4.setMaxWidth(130);

        tableView.getColumns().addAll(column1, column2, column3, column4);

        for (Pair<Student, Float> pair : lista) {
            tableView.getItems().add(new StudentAverageTableRow(pair.getKey(), pair.getValue()));
        }

        saveAsPDFButton.setDisable(false);
    }

    public void saveAsPDF(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home") + "/Desktop"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF document(*.pdf)", "*.pdf"));
        //fileChooser.setInitialFileName("*.pdf");
        fileChooser.setInitialFileName("raport.pdf");
        File file = fileChooser.showSaveDialog(tableView.getScene().getWindow());
        try {
            if (!file.getName().contains(".")) {
                file = new File(file.getAbsolutePath() + ".pdf");
            }
            String filePath = file.getPath();
            PDDocument document = new PDDocument();
            PDPage page = new PDPage();
            document.addPage(page);
            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            int x=40;
            int y=700;
            contentStream.beginText();
            contentStream.newLineAtOffset(x, y);
            contentStream.setFont(PDType1Font.TIMES_BOLD, 20);
            contentStream.showText(statisticsTitle);

            if (listaSelectata.equals("lista1")) {
                contentStream.newLineAtOffset(-20, -40);
                contentStream.setFont(PDType1Font.TIMES_ROMAN, 12);
                for (Pair<Student, Float> pair : lista1) {
                    String s = "";
                    s += "Studentul " + pair.getKey().getId().toString() + ". ";
                    s += pair.getKey().getNume() + " din grupa ";
                    s += pair.getKey().getGrupa().toString() + " are media pentru laborator  ";
                    s += pair.getValue().toString() + ".";
                    contentStream.showText(s);
                    contentStream.newLineAtOffset(0, -20);
                }
                contentStream.endText();
                contentStream.close();
            }
            else if (listaSelectata.equals("lista2")) {
                contentStream.newLineAtOffset(-20, -40);
                contentStream.setFont(PDType1Font.TIMES_ROMAN, 12);
                for (Pair<Tema, Integer> pair : lista2) {
                    String s = "";
                    s += "Tema " + pair.getKey().getId().toString() + ": \"";
                    s += pair.getKey().getDescriere() + "\" are ";
                    s += pair.getValue().toString() + " studenti cu penalizari.";
                    contentStream.showText(s);
                    contentStream.newLineAtOffset(0, -20);
                }
                contentStream.endText();
                contentStream.close();
            }
            else if (listaSelectata.equals("lista3")) {
                contentStream.newLineAtOffset(-20, -40);
                contentStream.setFont(PDType1Font.TIMES_ROMAN, 12);
                for (Pair<Student, Float> pair : lista3) {
                    String s = "";
                    s += "Studentul " + pair.getKey().getId().toString() + ". ";
                    s += pair.getKey().getNume() + " din grupa ";
                    s += pair.getKey().getGrupa().toString() + " are media pentru laborator  ";
                    s += pair.getValue().toString() + ".";
                    contentStream.showText(s);
                    contentStream.newLineAtOffset(0, -20);
                }
                contentStream.endText();
                contentStream.close();
            }

            contentStream.close();
            document.save(filePath);
            document.close();
        } catch (IOException | IllegalArgumentException e) {
            showErrorMessage("Fisierul nu poate fi creat: " + e.getMessage());
        } catch (NullPointerException ignored) {

        }
    }
}
