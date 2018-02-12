package Controller;

import Utils.ResourceManager;
import javafx.fxml.Initializable;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.ResourceBundle;

public class LogInFacebook implements Initializable {

    public WebView browser;
    public static JSONObject userInfo = null;
    public static String accessToken = null;
    public static InputStream image = null;
    public LogInFacebook() {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void auth() {
        browser.getEngine().load(
                String.format("http://graph.facebook.com/oauth/authorize?type=user_agent&client_id=%s&redirect_uri=%s&scope=%s",
                        ResourceManager.getResourceMangager().getValue("FacecebookAppId"), ResourceManager.getResourceMangager().getValue("RedirectUri"),
                        ResourceManager.getResourceMangager().getValue("FacebookScope")));
        browser.getEngine().locationProperty().addListener((observableValue, oldValue, newValue) -> {
            if (!browser.getEngine().getLocation().contains("facebook")) {
                String code = browser.getEngine().getLocation().replaceAll(".*#access_token=(.+)&.*", "$1");
                String content = getUrlContents(String.format("https://graph.facebook.com/me?fields=id,name,email&access_token=%s/", code));

                try {
                    userInfo = new JSONObject(content);
                    image = getUrlBinary(String.format("https://graph.facebook.com/me/picture?width=100&heigth=100&access_token=%s/", code));
                    accessToken = code;
                } catch (Exception ignored) {

                }
                Stage stage = (Stage) browser.getScene().getWindow();
                stage.hide();
            }
        });
    }

    private static InputStream getUrlBinary(String url) {
        try {
            URL connection = new URL(url);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            InputStream is;

            is = connection.openStream();
            byte[] byteChunk = new byte[4096];
            int n;

            while ((n = is.read(byteChunk)) > 0) {
                baos.write(byteChunk, 0, n);
            }
            is.close();
            return new ByteArrayInputStream(baos.toByteArray());
        } catch (Exception ignored) {

        }
        return null;
    }

    private static String getUrlContents(String url1) {
        try {
            URL url = new URL(url1);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(true);
            connection.connect();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder results = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                results.append(line);
            }
            connection.disconnect();
            return results.toString();
        } catch (Exception ignored) {
        }
        return "";
    }
}
