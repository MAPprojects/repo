package FxmlFiles;

import Entites.Candidate;
import Entites.Option;
import Entites.Section;
import Service.Service;
import Utils.Event;
import Utils.Observer;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Controller implements Observer<Event> {
    @FXML

    public TableView candidateView;
    public TableView sectionView;
    public TableView optionView;

    public TableColumn<Candidate, Integer> columnID;
    public TableColumn<Candidate, String> columnName;
    public TableColumn<Candidate, String> columnPhone;

    public TableColumn<Section, Integer> columnID2;
    public TableColumn<Section, String> columnName2;
    public TableColumn<Section, Integer> columnPlaces;

    public TableColumn<Option, Integer> columnIDC;
    public TableColumn<Option, Integer> columnIDS;
    public TableColumn<Option, Integer> columnPriority;

  //  public TextField idCandidate, name, phoneNumber;
  //  public TextField idSection, name2, number;
  //  public TextField idOption, idCandidate2, idSection2, priority;
    public TextField filter, filter2, filter3;

    private ObservableList<Candidate> model;
    private ObservableList<Section> model2;
    private ObservableList<Option> model3;

    private Service service;
    private Stage editStage;

    @FXML
    public ComboBox<String> comboFilter;
    public ComboBox<String> comboSorter;
    public ComboBox<String> comboFilter2;
    public ComboBox<String> comboSorter2;
    public ComboBox<String> comboFilter3;
    public ComboBox<String> comboSorter3;

    public void setService(Service service) {
        this.service = service;
        this.service.addObserver(this);
        loadDataHandler();
    }

    @FXML
    private void initialize() {
        columnID.setCellValueFactory(new PropertyValueFactory<>("ID"));
        columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnPhone.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        candidateView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showCandidatesDetails((Candidate) newValue));
        candidateView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        columnID2.setCellValueFactory(new PropertyValueFactory<>("ID"));
        columnName2.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnPlaces.setCellValueFactory(new PropertyValueFactory<>("number"));
        sectionView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showSectionsDetails((Section) newValue));
        sectionView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        columnIDC.setCellValueFactory(new PropertyValueFactory<>("idCandidate"));
        columnIDS.setCellValueFactory(new PropertyValueFactory<>("idSection"));
        columnPriority.setCellValueFactory(new PropertyValueFactory<>("priority"));
        optionView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showOptionsDetails((Option) newValue));
        optionView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        candidateView.getSelectionModel().selectedItemProperty().addListener(tableItemListener());

        ObservableList<String> listCombo1 = FXCollections.observableArrayList("name", "phoneNumber");
        comboFilter.setItems(listCombo1);

        ObservableList<String> listCombo2 = FXCollections.observableArrayList("name");
        comboSorter.setItems(listCombo2);

        ObservableList<String> listCombo3 = FXCollections.observableArrayList("name", "number");
        comboFilter2.setItems(listCombo3);

        ObservableList<String> listCombo4 = FXCollections.observableArrayList("name");
        comboSorter2.setItems(listCombo4);

        ObservableList<String> listCombo5 = FXCollections.observableArrayList("idCandidate", "idSection");
        comboFilter3.setItems(listCombo5);

        ObservableList<String> listCombo6 = FXCollections.observableArrayList("idSection");
        comboSorter3.setItems(listCombo6);

    }

    private ChangeListener<Candidate> tableItemListener() {
        return (observable, oldvalue, newValue) -> fillWithData(newValue);
    }

    private void fillWithData(Candidate candidate) {
        if (candidate == null) {
        //    name.setText("");
        //    phoneNumber.setText("");
        } else {
        //    name.setText(candidate.getName());
        //    phoneNumber.setText(candidate.getPhoneNumber());
        }
    }

    private void showCandidatesDetails(Candidate newValue) {
        if (newValue == null)
            clearFields();
        else {
            String id = String.valueOf(newValue.getID());
        //    idCandidate.setText(id);
        //    name.setText(newValue.getName());
        //    phoneNumber.setText(newValue.getPhoneNumber());
        }
    }

    private void showSectionsDetails(Section newValue) {
        if (newValue == null)
            clearFields();
        else {
            String id = String.valueOf(newValue.getID());
          //  idSection.setText(id);
            //name2.setText(newValue.getName());
        //    number.setText(String.valueOf(newValue.getNumber()));
        }
    }

    private void showOptionsDetails(Option newValue) {
        if (newValue == null)
            clearFields();
        else {
            String id = String.valueOf(newValue.getIdCandidate());
          //  idOption.setText(String.valueOf(newValue.getID()));
          //  idCandidate2.setText(id);
          //  idSection2.setText(String.valueOf(newValue.getIdSection()));
          //  priority.setText(String.valueOf(newValue.getPriority()));
        }
    }

    private void clearFields() {
        //idCandidate.setText("");
       // name.setText("");
       // phoneNumber.setText("");
    }

    private void loadCandidates() {
        ArrayList<Candidate> list = service.findAllCandidates();
        model = FXCollections.observableArrayList(list);
        candidateView.setItems(model);
    }

    private void loadSections() {
        ArrayList<Section> list = service.findAllSections();
        model2 = FXCollections.observableArrayList(list);
        sectionView.setItems(model2);
    }

    private void loadOptions() {
        ArrayList<Option> list = service.findAllOptions();
        model3 = FXCollections.observableArrayList(list);
        optionView.setItems(model3);
    }

    private void loadDataHandler() {
        loadCandidates();
        loadSections();
        loadOptions();
    }

    public void addButtonHandler() {
        try {
            Candidate candidate = (Candidate) candidateView.getSelectionModel().getSelectedItem();
            service.addCandidate(candidate);
        } catch (Exception e) {
            showErrorMessage(e.toString());
        }
        clearFields();
    }

    public void addButtonHandler2() {
        try {
            Section section = (Section) sectionView.getSelectionModel().getSelectedItem();
            service.addSection(section);
        } catch (Exception e) {
            showErrorMessage(e.toString());
        }
        clearFields();
    }

    public void addButtonHandler3() {
        try {
            Option option = (Option) optionView.getSelectionModel().getSelectedItem();
            service.addOption(option);
        } catch (Exception e) {
            showErrorMessage(e.toString());
        }
        clearFields();
    }

    public void deleteButtonHandler() {
        Candidate candidate = (Candidate) candidateView.getSelectionModel().getSelectedItem();
        int index = candidateView.getSelectionModel().getFocusedIndex();
        try {
            service.deleteCandidate(candidate.getID());
            candidateView.getSelectionModel().select(index == model.size() ? index - 1 : index);
            showMessage(Alert.AlertType.INFORMATION, "You deleted:" + candidate);
        } catch (Exception e) {
            showErrorMessage(e.toString());
        }
        clearFields();
    }

    public void deleteButtonHandler2() {
        Section section = (Section) sectionView.getSelectionModel().getSelectedItem();
        int index = sectionView.getSelectionModel().getFocusedIndex();
        try {
            service.deleteSection(section.getID());
            sectionView.getSelectionModel().select(index == model.size() ? index - 1 : index);
            showMessage(Alert.AlertType.INFORMATION, "You deleted:" + section);
        } catch (Exception e) {
            showErrorMessage(e.toString());
        }
        clearFields();
    }

    public void deleteButtonHandler3() {

        Option option = (Option) optionView.getSelectionModel().getSelectedItem();
        int index = optionView.getSelectionModel().getFocusedIndex();
        try {
            service.deleteOption(option.getID());
            candidateView.getSelectionModel().select(index == model.size() ? index - 1 : index);
            showMessage(Alert.AlertType.INFORMATION, "You deleted:" + option);
        } catch (Exception e) {
            showErrorMessage(e.toString());
        }
        clearFields();
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

    @FXML
    private void updateButtonHandler() {
        Candidate candidate = (Candidate) candidateView.getSelectionModel().getSelectedItem();
        if(candidate==null){
            showErrorMessage("Select one item first!");
        }
        else
        showEditStage(candidate);
    }

    @FXML
    private void updateButtonHandler2() {
        Section section = (Section) sectionView.getSelectionModel().getSelectedItem();
        if(section==null)
            showErrorMessage("Select one item first ;)");
        else {

            showEditStage2(section);
        }
    }

    @FXML
    private void updateButtonHandler3() {
        Option option = (Option) optionView.getSelectionModel().getSelectedItem();
        if (option == null) {
            showErrorMessage("Select one item first");
        }
       else
            showEditStage3(option);
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

    private void showEditStage2(Section section) {
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

    private void showEditStage3(Option option) {

        try {
            FXMLLoader loader = new FXMLLoader();

            loader.setLocation(Controller.class.getResource("EditOptionView.fxml"));

            AnchorPane editPane = loader.load();

            Stage stage = new Stage();
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setScene(new Scene(editPane));
            Image image = new Image("img2.png");
            stage.getIcons().add(image);
            EditOption ctrl = loader.getController();
            ctrl.setService(service, stage, option);
            ctrl.addObserver(this);

            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
        loadOptions();
    }

    @Override
    public void notifyOnEvent(Event candidateEvent) {
        List<Candidate> list = service.findAllCandidates();
        model.setAll(list);
        List<Section> list2 = service.findAllSections();
        model2.setAll(list2);
        List<Option> list3 = service.findAllOptions();
        model3.setAll(list3);

    }

    @FXML
    public void doButtonHandler() {
        List<Candidate> result;
        String value = filter.getText().toString();
        if (comboFilter.getSelectionModel().getSelectedItem().equals("name")) {
            result = service.filterByName(value);
        } else {
            result = service.filterByPhoneNumber(value);
        }
        model.setAll(result);
    }

    @FXML
    public void doButtonHandler2() {
        List<Section> result;
        String value = filter2.getText().toString();
        if (comboFilter2.getSelectionModel().getSelectedItem().equals("name")) {
            result = service.filterByNameSection(value);
        } else {
            result = service.filterByNumber(Integer.valueOf(value));
        }
        model2.setAll(result);
    }

    @FXML
    public void doButtonHandler3() {
        List<Option> result;
        String value = filter3.getText().toString();
        if (comboFilter3.getSelectionModel().getSelectedItem().equals("idCandidate")) {
            result = service.filterByIdCandidate(Integer.valueOf(value));
        } else {
            result = service.filterByIdSection(Integer.valueOf(value));
        }
        model3.setAll(result);
    }

    @FXML
    public void backButton() {
        List<Candidate> list = service.findAllCandidates();
        model.setAll(list);
    }

    @FXML
    public void backButton2() {
        List<Section> list = service.findAllSections();
        model2.setAll(list);
    }

    @FXML
    public void backButton3() {
        List<Option> list = service.findAllOptions();
        model3.setAll(list);
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

    @FXML
    public void sortHandler2() {
        List<Section> result;
        if (comboSorter2.getSelectionModel().getSelectedItem().equals("name")) {
            result = service.sortByNameSection();
        } else
            result = service.findAllSections();
        model2.setAll(result);

    }

    @FXML
    public void sortHandler3() {
        List<Option> result;
        if (comboSorter3.getSelectionModel().getSelectedItem().equals("idSection")) {
            result = service.sortByIdSection();
        } else
            result = service.findAllOptions();
        model3.setAll(result);

    }
}