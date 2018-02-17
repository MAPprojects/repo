package repository;

import domain.TemaLaborator;
import exceptii.EntityNotFoundException;
import exceptii.ValidationException;
import validator.Validator;

import java.io.*;

public class TemeLabFileRepository extends AbstractFileRepository<TemaLaborator,Integer>{

    /**
     * Constructor
     *
     * @param val  -validator.Validator<E></E>
     * @param file String the fileName where is the data
     */
    public TemeLabFileRepository(Validator<TemaLaborator> val, String file) {
        super(val, file);
    }

    /**
     *
     * @param fields String []
     * @return
     */
    @Override
    public TemaLaborator buildEntity(String[] fields) {
        return new TemaLaborator(Integer.parseInt(fields[0]),fields[1],Integer.parseInt(fields[2]));
    }
}
