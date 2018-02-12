package controller;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class LoadingSceneController {
    @FXML
    private ImageView loadingGifImage;
    @FXML
    private AnchorPane loadingRootAnchor;

    @FXML
    public void initialize() {
        loadingRootAnchor.heightProperty().addListener((obs, oldVal, newVal) -> {
            loadingGifImage.setLayoutY(loadingRootAnchor.getHeight() / 2 - 75);
        });
        loadingRootAnchor.widthProperty().addListener((obs, oldVal, newVal) -> {
            loadingGifImage.setLayoutX(loadingRootAnchor.getWidth() / 2 - 100);
        });
    }
}
