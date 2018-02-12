package tests;

import entities.Candidate;
import org.junit.Before;
import org.junit.Test;
import repositories.AbstractRepository;
import repositories.CandidateRepositoryInMemory;

import static org.junit.Assert.*;

public class CandidateRepositoryInMemoryTest {
    private CandidateRepositoryInMemory repo;
    @Before
    public void setUp() throws Exception {
        repo = new CandidateRepositoryInMemory();
    }

    @Test
    public void size() throws Exception {
        assertTrue(repo.size()==0);
        repo.save(new Candidate(1,"dragos","072355","ste@yahoo.com"));
        assertTrue(repo.size()==1);

    }

    @Test
    public void save() throws Exception {
        repo.save(new Candidate(1,"dragos","072355","ste@yahoo.com"));
        repo.save(new Candidate(2,"dragos","072355","ste@yahoo.com"));
        assertTrue(repo.size()==2);

    }

    @Test
    public void delete() throws Exception {
        repo.save(new Candidate(1,"dragos","072355","ste@yahoo.com"));
        repo.save(new Candidate(2,"dragos","072355","ste@yahoo.com"));
        assertTrue(repo.size()==2);
        repo.delete(1);
        assertTrue(repo.size()==1);

    }

    @Test
    public void update() throws Exception {
    }

    @Test
    public void findOne() throws Exception {
    }

    @Test
    public void findAll() throws Exception {
    }

}