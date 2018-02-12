package repositories;

import entities.Candidate;


public class CandidateRepositoryInMemory extends AbstractRepository<Candidate, Integer> {

    public CandidateRepositoryInMemory() {
        super();


    }

    @Override
    public Iterable<Candidate> nextValues(int pageNumber , int limit) {
        return null;
    }

    @Override
    public void saveData() {
        //nimic
    }

    @Override
    public void loadData() {
        //nimic
    }
}
