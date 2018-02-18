package GUI.GradesGUI;

import Domain.Assignment;
import Domain.Grade;
import Domain.Student;
import Domain.User;
import GUI.AbstractController;
import GUI.InitView;
import GUI.Message;
import Repository.RepositoryException;
import Service.AssignmentService;
import Service.GeneralService;
import Service.GradeService;
import Service.StudentService;
import Utils.CurrentWeek;
import Utils.ListEvent;
import Utils.Observer;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class UpdGrdController extends AbstractController implements Observer<Grade> {
    private StudentService studentService;
    private GradeService gradeService;
    private AssignmentService assignmentService;
    private GeneralService generalService;
    ObservableList model = FXCollections.observableArrayList();

    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @FXML
    AnchorPane contentStd;



    int rows_per_page=20;

    @FXML
    Pagination pagination;

    @FXML
    TableView<Grade> tableViewGrd = new TableView();

    @FXML
    TableColumn<Grade,String> colGrdNameStd = new TableColumn("student");

    @FXML
    TableColumn<Grade, String> colGrdDescAsg = new TableColumn("assignment");

    @FXML
    TableColumn<Grade, Integer> colGrdValue = new TableColumn("value");

    @FXML
    TableColumn<Grade, String> colGrdNotes = new TableColumn("grades");


    @FXML
    ComboBox<Student> comboBoxStd;

    @FXML
    ComboBox<Assignment> comboBoxAsg;

    @FXML
    ComboBox comboBoxGrd;

    @FXML
    TextField textFieldSearchNameStd;

    @FXML
    TextField textFieldSearchAsgDesc;

    @FXML
    ImageView imageViewStd;

    @FXML
    Button buttonBackUpdGrd;

    @FXML
    Button buttonUpd;

    @FXML
    TextField textFieldNotes;


    private Node grdView;

    public void setStudentService(StudentService studentService) {
        this.studentService = studentService;
    }

    public void setGradeService(GradeService gradeService) {
        this.gradeService = gradeService;
    }

    public void setAssignmentService(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    public void setGeneralService(GeneralService generalService) {
        this.generalService = generalService;

        this.colGrdNameStd.setCellValueFactory(c-> new SimpleStringProperty(c.getValue().getStd().getName()));
        this.colGrdDescAsg.setCellValueFactory(c-> new SimpleStringProperty(c.getValue().getAsg().getDescription()));
        this.colGrdValue.setCellValueFactory(c-> new SimpleObjectProperty<>(c.getValue().getValue()));
        this.colGrdNotes.setCellValueFactory(c-> new SimpleStringProperty(c.getValue().getNotes()));

        tableViewGrd.getColumns().addAll(colGrdNameStd,colGrdDescAsg,colGrdValue,colGrdNotes);

        ObservableList grds = FXCollections.observableArrayList("1","2","3","4","5","6","7","8","9","10");

        comboBoxGrd.setItems(grds);

        try {
            model = FXCollections.observableArrayList(gradeService.getAll());

            ObservableList<Assignment> asgs = FXCollections.observableArrayList(assignmentService.getAllAssignments());

            this.comboBoxAsg.setItems(asgs);

            ObservableList<Student> stds = FXCollections.observableArrayList(studentService.getAllStudents());

            this.comboBoxStd.setItems(stds);

        } catch (RepositoryException e) {

        }
        tableViewGrd.getSelectionModel().selectedItemProperty().addListener((o, n, m) ->  handleSelected());
        setPage();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        File file = new File("5.jpg");
        Image image = new Image(file.toURI().toString());
        imageViewStd.setImage(image);


    }

    public UpdGrdController(StudentService studentService, GradeService gradeService, AssignmentService assignmentService, GeneralService generalService) {
        this.studentService = studentService;
        this.gradeService = gradeService;
        this.assignmentService = assignmentService;
        this.generalService = generalService;
    }

    public void handleBackGrd() throws IOException {
        grdView  = InitView.initGrdView(studentService,assignmentService,gradeService,generalService,user);
        contentStd.getChildren().setAll(grdView);
        AnchorPane.setTopAnchor(grdView, 0d);
        AnchorPane.setBottomAnchor(grdView, 0d);
        AnchorPane.setLeftAnchor(grdView, 0d);
        AnchorPane.setRightAnchor(grdView, 0d);
    }

    public void handleUpd() throws Exception {
        try {
            gradeService.updateGrade(comboBoxStd.getSelectionModel().getSelectedItem().getIdStudent(),
                    comboBoxAsg.getSelectionModel().getSelectedItem().getIdAsg(),
                    CurrentWeek.getCurrentWeek(),
                    Integer.parseInt(comboBoxGrd.getSelectionModel().getSelectedItem().toString()),
                    textFieldNotes.getText());
        } catch (RepositoryException e) {
            Message.showMessage(Alert.AlertType.WARNING,"add grade",e.getMessage());
        }
        setPage();
    }

    public void setPage() {
        setPaginationPages();
        pagination.setPageFactory(this::createPage);
    }

    public void setPaginationPages() {
        int numOfPages = 1;
        if (model.size() % rows_per_page == 0) {
            numOfPages = model.size() / rows_per_page;
        } else if (model.size() > rows_per_page) {
            numOfPages = model.size() / rows_per_page + 1;
        }
        pagination.setPageCount(numOfPages);
    }

    public Node createPage(int pageIndex) {

        int fromIndex = pageIndex * rows_per_page;
        int toIndex = Math.min(fromIndex + rows_per_page, model.size());
        tableViewGrd.setItems(FXCollections.observableList(model.subList(fromIndex, toIndex)));

        return new BorderPane(tableViewGrd);

    }


    public void handleSelected() {
        comboBoxStd.setValue(tableViewGrd.getSelectionModel().getSelectedItem().getStd());
        comboBoxAsg.setValue(tableViewGrd.getSelectionModel().getSelectedItem().getAsg());
        comboBoxGrd.setValue(tableViewGrd.getSelectionModel().getSelectedItem().getValue());
        textFieldNotes.setText(tableViewGrd.getSelectionModel().getSelectedItem().getNotes());
    }

    public UpdGrdController() {
    }

    @Override
    public void notifyEvent(ListEvent<Grade> listEvent) {
        try {
            model.setAll(gradeService.getAll());
        } catch (RepositoryException e) {
            e.printStackTrace();
        }
        setPage();
        tableViewGrd.refresh();
    }

    public void handleSearchName() throws RepositoryException {
        this.model.setAll(generalService.getAllGrdStd(textFieldSearchNameStd.getText()));
        setPage();
    }


    public void handleSearchDesc() throws RepositoryException {
        this.model.setAll(generalService.getAllGrdAsgDesc(textFieldSearchAsgDesc.getText()));
        setPage();
    }
}
