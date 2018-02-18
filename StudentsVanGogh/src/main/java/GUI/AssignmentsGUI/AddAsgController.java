package GUI.AssignmentsGUI;

import Domain.Assignment;
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
import Validator.ValidatorException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class AddAsgController extends AbstractController implements Observer<Assignment> {
    private StudentService studentService;
    private GradeService gradeService;
    private AssignmentService assignmentService;
    private GeneralService generalService;
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    ObservableList model = FXCollections.observableArrayList();


    int rows_per_page=20;

    @FXML
    Pagination pagination;

    @FXML
    AnchorPane contentStd;

    @FXML
    TableView<Assignment> tableViewAsg = new TableView();

    @FXML
    TableColumn colAsgID = new TableColumn("ID");

    @FXML
    TableColumn colAsgDesc = new TableColumn("description");

    @FXML
    TableColumn colAsgDeadline = new TableColumn("deadline");
    @FXML
    TextField textFieldSearch;

    @FXML
    ImageView imageViewAsg;

    @FXML
    Button buttonBackAddAsg;

    @FXML
    Button buttonAdd;

    @FXML
    TextField textFieldDeadline;

    @FXML
    TextField textFieldID;

    @FXML
    TextField textFieldDesc;

    @FXML
    DatePicker datePicker;


    private Node asgView;

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
        this.colAsgID.setCellValueFactory(new PropertyValueFactory<Assignment,Integer>("idAsg"));
        this.colAsgDesc.setCellValueFactory(new PropertyValueFactory<Student, String>("description"));
        this.colAsgDeadline.setCellValueFactory(new PropertyValueFactory<Student, Integer>("deadline"));

        colAsgID.setMinWidth(100);
        colAsgDesc.setMinWidth(300);
        colAsgDeadline.setMinWidth(100);
        tableViewAsg.getColumns().addAll(colAsgID,colAsgDesc,colAsgDeadline);

        try {
            model.setAll(assignmentService.getAllAssignments());
        } catch (RepositoryException e) {
            e.printStackTrace();
        }

        //tableViewAsg.setItems(model);
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
        tableViewAsg.setItems(FXCollections.observableList(model.subList(fromIndex, toIndex)));

        return new BorderPane(tableViewAsg);

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        File file = new File("5.jpg");
        Image image = new Image(file.toURI().toString());
        imageViewAsg.setImage(image);


    }

    public AddAsgController(StudentService studentService, GradeService gradeService, AssignmentService assignmentService, GeneralService generalService) {
        this.studentService = studentService;
        this.gradeService = gradeService;
        this.assignmentService = assignmentService;
        this.generalService = generalService;
    }

    public void handleBackAddAsg() throws IOException {
        asgView  = InitView.initAsgView(studentService,assignmentService,gradeService,generalService,user);
        contentStd.getChildren().setAll(asgView);
        AnchorPane.setTopAnchor(asgView, 0d);
        AnchorPane.setBottomAnchor(asgView, 0d);
        AnchorPane.setLeftAnchor(asgView, 0d);
        AnchorPane.setRightAnchor(asgView, 0d);
    }

    public void handleAdd() {
        try {
            LocalDate localDate = datePicker.getValue();
            LocalDateTime localDateTime = LocalDateTime.of(localDate.getYear(),localDate.getMonth(),localDate.getDayOfMonth(),12,0);
            assignmentService.addAssignment(Integer.parseInt(textFieldID.getText()),textFieldDesc.getText(),(int) CurrentWeek.getWeek(localDateTime));
        } catch (ValidatorException e) {
            Message.showMessage(Alert.AlertType.WARNING,"add assignment",e.getMessage());
        } catch (RepositoryException e) {
            Message.showMessage(Alert.AlertType.WARNING,"add assignment",e.getMessage());
        }
        setPage();
    }


    public AddAsgController() {
    }

    @Override
    public void notifyEvent(ListEvent<Assignment> listEvent) {
        try {
            model.setAll(assignmentService.getAllAssignments());
        } catch (RepositoryException e) {
            e.printStackTrace();
        }
        setPage();
    }

    public void handleSearch() throws RepositoryException {
        this.model.setAll(generalService.getAllAsgDesc(textFieldSearch.getText()));
        setPage();
    }
}
