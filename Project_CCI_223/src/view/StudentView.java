package view;

import domain.Student;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class StudentView {
    //student controller
    private StudentController ctrlStudent;
    //root pane
    private BorderPane borderPane;
    //tableview
    public TableView<Student> tableView=new TableView<>();
    //textfields
    public TextField fieldID=createTextField();
    public TextField fieldNume=createTextField();
    public TextField fieldGrupa=createTextField();
    public TextField fieldEmail=createTextField();
    public TextField fieldCadruDidactic=createTextField();
    //buttons
    private Button buttonSave=createButton("SAVE");
    private Button buttonUpdate=createButton("UPDATE");
    private Button buttonDelete=createButton("DELETE");
    private Button buttonClearAll=createButton("Clear ALL");

    /**
     * Create a customized textField
     * @return TextField
     */
    private TextField createTextField(){
        TextField textField=new TextField();
        textField.setFont(new Font(15));
        textField.setStyle("-fx-text-fill: darkcyan");
        return textField;
    }

    /**
     * Creates a customized button with the text text
     * @param text Sting
     * @return Button
     */
    private Button createButton(String text){
        Button button=new Button(text);
        button.setTextFill(Color.DARKCYAN);
        //button.setFont(new Font());
        button.setStyle("-fx-font-weight: bold");
        return button;
    }

    /**
     * Getter for the main view
     * @return BorderPane
     */
    public BorderPane getView() {
        return borderPane;
    }

    /**
     * Constructor
     * @param ctrlStudent StudentController
     */
    public StudentView(StudentController ctrlStudent) {
        this.ctrlStudent = ctrlStudent;
        initView();
    }

    /**
     * Init the main view
     */
    private void initView() {
        borderPane=new BorderPane();
        //top AnchorPane
        borderPane.setTop(initTop());
        // left TableView
        borderPane.setLeft(initLeft());
        //right Fields+ labels
        borderPane.setRight(initRight());

    }

    /**
     * Creates a customized label with the text given as a parameter
     * @param text String
     * @return Label
     */
    private Label createLabel(String text){
        Label l=new Label(text);
        l.setStyle("-fx-font-family: cursive");
        l.setTextFill(Color.DARKCYAN);
        l.setFont(new Font(15));
        return l;
    }

    /**
     * Inits the right side of the view
     * @return AnchorPane
     */
    private AnchorPane initRight() {
        AnchorPane rightAnchorPane=new AnchorPane();
        GridPane gridPane=new GridPane();
        rightAnchorPane.getChildren().add(gridPane);
        AnchorPane.setLeftAnchor(gridPane,10d);
        AnchorPane.setTopAnchor(gridPane,20d);
        AnchorPane.setRightAnchor(gridPane,20d);

        Label labelID=createLabel("ID Student");
        Label labelNume=createLabel("Nume");
        Label labelGrupa=createLabel("Grupa");
        Label labelEmail=createLabel("Email");
        Label labelCadruDidactic=createLabel("Cadru didactic   ");

        gridPane.add(labelID,0,0);
        gridPane.add(labelNume,0,1);
        gridPane.add(labelGrupa,0,2);
        gridPane.add(labelEmail,0,3);
        gridPane.add(labelCadruDidactic,0,4);

        gridPane.add(fieldID,2,0);
        gridPane.add(fieldNume,2,1);
        gridPane.add(fieldGrupa,2,2);
        gridPane.add(fieldEmail,2,3);
        gridPane.add(fieldCadruDidactic,2,4);

        AnchorPane anchorPaneButoane=createButtonPane();
        rightAnchorPane.getChildren().add(anchorPaneButoane);
        AnchorPane.setTopAnchor(anchorPaneButoane,200d);
        AnchorPane.setLeftAnchor(anchorPaneButoane,10d);
        AnchorPane.setRightAnchor(anchorPaneButoane,20d);

        return  rightAnchorPane;
    }

    /**
     * Inits the pane with the buttons and the associated actions for the buttons
     * @return AnchorPane
     */
    private AnchorPane createButtonPane() {
        AnchorPane anchorPaneButoane=new AnchorPane();
        anchorPaneButoane.getChildren().add(buttonSave);
        AnchorPane.setLeftAnchor(buttonSave,0d);
        anchorPaneButoane.getChildren().add(buttonUpdate);
        AnchorPane.setLeftAnchor(buttonUpdate,65d);
        anchorPaneButoane.getChildren().add(buttonDelete);
        AnchorPane.setLeftAnchor(buttonDelete,150d);
        anchorPaneButoane.getChildren().add(buttonClearAll);
        AnchorPane.setRightAnchor(buttonClearAll,0d);

        buttonClearAll.setOnAction(ctrlStudent::handleClearAll);
        buttonSave.setOnAction(ctrlStudent::handleAddStudent);
        buttonDelete.setOnAction(ctrlStudent::handleDeleteStudent);
        buttonUpdate.setOnAction(ctrlStudent::handleUpdateStudent);

        return anchorPaneButoane;
    }

    /**
     * Inits the left view of the application
     * @return AnchorPane
     */
    private AnchorPane initLeft() {
        AnchorPane leftAnchrPane=new AnchorPane();
        HBox hBox=new HBox();
        leftAnchrPane.getChildren().add(hBox);
        tableView=createTableView();
        hBox.getChildren().add(tableView);
        AnchorPane.setLeftAnchor(hBox,10d);
        AnchorPane.setTopAnchor(hBox,20d);

        return leftAnchrPane;
    }

    /**
     * Creates the table view and customize it
     * @return TableView
     */
    private TableView<Student> createTableView() {
        TableColumn<Student,String> columnNume=new TableColumn<>("Nume");
        TableColumn<Student,Integer> columnGrupa=new TableColumn<>("Grupa");
        TableColumn<Student,String> columnEmail=new TableColumn<>("Email");
        TableColumn<Student,String> columnCadruDidactic=new TableColumn<>("Cadru didactic");
        tableView.getColumns().addAll(columnNume,columnGrupa,columnEmail,columnCadruDidactic);

        columnNume.setCellValueFactory(new PropertyValueFactory<Student,String>("nume"));
        columnGrupa.setCellValueFactory(new PropertyValueFactory<Student,Integer>("grupa"));
        columnEmail.setCellValueFactory(new PropertyValueFactory<Student,String>("email"));
        columnCadruDidactic.setCellValueFactory(new PropertyValueFactory<Student,String>("cadru_didactic_indrumator_de_laborator"));

        tableView.setItems(ctrlStudent.getModel());

        tableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Student>() {
            @Override
            public void changed(ObservableValue<? extends Student> observable, Student oldValue, Student newValue) {
                ctrlStudent.showStudentDetails(newValue);
            }
        });

        return tableView;
    }

    /**
     * Inits the top view of the application
     * @return AnchorPane
     */
    private AnchorPane initTop() {
        AnchorPane anchorPane=new AnchorPane();
        Label labelTitle=new Label("Student Management System");
        anchorPane.getChildren().add(labelTitle);
        labelTitle.setFont(new Font(37));
        labelTitle.setTextFill(Color.DARKCYAN);
        labelTitle.setStyle("-fx-font-weight: bold");
        AnchorPane.setLeftAnchor(labelTitle,30d);
        AnchorPane.setTopAnchor(labelTitle,10d);
        return anchorPane;
    }
}
