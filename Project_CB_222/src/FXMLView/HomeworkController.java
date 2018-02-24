package FXMLView;

import Domain.Tema;
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

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class HomeworkController {

    private final Image IMAGE_HOMEWORK = new Image(getClass().getClassLoader().getResource("Resources/homeworkPictogram.png").toString(), 64, 64, false, false);

    private final Image IMAGE_HOMEWORK_HOVER = new Image(getClass().getClassLoader().getResource("Resources/homeworkPictogramHover.png").toString(), 64, 64, false, false);

    private final Image IMAGE_HOMEWORK_SELECTED = new Image(getClass().getClassLoader().getResource("Resources/homeworkPictogramSelected.png").toString(), 100, 100, false, false);

    private final Image IMAGE_HOMEWORK_NOTSELECTED = new Image(getClass().getClassLoader().getResource("Resources/homeworkPictogramNotSelected.png").toString(), 100, 100, false, false);

    private final Image IMAGE_TRASH = new Image(getClass().getClassLoader().getResource("Resources/trashPictogram.png").toString(), 64, 64, false, false);

    private final Image IMAGE_TRASH_HOVERED = new Image(getClass().getClassLoader().getResource("Resources/trashPictogramHover.png").toString(), 64, 64, false, false);

    private final Image IMAGE_CANCEL_BUTTON = new Image(getClass().getClassLoader().getResource("Resources/cancelStudent.png").toString(), 20, 28, false, false);

    private final Image IMAGE_CANCEL_BUTTON_HOVER = new Image(getClass().getClassLoader().getResource("Resources/cancelStudentHover.png").toString(), 20, 28, false, false);

    private int start = 0;

    private int step = 10;

    private Service service;

    private StudentController studentController;

    private GradeController gradeController;

    private Stage primaryStage;

    private Scene previousScene;

    private AnchorPane studentStage;

    private AnchorPane gradeStage;

    private ObservableList<Tema> bigDataHomework;

    private ObservableList<Tema> lazyDataHomework = FXCollections.observableArrayList();

    private Tema homeworkSearched;

    @FXML
    private TextField titleFilter;

    @FXML
    private TextField deadlineFilter;

    @FXML
    private Button cancelSelectedHomework;

    @FXML
    private Label selectedHomeworkLabel;

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
    private TextField deadlineField;

    @FXML
    private TextField titleField;

    @FXML
    private TextArea descriptionTextArea;

    public void setService(Service service){
        this.service = service;
        artificialInitialize();
    }

    private void initializeListOfHomeworks(){

        listViewOfHomeworks.setItems(lazyDataHomework);

        listViewOfHomeworks.setCellFactory(param -> new HomeworkCell());

    }

    public void setControllers(StudentController studentController, GradeController gradeController){
        this.studentController = studentController;
        this.gradeController = gradeController;
    }

    public void setStage(Stage primaryStage, AnchorPane studentStage, AnchorPane gradeStage){
        this.primaryStage = primaryStage;
        this.studentStage = studentStage;
        this.gradeStage = gradeStage;
    }

    private void completeFields(Tema tema) {

        titleField.setText(tema.getTitlu());
        descriptionTextArea.setText(tema.getDescriere());
        deadlineField.setText(""+tema.getDeadline());

    }

    public void setPreviousScene(Scene previousScene){
        this.previousScene = previousScene;
    }

    private void hideAllButtons(){
        update.setVisible(false);
        add.setVisible(false);
    }


    private void setSelectedSideBarAndUpBarInTheBeginning(){
        panelHomework.getStyleClass().add("clickedEffect");

    }

    private void initializeLazyData() {
        if(start<step){
            if(step>=bigDataHomework.size()){
                step = bigDataHomework.size();
            }

            lazyDataHomework.addAll(bigDataHomework.subList(start, step));
            start = step;
            step+=10;
        }
    }

    private ScrollBar getListViewScrollBar() {
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

    private void checkFieldsToBeCompleted(){

        String error = "";
        int check=0;

        if (deadlineField.getText() == "" || deadlineField==null || deadlineField.getText().length()==0){
            error += "Deadline Field CANNOT be empty!" + "\n";
            check=1;
        }

        if (titleField.getText() == "" || titleField==null || titleField.getText().length()==0){
            error += "Title Field CANNOT be empty!" + "\n";
            check=1;
        }

        if (descriptionTextArea.getText() == "" || descriptionTextArea==null || descriptionTextArea.getText().length()==0){
            error += "Group cannot be empty!" + "\n";
            check=1;
        }
        if(check==1){
            throw new ValidatorException(error);
        }
    }

    private void lazyLoading() {

        ScrollBar listViewScrollBar = getListViewScrollBar();
        listViewScrollBar.valueProperty().addListener((observable, oldValue, newValue) -> {
            double position = newValue.doubleValue();
            ScrollBar scrollBar = getListViewScrollBar();
            if (position == scrollBar.getMax()) {
                if (start <= bigDataHomework.size()){
                    initializeLazyData();
                }
            }
        });
    }

    private void clearFields(){
        titleField.clear();
        descriptionTextArea.clear();
        deadlineField.clear();
    }

    private void setGraphicToCancelHomeworkButton(){

        cancelSelectedHomework.setVisible(false);
        cancelSelectedHomework.graphicProperty().bind(Bindings.when(cancelSelectedHomework.hoverProperty())
                .then(new ImageView(IMAGE_CANCEL_BUTTON_HOVER))
                .otherwise(new ImageView(IMAGE_CANCEL_BUTTON)));

        cancelSelectedHomework.setShape(new Circle());
    }

    private void handleDeleteHomework(int idHomework){

        try{
            service.removeTema(idHomework);
            clearFields();
            handleDataChanged();
            service.infoBox("HOMEWORK ADDED SUCCESSFULLY", "", "", Alert.AlertType.INFORMATION);
        }
        catch (ValidatorException | RepositoryException e){
        service.infoBox(e.getMessage(), "", "", Alert.AlertType.WARNING);
        }

    }

    private void setImageLabelSelected(int idHomework){
        hideAllButtons();
        update.setVisible(true);
        Circle circle = new Circle(50);
        circle.setFill(new ImagePattern(IMAGE_HOMEWORK_SELECTED));
        selectedHomeworkLabel.setGraphic(circle);
        homeworkSearched = service.getOneTema(idHomework);

        completeFields(service.getOneTema(idHomework));
        cancelSelectedHomework.setVisible(true);
    }

    public void handleDataChanged() {
        reinitializeLazyVariables();
        initializeData(service.getAllTeme());
    }

    private void initializeData(Iterable<Tema> listOfHomeworks) {
        bigDataHomework = FXCollections.observableArrayList(StreamSupport.stream(listOfHomeworks.spliterator(), false).collect(Collectors.toList()));
        initializeLazyData();
        lazyLoading();
        initializeListOfHomeworks();
    }

    private void reinitializeLazyVariables() {
        start = 0;
        step = 10;
        lazyDataHomework.clear();
    }

    private void artificialInitialize(){

        hideAllButtons();
        setSelectedSideBarAndUpBarInTheBeginning();
        setGraphicToCancelHomeworkButton();
        clearFields();
        setImageLabelNotSelected();
        add.setVisible(true);
    }

    private void setImageLabelNotSelected() {
        hideAllButtons();
        add.setVisible(true);
        Circle circle = new Circle(50);
        circle.setFill(new ImagePattern(IMAGE_HOMEWORK_NOTSELECTED));
        selectedHomeworkLabel.setGraphic(circle);
    }

    private class HomeworkCell extends ListCell<Tema> {

        @Override
        protected void updateItem(Tema homework, boolean empty) {
            super.updateItem(homework, empty);

            Button deleteButton = new Button();
            Button homeworkButton = new Button();

            deleteButton.setOnMouseClicked(e -> handleDeleteHomework((Integer) deleteButton.getUserData()));

            homeworkButton.setOnMouseClicked(e -> setImageLabelSelected((Integer) homeworkButton.getUserData()));

            homeworkButton.graphicProperty().bind(Bindings.when(homeworkButton.hoverProperty())
                    .then(new ImageView(IMAGE_HOMEWORK_HOVER))
                    .otherwise(new ImageView(IMAGE_HOMEWORK)));

            deleteButton.graphicProperty().bind(
                    Bindings.when(deleteButton.hoverProperty())
                            .then(new ImageView(IMAGE_TRASH_HOVERED))
                            .otherwise(new ImageView(IMAGE_TRASH)));

            deleteButton.setShape(new Circle(1.5));
            homeworkButton.setShape(new Circle(1.5));
            deleteButton.setStyle("-fx-background-insets: 0; -fx-padding: 0;");
            homeworkButton.setStyle("-fx-background-insets: 0; -fx-padding: 0;");

            if (empty || homework == null) {

                setGraphic(null);
                setText(null);
            } else {

                HBox hbox = new HBox();

                homeworkButton.setUserData(homework.getId());
                deleteButton.setUserData(homework.getId());

                hbox.getChildren().addAll(deleteButton, homeworkButton);

                setText(homework.getView());
                setGraphic(hbox);
            }
        }
    }

    @FXML
    private void ItemSelectedFromList(){
        homeworkSearched = listViewOfHomeworks.getSelectionModel().getSelectedItem();
        int id = homeworkSearched.getId();

        completeFields(service.getOneTema(id));

    }

    @FXML
    private void handleFilterField(){
        String title = titleFilter.getText();
        String deadline = deadlineFilter.getText();
        reinitializeLazyVariables();
        initializeData(service.filterHomeworks(title, deadline));
    }

    @FXML
    private void handleStudentPanelClickedButton(){

        AnchorPane ilTrimitLaPlimbare = new AnchorPane();
        this.previousScene.setRoot(ilTrimitLaPlimbare); // here happens the hack
        Scene scene = new Scene(studentStage, 900, 600);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        studentController.setService(service);
        studentController.setPreviousScene(scene);
        studentController.handleDataChanged();

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
    private void handleUpdateHomework(){
        try{
            int id = homeworkSearched.getId();
            service.updateTema(id, descriptionTextArea.getText(), Integer.parseInt( deadlineField.getText()), titleField.getText());
            clearFields();
            handleDataChanged();
            service.infoBox("HOMEWORK UPDATED SUCCESSFULLY", "", "", Alert.AlertType.INFORMATION);
        }
        catch (ValidatorException | RepositoryException e){
            service.infoBox(e.getMessage(), "","", Alert.AlertType.WARNING);
    }


    }

    @FXML
    private void handleCancelHomeworkSelected(){

        setImageLabelNotSelected();
        cancelSelectedHomework.setVisible(false);
        clearFields();
    }

    @FXML
    private void handleAddHomework(){

        try {
            service.addTema(descriptionTextArea.getText(), Integer.parseInt(deadlineField.getText()), titleField.getText());
            clearFields();
            handleDataChanged();
            service.infoBox("HOMEWORK ADDED SUCCESSFULLY", "", "", Alert.AlertType.INFORMATION);
        }
            catch (ValidatorException | RepositoryException e){
            service.infoBox(e.getMessage(), "","", Alert.AlertType.WARNING);
        }


    }

}
