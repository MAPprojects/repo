package GUI;

import Domain.User;
import GUI.AssignmentsGUI.AddAsgController;
import GUI.AssignmentsGUI.AsgController;
import GUI.AssignmentsGUI.UpdAsgController;
import GUI.GradesGUI.AddGrdController;
import GUI.GradesGUI.GrdController;
import GUI.GradesGUI.UpdGrdController;
import GUI.HomeGUI.HomeController;
import GUI.LoginGUI.LoginController;
import GUI.ReportsUI.ReportController;
import GUI.ReportsUI.Rpt1Ctrl;
import GUI.ReportsUI.Rpt2Ctrl;
import GUI.ReportsUI.Rpt3Ctrl;
import GUI.StudentsGUI.AddStdController;
import GUI.StudentsGUI.StdController;
import GUI.StudentsGUI.UpdStdController;
import Main.StartApplication;
import Service.AssignmentService;
import Service.GeneralService;
import Service.GradeService;
import Service.StudentService;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;

public class InitView {
    public static AnchorPane initRpt3View(StudentService studentService, AssignmentService assignmentService, GradeService gradeService, GeneralService generalService, User user) throws Exception {
        try {
            FXMLLoader loader = new FXMLLoader(HomeController.class.getClassLoader().getResource("rpt3View.fxml"));
            Parent view = loader.load();

            Rpt3Ctrl controller = loader.getController();
            controller.setStudentService(studentService);
            controller.setAssignmentService(assignmentService);
            controller.setGradeService(gradeService);
            controller.setGeneralService(generalService);


            gradeService.addObserver(controller);


            AnchorPane main = new AnchorPane(view);

            return main;
        } catch (IOException e) {
            System.out.println("Eroare");
            e.printStackTrace();
            return new AnchorPane();
        }
    }

    public static AnchorPane initRpt2View(StudentService studentService, AssignmentService assignmentService, GradeService gradeService, GeneralService generalService, User user) throws Exception {
        try {
            FXMLLoader loader = new FXMLLoader(HomeController.class.getClassLoader().getResource("rpt2View.fxml"));
            Parent view = loader.load();

            Rpt2Ctrl controller = loader.getController();
            controller.setStudentService(studentService);
            controller.setAssignmentService(assignmentService);
            controller.setGradeService(gradeService);
            controller.setGeneralService(generalService);


            gradeService.addObserver(controller);


            AnchorPane main = new AnchorPane(view);

            return main;
        } catch (IOException e) {
            System.out.println("Eroare");
            e.printStackTrace();
            return new AnchorPane();
        }
    }

    public static AnchorPane initRpt1View(StudentService studentService, AssignmentService assignmentService, GradeService gradeService, GeneralService generalService, User user) throws Exception {
        try {
            FXMLLoader loader = new FXMLLoader(HomeController.class.getClassLoader().getResource("rpt1View.fxml"));
            Parent view = loader.load();

            Rpt1Ctrl controller = loader.getController();
            controller.setStudentService(studentService);
            controller.setAssignmentService(assignmentService);
            controller.setGradeService(gradeService);
            controller.setGeneralService(generalService);

            studentService.addObserver(controller);

            AnchorPane main = new AnchorPane(view);

            return main;
        } catch (IOException e) {
            System.out.println("Eroare");
            e.printStackTrace();
            return new AnchorPane();
        }
    }

    public static AnchorPane initRptView(StudentService studentService, AssignmentService assignmentService, GradeService gradeService, GeneralService generalService,User user) {
        try {
            FXMLLoader loader = new FXMLLoader(HomeController.class.getClassLoader().getResource("reportsView.fxml"));
            Parent view = loader.load();

            ReportController controller = loader.getController();
            controller.setStudentService(studentService);
            controller.setAssignmentService(assignmentService);
            controller.setGradeService(gradeService);
            controller.setGeneralService(generalService);
            controller.setUser(user);



            AnchorPane main = new AnchorPane(view);

            return main;
        } catch (IOException e) {
            System.out.println("Eroare");
            e.printStackTrace();
            return new AnchorPane();
        } catch (Exception e) {
            e.printStackTrace();
            return new AnchorPane();
        }
    }

    public static AnchorPane initStdView(StudentService studentService, AssignmentService assignmentService, GradeService gradeService, GeneralService generalService, User user) {
        try {
            FXMLLoader loader = new FXMLLoader(StartApplication.class.getClassLoader().getResource("studentsView.fxml"));
            Parent view = loader.load();

            StdController controller = loader.getController();
            controller.setStudentService(studentService);
            controller.setAssignmentService(assignmentService);
            controller.setGradeService(gradeService);
            controller.setGeneralService(generalService);
            controller.setUser(user);

            studentService.addObserver(controller);

            AnchorPane main = new AnchorPane(view);

            return main;
        } catch (IOException e) {
            System.out.println("Eroare");
            e.printStackTrace();
            return new AnchorPane();
        }
    }

    public static AnchorPane initGrdView(StudentService studentService, AssignmentService assignmentService, GradeService gradeService, GeneralService generalService, User user) {
        try {
            FXMLLoader loader = new FXMLLoader(HomeController.class.getClassLoader().getResource("grdView.fxml"));
            Parent view = loader.load();

            GrdController controller = loader.getController();
            controller.setStudentService(studentService);
            controller.setAssignmentService(assignmentService);
            controller.setGradeService(gradeService);
            controller.setGeneralService(generalService);
            controller.setUser(user);

            gradeService.addObserver(controller);

            AnchorPane main = new AnchorPane(view);

            return main;
        } catch (IOException e) {
            System.out.println("Eroare");
            e.printStackTrace();
            return new AnchorPane();
        }
    }

    public static AnchorPane initLoginView(StudentService studentService, AssignmentService assignmentService, GradeService gradeService, GeneralService generalService, User user) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        URL x = LoginController.class.getClassLoader().getResource("loginView.fxml");
        loader.setLocation(x);
        AnchorPane root = null;

        root = loader.load();


        LoginController controller = loader.getController();
        controller.setStudentService(studentService);
        controller.setAssignmentService(assignmentService);
        controller.setGradeService(gradeService);
        controller.setGeneralService(generalService);

        AnchorPane main = new AnchorPane(root);

        return main;
    }

    public static AnchorPane initAsgView(StudentService studentService, AssignmentService assignmentService, GradeService gradeService, GeneralService generalService, User user) {
        try {
            FXMLLoader loader = new FXMLLoader(HomeController.class.getClassLoader().getResource("asgView.fxml"));
            Parent view = loader.load();

            AsgController controller = loader.getController();
            controller.setStudentService(studentService);
            controller.setAssignmentService(assignmentService);
            controller.setGradeService(gradeService);
            controller.setGeneralService(generalService);
            controller.setUser(user);

            assignmentService.addObserver(controller);

            AnchorPane main = new AnchorPane(view);

            return main;
        } catch (IOException e) {
            System.out.println("Eroare");
            e.printStackTrace();
            return new AnchorPane();
        }
    }

    public static AnchorPane initHomeView(StudentService studentService, AssignmentService assignmentService, GradeService gradeService, GeneralService generalService, User user) {
        try {
            FXMLLoader loader = new FXMLLoader(StdController.class.getClassLoader().getResource("homeView.fxml"));
            Parent view = loader.load();

            HomeController controller = loader.getController();
            controller.setStudentService(studentService);
            controller.setAssignmentService(assignmentService);
            controller.setGradeService(gradeService);
            controller.setGeneralService(generalService);
            controller.setUser(user);

            AnchorPane main = new AnchorPane(view);
            return main;
        } catch (IOException e) {
            System.out.println("Eroare");
            e.printStackTrace();
            return new AnchorPane();
        }
    }

    public static AnchorPane initAddStdView(StudentService studentService, AssignmentService assignmentService, GradeService gradeService, GeneralService generalService, User user) {
        try {
            FXMLLoader loader = new FXMLLoader(StdController.class.getClassLoader().getResource("addStdView.fxml"));
            Parent view = loader.load();

            AddStdController controller = loader.getController();
            controller.setStudentService(studentService);
            controller.setAssignmentService(assignmentService);
            controller.setGradeService(gradeService);
            controller.setGeneralService(generalService);
            controller.setUser(user);

            studentService.addObserver(controller);

            AnchorPane main = new AnchorPane(view);

            return main;
        } catch (IOException e) {
            System.out.println("Eroare");
            e.printStackTrace();
            return new AnchorPane();
        }
    }

    public static AnchorPane initUpdStdView(StudentService studentService, AssignmentService assignmentService, GradeService gradeService, GeneralService generalService, User user) {
        try {
            FXMLLoader loader = new FXMLLoader(StdController.class.getClassLoader().getResource("updStdView.fxml"));
            Parent view = loader.load();

            UpdStdController controller = loader.getController();
            controller.setStudentService(studentService);
            controller.setAssignmentService(assignmentService);
            controller.setGradeService(gradeService);
            controller.setGeneralService(generalService);
            controller.setUser(user);

            studentService.addObserver(controller);

            AnchorPane main = new AnchorPane(view);

            return main;
        } catch (IOException e) {
            System.out.println("Eroare");
            e.printStackTrace();
            return new AnchorPane();
        }
    }

    public static AnchorPane initAddGrdView(StudentService studentService, AssignmentService assignmentService, GradeService gradeService, GeneralService generalService, User user) {
        try {
            FXMLLoader loader = new FXMLLoader(GrdController.class.getClassLoader().getResource("addGrdView.fxml"));
            Parent view = loader.load();

            AddGrdController controller = loader.getController();
            controller.setStudentService(studentService);
            controller.setAssignmentService(assignmentService);
            controller.setGradeService(gradeService);
            controller.setGeneralService(generalService);
            controller.setUser(user);


            gradeService.addObserver(controller);

            AnchorPane main = new AnchorPane(view);

            return main;
        } catch (IOException e) {
            System.out.println("Eroare");
            e.printStackTrace();
            return new AnchorPane();
        }
    }

    public static AnchorPane initUpdGrdView(StudentService studentService, AssignmentService assignmentService, GradeService gradeService, GeneralService generalService, User user) {
        try {
            FXMLLoader loader = new FXMLLoader(GrdController.class.getClassLoader().getResource("updGrdView.fxml"));
            Parent view = loader.load();

            UpdGrdController controller = loader.getController();
            controller.setStudentService(studentService);
            controller.setAssignmentService(assignmentService);
            controller.setGradeService(gradeService);
            controller.setGeneralService(generalService);
            controller.setUser(user);


            gradeService.addObserver(controller);

            AnchorPane main = new AnchorPane(view);

            return main;
        } catch (IOException e) {
            System.out.println("Eroare");
            e.printStackTrace();
            return new AnchorPane();
        }
    }

    public static AnchorPane initAddAsgView(StudentService studentService, AssignmentService assignmentService, GradeService gradeService, GeneralService generalService, User user) {
        try {
            FXMLLoader loader = new FXMLLoader(AsgController.class.getClassLoader().getResource("addAsgView.fxml"));
            Parent view = loader.load();

            AddAsgController controller = loader.getController();
            controller.setStudentService(studentService);
            controller.setAssignmentService(assignmentService);
            controller.setGradeService(gradeService);
            controller.setGeneralService(generalService);
            controller.setUser(user);


            assignmentService.addObserver(controller);

            AnchorPane main = new AnchorPane(view);

            return main;
        } catch (IOException e) {
            System.out.println("Eroare");
            e.printStackTrace();
            return new AnchorPane();
        }
    }

    public static AnchorPane initUpdAsgView(StudentService studentService, AssignmentService assignmentService, GradeService gradeService, GeneralService generalService, User user) {
        try {
            FXMLLoader loader = new FXMLLoader(AsgController.class.getClassLoader().getResource("updAsgView.fxml"));
            Parent view = loader.load();

            UpdAsgController controller = loader.getController();
            controller.setStudentService(studentService);
            controller.setAssignmentService(assignmentService);
            controller.setGradeService(gradeService);
            controller.setGeneralService(generalService);
            controller.setUser(user);

            assignmentService.addObserver(controller);

            AnchorPane main = new AnchorPane(view);

            return main;
        } catch (IOException e) {
            System.out.println("Eroare");
            e.printStackTrace();
            return new AnchorPane();
        }
    }
}
