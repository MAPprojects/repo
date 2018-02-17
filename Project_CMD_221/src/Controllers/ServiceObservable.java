package Controllers;

import Entities.Nota;
import Entities.Student;
import Entities.Teme;
import Entities.User;
import Repository.IRepository;
import Repository.IUserRepository;
import Validators.ValidatorException;
import javafx.util.Pair;
import utils.*;

import java.util.ArrayList;
import java.util.List;

public class ServiceObservable extends Service implements Observable<Event> {
    private List<Observer<Event>> observableList;
    public ServiceObservable(IRepository<Integer, Student> repo_s, IRepository<Integer, Teme> repo_t, IRepository<Pair<Integer, Integer>, Nota> repo_n, IUserRepository<Pair<String, String>, User> repo_user) {
        super(repo_s, repo_t, repo_n, repo_user);
        observableList = new ArrayList<>();
    }

    @Override
    public void addObserver(Observer<Event> obs) {
        observableList.add(obs);
    }

    @Override
    public void removeObserver(Observer<Event> obs) {
        observableList.remove(obs);
    }

    @Override
    public void notifyObservers(Event e) {
        observableList.forEach(x->x.notifyOnEvent(e));
    }

    @Override
    public void save_student(Integer id, String nume, String email, String grupa, String prof, Integer note, Integer nr, Double medie, Boolean val, Boolean fint) throws ValidatorException {
        super.save_student(id, nume, email, grupa, prof, note, nr, medie, val, fint);
        notifyObservers(new StudentEvent(CRUDEventType.ADD, new Student(id, nume, email, grupa, prof, note, nr, medie, val, fint)));
    }

    @Override
    public void update_student(Integer id, String nume, String email, String grupa, String prof, Integer note, Integer nr, Double medie, Boolean val, Boolean fint) {
        super.update_student(id, nume, email, grupa, prof, note, nr, medie, val, fint);
        notifyObservers(new StudentEvent(CRUDEventType.UPDATE, new Student(id, nume, email, grupa, prof, note, nr, medie, val, fint)));
    }

    @Override
    public void delete_student(Integer id) {
        Student s = super.findStudent(id);
        super.delete_student(id);
        notifyObservers(new StudentEvent(CRUDEventType.DELETE, s));
    }

    @Override
    public void save_tema(Integer id, Integer deadline, String descriere, Integer dif) throws ValidatorException {
        super.save_tema(id, deadline, descriere, dif);
        notifyObservers(new TemeEvent(CRUDEventType.ADD, new Teme(id, deadline, descriere, dif)));
    }

    @Override
    public void update_tema(Integer id, Integer deadline, String descriere, Integer dif) {
        super.update_tema(id, deadline, descriere, dif);
        notifyObservers(new TemeEvent(CRUDEventType.UPDATE, new Teme(id, deadline, descriere, dif)));
    }

    @Override
    public void update_Deadline(Integer id, Integer deadline) throws ValidatorException {
        super.update_Deadline(id, deadline);
        notifyObservers(new TemeEvent(CRUDEventType.UPDATE, new Teme(id, deadline, findTema(id).getDescriere(), findTema(id).getDif())));
    }

    @Override
    public void delete_tema(Integer id) {
        Teme tema = super.findTema(id);
        super.delete_tema(id);
        notifyObservers(new TemeEvent(CRUDEventType.DELETE, tema));
    }

    @Override
    public void saveNota(Pair<Integer,Integer> id, String nume, Integer val, Integer week) {
        super.saveNota(id, nume, val, week);
        notifyObservers(new NotaEvent(CRUDEventType.ADD, new Nota(id.getKey(), nume, id.getValue(), val)));
    }

    @Override
    public void updateNota(Pair<Integer,Integer> id, String nume, Integer val, Integer week) {
        super.updateNota(id, nume, val, week);
        notifyObservers(new NotaEvent(CRUDEventType.UPDATE, new Nota(id.getKey(), nume, id.getValue(), val)));
    }

    @Override
    public void deleteNota(Pair<Integer,Integer> id) {
        Nota nota = super.findNota(id);
        super.deleteNota(id);
        notifyObservers(new NotaEvent(CRUDEventType.DELETE, nota));
    }

    public static int CompareNotaByIDStudent(Nota nota, Nota nota2) {
        if(nota.getIDStudent() < nota2.getIDStudent())
            return 1;
        if(nota.getIDStudent() > nota2.getIDStudent())
            return -1;
        return 0;
    }

    @Override
    public void save_user(Pair<String, String> ID, Integer tip1, Integer tip2) throws ValidatorException {
        super.save_user(ID, tip1, tip2);
        notifyObservers(new UserEvent(CRUDEventType.ADD, new User(ID.getKey(), ID.getValue(), tip1, tip2)));
    }

    @Override
    public void update_user(Pair<String, String> ID, Integer tip1, Integer tip2) throws ValidatorException {
        super.update_user(ID, tip1, tip2);
        notifyObservers(new UserEvent(CRUDEventType.UPDATE, new User(ID.getKey(), ID.getValue(), tip1, tip2)));
    }
}
