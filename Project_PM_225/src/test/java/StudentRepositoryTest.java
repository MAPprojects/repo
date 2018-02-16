//import domain.Student;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.Test;
//import repository.RepoStudent;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//
//class StudentRepositoryTest{
//     RepoStudent repoStudent= new RepoStudent();
//    @Test
//    void size(){
//        assertEquals(repoStudent.getSize(), new Integer(5));
//
//    }
//    @Test
//    void add() throws Exception {
//        Student student=new Student(6,"Popescu Iulian",225,"iulianpopsecu@gmail.com","Bota Florentin");
//        Student student2=new Student(7,"Cristi",225,"iulianpopsecu@gmail.com","Bota Florentin");
//        repoStudent.add(student);
//        assertEquals(repoStudent.getSize(), new Integer(6));
//        assertEquals(repoStudent.findobject(student.getId()), student);
//        repoStudent.delete(6);
//        student.setNume("ionica gheorghe");
//        try {
//            repoStudent.add(student);
//            fail("Incorrect test");
//        } catch (Exception e){
//            assertEquals(e.getMessage(),"Your name isn't starting with uppercase!");
//        }
//        try {
//            repoStudent.add(student2);
//            fail("Incorrect test");
//        } catch (Exception e){
//            assertEquals(e.getMessage(),"Cristi nu este acceptat din 3/11/2017!!!!");
//        }
//    }
//    @Test
//    void update() throws Exception{
//        Student student=new Student(1,"Popescu Iulian",225,"iulianpopsecu@gmail.com","Bota Florentin");
//        repoStudent.update(1,student);
//    }
//
//
//
//}
