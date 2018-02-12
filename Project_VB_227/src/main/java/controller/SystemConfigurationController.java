package controller;

import entities.*;
import exceptions.AbstractValidatorException;
import exceptions.StudentServiceException;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import observer_utils.AbstractObserver;
import observer_utils.SystemUserEvent;
import observer_utils.SystemUserEventType;
import services.NotaService;
import services.StudentService;
import services.SystemConfigurationService;
import services.UserSerivce;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class SystemConfigurationController extends AbstractObserver<SystemUserEvent> implements LogOutListener {

    private AnchorPane rootLayout;
    private Stage primaryStage;
    private Scene mainMenuScene;
    private StudentService studentService;
    private UserSerivce userSerivce;
    private SystemConfigurationService systemConfigurationService;
    private ObservableList<SystemUser> usersList = FXCollections.observableArrayList();
    private GaussianBlur gaussianBlur;
    private Pagination systemUsersPagination;
    private Integer numberOfRowsPerPage;
    private NotaService notaService;
    private List<SystemUser> totiUserii;
    @FXML
    private Pane paneEroare;
    @FXML
    private Text mesajEroare;
    @FXML
    private BorderPane systemSettingsBorderPane;
    @FXML
    private TableColumn coloanaEmail;
    //    @FXML
//    private TableColumn coloanaParola;
    @FXML
    private TableColumn coloanaRol;
    @FXML
    private CheckBox checkboxAutentificareActiva;
    @FXML
    private TableView<SystemUser> tabelUsers;

    public void setUserSerivce(UserSerivce userSerivce) {
        this.userSerivce = userSerivce;
        userSerivce.addObserver(this);
    }

    public void setStudentService(StudentService studentService) {
        this.studentService = studentService;
    }

    public void setSystemConfigurationService(SystemConfigurationService systemConfigurationService) {
        this.systemConfigurationService = systemConfigurationService;

    }

    public void setRootLayout(AnchorPane rootLayout) {
        this.rootLayout = rootLayout;
    }

    public void setMainMenuScene(Scene mainMenuScene) {
        this.mainMenuScene = mainMenuScene;
    }

    public void setNotaService(NotaService notaService) {
        this.notaService = notaService;
    }

    @FXML
    public void initialize() {
        coloanaEmail.setCellValueFactory(new PropertyValueFactory<SystemUser, String>("id"));
//        coloanaParola.setCellValueFactory(new PropertyValueFactory<SystemUser, String>("password"));
        coloanaRol.setCellValueFactory(new PropertyValueFactory<SystemUser, Role>("rol"));
        gaussianBlur = new GaussianBlur();
        numberOfRowsPerPage = ((Double) ((tabelUsers.getPrefHeight() - 50) / 80)).intValue();
        tabelUsers.heightProperty().addListener(((observable, oldValue, newValue) -> {
            numberOfRowsPerPage = ((Double) ((newValue.doubleValue() - 50) / 80)).intValue();
            int currentTotalCapacity = numberOfRowsPerPage * systemUsersPagination.getPageCount();
            if (((currentTotalCapacity - totiUserii.size()) / numberOfRowsPerPage) > 0) {
                Platform.runLater(() -> {
                    systemUsersPagination.setPageCount(((currentTotalCapacity - totiUserii.size()) / numberOfRowsPerPage));
                });
            } else if (currentTotalCapacity < totiUserii.size()) {
                Platform.runLater(() -> {
                    systemUsersPagination.setPageCount(systemUsersPagination.getPageCount() +
                            ((totiUserii.size() - currentTotalCapacity) / numberOfRowsPerPage) + 1);
                });
            }
            changePageCallback(systemUsersPagination.getCurrentPageIndex());
        }));
        paneEroare.setVisible(false);
        mesajEroare.setText("");
//        coloanaParola.setCellFactory(new Callback<TableColumn<SystemUser,String>, TableCell<SystemUser,String>>() {
//            @Override
//            public TableCell<SystemUser, String> call(TableColumn<SystemUser, String> param) {
//                return new PasswordFieldCell();
//            }
//        });
    }

    private TableView<SystemUser> changePageCallback(int pageIndex) {
        int fromIndex = pageIndex * numberOfRowsPerPage;
        int toIndex = Math.min(fromIndex + numberOfRowsPerPage, totiUserii.size());
        usersList.setAll(FXCollections.observableArrayList(totiUserii.subList(fromIndex, toIndex)));
        return tabelUsers;
    }

    public void populateTable() {
//        usersList.setAll(userSerivce.findAll());
        tabelUsers.setItems(usersList);
        totiUserii = (List<SystemUser>) userSerivce.findAll();
        checkboxAutentificareActiva.setSelected(systemConfigurationService.isEnabledAuthentication());
        setUpPagination();
    }

    private void setUpPagination() {
        int numberOfPages = totiUserii.size() / numberOfRowsPerPage;
        if (numberOfPages == 0) {
            systemUsersPagination = new Pagination(1, 0);
        } else if (totiUserii.size() % numberOfRowsPerPage == 0) {
            systemUsersPagination = new Pagination(numberOfPages, 0);
        } else {
            systemUsersPagination = new Pagination(numberOfPages + 1, 0);
        }
        systemUsersPagination.setPageFactory(this::changePageCallback);
        systemSettingsBorderPane.setCenter(systemUsersPagination);
    }

    @FXML
    private void handleEnableAuthentication(MouseEvent mouseEvent) throws IOException {
        boolean isAuthenticationEnabled = checkboxAutentificareActiva.selectedProperty().getValue();
        systemConfigurationService.updateEnableAuthentication(isAuthenticationEnabled);
    }

    @Override
    public void updateOnEvent(SystemUserEvent event) {
        SystemUserEventType tip = event.getTipEvent();
        if (tip.equals(SystemUserEventType.ADD)) {
            ObservableList<SystemUser> currentsPageStudents = tabelUsers.getItems();
            int currentNumberOfPages = systemUsersPagination.getPageCount();
            totiUserii.add(event.getData());
            if (totiUserii.size() > (numberOfRowsPerPage * currentNumberOfPages)) {
                Platform.runLater(() -> {
                    systemUsersPagination.setPageCount(currentNumberOfPages + 1);
                });
            }
            if (currentsPageStudents.size() < numberOfRowsPerPage) {
                usersList.add(event.getData());
            }
        } else if (tip.equals(SystemUserEventType.REMOVE)) {
            totiUserii.removeIf(s -> {
                return s.getId().equals(event.getData().getId());
            });
            changePageCallback(systemUsersPagination.getCurrentPageIndex());
            int currentTotalCapacity = numberOfRowsPerPage * systemUsersPagination.getPageCount();
            if (((currentTotalCapacity - totiUserii.size()) / numberOfRowsPerPage) > 0) {
                Platform.runLater(() -> {
                    systemUsersPagination.setPageCount(((currentTotalCapacity - totiUserii.size()) / numberOfRowsPerPage));
                });
            }
        }
    }

    @FXML
    private void addUserButtonHandler(MouseEvent mouseEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/systemConfiguration/add_user_view.fxml"));
        AnchorPane addUserRootLayout = loader.load();
        AddUserController controller = loader.getController();
        controller.setUserSerivce(userSerivce);
        Stage popupAddDialog = new Stage(StageStyle.TRANSPARENT);
        controller.setAddUserStage(popupAddDialog);
        controller.setStudentService(studentService);
        controller.setRootLayout(addUserRootLayout);
        controller.setSysConfigRootLayout(rootLayout);
        popupAddDialog.initOwner(primaryStage);
        popupAddDialog.initModality(Modality.APPLICATION_MODAL);
        Scene addUserScene = new Scene(addUserRootLayout);
        addUserScene.setFill(Color.TRANSPARENT);
        popupAddDialog.setScene(addUserScene);
        //blur the current window before displaying the pop-up
        rootLayout.getChildren().forEach(node -> {
            node.setDisable(true);
            node.setEffect(gaussianBlur);
        });
        popupAddDialog.show();
    }

    @FXML
    private void backTOMainMEnu(MouseEvent mouseEvent) {
        primaryStage.setScene(mainMenuScene);
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }


    @Override
    public void updateOnLogOut() {
        if (!checkboxAutentificareActiva.isSelected()) {
            checkboxAutentificareActiva.setSelected(true);
        }
    }

    @FXML
    private void stergeUserHandler(MouseEvent mouseEvent) {
        SystemUser user = (SystemUser) tabelUsers.getSelectionModel().getSelectedItem();
        //daca este student mai intai stergem studentul
        if (user != null) {
            if (getNumberOfAdministrators() == 1 && user.getRol().equals(Role.ADMINISTRATOR)) {
                mesajEroare.setText("Nu se pot sterge toti administratorii");
                paneEroare.setVisible(true);
            } else {
                if (user.getRol().equals(Role.STUDENT)) {
                    List<Student> students = studentService.findAllStudents();
                    students.forEach(s -> {
                        if (s.getEmail().equals(user.getId())) {
                            try {
                                notaService.stergeToateNotelePentruUnStudent(s.getId());
                                studentService.deleteStudent(s);
                            } catch (AbstractValidatorException e) {
                                e.printStackTrace();
                            } catch (StudentServiceException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
                try {
                    userSerivce.deleteUser(user.getId());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            mesajEroare.setText("Niciun user selectat");
            paneEroare.setVisible(true);
        }

    }

    @FXML
    private void deleteErrorMessage(MouseEvent mouseEvent) {
        paneEroare.setVisible(false);
    }

    @FXML
    private void deleteErrorMessageAndPane(MouseEvent mouseEvent) {
        paneEroare.setVisible(false);
    }

    private int getNumberOfAdministrators() {
        List<SystemUser> useri = userSerivce.findAll();
        AtomicInteger nrOFAdministrators = new AtomicInteger();
        nrOFAdministrators.set(0);
        useri.forEach(user -> {
            if (user.getRol().equals(Role.ADMINISTRATOR)) {
                nrOFAdministrators.getAndIncrement();
            }
        });
        return nrOFAdministrators.get();
    }
}
