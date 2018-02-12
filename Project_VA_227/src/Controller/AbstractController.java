package Controller;

import Domain.ExceptionValidator;
import Domain.Tip;
import Domain.User;
import Repository.HasID;
import Service.AbstractService;
import Utils.Observable;
import Utils.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.util.List;

public abstract class AbstractController<ID, E extends HasID<ID>> implements IController<ID, E>,
        Observer<E>, Initializable {

    @FXML
    protected TableView<E> table;

    @FXML
    private ComboBox<Integer> pageCombo;

    @FXML
    public Pagination paginator;

    protected AbstractService<ID, E> service;
    protected ObservableList<E> model;
    private Integer perPage;
    protected User user = null;
    @FXML
    protected ComboBox<String> actionsChoose;

    AbstractController() {
        perPage = 10;
    }

    public void setUser(User user) {
        this.user = user;
        for (Integer page = 1; page <= 10; page++)
            pageCombo.getItems().add(page);
        pageCombo.setValue(10);
        pageCombo.setOnAction(event -> {
            perPage = pageCombo.getValue();
            populateList(true);
        });
        if (user.getTip() == Tip.ADMIN)
            actionsChoose.getItems().addAll("Add", "Update", "Remove", "Filter", "Get All");
        else
            actionsChoose.getItems().addAll("Filter", "Get All");
    }

    public boolean userIsSet() {
        return user != null;
    }

    protected abstract void showDetails(E newValue);

    public void setService(AbstractService<ID, E> service) {
        if (service != null)
            service.removeObserver(this);
        this.service = service;
        service.addObserver(this);
        populateList(true);
    }

    private Node createPage(int pageIndex) {

        int fromIndex = pageIndex * perPage;
        int toIndex = Math.min(fromIndex + perPage, model.size());
        table.setItems(FXCollections.observableArrayList(model.subList(fromIndex, toIndex)));

        return new BorderPane(table);
    }

    @Override
    public void populateList(boolean firstTime) {
        Integer oldSize = model.size();
        model.clear();
        Iterable<ID> iterable = service.getAll();
        iterable.forEach(x -> model.add(service.find(x)));
        if (firstTime) {
            paginator.setCurrentPageIndex(0);
            paginator.setPageCount(model.size() > 0 ? (int) (Math.ceil((model.size() + perPage - 1) / perPage)) : 1);
            paginator.setMaxPageIndicatorCount(model.size() > 0 ? (int) (Math.ceil((model.size() + perPage - 1) / perPage)) : 1);
            paginator.setPageFactory(this::createPage);
        } else {
            int status = oldSize - model.size();
            int currPage = paginator.getCurrentPageIndex(), max = paginator.getMaxPageIndicatorCount();
            paginator.setPageFactory(this::createPage);
            paginator.setPageCount(model.size() > 0 ? (int) (Math.ceil((model.size() + perPage - 1) / perPage)) : 1);
            paginator.setMaxPageIndicatorCount(model.size() > 0 ? (int) (Math.ceil((model.size() + perPage - 1) / perPage)) : 1);
            if (currPage + 1 > paginator.getMaxPageIndicatorCount())
                paginator.setCurrentPageIndex(currPage > 0 ? currPage - 1 : 1);
            else if (status == -1 || paginator.getMaxPageIndicatorCount() > max)
                paginator.setCurrentPageIndex(max + 1);
            else
                paginator.setCurrentPageIndex(currPage);
        }
    }

    @Override
    public void populateList(List<E> list) {
        model.clear();
        model.addAll(list);
        paginator.setCurrentPageIndex(0);
        paginator.setPageCount(model.size() > 0 ? (int) (Math.ceil((model.size() + perPage - 1) / perPage)) : 1);
        paginator.setMaxPageIndicatorCount(model.size() > 0 ? (int) (Math.ceil((model.size() + perPage - 1) / perPage)) : 1);
        paginator.setPageFactory(this::createPage);
    }

    @Override
    public void add(E item) throws ExceptionValidator, IOException {
        service.add(item);
    }

    @Override
    public void delete(ID item) throws IOException {
        service.remove(item);
    }

    @Override
    public void update(E item) throws ExceptionValidator, IOException {
        service.update(item);
    }

    @Override
    public ObservableList<E> getMessageTaskModel() {
        return model;
    }

    public static void showError(String error) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(error);
        alert.showAndWait();
    }

    @Override
    public void notifyOnEvent(Observable<E> e) {
        populateList(false);
    }
}
