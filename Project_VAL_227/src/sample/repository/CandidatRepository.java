package sample.repository;
import sample.domain.Candidat;


public class CandidatRepository extends AbstractRepository<String, Candidat> {

    public CandidatRepository() {
    }

    @Override
    public Candidat add(Candidat candidat) throws RepositoryException {
        try {
            return super.add(candidat);

        } catch (RepositoryException e) {
            throw new RepositoryException("Candidatul cu CNP-ul " + candidat.getID() + " deja există.");
        }
    }

    @Override
    public Candidat delete(String cnp) throws RepositoryException {
        try {
            return super.delete(cnp);

        } catch (RepositoryException e) {
            throw new RepositoryException("Candidatul cu CNP-ul " + cnp + " nu există.");
        }
    }

    @Override
    public Candidat update(Candidat candidat) throws Exception {
        try {
            return super.update(candidat);

        } catch (RepositoryException e) {
            throw new RepositoryException("Candidatul cu CNP-ul " + candidat.getID() + " nu există.");
        }
    }

    @Override
    public Candidat findOne(String cnp) throws RepositoryException {
        try {
            return super.findOne(cnp);

        } catch (RepositoryException e){
            throw new RepositoryException("Candidatul cu CNP-ul " + cnp + " nu există.");
        }
    }

    @Override
    public Iterable<Candidat> findAll() {
        return super.findAll();
    }


}
