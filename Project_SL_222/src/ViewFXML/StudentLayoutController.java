package ViewFXML;

import Domain.Student;
import Repository.RepositoryException;
import Service.StudentService;
import Util.ListEvent;
import Util.ListEventType;
import Util.Observable;
import Util.Observer;
import Validator.ValidationException;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.util.List;
import java.util.Optional;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


public class StudentLayoutController implements Observer<Student>{
    StudentService service;
    ObservableList<Student> model;
    @FXML
    private TableView<Student> studentTable;
    @FXML
    private TableColumn<Student, Integer> id;
    @FXML
    private TableColumn<Student, String> name;
    @FXML
    private TableColumn<Student, String> email;
    @FXML
    private TableColumn<Student, Void> delete;
    @FXML
    private TableColumn<Student,Void> edit;
    @FXML
    private TextField idField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField groupField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField teacherField;

    @FXML
    private TextField filterbynameField;
    @FXML
    private TextField filterbygroupField;
    @FXML
    private TextField filterbyteacherField;

    @FXML
    private Button updatebtn;

    @FXML
    private  Button addBtn;
    @FXML
    private MenuButton filterBtn;
    @FXML
    private Button refreshBtn;
    @FXML
    private Button doneBtn1;
    @FXML
    private Button doneBtn2;
    @FXML
    private Button doneBtn3;
    @FXML
    private Slider slider=new Slider(1,25,5);
    @FXML
    private Pagination pagination;
    //@FXML
    //private AnchorPane anchor;

    //private final static int rowsPerPage = 6;

    public void setService(StudentService service) {
        this.service=service;
        this.model= FXCollections.observableArrayList(service.getAllS());
        //studentTable.getItems().clear();
        //studentTable.getItems().addAll(model);
        studentTable.setItems(model);
    }
    public int itemsPerPage() {
        return 1;
    }

    public int rowsPerPage() {
        return (int)slider.getValue();
    }



    @Override
    public void notifyEvent(ListEvent<Student> e){
        model.setAll(StreamSupport.stream(e.getList().spliterator(),false).collect(Collectors.toList()));
    }

    public VBox createPage(int pageIndex) {
        if(model.size()%rowsPerPage()==0)
            pagination.setPageCount(model.size()/rowsPerPage());
        else
            pagination.setPageCount(model.size()/rowsPerPage()+1);
        int lastIndex = 0;
        int displace = model.size() % rowsPerPage();
        if (displace > 0) {
            lastIndex = model.size() / rowsPerPage();
        } else {
            lastIndex = model.size() / rowsPerPage();

        }

        VBox box = new VBox(5);
        int page = pageIndex * itemsPerPage();
        for(int i=page;i<page+itemsPerPage();i++){
            if (lastIndex == pageIndex) {
                studentTable.setItems(FXCollections.observableArrayList(model.subList(pageIndex * rowsPerPage(), pageIndex * rowsPerPage() + displace)));
            }
            else {
                studentTable.setItems(FXCollections.observableArrayList(model.subList(pageIndex * rowsPerPage(), pageIndex * rowsPerPage() + rowsPerPage())));
            }
            box.getChildren().add(studentTable);
        }
        return box;
    }

    @FXML
    public void handleSlider(){
        pagination.setPageFactory(this::createPage);
    }

        @FXML
    private void initialize() {
        // Initialize the student table with the three columns.
        id.setCellValueFactory(new PropertyValueFactory<Student, Integer>("idStudent"));
        name.setCellValueFactory(new PropertyValueFactory<Student, String>("nume"));
        email.setCellValueFactory(new PropertyValueFactory<Student, String>("email"));
        slider.setShowTickLabels(true);
        slider.setMin(1);
        slider.setMajorTickUnit(5);
        slider.setMax(25);
        slider.setValue(5);
        slider.valueProperty().addListener((obs, oldval, newVal) ->
                    pagination.setPageFactory(this::createPage));
        addButtonDeleteToTable();
        String iconPath = "if_Lapiz_52421.png";
        Image icon = new Image(getClass().getResourceAsStream(iconPath), 35, 35, false, false);
        ImageView img = new ImageView(icon);
        img.fitHeightProperty();
        img.fitWidthProperty();
        updatebtn.setGraphic(new ImageView(icon));
        String iconPath2 = "if_user_close_add_103747.png";
        Image icon2 = new Image(getClass().getResourceAsStream(iconPath2), 35, 35, false, false);
        ImageView img2 = new ImageView(icon2);
        img2.fitHeightProperty();
        img2.fitWidthProperty();
        addBtn.setGraphic(new ImageView(icon2));
        String iconPath3 = "if_filter_data_44818.png";
        Image icon3 = new Image(getClass().getResourceAsStream(iconPath3), 32, 32, false, false);
        ImageView img3 = new ImageView(icon3);
        img3.fitHeightProperty();
        img3.fitWidthProperty();
        filterBtn.setGraphic(new ImageView(icon3));
        String iconPath4="if_view-refresh_118801.png";
        Image icon4=new Image(getClass().getResourceAsStream(iconPath4),25,25,false,false);
        ImageView img4=new ImageView(icon4);
        img4.fitHeightProperty();
        img4.fitWidthProperty();
        refreshBtn.setGraphic(new ImageView(icon4));
        String iconPath5="if_22_Done_Circle_Complete_Downloaded_Added_2142698.png";
        Image icon5=new Image(getClass().getResourceAsStream(iconPath5),32,32,false,false);
        ImageView img5=new ImageView(icon5);
        img5.fitHeightProperty();
        img5.fitWidthProperty();
        doneBtn1.setGraphic(new ImageView(icon5));
        doneBtn2.setGraphic(new ImageView(icon5));
        doneBtn3.setGraphic(new ImageView(icon5));
        pagination.setPageFactory(this::createPage);
    }


    private void addButtonDeleteToTable() {

        Callback<TableColumn<Student, Void>, TableCell<Student, Void>> cellFactory = new Callback<TableColumn<Student, Void>, TableCell<Student, Void>>() {
            @Override
            public TableCell<Student, Void> call(final TableColumn<Student, Void> param) {
                final TableCell<Student, Void> cell = new TableCell<Student, Void>() {

                    private final Button btn = new Button("");

                    {
                        //btn.setStyle("-fx-text-fill:black;-fx-font-size: 14;-fx-background-color: red;-fx-border-color: black");

                        //btn.setOnAction(ctr::connectDelete);
                        btn.setStyle("-fx-min-height:20;-fx-min-width:20;-fx-max-height: 20; -fx-max-width: 20; -fx-background-size: 15px 15px; -fx-background-position: center 8px;-fx-border-color: #1d1d1d;-fx-background-position: center");
                        btn.setOnAction((ActionEvent event)-> {
                        Student data = studentTable.getItems().get(getIndex());
                        handleDeleteStudent(data);
                });
            }

            @Override
            public void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(btn);
                    //btn.setStyle("-fx-background-color: red");

                    String iconPath="icon2.png";
                    Image icon=new Image(getClass().getResourceAsStream(iconPath),20,20,false,false);
                    ImageView img=new ImageView(icon);
                    img.fitHeightProperty();
                    img.fitWidthProperty();
                    btn.setGraphic(new ImageView(icon));

                }
            }
        };
        return cell;
            }
        };

        delete.setCellFactory(cellFactory);

        //studentTable.getColumns().add(delete);

    }

    /*
    public void editableRows(){
        studentTable.setEditable(true);


        PseudoClass editableCssClass = PseudoClass.getPseudoClass("editable");

        Callback<TableColumn<Student, String>, TableCell<Student, String>> defaultTextFieldCellFactory
                = TextFieldTableCell.<Student>forTableColumn();

        name.setCellFactory(col -> {
            TableCell<Student, String> cell = defaultTextFieldCellFactory.call(col);
            cell.itemProperty().addListener((obs, oldValue, newValue) -> {
                TableRow row = cell.getTableRow();
                if (row == null) {
                    cell.setEditable(false);
                } else {
                    Student item = (Student) cell.getTableRow().getItem();

                    if (item == null) {
                        cell.setEditable(false);
                    } else {
                        cell.setEditable(true);
                        Student stud = (Student) cell.getTableRow().getItem();
                        handleEditStudent(stud);
                    }
                }
                cell.pseudoClassStateChanged(editableCssClass, cell.isEditable());
            });
            return cell ;
        });

        email.setCellFactory(col -> {
            TableCell<Student, String> cell = defaultTextFieldCellFactory.call(col);
            cell.itemProperty().addListener((obs, oldValue, newValue) -> {
                TableRow row = cell.getTableRow();
                if (row == null) {
                    cell.setEditable(false);
                } else {
                    Student item = (Student) cell.getTableRow().getItem();
                    if (item == null) {
                        cell.setEditable(false);
                    } else {
                        cell.setEditable(true);
                        Student stud = (Student) cell.getTableRow().getItem();
                        handleEditStudent(stud);
                    }
                }
                cell.pseudoClassStateChanged(editableCssClass, cell.isEditable());
            });
            return cell ;
        });
    }
    */
    public void clearFields() {
        nameField.clear();
        groupField.clear();
        idField.clear();
        //view.idField.setDisable(false);
        emailField.clear();
        teacherField.clear();
    }

    @FXML
    public void handleRefresh(){
        model.clear();
        model.setAll(FXCollections.observableArrayList(service.getAllS()));
        studentTable.setItems(model);
        pagination.setPageFactory(this::createPage);
        clearFields();
        idField.setDisable(false);
    }

    @FXML
    public void umpleFielduri(){
        Student stud=studentTable.getSelectionModel().getSelectedItem();
        if(stud!=null){
            idField.setDisable(true);
            idField.setText(Integer.toString(stud.getID()));
            nameField.setText(stud.getNume());
            groupField.setText(Integer.toString(stud.getGrupa()));
            emailField.setText(stud.getEmail());
            teacherField.setText(stud.getProfesor());
        }
    }
    @FXML
    public void handleSaveStudent() {
        try {
            service.addStudent(Integer.parseInt(idField.getText()), nameField.getText(), Integer.parseInt(groupField.getText()), emailField.getText(), teacherField.getText());

            pagination.setPageFactory(this::createPage);
            clearFields();
            MessageAlert.showInfoMessage(null, "Student added successfully!");
        } catch (ValidationException e) {
            MessageAlert.showErrorMessage(null,e.getMessage());
        } catch (RepositoryException e) {
            MessageAlert.showErrorMessage(null,e.getMessage());
        } catch (NumberFormatException e) {
            MessageAlert.showErrorMessage(null, "Invalid fields!");
        }
    }
    /*
    @FXML
    public void handleUpdateStudent()
    {
        Student student = studentTable.getSelectionModel().getSelectedItem();
    }
    */
    @FXML
    public void handleDeleteStudent(Student student){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("CONFIRMATION");
        alert.setContentText("Are you sure you want to delete this item?");
        ButtonType okButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType noButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(okButton, noButton);
        Optional<ButtonType> result = alert.showAndWait();
        if ((result.isPresent()) && (result.get() == okButton)) {
            service.deleteStudent(student.getIdStudent());
            pagination.setPageFactory(this::createPage);
            MessageAlert.showInfoMessage(null, "Student deleted successfully!");
        }
    }

    @FXML
    public void handleEditStudent(){
        try{
            Student student = new Student(Integer.parseInt(idField.getText()), nameField.getText(), Integer.parseInt(groupField.getText()), emailField.getText(), teacherField.getText());

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("CONFIRMATION");
            alert.setContentText("Are you sure you want to update this item?");
            ButtonType okButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
            ButtonType noButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(okButton, noButton);
            Optional<ButtonType> result = alert.showAndWait();
            if ((result.isPresent()) && (result.get() == okButton)) {
                service.updateStudent(student);
                //handleRefresh();
                pagination.setPageFactory(this::createPage);
                MessageAlert.showInfoMessage(null, "Student updated successfully!");
                clearFields();
            }
        }
        catch (ValidationException e){
            MessageAlert.showErrorMessage(null,e.getMessage());
        }
        catch (RepositoryException e){
            MessageAlert.showErrorMessage(null,e.getMessage());
        }
        catch (NumberFormatException e){
            MessageAlert.showErrorMessage(null,e.getMessage());
        }
    }


    @FXML
    public void handleFilterByName(){
        try{

            List list = service.filterStudentNameCtr(filterbynameField.getText(), "Cresc");
            model.clear();
            model.setAll(FXCollections.observableArrayList(list));
            studentTable.setItems(model);
            pagination.setPageFactory(this::createPage);
            filterbynameField.clear();
        }
        catch (ValidationException e){
            MessageAlert.showErrorMessage(null,e.getMessage());
        }
        catch (RepositoryException e){
            MessageAlert.showErrorMessage(null,e.getMessage());
        }
        catch (NumberFormatException e){
            MessageAlert.showErrorMessage(null,e.getMessage());
        }
    }

    @FXML
    public void handleFilterByGroup(){
        try{

            List list = service.filterStudentGroupCtr(Integer.parseInt(filterbygroupField.getText()), "Cresc");
            //studentTable.setItems(FXCollections.observableArrayList(list));
            model.clear();
            model.setAll(FXCollections.observableArrayList(list));
            studentTable.setItems(model);
            pagination.setPageFactory(this::createPage);
            filterbygroupField.clear();
        }
        catch (ValidationException e){
            MessageAlert.showErrorMessage(null,e.getMessage());
        }
        catch (RepositoryException e){
            MessageAlert.showErrorMessage(null,e.getMessage());
        }
        catch (NumberFormatException e){
            MessageAlert.showErrorMessage(null,e.getMessage());
        }
    }

    @FXML
    public void handleFilterByTeacher(){
        try{

            List list = service.filterStudentTeacherCtr(filterbyteacherField.getText(), "Cresc");
            //studentTable.setItems(FXCollections.observableArrayList(list));
            model.clear();
            model.setAll(FXCollections.observableArrayList(list));
            studentTable.setItems(model);
            pagination.setPageFactory(this::createPage);
            filterbyteacherField.clear();
        }
        catch (ValidationException e){
            MessageAlert.showErrorMessage(null,e.getMessage());
        }
        catch (RepositoryException e){
            MessageAlert.showErrorMessage(null,e.getMessage());
        }
        catch (NumberFormatException e){
            MessageAlert.showErrorMessage(null,e.getMessage());
        }
    }
}
