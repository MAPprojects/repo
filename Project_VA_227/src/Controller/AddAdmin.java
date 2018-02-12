package Controller;

import Domain.User;
import Utils.Database;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AddAdmin implements Initializable {

    public ComboBox<String> users;
    public ImageView image;
    public Button addbtn;
    public Label userDetails;
    private List<User> list_users;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addbtn.setDisable(true);
        list_users = new ArrayList<>();
        try (PreparedStatement cmd = Database.getConnection().prepareStatement("SELECT ID, Username FROM [USER] WHERE Tip = ?")) {
            cmd.setInt(1, 1);
            try (ResultSet resultSet = cmd.executeQuery()) {
                while (resultSet.next()) {
                    list_users.add(new User(resultSet.getInt("ID")));
                    users.getItems().add(resultSet.getString("Username"));
                }
            }

        } catch (SQLException ignored) {
        }
        if (list_users.size() == 0)
            users.setPromptText("No normal user");
    }

    public void add_admin() {
        User user = list_users.get(users.getSelectionModel().getSelectedIndex());
        try (PreparedStatement cmd = Database.getConnection().prepareStatement("UPDATE [User] SET Tip = ? WHERE ID = ?"))
        {
            cmd.setInt(1, 0);
            cmd.setInt(2, user.getID());
            cmd.executeUpdate();
            ((Stage) addbtn.getScene().getWindow()).close();
            addbtn.getScene().getWindow().getOnCloseRequest().handle(null);
        } catch (SQLException ignored) {

        }


    }

    public void selectedChange() {
        if (users.getSelectionModel().getSelectedIndex() >= 0) {
            addbtn.setDisable(false);
            User user = list_users.get(users.getSelectionModel().getSelectedIndex());
            image.setImage(user.getAvatarImg());
            image.setClip(new Circle(50, 50, 45));
            userDetails.setText(String.format("Username : %s\nName : %s\nEmail : %s", user.getUsername(),
                    user.getName(), user.getEmail()));
        }
    }
}
