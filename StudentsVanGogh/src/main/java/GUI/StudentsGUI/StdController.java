package GUI.StudentsGUI;

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
import Utils.Observable;
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
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class StdController extends AbstractController implements Observer<Student> {
    private StudentService studentService;
    private GeneralService generalService;
    private AssignmentService assignmentService;
    private GradeService gradeService;

    ObservableList model = FXCollections.observableArrayList();

    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        if (user == User.userStudent) {
            buttonAddStd.setDisable(true);
            buttonDltStd.setDisable(true);
            buttonUpdStd.setDisable(true);
        }

        if (user == User.userProfessor) {
            buttonAddStd.setDisable(true);
            buttonUpdStd.setDisable(true);
            buttonDltStd.setDisable(true);
        }
    }

    int rows_per_page=20;

    @FXML
    Pagination pagination;

    @FXML
    AnchorPane contentStd;

    @FXML
    TableView tableViewStd = new TableView();

    @FXML
    TableColumn colStdID = new TableColumn("ID");

    @FXML
    TableColumn colStdName = new TableColumn("name");

    @FXML
    TableColumn colStdGroup = new TableColumn("group");

    @FXML
    TableColumn colStdEmail = new TableColumn("email");

    @FXML
    TableColumn colStdProf = new TableColumn("professor");

    @FXML
    Button buttonAddStd;

    @FXML
    Button buttonUpdStd;

    @FXML
    Button buttonDltStd;

    @FXML
    Button buttonRanking;

    @FXML
    ComboBox comboBoxGroup;

    @FXML
    Button buttonAll;

    @FXML
    TextField textFieldSearch;

    @FXML
    ImageView imageViewStd;

    @FXML
    Button buttonBackStd;


    private Node homeView;
    private Node addStdView;
    private Node updStdView;

    public StdController() {
    }

    public void setAssignmentService(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    public void setGradeService(GradeService gradeService) {
        this.gradeService = gradeService;
    }

    public StdController(StudentService studentService, GeneralService generalService, AssignmentService assignmentService, GradeService gradeService) {
        this.studentService = studentService;
        this.generalService = generalService;
        this.assignmentService = assignmentService;
        this.gradeService = gradeService;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        File file = new File("D:/StudentGrades/5.jpg");
        Image image = new Image(file.toURI().toString());
        imageViewStd.setImage(image);


    }

    public void handleAddStd() throws IOException {
        addStdView = InitView.initAddStdView(studentService,assignmentService,gradeService,generalService,user);
        contentStd.getChildren().setAll(addStdView);

        AnchorPane.setTopAnchor(addStdView, 0d);
        AnchorPane.setBottomAnchor(addStdView, 0d);
        AnchorPane.setLeftAnchor(addStdView, 0d);
        AnchorPane.setRightAnchor(addStdView, 0d);

    }


    public void handleUpdStd() throws IOException {
        updStdView = InitView.initUpdStdView(studentService,assignmentService,gradeService,generalService,user);
        contentStd.getChildren().setAll(updStdView);

        AnchorPane.setTopAnchor(updStdView, 0d);
        AnchorPane.setBottomAnchor(updStdView, 0d);
        AnchorPane.setLeftAnchor(updStdView, 0d);
        AnchorPane.setRightAnchor(updStdView, 0d);
    }

    public void handleDltStd() throws RepositoryException {
        if (tableViewStd.getSelectionModel().getSelectedItem() == null) {
            Message.showMessage(Alert.AlertType.WARNING, "delete student", "you must select a student to erase it");
            return;
        }
        try {
            studentService.deleteStudent(tableViewStd.getSelectionModel().getSelectedItem().hashCode());
        } catch (RepositoryException e) {
            Message.showMessage(Alert.AlertType.WARNING,"delete student",e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
        model.setAll(studentService.getAllStudents());
        setPage();
    }

    public void handleComboStd() throws RepositoryException {
        if (comboBoxGroup.getSelectionModel().getSelectedItem().toString().equals("look for a group..."))
            this.model.setAll(studentService.getAllStudents());
        else
            this.model.setAll(generalService.getAllStdGroup(Integer.parseInt(comboBoxGroup.getSelectionModel().getSelectedItem().toString())));

        setPage();

    }

    public void handleAll() throws RepositoryException {
        this.model.setAll(studentService.getAllStudents());
        setPage();
    }

    public void handleRanking() throws Exception {
        this.model.setAll(generalService.getAllStdAvg());
        setPage();

    }

    public void handleSearch() throws RepositoryException {
        this.model.setAll(generalService.getAllStdName(textFieldSearch.getText()));
        setPage();
    }

    public void handleBackStd() throws IOException {
        homeView = InitView.initHomeView(studentService,assignmentService,gradeService,generalService,user);
        StartApplication.getScene().setRoot((Parent) homeView);

    }



    public StudentService getStudentService() {
        return studentService;
    }

    public void setStudentService(StudentService studentService) {


        this.studentService = studentService;

        this.colStdID.setCellValueFactory(new PropertyValueFactory<Student,Integer>("idStudent"));
        this.colStdName.setCellValueFactory(new PropertyValueFactory<Student, String>("name"));
        this.colStdGroup.setCellValueFactory(new PropertyValueFactory<Student, Integer>("group"));
        this.colStdEmail.setCellValueFactory(new PropertyValueFactory<Student, String>("email"));
        this.colStdProf.setCellValueFactory(new PropertyValueFactory<Student, String>("professor"));

        tableViewStd.getColumns().setAll(colStdID,colStdName,colStdGroup,colStdEmail,colStdProf);


        ObservableList<String> groups =FXCollections.observableArrayList("look for a group...","220","221","222","223","224","225","226","227");

        comboBoxGroup.setItems(groups);
        try {
            model = FXCollections.observableArrayList(studentService.getAllStudents());
        } catch (RepositoryException e) {

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
        tableViewStd.setItems(FXCollections.observableList(model.subList(fromIndex, toIndex)));

        return new BorderPane(tableViewStd);

    }

    public GeneralService getGeneralService() {
        return generalService;
    }

    public void setGeneralService(GeneralService generalService) {
        this.generalService = generalService;

    }


    @Override
    public void notifyEvent(ListEvent<Student> listEvent) {
        model.setAll(studentService);

        setPaginationPages();
        pagination.setPageFactory(this::createPage);
    }
}
