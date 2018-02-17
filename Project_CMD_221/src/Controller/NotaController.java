package Controller;

import Controllers.Service;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Pair;
import utils.Observer;
import Entities.Nota;
import utils.NotaEvent;

public class NotaController implements Observer<NotaEvent> {
    private Service service;
    private ObservableList<Nota> NotaModel;

    public NotaController(Service service) {
        this.service = service;
        //service.addObserverNota(this);
        NotaModel = FXCollections.observableArrayList();
        populateList();
    }

    private void populateList() {
        Iterable<Nota> note = service.getAllNote();
        System.out.println(note);
        note.forEach(x -> NotaModel.add(x));
    }

    public void addNota(Pair<Integer,Integer> id, Integer val, Integer week) {
        //service.saveNota(id, val, week);
    }

    public void updateNota(Pair<Integer,Integer> id, Integer val, Integer week) {
        //service.updateNota(id, val, week);
    }

    public void deleteNota(Pair<Integer,Integer> id) {
        service.deleteNota(id);
    }

    public ObservableList<Nota> getNotaModel() {
        return NotaModel;
    }

    @Override
    public void notifyOnEvent(NotaEvent notaevent) {
        NotaModel.clear();
        Iterable<Nota> nota = service.getAllNote();
        System.out.println(nota);
        nota.forEach(x -> NotaModel.add(x));
    }
}
