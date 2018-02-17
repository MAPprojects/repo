package FxmlFiles;

import Entites.Section;
import Service.Service;
import Utils.DBConnection;
import Utils.Event;
import Utils.Observer;
import com.sun.javafx.scene.control.skin.TableColumnHeader;
import com.sun.javafx.scene.control.skin.TableViewSkin;
import com.sun.javafx.scene.control.skin.VirtualFlow;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SectionsView implements Observer<Event> {
    private Service service;
    private int lastIndex=0;
    @FXML
    public TableView<Section> sectionsView;
    public TableColumn<Section, Integer> idColumn;
    public TableColumn<Section, String> nameColumn;
    public TableColumn<Section, String> numberColumn;
    private ObservableList<Section> model;
    private Stage editStage;
    public TextField filter;
    public ComboBox<String> comboSorter;
    public FontAwesomeIconView addButton;
    public FontAwesomeIconView updateButton;
    public FontAwesomeIconView deleteButton;
    public boolean isAdmin;

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("ID"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        numberColumn.setCellValueFactory(new PropertyValueFactory<>("number"));
        sectionsView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        filter.textProperty().addListener((observable, oldValue, newValue) -> {
            filterButtonHandler();
        });
        ObservableList<String> listCombo2 = FXCollections.observableArrayList("name", "none");
        comboSorter.setItems(listCombo2);
        sectionsView.addEventFilter(ScrollEvent.ANY, scrollEvent -> manageData());
        sectionsView.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            if ((event.getTarget() instanceof TableColumnHeader) | event.isDragDetect()) {
                manageData();
            }
        });
    }
    @FXML
    private void manageData() {
        if(lastIndex-1<this.getVisibleRows())
        {
            lastIndex=this.getVisibleRows();
            service.alertLoadDataS();
            loadData();
            //System.out.println("load data"+lastIndex + "  "+ getVisibleRows());
        }
        else {



        }
    }
    @FXML
    public int getVisibleRows() {
        TableViewSkin<?> skin = (TableViewSkin<?>) sectionsView.getSkin();
        if (skin == null) return  0;
        VirtualFlow<?> flow = (VirtualFlow<?>) skin.getChildren().get(1);
        int idxFirst;
        int idxLast;
        if (flow != null &&
                flow.getFirstVisibleCellWithinViewPort() != null &&
                flow.getLastVisibleCellWithinViewPort() != null) {
            idxFirst = flow.getFirstVisibleCellWithinViewPort().getIndex();
            if (idxFirst > sectionsView.getItems().size()) {
                idxFirst = sectionsView.getItems().size() - 1;
            }
            idxLast = flow.getLastVisibleCellWithinViewPort().getIndex();
            if (idxLast > sectionsView.getItems().size()) {
                idxLast = sectionsView.getItems().size() - 1;
            }
        }
        else {
            idxFirst = 0;
            idxLast = 0;
        }
        return idxLast;
    }

    public void setService(Service service) {

        this.service = service;
        this.service.addObserver(this);
        loadData();
    }

    private void loadData() {
        ArrayList<Section> list = service.findAllSections();
        model = FXCollections.observableArrayList(list);
        sectionsView.setItems(model);
        if(!isAdmin){
            addButton.setDisable(true);
            addButton.setVisible(false);
            updateButton.setDisable(true);
            updateButton.setVisible(false);
            deleteButton.setDisable(true);
            deleteButton.setVisible(false);
        }
    }

    @FXML
    private void addButtonHandler() {
        showAddStage();
    }

    private void showAddStage() {
        try {
            FXMLLoader loader = new FXMLLoader();

            loader.setLocation(Controller.class.getResource("AddSectionView.fxml"));

            AnchorPane editPane = loader.load();

            Stage stage = new Stage();
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setScene(new Scene(editPane));
            Image image = new Image("img2.png");
            stage.getIcons().add(image);
            AddSectionView ctrl = loader.getController();
            ctrl.setService(service, stage);
            ctrl.addObserver(this);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void updateButtonHandler() {
        Section section =  sectionsView.getSelectionModel().getSelectedItem();
        if (section == null) {
            showErrorMessage("Select one item first!");
        } else
            showEditStage(section);
    }

    private void showEditStage(Section section) {
        try {
            FXMLLoader loader = new FXMLLoader();

            loader.setLocation(Controller.class.getResource("EditSectionView.fxml"));

            AnchorPane editPane = loader.load();

            Stage stage = new Stage();
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setScene(new Scene(editPane));
            Image image = new Image("img2.png");
            stage.getIcons().add(image);
            EditSection ctrl = loader.getController();
            ctrl.setService(service, stage, section);
            ctrl.addObserver(this);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteButtonHandler() {
        Section section = (Section) sectionsView.getSelectionModel().getSelectedItem();
        int index = sectionsView.getSelectionModel().getFocusedIndex();
        try {

            service.deleteSection(section.getID());
            sectionsView.getSelectionModel().select(index == model.size() ? index - 1 : index);
            showMessage(Alert.AlertType.INFORMATION, "You deleted:" + section);
        } catch (Exception e) {
            showErrorMessage(e.toString());
        }
    }

    @FXML
    public void filterButtonHandler() {
        List<Section> result;
        String value = filter.getText();
        if (value.equals(""))
            result = service.findAllSections();
        else
            result = service.filterByNameSection(value);
        model.setAll(result);
    }

    @FXML
    public void sortHandler() {
        List<Section> result;
        if (comboSorter.getSelectionModel().getSelectedItem().equals("name")) {
            result = service.sortByNameSection();
        } else
            result = service.findAllSections();
        model.setAll(result);

    }

    private void showErrorMessage(String text) {
        Alert message = new Alert(Alert.AlertType.ERROR);
        message.setTitle("Error Message");
        message.initOwner(editStage);
        message.setContentText(text);
        message.showAndWait();
    }

    private void showMessage(Alert.AlertType type, String text) {
        Alert message = new Alert(type);
        message.setHeaderText("");
        message.setContentText(text);
        message.showAndWait();
    }

    @Override
    public void notifyOnEvent(Event event) {
        loadData();
    }
}
