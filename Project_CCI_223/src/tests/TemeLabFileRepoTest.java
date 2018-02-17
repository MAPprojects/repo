package tests;

import domain.TemaLaborator;
import exceptii.EntityNotFoundException;
import exceptii.ValidationException;
import junit.framework.TestCase;

import repository.Repository;
import repository.TemeLabFileRepository;
import validator.TemeLabValidator;
import validator.Validator;

import java.io.File;

public class TemeLabFileRepoTest extends TestCase {
    private String fileName= "D:\\MAP\\lab2\\src\\tests\\temeTest.txt";
    private Repository<TemaLaborator,Integer> repo;
    private Validator<TemaLaborator> val;
    private File file;
    public void setUp(){
        this.val = new TemeLabValidator();
        this.file = new File(fileName);
        this.repo = new TemeLabFileRepository(val,fileName);

        try {
            repo.save(new TemaLaborator(1,"a",1));
            repo.save(new TemaLaborator(2,"b",2));
            repo.save(new TemaLaborator(3,"c",3));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    public void tearDown(){
        file.delete();
    }

    public void testSave(){
        setUp();
        Repository<TemaLaborator,Integer> repo2=new TemeLabFileRepository(val,fileName);
        assertEquals(3,repo2.size());
        assertEquals(3,repo.size());
        try {
            repo.save(new TemaLaborator(1,"a",8));
            assertEquals(3,repo.size());
            repo.save(new TemaLaborator(4,"",0));
        } catch (ValidationException e) {
            assertEquals(2,e.getMesaje().size());
        }
        tearDown();
    }

    public void testDelete(){
        setUp();
        try {
            repo.delete(1);
            assertEquals(2,repo.size());
            repo.delete(37);
        } catch (EntityNotFoundException e) {
            assertEquals("Entitatea dorita a fi stearsa nu a fost gasita in repository",e.getMessage());
        }
        tearDown();
    }

    public void testUpdate(){
        setUp();
        try{
            repo.update(new TemaLaborator(1,"nou",9));
            assertEquals(3,repo.size());
            assertEquals("nou",repo.findOne(1).get().getCerinta());
            assertEquals(9,repo.findOne(1).get().getDeadline());

            repo.update(new TemaLaborator(90,"a",9));
        } catch (EntityNotFoundException e) {
            assertEquals("Entitatea nu a fost gasita in repository",e.getMessage());
        } catch (ValidationException e) {
            e.printStackTrace();
        }

        try{
            repo.update(new TemaLaborator(5,"",0));
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
        } catch (ValidationException e) {
            assertEquals(2,e.getMesaje().size());
        }
        tearDown();
    }

}
