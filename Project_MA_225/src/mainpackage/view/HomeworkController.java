package mainpackage.view;

        import com.jfoenix.controls.JFXTextField;
        import com.jfoenix.controls.JFXTreeTableColumn;
        import com.jfoenix.controls.JFXTreeTableView;
        import com.jfoenix.controls.RecursiveTreeItem;
        import com.jfoenix.controls.cells.editors.TextFieldEditorBuilder;
        import com.jfoenix.controls.cells.editors.base.GenericEditableTreeTableCell;
        import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
        import javafx.beans.property.SimpleIntegerProperty;
        import javafx.beans.property.SimpleStringProperty;
        import javafx.collections.FXCollections;
        import javafx.collections.ObservableList;
        import javafx.event.ActionEvent;
        import javafx.fxml.FXML;
        import javafx.scene.Node;
        import javafx.scene.control.*;
        import javafx.scene.input.KeyEvent;
        import javafx.scene.input.MouseEvent;
        import javafx.scene.layout.AnchorPane;
        import javafx.scene.layout.BorderPane;
        import javafx.stage.Stage;
        import mainpackage.domain.Homework;
        import mainpackage.domain.Student;
        import mainpackage.exceptions.MyException;
        import mainpackage.service.Service;
        import mainpackage.utils.ListEvent;
        import mainpackage.utils.Observer;

        import java.util.ArrayList;
        import java.util.List;

public class HomeworkController implements Observer<Student> {
    private Service service;
    private ObservableList<Homework> homeworkModel= FXCollections.observableArrayList();
    @FXML JFXTreeTableView homeworkTable;
    @FXML
    AnchorPane mainHomeworkPane;

    @FXML
    Label lastOperationLabel;
    JFXTreeTableColumn<Homework, Integer> idCol;
    JFXTreeTableColumn<Homework, String> descriereCol;
    JFXTreeTableColumn<Homework, String> deadlineCol;

    @FXML
    JFXTextField idLabel;
    @FXML
    JFXTextField descriereLabel;
    @FXML
    ComboBox deadlineCombo;
    @FXML
    ComboBox currentWeekBox;
    @FXML
    ComboBox deadlineFilterBox;
    @FXML
    JFXTextField descriereFilter;
    @FXML
    Pagination homeworkPagination;
    Integer rowsPerPage = 5;


    public HomeworkController()
    {
        System.out.println("Im in constructor.");
    }

    public void initData()
    {
        this.homeworkModel.setAll(service.get_all_homeworks());
        initHomeworkTable();
        lastOperationLabel.setText("Loaded");
        deadlineCombo.getItems().setAll("1","2","3","4","5","6","7","8","9","10","11","12","13","14");
        deadlineFilterBox.getItems().setAll("1","2","3","4","5","6","7","8","9","10","11","12","13","14");
        currentWeekBox.getItems().setAll("1","2","3","4","5","6","7","8","9","10","11","12","13","14");
        homeworkPagination.setPageFactory(this::createPage);
    }
    private Node createPage(int pageIndex) {
        int fromIndex = pageIndex * rowsPerPage;
        int toIndex = Math.min(fromIndex + rowsPerPage, service.get_all_homeworks().size());
        List<Homework> stlist = new ArrayList<>();
        stlist.addAll(service.get_all_homeworks());
        try{
            this.homeworkModel.setAll(stlist.subList(fromIndex, toIndex));
        }catch (Exception ex)
        {
            homeworkPagination.setPageCount(0);
        }
        return new BorderPane(homeworkTable);
    }
    private void initHomeworkTable() {
        idCol = new JFXTreeTableColumn<>("Id");
        idCol.setPrefWidth(100);
        idCol.setCellValueFactory( (TreeTableColumn.CellDataFeatures<Homework, Integer> param) ->
        {
            if(idCol.validateValue(param))
                return new SimpleIntegerProperty(param.getValue().getValue().getId()).asObject();
            else
                return idCol.getComputedValue(param);
        });

        descriereCol = new JFXTreeTableColumn<>("Descriere");
        descriereCol.setPrefWidth(550);
        descriereCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<Homework, String> param) ->{
            if(descriereCol.validateValue(param))
                return new SimpleStringProperty(param.getValue().getValue().getDescription());
            else
                return descriereCol.getComputedValue(param);
        });

        deadlineCol = new JFXTreeTableColumn<>("Deadline");
        deadlineCol.setPrefWidth(150);
        deadlineCol.setCellValueFactory( (TreeTableColumn.CellDataFeatures<Homework, String> param) ->
        {
            if(deadlineCol.validateValue(param))
                return new SimpleStringProperty(String.valueOf(param.getValue().getValue().getDeadline()));
            else
                return  deadlineCol.getComputedValue(param);
        });

        descriereCol.setCellFactory((TreeTableColumn<Homework, String> param) -> new GenericEditableTreeTableCell<>(
                new TextFieldEditorBuilder()));
        descriereCol.setOnEditCommit((TreeTableColumn.CellEditEvent<Homework, String> t) ->
        {
            System.out.println("EDITING DESCRIPTION");
            System.out.println(t.getTreeTableView().getTreeItem(t.getTreeTablePosition().getRow()));
            Homework tempHw = t.getTreeTableView().getTreeItem(t.getTreeTablePosition().getRow()).getValue();
            tempHw.setDescription(t.getNewValue());
            try {
                service.update_homework(tempHw);
                lastOperationLabel.setText("Updated");
                lastOperationLabel.setStyle("-fx-text-fill: green");
            }catch(MyException ex)
            {
                System.out.println("Error editing description");
                lastOperationLabel.setText(ex.getMessage());
                lastOperationLabel.setStyle("-fx-text-fill: red");
                notifyEvent(null);
            }
        });

        deadlineCol.setCellFactory((TreeTableColumn<Homework, String> param) -> new GenericEditableTreeTableCell<>(
                new TextFieldEditorBuilder()));
        deadlineCol.setOnEditCommit((TreeTableColumn.CellEditEvent<Homework, String> t) ->
        {
            System.out.println("EDITING DEADLINE");
            System.out.println(t.getTreeTableView().getTreeItem(t.getTreeTablePosition().getRow()));
            Homework tempHw = t.getTreeTableView().getTreeItem(t.getTreeTablePosition().getRow()).getValue();
            try {
                tempHw.setDeadline(Integer.parseInt(t.getNewValue()));
                service.update_homework(tempHw);
                lastOperationLabel.setText("Updated");
                lastOperationLabel.setStyle("-fx-text-fill: green");
            }catch(MyException ex)
            {
                System.out.println("Error editing description");
                lastOperationLabel.setText(ex.getMessage());
                lastOperationLabel.setStyle("-fx-text-fill: red");
                notifyEvent(null);
            }
            catch(Exception ex)
            {
                System.out.println("GENERIC Error editing description");
                lastOperationLabel.setText("Deadline must pe an int");
                lastOperationLabel.setStyle("-fx-text-fill: red");
                notifyEvent(null);
            }
        });


        TreeItem<Homework> root = new RecursiveTreeItem<>(homeworkModel, RecursiveTreeObject::getChildren);
        homeworkTable.setRoot(root);
        homeworkTable.setShowRoot(false);
        homeworkTable.setEditable(true);
        homeworkTable.getColumns().setAll(idCol, deadlineCol, descriereCol);
    }

    public ObservableList<Homework> getModel() {
        return homeworkModel;
    }

    @Override
    public void notifyEvent(ListEvent<Student> e) {
        System.out.println("UPDATING MODEL");
//        homeworkModel.setAll(service.filter_homeworks(null,null,null));
        homeworkModel.setAll(service.filter_homeworks_deadline());
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
        return mainHomeworkPane;
    }

    public void addHomework(ActionEvent actionEvent) {
        System.out.println("ADDING NEW HW");
        lastOperationLabel.setText("");
        try{
            Homework hw = getHomeworkFromView();
            service.add_homework(hw);
        }catch(MyException ex)
        {
            lastOperationLabel.setText(ex.getMessage());
            lastOperationLabel.setStyle("-fx-text-fill: red");
        }
    }

    public Homework getHomeworkFromView() {
        String id = idLabel.getText();
        String descriere = descriereLabel.getText();
        String deadline = (String) deadlineCombo.getValue();
        Integer idInt = null, deadlineInt = null;
        try{
            idInt = Integer.valueOf(id);
            deadlineInt = Integer.valueOf(deadline);
        }catch(Exception ex)
        {
            lastOperationLabel.setText("id-ul si deadline-ul trebuie sa fie nr intregi");
            lastOperationLabel.setStyle("-fx-text-fill: red");
        }
        return new Homework(idInt, descriere, deadlineInt);
    }

    public void handleDelete(ActionEvent actionEvent) {
        TreeItem<Homework> st = (TreeItem<Homework>) homeworkTable.getSelectionModel().getSelectedItem();
        if(st == null)
            return;
        Homework selected = st.getValue();
        if (!(service.delete_homework(selected.getId()) == null))
        {
            lastOperationLabel.setText("Homework deleted.");
            lastOperationLabel.setStyle("-fx-text-fill: green");
        }
        else
        {
            lastOperationLabel.setText("Delete failed.");
            lastOperationLabel.setStyle("-fx-text-fill: red");
        }
    }

    public void setCurrentWeek(ActionEvent actionEvent) {
        service.setCurrent_week(Integer.valueOf((String)currentWeekBox.getValue()));
    }

    public void filterHomeworks(KeyEvent keyEvent) {
        System.out.println("Im filtering");
        String descriere =null; String deadline =null;
        if(!descriereFilter.getText().equals(""))
        {
            descriere = descriereFilter.getText();
        }
        deadline = (String) deadlineFilterBox.getValue();
        Integer deadlineInt;
        if(deadline == null)
           deadlineInt = null;
        else{
            deadlineInt = Integer.parseInt(deadline);
        }

        homeworkModel.setAll(service.filter_homeworks(descriere, deadlineInt));
        lastOperationLabel.setText("Filtered");
        lastOperationLabel.setStyle("-fx-text-fill: green");

    }

    public void filterHomeworksBox(ActionEvent actionEvent) {
        filterHomeworks(null);
    }
}