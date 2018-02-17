package Main;

import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Maintest extends Application {

    @Override
    public void start(Stage primaryStage) {

        ObservableList<EditableFileRow> data = FXCollections.observableArrayList(
                new EditableFileRow("A File"),
                new EditableFileRow("A Big File"),
                new EditableFileRow("A Lost File")
        );

        TableColumn editColumn = new TableColumn("Edit");
        editColumn.setCellValueFactory(new PropertyValueFactory<>("editButton"));
        TableColumn fileNameColumn = new TableColumn("File Name");
        fileNameColumn.setCellValueFactory(new PropertyValueFactory<>("fileName"));
        TableView table = new TableView();
        table.getColumns().addAll(editColumn, fileNameColumn);
        table.setItems(data);

        StackPane root = new StackPane();

        root.getChildren().add(table);

        Scene scene = new Scene(root, 300, 250);

        primaryStage.setTitle("Button Column Demo");
        primaryStage.setScene(scene);

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static class EditButton extends Button {

        public EditButton(String fileName) {
            super("Edit");
            setOnAction((event) -> {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Hey!");
                alert.setHeaderText(null);
                alert.setContentText("You're editing \"" + fileName + "\"");
                alert.showAndWait();
            });
        }
    }

    public static class EditableFileRow {

        private final SimpleStringProperty fileName;
        private final SimpleObjectProperty<EditButton> editButton;

        public EditableFileRow(String fileName) {
            this.fileName = new SimpleStringProperty(fileName);
            editButton = new SimpleObjectProperty(new EditButton(fileName));
        }

        public String getFileName() {
            return fileName.get();
        }

        public void setFileName(String fName) {
            fileName.set(fName);
        }

        public StringProperty fileNameProperty() {
            return fileName;
        }

        public EditButton getEditButton() {
            return editButton.get();
        }

        public void setEditButton(EditButton editButton) {
            this.editButton.set(editButton);
        }

        public ObjectProperty<EditButton> editButtonProperty() {
            return editButton;
        }

    }
}
