package controller;

import entities.Student;
import exceptions.AbstractValidatorException;
import exceptions.StudentServiceException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import observer_utils.AbstractObserver;
import observer_utils.StudentEvent;
import observer_utils.StudentEventType;
import services.StudentService;

import java.io.IOException;
import java.util.List;

public class StudentController extends AbstractObserver<StudentEvent> {
    private StudentService studentService;
    private ObservableList<Student> studentModel;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
        this.studentService.addObserver(this);
        studentModel = FXCollections.observableArrayList();
        populateModel();
    }

    public void addStudent(String nume, String grupa, String email, String prof) throws IOException, AbstractValidatorException, StudentServiceException {
        studentService.addStudent(nume, grupa, email, prof);
    }

    public void removeStudent(Student student) throws IOException, AbstractValidatorException, StudentServiceException {
        studentService.deleteStudent(student);
    }

    public void updateStudent(String actualStudentId, String nume, String grupa, String email, String prof) throws IOException, StudentServiceException, AbstractValidatorException {
        studentService.updateStudent(actualStudentId, nume, grupa, email, prof);
    }

    public ObservableList<Student> getStudentModel() {
        return studentModel;
    }

    @Override
    public void updateOnEvent(StudentEvent event) {
        StudentEventType studentEventType = event.getTipEvent();
        if (studentEventType.equals(StudentEventType.ADD)) {
            studentModel.add(event.getData());
        } else if (studentEventType.equals(StudentEventType.REMOVE)) {
            final Student[] student = new Student[1];
            studentModel.forEach(s -> {
                if (s.getId().equals(event.getData().getId())) {
                    student[0] = s;
                }
            });
            studentModel.remove(student[0]);
        } else if (studentEventType.equals(StudentEventType.UPDATE)) {
            final Student[] student = new Student[1];
            studentModel.forEach(x -> {
                if (x.getId().equals(event.getData().getId())) {
                    student[0] = x;
                }
            });
            String nume = event.getData().getNume();
            String email = event.getData().getEmail();
            String prof = event.getData().getCadruDidacticIndrumator();
            String grupa = event.getData().getGrupa();
            studentModel.remove(student[0]);
            studentModel.add(new Student(event.getData().getId(), nume, grupa, email, prof));
        }
    }

    private void populateModel() {
        List<Student> students = studentService.findAllStudents();
        students.forEach(x -> studentModel.add(x));
    }
}
