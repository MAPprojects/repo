package controller;

import domain.TemaLaborator;
import exceptii.EntityNotFoundException;
import exceptii.ValidationException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
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
import sablonObserver.Observer;
import service.Service;
import view_FXML.AlertMessage;
import view_FXML.DropShadowForImage;
import view_FXML.EditingCellInteger;

import java.util.ArrayList;
import java.util.List;
import java.io.IOException;


public class ControllerTemeLaborator implements Observer{
    protected Service service;
    protected ObservableList<TemaLaborator> model= FXCollections.observableArrayList();
    protected ObservableList<TemaLaborator> data=FXCollections.observableArrayList();

    @FXML
    protected TableView tableViewTeme;
    @FXML
    protected TableColumn columnNrTema;
    @FXML
    protected TableColumn columnCerinta;
    @FXML
    protected TableColumn columnDeadline;
    @FXML
    protected ComboBox comboBoxFiltrariTemeLaborator;
    @FXML
    protected TextField textFieldFiltrariTeme;
    @FXML
    protected Pagination paginationTeme;
    @FXML
    protected ComboBox comboBoxItemsPerPage;

    protected Integer itemsPerPage;

    @FXML
    protected ImageView imageAdd;
    @FXML
    protected ImageView imageUpdate;

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

    public void setItemsPerPage(Integer itemsPerPage) {
        this.itemsPerPage = itemsPerPage;
    }

    public ControllerTemeLaborator() { }

    @FXML
    public void handleAdd(MouseEvent event){
        showEditingTemaView(null);
    }

    private void showEditingTemaView(TemaLaborator temaLaborator) {
        FXMLLoader loader=new FXMLLoader();
        loader.setLocation(getClass().getResource("/view_FXML/editingTema.fxml"));
        try {
            AnchorPane anchorPane=(AnchorPane) loader.load();

            Stage dialogStage=new Stage();
            dialogStage.setTitle("Editati tema de laborator");
            dialogStage.initModality(Modality.WINDOW_MODAL);

            Scene scene=new Scene(anchorPane);
            dialogStage.setScene(scene);

            EditTemaLaboratorController editTemaLaboratorController=loader.getController();
            editTemaLaboratorController.setService(service,temaLaborator,dialogStage);
            editTemaLaboratorController.setScene(scene);

            dialogStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean verificareDacaSePoateModifica(TemaLaborator temaLaborator) {
        if ((service.getSaptamana_curenta()>temaLaborator.getDeadline())) {
            AlertMessage message = new AlertMessage();
            message.showMessage(null, Alert.AlertType.WARNING, "Atentie!!!", "Deadlineul temei pe care doriti sa o modificati este depasit. " +
                    "Nu puteti efectua modificari!");
            return false;
        }
        else return true;
    }

    @FXML
    public void handleUpdate(MouseEvent event){
        TemaLaborator temaLaborator=(TemaLaborator) tableViewTeme.getSelectionModel().getSelectedItem();
        if (temaLaborator!=null){
            if (verificareDacaSePoateModifica(temaLaborator)) showEditingTemaView(temaLaborator);
        }
        else{
            AlertMessage message=new AlertMessage();
            message.showMessage(null, Alert.AlertType.WARNING,"Atentie!","Nu ati selectat nimic din tabelul de Teme");
        }
    }

    @FXML
    public void handleEditCell(TableColumn.CellEditEvent<?,?> event){
        handleUpdateCellAndTema((Integer)event.getNewValue());
    }

    @FXML
    private void handleUpdateCellAndTema(Integer newDeadline){
        TemaLaborator temaLaborator=(TemaLaborator) tableViewTeme.getSelectionModel().getSelectedItem();
        if (temaLaborator!=null){
            if (verificareDacaSePoateModifica(temaLaborator)) {
                try{
                    service.updateTemaLabTermenPredare(temaLaborator.getId(),newDeadline);
                    AlertMessage message=new AlertMessage();
                    message.showMessage(null, Alert.AlertType.INFORMATION,"Informatie","Termenul de predare a fost modificat cu succes");
                    update();
                } catch (EntityNotFoundException e) {
                    e.printStackTrace();
                } catch (ValidationException e) {
                    AlertMessage message=new AlertMessage();
                    message.showMessage(null, Alert.AlertType.ERROR,"Eroare!","Deadlineul introdus trebuie sa fie un numar natural cuprins intre 1 si 14");
                    update();
                }
            }
        }
        else{
            AlertMessage message=new AlertMessage();
            message.showMessage(null, Alert.AlertType.WARNING,"Atentie!!!","Nu ati selectat nimic din tabelul teme");
        }
    }

    private void setComboBoxItemsPerPage(){
        ObservableList<Integer> list=FXCollections.observableArrayList();
        list.addAll(5,10,20,50);
        comboBoxItemsPerPage.getItems().setAll(list);
    }

    @FXML
    public void initialize(){

        setComboBoxItemsPerPage();
        setItemsPerPage(10);
        comboBoxItemsPerPage.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                setItemsPerPage((Integer) comboBoxItemsPerPage.getValue());
                updatePage();
            }
        });

        tableViewTeme.setEditable(true);
        columnNrTema.setCellValueFactory(new PropertyValueFactory<TemaLaborator,Integer>("nr_tema_de_laborator"));
        columnCerinta.setCellValueFactory(new PropertyValueFactory<TemaLaborator,String>("cerinta"));

        Callback<TableColumn,TableCell> cellFactory=
                new Callback<TableColumn,TableCell>() {
                    @Override
                    public TableCell call(TableColumn p){
                        return new EditingCellInteger();
                    }

                };

        columnDeadline.setCellValueFactory(new PropertyValueFactory<TemaLaborator,Integer>("deadline"));
        columnDeadline.setCellFactory(cellFactory);

//        tableViewTeme.setItems(model);

        ObservableList<String> listFiltrari=FXCollections.observableArrayList();
        listFiltrari.addAll("dupa Cerinta","deadline Saptamana curenta","cu deadline depasit");
        comboBoxFiltrariTemeLaborator.setItems(listFiltrari);

        comboBoxFiltrariTemeLaborator.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                handleFiltrari();
//                updateTable();
            }
        });

        textFieldFiltrariTeme.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                handleFiltrari();
            }
        });

        paginationTeme.setPageFactory(new Callback<Integer, Node>() {
            @Override
            public Node call(Integer param) {
                if (param<=data.size()/getItemsPerPage())return createPage(param);
                else return null;
            }
        });

    }

    private VBox createPage(Integer pageNumber){
        VBox vBox=new VBox();
        vBox.getChildren().add(tableViewTeme);

        Integer indexFrom=0;
        if (data.size()>=pageNumber*getItemsPerPage())
            indexFrom=pageNumber*getItemsPerPage();
        Integer indexTo=Math.min(indexFrom+getItemsPerPage(),data.size());
        model.setAll(data.subList(indexFrom,indexTo));
        tableViewTeme.setItems(model);

        return vBox;

    }

    protected void updateTable( ) {
        if (data.size()%getItemsPerPage()!=0) {
            paginationTeme.setMaxPageIndicatorCount((int) ((data.size() / getItemsPerPage()) + 1));
            paginationTeme.setPageCount((int) ((data.size() / getItemsPerPage()) + 1));
        }
        else{
            paginationTeme.setMaxPageIndicatorCount((int) ((data.size() / getItemsPerPage())));
            paginationTeme.setPageCount((int) ((data.size() / getItemsPerPage()) ));
        }
        for (int i=0;i<(int)(data.size()/getItemsPerPage())+1;i++){
            Integer indexFrom=i*getItemsPerPage();
            long indexTo=Math.min(indexFrom+getItemsPerPage(),data.size());
            model.setAll(data.subList(indexFrom,(int) indexTo));
            tableViewTeme.setItems(model);
        }
    }

    private Integer getItemsPerPage(){
        return itemsPerPage;
    }

    public void setService(Service service){
        this.service=service;
        setDataInit();
        updateTable();
    }

    public void setDataInit(){
        this.data.setAll(service.getListTemeLaborator());
    }

    @Override
    public void update() {
        setDataInit();
        updatePage();
    }

    protected void updatePage() {
        Integer index=paginationTeme.getCurrentPageIndex();
        if (data.size()==0){
            paginationTeme.setPageCount(1);
        }
        if (data.size() % getItemsPerPage() != 0) {
            paginationTeme.setMaxPageIndicatorCount((int) ((data.size() / getItemsPerPage()) + 1));
            paginationTeme.setPageCount((int) ((data.size() / getItemsPerPage()) + 1));
        } else {
            paginationTeme.setMaxPageIndicatorCount((int) ((data.size() / getItemsPerPage())));
            paginationTeme.setPageCount((int) ((data.size() / getItemsPerPage())));
        }
        if (index<(int)data.size()/getItemsPerPage()+1) {

            paginationTeme.getPageFactory().call(index);
            paginationTeme.setPageFactory((Integer page)->{return createPage(page);});
        }
        else {

            paginationTeme.getPageFactory().call(0);
            paginationTeme.setPageFactory((Integer page)->{return createPage(page);});
        }
    }

    public void handleFiltrari(){
        String filtrareTip=(String) comboBoxFiltrariTemeLaborator.getValue();
        if (filtrareTip!=null){
            switch (filtrareTip){
                case "dupa Cerinta":{
                    filtrareByCerinta();
                    break;
                }
                case "deadline Saptamana curenta":{
                    filtrareBySaptamanaCurenta();
                    break;
                }
                case "cu deadline depasit":{
                    filtrareByDeadlineDepasit();
                    break;
                }
            }
        }
        else update();
    }

    private void filtrareByDeadlineDepasit() {
        List<TemaLaborator> list=filtrareTeme(service.filteredTemeByDeadlineDepasit());
        data.setAll(list);
        updatePage();
    }

    private void filtrareBySaptamanaCurenta() {
        List<TemaLaborator> list=filtrareTeme(service.filteredTemeByDeadlineThisWeek());
        data.setAll(list);
        updatePage();

    }

    protected void filtrareByCerinta() {
        String cuvantCheie=textFieldFiltrariTeme.getText();
        if (!cuvantCheie.isEmpty()){
            List<TemaLaborator> list=filtrareTeme(service.filteredTemeByCerinta(cuvantCheie));
            data.setAll(list);
            updatePage();
        }
        else {
            update();
        }
    }

    private List<TemaLaborator> filtrareTeme(List<TemaLaborator> listaToata){
        List<TemaLaborator> list=getDataInit();
        List<TemaLaborator> result=new ArrayList<>();
        for (TemaLaborator tema:listaToata){
            if (list.contains(tema)){
                result.add(tema);
            }
        }
        return result;
    }

    protected List<TemaLaborator> getDataInit() {
        return service.getListTemeLaborator();
    }
}
