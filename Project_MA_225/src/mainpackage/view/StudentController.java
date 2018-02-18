package mainpackage.view;

import com.jfoenix.controls.*;
import com.jfoenix.controls.cells.editors.TextFieldEditorBuilder;
import com.jfoenix.controls.cells.editors.base.GenericEditableTreeTableCell;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import mainpackage.domain.Student;
import mainpackage.exceptions.MyException;
import mainpackage.service.Service;
import mainpackage.utils.ListEvent;
import mainpackage.utils.ListEventType;
import mainpackage.utils.Observer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StudentController implements Observer<Student> {
    private Service service;
    private ObservableList<Student> studentModel= FXCollections.observableArrayList();
    @FXML JFXTreeTableView studentTable;
    @FXML AnchorPane mainStudentPane;
    @FXML Label lastOperationLabel;
    JFXTreeTableColumn<Student, Integer> idCol;
    JFXTreeTableColumn<Student, String> nameCol;
    JFXTreeTableColumn<Student, String> groupCol;
    JFXTreeTableColumn<Student, String> emailCol;
    JFXTreeTableColumn<Student, String> teacherCol;
    Stage addStudentStage;

    @FXML JFXToggleButton numeToggle;
    @FXML JFXToggleButton profToggle;
    @FXML JFXToggleButton grupaToggle;
    @FXML JFXTextField filterNumeLabel;
    @FXML JFXTextField filterProfesorLabel;
    @FXML ComboBox<String> filterGrupa;
    @FXML AnchorPane BigAnchor;


    /*********************************/
    @FXML
    AnchorPane addStudentPane;
    @FXML JFXTextField idLabel;
    @FXML JFXTextField numeLabel;
    @FXML JFXTextField emailLabel;
    @FXML JFXTextField grupaLabel;
    @FXML JFXTextField teacherLabel;
    @FXML Label labelErrorAddStudent;
    @FXML Pagination studentPagination;
    Integer rowsPerPage = 20;

    public void initData(){
        this.studentModel.setAll(service.get_all_student());
        initStudentTable();
        lastOperationLabel.setText("Loaded");
        filterGrupa.getItems().addAll(service.getGroups());
        studentPagination.setPageFactory(this::createPage);
    }
    public StudentController()
    {
        System.out.println("IM IN CONSTRUCTOR");
    }

    private void initStudentTable() {
        idCol = new JFXTreeTableColumn<>("Id");
        idCol.setPrefWidth(90);
        idCol.setCellValueFactory( (TreeTableColumn.CellDataFeatures<Student, Integer> param) ->
        {
            if(idCol.validateValue(param))
                return new SimpleIntegerProperty(param.getValue().getValue().getId()).asObject();
            else
                return idCol.getComputedValue(param);
        });

        nameCol = new JFXTreeTableColumn<>("Nume");
        nameCol.setPrefWidth(160);
        nameCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<Student, String> param) ->{
            if(nameCol.validateValue(param))
                return new SimpleStringProperty(param.getValue().getValue().getName());
            else
                return nameCol.getComputedValue(param);
        });

        groupCol = new JFXTreeTableColumn<>("Grupa");
        groupCol.setPrefWidth(100);
        groupCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<Student, String> param) ->{
            if(groupCol.validateValue(param))
                return new SimpleStringProperty(param.getValue().getValue().getGroup());
            else
                return groupCol.getComputedValue(param);
        });

        emailCol = new JFXTreeTableColumn<>("Email");
        emailCol.setPrefWidth(145);
        emailCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<Student, String> param) ->{
            if(emailCol.validateValue(param))
                return new SimpleStringProperty(param.getValue().getValue().getEmail());
            else
                return emailCol.getComputedValue(param);
        });

        teacherCol = new JFXTreeTableColumn<>("Profesor");
        teacherCol.setPrefWidth(145);
        teacherCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<Student, String> param) ->{
            if(teacherCol.validateValue(param))
                return new SimpleStringProperty(param.getValue().getValue().getTeacher());
            else
                return teacherCol.getComputedValue(param);
        });


        nameCol.setCellFactory((TreeTableColumn<Student, String> param) -> new GenericEditableTreeTableCell<>(
                new TextFieldEditorBuilder()));
        nameCol.setOnEditCommit((TreeTableColumn.CellEditEvent<Student, String> t) ->
        {
            System.out.println("EDITING");
            System.out.println(t.getTreeTableView().getTreeItem(t.getTreeTablePosition().getRow()));
            Student tempStudent = t.getTreeTableView().getTreeItem(t.getTreeTablePosition().getRow()).getValue();
            tempStudent.setName(t.getNewValue());
            editValidationAction(tempStudent);
        });

        groupCol.setCellFactory((TreeTableColumn<Student, String> param) -> new GenericEditableTreeTableCell<>(
                new TextFieldEditorBuilder()));
        groupCol.setOnEditCommit((TreeTableColumn.CellEditEvent<Student, String> t) ->
        {
            System.out.println("EDITING GROUP");
            System.out.println(t.getTreeTableView().getTreeItem(t.getTreeTablePosition().getRow()));
            Student tempStudent = t.getTreeTableView().getTreeItem(t.getTreeTablePosition().getRow()).getValue();
            tempStudent.setGroup(t.getNewValue());
            editValidationAction(tempStudent);
        });

        emailCol.setCellFactory((TreeTableColumn<Student, String> param) -> new GenericEditableTreeTableCell<>(
                new TextFieldEditorBuilder()));
        emailCol.setOnEditCommit((TreeTableColumn.CellEditEvent<Student, String> t) ->
        {
            System.out.println("EDITING EMAIL");
            System.out.println(t.getTreeTableView().getTreeItem(t.getTreeTablePosition().getRow()));
            Student tempStudent = t.getTreeTableView().getTreeItem(t.getTreeTablePosition().getRow()).getValue();
            tempStudent.setEmail(t.getNewValue());
            editValidationAction(tempStudent);
        });

        teacherCol.setCellFactory((TreeTableColumn<Student, String> param) -> new GenericEditableTreeTableCell<>(
                new TextFieldEditorBuilder()));
        teacherCol.setOnEditCommit((TreeTableColumn.CellEditEvent<Student, String> t) ->
        {
            System.out.println("EDITING TEACHER");
            System.out.println(t.getTreeTableView().getTreeItem(t.getTreeTablePosition().getRow()));
            Student tempStudent = t.getTreeTableView().getTreeItem(t.getTreeTablePosition().getRow()).getValue();
            tempStudent.setTeacher(t.getNewValue());
            editValidationAction(tempStudent);
        });



        TreeItem<Student> root = new RecursiveTreeItem<>(studentModel, RecursiveTreeObject::getChildren);
        studentTable.setRoot(root);
        studentTable.setShowRoot(false);
        studentTable.setEditable(true);
        studentTable.getColumns().setAll(idCol, nameCol, groupCol, emailCol, teacherCol);
    }

    private Node createPage(int pageIndex) {

        int fromIndex = pageIndex * rowsPerPage;
        int toIndex = Math.min(fromIndex + rowsPerPage, service.get_all_student().size());
        List<Student> stlist = new ArrayList<>();
        stlist.addAll(service.get_all_student());
        try{
            this.studentModel.setAll(stlist.subList(fromIndex, toIndex));
//            this.studentModel.setAll(stlist.subList(fromIndex, toIndex));
        }catch (Exception ex)
        {
            studentPagination.setPageCount(0);
        }
        return new BorderPane(studentTable);
    }

    private Boolean editValidationAction(Student tempStudent) {
        try {
            service.update_student(tempStudent);
            lastOperationLabel.setText("Updated");
            lastOperationLabel.setStyle("-fx-text-fill: green");
            return true;
        }
        catch(MyException ex)
        {
            System.out.println("Error editing");
            lastOperationLabel.setText(ex.getMessage());
            lastOperationLabel.setStyle("-fx-text-fill: red");
            notifyEvent(null);
            return false;
            //service.notifyObservers(null); //fix this shit
        }
    }

    public ObservableList<Student> getModel() {
        return studentModel;
    }

    @Override
    public void notifyEvent(ListEvent<Student> e) {
        //TreeItem<Student> root = new RecursiveTreeItem<>(studentModel, RecursiveTreeObject::getChildren);
        //studentTable.setRoot(root);
        System.out.println("UPDATING MODEL");
        studentModel.setAll(service.filter_students(null,null,null));
//        System.out.println(service.get_all_student());
//        TreeItem<Student> root = new RecursiveTreeItem<>(studentModel, RecursiveTreeObject::getChildren);
//        studentTable.setRoot(root);
//        studentTable.refresh();
    }

    public Node getWindow() {
        return mainStudentPane;
    }

    public void setService(Service service) {
        if(service == null)
            System.out.println("Service is null in setService");
        else
            System.out.println(service);
        this.service = service;
    }

    public void handleAddStudent(ActionEvent actionEvent) {
        System.out.println("Show Add Student Menu");
        FXMLLoader addStudent = new FXMLLoader();
        addStudent.setLocation(getClass().getResource("/mainpackage/view/AddStudent.fxml"));
        try {
            Parent root = addStudent.load();
            StudentController ctr = addStudent.getController();
            ctr.setService(this.service);
            addStudentStage = new Stage();

            addStudentStage.initStyle(StageStyle.UNDECORATED);
            addStudentStage.setScene(new Scene(root));
            addStudentStage.show();
            lastOperationLabel.setText("");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleDeleteStudent(ActionEvent actionEvent) {
        TreeItem<Student> st = (TreeItem<Student>) studentTable.getSelectionModel().getSelectedItem();
        if(st == null)
            return;
        Student selected = st.getValue();
        try {
            service.delete_student(selected.getId());
            lastOperationLabel.setText("Student deleted.");
            lastOperationLabel.setStyle("-fx-text-fill: green");
        } catch (MyException e) {
            lastOperationLabel.setText(e.getMessage());
            lastOperationLabel.setStyle("-fx-text-fill: red");
        }
    }

    private Student getStudentFromView()
    {
        String id = idLabel.getText().trim();
        String name = numeLabel.getText();
        String teacher = teacherLabel.getText();
        String email = emailLabel.getText();
        String group = grupaLabel.getText();
        Integer intid;
        try {
            intid = Integer.parseInt(id);
        }catch(Exception e)
        {
            intid = 0;
        }
        return new Student(name, intid, email, teacher, group);
    }

    public void handleAdd(ActionEvent actionEvent) {
        System.out.println("Adding Student");
        labelErrorAddStudent.setText("");
        try {
            Student st = getStudentFromView();
            System.out.println(st);
            if(this.service == null)
                System.out.println("Service is null");
            this.service.add_student(st);
            labelErrorAddStudent.setText("Student Adaugat");
            labelErrorAddStudent.setStyle("-fx-text-fill: green");

        } catch (MyException e) {
            labelErrorAddStudent.setText(e.getMessage());
            labelErrorAddStudent.setStyle("-fx-text-fill: red");
        }
    }

    public void handleX(MouseEvent mouseEvent) {
        Node  source = (Node)  mouseEvent.getSource();
        Stage stage  = (Stage) source.getScene().getWindow();
        stage.close();
    }

    public void handleFilter(ActionEvent actionEvent) {
        String nume=null, prof=null, grupa=null;
        System.out.println("Im filtering.");
        if(numeToggle.selectedProperty().get() && !filterNumeLabel.getText().equals(""))
        {
            nume = filterNumeLabel.getText();
        }
        if(profToggle.selectedProperty().get() && !filterProfesorLabel.getText().equals(""))
        {
            prof = filterProfesorLabel.getText();
        }
        if(grupaToggle.selectedProperty().get())
        {
            grupa = filterGrupa.getValue();
        }
        studentModel.setAll(service.filter_students(nume, prof, grupa));
        lastOperationLabel.setText("Filtered");
        lastOperationLabel.setStyle("-fx-text-fill: green");
    }

    public void filterchange(KeyEvent keyEvent) {
        handleFilter(null);
    }
}
