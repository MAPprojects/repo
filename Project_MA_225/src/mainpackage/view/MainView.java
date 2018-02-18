//package mainpackage.view;
//
//import javafx.beans.value.ChangeListener;
//import javafx.beans.value.ObservableValue;
//import javafx.scene.control.*;
//import javafx.scene.control.cell.PropertyValueFactory;
//import javafx.scene.layout.AnchorPane;
//import javafx.scene.layout.BorderPane;
//import javafx.scene.layout.GridPane;
//import javafx.scene.layout.HBox;
//import javafx.scene.paint.Color;
//import javafx.scene.text.Font;
//import mainpackage.domain.Student;
//
//public class MainView {
//
//    private MainController ctr;
//    private BorderPane borderPane;
//
//    TableView<Student> student_table=new TableView<>();
//
//    TextField student_id_field = new TextField();
//    TextField student_name_field = new TextField();
//    TextField student_email_field = new TextField();
//    TextField student_teacher_field = new TextField();
//
//    Button button_clear = new Button("Clear All");
//    Button button_add = new Button("Add");
//    Button button_update = new Button("Update");
//    Button button_delete = new Button("Delete");
//    Button button_useless = new Button("NIMIC");
//
//    private Label createLabel(String s){
//        Label l=new Label(s);
//        l.setFont(new Font(15));
//        l.setTextFill(Color.BLACK);
//        l.setStyle("-fx-font-weight: bold");
//        return l;
//    }
//
//
//    public MainView(MainController ctr) {
//        this.ctr = ctr;
//        initView();
//    }
//
//    private void initView() {
//        borderPane = new BorderPane();
//        borderPane.setLeft(initLeft());
//        borderPane.setRight(initRight());
//    }
//
//    private AnchorPane initRight() {
//        AnchorPane right_anchor = new AnchorPane();
//
//        GridPane gridPane = createStudentGridPane();
//        HBox hbox = createHBox();
//
//        right_anchor.getChildren().add(gridPane);
//        right_anchor.getChildren().add(hbox);
//
//        AnchorPane.setBottomAnchor(hbox, 200d);
//        AnchorPane.setRightAnchor(gridPane, 30d);
//        AnchorPane.setTopAnchor(gridPane, 50d);
//
//        return right_anchor;
//    }
//
//    private HBox createHBox() {
//        HBox hBox = new HBox();
//
//        hBox.getChildren().addAll(button_add, button_update, button_delete, button_clear, button_useless);
//        button_add.setOnAction(ctr::handleAddStudent);
//        button_delete.setOnAction(ctr::handleDeleteStudent);
//        button_update.setOnAction(ctr::handleUpdateStudent);
//        button_clear.setOnAction(ctr::handleClearFields);
//
//        return hBox;
//    }
//
//    private GridPane createStudentGridPane() {
//
//        GridPane gridPane = new GridPane();
//        gridPane.add(createLabel("Id"), 0, 0);
//        gridPane.add(createLabel("Name"), 0, 1);
//        gridPane.add(createLabel("Email"), 0, 2);
//        gridPane.add(createLabel("Teacher"), 0, 3);
//
//        gridPane.add(student_id_field, 1,0);
//        gridPane.add(student_name_field, 1,1);
//        gridPane.add(student_email_field, 1,2);
//        gridPane.add(student_teacher_field, 1,3);
//
//        return gridPane;
//    }
//
//    private AnchorPane initLeft() {
//        AnchorPane left_anchor = new AnchorPane();
//        TableView<Student> tableView = createStudentTableView();
//        left_anchor.getChildren().add(tableView);
//        AnchorPane.setTopAnchor(tableView, 30d);
//        AnchorPane.setLeftAnchor(tableView, 15d);
//        AnchorPane.setBottomAnchor(tableView, 30d);
//
//        return left_anchor;
//    }
//
//    private TableView<Student> createStudentTableView() {
//        TableColumn<Student, String> id_column= new TableColumn<>("Id");
//        TableColumn<Student, String> name_column= new TableColumn<>("Name");
//        TableColumn<Student, String> email_column= new TableColumn<>("Email");
//        TableColumn<Student, String> teacher_column= new TableColumn<>("Teacher");
//        student_table.getColumns().addAll(id_column, name_column, email_column, teacher_column);
//
//        id_column.setCellValueFactory(new PropertyValueFactory<Student, String>("id"));
//        name_column.setCellValueFactory(new PropertyValueFactory<Student, String>("name"));
//        email_column.setCellValueFactory(new PropertyValueFactory<Student, String>("email"));
//        teacher_column.setCellValueFactory(new PropertyValueFactory<Student, String>("teacher"));
//
//        student_table.setItems(ctr.getModel());
//
//        student_table.getSelectionModel().selectedItemProperty().
//                addListener(new ChangeListener<Student>() {
//                    @Override
//                    public void changed(ObservableValue<? extends Student> observable,
//                                        Student oldValue, Student newValue) {
//                        ctr.showStudent(newValue);
//                    }
//                });
//
//        return student_table;
//    }
//
//    public BorderPane getView(){
//        return borderPane;
//    }
//
//}
