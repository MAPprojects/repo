import controller.*;
import entities.Role;
import exceptions.AbstractValidatorException;
import exceptions.StudentServiceException;
import exceptions.TemaServiceException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import repository.*;
import services.*;
import validator.NotaValidator;
import validator.StudentValidator;
import validator.TemaValidator;
import validator.UserValidator;

import java.io.IOException;

public class Main extends Application {

    private static StandardServiceRegistry standardRegistry;
    private static Metadata metadata;
    private static SessionFactory sessionFactory;

    private static StudentValidator studentValidator;
    private static StudentRepository studentRepository;
    private static StudentService studentService;

    private static TemaValidator temaValidator;
    private static TemaRepository temaRepository;
    private static TemaService temaService;

    private static NotaValidator notaValidator;
    private static NotaRepository notaRepo;
    private static NotaService notaService;

    private static UserRepository userRepository;
    private static UserValidator userValidator;
    private static UserSerivce userSerivce;

    private static SystemConfigurationRepository systemConfigurationRepository;
    private static SystemConfigurationService systemConfigurationService;

    private static StatisticsService statisticsService;

    private static StudentControllerV2 studentController;

    private static boolean isAuthenticationEnabled;

    private static ForgotPasswordService forgotPasswordService;

    private static ChangePasswordService changePasswordService;

    public static void main(String[] args) throws AbstractValidatorException, StudentServiceException, TemaServiceException, IOException {
        try {
            standardRegistry = new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build();
            metadata = new MetadataSources(standardRegistry).getMetadataBuilder().build();
            sessionFactory = metadata.getSessionFactoryBuilder().build();

            studentValidator = new StudentValidator();
            studentRepository = new StudentRepository();
            studentRepository.setSessionFactory(sessionFactory);
            studentService = new StudentService(studentRepository, studentValidator);

            temaValidator = new TemaValidator();
            temaRepository = new TemaRepository();
            temaRepository.setSessionFactory(sessionFactory);
            temaService = new TemaService(temaRepository, studentRepository, temaValidator);

            notaValidator = new NotaValidator();
            notaRepo = new NotaRepository();
            notaRepo.setSessionFactory(sessionFactory);
            notaService = new NotaService(notaRepo, notaValidator, studentRepository, temaRepository);

            userRepository = new UserRepository();
            userValidator = new UserValidator();
            userRepository.setSessionFactory(sessionFactory);
            userSerivce = new UserSerivce(userRepository, userValidator);

            systemConfigurationRepository = new SystemConfigurationRepository();
            systemConfigurationRepository.setSessionFactory(sessionFactory);
            systemConfigurationService = new SystemConfigurationService(systemConfigurationRepository);

            statisticsService = new StatisticsService(notaRepo, studentRepository, temaRepository);
            forgotPasswordService = new ForgotPasswordService(userRepository);
            changePasswordService = new ChangePasswordService(userRepository);

            studentService.setUserSerivce(userSerivce);


            isAuthenticationEnabled = systemConfigurationService.isEnabledAuthentication();

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        launch(args);
        sessionFactory.close();

    }

    @Override
    public void start(Stage primaryStage) throws Exception {
//        Role roleOFCurrentUser1 = systemConfigurationService.getRoleOfCurrentUser();
//        MainMenuController.setRoleOfCurrentUserFromDb(roleOFCurrentUser1);
        //1.main menu
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/mainMenu/main_menu_view.fxml"));
        AnchorPane rootLayout = loader.load();
        MainMenuController controller = loader.getController();
        controller.setPrimaryStage(primaryStage);
        controller.setSystemConfigurationService(systemConfigurationService);
        Scene mainMenuScene = new Scene(rootLayout);

        //2.student view
        FXMLLoader studentLoader = new FXMLLoader();
        studentLoader.setLocation(getClass().getResource("/views/student/student_view.fxml"));
        AnchorPane studentRootLayout = studentLoader.load();
        StudentControllerV2 studentControllerV2 = studentLoader.getController();
        studentControllerV2.setPrimaryStage(primaryStage);
        studentControllerV2.setMainMenuScene(mainMenuScene);
        studentControllerV2.setStudentService(studentService);
        studentControllerV2.setStudentRootLayout(studentRootLayout);
        studentControllerV2.loadData();
        studentControllerV2.setUserSerivce(userSerivce);
        Scene studentScene = new Scene(studentRootLayout);

        //3. tema view
        FXMLLoader temaLoader = new FXMLLoader();
        temaLoader.setLocation(getClass().getResource("/views/tema/tema_view.fxml"));
        AnchorPane temaRootLayout = temaLoader.load();
        TemaController temaController = temaLoader.getController();
        temaController.setPrimaryStage(primaryStage);
        temaController.setMainMenuScene(mainMenuScene);
        temaController.setTemaService(temaService);
        temaController.setTemaSceneRootLayout(temaRootLayout);
        temaController.loadData();
        temaController.setNotaService(notaService);
        Scene temaScene = new Scene(temaRootLayout);

        //6.System configuration view
        FXMLLoader systemConfigLoader = new FXMLLoader();
        systemConfigLoader.setLocation(getClass().getResource("/views/systemConfiguration/system_config.fxml"));
        AnchorPane systemConfigRoot = systemConfigLoader.load();
        SystemConfigurationController systemConfigurationController = systemConfigLoader.getController();
        systemConfigurationController.setUserSerivce(userSerivce);
        systemConfigurationController.setStudentService(studentService);
        systemConfigurationController.setMainMenuScene(mainMenuScene);
        systemConfigurationController.setPrimaryStage(primaryStage);
        systemConfigurationController.setSystemConfigurationService(systemConfigurationService);
        systemConfigurationController.populateTable();
        systemConfigurationController.setRootLayout(systemConfigRoot);
        systemConfigurationController.setNotaService(notaService);
        Scene systemConfigScene = new Scene(systemConfigRoot);


        //4.nota view
        FXMLLoader notaLoader = new FXMLLoader();
        notaLoader.setLocation(getClass().getResource("/views/nota/nota_view.fxml"));
        AnchorPane notaRootLayout = notaLoader.load();
        NotaController notaController = notaLoader.getController();
        notaController.setStudentService(studentControllerV2.getStudentService());
        notaController.setTemaService(temaController.getTemaService());
        notaController.setPrimaryStage(primaryStage);
        notaController.setMenuScene(mainMenuScene);
        notaController.setNotaService(notaService);
        notaController.setNotaRootLayout(notaRootLayout);
        notaController.loadData();
        Scene notaScene = new Scene(notaRootLayout);

        //5.Login view
        FXMLLoader loginLoader = new FXMLLoader();
        loginLoader.setLocation(getClass().getResource("/views/login/login_view.fxml"));
        AnchorPane loginRootLayout = loginLoader.load();
        LoginController loginController = loginLoader.getController();
        loginController.setUserSerivce(userSerivce);
        loginController.setPrimaryStage(primaryStage);
        loginController.setMenuScene(mainMenuScene);
        loginController.setMainMenuController(controller);
        loginController.setSystemConfigurationService(systemConfigurationService);
        loginController.setLoginRootLayout(loginRootLayout);
        loginController.setForgotPasswordService(forgotPasswordService);
        loginController.setChangePasswordService(changePasswordService);
        loginController.addListener(controller);
        loginController.addListener(notaController);
        loginController.addListener(temaController);
        Scene loginScene = new Scene(loginRootLayout);
        loginController.setLoginScene(loginScene);

        //still counting. Loading scene
        FXMLLoader loadingLoader = new FXMLLoader();
        loadingLoader.setLocation(getClass().getResource("/views/loadingScene/loadin_scene.fxml"));
        AnchorPane loadingRootLayout = loadingLoader.load();
        Scene loadingScene = new Scene(loadingRootLayout);

        //not finished yet . Statistics scene
        FXMLLoader loaderStatistics = new FXMLLoader();
        loaderStatistics.setLocation(getClass().getResource("/views/statisticsView/statistics.fxml"));
        AnchorPane statisticsRootLayout = loaderStatistics.load();
        StatisticsController statisticsController = loaderStatistics.getController();
        statisticsController.setPrimaryStage(primaryStage);
        statisticsController.setStatisticsService(statisticsService);
        statisticsController.setMainMenuScene(mainMenuScene);
        statisticsController.setStatisticsRootLayout(statisticsRootLayout);
        Scene statisticsScene = new Scene(statisticsRootLayout);

        //set all rootLayout to the mainMenu controller
        controller.setStudentRootLayout(studentScene);
        controller.setTemaRootLayout(temaScene);
        controller.setNotaRootLayout(notaScene);
        controller.setSystemConfigScene(systemConfigScene);
        controller.setStatisticsScene(statisticsScene);
        controller.setStatisticsController(statisticsController);
        controller.setLogInScene(loginScene);
        controller.setNotaController(notaController);
        controller.setSystemConfigurationController(systemConfigurationController);
        studentControllerV2.setLoadingScene(loadingScene);
        studentControllerV2.setNotaService(notaService);

        //adding log out listeners
        controller.addLogOutListener(loginController);
        controller.addLogOutListener(systemConfigurationController);
        controller.addLogOutListener(temaController);
        controller.addLogOutListener(notaController);

        if (isAuthenticationEnabled) {
            primaryStage.setScene(loginScene);
        } else {
            Role roleOFCurrentUser = systemConfigurationService.getRoleOfCurrentUser();
            loginController.notifyListenersOnLogIn(roleOFCurrentUser);
//            MainMenuController.setRoleOfCurrentUserFromDb(roleOFCurrentUser);
//            controller.setNodesVisibilityDependingOnRole(roleOFCurrentUser);
//            notaController.setRoleOFCurrentUser(roleOFCurrentUser);
//            notaController.setNodesVisibilityDependingOnRole();
            primaryStage.setScene(mainMenuScene);
        }
        primaryStage.show();
    }
}
