package MVC;

import Service.Service;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class MainController {
    private Service service;
    Stage primaryStage;

    @FXML
    private Hyperlink creareCont;
    @FXML
    private CheckBox retineCheckBox;
    @FXML
    private ImageView logoImageView;
    @FXML
    private TextField utilizatorTextField;
    @FXML
    private PasswordField parolaTextField;

    private String mailAnumitUtilizator;


    @FXML
    private void initialize() {
        Image image = new Image("studentLogo.png");
        logoImageView.setImage(image);
        logoImageView.setFitHeight(80);
        logoImageView.setFitWidth(65);
        logoImageView.setSmooth(true);
        incarcareUP(null);
    }

    public static void showMessage(Alert.AlertType type, String header, String text) {
        Alert message = new Alert(type);
        message.setHeaderText(header);
        message.setContentText(text);
        message.showAndWait();
    }

    public static void showErrorMessage(String text) {
        showMessage(Alert.AlertType.WARNING, "Warning!", text);
    }

    @FXML
    private void verificareLogin(ActionEvent actionEvent){
        ArrayList<Pair<String,String>> perechi=new ArrayList<>();
        ArrayList<String> mailPerechi=new ArrayList<>();
        try (BufferedReader br=new BufferedReader(new FileReader("conturi.txt"))){
            String linie;
            while ((linie=br.readLine())!=null){
                String[] s=linie.split("[;]");
                if (s.length!=3){
                    showErrorMessage("Fisier corupt!");
                    continue;
                }
                perechi.add(new Pair<>(s[0],s[1]));
                mailPerechi.add(s[2]);
            }
            boolean gasit=false;
            int i=0;
            for (Pair<String,String> p : perechi){
                if (perechi.get(i).getKey().equals(utilizatorTextField.getText()) &&
                        perechi.get(i).getValue().equals(hashPassword(parolaTextField.getText()))) {
                    gasit = true;
                    mailAnumitUtilizator=mailPerechi.get(i);
                    break;
                }
                i++;
            }

            boolean admin=false;
            if (Objects.equals(utilizatorTextField.getText(), "admin"))
                admin=true;
            if (!gasit) {
                showErrorMessage("Utilizator sau parola incorecta!");
            }
            else{
                //meniu nou
                salvareUP(null);
                primaryStage.close();
                intraInMeniulPrincipal(admin);
            }

        } catch (IOException e){
            showErrorMessage("Fisierul nu poate fi deschis!");
        }
    }

    private void intraInMeniulPrincipal(boolean admin) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MeniuView.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Meniu principal");
            stage.getIcons().add(new Image(getClass().getResourceAsStream("../studentLogo.png")));
            //stage.setResizable(false);

            MeniuController meniuController = fxmlLoader.getController();
            meniuController.setDrepturi(admin);
            meniuController.setMailAnumitUtilizator(mailAnumitUtilizator);
            meniuController.setService(service);
            stage.show();
        } catch (IOException e) {
            showErrorMessage("Nu se poate incarca meniul principal!");
        }
    }

    @FXML
    private void incarcareUP(ActionEvent actionEvent) {
        try (Stream<String> s = Files.lines(Paths.get("retine.txt"))){
            String[] l=s.collect(Collectors.toList()).get(0).split("[|]",-1);
            if (l[0].equals("1")) {
                //bifat
                utilizatorTextField.setText(l[1]);
                parolaTextField.setText(l[2]);
                mailAnumitUtilizator=l[3];
                retineCheckBox.setSelected(true);
            }
            else {
                retineCheckBox.setSelected(false);
                utilizatorTextField.clear();
                parolaTextField.clear();
            }
        } catch (IOException e){
            //
        }
    }

    public void salvareUP(ActionEvent actionEvent){
        try (PrintWriter pw= new PrintWriter("retine.txt")) {
            String s="";
            if (retineCheckBox.isSelected())
                s += "1|"+utilizatorTextField.getText()+"|"+parolaTextField.getText()+"|"+mailAnumitUtilizator;
            else s+="0|||";
            pw.println(s);
        } catch (IOException e){
            //
        }
    }

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
        primaryStage.setOnCloseRequest(event -> salvareUP(null));
    }

    @FXML
    private void creeazaCont(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CreareContView.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Creare cont");
            stage.getIcons().add(new Image(getClass().getResourceAsStream("../studentLogo.png")));

            CreareContController creareContController = fxmlLoader.getController();

            stage.show();
        } catch (IOException e) {
            showErrorMessage("Nu se poate crea cont nou!");
        }
    }

    public void setService(Service service) {
        this.service = service;
    }

    public String getMailAnumitUtilizator(){return mailAnumitUtilizator;}

    public static String hashPassword(String password) {
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());
            byte[] bytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte aByte : bytes)
                sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
            generatedPassword = sb.toString();
            return generatedPassword;
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
