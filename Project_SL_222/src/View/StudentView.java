package View;

import Domain.Student;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Callback;

public class StudentView {
    private StudentController ctr;
    private BorderPane borderPane;
    TableView<Student> tableView=new TableView<>();

    Button addBtn = new Button("Add");
    Button delBtn = new Button("Delete");
    Button updBtn = new Button("Update");
    TextField idField = createField();
    TextField nameField = createField();
    TextField groupField = createField();
    TextField emailField = createField();
    TextField teacherField = createField();

    public StudentView(StudentController ctr){
        this.ctr=ctr;
        initView();
    }
    public BorderPane getView(){
        return borderPane;
    }

    /*
    public BorderPane initaddView(){
        BorderPane addPane=new BorderPane();
        GridPane gridPane= createGridPane();
        addPane.getChildren().add(gridPane);
        return addPane;
    }
    */

    private void initView(){
        borderPane = new BorderPane();
        borderPane.setTop(initTop());
        borderPane.setLeft(initLeft());
        borderPane.setCenter(initCenter());
        borderPane.setBottom(initBottom());
    }

    private AnchorPane initCenter(){
        AnchorPane centerPane = new AnchorPane();
        TableView<Student> tableView=createTableView();
        //tableView.setStyle("-fx-border-color: darkgreen;-fx-background-color: darkslategrey;-fx-text-fill: darkgreen");
        centerPane.getChildren().add(tableView);
        AnchorPane.setTopAnchor(tableView,90d);
        AnchorPane.setRightAnchor(tableView,20d);
        return centerPane;
    }

    private TableView<Student> createTableView(){
        TableColumn<Student,Integer> columnId=new TableColumn<>("ID");
        TableColumn<Student,String> columnName=new TableColumn<>("Name");
        //TableColumn<Student,Integer> columnGroup=new TableColumn<>("Group");
        TableColumn<Student,String> columnEmail=new TableColumn<>("E-mail");
        //TableColumn<Student,String> columnTeacher=new TableColumn<>("Teacher");


        tableView.getColumns().addAll(columnId,columnName,columnEmail);
        addButtonToTable();

        columnId.setCellValueFactory(new PropertyValueFactory<Student,Integer>("idStudent"));
        columnName.setCellValueFactory(new PropertyValueFactory<Student,String>("nume"));
        //columnGroup.setCellValueFactory(new PropertyValueFactory<Student,Integer>("grupa"));
        columnEmail.setCellValueFactory(new PropertyValueFactory<Student,String>("email"));
        //columnTeacher.setCellValueFactory(new PropertyValueFactory<Student,String>("profesor"));

        tableView.setItems(ctr.getModel());
        tableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Student>() {
            @Override
            public void changed(ObservableValue<? extends Student> observable, Student oldValue, Student newValue) {
                ctr.showStudentDetails(newValue);
            }
        });
        return tableView;
    }

    private void addButtonToTable() {
        TableColumn<Student, Void> colBtn = new TableColumn("Delete");

        Callback<TableColumn<Student, Void>, TableCell<Student, Void>> cellFactory = new Callback<TableColumn<Student, Void>, TableCell<Student, Void>>() {
            @Override
            public TableCell<Student, Void> call(final TableColumn<Student, Void> param) {
                final TableCell<Student, Void> cell = new TableCell<Student, Void>() {

                    private final Button btn = new Button("");

                    {
                        btn.setStyle("-fx-text-fill:black;-fx-font-size: 14;-fx-background-color: red;-fx-border-color: black");

                        //btn.setOnAction(ctr::connectDelete);

                        btn.setOnAction((ActionEvent event)-> {
                        Student data = getTableView().getItems().get(getIndex());
                        ctr.connectDelete(data,event);
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                            btn.setStyle(
                                "-fx-min-height: 20px; -fx-min-width: 20px; -fx-background-image: url(file:icon.jpeg); -fx-background-size: 15px 15px; -fx-background-repeat: no-repeat; -fx-background-position: center 8px;");
                        }
                    }
                };
                return cell;
            }
        };

        colBtn.setCellFactory(cellFactory);

        tableView.getColumns().add(colBtn);

    }

    private MenuBar initTop(){
        final Menu menu1 = new Menu("Students");
        menu1.setStyle("-fx-text-fill:white;-fx-font-size: 14;-fx-background-color: black;-fx-border-color: darkslategrey");
        final Menu menu2 = new Menu("Assignments");
        menu2.setStyle("-fx-text-fill:white;-fx-font-size: 14;-fx-background-color: black;-fx-border-color: darkslategrey");
        final Menu menu3 = new Menu("Grades");
        menu3.setStyle("-fx-text-fill:white;-fx-font-size: 14;-fx-background-color: black;-fx-border-color: darkslategrey");


        MenuBar menuBar = new MenuBar();
        menuBar.setStyle("-fx-background-color: black");
        menuBar.getMenus().addAll(menu1, menu2, menu3);
        return menuBar;
    }

    private AnchorPane initLeft(){
        AnchorPane leftPane = new AnchorPane();
        GridPane gridPane = createGridPane();
        leftPane.getChildren().add(gridPane);

        AnchorPane.setTopAnchor(gridPane,200d);
        AnchorPane.setLeftAnchor(gridPane,20d);

        return leftPane;
    }

    private GridPane createGridPane(){
        GridPane gridPane = new GridPane();
        gridPane.add(createLabel("ID"),0,0);
        gridPane.add(createLabel("Name"),0,1);
        gridPane.add(createLabel("Group"),0,2);
        gridPane.add(createLabel("E-mail"),0,3);
        gridPane.add(createLabel("Teacher"),0,4);

        gridPane.add(idField,1,0);
        gridPane.add(nameField,1,1);
        gridPane.add(groupField,1,2);
        gridPane.add(emailField,1,3);
        gridPane.add(teacherField,1,4);

        ColumnConstraints c1 = new ColumnConstraints();
        c1.setPrefWidth(100d);
        ColumnConstraints c2 = new ColumnConstraints();
        c2.setPrefWidth(200d);

        gridPane.getColumnConstraints().addAll(c1,c2);
        return gridPane;
    }

    private Label createLabel(String s){
        Label l = new Label(s);
        l.setFont(new Font(15));
        l.setTextFill(Color.WHITE);
        l.setStyle("-fx-font-weight:bold;-fx-font-size: 14");
        return l;
    }

    private TextField createField(){
        TextField textField=new TextField();
        textField.setStyle("-fx-text-fill:white;-fx-font-size: 14;-fx-background-color: darkslategrey;-fx-border-color: black");
        return textField;
    }

    private AnchorPane initBottom(){
        AnchorPane bottomPane = new AnchorPane();
        HBox hBox=createHBox();

        bottomPane.getChildren().add(hBox);
        AnchorPane.setLeftAnchor(hBox,320d);
        AnchorPane.setRightAnchor(hBox,50d);
        AnchorPane.setBottomAnchor(hBox,20d);

        return bottomPane;
    }
    private HBox createHBox(){
        HBox hBox = new HBox();

        addBtn.setTextFill(Color.WHITE);
        addBtn.setStyle("-fx-background-color: darkslategrey;-fx-border-color: black");
        delBtn.setTextFill(Color.WHITE);
        delBtn.setStyle("-fx-background-color: darkslategrey;-fx-border-color: black");
        updBtn.setTextFill(Color.WHITE);
        updBtn.setStyle("-fx-background-color: darkslategrey;-fx-border-color: black");
        hBox.getChildren().addAll(addBtn,updBtn);

        addBtn.setOnAction(ctr::connectAdd);
        //delBtn.setOnAction(ctr::connectDelete);
        updBtn.setOnAction(ctr::connectUpdate);
        return hBox;
    }
}
