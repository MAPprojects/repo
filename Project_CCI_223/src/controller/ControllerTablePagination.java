package controller;

import domain.DetaliiLog;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import sablonObserver.Observer;
import java.util.List;

public abstract class ControllerTablePagination<E> implements Observer {

    @FXML
    private Pagination pagination;
    @FXML
    protected TableView tableView;
    @FXML
    private ComboBox comboBoxItemsPerPage;
    private Integer itemsPerPage = 10;
    protected ObservableList<E> model = FXCollections.observableArrayList();
    protected ObservableList<E> data=FXCollections.observableArrayList();


    public ControllerTablePagination() {
    }

    public void setItemsPerPage(Integer itemsPerPage) {
        this.itemsPerPage = itemsPerPage;
    }

    public void initialize(){
        setItemsPerPageList();
        tableView.setItems(model);

        setItemsPerPage(10);
        comboBoxItemsPerPage.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                setItemsPerPage((Integer)comboBoxItemsPerPage.getValue());
                updatePage();
            }
        });

        pagination.setPageFactory(new Callback<Integer, Node>() {
            @Override
            public Node call(Integer param) {
                if (param<=data.size()/getItemsPerPage())return createPage(param);
                else return null;
            }
        });
        updateTable();
    }

    private void setItemsPerPageList() {
        ObservableList<Integer> list = FXCollections.observableArrayList();
        list.addAll(5, 10, 20, 50);
        comboBoxItemsPerPage.getItems().setAll(list);
    }

    private Node createPage(Integer page) {
        VBox vBox=new VBox();
        vBox.getChildren().add(tableView);

        Integer indexFrom=0;
        if (data.size()>=page*getItemsPerPage())
            indexFrom=page*getItemsPerPage();
        Integer indexTo=Math.min(indexFrom+getItemsPerPage(),data.size());
        model.setAll(data.subList(indexFrom,indexTo));
        tableView.setItems(model);

        return vBox;
    }

    public Integer getItemsPerPage() {
        return itemsPerPage;
    }

    @Override
    public void update() {
        setareData(setareDataInit());
        updatePage();
    }

    private void updateTable(){
        if (data.size()%getItemsPerPage()!=0){
            pagination.setMaxPageIndicatorCount((int) ((data.size() / getItemsPerPage()) + 1));
            pagination.setPageCount((int) ((data.size() / getItemsPerPage()) + 1));
        }
        else{
            pagination.setMaxPageIndicatorCount((int) ((data.size() / getItemsPerPage())));
            pagination.setPageCount((int) ((data.size() / getItemsPerPage()) ));
        }
        for (int i=0;i<(int)(data.size()/getItemsPerPage())+1;i++){
            Integer indexFrom=i*getItemsPerPage();
            Integer indexTo=Math.min(indexFrom+getItemsPerPage(),data.size());
            model.setAll(data.subList(indexFrom, indexTo));
            tableView.setItems(FXCollections.observableList(model));
        }
    }

    private void updatePage() {
        Integer index=pagination.getCurrentPageIndex();

        if (data.size()==0){
            pagination.setPageCount(1);
        }
        if (data.size() % getItemsPerPage() != 0) {
            pagination.setMaxPageIndicatorCount((int) ((data.size() / getItemsPerPage()) + 1));
            pagination.setPageCount((int) ((data.size() / getItemsPerPage()) + 1));
        } else {
            pagination.setMaxPageIndicatorCount((int) ((data.size() / getItemsPerPage())));
            pagination.setPageCount((int) ((data.size() / getItemsPerPage())));
        }


        if (index<(int)data.size()/getItemsPerPage()+1) {
            pagination.getPageFactory().call(index);
            pagination.setPageFactory((Integer page)->{return createPage(page);});
        }
        else {
            pagination.getPageFactory().call(0);
            pagination.setPageFactory((Integer page)->{return createPage(page);});
        }
    }

    public void setareData(List<E> list){
        data.removeAll();
        data.addAll(list);
    }

    public abstract List<E> setareDataInit();
}
