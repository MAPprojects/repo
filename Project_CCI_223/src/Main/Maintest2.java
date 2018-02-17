package Main;// Here is the full code.
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Maintest2 extends Application
{

    @Override
    public void start(Stage primaryStage)
    {
        TableView<SimpleStringProperty> table = new TableView<SimpleStringProperty>();
        table.getSelectionModel().setCellSelectionEnabled(true);
        table.setEditable(true);

        table.getColumns().add(this.createColumn());

        ObservableList<SimpleStringProperty> rowData = FXCollections.observableArrayList();
        //table.getItems().addAll(rowData);
        for(int j = 0; j < 10; j++)
        {
            rowData.add(new SimpleStringProperty(String.format("Cell [%d", j)));
        }

        table.setItems(rowData);

        table.setOnKeyTyped(event -> {
            TablePosition<SimpleStringProperty, String> focusedCell = table.getFocusModel().getFocusedCell();
            if (focusedCell != null)
            {
                table.getItems().get(focusedCell.getRow()).set(event.getCharacter());
                table.edit(focusedCell.getRow(), focusedCell.getTableColumn());
            }
        });

        Scene scene = new Scene(new BorderPane(table), 880, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private TableColumn<SimpleStringProperty, String> createColumn()
    {
        TableColumn<SimpleStringProperty, String> col = new TableColumn<>("Column ");
        col.setCellValueFactory(cellData -> cellData.getValue());
        col.setCellFactory(column -> new EditCell());
        return col;
    }

    private static class EditCell extends TableCell<SimpleStringProperty, String>
    {

        private final TextField textField = new TextField();

        EditCell()
        {
            this.textProperty().bind(this.itemProperty());
            this.setGraphic(this.textField);
            this.setContentDisplay(ContentDisplay.TEXT_ONLY);

            this.textField.setOnAction(evt -> this.commitEdit(this.textField.getText()));
            this.textField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
                if (!isNowFocused)
                {
                    this.commitEdit(this.textField.getText());
                }
            });
        }

        @Override
        public void startEdit()
        {
            super.startEdit();
            this.textField.setText(this.getItem());
            this.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            this.textField.requestFocus();
        }

        @Override
        public void cancelEdit()
        {
            super.cancelEdit();
            this.setContentDisplay(ContentDisplay.TEXT_ONLY);
        }

        @Override
        public void commitEdit(String text)
        {
            super.commitEdit(text);
            this.setContentDisplay(ContentDisplay.TEXT_ONLY);
        }

    }

    public static void main(String[] args)
    {
        launch(args);
    }
}

