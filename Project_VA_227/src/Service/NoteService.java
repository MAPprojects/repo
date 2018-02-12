package Service;

import Domain.ExceptionValidator;
import Domain.Note;
import Domain.Validator;
import Repository.GradesDatabaseRepository;
import Repository.NoteRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class NoteService extends AbstractService<Integer, Note> {

    private StudentService studentService;
    private HomeworkService homeworkService;

    public NoteService(Validator validator, GradesDatabaseRepository repository, StudentService studentService,
                       HomeworkService homeworkService) {
        super(validator, repository);
        this.studentService = studentService;
        this.homeworkService = homeworkService;
    }

    @Override
    public void add(Note nota) throws ExceptionValidator, IOException {
        try {
            super.add(nota);

        } catch (ExceptionValidator exceptionValidator) {
            if (exceptionValidator.getMessage().equals("Duplicate Id"))
                throw new ExceptionValidator("Nota data deja");
            else
                throw exceptionValidator;
        }
    }

    @Override
    public void update(Note nota) throws ExceptionValidator, IOException {
        if (nota.getValue() >= this.find(nota.getId()).getValue())
            super.update(nota);
    }

    public void removeByStudent(int id) throws IOException {
        Vector<Integer> iterable = new Vector<>();
        for (Integer ids : this.getAll())
            if (this.find(ids).getStudentID() == id)
                iterable.add(ids);
        for (int index = 0; index < iterable.size(); index++)
            this.remove(iterable.get(index));
    }

    public void removeByHomework(int id) throws IOException {
        Vector<Integer> iterable = new Vector<>();
        for (Integer ids : this.getAll())
            if (this.find(ids).getHomeworkID() == id)
                iterable.add(ids);
        for (int index = 0; index < iterable.size(); index++)
            this.remove(iterable.get(index));
    }


    public List<Note> filterNote(int note) {
        return filterAndSorter(StreamSupport.stream(this.getAll().spliterator(), false)
                        .map(this::find)
                        .collect(Collectors.toList()),
                x -> x.getValue() == note,
                (a, b) -> (this.studentService.find(a.getStudentID()).getNume().compareTo(
                        this.studentService.find(b.getStudentID()).getNume())));
    }

    public List<Note> filterHomework(int homeworkID) {
        return filterAndSorter(StreamSupport.stream(this.getAll().spliterator(), false)
                        .map(this::find)
                        .collect(Collectors.toList()),
                x -> x.getHomeworkID() == homeworkID,
                (a, b) -> (this.studentService.find(a.getStudentID()).getNume().compareTo(
                        this.studentService.find(b.getStudentID()).getNume())));
    }

    public List<Note> filterStudent(int studentID) {
        return filterAndSorter(StreamSupport.stream(this.getAll().spliterator(), false)
                        .map(this::find)
                        .collect(Collectors.toList()),
                x -> x.getStudentID() == studentID,
                (a, b) -> (a.getValue() - b.getValue()));
    }

    public List<Note> filter(Integer studentID, Integer homeworkID, Integer note)
    {
        if (note == null && studentID == null && homeworkID == null)
            return new ArrayList<>();
        if (note == null && studentID == null)
            return filterHomework(homeworkID);
        if (note == null && homeworkID == null)
            return filterStudent(studentID);
        if (studentID == null && homeworkID == null)
            return filterNote(note);
        Predicate<Note> notePredicate = (x) -> x.getValue().equals(note);
        Predicate<Note> studentPredicate = (x) -> x.getStudentID().equals(studentID);
        Predicate<Note> homeworkPredicate = (x) -> x.getHomeworkID().equals(homeworkID);
        if (note == null)
            return filterAndSorter(StreamSupport.stream(this.getAll().spliterator(), false)
                    .map(this::find)
                    .collect(Collectors.toList()),
                    studentPredicate.and(homeworkPredicate),
                    (a, b) -> (a.getValue() - b.getValue()));

        if (studentID == null)
            return filterAndSorter(StreamSupport.stream(this.getAll().spliterator(), false)
                            .map(this::find)
                            .collect(Collectors.toList()),
                    notePredicate.and(homeworkPredicate),
                    (a, b) -> (a.getStudentID() - b.getStudentID()));
        if (homeworkID == null)
            return filterAndSorter(StreamSupport.stream(this.getAll().spliterator(), false)
                            .map(this::find)
                            .collect(Collectors.toList()),
                    studentPredicate.and(notePredicate),
                    (a, b) -> (a.getHomeworkID() - b.getHomeworkID()));
        return filterAndSorter(StreamSupport.stream(this.getAll().spliterator(), false)
                        .map(this::find)
                        .collect(Collectors.toList()),
                studentPredicate.and(notePredicate).and(homeworkPredicate),
                (a, b) -> (a.getValue() - b.getValue()));
    }

    public HomeworkService getHomeworkService() {
        return homeworkService;
    }

    public StudentService getStudentService() {
        return studentService;
    }

    public void delete(Integer studentId, Integer homeworkId)
    {
        ((GradesDatabaseRepository) this.repository).delete(studentId, homeworkId);
        notifyObservers();
    }
}
