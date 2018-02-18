package View;

import Service.Service;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class SettingsController {
    private Service service;
    private String accountName;
    private String accountRole;
    private String accountPicturePath;
    private RootController rootController;

    @FXML
    private ImageView profileImageView;
    @FXML
    private Text accountRoleText;
    @FXML
    private Text accountUsernameText;

    public void setService(Service service) {
        this.service = service;

        Image image;
        try {
            image = new Image(accountPicturePath);
        } catch (IllegalArgumentException e) {
            image = new Image("Resources/Images/Missing.jpg");
        }
        profileImageView.setImage(image);
        profileImageView.setFitHeight(70);
        profileImageView.setFitWidth(50);
        profileImageView.setSmooth(true);

        accountUsernameText.setText(accountName);

        accountRoleText.setText(accountRole);
        if (accountRole.equals("Standard User")) {
            accountRoleText.setFill(Color.WHITE);
        }
        else if (accountRole.equals("Moderator")) {
            accountRoleText.setFill(Color.BLUE);
        }
        else if (accountRole.equals("Administrator")) {
            accountRoleText.setFill(Color.RED);
        }
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public void setAccountRole(String accountRole) {
        this.accountRole = accountRole;
    }

    public void setAccountPicturePath(String accountPicturePath) {
        this.accountPicturePath = accountPicturePath;
    }

    public void setRootController(RootController rootController) {
        this.rootController = rootController;
    }
}
