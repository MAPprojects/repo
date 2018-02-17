package controller;

import domain.Student;
import domain.StudentWithCheckBox;
import domain.StudentWithCheckBoxImageView;
import domain.User;
import exceptii.EntityNotFoundException;
import exceptii.ValidationException;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.converter.IntegerStringConverter;
import service.Service;
import utils.ListEvent;
import view_FXML.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ControllerStudent implements sablonObserver.Observer{
    private Service service;
    private ObservableList modelStudenti=FXCollections.observableArrayList();
    private ObservableList<StudentWithCheckBoxImageView> data=FXCollections.observableArrayList();
    private User currentUser;

    /****************************STUDENTI**********************************************************/
    @FXML
    private ComboBox comboBoxFiltrariStudenti;
    @FXML
    private TextField textFieldFiltrariStudenti;

    @FXML
    private TableView tableViewStudenti;
    @FXML
    private TableColumn columnID;
    @FXML
    private TableColumn columnNume;
    @FXML
    private TableColumn columnGrupa;
    @FXML
    private TableColumn columnEmail;
    @FXML
    private TableColumn columnProfesor;
    @FXML
    private TableColumn tableColumnSelect;
    @FXML
    private Pagination paginationStudenti;
    @FXML
    private TableColumn tableColumnLog;
    @FXML
    private ComboBox comboBoxItemsPerPage;
    private Integer itemsPerPage;

    @FXML
    private ImageView imageAdd;
    @FXML
    private ImageView imageUpdate;
    @FXML
    private ImageView imageRemove;

    @FXML
    private ImageView image;

    @FXML
    public void handleImageEntered(MouseEvent event){
        image.setEffect(DropShadowForImage.dropShadow(20d,20d,1d,2d,3d));
    }

    @FXML
    public void handleImageExit(MouseEvent event){
        image.setEffect(null);
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    @FXML
    public void handleImageEnterAdd(MouseEvent event){
        imageAdd.setEffect(DropShadowForImage.dropShadow(20d,20d,1d,2d,3d));
    }
    @FXML
    public void handleImageExitAdd(MouseEvent ev){
        imageAdd.setEffect(null);
    }
    @FXML
    public void handleImageEnterUpdate(MouseEvent event){
        imageUpdate.setEffect(DropShadowForImage.dropShadow(20d,20d,1d,2d,3d));
    }
    @FXML
    public void handleImageExitUpdate(MouseEvent ev){
        imageUpdate.setEffect(null);
    }
    @FXML
    public void handleImageEnterRemove(MouseEvent event){
        imageRemove.setEffect(DropShadowForImage.dropShadow(20d,20d,1d,2d,3d));
    }
    @FXML
    public void handleImageExitRemove(MouseEvent ev){
        imageRemove.setEffect(null);
    }

    public void setItemsPerPage(Integer itemsPerPage) {
        this.itemsPerPage = itemsPerPage;
    }

    private void setareData(List<Student> studentList){
        List<StudentWithCheckBoxImageView> listNew=new ArrayList<>();
        data.removeAll();
        studentList.forEach(student -> {
            listNew.add(new StudentWithCheckBoxImageView(student.getId(),student.getNume(),student.getGrupa(),student.getEmail(),student.getCadru_didactic_indrumator_de_laborator(),service));
        });
        data.setAll(listNew);
    }

    public void setService(Service service) {
        this.service = service;
        setareData(service.getStudentiForProfesor(currentUser.getNume()));
        updateTable();
    }

    /**
     * Constructor
     */
    public ControllerStudent() {}

    @FXML
    /**
     * Handles for the saveEvent
     * @param event ActionEvent
     */
    public void handleAddStudent(MouseEvent event) {
        showEditingStudentWindow(null);
    }

    public void showEditingStudentWindow(Student student){
        try {

            FXMLLoader loader=new FXMLLoader();
            loader.setLocation(getClass().getResource("/view_FXML/editingStudent.fxml"));
            AnchorPane root=(AnchorPane)loader.load();

            Stage dialogStage=new Stage();
            dialogStage.setTitle("Editati studentul");
            dialogStage.initModality(Modality.WINDOW_MODAL);

            Scene scene=new Scene(root);
            dialogStage.setScene(scene);

            EditStudentController editStudent=loader.getController();
            editStudent.setService(service,dialogStage,student);
            editStudent.setScene(scene);

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void handleEditColumnNume(TableColumn.CellEditEvent<Student,String> event){
        handleUpdateStudentCellNume((String)event.getNewValue());
    }

    private void  handleUpdateStudentCellNume(String newValue) {
        StudentWithCheckBox studentWithCheckBox=(StudentWithCheckBox) tableViewStudenti.getSelectionModel().getSelectedItem();
        if (studentWithCheckBox!=null){
            Student student=new Student(studentWithCheckBox.getId(),newValue,studentWithCheckBox.getGrupa(),studentWithCheckBox.getEmail(),studentWithCheckBox.getCadru_didactic_indrumator_de_laborator());
            updateStudent(student);
        }
        else{
            AlertMessage message=new AlertMessage();
            message.showMessage(null, Alert.AlertType.WARNING,"Warning","Nu ati selectat nimic!");
        }
    }

    private void updateStudent(Student student) {
        try {
            service.updateStudent(student.getId(),student.getNume(),student.getEmail(),student.getCadru_didactic_indrumator_de_laborator(),student.getGrupa());
            AlertMessage message=new AlertMessage();
            message.showMessage(null, Alert.AlertType.CONFIRMATION,"Modificare efectuata","Studentul a fost modificat cu succes");
            update();
        } catch (ValidationException e) {
            AlertMessage message=new AlertMessage();
            message.showMessage(null, Alert.AlertType.ERROR,"Eroare",e.toString());
            update();
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void handleEditColumnGrupa(TableColumn.CellEditEvent<Student,Integer> event) {
        handleUpdateStudentCellGrupa((Integer)event.getNewValue());
    }

    private void handleUpdateStudentCellGrupa(Integer newValue) {
        StudentWithCheckBox studentWithCheckBox=(StudentWithCheckBox) tableViewStudenti.getSelectionModel().getSelectedItem();
        if (studentWithCheckBox!=null){
            Student student=new Student(studentWithCheckBox.getId(),studentWithCheckBox.getNume(),newValue,studentWithCheckBox.getEmail(),studentWithCheckBox.getCadru_didactic_indrumator_de_laborator());
            updateStudent(student);
        }
        else{
            AlertMessage message=new AlertMessage();
            message.showMessage(null, Alert.AlertType.WARNING,"Warning","Nu ati selectat nimic!");
        }
    }

    public void handleEditColumnEmail(TableColumn.CellEditEvent<Student,String> event){
        handleUpdateStudentCellEmail((String)event.getNewValue());
    }

    private void handleUpdateStudentCellEmail(String newValue) {
        StudentWithCheckBox studentWithCheckBox=(StudentWithCheckBox) tableViewStudenti.getSelectionModel().getSelectedItem();
        if (studentWithCheckBox!=null){
            Student student=new Student(studentWithCheckBox.getId(),studentWithCheckBox.getNume(),studentWithCheckBox.getGrupa(),newValue,studentWithCheckBox.getCadru_didactic_indrumator_de_laborator());
            updateStudent(student);
        }
        else{
            AlertMessage message=new AlertMessage();
            message.showMessage(null, Alert.AlertType.WARNING,"Warning","Nu ati selectat nimic!");
        }
    }

    public void handleEditColumnProfesor(TableColumn.CellEditEvent<Student,String > event){
        handleUpdateStudentCellProfesor((String)event.getNewValue());
    }

    private void handleUpdateStudentCellProfesor(String newValue) {
        StudentWithCheckBox studentWithCheckBox=(StudentWithCheckBox) tableViewStudenti.getSelectionModel().getSelectedItem();
        if (studentWithCheckBox!=null){
            Student student=new Student(studentWithCheckBox.getId(),studentWithCheckBox.getNume(),studentWithCheckBox.getGrupa(),studentWithCheckBox.getEmail(),newValue);
            updateStudent(student);
        }
        else{
            AlertMessage message=new AlertMessage();
            message.showMessage(null, Alert.AlertType.WARNING,"Warning","Nu ati selectat nimic!");
        }
    }

    public void editableTable(){

        tableViewStudenti.setEditable(true);

        Callback<TableColumn,TableCell> cellFactoryGrupa=
                new Callback<TableColumn,TableCell>() {
                    @Override
                    public TableCell call(TableColumn p){
                        return new EditingCellStudentInteger();
                    }

                };
        columnGrupa.setCellFactory(cellFactoryGrupa);

        Callback<TableColumn,TableCell> cellFactoryString=
                new Callback<TableColumn,TableCell>() {
                    @Override
                    public TableCell call(TableColumn p){
                        return new EditingCellString();
                    }

                };
        columnNume.setCellFactory(cellFactoryString);
        columnProfesor.setCellFactory(cellFactoryString);
        columnEmail.setCellFactory(cellFactoryString);
    }

    @FXML
    /**
     * Handles the updateEvent for student
     * @param event ActionEvent
     */
    public void handleUpdateStudent(MouseEvent event){
        StudentWithCheckBox student=(StudentWithCheckBox) tableViewStudenti.getSelectionModel().getSelectedItem();

        if (student!=null) {
            Student st = new Student(student.getId(), student.getNume(), student.getGrupa(), student.getEmail(), student.getCadru_didactic_indrumator_de_laborator());
            showEditingStudentWindow(student);
        }
        else{
            AlertMessage message=new AlertMessage();
            message.showMessage(null, Alert.AlertType.WARNING,"Warning","Nu ati selectat nimic!");
        }

    }

    @FXML
    /**
     * Handles the deleteEvent for student
     * @param event ActionEvent
     */
    public void handleDeleteStudent(MouseEvent event){
        List<Student> studentListDeSters=new ArrayList<>();
        for (int i=0;i<modelStudenti.size();i++){
            StudentWithCheckBox studentWithCheckBox=(StudentWithCheckBox) modelStudenti.get(i);
            if (studentWithCheckBox.getSelectedCheckBox().isSelected()){
                studentListDeSters.add(new Student(studentWithCheckBox.getId(),studentWithCheckBox.getNume(),studentWithCheckBox.getGrupa(),studentWithCheckBox.getEmail(),studentWithCheckBox.getCadru_didactic_indrumator_de_laborator()));
            }
        }

        String studentiCareNuAuPututFiStersi="";

        for (Student student:studentListDeSters){
            try {
                Optional<Student> aux=service.deleteStudent(student.getId());
                if (!aux.isPresent()){
                    studentiCareNuAuPututFiStersi=studentiCareNuAuPututFiStersi.concat(student.getNume());
                    studentiCareNuAuPututFiStersi=studentiCareNuAuPututFiStersi.concat(", ");
                }

            } catch (EntityNotFoundException e) {
                e.printStackTrace();
            }
        }
        if (!studentiCareNuAuPututFiStersi.isEmpty()){
            AlertMessage message=new AlertMessage();
            message.showMessage(null, Alert.AlertType.WARNING,"Atentie!!!","Studentii "+studentiCareNuAuPututFiStersi+"nu pot fi stersi, au note asignate");

        }
        else
        {
            AlertMessage message=new AlertMessage();
            message.showMessage(null, Alert.AlertType.INFORMATION,"Info","Studentii au fost stersi cu succes!");

        }

    }

    @FXML
    public void initialize(){
        initializeStudenti();

        setItemsPerPage(10);
        comboBoxItemsPerPage.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                setItemsPerPage((Integer)comboBoxItemsPerPage.getValue());
                updatePage();
            }
        });

        setComboBoxItemsPerPageList();

        paginationStudenti.setPageFactory(new Callback<Integer, Node>() {
            @Override
            public Node call(Integer param) {
                if (param<=data.size()/getItemsPerPage())return createPage(param);
                else return null;
            }
        });
    }

    private void setComboBoxItemsPerPageList() {
        ObservableList<Integer> list=FXCollections.observableArrayList();
        list.addAll(5,10,20,50);
        comboBoxItemsPerPage.getItems().setAll(list);
    }

    private void updatePage() {
        Integer index=paginationStudenti.getCurrentPageIndex();

        if (data.size()==0){
            paginationStudenti.setPageCount(1);
        }
        if (data.size() % getItemsPerPage() != 0) {
            paginationStudenti.setMaxPageIndicatorCount((int) ((data.size() / getItemsPerPage()) + 1));
            paginationStudenti.setPageCount((int) ((data.size() / getItemsPerPage()) + 1));
        } else {
            paginationStudenti.setMaxPageIndicatorCount((int) ((data.size() / getItemsPerPage())));
            paginationStudenti.setPageCount((int) ((data.size() / getItemsPerPage())));
        }


        if (index<(int)data.size()/getItemsPerPage()+1) {
//            createPage(index);

            paginationStudenti.getPageFactory().call(index);
            paginationStudenti.setPageFactory((Integer page)->{return createPage(page);});

        }
        else {
//            createPage(0);
            paginationStudenti.getPageFactory().call(0);
            paginationStudenti.setPageFactory((Integer page)->{return createPage(page);});
        }
    }

    public Integer getItemsPerPage(){
        return itemsPerPage;
    }

    private VBox createPage(Integer pageIndex){
        VBox vBox=new VBox();
        vBox.getChildren().add(tableViewStudenti);

        Integer indexFrom=0;
        if (data.size()>=pageIndex*getItemsPerPage())
            indexFrom=pageIndex*getItemsPerPage();
        Integer indexTo=Math.min(indexFrom+getItemsPerPage(),data.size());
        modelStudenti.setAll(data.subList(indexFrom,indexTo));
        tableViewStudenti.setItems(modelStudenti);

        return vBox;
    }

    private void updateTable(){
        if (data.size()%getItemsPerPage()!=0){
            paginationStudenti.setMaxPageIndicatorCount((int) ((data.size() / getItemsPerPage()) + 1));
            paginationStudenti.setPageCount((int) ((data.size() / getItemsPerPage()) + 1));
        }
        else{
            paginationStudenti.setMaxPageIndicatorCount((int) ((data.size() / getItemsPerPage())));
            paginationStudenti.setPageCount((int) ((data.size() / getItemsPerPage()) ));
        }
        for (int i=0;i<(int)(data.size()/getItemsPerPage())+1;i++){
            Integer indexFrom=i*getItemsPerPage();
            Integer indexTo=Math.min(indexFrom+getItemsPerPage(),data.size());
            modelStudenti.setAll(data.subList(indexFrom, indexTo));
            tableViewStudenti.setItems(FXCollections.observableList(modelStudenti));
        }
    }

    private void initializeStudenti() {
        columnID.setCellValueFactory(new PropertyValueFactory<StudentWithCheckBoxImageView,String>("idStudent"));
        columnNume.setCellValueFactory(new PropertyValueFactory<StudentWithCheckBoxImageView,String>("nume"));
        columnGrupa.setCellValueFactory(new PropertyValueFactory<StudentWithCheckBoxImageView,Integer>("grupa"));
        columnEmail.setCellValueFactory(new PropertyValueFactory<StudentWithCheckBoxImageView,String>("email"));
        columnProfesor.setCellValueFactory(new PropertyValueFactory<StudentWithCheckBoxImageView,String>("cadru_didactic_indrumator_de_laborator"));
        tableColumnSelect.setCellValueFactory(new PropertyValueFactory<StudentWithCheckBoxImageView,CheckBox>("selectedCheckBox"));
        tableColumnLog.setCellValueFactory(new PropertyValueFactory<StudentWithCheckBoxImageView,Button>("button"));


        editableTable();

        ObservableList<String> optionsFiltrari= FXCollections.observableArrayList(
                "Filtrare dupa grupa",
                "Filtrare dupa email",
                "Filtrare dupa nume"
        );
        comboBoxFiltrariStudenti.setItems(optionsFiltrari);

        textFieldFiltrariStudenti.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                handleComboBoxFiltrariStudenti(new ActionEvent());
            }
        });

        comboBoxFiltrariStudenti.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                handleComboBoxFiltrariStudenti(new ActionEvent());
            }
        });
    }

    public void handleComboBoxFiltrariStudenti(ActionEvent event){
        String option=(String) comboBoxFiltrariStudenti.getSelectionModel().getSelectedItem();
        if (option!=null) {
            switch (option) {
                case "Filtrare dupa grupa": {
                    filtrareStudentiDupaGrupa();
                    break;
                }
                case "Filtrare dupa email": {
                    filtrareStudentiDupaEmail();
                    break;
                }
                case "Filtrare dupa nume": {
                    filtrareStudentiDupaNume();
                    break;
                }
            }
        }
    }

    private void filtrareStudentiDupaNume() {
        if (!textFieldFiltrariStudenti.getText().isEmpty()){
            String nume=textFieldFiltrariStudenti.getText();
            List<Student> studentListToata=service.filteredStudentsByName(nume);
            List<Student> studentList=filtrareStudenti(studentListToata);
            setareData(studentList);
            updatePage();
        }
        else {
            update();
        }
    }

    private void filtrareStudentiDupaEmail() {
        if (!textFieldFiltrariStudenti.getText().isEmpty()){
            String email=textFieldFiltrariStudenti.getText();
            List<Student> studentListToata=service.filteredStudentsByEmail(email);
            List<Student> studentList=filtrareStudenti(studentListToata);
            setareData(studentList);
            updatePage();
        }
        else {
            update();
        }
    }

    private void filtrareStudentiDupaGrupa() {
        try {
            if (!textFieldFiltrariStudenti.getText().isEmpty()) {
                Integer grupa = Integer.parseInt(textFieldFiltrariStudenti.getText());
                if (grupa > 0) {
                    List<Student> studentListToata = service.filteredStudentsByGroup(grupa);
                    List<Student> studentList=filtrareStudenti(studentListToata);
                    setareData(studentList);
                    updatePage();
                } else {
                    AlertMessage message = new AlertMessage();
                    message.showMessage(null, Alert.AlertType.WARNING, "Warning", "Grupa trebuie sa fie un numar strict pozitiv");
                    setareData(service.getStudentiForProfesor(currentUser.getNume()));
                    updatePage();
                }
            }
            else {
                setareData(service.getStudentiForProfesor(currentUser.getNume()));
                updatePage();
            }
        }
        catch (NumberFormatException e){
            AlertMessage message=new AlertMessage();
            message.showMessage(null, Alert.AlertType.WARNING,"Warning","Grupa trebuie sa fie un numar strict pozitiv");
            setareData(service.getStudentiForProfesor(currentUser.getNume()));
            updatePage();
        }
    }

    private List<Student> filtrareStudenti(List<Student> studentListToata) {
        List<Student> list=service.getStudentiForProfesor(currentUser.getNume());
        List<Student> rezultat=new ArrayList<>();
        for (Student st:studentListToata){
            if (list.contains(st)){
                rezultat.add(st);
            }
        }
        return rezultat;
    }

    @Override
    public void update() {
        setareData(service.getStudentiForProfesor(currentUser.getNume()));
        updatePage();
    }

    @FXML
    public void handleSendMail(MouseEvent event){
        StudentWithCheckBoxImageView student=(StudentWithCheckBoxImageView) tableViewStudenti.getSelectionModel().getSelectedItem();
        if (student==null){
            AlertMessage message=new AlertMessage();
            message.showMessage(null, Alert.AlertType.WARNING,"Atentie","Var rugam selectati un student din tabel");
        }
        else {
            FXMLLoader loader = new FXMLLoader();
            try {
                loader.setLocation(getClass().getResource("/view_FXML/mail.fxml"));
                AnchorPane anchorPane = loader.load();
                ControllerMail controllerMail = loader.getController();
                Stage stage = new Stage();
                Scene scene = new Scene(anchorPane);
                stage.setTitle("Send EMAIL");
                stage.setScene(scene);

                controllerMail.setStage(stage);
                controllerMail.setIdStudent(student.getId());
                controllerMail.setEmailTo(student.getEmail());

                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
