package validator;


import entities.Candidat;
import junit.framework.TestCase;

public class CandidatValidatorTest extends TestCase {
    CandidatValidator validator;

    protected void setUp() {
        validator=new CandidatValidator();
    }

    public void testUp(){
        validator.validate(new Candidat(1234,"Popescu","0754123544","popescu@mail.com"));
        try{
            validator.validate(new Candidat(-1234,"Popescu","0754123544","popescu@mail.com"));
            assertTrue(false);
        }
        catch (ValidationException e){}
        try{
            validator.validate(new Candidat(1234,"","0754123544","popescu@mail.com"));
            assertTrue(false);
        }
        catch (ValidationException e){}
        try{
            validator.validate(new Candidat(1234,"Popescu","075412er3544","popescu@mail.com"));
            assertTrue(false);
        }
        catch (ValidationException e){}
        try{
            validator.validate(new Candidat(1234,"Popescu","0754123544",""));
            assertTrue(false);
        }
        catch (ValidationException e){}
    }
}
