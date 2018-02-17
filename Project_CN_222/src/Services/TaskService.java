package Services;

import Domain.Task;
import Exceptions.RepositoryException;
import Exceptions.ValidationException;
import Repository.GenericRepository;
import Utilities.ListEvent;
import Utilities.ListEventType;

import java.util.ArrayList;
import java.util.List;


public class TaskService extends AbstractService<Task> {
    GenericRepository<Integer, Task> repository;

    public TaskService(GenericRepository<Integer, Task> r) {
        repository = r;
    }

    public GenericRepository<Integer, Task> getRepository() {
        return repository;
    }


    //throws exception if deadline is not good
    private void checkDeadLine(int deadline) {
        if (deadline <= getCurrentWeek()) {
            throw new ValidationException("Deadline must be > current week");
        }
    }

    public void add(int id, String descriere, int deadline) {
        checkDeadLine(deadline);
        System.out.println(getCurrentWeek());
        Task tema = new Task(id,descriere,deadline);
        repository.add(tema);
        ListEvent<Task> ev = createEvent(ListEventType.ADD, tema, getAllAsList());
        notifyObservers(ev);
    }

    public Task remove(int id) {
        Task deleted = repository.delete(id);
        try {
            ListEvent<Task> ev = createEvent(ListEventType.REMOVE, deleted, getAllAsList());
            notifyObservers(ev);
            return deleted;
        } catch (NullPointerException e) {
            throw new ValidationException("tema does not exist");
        }

    }

    public void updateDeadline(int id, int newDeadline) {
        checkDeadLine(newDeadline);
        Task opTema = repository.get(id);
        if (opTema != null) {
            Task tema = new Task(opTema);
            tema.setDeadline(newDeadline);
            repository.update(id, tema);
            ListEvent<Task> ev = createEvent(ListEventType.UPDATE, tema, getAllAsList());
            notifyObservers(ev);
            return;
        }
        throw new RepositoryException("tema nu exista");

    }

    public Iterable<Task> getAll() {
        return repository.getAll();
    }

    public List<Task> getAllAsList() {
        List<Task> all = new ArrayList<>();
        repository.getAll().forEach(all::add);
        return all;
    }

    public Task get(int id) throws RepositoryException {
        return repository.get(id);
    }

    //filtrari teme
    public List<Task> fillterDeadline(int deadline) {
        return fillter(
                getAllAsList(),
                x->x.getDeadline() == deadline,
                (x,y) -> x.getDescriere().compareTo(y.getDescriere())
        );
    }

    public List<Task> fillterDescriereBeginsWith(String character) {
        return fillter(
                getAllAsList(),
                x->x.getDescriere().startsWith(character),
                (x,y) -> x.getDescriere().compareTo(y.getDescriere())
        );
    }

    public List<Task> fillterDeadlineGreater(int deadline) {
        return fillter(
                getAllAsList(),
                x->x.getDeadline() >= deadline,
                (x,y) -> x.getDescriere().compareTo(y.getDescriere())
        );
    }

}
