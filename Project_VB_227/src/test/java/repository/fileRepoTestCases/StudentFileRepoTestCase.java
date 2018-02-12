package repository.fileRepoTestCases;

import entities.Student;
import javafx.print.Printer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import repository.fileRepo.StudentFileRepository;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public class StudentFileRepoTestCase {
    private static final String FILE_PATH = "src\\test\\java\\resources\\stsTest.txt";
    private static StudentFileRepository studentFileRepository;

    @Before
    public void populateRepository() {
        try {
            clearFile(FILE_PATH);
            studentFileRepository = new StudentFileRepository(FILE_PATH);
            studentFileRepository.save(new Student("1", "nume1", "1", "mail1", "prof1"));
            studentFileRepository.save(new Student("2", "nume2", "2", "mail2", "prof2"));
            studentFileRepository.save(new Student("3", "nume3", "3", "mail3", "prof3"));
        } catch (IOException e) {
            Assert.fail();
        }
    }

    @Test
    public void openInexistentFile() {
        try {
            StudentFileRepository derp = new StudentFileRepository("sts.txt");
        } catch (IOException e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void deleteStudentTestCase() {
        Student st = null;
        try {
            st = studentFileRepository.delete("1").get();
        } catch (IOException e) {
            Assert.fail();
        }
        Assert.assertEquals(st.getId(), (Integer) 1);
        Assert.assertEquals(st.getNume(), "nume1");
        Assert.assertEquals(st.getGrupa(), "1");
        Assert.assertEquals(st.getEmail(), "mail1");
        Assert.assertEquals(st.getCadruDidacticIndrumator(), "prof1");
        //Asserting the new size
        Assert.assertTrue(studentFileRepository.size() == 2);
    }

    @Test
    public void saveStudentTestCase() {
        try {
            studentFileRepository.save(new Student("4", "nume4", "4", "mail4", "prof4"));
        } catch (IOException e) {
            Assert.fail();
        }
        Assert.assertTrue(studentFileRepository.size() == 4);
    }

    @Test
    public void findOneStudentTestCase() {
        Student st = studentFileRepository.findOne("3").get();
        Assert.assertEquals(st.getId(), 3);
        Assert.assertEquals(st.getNume(), "nume3");
        Assert.assertEquals(st.getGrupa(), "3");
        Assert.assertEquals(st.getEmail(), "mail3");
        Assert.assertEquals(st.getCadruDidacticIndrumator(), "prof3");
    }

    @Test
    public void findAllStudentsTestCase() {
        ArrayList<Student> sts = new ArrayList<>((Collection<Student>) studentFileRepository.findAll());
        Assert.assertTrue(sts.get(0).getId().equals("1"));
        Assert.assertTrue(sts.get(1).getId().equals("2"));
        Assert.assertTrue(sts.get(2).getId().equals("3"));

    }

    private void clearFile(String file) throws IOException {
        PrintWriter pw = new PrintWriter(new FileWriter(file));
        pw.flush();
        pw.close();
    }

    @Test
    public void nullPointerExceptionTestCase() {
        try {
            studentFileRepository.findOne("123");
            Optional<Student> studentOptional = studentFileRepository.findOne("1");
            Assert.assertEquals(studentOptional.isPresent(), true);
            studentOptional = studentFileRepository.findOne("213423");
            Assert.assertEquals(studentOptional.isPresent(), false);
            Assert.assertTrue(true);
        } catch (NullPointerException e) {
            Assert.fail();
        }
    }

}
