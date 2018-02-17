package Services;
import Exceptions.RepositoryException;
import Exceptions.ValidationException;
import Repository.*;
import Domain.*;
import Utilities.ListEvent;
import Utilities.ListEventType;

import java.util.*;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import java.util.function.*;
import Utilities.Observable;
import Utilities.Observer;

public class StudentService extends AbstractService {
    GenericRepository<Integer, Student> repository;

    ArrayList<Observer<Student>> observers = new ArrayList<>();

    public GenericRepository<Integer,Student> getRepository() {
        return repository;
    }

    public StudentService(GenericRepository<Integer, Student> studentRep) {
        this.repository = studentRep;
    }

    public void add(int id, String nume, int grupa, String email, String profesor) {
        for (Student st : repository.getAll()) {
            if (st.getEmail().equals(email)) {
                throw new RepositoryException("student email already exists");
            }
        }
        Student st = new Student(id,nume,grupa,email,profesor);
        repository.add(st);
        ListEvent<Student> ev = createEvent(ListEventType.ADD, st, getAllAsList());
        notifyObservers(ev);
    }

    public void update(int id, String nume, int grupa, String email, String prof) {
        Student st = new Student(id, nume, grupa, email, prof);
        repository.update(id, st);
        ListEvent<Student> ev = createEvent(ListEventType.UPDATE, st, getAllAsList());
        notifyObservers(ev);

    }

    public Student findByEmail(String email) {
        for (Student student :getAllAsList()) {
            if (student.getEmail().equals(email)) {
                return student;
            }
        }
        throw new ValidationException("student with email " + email + " not found");

    }

    public Student remove(int id) {
        Student deleted = repository.delete(id);
        if (deleted != null) {
            ListEvent<Student> ev = createEvent(ListEventType.REMOVE, deleted, getAllAsList());
            notifyObservers(ev);
            return deleted;
        }
        throw new ValidationException("student does not exist");
    }

    //filtrari studenti
    public List<Student> fillterGrupa(int grupa) {
        Predicate<Student> buni = x->x.getGrupa() == grupa;
        Comparator<Student> comparati = (x,y) -> x.getNume().compareTo(y.getNume());
        List<Student> all = getAllAsList();
        return fillter(all, buni, comparati);
    }

    public List<Student> fillterBeginsWith(String character) {
        Predicate<Student> buni = x->x.getNume().startsWith(character);
        Comparator<Student> comparati = (x,y) -> x.getNume().compareTo(y.getNume());
        return fillter(getAllAsList(), buni, comparati);
    }

    public List<Student> fillterByProf(String prof) {
        Predicate<Student> buni = x->x.getProfesor().startsWith(prof);
        Comparator<Student> comparati = (x,y) -> x.getNume().compareTo(y.getNume());
        return fillter(getAllAsList(), buni, comparati);
    }

    public List<Student> getAllAsList() {
        List<Student> all = new ArrayList<>();
        repository.getAll().forEach(all::add);
        return all;
    }

    public Student get(int id) {
        return repository.get(id);
    }

    public Iterable<Student> getAll() {
        return repository.getAll();
    }

}
