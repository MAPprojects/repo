package entities;

import junit.framework.TestCase;

public class SectieTest extends TestCase {
    protected Sectie sectie;
    protected void setUp(){
        sectie=new Sectie(1,"Mate-Info",120);
    }
   public void testUp(){
        assertTrue(sectie.getNume()=="Mate-Info");
        assertTrue(sectie.getID()==1);
        assertTrue(sectie.getNrLoc()==120);
        assertTrue(sectie.toString().equals("Mate-Info 120"));
        sectie.setID(233);
        sectie.setNume("Info-Engleza");
        sectie.setNrLoc(220);
        assertTrue(sectie.getNume()=="Info-Engleza");
        assertTrue(sectie.getID()==233);
        assertTrue(sectie.getNrLoc()==220);

   }

}
