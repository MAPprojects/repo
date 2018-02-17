package Entities;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class StudentTest {
    Student st;

    @Before
    public void setUp() throws Exception {
        st = new Student(1, "Paul", "cuspaul@gmail.com", "221", "Bufny", 0, 0, 0.0, true, true);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getIDStudent() throws Exception {
        assertEquals(st.getIDStudent(), 1);
    }

    @Test
    public void setIDStudent() throws Exception {
        st.setIDStudent(2);
        assertEquals(st.getIDStudent(), 2);
    }

    @Test
    public void getNume() throws Exception {
        assertEquals(st.getNume(), "Paul");
    }

    @Test
    public void setNume() throws Exception {
        st.setNume("Gabriel");
        assertEquals(st.getNume(), "Gabriel");
    }

    @Test
    public void getEmail() throws Exception {
        assertEquals(st.getEmail(), "cuspaul@gmail.com");
    }

    @Test
    public void setEmail() throws Exception {
        st.setEmail("cuspaul@yahoo.com");
        assertEquals(st.getEmail(), "cuspaul@yahoo.com");
    }

    @Test
    public void getGrupa() throws Exception {
        assertEquals(st.getGrupa(), "221");
    }

    @Test
    public void setGrupa() throws Exception {
        st.setGrupa("222");
        assertEquals(st.getGrupa(), "222");
    }

    @Test
    public void getProf() throws Exception {
        assertEquals(st.getProf(), "Bufny");
    }

    @Test
    public void setProf() throws Exception {
        st.setProf("Istvan");
        assertEquals(st.getProf(), "Istvan");
    }

    @Test
    public void setID() throws Exception {
        st.setID(1);
        assertEquals(st.getIDStudent(), 1);
    }

    @Test
    public void getID() throws Exception {
        assertEquals(st.getIDStudent(), 1);
    }

}