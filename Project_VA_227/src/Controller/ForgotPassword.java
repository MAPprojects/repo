package Controller;

import Utils.Database;
import Utils.ResourceManager;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import java.util.ResourceBundle;

public class ForgotPassword implements Initializable {
    private static String FROM, emailpass ;
    public TextField username;
    public Button resetBtn;

    private String generatePassword(Integer length)
    {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < length) {
            int index = (int) (rnd.nextFloat() * chars.length());
            salt.append(chars.charAt(index));
        }
        return salt.toString();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            FROM = ResourceManager.getResourceMangager().getValue("FromEmail");
            byte[] publicKeybytes = Files.readAllBytes(Paths.get(ResourceManager.getResourceMangager().getValue("PubKey")));
            String encryptedText = Files.readAllLines(Paths.get(ResourceManager.getResourceMangager().getValue("EmailPassword"))).get(0);
            KeyFactory keyPairGenerator = KeyFactory.getInstance("RSA");
            PublicKey publicKey = keyPairGenerator.generatePublic(new X509EncodedKeySpec(publicKeybytes));
            emailpass = MainMenuController.decryptMessage(encryptedText, publicKey);
        } catch (Exception ignored) {
        }
    }

    private Integer updatePass(String pass)
    {
        try (PreparedStatement cmd = Database.getConnection().prepareStatement("UPDATE [User] SET Password = ? WHERE Username = ?"))
        {
            cmd.setString(1, pass);
            cmd.setString(2, username.getText().trim());
            return cmd.executeUpdate();
        } catch (SQLException ignored) {

        }
        return 0;
    }

    public void reset() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION,
                "If this username exists, you'll get a new password on registered email.",
                ButtonType.OK);
        alert.setTitle("Reset Password");
        alert.showAndWait();
        Random random = new Random();
        String password = this.generatePassword(5 + (random.nextInt() % 10));
        if (updatePass(password) != 0)
        {
            try (PreparedStatement cmd = Database.getConnection().prepareStatement("SELECT Email FROM [User] WHERE Username = ?"))
            {
                cmd.setString(1, username.getText());
                try (ResultSet resultSet = cmd.executeQuery())
                {
                    if (resultSet.next())
                    {
                        String Email = resultSet.getString("Email");
                        Thread thread = new Thread(() ->
                        {
                            try {
                                MainMenuController.sendFromGMail(FROM, emailpass, Email, "Reset Password",
                                        String.format("<div>Your password was reset.<br><strong>Username</strong> : %s<br><strong>Password</strong> : %s<br><br> Regards,<br>HomeworkSystem Team</div>",
                                                username.getText(), password));
                            } catch (Exception ignored) {
                            }
                        });
                        thread.start();
                    }
                }

            } catch (Exception ignored) {
            }
        }
        ((Stage) resetBtn.getScene().getWindow()).close();
        resetBtn.getScene().getWindow().getOnCloseRequest().handle(null);
    }
}
