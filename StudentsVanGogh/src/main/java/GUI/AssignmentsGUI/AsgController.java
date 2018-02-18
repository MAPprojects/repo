package GUI.AssignmentsGUI;

import Domain.Assignment;
import Domain.Student;
import Domain.User;
import GUI.AbstractController;
import GUI.InitView;
import GUI.Message;
import Main.StartApplication;
import Repository.RepositoryException;
import Service.AssignmentService;
import Service.GeneralService;
import Service.GradeService;
import Service.StudentService;
import Utils.ListEvent;
import Utils.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class AsgController extends AbstractController implements Observer<Assignment> {
    private StudentService studentService;
    private AssignmentService assignmentService;
    private GradeService gradeService;
    private GeneralService generalService;
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        if (user == User.userStudent)
        {
            buttonAddStd.setDisable(true);
            buttonUpdStd.setDisable(true);
            buttonDltStd.setDisable(true);
        }

    }

    ObservableList model = FXCollections.observableArrayList();


    int rows_per_page=20;

    @FXML
    Pagination pagination;

    @FXML
    AnchorPane contentAsg;

    @FXML
    TableView<Assignment> tableViewAsg = new TableView();

    @FXML
    TableColumn colAsgID = new TableColumn("ID");

    @FXML
    TableColumn colAsgDesc = new TableColumn("description");

    @FXML
    TableColumn colAsgDeadline = new TableColumn("deadline");

    @FXML
    Button buttonAddStd;

    @FXML
    Button buttonUpdStd;

    @FXML
    Button buttonDltStd;



    @FXML
    TextField textFieldSearch;

    @FXML
    ImageView imageViewAsg;

    @FXML
    Button buttonBackAsg;

    @FXML
    Button buttonTODO;

    @FXML
    Button buttonPast;

    @FXML
    Button buttonAll;

    private Node homeView;
    private Node addAsgView;
    private Node updAsgView;

    public AsgController(StudentService studentService, AssignmentService assignmentService, GradeService gradeService, GeneralService generalService) {
        this.studentService = studentService;
        this.assignmentService = assignmentService;
        this.gradeService = gradeService;
        this.generalService = generalService;
    }



    public void setStudentService(StudentService studentService) {
        this.studentService = studentService;

    }

    public void setAssignmentService(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    public void setGradeService(GradeService gradeService) {
        this.gradeService = gradeService;
    }

    public void setGeneralService(GeneralService generalService) {
        this.generalService = generalService;

        this.colAsgID.setCellValueFactory(new PropertyValueFactory<Assignment,Integer>("idAsg"));
        this.colAsgDesc.setCellValueFactory(new PropertyValueFactory<Assignment, String>("description"));
        this.colAsgDeadline.setCellValueFactory(new PropertyValueFactory<Student, Integer>("deadline"));

        tableViewAsg.getColumns().addAll(colAsgID,colAsgDesc,colAsgDeadline);
        try {
            model = FXCollections.observableArrayList(assignmentService.getAllAssignments());
        } catch (RepositoryException e) {

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


    public AsgController() {
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        File file = new File("5.jpg");
        Image image = new Image(file.toURI().toString());
        imageViewAsg.setImage(image);

    }

    public void handleAll() throws RepositoryException {
        model.setAll(assignmentService.getAllAssignments());
        setPage();
    }

    public void handleBackAsg() {
        homeView = InitView.initHomeView(studentService,assignmentService,gradeService,generalService,user);
        //contentAsg.getChildren().setAll(homeView);
        StartApplication.getScene().setRoot((Parent) homeView);
        AnchorPane.setTopAnchor(homeView, 0d);
        AnchorPane.setBottomAnchor(homeView, 0d);
        AnchorPane.setLeftAnchor(homeView, 0d);
        AnchorPane.setRightAnchor(homeView, 0d);

    }

    public void handleAddAsg() {
        addAsgView = InitView.initAddAsgView(studentService,assignmentService,gradeService,generalService,user);
        contentAsg.getChildren().setAll(addAsgView);
        AnchorPane.setTopAnchor(addAsgView, 0d);
        AnchorPane.setBottomAnchor(addAsgView, 0d);
        AnchorPane.setLeftAnchor(addAsgView, 0d);
        AnchorPane.setRightAnchor(addAsgView, 0d);
    }

    public void handleUpdAsg() {
        updAsgView = InitView.initUpdAsgView(studentService,assignmentService,gradeService,generalService,user);
        contentAsg.getChildren().setAll(updAsgView);
        AnchorPane.setTopAnchor(updAsgView, 0d);
        AnchorPane.setBottomAnchor(updAsgView, 0d);
        AnchorPane.setLeftAnchor(updAsgView, 0d);
        AnchorPane.setRightAnchor(updAsgView, 0d);
    }

    public void handleDeleteAsg() {
        if (tableViewAsg.getSelectionModel().getSelectedItem() == null) {
            Message.showMessage(Alert.AlertType.WARNING, "delete assignment", "you must select an assignment to erase it");
            return;
        }
        try {
            assignmentService.deleteAssignment(tableViewAsg.getSelectionModel().getSelectedItem().getIdAsg());
        } catch (RepositoryException e) {
            Message.showMessage(Alert.AlertType.WARNING,"delete assignment",e.getMessage());
        }
        setPage();
    }

    public void handleTODO() throws RepositoryException {
        model.setAll(generalService.getAllAsgTODO());
        setPage();
    }

    public void handlePast() throws RepositoryException {
        model.setAll(generalService.getAllAsgPast());
        setPage();
    }

    public void handleSearch() throws RepositoryException {
        this.model.setAll(generalService.getAllAsgDesc(textFieldSearch.getText()));
        setPage();
    }

}
