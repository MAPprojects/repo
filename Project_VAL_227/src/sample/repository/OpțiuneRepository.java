package sample.repository;
import sample.domain.Opțiune;


public class OpțiuneRepository extends AbstractRepository<String, Opțiune> {

    public OpțiuneRepository() {
    }

    @Override
    public Opțiune add(Opțiune opțiune) throws RepositoryException {
        try {
            return super.add(opțiune);

        } catch (RepositoryException e){
            throw new RepositoryException("Opțiunea cu ID-ul " + opțiune.getID() + " deja există.");
        }
    }

    @Override
    public Opțiune delete(String id) throws RepositoryException {
        try {
            return super.delete(id);

        } catch (RepositoryException e) {
            throw new RepositoryException("Opțiunea cu ID-ul " + id + " nu există.");
        }
    }

    @Override
    public Opțiune update(Opțiune entity) throws Exception {
        try {
            return super.update(entity);

        } catch (RepositoryException e) {
            throw new RepositoryException("Opțiunea cu ID-ul " + entity.getID() + " nu există.");
        }
    }

    @Override
    public Opțiune findOne(String id) throws RepositoryException {
        try {
            return super.findOne(id);

        } catch (RepositoryException e){
            throw new RepositoryException("Opțiunea cu ID-ul " + id + " nu există.");
        }
    }

    @Override
    public Iterable<Opțiune> findAll() {
        return super.findAll();
    }
}
