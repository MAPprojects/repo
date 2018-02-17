package tests;

import domain.Nota;
import junit.framework.TestCase;

public class NotaTest extends TestCase{
    public void testNota(){
        Nota nota=new Nota((Integer)1,(Integer)1,(Integer)1, new Float(9.8));
        assertEquals((Integer) 1,nota.getId());
        assertEquals((Integer)1,nota.getNrTema());
        assertEquals((Integer) 1,nota.getIdStudent());
        assertEquals(new Float(9.8),nota.getValoare());

        nota.setValoare(new Float(7.8));
        nota.setIdStudent(2);
        nota.setIdNota(2);
        nota.setNrTema(2);

        assertEquals((Integer) 2,nota.getId());
        assertEquals((Integer)2,nota.getNrTema());
        assertEquals((Integer) 2,nota.getIdStudent());
        assertEquals(new Float(7.8),nota.getValoare());

    }
}
