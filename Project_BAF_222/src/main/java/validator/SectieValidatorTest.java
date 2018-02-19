package validator;

import entities.Sectie;
import junit.framework.TestCase;

public class SectieValidatorTest extends TestCase {
    SectieValidator validator;

    protected void setUp(){
        validator=new SectieValidator();
    }

    public void testUp(){
        validator.validate(new Sectie(1,"Mate-Info",120));
        try{
            validator.validate(new Sectie(-1,"Mate-Info",120));
            assertTrue(false);
        }
        catch (ValidationException e){}
        try{
            validator.validate(new Sectie(1,"",120));
            assertTrue(false);
        }
        catch (ValidationException e){}
        try{
            validator.validate(new Sectie(1,"Mate-Info",-120));
            assertTrue(false);
        }
        catch (ValidationException e){}
    }
}
