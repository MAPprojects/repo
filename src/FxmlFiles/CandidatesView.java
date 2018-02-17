package FxmlFiles;

import Entites.Candidate;
import Service.Service;
import Utils.Event;
import Utils.Observer;
import com.sun.javafx.scene.control.skin.TableColumnHeader;
import com.sun.javafx.scene.control.skin.TableViewSkin;
import com.sun.javafx.scene.control.skin.VirtualFlow;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import java.util.ArrayList;
import java.util.List;

public class CandidatesView implements Observer<Event> {
    private Service service;

    private int lastIndex=0;
    @FXML
    public TableView<Candidate> candidatesView;
    public TableColumn<Candidate, Integer> idColumn;
    public TableColumn<Candidate, String> nameColumn;
    public TableColumn<Candidate, String> phoneColumn;
    private ObservableList<Candidate> model;
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
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        candidatesView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        filter.textProperty().addListener((observable, oldValue, newValue) -> {
            filterButtonHandler();
        });
        ObservableList<String> listCombo2 = FXCollections.observableArrayList("name", "none");
        comboSorter.setItems(listCombo2);
        candidatesView.addEventFilter(ScrollEvent.ANY, scrollEvent -> manageData());
        candidatesView.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
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
            service.alertLoadDataC();
            loadData();
        //    System.out.println("load data"+lastIndex + "  "+ getVisibleRows());
        }
        else {
         //   System.out.println("scrool up,"+lastIndex +" "+ getVisibleRows());


        }
    }
    @FXML
    public int getVisibleRows() {
        TableViewSkin<?> skin = (TableViewSkin<?>) candidatesView.getSkin();
        if (skin == null) return  0;
        VirtualFlow<?> flow = (VirtualFlow<?>) skin.getChildren().get(1);
        int idxFirst;
        int idxLast;
        if (flow != null &&
                flow.getFirstVisibleCellWithinViewPort() != null &&
                flow.getLastVisibleCellWithinViewPort() != null) {
            idxFirst = flow.getFirstVisibleCellWithinViewPort().getIndex();
            if (idxFirst > candidatesView.getItems().size()) {
                idxFirst = candidatesView.getItems().size() - 1;
            }
            idxLast = flow.getLastVisibleCellWithinViewPort().getIndex();
            if (idxLast > candidatesView.getItems().size()) {
                idxLast = candidatesView.getItems().size() - 1;
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
        ArrayList<Candidate> list = service.findAllCandidates();
        model = FXCollections.observableArrayList(list);
        candidatesView.setItems(model);
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
            loader.setLocation(Controller.class.getResource("AddCandidateView.fxml"));
            AnchorPane editPane = loader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setScene(new Scene(editPane));
            Image image = new Image("img2.png");
            stage.getIcons().add(image);
            AddCandidateView ctrl = loader.getController();
            ctrl.setService(service, stage);
            ctrl.addObserver(this);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void updateButtonHandler() {
        Candidate candidate =  candidatesView.getSelectionModel().getSelectedItem();
        if (candidate == null) {
            showErrorMessage("Select one item first!");
        } else
            showEditStage(candidate);
    }

    private void showEditStage(Candidate candidate) {
        try {
            FXMLLoader loader = new FXMLLoader();

            loader.setLocation(Controller.class.getResource("EditCandidateView.fxml"));

            AnchorPane editPane = loader.load();

            Stage stage = new Stage();
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setScene(new Scene(editPane));
            Image image = new Image("img2.png");
            stage.getIcons().add(image);
            EditCandidate ctrl = loader.getController();
            ctrl.setService(service, stage, candidate);
            ctrl.addObserver(this);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteButtonHandler() {
        Candidate candidate = candidatesView.getSelectionModel().getSelectedItem();
        int index = candidatesView.getSelectionModel().getFocusedIndex();
        try {
            service.deleteCandidate(candidate.getID());
            candidatesView.getSelectionModel().select(index == model.size() ? index - 1 : index);
            showMessage(Alert.AlertType.INFORMATION, "You deleted:" + candidate);
        } catch (Exception e) {
            showErrorMessage(e.toString());
        }
    }

    @FXML
    public void filterButtonHandler() {
        List<Candidate> result;
        String value = filter.getText();
        if (value.equals(""))
            result = service.findAllCandidates();
        else
            result = service.filterByName(value);
        model.setAll(result);
    }

    @FXML
    public void sortHandler() {
        List<Candidate> result;
        if (comboSorter.getSelectionModel().getSelectedItem().equals("name")) {
            result = service.sortByName();
        } else
            result = service.findAllCandidates();
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
