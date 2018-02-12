//package services;
//
//import exceptions.AbstractValidatorException;
//import exceptions.StudentServiceException;
//import exceptions.TemaServiceException;
//import org.junit.Assert;
//import org.junit.BeforeClass;
//import org.junit.Test;
//import repository.fileRepo.NotaFileRepo;
//import repository.StudentRepository;
//import repository.TemaRepository;
//import validator.NotaValidator;
//import validator.StudentValidator;
//import validator.TemaValidator;
//import vos.NotaVO;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//public class NotaServiceTestCase {
//
//    private static StudentValidator studentValidator;
//    private static StudentRepository studentRepository;
//    private static StudentService studentService;
//    private static TemaValidator temaValidator;
//    private static TemaRepository temaRepository;
//    private static TemaService temaService;
//    private static NotaValidator notaValidator;
//    private static NotaFileRepo notaFileRepo;
//    private static NotaService notaService;
//
//    @BeforeClass
//    public static void initializare() throws IOException, AbstractValidatorException, StudentServiceException, TemaServiceException {
//        studentRepository = new StudentRepository();
//        studentValidator = new StudentValidator();
//        studentService = new StudentService(studentRepository, studentValidator);
//
//        temaValidator = new TemaValidator();
//        temaRepository = new TemaRepository();
//        temaService = new TemaService(temaRepository, temaValidator);
//
//        notaFileRepo = new NotaFileRepo("src\\test\\java\\resources\\noteTestFilter.txt");
//        notaValidator = new NotaValidator();
//        notaService = new NotaService(notaFileRepo, notaValidator, studentRepository, temaRepository);
//
//        studentService.addStudent("n1", "g1", "e1", "p1");
//        studentService.addStudent("n2", "g2", "e2", "p2");
//        studentService.addStudent("n3", "g3", "e3", "p3");
//        studentService.addStudent("n4", "g4", "e4", "p4");
//
//        temaService.addTema("c1", 1);
//        temaService.addTema("c2", 2);
//        temaService.addTema("c3", 3);
//    }
//
////    @BeforeClass
////    public static void populateRepos() throws IOException, AbstractValidatorException, StudentServiceException, TemaServiceException, ValidationException, NotaServiceException {
////        studentService.addStudent(new Student(1, "n1", "g1", "e1", "p1"));
////        studentService.addStudent(new Student(2, "n2", "g2", "e2", "p2"));
////        studentService.addStudent(new Student(3, "n3", "g3", "e3", "p3"));
////        studentService.addStudent(new Student(4, "n4", "g4", "e4", "p4"));
////
////        temaService.addTema(new Tema(1, "c1", 1));
////        temaService.addTema(new Tema(2, "c2", 2));
////        temaService.addTema(new Tema(3, "c3", 3));
////
//////        notaService.addNota(new Nota(1,1,10),1,"");
//////        notaService.addNota(new Nota(2,3,10),1,"c2");
//////        notaService.addNota(new Nota(3,1,8),6,"c3");
////        //notaService.addNota(new Nota(3,2,8),7,"c3");
////
////    }
//
//    @Test
//    public void filterByValoareNotaTestCase() {
//        List<NotaVO> note = new ArrayList<NotaVO>(notaService.filtreazaPentruValoare(10).get());
//        Assert.assertTrue(note.size() == 2);
//        NotaVO nota1 = note.get(0);
//        NotaVO nota2 = note.get(1);
//        Assert.assertTrue(nota1.getValoare().equals(10) && nota1.getIdTema().equals(1) && nota1.getCerintaTema().equals("c1"));
//    }
//
//    @Test
//    public void filterNoteForOneStTestCase() {
//        List<NotaVO> note = new ArrayList<NotaVO>(notaService.filtreazaNotelePentruUnStudent("n3").get());
//        Assert.assertTrue(note.size() == 2);
//        NotaVO notaVO1 = note.get(0);
//        NotaVO notaVO2 = note.get(1);
//        Assert.assertEquals(notaVO1.getValoare(), (Integer) 1);
//    }
//
//}
