package Domain;

import Repository.HasID;
import Utils.Database;
import Utils.ResourceManager;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.InputStream;
import java.sql.*;

public class User {
    private String Username;
    private String Name;
    private Integer ID;
    private Tip tip;
    private String FacebookID, GoogleID;
    private Image AvatarImg;
    private String accessTokenFabebook = null;
    private String Email;
    private Integer MailOption = 0;
    private boolean passwordSet = true;

    public User(Integer ID) {
        this.ID = ID;
        try (PreparedStatement cmd = Database.getConnection().prepareStatement("SELECT * FROM [USER] WHERE ID = ?")) {
            cmd.setInt(1, ID);
            try (ResultSet result = cmd.executeQuery()) {
                while (result.next()) {
                    Username = result.getObject("Username").toString();
                    Name = result.getObject("Name").toString();
                    FacebookID = result.getObject("FacebookID") == null ? null : result.getObject("FacebookID").toString();
                    GoogleID = result.getObject("GoogleID") == null ? null : result.getObject("GoogleID").toString();
                    InputStream inputStream = result.getBinaryStream("AvatarImg");
                    AvatarImg = inputStream == null ? SwingFXUtils.toFXImage(ImageIO.read(new File(ResourceManager.getResourceMangager().getValue("AvatarDefault"))), null) : SwingFXUtils.toFXImage(ImageIO.read(inputStream), null);
                    tip = Integer.parseInt(result.getObject("Tip").toString()) == 0 ? Tip.ADMIN : Tip.USER;
                    Email = result.getObject("Email").toString();
                    MailOption = result.getInt("MailOption");
                }
            }
        } catch (Exception ignored) {
        }
    }

    private boolean existFbUser() {
        try (PreparedStatement cmd = Database.getConnection().prepareStatement("SELECT * FROM [USER] WHERE FacebookID = ?")) {
            cmd.setString(1, this.getFacebookID());
            try (ResultSet result = cmd.executeQuery()) {
                boolean ok = false;
                if (result.next()) {
                    Username = result.getObject("Username").toString();
                    Name = result.getObject("Name").toString();
                    ID = Integer.parseInt(result.getObject("ID").toString());
                    GoogleID = result.getObject("GoogleID") == null ? null : result.getObject("GoogleID").toString();
                    InputStream inputStream = result.getBinaryStream("AvatarImg");
                    AvatarImg = inputStream == null ? SwingFXUtils.toFXImage(ImageIO.read(new File(ResourceManager.getResourceMangager().getValue("AvatarDefault"))), null) : SwingFXUtils.toFXImage(ImageIO.read(inputStream), null);
                    tip = Integer.parseInt(result.getObject("Tip").toString()) == 0 ? Tip.ADMIN : Tip.USER;
                    Email = result.getObject("Email").toString();
                    MailOption = result.getInt("MailOption");
                    if (result.getString("Password").equals("blablabla123456"))
                        passwordSet = false;
                    ok = true;
                }
                return ok;
            }
        } catch (Exception ignored) {
        }
        return false;
    }

    private boolean existFbEmail() {
        try (PreparedStatement cmd = Database.getConnection().prepareStatement("SELECT * FROM [USER] WHERE Email = ?")) {
            cmd.setString(1, this.getEmail());
            try (ResultSet result = cmd.executeQuery()) {
                boolean ok = false;
                if (result.next()) {
                    ok = true;
                    ID = Integer.parseInt(result.getObject("ID").toString());
                    Username = result.getObject("Username").toString();
                    Name = result.getObject("Name").toString();
                    GoogleID = result.getObject("GoogleID") == null ? null : result.getObject("GoogleID").toString();
                    InputStream inputStream = result.getBinaryStream("AvatarImg");
                    AvatarImg = inputStream == null ? SwingFXUtils.toFXImage(ImageIO.read(new File(ResourceManager.getResourceMangager().getValue("AvatarDefault"))), null) : SwingFXUtils.toFXImage(ImageIO.read(inputStream), null);
                    tip = Integer.parseInt(result.getObject("Tip").toString()) == 0 ? Tip.ADMIN : Tip.USER;
                    updateFbID();
                }
                return ok;
            }
        } catch (Exception ignored) {
        }
        return false;
    }

    private void updateFbID() {
        try (PreparedStatement cmd = Database.getConnection().prepareStatement("UPDATE [USER] SET FacebookID = ? WHERE ID = ?")) {
            cmd.setString(1, this.getFacebookID());
            cmd.setInt(2, this.getID());
            cmd.executeUpdate();
        } catch (SQLException ignored) {

        }
    }

    private void addFbUser(JSONObject userInfo, InputStream image)
    {
        try (PreparedStatement cmd = Database.getConnection().prepareStatement("INSERT INTO [USER](Username, Password, [Name], FacebookID, Email, AvatarImg) VALUES(?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            Username = String.format("user%s", this.getFacebookID());
            Name = userInfo.getString("name");
            tip = Tip.USER;
            cmd.setString(1, String.format("user%s", this.getFacebookID()));
            cmd.setString(2, "blablabla123456");
            cmd.setString(3, userInfo.getString("name"));
            cmd.setString(4, this.getFacebookID());
            cmd.setString(5, this.getEmail());
            cmd.setBinaryStream(6, image);
            cmd.executeUpdate();
            try (ResultSet resultSet = cmd.getGeneratedKeys())
            {
                if (resultSet.next())
                    ID = resultSet.getInt(1);
            }
            passwordSet = false;
        } catch (Exception ignored) {

        }
    }

    public User(String accessTokenFabebook, JSONObject userInfo, InputStream image) {
        this.accessTokenFabebook = accessTokenFabebook;
        try {
            this.setFacebookID(userInfo.getString("id"));
            if (!existFbUser()) {
                this.setEmail(userInfo.getString("email"));
                if (!existFbEmail()) {
                    addFbUser(userInfo, image);
                    try (PreparedStatement cmd = Database.getConnection().prepareStatement("SELECT AvatarImg FROM [USER] WHERE ID = ?"))
                    {
                        cmd.setInt(1, ID);
                        try (ResultSet resultSet = cmd.executeQuery())
                        {
                            if (resultSet.next())
                                AvatarImg = SwingFXUtils.toFXImage(ImageIO.read(resultSet.getBinaryStream("AvatarImg")), null);
                        }
                    }
                }
            }
        } catch (Exception ignored) {
            System.out.println(ignored.getMessage());
        }
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public Tip getTip() {
        return tip;
    }

    public void setTip(Tip tip) {
        this.tip = tip;
    }

    public String getFacebookID() {
        return FacebookID;
    }

    public void setFacebookID(String facebookID) {
        FacebookID = facebookID;
    }

    public String getGoogleID() {
        return GoogleID;
    }

    public void setGoogleID(String googleID) {
        GoogleID = googleID;
    }

    public Image getAvatarImg() {
        return AvatarImg;
    }

    public void setAvatarImg(Image avatarImg) {
        AvatarImg = avatarImg;
    }

    public String getAccessTokenFabebook() {
        return accessTokenFabebook;
    }

    public void setAccessTokenFabebook(String accessTokenFabebook) {
        this.accessTokenFabebook = accessTokenFabebook;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public boolean isPasswordSet() {
        return passwordSet;
    }

    public void setPasswordSet(boolean passwordSet) {
        this.passwordSet = passwordSet;
    }

    public Integer getMailOption() {
        return MailOption;
    }

    public void setMailOption(Integer mailOption) {
        try (PreparedStatement cmd = Database.getConnection().prepareStatement("UPDATE [USER] SET MailOption = ? WHERE ID = ?")){
            cmd.setInt(1, mailOption);
            cmd.setInt(2, ID);
            cmd.executeUpdate();
            MailOption = mailOption;
        } catch (SQLException ignored) {

        }
    }
}
