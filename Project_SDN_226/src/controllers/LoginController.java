package controllers;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import controllers.dashController;
import db_stuff.DBConnect;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import repositories.*;
import services.StaffManager;
import validators.CandidateValidator;
import validators.DepartmentValidator;
import validators.OptionValidator;
import views.formDecorator;

import java.io.IOException;

public class LoginController {
    @FXML
    private JFXTextField usernameLogin;

    @FXML
    private JFXPasswordField passwordLogin;

    private DBConnect db;

    @FXML
    public void initialize() {
        db = new DBConnect();

    }

    /**
     * Handler for closing page
     * @param mouseEvent
     */
    public void handleCloseLoginButton(MouseEvent mouseEvent) {
        db.close();
        Platform.exit();

    }

    /**
     * Handler for login button
     * @param mouseEvent
     */
    public void handleLoginButton(MouseEvent mouseEvent) {
        String user = usernameLogin.getText();
        String pass = passwordLogin.getText();

        if (db.init(user, pass)) {
           Integer accesLevel = db.getAccesLevel(user);
            db.close();

            try {
                Stage st = (Stage) usernameLogin.getScene().getWindow();
                st.close();

                CandidateValidator Cval = new CandidateValidator();
                DepartmentValidator Dval = new DepartmentValidator();
                OptionValidator Oval = new OptionValidator();


                MYSQLCandidateRepository reposql_c = new MYSQLCandidateRepository();
                MYSQLDepartmentRepository reposql_d = new MYSQLDepartmentRepository();
                MYSQLOptionRepository reposql_o = new MYSQLOptionRepository();

               // CandidateFileRepository repof_c = new CandidateFileRepository("resources\\candidates.txt");
               // DepartmentFileRepository repof_d = new DepartmentFileRepository("resources\\departments.txt");
               // OptionFileRepository repof_o = new OptionFileRepository("resources\\options.txt");

                StaffManager service = new StaffManager(reposql_c, reposql_d, reposql_o, Cval, Dval, Oval);

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/general/dash.fxml"));
                Parent root = (Parent) loader.load();
                dashController dc = loader.getController();

                dc.seteaza(service ,accesLevel );

                Stage newStage = new Stage();
                newStage.getIcons().add(new Image("images\\icon.png"));

                Scene newScene = new Scene(root);

                newStage.initStyle(StageStyle.TRANSPARENT);

                Scene scene = new Scene(new formDecorator(newStage, root));
                scene.setFill(null);

                newStage.setScene(scene);


                newStage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
