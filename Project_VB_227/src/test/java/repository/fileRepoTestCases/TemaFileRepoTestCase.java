package repository.fileRepoTestCases;

import entities.Tema;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import repository.fileRepo.TemaFileRepo;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public class TemaFileRepoTestCase {
    private static final String FILE_PATH = "src\\test\\java\\resources\\temeTest.txt";
    private static TemaFileRepo temaFileRepo;

    @Before
    public void populateRepo() {
        try {
            clearFile(FILE_PATH);
            temaFileRepo = new TemaFileRepo(FILE_PATH);
            temaFileRepo.save(new Tema("1", "c1", 1));
            temaFileRepo.save(new Tema("2", "c2", 2));
            temaFileRepo.save(new Tema("3", "c3", 3));
        } catch (IOException e) {
            Assert.fail();
        }
    }

    @Test
    public void openInexistentFile() {
        try {
            TemaFileRepo temaFileRepo = new TemaFileRepo("derp");
        } catch (IOException e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void saveTemaTestCase() {
        try {
            temaFileRepo.save(new Tema("4", "c4", 4));
        } catch (IOException e) {
            Assert.fail();
        }
        Assert.assertEquals(temaFileRepo.size(), 4);
    }

    @Test
    public void findOneTestCase() {
        Tema tema = temaFileRepo.findOne("1").get();
        Assert.assertEquals(tema.getId(), (Integer) 1);
        Assert.assertEquals(tema.getCerinta(), "c1");
        Assert.assertEquals((int) tema.getTermenPredare(), 1);
    }

    @Test
    public void findAllTemeTestCase() {
        ArrayList<Tema> teme = new ArrayList<>((Collection<Tema>) temaFileRepo.findAll());
        Assert.assertEquals(teme.get(0).getId(), (Integer) 1);
        Assert.assertEquals(teme.get(1).getId(), (Integer) 2);
        Assert.assertEquals(teme.get(2).getId(), (Integer) 3);
    }

    @Test
    public void deleteTemaTestCase() {
        Tema tema = null;
        try {
            tema = temaFileRepo.delete("1").get();
        } catch (IOException e) {
            Assert.fail();
        }
        Assert.assertEquals(tema.getId(), "1");
        Assert.assertEquals(temaFileRepo.size(), 2);
    }

    private void clearFile(String file) throws IOException {
        PrintWriter pw = new PrintWriter(new FileWriter(file));
        pw.flush();
        pw.close();
    }

    @Test
    public void nullPointerExceptionTestCase() {
        try {
            temaFileRepo.findOne("123");
            Optional<Tema> temaOptional = temaFileRepo.findOne("1");
            Assert.assertEquals(temaOptional.isPresent(), true);
            temaOptional = temaFileRepo.findOne("213423");
            Assert.assertEquals(temaOptional.isPresent(), false);
            Assert.assertTrue(true);
        } catch (NullPointerException e) {
            Assert.fail();
        }
    }
}
