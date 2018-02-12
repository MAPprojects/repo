package repository;

import entities.Tema;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public class TemaRepoTestCase {
    private TemaRepository temaRepository = new TemaRepository();

    @Before
    public void populateRepository(){
        try {
            temaRepository.save(new Tema("1", "c1", 1));
            temaRepository.save(new Tema("2", "c2", 2));
            temaRepository.save(new Tema("3", "c3", 3));
        } catch (IOException e) {
            Assert.fail();
        }
    }

    @Test
    public void saveTemaTestCase(){
        try {
            temaRepository.save(new Tema("4", "c4", 4));
        } catch (IOException e) {
            Assert.fail();
        }
        Assert.assertEquals(temaRepository.size(),4);
    }

    @Test
    public void findOneTestCase(){
        Tema tema = temaRepository.findOne("1").get();
        Assert.assertEquals(tema.getId(),(Integer)1);
        Assert.assertEquals(tema.getCerinta(),"c1");
        Assert.assertEquals((int) tema.getTermenPredare(), 1);
    }

    @Test
    public void findAllTemeTestCase(){
        ArrayList<Tema> teme = new ArrayList<>((Collection<Tema>)temaRepository.findAll());
        Assert.assertEquals(teme.get(0).getId(),(Integer)1);
        Assert.assertEquals(teme.get(1).getId(),(Integer)2);
        Assert.assertEquals(teme.get(2).getId(),(Integer)3);
    }

    @Test
    public void deleteTemaTestCase(){
        Tema tema = null;
        try {
            tema = temaRepository.delete("1").get();
        } catch (IOException e) {
            Assert.fail();
        }
        Assert.assertEquals(tema.getId(), "1");
        Assert.assertEquals(temaRepository.size(),2);
    }

    @Test
    public void nullPointerExceptionTestCase() {
        try {
            temaRepository.findOne("123");
            Optional<Tema> temaOptional = temaRepository.findOne("1");
            Assert.assertEquals(temaOptional.isPresent(), true);
            temaOptional = temaRepository.findOne("213423");
            Assert.assertEquals(temaOptional.isPresent(), false);
            Assert.assertTrue(true);
        } catch (NullPointerException e) {
            Assert.fail();
        }
    }

}
