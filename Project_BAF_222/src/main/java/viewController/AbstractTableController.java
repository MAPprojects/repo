package viewController;

import com.jfoenix.controls.JFXTreeTableView;
import entities.Candidat;
import entities.HasID;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableView;
import javafx.util.Callback;
import observer.Observer;
import service.AbstractService;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractTableController <E extends HasID<ID>,ID> implements Observer {

    @FXML
    protected TableView<E> table;

    @FXML
    protected Pagination pagination;

    protected E selectedEntity;
    protected ObservableList<E> model;
    protected MainWindowController mainWindowsController;
    protected AbstractService service;
    protected int rowsPerPage=14;

    @FXML
    protected void initialize() {
        table.getSelectionModel().selectedItemProperty().
                addListener(new ChangeListener<E>() {
                    @Override
                    public void changed(ObservableValue<? extends E> observable,
                                        E oldCandidat, E newCandidat) {
                        mainWindowsController.deleteButton.setDisable(false);
                        mainWindowsController.modifyButton.setDisable(false);
                        if(mainWindowsController.currentType==TableTipes.CANDIDAT)
                             mainWindowsController.mailButton.setDisable(false);
                        selectedEntity=newCandidat;
                    }
                });
        pagination.getStylesheets().add("/paginationStyle.css");
        table.getStylesheets().add("/table.css");
     //   table.setStyle("-fx-selection-bar: #1ac9ff; -fx-selection-bar-non-focused: #00141a;");
    }

    public void setService(AbstractService service,MainWindowController mainWindowsController){
        this.service=service;
        this.mainWindowsController=mainWindowsController;
        initPagination(0,(Collection<? extends E>) service.getAllEntities());
    }

    protected void initPagination(int currentPage, Collection<? extends E> entityList) {
        int repoSize=entityList.size();
        int rest=0;
        if(repoSize%rowsPerPage>0 || repoSize == 0)
            rest=1;
        if(pagination.getPageCount() != repoSize/rowsPerPage+rest){

        pagination.setCurrentPageIndex(currentPage);
        pagination.setPageCount(repoSize/rowsPerPage+rest);

        pagination.setMaxPageIndicatorCount(10);
        pagination.setPageFactory(new Callback<Integer, Node>() {
            @Override
            public Node call(Integer pageIndex) {
                if (pageIndex > entityList.size() / rowsPerPage + 1)
                    return null;
                loadData(pageIndex,entityList);
                return table;
            }
        });

        }
        loadData(pagination.getCurrentPageIndex(),entityList);
    }

    private void loadData(int currentPage,Collection<? extends E> entityList) {
        int lastIndex = 0;
        int endIndex = 0;
        int listSize = (entityList.size());
        lastIndex = rowsPerPage * currentPage;
        if (lastIndex+rowsPerPage > listSize)
            endIndex = listSize;
        else endIndex = lastIndex + rowsPerPage;
        this.model = FXCollections.observableArrayList( (entityList.stream().collect(Collectors.toList()).subList(lastIndex,endIndex)));
        table.setItems(model);
    }

    public void setTableValues(List<E> newList){
        initPagination(0,newList);
    }

    @Override
    public void update() {
        initPagination(pagination.getCurrentPageIndex(),(Collection<? extends E>) service.getAllEntities());
    }

    public void handleDelete(){
        if(selectedEntity!=null) {
            service.deleteEntity(selectedEntity.getID());
            mainWindowsController.deleteButton.setDisable(true);
            mainWindowsController.modifyButton.setDisable(true);
            mainWindowsController.mailButton.setDisable(true);
        }
    }

    public E getSelectedEntity(){
        return selectedEntity;
    }
}
