package ViewFXML;

import Domain.Nota;
import Domain.NotaID;
import Domain.Student;
import Domain.Tema;
import Repository.RepositoryException;
import Service.NotaService;
import Service.StudentService;
import Util.ListEvent;
import Util.Observer;
import Validator.ValidationException;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class NotaLayoutController implements Observer<Nota>{
    NotaService service;
    StudentService serviceStudent;
    ObservableList<Nota> model;
    ObservableList<Student> model2;
    @FXML
    private TableView<Nota> notaTable;
    @FXML
    private TableColumn<Nota, String> studentName;
    @FXML
    private TableColumn<Nota, String> temaName;
    @FXML
    private TableColumn<Nota, Double> value;
    @FXML
    private TextField idStudent;
    @FXML
    private TextField nrTema;
    @FXML
    private TextField numeField;
    @FXML
    private TextField obsField;
    @FXML
    private TextField valoare;

    @FXML
    private TextField filterValueField;
    @FXML
    private TextField filterminvalueField;
    @FXML
    private TextField filtermaxvalueField;
    @FXML
    private TextField filteridField;

    @FXML
    private Button updatebtn;

    @FXML
    private  Button addBtn;

    @FXML
    private Button refreshBtn;
    @FXML
    private MenuButton filterBtn;
    @FXML
    private Button doneBtn1;
    @FXML
    private Button doneBtn2;
    @FXML
    private Button doneBtn3;
    @FXML
    private Slider slider;
    @FXML
    private Pagination pagination;

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
            if (lastIndex == pageIndex) {
                notaTable.setItems(FXCollections.observableArrayList(model.subList(pageIndex * rowsPerPage(), pageIndex * rowsPerPage() + displace)));
            }
            else
                notaTable.setItems(FXCollections.observableArrayList(model.subList(pageIndex * rowsPerPage(), pageIndex * rowsPerPage() + rowsPerPage())));
            box.getChildren().add(notaTable);
        }
        return box;
    }

    public void setService(NotaService service) {
        this.service=service;
        //this.model2=FXCollections.observableArrayList(service1.getAllS());
        //for(Nota nota:service.getAllN())
            //if(service1.findStudent(nota.getID().getIdStudent())==false)
                //service.deleteNota(nota.getID());
        this.model= FXCollections.observableArrayList(service.getAllN());
        notaTable.setItems(model);
    }

    @Override
    public void notifyEvent(ListEvent<Nota> e){
        model.setAll(StreamSupport.stream(e.getList().spliterator(),false).collect(Collectors.toList()));
    }

    @FXML
    private void initialize() {
            studentName.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Nota, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<Nota, String> c) {
                    return new SimpleStringProperty(service.findStudent(c.getValue().getIdStudent()).getNume());
                }
            });
            temaName.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Nota, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<Nota, String> c) {
                    return new SimpleStringProperty(service.findTema(c.getValue().getNrTema()).getDescriere());
                }
            });
            value.setCellValueFactory(new PropertyValueFactory<Nota, Double>("valoare"));
        slider.setShowTickLabels(true);
        slider.setMin(1);
        slider.setMajorTickUnit(5);
        slider.setMax(25);
        slider.setValue(5);
        slider.valueProperty().addListener((obs, oldval, newVal) ->
                pagination.setPageFactory(this::createPage));
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
    public void umpleFielduri(){
        Nota nota=notaTable.getSelectionModel().getSelectedItem();
        if(nota!=null){
            idStudent.setDisable(true);
            idStudent.setText(Integer.toString(nota.getIdStudent()));
            nrTema.setText(Integer.toString(nota.getNrTema()));
            nrTema.setDisable(true);
            numeField.setText(service.findStudent(nota.getIdStudent()).getNume());
            valoare.setText(Double.toString(nota.getValoare()));
        }
    }

    private void clearFields(){
        idStudent.clear();
        nrTema.clear();
        numeField.clear();
        valoare.clear();
        obsField.clear();
    }
    @FXML
    public void handleRefresh(){
        model.clear();
        model.setAll(FXCollections.observableArrayList(service.getAllN()));
        notaTable.setItems(model);
        pagination.setPageFactory(this::createPage);
        clearFields();
        idStudent.setDisable(false);
        nrTema.setDisable(false);
    }
    @FXML
    public void handleSaveNota(){
        try {
            service.addNota(Integer.parseInt(idStudent.getText()), Integer.parseInt(nrTema.getText()), Double.parseDouble(valoare.getText()), service.sapt_de_facultate_curenta(),obsField.getText());
            pagination.setPageFactory(this::createPage);
            //System.out.println(service.sapt_de_facultate_curenta());
            clearFields();
            MessageAlert.showInfoMessage(null, "Grade added successfully!");
        } catch (ValidationException e) {
            MessageAlert.showErrorMessage(null,e.getMessage());
        } catch (RepositoryException e) {
            MessageAlert.showErrorMessage(null,e.getMessage());
        } catch (NumberFormatException e) {
            MessageAlert.showErrorMessage(null, "Invalid fields!");
        }
    }

    @FXML
    public void handleUpdateNota(){
        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("CONFIRMATION");
            alert.setContentText("Are you sure you want to update this item?");
            ButtonType okButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
            ButtonType noButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(okButton, noButton);
            Optional<ButtonType> result = alert.showAndWait();
            if ((result.isPresent()) && (result.get() == okButton)) {
                service.updateNota(Integer.parseInt(idStudent.getText()), Integer.parseInt(nrTema.getText()), Double.parseDouble(valoare.getText()), service.sapt_de_facultate_curenta(), obsField.getText());
                pagination.setPageFactory(this::createPage);
                MessageAlert.showInfoMessage(null, "Grade updated successfully!");
                clearFields();
                idStudent.setDisable(false);
                nrTema.setDisable(false);
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
    public void handleFilterValue(){
        try{

            List list = service.filterNotaSpecificValue(Double.parseDouble(filterValueField.getText()), "Cresc");
            //notaTable.setItems(FXCollections.observableArrayList(list));
            model.clear();
            model.setAll(FXCollections.observableArrayList(list));
            notaTable.setItems(model);
            pagination.setPageFactory(this::createPage);
            filterValueField.clear();
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
    public void handleFilterValues(){
        try{

            List list = service.filterNotaTwoValues(Double.parseDouble(filterminvalueField.getText()),Double.parseDouble(filtermaxvalueField.getText()), "Cresc");
            //notaTable.setItems(FXCollections.observableArrayList(list));
            model.clear();
            model.setAll(FXCollections.observableArrayList(list));
            notaTable.setItems(model);
            pagination.setPageFactory(this::createPage);
            filterminvalueField.clear();
            filtermaxvalueField.clear();
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
    public void handleFilterId(){
        try{

            List list = service.filterNotaIDStudent(Integer.parseInt(filteridField.getText()), "Cresc");
            //notaTable.setItems(FXCollections.observableArrayList(list));
            model.clear();
            model.setAll(FXCollections.observableArrayList(list));
            notaTable.setItems(model);
            pagination.setPageFactory(this::createPage);
            filteridField.clear();
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
