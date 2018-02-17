package controller;

import domain.Nota;
import domain.NotaStudent;
import domain.Student;
import domain.User;
import exceptii.EntityNotFoundException;
import exceptii.ValidationException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.controlsfx.control.CheckComboBox;
import sablonObserver.Observer;
import service.Service;
import utils.ListEvent;
import view_FXML.AlertMessage;
import view_FXML.DropShadowForImage;
import view_FXML.EditingCellFloat;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ControllerNote implements Observer {
    protected Service service;
    protected User currentUser;

    @FXML
    protected Label labelIdStudent;
    @FXML
    protected TableView tableViewNote;
    @FXML
    protected TableColumn columnIdStudent;
    @FXML
    protected TableColumn tableColumnNumeStudent;
    @FXML
    protected TableColumn tableColumnDeadline;
    @FXML
    protected TableColumn tableColumnCerinta;
    @FXML
    protected TableColumn columnNrTema;
    @FXML
    protected TableColumn columnValoare;
    @FXML
    protected ComboBox comboBoxFiltrariNote;
    @FXML
    protected TextField textFieldFiltrariNote;
    @FXML
    protected CheckComboBox checkComboBoxIdStudent;
    @FXML
    protected CheckComboBox checkComboBoxNrTema;
    @FXML
    protected Pagination paginationNote;
    @FXML
    protected ComboBox comboBoxItemsPerPage;

    public void setItemsPerPage(Integer itemsPerPage) {
        this.itemsPerPage = itemsPerPage;
    }

    protected Integer itemsPerPage;

    @FXML
    protected ImageView imageAdd;
    @FXML
    protected ImageView imageUpdate;

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

    public ControllerNote() { }

    private ObservableList<NotaStudent> model= FXCollections.observableArrayList();
    private ObservableList<NotaStudent> data=FXCollections.observableArrayList();

    private void setComboBoxItemsPerPageList(){
        ObservableList<Integer> list=FXCollections.observableArrayList();
        list.addAll(5,10,20,50);
        comboBoxItemsPerPage.getItems().setAll(list);
    }


    private void setareData(List<Nota> listNota){
        List<NotaStudent> lista=new ArrayList<>();
        this.data.removeAll();
        for (Nota nota : listNota) {
            try {
                NotaStudent notaStudent=new NotaStudent(nota.getIdNota(),nota.getIdStudent(),nota.getNrTema(),nota.getValoare(),service.getStudent(nota.getIdStudent()).get().getNume(),service.getTemaLab(nota.getNrTema()).get().getCerinta(),service.getTemaLab(nota.getNrTema()).get().getDeadline());
                lista.add(notaStudent);
            } catch (EntityNotFoundException e) {
                e.printStackTrace();
            }
        }
        data.setAll(lista);
    }

    private void updateTable(){
        if (data.size()%getItemsPerPage()!=0){
            paginationNote.setMaxPageIndicatorCount((int) ((data.size() / getItemsPerPage()) + 1));
            paginationNote.setPageCount((int) ((data.size() / getItemsPerPage()) + 1));
        }
        else{
            paginationNote.setMaxPageIndicatorCount((int) ((data.size() / getItemsPerPage())));
            paginationNote.setPageCount((int) ((data.size() / getItemsPerPage()) ));
        }
        for (int i=0;i<(int)(data.size()/getItemsPerPage())+1;i++){
            Integer indexFrom=i*getItemsPerPage();
            Integer indexTo=Math.min(indexFrom+getItemsPerPage(),data.size());
            model.setAll(data.subList(indexFrom, indexTo));
            tableViewNote.setItems(FXCollections.observableList(model));
        }
    }

    public void setService(Service service) {
        this.service = service;
        setareData(setDataInit(service));
        setComboBoxCheckIdStudent();
        setComboBoxCheckNrTema();
        updateTable();

    }

    protected List<Nota> setDataInit(Service service) {
        return service.getNotePentruStudentiiUnuiProfesor(currentUser.getNume());
    }

    private void setComboBoxCheckNrTema() {
        ObservableList<String> list=FXCollections.observableArrayList();
        service.getListTemeLaborator().forEach(temaLaborator -> list.add(temaLaborator.getId().toString()));
        list.add("toate temele");
        checkComboBoxNrTema.getItems().setAll(list);
    }

    private void setComboBoxCheckIdStudent() {
        ObservableList<String> list=FXCollections.observableArrayList();
        service.getStudentiForProfesor(currentUser.getNume()).forEach(student -> {
            list.add(student.getId().toString());
        });
        list.add("toti studentii");
        checkComboBoxIdStudent.getItems().setAll(list);
    }

    @FXML
    public void initialize(){
        tableViewNote.setEditable(true);
        columnIdStudent.setCellValueFactory(new PropertyValueFactory<NotaStudent,Integer>("idStudent"));
        columnNrTema.setCellValueFactory(new PropertyValueFactory<NotaStudent,Integer>("nrTema"));
        columnValoare.setCellValueFactory(new PropertyValueFactory<NotaStudent,Float>("valoare"));
        tableColumnNumeStudent.setCellValueFactory(new PropertyValueFactory<NotaStudent,String>("numeStudent"));
        tableColumnCerinta.setCellValueFactory(new PropertyValueFactory<NotaStudent,String>("cerinta"));
        tableColumnDeadline.setCellValueFactory(new PropertyValueFactory<NotaStudent,Integer>("deadline"));
        tableViewNote.setItems(model);

        comboBoxItemsPerPage.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                setItemsPerPage((Integer) comboBoxItemsPerPage.getValue());
                updatePage();
            }
        });

        setComboBoxItemsPerPageList();
        setItemsPerPage(10);


        Callback<TableColumn,TableCell> cellFactory=
                new Callback<TableColumn,TableCell>() {
                    @Override
                    public TableCell call(TableColumn p){
                        return new EditingCellFloat();
                    }

                };
        columnValoare.setCellFactory(cellFactory);

        setComboBoxFiltrariList();

        comboBoxFiltrariNote.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                filtrariNote();
            }
        });
        
        textFieldFiltrariNote.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                filtrariNote();
            }
        });

        checkComboBoxIdStudent.getCheckModel().getCheckedItems().addListener(new ListChangeListener<Integer>() {
            public void onChanged(ListChangeListener.Change<? extends Integer> c) {
                filtrariNote();
            }});

        checkComboBoxNrTema.getCheckModel().getCheckedItems().addListener(new ListChangeListener<Integer>() {
            public void onChanged(ListChangeListener.Change<? extends Integer> c) {
                filtrariNote();
            }});

        paginationNote.setPageFactory(new Callback<Integer, Node>() {
            @Override
            public Node call(Integer param) {
                if (param<=data.size()/getItemsPerPage())return createPage(param);
                else return null;
            }
        });

    }

    protected void setComboBoxFiltrariList() {
        ObservableList<String> listFiltrari= FXCollections.observableArrayList();
        listFiltrari.addAll("dupa Valoare","dupa Id Student","dupa Numar tema");
        comboBoxFiltrariNote.setItems(listFiltrari);
    }

    private VBox createPage(Integer currentPage) {
        VBox vBox=new VBox();
        vBox.getChildren().add(tableViewNote);

        Integer indexFrom=0;
        if (data.size()>=currentPage*getItemsPerPage()){
            indexFrom=currentPage*getItemsPerPage();
        }
        Integer indexTo=Math.min(indexFrom+getItemsPerPage(),data.size());
        model.setAll(data.subList(indexFrom,indexTo));
        tableViewNote.setItems(model);

        return vBox;
    }

    private void updatePage() {
        Integer index=paginationNote.getCurrentPageIndex();
        if (data.size()==0){
            paginationNote.setPageCount(1);
        }
        if (data.size() % getItemsPerPage() != 0) {
            paginationNote.setMaxPageIndicatorCount((int) ((data.size() / getItemsPerPage()) + 1));
            paginationNote.setPageCount((int) ((data.size() / getItemsPerPage()) + 1));
        } else {
            paginationNote.setMaxPageIndicatorCount((int) ((data.size() / getItemsPerPage())));
            paginationNote.setPageCount((int) ((data.size() / getItemsPerPage())));
        }


        if (index<(int)data.size()/getItemsPerPage()+1) {
            paginationNote.getPageFactory().call(index);
            paginationNote.setPageFactory((Integer page)->{return createPage(page);});
        }
        else {
            paginationNote.getPageFactory().call(0);
            paginationNote.setPageFactory((Integer page)->{return createPage(page);});
        }
    }

    private void filtrariNote() {
        String filtrareTip=(String) comboBoxFiltrariNote.getValue();
        if (filtrareTip!=null){
            switch (filtrareTip){
                case "dupa Valoare":{
                    filtrareDupaValoare();
                    break;
                }
                case "dupa Id Student":{
                    filtrareDupaIdStudent();
                    break;
                }
                case "dupa Numar tema":{
                    filtrareDupaNrTema();
                    break;
                }
            }
        }
    }

    private void filtrareDupaNrTema() {
        if (checkComboBoxNrTema.getCheckModel().isEmpty()){
            updateTableAndPage();
        }
        else {
            List<Nota> fileredList = new ArrayList<>();
            if (!checkComboBoxNrTema.getCheckModel().getCheckedItems().contains("toate temele")) {
                checkComboBoxNrTema.getCheckModel().getCheckedItems().forEach(nrTema -> {
                    String nr=(String) nrTema;
                    fileredList.addAll(fileredList.size(), service.filteredNoteByNrTema(Integer.parseInt(nr)));
                });
                List<Nota> list=filtrareNote(fileredList);
                setareData(list);
                updatePage();
            }
            else {
                updateTableAndPage();
            }
        }
    }

    private void filtrareDupaIdStudent() {

        if (checkComboBoxIdStudent.getCheckModel().isEmpty()){
            updateTableAndPage();
        }
        else {
            List<Nota> fileredList = new ArrayList<>();
            if (!checkComboBoxIdStudent.getCheckModel().getCheckedItems().contains("toti studentii")) {
                checkComboBoxIdStudent.getCheckModel().getCheckedItems().forEach(idStudent -> {
                    String id=(String )idStudent;
                    fileredList.addAll(fileredList.size(), service.filteredNotebyIdStudent(Integer.parseInt(id)));
                });
                List<Nota> list=filtrareNote(fileredList);
                setareData(list);
                updatePage();
            }
            else updateTableAndPage();
        }
    }

    private void filtrareDupaValoare() {
        if (!textFieldFiltrariNote.getText().isEmpty()) {
            try {
                Float valoare = Float.parseFloat(textFieldFiltrariNote.getText());
                if (valoare <= 0) throw new NumberFormatException();
                List<Nota> list=filtrareNote(service.filteredNoteByValoare(valoare));
                setareData(list);
                updatePage();
            } catch (NumberFormatException e) {
                AlertMessage message = new AlertMessage();
                message.showMessage(null, Alert.AlertType.ERROR, "Eroare", "Valoarea notei trebuie sa fie un numar real strict pozitiv!");
            }
        }
        else updateTableAndPage();
    }

    @Override
    public void update() {

        setComboBoxCheckIdStudent();
        setComboBoxCheckNrTema();
        setareData(setDataInit(service));
        updatePage();

    }

    public void updateTableAndPage(){
        setareData(setDataInit(service));
        updatePage();
    }

    @FXML
    public void handleAdd(MouseEvent event){
        showEditingGrade(null);
    }

    private void showEditingGrade(Nota nota) {
        try{
            FXMLLoader loader=new FXMLLoader();
            loader.setLocation(getClass().getResource("/view_FXML/editingGrade.fxml"));
            AnchorPane root=loader.load();
            Stage stage=new Stage();
            stage.setTitle("Editati nota");
            stage.initModality(Modality.WINDOW_MODAL);
            Scene scene=new Scene(root);
            stage.setScene(scene);

            EditNotaController editNotaController=loader.getController();
            editNotaController.setCurrentUser(currentUser);
            editNotaController.setService(service,stage,nota);
            editNotaController.setScene(scene);


            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleUpdate(MouseEvent event){
        NotaStudent notaSt=(NotaStudent) tableViewNote.getSelectionModel().getSelectedItem();
        if (notaSt!=null){
            Nota nota=new Nota(notaSt.getIdNota(),notaSt.getIdStudent(),notaSt.getNrTema(),notaSt.getValoare());
            showEditingGrade(nota);
        }
        else {
            AlertMessage message=new AlertMessage();
            message.showMessage(null,Alert.AlertType.WARNING,"Atentie!","Nu ati selectat nimic din tabelul de Note!");
        }
    }

    @FXML
    public void handleEditCell(TableColumn.CellEditEvent<?,?> event){
        handleUpdateCellAndNota((Float)event.getNewValue());
    }

    private void handleUpdateCellAndNota(Float newValue) {
        NotaStudent notaStudent=(NotaStudent) tableViewNote.getSelectionModel().getSelectedItem();
        if (notaStudent!=null){
            Nota nota=new Nota(notaStudent.getIdNota(),notaStudent.getIdStudent(),notaStudent.getNrTema(),newValue);
            try {
                Nota aux=service.modificareNota(nota);
                if (aux==null){
                    AlertMessage message=new AlertMessage();
                    message.showMessage(null, Alert.AlertType.INFORMATION,"Modificare efectuata","Nota a fost modificata cu succes!");
                    updateTableAndPage();
                }
                else {
                    AlertMessage message=new AlertMessage();
                    message.showMessage(null, Alert.AlertType.INFORMATION,"Modificare neefectuata","Nota nu a putut fi modificata. " +
                            "Nota recalculata este mai mica decat cea initiala.");
                    updateTableAndPage();
                }
            } catch (ValidationException e) {
                AlertMessage message=new AlertMessage();
                message.showMessage(null, Alert.AlertType.ERROR,"Eroare",e.toString());
            } catch (EntityNotFoundException e) {
                e.printStackTrace();
            }
        }
        else{
            AlertMessage message=new AlertMessage();
            message.showMessage(null,Alert.AlertType.WARNING,"Atentie!","Nu ati selectat nimic din tabelul de Note!");
        }


    }

    public Integer getItemsPerPage() {
        return itemsPerPage;
    }

    private List<Nota> filtrareNote(List<Nota> studentListToata) {
        List<Nota> list=setDataInit(service);
        List<Nota> rezultat=new ArrayList<>();
        for (Nota nota:studentListToata){
            if (list.contains(nota)){
                rezultat.add(nota);
            }
        }
        return rezultat;
    }
}
