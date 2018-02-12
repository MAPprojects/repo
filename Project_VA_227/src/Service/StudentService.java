package Service;

import Domain.ExceptionValidator;
import Domain.Student;
import Domain.Validator;
import Repository.StudentDatabaseRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class StudentService extends AbstractService<Integer, Student> {

    private NoteService noteService = null;

    public StudentService(Validator validator, StudentDatabaseRepository repository) {
        super(validator, repository);
    }

    public List<Student> filterGroup(int grupa) {
        return filterAndSorter(StreamSupport.stream(this.getAll().spliterator(), false)
                        .map(this::find)
                        .collect(Collectors.toList()),
                x -> x.getGrupa() == grupa,
                (a, b) -> (a.getNume().compareTo(b.getNume())));
    }

    public List<Student> filterName(String nume) {
        return filterAndSorter(StreamSupport.stream(this.getAll().spliterator(), false)
                        .map(this::find)
                        .collect(Collectors.toList()),
                x -> x.getNume().toLowerCase().contains(nume.toLowerCase()),
                (a, b) -> (a.getNume().compareTo(b.getNume())));
    }

    public List<Student> filterProfesor(String profesor) {
        return filterAndSorter(StreamSupport.stream(this.getAll().spliterator(), false)
                        .map(this::find)
                        .collect(Collectors.toList()),
                x -> x.getProfesor().toLowerCase().contains(profesor.toLowerCase()),
                (a, b) -> (a.getNume().compareTo(b.getNume())));
    }

    public List<Student> filter(String nume, Integer grupa, String profesor) {
        if (nume == null && grupa == null && profesor == null)
            return new ArrayList<>();
        if (nume == null && grupa == null)
            return filterProfesor(profesor);
        if (nume == null && profesor == null)
            return filterGroup(grupa);
        if (grupa == null && profesor == null)
            return filterName(nume);
        Predicate<Student> namePredicate = x -> x.getNume().toLowerCase().contains(nume.toLowerCase());
        Predicate<Student> grupaPredicate = (x) -> x.getGrupa().equals(grupa);
        Predicate<Student> profesorPredicate = x -> x.getProfesor().toLowerCase().contains(profesor.toLowerCase());
        if (nume == null)
            return filterAndSorter(StreamSupport.stream(this.getAll().spliterator(), false)
                            .map(this::find)
                            .collect(Collectors.toList()),
                    grupaPredicate.and(profesorPredicate),
                    (a, b) -> (a.getNume().compareTo(b.getNume())));

        if (grupa == null)
            return filterAndSorter(StreamSupport.stream(this.getAll().spliterator(), false)
                            .map(this::find)
                            .collect(Collectors.toList()),
                    namePredicate.and(profesorPredicate),
                    (a, b) -> (a.getGrupa() - b.getGrupa()));
        if (profesor == null)
            return filterAndSorter(StreamSupport.stream(this.getAll().spliterator(), false)
                            .map(this::find)
                            .collect(Collectors.toList()),
                    grupaPredicate.and(namePredicate),
                    (a, b) -> (a.getProfesor().compareTo(b.getProfesor())));
        return filterAndSorter(StreamSupport.stream(this.getAll().spliterator(), false)
                        .map(this::find)
                        .collect(Collectors.toList()),
                grupaPredicate.and(namePredicate).and(profesorPredicate),
                (a, b) -> (a.getNume().compareTo(b.getNume())));
    }

    public void setNoteService(NoteService noteService) {
        this.noteService = noteService;
    }

    @Override
    public void remove(Integer item) throws IOException {
        noteService.filterStudent(item).forEach(note -> {
            noteService.delete(note.getStudentID(), note.getHomeworkID());
        });
        super.remove(item);
        noteService.notifyObservers();
    }

    @Override
    public void add(Student item) throws ExceptionValidator, IOException {
        super.add(item);
        noteService.notifyObservers();
    }
}