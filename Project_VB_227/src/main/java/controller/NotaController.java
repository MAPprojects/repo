package controller;

import entities.*;
import exceptions.AbstractValidatorException;
import exceptions.NotaServiceException;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import observer_utils.*;
import org.hibernate.SessionFactory;
import services.NotaService;
import services.StudentService;
import services.TemaService;
import thread_utils.NotifyingThread;
import thread_utils.ThreadCompleteListener;
import vos.NotaVO;

import javax.mail.MessagingException;
import javax.xml.bind.ValidationException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class NotaController extends AbstractObserver<NotaEvent> implements ThreadCompleteListener, LogInListener, LogOutListener {

    private Role roleOFCurrentUser;
    private static final String MESAJ_EROARE_STUDENT_VID = "Niciun student selectat.";
    private static final String MESAJ_EROARE_TEMA_VIDA = "Nicio tema selectata.";
    private static final String MESAJ_EROARE_NR_SAPTAMANA_INVALIDA = "Introduceti o saptamana valida.";
    private static final String MESAJ_EROARE_NOTA_INVALIDA = "Introduceti o nota valida.";
    private static final String MESAJ_EROARE_UPDATE_LIPSA_SELECTIE = "Selectati o nota din tabel pentru a o modifica.";
    private Pagination notaPagination;
    private Integer numberOfRowsPerPage;
    private List<NotaVO> toateNotele;
    private ImageView loadingImageView;
    private GaussianBlur gaussianBlur;
    private NotaService notaService;
    private StudentService studentService;
    private TemaService temaService;
    private Stage primaryStage;
    private Scene menuScene;
    private SessionFactory sessionFactory;
    private AnchorPane notaRootLayout;
    private ObservableList<NotaVO> noteVO = FXCollections.observableArrayList();
    @FXML
    private Button buttonStergeNota;
    @FXML
    private Button butonUpdateNota;
    @FXML
    private Button butonAdaugaNota;
    @FXML
    private Pane paneEroareNota;
    @FXML
    private Pane paneEroareNrSapt;
    @FXML
    private Pane paneEroareCerinta;
    @FXML
    private ImageView paneEroareNume;
    @FXML
    private ImageView grassImageVIew;
    @FXML
    private Pane paneImaginiRezolutieMare;
    @FXML
    private ImageView redAngryBirdImageVIew;
    @FXML
    private BorderPane noteBorderPane;
    @FXML
    private Pane paneOptiuniNota;
    @FXML
    private Pane paneCenterBorderPaneNota;
    @FXML
    private TextArea observatiiNota;
    @FXML
    private TextField valoareNotaField;
    @FXML
    private TextField nrSapatamanField;
    @FXML
    private Text mesajEroareValoareNota;
    @FXML
    private Text mesajEroareNrSaptamana;
    @FXML
    private Text mesajEroareCerinta;
    @FXML
    private Text mesajEroareNumeStudent;
    @FXML
    private TableColumn coloanaNume;
    @FXML
    private TableColumn coloanaCerinta;
    @FXML
    private TableColumn coloanaNota;
    @FXML
    private TableView<NotaVO> tabelNote;
    @FXML
    private TextField numeStudentInput;
    @FXML
    private ListView<Student> listViewNumeStudent;
    @FXML
    private Button backButton;
    @FXML
    private TextArea cerintaTextArea;
    @FXML
    private ListView<Tema> temaSuggestions;
    @FXML
    private Text mesajEroareAddOrUpdate;
    @FXML
    private Slider sliderNota;
    private Student selectedStudent;
    private Tema selectedTema;

    public void setNotaService(NotaService notaService) {
        this.notaService = notaService;
        this.notaService.addObserver(this);
    }

    public void setStudentService(StudentService studentService) {
        this.studentService = studentService;
    }

    public void setTemaService(TemaService temaService) {
        this.temaService = temaService;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setMenuScene(Scene menuScene) {
        this.menuScene = menuScene;
    }

    public void setNotaRootLayout(AnchorPane notaRootLayout) {
        this.notaRootLayout = notaRootLayout;
    }

    public void setRoleOFCurrentUser(Role roleOFCurrentUser) {
        this.roleOFCurrentUser = roleOFCurrentUser;
    }

    @FXML
    private void initialize() throws IOException {
        temaSuggestions.setVisible(false);
        listViewNumeStudent.setVisible(false);
        initTable();
        paneEroareNume.setVisible(false);
        mesajEroareValoareNota.setText("");
        mesajEroareNrSaptamana.setText("");
        mesajEroareCerinta.setText("");
        mesajEroareNumeStudent.setText("");
        gaussianBlur = new GaussianBlur();
        paneImaginiRezolutieMare.setVisible(false);
        paneEroareCerinta.setVisible(false);
        paneEroareNrSapt.setVisible(false);
        paneEroareNota.setVisible(false);
        paneCenterBorderPaneNota.widthProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue.doubleValue() != 0) {
                paneOptiuniNota.setLayoutX(newValue.doubleValue() / 2 - paneOptiuniNota.getWidth() / 2);
            }
        });
        numberOfRowsPerPage = ((Double) ((tabelNote.getPrefHeight() - 50) / 80)).intValue();
        tabelNote.heightProperty().addListener(((observable, oldValue, newValue) -> {
            numberOfRowsPerPage = ((Double) ((newValue.doubleValue() - 50) / 80)).intValue();
            int currentTotalCapacity = numberOfRowsPerPage * notaPagination.getPageCount();
            if (((currentTotalCapacity - toateNotele.size()) / numberOfRowsPerPage) > 0) {
                Platform.runLater(() -> {
                    notaPagination.setPageCount(((currentTotalCapacity - toateNotele.size()) / numberOfRowsPerPage));
                });
            } else if (currentTotalCapacity < toateNotele.size()) {
                Platform.runLater(() -> {
                    notaPagination.setPageCount(notaPagination.getPageCount() +
                            ((toateNotele.size() - currentTotalCapacity) / numberOfRowsPerPage) + 1);
                });
            }
            changePageCallback(notaPagination.getCurrentPageIndex());
        }));
        tabelNote.heightProperty().addListener((observable, oldValue, newValue) -> {
            redAngryBirdImageVIew.setLayoutX(paneOptiuniNota.getLayoutX() + paneOptiuniNota.getPrefWidth() + 40);
            grassImageVIew.setLayoutY(paneCenterBorderPaneNota.getHeight() - 240);
            if (((paneCenterBorderPaneNota.getWidth() / 2) - (paneOptiuniNota.getWidth() / 2)) > 200) {
                paneImaginiRezolutieMare.setVisible(true);
            } else {
                paneImaginiRezolutieMare.setVisible(false);
            }
        });
    }

    private TableView<NotaVO> changePageCallback(int pageIndex) {
        int fromIndex = pageIndex * numberOfRowsPerPage;
        int toIndex = Math.min(fromIndex + numberOfRowsPerPage, toateNotele.size());
        noteVO.setAll(FXCollections.observableArrayList(toateNotele.subList(fromIndex, toIndex)));
        return tabelNote;
    }

    public void loadData() {
//        noteVO.setAll(notaService.findAllNote());
        tabelNote.setItems(noteVO);
        toateNotele = (List<NotaVO>) notaService.findAllNote();
        setUpPagination();
    }

    private void setUpPagination() {
        int numberOfPages = toateNotele.size() / numberOfRowsPerPage;
        if (numberOfPages == 0) {
            notaPagination = new Pagination(1, 0);
        } else if (toateNotele.size() % numberOfRowsPerPage == 0) {
            notaPagination = new Pagination(numberOfPages, 0);
        } else {
            notaPagination = new Pagination(numberOfPages + 1, 0);
        }
        notaPagination.setPageFactory(this::changePageCallback);
        notaPagination.setMinWidth(379);
        noteBorderPane.setRight(notaPagination);
    }

    private void initTable() {
        coloanaNume.setCellValueFactory(new PropertyValueFactory<NotaVO, String>("numeStudent"));
        coloanaNota.setCellValueFactory(new PropertyValueFactory<NotaVO, Integer>("valoare"));
        coloanaCerinta.setCellValueFactory(new PropertyValueFactory<NotaVO, String>("cerintaTema"));
    }


    @FXML
    private void showStudentsRelatedToInput(KeyEvent keyEvent) {
        List<Student> students = studentService.findAllStudents();
        List<Student> suggestions = students.stream().filter(s -> {
            return s.getNume().contains(numeStudentInput.getText());
        }).collect(Collectors.toList());
        listViewNumeStudent.getItems().setAll(suggestions);
        listViewNumeStudent.setVisible(true);
    }

    @FXML
    private void anchorPaneClickHandler(MouseEvent mouseEvent) {
        listViewNumeStudent.setVisible(false);
        temaSuggestions.setVisible(false);
        if (numeStudentInput.getText().equals("")) {
            selectedStudent = null;
        }
        if (cerintaTextArea.getText().equals("")) {
            selectedTema = null;
        }
        clearErrorMessages();
    }

    @FXML
    private void selectStudentHandler(MouseEvent mouseEvent) {
        selectedStudent = listViewNumeStudent.getSelectionModel().getSelectedItem();
        if (selectedStudent != null) {
            numeStudentInput.setText(selectedStudent.getNume());
            listViewNumeStudent.setVisible(false);
        }

    }

    @FXML
    private void selectTema(MouseEvent mouseEvent) {
        selectedTema = temaSuggestions.getSelectionModel().getSelectedItem();
        if (selectedTema != null) {
            cerintaTextArea.setText(selectedTema.getCerinta());
            temaSuggestions.setVisible(false);
        }

    }

    @FXML
    private void showTemaSuggestions(KeyEvent keyEvent) {
        List<Tema> teme = temaService.findAllTeme();
        List<Tema> suggestions = teme.stream().filter(s -> {
            return s.getCerinta().contains(cerintaTextArea.getText());
        }).collect(Collectors.toList());
        temaSuggestions.getItems().setAll(suggestions);
        temaSuggestions.setVisible(true);
    }

    @FXML
    private void backToMenuHandler(MouseEvent mouseEvent) {
        primaryStage.setScene(menuScene);
    }

    @Override
    public void updateOnEvent(NotaEvent event) {
        NotaEventType notaEventType = event.getTipEvent();
        NotaVO data = event.getData();
        if (notaEventType.equals(NotaEventType.ADD)) {
//            noteVO.add(event.getData());
            ObservableList<NotaVO> currentsPageStudents = tabelNote.getItems();
            int currentNumberOfPages = notaPagination.getPageCount();
            toateNotele.add(event.getData());
            if (toateNotele.size() > (numberOfRowsPerPage * currentNumberOfPages)) {
                Platform.runLater(() -> {
                    notaPagination.setPageCount(currentNumberOfPages + 1);
                });
            }
            if (currentsPageStudents.size() < numberOfRowsPerPage) {
                noteVO.add(event.getData());
            }
        } else if (notaEventType.equals(NotaEventType.UPDATE)) {
            final NotaVO[] nota = new NotaVO[1];
//            noteVO.forEach(x -> {
//                if (x.getIdStudent().equals(data.getIdStudent()) && x.getIdTema().equals(data.getIdTema())) {
//                    nota[0] = x;
//                }
//            });
//            String idStudent = data.getIdStudent();
//            String idTema = data.getIdTema();
//            Integer valoare = data.getValoare();
//            String numeStudent = nota[0].getNumeStudent();
//            String cerintaTema = data.getCerintaTema();
//            toateNotele.add(new NotaVO(idStudent, idTema, valoare, numeStudent, cerintaTema));
//            changePageCallback(notaPagination.getCurrentPageIndex());

            toateNotele.forEach(x -> {
                if (x.getIdStudent().equals(data.getIdStudent()) && x.getIdTema().equals(data.getIdTema())) {
                    nota[0] = x;
                }
            });
            toateNotele.remove(nota[0]);
            String idStudent = data.getIdStudent();
            String idTema = data.getIdTema();
            Integer valoare = data.getValoare();
            String numeStudent = nota[0].getNumeStudent();
            String cerintaTema = data.getCerintaTema();
            toateNotele.add(new NotaVO(idStudent, idTema, valoare, numeStudent, cerintaTema));
            changePageCallback(notaPagination.getCurrentPageIndex());
        } else if (notaEventType.equals(NotaEventType.REMOVE)) {
            toateNotele.removeIf(n -> {
                return n.getIdStudent().equals(event.getData().getIdStudent()) &&
                        n.getIdTema().equals(event.getData().getIdTema());
            });
            changePageCallback(notaPagination.getCurrentPageIndex());
            int currentTotalCapacity = numberOfRowsPerPage * notaPagination.getPageCount();
            if (((currentTotalCapacity - toateNotele.size()) / numberOfRowsPerPage) > 0) {
                Platform.runLater(() -> {
                    notaPagination.setPageCount(((currentTotalCapacity - toateNotele.size()) / numberOfRowsPerPage));
                });
            }
        }
    }

    public NotaService getNotaService() {
        return notaService;
    }

    public StudentService getStudentService() {

        return studentService;
    }

    public TemaService getTemaService() {
        return temaService;
    }

    @FXML
    private void addNotaHandler(MouseEvent mouseEvent) {
        clearErrorMessages();
        if (validateInformations()) {
            FileInputStream loadingGifInputStream = null;
            try {
                loadingGifInputStream = new FileInputStream("src\\main\\resources\\views\\loadingScene\\loader.gif");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Image loadingImage = new Image(loadingGifInputStream);
            loadingImageView = new ImageView(loadingImage);
            notaRootLayout.getChildren().add(loadingImageView);
            loadingImageView.setX(notaRootLayout.getWidth() / 2 - 80);
            loadingImageView.setY(notaRootLayout.getHeight() / 2 - 100);
            notaRootLayout.getChildren().forEach(node -> {
                if (node != loadingImageView) {
                    node.setDisable(true);
                    node.setEffect(gaussianBlur);
                }
            });
            Integer nrSaptamana = Integer.parseInt(nrSapatamanField.getText());
            Integer valaoreNota = Integer.parseInt(valoareNotaField.getText());
            NotifyingThread notifyingThread = new NotifyingThread() {
                @Override
                public void doRun() {
                    String observatii = observatiiNota.getText();
                    try {
                        stergeFiltreTabel();
                        Platform.runLater(() -> {
                            sliderNota.setValue(0);
                        });
                        notaService.addNota(selectedStudent.getId(), selectedTema.getId(), nrSaptamana, valaoreNota, observatii);
                    } catch (AbstractValidatorException e) {
                        e.printStackTrace();
                    } catch (ValidationException e) {
                        e.printStackTrace();
                    } catch (NotaServiceException e) {
                        paneEroareNume.setVisible(true);
                        mesajEroareNumeStudent.setText(e.getMessage());
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }
                }
            };
            notifyingThread.addListener(this);
            notifyingThread.start();
            clearInputFields();
        }
    }

    private boolean validateInformations() {
        boolean validInfo = true;
        if (selectedStudent == null) {
            validInfo = false;
            paneEroareNume.setVisible(true);
            mesajEroareNumeStudent.setText(MESAJ_EROARE_STUDENT_VID);
        }
        if (selectedTema == null) {
            validInfo = false;
            paneEroareCerinta.setVisible(true);
            mesajEroareCerinta.setText(MESAJ_EROARE_TEMA_VIDA);
        }
        try {
            if (Integer.parseInt(nrSapatamanField.getText()) < 1 || Integer.parseInt(nrSapatamanField.getText()) > 14) {
                validInfo = false;
                paneEroareNrSapt.setVisible(true);
                mesajEroareNrSaptamana.setText(MESAJ_EROARE_NR_SAPTAMANA_INVALIDA);
            }
        } catch (NumberFormatException e) {
            validInfo = false;
            paneEroareNrSapt.setVisible(true);
            mesajEroareNrSaptamana.setText(MESAJ_EROARE_NR_SAPTAMANA_INVALIDA);
        }
        try {
            if (Integer.parseInt(valoareNotaField.getText()) > 10 || Integer.parseInt(valoareNotaField.getText()) < 1) {
                validInfo = false;
                paneEroareNota.setVisible(true);
                mesajEroareValoareNota.setText(MESAJ_EROARE_NOTA_INVALIDA);
            }
        } catch (NumberFormatException e) {
            validInfo = false;
            paneEroareNota.setVisible(true);
            mesajEroareValoareNota.setText(MESAJ_EROARE_NOTA_INVALIDA);
        }
        return validInfo;
    }

    @FXML
    private void updateButtonHandler(MouseEvent mouseEvent) {
        clearErrorMessages();
        boolean infoValida = validateInformationsForUpdate();
        if (infoValida) {
            NotaVO selectedNota = tabelNote.getSelectionModel().getSelectedItem();
            Nota notaNoua = new Nota(selectedNota.getIdStudent(), selectedNota.getIdTema(), Integer.parseInt(valoareNotaField.getText()));
            FileInputStream loadingGifInputStream = null;
            try {
                loadingGifInputStream = new FileInputStream("src\\main\\resources\\views\\loadingScene\\loader.gif");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Image loadingImage = new Image(loadingGifInputStream);
            loadingImageView = new ImageView(loadingImage);
            notaRootLayout.getChildren().add(loadingImageView);
            loadingImageView.setX(notaRootLayout.getWidth() / 2 - 80);
            loadingImageView.setY(notaRootLayout.getHeight() / 2 - 100);
            notaRootLayout.getChildren().forEach(node -> {
                if (node != loadingImageView) {
                    node.setDisable(true);
                    node.setEffect(gaussianBlur);
                }
            });
            NotifyingThread notifyingThread = new NotifyingThread() {
                @Override
                public void doRun() {
                    try {
                        notaService.updateNota(notaNoua, Integer.parseInt(nrSapatamanField.getText()));
                    } catch (NotaServiceException e) {
                        mesajEroareNumeStudent.setText(e.getMessage());
                        paneEroareNume.setVisible(true);
                    } catch (AbstractValidatorException e) {
                        mesajEroareNumeStudent.setText(e.getMessage());
                        paneEroareNume.setVisible(true);
                    } catch (ValidationException e) {
                        mesajEroareNumeStudent.setText(e.getMessage());
                        paneEroareNume.setVisible(true);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }
                }
            };
            notifyingThread.addListener(this);
            notifyingThread.start();
            clearInputFields();
        }
    }

    private boolean validateInformationsForUpdate() {
        boolean infoValide = true;
        if (numeStudentInput.getText().equals("") && cerintaTextArea.getText().equals("")) {
            infoValide = false;
            paneEroareNume.setVisible(true);
            mesajEroareNumeStudent.setText(MESAJ_EROARE_UPDATE_LIPSA_SELECTIE);
        } else {
            if (nrSapatamanField.getText().equals("")) {
                infoValide = false;
                paneEroareNrSapt.setVisible(true);
                mesajEroareNrSaptamana.setText(MESAJ_EROARE_NR_SAPTAMANA_INVALIDA);
            }
            if (valoareNotaField.getText().equals("")) {
                infoValide = false;
                paneEroareNota.setVisible(true);
                mesajEroareValoareNota.setText(MESAJ_EROARE_NOTA_INVALIDA);
            }
        }
        return infoValide;
    }

    private void clearErrorMessages() {
        mesajEroareValoareNota.setText("");
        mesajEroareNrSaptamana.setText("");
        mesajEroareCerinta.setText("");
        mesajEroareNumeStudent.setText("");
        mesajEroareAddOrUpdate.setText("");
        paneEroareCerinta.setVisible(false);
        paneEroareNume.setVisible(false);
        paneEroareNrSapt.setVisible(false);
        paneEroareNota.setVisible(false);
    }

    public void clearInputFields() {
        numeStudentInput.setText("");
        cerintaTextArea.setText("");
        nrSapatamanField.setText("");
        valoareNotaField.setText("");
    }


    @FXML
    private void completeDetailsForSelectedStudent(MouseEvent mouseEvent) {
        if (tabelNote.getSelectionModel().getSelectedItem() != null) {
            numeStudentInput.setText(tabelNote.getSelectionModel().getSelectedItem().getNumeStudent());
            cerintaTextArea.setText(tabelNote.getSelectionModel().getSelectedItem().getCerintaTema());
        }
    }

    @FXML
    private void filterHandler(MouseEvent mouseEvent) {
        if (numeStudentInput.getText().equals("")) {
            if (cerintaTextArea.getText().equals("")) {
                if (sliderNota.getValue() != 0) {
                    Platform.runLater(() -> {
                        List<NotaVO> note = notaService.filtreazaPentruValoare((int) sliderNota.getValue()).get();
                        toateNotele = note;
                        if (toateNotele.size() % numberOfRowsPerPage == 0) {
                            notaPagination.setPageCount(toateNotele.size() / numberOfRowsPerPage);
                        } else {
                            notaPagination.setPageCount(toateNotele.size() / numberOfRowsPerPage + 1);
                        }
                        notaPagination.setCurrentPageIndex(0);
                        changePageCallback(0);
                    });
                }
            } else if (sliderNota.getValue() != 0) {
                try {
                    List<NotaVO> note = notaService.filtreazaNotelePentruCerintaSiNota(cerintaTextArea.getText(), (int) sliderNota.getValue()).get();
                    toateNotele = note;
                    Platform.runLater(() -> {
                        if (toateNotele.size() % numberOfRowsPerPage == 0) {
                            notaPagination.setPageCount(toateNotele.size() / numberOfRowsPerPage);
                        } else {
                            notaPagination.setPageCount(toateNotele.size() / numberOfRowsPerPage + 1);
                        }
                        notaPagination.setCurrentPageIndex(0);
                        changePageCallback(0);
                    });
                } catch (NotaServiceException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    List<NotaVO> note = notaService.filtreazaNotelePentruCerinta(cerintaTextArea.getText()).get();
                    toateNotele = note;
                    Platform.runLater(() -> {
                        if (toateNotele.size() % numberOfRowsPerPage == 0) {
                            notaPagination.setPageCount(toateNotele.size() / numberOfRowsPerPage);
                        } else {
                            notaPagination.setPageCount(toateNotele.size() / numberOfRowsPerPage + 1);
                        }
                        notaPagination.setCurrentPageIndex(0);
                        changePageCallback(0);
                    });
                } catch (NotaServiceException e) {
                    e.printStackTrace();
                }
            }
        } else {
            if (cerintaTextArea.getText().equals("")) {
                if (sliderNota.getValue() == 0) {
                    List<NotaVO> note = notaService.filtreazaNotelePentruUnStudent(numeStudentInput.getText()).get();
                    toateNotele = note;
                    Platform.runLater(() -> {
                        if (toateNotele.size() % numberOfRowsPerPage == 0) {
                            notaPagination.setPageCount(toateNotele.size() / numberOfRowsPerPage);
                        } else {
                            notaPagination.setPageCount(toateNotele.size() / numberOfRowsPerPage + 1);
                        }
                        notaPagination.setCurrentPageIndex(0);
                        changePageCallback(0);
                    });
                } else {
                    try {
                        List<NotaVO> note = notaService.filtreazaNotelePentruStudentSiNota(numeStudentInput.getText(), (int) sliderNota.getValue()).get();
                        toateNotele = note;
                        Platform.runLater(() -> {
                            if (toateNotele.size() % numberOfRowsPerPage == 0) {
                                notaPagination.setPageCount(toateNotele.size() / numberOfRowsPerPage);
                            } else {
                                notaPagination.setPageCount(toateNotele.size() / numberOfRowsPerPage + 1);
                            }
                            notaPagination.setCurrentPageIndex(0);
                            changePageCallback(0);
                        });
                    } catch (NotaServiceException e) {
                        e.printStackTrace();
                    }

                }
            } else if (sliderNota.getValue() == 0) {
                try {
                    List<NotaVO> note = notaService.filtreazaNotelePentruStudentSiCerinta(numeStudentInput.getText(), cerintaTextArea.getText()).get();
                    toateNotele = note;
                    Platform.runLater(() -> {
                        if (toateNotele.size() % numberOfRowsPerPage == 0) {
                            notaPagination.setPageCount(toateNotele.size() / numberOfRowsPerPage);
                        } else {
                            notaPagination.setPageCount(toateNotele.size() / numberOfRowsPerPage + 1);
                        }
                        notaPagination.setCurrentPageIndex(0);
                        changePageCallback(0);
                    });
                } catch (NotaServiceException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    List<NotaVO> note = notaService.filtreazaPentruToateCriteriile(numeStudentInput.getText(), cerintaTextArea.getText(), (int) sliderNota.getValue()).get();
                    toateNotele = note;
                    Platform.runLater(() -> {
                        if (toateNotele.size() % numberOfRowsPerPage == 0) {
                            notaPagination.setPageCount(toateNotele.size() / numberOfRowsPerPage);
                        } else {
                            notaPagination.setPageCount(toateNotele.size() / numberOfRowsPerPage + 1);
                        }
                        notaPagination.setCurrentPageIndex(0);
                        changePageCallback(0);
                    });
                } catch (NotaServiceException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void notifyOfThreadComplete(Thread thread) {
        Platform.runLater(() -> {
            notaRootLayout.getChildren().remove(loadingImageView);
            notaRootLayout.getChildren().forEach(node -> {
                node.setDisable(false);
                node.setEffect(null);
            });
        });
    }

    @FXML
    private void stergeFiltre(MouseEvent mouseEvent) {
        stergeFiltreTabel();
//        toateNotele = notaService.findAllNote();
//        int currentTotalCapacity = numberOfRowsPerPage * notaPagination.getPageCount();
//        if (((currentTotalCapacity - toateNotele.size()) / numberOfRowsPerPage) > 0) {
//            Platform.runLater(() -> {
//                notaPagination.setPageCount(((currentTotalCapacity - toateNotele.size()) / numberOfRowsPerPage));
//            });
//        } else if (currentTotalCapacity < toateNotele.size()) {
//            Platform.runLater(() -> {
//                notaPagination.setPageCount(notaPagination.getPageCount() +
//                        ((toateNotele.size() - currentTotalCapacity) / numberOfRowsPerPage) + 1);
//            });
//        }
//        changePageCallback(notaPagination.getCurrentPageIndex());
    }

    private void stergeFiltreTabel() {
        //first we remove the data
        numeStudentInput.setText("");
        cerintaTextArea.setText("");
        sliderNota.setValue(0);
        observatiiNota.setText("");
        nrSapatamanField.setText("");
        valoareNotaField.setText("");

        toateNotele = notaService.findAllNote();
        int currentTotalCapacity = numberOfRowsPerPage * notaPagination.getPageCount();
        if (((currentTotalCapacity - toateNotele.size()) / numberOfRowsPerPage) > 0) {
            int newNrOfPages = ((currentTotalCapacity - toateNotele.size()) / numberOfRowsPerPage);
            Platform.runLater(() -> {
                notaPagination.setPageCount(newNrOfPages);
            });
        } else if (currentTotalCapacity < toateNotele.size()) {
            int newNrOfPages = notaPagination.getPageCount() +
                    ((toateNotele.size() - currentTotalCapacity) / numberOfRowsPerPage) + 1;
            Platform.runLater(() -> {
                notaPagination.setPageCount(newNrOfPages);
            });
        }
        changePageCallback(notaPagination.getCurrentPageIndex());
    }

    @Override
    public void updateOfLogIn(Role roleOfTheUser) {
        if (roleOfTheUser.equals(Role.STUDENT)) {
            butonAdaugaNota.setVisible(false);
            butonUpdateNota.setVisible(false);
            buttonStergeNota.setVisible(false);
        } else {
            butonAdaugaNota.setVisible(true);
            butonUpdateNota.setVisible(true);
            buttonStergeNota.setVisible(true);
        }
    }

    @FXML
    private void deleteNota(MouseEvent mouseEvent) {
        NotaVO nota = tabelNote.getSelectionModel().getSelectedItem();
        if (nota == null) {
            paneEroareNume.setVisible(true);
            mesajEroareNumeStudent.setText("Nicio tema selectata");
        } else {
            try {
                notaService.deleteNota(new Nota(nota.getIdStudent(), nota.getIdTema(), nota.getValoare()));
                clearInputFields();
            } catch (NotaServiceException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void updateOnLogOut() {
        clearInputFields();
        observatiiNota.setText("");
    }
}


