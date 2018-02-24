package FXMLView;

import Domain.Student;
import Mail.GoogleMail;
import Repository.RepositoryException;
import Repository.ValidatorException;
import Service.Service;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;


import javax.mail.MessagingException;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


public class StudentController implements Observer {

    private final Image IMAGE_STUDENT = new Image(getClass().getClassLoader().getResource("Resources/studentPictogram.png").toString(), 64, 64, false, true);

    private final Image IMAGE_STUDENT_NOTSELECTED = new Image(getClass().getClassLoader().getResource("Resources/studentPictogramNotSelected.png").toString(), 100, 100, false, true);

    private final Image IMAGE_CANCEL_BUTTON = new Image(getClass().getClassLoader().getResource("Resources/cancelStudent.png").toString(), 20, 28, false, false);

    private final Image IMAGE_CANCEL_BUTTON_HOVER = new Image(getClass().getClassLoader().getResource("Resources/cancelStudentHover.png").toString(), 20, 28, false, false);

    private final Image IMAGE_STUDENT_SELECTED = new Image(getClass().getClassLoader().getResource("Resources/studentPictogramSelected.png").toString(), 100, 100, false, true);

    private final Image IMAGE_TRASH = new Image(getClass().getClassLoader().getResource("Resources/trashPictogram.png").toString(), 64, 64, false, false);

    private final Image IMAGE_STUDENT_HOVERED = new Image(getClass().getClassLoader().getResource("Resources/studentPictogramHover.png").toString(), 64, 64, false, false);

    private final Image IMAGE_TRASH_HOVERD = new Image(getClass().getClassLoader().getResource("Resources/trashPictogramHover.png").toString(), 64, 64, false, false);

    private Service service;

    private AnchorPane homeworkStage;

    private AnchorPane gradeStage;

    private Scene previousScene;

    private HomeworkController homeworkController;

    private GradeController gradeController;

    private ObservableList<Student> bigDataStudent;

    private ObservableList<Student> lazyDataStudent = FXCollections.observableArrayList();

    private Student studentSearched;

    private Stage primaryStage;

    private int start;
    private int step;

    @FXML
    private TextField nameFilter;

    @FXML
    private TextField teacherFilter;

    @FXML
    private ComboBox groupFilter;

    @FXML
    private Button emailStudentButton;

    @FXML
    private Button panelStudent;


    @FXML
    private Button panelHomework;

    @FXML
    private Button cancelSelectedStudent;

    @FXML
    private Button panelGrade;

    @FXML
    private Label selectedStudentLabel;

    @FXML
    private Button add;

    @FXML
    private Button update;

    @FXML
    private TextField nameField;

    @FXML
    private TextField teacherField;

    @FXML
    private TextField groupField;

    @FXML
    private TextField emailField;

    @FXML
    private ListView<Student> listViewOfStudents;


    private void completeFields(Student student){

        nameField.setText(student.getNume());
        groupField.setText(""+student.getGrupa());
        emailField.setText(student.getEmail());
        teacherField.setText(student.getCadruDidactic());

    }

    @FXML
    private void ItemSelectedFromList(){
        studentSearched = listViewOfStudents.getSelectionModel().getSelectedItem();
        setImageToLabelSelected(studentSearched.getId());

        completeFields(service.getOneStudent(studentSearched.getId()));

    }

    private void setSelectedSideBarInTheBeginning(){
        panelStudent.getStyleClass().add("clickedEffect");

    }

    private void checkFieldsToBeCompleted() {

        String error = "";
        int check=0;

        if (nameField.getText() == "" || nameField==null || nameField.getText().length()==0){
            error += "Name Field CANNOT be empty!" + "\n";
            check=1;
        }

        if (teacherField.getText() == "" || teacherField==null || teacherField.getText().length()==0){
            error += "Teacher Field CANNOT be empty!" + "\n";
            check=1;
        }

        if (groupField.getText() == "" || groupField==null || groupField.getText().length()==0){
            error += "Group cannot be empty!" + "\n";
            check=1;
        }

        if (emailField.getText() == "" || emailField==null || emailField.getText().length()==0){
            error += "Email CANNOT be empty!" + "\n";
            check=1;
        }

        if(check==1){
            throw new ValidatorException(error);
        }


    }

    private void lazyLoading(){
        ScrollBar listViewScrollBar = getListViewScrollBar();
        listViewScrollBar.valueProperty().addListener((observable, oldValue, newValue) -> {
            double position = newValue.doubleValue();
            ScrollBar scrollBar = getListViewScrollBar();
            if (position == scrollBar.getMax()) {
                if (start <= bigDataStudent.size()){
                    initializeLazyData();
                }
            }
        });
    }

    private ScrollBar getListViewScrollBar() {
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

    public void handleDataChanged(){

        reinitializeLazyVariables();
        initializeData(service.getAllStudenti());
    }

    private void handleDeleteStudent(int idStudent){

        try{
            service.removeStudent(idStudent);
            handleDataChanged();
            service.infoBox("STUDENT DELETED SUCCESSFULLY", "", "", Alert.AlertType.INFORMATION);
        }catch (ValidatorException | RepositoryException e){
            service.infoBox(e.getMessage(), "","", Alert.AlertType.WARNING);
        }


    }

    @FXML
    private void handleGradePanelClickedButton() throws IOException {

        AnchorPane ilTrimitLaPlimbare = new AnchorPane();
        Scene scene = new Scene(gradeStage, 900, 600);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        gradeController.setService(service);
        this.previousScene.setRoot(ilTrimitLaPlimbare); // here happens the hack
        gradeController.setPreviousScene(scene);
        gradeController.handleDataChanged();

    }

    @FXML
    private void handleUpdateStudent(){

        try{
            service.updateStudent(studentSearched.getId(), nameField.getText(), Integer.parseInt(groupField.getText()), emailField.getText(), teacherField.getText());
            handleCancelStudentSelected();
            handleDataChanged();
            service.infoBox("STUDENT UPDATED SUCCESSFULLY", "", "", Alert.AlertType.INFORMATION);
        }
        catch (ValidatorException | RepositoryException e){
            service.infoBox(e.getMessage(), "","", Alert.AlertType.WARNING);
        }

    }

    @FXML
    private void handleAddStudent(){

        try{
            checkFieldsToBeCompleted();
            service.addStudent(nameField.getText(), Integer.parseInt( groupField.getText()), emailField.getText(), teacherField.getText());
            clearFields();
            handleDataChanged();
            service.infoBox("STUDENT CREATED SUCCESSFULLY", "", "", Alert.AlertType.INFORMATION);
        }
        catch (RepositoryException | ValidatorException e){
            service.infoBox(e.getMessage(), "","", Alert.AlertType.WARNING);
        }


    }

    @FXML
    private void handleCancelStudentSelected(){

        setImageLabelNotSelected();
        cancelSelectedStudent.setVisible(false);
        emailStudentButton.setVisible(false);
        clearFields();
    }

    @FXML
    private void emailStudent(){

        service.infoBox("SENDING MAIL... PLEASE WAIT!", "", "", Alert.AlertType.INFORMATION);
        service.sendEmail(studentSearched);
        service.infoBox("MAIL SENT SUCCESSGULLY", "", "", Alert.AlertType.INFORMATION);

    }

    private void clearFields(){

        nameField.clear();
        groupField.clear();
        teacherField.clear();
        emailField.clear();
    }

    private void setGraphicToCancelStudentButton(){

        cancelSelectedStudent.setVisible(false);
        cancelSelectedStudent.graphicProperty().bind(Bindings.when(cancelSelectedStudent.hoverProperty())
                .then(new ImageView(IMAGE_CANCEL_BUTTON_HOVER))
                .otherwise(new ImageView(IMAGE_CANCEL_BUTTON)));

        cancelSelectedStudent.setShape(new Circle());
    }

    private boolean checkExistance(int value, List<Integer> values){

        for(Integer thisValue: values){
            if(thisValue == value)
                return true;
        }

        return false;

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

    private void initializeLazyData(){
        if(start<step){
            if(step>=bigDataStudent.size()){
                step = bigDataStudent.size();
            }

            lazyDataStudent.addAll(bigDataStudent.subList(start, step));
            start = step;
            step+=10;
        }
    }

    private void initializeData(Iterable<Student> listOfStudents){
        bigDataStudent = FXCollections.observableArrayList(StreamSupport.stream(listOfStudents.spliterator(), false).collect(Collectors.toList()));
        initializeLazyData();
        lazyLoading();
        initializeListOfStudents();
    }


    private void artificialInitialize(){

        setSelectedSideBarInTheBeginning();
        hideAllButtons();
        add.setVisible(true);

        setGraphicToCancelStudentButton();
        setImageLabelNotSelected();
        filterInitialize();
    }

    public void setService(Service service){
        this.service = service;
        artificialInitialize();
    }

    public void setControllers(HomeworkController homeworkController , GradeController gradeController){
        this.homeworkController = homeworkController;
        this.gradeController = gradeController;
    }

    public void setStage(Stage primaryStage, AnchorPane homeworkStage, AnchorPane gradeStage){
        this.primaryStage = primaryStage;
        this.homeworkStage = homeworkStage;
        this.gradeStage = gradeStage;
    }

    @FXML
    private void handleFilterField(){

        if(!groupFilter.getSelectionModel().isEmpty()){
            String group = groupFilter.getSelectionModel().getSelectedItem().toString();
            String name = nameFilter.getText();
            String teacher = teacherFilter.getText();
            reinitializeLazyVariables();
            initializeData(service.filterStudents(group, name, teacher));
        }
    }

    private void reinitializeLazyVariables() {
        start = 0;
        step = 10;
        lazyDataStudent.clear();
    }


    @FXML
    private void handleHomeworkPanelClickedButton() throws IOException {

        AnchorPane ilTrimitLaPlimbare = new AnchorPane();
        AnchorPane rootLayout = homeworkStage;
        this.previousScene.setRoot(ilTrimitLaPlimbare); // here happens the hack
        Scene scene = new Scene(rootLayout, 900, 600);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        homeworkController.setService(service);
        homeworkController.setPreviousScene(scene);
        homeworkController.handleDataChanged();
    }

    private void hideAllButtons(){
        update.setVisible(false);
        add.setVisible(false);
        emailStudentButton.setVisible(false);
    }

    private void setImageLabelNotSelected(){

        hideAllButtons();
        add.setVisible(true);
        Circle circle = new Circle(50);
        circle.setFill(new ImagePattern(IMAGE_STUDENT_NOTSELECTED));
        selectedStudentLabel.setGraphic(circle);

    }

    private void setImageToLabelSelected(int idStudent){
        hideAllButtons();
        update.setVisible(true);
        Circle circle = new Circle(50);
        circle.setFill(new ImagePattern(IMAGE_STUDENT_SELECTED));
        selectedStudentLabel.setGraphic(circle);
        studentSearched = service.getOneStudent(idStudent);

        completeFields(service.getOneStudent(idStudent));
        cancelSelectedStudent.setVisible(true);
        emailStudentButton.setVisible(true);
    }

    @Override
    public void update(Observable o, Object arg) {
        bigDataStudent.setAll(StreamSupport.stream(service.getAllStudenti().spliterator(),false)
                .collect(Collectors.toList()));
    }


    private class StudentCell extends ListCell<Student> {

        @Override
        protected void updateItem(Student student, boolean empty) {
            super.updateItem(student, empty);
            Button deleteButton = new Button();
            Button studentButton = new Button();

            studentButton.setOnMouseClicked(e -> setImageToLabelSelected((Integer) studentButton.getUserData()));

            deleteButton.setOnMouseClicked(e -> handleDeleteStudent((Integer) deleteButton.getUserData()));

            studentButton.graphicProperty().bind(Bindings.when(studentButton.hoverProperty())
                    .then(new ImageView(IMAGE_STUDENT_HOVERED))
                    .otherwise(new ImageView(IMAGE_STUDENT)));

            deleteButton.graphicProperty().bind(
                    Bindings.when(deleteButton.hoverProperty())
                            .then(new ImageView(IMAGE_TRASH_HOVERD))
                            .otherwise(new ImageView(IMAGE_TRASH)));

            deleteButton.setShape(new Circle(1.5));
            studentButton.setShape(new Circle(1.5));
            deleteButton.setStyle("-fx-background-insets: 0; -fx-padding: 0;");
            studentButton.setStyle("-fx-background-insets: 0; -fx-padding: 0;");


            if (empty || student == null) {

                setGraphic(null);
                setText(null);
            } else {
                HBox hbox = new HBox();

                studentButton.setUserData(student.getId());
                deleteButton.setUserData(student.getId());
                hbox.getChildren().addAll(deleteButton, studentButton);
                setGraphic(hbox);
                setText(student.getView());

            }
        }
    }

    private void initializeListOfStudents(){

        listViewOfStudents.setItems(lazyDataStudent);

        listViewOfStudents.setCellFactory( (param) -> new StudentCell());

    }

    public void setPreviousScene(Scene previousScen){
        this.previousScene = previousScen;
    }

}
