package Repository;

import Domain.Nota;
import Domain.Student;
import Domain.Tema;
import ExceptionsAndValidators.IValidator;
import ExceptionsAndValidators.NotExistingKeyException;
import javafx.util.Pair;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.stream.Stream;

public class RepositoryNotaInFile extends AbstractRepository<Nota,Pair<Integer,Integer>>
{
    String fileName;
    public RepositoryNotaInFile(String fileName, IValidator<Nota> validator,  IRepository<Student,Integer> repoStudent, IRepository<Tema,Integer> repoTema)
    {
        super(validator);
        this.fileName=fileName;
        load(repoStudent,repoTema);
    }

//    @Override
//    public void createObject(String s) {
//        String[] valori = s.split("[|]");
//        if(valori.length!=5) {
//            System.err.println("Linie invalida: " + s);
//            return;
//        }
//        try {
//            Integer idStudent = Integer.parseInt(valori[0]);
//            Integer idTema = Integer.parseInt(valori[1]);
//            int valoare = Integer.parseInt(valori[2]);
//            Student student = repoStudent.find(idStudent);
//            Tema tema = repoTema.find(idTema);
//            Nota nota = new Nota(student,tema,valoare);
//
//            super.add(nota);
//        }
//        catch (NumberFormatException err) {
//            System.err.println(err);
//        }
//    }

    private void load(IRepository<Student,Integer> repoStudent, IRepository<Tema,Integer> repoTema)
    {

            Path path = Paths.get(fileName);
            Stream<String> lines;
            try
            {
                lines = Files.lines(path); //Files – helper class
                lines.forEach(s -> { String[] valori = s.split("[|]");
                    if(valori.length!=3)
                    {
                        System.err.println("Linie invalida: "+s);
                        return;
                    }
                    try
                    {
                        Integer idStudent = Integer.parseInt(valori[0]);
                        Integer idTema = Integer.parseInt(valori[1]);
                        int valoare = Integer.parseInt(valori[2]);

                        Optional<Student> ops = repoStudent.find(idStudent);
                        Optional<Tema> opt = repoTema.find(idTema);

                        if( ops.isPresent() && opt.isPresent())
                        {
                            Student student = ops.get();
                            Tema tema = opt.get();
                            Nota nota = new Nota(student, tema, valoare);
                            super.add(nota);
                        }
                        else
                            throw new NotExistingKeyException();


                    }
                    catch (NumberFormatException err)
                    {
                        System.err.println(err);
                    } });

            } catch (IOException e)
            {
                e.printStackTrace();
            }




    }


    private void writeToFile()
    {
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(fileName)))
        {
            for(Nota nota : getAll())
            {
                writer.write("" + nota.getID().getKey() + "|" + nota.getID().getValue() + "|" + nota.getNota() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void add(Nota nota)
    {
        super.add(nota);
        writeToFile();
    }

    @Override
    public Optional<Nota> delete(Pair<Integer,Integer> id)
    {
        Optional<Nota> temp = super.delete(id);
        writeToFile();
        return temp;
    }




    @Override
    public void update(Nota entitate) {
        super.update(entitate);
        writeToFile();
    }


}
