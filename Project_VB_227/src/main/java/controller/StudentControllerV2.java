package controller;

import entities.Student;
import entities.SystemUser;
import exceptions.AbstractValidatorException;
import exceptions.StudentServiceException;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import observer_utils.AbstractObserver;
import observer_utils.Observable;
import observer_utils.StudentEvent;
import observer_utils.StudentEventType;
import org.hibernate.SessionFactory;
import repository.StudentRepository;
import services.NotaService;
import services.StudentService;
import services.UserSerivce;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;


public class StudentControllerV2 extends AbstractObserver<StudentEvent> {
    private Stage primaryStage;
    private Scene mainMenuScene;
    private AnchorPane studentRootLayout;
    private StudentRepository studentRepository;
    private GaussianBlur gaussianBlur;
    private Scene loadingScene;
    private Pagination studentPagination;
    private Integer numberOfRowsPerPage;
    private List<Student> totiStudentii;
    private NotaService notaService;

    private SessionFactory sessionFactory;

    private StudentService studentService;
    private UserSerivce userSerivce;

    private ObservableList<Student> students = FXCollections.observableArrayList();

    @FXML
    private BorderPane studentBorderPane;

    @FXML
    private TextField fieldNumeStudent;

    @FXML
    private TableColumn<Student, String> studentNumeColoana;

    @FXML
    private TableColumn<Student, String> studentGrupaColoana;

    @FXML
    private TableColumn<Student, String> studentEmailColoana;

    @FXML
    private TableColumn<Student, String> studentProfColoana;

    @FXML
    private TableView<Student> studentTable;

    @FXML
    private ComboBox studentGrupaFilter;

    @FXML
    private ComboBox profesorStudentFilter;

    @FXML
    private Button studentFilterButton;

    @FXML
    private Button studentClearFiltersButton;

    public StudentControllerV2() {
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory.close();
        this.sessionFactory = sessionFactory;
        this.studentRepository.setSessionFactory(sessionFactory);
    }


    @FXML
    public void initialize() throws IOException {
        initTable();
        gaussianBlur = new GaussianBlur();
        numberOfRowsPerPage = ((Double) ((studentTable.getPrefHeight() - 50) / 80)).intValue();
        studentTable.heightProperty().addListener(((observable, oldValue, newValue) -> {
            numberOfRowsPerPage = ((Double) ((newValue.doubleValue() - 50) / 80)).intValue();
            int currentTotalCapacity = numberOfRowsPerPage * studentPagination.getPageCount();
            if (((currentTotalCapacity - totiStudentii.size()) / numberOfRowsPerPage) > 0) {
                Platform.runLater(() -> {
                    studentPagination.setPageCount(((currentTotalCapacity - totiStudentii.size()) / numberOfRowsPerPage));
                });
            } else if (currentTotalCapacity < totiStudentii.size()) {
                Platform.runLater(() -> {
                    studentPagination.setPageCount(studentPagination.getPageCount() +
                            ((totiStudentii.size() - currentTotalCapacity) / numberOfRowsPerPage) + 1);
                });
            }
            changePageCallback(studentPagination.getCurrentPageIndex());
        }));
    }

    private TableView<Student> changePageCallback(int pageIndex) {
        int fromIndex = pageIndex * numberOfRowsPerPage;
        int toIndex = Math.min(fromIndex + numberOfRowsPerPage, totiStudentii.size());
        students.setAll(FXCollections.observableArrayList(totiStudentii.subList(fromIndex, toIndex)));
        return studentTable;
    }

    public void setUserSerivce(UserSerivce userSerivce) {
        this.userSerivce = userSerivce;
    }

    public void setLoadingScene(Scene loadingScene) {
        this.loadingScene = loadingScene;
    }

    public void setNotaService(NotaService notaService) {
        this.notaService = notaService;
    }

    public void loadData() {
//        students.setAll(studentService.findAllStudents());
        totiStudentii = (List<Student>) studentService.findAllStudents();
        studentTable.setItems(students);
        setUpPagination();
    }

    private void setUpPagination() {
        int numberOfPages = totiStudentii.size() / numberOfRowsPerPage;
        if (numberOfPages == 0) {
            studentPagination = new Pagination(1, 0);
        } else if (totiStudentii.size() % numberOfRowsPerPage == 0) {
            studentPagination = new Pagination(numberOfPages, 0);
        } else {
            studentPagination = new Pagination(numberOfPages + 1, 0);
        }
        studentPagination.setPageFactory(this::changePageCallback);
        studentBorderPane.setCenter(studentPagination);
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setStudentService(StudentService studentService) {
        this.studentService = studentService;
        this.studentService.addObserver(this);
    }

    public void setMainMenuScene(Scene mainMenuScene) {
        this.mainMenuScene = mainMenuScene;
    }

    public void setStudentRootLayout(AnchorPane studentRootLayout) {
        this.studentRootLayout = studentRootLayout;
    }

    private void initTable() {
        studentNumeColoana.setCellValueFactory(new PropertyValueFactory<Student, String>("nume"));
        studentGrupaColoana.setCellValueFactory(new PropertyValueFactory<Student, String>("grupa"));
        studentEmailColoana.setCellValueFactory(new PropertyValueFactory<Student, String>("email"));
        studentProfColoana.setCellValueFactory(new PropertyValueFactory<Student, String>("cadruDidacticIndrumator"));
    }


    @Override
    public void updateOnEvent(StudentEvent event) {
        StudentEventType studentEventType = event.getTipEvent();
        if (studentEventType.equals(StudentEventType.ADD)) {
            ObservableList<Student> currentsPageStudents = studentTable.getItems();
            int currentNumberOfPages = studentPagination.getPageCount();
            totiStudentii.add(event.getData());
            if (totiStudentii.size() > (numberOfRowsPerPage * currentNumberOfPages)) {
                Platform.runLater(() -> {
                    studentPagination.setPageCount(currentNumberOfPages + 1);
                });
            }
            if (currentsPageStudents.size() < numberOfRowsPerPage) {
                students.add(event.getData());
            }
        } else if (studentEventType.equals(StudentEventType.REMOVE)) {
            totiStudentii.removeIf(s -> {
                return s.getId().equals(event.getData().getId());
            });
            changePageCallback(studentPagination.getCurrentPageIndex());
            int currentTotalCapacity = numberOfRowsPerPage * studentPagination.getPageCount();
            if (((currentTotalCapacity - totiStudentii.size()) / numberOfRowsPerPage) > 0) {
                Platform.runLater(() -> {
                    studentPagination.setPageCount(((currentTotalCapacity - totiStudentii.size()) / numberOfRowsPerPage));
                });
            }
        } else if (studentEventType.equals(StudentEventType.UPDATE)) {
            totiStudentii.removeIf(s -> {
                return s.getId().equals(event.getData().getId());
            });
            String nume = event.getData().getNume();
            String email = event.getData().getEmail();
            String prof = event.getData().getCadruDidacticIndrumator();
            String grupa = event.getData().getGrupa();
            totiStudentii.add(event.getData());
            changePageCallback(studentPagination.getCurrentPageIndex());
        }

    }

    @FXML
    private void backToMainMenu(MouseEvent mouseEvent) {
        primaryStage.setScene(mainMenuScene);
    }

    @FXML
    private void populateGrupaFilter(MouseEvent mouseEvent) {
        List<String> grupe = students.stream().map(s -> {
            return s.getGrupa();
        }).distinct().collect(Collectors.toList());
//        studentGrupaFilter.getSelectionModel().clearSelection();
        studentGrupaFilter.getItems().clear();
        studentGrupaFilter.getItems().addAll(grupe);
    }

    @FXML
    private void populateProfesorFilter(MouseEvent mouseEvent) {
        List<String> profesori = students.stream().map(s -> {
            return s.getCadruDidacticIndrumator();
        }).distinct().collect(Collectors.toList());
//        profesorStudentFilter.getSelectionModel().clearSelection();
        profesorStudentFilter.getItems().clear();
        profesorStudentFilter.getItems().addAll(profesori);
    }

    @FXML
    private void filterStudents(MouseEvent mouseEvent) {
        String grupa = (String) studentGrupaFilter.getValue();
        String prof = (String) profesorStudentFilter.getValue();
        String nume = fieldNumeStudent.getText();
        if (grupa == null) {
            if (prof == null) {
                Platform.runLater(() -> {
                    List<Student> studenti = studentService.filtreazaStudentiiCuNumele(nume).get();
                    totiStudentii = studenti;
                    if (totiStudentii.size() % numberOfRowsPerPage == 0) {
                        studentPagination.setPageCount(totiStudentii.size() / numberOfRowsPerPage);
                    } else {
                        studentPagination.setPageCount(totiStudentii.size() / numberOfRowsPerPage + 1);
                    }
                    studentPagination.setCurrentPageIndex(0);
                    changePageCallback(0);
                });
            } else if (nume != null) {
                Platform.runLater(() -> {
                    List<Student> studenti = studentService.filtreazaStudentiiCuNumeleSiProf(prof, nume).get();
                    totiStudentii = studenti;
                    if (totiStudentii.size() % numberOfRowsPerPage == 0) {
                        studentPagination.setPageCount(totiStudentii.size() / numberOfRowsPerPage);
                    } else {
                        studentPagination.setPageCount(totiStudentii.size() / numberOfRowsPerPage + 1);
                    }
                    studentPagination.setCurrentPageIndex(0);
                    changePageCallback(0);
                });
            } else {
                Platform.runLater(() -> {
                    List<Student> studenti = studentService.filtreazaStudentiiDinGrupa(grupa).get();
                    totiStudentii = studenti;
                    if (totiStudentii.size() % numberOfRowsPerPage == 0) {
                        studentPagination.setPageCount(totiStudentii.size() / numberOfRowsPerPage);
                    } else {
                        studentPagination.setPageCount(totiStudentii.size() / numberOfRowsPerPage + 1);
                    }
                    studentPagination.setCurrentPageIndex(0);
                    changePageCallback(0);
                });
            }
        } else if (prof == null) {
            if (nume.equals("")) {
                Platform.runLater(() -> {
                    List<Student> studenti = studentService.filtreazaStudentiiDinGrupa(grupa).get();
                    totiStudentii = studenti;
                    if (totiStudentii.size() % numberOfRowsPerPage == 0) {
                        studentPagination.setPageCount(totiStudentii.size() / numberOfRowsPerPage);
                    } else {
                        studentPagination.setPageCount(totiStudentii.size() / numberOfRowsPerPage + 1);
                    }
                    studentPagination.setCurrentPageIndex(0);
                    changePageCallback(0);
                });
            } else {
                Platform.runLater(() -> {
                    List<Student> studenti = studentService.filtreazaStudentiiCuNumeleSiGrupa(grupa, nume).get();
                    totiStudentii = studenti;
                    if (totiStudentii.size() % numberOfRowsPerPage == 0) {
                        studentPagination.setPageCount(totiStudentii.size() / numberOfRowsPerPage);
                    } else {
                        studentPagination.setPageCount(totiStudentii.size() / numberOfRowsPerPage + 1);
                    }
                    studentPagination.setCurrentPageIndex(0);
                    changePageCallback(0);
                });
            }
        } else if (nume.equals("")) {
            Platform.runLater(() -> {
                List<Student> studenti = studentService.filtreazaStudentiiCuGrupaSiProf(grupa, prof).get();
                totiStudentii = studenti;
                if (totiStudentii.size() % numberOfRowsPerPage == 0) {
                    studentPagination.setPageCount(totiStudentii.size() / numberOfRowsPerPage);
                } else {
                    studentPagination.setPageCount(totiStudentii.size() / numberOfRowsPerPage + 1);
                }
                studentPagination.setCurrentPageIndex(0);
                changePageCallback(0);
            });
        } else {
            Platform.runLater(() -> {
                List<Student> studenti = studentService.filtreazaStudentiiCuToateFiltrele(grupa, prof, nume).get();
                totiStudentii = studenti;
                if (totiStudentii.size() % numberOfRowsPerPage == 0) {
                    studentPagination.setPageCount(totiStudentii.size() / numberOfRowsPerPage);
                } else {
                    studentPagination.setPageCount(totiStudentii.size() / numberOfRowsPerPage + 1);
                }
                studentPagination.setCurrentPageIndex(0);
                changePageCallback(0);
            });
        }
    }


    @FXML
    private void studentClearFilters(MouseEvent mouseEvent) {
        studentGrupaFilter.getSelectionModel().clearSelection();
        profesorStudentFilter.getSelectionModel().clearSelection();
        fieldNumeStudent.setText("");
        clearFiltersTabel();
    }

    private void clearFiltersTabel() {
        totiStudentii = studentService.findAllStudents();
        int currentTotalCapacity = numberOfRowsPerPage * studentPagination.getPageCount();
        if (((currentTotalCapacity - totiStudentii.size()) / numberOfRowsPerPage) > 0) {
            int newNrOfPages = ((currentTotalCapacity - totiStudentii.size()) / numberOfRowsPerPage);
            Platform.runLater(() -> {
                studentPagination.setPageCount(newNrOfPages);
            });
        } else if (currentTotalCapacity < totiStudentii.size()) {
            int newNrOfPages = studentPagination.getPageCount() +
                    ((totiStudentii.size() - currentTotalCapacity) / numberOfRowsPerPage) + 1;
            Platform.runLater(() -> {
                studentPagination.setPageCount(newNrOfPages);
            });
        }
        changePageCallback(studentPagination.getCurrentPageIndex());
    }

    public void showAddStudentDialog(MouseEvent mouseEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/student/add_student_view.fxml"));
        AnchorPane addStudentRootLayout = loader.load();
        AddStudentController controller = loader.getController();
        controller.setStudentService(studentService);
        controller.setUserSerivce(userSerivce);
        controller.setStudentViewRootLayout(studentRootLayout);
        Stage popupAddDialog = new Stage(StageStyle.TRANSPARENT);
        popupAddDialog.initStyle(StageStyle.TRANSPARENT);
        controller.setStage(popupAddDialog);
        controller.setRootLayout(addStudentRootLayout);
        popupAddDialog.initOwner(primaryStage);
        popupAddDialog.initModality(Modality.APPLICATION_MODAL);
        Scene addStudentScene = new Scene(addStudentRootLayout);
        addStudentScene.setFill(Color.TRANSPARENT);
        popupAddDialog.setScene(addStudentScene);
        studentRootLayout.getChildren().forEach(node -> {
            node.setDisable(true);
            node.setEffect(gaussianBlur);
        });
        popupAddDialog.show();


    }

    @FXML
    private void stergeStudentHandler(MouseEvent mouseEvent) throws IOException, AbstractValidatorException, StudentServiceException {
        if (studentTable.getSelectionModel().getSelectedItem() != null) {
            Student student = studentTable.getSelectionModel().getSelectedItem();
            notaService.stergeToateNotelePentruUnStudent(student.getId());
            studentService.deleteStudent(student);
            userSerivce.deleteUser(student.getEmail());
        }
    }

    /**
     * Update se bazeaza pe pop up window-ul de adaugare a unui student
     *
     * @param mouseEvent Event-ul care declnseaza toata seria de modificari
     * @throws IOException Daca apar unele probleme la adaugare
     */
    @FXML
    private void updateStudentButtonHandler(MouseEvent mouseEvent) throws IOException {
        if (studentTable.getSelectionModel().getSelectedItem() != null) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/student/update_student_view.fxml"));
            AnchorPane addStudentRootLayout = loader.load();
            UpdateStudentController controller = loader.getController();
            controller.setService(studentService);
            controller.setOldStudentsId(studentTable.getSelectionModel().getSelectedItem().getId());
            controller.setStudentToBeUpdated(studentTable.getSelectionModel().getSelectedItem());
            controller.setStudentRootLayout(studentRootLayout);
            controller.initializeFIeldsWithOldValues();
            Stage popupAddDialog = new Stage(StageStyle.TRANSPARENT);
            controller.setStage(popupAddDialog);
            popupAddDialog.initOwner(primaryStage);
            popupAddDialog.initModality(Modality.APPLICATION_MODAL);
            Scene scene = new Scene(addStudentRootLayout);
            scene.setFill(Color.TRANSPARENT);
            popupAddDialog.setScene(scene);
            studentRootLayout.getChildren().forEach(node -> {
                node.setDisable(true);
                node.setEffect(gaussianBlur);
            });
            popupAddDialog.show();
        }
    }

    public StudentService getStudentService() {
        return studentService;
    }
}
