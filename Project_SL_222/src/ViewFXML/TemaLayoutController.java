package ViewFXML;

import Domain.Tema;
import Repository.RepositoryException;
import Service.StudentService;
import Service.TemaService;
import Util.ListEvent;
import Util.Observer;
import Validator.ValidationException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.control.cell.TextFieldTreeTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import javafx.util.converter.IntegerStringConverter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class TemaLayoutController implements Observer<Tema>{
    TemaService service;
    ObservableList<Tema> model;
    @FXML
    private TableView<Tema> temaTable;
    @FXML
    private TableColumn<Tema, Integer> nr;
    @FXML
    private TableColumn<Tema, String> descriere;
    @FXML
    private TableColumn<Tema, Integer> deadline;
    @FXML
    private TableColumn<Tema,Void> edit;
    @FXML
    private TextField nrField;
    @FXML
    private TextField descriereField;
    @FXML
    private TextField deadlineField;

    @FXML
    private TextField filterbydescriptionField;
    @FXML
    private TextField filterbydeadlineField;
    @FXML
    private TextField filterdeadlineminField;
    @FXML
    private TextField filterdeadlinemaxField;

    @FXML
    private Button updatebtn;
    @FXML
    private Button refreshBtn;
    @FXML
    private Pagination pagination;
    @FXML
    private Button doneBtn1;
    @FXML
    private Button doneBtn2;
    @FXML
    private Button doneBtn3;
    @FXML
    private Slider slider;

    public int itemsPerPage() {
        return 1;
    }

    public int rowsPerPage() {
        return (int)slider.getValue();
    }

    public VBox createPage(int pageIndex) {
        if(model.size()%rowsPerPage()==0)
            pagination.setPageCount(model.size()/rowsPerPage());
        else
            pagination.setPageCount(model.size()/rowsPerPage()+1);
        int lastIndex = 0;
        int displace = model.size() % rowsPerPage();
        if (displace > 0) {
            lastIndex = model.size() / rowsPerPage();
        } else {
            lastIndex = model.size() / rowsPerPage();

        }

        VBox box = new VBox(5);
        int page = pageIndex * itemsPerPage();
        for(int i=page;i<page+itemsPerPage();i++){
            if (lastIndex == pageIndex)
                temaTable.setItems(FXCollections.observableArrayList(model.subList(pageIndex * rowsPerPage(), pageIndex * rowsPerPage() + displace)));
            else {
                temaTable.setItems(FXCollections.observableArrayList(model.subList(pageIndex * rowsPerPage(), pageIndex * rowsPerPage() + rowsPerPage())));
            }
            box.getChildren().add(temaTable);
        }
        return box;
    }
    @FXML
    private  Button addBtn;
    @FXML
    private MenuButton filterBtn;
    public void setService(TemaService service) {
        this.service=service;
        this.model= FXCollections.observableArrayList(service.getAllT());
        temaTable.setItems(model);
    }

    @Override
    public void notifyEvent(ListEvent<Tema> e){
        model.setAll(StreamSupport.stream(e.getList().spliterator(),false).collect(Collectors.toList()));
    }

    @FXML
    private void initialize() {
        nr.setCellValueFactory(new PropertyValueFactory<Tema, Integer>("nr"));
        descriere.setCellValueFactory(new PropertyValueFactory<Tema, String>("descriere"));
        deadline.setCellValueFactory(new PropertyValueFactory<Tema, Integer>("deadline"));
        slider.setShowTickLabels(true);
        slider.setMin(1);
        slider.setMajorTickUnit(5);
        slider.setMax(25);
        slider.setValue(3);
        slider.valueProperty().addListener((obs, oldval, newVal) ->
                pagination.setPageFactory(this::createPage));
        //addButtonEditToTable();
        String iconPath="if_Lapiz_52421.png";
        Image icon=new Image(getClass().getResourceAsStream(iconPath),35,35,false,false);
        ImageView img=new ImageView(icon);
        img.fitHeightProperty();
        img.fitWidthProperty();
        updatebtn.setGraphic(new ImageView(icon));
        String iconPath2="if_user_close_add_103747.png";
        Image icon2=new Image(getClass().getResourceAsStream(iconPath2),35,35,false,false);
        ImageView img2=new ImageView(icon2);
        img2.fitHeightProperty();
        img2.fitWidthProperty();
        addBtn.setGraphic(new ImageView(icon2));
        String iconPath3="if_filter_data_44818.png";
        Image icon3=new Image(getClass().getResourceAsStream(iconPath3),32,32,false,false);
        ImageView img3=new ImageView(icon3);
        img3.fitHeightProperty();
        img3.fitWidthProperty();
        filterBtn.setGraphic(new ImageView(icon3));
        String iconPath4="if_view-refresh_118801.png";
        Image icon4=new Image(getClass().getResourceAsStream(iconPath4),25,25,false,false);
        ImageView img4=new ImageView(icon4);
        img4.fitHeightProperty();
        img4.fitWidthProperty();
        refreshBtn.setGraphic(new ImageView(icon4));
        String iconPath5="if_22_Done_Circle_Complete_Downloaded_Added_2142698.png";
        Image icon5=new Image(getClass().getResourceAsStream(iconPath5),32,32,false,false);
        ImageView img5=new ImageView(icon5);
        img5.fitHeightProperty();
        img5.fitWidthProperty();
        doneBtn1.setGraphic(new ImageView(icon5));
        doneBtn2.setGraphic(new ImageView(icon5));
        doneBtn3.setGraphic(new ImageView(icon5));

        pagination.setPageFactory(this::createPage);
    }

    @FXML
    public void handleRefresh(){
        model.clear();
        model.setAll(FXCollections.observableArrayList(service.getAllT()));
        temaTable.setItems(model);
        pagination.setPageFactory(this::createPage);
        clearFields();
        nrField.setDisable(false);
        descriereField.setDisable(false);
    }
    public void clearFields() {
        nrField.clear();
        descriereField.clear();
        deadlineField.clear();
        //view.idField.setDisable(false);
    }

    @FXML
    public void umpleFielduri(){
        Tema tema=temaTable.getSelectionModel().getSelectedItem();
        if(tema!=null){
            nrField.setDisable(true);
            nrField.setText(Integer.toString(tema.getNr()));
            descriereField.setText(tema.getDescriere());
            descriereField.setDisable(true);
            deadlineField.setText(Integer.toString(tema.getDeadline()));
        }
    }

    @FXML
    public void handleSaveTema() {
        try {
            service.addTema(Integer.parseInt(nrField.getText()), descriereField.getText(), Integer.parseInt(deadlineField.getText()));
            pagination.setPageFactory(this::createPage);
            clearFields();
            MessageAlert.showInfoMessage(null, "Assignment added successfully!");
        } catch (ValidationException e) {
            MessageAlert.showErrorMessage(null,e.getMessage());
        } catch (RepositoryException e) {
            MessageAlert.showErrorMessage(null,e.getMessage());
        } catch (NumberFormatException e) {
            MessageAlert.showErrorMessage(null, "Invalid fields!");
        }
    }


    @FXML
    public void handleEditTema(){
        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("CONFIRMATION");
            alert.setContentText("Are you sure you want to update this item?");
            ButtonType okButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
            ButtonType noButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(okButton, noButton);
            Optional<ButtonType> result = alert.showAndWait();
            if ((result.isPresent()) && (result.get() == okButton)) {
                service.updateTermen(Integer.parseInt(nrField.getText()), Integer.parseInt(deadlineField.getText()));
                pagination.setPageFactory(this::createPage);
                MessageAlert.showInfoMessage(null, "Student updated successfully!");
                clearFields();
            }
        } catch (ValidationException e) {
            MessageAlert.showErrorMessage(null,e.getMessage());
        } catch (RepositoryException e) {
            MessageAlert.showErrorMessage(null,e.getMessage());
        } catch (NumberFormatException e) {
            MessageAlert.showErrorMessage(null, "Invalid fields!");
        }
    }

    @FXML
    public void handleFilterByDescription(){
        try{

            List list = service.filterTemaDescriptionCtr(filterbydescriptionField.getText(), "Cresc");
            //temaTable.setItems(FXCollections.observableArrayList(list));
            model.clear();
            model.setAll(FXCollections.observableArrayList(list));
            temaTable.setItems(model);
            pagination.setPageFactory(this::createPage);
            filterbydescriptionField.clear();
        }
        catch (ValidationException e){
            MessageAlert.showErrorMessage(null,e.getMessage());
        }
        catch (RepositoryException e){
            MessageAlert.showErrorMessage(null,e.getMessage());
        }
        catch (NumberFormatException e){
            MessageAlert.showErrorMessage(null,e.getMessage());
        }
    }

    @FXML
    public void handleFilterByDeadline(){
        try{

            List list = service.filterTemaDeadlineCtr(Integer.parseInt(filterbydeadlineField.getText()), "Cresc");
            //temaTable.setItems(FXCollections.observableArrayList(list));
            model.clear();
            model.setAll(FXCollections.observableArrayList(list));
            temaTable.setItems(model);
            pagination.setPageFactory(this::createPage);
            filterbydeadlineField.clear();
        }
        catch (ValidationException e){
            MessageAlert.showErrorMessage(null,e.getMessage());
        }
        catch (RepositoryException e){
            MessageAlert.showErrorMessage(null,e.getMessage());
        }
        catch (NumberFormatException e){
            MessageAlert.showErrorMessage(null,e.getMessage());
        }
    }

    @FXML
    public  void handleFilterValues(){
        try{
            List list = service.filterTemaValues(Integer.parseInt(filterdeadlineminField.getText()),Integer.parseInt(filterdeadlinemaxField.getText()), "Cresc");
            //temaTable.setItems(FXCollections.observableArrayList(list));
            model.clear();
            model.setAll(FXCollections.observableArrayList(list));
            temaTable.setItems(model);
            pagination.setPageFactory(this::createPage);
            filterdeadlinemaxField.clear();
            filterdeadlineminField.clear();
        }
        catch (ValidationException e){
            MessageAlert.showErrorMessage(null,e.getMessage());
        }
        catch (RepositoryException e){
            MessageAlert.showErrorMessage(null,e.getMessage());
        }
        catch (NumberFormatException e){
            MessageAlert.showErrorMessage(null,e.getMessage());
        }
    }
}
