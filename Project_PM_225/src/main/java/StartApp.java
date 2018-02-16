import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

import java.io.IOException;

public class StartApp extends Application {

//    RepoStudent studentRepository = new RepoStudent();
//    RepoTema temaRepository = new RepoTema();
//    RepoNota notaRepo = new RepoNota();
//
//    ServiceStudent studentService = new ServiceStudent(studentRepository, notaRepo);
//    ServiceLaborator laboratorService = new ServiceLaborator(temaRepository, studentRepository);
//    ServiceNota notaService = new ServiceNota(studentRepository, notaRepo, temaRepository);

    public StartApp() throws Exception {
    }

    @Override
    public void start(Stage primaryStage) {
//        Date date = new Date(2017,3,10);
//        Calendar cal = Calendar.getInstance();
//        DateTime azi = new DateTime(cal.getTime());
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy")
//                .withResolverStyle(ResolverStyle.STRICT);
//        LocalDate.parse("10-3-2017", formatter);
//        DateTime inceputulFacultatii = new DateTime(formatter);
//        System.out.println(azi);
//        System.out.println(formatter);
//        int week = Weeks.weeksBetween(inceputulFacultatii,azi).getWeeks();
//        System.out.println(week);

        FXMLLoader loader = new FXMLLoader();
        TabPane rootLayout = null;


//        loader.setLocation(StartApp.class.getResource("/studentView/StudentView_FXML.fxml"));
//        try
//        {
//            rootLayout = (AnchorPane)loader.load();
//            StudentController_FXML rootController = loader.getController();
//            try {
//                rootController.setService(this.studentService);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            this.studentService.addObserver(rootController);
//
////            rootLayout = (AnchorPane)loader.load();
////            TemaController_FXML rootController = loader.getController();
////            rootController.setService(this.temaService);
////            this.temaService.addObserver(rootController);
//
////            rootLayout = (AnchorPane) loader.load();
////            NotaController_FXML rootController = loader.getController();
////            rootController.setService(this.studentService, this.temaService, this.notaService);
////            this.notaService.addObserver(rootController);
//            primaryStage.setScene(new Scene(rootLayout, 660, 400));
//            primaryStage.setTitle("My app");
//            primaryStage.setResizable(false);
//            primaryStage.show();
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
//    }
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//}


//
//        loader.setLocation(StartApp.class.getResource("/laboratorView/LaboratorView_FXML.fxml"));
//        try
//        {
//            rootLayout = (AnchorPane)loader.load();
//            LaboratorController_FXML rootController = loader.getController();
//            try {
//                rootController.setService(this.laboratorService);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            this.laboratorService.addObserver(rootController);
//
////            rootLayout = (AnchorPane)loader.load();
////            TemaController_FXML rootController = loader.getController();
////            rootController.setService(this.temaService);
////            this.temaService.addObserver(rootController);
//
////            rootLayout = (AnchorPane) loader.load();
////            NotaController_FXML rootController = loader.getController();
////            rootController.setService(this.studentService, this.temaService, this.notaService);
////            this.notaService.addObserver(rootController);
//            primaryStage.setScene(new Scene(rootLayout, 860, 600));
//            primaryStage.setTitle("My app");
//            primaryStage.setResizable(false);
//            primaryStage.show();
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
//    }


//        loader.setLocation(StartApp.class.getResource("/notaView/NotaView_FXML.fxml"));
//
//        try
//        {
//            rootLayout = (AnchorPane)loader.load();
//            NotaController_FXML rootController = loader.getController();
//            try {
//                rootController.setService(this.notaService);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            this.notaService.addObserver(rootController);
//
////            rootLayout = (AnchorPane)loader.load();
////            TemaController_FXML rootController = loader.getController();
////            rootController.setService(this.temaService);
////            this.temaService.addObserver(rootController);
//
////            rootLayout = (AnchorPane) loader.load();
////            NotaController_FXML rootController = loader.getController();
////            rootController.setService(this.studentService, this.temaService, this.notaService);
////            this.notaService.addObserver(rootController);
//            primaryStage.setScene(new Scene(rootLayout, 860, 600));
//            primaryStage.setTitle("My app");
//            primaryStage.setResizable(false);
//            primaryStage.show();
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
//    }



        loader.setLocation(StartApp.class.getResource("/MainInterface.fxml"));

        try
        {

            rootLayout = (TabPane) loader.load();
            MainController_FXML rootController = loader.getController();
            rootController.setService();

//            this.studentService.addObserver(rootController);
//            this.laboratorService.addObserver(rootController);
//            this.notaService.addObserver(rootController);
        primaryStage.setScene(new Scene(rootLayout, 860, 600));
        primaryStage.setTitle("Class evidence");
        primaryStage.setResizable(false);
        primaryStage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
    }

    }

    public static void main(String[] args) {
        launch(args);
    }
}
