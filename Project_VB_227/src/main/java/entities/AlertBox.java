package entities;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class AlertBox {
    public static void display(String title, String message) {
        Stage stage = new Stage();

        stage.initModality(Modality.APPLICATION_MODAL);
//        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setTitle(title);
        stage.setMinWidth(350);

        Label label = new Label();
        label.setText(message);
        label.setStyle("-fx-font-weight: bold;-fx-text-fill: white;-fx-font-size: 16;");
        Button closeButton = new Button("Close");
        closeButton.setStyle("-fx-background-color: white;-fx-text-fill: red;-fx-font-weight: bold;" +
                "-fx-background-radius: 30;-fx-background-insets: 5;");
        closeButton.setPrefWidth(80);
        closeButton.setPrefHeight(40);
        closeButton.setOnAction(node -> stage.close());

        VBox layout = new VBox(30);
        layout.getChildren().addAll(label, closeButton);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: red;");

        Scene scene = new Scene(layout);
        stage.setScene(scene);
        stage.showAndWait();
    }
}
