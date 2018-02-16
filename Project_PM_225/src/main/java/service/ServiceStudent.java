package service;

import observer.ListEvent;
import observer.ListEventType;
import observer.Observable;
import observer.Observer;
import domain.Student;
import repository.Filtre;
import repository.RepoStudent;
import repository.RepoNota;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public class ServiceStudent implements Observable<Student> {
    private ArrayList<Observer<Student>> studentObservers;
    private RepoStudent repoStudent;
    private RepoNota repoNota;
    private Filtre filtre = new Filtre();

    public ServiceStudent(RepoStudent repoStudent, RepoNota rNota) throws Exception {
        repoNota = rNota;
        this.repoStudent = repoStudent;
        this.studentObservers = new ArrayList<>();

    }

//    public ServiceStudent() throws Exception {
//
//    }

    public void add(Integer id, String nume, Integer grupa, String email, String indrumator) throws Exception {
        Student student = new Student(id, nume, grupa, email, indrumator);
        repoStudent.add(student);
        ListEvent<Student> event = createEvent(ListEventType.ADD, null, this.returnall());
        notifyAllObservers(event);
    }

    public Student findobject(Integer id) throws Exception {
        return repoStudent.findobject(id, "getId");

    }

    public List<Student> returnall() throws Exception {
        return repoStudent.getLista();

    }

    public void update(Integer id, String nume, Integer grupa, String email, String indrumator) throws Exception {
        Student student = new Student(id, nume, grupa, email, indrumator);
        repoStudent.update(id, student);
        ListEvent<Student> event = createEvent(ListEventType.UPDATE, null, this.returnall());
        notifyAllObservers(event);

    }

    public void delete(Integer id) throws Exception {
        if (repoNota.findobject(id, "getIdStudent") != null) {
            throw new Exception("Student imposibil de sters!");
        } else {
            repoStudent.delete(id, "getId");
        }
        ListEvent<Student> event = createEvent(ListEventType.REMOVE, null, this.returnall());
        notifyAllObservers(event);
    }

    public List<Student> studentiPeAni(Integer an) {
        Predicate<Student> p = student -> student.getGrupa() / 10 % 10 == an;
        Comparator<Student> c = Comparator.comparing(Student::getNume);
        return filtre.filterAndSorter(repoStudent.getLista(), p, c);
    }

    public List<Student> tataLor(String profesor) {
        Predicate<Student> p = student -> student.getIndrumator().equals(profesor);
        Comparator<Student> c = Comparator.comparing(Student::getNume).reversed();
        return filtre.filterAndSorter(repoStudent.getLista(), p, c);
    }

    public List<Student> studentiNumeSauPrenumeCuA(String litera) {
        Predicate<Student> p = student -> (!student.getNume().contains(" ") && student.getNume().startsWith(litera)) || (student.getNume().contains(" ") && (student.getNume().startsWith(litera) || student.getNume().split(" ")[1].startsWith(litera)));

        Comparator<Student> c = Comparator.comparing(Student::getNume);
        return filtre.filterAndSorter(repoStudent.getLista(), p, c);
    }

    private <E> ListEvent<E> createEvent(ListEventType type, final E element, final List<Student> list) {
        return new ListEvent<E>(type) {
            @Override
            public ArrayList<E> getList() {
                return (ArrayList<E>) list;
            }

            @Override
            public E getElement() {
                return element;
            }
        };
    }

    @Override
    public void addObserver(Observer<Student> obs) {
        this.studentObservers.add(obs);
    }

    @Override
    public void removeObserver(Observer<Student> obs) {
        this.studentObservers.remove(obs);
    }

    @Override
    public void notifyAllObservers(ListEvent<Student> event) {
        this.studentObservers.forEach(x -> x.notifyEvent(event));
    }
}
