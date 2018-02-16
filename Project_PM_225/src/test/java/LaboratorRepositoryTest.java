//import domain.Laborator;
//import org.junit.jupiter.api.Test;
//import repository.RepoTema;
//import util.CurrentWeek;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//
//class LaboratorRepositoryTest{
//    RepoTema repoTema = new RepoTema();
//    @Test
//    void size(){
//        Integer i = CurrentWeek.CURRENT_WEEK;
//        assertEquals(repoTema.getSize(), new Integer(3));
//
//    }
//    @Test
//    void add() throws Exception {
//        Laborator laborator=new Laborator(6,"test numar palindrom",8);
//        Laborator laborator2=new Laborator(7,"bubble sort",10);
//
//        repoTema.add(laborator);
//        assertEquals(repoTema.getSize(), new Integer(4));
//        assertEquals(repoTema.findobject(laborator.getNrTema()), laborator);
//        repoTema.delete(6);
////        laborator.setCerinta("turnurile din hanoi");
////        try {
////            repoTema.add(laborator);
////            fail("failed");
////        } catch (Exception e){
////            assertEquals(e.getMessage(),"Your name isn't starting with uppercase!");
////        }
//
//
//    }
//
//
//
//}
