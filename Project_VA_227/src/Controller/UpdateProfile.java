package Controller;

import Domain.User;
import Utils.Database;
import Utils.ResourceManager;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class UpdateProfile implements Initializable {
    public Button updateBtn;
    public TextField username;
    public TextField name;
    public TextField email;
    public TextField avatarImg;
    public ImageView imageView;
    public Label message_no_passwd;
    public PasswordField oldpassword;
    public PasswordField newpassword;
    public PasswordField renewpassword;

    private User user;
    public static boolean Updated = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setUser(User user) {
        this.user = user;
        username.setText(user.getUsername());
        name.setText(user.getName());
        email.setText(user.getEmail());
        imageView.setImage(user.getAvatarImg());
        imageView.setClip(new Circle(50, 50, 45));
        is_deleted = false;
    }

    private boolean is_deleted;

    public void UpdateProfile() {
        if (!Pattern.compile("[a-zA-Z][a-zA-Z0-9_.]{4,}").matcher(username.getText()).matches())
            AbstractController.showError("Username invalid. Should have minimum five chars (letters, digits, _ or .) and first char should be a letter");
        else if (!user.getUsername().equals(username.getText()) && Register.existUser(username.getText()))
            AbstractController.showError("User already exist.");
        else if (name.getLength() == 0)
            AbstractController.showError("Name should not be empty.");
        else if (!Pattern.compile("[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?").matcher(email.getText()).matches())
            AbstractController.showError("Email is not valid.");
        else {
            try (PreparedStatement cmd = Database.getConnection().prepareStatement(imageFile == null && !is_deleted ? "UPDATE [User] SET Name = ?, Email = ?, Username = ? WHERE ID = ?" : "UPDATE [User] SET Name = ?, Email = ?, AvatarImg = ? WHERE ID = ?")) {
                cmd.setString(1, name.getText());
                cmd.setString(2, email.getText());
                cmd.setString(3, username.getText());
                if (imageFile == null && !is_deleted)
                    cmd.setInt(4, user.getID());
                else {
                    cmd.setBinaryStream(4, is_deleted ? null : new FileInputStream(imageFile), is_deleted ? 0 : imageFile.length());
                    cmd.setInt(5, user.getID());
                }
                cmd.executeUpdate();
                user.setUsername(username.getText());
                user.setName(name.getText());
                user.setEmail(email.getText());
                if (imageFile != null || is_deleted)
                    user.setAvatarImg(is_deleted ?
                            SwingFXUtils.toFXImage(ImageIO.read(new File(ResourceManager.getResourceMangager().getValue("AvatarDefault"))), null)
                            : SwingFXUtils.toFXImage(ImageIO.read(imageFile), null));
                Updated = true;
                ((Stage) updateBtn.getScene().getWindow()).close();
                updateBtn.getScene().getWindow().getOnCloseRequest().handle(null);
            } catch (Exception ignored) {
            }
        }
    }

    private File imageFile = null;

    public void handleBrowse() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Avatar Img");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All Files", "*.*"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg", "*.jpeg"),
                new FileChooser.ExtensionFilter("PNG", "*.png"),
                new FileChooser.ExtensionFilter("GIF", "*.gif"),
                new FileChooser.ExtensionFilter("Bitmap", "*.btm"));
        File imageFile1 = fileChooser.showOpenDialog(avatarImg.getScene().getWindow());
        if (imageFile1 != null) {
            imageFile = imageFile1;
            avatarImg.setText(imageFile.getName());
            try {
                imageView.setImage(SwingFXUtils.toFXImage(ImageIO.read(imageFile), null));
            } catch (IOException ignored) {
            }
            is_deleted = false;
        }
    }

    public void resetImg() {
        is_deleted = true;
        imageFile = null;
        avatarImg.clear();
        try {
            imageView.setImage(SwingFXUtils.toFXImage(ImageIO.read(new File(ResourceManager.getResourceMangager().getValue("AvatarDefault"))), null));
        } catch (IOException ignored) {
        }
    }

    public void setUserForPasword(User user) {
        this.user = user;
        if (user.isPasswordSet())
            message_no_passwd.setVisible(false);
        else
            oldpassword.setVisible(false);
    }

    public void changePassword() {
        if (user.isPasswordSet() && !LogIn.existUser(user.getUsername(), oldpassword.getText()).getValue()) {
            oldpassword.clear();
            AbstractController.showError("Old password is wrong !");
        } else if (newpassword.getLength() < 6)
            AbstractController.showError("Password should have minimum 6 chars");
        else if (!newpassword.getText().equals(renewpassword.getText()))
            AbstractController.showError("Password doesn't match.");
        else {
            try (PreparedStatement cmd = Database.getConnection().prepareStatement("UPDATE [User] SET Password = ? WHERE ID = ?")) {
                cmd.setString(1, newpassword.getText());
                cmd.setInt(2, user.getID());
                cmd.executeUpdate();
                user.setPasswordSet(true);
                ((Stage) newpassword.getScene().getWindow()).close();
                newpassword.getScene().getWindow().getOnCloseRequest().handle(null);
            } catch (SQLException ignored) {
            }
        }
    }
}
