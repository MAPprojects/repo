package tests;

import domain.Student;
import exceptii.ValidationException;
import junit.framework.TestCase;
import validator.StudentValidator;

public class StudentValidatorTest extends TestCase {
    public void testValidatorStudent(){
        StudentValidator stVal=new StudentValidator();
        Student s1=new Student(1,"a",1,"a","a");
        try{
            stVal.validate(s1);
        }catch (ValidationException e){
            assertEquals(1,0);
        }

        s1.setId(-1);
        s1.setNume("");
        s1.setGrupa(-67);
        s1.setEmail("");
        s1.setCadru_didactic_indrumator_de_laborator("");

        try{
            stVal.validate(s1);
        }catch (ValidationException e){
            assertEquals(5,e.getMesaje().size());
        }

    }
}
