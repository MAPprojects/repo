package mainpackage.view;

import com.jfoenix.controls.*;
import com.jfoenix.controls.cells.editors.TextFieldEditorBuilder;
import com.jfoenix.controls.cells.editors.base.GenericEditableTreeTableCell;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.SimpleStyleableObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import mainpackage.domain.ExtendedGrade;
import mainpackage.domain.Grade;
import mainpackage.domain.Homework;
import mainpackage.domain.Student;
import mainpackage.exceptions.MyException;
import mainpackage.service.Service;
import mainpackage.utils.ListEvent;
import mainpackage.utils.Observer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class GradeController implements Observer<Student> {
    private Service service;
    private ObservableList<ExtendedGrade> gradeModel= FXCollections.observableArrayList();
    @FXML JFXTreeTableView gradeTable;
    @FXML
    AnchorPane mainGradePane;
    @FXML
    AnchorPane commentGradePane;
    @FXML
    JFXTextArea gradeCommenttextArea;
    @FXML
    Label lastOperationLabel;

    JFXTreeTableColumn<ExtendedGrade, Integer> idStudentCol;
    JFXTreeTableColumn<ExtendedGrade, Integer> idTemaCol;
    JFXTreeTableColumn<ExtendedGrade, String> NumeCol;
    JFXTreeTableColumn<ExtendedGrade, String> GrupaCol;
    JFXTreeTableColumn<ExtendedGrade, String> notaCol;

    @FXML
    ComboBox currentWeekBox;
    @FXML
    ComboBox<String> idSBox;
    @FXML
    ComboBox<Integer> idTBox;
    @FXML
    JFXTextField notaField;
    @FXML
    ComboBox<String> filterGroupBox;
    @FXML
    ComboBox<String> filterHwBox;
    @FXML Pagination gradePagination;
    Integer rowsPerPage = 10;

    private static Float g_mark;
    private static String gradeComment;
    private static Integer g_idStudent;
    private static Integer g_idHomework;
    private static Boolean addOrEdit = false; // false -> add true -> edit

    public GradeController()
    {
        System.out.println("Im in constructor.");
    }

    public void initData()
    {
        this.gradeModel.setAll(service.get_extended_grades());
        initGradeTable();
        currentWeekBox.getItems().setAll("1","2","3","4","5","6","7","8","9","10","11","12","13","14");
        refreshBoxes();
        filterGroupBox.getItems().addAll(service.getGroups());
        filterHwBox.getItems().addAll(service.getHw());
        lastOperationLabel.setText("Loaded");
        FxUtilTest.autoCompleteComboBoxPlus(idSBox, (typedText, itemToCompare) -> itemToCompare.toString().toLowerCase().contains(typedText.toLowerCase()));
        gradePagination.setPageFactory(this::createPage);
    }

    private Node createPage(int pageIndex) {
    int fromIndex = pageIndex * rowsPerPage;
    int toIndex = Math.min(fromIndex + rowsPerPage, service.get_extended_grades().size());
    List<ExtendedGrade> stlist = service.get_extended_grades();
        try{
        this.gradeModel.setAll(stlist.subList(fromIndex, toIndex));
    }catch (Exception ex)
    {
        gradePagination.setPageCount(0);
    }
        return new BorderPane(gradeTable);
}

    public void refreshBoxes()
    {
        idSBox.getItems().setAll(service.get_students_names_with_ids());
        idTBox.getItems().setAll(service.get_all_homework_ids());
    }

    private void initGradeTable() {
        idStudentCol = new JFXTreeTableColumn<>("Id Student");
        idStudentCol.setPrefWidth(70);
        idStudentCol.setCellValueFactory( (TreeTableColumn.CellDataFeatures<ExtendedGrade, Integer> param) ->
        {
            if(idStudentCol.validateValue(param))
                return new SimpleIntegerProperty(param.getValue().getValue().getGrade().get_idStudent()).asObject();
            else
                return idStudentCol.getComputedValue(param);
        });

        idTemaCol = new JFXTreeTableColumn<>("Id Tema");
        idTemaCol.setPrefWidth(70);
        idTemaCol.setCellValueFactory( (TreeTableColumn.CellDataFeatures<ExtendedGrade, Integer> param) ->
        {
            if(idTemaCol.validateValue(param))
                return new SimpleIntegerProperty(param.getValue().getValue().getGrade().get_idHomework()).asObject();
            else
                return idTemaCol.getComputedValue(param);
        });
        NumeCol = new JFXTreeTableColumn<>("Nume");
        NumeCol.setPrefWidth(140);
        NumeCol.setCellValueFactory( (TreeTableColumn.CellDataFeatures<ExtendedGrade, String> param) ->
        {
            if(NumeCol.validateValue(param))
                return new SimpleStringProperty(param.getValue().getValue().getName());
            else
                return NumeCol.getComputedValue(param);
        });
        GrupaCol = new JFXTreeTableColumn<>("Grupa");
        GrupaCol.setPrefWidth(140);
        GrupaCol.setCellValueFactory( (TreeTableColumn.CellDataFeatures<ExtendedGrade, String> param) ->
        {
            if(GrupaCol.validateValue(param))
                return new SimpleStringProperty(param.getValue().getValue().getGroup());
            else
                return GrupaCol.getComputedValue(param);
        });

        notaCol = new JFXTreeTableColumn<>("Nota");
        notaCol.setPrefWidth(90);
        notaCol.setCellValueFactory( (TreeTableColumn.CellDataFeatures<ExtendedGrade, String> param) ->
        {
            if(notaCol.validateValue(param))
                return new SimpleStringProperty(String.valueOf(param.getValue().getValue().getGrade().get_value()));
            else
                return notaCol.getComputedValue(param);
        });

        notaCol.setCellFactory((TreeTableColumn<ExtendedGrade, String> param) -> new GenericEditableTreeTableCell<>(
                new TextFieldEditorBuilder()));
        notaCol.setOnEditCommit((TreeTableColumn.CellEditEvent<ExtendedGrade, String> t) ->
        {
            System.out.println("EDITING GRADE");
            System.out.println(t.getTreeTableView().getTreeItem(t.getTreeTablePosition().getRow()));
            ExtendedGrade tempGrade = t.getTreeTableView().getTreeItem(t.getTreeTablePosition().getRow()).getValue();
            tempGrade.getGrade().set_value(Float.valueOf(t.getNewValue()));
            try{
                service.modify_grade(tempGrade.getGrade(),"");
                lastOperationLabel.setText("Modified Grade");
                lastOperationLabel.setStyle("-fx-text-fill: green");
            }catch(MyException ex)
            {
                lastOperationLabel.setText(ex.getMessage());
                lastOperationLabel.setStyle("-fx-text-fill: red");
                notifyEvent(null);
            }
        });

        TreeItem<ExtendedGrade> root = new RecursiveTreeItem<>(gradeModel, RecursiveTreeObject::getChildren);
        gradeTable.setRoot(root);
        gradeTable.setShowRoot(false);
        gradeTable.setEditable(true);
        gradeTable.getColumns().setAll(idStudentCol, idTemaCol, NumeCol, GrupaCol, notaCol);
    }

    public ObservableList<ExtendedGrade> getModel() {
        return gradeModel;
    }

    @Override
    public void notifyEvent(ListEvent<Student> e) {
        System.out.println("UPDATING MODEL");
        gradeModel.setAll(service.get_extended_grades());
    }

    public void handleX(MouseEvent mouseEvent) {
        Node source = (Node)  mouseEvent.getSource();
        Stage stage  = (Stage) source.getScene().getWindow();
        stage.close();
    }
    public void setService(Service service) {
        if(service == null)
            System.out.println("Service is null in setService");
        else
            System.out.println(service);
        this.service = service;
    }
    public Node getWindow() {
        return mainGradePane;
    }
    public void setCurrentWeek(ActionEvent actionEvent) {
        service.setCurrent_week(Integer.valueOf((String)currentWeekBox.getValue()));
    }

    public void handleAddGrade(ActionEvent actionEvent) {
        String nota = notaField.getText();
        String idS = idSBox.getValue().split("_")[1];
        System.out.println(idS);
        Integer idT =idTBox.getValue();
        if( idS == null || idT == null)
        {
            lastOperationLabel.setText("Select student and homework");
            lastOperationLabel.setStyle("-fx-text-fill: red");
        }
        try
        {
            g_mark = Float.valueOf(nota);
        }catch (Exception ex)
        {
            lastOperationLabel.setText("Nota trebuie sa fie Float");
            lastOperationLabel.setStyle("-fx-text-fill: red");
            return;
        }
        gradeComment = null;
        g_idStudent = Integer.parseInt(idS);
        g_idHomework = idT;
        showGradeCommentWindow();
    }

    public void handleFilter(ActionEvent actionEvent) {
        System.out.println("Im filtering");
        String grupa = null;
        Integer hwId = null;
        Float maxGrade= null;
        try {
            grupa = filterGroupBox.getValue();
            hwId =   Integer.valueOf(filterHwBox.getValue());
//            maxGrade = Float.valueOf(dragGrade.getIndicatorPosition().toString());
            System.out.println(grupa);
            System.out.println(hwId);
            System.out.println(maxGrade);
        }catch(Exception ex)
        {
            System.out.println(ex.getMessage());
        }
        gradeModel.setAll(service.filter_grades (grupa, hwId, maxGrade));
        lastOperationLabel.setText("Filtered");
        lastOperationLabel.setStyle("-fx-text-fill: green");
    }

    public void resetFilter(ActionEvent actionEvent) {
        notifyEvent(null);
    }

    public void submitGradeCommentHandle(ActionEvent actionEvent) {
        gradeComment =gradeCommenttextArea.getText();
        System.out.println(gradeComment);
        System.out.println(g_mark);
        try {
            if(!addOrEdit)
                service.add_grade(new Grade(g_idStudent, g_idHomework, g_mark),gradeComment);
        } catch (MyException ex) {
            Alert alert = new Alert(Alert.AlertType.WARNING, ex.getMessage());
            alert.initStyle(StageStyle.UNDECORATED);
            alert.show();
        }
        finally {
            Node source = (Node) actionEvent.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.close();
        }
    }

    Stage comGradeStage;
    public void showGradeCommentWindow() {
        System.out.println("Show Add Student Menu");
        FXMLLoader comGrade = new FXMLLoader();
        comGrade.setLocation(getClass().getResource("/mainpackage/view/AddGradeComment.fxml"));
        try {
            Parent root = comGrade.load();
            GradeController ctr = comGrade.getController();
            ctr.setService(this.service);
            comGradeStage = new Stage();
            comGradeStage.initStyle(StageStyle.UNDECORATED);
            comGradeStage.setScene(new Scene(root));
            comGradeStage.show();
            lastOperationLabel.setText("");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}