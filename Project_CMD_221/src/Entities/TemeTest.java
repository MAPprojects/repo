package Entities;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TemeTest {
    Teme t;

    @Before
    public void setUp() throws Exception {
        t = new Teme(1, 3, "Tema smechera", 0);
    }

    @After
    public void tearDown() throws Exception {
    }
    @Test
    public void getIDTema() throws Exception {
        assertEquals(t.getIDTema(), 1);
    }

    @Test
    public void setIDTema() throws Exception {
        t.setIDTema(2);
        assertEquals(t.getIDTema(), 2);
    }

    @Test
    public void getDeadline() throws Exception {
        assertEquals(t.getDeadline(), 3);
    }

    @Test
    public void setDeadline() throws Exception {
        t.setDeadline(4);
        assertEquals(t.getDeadline(), 4);
    }

    @Test
    public void getDescriere() throws Exception {
        assertEquals(t.getDescriere(), "Tema smechera");
    }

    @Test
    public void setDescriere() throws Exception {
        t.setDescriere("Tema buna");
        assertEquals(t.getDescriere(), "Tema buna");
    }

}