package Repository;

import Domain.Student;
import Validator.StudentValidator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class StudentRepositoryTest {
    StudentRepository sr;
    StudentValidator sval;
    @Before
    public void setUp() throws Exception {
        sval=new StudentValidator();
        sr=new StudentRepository(sval);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void save() throws Exception {
        sr.save(new Student(1,"Mark","abc","221","Istvan"));
        assertEquals(sr.size(),1);
    }

    @Test
    public void delete() throws Exception {
        sr.save(new Student(1,"Mark","abc","221","Istvan"));
        sr.delete(1);
        assertEquals(sr.size(),0);
    }

    @Test
    public void update() throws Exception {
        try
        {
            sr.update(1,new Student(1,"Mark","abc","221","Istvan"));
        }
        catch(RepositoryException rex)
        {
            System.err.println("am aruncat RepositoryException la update repo");
        }
    }

    @Test
    public void getAll() throws Exception {
        assertEquals(sr.size(),0);
    }

    @Test
    public void findOne() throws Exception {
        try
        {
            sr.findOne(1);
        }
        catch(RepositoryException rex)
        {
            System.err.println("am aruncat RepositoryException la findOne repo");
        }
    }

    @Test
    public void size() throws Exception {
        assertEquals(sr.size(),0);
    }

}