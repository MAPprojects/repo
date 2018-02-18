package GUI.GradesGUI;

import Domain.Grade;
import Domain.User;
import GUI.AbstractController;
import GUI.InitView;
import Main.StartApplication;
import Repository.RepositoryException;
import Service.AssignmentService;
import Service.GeneralService;
import Service.GradeService;
import Service.StudentService;
import Utils.ListEvent;
import Utils.Observer;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class GrdController extends AbstractController implements Observer<Grade> {
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
        if (user == User.userStudent) {
            buttonAddGrd.setDisable(true);
            buttonUpdGrd.setDisable(true);
        }
    }

    ObservableList model = FXCollections.observableArrayList();

    int rows_per_page=20;

    @FXML
    Pagination pagination;


    @FXML
    AnchorPane contentAsg;

    @FXML
    TableView<Grade> tableViewGrd = new TableView();

    @FXML
    TableColumn<Grade,String> colGrdNameStd = new TableColumn("student");

    @FXML
    TableColumn<Grade, String> colGrdDescAsg = new TableColumn("assignment");

    @FXML
    TableColumn<Grade, Integer> colGrdValue = new TableColumn("value");

    @FXML
    TableColumn<Grade, String> colGrdNotes = new TableColumn("notes");

    @FXML
    Button buttonAddGrd;

    @FXML
    Button buttonUpdGrd;

    @FXML
    TextField textFieldSearchNameStd;

    @FXML
    TextField textFieldSearchAsgDesc;

    @FXML
    ImageView imageViewGrd;

    @FXML
    Button buttonBackGrd;

    @FXML
    TextField textFieldGroup;

    private Node homeView;
    private Node addGrdView;
    private Node updGrdView;

    public GrdController(StudentService studentService, AssignmentService assignmentService, GradeService gradeService, GeneralService generalService) {
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

        this.colGrdNameStd.setCellValueFactory(c-> new SimpleStringProperty(c.getValue().getStd().getName()));
        this.colGrdDescAsg.setCellValueFactory(c-> new SimpleStringProperty(c.getValue().getAsg().getDescription()));
        this.colGrdValue.setCellValueFactory(c-> new SimpleObjectProperty<>(c.getValue().getValue()));
        this.colGrdNotes.setCellValueFactory(c-> new SimpleStringProperty(c.getValue().getNotes()));

        tableViewGrd.getColumns().addAll(colGrdNameStd,colGrdDescAsg,colGrdValue,colGrdNotes);
        try {
            model = FXCollections.observableArrayList(gradeService.getAll());

        } catch (RepositoryException e) {

        }
        //tableViewGrd.setItems(model);
        setPage();
    }

    public GrdController() {
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

    @Override
    public void notifyEvent(ListEvent<Grade> listEvent) {
        try {
            model.setAll(gradeService.getAll());
        } catch (RepositoryException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        File file = new File("5.jpg");
        Image image = new Image(file.toURI().toString());
        imageViewGrd.setImage(image);
    }

    public void handleBackGrd() {
        homeView = InitView.initHomeView(studentService,assignmentService,gradeService,generalService,user);
        //contentAsg.getChildren().setAll(homeView);
        StartApplication.getScene().setRoot((Parent) homeView);
        AnchorPane.setTopAnchor(homeView, 0d);
        AnchorPane.setBottomAnchor(homeView, 0d);
        AnchorPane.setLeftAnchor(homeView, 0d);
        AnchorPane.setRightAnchor(homeView, 0d);

    }

    public void handleAddGrd() {
        addGrdView = InitView.initAddGrdView(studentService,assignmentService,gradeService,generalService,user);
        contentAsg.getChildren().setAll(addGrdView);
        AnchorPane.setTopAnchor(addGrdView, 0d);
        AnchorPane.setBottomAnchor(addGrdView, 0d);
        AnchorPane.setLeftAnchor(addGrdView, 0d);
        AnchorPane.setRightAnchor(addGrdView, 0d);
    }

    public void handleUpdGrd() {
        updGrdView = InitView.initUpdGrdView(studentService,assignmentService,gradeService,generalService,user);
        contentAsg.getChildren().setAll(updGrdView);
        AnchorPane.setTopAnchor(updGrdView, 0d);
        AnchorPane.setBottomAnchor(updGrdView, 0d);
        AnchorPane.setLeftAnchor(updGrdView, 0d);
        AnchorPane.setRightAnchor(updGrdView, 0d);
    }



    public void handleGroup() throws RepositoryException {
        if(textFieldGroup.getText().equals(""))
            model.setAll(gradeService.getAll());
        else
            model.setAll(generalService.getAllGrdGroup(Integer.parseInt(textFieldGroup.getText())));
        setPage();
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
