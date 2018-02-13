package sample;

import Domain.User;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;


import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Random;

import Service.Service;
import javafx.scene.text.Text;

public class Controller {
    @FXML BorderPane borderPaneMain;
    @FXML MenuItem studentMenuItem,temeMenuItem,noteMenuItem,exitMenuItem,filtrari;
    @FXML TextField userText;
    @FXML PasswordField userPass;
    @FXML MenuBar menuBar;
    @FXML
    Text logMessage;

    Service service;
    private int user=0;
    private int pass=0;
    private AnchorPane studentPane,labPane,gradePane,filterPane,averagePane,promotedPane;
    private StudentController stdctr;
    private LabController lbctr;
    private GradeController grctr;
    private FilterController flctr;
    private AverageController avrctr;
    private PromotedController prctr;
    private String captcha;
    private List<User> useri;
    public Controller(){}

    private void incarcareFisiereFXML(){
        //fac aici incarcari pentru ca altfel nu ma lasa sa fac la pornire initializare
        //ca nu prind IOException
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/utils/studentGraphic.fxml"));
            studentPane = (AnchorPane) loader.load();
            stdctr = loader.getController();
            stdctr.setServices(service);
            service.addObserver(stdctr);

            FXMLLoader loader2=new FXMLLoader(getClass().getResource("/utils/homeworkGraphic.fxml"));
            labPane=(AnchorPane) loader2.load();
            lbctr=loader2.getController();
            lbctr.setService(service);
            service.addObserver(lbctr);

            FXMLLoader loader3=new FXMLLoader(getClass().getResource("/utils/gradesGraphic.fxml"));
            gradePane=(AnchorPane) loader3.load();
            grctr=loader3.getController();
            grctr.setServices(service);
            service.addObserver(grctr);

            FXMLLoader loader4=new FXMLLoader(getClass().getResource("/utils/filtrari.fxml"));
            filterPane=(AnchorPane) loader4.load();
            flctr=loader4.getController();
            flctr.setServices(service);


            FXMLLoader loader5=new FXMLLoader(getClass().getResource("/utils/average.fxml"));
            averagePane=(AnchorPane) loader5.load();
            avrctr=loader5.getController();
            avrctr.setServices(service);
            service.addObserver(avrctr);

            FXMLLoader loader6=new FXMLLoader(getClass().getResource("/utils/promoted.fxml"));
            promotedPane=(AnchorPane) loader6.load();
            prctr=loader6.getController();
            prctr.setServices(service);
            service.addObserver(prctr);



        }catch (IOException e){
            showErrorMessage("Eroare la incarcare :D");
        }

    }

    public void setServices(Service service) {
        this.service=service;
        incarcareFisiereFXML();
        exitMenuItem.setOnAction(this::closeApp);
        studentMenuItem.setOnAction(this::openStudent);
        temeMenuItem.setOnAction(this::openHomework);
        noteMenuItem.setOnAction(this::openGrades);
        filtrari.setOnAction(this::openFilters);
    }

    @FXML
    public void openStudent(ActionEvent event){
        borderPaneMain.setCenter(studentPane);
    }
    @FXML
    public void openHomework(ActionEvent event){
        borderPaneMain.setCenter(labPane);
    }
    @FXML
    public void openGrades(ActionEvent event){
        grctr.setPagination();
        borderPaneMain.setCenter(gradePane);
    }
    @FXML
    public void openFilters(ActionEvent actionEvent){
        borderPaneMain.setCenter(filterPane);
    }
    @FXML
    public void closeApp(ActionEvent event){
        Platform.exit();
        System.exit(0);
    }

    @FXML
    public void openAverageStatistics(ActionEvent actionEvent){
        borderPaneMain.setCenter(averagePane);
    }

    @FXML
    public void openPromotedStatistics(ActionEvent actionEvent){
        borderPaneMain.setCenter(promotedPane);
    }

    private String generateCaptchaCode(){
        String captcha="";
        int i=1;
        Random r= new Random();
        String[] source={"A","2","a","1","B","3","b","4","C","c","D","5","d","E","6","e","F","7","f","8","G","g","9","H","h","@","I","#","i","$","J","%","j","*","K","^","k","L","l","M","m","N","n","O","o","P","p","Q","q","R","r","S","s","T","t","W","w","X","x","Y","y","Z","z"};
        while(i<=8){
            int res=r.nextInt(61);
            captcha=captcha+source[res];
            i++;
        }
        return captcha;
    }

    private void setAbilities(String user){
        borderPaneMain.setCenter(gradePane);
        menuBar.setVisible(false);
        //stdctr.setAbilities();
        //lbctr.setAbilities();
        grctr.setAbilities(user);
    }

    @FXML
    public void handleLogInButton(ActionEvent event){
        try {
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.reset();
            m.update(userPass.getText().getBytes());
            byte[] digest = m.digest();
            BigInteger bigInt = new BigInteger(1, digest);
            String hashtext = bigInt.toString(16);
            // Now we need to zero pad it if you actually want the full 32 chars.
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            userPass.setText(hashtext);
        }catch (NoSuchAlgorithmException ex){
            System.out.println("Nu s-a putut produce translatarea paroleri..");
        }

        useri=service.getAllUsers();
        String erorile="";
        for(int i=0;i<useri.size();i++) {
            if (userText.getText().equals(useri.get(i).getUsername()) && userPass.getText().equals(useri.get(i).getPassword())) {
                user = 1;
                break;
            }
        }
        try {
            if(!(user==1))
                erorile=erorile+"Datele nu sunt valide!";
            if(!erorile.equals(""))
                throw new FXExceptions(erorile);

            incarcareFisiereFXML();
            menuBar.setVisible(true);
            logMessage.setVisible(false);
            borderPaneMain.setCenter(gradePane);
            exitMenuItem.setOnAction(this::closeApp);
            studentMenuItem.setOnAction(this::openStudent);
            temeMenuItem.setOnAction(this::openHomework);
            noteMenuItem.setOnAction(this::openGrades);
            filtrari.setOnAction(this::openFilters);
            if(!userText.getText().equals("admin"))
                setAbilities(userText.getText());
        }catch (FXExceptions me){
            showErrorMessage(me.getMessage());
            clearFields();
        }
    }

    private void clearFields(){
        userText.clear();
        userPass.clear();
    }



    private static void showErrorMessage(String text){
        Alert message=new Alert(Alert.AlertType.ERROR);
        message.setTitle("Mesaj eroare");
        message.setContentText(text);
        message.showAndWait();
    }

    @FXML
    public void initialize(){
        menuBar.setVisible(false);
    }
}
