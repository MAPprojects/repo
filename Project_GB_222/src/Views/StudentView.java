package Views;

import Constrollers.StudentController;
import Domain.Student;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;

public class StudentView
{
    StudentController studentController;

    TableView<Student> tabel = new TableView<>();

    BorderPane borderPane;

    Label labelID;
    Label labelNume;
    Label labelGrupa;
    Label labelEmail;
    Label labelCadruDidactic;

    public TextField textID;
    public TextField textNume;
    public TextField textGrupa;
    public TextField textEmail;
    public TextField textCadruDidactic;

    Button bttAdd;
    Button bttUpdate;
    Button bttDelete;
    Button bttClear;

    public ComboBox<String> cmbFilt;
    public TextField txtFilt;
    Button bttFilt;

    public StudentView(StudentController studentController)
    {
        this.studentController=studentController;
        initView();
    }

    private void initView()
    {
        borderPane = new BorderPane();

        borderPane.setLeft(initLeft());
        borderPane.setRight(initRight());


    }

    private AnchorPane initRight() {
        AnchorPane topAnchor = new AnchorPane();


        createLabels();
        createTextFields();
        createButtons();

        GridPane gridPane = new GridPane();
        gridPane.add(labelID,0,0);
        gridPane.add(labelNume,0,1);
        gridPane.add(labelGrupa,0,2);
        gridPane.add(labelEmail,0,3);
        gridPane.add(labelCadruDidactic,0,4);

        gridPane.setHgap(20d);

        gridPane.add(textID,1,0);
        gridPane.add(textNume,1,1);
        gridPane.add(textGrupa,1,2);
        gridPane.add(textEmail,1,3);
        gridPane.add(textCadruDidactic,1,4);

        HBox hBox1 = new HBox();
        hBox1.getChildren().addAll(bttAdd,bttUpdate,bttDelete,bttClear);
        hBox1.setSpacing(10d);

        HBox filt = new HBox();
        bttFilt = new Button("Filter");
        bttFilt.setOnAction(studentController::handleFilter);
        cmbFilt=new ComboBox<>();
        cmbFilt.getItems().setAll("Nume","Grupa","Cadru Didactic");
        txtFilt = new TextField();
        filt.getChildren().addAll(cmbFilt,txtFilt,bttFilt);
        filt.setSpacing(10d);

        VBox vBox1 = new VBox();
        vBox1.getChildren().addAll(gridPane,hBox1,filt);
        vBox1.setSpacing(10d);

        AnchorPane.setRightAnchor(vBox1,30d);
        AnchorPane.setTopAnchor(vBox1,20d);
        topAnchor.getChildren().addAll(vBox1);
        return topAnchor;
    }

    private AnchorPane initLeft()
    {
        AnchorPane leftAnch = new AnchorPane();
        tabel=createTableView();
        tabel.setPrefSize(400,600);
        leftAnch.getChildren().add(tabel);

        AnchorPane.setLeftAnchor(tabel,20d);
        AnchorPane.setTopAnchor(tabel,20d);

        return leftAnch;
    }

    private TableView<Student> createTableView()
    {
        TableColumn<Student, String> columnNume = new TableColumn<>("Nume");
        TableColumn<Student, Integer> columnGrupa = new TableColumn<>("Grupa");
        TableColumn<Student, String> columnEmail = new TableColumn<>("Email");
        TableColumn<Student,String> columnCadruD = new TableColumn<>("Cadru Didactic");

        tabel.getColumns().addAll(columnNume,columnGrupa,columnEmail,columnCadruD);

        columnNume.setCellValueFactory(new PropertyValueFactory<Student,String>("nume"));
        columnEmail.setCellValueFactory(new PropertyValueFactory<Student, String>("email"));
        columnCadruD.setCellValueFactory(new PropertyValueFactory<Student,String>("cadruDidactic"));
        columnGrupa.setCellValueFactory(new PropertyValueFactory<Student,Integer>("grupa"));

        //set items lista de studenti
        tabel.setItems(studentController.getModelStudent());

        tabel.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Student>() {
            @Override
            public void changed(ObservableValue<? extends Student> observable, Student oldValue, Student newValue) {
                studentController.fillFields(newValue);
            }
        });

        return tabel;
    }

    private void createLabels()
    {
        labelNume=new Label("Nume");
        labelGrupa=new Label("Grupa");
        labelID=new Label("ID");
        labelEmail=new Label("Email");
        labelCadruDidactic=new Label("Cadru Didactic");
    }

    private void createTextFields()
    {
        textID=new TextField();
        textNume=new TextField();
        textGrupa=new TextField();
        textEmail=new TextField();
        textCadruDidactic=new TextField();
    }

    private void createButtons()
    {
        bttAdd=new Button("Adauga");
        bttAdd.setOnAction(studentController::handleAdd);
        bttUpdate= new Button("Update");
        bttUpdate.setOnAction(studentController::handleUpdate);
        bttDelete=new Button("Sterge");
        bttDelete.setOnAction(studentController::handleDelete);
        bttClear= new Button("Clear");
        bttClear.setOnAction(studentController::handleClear);
    }

    public BorderPane getView()
    {
        return borderPane;
    }
}
