package tests;

import domain.Student;
import junit.framework.TestCase;

public class StudentTest extends TestCase {
    public void testStudent(){
        Student st=new Student(1,"ioana",154,"ioana@mail.fr","Popescu Maria");
        assertEquals(1,st.getIdStudent());
        assertEquals("Popescu Maria",st.getCadru_didactic_indrumator_de_laborator());
        assertEquals("ioana",st.getNume());
        assertEquals(154,st.getGrupa());
        assertEquals("ioana@mail.fr",st.getEmail());

        st.setIdStudent(2);
        st.setCadru_didactic_indrumator_de_laborator("Pop Maria");
        st.setEmail("ioa@mail.ro");
        st.setGrupa(90);
        st.setNume("ioa");

        assertEquals(2,st.getIdStudent());
        assertEquals("Pop Maria",st.getCadru_didactic_indrumator_de_laborator());
        assertEquals("ioa",st.getNume());
        assertEquals(90,st.getGrupa());
        assertEquals("ioa@mail.ro",st.getEmail());

        st.setId(90);
        assertEquals((Integer)90,st.getId());
    }
}
