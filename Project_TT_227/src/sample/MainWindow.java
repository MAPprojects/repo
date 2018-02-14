package sample;

import Domain.Globals;
import Domain.Nota;
import Domain.Student;
import Domain.Tema;
import Exceptions.RepositoryException;
import Service.Service;
import Utils.Observer;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class MainWindow implements Observer {

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
    public TableColumn<Nota, Integer> idNotaColumn;
    public TableColumn<Nota, Integer> studentidColumn;
    public TableColumn<Nota, Integer> temaColumn;
    public TableColumn<Nota, Integer> notaColumn;
    public Label label1;
    public TextField txt1;
    public Label label2;
    public TextField txt2;
    public Label label3;
    public TextField txt3;
    public Label label4;
    public TextField txt4;
    public Label label5;
    public TextField txt5;
    public Label lblSaptCurenta;
    public Label lblMessage;
    public Button btnAdd;
    public Button btnDelete;
    public Button btnUpdate;
    public TabPane tabPane;
    public MenuItem menuSaptCurenta;
    public Slider slider1;
    public Label lbl1;
    public Label lbl2;
    public Label lbl3;
    public ImageView imageView1;
    public Menu menuFile;
    public Menu menuEdit;
    public Menu menuView;
    public Button btnPrevPage;
    public Button btnNextPage;
    public TextField txtPage;
    public Label lblDate;
    public ImageView imgEmail;
    private Integer noOfPages;
    private Integer currentPageStudents;
    private Integer currentPageHomeworks;
    private Integer currentPageMarks;
    private Integer studentsServiceSize;
    private Integer homeworksServiceSize;
    private Integer marksServiceSize;
    public TextField txtGoToPage;
    public Tab tab1;
    public Tab tab2;
    public Tab tab3;
    private Service service;

    @FXML
    private void initialize() {

        allToolTips();

        currentPageStudents = 1;
        currentPageHomeworks = 1;
        currentPageMarks = 1;

        imgEmail.setImage(new Image("Email.png"));
        //Tooltip.install(imgEmail,new Tooltip("Website-ul facultatii"));

        lblDate.setText(lblDate.getText() + " " + LocalDateTime.now().format(DateTimeFormatter.ISO_DATE));

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

        idNotaColumn.setCellValueFactory(new PropertyValueFactory<>("ID"));
        idNotaColumn.setStyle( "-fx-alignment: CENTER;");
        studentidColumn.setCellValueFactory(new PropertyValueFactory<>("idStudent"));
        studentidColumn.setStyle( "-fx-alignment: CENTER;");
        temaColumn.setCellValueFactory(new PropertyValueFactory<>("nrTema"));
        temaColumn.setStyle( "-fx-alignment: CENTER;");
        notaColumn.setCellValueFactory(new PropertyValueFactory<>("nota"));
        notaColumn.setStyle( "-fx-alignment: CENTER;");

        lblSaptCurenta.setText("Saptamana curenta: " + String.valueOf(Globals.getInstance().getSaptCurenta()));
        lblMessage.setText("All good so far. ");

        label1.setText("ID:");
        label2.setText("Nume:");
        label3.setText("Grupa:");
        label4.setText("E-mail:");
        label5.setText("Indrumator:");
        txt1.setVisible(true);
        txt2.setVisible(true);
        txt3.setVisible(true);
        txt4.setVisible(true);
        txt5.setVisible(true);
        label1.setVisible(true);
        label2.setVisible(true);
        label3.setVisible(true);
        label4.setVisible(true);
        label5.setVisible(true);

        Label l1 = new Label("File");
        l1.setStyle("-fx-text-fill: white; -fx-text-alignment: center;");
        menuFile.setGraphic(l1);

        Label l2 = new Label("Edit");
        l2.setStyle("-fx-text-fill: white; -fx-text-alignment: center;");
        menuEdit.setGraphic(l2);

        Label l3 = new Label("View");
        l3.setStyle("-fx-text-fill: white; -fx-text-alignment: center;");
        menuView.setGraphic(l3);

        Tab studentsTab = tabPane.getTabs().get(0);
        studentsTab.setStyle("-fx-background-color: rgb(0, 80, 100);");
        studentsTab.setGraphic(new Label("Students"));
        studentsTab.getGraphic().setStyle("-fx-text-fill: white");

        Tab homeworksTab = tabPane.getTabs().get(1);
        homeworksTab.setStyle("-fx-background-color: rgb(0, 80, 100);");
        homeworksTab.setGraphic(new Label("Homeworks"));
        homeworksTab.getGraphic().setStyle("-fx-text-fill: white");

        Tab marksTab = tabPane.getTabs().get(2);
        marksTab.setStyle("-fx-background-color: rgb(0, 80, 100);");
        marksTab.setGraphic(new Label("Marks"));
        marksTab.getGraphic().setStyle("-fx-text-fill: white");

        studentsTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue)->fillFieldsStudents(newValue)
        );
        homeworksTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue)->fillFieldsHomeworks(newValue)
        );
        marksTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue)->fillFieldsMarks(newValue)
        );
    }

    @FXML
    private void imageClicked() throws IOException, URISyntaxException {
        Desktop desktop = Desktop.getDesktop();
        desktop.browse(new URI("http://www.cs.ubbcluj.ro/"));
    }

    @FXML
    private void closeButton() {
        Platform.exit();
        System.exit(0);
    }

    @FXML
    private void allToolTips(){
        Tooltip Studentstooltip = new Tooltip("Tabel studenti");
        Tooltip Homeworkstooltip = new Tooltip("Tabel teme");
        Tooltip Markstooltip = new Tooltip("Tabel note");
        Tooltip Addtooltip = new Tooltip("Adaugari");
        Tooltip Deletetooltip = new Tooltip("Stergeri");
        Tooltip Updatetooltip = new Tooltip("Actualizari");
        tabPane.getTabs().get(0).setTooltip(Studentstooltip);
        tabPane.getTabs().get(1).setTooltip(Homeworkstooltip);
        tabPane.getTabs().get(2).setTooltip(Markstooltip);
        btnAdd.setTooltip(Addtooltip);
        btnDelete.setTooltip(Deletetooltip);
        btnUpdate.setTooltip(Updatetooltip);
    }

    private void fillFieldsStudents(Student student) {
        if (student == null) return;

        if(tabPane.getTabs().get(0).isSelected()) {
            txt1.setText(String.valueOf(student.getID()));
            txt2.setText(student.getNume());
            txt3.setText(String.valueOf(student.getGrupa()));
            txt4.setText(student.getEmail());
            txt5.setText(student.getProfLab());
        }
    }

    private void fillFieldsHomeworks(Tema tema) {
        if (tema == null) return;

        if(tabPane.getTabs().get(1).isSelected()) {
            txt1.setText(String.valueOf(tema.getID()));
            txt2.setText(tema.getDescriere());
            txt3.setText(String.valueOf(tema.getDeadline()));
        }
    }

    private void fillFieldsMarks(Nota nota) {
        if (nota == null) return;

        if(tabPane.getTabs().get(2).isSelected()) {
            txt1.setText(String.valueOf(nota.getIdStudent()));
            txt2.setText(String.valueOf(nota.getNrTema()));
            txt3.setText(String.valueOf(nota.getNota()));
            try {
                txt4.setText(String.valueOf(service.getStudentByMark(nota.getIdStudent()).getNume()));
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
        studentsServiceSize = service.noOfStudents();
        homeworksServiceSize = service.noOfHomeworks();
        marksServiceSize = service.noOfMarks();
    }

    @FXML
    private void movingElements() {
        btnAdd.setTranslateY(btnAdd.getTranslateY() - 90);
        btnDelete.setTranslateY(btnDelete.getTranslateY() - 90);
        btnUpdate.setTranslateY(btnUpdate.getTranslateY() - 90);
        slider1.setTranslateY(slider1.getTranslateY() - 90);
        lbl1.setTranslateY(lbl1.getTranslateY() - 90);
        lbl2.setTranslateY(lbl2.getTranslateY() - 90);
        lbl3.setTranslateY(lbl3.getTranslateY() - 90);
    }

    @FXML
    private void updateControls() {

        currentPageStudents = 1;
        currentPageHomeworks = 1;
        currentPageMarks = 1;

        updateTables();

        if(tabPane.getTabs().get(0).isSelected()) {
            slider1.setValue(0);
            noOfPages = service.noOfStudents() / 12 + (service.noOfStudents() > service.noOfStudents() / 12 * 12 ? 1 : 0);
            txtPage.setText(currentPageStudents + "/" + noOfPages);
        }
        else if(tabPane.getTabs().get(1).isSelected()) {
            slider1.setValue(1);
            noOfPages = service.noOfHomeworks() / 12 + (service.noOfHomeworks() > service.noOfHomeworks() / 12 * 12 ? 1 : 0);
            txtPage.setText(currentPageHomeworks + "/" + noOfPages);
        }
        else if(tabPane.getTabs().get(2).isSelected()) {
            slider1.setValue(2);
            noOfPages = service.noOfMarks() / 12 + (service.noOfMarks() > service.noOfMarks() / 12 * 12 ? 1 : 0);
            txtPage.setText(currentPageMarks + "/" + noOfPages);
        }

        txt1.setText("");
        txt2.setText("");
        txt3.setText("");
        txt4.setText("");
        txt5.setText("");

        if(tabPane.getTabs().get(0).isSelected()){
            label1.setText("ID:");
            label2.setText("Nume:");
            label3.setText("Grupa:");
            label4.setText("E-mail:");
            label5.setText("Indrumator:");
            txt1.setVisible(true);
            txt2.setVisible(true);
            txt3.setVisible(true);
            txt4.setVisible(true);
            txt5.setVisible(true);
            label1.setVisible(true);
            label2.setVisible(true);
            label3.setVisible(true);
            label4.setVisible(true);
            label5.setVisible(true);
            if(btnAdd.getTranslateY() == -90) {
                btnAdd.setTranslateY(btnAdd.getTranslateY() + 90);
                btnDelete.setTranslateY(btnDelete.getTranslateY() + 90);
                btnUpdate.setTranslateY(btnUpdate.getTranslateY() + 90);
                slider1.setTranslateY(slider1.getTranslateY() + 90);
                lbl1.setTranslateY(lbl1.getTranslateY() + 90);
                lbl2.setTranslateY(lbl2.getTranslateY() + 90);
                lbl3.setTranslateY(lbl3.getTranslateY() + 90);
            }
            if(btnAdd.getTranslateY() == -40) {
                btnAdd.setTranslateY(btnAdd.getTranslateY() + 40);
                btnDelete.setTranslateY(btnDelete.getTranslateY() + 40);
                btnUpdate.setTranslateY(btnUpdate.getTranslateY() + 40);
                slider1.setTranslateY(slider1.getTranslateY() + 40);
                lbl1.setTranslateY(lbl1.getTranslateY() + 40);
                lbl2.setTranslateY(lbl2.getTranslateY() + 40);
                lbl3.setTranslateY(lbl3.getTranslateY() + 40);
            }
        }
        if(tabPane.getTabs().get(1).isSelected()){
            label1.setText("Numar:");
            label2.setText("Descriere:");
            label3.setText("Deadline:");
            label4.setVisible(false);
            label5.setVisible(false);
            txt4.setVisible(false);
            txt5.setVisible(false);
            if(btnAdd.getTranslateY() == 0) {
                movingElements();
            }
            if(btnAdd.getTranslateY() == -90) {
                btnAdd.setTranslateY(btnAdd.getTranslateY() + 50);
                btnDelete.setTranslateY(btnDelete.getTranslateY() + 50);
                btnUpdate.setTranslateY(btnUpdate.getTranslateY() + 50);
                slider1.setTranslateY(slider1.getTranslateY() + 50);
                lbl1.setTranslateY(lbl1.getTranslateY() + 50);
                lbl2.setTranslateY(lbl2.getTranslateY() + 50);
                lbl3.setTranslateY(lbl3.getTranslateY() + 50);
            }
            if(btnAdd.getTranslateY() == -40) {
                btnAdd.setTranslateY(btnAdd.getTranslateY() - 50);
                btnDelete.setTranslateY(btnDelete.getTranslateY() - 50);
                btnUpdate.setTranslateY(btnUpdate.getTranslateY() - 50);
                slider1.setTranslateY(slider1.getTranslateY() - 50);
                lbl1.setTranslateY(lbl1.getTranslateY() - 50);
                lbl2.setTranslateY(lbl2.getTranslateY() - 50);
                lbl3.setTranslateY(lbl3.getTranslateY() - 50);
            }
        }
        if(tabPane.getTabs().get(2).isSelected()){
            label1.setText("ID Student:");
            label2.setText("Numar tema:");
            label3.setText("Nota:");
            label4.setText("Nume Student:");
            label4.setVisible(true);
            label5.setVisible(false);
            txt4.setVisible(true);
            txt5.setVisible(false);
            if(btnAdd.getTranslateY() == 0) {
                btnAdd.setTranslateY(btnAdd.getTranslateY() - 40);
                btnDelete.setTranslateY(btnDelete.getTranslateY() - 40);
                btnUpdate.setTranslateY(btnUpdate.getTranslateY() - 40);
                slider1.setTranslateY(slider1.getTranslateY() - 40);
                lbl1.setTranslateY(lbl1.getTranslateY() - 40);
                lbl2.setTranslateY(lbl2.getTranslateY() - 40);
                lbl3.setTranslateY(lbl3.getTranslateY() - 40);
            }
            if(btnAdd.getTranslateY() == -90) {
                btnAdd.setTranslateY(btnAdd.getTranslateY() + 50);
                btnDelete.setTranslateY(btnDelete.getTranslateY() + 50);
                btnUpdate.setTranslateY(btnUpdate.getTranslateY() + 50);
                slider1.setTranslateY(slider1.getTranslateY() + 50);
                lbl1.setTranslateY(lbl1.getTranslateY() + 50);
                lbl2.setTranslateY(lbl2.getTranslateY() + 50);
                lbl3.setTranslateY(lbl3.getTranslateY() + 50);
            }
        }
    }

    @FXML
    private void editSaptCurentaWindow() {
        Parent saptCurentaWindow;
        try {
            saptCurentaWindow = FXMLLoader.load(getClass().getResource("EditSaptCurenta.fxml"));
        } catch (IOException e) {
            lblMessage.setTextFill(Color.RED);
            lblMessage.setText(e.getMessage());
            return;
        }
        Scene saptCurentaScene = new Scene(saptCurentaWindow);
        Stage saptCurentaStage = new Stage();
        saptCurentaStage.setResizable(false);
        saptCurentaStage.setTitle("Schimbare saptamana curenta");
        saptCurentaScene.getStylesheets().add("Theme.css");
        saptCurentaStage.setScene(saptCurentaScene);
        saptCurentaStage.show();
    }

    @FXML
    private void FiltersWindow() {
        Parent FiltersWindow;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FiltersWindow.fxml"));
        try {
            FiltersWindow = loader.load();
        } catch (IOException e) {
            lblMessage.setTextFill(Color.RED);
            lblMessage.setText(e.getMessage());
            return;
        }
        FiltersWindow filtersWindow = loader.getController();
        filtersWindow.setService(service);
        Scene FiltersScene = new Scene(FiltersWindow);
        Stage FiltersStage = (Stage)btnAdd.getScene().getWindow();
        FiltersStage.setTitle("Filtrari");
        FiltersScene.getStylesheets().add("Theme.css");
        FiltersScene.getStylesheets().add("scrollbar.css");
        FiltersStage.setScene(FiltersScene);
        FiltersStage.show();
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
        Stage RapoarteStage = (Stage)btnAdd.getScene().getWindow();
        RapoarteStage.setTitle("Rapoarte");
        RapoarteScene.getStylesheets().add("Theme.css");
        //RapoarteScene.getStylesheets().add("scrollbar.css");
        RapoarteStage.setScene(RapoarteScene);
        RapoarteStage.show();
    }

    @FXML
    private void LogFileWindow() {
        Parent logFileWindow;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("LogWindow.fxml"));
        LogWindow logWindow;
        try {
            logFileWindow = loader.load();
            logWindow = loader.getController();
            logWindow.setService(service);
        } catch (IOException e) {
            lblMessage.setTextFill(Color.RED);
            lblMessage.setText(e.getMessage());
            return;
        }
        Scene logFileScene = new Scene(logFileWindow);
        Stage logFileStage = new Stage();
        logFileStage.setResizable(false);
        logFileStage.setTitle("Log Files");
        logFileScene.getStylesheets().add("Theme.css");
        logFileStage.setScene(logFileScene);
        logFileStage.show();
    }

    @FXML
    public void CRUDWindowAdd() {
        Parent addStudentsWindow;
        Parent addHomeworksWindow;
        Parent addMarksWindow;
        AddStudents addStudents;
        AddHomeworks addHomeworks;
        AddMarks addMarks;
        FXMLLoader loader1 = new FXMLLoader(getClass().getResource("AdaugareStudenti.fxml"));
        FXMLLoader loader2 = new FXMLLoader(getClass().getResource("AdaugareTeme.fxml"));
        FXMLLoader loader3 = new FXMLLoader(getClass().getResource("AdaugareNote.fxml"));
        if(slider1.getValue() == 0) {
            try {
                addStudentsWindow = loader1.load();
                addStudents = loader1.getController();
                addStudents.setService(service);
            } catch (IOException e) {
                lblMessage.setTextFill(Color.RED);
                lblMessage.setText(e.getMessage());
                return;
            }
            Scene addStudentsScene = new Scene(addStudentsWindow);
            Stage addStudentsStage = new Stage();
            addStudentsStage.setResizable(false);
            addStudentsStage.setTitle("Adaugare studenti");
            addStudentsScene.getStylesheets().add("Theme.css");
            addStudentsStage.setScene(addStudentsScene);
            addStudentsStage.show();
        }
        if(slider1.getValue() == 1) {
            try {
                addHomeworksWindow = loader2.load();
                addHomeworks = loader2.getController();
                addHomeworks.setService(service);
            } catch (IOException e) {
                lblMessage.setTextFill(Color.RED);
                lblMessage.setText(e.getMessage());
                return;
            }
            Scene addHomeworksScene = new Scene(addHomeworksWindow);
            Stage addHomeworksStage = new Stage();
            addHomeworksStage.setResizable(false);
            addHomeworksStage.setTitle("Adaugare teme");
            addHomeworksScene.getStylesheets().add("Theme.css");
            addHomeworksStage.setScene(addHomeworksScene);
            addHomeworksStage.show();
        }
        if(slider1.getValue() == 2) {
            try {
                addMarksWindow = loader3.load();
                addMarks = loader3.getController();
                addMarks.setService(service);
            } catch (IOException e) {
                lblMessage.setTextFill(Color.RED);
                lblMessage.setText(e.getMessage());
                return;
            }
            Scene addMarksScene = new Scene(addMarksWindow);
            Stage addMarksStage = new Stage();
            addMarksStage.setResizable(false);
            addMarksStage.setTitle("Adaugare note");
            addMarksScene.getStylesheets().add("Theme.css");
            addMarksStage.setScene(addMarksScene);
            addMarksStage.show();
        }
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
        marksTable.refresh();
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
        marksTable.refresh();
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
        marksTable.refresh();
    }

    @FXML
    private void updateTables() {
        updateStudentsTable();
        updateHomeworksTable();
        updateMarksTable();
    }

    @FXML
    private void updateEverything() {
        lblSaptCurenta.setText("Saptamana curenta: " + String.valueOf(Globals.getInstance().getSaptCurenta()));
        updateTables();
        updateNoOfPages();
    }

    @FXML
    private void deleteAction() {

        String id = txt1.getText();
        if(tabPane.getTabs().get(0).isSelected() && id.length() > 0) {
            try {
                ButtonType btnYes = new ButtonType("Da",ButtonBar.ButtonData.YES);
                ButtonType btnNo = new ButtonType("Nu",ButtonBar.ButtonData.NO);
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Toate notele studentului acestuia vor fi sterse(daca exista). Continuati?",btnYes,btnNo);
                alert.setHeaderText("");
                alert.setTitle("");

                Optional<ButtonType> result = alert.showAndWait();
                if(result.isPresent() && result.get() == btnYes) {
                    service.removeStudent(Integer.valueOf(id));
                    updateTables();
                    lblMessage.setText("Studentul a fost sters! ");
                    lblMessage.setTextFill(Color.CYAN);
                    txt1.setText("");
                    txt2.setText("");
                    txt3.setText("");
                    txt4.setText("");
                    txt5.setText("");
                }
            } catch (RepositoryException e) {
                lblMessage.setText(e.getMessage());
                lblMessage.setTextFill(Color.RED);
                txt1.setText("");
                txt2.setText("");
                txt3.setText("");
                txt4.setText("");
                txt5.setText("");
            }
        }
        else if(tabPane.getTabs().get(1).isSelected() && id.length() > 0) {
            try {
                ButtonType btnYes = new ButtonType("Da",ButtonBar.ButtonData.YES);
                ButtonType btnNo = new ButtonType("Nu",ButtonBar.ButtonData.NO);
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Toate notele asignate acestei teme vor fi sterse(daca exista). Continuati?",btnYes,btnNo);
                alert.setHeaderText("");
                alert.setTitle("");

                Optional<ButtonType> result = alert.showAndWait();
                if(result.isPresent() && result.get() == btnYes) {
                    service.removeHomework(Integer.valueOf(id));
                    updateTables();
                    lblMessage.setText("Tema a fost stearsa! ");
                    lblMessage.setTextFill(Color.CYAN);
                    txt1.setText("");
                    txt2.setText("");
                    txt3.setText("");
                    txt4.setText("");
                    txt5.setText("");
                }
            } catch (RepositoryException e) {
                lblMessage.setText(e.getMessage());
                lblMessage.setTextFill(Color.RED);
                txt1.setText("");
                txt2.setText("");
                txt3.setText("");
                txt4.setText("");
                txt5.setText("");
            }
        }
        else if(tabPane.getTabs().get(2).isSelected() && id.length() > 0) {
            try {
                service.removeMark(Integer.valueOf(id) * 69 + Integer.valueOf(txt2.getText()));
                updateTables();
                lblMessage.setText("Nota a fost stearsa! ");
                lblMessage.setTextFill(Color.CYAN);
                txt1.setText("");
                txt2.setText("");
                txt3.setText("");
                txt4.setText("");
                txt5.setText("");
            } catch (RepositoryException e) {
                lblMessage.setText(e.getMessage());
                lblMessage.setTextFill(Color.RED);
                txt1.setText("");
                txt2.setText("");
                txt3.setText("");
                txt4.setText("");
                txt5.setText("");
            }
        }
        else{
            lblMessage.setText("Nu ati selectat nimic! ");
            lblMessage.setTextFill(Color.RED);
        }
    }

    @FXML
    private void CRUDWindowUpdate() {
        Parent updateStudentsWindow;
        Parent updateHomeworksWindow;
        Parent updateMarksWindow;
        UpdateStudents updateStudents;
        UpdateHomeworks updateHomeworks;
        UpdateMarks updateMarks;
        FXMLLoader loader1 = new FXMLLoader(getClass().getResource("UpdateStudenti.fxml"));
        FXMLLoader loader2 = new FXMLLoader(getClass().getResource("UpdateTeme.fxml"));
        FXMLLoader loader3 = new FXMLLoader(getClass().getResource("UpdateNote.fxml"));
        if(slider1.getValue() == 0) {
            try {
                updateStudentsWindow = loader1.load();
                updateStudents = loader1.getController();
                if(txt1.getText().length() > 0 && tabPane.getTabs().get(0).isSelected()) {
                    updateStudents.txtID.setText(txt1.getText());
                    updateStudents.txtID.setDisable(true);
                    updateStudents.txtID.setEditable(false);
                }
                else {
                    updateStudents.txtID.setText("");
                    updateStudents.txtID.setDisable(false);
                    updateStudents.txtID.setEditable(true);
                }
                updateStudents.setService(service);
            } catch (IOException e) {
                lblMessage.setTextFill(Color.RED);
                lblMessage.setText(e.getMessage());
                return;
            }
            Scene updateStudentsScene = new Scene(updateStudentsWindow);
            Stage updateStudentsStage = new Stage();
            updateStudentsStage.setResizable(false);
            updateStudentsStage.setTitle("Actualizare studenti");
            updateStudentsScene.getStylesheets().add("Theme.css");
            updateStudentsStage.setScene(updateStudentsScene);
            updateStudentsStage.show();
        }
        if(slider1.getValue() == 1) {
            try {
                updateHomeworksWindow = loader2.load();
                updateHomeworks = loader2.getController();
                if(txt1.getText().length() > 0 && tabPane.getTabs().get(1).isSelected()) {
                    updateHomeworks.txtID.setText(txt1.getText());
                    updateHomeworks.txtID.setDisable(true);
                    updateHomeworks.txtID.setEditable(false);
                }
                else {
                    updateHomeworks.txtID.setText("");
                    updateHomeworks.txtID.setDisable(false);
                    updateHomeworks.txtID.setEditable(true);
                }
                updateHomeworks.setService(service);
            } catch (IOException e) {
                lblMessage.setTextFill(Color.RED);
                lblMessage.setText(e.getMessage());
                return;
            }
            Scene updateHomeworksScene = new Scene(updateHomeworksWindow);
            Stage updateHomeworksStage = new Stage();
            updateHomeworksStage.setResizable(false);
            updateHomeworksStage.setTitle("Actualizare teme");
            updateHomeworksScene.getStylesheets().add("Theme.css");
            updateHomeworksStage.setScene(updateHomeworksScene);
            updateHomeworksStage.show();
        }
        if(slider1.getValue() == 2) {
            try {
                updateMarksWindow = loader3.load();
                updateMarks = loader3.getController();
                if(txt1.getText().length() > 0 && tabPane.getTabs().get(2).isSelected()) {
                    updateMarks.txtID.setText(String.valueOf(Integer.valueOf(txt1.getText())*69+Integer.valueOf(txt2.getText())));
                    updateMarks.txtID.setDisable(true);
                    updateMarks.txtID.setEditable(false);
                }
                else {
                    updateMarks.txtID.setText("");
                    updateMarks.txtID.setDisable(false);
                    updateMarks.txtID.setEditable(true);
                }
                updateMarks.setService(service);
            } catch (IOException e) {
                lblMessage.setTextFill(Color.RED);
                lblMessage.setText(e.getMessage());
                return;
            }
            Scene updateMarksScene = new Scene(updateMarksWindow);
            Stage updateMarksStage = new Stage();
            updateMarksStage.setResizable(false);
            updateMarksStage.setTitle("Actualizare note");
            updateMarksScene.getStylesheets().add("Theme.css");
            updateMarksStage.setScene(updateMarksScene);
            updateMarksStage.show();
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

    private void updateNoOfPages() {
        if(tabPane.getTabs().get(0).isSelected()) {
            if(service.noOfStudents() > studentsServiceSize && studentsServiceSize % 12 == 0) {
                noOfPages++;
                txtPage.setText(currentPageStudents + "/" + noOfPages);
                studentsServiceSize = service.noOfStudents();
            }
            else if(service.noOfStudents() < studentsServiceSize && service.noOfStudents() % 12 == 0 && service.noOfStudents() !=0) {
                noOfPages--;
                currentPageStudents--;
                txtPage.setText(currentPageStudents + "/" + noOfPages);
                studentsServiceSize = service.noOfStudents();
                updateStudentsTable();
            }
        }
        else if(tabPane.getTabs().get(1).isSelected()) {
            if(service.noOfHomeworks() > homeworksServiceSize && homeworksServiceSize % 12 == 0) {
                noOfPages++;
                txtPage.setText(currentPageHomeworks + "/" + noOfPages);
                homeworksServiceSize = service.noOfHomeworks();
            }
            else if(service.noOfHomeworks() < homeworksServiceSize && service.noOfHomeworks() % 12 == 0 && service.noOfHomeworks() !=0) {
                noOfPages--;
                currentPageHomeworks--;
                txtPage.setText(currentPageHomeworks + "/" + noOfPages);
                homeworksServiceSize = service.noOfHomeworks();
                updateHomeworksTable();
            }
        }
        else if(tabPane.getTabs().get(2).isSelected()) {
            if(service.noOfMarks() > marksServiceSize && marksServiceSize % 12 == 0) {
                noOfPages++;
                txtPage.setText(currentPageMarks + "/" + noOfPages);
                marksServiceSize = service.noOfMarks();
            }
            else if(service.noOfMarks() < marksServiceSize && service.noOfMarks() % 12 == 0 && service.noOfMarks() !=0) {
                noOfPages--;
                currentPageMarks--;
                txtPage.setText(currentPageMarks + "/" + noOfPages);
                marksServiceSize = service.noOfMarks();
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

    @FXML
    private void emailWindow() {
        Parent emailWindow;
        EmailForm emailForm;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("EmailForm.fxml"));
        try {
            emailWindow = loader.load();
            emailForm = loader.getController();
            emailForm.setService(service);
        } catch (IOException e) {
            lblMessage.setTextFill(Color.RED);
            lblMessage.setText(e.getMessage());
            return;
        }
        Scene emailFormScene = new Scene(emailWindow);
        Stage emailFormStage = new Stage();
        emailFormStage.setResizable(false);
        emailFormStage.setTitle("Send an e-mail to a student");
        emailFormScene.getStylesheets().add("Theme.css");
        emailFormStage.setScene(emailFormScene);
        emailFormStage.show();
    }

    @Override
    public void notifyObs() {
        updateEverything();
    }
}
