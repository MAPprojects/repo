package GUI.StudentsGUI;

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
import Utils.ListEvent;
import Utils.Observer;
import Validator.ValidatorException;
import com.jfoenix.controls.JFXTextField;
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
import java.util.ResourceBundle;

public class AddStdController extends AbstractController implements Observer<Student> {
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
    ComboBox comboBoxGroupAdd;

    @FXML
    TextField textFieldSearch;

    @FXML
    ImageView imageViewStd;

    @FXML
    Button buttonBackAddStd;

    @FXML
    Button buttonAdd;

    @FXML
    TextField textFieldName;

    @FXML
    TextField textFieldID;

    @FXML
    TextField textFieldEmail;

    @FXML
    TextField textFieldProf;

    private Node stdView;

    public void setStudentService(StudentService studentService) {
        this.studentService = studentService;

        this.colStdID.setCellValueFactory(new PropertyValueFactory<Student,Integer>("idStudent"));
        this.colStdName.setCellValueFactory(new PropertyValueFactory<Student, String>("name"));
        this.colStdGroup.setCellValueFactory(new PropertyValueFactory<Student, Integer>("group"));
        this.colStdEmail.setCellValueFactory(new PropertyValueFactory<Student, String>("email"));
        this.colStdProf.setCellValueFactory(new PropertyValueFactory<Student, String>("professor"));

        tableViewStd.getColumns().setAll(colStdID,colStdName,colStdGroup,colStdEmail,colStdProf);


        ObservableList<String> groups =FXCollections.observableArrayList("look for a group...","220","221","222","223","224","225","226","227");

        comboBoxGroupAdd.setItems(groups);
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

    public void setGradeService(GradeService gradeService) {
        this.gradeService = gradeService;
    }

    public void setAssignmentService(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    public void setGeneralService(GeneralService generalService) {
        this.generalService = generalService;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        File file = new File("5.jpg");
        Image image = new Image(file.toURI().toString());
        imageViewStd.setImage(image);


    }

    public AddStdController(StudentService studentService, GradeService gradeService, AssignmentService assignmentService, GeneralService generalService) {
        this.studentService = studentService;
        this.gradeService = gradeService;
        this.assignmentService = assignmentService;
        this.generalService = generalService;
    }

    public void handleBackAddStd() throws IOException {
        stdView  = InitView.initStdView(studentService,assignmentService,gradeService,generalService,user);
        contentStd.getChildren().setAll(stdView);

        AnchorPane.setTopAnchor(stdView, 0d);
        AnchorPane.setBottomAnchor(stdView, 0d);
        AnchorPane.setLeftAnchor(stdView, 0d);
        AnchorPane.setRightAnchor(stdView, 0d);
    }

    public void handleAdd() {
        try {
            studentService.addStudent(Integer.parseInt(textFieldID.getText()),textFieldName.getText(),Integer.parseInt(comboBoxGroupAdd.getSelectionModel().getSelectedItem().toString()),textFieldEmail.getText(),textFieldProf.getText());
        } catch (ValidatorException e) {
            Message.showMessage(Alert.AlertType.WARNING,"add student",e.getMessage());
        } catch (RepositoryException e) {
            Message.showMessage(Alert.AlertType.WARNING,"add student",e.getMessage());
        }
        setPage();
    }

    public void handleSearch() throws RepositoryException {
        this.model.setAll(generalService.getAllStdName(textFieldSearch.getText()));
        setPage();
    }

    public AddStdController() {
    }

    @Override
    public void notifyEvent(ListEvent<Student> listEvent) {
        try {
            model.setAll(studentService.getAllStudents());
        } catch (RepositoryException e) {
            e.printStackTrace();
        }
        setPage();
    }
}
