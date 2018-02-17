package repository;

import domain.Nota;
import domain.Student;
import exceptii.EntityNotFoundException;
import exceptii.ValidationException;
import validator.Validator;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class StudentFileRepository extends AbstractFileRepository<Student,Integer>{

    /**
     * Constructor
     *
     * @param val  -validator.Validator<E></E>
     * @param file String the fileName where is the data
     */
    public StudentFileRepository(Validator<Student> val, String file) {
        super(val, file);
    }

    /**
     * @param fields String []
     * @return Student a student fromed by the atributtes from the fields
     */
    @Override
    public Student buildEntity(String[] fields) {
        Student student=new Student(Integer.parseInt(fields[0]),fields[1],Integer.parseInt(fields[2]),fields[3],fields[4]);
        return student;
    }
}
