package Service;

import Domain.Assignment;
import Repository.Repository;
import Repository.RepositoryException;
import Utils.*;
import Validator.AssignmentValidator;
import Validator.ValidatorException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AssignmentService implements Observable<Assignment> {
    private AssignmentValidator assignmentValidator;
    private List<Observer<Assignment>> observers = new ArrayList<>();

    public AssignmentService(AssignmentValidator assignmentValidator) {
        this.assignmentValidator = assignmentValidator;
    }

    public void addAssignment(int IDasg,String desc,int deadline) throws RepositoryException, ValidatorException {
        if (Repository.get(Assignment.class,IDasg).isPresent())
            throw new RepositoryException("The object already exists!");
        Assignment asg = new Assignment(IDasg,desc,deadline);
        assignmentValidator.validate(asg);
        if (deadline > 14 || deadline < CurrentWeek.getCurrentWeek())
            throw new RepositoryException("The deadline must be a number between 1 and 14 (the week of the deadline) and greater or equal with the current week.");

        Repository.add(Assignment.class, asg);
        ListEvent<Assignment> listEvent = createListEvent(ListEventType.ADD, asg, Repository.getAll(Assignment.class));
        notifyObservers(listEvent);
    }

    public void deleteAssignment(int IDasg) throws RepositoryException {
        Optional<Assignment> studentOptional = Repository.get(Assignment.class, IDasg);
        if (!studentOptional.isPresent())
            throw new RepositoryException("The assignment to delete doesn't exist.");

        Assignment asg = studentOptional.get();
        Repository.delete(Assignment.class, asg);

        ListEvent<Assignment> listEvent = createListEvent(ListEventType.REMOVE, asg, Repository.getAll(Assignment.class));
        notifyObservers(listEvent);
    }

    public void updateDeadline(int IDasg, int newDeadline, long currentWeek ) throws RepositoryException {
        if (newDeadline > 14 || newDeadline < currentWeek)
            throw new RepositoryException("The deadline must be a number between 1 and 14 (the week of the deadline) and greater or equal with the current week.");

        Optional<Assignment> optional = Repository.get(Assignment.class,IDasg);
        if (!optional.isPresent())
            throw new RepositoryException("The assignment to update doesn't exist.");

        Assignment asg = optional.get();

        if (newDeadline >asg.getDeadline()) {
            Assignment asgNew = new Assignment(asg.getIdAsg(),asg.getDescription(),newDeadline);
            Repository.update(Assignment.class, asg, asgNew);
        }

        ListEvent<Assignment> listEvent = createListEvent(ListEventType.UPDATE, asg, Repository.getAll(Assignment.class));
        notifyObservers(listEvent);
    }

    public boolean exist(int IDasg) {
        Optional<Assignment> asgOptional = null;
        asgOptional = Repository.get(Assignment.class,IDasg);

        if (!asgOptional.isPresent())
            return false;
        return true;
    }

    public Assignment getAsg(int IDasg) throws RepositoryException {
        Optional<Assignment> asgOptional = Repository.get(Assignment.class,IDasg);
        if(!asgOptional.isPresent())
            throw new RepositoryException("The assignment does not exist.");
        else
            return asgOptional.get();
    }

    public List<Assignment> getAllAssignments() throws RepositoryException {
        return Repository.getAll(Assignment.class);
    }

    @Override
    public void addObserver(Observer<Assignment> observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer<Assignment> observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(ListEvent<Assignment> event) {
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
}
