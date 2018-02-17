package Repository;

import Validators.StudentValidator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import Entities.Student;

public class StudentRepositoryTest {
    Student st;
    private StudentValidator val = new StudentValidator();
    private StudentRepository repo = new StudentRepository(val);

    @Before
    public void setUp() throws Exception {
        st = new Student(1, "Paul", "cuspaul@gmail.com", "221", "Bufny", 0, 0, 0.0, true, true);
        repo.save(st);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void save() throws Exception {
        Student st1 = new Student(2, "Traian", "chisioantraian@gmail.com", "221", "Istvan", 0, 0, 0.0, true, true);
        repo.save(st1);
        assertEquals(repo.size(), 2);
    }

    @Test
    public void delete() throws Exception {
        Student st3 = new Student(3, "Vladut", "vladut@gmail.com", "227", "Istvan", 0, 0, 0.0, true, true);
        repo.save(st3);
        repo.delete(3);
        assertEquals(repo.size(), 1);
    }

    @Test
    public void update() throws Exception {
        Student st2 = new Student(1, "Alex", "alexardelean@gmail.com", "221", "Bufny", 0, 0, 0.0, true, true);
        repo.update(1, st2);
        assertEquals(repo.findOne(1).getNume(), "Alex");
    }

    @Test
    public void findOne() throws Exception {
        assertEquals(repo.findOne(1).getNume(), "Paul");
    }

    @Test
    public void size() throws Exception {
    }

}