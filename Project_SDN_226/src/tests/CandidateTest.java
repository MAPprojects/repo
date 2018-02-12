package tests;

import entities.Candidate;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CandidateTest {
    Candidate c1;
    Candidate c2;
    @Before
    public void setUp() throws Exception {
         c1 = new Candidate(1,"Dragos","070000000","dragos@gmail.com");
         c2 = new Candidate(10,"Alin","0777777","alin@gmail.com");
    }

    @Test
    public void getId() throws Exception {

            assertTrue(c1.getId()==1);
            assertTrue(c2.getId()==10);
            System.out.println("getId() passed !! :)");



    }

    @Test
    public void setId() throws Exception {
        assertTrue(c1.getId()==1);
        c1.setId(2);
        assertTrue(c1.getId()==2);
        System.out.println("setId() passed !! :)");

    }

    @Test
    public void getName() throws Exception {
        assertTrue(c1.getName().equals("Dragos"));
        assertTrue(c2.getName().equals("Alin"));
        System.out.println("getName() passed !! :)");
    }

    @Test
    public void setName() throws Exception {
        assertTrue(c1.getName().equals("Dragos"));
        c1.setName("Alex");
        assertEquals("Alex",c1.getName());
        System.out.println("setName() passed !! :)");
    }

    @Test
    public void getPhone() throws Exception {
        assertEquals("070000000",c1.getPhone());
        assertEquals("0777777",c2.getPhone());
        System.out.println("getPhone()) passed !! :)");
    }

    @Test
    public void setPhone() throws Exception {
        assertEquals("070000000",c1.getPhone());
        c1.setPhone("072789234");
        assertEquals("072789234",c1.getPhone());
        System.out.println("setPhone() passed !! :)");
    }

    @Test
    public void getEmail() throws Exception {
        assertTrue(c1.getEmail().equals("dragos@gmail.com"));
        assertTrue(c2.getEmail().equals("alin@gmail.com"));
        System.out.println("getEmail() passed !! :)");

    }

    @Test
    public void setEmail() throws Exception {
        assertTrue(c1.getEmail().equals("dragos@gmail.com"));
        c1.setEmail("dragos2@yahoo.com");
        assertTrue(c1.getEmail().equals("dragos2@yahoo.com"));
        System.out.println("setEmail() passed !!! :)");
    }

}