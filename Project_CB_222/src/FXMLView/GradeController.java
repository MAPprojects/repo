package FXMLView;

import Domain.Nota;
import Domain.Student;
import Domain.Tema;
import Repository.RepositoryException;
import Repository.Validator;
import Repository.ValidatorException;
import Service.Service;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


public class GradeController implements Observer {

    private final Image IMAGE_HOMEWORK = new Image(getClass().getClassLoader().getResource("Resources/homeworkPictogram.png").toString(), 64, 64, false, false);

    private final Image IMAGE_STUDENT = new Image(getClass().getClassLoader().getResource("Resources/studentPictogram.png").toString(), 64, 64, false, false);

    private final Image IMAGE_STUDENT_NOTSELECTED = new Image(getClass().getClassLoader().getResource("Resources/studentPictogramNotSelected.png").toString(), 100, 100, false, true);

    private final Image IMAGE_STUDENT_SELECTED = new Image(getClass().getClassLoader().getResource("Resources/studentPictogramSelected.png").toString(), 100, 100, false, true);

    private final Image IMAGE_STUDENT_HOVERED = new Image(getClass().getClassLoader().getResource("Resources/studentPictogramHover.png").toString(), 64, 64, false, false);

    private final Image IMAGE_GRADE = new Image(getClass().getClassLoader().getResource("Resources/gradePictogram.png").toString(), 64, 64, false, false);

    private final Image IMAGE_GRADE_HOVERED = new Image(getClass().getClassLoader().getResource("Resources/gradePictogramHover.png").toString(), 64, 64, false, false);

    private final Image IMAGE_HOMEWORK_NOTSELECTED = new Image(getClass().getClassLoader().getResource("Resources/homeworkPictogramNotSelected.png").toString(), 100, 100, false, false);

    private final Image IMAGE_HOMEWORK_SELECTED = new Image(getClass().getClassLoader().getResource("Resources/homeworkPictogramSelected.png").toString(), 100, 100, false, false);

    private final Image IMAGE_GRADE_NOTSELECTED = new Image(getClass().getClassLoader().getResource("Resources/gradePictogramNotSelected.png").toString(), 100, 100, false, false);

    private final Image IMAGE_GRADE_SELECTED = new Image(getClass().getClassLoader().getResource("Resources/gradePictogramSelected.png").toString(), 100, 100, false, false);

    private final Image IMAGE_HOMEWORK_HOVERED = new Image(getClass().getClassLoader().getResource("Resources/homeworkPictogramHover.png").toString(), 64, 64, false, false);

    private final Image IMAGE_CANCEL_BUTTON = new Image(getClass().getClassLoader().getResource("Resources/cancelStudent.png").toString(), 20, 28, false, false);

    private final Image IMAGE_CANCEL_BUTTON_HOVER = new Image(getClass().getClassLoader().getResource("Resources/cancelStudentHover.png").toString(), 20, 28, false, false);

    private final Image IMAGE_TRASH = new Image(getClass().getClassLoader().getResource("Resources/trashPictogram.png").toString(), 64, 64, false, false);

    private final Image IMAGE_TRASH_HOVERED = new Image(getClass().getClassLoader().getResource("Resources/trashPictogramHover.png").toString(), 64, 64, false, false);

    private HomeworkController homeworkController;

    private StudentController studentController;

    private int startStudent;

    private int startHomework;

    private int stepStudent;

    private int stepHomework;

    private int startGrade;

    private int stepGrade;

    private Service service;

    private AnchorPane studentStage;

    private AnchorPane homeworkStage;

    private Stage primaryStage;

    private Scene previousScene;

    private Tema homeworkSearched;

    private Nota gradeSearched;

    private ObservableList<Tema> bigDataHomework;

    private ObservableList<Tema> lazyDataHomework = FXCollections.observableArrayList();

    private ObservableList<Student> bigDataStudent;

    private ObservableList<Student> lazyDataStudent = FXCollections.observableArrayList();

    private ObservableList<Nota> bigDataGrade;

    private ObservableList<Nota> lazyDataGrade = FXCollections.observableArrayList();

    private Student studentSearched;

    private Charts charts;

    private PDF pdf;

    @FXML
    private Button homeworksaverageGrades;

    @FXML
    private Button promotionPercentage;

    @FXML
    private Button studentsAverageGrades;

    @FXML
    private Button exportCharts;

    @FXML
    private PieChart pieChart;

    @FXML
    private NumberAxis yAxisHomeworks;

    @FXML
    private CategoryAxis xAxisHomeworks;

    @FXML
    private BarChart<String, Number> barChartHomeworks;

    @FXML
    private NumberAxis yAxis;

    @FXML
    private CategoryAxis xAxis;

    @FXML
    private BarChart<String, Number> barChart;

    @FXML
    private Label selectedGradeLabel;

    @FXML
    private Button cancelSelectedGrade;

    @FXML
    private AnchorPane inputGradeAnchor;

    @FXML
    private AnchorPane studentHomeworkLabelPane;

    @FXML
    private AnchorPane filterGrades;

    @FXML
    private TextField titleGradeFilter;

    @FXML
    private TextField deadlineGradeFilter;

    @FXML
    private Label studentSelected;

    @FXML
    private Label homeworkSelected;

    @FXML
    private Button cancelStudent;

    @FXML
    private Button cancelHomework;

    @FXML
    private AnchorPane filterPane;

    @FXML
    private ComboBox groupFilter;

    @FXML
    private TextField nameFilter;

    @FXML
    private TextField teacherFilter;

    @FXML
    private TextField titleFilter;

    @FXML
    private TextField deadlineFilter;

    @FXML
    private ToggleButton upBarUpdate;

    @FXML
    private ToggleButton upBarDelete;

    @FXML
    private ToggleButton upBarAdd;

    @FXML
    private Button panelStudent;

    @FXML
    private Button panelHomework;

    @FXML
    private Button panelGrade;

    @FXML
    private Button update;

    @FXML
    private Button add;

    @FXML
    private ListView<Tema> listViewOfHomeworks;

    @FXML
    private ListView<Student> listViewOfStudents;

    @FXML
    private ListView<Nota> listViewOfGrades;

    @FXML
    private TextField markField;

    @FXML
    private TextArea observationTextArea;


    public void setControllers(StudentController studentController, HomeworkController homeworkController){
        this.homeworkController = homeworkController;
        this.studentController = studentController;
    }

    public void setService(Service service) throws IOException {
        this.service = service;
        charts = new Charts(service);
        pdf = new PDF();
        artificialInitialize();
    }

    public void setStage(Stage primaryStage, AnchorPane studentStage, AnchorPane homeworkStage){
        this.primaryStage = primaryStage;
        this.studentStage = studentStage;
        this.homeworkStage = homeworkStage;
    }

    private boolean checkExistance(int value, List<Integer> values){

        for(Integer thisValue: values){
            if(thisValue == value)
                return true;
        }

        return false;

    }

    private void checkIfSelectedForAddGrade(){
        String error = "";
        int check = 0;
        if(cancelHomework.isVisible()==false){
            error += "You need to select a Homework!" + "\n";
        }

        if(cancelStudent.isVisible()==false){
            error += "You need to select a Student!" + "\n";
        }

        if (check == 1) {
            throw new ValidatorException(error);

        }
    }

    private void checkFieldsToBeCompleted() {

        String error = "";
        int check = 0;

        if (markField.getText() == "" || markField == null || markField.getText().length() == 0) {
            error += "Mark Field CANNOT be empty!" + "\n";
            check = 1;
        }

        if (observationTextArea.getText() == "" || observationTextArea == null || observationTextArea.getText().length() == 0) {
            error += "Observations Field CANNOT be empty!" + "\n";
            check = 1;
        }

        if (check == 1) {
            throw new ValidatorException(error);
        }
    }

    private void initializeDataGrade(Iterable<Nota> listOfGrades){
        bigDataGrade = FXCollections.observableArrayList(StreamSupport.stream(listOfGrades.spliterator(), false).collect(Collectors.toList()));
        initializeLazyDataGrade();
        lazyLoadingGrade();
        initializeListOfGrades();
    }

    private void lazyLoadingGrade() {

        ScrollBar listViewScrollBar = getListViewScrollBarGrade();
        listViewScrollBar.valueProperty().addListener((observable, oldValue, newValue) -> {
            double position = newValue.doubleValue();
            ScrollBar scrollBar = getListViewScrollBarGrade();
            if (position == scrollBar.getMax()) {
                if (startGrade <= bigDataGrade.size()){
                    initializeLazyDataGrade();
                }
            }
        });
    }

    private ScrollBar getListViewScrollBarGrade() {

        ScrollBar scrollbar = null;
        for (Node node : listViewOfGrades.lookupAll(".scroll-bar")) {
            if (node instanceof ScrollBar) {
                ScrollBar bar = (ScrollBar) node;
                if (bar.getOrientation().equals(Orientation.VERTICAL)) {
                    scrollbar = bar;
                }
            }
        }
        return scrollbar;
    }

    private void initializeLazyDataGrade() {

        if(startGrade<stepGrade){
            if(stepGrade>=bigDataGrade.size()){
                stepGrade = bigDataGrade.size();
            }

            lazyDataGrade.addAll(bigDataGrade.subList(startGrade, stepGrade));
            startGrade = stepGrade;
            stepGrade+=10;
        }
    }

    private void lazyLoadingStudent(){
        ScrollBar listViewScrollBar = getListViewScrollBarStundent();
        listViewScrollBar.valueProperty().addListener((observable, oldValue, newValue) -> {
            double position = newValue.doubleValue();
            ScrollBar scrollBar = getListViewScrollBarStundent();
            if (position == scrollBar.getMax()) {
                if (startStudent <= bigDataStudent.size()){
                    initializeLazyDataStudent();
                }
            }
        });
    }

    private void initializeLazyDataStudent() {

        if(startStudent<stepStudent){
            if(stepStudent>=bigDataStudent.size()){
                stepStudent = bigDataStudent.size();
            }

            lazyDataStudent.addAll(bigDataStudent.subList(startStudent, stepStudent));
            startStudent = stepStudent;
            stepStudent+=10;
        }
    }

    private void lazyLoadingHomework(){
        ScrollBar listViewScrollBar = getListViewScrollBarHomework();
        listViewScrollBar.valueProperty().addListener((observable, oldValue, newValue) -> {
            double position = newValue.doubleValue();
            ScrollBar scrollBar = getListViewScrollBarHomework();
            if (position == scrollBar.getMax()) {
                if (startStudent <= bigDataHomework.size()){
                    initializeLazyDataHomework();
                }
            }
        });
    }

    private void reinitializeLazyVariablesStudent(){
        startStudent = 0;
        stepStudent = 10;
        lazyDataStudent.clear();
    }

    private void reinitializeLazyVariablesHomework(){
        startHomework = 0;
        stepHomework = 10;
        lazyDataHomework.clear();

    }

    private void reinitializeLazyVariablesGrade(){
        startGrade = 0;
        stepGrade = 10;
        lazyDataGrade.clear();

    }

    private void initializeLazyDataHomework() {

        if(startHomework<stepHomework){
            if(stepHomework>=bigDataHomework.size()){
                stepHomework = bigDataHomework.size();
            }

            lazyDataHomework.addAll(bigDataHomework.subList(startHomework, stepHomework));
            startHomework = stepHomework;
            stepHomework+=10;
        }
    }

    private ScrollBar getListViewScrollBarHomework() {

        ScrollBar scrollbar = null;
        for (Node node : listViewOfHomeworks.lookupAll(".scroll-bar")) {
            if (node instanceof ScrollBar) {
                ScrollBar bar = (ScrollBar) node;
                if (bar.getOrientation().equals(Orientation.VERTICAL)) {
                    scrollbar = bar;
                }
            }
        }
        return scrollbar;
    }

    private ScrollBar getListViewScrollBarStundent() {

        ScrollBar scrollbar = null;
        for (Node node : listViewOfStudents.lookupAll(".scroll-bar")) {
            if (node instanceof ScrollBar) {
                ScrollBar bar = (ScrollBar) node;
                if (bar.getOrientation().equals(Orientation.VERTICAL)) {
                    scrollbar = bar;
                }
            }
        }
        return scrollbar;
    }

    private void lazyLoading(){
        lazyLoadingHomework();
        lazyLoadingStudent();
        lazyLoadingGrade();
    }

    private void initializeDataStudent(Iterable<Student> listOfStudents){

        bigDataStudent = FXCollections.observableArrayList(StreamSupport.stream(listOfStudents.spliterator(), false).collect(Collectors.toList()));
        initializeLazyDataStudent();
        lazyLoading();
        initilizeListOfStudents();

    }

    private void initializeDataHomework(Iterable<Tema> listOfHomeworks) {
        bigDataHomework = FXCollections.observableArrayList(StreamSupport.stream(listOfHomeworks.spliterator(), false).collect(Collectors.toList()));
        initializeLazyDataHomework();
        lazyLoading();
        initilizeListOfHomeworks();
    }

    private List<Integer> formGroupFitlerValues(){

        List<Integer> values = new ArrayList<>();
        for(Student student: service.getAllStudenti()){
            if(!checkExistance(student.getGrupa(), values)){
                values.add(student.getGrupa());
            }
        }
        return values;

    }

    private void filterInitialize(){

        groupFilter.getItems().clear();
        groupFilter.getItems().add("No Group");

        groupFilter.getItems().addAll(formGroupFitlerValues());
        groupFilter.getSelectionModel().selectFirst();

    }

    public void setPreviousScene(Scene previousScene){
        this.previousScene = previousScene;
    }

    private void hideAllButtons(){
        update.setVisible(false);
        add.setVisible(false);
    }

    private void setUpBarsSelectedToFalse(){
        upBarAdd.setSelected(false);
        upBarUpdate.setSelected(false);
        upBarDelete.setSelected(false);

    }

    private void setSelectedSideBarAndUpBarInTheBeginning(){
        panelGrade.getStyleClass().add("clickedEffect");
        setUpBarsSelectedToFalse();
        upBarAdd.setSelected(true);

    }

    private void initilizeListOfHomeworks(){

        listViewOfHomeworks.setItems(lazyDataHomework);
        listViewOfHomeworks.setCellFactory(param -> new HomeworkCell());
    }

    private void setImageLabelStudentNotSelected(){

        Circle circle = new Circle(50);
        circle.setFill(new ImagePattern(IMAGE_STUDENT_NOTSELECTED));
        studentSelected.setGraphic(circle);
    }

    private void hideSelectedGradePart(){
        selectedGradeLabel.setVisible(false);
        cancelSelectedGrade.setVisible(false);
    }

    private void setImageLabelStudentSelected(Student studentSearched){
        this.studentSearched = studentSearched;

        Circle circle = new Circle(50);
        circle.setFill(new ImagePattern(IMAGE_STUDENT_SELECTED));
        studentSelected.setGraphic(circle);

        cancelStudent.setVisible(true);
    }

    private void setImageLabelHomeworkSelected(Tema homeworkSearched){

        this.homeworkSearched = homeworkSearched;

        Circle circle = new Circle(50);
        circle.setFill(new ImagePattern(IMAGE_HOMEWORK_SELECTED));
        homeworkSelected.setGraphic(circle);
        cancelHomework.setVisible(true);

    }

    public void handleDataChanged(){
        handleDataChangedStudents();
        handleDataChangedHomeworks();
        handleDataChangedGrades();
    }

    public void handleDataChangedGrades() {

        reinitializeLazyVariablesGrade();
        initializeDataGrade(service.getAllGrades());
    }

    public void handleDataChangedHomeworks() {

        reinitializeLazyVariablesHomework();
        initializeDataHomework(service.getAllTeme());
    }

    public void handleDataChangedStudents() {
        reinitializeLazyVariablesStudent();
        initializeDataStudent(service.getAllStudenti());
    }

    private void setImageLabelHomeworkNotSelected(){

        Circle circle = new Circle(50);
        circle.setFill(new ImagePattern(IMAGE_HOMEWORK_NOTSELECTED));
        homeworkSelected.setGraphic(circle);

    }

    private void setGraphicToCancelGradeButton(){

        cancelSelectedGrade.graphicProperty().bind(Bindings.when(cancelSelectedGrade.hoverProperty())
                .then(new ImageView(IMAGE_CANCEL_BUTTON_HOVER))
                .otherwise(new ImageView(IMAGE_CANCEL_BUTTON)));

        cancelSelectedGrade.setShape(new Circle());
    }

    private void setImageLabelGradeSelected(int idGrade){
        Circle circle = new Circle(50);
        circle.setFill(new ImagePattern(IMAGE_GRADE_SELECTED));
        selectedGradeLabel.setGraphic(circle);
        setGraphicToCancelGradeButton();
        cancelSelectedGrade.setVisible(true);
        update.setVisible(true);

        gradeSearched = service.getOneNota(idGrade);
        completeFieldsGrade(gradeSearched);

    }

    @FXML
    private void handleCancelGradeSelected(){

        setImageLabelGradeNotSelected();
        update.setVisible(false);
        cancelSelectedGrade.setVisible(false);
        clearFields();
    }

    private class HomeworkCell extends ListCell<Tema> {


        @Override
        protected void updateItem(Tema homework, boolean empty) {
            super.updateItem(homework, empty);

            Button homeworkButton = new Button();

            homeworkButton.setOnMouseClicked(e -> setImageLabelHomeworkSelected((Tema) homeworkButton.getUserData()));

            homeworkButton.graphicProperty().bind(Bindings.when(homeworkButton.hoverProperty())
                    .then(new ImageView(IMAGE_HOMEWORK_HOVERED))
                    .otherwise(new ImageView(IMAGE_HOMEWORK)));

            homeworkButton.setShape(new Circle(1.5));
            homeworkButton.setStyle("-fx-background-insets: 0; -fx-padding: 0;");

            if (empty || homework == null) {

                setGraphic(null);
                setText(null);
            } else {
                HBox hbox = new HBox();

                hbox.getChildren().addAll(homeworkButton);

                homeworkButton.setUserData(homework);

                setGraphic(hbox);
                setText(homework.getView());
            }
        }
    }

    private class GradeCell extends ListCell<Nota> {

        @Override
        protected void updateItem(Nota grade, boolean empty) {
            super.updateItem(grade, empty);

            Button deleteButton = new Button();
            Button gradeButton = new Button();

            deleteButton.setOnMouseClicked(e -> handleDeleteGrade((Integer) deleteButton.getUserData()));
            gradeButton.setOnMouseClicked(e -> setImageLabelGradeSelected((Integer) gradeButton.getUserData()));

            gradeButton.graphicProperty().bind(Bindings.when(gradeButton.hoverProperty())
                    .then(new ImageView(IMAGE_GRADE_HOVERED))
                .otherwise(new ImageView(IMAGE_GRADE)));

            deleteButton.graphicProperty().bind(
                    Bindings.when(deleteButton.hoverProperty())
                            .then(new ImageView(IMAGE_TRASH_HOVERED))
                            .otherwise(new ImageView(IMAGE_TRASH)));

            deleteButton.setShape(new Circle(1.5));
            gradeButton.setShape(new Circle(1.5));
            deleteButton.setStyle("-fx-background-insets: 0; -fx-padding: 0;");
            gradeButton.setStyle("-fx-background-insets: 0; -fx-padding: 0;");

            if (empty || grade == null) {

                setGraphic(null);
                setText(null);
            } else {

                HBox hbox = new HBox();

                gradeButton.setUserData(grade.getId());
                deleteButton.setUserData(grade.getId());

                hbox.getChildren().addAll(deleteButton, gradeButton);

                setText(grade.getView());
                setGraphic(hbox);
            }
        }
    }

    private class StudentCell extends ListCell<Student> {

        @Override
        protected void updateItem(Student student, boolean empty) {
            super.updateItem(student, empty);

            Button studentButton = new Button();

            studentButton.setOnMouseClicked(e -> setImageLabelStudentSelected((Student) studentButton.getUserData()));

            studentButton.graphicProperty().bind(Bindings.when(studentButton.hoverProperty())
                    .then(new ImageView(IMAGE_STUDENT_HOVERED))
                    .otherwise(new ImageView(IMAGE_STUDENT)));

            studentButton.setShape(new Circle(1.5));
            studentButton.setStyle("-fx-background-insets: 0; -fx-padding: 0;");

            if (empty || student == null) {

                setGraphic(null);
                setText(null);
            } else {
                HBox hbox = new HBox();
                hbox.getChildren().addAll(studentButton);
                studentButton.setUserData(student);

                setGraphic(hbox);
                setText(student.getView());

            }
        }
    }

    private void initilizeListOfStudents(){

        listViewOfStudents.setItems(lazyDataStudent);

        listViewOfStudents.setCellFactory(param -> new StudentCell());

    }

    private void initializeListOfGrades(){

        listViewOfGrades.setItems(lazyDataGrade);

        listViewOfGrades.setCellFactory(param -> new GradeCell());

    }

    @FXML
    private void handleFilterHomworks(){

        String title = titleFilter.getText();
        String deadline = deadlineFilter.getText();

        reinitializeLazyVariablesHomework();
        initializeDataHomework(service.filterHomeworks(title, deadline));

    }

    @FXML
    private void handleFilterGrades(){

        String title = titleGradeFilter.getText();
        String deadline = deadlineGradeFilter.getText();

        reinitializeLazyVariablesGrade();
        initializeDataGrade(service.filterGrades(title, deadline));

    }

    @FXML
    private void handleFilterStudents(){

        if(!groupFilter.getSelectionModel().isEmpty()){
            String group = groupFilter.getSelectionModel().getSelectedItem().toString();
            String name = nameFilter.getText();
            String teacher = teacherFilter.getText();

            reinitializeLazyVariablesStudent();
            initializeDataStudent(service.filterStudents(group, name, teacher));
        }
    }

    private void selectedLabelInitialize(){
        setImageLabelStudentNotSelected();
        setImageLabelHomeworkNotSelected();
    }

    private void hideCancelButtons(){
        cancelHomework.setVisible(false);
        cancelStudent.setVisible(false);
    }

    private void setGraphicToCancelButtons(){

        cancelStudent.graphicProperty().bind(Bindings.when(cancelStudent.hoverProperty())
                .then(new ImageView(IMAGE_CANCEL_BUTTON_HOVER))
                .otherwise(new ImageView(IMAGE_CANCEL_BUTTON)));

        cancelHomework.graphicProperty().bind(Bindings.when(cancelHomework.hoverProperty())
                .then(new ImageView(IMAGE_CANCEL_BUTTON_HOVER))
                .otherwise(new ImageView(IMAGE_CANCEL_BUTTON)));

        cancelStudent.setShape(new Circle());
        cancelHomework.setShape(new Circle());
        hideCancelButtons();
    }

    private void artificialInitialize(){
        initilizeListOfHomeworks();
        initilizeListOfStudents();
        hideAllButtons();
        hideCharts();
        setSelectedSideBarAndUpBarInTheBeginning();
        hideReports();
        filterInitialize();
        hideSelectedGradePart();
        selectedLabelInitialize();
        setGraphicToCancelButtons();

        add.setVisible(true);
        listViewOfGrades.setVisible(false);
        filterGrades.setVisible(false);
    }

    private void hideCharts(){
        barChart.setVisible(false);
        pieChart.setVisible(false);
        barChartHomeworks.setVisible(false);
    }

    private void hideButtonsCharts(){
        homeworksaverageGrades.setVisible(false);
        promotionPercentage.setVisible(false);
        studentsAverageGrades.setVisible(false);
        exportCharts.setVisible(false);
    }

    private void hideReports(){
        hideButtonsCharts();
        hideCharts();

    }


    @Override
    public void update(Observable o, Object arg) {

    }

    @FXML
    private void handleStudentPanelClickedButton(){

        AnchorPane ilTrimitLaPlimbare = new AnchorPane();
        Scene scene = new Scene(studentStage, 900, 600);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        studentController.setService(service);
        this.previousScene.setRoot(ilTrimitLaPlimbare); // here happens the hack
        studentController.setPreviousScene(scene);
        studentController.handleDataChanged();

    }


    private void completeFieldsGrade(Nota nota){
        observationTextArea.setText(nota.getObservatii());
        markField.setText(""+nota.getValoareNota());
    }

    @FXML
    private void ItemSelectedFromListStudents(){
        studentSearched = listViewOfStudents.getSelectionModel().getSelectedItem();
    }

    @FXML
    private void handleCancelStudentButton(){

        setImageLabelStudentNotSelected();
        cancelStudent.setVisible(false);

    }

    @FXML
    private void handleCancelHomeworkButton(){

        setImageLabelHomeworkNotSelected();
        cancelHomework.setVisible(false);

    }

    @FXML
    private void ItemSelectedFromListGrades(){
        gradeSearched = listViewOfGrades.getSelectionModel().getSelectedItem();
        completeFieldsGrade(gradeSearched);
        setImageLabelGradeSelected(gradeSearched.getId());

    }

    @FXML
    private void ItemSelectedFromListHomeworks(){
        homeworkSearched = listViewOfHomeworks.getSelectionModel().getSelectedItem();
    }

    @FXML
    private void handleHomeWorkPanelClickedButton(){

        AnchorPane ilTrimitLaPlimbare = new AnchorPane();
        Scene scene = new Scene(homeworkStage, 900, 600);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        homeworkController.setService(service);
        this.previousScene.setRoot(ilTrimitLaPlimbare); // here happens the hack
        homeworkController.setPreviousScene(scene);
        homeworkController.handleDataChanged();

    }

    private void manageListWhenChangingFromAddGradeToOher(){
        listViewOfStudents.setVisible(false);
        listViewOfHomeworks.setVisible(false);
        listViewOfGrades.setVisible(true);
    }

    private void setImageLabelGradeNotSelected() {
        Circle circle = new Circle(50);
        circle.setFill(new ImagePattern(IMAGE_GRADE_NOTSELECTED));
        selectedGradeLabel.setGraphic(circle);
        selectedGradeLabel.setVisible(true);
    }

    private void hidePreviousElements(){
        setUpBarsSelectedToFalse();
        upBarUpdate.setSelected(true);
        hideAllButtons();
        listViewOfStudents.setVisible(false);
        listViewOfHomeworks.setVisible(false);
        listViewOfGrades.setVisible(false);
        update.setVisible(false);
        filterGrades.setVisible(false);
        filterPane.setVisible(false);
        studentHomeworkLabelPane.setVisible(false);
        inputGradeAnchor.setVisible(false);
    }

    private void prepareReports(){
        hidePreviousElements();
        hideReports();
    }

    private void showChartsButtons(){
        homeworksaverageGrades.setVisible(true);
        promotionPercentage.setVisible(true);
        studentsAverageGrades.setVisible(true);
    }

    @FXML
    private void handleHomeworksAverageGrades(){
        hideCharts();
        charts.initializeBarChartHomeworks(barChartHomeworks, xAxisHomeworks, yAxisHomeworks);
        charts.setDataToBarChartHomeworks(barChartHomeworks);
        barChartHomeworks.setVisible(true);
        exportCharts.setVisible(true);
    }

    @FXML
    private void handlePromotionPercentage(){
        hideCharts();
        charts.initializePieChart(pieChart, primaryStage);
        pieChart.setVisible(true);
        exportCharts.setVisible(true);
    }

    @FXML
    private void handleStudentsAverageGrades(){
        hideCharts();
        charts.initializeBarChart(barChart, xAxis, yAxis);
        charts.setDataToBarChart(barChart);
        barChart.setVisible(true);
        exportCharts.setVisible(true);
    }

    @FXML
    private void handleExportToPDF() throws IOException {

        try {
            if(barChart.isVisible()){
                WritableImage snapshot = barChart.snapshot(new SnapshotParameters(), null);
                pdf.createPDFWithImage(snapshot);
            }
            else if(barChartHomeworks.isVisible()){
                WritableImage snapshot = barChartHomeworks.snapshot(new SnapshotParameters(), null);
                pdf.createPDFWithImage(snapshot);
            }
            else{
                WritableImage snapshot = pieChart.snapshot(new SnapshotParameters(), null);
                pdf.createPDFWithImage(snapshot);
            }
            service.infoBox("PDF CREATED SUCCESSFULLY", "", "", Alert.AlertType.INFORMATION);

        }
        catch (IOException e){
            service.infoBox("ERROR WHEN CREATING PDF", "", "", Alert.AlertType.INFORMATION);

        }

    }

    @FXML
    private void reportsUpBarClicked(){

        hideSelectedGradePart();
        prepareReports();
        showChartsButtons();

    }

    @FXML
    private void deleteUpdateUpBarClicked(){

        setUpBarsSelectedToFalse();
        setImageLabelGradeNotSelected();
        upBarDelete.setSelected(true);
        hideAllButtons();
        hideReports();
        filterPane.setVisible(false);
        studentHomeworkLabelPane.setVisible(false);
        filterGrades.setVisible(true);
        inputGradeAnchor.setVisible(true);

        manageListWhenChangingFromAddGradeToOher();
        initializeListOfGrades();
    }

    @FXML
    private void addUpBarClicked(){

        setUpBarsSelectedToFalse();
        upBarAdd.setSelected(true);
        hideAllButtons();
        hideReports();
        add.setVisible(true);
        filterPane.setVisible(true);
        studentHomeworkLabelPane.setVisible(true);
        filterGrades.setVisible(false);
        hideSelectedGradePart();

        listViewOfStudents.setVisible(true);
        listViewOfHomeworks.setVisible(true);
        listViewOfGrades.setVisible(false);
        initilizeListOfStudents();
        initilizeListOfHomeworks();
        clearFields();
    };

    //!!!Ai grija la saptamana predarii

    @FXML
    private void handleUpdateGrade(){
        try{
            service.updateNota(gradeSearched.getId(), Integer.parseInt(markField.getText()), gradeSearched.getIdStudent(), gradeSearched.getIdTema(), gradeSearched.getDeadline(), gradeSearched.getTitlu(), 0, observationTextArea.getText());
            clearFields();
            handleDataChangedGrades();
            handleCancelGradeSelected();
            service.infoBox("GRADE UPDATED SUCCESSFULLY", "", "", Alert.AlertType.INFORMATION);
        }
        catch (ValidatorException | RepositoryException e){
            service.infoBox(e.getMessage(), "", "", Alert.AlertType.INFORMATION);
        }

    }

    @FXML
    private void handleDeleteGrade(int id){

        try{

            service.removeNota(id);
            clearFields();
            handleDataChangedGrades();
            service.infoBox("GRADE DELETED SUCCESSFULLY", "", "", Alert.AlertType.INFORMATION);
        }
        catch (ValidatorException | RepositoryException e){
            service.infoBox(e.getMessage(), "", "", Alert.AlertType.INFORMATION);
        }



    }

    private  void clearFields(){

        markField.clear();
        observationTextArea.clear();
    }

    @FXML
    private void handleAddGrade(){

        try{
            checkFieldsToBeCompleted();
            checkIfSelectedForAddGrade();

            int mark = Integer.parseInt(markField.getText());
            String observations = observationTextArea.getText();
            int idStudent = studentSearched.getId();
            int idHomework = homeworkSearched.getId();

            service.addNota(mark, idStudent, idHomework, observations);

            setImageLabelHomeworkNotSelected();
            setImageLabelStudentNotSelected();
            hideCancelButtons();
            clearFields();

            handleDataChangedGrades();

            service.infoBox("GRADE CREATED SUCCESSFULLY", "", "", Alert.AlertType.INFORMATION);
        }
        catch (ValidatorException | RepositoryException e){
            service.infoBox(e.getMessage(), "","", Alert.AlertType.WARNING);
        }
        catch (NumberFormatException | NullPointerException e){
            service.infoBox("Please SELECT a STUDENT and a GRADE", "","", Alert.AlertType.WARNING);
        }


    }

}
