package Controller;

import Controllers.Service;
import Entities.Student;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventType;
import utils.CRUDEventType;
import utils.Observer;
import utils.Observable;
import utils.StudentEvent;

import javax.swing.event.DocumentEvent;

public class StudentController implements Observer<StudentEvent> {
    private Service service;
    private ObservableList<Student> StudentModel;

    public StudentController(Service service) {
        this.service = service;
        //service.addObserverStudent(this);
        StudentModel = FXCollections.observableArrayList();
        populateList();
    }

    private void populateList() {
        Iterable<Student> st = service.getAllStudents();
        st.forEach(x->StudentModel.add(x));
    }

    public void addStudent(Integer id, String nume, String email, String grupa, String prof, Integer note, Integer nr, Double medie, Boolean val, Boolean fint) {
        service.save_student(id, nume, email, grupa, prof, note, nr, medie, val, fint);
        //service.notifyObserversStudent(new StudentEvent(CRUDEventType.ADD, new Student(id, nume, email, grupa, prof)));
    }

    public void updateStudent(Integer id, String nume, String email, String grupa, String prof, Integer note, Integer nr, Double medie, Boolean val, Boolean fint) {
        service.update_student(id, nume, email, grupa, prof, note, nr, medie, val, fint);
        //service.notifyObserversStudent(new StudentEvent(CRUDEventType.UPDATE, new Student(id, nume, email, grupa, prof)));
    }

    public void deleteStudent(Integer id) {
        Student st = service.findStudent(id);
        service.delete_student(id);
        //service.notifyObserversStudent(new StudentEvent(CRUDEventType.DELETE, st));
    }

    public ObservableList<Student> getStudentModel() {
        return StudentModel;
    }

    @Override
    public void notifyOnEvent(StudentEvent stevent) {
        StudentModel.clear();
        Iterable<Student> st = service.getAllStudents();
        st.forEach(x->StudentModel.add(x));
    }
}
