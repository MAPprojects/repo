package tests;

import domain.TemaLaborator;
import junit.framework.TestCase;

public class TemaLaboratorTest extends TestCase {
    public void testTemaLaborator(){
        TemaLaborator tb=new TemaLaborator(1,"1...",2);

        assertEquals(1,tb.getNr_tema_de_laborator());
        assertEquals("1...",tb.getCerinta());
        assertEquals(2,tb.getDeadline());

        tb.setDeadline(3);
        tb.setNr_tema_de_laborator(4);
        assertEquals((Integer) 4,tb.getId());
        tb.setCerinta("2...");
        tb.setId(5);

        assertEquals(5,tb.getNr_tema_de_laborator());
        assertEquals("2...",tb.getCerinta());
        assertEquals(3,tb.getDeadline());
    }
}
