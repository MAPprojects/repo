package viewController;

import entities.Sectie;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

public class SectieTableController extends AbstractTableController<Sectie,Integer>{

    @FXML
    private TableColumn<Sectie, Integer> idColumn;
    @FXML
    private TableColumn<Sectie, String> nameColumn;
    @FXML
    private TableColumn<Sectie, Integer> numberOfPlacesColumn;

    @FXML
    protected void initialize() {

        idColumn.setCellValueFactory(new PropertyValueFactory<Sectie, Integer>("ID"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<Sectie, String>("nume"));
        numberOfPlacesColumn.setCellValueFactory(new PropertyValueFactory<Sectie, Integer>("nrLoc"));
        super.initialize();
    }


}
