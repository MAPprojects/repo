package Utils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;

public class MyPagination<T>  {
    private TableView<T> workingTableView;
    private ObservableList<T> workingModel;
    private int rowsPerPage;
    private Pagination pagination;
    private Pane workingPane;
    public MyPagination(TableView<T> tableView,int rows,ObservableList<T> model)
    {
        workingTableView=tableView;
        rowsPerPage=rows;
        workingModel=model;
        pagination=new Pagination(workingModel.size()/rowsPerPage+1,0);
        pagination.setPageFactory(this::createPage);
    }

    private Node createPage(int pageIndex) {
            int fromIndex = pageIndex * rowsPerPage;
            int toIndex = Math.min(fromIndex + rowsPerPage, workingModel.size());
            workingTableView.setItems(FXCollections.observableArrayList(workingModel.subList(fromIndex, toIndex)));
            return new Pane(workingTableView);
        }

    public Pagination getPagination() {
        return pagination;
    }
}
