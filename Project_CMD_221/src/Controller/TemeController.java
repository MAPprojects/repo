package Controller;

import Controllers.Service;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utils.Observer;
import Entities.Teme;
import utils.TemeEvent;

public class TemeController implements Observer<TemeEvent> {
    private Service service;
    private ObservableList<Teme> TemeModel;

    public TemeController(Service service) {
        this.service = service;
        //service.addObserverTeme(this);
        TemeModel = FXCollections.observableArrayList();
        populateList();
    }

    private void populateList() {
        Iterable<Teme> teme = service.getAllTeme();
        System.out.println(teme);
        teme.forEach(x->TemeModel.add(x));
    }

    public void addTema(Integer id, Integer deadline, String descriere) {
        service.save_tema(id, deadline, descriere, 0);
    }

    public void updateTema(Integer id, Integer deadline, String descriere) {
        service.update_tema(id, deadline, descriere, 0);
    }

    public void deleteTema(Integer id) {
        service.delete_tema(id);
    }

    public ObservableList<Teme> getTemeModel() {
        return TemeModel;
    }

    @Override
    public void notifyOnEvent(TemeEvent temeevent) {
        TemeModel.clear();
        Iterable<Teme> teme = service.getAllTeme();
        System.out.println(teme);
        teme.forEach(x->TemeModel.add(x));
    }
}
