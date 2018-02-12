package tests;

import entities.Option;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class OptionTest {
    private Option o;
    @Before
    public void setUp() throws Exception {
     o = new Option(1,2,"romana",0);
    }

    @Test
    public void getId() throws Exception {
        assertTrue(o.getId().equals("102r"));
    }

    @Test
    public void getIdCandidate() throws Exception {
        assertTrue(o.getIdCandidate() == 1);
    }

    @Test
    public void setIdCandidate() throws Exception {
        assertTrue(o.getIdCandidate() == 1);
        o.setIdCandidate(23);
        assertTrue(o.getIdCandidate() == 23);
    }

    @Test
    public void getIdDepartment() throws Exception {
        assertTrue( o.getIdDepartment() ==2);
    }

    @Test
    public void setIdDepartment() throws Exception {
        assertTrue( o.getIdDepartment() ==2);
        o.setIdDepartment(22);
        assertTrue( o.getIdDepartment() ==22);
    }

    @Test
    public void getLanguage() throws Exception {
        assertTrue(o.getLanguage().equals("romana"));
    }

    @Test
    public void setLanguage() throws Exception {
        assertTrue(o.getLanguage().equals("romana"));
        o.setLanguage("engleza");
        assertTrue(o.getLanguage().equals("engleza"));
    }

}