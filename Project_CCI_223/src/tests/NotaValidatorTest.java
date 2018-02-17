package tests;

import domain.Nota;
import exceptii.ValidationException;
import junit.framework.TestCase;
import validator.NotaValidator;
import validator.Validator;

public class NotaValidatorTest extends TestCase {
    public void testValidator(){
        Validator<Nota> val=new NotaValidator();
        Nota nota=new Nota ((Integer)1,(Integer)1,(Integer)1,new Float(9.6));
        try {
            val.validate(nota);
            nota.setNrTema(0);
            nota.setId(0);
            nota.setIdStudent(0);
            nota.setValoare(new Float(100.5));
            val.validate(nota);
        } catch (ValidationException e) {
            assertEquals(4,e.getMesaje().size());
        }
    }

}
