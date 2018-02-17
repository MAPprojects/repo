package tests;

import domain.TemaLaborator;
import exceptii.ValidationException;
import junit.framework.TestCase;
import validator.TemeLabValidator;

public class TemeLabValidatorTest extends TestCase {
    public void testTemeLabValidator(){
        TemeLabValidator val=new TemeLabValidator();
        TemaLaborator t=new TemaLaborator(1,"a",8);
        try{
            val.validate(t);
        }catch (ValidationException e){
            assertEquals(1,0);
        }
        t.setCerinta("");
        t.setNr_tema_de_laborator(-8);
        t.setDeadline(0);
        try{
            val.validate(t);
        }catch (ValidationException e){
            assertEquals(3,e.getMesaje().size());
        }
    }
}
