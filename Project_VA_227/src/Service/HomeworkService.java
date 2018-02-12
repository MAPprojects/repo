package Service;

import Domain.ExceptionValidator;
import Domain.Homework;
import Domain.Validator;
import Repository.HomeworkDatabaseRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class HomeworkService extends AbstractService<Integer, Homework> {
    private NoteService noteService = null;

    public HomeworkService(Validator validator, HomeworkDatabaseRepository repository) {
        super(validator, repository);
    }

    public List<Homework> filterDeadline(int deadline) {
        return filterAndSorter(StreamSupport.stream(this.getAll().spliterator(), false)
                        .map(this::find)
                        .collect(Collectors.toList()),
                x -> x.getDeadline() == deadline,
                (a, b) -> (a.getTask().compareTo(b.getTask())));
    }

    public List<Homework> filterTask(String task) {
        return filterAndSorter(StreamSupport.stream(this.getAll().spliterator(), false)
                        .map(this::find)
                        .collect(Collectors.toList()),
                x -> x.getTask().toLowerCase().contains(task.toLowerCase()),
                (a, b) -> (a.getDeadline() - b.getDeadline()));
    }

    public List<Homework> filterTaskAndDeadline(String task, int deadline) {
        return filterAndSorter(StreamSupport.stream(this.getAll().spliterator(), false)
                        .map(this::find)
                        .collect(Collectors.toList()),
                x -> x.getTask().toLowerCase().contains(task.toLowerCase()) && x.getDeadline() == deadline,
                (a, b) -> (a.getTask().compareTo(b.getTask())));
    }

    public List<Homework> filter(Integer deadline, String task)
    {
        if (deadline == null && task == null)
            return new ArrayList<>();
        if (deadline == null)
            return filterTask(task);
        if (task == null)
            return filterDeadline(deadline);
        return filterTaskAndDeadline(task, deadline);
    }

    public void setNoteService(NoteService noteService) {
        this.noteService = noteService;
    }

    @Override
    public void remove(Integer item) throws IOException {
        noteService.filterHomework(item).forEach(note -> {
            noteService.delete(note.getStudentID(), note.getHomeworkID());
        });
        super.remove(item);
        noteService.notifyObservers();
    }

    @Override
    public void add(Homework item) throws ExceptionValidator, IOException {
        super.add(item);
        noteService.notifyObservers();
    }
}
