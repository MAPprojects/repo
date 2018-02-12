package validator;

import entities.Tema;
import exceptions.AbstractValidatorException;
import exceptions.TemaValidatorException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class TemaValidatorTestCase {
    @Rule
    public ExpectedException exeptions = ExpectedException.none();

    private Tema tema = new Tema("1", "cerinta1", 20);
    private TemaValidator temaValidator = new TemaValidator();


    @Test
    public void validateTemaCuSaptamanGresitaTestCase() throws AbstractValidatorException {
        exeptions.expect(TemaValidatorException.class);
        exeptions.expectMessage("Termenul de predare invalid(valori din intervalul [1,14]).");
        temaValidator.validate(tema);
    }
}
