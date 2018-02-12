package Domain;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class StudentTest {
    private Student st;
    @Before
    public void setUp() throws Exception {
        st=new Student(1,"Mark","czelimark97@magyar.hu","221","Istvan");
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getIdStudent() throws Exception {
        assertEquals(st.getIdStudent(),1);
    }

    @Test
    public void setIdStudent() throws Exception {
        st.setIdStudent(5);
        assertEquals(st.getIdStudent(),5);
    }

    @Test
    public void getName() throws Exception {
        assertEquals(st.getName(),"Mark");
    }

    @Test
    public void setName() throws Exception {
        st.setName("Domi");
        assertEquals(st.getName(),"Domi");
    }

    @Test
    public void getEmail() throws Exception {
        assertEquals(st.getEmail(),"czelimark97@magyar.hu");
    }

    @Test
    public void setEmail() throws Exception {
        st.setEmail("xx.yyy.yahoo.com");
        assertEquals(st.getEmail(),"xx.yyy.yahoo.com");
    }

    @Test
    public void getGroup() throws Exception {
        assertEquals(st.getGroup(),"221");
    }

    @Test
    public void setGroup() throws Exception {
        st.setGroup("222");
        assertEquals(st.getGroup(),"222");

    }

    @Test
    public void getProfessor() throws Exception {
        assertEquals(st.getProfessor(),"Istvan");
    }

    @Test
    public void setProfessor() throws Exception {
        st.setProfessor("Attila");
        assertEquals(st.getProfessor(),"Attila");
    }
}