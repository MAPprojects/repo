package Controller;

import Domain.ExceptionValidator;
import Repository.HasID;
import Service.AbstractService;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.util.List;

public interface IController<ID, E extends HasID<ID>> {
    void populateList(boolean firstTime);
    void populateList(List<E> list);
    void add(E item) throws ExceptionValidator, IOException;
    void delete(ID item) throws IOException;
    void update(E item) throws ExceptionValidator, IOException;
    void setService(AbstractService<ID, E> service);
    ObservableList<E> getMessageTaskModel();
}
