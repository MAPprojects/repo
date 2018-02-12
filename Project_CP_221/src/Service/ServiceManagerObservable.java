package Service;

import Domain.Grade;
import Domain.LabHomework;
import Domain.Student;
import Repository.IRepository;
import Repository.StudentXmlRepository;
import Utils.*;
import Validator.ValidatorException;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class ServiceManagerObservable extends ServiceManager implements Observable<Event> {

    private List<Observer<Event>> observableList;

    public ServiceManagerObservable(IRepository<Integer, Student> stRepo, IRepository<Integer, LabHomework> labRepo, IRepository<Pair<Integer, Integer>, Grade> gradeRepo) {
        super(stRepo, labRepo, gradeRepo);
        observableList=new ArrayList<>();
    }


    @Override
    public void add(Observer<Event> obs) {
        observableList.add(obs);
    }

    @Override
    public void remove(Observer<Event> obs) {
        observableList.remove(obs);
    }

    @Override
    public void notifyObservers(Event s) {
        observableList.forEach(obss -> obss.notify(s));
    }


    @Override
    public void addStudent(int id, String name, String email, String group, String professor) throws ValidatorException {
        super.addStudent(id, name, email, group, professor);
        notifyObservers(new StudentEvent(EvType.ADD,new Student(id,name,email,group,professor)));
    }

    @Override
    public void deleteStudent(int id) {
        Student st=this.findOneStudent(id);
        super.deleteStudent(id);
        notifyObservers(new StudentEvent(EvType.REMOVE,st));
    }

    @Override
    public void updateStudent(int id, String name, String email, String group, String professor) throws ValidatorException {
        super.updateStudent(id, name, email, group, professor);
        notifyObservers(new StudentEvent(EvType.UPDATE,new Student(id,name,email,group,professor)));
    }

    @Override
    public void addHomework(int id, int deadline, String description) throws ValidatorException {
        super.addHomework(id, deadline, description);
        notifyObservers(new HomeworkEvent(EvType.ADD,new LabHomework(id,deadline,description)));
    }

    @Override
    public void deleteHomework(int id) {
        LabHomework hw=super.findOneHomework(id);
        super.deleteHomework(id);
        notifyObservers(new HomeworkEvent(EvType.REMOVE,hw));
    }

    @Override
    public void updateHomework(int id, int deadline, String description) throws ValidatorException {
        super.updateHomework(id, deadline, description);
        notifyObservers(new HomeworkEvent(EvType.UPDATE,new LabHomework(id,deadline,description)));
    }

    @Override
    public void extendDeadline(int id, int deadline) throws ValidatorException {
        super.extendDeadline(id, deadline);
        notifyObservers(new HomeworkEvent(EvType.UPDATE,new LabHomework(id,deadline,findOneHomework(id).getDescription())));
    }

    @Override
    public void addGrade(int ids, int idh, int grade, int week,String obs,String type) throws ValidatorException {
        super.addGrade(ids, idh, grade, week,obs,type);
        notifyObservers(new GradeEvent(EvType.ADD,new Grade(ids,idh,grade)));
    }

    @Override
    public void deleteGrade(Pair<Integer, Integer> p) {
        Grade gr=super.findOneGrade(p);
        super.deleteGrade(p);
        notifyObservers(new GradeEvent(EvType.REMOVE,gr));
    }

    @Override
    public void updateGrade(int ids, int idh, int grade, int week) throws ValidatorException {
        super.updateGrade(ids, idh, grade, week);
        notifyObservers(new GradeEvent(EvType.UPDATE,new Grade(ids,idh,grade)));
    }

    @Override
    public Iterable<Student> getAllStudents() {
        return super.getAllStudents();
    }

    @Override
    public int getSizeStudents() {
        return super.getSizeStudents();
    }

    @Override
    public Student findOneStudent(Integer id) {
        return super.findOneStudent(id);
    }




}

