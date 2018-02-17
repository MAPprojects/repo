package Service;

import Domain.Project;
import Repository.DBAbstractRepository;
import Statistics.FilterAndSorter;
import Statistics.Sorter;
import Utils.ListEvent;
import Utils.ListEventType;
import Utils.Observable;
import Utils.Observer;
import Validate.ValidationException;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static Utils.Utils.createEvent;

public class ProjectService extends AbstractService<Project, UUID> implements Observable<Project> {

    private ArrayList<Observer<Project>> observers = new ArrayList<>();
    //private DBAbstractRepository<UUID, Project> repository;

    /**
     * StudentService constructor
     */
    public ProjectService(DBAbstractRepository<UUID, Project> repository) {
        super(repository);
    }

    /**
     * updates an existing project
     * @param id
     * @param deadline
     */
    public Optional<Project> updateProject(UUID id, String deadline) throws FileNotFoundException, UnsupportedEncodingException, ValidationException {
        Optional<Project> toUpdate = repository.getEntity(id);
        if(!toUpdate.isPresent()){
            throw new ValidationException("project does not exist!");
        }
        if(deadline.isEmpty()){
            throw new ValidationException("projects attributes must not be empty");
        }
        if(toUpdate.get().getDeadline()<=Integer.parseInt(deadline)){
            return Optional.empty();
        }
        toUpdate.get().setDeadline(Integer.parseInt(deadline));
        Optional<Project> result = repository.update(toUpdate.get());
        if(result.isPresent()){
            ListEvent<Project> listEvent = createEvent(ListEventType.UPDATE, result.get(), this.repository.getAll());
            notifyObservers(listEvent);
        }
        return toUpdate;
    }

    /**
     * saves a new project
     * @param ID
     * @param description
     * @param deadline
     * @return
     * @throws ValidationException
     */
    public Optional<Project> saveProject(String ID, String description, String deadline) throws ValidationException, FileNotFoundException, UnsupportedEncodingException {
        if(deadline.isEmpty() || ID.isEmpty() || description.isEmpty()){
            throw new ValidationException("projects attributes must not be empty");
        }
        Project entity = new Project(UUID.fromString(ID), description, Integer.parseInt(deadline));
        Optional<Project> saved = repository.save(entity);
        if(!saved.isPresent()){
            ListEvent<Project> listEvent = createEvent(ListEventType.ADD, saved.get(), this.getAllProjects());
            notifyObservers(listEvent);
        }
        return saved;
    }

    public Optional<Project> saveProject(String description, String deadline) throws ValidationException, FileNotFoundException, UnsupportedEncodingException {
        if(deadline.isEmpty() || description.isEmpty()){
            throw new ValidationException("projects attributes must not be empty");
        }
        Project entity = new Project(description, Integer.parseInt(deadline));
        Optional<Project> saved = repository.save(entity);
        if(!saved.isPresent()){
            ListEvent<Project> listEvent = createEvent(ListEventType.ADD, entity, this.getAllProjects());
            notifyObservers(listEvent);
        }
        return saved;
    }

    /**
     * gets an existing project
     * @param id
     * @return
     */
    public Optional<Project> getProject(UUID id) throws ValidationException {
        Optional<Project> project = repository.getEntity(id);
        if(!project.isPresent()){
            throw new ValidationException("project does not exist!");
        }
        return project;
    }

    /**
     * deletes an existing project
     * @param id
     * @return Project - deleted entity
     */
    public Optional<Project> deleteProject(UUID id){
        try {
            Optional<Project> result = repository.delete(id);
            if(result.isPresent()){
                ListEvent<Project> listEvent = createEvent(ListEventType.REMOVE, result.get(), this.repository.getAll());
                notifyObservers(listEvent);
            }
            return result;
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    /**
     * gets all projects
     * @return Iterable<E> all projects values
     */
    public List<Project> getAllProjects(){
        return StreamSupport.stream(this.repository.getAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    /**
     * gets number of projects
     * @return long number of projects
     */
    public long projectsNumber(){
        return repository.size();
    }

    /**
     * sorts projects by id
     * @return sorted list of projects
     */
    public List<Project> sortProjectsByID(){
        Comparator<Project> projectComparator = (p1, p2)->p1.getID().compareTo(p2.getID());
        return Sorter.sort(this.getAll(), projectComparator);
    }

    /**
     * sorts projects by deadline
     * @return sorted list of projects
     */
    public List<Project> sortProjectsByDeadline(){
        Comparator<Project> projectComparator = (p1, p2)->p1.getDeadline()-p2.getDeadline();
        return Sorter.sort((List<Project>)this.getAll(), projectComparator);
    }

    public List<Project> sortProjectsByDescription(){
        Comparator<Project> projectComparator = (p1, p2)->p1.getDescription().compareTo(p2.getDescription());
        return Sorter.sort((List<Project>)this.getAll(), projectComparator);
    }

    public List<Project> sortProjectsByDeadlineAndDescription(){
        Comparator<Project> projectComparator = (p1, p2)->p1.getDescription().compareTo(p2.getDescription());
        return Sorter.sort(this.sortProjectsByDeadline(), projectComparator);
    }

    /**
     * filters projects alphabetically by given deadline
     * @param deadline given deadline
     * @return filtered and sorted list
     */
    public List<Project> filterByDeadlineAlphabetically(int deadline){
        Predicate<Project> p = x->x.getDeadline()==deadline;
        Comparator<Project> c = (x, y)->x.getID().compareTo(y.getID());
        return FilterAndSorter.filterAndSort(this.getAll(), p, c);
    }

    public List<Project> getProjectsPage(int pageNumber){
        return StreamSupport.stream(repository.getPage(pageNumber).spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public void addObserver(Observer<Project> observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer<Project> observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(ListEvent<Project> event) {
        observers.forEach(o->o.update(event));
    }


}
