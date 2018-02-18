package Repository;

import Domain.Student;
import Domain.Tema;
import ValidatorsAndExceptions.Validator;

import java.io.*;

public class TemaRepositoryInFile extends AbstractRepositoryInFile<Tema,Integer>
{
    private String fileName;
    public TemaRepositoryInFile(String fileName, Validator<Tema> validator)
    {
        super(fileName,validator);
    }

    @Override
    public void createInstance(String linie) {
        String[] atribute=linie.split("[|]");
        if(atribute.length!=3)
        {
            System.err.println("Linie invalida"+linie);
            return;
        }
        try
        {
            int id=Integer.parseInt(atribute[0]);
            int deadline=Integer.parseInt(atribute[2]);
            String descriere=atribute[1];
            Tema tema=new Tema(id,descriere,deadline);
            super.add(tema);
        }
        catch (NumberFormatException numberException)
        {
            System.err.println(numberException);
        }
    }

    @Override
    public void writeInstace(BufferedWriter writer, Tema tema) {

        try
        {
            writer.write("" + tema.getID() + '|' + tema.getDescriere() + '|' + tema.getDeadline() + '\n');
        }catch (IOException e)
        {
            System.err.println("Can not write to file\n");
        }
    }
}
