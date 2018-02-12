//package repository.fileRepoTestCases;
//
//import entities.Nota;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import repository.fileRepo.NotaFileRepo;
//
//import java.io.FileWriter;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.util.Optional;
//
//public class NotaFileRepoTestCase {
//    private NotaFileRepo notaFileRepo;
//
//    @Before
//    public void populateRepo() {
//        try {
//            clearFile("src\\test\\java\\resources\\noteTest.txt");
//            notaFileRepo = new NotaFileRepo("src\\test\\java\\resources\\noteTest.txt");
//            notaFileRepo.save(new Nota("1", "1", 1));
//            notaFileRepo.save(new Nota("2", "2", 2));
//            notaFileRepo.save(new Nota("3", "3", 3));
//        } catch (IOException e) {
//            Assert.fail();
//        }
//    }
//
//    @Test
//    public void testSize() {
//        Assert.assertEquals(notaFileRepo.size(), 3);
//    }
//
//    @Test
//    public void repoDeleteTestCase() {
//        try {
//            notaFileRepo.delete(new Nota("1", "1", 1).getId());
//            Assert.assertEquals(notaFileRepo.size(), 2);
//        } catch (IOException e) {
//            Assert.fail();
//        }
//    }
//
//    @Test
//    public void updateTestCase() {
//        Nota n = new Nota("10", "10", 10);
//        try {
//            notaFileRepo.update(new Nota("1", "1", 1).getId(), n);
//            Assert.assertEquals(notaFileRepo.size(), 3);
//            Assert.assertEquals(notaFileRepo.findOne(n.getId()).get().getNrTemei(), 10);
//        } catch (IOException e) {
//            Assert.fail();
//        }
//    }
//
//    @Test
//    public void findNotaTestCase() {
//        Assert.assertEquals(notaFileRepo.findOne(new Nota("1", "1", 1).getId()).get().getNrTemei(), 1);
//    }
//
//    private void clearFile(String file) throws IOException {
//        PrintWriter pw = new PrintWriter(new FileWriter(file));
//        pw.flush();
//        pw.close();
//    }
//
//    @Test
//    public void nullPointerExceptionTestCase() {
//        try {
//            notaFileRepo.findOne(123);
//            Optional<Nota> studentOptional = notaFileRepo.findOne(new Nota("1", "1", 3).getId());
//            Assert.assertEquals(studentOptional.isPresent(), true);
//            studentOptional = notaFileRepo.findOne(213423);
//            Assert.assertEquals(studentOptional.isPresent(), false);
//            Assert.assertTrue(true);
//        } catch (NullPointerException e) {
//            Assert.fail();
//        }
//    }
//}
