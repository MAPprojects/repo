package Controller;

import Domain.Grade;
import Domain.HasId;
import Domain.LabHomework;
import Domain.Student;

import Repository.GradeFileRepository;
import Repository.LabHomeworkFileRepository;
import Repository.RepositoryException;
import Repository.StudentFileRepository;
import Service.ServiceManager;
import Service.ServiceManagerObservable;

import Utils.*;
import Utils.Observer;
import Validator.GradeValidator;
import Validator.HomeworkValidator;
import Validator.StudentValidator;
import Validator.ValidatorException;
import javafx.beans.InvalidationListener;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import javafx.util.Pair;



import javax.print.DocFlavor;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class ControllerFXML implements Observer<Event> {

    private ServiceManagerObservable smo;

    private ControllerFXMLAddGrades ctrAddGrades;
    private ObservableList<Student> studentModel = FXCollections.observableArrayList();
    private ObservableList<LabHomework> homeworkModel = FXCollections.observableArrayList();
    private ObservableList<Grade> gradeModel = FXCollections.observableArrayList();
    @FXML
    private Tab tabGrades=new Tab();
    public ObservableList<Student> getStudentModel() {
        return studentModel;
    }

    public TableView<Student> getTableViewStudents() {
        return tableViewStudents;
    }

    @FXML
    private ListView<Reports> listViewReport=new ListView<>();

    @FXML
    private TextArea textArea;
    @FXML
    private Button buttonGenerateReport;
    @FXML
    private ListView<LabHomework> listViewHomework;
    @FXML
    private TextField textFieldId;
    @FXML
    private TextField textFieldName;
    @FXML
    private TextField textFieldEmail;
    @FXML
    private TextField textFieldGroup;
    @FXML
    private TextField textFieldProfessor;
    @FXML
    private TextField textFieldStudentFind;
    @FXML
    private TableView<Student> tableViewStudents;
    @FXML
    private Button buttonAddStudent;
    @FXML
    private Button buttonDeleteStudent;
    @FXML
    private Button buttonSendEmailStudent;
    @FXML
    private Button buttonAddHomework;
    @FXML
    private Button buttonUpdateHomework;
    @FXML
    private TabPane tabPaneAll;
    @FXML
    private ToggleGroup groupFilterStudents;
    @FXML
    private RadioButton radioButtonFilterStudent1;
    @FXML
    private RadioButton radioButtonFilterStudent2;
    @FXML
    private RadioButton radioButtonFilterStudent3;
    @FXML
    private TextField textFieldFilterStudent;
    @FXML
    private TableView<Grade> tableViewGrades;
    @FXML
    private TextField textFieldIdHw;
    @FXML
    private TextField textFieldHwFilter2;

    @FXML
    private TextField textFieldHwFilter3;
    @FXML
    private TextField textFieldDeadline;
    @FXML
    private TextField textFieldDescription;
    @FXML
    private Label labelCurrentWeek;
    @FXML
    private CheckBox checkBoxFilter1Hw;
    @FXML
    private CheckBox checkBoxFilter2Hw;

    @FXML
    private CheckBox checkBoxFilter3Hw;
    @FXML
    private ComboBox<String> comboBoxStudents;

    @FXML
    private ComboBox<String> comboBoxHw;
    @FXML
    private Slider sliderGrades = new Slider();
    @FXML
    private Label labelGradeValue;
    @FXML
    private Button buttonAddGrade;
    @FXML
    private Button buttonUpdateGrade;
    @FXML
    private CheckBox checkBoxFilter1Grades;
    @FXML
    private CheckBox checkBoxFilter2Grades;
    @FXML
    private CheckBox checkBoxFilter3Grades;

    //controls for inprogress window

    private ControllerFXMLinprogress ctrInprogres;



    private Task<Void> runningTask;



    public void updateStudentModel(Student newS,EvType type) {
        switch (type)
        {
            case ADD: //adaugare
                studentModel.add(newS);
                break;
            case REMOVE: //stergere
                studentModel.remove(studentModel.indexOf(newS));
                break;
            case UPDATE://update
                int position=0;
                for (Student s:studentModel
                     ) {
                    if(s.getId()== newS.getId())
                        break;
                    position++;
                }
                studentModel.set(position,newS);
                break;
        }
    }

    public void updateHomeworkModel(LabHomework newH,EvType type) {
        switch (type)
        {
            case ADD: //adaugare
                homeworkModel.add(newH);
                break;
            case REMOVE://stergere
                homeworkModel.remove(homeworkModel.indexOf(newH));
                break;
            case UPDATE://update
                int position=0;
                for (LabHomework s:homeworkModel
                        ) {
                    if(s.getId()== newH.getId())
                        break;
                    position++;
                }
                homeworkModel.set(homeworkModel.indexOf(position),newH);
                break;
        }
    }

    public void updateGradeModel(Grade newG,EvType type) {
        switch (type)
        {
            case ADD: //adaugare
                break;
            case REMOVE://stergere
                break;
            case UPDATE://update
                break;
        }
    }

    private ObservableList<Student> studentModelReport=FXCollections.observableArrayList();

    public ControllerFXML() {
    }
    private int rowsPerPageStudents;
    private int rowsPerPageGrades;


    private Node createPageStudents(int pageIndex) {

        int fromIndex = pageIndex * rowsPerPageStudents;
        int toIndex = Math.min(fromIndex + rowsPerPageStudents, studentModel.size());
        if(fromIndex<toIndex)
            tableViewStudents.setItems(FXCollections.observableArrayList(studentModel.subList(fromIndex, toIndex)));
            return new Pane(tableViewStudents);
    }

    private Node createPageGrades(int pageIndex) {
        int fromIndex = pageIndex * rowsPerPageGrades;
        int toIndex = Math.min(fromIndex + rowsPerPageGrades, getServiceManagerObs().getSizeGrades());
        tableViewGrades.setItems(FXCollections.observableArrayList(gradeModel.subList(fromIndex, toIndex)));
        return new Pane(tableViewGrades);
    }

    @FXML
    private AnchorPane anchorPaneTableViewStudents;


    @FXML
    private AnchorPane anchorPaneTableViewGrades;

    private Pagination paginationStudents=new Pagination();

    private Pagination paginationGrades=new Pagination();

    private void verifyPaginationUpdateConditions(EvType ev,Object o)
    {
        if(o instanceof Student)
        {
            switch (ev) {
                case ADD:
                    if(studentModel.size()%rowsPerPageStudents==1)
                        setPaginationStudents(rowsPerPageStudents);
                    break;
                case REMOVE:
                    if(studentModel.size()%rowsPerPageStudents==0)
                        setPaginationStudents(rowsPerPageStudents);
                    break;
            }
        }
        else if(o instanceof Grade)
        {
            switch (ev) {
                case ADD:
                    if(gradeModel.size()%rowsPerPageGrades==1)
                        setPaginationStudents(rowsPerPageGrades);
                    break;
                case REMOVE:
                    if(gradeModel.size()%rowsPerPageGrades==0)
                        setPaginationStudents(rowsPerPageGrades);
                    break;
            }

        }
    }

    private void setPaginationGrades(int rowspp)
    {
        rowsPerPageGrades=rowspp;
        paginationGrades=new Pagination(gradeModel.size()/rowsPerPageGrades+(gradeModel.size()%rowsPerPageGrades>0?1:0),0);
        paginationGrades.setPageFactory(this::createPageGrades);
        anchorPaneTableViewGrades.getChildren().add(paginationGrades);
    }

    private void setPaginationStudents(int rowspp) {
        rowsPerPageStudents=rowspp;
        paginationStudents=new Pagination(studentModel.size()/rowsPerPageStudents+(studentModel.size()%rowsPerPageStudents>0?1:0),0);
        paginationStudents.setPageFactory(this::createPageStudents);
        anchorPaneTableViewStudents.getChildren().add(paginationStudents);
    }



    public ServiceManagerObservable getServiceManagerObs() {
        return smo;
    }


    public void setServiceManagerObs(ServiceManagerObservable smo) {

        System.out.println("am setat");
        this.smo = smo;
        smo.getAllStudents().forEach(x -> studentModel.add(x));
        tableViewStudents.setItems(studentModel);
        smo.getAllHomework().forEach(x -> homeworkModel.add(x));
        listViewHomework.setItems(homeworkModel);
        labelCurrentWeek.setText("Current week:" + smo.getCurrentWeek());
        smo.getAllGrades().forEach(x -> gradeModel.add(x));
        tableViewGrades.setItems(gradeModel);

        setPaginationStudents(15);
        setPaginationGrades(10);  //anchorPaneTableViewGrades.getChildren().add(paginationGrades);


        //TableColumn<Grade,String> columnStudentName=new TableColumn<>("Student");
        //tableViewGrades.getColumns().add(columnStudentName);
        //columnStudentName.setCellValueFactory(c->new SimpleStringProperty());

        /*for(int i=0;i<gradeModel.size();i++)
        {
            Grade gr=gradeModel.get(i);
            System.out.println(gr.getIdStudent()+" ");

            //ObservableValue<String> names=new SimpleStringProperty(nameS);

            columnStudentName.cellFactoryProperty().setValue(new Callback<TableColumn<Grade, String>, TableCell<Grade, String>>() {
                @Override
                public TableCell<Grade, String> call(TableColumn<Grade, String> param) {
                    String nameS=smo.findOneStudent(gr.getIdStudent()).getName();
                    TableCell<Grade,String> cell=new TableCell<>();
                    cell.commitEdit(nameS);
                    cell.setUserData(nameS);
                    System.out.println(cell.getUserData());
                    return cell;
                }1
            });
        }*/
        //undoListHw.add(homeworkModel);
        sliderGrades.setMin(1);
        sliderGrades.setMax(10);
        sliderGrades.setValue(1);
        labelGradeValue.textProperty().bind(
                Bindings.format(
                        "%.0f",
                        sliderGrades.valueProperty()
                )
        );

    }

    private ObservableList<LabHomework> workingListHw=FXCollections.observableArrayList();
    private ObservableList<Grade> workingListGrades=FXCollections.observableArrayList();
    @FXML
    private TableView<Student> tableViewReport;
    @FXML
    public void initialize() {
        tabPaneAll.setOpacity(50);
        sliderGrades.setBlockIncrement(1);
        sliderGrades.setValue(1);
        radioButtonFilterStudent1.setId("radioButton1");
        radioButtonFilterStudent1.setId("radioButton2");
        radioButtonFilterStudent1.setId("radioButton3");
        radioButtonFilterStudent1.setToggleGroup(groupFilterStudents);
        radioButtonFilterStudent2.setToggleGroup(groupFilterStudents);
        radioButtonFilterStudent3.setToggleGroup(groupFilterStudents);



        listViewReport.setItems(FXCollections.observableArrayList(Reports.values()));
        listViewReport.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Reports>() {
            @Override
            public void changed(ObservableValue<? extends Reports> observable, Reports oldValue, Reports newValue) {
                studentModelReport=smo.generateListForReport(newValue);
                tableViewReport.setItems(studentModelReport);
            }
        });

        tableViewStudents.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> showStudents(newValue)));
        listViewHomework.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> showHomework(newValue)));






        /*Callback<TableColumn, TableCell> cellFactory = new Callback<TableColumn, TableCell>() {
            @Override
            public TableCell call(TableColumn p) {
                TableCell editableCell=new TableCell<>();
                return editableCell;
            }
        };*/


        tabGrades.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                ObservableList<String> comboModelSts = FXCollections.observableArrayList(), comboModelHw = FXCollections.observableArrayList();
                smo.getAllStudents().forEach(x -> comboModelSts.add(x.getId() + " " + x.getName()));
                smo.getAllHomework().forEach(x -> comboModelHw.add(x.getId() + " Deadline:" + x.getDeadline()));
                comboBoxStudents.setItems(comboModelSts);
                comboBoxHw.setItems(comboModelHw);
                comboBoxStudents.setItems(comboModelSts);
            }
        });

        textFieldStudentFind.textProperty().addListener(((observable, oldValue, newValue) ->
        {
            if(textFieldStudentFind.getText().compareTo("")==0)
                tableViewStudents.setItems(studentModel);
            else
            {
                ObservableList<Student> newModel = FXCollections.observableArrayList(smo.filterStudentsContaining(textFieldStudentFind.getText()));
                tableViewStudents.setItems(newModel);
                 }
        }));

        /*TableColumn<Grade,Integer> column=new TableColumn<>();
        tableViewGrades.getColumns().add(column);
        column.setCellFactory(new Callback<TableColumn<Grade, Integer>, TableCell<Grade, Integer>>() {
            @Override
            public TableCell<Grade, Integer> call(TableColumn<Grade,Integer> param) {
                System.out.println("safdgrht");
                return null;
            }
        });*/
        checkBoxFilter1Hw.selectedProperty().addListener(((observable, oldValue, newValue) ->
        {
            if(!checkBoxFilter1Hw.isSelected())
            {
                workingListHw=homeworkModel;

            }
            else {
                workingListHw=listViewHomework.getItems();
            }
        }));
        checkBoxFilter2Hw.selectedProperty().addListener(((observable, oldValue, newValue) ->
        {
            if(!checkBoxFilter2Hw.isSelected()) {
                workingListHw = homeworkModel;
            }
            else {
                workingListHw=listViewHomework.getItems();
            }

        }));
        checkBoxFilter3Hw.selectedProperty().addListener(((observable, oldValue, newValue) ->
        {
            if(!checkBoxFilter3Hw.isSelected())
            {
                workingListHw=homeworkModel;
            }
            else
            {
                workingListHw=listViewHomework.getItems();
            }
        }));

        checkBoxFilter1Grades.selectedProperty().addListener(((observable, oldValue, newValue) ->
        {
            System.out.println("listener");
            if(!checkBoxFilter1Grades.isSelected())
            {
                workingListGrades=gradeModel;
            }
            else {
                workingListGrades=tableViewGrades.getItems();
            }
        }));
        checkBoxFilter2Grades.selectedProperty().addListener(((observable, oldValue, newValue) ->
        {
            if(!checkBoxFilter2Grades.isSelected()) {
                workingListGrades = gradeModel;
            }
            else {
                workingListGrades=tableViewGrades.getItems();
            }

        }));
        checkBoxFilter3Grades.selectedProperty().addListener(((observable, oldValue, newValue) ->
        {
            if(!checkBoxFilter3Grades.isSelected())
            {
                workingListGrades=gradeModel;
            }
            else
            {
                workingListGrades=tableViewGrades.getItems();
            }
        }));
        tableViewStudents.itemsProperty().addListener(new ChangeListener<ObservableList<Student>>() {
            @Override
            public void changed(ObservableValue<? extends ObservableList<Student>> observable, ObservableList<Student> oldValue, ObservableList<Student> newValue) {

            }
        });
        tableViewStudents.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if( event.getClickCount() == 2 && tableViewStudents.getSelectionModel().getSelectedIndex()>=0 ) {
                    //dublu click
                    ungradedHomework=smo.filterAllUngradedHomeworkForStudent(tableViewStudents.getSelectionModel().getSelectedItem().getId());
                    if(ungradedHomework.size()>0)
                        initalizeControllerAddGrade();
                    else
                    {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setContentText("There is no ungraded homework for this student!");
                        alert.showAndWait();
                    }
                }
            }});
    }



    @FXML
    private void handlerHomeworkFilters(ActionEvent ev) {
        boolean ok=false;
        if (checkBoxFilter1Hw.isSelected()) {
            ok=true;
            workingListHw= FXCollections.observableArrayList(smo.filterHomeworkStillInProgress(workingListHw));
            listViewHomework.setItems(workingListHw);

        }
        if (checkBoxFilter2Hw.isSelected()) {

            ok=true;
            workingListHw= FXCollections.observableArrayList(smo.filterHomeworkDescriptionContainsGivenString(workingListHw,textFieldHwFilter2.getText()));
            listViewHomework.setItems(workingListHw);

        }
        if (checkBoxFilter3Hw.isSelected()) {
            try {
                workingListHw= FXCollections.observableArrayList(smo.filterHomeworkHavingGivenDeadline(workingListHw,Integer.parseInt(textFieldHwFilter3.getText())));
                ok=true;
                listViewHomework.setItems(workingListHw);
            }
            catch (NumberFormatException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("You must introduce a number in the field!");
                alert.showAndWait();
                checkBoxFilter3Hw.setSelected(false);
            }
        }
        if(ok==false)
            listViewHomework.setItems(homeworkModel);

    }


    @FXML
    private void handlerGradesFilters(ActionEvent ev) {
        boolean ok=false;
        if (checkBoxFilter1Grades.isSelected()) {
            ok=true;
            workingListGrades=FXCollections.observableArrayList(smo.filterGradesHavingSameValue(workingListGrades,Integer.parseInt(labelGradeValue.getText())));
            tableViewGrades.setItems(workingListGrades);
        }
        if (checkBoxFilter2Grades.isSelected()) {
            String info=comboBoxStudents.getSelectionModel().getSelectedItem();
            if(info!=null) {
                ok=true;
                workingListGrades=FXCollections.observableArrayList(smo.filterGradesHavingSameStudent(workingListGrades,Integer.parseInt(info.substring(0, info.indexOf(' ')))));
                tableViewGrades.setItems(workingListGrades);
            }
            else
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("You must select a student from the comboBox!");
                alert.showAndWait();
                checkBoxFilter2Grades.setSelected(false);
            }
        }
        if (checkBoxFilter3Grades.isSelected()) {
            String info=comboBoxHw.getSelectionModel().getSelectedItem();
            if(info!=null)
            {
                workingListGrades=FXCollections.observableArrayList(smo.filterGradesHavingSameHomework(workingListGrades,Integer.parseInt(info.substring(0, info.indexOf(' ')))));
                tableViewGrades.setItems(workingListGrades);
                ok=true;
            }
            else
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("You must select a homework from the comboBox!");
                alert.showAndWait();
                checkBoxFilter3Grades.setSelected(false);
            }
            }
        if(ok==false)
            tableViewGrades.setItems(gradeModel);
    }

    @FXML
    private void showHomework(LabHomework hw) {
        if (hw == null)
            clearHomeworkTextFields();
        else {
            //System.out.println(""+hw.getId()+hw.getDeadline()+hw.getDescription());
            textFieldDeadline.setText("" + hw.getDeadline());
            textFieldIdHw.setText("" + hw.getId());
            textFieldDescription.setText("" + hw.getDescription());

        }
    }

    @FXML
    private void showStudents(Student st) {
        if (st == null)
            clearStudentTextFields();
        else {
            textFieldId.setText("" + st.getId());
            textFieldName.setText("" + st.getName());
            textFieldEmail.setText("" + st.getEmail());
            textFieldGroup.setText("" + st.getGroup());
            textFieldProfessor.setText("" + st.getProfessor());
        }
    }


    private void clearStudentTextFields() {
        textFieldId.clear();
        textFieldName.clear();
        textFieldEmail.clear();
        textFieldGroup.clear();
        textFieldProfessor.clear();
    }


    private void clearHomeworkTextFields() {
        textFieldIdHw.clear();
        textFieldDeadline.clear();
        textFieldDescription.clear();
    }


    private void refreshTabelAndModel(Object o,EvType type)
    {
        if(o instanceof Student)
        {
            tableViewStudents.setItems(studentModel);
            //tableViewStudents.refresh();
        }
        else if(o instanceof Grade)
        {
            tableViewGrades.setItems(gradeModel);
           // tableViewStudents.refresh();
        }
    }

    @FXML
    public void handlerCloseRequest() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Bro,did you really want to leave?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();
        if (alert.getResult().equals(ButtonType.YES)) {
            Stage stage = (Stage) tabPaneAll.getScene().getWindow();
            alert.close();
            stage.close();
        }
        tabPaneAll.getSelectionModel().clearAndSelect(tabPaneAll.getSelectionModel().getSelectedIndex() - 1);
    }

    @FXML
    public void handlerAddStudentEvent(ActionEvent event) {
        try {
            smo.addStudent(Integer.parseInt(textFieldId.getText().trim()), textFieldName.getText().trim(), textFieldEmail.getText().trim(), textFieldGroup.getText().trim(), textFieldProfessor.getText().trim());
            refreshTabelAndModel(new Student(Integer.parseInt(textFieldId.getText().trim()), textFieldName.getText().trim(), textFieldEmail.getText().trim(), textFieldGroup.getText().trim(), textFieldProfessor.getText().trim())
            ,EvType.ADD);
            verifyPaginationUpdateConditions(EvType.ADD,new Student(1,"d","f","g","g"));
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Student " + textFieldName.getText() + " has been succesufully added!");
            alert.showAndWait();
            //clearStudentTextFields();
        } catch (ValidatorException | RepositoryException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(e.getMessage().toString());
            alert.showAndWait();
        } catch (NumberFormatException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Id must be a valid positive number!");
            alert.showAndWait();
        }
    }

    @FXML
    public void handlerDeleteStudentEvent(ActionEvent event) {

        Student st = tableViewStudents.getSelectionModel().getSelectedItem();
        if (st != null) {
            int id=st.getId();
            refreshTabelAndModel(st,EvType.REMOVE);
            smo.deleteStudent(id);
            deleteAllGradesForStudent(id);
            verifyPaginationUpdateConditions(EvType.REMOVE,new Student(1,"1","df","j","h"));
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Student has been succesufully deleted!");
            alert.showAndWait();
            clearStudentTextFields();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("You must select the student you want to delete from the table!");
            alert.showAndWait();
        }

    }

    public void deleteAllGradesForStudent(int id)
    {
        smo.getAllHomework().forEach(
            x->
            {
                Pair<Integer,Integer> idd=new Pair<>(id,x.getId());
                if(smo.findOneGrade(idd)!=null)
                    smo.deleteGrade(idd);
            }
        );
        refreshTabelAndModel(new Grade(1,1,1,"ion"),EvType.REMOVE);
    }

    @FXML
    public void handlerUpdateStudentEvent(ActionEvent event) {
        if (tableViewStudents.getSelectionModel().getSelectedItem() != null) {
            try {
                smo.updateStudent(Integer.parseInt(textFieldId.getText()), textFieldName.getText(), textFieldEmail.getText(), textFieldGroup.getText(), textFieldProfessor.getText());
                refreshTabelAndModel(new Student(Integer.parseInt(textFieldId.getText()), textFieldName.getText(), textFieldEmail.getText(), textFieldGroup.getText(), textFieldProfessor.getText()),EvType.UPDATE);
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setContentText("Student with id" + textFieldId.getText() + " has been succesufully updated.");
                alert.showAndWait();
                clearStudentTextFields();
            } catch (ValidatorException | RepositoryException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText(e.getMessage().toString());
                alert.showAndWait();
            } catch (NumberFormatException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Id must be a positive number");
                alert.showAndWait();
            }
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("You must select the student you want to delete from the table!");
            alert.showAndWait();
        }
    }

    @FXML
    public void handlerFilterStudents1() {
        List<Student> sts = smo.filterStudentsStartingWith(textFieldFilterStudent.getText());
        if (sts.size() == 0)
            handlerUnsuccesufulFilter();
        else
            handlerSuccesufulFilter(sts);
    }

    @FXML
    public void handlerFilterStudents2() {
        List<Student> sts = smo.filterStudentsHavingSameGroupAlphabetically(textFieldFilterStudent.getText());
        if (sts.size() == 0)
            handlerUnsuccesufulFilter();
        else
            handlerSuccesufulFilter(sts);
    }

    @FXML
    public void handlerFilterStudents3() {
        List<Student> sts = smo.filterStudentsHavingSameProfessorOrderedByName(textFieldFilterStudent.getText().toString());
        if (sts.size() == 0)
            handlerUnsuccesufulFilter();
        else
            handlerSuccesufulFilter(sts);
    }

    private void handlerUnsuccesufulFilter() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText("The introduced professor doesn't exist!Retry.");
        alert.showAndWait();
        groupFilterStudents.getSelectedToggle().setSelected(false);
    }


    private void handlerSuccesufulFilter(List<Student> sts) {
        studentModel.clear();
        sts.forEach(x -> studentModel.add(x));
        tableViewStudents.setItems(studentModel);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Filter executed succesufully!");
        alert.showAndWait();
        //groupFilterStudents.getSelectedToggle().setSelected(false);
    }


    @FXML
    private void handlerAddHomeworkEvent(ActionEvent event) {
        try {
            smo.addHomework(Integer.parseInt(textFieldIdHw.getText().toString()), Integer.parseInt(textFieldDeadline.getText().toString()), textFieldDescription.getText().toString());
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Homework succesufully added!");
            alert.showAndWait();
            clearHomeworkTextFields();
        } catch (ValidatorException | RepositoryException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(e.getMessage().toString());
            alert.showAndWait();
        } catch (NumberFormatException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Id and deadline must be valid integers!");
            alert.showAndWait();
        }
    }

    @FXML
    private void handlerModifyDeadlineHomeworkEvent(ActionEvent event) {
        try {
            int id=Integer.parseInt(textFieldIdHw.getText());
            smo.extendDeadline(id, Integer.parseInt(textFieldDeadline.getText()));
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Deadline succesufully updated!");
            alert.showAndWait();
        } catch (ValidatorException | RepositoryException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(ex.getMessage().toString());
            alert.showAndWait();
        } catch (NumberFormatException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("You must select one homework from the list.");
            alert.showAndWait();
        }
    }


    private List<LabHomework> ungradedHomework=FXCollections.observableArrayList();

    private Stage initalizeControllerAddGrade()
    {
        FXMLLoader loader2=new FXMLLoader();
        System.out.println("0");
        loader2.setLocation(getClass().getResource("/Controller/addgrades.fxml"));
        System.out.println("1");
        try {
            int idS=Integer.parseInt(textFieldId.getText());
            loader2.load();
            Pane pane=(Pane)loader2.getRoot();
            ctrAddGrades=loader2.getController();
            ctrAddGrades.setModel(FXCollections.observableArrayList(ungradedHomework));
            System.out.println("2");
            Stage stage=new Stage();
            stage.setScene(new Scene(pane));
            stage.initModality(Modality.APPLICATION_MODAL);
            System.out.println("3");
            stage.show();
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    //ctrAddGrades.setNewGrades();
                    System.out.println("in handler");
                    int week=(int)smo.getCurrentWeek();
                    ctrAddGrades.getNewGrades().forEach(
                            x -> {
                                try {
                                    smo.addGrade(Integer.parseInt(textFieldId.getText()),x.getIdHomework(),x.getValue(),week,"","");
                                    System.out.println("dfgfd");
                                } catch (ValidatorException |RepositoryException e) {
                                }
                            }
                    );
                    refreshTabelAndModel(new Grade(1,1,1),EvType.ADD);
                }
            });
            return stage;
            }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    private Stage initalizeControllerInprogress()
    {
        FXMLLoader loader2=new FXMLLoader();
        System.out.println("0");
        loader2.setLocation(getClass().getResource("/Controller/inprogress.fxml"));
        System.out.println("1");
        try {
            loader2.load();
            ctrInprogres=loader2.getController();
            ctrInprogres.setLabelInProgressTask("This will take a while!:)");
            Pane pane=(Pane)loader2.getRoot();
            System.out.println("2");
            Stage stage=new Stage();
            stage.setScene(new Scene(pane));
            System.out.println("3");
            stage.show();
            return stage;
            //System.out.println("dfxgc");
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @FXML
    private void handlerAddGrade(ActionEvent ev) {
        String sts = comboBoxStudents.getSelectionModel().getSelectedItem();
        if (sts != null) {
            String idSt = sts.substring(0, sts.indexOf(' '));
            String hw = comboBoxHw.getSelectionModel().getSelectedItem();
            if (hw != null) {
                String idHw = hw.substring(0, hw.indexOf(' '));

                if (smo.findOneGrade(new Pair<>(Integer.parseInt(idSt), Integer.parseInt(idHw))) == null)
                {
                    Stage littleStage = initalizeControllerInprogress();
                    runningTask = new Task<Void>() {
                        @Override
                        public Void call() {
                            try {
                                updateMessage("This will take a time!");
                                int idStudent = Integer.parseInt(idSt);
                                int idHomework = Integer.parseInt(idHw);
                                smo.addGrade(idStudent, idHomework, Integer.parseInt(labelGradeValue.getText()), (int) smo.getCurrentWeek(), textArea.getText(), "Adaugare Nota");
                                refreshTabelAndModel(smo.findOneGrade(new Pair<>(idStudent, idHomework)), EvType.ADD);
                                updateProgress(1,1);
                                updateMessage("Done!");
                            } catch (ValidatorException | RepositoryException ex) {
                                //updateProgress(0,1);
                                //updateMessage("Cancelled!");
                                }
                            return null;
                        }

                        @Override
                        protected void succeeded() {
                            super.succeeded();
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.setContentText("Grade succesufully assigned!");
                            alert.showAndWait();
                            ctrInprogres.unbindProgressBar();
                        }

                    };
                    ctrInprogres.bindComponents(runningTask);
                    System.out.println("3");

                    Thread thread = new Thread(runningTask);
                    thread.setDaemon(true);
                    thread.start();
                }
                else
                {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Duplicated key!");
                    alert.showAndWait();
                }
            }

            else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("You must select one homework from the comboBox.");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("You must select one student from the comboBox.");
            alert.showAndWait();
        }
    }

    @FXML
    private void handlerUpdateGrade(ActionEvent ev) {
        String sts = comboBoxStudents.getSelectionModel().getSelectedItem();
        if (sts != null) {
            String idSt = sts.substring(0, sts.indexOf(' '));
            String hw = comboBoxHw.getSelectionModel().getSelectedItem();
            if (hw != null) {
                String idHw = hw.substring(0, hw.indexOf(' '));
                try {
                    int idStudent=Integer.parseInt(idSt);
                    int idHomework=Integer.parseInt(idHw);
                    smo.updateGrade(idStudent, idHomework, Integer.parseInt(labelGradeValue.getText()), (int) smo.getCurrentWeek());
                    refreshTabelAndModel(smo.findOneGrade(new Pair<>(idStudent,idHomework)),EvType.UPDATE);
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setContentText("Grade succesufully updated.");
                    alert.showAndWait();
                } catch (ValidatorException | RepositoryException ex) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText(ex.getMessage().toString());
                    alert.showAndWait();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("You must select one homework from the comboBox.");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("You must select one student from the comboBox.");
            alert.showAndWait();
        }
    }

    private File setFileChooser()
    {
        FileChooser chooser=new FileChooser();
        chooser.setTitle("Select folder or directory");
        Stage stage=new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        FileChooser.ExtensionFilter extFilter =
                new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf");
        chooser.getExtensionFilters().add(extFilter);
        return chooser.showSaveDialog(stage);
    }


    @FXML private void handlerResolvReport() {
        File file = setFileChooser();
        if (file != null) {
            Reports currentR=listViewReport.getSelectionModel().getSelectedItem();
            if(currentR!=null) {
                String filePath = file.getAbsolutePath();
                String context="";
                if(currentR==Reports.Cele_mai_dificile_teme)
                    context="Most dificult homework:";
                else if(currentR==Reports.Media_pentru_fiecare_student)
                    context="All students' grades:";
                else if(currentR==Reports.Studenti_eligibili_pentru_examen)
                    context="Students eligible for exam:";
                else
                    context="Students having homework done on time:";
                if(currentR==Reports.Cele_mai_dificile_teme)
                    smo.exportPDFTop3MostDifficultHomework(filePath);
                else
                    smo.exportReportToPdf(studentModelReport, filePath,context);
                System.out.println("raportat cu succes ;)");
            }
            else
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("You must select one report from the list!");
                alert.showAndWait();
            }
        }
    }

    /*
    @FXML private void handlerExportReport1ToPdf(ActionEvent ev) {
        File file=setFileChooser();
        if(file!=null)
        {
            String filePath=file.getAbsolutePath();
            System.out.println(filePath);
            try {
                smo.exportPDFAllStudentsGrades(filePath);
            } catch (Exception e) {
                Alert a=new Alert(Alert.AlertType.ERROR);
                a.setContentText(e.getMessage());
                a.showAndWait();
            }
        }
    }

    @FXML private void handlerExportReport2ToPdf(ActionEvent ev) {
        File file=setFileChooser();
        if(file!=null)//cream fisier
        {
            String filePath=file.getAbsolutePath();
            System.out.println(filePath);
            try {
                smo.exportPDFStudentsEligibleForExam(filePath);
            } catch (Exception e) {
                Alert a=new Alert(Alert.AlertType.ERROR);
                a.setContentText(e.getMessage());
                a.showAndWait();
            }
        }
    }


    @FXML private void handlerExportReport3ToPdf(ActionEvent ev) {
        File file=setFileChooser();
        if(file!=null)//cream fisier
        {
            String filePath=file.getAbsolutePath();
            System.out.println(filePath);
            try {
                smo.exportPDFStudentsHavingHomeworkOnTime(filePath);
            } catch (Exception e) {
                Alert a=new Alert(Alert.AlertType.ERROR);
                a.setContentText(e.getMessage());
                a.showAndWait();
            }
        }
    }

    @FXML private void handlerExportReport4ToPdf(ActionEvent ev) {
        File file=setFileChooser();
        if(file!=null)//cream fisier
        {
            String filePath=file.getAbsolutePath();
            System.out.println(filePath);
            try {
                smo.exportPDFTop3MostDifficultHomework(filePath);
            } catch (Exception e) {
                Alert a=new Alert(Alert.AlertType.ERROR);
                a.setContentText(e.getMessage());
                a.showAndWait();
            }
        }
    }
*/

    private void notifyEvent(StudentEvent el) {
        studentModel.clear();
        smo.getAllStudents().forEach(x->studentModel.add(x));
    }

    private void notifyEvent(HomeworkEvent el) {
        homeworkModel.clear();
        smo.getAllHomework().forEach(x->homeworkModel.add(x));
    }
    private void notifyEvent(GradeEvent el) {
        gradeModel.clear();
        smo.getAllGrades().forEach(x->gradeModel.add(x));
    }
    @Override
    public void notify(Event el) {

        if (el instanceof StudentEvent)
            notifyEvent((StudentEvent) el);
        else if(el instanceof HomeworkEvent)
            notifyEvent((HomeworkEvent)el);
        else
            notifyEvent((GradeEvent)el);
    }
}

