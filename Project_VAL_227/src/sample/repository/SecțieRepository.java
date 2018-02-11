package sample.repository;
import sample.domain.Secție;


public class SecțieRepository extends AbstractRepository<String, Secție> {

    public SecțieRepository() {
    }

    @Override
    public Secție add(Secție secție) throws RepositoryException {
        try {
            return super.add(secție);

        } catch (RepositoryException e){
            throw new RepositoryException("Secția cu ID-ul " + secție.getID() + " deja există.");
        }
    }

    @Override
    public Secție delete(String id) throws RepositoryException {
        try {
            return super.delete(id);

        } catch (RepositoryException e) {
            throw new RepositoryException("Secția cu ID-ul " + id + " nu există.");
        }
    }

    @Override
    public Secție update(Secție entity) throws Exception {
        try {
            return super.update(entity);

        } catch (RepositoryException e) {
            throw new RepositoryException("Secția cu ID-ul " + entity.getID() + " nu există.");
        }
    }

    @Override
    public Secție findOne(String id) throws RepositoryException {
        try {
            return super.findOne(id);

        } catch (RepositoryException e){
            throw new RepositoryException("Secția cu ID-ul " + id + " deja există.");
        }
    }

    @Override
    public Iterable<Secție> findAll() {
        return super.findAll();
    }
}
