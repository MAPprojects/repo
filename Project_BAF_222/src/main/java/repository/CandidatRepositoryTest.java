package repository;

import entities.Candidat;
import junit.framework.TestCase;
import validator.CandidatValidator;
import validator.Validator;

public class CandidatRepositoryTest extends TestCase {
    private repository.CandidatRepository repository;
    private Candidat candidat1,candidat2,candidat3;
    protected void setUp(){
        Validator<Candidat> validator=new CandidatValidator();
        repository=new repository.CandidatRepository(validator);
        candidat1=new Candidat(1234,"Popescu","0754123544","popescu@mail.com");
        candidat2=new Candidat(1234,"Grigore","0754353564","grigore@mail.com");
        candidat3=new Candidat(5641,"Vasile","0346312355","vasile@mail.com");
    }

    public void testUp(){

        repository.save(candidat1);
        repository.save(candidat3);
        assertTrue(repository.size()==2);
        try{
            repository.save(candidat2);
            assertTrue(false);
        }
        catch (RepositoryException e){}
        repository.delete(1234);
        assertTrue(repository.size()==1);
        try{
            repository.delete(1234);
            assertTrue(false);
        }
        catch (RepositoryException e){}
        repository.save(candidat1);
        repository.update(1234,candidat2);
        Candidat candidat4=repository.findOne(1234);
        assertTrue(candidat4.getEmail()=="grigore@mail.com");
        assertTrue(candidat4.getNume()=="Grigore");
        assertTrue(candidat4.getTelefon()=="0754353564");
        try{
            repository.update(45234,candidat4);
            assertTrue(false);
        }
        catch (RepositoryException e){}
        repository.delete(5641);
        try{
            repository.update(5641,candidat3);
            assertTrue(false);
        }
        catch (RepositoryException e){}
        try{
            repository.findOne(5325);
            assertTrue(false);
        }
        catch (RepositoryException e){}
        int nr=0;
        for (Candidat el:repository.getAll()){
            nr++;
        }
        assertTrue(nr==repository.size());
    }
}
