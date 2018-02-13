package sample;

import Domain.EntitiesException;
import Domain.Tema;
import Repository.RepositoryException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;
import utils.ListEvent;
import utils.Observer;
import Service.Service;
import Service.ServiceException;


import javax.sql.rowset.serial.SerialException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class LabController implements java.util.Observer {
    private ObservableList<Tema> model2;
    private Service service;
    private final static int rowsPerPage = 3;
    @FXML
    Pagination pagination;
    @FXML
    TableView<Tema> tableView2;
    @FXML
    TableColumn<Tema,String> numar,deadline,descriere;
    @FXML
    TextField numarField,descriereField,deadlineField;

    public void update(ListEvent<Tema> e){
        model2.setAll(StreamSupport.stream(e.getList().spliterator(),false)
                .collect(Collectors.toList()));
    }

    public LabController(){}

    public void setService(Service service) {
        this.service = service;
        model2= FXCollections.observableArrayList(this.service.fromIterableToList(service.getAllLabs()));
        tableView2.setItems(model2);
        pagination.setPageFactory(this::createPage);
        setPagination();
    }

    private void setPagination(){
        int size=service.fromIterableToList(service.getAllLabs()).size();
        int pageCount=size/rowsPerPage;
        if(size%rowsPerPage>0){
            pageCount=pageCount+1;
        }
        pagination.setPageCount(pageCount);
        pagination.setPageFactory(this::createPage);
    }

    private Node createPage(int pageIndex) {

        int fromIndex = pageIndex * rowsPerPage;
        int toIndex = Math.min(fromIndex + rowsPerPage, model2.size());
        //tableView2.setItems(FXCollections.observableArrayList(service.fromIterableToList(service.getAllLabs()).subList(fromIndex, toIndex)));
        tableView2.setItems(FXCollections.observableArrayList(service.getLabSublist(fromIndex,toIndex)));
        if(fromIndex!=toIndex)
            return new BorderPane(tableView2);
        else{
            List<Tema> list=new ArrayList<>();
            tableView2.setItems(FXCollections.observableArrayList(list));
            return new BorderPane(tableView2);
        }
    }



    public void showDetails(Tema t){
        if(t!=null) {
            numarField.setText(t.getId().toString());
            descriereField.setText(t.getDescriere());
            deadlineField.setText(t.getDeadline().toString());
        }
    }

    private static void showMessage(Alert.AlertType type, String header, String text){
        Alert message=new Alert(type);
        message.setHeaderText(header);
        message.setContentText(text);
        message.showAndWait();
    }

    private static void showErrorMessage(String text){
        Alert message=new Alert(Alert.AlertType.ERROR);
        message.setTitle("Mesaj eroare");
        message.setContentText(text);
        message.showAndWait();
    }

    private void clearAll(){
        numarField.clear();
        descriereField.clear();
        deadlineField.clear();
        numarField.setDisable(false);
    }

    private Tema getTemaFromFields(){

        Tema t=new Tema(Integer.parseInt(numarField.getText()),descriereField.getText(),Integer.parseInt(deadlineField.getText()));
        return t;
    }

    public void handleAddButton(ActionEvent actionEvent){
        String erori="";
        if(numarField.getText().equals(""))
            erori+="Nu ati completat id`ul!";
        if(deadlineField.getText().equals(""))
            erori+="Nu ati completat deadlineul";
        if(!erori.equals(""))
            showErrorMessage(erori);
        else {
            try {
                service.saveTema(getTemaFromFields());
                int curr = pagination.getCurrentPageIndex();
                setPagination();
                pagination.setCurrentPageIndex(curr);
                showMessage(Alert.AlertType.INFORMATION, "Adaugare...", "S-a adaugat tema de laborator.");
                clearAll();
            } catch (RepositoryException re) {
                showErrorMessage("Exista deja o tema cu acest id!");
                clearAll();
            } catch (EntitiesException e) {
                showErrorMessage("Nu ati introdus date valide. ID NEVALID!");
            } catch (NumberFormatException ne) {
                showErrorMessage("Date invalide! Nu ati completat campurile!");
                clearAll();
            }
        }
    }

    public void handleDeleteButton(ActionEvent actionEvent){
        Tema t=tableView2.getSelectionModel().getSelectedItem();
        if(t!=null){
            int id=t.getId();
            service.deleteTema(id);
            int curr=pagination.getCurrentPageIndex();
            setPagination();
            pagination.setCurrentPageIndex(curr);
            showMessage(Alert.AlertType.INFORMATION,"Stergere...","Tema a fost stearsa.");
            clearAll();
        }
        else
            showMessage(Alert.AlertType.WARNING, "Stergere", "Tema cu acest ID nu exista");
    }

    public void handleUpdateButton(ActionEvent actionEvent){
            try {
                Tema t = getTemaFromFields();
                service.updateDeadLineTema(t);
                int curr=pagination.getCurrentPageIndex();
                setPagination();
                pagination.setCurrentPageIndex(curr);
                showMessage(Alert.AlertType.INFORMATION,"Modificare...","Deadlineul a fost modificat.");
                clearAll();
            }catch (RepositoryException re){
                showMessage(Alert.AlertType.ERROR,"Modificare...",re.getMessage());
            }catch (ServiceException srv){
                showMessage(Alert.AlertType.ERROR,"Modificare...",srv.getMessage());
            }
    }

    public void setAbilities(){
        numarField.setDisable(true);
        descriereField.setDisable(true);
        deadlineField.setDisable(true);
    }

    @FXML
    public void initialize(){
        numar.setCellValueFactory(new PropertyValueFactory<Tema,String>("id"));
        deadline.setCellValueFactory(new PropertyValueFactory<Tema,String>("deadline"));
        descriere.setCellValueFactory(new PropertyValueFactory<Tema,String>("descriere"));
        tableView2.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tema>() {
            @Override
            public void changed(ObservableValue<? extends Tema> observable, Tema oldValue, Tema newValue) {
                showDetails(newValue);
                numarField.setDisable(true);
            }
        });
    }

    @Override
    public void update(Observable o, Object arg) {
        model2.setAll(service.fromIterableToList(service.getAllLabs()));
    }

}
