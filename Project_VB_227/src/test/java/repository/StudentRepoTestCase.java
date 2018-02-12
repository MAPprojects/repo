package repository;

import entities.Student;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public class StudentRepoTestCase {
    private StudentRepository studentRepository;

    @Before
    public void instantiateRepoAndPopulateIt(){
        try {
            studentRepository = new StudentRepository();
            studentRepository.save(new Student("1", "nume1", "1", "mail1", "prof1"));
            studentRepository.save(new Student("2", "nume2", "2", "mail2", "prof2"));
            studentRepository.save(new Student("3", "nume3", "3", "mail3", "prof3"));
        } catch (IOException e) {
            Assert.fail();
        }
    }

    @Test
    public void deleteStudentTestCase(){
        Student st = null;
        try {
            st = studentRepository.delete("1").get();
        } catch (IOException e) {
            Assert.fail();
        }
        Assert.assertEquals(st.getId(),(Integer)1);
        Assert.assertEquals(st.getNume(),"nume1");
        Assert.assertEquals(st.getGrupa(),"1");
        Assert.assertEquals(st.getEmail(),"mail1");
        Assert.assertEquals(st.getCadruDidacticIndrumator(),"prof1");
        //Asserting the new size
        Assert.assertTrue(studentRepository.size() == 2);
    }

    @Test
    public void saveStudentTestCase(){
        try {
            studentRepository.save(new Student("4", "nume4", "4", "mail4", "prof4"));
        } catch (IOException e) {
            Assert.fail();
        }
        Assert.assertTrue(studentRepository.size() == 4);
    }

    @Test
    public void findOneStudentTestCase(){
        Student st = studentRepository.findOne("3").get();
        Assert.assertEquals(st.getId(),(Integer)3);
        Assert.assertEquals(st.getNume(),"nume3");
        Assert.assertEquals(st.getGrupa(),"3");
        Assert.assertEquals(st.getEmail(),"mail3");
        Assert.assertEquals(st.getCadruDidacticIndrumator(),"prof3");
    }

    @Test
    public void findAllStudentsTestCase(){
        ArrayList<Student> sts = new ArrayList<>((Collection<Student>) studentRepository.findAll());
        Assert.assertTrue(sts.get(0).getId().equals(1));
        Assert.assertTrue(sts.get(1).getId().equals(2));
        Assert.assertTrue(sts.get(2).getId().equals(3));

    }

    @Test
    public void nullPointerExceptionTestCase() {
        try {
            studentRepository.findOne("123");
            Optional<Student> studentOptional = studentRepository.findOne("1");
            Assert.assertEquals(studentOptional.isPresent(), true);
            studentOptional = studentRepository.findOne("213423");
            Assert.assertEquals(studentOptional.isPresent(), false);
            Assert.assertTrue(true);
        } catch (NullPointerException e) {
            Assert.fail();
        }
    }
}
