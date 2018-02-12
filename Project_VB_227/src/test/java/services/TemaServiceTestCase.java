package services;

import entities.Tema;
import exceptions.AbstractValidatorException;
import exceptions.TemaServiceException;
import exceptions.TemaValidatorException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import repository.StudentRepository;
import repository.TemaRepository;
import validator.TemaValidator;

import java.io.IOException;
import java.util.ArrayList;

public class TemaServiceTestCase {
    private TemaRepository temaRepository = new TemaRepository();
    private TemaValidator temaValidator = new TemaValidator();
    private StudentRepository studentRepository = new StudentRepository();
    private TemaService temaService = new TemaService(temaRepository, studentRepository, temaValidator);

    public TemaServiceTestCase() throws IOException {
    }

    @Before
    public void populateTheRepository() throws AbstractValidatorException, TemaServiceException {

        try {
            temaService.addTema("c1", 1);
            temaService.addTema("c2", 2);
            temaService.addTema("c3", 3);
        } catch (IOException e) {
            Assert.fail();
        }
    }

    @Test
    public void addNewTemaTestCase() throws AbstractValidatorException, TemaServiceException {
        try {
            temaService.addTema("c4", 4);
        } catch (IOException e) {
            Assert.fail();
        }
        Assert.assertEquals(temaService.numarTeme(),4);
    }

    @Test
    public void addTemaWithDuplicateIdTestCase(){
        try{
            temaService.addTema("c7", 2);
        }catch (TemaServiceException e){

        } catch (AbstractValidatorException e) {
            Assert.assertEquals(e.getMessage(),"Exista deja un student cu acest id.");
        } catch (IOException e) {
            Assert.fail();
        }
    }

    @Test
    public void addTemaWithWrongFormat(){
        try{
            temaService.addTema("", 22);
        }catch (TemaValidatorException e){
            Assert.assertEquals(e.getMessage(),"Cerinta nu poate fi vida." +
                    "Termenul de predare invalid(valori din intervalul [1,14]).");
        } catch (TemaServiceException e) {
            Assert.fail("Exceptia nu ar trebui sa apara.");
        } catch (IOException e) {
            Assert.fail();
        }
    }

    @Test
    public void deleteExistingTemaTestCase(){
        try{
            temaService.deleteTema(new Tema("1", "c7", 2));
        } catch (TemaValidatorException e) {
            Assert.fail("Formatul temei este corect,nu ar trebui sa necesite o exceptie.");
        } catch (TemaServiceException e) {
            Assert.fail("Tema exista in repository");
        } catch (IOException e) {
            Assert.fail();
        }
        Assert.assertEquals(temaService.numarTeme(),2);
    }

    @Test
    public void deleteInexistentTemaTestCase(){
        try{
            temaService.deleteTema(new Tema("17", "c17", 13));
        } catch (TemaValidatorException e) {
            Assert.fail("Tema este corect introduse");
        } catch (TemaServiceException e) {
            Assert.assertTrue(true);
        } catch (IOException e) {
            Assert.fail();
        }
    }

    @Test
    public void deleteTemaWithWrongFormat(){
        try{
            temaService.deleteTema(new Tema("17", "", 13));
        } catch (TemaValidatorException e) {
            Assert.assertEquals(e.getMessage(),"Cerinta nu poate fi vida.");
        } catch (TemaServiceException e) {
            Assert.fail();
        } catch (IOException e) {
            Assert.fail();
        }
    }

    @Test
    public void findExistenTemaByIdTestCase(){
        try {
            Tema tema = temaService.findOneTemaById("1");
            Assert.assertEquals(tema.getId(),(Integer)1);
            Assert.assertEquals((int) tema.getTermenPredare(), 1);
            Assert.assertEquals(tema.getCerinta(),"c1");
        } catch (TemaServiceException e) {
            Assert.fail();
        }
    }

    @Test
    public void findInexistentTemaTestCase(){
        try {
            temaService.findOneTemaById("1143");
            Assert.fail();
        } catch (TemaServiceException e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void finAllTemeTestCase(){
        ArrayList<Tema> teme = temaService.findAllTeme();
        Assert.assertEquals(teme.get(0).getId(),(Integer) 1);
        Assert.assertEquals(teme.get(1).getId(),(Integer) 2);
        Assert.assertEquals(teme.get(2).getId(),(Integer) 3);
    }

    @Test
    public void filterByCerintaTestCase() {
        try {
            temaService.addTema("cerinta derp", 14);
        } catch (TemaValidatorException e) {
            Assert.fail();
        } catch (TemaServiceException e) {
            Assert.fail();
        } catch (IOException e) {
            Assert.fail();
        }
        ArrayList<Tema> teme = new ArrayList<>(temaService.filtreazaCerinta("nta").get());
        Assert.assertTrue(teme.size() == 1);
        Assert.assertTrue(teme.get(0).getCerinta().equals("cerinta derp"));
    }

    @Test
    public void filterByDeadlineTestCase() {
        try {
            temaService.addTema("c4", 3);
        } catch (TemaValidatorException e) {
            Assert.fail();
        } catch (TemaServiceException e) {
            Assert.fail();
        } catch (IOException e) {
            Assert.fail();
        }
        ArrayList<Tema> teme = new ArrayList<>(temaService.filtreazaTermenPredare(3).get());
        Assert.assertTrue(teme.size() == 2);
        Tema tema1 = teme.get(0);
        Tema tema2 = teme.get(1);
        Assert.assertTrue(tema1.getId().equals(3));
        Assert.assertTrue(tema2.getId().equals(4));
    }

    @Test
    public void filterByDeadlineAndTextTestCase() {
        try {
            temaService.addTema("abcdfg", 3);
            temaService.addTema("dasmdasmdbcl", 3);
        } catch (TemaValidatorException e) {
            Assert.fail();
        } catch (TemaServiceException e) {
            Assert.fail();
        } catch (IOException e) {
            Assert.fail();
        }
        ArrayList<Tema> teme = new ArrayList<>(temaService.filtreazaTermenPredareSiCerinta("bc", 3).get());
        Assert.assertTrue(teme.size() == 2);
        Tema tema1 = teme.get(0);
        Tema tema2 = teme.get(1);
        Assert.assertTrue(tema1.getId().equals(4) && tema1.getCerinta().equals("abcdfg"));
        Assert.assertTrue(tema2.getId().equals(5) && tema2.getCerinta().equals("dasmdasmdbcl"));
    }
}
