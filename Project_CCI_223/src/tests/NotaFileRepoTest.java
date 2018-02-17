package tests;

import domain.Nota;
import exceptii.EntityNotFoundException;
import exceptii.ValidationException;
import junit.framework.TestCase;
import repository.NotaFileRepository;
import repository.Repository;
import validator.NotaValidator;
import validator.Validator;

import java.io.File;

public class NotaFileRepoTest extends TestCase {
    String fileName="D:\\MAP\\lab2\\src\\repository\\noteTest.txt";
    Repository<Nota,Integer> repo;
    File file;

    public void setUp(){
        file=new File(fileName);
        Validator<Nota> val=new NotaValidator();
        repo=new NotaFileRepository(val,fileName);

        try {
            repo.save(new Nota((Integer) 1,(Integer) 1,(Integer) 1,new Float(9.8)));
            repo.save(new Nota((Integer) 2,(Integer) 2,(Integer) 2,new Float(8.8)));
            repo.save(new Nota((Integer) 3,(Integer) 3,(Integer) 3,new Float(7.8)));
            repo.save(new Nota((Integer) 4,(Integer) 4,(Integer) 4,new Float(5.8)));

        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    public void tearDown(){
        file.delete();
    }

    public void testSave(){
        setUp();
        try{
            repo.save(new Nota((Integer) 1,(Integer) 1,(Integer) 1,new Float(9.8)));
            assertEquals(4,repo.size());
            repo.save(new Nota((Integer) 19,(Integer) 1,(Integer) 1,new Float(9.8)));
            assertEquals(4,repo.size());
            repo.save(new Nota((Integer)15,(Integer)2,(Integer)6,new Float(-4.7)));

        } catch (ValidationException e) {
            assertEquals(1,e.getMesaje().size());
        }
        tearDown();
    }

    public void testUpdate(){
        setUp();
        try {
            repo.update(new Nota((Integer)1,(Integer)7,(Integer)7,new Float(6.9)));
            assertEquals(4,repo.size());
            assertEquals((Integer) 7,repo.findOne(1).get().getIdStudent());
            assertEquals((Integer)7,repo.findOne(1).get().getNrTema());
            assertEquals(new Float(6.9),repo.findOne(1).get().getValoare());
            repo.update(new Nota((Integer)1,(Integer)7,(Integer)7,new Float(-6.9)));
        } catch (ValidationException e) {
            assertEquals(1,e.getMesaje().size());
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
        }

        try{
            repo.update(new Nota((Integer)17,(Integer)7,(Integer)7,new Float(6.9)));
        } catch (EntityNotFoundException e) {
            assertEquals("Entitatea nu a fost gasita in repository",e.getMessage());
        } catch (ValidationException e) {
            e.printStackTrace();
        }
        tearDown();
    }

    public void testDelete(){
        setUp();
        try {
            repo.delete(1);
            assertEquals(3,repo.size());
            repo.delete(67);
        } catch (EntityNotFoundException e) {
            assertEquals("Entitatea dorita a fi stearsa nu a fost gasita in repository",e.getMessage());
        }
        tearDown();
    }


}
