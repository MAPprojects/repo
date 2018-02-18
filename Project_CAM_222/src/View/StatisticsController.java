package View;

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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

public class StatisticsController {
    private Service service;
    private String accountName;
    private String accountRole;
    private String accountPicturePath;
    private RootController rootController;

    private String statisticsTitle = "";
    private String listaSelectata = "lista1";
    private List<Pair<Student, Float>> lista1;
    private List<Pair<Tema, Float>> lista2;
    private List<Pair<Student, Float>> lista3;

    @FXML
    public TextField averageTextField;
    @FXML
    private PieChart pieChart;
    @FXML
    private BarChart<String, Number> barChart;
    @FXML
    private CategoryAxis xAxis;
    @FXML
    private NumberAxis yAxis;
    @FXML
    private TableView tableView;
    @FXML
    private Button saveAsPDFButton;

    public void setService(Service service) {
        this.service = service;

        saveAsPDFButton.setDisable(true);

        pieChart.setTitle("Pie Chart");
        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                        new PieChart.Data("0", 1),
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

        barChart.setTitle("Bar Chart");
        barChart.setLegendVisible(false);
        xAxis.setLabel("Averages");
        yAxis.setLabel("Number of Averages");
        XYChart.Series<String, Number> series1 = new XYChart.Series<>();
        series1.setName("");
        series1.getData().add(new XYChart.Data<>("0", 0));
        series1.getData().add(new XYChart.Data<>("1", 0));
        series1.getData().add(new XYChart.Data<>("2", 0));
        series1.getData().add(new XYChart.Data<>("3", 0));
        series1.getData().add(new XYChart.Data<>("4", 0));
        series1.getData().add(new XYChart.Data<>("5", 0));
        series1.getData().add(new XYChart.Data<>("6", 0));
        series1.getData().add(new XYChart.Data<>("7", 0));
        series1.getData().add(new XYChart.Data<>("8", 0));
        series1.getData().add(new XYChart.Data<>("9", 0));
        series1.getData().add(new XYChart.Data<>("10", 0));
        barChart.getData().clear();
        barChart.getData().add(series1);
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public void setAccountRole(String accountRole) {
        this.accountRole = accountRole;
    }

    public void setAccountPicturePath(String accountPicturePath) {
        this.accountPicturePath = accountPicturePath;
    }

    public void setRootController(RootController rootController) {
        this.rootController = rootController;
    }

    public void showGradeAverageStudents(ActionEvent actionEvent) {
        // initializing the PDF exporting variables
        statisticsTitle = "GRADE AVERAGE OF EVERY STUDENT";
        listaSelectata = "lista1";

        // Grade average of every student
        List<Pair<Student, Float>> lista = new ArrayList<>();
        lista1 = lista;
        for (Student student : service.getAllStudenti()) {
            float medie = 0;
            int nrNote = service.getTemeCount();
            for (Tema tema : service.getAllTeme()) {
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

        // Making the piechart work
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

        // making the barchart work
        XYChart.Series<String, Number> series1 = new XYChart.Series<>();
        series1.setName("");
        for (Pair<String, Float> pair : listaPieChart) {
            series1.getData().add(new XYChart.Data<>(pair.getKey(), pair.getValue()));
        }
        barChart.getData().clear();
        barChart.getData().add(series1);

        // making the table work
        tableView.getItems().clear();
        tableView.getColumns().clear();

        TableColumn<StudentAverageTableRow, Integer> column1 = new TableColumn<>("ID");
        column1.setCellValueFactory(new PropertyValueFactory<>("idStudent"));
        column1.setMinWidth(60);
        column1.setMaxWidth(60);

        TableColumn<StudentAverageTableRow, String> column2 = new TableColumn<>("Name");
        column2.setCellValueFactory(new PropertyValueFactory<>("numeStudent"));
        column2.setMinWidth(260);
        column2.setMaxWidth(260);

        TableColumn<StudentAverageTableRow, Integer> column3 = new TableColumn<>("Class");
        column3.setCellValueFactory(new PropertyValueFactory<>("grupaStudent"));
        column3.setMinWidth(80);
        column3.setMaxWidth(80);

        TableColumn<StudentAverageTableRow, Integer> column4 = new TableColumn<>("Average");
        column4.setCellValueFactory(new PropertyValueFactory<>("medie"));
        column4.setMinWidth(130);
        column4.setMaxWidth(130);

        tableView.getColumns().addAll(column1, column2, column3, column4);

        for (Pair<Student, Float> pair : lista) {
            tableView.getItems().add(new StudentAverageTableRow(pair.getKey(), pair.getValue()));
        }

        saveAsPDFButton.setDisable(false);
    }

    public void showGradeAverageAssignments(ActionEvent actionEvent) {
        // initializing the PDF exporting variables
        statisticsTitle = "GRADE AVERAGE OF EVERY ASSIGNMENT";
        listaSelectata = "lista2";

        // Grade average of every assignment
        List<Pair<Tema, Float>> lista = new ArrayList<>();
        lista2 = lista;
        for (Tema tema : service.getAllTeme()) {
            float medie = 0;
            int nrNote = 0;
            for (Nota nota : service.getAllNote()) {
                if (Objects.equals(nota.getIdTema(), tema.getId())) {
                    medie += nota.getValoare();
                    nrNote++;
                }
            }
            if (nrNote > 0)
                medie = medie / nrNote;
            lista.add(new Pair<>(tema, medie));
        }

        // Making the piechart work
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

        // making the barchart work
        XYChart.Series<String, Number> series1 = new XYChart.Series<>();
        series1.setName("");
        for (Pair<String, Float> pair : listaPieChart) {
            series1.getData().add(new XYChart.Data<>(pair.getKey(), pair.getValue()));
        }
        barChart.getData().clear();
        barChart.getData().add(series1);

        // making the table work
        tableView.getItems().clear();
        tableView.getColumns().clear();

        TableColumn<AssignmentAverageTableRow, Integer> column1 = new TableColumn<>("ID");
        column1.setCellValueFactory(new PropertyValueFactory<>("idTema"));
        column1.setMinWidth(60);
        column1.setMaxWidth(60);

        TableColumn<AssignmentAverageTableRow, String> column2 = new TableColumn<>("Description");
        column2.setCellValueFactory(new PropertyValueFactory<>("descriereTema"));
        column2.setMinWidth(340);
        column2.setMaxWidth(340);

        TableColumn<AssignmentAverageTableRow, Integer> column3 = new TableColumn<>("Average");
        column3.setCellValueFactory(new PropertyValueFactory<>("medie"));
        column3.setMinWidth(130);
        column3.setMaxWidth(130);

        tableView.getColumns().addAll(column1, column2, column3);

        for (Pair<Tema, Float> pair : lista) {
            tableView.getItems().add(new AssignmentAverageTableRow(pair.getKey(), pair.getValue()));
        }

        saveAsPDFButton.setDisable(false);
    }

    public void showStudentsAverageAbove(ActionEvent actionEvent) {
        try {
            Float medieMinima = Float.parseFloat(averageTextField.getText());

            // initializing the PDF exporting variables
            statisticsTitle = "STUDENTS WITH AN AVERAGE ABOVE " + medieMinima.toString();
            listaSelectata = "lista3";

            // Students with an average above the one specified in the TextField
            List<Pair<Student, Float>> lista = new ArrayList<>();
            lista3 = lista;
            for (Student student : service.getAllStudenti()) {
                float medie = 0;
                int nrNote = service.getTemeCount();
                for (Tema tema : service.getAllTeme()) {
                    String id = "" + student.getId() + "_" + tema.getId();
                    Optional<Nota> notaOptional = service.findNota(id);
                    if (notaOptional.isPresent())
                        medie += notaOptional.get().getValoare();
                    else
                        medie += 1;
                }
                if (nrNote > 0)
                    medie = medie / nrNote;
                if (medie >= medieMinima) {
                    lista.add(new Pair<>(student, medie));
                }
            }

            // Making the piechart work
            List<PieChart.Data> listaPieChart = new ArrayList<>();
            int valueCount = Math.round(((float) lista.size() / (float) service.getStudentiCount()) * 100);
            listaPieChart.add(new PieChart.Data("#Averages over " + medieMinima.toString(), valueCount));
            listaPieChart.add(new PieChart.Data("#Averages under " + medieMinima.toString(), 100 - valueCount));
            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(listaPieChart);
            pieChart.setData(pieChartData);

            // making the barchart work
            List<Pair<String, Float>> listaBarChart = new ArrayList<>();
            for (int i = 0; i < 11; i++) {
                listaBarChart.add(new Pair<>(Integer.toString(i), 0.0f));
            }
            for (int i = 0; i < lista.size(); i++) {
                Pair<String, Float> pereche = new Pair<>(listaBarChart.get(Math.round(lista.get(i).getValue())).getKey(),
                        listaBarChart.get(Math.round(lista.get(i).getValue())).getValue() + 1);
                listaBarChart.set(Math.round(lista.get(i).getValue()), pereche);
            }
            XYChart.Series<String, Number> series1 = new XYChart.Series<>();
            series1.setName("");
            for (Pair<String, Float> pair : listaBarChart) {
                series1.getData().add(new XYChart.Data<>(pair.getKey(), pair.getValue()));
            }
            barChart.getData().clear();
            barChart.getData().add(series1);

            // making the table work
            tableView.getItems().clear();
            tableView.getColumns().clear();

            TableColumn<StudentAverageTableRow, Integer> column1 = new TableColumn<>("ID");
            column1.setCellValueFactory(new PropertyValueFactory<>("idStudent"));
            column1.setMinWidth(60);
            column1.setMaxWidth(60);

            TableColumn<StudentAverageTableRow, String> column2 = new TableColumn<>("Name");
            column2.setCellValueFactory(new PropertyValueFactory<>("numeStudent"));
            column2.setMinWidth(260);
            column2.setMaxWidth(260);

            TableColumn<StudentAverageTableRow, Integer> column3 = new TableColumn<>("Class");
            column3.setCellValueFactory(new PropertyValueFactory<>("grupaStudent"));
            column3.setMinWidth(80);
            column3.setMaxWidth(80);

            TableColumn<StudentAverageTableRow, Integer> column4 = new TableColumn<>("Average");
            column4.setCellValueFactory(new PropertyValueFactory<>("medie"));
            column4.setMinWidth(130);
            column4.setMaxWidth(130);

            tableView.getColumns().addAll(column1, column2, column3, column4);

            for (Pair<Student, Float> pair : lista) {
                tableView.getItems().add(new StudentAverageTableRow(pair.getKey(), pair.getValue()));
            }

            saveAsPDFButton.setDisable(false);
        }
        catch (NumberFormatException e) {
            LoginController.showErrorMessage("A real number as a minimum average must be specified!");
        }
    }

    public void saveAsPDF(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home") + "/Desktop"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF document (*.pdf)", "*.pdf"));
        fileChooser.setInitialFileName("*.pdf");
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
                    s += "Student " + pair.getKey().getId().toString() + ". ";
                    s += pair.getKey().getNume() + " from Group ";
                    s += pair.getKey().getGrupa().toString() + " has an average of  ";
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
                for (Pair<Tema, Float> pair : lista2) {
                    String s = "";
                    s += "Assignment " + pair.getKey().getId().toString() + ": \"";
                    s += pair.getKey().getDescriere() + "\" has a grade average of ";
                    s += pair.getValue().toString() + ".";
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
                    s += "Student " + pair.getKey().getId().toString() + ". ";
                    s += pair.getKey().getNume() + " from Group ";
                    s += pair.getKey().getGrupa().toString() + " has an average of  ";
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
            LoginController.showErrorMessage("The file could not be created: " + e.getMessage());
        } catch (NullPointerException ignored) {

        }
    }
}
