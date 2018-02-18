package Service;

import Domain.Assignment;
import Domain.Grade;
import Repository.Repository;
import Repository.RepositoryException;
import Utils.*;
import Validator.ValidatorException;

import javax.mail.Address;
import javax.mail.internet.InternetAddress;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GradeService implements Observable<Grade> {
    private StudentService studentService;
    private AssignmentService assignmentService;
    private GradeWriter gradeWriter = GradeWriter.getInstance();
    private List<Observer<Grade>> observers = new ArrayList<>();

    private void validateGrade(Grade object, long currentWeek) throws ValidatorException, RepositoryException {
        if(currentWeek < 1 || currentWeek > 14)
            throw new ValidatorException("The week must be an integer between 1 and 14.");
        if(object.getValue()<1 || object.getValue()>10)
            throw new ValidatorException("The value of the grade must be between 1 and 10.");
        if (!studentService.exist(object.getStd().getIdStudent()))
            throw new ValidatorException("The student to whom is assigned the grade must exist.");
        if (!assignmentService.exist(object.getAsg().getIdAsg()))
            throw new ValidatorException("The assignment graded must exist.");
    }

    private int calculateGrade(int value, long currentWeek, int deadline) {
        if (currentWeek-deadline>2)
            return 1;
        if (currentWeek-deadline==2)
            return Math.max(value-4,1);
        if (currentWeek-deadline==1)
            return Math.max(value-2,1);
        return value;
    }

    private void setValueAndNotes(Grade grade, long currentWeek) {
        int value=grade.getValue();
        int deadline = grade.getAsg().getDeadline();
        int newValue = calculateGrade(value,currentWeek,deadline);
        grade.setValue(newValue);
        String notes = grade.getNotes();
        if (newValue == value)
            notes+=" There was no penalty for deadline. ";
        else
            notes+="The deadline of the assignment was not respected. The assignment was " + String.valueOf(currentWeek-deadline) + " week(s) late.";
    }

    private void inCaseOfDelete() throws RepositoryException {
        Repository.getAll(Grade.class).removeIf(
                grade ->  !studentService.exist(grade.getStd().getIdStudent()) ||
                        !assignmentService.exist(grade.getAsg().getIdAsg())
        );
    }

    private Optional<Grade> getGrade(int IDstd, int IDasg) throws RepositoryException {
        for (Grade grade : Repository.getAll(Grade.class))
            if(grade.getStd().getIdStudent()==IDstd && grade.getAsg().getIdAsg()==IDasg)
                return Optional.of(grade);
        return Optional.empty();
    }

    public GradeService(StudentService studentService, AssignmentService assignmentService) {
        this.studentService = studentService;
        this.assignmentService = assignmentService;
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

    public void addGrade(int IDstudent, int IDasg, long currentWeek, int value, String notes) throws Exception{
        if (getGrade(IDstudent,IDasg).isPresent())
            throw new RepositoryException("The object already exists!");

        inCaseOfDelete();
        Assignment asg = assignmentService.getAsg(IDasg);
        int newValue = calculateGrade(value,currentWeek,asg.getDeadline());
        if (newValue == value)
            notes+=" There was no penalty for deadline. ";
        else
            notes+="The deadline of the assignment was not respected. The assignment was " + String.valueOf(currentWeek-asg.getDeadline()) + " week(s) late.";
        Grade grade = new Grade(
                studentService.getStd(IDstudent),
                asg,
                newValue,
                notes
        );
        validateGrade(grade,currentWeek);
        Repository.add(Grade.class,grade);
        gradeWriter.setWriter(String.valueOf(IDstudent).concat(".txt"),IDasg,newValue,asg.getDeadline(),currentWeek);
        gradeWriter.setEvent("Grade added");
        gradeWriter.write(notes);
        String content = new String(Files.readAllBytes(Paths.get(String.valueOf(IDstudent).concat(".txt"))));
        Address sender = new InternetAddress("ariadnatestemail@gmail.com", "ariadnatestemail");
        Address[] recipients = {new InternetAddress(studentService.getStd(IDstudent).getEmail())};
        String subject = "Grade summary";
        String body = content;
        new Email().sendUsingSmtps(sender, recipients, subject, body);
        ListEvent<Grade> listEvent = createListEvent(ListEventType.ADD, grade, Repository.getAll(Grade.class));
        notifyObservers(listEvent);
    }

    public void updateGrade(int IDstd, int IDasg, long currentWeek, int value, String notes) throws Exception{
        inCaseOfDelete();
        Optional<Grade> gradeOptional = getGrade(IDstd,IDasg);
        if(!gradeOptional.isPresent())
            throw new RepositoryException("There is no grade for this student on this assignment.");
        Grade grade = gradeOptional.get();
        Assignment asg = assignmentService.getAsg(IDasg);
        int newValue = calculateGrade(value,currentWeek,asg.getDeadline());
        if (newValue == value)
            notes+=" There was no penalty for deadline. ";
        else
            notes+="The deadline of the assignment was not respected. The assignment was " + String.valueOf(currentWeek-asg.getDeadline()) + " week(s) late.";
        if (grade.getValue()>=newValue)
            return;
        Grade newGrd = new Grade(grade.getStd(),grade.getAsg(),newValue,grade.getNotes());
        Repository.update(Grade.class,grade,newGrd);
        gradeWriter.setWriter(String.valueOf(IDstd).concat(".txt"),IDasg,newValue,asg.getDeadline(),currentWeek);
        gradeWriter.setEvent("Grade updated");
        gradeWriter.write(notes);
        String content = new String(Files.readAllBytes(Paths.get(String.valueOf(IDstd).concat(".txt"))));
        Address sender = new InternetAddress("ariadnatestemail@gmail.com", "ariadnatestemail");
        Address[] recipients = {new InternetAddress(studentService.getStd(IDstd).getEmail())};
        String subject = "Grade summary";
        String body = content;
        new Email().sendUsingSmtps(sender, recipients, subject, body);
        ListEvent<Grade> listEvent = createListEvent(ListEventType.UPDATE, grade, Repository.getAll(Grade.class));
        notifyObservers(listEvent);

    }

    public List<Grade> getAll() throws RepositoryException {
        inCaseOfDelete();
        return Repository.getAll(Grade.class);
    }


    @Override
    public void addObserver(Observer<Grade> observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer<Grade> observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(ListEvent<Grade> event) {
        observers.forEach(obs->obs.notifyEvent(event));
    }
}
