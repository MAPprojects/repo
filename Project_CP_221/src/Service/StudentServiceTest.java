package Service;

import Domain.Student;
import Repository.StudentRepository;
import Validator.StudentValidator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.swing.text.html.HTMLDocument;

import java.io.Serializable;
import java.util.Spliterator;

import static org.junit.Assert.*;

public class StudentServiceTest {
    StudentService serv;
    @Before
    public void setUp() throws Exception {
        StudentValidator sval=new StudentValidator();
        StudentRepository sr=new StudentRepository(sval);
        serv=new StudentService(sr);
        sr.save(new Student(1,"Mark","abc","221","Istvan"));
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void addStudent() throws Exception {
        serv.addStudent(2,"Dutz","Vldtz","227","Bufny");
        assertEquals(serv.getSizeObjects(),2);
    }


    @Test
    public void deleteStudent() throws Exception {
        serv.deleteStudent(1);
        assertEquals(serv.getSizeObjects(),0);
    }

    @Test
    public void updateStudent() throws Exception {
        serv.updateStudent(1,"Traian","email","227","John");
        assertEquals(serv.findOne(1).getName(),"Traian");
    }

    @Test
    public void getAllObjects() throws Exception {
        Iterable<Student> sts=serv.getAllObjects();
        Spliterator<Student>split=sts.spliterator();
        //Serializable ss;
        assertEquals(split.getExactSizeIfKnown(),1);
    }

    @Test
    public void getSizeObjects() throws Exception {
        assertEquals(serv.getSizeObjects(),1);
    }

    @Test
    public void findOne() throws Exception {
        assertEquals(serv.findOne(1).getName(),"Mark");
    }

}