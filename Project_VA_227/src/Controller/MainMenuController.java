package Controller;

import Domain.*;
import Repository.EventDatabaseRepository;
import Repository.GradesDatabaseRepository;
import Repository.HomeworkDatabaseRepository;
import Repository.StudentDatabaseRepository;
import Service.*;
import Utils.ResourceManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import javax.crypto.Cipher;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.BufferedWriter;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;

public class MainMenuController implements Initializable {

    private static String FROM;
    private static String password;

    public Menu userWelcome;
    public ImageView imageView;
    public Label userDetails;
    public MenuItem add_admin;
    public MenuItem mailoption;

    private User user;

    public MainMenuController() {
    }

    public void setUser(User user) {
        this.user = user;
        userWelcome.setText("Hi, " + user.getUsername());
        imageView.setImage(user.getAvatarImg());
        imageView.setClip(new Circle(50, 50, 45));
        userDetails.setText(String.format("%s\n%s\n", user.getName(), user.getEmail()));
        if (user.getTip() == Tip.USER) {
            add_admin.setVisible(false);
            mailoption.setVisible(false);
        }
        mailoption.setText(user.getMailOption() == 0 ? "Enable Emails" : "Disable Emails");
    }

    private static AnchorPane studentsLayout, homeworkLayout, noteLayout, statisticsAvgLayout, statisticsHomeworksLayout,
            eligibleStudentsLayout, UpToDateStudentsLayout;

    @FXML
    BorderPane mainLayout;

    @FXML
    public void handleClose() {
        Platform.exit();
    }

    @FXML
    public void handleStudents() {
        if (!studentController.userIsSet())
            studentController.setUser(user);
        mainLayout.setCenter(studentsLayout);
    }

    @FXML
    public void handleHomeworks() {
        if (!homeworkController.userIsSet())
            homeworkController.setUser(user);
        mainLayout.setCenter(homeworkLayout);
    }

    @FXML
    public void handleNotes() {
        if (!noteController.userIsSet())
            noteController.setUser(user);
        mainLayout.setCenter(noteLayout);
    }

    private static StudentService studentService;
    private static HomeworkService homeworkService;
    private static NoteService noteService;
    private static StatisticsService statisticsService;

    private static StudentController studentController;
    private static HomeworkController homeworkController;
    private static NoteController noteController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        studentService = null;
        homeworkService = null;
        noteService = null;

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainMenuController.class.getResource("/View/StudentView.fxml"));
            studentService = new StudentService(new Validator(new StundentValidator()), new StudentDatabaseRepository());
            studentsLayout = loader.load();
            studentController = loader.getController();
            studentController.setService(studentService);
        } catch (Exception ignored) {
        }
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainMenuController.class.getResource("/View/HomeworkView.fxml"));
            homeworkService = new HomeworkService(new Validator(new HomeworkValidator()), new HomeworkDatabaseRepository());
            homeworkLayout = loader.load();
            homeworkController = loader.getController();
            homeworkController.setService(homeworkService);
        } catch (Exception ignored) {
        }
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainMenuController.class.getResource("/View/NoteView.fxml"));
            noteService = new NoteService(new Validator(new NoteValidator()), new GradesDatabaseRepository(), studentService,
                    homeworkService);
            noteLayout = loader.load();
            noteController = loader.getController();
            noteController.setService(noteService);
            studentService.setNoteService(noteService);
            homeworkService.setNoteService(noteService);
        } catch (Exception ignored) {
        }

        statisticsService = new StatisticsService(studentService, homeworkService, noteService);

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainMenuController.class.getResource("/View/StatisticsAvg.fxml"));
            statisticsAvgLayout = loader.load();
            StatisticsController statisticsController = loader.getController();
            statisticsController.setStatisticsService(statisticsService);
        } catch (Exception ignored) {
        }

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainMenuController.class.getResource("/View/StatisticsHomeworks.fxml"));
            statisticsHomeworksLayout = loader.load();
            StatisticsController statisticsController = loader.getController();
            statisticsController.setStatisticsService(statisticsService);
        } catch (Exception ignored) {
        }

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainMenuController.class.getResource("/View/StatisticsStudents1.fxml"));
            eligibleStudentsLayout = loader.load();
            StatisticsController statisticsController = loader.getController();
            statisticsController.setStatisticsService(statisticsService);
        } catch (Exception ignored) {
        }

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainMenuController.class.getResource("/View/StatisticsStudents2.fxml"));
            UpToDateStudentsLayout = loader.load();
            StatisticsController statisticsController = loader.getController();
            statisticsController.setStatisticsService(statisticsService);
        } catch (Exception ignored) {
        }


        try {
            FROM = ResourceManager.getResourceMangager().getValue("FromEmail");
            byte[] publicKeybytes = Files.readAllBytes(Paths.get(ResourceManager.getResourceMangager().getValue("PubKey")));
            String encryptedText = Files.readAllLines(Paths.get(ResourceManager.getResourceMangager().getValue("EmailPassword"))).get(0);
            KeyFactory keyPairGenerator = KeyFactory.getInstance("RSA");
            PublicKey publicKey = keyPairGenerator.generatePublic(new X509EncodedKeySpec(publicKeybytes));
            password = decryptMessage(encryptedText, publicKey);
        } catch (Exception ignored) {
        }
    }

    public static String decryptMessage(String encryptedText, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        return new String(cipher.doFinal(Base64.getDecoder().decode(encryptedText)));
    }

    static void sendGrades(List<Note> grades, Action action) {
        Thread thread = new Thread(() ->
        {
            String subject = action == Action.ADD ? "New grade added" : "Grade modified";
            Student student = studentService.find(grades.get(0).getStudentID());
            StringBuilder body = new StringBuilder();
            body.append("<html><head><style>table { border: 1px solid black; border-collapse: collapse; " +
                    "border-spacing: 0; color: black; font-size : 15px; margin-top : 20px; } " +
                    "th, td { padding: 10px 15px; vertical-align: middle; text-align : center; " +
                    "border: 1px solid black; } th { background-color: #395870; color: white; " +
                    "text-align : center; font-size : 18px; border: 0px; } table tr:nth-child(even) " +
                    "{ background-color: #cacaca;} table tr:hover { background-color: #c2dcff; } </style></head>"
                    + "<body>"
                    + "Hi, " + student.getNume()
                    + "<br><br>Your grades are :<br>"
                    + "<table>" +
                    "<tr><th>Task</th><th>Grade</th><th>Week</th><th>Observation</th></tr>");
            for (Note grade : grades) {
                String task = homeworkService.find(grade.getHomeworkID()).getTask();
                body.append("<tr>");
                body.append("<td>");
                body.append(task.length() <= 50 ? task : task.substring(0, 50) + "...");
                body.append("</td>");

                body.append("<td>");
                body.append(grade.getValue().toString());
                body.append("</td>");

                body.append("<td>");
                body.append(grade.getSapt_predare());
                body.append("</td>");

                body.append("<td>");
                body.append(grade.getObservatii());
                body.append("</td>");
                body.append("</tr>");
            }
            body.append("</table>" +
                    "<br>Thank you,<br><strong>HomeworkSystem Team</strong></body></html>");
            try {
                BufferedWriter bw = Files.newBufferedWriter(Paths.get("Files/test.html"));
                bw.write(body.toString());
                bw.close();
                sendFromGMail(FROM, password, student.getEmail(), subject, body.toString());
            } catch (Exception ignored) {

            }
        });
        thread.start();
    }

    static void sendToAll(Homework homework, Action action) {
        Thread thread = new Thread(() -> sendAll(homework, action));
        thread.start();
    }

    private static void sendAll(Homework homework, Action action) {
        String subject, content;

        if (action == Action.ADD) {
            subject = "New Howework Added";
            content = "Hi,<br><br>There was added a <em>new homework</em>(" + (homework.getTask().length() > 10 ? homework.getTask().substring(0, 10) + "..." :
                    homework.getTask()) + "). Deadline is <strong>week " + homework.getDeadline().toString() +
                    "</strong>.<br><br>Thank you,<br><strong>HomeworkSystem Team</strong>";
        } else {
            subject = "Homework deadline modified";
            content = "Hi,<br><br>For this homework (" + (homework.getTask().length() > 10 ? homework.getTask().substring(0, 10) + "..." :
                    homework.getTask()) + "), deadline is now <strong>week " +
                    homework.getDeadline().toString() + "</strong>.<br><br>Thank you,<br><strong>HomeworkSystem Team</strong>";
        }


        try {
            List<String> emails = new ArrayList<>();
            studentService.getAll().forEach(id -> emails.add(studentService.find(id).getEmail()));
            Thread[] threads = new Thread[emails.size()];
            int index = 0;
            for (String email : emails) {
                threads[index++] = new Thread(() -> {
                    try {
                        sendFromGMail(FROM, password, email, subject, content);
                    } catch (Exception ignored) {
                        System.out.println(ignored.getMessage());
                    }
                });
            }
            for (Thread thread : threads)
                thread.start();
            for (Thread thread : threads)
                thread.join();
        } catch (Exception ignored) {
            System.out.println(ignored.getMessage());
        }
    }

    public static void sendFromGMail(String from, String pass, String to, String subject, String body) throws Exception {
        Properties props = System.getProperties();
        String host = "smtp.gmail.com";
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.user", from);
        props.put("mail.smtp.password", pass);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");

        Session session = Session.getDefaultInstance(props);
        MimeMessage message = new MimeMessage(session);

        message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
        message.setSubject(subject);
        message.setContent(body, "text/html");
        Transport transport = session.getTransport("smtp");
        transport.connect(host, from, pass);
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
    }

    public void handleAverage() {
        mainLayout.setCenter(statisticsAvgLayout);
    }

    public void handleStatisticsHomeworks() {
        mainLayout.setCenter(statisticsHomeworksLayout);
    }

    public void handleStatisticsStudents1() {
        mainLayout.setCenter(eligibleStudentsLayout);
    }

    public void handleStatisticsStudents2() {
        mainLayout.setCenter(UpToDateStudentsLayout);
    }

    public void updateProfile() {
        Parent root;
        try {
            Stage stage = (Stage) mainLayout.getScene().getWindow();
            stage.hide();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/UpdateProfile.fxml"));
            root = loader.load();
            UpdateProfile updateProfile = loader.getController();
            updateProfile.setUser(user);
            Stage stage1 = new Stage();
            stage1.setTitle("Homework System v1.0");
            stage1.setScene(new Scene(root));
            stage1.show();
            stage1.setOnCloseRequest(event -> {
                if (UpdateProfile.Updated)
                    this.setUser(user);
                stage.show();
            });
        } catch (Exception ignored) {
        }
    }

    public void handleLogOut() {
        ((Stage) mainLayout.getScene().getWindow()).close();
        mainLayout.getScene().getWindow().getOnCloseRequest().handle(null);
    }

    public void updatePassword() {
        Parent root;
        try {
            Stage stage = (Stage) mainLayout.getScene().getWindow();
            stage.hide();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/UpdatePassword.fxml"));
            root = loader.load();
            UpdateProfile updateProfile = loader.getController();
            updateProfile.setUserForPasword(user);
            Stage stage1 = new Stage();
            stage1.setTitle("Homework System v1.0");
            stage1.setScene(new Scene(root));
            stage1.show();
            stage1.setOnCloseRequest(event -> stage.show());
        } catch (Exception ignored) {
        }
    }

    public void setAdmins() {
        Parent root;
        try {
            Stage stage = (Stage) mainLayout.getScene().getWindow();
            stage.hide();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/AddAdmin.fxml"));
            root = loader.load();
            Stage stage1 = new Stage();
            stage1.setTitle("Homework System v1.0");
            stage1.setScene(new Scene(root));
            stage1.show();
            stage1.setOnCloseRequest(event -> stage.show());
        } catch (Exception ignored) {
        }
    }

    private EventService eventService = null;

    public void events() {
        if (eventService == null)
            eventService = new EventService(new Validator(new EventValidator()),
                    new EventDatabaseRepository());
        Parent root;
        try {
            Stage stage = (Stage) mainLayout.getScene().getWindow();
            stage.hide();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Events.fxml"));
            root = loader.load();
            EventController eventController = loader.getController();
            eventController.setEnviroment(eventService, user);
            Stage stage1 = new Stage();
            stage1.setTitle("Homework System v1.0");
            stage1.setScene(new Scene(root));
            stage1.show();
            stage1.setOnCloseRequest(event -> stage.show());
        } catch (Exception ignored) {
        }
    }

    public void MailOption() {
        String content = (this.user.getMailOption() == 0 ? "This will enable email sending.\nAny change at Homeworks or Grades will be automatically notified to students." :
                "This will disable email sending.\nStudents won't be able to get any updates of Homeworks or their grades.") + "\n\nAre you sure ?";
        Alert alert = new Alert(Alert.AlertType.WARNING,
                content,
                ButtonType.YES,
                ButtonType.NO);
        alert.setTitle(mailoption.getText());
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.YES) {
            this.user.setMailOption(1 - this.user.getMailOption());
            mailoption.setText(this.user.getMailOption() == 0 ? "Enable Emails" : "Disable Emails");
        }

    }
}
