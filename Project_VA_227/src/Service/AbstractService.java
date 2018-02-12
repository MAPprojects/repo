package Service;

import Domain.ExceptionValidator;
import Domain.Validator;
import Repository.AbstractRepository;
import Repository.HasID;
import Utils.Observable;
import Utils.Observer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;


public abstract class AbstractService<ID, E extends HasID<ID>> implements IService<ID, E>,
        Observable<E> {

    private Validator validator;
    protected AbstractRepository<ID, E> repository;
    private List<Observer<E>> observers;

    AbstractService(Validator validator, AbstractRepository<ID, E> repository) {
        this.validator = validator;
        this.repository = repository;
        observers = new ArrayList<>();
    }

    @Override
    public void add(E item) throws ExceptionValidator, IOException {
        Iterable<ID> iterable = this.getAll();
        for (ID id : iterable)
            if (id == item.getId())
                throw new ExceptionValidator("Duplicate Id");
        this.validator.Validate(item);
        this.repository.save(item);
        this.repository.saveData();
        notifyObservers();
    }

    @Override
    public void remove(ID item) throws IOException {
        this.repository.delete(item);
        this.repository.saveData();
        notifyObservers();
    }

    @Override
    public void update(E item) throws ExceptionValidator, IOException {
        this.validator.Validate(item);
        this.repository.update(item.getId(), item);
        this.repository.saveData();
        notifyObservers();
    }

    @Override
    public E find(ID item) {
        Optional<E> result = this.repository.findOne(item);
        return result.orElse(null);
    }

    @Override
    public <E> List<E> filterAndSorter(List<E> lista,   Predicate<E> p, Comparator<E> c) {
        return lista
                .stream()
                .filter(p)
                .sorted(c)
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<ID> getAll() {
        return this.repository.findAll();
    }

    @Override
    public void addObserver(Observer<E> e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer<E> e) {
        observers.remove(e);
    }

    @Override
    public void notifyObservers() {
        observers.forEach(x -> x.notifyOnEvent(this));
    }

    @Override
    public Integer getSize() {
        return this.repository.size();
    }
}
