package tests;

import entities.Department;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DepartmentTest {
    private Department d1;
    private Department d2;

    @Before
    public void setUp() throws Exception {
        d1 = new Department(1,"Informatica",280);
        d2 = new Department(2,"Matematica",120);
    }

    @Test
    public void getId() throws Exception {
        assertTrue(d1.getId() == 1);
        assertEquals(1,(long)d1.getId());
        assertTrue(d2.getId() == 2);
        System.out.println("getId() passed !!! :) ");
    }

    @Test
    public void setId() throws Exception {
        assertTrue(d1.getId() == 1);
        d1.setId(3);
        assertTrue(d1.getId() == 3);
        System.out.println("setId() passed !!! :)");
    }

    @Test
    public void getName() throws Exception {
        assertEquals("Informatica",d1.getName());
        assertEquals("Matematica",d2.getName());
        System.out.println("getName() passed !!!! :)");
    }

    @Test
    public void setName() throws Exception {
        assertEquals("Informatica",d1.getName());
        d1.setName("Info");
        assertEquals("Info",d1.getName());
        System.out.println("setName() passed !!! :)");
    }

    @Test
    public void getNrLoc() throws Exception {
        assertTrue(d1.getNrLoc() == 280);
        assertTrue(d2.getNrLoc() == 120);
        System.out.println("getNrLoc() passed !!! :)");
    }

    @Test
    public void setNrLoc() throws Exception {
        assertTrue(d1.getNrLoc() == 280);
        d1.setNrLoc(220);
        assertTrue(d1.getNrLoc() == 220);
        System.out.println("setNrLoc() passed");
    }

}