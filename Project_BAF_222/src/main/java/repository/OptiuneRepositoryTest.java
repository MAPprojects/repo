package repository;

import entities.Optiune;
import junit.framework.TestCase;
import validator.OptiuneValidator;
import validator.Validator;

public class OptiuneRepositoryTest extends TestCase {
    protected OptiuneRepository repository;
    protected Optiune o1,o2,o3,o4;

    protected void setUp(){
        Validator<Optiune> validator=new OptiuneValidator();
        repository=new OptiuneRepository(validator);
        o1=new Optiune(1,2,3);
        o2=new Optiune(1,2,3);
        o3=new Optiune(2,2,3);
        o4=new Optiune(2,2,4);


    }

    public void testUp(){
        repository.save(o1);
        try{
            repository.save(o2);
            assertTrue(false);
        }
        catch (RepositoryException e){ }
        try{
            repository.save(o3);
            assertTrue(false);
        }
        catch (RepositoryException e){ }
        repository.save(o4);
    }
}
