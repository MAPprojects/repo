package Controller;

import Domain.Nota;
import Domain.Student;
import Domain.Tema;
import Domain.User;
import Repository.IRepository;
import Service.MailSender;
import Service.Service;
import Service.AccountService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.util.Pair;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

public class LoginController
{
    @FXML PasswordField txtPassword;
    @FXML TextField txtEmail;
    @FXML Button buttonLogin;
    @FXML TextField txtConfirmPassword;
    @FXML Button buttonAccount2;
    @FXML Label labelConfirm;
    @FXML Label labelLogin;
    @FXML Hyperlink linkCreateAccount;
    @FXML Hyperlink linkLogin;
    @FXML Label labelErrors;
    @FXML AnchorPane mainAnchorLogin;
    @FXML
    ImageView  imgEmail;
    @FXML
    ImageView  imgPassword;
    AccountService service;



    @FXML
    public void initialize()
    {
        imgEmail.setImage(new Image("/FXML/mail.png"));
        imgPassword.setImage(new Image("/FXML/password.png"));
        labelErrors.setText("");
        linkLogin.setVisible(false);
        labelConfirm.setVisible(false);
        txtConfirmPassword.setVisible(false);
        buttonAccount2.setVisible(false);
        txtPassword.setTooltip(new Tooltip("Password must contain at least 6 characters"));
    }

    private Service initMenu()
    {
        ValidatorsAndExceptions.Validator<Student> validatorStudent = new ValidatorsAndExceptions.ValidatorStudent();
        IRepository<Student, Integer> repoStudent = new Repository.StudentRepositoryXML("src\\Resources\\Studenti.xml", validatorStudent);
        ValidatorsAndExceptions.Validator<Tema> validatorTema = new ValidatorsAndExceptions.ValidatorTema();
        IRepository<Tema, Integer> repoTema = new Repository.TemaRepositoryXML("src\\Resources\\Teme.xml", validatorTema);
        ValidatorsAndExceptions.Validator<Domain.Nota> validatorNota = new ValidatorsAndExceptions.ValidatorNota();
        IRepository<Nota, Pair<Integer, Integer>> repoNota = new Repository.NotaRepositoryXML("src\\Resources\\Catalog.xml",validatorNota);
        Service service = new Service(repoStudent, repoTema, repoNota);

        if(repoStudent.size()==0) {
            ArrayList<String> nume222 = new ArrayList<>();
            ArrayList<String> prenume222 = new ArrayList<>();
            ArrayList<String> nume223 = new ArrayList<>();
            ArrayList<String> prenume223 = new ArrayList<>();
            ArrayList<String> nume224 = new ArrayList<>();
            ArrayList<String> prenume224 = new ArrayList<>();
            Path path = Paths.get("E:\\FACULTATE\\Anul2\\MAP\\Lab8\\src\\Resources\\nume222.txt");
            Stream<String> lines;
            try {
                lines = Files.lines(path);
                lines.forEach(instance -> nume222.add(instance));
            } catch (IOException e) {
                System.err.println("Can't load data from file\n");
            }

            path = Paths.get("E:\\FACULTATE\\Anul2\\MAP\\Lab8\\src\\Resources\\prenume222.txt");
            try {
                lines = Files.lines(path);
                lines.forEach(instance -> prenume222.add(instance));
            } catch (IOException e) {
                System.err.println("Can't load data from file\n");
            }

            int i;
            for (i = 0; i < nume222.size(); i++) {
                String nume = nume222.get(i);
                String prenume = prenume222.get(i);
                int grupa = 222;
                String email = nume + "@gmail.com";
                String cadruD = "Camelia Serban";
                Student student = new Student(i + 1, nume + ' ' + prenume, grupa, email, cadruD);
                repoStudent.add(student);
            }


            path = Paths.get("E:\\FACULTATE\\Anul2\\MAP\\Lab8\\src\\Resources\\nume223.txt");
            try {
                lines = Files.lines(path);
                lines.forEach(instance -> nume223.add(instance));
            } catch (IOException e) {
                System.err.println("Can't load data from file\n");
            }

            path = Paths.get("E:\\FACULTATE\\Anul2\\MAP\\Lab8\\src\\Resources\\prenume223.txt");
            try {
                lines = Files.lines(path);
                lines.forEach(instance -> prenume223.add(instance));
            } catch (IOException e) {
                System.err.println("Can't load data from file\n");
            }

            for (i = 0; i < nume223.size(); i++) {
                String nume = nume223.get(i);
                String prenume = prenume223.get(i);
                int grupa = 223;
                String email = nume + "@gmail.com";
                String cadruD = "Cojocaru Grigoreta";
                Student student = new Student(nume222.size() + i + 1, nume + ' ' + prenume, grupa, email, cadruD);
                repoStudent.add(student);
            }


            path = Paths.get("E:\\FACULTATE\\Anul2\\MAP\\Lab8\\src\\Resources\\nume224.txt");
            try {
                lines = Files.lines(path);
                lines.forEach(instance -> nume224.add(instance));
            } catch (IOException e) {
                System.err.println("Can't load data from file\n");
            }

            path = Paths.get("E:\\FACULTATE\\Anul2\\MAP\\Lab8\\src\\Resources\\prenume224.txt");
            try {
                lines = Files.lines(path);
                lines.forEach(instance -> prenume224.add(instance));
            } catch (IOException e) {
                System.err.println("Can't load data from file\n");
            }

            for (i = 0; i < nume224.size(); i++) {
                String nume = nume224.get(i);
                String prenume = prenume224.get(i);
                int grupa = 224;
                String email = nume + "@gmail.com";
                String cadruD = "Camelia Serban";
                Student student = new Student(nume222.size() + nume223.size() + i + 1, nume + ' ' + prenume, grupa, email, cadruD);
                repoStudent.add(student);
            }
        }

        if(repoTema.size()==0) {
            repoTema.add(new Tema(1, " Implementati funcționalitățile definite de Iteratia 1 din fisierul TemeLaborator.pdf ", 4));
            repoTema.add(new Tema(2, " 1. Implementati funcționalitățile definite de Iteratia 2 din fisierul TemeLaborator.pdf -\n" +
                    "2. Asigurați persistența datelor folosind clase din pachetele Java IO sau Java NIO2\n" +
                    "3. Completați layerul UI cu un meniu care să permită utilizatorului:\n" +
                    "a) Efectuarea operațiilor definite de Iterația 1 și Iterația 2.\n" +
                    "b) Validarea datelor introduse (Definiti excepții checked).\n ", 6));
            repoTema.add(new Tema(3, " Extindeți proiectul Lab2 cu umătoarele cerințe funcționale sau de proiectare:\n" +
                    "1. Implementati funcționalitățile definite de Iteratia 3 din fisierul TemeLaborator.pdf -\n" +
                    "2. In clasa Service sau EntityService (daca aveti cate unul pentru fiecare entitate), utilizati Stream.filter\n" +
                    "pentru a realiza cerintele iteratiei 3. Mai precis:\n" +
                    "a. Scrieti o metoda generica care filtreaza o lista de entitati de tipul E dupa un anumit criteriu,\n" +
                    "specificat ca si un Predicate in lista de parametri ai metodei. Rezultatul filtrarii va fi apoi sortat\n" +
                    "folosind un comparator care este de asemenea furnizat ca si parametru al metodei.\n" +
                    "public <E> List<E> filterAndSorter(List<E> lista, Predicate<E> p,\n" +
                    "Comparator<T> c)\n" +
                    "Observatie: Puteti defini clase filtre!\n" +
                    "b. Scrieti metode de filtrare concrete care apeleaza metoda generica de la punctul a. (3 metode\n" +
                    "concrete (3 filtre) pentru fiecare entitate)). Folositi lamba si referinte la metode.\n" +
                    "3. Folositi Optional<T> , oriunde este necesar, pentru a preveni NullPointerException (vezi cursul 4 slide 30)\n" +
                    "4. In clasa FileRepository folositi Stream pt citirea(prelucrarea) datelor din fisier.\n ", 7));
            repoTema.add(new Tema(4, " Extindeți proiectul Lab4 cu umătoarele cerințe funcționale sau de proiectare:\n" +
                    "- Proiectati o interfata grafica, folosind JavaFX, care sa permita efectuarea de operatii CRUD pentru o\n" +
                    "entitate definita la Iteratia 1.\n" +
                    "- Separati partea de logica de partea de vizualizare si manipulare (use MVC); Atentie Controllerul din\n" +
                    "MVC nu este acelasi cu Controllerul GRASP pe care l-ati avut anul trecut.\n" +
                    "- Fara FXML (Seminar 7) ", 8));
            repoTema.add(new Tema(5, " Extindeți proiectul Lab5 cu umătoarele cerințe funcționale sau de proiectare:\n" +
                    "- Proiectati o interfata grafica, folosind JavaFX, care sa permita efectuarea operatiilor ce definesc\n" +
                    "functionalitatile din Iteratia1, Iteratia2 si Iteratia3.\n" +
                    "- Separati partea de logica de partea de vizualizare si manipulare (use MVC);\n" +
                    "View-l sa fie un fisier FXML (Seminar 8).\n ", 11));
            repoTema.add(new Tema(6, " Conversie Lab2+Lab3 din Java IN C#", 13));
        }
        if(repoNota.size()==0)
        {
            repoNota.add(new Nota(new Student(33),new Tema(6),9,11));
            repoNota.add(new Nota(new Student(2),new Tema(1),10,7));
            repoNota.add(new Nota(new Student(3),new Tema(1),8,8));
            repoNota.add(new Nota(new Student(4),new Tema(1),8,6));
            repoNota.add(new Nota(new Student(5),new Tema(1),8,6));
            repoNota.add(new Nota(new Student(6),new Tema(1),8,6));
            repoNota.add(new Nota(new Student(7),new Tema(1),9,6));
            repoNota.add(new Nota(new Student(8),new Tema(1),7,7));
            repoNota.add(new Nota(new Student(9),new Tema(1),6,6));
            repoNota.add(new Nota(new Student(10),new Tema(1),6,6));
            repoNota.add(new Nota(new Student(22),new Tema(1),8,7));
            repoNota.add(new Nota(new Student(12),new Tema(1),6,6));
            repoNota.add(new Nota(new Student(13),new Tema(1),10,6));
            repoNota.add(new Nota(new Student(14),new Tema(1),9,6));
            repoNota.add(new Nota(new Student(15),new Tema(1),7,8));
            repoNota.add(new Nota(new Student(16),new Tema(1),6,6));
            repoNota.add(new Nota(new Student(17),new Tema(1),6,7));
            repoNota.add(new Nota(new Student(22),new Tema(5),8,10));
            repoNota.add(new Nota(new Student(12),new Tema(5),6,14));
            repoNota.add(new Nota(new Student(13),new Tema(5),10,12));
            repoNota.add(new Nota(new Student(20),new Tema(5),5,13));
            repoNota.add(new Nota(new Student(21),new Tema(5),6,14));
            repoNota.add(new Nota(new Student(26),new Tema(5),6,14));
            repoNota.add(new Nota(new Student(23),new Tema(5),7,10));
            repoNota.add(new Nota(new Student(24),new Tema(5),6,14));
            repoNota.add(new Nota(new Student(25),new Tema(5),10,12));
            repoNota.add(new Nota(new Student(40),new Tema(4),6,9));
            repoNota.add(new Nota(new Student(41),new Tema(4),6,9));
            repoNota.add(new Nota(new Student(42),new Tema(4),7,11));
            repoNota.add(new Nota(new Student(43),new Tema(4),6,10));
            repoNota.add(new Nota(new Student(60),new Tema(1),6,6));
            repoNota.add(new Nota(new Student(61),new Tema(1),6,7));
            repoNota.add(new Nota(new Student(42),new Tema(1),7,8));
            repoNota.add(new Nota(new Student(63),new Tema(1),6,6));
            repoNota.add(new Nota(new Student(64),new Tema(1),10,6));

        }


        return service;
    }

    public void handleButtonLogin(javafx.event.ActionEvent actionEvent)
    {
        try
        {
            User user=service.Login(txtEmail.getText(),txtPassword.getText());
            labelErrors.setText("");
            Service service=initMenu();
            FXMLLoader loader=new FXMLLoader((getClass().getResource("/FXML/menu.fxml")));
            AnchorPane root=(AnchorPane) loader.load();
            MenuController menuController=loader.getController();
            menuController.setService(service);
            menuController.setActiveUser(user);
            mainAnchorLogin.getChildren().setAll(root);

        }catch(Exception ex)
        {
            ex.printStackTrace();
            labelErrors.setText(ex.getMessage());
        }

    }
    public void handleButtonRegister(javafx.event.ActionEvent actionEvent)
    {
        try
        {
            User user=service.Register(txtEmail.getText(),txtPassword.getText(),txtConfirmPassword.getText());
            labelErrors.setText("");
            Service service=initMenu();
            FXMLLoader loader=new FXMLLoader((getClass().getResource("/FXML/menu.fxml")));
            AnchorPane root=(AnchorPane) loader.load();
            MenuController menuController=loader.getController();
            menuController.setService(service);
            menuController.setActiveUser(user);
            mainAnchorLogin.getChildren().setAll(root);

        }catch(Exception ex){labelErrors.setText(ex.getMessage());}
    }

    public void handleCreateAccount(javafx.event.ActionEvent actionEvent) {
        labelConfirm.setVisible(true);
        txtConfirmPassword.setVisible(true);
        buttonAccount2.setVisible(true);
        buttonLogin.setVisible(false);
        labelLogin.setText("Register");
        linkCreateAccount.setVisible(false);
        linkLogin.setVisible(true);

    }

    public void handleLogin(javafx.event.ActionEvent actionEvent) {
        labelConfirm.setVisible(false);
        txtConfirmPassword.setVisible(false);
        buttonAccount2.setVisible(false);
        buttonLogin.setVisible(true);
        labelLogin.setText("Login");
        linkCreateAccount.setVisible(true);
        linkLogin.setVisible(false);
    }

    public void handleForgotPassword(javafx.event.ActionEvent actionEvent) {
       String email=txtEmail.getText();
       try
       {
           service.RemindPassword(email);
           labelErrors.setText("Check your email");
       }catch (Exception ex)
       {
           labelErrors.setText(ex.getMessage());
       }
    }

    public void setService(AccountService service) {
        this.service = service;
    }
}
