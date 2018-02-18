package Repository;

import Domain.Student;
import ExceptionsAndValidators.IValidator;
import ExceptionsAndValidators.ValidatorStudent;

import java.io.*;

public class RepositoryStudentInFile extends AbstractFileRepository<Student,Integer>
{
    private String filename;
    public RepositoryStudentInFile(String filename, IValidator<Student> validatorStudent)
    {
        super(filename,validatorStudent);
//        this.filename=filename;
//        load();
    }

    @Override
    public void createObject(String s) {
        String[] valori = s.split("[|]");
        if(valori.length!=5) {
            System.err.println("Linie invalida: " + s);
            return;
        }
                try {
                    Integer id = Integer.parseInt(valori[0]);
                    String nume = valori[1];
                    Integer grupa = Integer.parseInt(valori[2]);
                    String email = valori[3];
                    String cadruDidactic = valori[4];
                    Student student = new Student(id, nume, grupa, email, cadruDidactic);
                    super.add(student);
                }
                catch (NumberFormatException err) {
                    System.err.println(err);
                }
    }

    @Override
    public void writeObject(BufferedWriter writer,Student student)
    {
        try
        {
            writer.write("" + student.getID() + "|" +
                    student.getNume() + "|" +
                    student.getGrupa() + "|" +
                    student.getEmail() + "|" +
                    student.getCadruDidactic() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    }


