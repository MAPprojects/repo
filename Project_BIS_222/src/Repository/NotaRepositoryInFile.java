package Repository;

import Domain.Nota;
import Domain.Student;
import Domain.Tema;
import ValidatorsAndExceptions.Validator;
import javafx.util.Pair;

import java.io.*;

public class NotaRepositoryInFile extends AbstractRepositoryInFile<Nota,Pair<Integer,Integer>> {
    private String fileName;

    public NotaRepositoryInFile(String fileName, Validator<Nota> validator) {
        super(fileName,validator);
    }

    @Override
    public void createInstance(String linie) {
        String[] atribute = linie.split("[|]");
        if (atribute.length != 4) {
            System.err.println("Linie incorecta" + linie);
            return;
        }
        try
        {
            int idStudent = Integer.parseInt(atribute[0]);
            int idTema = Integer.parseInt(atribute[1]);
            int valoare = Integer.parseInt(atribute[2]);
            int saptPredare = Integer.parseInt(atribute[3]);
            Student student=new Student(idStudent);
            Tema tema=new Tema(idTema);
            Nota nota = new Nota(student, tema, valoare,saptPredare);
            super.add(nota);
        } catch (NumberFormatException numberEx) {
            System.err.println(numberEx);
        }
    }

    @Override
    public void writeInstace(BufferedWriter writer, Nota nota)
    {
        try
        {
            writer.write("" + nota.getStudent().getID() + "|" + nota.getTema().getID() + "|" + nota.getValoare() +"|"+nota.getSaptPredare()+ "\n");
        } catch (IOException e)
        {
            System.err.println("Can not write to file");
        }
    }
}
