package tests;

import domain.Student;
import exceptii.EntityNotFoundException;
import exceptii.ValidationException;
import junit.framework.TestCase;
import repository.AbstractRepository;
import validator.StudentValidator;
import validator.Validator;

public class AbstractRepositoryTest extends TestCase {
    Validator<Student> val;
    AbstractRepository<Student,Integer> repoSt;

    public void setUp(){
        val=new StudentValidator();
        repoSt=new AbstractRepository<Student,Integer>(val);
        try {
            repoSt.save(new Student(1,"a",1,"a","a"));
            assertEquals(1,repoSt.size());
            repoSt.save(new Student(2,"b",2,"b","b"));
            assertEquals(2,repoSt.size());
        } catch (ValidationException e) {
            assertEquals(1,0);
        }
    }

    public void testSave(){
        try {
            repoSt.save(new Student(-9,"",1,"1","1"));
        } catch (ValidationException e) {
            assertEquals(2,e.getMesaje().size());
        }
        try {
            repoSt.save(new Student(3,"q",-1,"",""));
        } catch (ValidationException e) {
            assertEquals(3,e.getMesaje().size());
        }
    }

    public void testDelete(){
        try {
            repoSt.delete(1);
            assertEquals(1,repoSt.size());
        } catch (EntityNotFoundException e) {
            assertEquals(1,0);
        }

        try {
            repoSt.delete(45);
        } catch (EntityNotFoundException e) {
            assertEquals("Entitatea dorita a fi stearsa nu a fost gasita in repository",e.getMessage());
        }

    }

    public void testUpdate(){
        try{
            repoSt.update(new Student(1,"nou",78,"nou","nou"));
            assertEquals(2,repoSt.size());
            assertEquals("nou",repoSt.findOne(1).get().getNume());
            assertEquals("nou",repoSt.findOne(1).get().getEmail());
            assertEquals("nou",repoSt.findOne(1).get().getCadru_didactic_indrumator_de_laborator());
            assertEquals(78,repoSt.findOne(1).get().getGrupa());

        }catch (EntityNotFoundException e){
            assertEquals(1,0);

        }catch (ValidationException e){
            assertEquals(1,0);
        }

        try{
            repoSt.update(new Student(95,"nou",78,"nou","nou"));
        }catch (ValidationException e){
            assertEquals(1,0);
        }catch (EntityNotFoundException e){
            assertEquals("Entitatea nu a fost gasita in repository",e.getMessage());
        }

        try{
            repoSt.update(new Student(95,"",78,"","nou"));
        }catch (ValidationException e){
            assertEquals(2,e.getMesaje().size());
        }catch (EntityNotFoundException e){
            assertEquals(1,0);
        }
    }
}
