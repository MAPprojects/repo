package FXML;



        import java.io.IOException;
        import java.net.URL;
        import java.sql.*;
        import java.util.ResourceBundle;


        import Domain.Profesor;
        import Domain.User;
        import Repository.*;
        import Service.Service;
        import javafx.event.ActionEvent;
        import javafx.fxml.FXML;
        import javafx.fxml.FXMLLoader;
        import javafx.scene.Node;
        import javafx.scene.Parent;
        import javafx.scene.Scene;
        import javafx.scene.control.Button;
        import javafx.scene.control.TextField;
        import javafx.stage.Stage;
        import java.io.FileInputStream;
        import java.io.UnsupportedEncodingException;
        import java.math.BigInteger;
        import java.security.MessageDigest;
        import java.security.NoSuchAlgorithmException;


public class LoginController {
    private LoginRepository repoUsers=new LoginRepository();
//    StudentRepoSQL repoStudenti = new StudentRepoSQL(new StudentValidator());
//    TemeRepoSQL repoTeme = new TemeRepoSQL(new TemeValidator());
//    NoteRepoSQL noteRepo =new NoteRepoSQL(new NoteValidate());
//
//    Service service=new Service(new Service.ServiceNote(),repoTeme,repoStudenti);
    private StartApp main;
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button bt_login;

    @FXML
    private TextField tf_username;

    @FXML
    private TextField tf_pass;

    public LoginController() {
    }
    public static String getMD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger number = new BigInteger(1, messageDigest);
            String hashtext = number.toString(16);
            // Now we need to zero pad it if you actually want the full 32 chars.
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void handleLoginScene(ActionEvent event) throws IOException, SQLException {


        User user=repoUsers.usersExist(tf_username.getText());

        if (user!=null) {
            System.out.println(user.getPass());
            System.out.println(getMD5(tf_pass.getText()));
            if(user.getPass().equals(getMD5(tf_pass.getText()))) {

                if(user.getProfesor()==1){


                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("ProfesorPart.fxml"));
                    Parent profesorPart =  loader.load();

                    Scene profesorPartScene = new Scene(profesorPart);
                    ProfesorPartController controller=loader.getController();


                    controller.setUser(tf_username.getText(),tf_pass.getText());

                    Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    window.setScene(profesorPartScene);

                    window.show();
                    window.setResizable(false);
                    window.setTitle("Admin");
                }
                else
                    if (user.getProfesor()==2){
                        Parent profesorTable = FXMLLoader.load(getClass().getResource("Admin.fxml"));
                        Scene profesorTable_scene = new Scene(profesorTable);

                        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        window.setScene(profesorTable_scene);
                        window.show();
                        window.setResizable(false);
                        window.setTitle("Profesor");
                    }
                    else
                        if (user.getProfesor()==0){
                            /*Parent profesorTable = FXMLLoader.load(getClass().getResource("StudentPart.fxml"));*/
                            FXMLLoader loader = new FXMLLoader();
                            loader.setLocation(getClass().getResource("StudentPart.fxml"));
                            Parent studentPart =  loader.load();

//                            Scene studentPartScene = new Scene(studentPart);
                            StudentPartControllerFXML controller=loader.getController();


                            controller.setUser(tf_username.getText(),tf_pass.getText());
                            Scene studentPartScene = new Scene(studentPart);
                            controller.initData();

                            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                            window.setScene(studentPartScene);
                            window.setTitle("Student");
                            window.show();
                            window.setResizable(false);
                        }
            }
            else {
                MessageAlert.showErrorMessage(null,"User sau parola gresita!","Login");
                tf_pass.setStyle("-fx-border-color: #CC3F12 ");
            }
        }
        else
            MessageAlert.showErrorMessage(null,"User sau parola gresita!","Login");
            tf_pass.setStyle("-fx-border-color: #CC3F12 ");
            tf_username.setStyle("-fx-border-color: #CC3F12 ");
    }

    @FXML
    void initialize() {
        assert bt_login != null : "fx:id=\"bt_login\" was not injected: check your FXML file 'LoginPage.fxml'.";
        assert tf_username != null : "fx:id=\"tf_username\" was not injected: check your FXML file 'LoginPage.fxml'.";
        assert tf_pass != null : "fx:id=\"tf_pass\" was not injected: check your FXML file 'LoginPage.fxml'.";

    }
}
