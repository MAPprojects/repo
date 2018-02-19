package viewController;

import entities.Candidat;
import entities.HybridOptiune;
import entities.Optiune;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;

import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;



public class CandidatTableController extends AbstractTableController<Candidat,Integer>  {
    @FXML
    private TableColumn<Candidat, Integer> idColumn;
    @FXML
    private TableColumn<Candidat, String> nameColumn;
    @FXML
    private TableColumn<Candidat, String> phoneColumn;
    @FXML
    private TableColumn<Candidat, String> emailColumn;
    @FXML
    private TableColumn<Candidat, Boolean> optionColumn;


    @FXML
    protected void initialize() {

        rowsPerPage=11;
        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        phoneColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        emailColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        CandidatTableController thisController = this;
        idColumn.setCellValueFactory(new PropertyValueFactory<Candidat, Integer>("ID"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<Candidat, String>("nume"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<Candidat, String>("telefon"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<Candidat, String>("email"));
        optionColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Candidat, Boolean>, ObservableValue<Boolean>>() {
            @Override public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<Candidat, Boolean> features) {
                return new SimpleBooleanProperty(features.getValue() != null);
            }
        });

        optionColumn.setCellFactory(new Callback<TableColumn<Candidat, Boolean>, TableCell<Candidat, Boolean>>() {
            @Override
            public TableCell<Candidat, Boolean> call(TableColumn<Candidat, Boolean> personBooleanTableColumn) {
                return new AddButtonsToCell(table,thisController);
            }
        });
        super.initialize();

    }

}

class AddButtonsToCell extends TableCell<Candidat, Boolean> {
    // a button for adding a new person.
    Button addButton       = new Button();
    Button searchButton = new Button();
    // pads and centers the add button in the cell.
    StackPane paddedButton = new StackPane();
    HBox hBox = new HBox();
    AnchorPane anchorPane = new AnchorPane();
    TableView table;



    AddButtonsToCell(final TableView table, CandidatTableController controller) {
        //paddedButton.setPadding(new Insets(3));
        this.table=table;
        Image addImage = new Image(getClass().getClassLoader().getResourceAsStream("icons8_Add_32px.png"));
        Image searchImage = new Image(getClass().getClassLoader().getResourceAsStream("icons8_Search_32px.png"));
        addButton.setMaxSize(25,100);
        ImageView addImageView = new ImageView( addImage);
        addImageView.setFitHeight(15);
        addImageView.setFitWidth(15);
        ImageView searchImageView = new ImageView( searchImage);
        searchImageView.setFitHeight(15);
        searchImageView.setFitWidth(15);
        addButton.setGraphic(addImageView);
        searchButton.setGraphic(searchImageView);
        searchButton.setMaxSize(25,100);
        hBox.getChildren().add(addButton);
        hBox.getChildren().add(searchButton);
        addButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent actionEvent) {
                Candidat c = (Candidat) table.getItems().get(getIndex());
                controller.mainWindowsController.showOptiuneDialog(new HybridOptiune(-1,c.getID(),-1,c.getNume(),""));
            }
        });
        searchButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent actionEvent) {
                Candidat c = (Candidat) table.getItems().get(getIndex());
                controller.mainWindowsController.handleOptiuniForCandidat(c);
            }
        });

    }

    @Override protected void updateItem(Boolean item, boolean empty) {
        super.updateItem(item, empty);
        if (!empty) {
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            setGraphic(hBox);
        }
    }


}