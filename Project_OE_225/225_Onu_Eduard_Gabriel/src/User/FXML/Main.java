package User.FXML;

import Domain.Nota;
import Domain.Student;
import Domain.Tema;

import Repository.MSSQLStudentRepository;
import Repository.Repository;
import Services.NotaService;
import Services.StatisticsService;
import Services.StudentService;
import Services.TemeService;
import User.FXML.Controlers.*;
import Utils.Notifier;
import Utils.StudentFileMover;
import Utils.XMLConfigLoader;
import Validators.StudentValidator;
import Validators.TemeValidator;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import Repository.MSSQLTemaRepository;
import  Repository.MSSQLNotaRepository;

import java.io.BufferedReader;
import java.io.FileReader;


public class Main extends Application{

    private Repository<Student> studentAbstractRepo;

    private Repository <Tema> temaAbstractRepo;

    private Repository <Nota> notaAbstractRepo;

    private StudentService studentService;

    private TemeService temeService;

    private NotaService notaService;

    private StatisticsService statisticsService;

    private MarksViewControler marksViewControler;

    private StudentsWindowControler controler;

    private HomeworksWindowViewControler homeworksWindowViewControler;

    private MainWindowViewControler mainWindowViewControler;

    private StatisticsControler statisticsControler;

    private PDFSaveControler pdfSaveControler;

    private AddMarkWindowControler addMarkWindowControler;

    private XMLConfigLoader configLoader = XMLConfigLoader.getInstance();

    private Notifier notifier;

    private Parent getStudentWindowAndSetControlers() throws  Exception{

        //studentAbstractRepo = new FileRepo<Student>("Students.txt");

        //studentAbstractRepo = new MSSQLStudentRepository();

        studentAbstractRepo = configLoader.getStudentsRepo();

        studentService = new StudentService(studentAbstractRepo,new StudentValidator());

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../../User/FXML/Views/StudentsWindowView.fxml"));

        FXMLLoader l2 = new FXMLLoader(getClass().getResource("../../User/FXML/Views/AddWindowFxml.fxml"));

        FXMLLoader l3 = new FXMLLoader(getClass().getResource("../../User/FXML/Views/StudentEditWindowView.fxml"));

        Parent studentWindow = loader.load();

        Parent studentEditWindow = l3.load();

        Parent addWindow = l2.load();

        controler = loader.getController();

        controler.setStudentService(studentService);

        controler.addWindow(addWindow);

        controler.addEditWindow(studentEditWindow);

        AddWindowFxmlControler addWindowFxmlControler = l2.getController();

        addWindowFxmlControler.setStudentWindowControler(controler);

        StudentEditWindowViewControler studentEditWindowViewControler = l3.getController();

        studentEditWindowViewControler.setStudentsWindowControler(controler);

        controler.addEditWindowControler(studentEditWindowViewControler);

        return studentWindow;
    }

    private Parent getHomeworkWindowAndSetControlers() throws  Exception{

        //temaAbstractRepo = new FileRepo<Tema>("Teme.txt");

        //temaAbstractRepo = new MSSQLTemaRepository();

        temaAbstractRepo = configLoader.getHomeworksRepo();

        notifier = new Notifier(studentAbstractRepo);

        temeService = new TemeService(temaAbstractRepo,new TemeValidator(),notifier);


        FXMLLoader loader = new FXMLLoader(getClass().getResource("../../User/FXML/Views/HomeworksWindowView.fxml"));

        Parent homeworkWindow = loader.load();

        homeworksWindowViewControler = loader.getController();

        temeService.addObserver(homeworksWindowViewControler);

        homeworksWindowViewControler.setTemeService(temeService);

        return homeworkWindow;
    }

    private Parent getMarksWindowAndSetControlers() throws  Exception{

        //notaAbstractRepo = new FileRepo<Nota>("Catalog.txt");

        //notaAbstractRepo = new MSSQLNotaRepository();

        notaAbstractRepo = configLoader.getMarksRepo();

        notaService = new NotaService(notaAbstractRepo,studentService,temeService,notifier);


        FXMLLoader loader = new FXMLLoader(getClass().getResource("../../User/FXML/Views/MarksView.fxml"));

        Parent markWindow = loader.load();

        marksViewControler = loader.getController();

        marksViewControler.setNotaService(notaService);

        return markWindow;
    }

    private Parent getAddWindow()throws  Exception{

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../../User/FXML/Views/AddMarkWindow.fxml"));

        Parent addWindow = fxmlLoader.load();

        addMarkWindowControler = fxmlLoader.getController();

        addMarkWindowControler.setStudentService(studentService);
        addMarkWindowControler.setTemeService(temeService);
        addMarkWindowControler.setNotaService(notaService);
        addMarkWindowControler.setMarksViewControler(marksViewControler);

        marksViewControler.setAddMarkWindowControler(addMarkWindowControler);

        return addWindow;
    }

    private Parent getMainWindow() throws  Exception{

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../../User/FXML/Views/MainWindowView.fxml"));

        Parent mainWindow = loader.load();

        mainWindowViewControler = loader.getController();

        return  mainWindow;

    }

    private Parent getStatistics() throws Exception{

        statisticsService = new StatisticsService(studentAbstractRepo,temaAbstractRepo,notaAbstractRepo);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../../User/FXML/Views/StatisticsView.fxml"));

        Parent window = loader.load();

        statisticsControler = loader.getController();

        statisticsControler.setStatisticsService(statisticsService);

        return window;
    }

    private Parent getPdfSave() throws  Exception{

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../../User/FXML/Views/PDFSaveView.fxml"));

        Parent pdf = loader.load();

        pdfSaveControler = loader.getController();

        return pdf;
    }

    private void writeBack(){
        StudentFileMover studentFileMover = new StudentFileMover();

        studentFileMover.setCurrentLocation("C:\\Users\\Onu Edy\\Desktop\\Project\\225_Onu_Eduard_Gabriel");

        studentFileMover.setRepository(studentAbstractRepo);

        String location = "C:\\Users\\Onu Edy\\Desktop\\Project\\225_Onu_Eduard_Gabriel\\src\\Config\\currentConfig.txt";

        try(BufferedReader reader = new BufferedReader(new FileReader(location))){
            location = reader.readLine();

        }catch (Exception e){
            e.printStackTrace();
        }


        studentFileMover.setDestination("C:\\Users\\Onu Edy\\Desktop\\Project\\225_Onu_Eduard_Gabriel\\src\\Config\\"+location);

        studentFileMover.moveAll();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        try {

            configLoader.checkCurrentConfig();

            Parent studentWindow = getStudentWindowAndSetControlers();

            Parent homeworkWindow = getHomeworkWindowAndSetControlers();

            Parent markWindow = getMarksWindowAndSetControlers();

            Parent addMark = getAddWindow();

            Parent statWind = getStatistics();

            Parent mainWindow = getMainWindow();

            studentService.addObserver(marksViewControler);
            studentService.addObserver(statisticsControler);
            temeService.addObserver(marksViewControler);
            temeService.addObserver(statisticsControler);
            notaService.addObserver(statisticsControler);

            marksViewControler.addInCenter(addMark);

            mainWindowViewControler.setHomeworkWindow(homeworkWindow);
            mainWindowViewControler.setMakrsWindow(markWindow);
            mainWindowViewControler.setStudentWindow(studentWindow);
            mainWindowViewControler.setStatisticsWindow(statWind);

            statisticsControler.setPopOverContent(getPdfSave());
            statisticsControler.setPdfControler(pdfSaveControler);

            pdfSaveControler.setFileChooserStage(primaryStage);

            pdfSaveControler.setStatistics(statisticsControler);


            Scene scene = new Scene(mainWindow);
            scene.getStylesheets().add(getClass().getResource("../../User/FXML/Sheets/style.css").toExternalForm());

            primaryStage.setScene(scene);

            primaryStage.show();

            primaryStage.setResizable(false);

            primaryStage.getIcons().add(new Image("file:C:\\Users\\Onu Edy\\Desktop\\Project\\225_Onu_Eduard_Gabriel\\src\\User\\FXML\\Images\\manager.png"));

            primaryStage.setOnCloseRequest(event -> {

                controler.hideAllPopOvers();
                homeworksWindowViewControler.hideAllPopOvers();
                marksViewControler.hideAllPopOvers();
                statisticsControler.hideAllPopOvers();
                addMarkWindowControler.hideAllPopOvers();

                writeBack();
            });


        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

    public static void main(String...args){
        launch(args);
    }
}
