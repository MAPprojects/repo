package tests;

import domain.Nota;
import exceptii.ValidationException;
import junit.framework.TestCase;
import repository.NotaRepository;
import repository.Repository;
import validator.NotaValidator;
import validator.Validator;

public class NotaRepositoryTest extends TestCase {
    Repository<Nota,Integer> repo;
    public void setUp(){
        Validator<Nota> val=new NotaValidator();
        repo=new NotaRepository(val);
    }

    public void testSave(){
        setUp();
        try {
            repo.save(new Nota((Integer)1,(Integer)1,(Integer)1,new Float(4.7)));
            assertEquals(1,repo.size());
            repo.save(new Nota((Integer)2,(Integer)2,(Integer)2,new Float(7.7)));
            assertEquals(2,repo.size());
            repo.save(new Nota((Integer)3,(Integer)1,(Integer)1,new Float(4.7)));
            assertEquals(2,repo.size());
            repo.save(new Nota((Integer)1,(Integer)1,(Integer)1,new Float(0.3)));
        } catch (ValidationException e) {
            assertEquals(2,repo.size());
        }
    }

}
