package Service;

import Domain.Student;
import Repository.Repository;
import Repository.RepositoryException;
import Utils.*;
import Validator.StudentValidator;
import Validator.ValidatorException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StudentService implements Observable<Student> {
    private StudentValidator studentValidator;
    private List<Observer<Student>> observers = new ArrayList<>();

    public StudentService(StudentValidator studentValidator) {
        this.studentValidator = studentValidator;
    }

    @Override
    public void addObserver(Observer<Student> observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer<Student> observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(ListEvent<Student> event) {
        observers.forEach(obs->obs.notifyEvent(event));
    }

    private <E> ListEvent<E> createListEvent(ListEventType listEventType, final E elem, final Iterable<E> iterable) {
        return new ListEvent<E>(listEventType) {
            @Override
            public Iterable<E> getList() {
                return iterable;
            }

            @Override
            public E getElement() {
                return elem;
            }
        };
    }

    public void addStudent(int IDstudent, String name, int group, String email, String professor ) throws ValidatorException, RepositoryException {
        if (exist(IDstudent))
            throw new RepositoryException("The object already exists! ");
        Student student = new Student(IDstudent,name,group,email,professor);
        studentValidator.validate(student);
        Repository.add(Student.class, student);
        CheckUser.writeUser(IDstudent,email);
        ListEvent<Student> listEvent = createListEvent(ListEventType.ADD, student, Repository.getAll(Student.class));
        notifyObservers(listEvent);
    }

    public void updateStudent(int IDstudent, String name, int group, String email, String professor) throws RepositoryException, ValidatorException {
        Optional<Student> studentOptional = Repository.get(Student.class, IDstudent);
        if (!studentOptional.isPresent())
            throw new RepositoryException("The student to update doesn't exist.");

        Student student = studentOptional.get();

        Student student1 = new Student(
                IDstudent,
                name.equals("-") ? student.getName() : name,
                group==0 ? student.getGroup() :group,
                email.equals("-") ? student.getEmail() : email,
                professor.equals("-") ? student.getProfessor() : professor);

        studentValidator.validate(student1);
        Repository.update(Student.class, student,student1);

        ListEvent<Student> listEvent = createListEvent(ListEventType.UPDATE, student1, Repository.getAll(Student.class));
        notifyObservers(listEvent);
    }

    public void deleteStudent(int IDstudent) throws RepositoryException, IOException {
        new DeleteFile().deleteFile(String.valueOf(IDstudent).concat(".txt"));

        Optional<Student> studentOptional = Repository.get(Student.class, IDstudent);
        if (!studentOptional.isPresent())
            throw new RepositoryException("The student to delete doesn't exist.");

        Student student = studentOptional.get();

        Repository.delete(Student.class, student);
        CheckUser.deleteUser(IDstudent);

        ListEvent<Student> listEvent = createListEvent(ListEventType.REMOVE, student, Repository.getAll(Student.class));
        notifyObservers(listEvent);
    }

    public boolean exist(int IDstudent) {
        Optional<Student> studentOptional = null;
        studentOptional = Repository.get(Student.class, IDstudent);

        if (!studentOptional.isPresent())
            return false;

        return true;
    }

    public Student getStd(int IDstd) throws RepositoryException {
        Optional<Student> stdOptional = Repository.get(Student.class, IDstd);
        if(!stdOptional.isPresent())
            throw new RepositoryException("The assignment does not exist.");
        else
            return stdOptional.get();
    }

    public List<Student> getAllStudents() throws RepositoryException {
        return Repository.getAll(Student.class);
    }


}
