package controllers;

import com.jfoenix.controls.JFXTextField;
import entities.Department;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import services.StaffManager;
import utils.ListEvent;
import utils.Observer;
import validators.RepositoryException;
import validators.ValidationException;

import java.io.IOException;

public class departmentController implements Observer<Department> {
    StaffManager service;

    ObservableList<Department> lista;

    private IntegerProperty limit = new SimpleIntegerProperty(10);

    @FXML
    private JFXTextField textFieldSearch;

    @FXML
    private Pagination pagination;

    @FXML
    private TableView<Department>  depView;

    @FXML
    private TableColumn<Department , Integer> idColumn;

    @FXML
    private TableColumn<Department , String> nameColumn;

    @FXML
    private TableColumn<Department , Integer> sizeColumn;

    @FXML
    private JFXTextField idText;

    @FXML
    private JFXTextField nameText;

    @FXML
    private JFXTextField sizeText;


    /**
     * Initialize fxml elements
     */
    @FXML
    public void initialize() {

        lista = FXCollections.observableArrayList();

        idColumn.setCellValueFactory(new PropertyValueFactory<Department, Integer>("Id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<Department, String>("Name"));
        sizeColumn.setCellValueFactory((new PropertyValueFactory<Department, Integer>("NrLoc")));

        depView.getSelectionModel().selectedItemProperty().addListener((observable, oldvalue, newValue) -> {
            if (newValue == null)
                setFields(null);
            else
                setFields(newValue);
        });

        pagination.currentPageIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                changeTableView(newValue.intValue(), limit.get());
            }

        });

        limit.addListener(new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                changeTableView(pagination.getCurrentPageIndex(), newValue.intValue());
            }

        });
        depView.setItems(lista);
    }


    /**
     * Reinitialize table for current page
     * @param index
     * @param limit
     */
    public void changeTableView(int index, int limit) {

        depView.getItems().clear();
        depView.setItems(null);
        lista.clear();

        service.showNextValuesDepartments(index,limit).forEach(lista::add);
        depView.setItems(lista);

    }

    /**
     * Load paginated data in tabel
     */
    public void init() {
        resetPage();
        pagination.setCurrentPageIndex(0);
        changeTableView(0, limit.get());
    }

    /**
     * Resets page count
     */
    public void resetPage() {
        int size = (int)service.ShowSizeDepartments();
        int pageCount = size/limit.getValue() + 1;
        pagination.setPageCount(pageCount);
    }

    /**
     * Load data in fields
     * @param d
     */
    private void setFields(Department d) {
        if (d == null) {
            idText.setText("");
            nameText.setText("");
            sizeText.setText("");
        } else {
            idText.setText(String.valueOf(d.getId()));
            nameText.setText(d.getName());
            sizeText.setText(d.getNrLoc().toString());
          }
    }

    /**
     * Set up things for departmentController
     * @param staffService
     */
    public void seteaza(StaffManager staffService) {
        this.service = staffService;
        setUpDepartments();

    }

    /**
     * Initialize data
     */
    private void setUpDepartments() {
        init();

    }

    /**
     * Handler for adding a department
     * @param actionEvent
     */
    public void handleAddButton(ActionEvent actionEvent) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/administrator/department.fxml"));
            Parent root = null;
            try {
                root = (Parent) loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            addDepController dc = loader.getController();
            dc.seteaza(service);
            dc.addObserver(this);

            Stage newStage = new Stage();
            newStage.getIcons().add(new Image("images\\icon.png"));
            newStage.initModality(Modality.APPLICATION_MODAL);
            Scene newScene = new Scene(root);
            newStage.initOwner(this.idText.getScene().getWindow());


            newStage.setScene(newScene);
            newStage.show();


    }

    /**
     * Handler for deleting a department
     * @param actionEvent
     */
    public void handleDeleteButton(ActionEvent actionEvent) {
        Integer id = 0;

        try {
            id = Integer.parseInt(idText.getText());
            this.service.deleteDepartment(id);

        } catch (ValidationException | RepositoryException e) {
            showErrorMessage("Delete error", e.getMessage());
        } catch (NumberFormatException e) {
            showErrorMessage("Delete Error", "ID must not be empty");
        }

        setUpDepartments();
        idText.setText("");
        idText.setText("");
        idText.setText("");

    }


    /**
     * Handler for updating a department
     * @param actionEvent
     */
    public void handleUpdateButton(ActionEvent actionEvent) {
        Integer id = 0;
        Integer size = 0;
        String name = "";

        try {
            id = Integer.parseInt(idText.getText());
            name = nameText.getText();
            size = Integer.parseInt(sizeText.getText());

            this.service.updateDepartment(id, name, size);

        } catch (NumberFormatException e) {
            showErrorMessage("Edit Error", "ID/Size must not be positive integers");
        } catch (RepositoryException | ValidationException e) {
            showErrorMessage("Edit Error", e.getMessage());
        }
        setUpDepartments();
        idText.setText("");
        nameText.setText("");
        sizeText.setText("");
    }

    /**
     * Handler for searching a department
     * @param keyEvent
     */
    public void handleSearch(KeyEvent keyEvent) {
        ObservableList<Department> lista_aux = FXCollections.observableArrayList();
        this.service.filterDepartments(textFieldSearch.getText()).forEach(lista_aux::add);
        this.depView.setItems(lista_aux);
    }

    /**
     * Show an Error Alert with a given title and content
     * @param title
     * @param content
     */
    static void showErrorMessage(String title, String content) {
        Alert message = new Alert(Alert.AlertType.ERROR);
        message.setTitle(title);
        message.setContentText(content);
        message.showAndWait();
    }

    /**
     * Handler for changing limit for pagination
     * @param actionEvent
     */
    public void changeLimit(ActionEvent actionEvent) {
        TextField txt = (TextField) actionEvent.getSource();
        if (txt != null) {
            try {
                int i = Integer.parseInt(txt.getText());
                if(i!=0) {
                    limit.set(i);

                    resetPage();
                }

            } catch (NumberFormatException nfe) {
                System.err.println("NFE error");
            }
        }

    }

    @Override
    public void notifyEvent(ListEvent<Department> e) {
        setUpDepartments();
    }
}
