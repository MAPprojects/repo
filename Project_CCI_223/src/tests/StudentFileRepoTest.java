package tests;

import domain.Student;
import exceptii.EntityNotFoundException;
import exceptii.ValidationException;
import junit.framework.TestCase;
import repository.Repository;
import repository.StudentFileRepository;
import validator.StudentValidator;
import validator.Validator;

import java.io.File;

public class StudentFileRepoTest extends TestCase {
    private String fileName= "D:\\MAP\\lab2\\src\\tests\\studentiTest.txt";
    private Repository<Student,Integer> repo;
    private Validator<Student> val;
    private File file;

    public void setUp()  {
        this.val = new StudentValidator();
        this.file = new File(fileName);
        this.repo = new StudentFileRepository(val,fileName);;

        Student st1=new Student(1,"a",15,"a","a");
        Student st2=new Student(2,"b",16,"b","b");
        Student st3=new Student(3,"c",17,"c","c");
        try{
            repo.save(st1);
            repo.save(st2);
            repo.save(st3);
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    public void tearDown()  {
        file.delete();
    }

    public void testSave()  {
        setUp();
        Repository<Student,Integer> repo2=new StudentFileRepository(val,fileName);
        assertEquals(3,repo2.size());
        try{
            repo.save(new Student(1,"a",15,"a","a"));
            assertEquals(3,repo.size());
            repo.save(new Student(4,"a",15,"a","a"));
            assertEquals(4,repo.size());
            repo.save(new Student(1,"",15,"","a"));
        } catch (ValidationException e) {
            assertEquals(4,repo.size());
            assertEquals(2,e.getMesaje().size());
        }
        tearDown();
    }

    public void testUpdate()  {
        setUp();
        try{
            repo.update(new Student(1,"an",14,"an","an"));
            assertEquals(3,repo.size());
            assertEquals("an",repo.findOne(1).get().getNume());
            assertEquals("an",repo.findOne(1).get().getCadru_didactic_indrumator_de_laborator());
            assertEquals("an",repo.findOne(1).get().getEmail());
            assertEquals(14,repo.findOne(1).get().getGrupa());
            repo.update(new Student(13,"a",4,"a","a"));

        } catch (EntityNotFoundException e) {
            assertEquals("Entitatea nu a fost gasita in repository",e.getMessage());
        } catch (ValidationException e) {
            e.printStackTrace();
        }

        try{
            repo.update(new Student(1,"",8,"",""));
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
        } catch (ValidationException e) {
            assertEquals(3,e.getMesaje().size());
        }
        tearDown();
    }

    public void testDelete() {
        setUp();
        assertEquals(3,repo.size());
        try {
            repo.delete(1);
            assertEquals(2,repo.size());
            repo.delete(45);
        } catch (EntityNotFoundException e) {
            assertEquals("Entitatea dorita a fi stearsa nu a fost gasita in repository",e.getMessage());
        }
        tearDown();
    }

}
