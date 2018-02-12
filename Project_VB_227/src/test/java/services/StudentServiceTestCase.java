//package services;
//
//import entities.Student;
//import exceptions.StudentServiceException;
//import exceptions.StudentValidatorException;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Rule;
//import org.junit.Test;
//import org.junit.rules.ExpectedException;
//import repository.StudentRepository;
//import validator.StudentValidator;
//import exceptions.AbstractValidatorException;
//
//import java.io.IOException;
//import java.util.ArrayList;
//
//public class StudentServiceTestCase {
//    private StudentValidator studentValidator = new StudentValidator();
//    private StudentRepository studentRepository = new StudentRepository();
//    private StudentService studentService = new StudentService(studentRepository,studentValidator);
//
//    @Rule
//    public ExpectedException expectedException = ExpectedException.none();
//
//    public StudentServiceTestCase() throws IOException {
//    }
//
//    @Before
//    public void populateRepository() throws AbstractValidatorException, StudentServiceException {
//        try {
//            studentService.addStudent("n1", "g1", "e1", "p1");
//            studentService.addStudent("n2", "g2", "e2", "p2");
//            studentService.addStudent("n3", "g3", "e3", "p3");
//        } catch (IOException e) {
//            Assert.fail();
//        }
//    }
//
//    @Test
//    public void addStudentWithExistingIdTestCase() throws AbstractValidatorException, StudentServiceException, IOException {
//        expectedException.expect(StudentServiceException.class);
//        expectedException.expectMessage("Exista deja un student cu acest id.");
//        studentService.addStudent("n4", "g1", "e1", "p1");
//    }
//
//    @Test
//    public void addStudentTestCase() throws AbstractValidatorException, StudentServiceException, IOException {
//        studentService.addStudent("n4", "g4", "e4", "p4");
//        Assert.assertEquals(studentService.numarStudenti(),4);
//    }
//
//    @Test
//    public void addStudentWrongFormatTestCase() throws AbstractValidatorException, StudentServiceException, IOException {
//        expectedException.expect(StudentValidatorException.class);
//        expectedException.expectMessage("Numele studentului nu poate fi nul.Grupa studentului nu poati fi vida.");
//        studentService.addStudent("", "", "e4", "p4");
//    }
//
//    @Test
//    public void findOneStudentByIdTestCase(){
//        Student student;
//        try {
//            student = studentService.findOneStudentById("1");
//            Assert.assertEquals(student.getId(),(Integer)1);
//        } catch (StudentServiceException e) {
//            Assert.fail();
//        }
//    }
//
//    @Test
//    public void findAllStudentsTestCase(){
//        ArrayList<Student> students = studentService.findAllStudents();
//        Assert.assertTrue(students.get(0).getId().equals("1"));
//        Assert.assertTrue(students.get(0).getId().equals("2"));
//        Assert.assertTrue(students.get(0).getId().equals("3"));
//    }
//
//    @Test
//    public void findExistingStudentTestCase(){
//        try {
//            Student student = studentService.findOneStudentById("1");
//            Assert.assertEquals(student.getId(),(Integer)1);
//            Assert.assertEquals(student.getNume(),"n1");
//        } catch (StudentServiceException e) {
//            Assert.fail();
//        }
//    }
//
//    @Test
//    public void filterByNameOfProfTestCase() {
//        try {
//            studentService.addStudent("n4", "g4", "e4", "p3");
//        } catch (AbstractValidatorException e) {
//            Assert.fail();
//        } catch (StudentServiceException e) {
//            Assert.fail();
//        } catch (IOException e) {
//            Assert.fail();
//        }
//        ArrayList<Student> studenti = studentService.filtreazaStudentiiDeLaUnProfesor("p3").get();
//        Student student1 = studenti.get(0);
//        Assert.assertTrue(student1.getId().equals("3") && student1.getNume().equals("n3") && student1.getEmail().equals("e3")
//                && student1.getCadruDidacticIndrumator().equals("p3"));
//        Student student2 = studenti.get(1);
//        Assert.assertTrue(student2.getId().equals("4") && student2.getNume().equals("n4") && student2.getEmail().equals("e4")
//                && student2.getCadruDidacticIndrumator().equals("p3"));
//    }
//
//    @Test
//    public void filterByNGrupaDescTestCase() {
//        try {
//            studentService.addStudent("n4", "g3", "e4", "p4");
//        } catch (AbstractValidatorException e) {
//            Assert.fail();
//        } catch (StudentServiceException e) {
//            Assert.fail();
//        } catch (IOException e) {
//            Assert.fail();
//        }
//        ArrayList<Student> studenti = studentService.filtreazaStudentiiDinGrupa("g3").get();
//        Student student1 = studenti.get(1);
//        Assert.assertTrue(student1.getId().equals("3") && student1.getNume().equals("n3") && student1.getEmail().equals("e3")
//                && student1.getCadruDidacticIndrumator().equals("p3") && student1.getGrupa().equals("g3"));
//        Student student2 = studenti.get(0);
//        Assert.assertTrue(student2.getId().equals("4") && student2.getNume().equals("n4") && student2.getEmail().equals("e4")
//                && student2.getCadruDidacticIndrumator().equals("p4") && student2.getGrupa().equals("g3"));
//    }
//
//    @Test
//    public void filterByNameAndSortAscTestCase() {
//        try {
//            studentService.addStudent("n4", "g4", "e4", "p5");
//            studentService.addStudent("n4", "g5", "e5", "p4");
//        } catch (AbstractValidatorException e) {
//            Assert.fail();
//        } catch (StudentServiceException e) {
//            Assert.fail();
//        } catch (IOException e) {
//            Assert.fail();
//        }
//        ArrayList<Student> studenti = studentService.filtreazaStudentiiCuNumele("n4").get();
//        Student student1 = studenti.get(0);
//        Assert.assertTrue(student1.getId().equals("5") && student1.getNume().equals("n4") && student1.getEmail().equals("e5")
//                && student1.getCadruDidacticIndrumator().equals("p4") && student1.getGrupa().equals("g5"));
//        Student student2 = studenti.get(1);
//        Assert.assertTrue(student2.getId().equals("4") && student2.getNume().equals("n4") && student2.getEmail().equals("e4")
//                && student2.getCadruDidacticIndrumator().equals("p5") && student2.getGrupa().equals("g4"));
//    }
//
//}
