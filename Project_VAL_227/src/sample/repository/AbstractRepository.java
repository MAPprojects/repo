package sample.repository;

import sample.domain.HasID;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Vector;

public abstract class AbstractRepository<ID, E extends HasID<ID>> implements Repository<ID, E> {
    protected Map<ID, E> entities;


    protected AbstractRepository() {
        this.entities = new HashMap<ID, E>();
    }

    /**
     * @return numărul de entități stocate
     */
    @Override
    public long size() {
        return entities.size();
    }

    /**
     * @param obiectul de adăugat
     * @return obiectul adăugat
     * @throws RepositoryException dacă obiectul deja există (ID duplicat)
     */
    @Override
    public E add(E entity) throws RepositoryException {
            if (entities.get(entity.getID()) != null)
                throw new RepositoryException("Entitatea cu ID-ul " + entity.getID() + " există deja.");

            entities.put(entity.getID(), entity);
            return entity;
    }

    /**
     * @param id-ul obiectului
     * @return obiectul șters
     * @throws RepositoryException dacă nu s-a găsit ID-ul
     */
    @Override
    public E delete(ID id) throws RepositoryException {
        if (entities.get(id) == null)
            throw new RepositoryException("Entitatea cu ID-ul " + id + " nu există.");

        return entities.remove(id);
    }

    @Override
    public E update(E entity) throws Exception {
        if (entities.get(entity.getID()) == null)
            throw new RepositoryException("Entitatea cu ID-ul " + entity.getID() + " nu există.");

        entities.put(entity.getID(), entity);
        return entity;
    }

    /**
     * @param id-ul obiectului
     * @return obiectul cu ID-ul căutat
     * @throws RepositoryException dacă nu s-a găsit obiectul
     */
    @Override
    public E findOne(ID id) throws RepositoryException {
        if (entities.get(id) == null   )
            throw new RepositoryException("Entitatea cu ID-ul " + id + " nu există.");

        return entities.get(id);
    }

    /**
     * @return un vector cu toate obiectele stocate
     */
    @Override
    public Iterable<E> findAll() {
        return new Vector<E>(entities.values());
    }

    @Override
    public Optional<E> deleteOptional(ID id) {
        return Optional.ofNullable(entities.remove(id));
    }

    public Optional<E> updateOptional(E entity) throws Exception {
        if (entities.containsKey(entity.getID())) {
            entities.put(entity.getID(), entity);
            return Optional.empty();
        }
        return Optional.of(entity);
    }

}
