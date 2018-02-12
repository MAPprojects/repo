package controller;

import com.sun.javafx.scene.control.skin.LabeledText;
import entities.LogInListener;
import entities.LogOutListener;
import entities.Role;
import entities.Tema;
import exceptions.TemaServiceException;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import observer_utils.*;
import org.hibernate.SessionFactory;
import repository.TemaRepository;
import services.NotaService;
import services.TemaService;
import thread_utils.NotifyingThread;
import thread_utils.ThreadCompleteListener;

import javax.persistence.Table;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class TemaController extends AbstractObserver<TemaEvent> implements ThreadCompleteListener, LogInListener, LogOutListener {

    private Integer numberOfRowsPerPage;
    private AnchorPane temaSceneRootLayout;
    private ImageView loadingImageView;
    private ObservableList<Tema> teme = FXCollections.observableArrayList();
    private List<Tema> toateTemele;
    private Scene mainMenuScene;
    private Stage primaryStage;
    private TemaService temaService;
    private SessionFactory sessionFactory;
    private NotaService notaService;
    private TemaRepository temaRepository;
    private GaussianBlur gaussianBlur;
    private boolean areElementsLoaded;
    private Pagination temePagination;
    @FXML
    private Button stergeButton;
    @FXML
    private BorderPane temeBorderPane;
    @FXML
    private Pane paneMesajeEroare;
    @FXML
    private AnchorPane temaRootAnchorPane;
    @FXML
    private Pane userOptionsPane;
    @FXML
    private Text textCerinta;
    @FXML
    private ComboBox termenPredareComboBox;
    @FXML
    private TextArea cerintaTextArea;
    @FXML
    private Text mesajEroarePrelungire;
    @FXML
    private TextField prelungireTextField;
    @FXML
    private Pane prelungireInputPane;
    @FXML
    private TableColumn<Tema, String> coloanaCerinta;
    @FXML
    private TableColumn<Tema, Integer> coloanaTermenPredare;

    @FXML
    private TableView<Tema> tableTeme;
    @FXML
    private Button temaBackButton;
    @FXML
    private Button addButton;
    @FXML
    private Button prelungireButton;

    public TemaService getTemaService() {
        return temaService;
    }

    public TemaController() {
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory.close();
        this.sessionFactory = sessionFactory;
        temaRepository.setSessionFactory(sessionFactory);
    }

    @FXML
    private void initialize() {
        areElementsLoaded = false;
        initTable();
        prelungireInputPane.setVisible(false);
        cerintaTextArea.setWrapText(false);
        gaussianBlur = new GaussianBlur();
        numberOfRowsPerPage = ((Double) ((tableTeme.getPrefHeight() - 50) / 85)).intValue();
        temaRootAnchorPane.heightProperty().addListener(((observable, oldValue, newValue) -> {
            userOptionsPane.setLayoutY(temaSceneRootLayout.getHeight() - userOptionsPane.getPrefHeight() - 20);
        }));
//        tableTeme.setFixedCellSize(100);
        tableTeme.heightProperty().addListener(((observable, oldValue, newValue) -> {
            numberOfRowsPerPage = ((Double) ((newValue.doubleValue() - 50) / 85)).intValue();
            int currentTotalCapacity = numberOfRowsPerPage * temePagination.getPageCount();
            if (((currentTotalCapacity - toateTemele.size()) / numberOfRowsPerPage) > 0) {
                Platform.runLater(() -> {
                    temePagination.setPageCount(((currentTotalCapacity - toateTemele.size()) / numberOfRowsPerPage));
                });
            } else if (currentTotalCapacity < toateTemele.size()) {
                Platform.runLater(() -> {
                    temePagination.setPageCount(temePagination.getPageCount() +
                            ((toateTemele.size() - currentTotalCapacity) / numberOfRowsPerPage) + 1);
                });
            }
            changePageCallback(temePagination.getCurrentPageIndex());
        }));
        paneMesajeEroare.setVisible(false);
        areElementsLoaded = true;
    }

    private Node changePageCallback(int pageIndex) {
        int fromIndex = pageIndex * numberOfRowsPerPage;
        int toIndex = Math.min(fromIndex + numberOfRowsPerPage, toateTemele.size());
        teme.setAll(FXCollections.observableArrayList(toateTemele.subList(fromIndex, toIndex)));
        return tableTeme;
    }

    private void initTable() {
        coloanaCerinta.setCellValueFactory(new PropertyValueFactory<Tema, String>("cerinta"));
        coloanaTermenPredare.setCellValueFactory(new PropertyValueFactory<Tema, Integer>("termenPredare"));
    }

    public void setTemaSceneRootLayout(AnchorPane temaSceneRootLayout) {
        this.temaSceneRootLayout = temaSceneRootLayout;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void loadData() {
//        teme.setAll(temaService.findAllTeme());
        toateTemele = (List<Tema>) temaService.findAllTeme();
        tableTeme.setItems(teme);
        setUpPagination();
    }

    private void setUpPagination() {
        int numberOfPages = toateTemele.size() / numberOfRowsPerPage;
        if (numberOfPages == 0) {
            temePagination = new Pagination(1, 0);
        } else if (toateTemele.size() % numberOfRowsPerPage == 0) {
            temePagination = new Pagination(numberOfPages, 0);
        } else {
            temePagination = new Pagination(numberOfPages + 1, 0);
        }
        temePagination.setPageFactory(this::changePageCallback);
        temeBorderPane.setCenter(temePagination);
    }

    public void setTemaService(TemaService temaService) {
        this.temaService = temaService;
        this.temaService.addObserver(this);
    }

    public void setNotaService(NotaService notaService) {
        this.notaService = notaService;
    }

    public void setMainMenuScene(Scene mainMenuScene) {
        this.mainMenuScene = mainMenuScene;
    }

    @FXML
    private void backButtonHandler(MouseEvent mouseEvent) {
        primaryStage.setScene(mainMenuScene);
    }

    @FXML
    private void addButtonHandler(MouseEvent mouseEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/tema/add_tema_view.fxml"));
        AnchorPane addTemaRootLayout = loader.load();
        AddTemaController controller = loader.getController();
        controller.setTemaService(temaService);
        controller.setRootLayout(addTemaRootLayout);
        controller.setTemaViewRootLayout(temaSceneRootLayout);
        Stage popupAddDialog = new Stage(StageStyle.TRANSPARENT);
        controller.setAddTemaDialogStage(popupAddDialog);
        popupAddDialog.initOwner(primaryStage);
        popupAddDialog.initStyle(StageStyle.TRANSPARENT);
        popupAddDialog.initModality(Modality.APPLICATION_MODAL);
        Scene addTemaScene = new Scene(addTemaRootLayout);
        addTemaScene.setFill(Color.TRANSPARENT);
        popupAddDialog.setScene(addTemaScene);
        temaSceneRootLayout.getChildren().forEach(node -> {
            if (node != loadingImageView) {
                node.setDisable(true);
                node.setEffect(gaussianBlur);
            }
        });
        popupAddDialog.show();
    }

    @FXML
    private void prelungireButtonHandler(MouseEvent mouseEvent) {
        if ((tableTeme.getSelectionModel().getSelectedItem() != null) &&
                (!cerintaTextArea.getText().equals(""))) {
            prelungireTextField.setText("");
            mesajEroarePrelungire.setText("");
            prelungireInputPane.setVisible(true);
            paneMesajeEroare.setVisible(false);
        } else {
            prelungireTextField.setText("");
            mesajEroarePrelungire.setText("Niciun obiect selectat");
            prelungireInputPane.setVisible(false);
            paneMesajeEroare.setVisible(true);
        }
    }

    @FXML
    private void prelungireHandler(KeyEvent keyEvent) throws FileNotFoundException {
        if (keyEvent.getCode().equals(KeyCode.ENTER)) {
            String noulTermen = prelungireTextField.getText();
            FileInputStream loadingGifInputStream = new FileInputStream("src\\main\\resources\\views\\loadingScene\\loader.gif");
            Image loadingImage = new Image(loadingGifInputStream);
            loadingImageView = new ImageView(loadingImage);
            temaSceneRootLayout.getChildren().add(loadingImageView);
            loadingImageView.setX(temaSceneRootLayout.getWidth() / 2 - 85);
            loadingImageView.setY(temaSceneRootLayout.getHeight() / 2 - 100);
            temaSceneRootLayout.getChildren().forEach(node -> {
                if (node != loadingImageView) {
                    node.setDisable(true);
                    node.setEffect(gaussianBlur);
                }
            });
            NotifyingThread notifyingThread = new NotifyingThread() {
                @Override
                public void doRun() {
                    Tema tema = tableTeme.getSelectionModel().getSelectedItem();
                    try {
                        temaService.actualizeazaTermenulDePredare(tema.getId(), Integer.parseInt(noulTermen));
                    } catch (NumberFormatException e) {
                        mesajEroarePrelungire.setText("Format invalid.");
                        paneMesajeEroare.setVisible(true);
                    } catch (IOException e) {
                        mesajEroarePrelungire.setText(e.getMessage());
                        paneMesajeEroare.setVisible(true);
                    } catch (TemaServiceException e) {
                        mesajEroarePrelungire.setText(e.getMessage());
                        paneMesajeEroare.setVisible(true);
                    }
                }
            };
            notifyingThread.addListener(this);
            notifyingThread.start();
        }
    }

    @FXML
    private void borderPaneHandler(MouseEvent mouseEvent) {
        prelungireInputPane.setVisible(false);
        mesajEroarePrelungire.setText("");
        paneMesajeEroare.setVisible(false);
        cerintaTextArea.setText("");
    }

    @FXML
    private void clickTableHandler(MouseEvent mouseEvent) {
        String text;
        if (mouseEvent.getTarget().getClass().getSimpleName().equals("LabeledText")) {
            text = ((LabeledText) mouseEvent.getPickResult().getIntersectedNode()).getText();
        } else {
            text = ((TableCell) mouseEvent.getPickResult().getIntersectedNode()).getText();
        }
        if (text != null) {
            cerintaTextArea.setText(tableTeme.getSelectionModel().getSelectedItem().getCerinta());
        } else {
            paneMesajeEroare.setVisible(false);
            prelungireInputPane.setVisible(false);
            cerintaTextArea.setText("");
        }
    }

    @FXML
    private void filtreazaTemaHandler(MouseEvent mouseEvent) {
        Integer termenPredare = (Integer) termenPredareComboBox.getValue();
        String cerinta = cerintaTextArea.getText();
        if (termenPredare != null || !cerinta.equals("")) {
            if (cerinta.equals("")) {
                Platform.runLater(() -> {
                    List<Tema> teme = temaService.filtreazaTermenPredare(termenPredare).get();
                    toateTemele = teme;
                    if (toateTemele.size() % numberOfRowsPerPage == 0) {
                        temePagination.setPageCount(toateTemele.size() / numberOfRowsPerPage);
                    } else {
                        temePagination.setPageCount(toateTemele.size() / numberOfRowsPerPage + 1);
                    }
                    temePagination.setCurrentPageIndex(0);
                    changePageCallback(0);
                });
            }
            if (termenPredare == null) {
                Platform.runLater(() -> {
                    List<Tema> teme = temaService.filtreazaCerinta(cerinta).get();
                    toateTemele = teme;
                    if (toateTemele.size() % numberOfRowsPerPage == 0) {
                        temePagination.setPageCount(toateTemele.size() / numberOfRowsPerPage);
                    } else {
                        temePagination.setPageCount(toateTemele.size() / numberOfRowsPerPage + 1);
                    }
                    temePagination.setCurrentPageIndex(0);
                    changePageCallback(0);
                });
            } else {
                Platform.runLater(() -> {
                    List<Tema> teme = temaService.filtreazaTermenPredareSiCerinta(cerinta, termenPredare).get();
                    toateTemele = teme;
                    if (toateTemele.size() % numberOfRowsPerPage == 0) {
                        temePagination.setPageCount(toateTemele.size() / numberOfRowsPerPage);
                    } else {
                        temePagination.setPageCount(toateTemele.size() / numberOfRowsPerPage + 1);
                    }
                    temePagination.setCurrentPageIndex(0);
                    changePageCallback(0);
                });
            }
        }
    }

    @FXML
    private void stergeFiltreHandler(MouseEvent mouseEvent) {
        cerintaTextArea.setText("");
        termenPredareComboBox.getSelectionModel().clearSelection();
        removeFIlterTable();
//        teme.setAll(temaService.findAllTeme());
    }

    private void removeFIlterTable() {
        toateTemele = temaService.findAllTeme();
        int currentTotalCapacity = numberOfRowsPerPage * temePagination.getPageCount();
        if (((currentTotalCapacity - toateTemele.size()) / numberOfRowsPerPage) > 0) {
            int newNrOfPages = ((currentTotalCapacity - toateTemele.size()) / numberOfRowsPerPage);
            Platform.runLater(() -> {
                temePagination.setPageCount(newNrOfPages);
            });
        } else if (currentTotalCapacity < toateTemele.size()) {
            int newNrOfPages = temePagination.getPageCount() +
                    ((toateTemele.size() - currentTotalCapacity) / numberOfRowsPerPage) + 1;
            Platform.runLater(() -> {
                temePagination.setPageCount(newNrOfPages);
            });
        }
        changePageCallback(temePagination.getCurrentPageIndex());
    }
    
    @FXML
    private void populateTermenPredareCombo(MouseEvent mouseEvent) {
        List<Integer> termeneDePredare = teme.stream().map(t -> {
            return t.getTermenPredare();
        }).distinct().collect(Collectors.toList());
        termenPredareComboBox.getItems().clear();
        termenPredareComboBox.getItems().setAll(termeneDePredare);
    }

    @Override
    public void updateOnEvent(TemaEvent event) {
        TemaEventType temaEventType = event.getTipEvent();
        if (temaEventType.equals(TemaEventType.ADD)) {
            toateTemele.add(event.getData());
            int currentNumberOfPages = temePagination.getPageCount();
            if (toateTemele.size() > (numberOfRowsPerPage * currentNumberOfPages)) {
                Platform.runLater(() -> {
                    temePagination.setPageCount(currentNumberOfPages + 1);
                });
            }
            changePageCallback(temePagination.getCurrentPageIndex());
        } else if (temaEventType.equals(TemaEventType.REMOVE)) {
            toateTemele.removeIf(s -> {
                return s.getId().equals(event.getData().getId());
            });
            changePageCallback(temePagination.getCurrentPageIndex());
            int currentTotalCapacity = numberOfRowsPerPage * temePagination.getPageCount();
            if (((currentTotalCapacity - toateTemele.size()) / numberOfRowsPerPage) > 0) {
                Platform.runLater(() -> {
                    temePagination.setPageCount(((currentTotalCapacity - toateTemele.size()) / numberOfRowsPerPage));
                });
            }
        } else if (temaEventType.equals(TemaEventType.PRELUNGIRE)) {
            teme.removeIf(t -> {
                return t.getId().equals(event.getData().getId());
            });
            toateTemele.removeIf(t -> {
                return t.getCerinta().equals(event.getData().getCerinta());
            });
            toateTemele.add(event.getData());
            changePageCallback(temePagination.getCurrentPageIndex());
        }
    }

    @FXML
    private void stergeTemaHandler(MouseEvent mouseEvent) throws IOException {
        if (tableTeme.getSelectionModel().getSelectedItem() != null) {
            Tema tema = (Tema) tableTeme.getSelectionModel().getSelectedItem();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/tema/delete_tema.fxml"));
            AnchorPane deleteTemaRootLayout = loader.load();
            DeleteTemaController deleteTemaController = loader.getController();
            deleteTemaController.setNotaService(notaService);
            deleteTemaController.setTemaRootLayout(temaSceneRootLayout);
            deleteTemaController.setTemaService(temaService);
            deleteTemaController.setTemaToBeDeleted(tema);
            Scene deleteTemaScene = new Scene(deleteTemaRootLayout);
            Stage deleteTemaStage = new Stage(StageStyle.TRANSPARENT);
            deleteTemaScene.setFill(Color.TRANSPARENT);
            deleteTemaStage.initOwner(primaryStage);
            deleteTemaStage.initStyle(StageStyle.TRANSPARENT);
            deleteTemaStage.initModality(Modality.APPLICATION_MODAL);
            deleteTemaStage.setScene(deleteTemaScene);
            deleteTemaController.setThisStage(deleteTemaStage);
            temaSceneRootLayout.getChildren().forEach(node -> {
                if (node != loadingImageView) {
                    node.setDisable(true);
                    node.setEffect(gaussianBlur);
                }
            });
            deleteTemaStage.show();
        } else {
            mesajEroarePrelungire.setText("Nicio tema selectata");
            paneMesajeEroare.setVisible(true);
        }
    }

    @Override
    public void notifyOfThreadComplete(Thread thread) {
        Platform.runLater(() -> {
            temaSceneRootLayout.getChildren().remove(loadingImageView);
            temaSceneRootLayout.getChildren().forEach(node -> {
                node.setDisable(false);
                node.setEffect(null);
            });
        });
    }


    @Override
    public void updateOfLogIn(Role roleOfTheUser) {
        if (roleOfTheUser.equals(Role.STUDENT)) {
            addButton.setVisible(false);
            stergeButton.setVisible(false);
            prelungireButton.setVisible(false);
        } else if (roleOfTheUser.equals(Role.ADMINISTRATOR)) {
            addButton.setVisible(true);
            stergeButton.setVisible(true);
            prelungireButton.setVisible(true);
        } else if (roleOfTheUser.equals(Role.PROFESOR)) {
            addButton.setVisible(true);
            stergeButton.setVisible(false);
            prelungireButton.setVisible(true);
        }
    }

    @Override
    public void updateOnLogOut() {
        paneMesajeEroare.setVisible(false);
        prelungireTextField.setText("");
        cerintaTextArea.setText("");
    }
}
