package Graphics.Teachers.Students;

import Domain.Student;
import Domain.StudentNotaDTO;
import Exceptions.GuiException;
import Exceptions.RepositoryException;
import Exceptions.ValidationException;
import Graphics.AlertMessage;
import Graphics.Teachers.TeacherController;
import Services.StudentService;
import Utilities.ListEvent;
import Utilities.Observer;
import Utilities.TextFieldFormatter;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.util.Callback;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class StudentTableController implements Observer<Student> {
    private TeacherController rootController;
    private StudentService service;
    @FXML private TableColumn numeColumn;
    @FXML private TableColumn grupaColumn;
    @FXML private TableColumn emailColumn;
    @FXML private TableColumn profColumn;
    @FXML private TableColumn notaColumn;

    @FXML private Label taskName;
    @FXML private Button backButton;
    @FXML private Button giveGradeBtn;
    @FXML private Button updateGradeBtn;
    @FXML private Button mailButton;
    @FXML private TextField numeField;
    @FXML private TextField gradeField;
    @FXML private TextField weekField;
    @FXML private TextArea obsArea;

    private Domain.Task currentTask;
    @FXML private Pane loadingPane;
    @FXML private TableView<StudentNotaDTO> tableView;
    @FXML private TableView othersTable;
    private List<StudentNotaDTO> dtoList = new ArrayList<>();

    @FXML Pagination mainPagination;
    ObservableList<StudentNotaDTO> model = FXCollections.observableArrayList();
    ObservableList<Student> othersModel = FXCollections.observableArrayList();

    private int rowsPerPage = 12;
    private int rowsPerPageOthers = 9;
    public StudentTableController() {}

    @FXML
    public void initialize() {
        numeField.setDisable(true);

        tableView.setItems(model);
        othersTable.setItems(othersModel);
        addTableSelections();
        addButtonActions();
        addGradesInTable();
        restrictFieldsInput();
        stopLoading();

    }

    private void restrictFieldsInput() {
        for (TextField field : Arrays.asList(gradeField, weekField)) {
            TextFieldFormatter.formatNumericField(field);
        }
        TextFieldFormatter.formatNameField(numeField);
    }

    private void addGradesInTable() {
        List<Integer> cols = new ArrayList<>();
        for (Object obj : tableView.getItems()) {
            Student student = (Student)obj;
            cols.add(rootController
                    .getGradeService()
                    .findByStudentAndTask(student.getId(), currentTask.getId())
                    .getValoare());

        }

    }

    private void clearFields() {
        for (TextField textField : Arrays.asList(numeField, gradeField, weekField)) {
            textField.setText("");
        }
        obsArea.setText("");
    }

    private void addButtonActions() {
        backButton.setOnAction(event -> {
            rootController.loadTasks();
            giveGradeBtn.setDisable(true);
            updateGradeBtn.setDisable(true);
            mailButton.setDisable(true);
            clearFields();
        });
        giveGradeBtn.setDisable(true);
        updateGradeBtn.setDisable(true);
        mailButton.setDisable(true);
        ImageView imageView = new ImageView("/resources/email-envelope-button.png");
        imageView.setFitHeight(20d);
        imageView.setFitWidth(20d);
        mailButton.setGraphic(imageView);
        giveGradeBtn.setOnAction(event -> {

            Student student = (Student) othersTable.getSelectionModel().getSelectedItem();
            for (; ; ) {
                try {
                    checkFields();
                    rootController.getGradeService().add(
                            (int) (Math.random() * 10000),
                            student.getId(),
                            currentTask.getId(),
                            Integer.parseInt(gradeField.getText()),
                            Integer.parseInt(weekField.getText()),
                            obsArea.getText(),
                            rootController.getTaskService().getRepository(),
                            service.getRepository()
                    );
                    rootController.loadStudents(currentTask);
                    //refreshMainPage();
                    //refreshOthersPage();
                    break;
                } catch (RepositoryException| GuiException e) {
                    AlertMessage.show("ERROR", "Student error",e.getMessage(), Alert.AlertType.ERROR);
                    clearFields();
                    break;
                }

            }

        });

        updateGradeBtn.setOnAction(event -> {
            try {
                checkFields();
                StudentNotaDTO student = tableView.getSelectionModel().getSelectedItem();
                //update grade
                rootController.getGradeService().update(
                        rootController
                                .getGradeService()
                                .findByStudentAndTask(student.getId(), currentTask.getId())
                                .getId(),
                        Integer.parseInt(gradeField.getText()),
                        Integer.parseInt(weekField.getText()),
                        obsArea.getText(),
                        rootController.getTaskService()
                );
                rootController.loadStudents(currentTask);
                //refreshMainPage();
                //refreshOthersPage();
            } catch (GuiException|RepositoryException| ValidationException e) {
                AlertMessage.show("ERROR", "Grade error",e.getMessage(), Alert.AlertType.ERROR);
                clearFields();
            }
        });



        mailButton.setOnAction(event -> {
            //send mail
            Task<Void> task = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    startLoading();
                    handleSendMail();
                    //tableView.getSelectionModel().clearSelection();
                    clearFields();
                    return null;
                }
            };
            task.setOnSucceeded(event1 -> {
                AlertMessage.show("Success", "Mail sent successfully", "Mail sent to " + tableView.getSelectionModel().getSelectedItem().getEmail(), Alert.AlertType.CONFIRMATION);
                stopLoading();
                clearFields();
                tableView.getSelectionModel().clearSelection();
            });
            task.setOnFailed(event3 -> {
                AlertMessage.show("error", "mail error", "mail could not be sent", Alert.AlertType.ERROR);
                stopLoading();
                clearFields();
                tableView.getSelectionModel().clearSelection();
            });
            new Thread(task).start();

        });

    }

    private void startLoading() {
        loadingPane.setVisible(true);
    }

    private void stopLoading() {
        loadingPane.setVisible(false);
    }

    private void handleSendMail() {
        // create a gmail and use the id and password here
        // in gmail account settings >> allow less secure applications
        final String user = "***";
        final String pass = "***";
        Properties props = new Properties();
        props.put("mail.smtp.user", user);
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.starttls.enable","true");
        props.put("mail.smtp.debug", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        Session session=Session.getInstance(props,new GMailAuthenticator(user, pass));
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(user));
            //add recipient
            message.addRecipients(
                    Message.RecipientType.TO, InternetAddress.parse(
                            tableView.getSelectionModel().getSelectedItem().getEmail()
                    )
            );
            message.setSubject("MAP Proiect facultativ");

            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText("Check attachment for your activity (This is a test mail sent from java)");
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            // add attachment
            messageBodyPart = new MimeBodyPart();
            DataSource dataSource = new FileDataSource(
                    String.valueOf(tableView.getSelectionModel().getSelectedItem().getId()) + ".txt"
            );
            messageBodyPart.setDataHandler(new DataHandler(dataSource));
            multipart.addBodyPart(messageBodyPart);

            message.setContent(multipart);

            Transport.send(message);

            updateGradeBtn.setDisable(true);
            giveGradeBtn.setDisable(true);
            mailButton.setDisable(true);

        } catch (MessagingException msg){
            //msg.printStackTrace();
            AlertMessage.show("error", "mail error", msg.getMessage(), Alert.AlertType.ERROR);
        }

    }

    static class GMailAuthenticator extends Authenticator {
        String user;
        String pw;
        public GMailAuthenticator (String username, String password)
        {
            super();
            this.user = username;
            this.pw = password;
        }
        public PasswordAuthentication getPasswordAuthentication()
        {
            return new PasswordAuthentication(user, pw);
        }
    }

    private void checkFields() {
        if(weekField.getText().isEmpty() ||
                gradeField.getText().isEmpty() ||
                obsArea.getText().isEmpty()) {
            throw new GuiException("some fields are empty!");
        }

    }

    private Node createMainPage(int pageIndex) {
        int fromIndex = pageIndex * rowsPerPage;
        int toIndex = Math.min(fromIndex + rowsPerPage, getDTOObjects(service.getAllAsList()).size());
        tableView.setItems(FXCollections.observableArrayList(getDTOObjects(service.getAllAsList()).subList(fromIndex, toIndex)));
        return new BorderPane(tableView);
    }


    private void refreshMainPage(){
        int size = getDTOObjects(service.getAllAsList()).size();
        int pageCount = size / rowsPerPage;
        if (size % rowsPerPage > 0){
            pageCount = pageCount + 1;
        }
        mainPagination.setPageCount(pageCount);
        mainPagination.setPageFactory(this::createMainPage);

    }

    private void setupMainPagination() {
        mainPagination.setPageCount(getDTOObjects(service.getAllAsList()).size() / rowsPerPage + 1);
        mainPagination.setCurrentPageIndex(0);
        mainPagination.setPageFactory(new Callback<Integer, Node>() {
            @Override
            public Node call(Integer param) {
                return createMainPage(param);
            }
        });

        mainPagination.currentPageIndexProperty().addListener(new javafx.beans.value.ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                model.setAll(getDTOObjects(service.getAllAsList()).subList(newValue.intValue(),
                        (newValue.intValue() + rowsPerPage))
                );
            }
        });
    }


    public void setupPaginations() {
        setupMainPagination();
        //setupOthersPagination();
    }

    private void addTableSelections() {
        tableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<StudentNotaDTO>() {
            @Override
            public void changed(ObservableValue observable, StudentNotaDTO oldValue, StudentNotaDTO newValue) {
                if (newValue != null) {
                    //update buttons
                    updateGradeBtn.setDisable(false);
                    giveGradeBtn.setDisable(true);
                    mailButton.setDisable(false);
                    tableView.requestFocus();
                    //fill fields
                    fillFields(newValue);
                }
            }
        });

        othersTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Student>() {
            @Override
            public void changed(ObservableValue observable, Student oldValue, Student newValue) {
                if (newValue != null) {
                    //update buttons
                    updateGradeBtn.setDisable(true);
                    giveGradeBtn.setDisable(false);
                    mailButton.setDisable(true);
                    othersTable.requestFocus();
                    fillFields(new StudentNotaDTO(
                            newValue.getId(),
                            newValue.getNume(),
                            newValue.getGrupa(),
                            newValue.getEmail(),
                            newValue.getProfesor(), 0)
                    );
                }
            }
        });
    }

    private void fillFields(StudentNotaDTO student) {
        numeField.setText(student.getStudentName());
        try {
            gradeField.setText(
                    String.valueOf(rootController
                            .getGradeService()
                            .findByStudentAndTask(student.getId(), currentTask.getId()).getValoare())
            );
        } catch (RepositoryException e) {
            //selected from others table
            //System.out.println("selected from others table");
            gradeField.setText("");

        }
        weekField.setText("");
        obsArea.setText("");

    }

    @Override
    public void notifyEvent(ListEvent<Student> ev) {
        model.setAll(getDTOObjects(service.getAllAsList()));
        refreshMainPage();
    }

    public void setService(StudentService service) {
        this.service = service;

    }

    private List<StudentNotaDTO> getDTOObjects(List<Student> list) {
        List<StudentNotaDTO> res = new ArrayList<>();
        list.forEach(x -> {
            try {
                res.add(new StudentNotaDTO(x.getId(), x.getNume(), x.getGrupa(), x.getEmail(), x.getProfesor(),
                        rootController.getGradeService().findByStudentAndTask(x.getId(), currentTask.getId()).getValoare()));
            } catch (RepositoryException e) {
                //lol
            }

        });
        return res;
    }

    public StudentService getService() {
        return service;
    }

    public void setTable(List list, List others,Domain.Task task) {
        currentTask = task;
        model.setAll(getDTOObjects(list));
        othersModel.setAll(others);
        this.taskName.setText(task.getDescriere());

    }

    public void setRootController(TeacherController rootController) {
        this.rootController = rootController;
    }

}
