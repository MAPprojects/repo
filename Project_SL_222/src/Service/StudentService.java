package Service;
import Domain.Nota;
import Domain.NotaID;
import Domain.Student;

import Repository.IRepository;
import Repository.RepositoryException;
import Util.ListEvent;
import Util.ListEventType;
import Util.Observable;
import Util.Observer;
import Validator.ValidationException;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static Util.Util.toList;

public class StudentService implements Observable<Student>{
    private IRepository<Integer, Student> repoStudent;
    ArrayList<Observer<Student>> studentObservers=new ArrayList<>();
    public StudentService(IRepository<Integer,Student> repoStudent){
        this.repoStudent=repoStudent;
    }

    @Override
    public void addObserver(Util.Observer<Student> o) {
        studentObservers.add(o);
    }

    @Override
    public void removeObserver(Observer<Student> o) {
        studentObservers.remove(o);
    }

    @Override
    public void notifyObservers(ListEvent<Student> event) {
        studentObservers.forEach(x->x.notifyEvent(event));
    }

    public <E> ListEvent<E> createEvent(ListEventType type, final E elem, final Iterable<E> l){
        return new ListEvent<E>(type) {
            @Override
            public Iterable<E> getList() {
                return l;
            }
            @Override
            public E getElement() {
                return elem;
            }
        };
    }

    public List<Student> getAllS(){
        return StreamSupport.stream(repoStudent.getAll().spliterator(),false).collect(Collectors.toList());
    }

    public void addStudent(int id,String nume,int grupa, String email,String profesor){
        Student stud = new Student(id, nume, grupa, email, profesor);
        repoStudent.save(stud);
        try{
            ListEvent<Student> ev = createEvent(ListEventType.ADD, stud, repoStudent.getAll());
            notifyObservers(ev);
        }
        catch(RepositoryException e){
            throw e;
        }
    }

    public boolean findStudent(int id){
        for(Student stud:repoStudent.getAll())
            if(stud.getID()==id)
                return true;
        return false;
    }

    public Student deleteStudent(int id){
        Student stud = repoStudent.delete(id);
        if(stud!=null){
            ListEvent<Student> ev = createEvent(ListEventType.REMOVE,stud,repoStudent.getAll());
            notifyObservers(ev);
        }
        return stud;
    }

    public void updateStudent(Student stud){
        try{
            repoStudent.update(stud);
            ListEvent<Student> ev = createEvent(ListEventType.UPDATE,stud,repoStudent.getAll());
            notifyObservers(ev);
        }
        catch(ValidationException e){
            throw e;
        }
    }



    public <E>List<E> filterAndSorter(List<E> lista, Predicate<E> pred, Comparator<E> comp){
        return lista.stream().filter(pred).sorted(comp).collect(Collectors.toList());
    }

    Comparator<Student> compStudentIDCresc = (x,y) -> x.compare(x,y);


    public List<Student> filterStudentNameCtr(String name,String comp){
        Predicate<Student> pred=x->x.getNume().equals(name);
        if(comp.contentEquals("Desc"))
            return filterAndSorter(toList(getAllS()),pred,compStudentIDCresc.reversed());
        else
            return filterAndSorter(toList(getAllS()),pred,compStudentIDCresc);
    }

    public List<Student> filterStudentGroupCtr(int group,String comp){
        Predicate<Student> pred=(x->x.getGrupa()==group);
        if(comp.contentEquals("Desc"))
            return filterAndSorter(toList(getAllS()),pred,compStudentIDCresc.reversed());
        else
            return filterAndSorter(toList(getAllS()),pred,compStudentIDCresc);
    }

    public List<Student> filterStudentTeacherCtr(String teacher,String comp){
        Predicate<Student> pred=x->x.getProfesor().equals(teacher);
        if(comp.contentEquals("Desc"))
            return filterAndSorter(toList(getAllS()),pred,compStudentIDCresc.reversed());
        else if(comp.contentEquals("Cresc"))
            return filterAndSorter(toList(getAllS()),pred,compStudentIDCresc);
        else
            throw new ValidationException("Trebuie sa alegeti dintre: Desc si Cresc!");
    }

}
