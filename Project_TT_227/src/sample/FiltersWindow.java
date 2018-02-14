package sample;

import Domain.Globals;
import Domain.Nota;
import Domain.Student;
import Domain.Tema;
import Exceptions.RepositoryException;
import Repositories.*;
import Service.Service;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FiltersWindow {

    public TabPane tabPane;
    public TableView<Student> studentsTable;
    public TableColumn<Student, Integer> idColumn;
    public TableColumn<Student, String> nameColumn;
    public TableColumn<Student, Integer> grupaColumn;
    public TableColumn<Student, String> profColumn;
    public TableColumn<Student, String> emailColumn;
    public TableView<Tema> homeworksTable;
    public TableColumn<Tema, Integer> nrTemaColumn;
    public TableColumn<Tema, String> descriereColumn;
    public TableColumn<Tema, Integer> deadlineColumn;
    public TableView<Nota> marksTable;
    public TableColumn<Nota, Integer> idtemaColumn;
    public TableColumn<Nota, Integer> studentidColumn;
    public TableColumn<Nota, Integer> temaColumn;
    public TableColumn<Nota, Integer> notaColumn;
    public Label lblMessage;
    public ComboBox<String> comboBox;
    public ChoiceBox<String> choiceBox1;
    public TextField txt1;
    public RadioButton radio1;
    public RadioButton radio2;
    public RadioButton radio3;
    public TextField txt2;
    public Slider slider1;
    public TextField txt3;
    public Button btnReset;
    public Menu menuFile;
    public Label lblStudent;
    public Button btnPrevPage;
    public Button btnNextPage;
    public TextField txtPage;
    private Integer currentPageStudents;
    private Integer currentPageHomeworks;
    private Integer currentPageMarks;
    private Integer noOfPages;
    public TextField txtGoToPage;
    private Service service;

    @FXML
    private void initialize() {

        currentPageStudents = 1;
        currentPageHomeworks = 1;
        currentPageMarks = 1;

        comboBox.getItems().addAll("Studenti","Teme","Nota");
        choiceBox1.setDisable(true);
        txt1.setDisable(true);
        txt2.setDisable(true);
        txt3.setDisable(true);
        slider1.setDisable(true);
        radio1.setDisable(true);
        radio2.setDisable(true);
        radio3.setDisable(true);
        choiceBox1.getItems().addAll("Dupa nume", "Dupa indrumator", "Dupa provider E-mail");

        lblMessage.setText("All good so far. ");

        idColumn.setCellValueFactory(new PropertyValueFactory<>("ID"));
        idColumn.setStyle( "-fx-alignment: CENTER;");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("nume"));
        nameColumn.setStyle( "-fx-alignment: CENTER;");
        grupaColumn.setCellValueFactory(new PropertyValueFactory<>("grupa"));
        grupaColumn.setStyle( "-fx-alignment: CENTER;");
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        emailColumn.setStyle( "-fx-alignment: CENTER;");
        profColumn.setCellValueFactory(new PropertyValueFactory<>("profLab"));
        profColumn.setStyle( "-fx-alignment: CENTER;");

        nrTemaColumn.setCellValueFactory(new PropertyValueFactory<>("ID"));
        nrTemaColumn.setStyle( "-fx-alignment: CENTER;");
        descriereColumn.setCellValueFactory(new PropertyValueFactory<>("descriere"));
        descriereColumn.setStyle( "-fx-alignment: CENTER;");
        deadlineColumn.setCellValueFactory(new PropertyValueFactory<>("deadline"));
        deadlineColumn.setStyle( "-fx-alignment: CENTER;");

        idtemaColumn.setCellValueFactory(new PropertyValueFactory<>("ID"));
        idtemaColumn.setStyle( "-fx-alignment: CENTER;");
        studentidColumn.setCellValueFactory(new PropertyValueFactory<>("idStudent"));
        studentidColumn.setStyle( "-fx-alignment: CENTER;");
        temaColumn.setCellValueFactory(new PropertyValueFactory<>("nrTema"));
        temaColumn.setStyle( "-fx-alignment: CENTER;");
        notaColumn.setCellValueFactory(new PropertyValueFactory<>("nota"));
        notaColumn.setStyle( "-fx-alignment: CENTER;");

        Label l1 = new Label("File");
        l1.setStyle("-fx-text-fill: white; -fx-text-alignment: center;");
        menuFile.setGraphic(l1);

        Tab studentsTab = tabPane.getTabs().get(0);
        studentsTab.setStyle("-fx-background-color: rgb(0,80,100);");
        studentsTab.setGraphic(new Label("Students"));
        studentsTab.getGraphic().setStyle("-fx-text-fill: white");

        Tab homeworksTab = tabPane.getTabs().get(1);
        homeworksTab.setStyle("-fx-background-color: rgb(0,80,100);");
        homeworksTab.setGraphic(new Label("Homeworks"));
        homeworksTab.getGraphic().setStyle("-fx-text-fill: white");

        Tab marksTab = tabPane.getTabs().get(2);
        marksTab.setStyle("-fx-background-color: rgb(0,80,100);");
        marksTab.setGraphic(new Label("Marks"));
        marksTab.getGraphic().setStyle("-fx-text-fill: white");

        marksTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue)->fillFieldsMarks(newValue)
        );
    }

    private void fillFieldsMarks(Nota nota) {
        if (nota == null) return;

        if (tabPane.getTabs().get(2).isSelected()) {
            try {
                lblStudent.setText(String.valueOf(service.getStudentByMark(nota.getIdStudent()).getNume()));
            } catch (RepositoryException e) {
                lblMessage.setText(e.getMessage());
                lblMessage.setTextFill(Color.RED);
            }
        }
    }

    public void setService(Service service) {
        this.service = service;
        updateTables();
        if(tabPane.getTabs().get(0).isSelected())
            noOfPages = service.noOfStudents()/12 + (service.noOfStudents() > service.noOfStudents()/12 * 12 ? 1 : 0);
        txtPage.setText(currentPageStudents + "/" + noOfPages);
    }

    @FXML
    private void closeButton() {
        Platform.exit();
        System.exit(0);
    }

    @FXML
    private void comboActivation() {
        updateTables();
        lblMessage.setText("All good so far.");
        lblMessage.setTextFill(Color.WHITE);
        if(comboBox.getSelectionModel().isSelected(0)) {
            choiceBox1.setDisable(false);
            txt1.setDisable(false);
            txt2.setDisable(true);
            txt2.setText("");
            txt3.setDisable(true);
            txt3.setText("");
            slider1.setDisable(true);
            slider1.setValue(0);
            radio1.setDisable(true);
            radio1.setSelected(false);
            radio2.setDisable(true);
            radio2.setSelected(false);
            radio3.setDisable(true);
            radio3.setSelected(false);
        }
        if(comboBox.getSelectionModel().isSelected(1)) {
            radio1.setSelected(true);
            choiceBox1.setDisable(true);
            txt1.setDisable(true);
            txt1.setText("");
            txt2.setDisable(false);
            txt3.setDisable(true);
            txt3.setText("");
            slider1.setDisable(true);
            slider1.setValue(0);
            radio1.setDisable(false);
            radio2.setDisable(false);
            radio3.setDisable(false);
        }
        if(comboBox.getSelectionModel().isSelected(2)) {
            choiceBox1.setDisable(true);
            txt1.setDisable(true);
            txt1.setText("");
            txt2.setDisable(true);
            txt2.setText("");
            txt3.setDisable(false);
            slider1.setDisable(false);
            radio1.setDisable(true);
            radio1.setSelected(false);
            radio2.setDisable(true);
            radio2.setSelected(false);
            radio3.setDisable(true);
            radio3.setSelected(false);
        }
    }

    @FXML
    private void resetAction() {
        updateNoOfPages();
        //lblMessage.setText("Reset succesful! ");
        //lblMessage.setTextFill(Color.CYAN);
        txt1.setText("");
        txt2.setText("");
        txt3.setText("");
    }

    @FXML
    private void goBackToCRUD() {
        Parent CRUDWindow;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainWindow.fxml"));
        try {
            CRUDWindow = loader.load();
        } catch (IOException e) {
            lblMessage.setTextFill(Color.RED);
            lblMessage.setText(e.getMessage());
            return;
        }
        MainWindow mainWindow = loader.getController();
        mainWindow.setService(service);
        SQLStudentRepo sqlStudentRepo = service.getStudentRepository();
        sqlStudentRepo.addObserver(mainWindow);
        SQLTemaRepo sqlTemaRepo = service.getTemaRepository();
        sqlTemaRepo.addObserver(mainWindow);
        SQLNotaRepo sqlNotaRepo = service.getNotaRepository();
        sqlNotaRepo.addObserver(mainWindow);
        Globals.getInstance().addObserver(mainWindow);
        Scene CRUDScene = new Scene(CRUDWindow);
        Stage CRUDStage = (Stage)lblMessage.getScene().getWindow();
        CRUDStage.setTitle("Lab6 - Main Window");
        CRUDStage.setScene(CRUDScene);
        CRUDScene.getStylesheets().add("Theme.css");
        CRUDScene.getStylesheets().add("scrollbar.css");
        CRUDStage.show();
    }

    @FXML
    private void RapoarteWindow() {
        Parent RapoarteWindow;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Rapoarte.fxml"));
        try {
            RapoarteWindow = loader.load();
        } catch (IOException e) {
            lblMessage.setTextFill(Color.RED);
            lblMessage.setText(e.getMessage());
            return;
        }
        Rapoarte rapoarteWindow = loader.getController();
        rapoarteWindow.setService(service);
        Scene RapoarteScene = new Scene(RapoarteWindow);
        Stage RapoarteStage = (Stage)btnReset.getScene().getWindow();
        RapoarteStage.setTitle("Rapoarte");
        RapoarteScene.getStylesheets().add("Theme.css");
        //RapoarteScene.getStylesheets().add("scrollbar.css");
        RapoarteStage.setScene(RapoarteScene);
        RapoarteStage.show();
    }

    private void updateStudentsTable() {
        Iterable<Student> itS = service.findAllStudents();
        ArrayList<Student> allStudents = new ArrayList<>();
        ObservableList<Student> observableListS;
        for (Student student : itS)
            allStudents.add(student);
        if(allStudents.size() >= (currentPageStudents-1)*12+12) {
            List<Student> allStudentsList = allStudents.subList((currentPageStudents - 1) * 12, (currentPageStudents - 1) * 12 + 12);
            observableListS = FXCollections.observableList(allStudentsList);
        }
        else {
            List<Student> allStudentsList = allStudents.subList((currentPageStudents-1)*12,(currentPageStudents-1)*12+allStudents.size()-(currentPageStudents-1)*12);
            observableListS = FXCollections.observableList(allStudentsList);
        }
        studentsTable.setItems(observableListS);
    }

    private void updateHomeworksTable() {
        Iterable<Tema> itT = service.findAllHomeworks();
        ArrayList<Tema> allHomeworks = new ArrayList<>();
        ObservableList<Tema> observableListT;
        for (Tema tema : itT)
            allHomeworks.add(tema);
        if(allHomeworks.size() >= (currentPageHomeworks-1)*12+12) {
            List<Tema> allHomeworksList = allHomeworks.subList((currentPageHomeworks - 1) * 12, (currentPageHomeworks - 1) * 12 + 12);
            observableListT = FXCollections.observableList(allHomeworksList);
        }
        else {
            List<Tema> allHomeworksList = allHomeworks.subList((currentPageHomeworks-1)*12,(currentPageHomeworks-1)*12+allHomeworks.size()-(currentPageHomeworks-1)*12);
            observableListT = FXCollections.observableList(allHomeworksList);
        }
        homeworksTable.setItems(observableListT);
    }

    private void updateMarksTable() {
        Iterable<Nota> itN = service.findAllMarks();
        ArrayList<Nota> allMarks = new ArrayList<>();
        ObservableList<Nota> observableListN;
        for (Nota nota : itN)
            allMarks.add(nota);
        if(allMarks.size() >= (currentPageMarks-1)*12+12) {
            List<Nota> allMarksList = allMarks.subList((currentPageMarks - 1) * 12, (currentPageMarks - 1) * 12 + 12);
            observableListN = FXCollections.observableList(allMarksList);
        }
        else {
            List<Nota> allMarksList = allMarks.subList((currentPageMarks - 1) * 12, (currentPageMarks - 1) * 12 + allMarks.size() - (currentPageMarks - 1) * 12);
            observableListN = FXCollections.observableList(allMarksList);
        }
        marksTable.setItems(observableListN);
    }

    @FXML
    private void updateTables() {
        updateStudentsTable();
        updateHomeworksTable();
        updateMarksTable();
        /*Iterable<Student> itS = service.findAllStudents();
        ArrayList<Student> allStudents = new ArrayList<>();
        for (Student student : itS)
            allStudents.add(student);
        ObservableList<Student> observableListS = FXCollections.observableList(allStudents);
        studentsTable.setItems(observableListS);
        Iterable<Tema> itT = service.findAllHomeworks();
        ArrayList<Tema> allHomeworks = new ArrayList<>();
        for (Tema tema : itT)
            allHomeworks.add(tema);
        ObservableList<Tema> observableListT = FXCollections.observableList(allHomeworks);
        homeworksTable.setItems(observableListT);
        Iterable<Nota> itN = service.findAllMarks();
        ArrayList<Nota> allMarks = new ArrayList<>();
        for (Nota nota : itN)
            allMarks.add(nota);
        ObservableList<Nota> observableListN = FXCollections.observableList(allMarks);
        marksTable.setItems(observableListN);*/
    }

    /*@FXML
    private void updateEverything() {
        updateTables();
    }*/

    @FXML
    private void radioButton1Property() {
        if(txt2.getText().length() > 0)
            doFilters();
        if(radio2.isSelected()) {
            radio2.setSelected(false);
            radio1.setSelected(true);
        }
        if(radio3.isSelected()) {
            radio3.setSelected(false);
            radio1.setSelected(true);
        }
    }

    @FXML
    private void radioButton2Property() {
        if(txt2.getText().length() > 0)
            doFilters();
        if(radio1.isSelected()) {
            radio1.setSelected(false);
            radio2.setSelected(true);
        }
        if(radio3.isSelected()) {
            radio3.setSelected(false);
            radio2.setSelected(true);
        }
    }

    @FXML
    private void radioButton3Property() {
        if(txt2.getText().length() > 0)
            doFilters();
        if(radio1.isSelected()) {
            radio1.setSelected(false);
            radio3.setSelected(true);
        }
        if(radio2.isSelected()) {
            radio2.setSelected(false);
            radio3.setSelected(true);
        }
    }

    @FXML
    private void doFilters() {
        txtGoToPage.setText("");
        if(comboBox.getSelectionModel().isSelected(0)) {
            if(choiceBox1.getSelectionModel().isSelected(0)) {
                if (txt1.getText().length() > 0) {
                    ObservableList<Student> observableListS = FXCollections.observableList(service.filterByName(txt1.getText()));
                    studentsTable.setItems(observableListS);
                    updateNoOfPagesAfterFilter();
                    lblMessage.setText("Table filtered! ");
                    lblMessage.setTextFill(Color.CYAN);
                }
                else {
                    resetAction();
                    lblMessage.setText("Introduceti un string. ");
                    lblMessage.setTextFill(Color.RED);
                }
            }
            else if(choiceBox1.getSelectionModel().isSelected(1)) {
                if (txt1.getText().length() > 0) {
                    ObservableList<Student> observableListS = FXCollections.observableList(service.filterByIndrumator(txt1.getText()));
                    studentsTable.setItems(observableListS);
                    lblMessage.setText("Table filtered! ");
                    lblMessage.setTextFill(Color.CYAN);
                }
                else {
                    resetAction();
                    lblMessage.setText("Introduceti un string. ");
                    lblMessage.setTextFill(Color.RED);
                }
            }
            else if(choiceBox1.getSelectionModel().isSelected(2)) {
                if (txt1.getText().length() > 0) {
                    ObservableList<Student> observableListS = FXCollections.observableList(service.filterByEmail(txt1.getText()));
                    studentsTable.setItems(observableListS);
                    lblMessage.setText("Table filtered! ");
                    lblMessage.setTextFill(Color.CYAN);
                }
                else {
                    resetAction();
                    lblMessage.setText("Introduceti un string. ");
                    lblMessage.setTextFill(Color.RED);
                }
            }
            else {
                lblMessage.setText("Alegeti un criteriu. ");
                lblMessage.setTextFill(Color.RED);
            }
        }
        if(comboBox.getSelectionModel().isSelected(1)) {
            if(radio1.isSelected()) {
                if (txt2.getText().length() > 0) {
                    ObservableList<Tema> observableListT = FXCollections.observableList(service.filterByDescriere(txt2.getText()));
                    homeworksTable.setItems(observableListT);
                    lblMessage.setText("Table filtered! ");
                    lblMessage.setTextFill(Color.CYAN);
                }
                else {
                    resetAction();
                    lblMessage.setText("Introduceti un string. ");
                    lblMessage.setTextFill(Color.RED);
                }
            }
            else if(radio2.isSelected()) {
                if (txt2.getText().length() > 0) {
                    Integer deadline;
                    try {
                        deadline = Integer.valueOf(txt2.getText());
                    } catch (NumberFormatException e) {
                        lblMessage.setText("Deadline - Integer. ");
                        lblMessage.setTextFill(Color.RED);
                        return;
                    }
                    ObservableList<Tema> observableListT = FXCollections.observableList(service.filterByDeadline(deadline));
                    homeworksTable.setItems(observableListT);
                    lblMessage.setText("Table filtered! ");
                    lblMessage.setTextFill(Color.CYAN);
                }
                else {
                    resetAction();
                    lblMessage.setText("Introduceti un string. ");
                    lblMessage.setTextFill(Color.RED);
                }
            }
            else if(radio3.isSelected()) {
                if (txt2.getText().length() > 0) {
                    Integer nrTema;
                    try {
                        nrTema = Integer.valueOf(txt2.getText());
                    } catch (NumberFormatException e) {
                        lblMessage.setText("Numar tema - Integer. ");
                        lblMessage.setTextFill(Color.RED);
                        return;
                    }
                    ObservableList<Tema> observableListT = FXCollections.observableList(service.filterByNrTema(nrTema));
                    homeworksTable.setItems(observableListT);
                    lblMessage.setText("Table filtered! ");
                    lblMessage.setTextFill(Color.CYAN);
                }
                else {
                    resetAction();
                    lblMessage.setText("Introduceti un string. ");
                    lblMessage.setTextFill(Color.RED);
                }
            }
            else {
                lblMessage.setText("Alegeti un criteriu. ");
                lblMessage.setTextFill(Color.RED);
            }
        }
        if(comboBox.getSelectionModel().isSelected(2)) {
            if(slider1.getValue() == 0) {
                if (txt3.getText().length() > 0) {
                    Integer nota;
                    try {
                        nota = Integer.valueOf(txt3.getText());
                    } catch (NumberFormatException e) {
                        lblMessage.setText("Nota - Integer. ");
                        lblMessage.setTextFill(Color.RED);
                        return;
                    }
                    ObservableList<Nota> observableListN = FXCollections.observableList(service.filterByNota(nota));
                    marksTable.setItems(observableListN);
                    lblMessage.setText("Table filtered! ");
                    lblMessage.setTextFill(Color.CYAN);
                }
                else {
                    resetAction();
                    lblMessage.setText("Introduceti un string. ");
                    lblMessage.setTextFill(Color.RED);
                }
            }
            else if(slider1.getValue() == 1) {
                if (txt3.getText().length() > 0) {
                    Integer idStudent;
                    try {
                        idStudent = Integer.valueOf(txt3.getText());
                    } catch (NumberFormatException e) {
                        lblMessage.setText("ID Student - Integer. ");
                        lblMessage.setTextFill(Color.RED);
                        return;
                    }
                    ObservableList<Nota> observableListN = FXCollections.observableList(service.filterByStudent(idStudent));
                    marksTable.setItems(observableListN);
                    lblMessage.setText("Table filtered! ");
                    lblMessage.setTextFill(Color.CYAN);
                }
                else {
                    resetAction();
                    lblMessage.setText("Introduceti un string. ");
                    lblMessage.setTextFill(Color.RED);
                }
            }
            else if(slider1.getValue() == 2) {
                if (txt3.getText().length() > 0) {
                    Integer nrTema;
                    try {
                        nrTema = Integer.valueOf(txt3.getText());
                    } catch (NumberFormatException e) {
                        lblMessage.setText("Numar tema - Integer. ");
                        lblMessage.setTextFill(Color.RED);
                        return;
                    }
                    ObservableList<Nota> observableListN = FXCollections.observableList(service.filterByTema(nrTema));
                    marksTable.setItems(observableListN);
                    lblMessage.setText("Table filtered! ");
                    lblMessage.setTextFill(Color.CYAN);
                }
                else {
                    resetAction();
                    lblMessage.setText("Introduceti un string. ");
                    lblMessage.setTextFill(Color.RED);
                }
            }
            else {
                lblMessage.setText("Alegeti un criteriu. ");
                lblMessage.setTextFill(Color.RED);
            }
        }
    }
    @FXML
    private void goToNextPage() {
        txtGoToPage.setText("");
        if(tabPane.getTabs().get(0).isSelected()) {
            if (currentPageStudents + 1 <= noOfPages) {
                currentPageStudents += 1;
                txtPage.setText(currentPageStudents.toString() + "/" + noOfPages);
                updateStudentsTable();
            }
        }
        else if(tabPane.getTabs().get(1).isSelected()) {
            if (currentPageHomeworks + 1 <= noOfPages) {
                currentPageHomeworks += 1;
                txtPage.setText(currentPageHomeworks.toString() + "/" + noOfPages);
                updateHomeworksTable();
            }
        }
        else if(tabPane.getTabs().get(2).isSelected()) {
            if (currentPageMarks + 1 <= noOfPages) {
                currentPageMarks += 1;
                txtPage.setText(currentPageMarks.toString() + "/" + noOfPages);
                updateMarksTable();
            }
        }
    }

    @FXML
    private void goToPreviousPage() {
        txtGoToPage.setText("");
        if(tabPane.getTabs().get(0).isSelected()) {
            if (currentPageStudents - 1 >= 1) {
                currentPageStudents -= 1;
                txtPage.setText(currentPageStudents.toString() + "/" + noOfPages);
                updateStudentsTable();
            }
        }
        else if(tabPane.getTabs().get(1).isSelected()) {
            if (currentPageHomeworks - 1 >= 1) {
                currentPageHomeworks -= 1;
                txtPage.setText(currentPageHomeworks.toString() + "/" + noOfPages);
                updateHomeworksTable();
            }
        }
        else if(tabPane.getTabs().get(2).isSelected()) {
            if (currentPageMarks - 1 >= 1) {
                currentPageMarks -= 1;
                txtPage.setText(currentPageMarks.toString() + "/" + noOfPages);
                updateMarksTable();
            }
        }
    }

    @FXML
    private void goToPage() {
        Integer page;
        if(Objects.equals(txtGoToPage.getText(), "")) {
            txtGoToPage.setStyle("");
            return;
        }
        try {
            page = Integer.valueOf(txtGoToPage.getText());
        } catch (Exception e) {
            txtGoToPage.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            return;
        }
        if(page < 1 || page > noOfPages){
            txtGoToPage.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
        }
        else {
            txtGoToPage.setStyle("");
            if(tabPane.getTabs().get(0).isSelected()) {
                currentPageStudents = page;
                txtPage.setText(currentPageStudents + "/" + noOfPages);
                updateStudentsTable();
            }
            else if(tabPane.getTabs().get(1).isSelected()) {
                currentPageHomeworks = page;
                txtPage.setText(currentPageHomeworks + "/" + noOfPages);
                updateHomeworksTable();
            }
            else if(tabPane.getTabs().get(2).isSelected()) {
                currentPageMarks = page;
                txtPage.setText(currentPageMarks + "/" + noOfPages);
                updateMarksTable();
            }
        }
    }

    private void updateNoOfPagesAfterFilter() {
        currentPageStudents = 1;
        currentPageHomeworks = 1;
        currentPageMarks = 1;

        if(tabPane.getTabs().get(0).isSelected()) {
            noOfPages = studentsTable.getItems().size() / 12 + (studentsTable.getItems().size() > studentsTable.getItems().size() / 12 * 12 ? 1 : 0);
            txtPage.setText(currentPageStudents + "/" + noOfPages);
        }
        else if(tabPane.getTabs().get(1).isSelected()) {
            noOfPages = service.noOfHomeworks() / 12 + (service.noOfHomeworks() > service.noOfHomeworks() / 12 * 12 ? 1 : 0);
            txtPage.setText(currentPageHomeworks + "/" + noOfPages);
        }
        else if(tabPane.getTabs().get(2).isSelected()) {
            noOfPages = service.noOfMarks() / 12 + (service.noOfMarks() > service.noOfMarks() / 12 * 12 ? 1 : 0);
            txtPage.setText(currentPageMarks + "/" + noOfPages);
        }
    }

    @FXML
    private void updateNoOfPages() {

        currentPageStudents = 1;
        currentPageHomeworks = 1;
        currentPageMarks = 1;

        updateTables();

        if(tabPane.getTabs().get(0).isSelected()) {
            noOfPages = service.noOfStudents() / 12 + (service.noOfStudents() > service.noOfStudents() / 12 * 12 ? 1 : 0);
            txtPage.setText(currentPageStudents + "/" + noOfPages);
        }
        else if(tabPane.getTabs().get(1).isSelected()) {
            noOfPages = service.noOfHomeworks() / 12 + (service.noOfHomeworks() > service.noOfHomeworks() / 12 * 12 ? 1 : 0);
            txtPage.setText(currentPageHomeworks + "/" + noOfPages);
        }
        else if(tabPane.getTabs().get(2).isSelected()) {
            noOfPages = service.noOfMarks() / 12 + (service.noOfMarks() > service.noOfMarks() / 12 * 12 ? 1 : 0);
            txtPage.setText(currentPageMarks + "/" + noOfPages);
        }
    }
}
