package View;

import Controllers.ServiceObservable;
import Entities.Nota;
import Entities.Student;
import Entities.Teme;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utils.*;

public class ViewController implements Observer<Event> {
    private ServiceObservable so;
    private ObservableList<Student> model_s = FXCollections.observableArrayList();
    private ObservableList<Teme> model_t = FXCollections.observableArrayList();
    private ObservableList<Nota> model_n = FXCollections.observableArrayList();

    public ViewController(ServiceObservable so) {
        this.so = so;
        so.getAllStudents().forEach(x->model_s.add(x));
        so.getAllTeme().forEach(x->model_t.add(x));
        so.getAllNote().forEach(x->model_n.add(x));
    }

    public ViewController() {}

    public ServiceObservable getSo() {
        return so;
    }

    private void notifyEvent(StudentEvent e) {
        model_s.clear();
        so.getAllStudents().forEach(x->model_s.add(x));
    }

    private void notifyEvent(TemeEvent e) {
        model_t.clear();
        so.getAllTeme().forEach(x->model_t.add(x));
    }

    private void notifyEvent(NotaEvent e) {
        model_n.clear();
        so.getAllNote().forEach(x->model_n.add(x));
    }

    public ObservableList<Student> getModel_s() {
        return model_s;
    }

    public ObservableList<Teme> getModel_t() {
        return model_t;
    }

    public ObservableList<Nota> getModel_n() {
        return model_n;
    }

    @Override
    public void notifyOnEvent(Event e) {
        if(e instanceof  StudentEvent)
            notifyEvent((StudentEvent) e);
        else if(e instanceof TemeEvent)
            notifyEvent((TemeEvent) e);
        else
            notifyEvent((NotaEvent) e);
    }
}
