package Repository;

import Domain.HasID;
import Domain.Student;
import ValidatorsAndExceptions.Validator;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.stream.Stream;

public abstract class AbstractRepositoryInFile<E extends HasID<ID>,ID> extends AbstractRepository<E,ID>
{
    private String fileName;

    public AbstractRepositoryInFile(String filename, Validator<E>validator)
    {
        super(validator);
        this.fileName=filename;
        loading();
    }
    public abstract void createInstance(String line);
    public abstract void writeInstace(BufferedWriter writer, E instance);

    private void loading()
    {
        Path path= Paths.get(fileName);
        Stream<String> lines;
        try
        {
            lines= Files.lines(path);
            lines.forEach(instance->createInstance(instance));
        }catch (IOException e)
        {
            System.err.println("Can't load data from file\n");
        }
    }
    private void writeToFile()
    {
        try(BufferedWriter writer=new BufferedWriter(new FileWriter(fileName)))
        {
            for(E instance:getAll())
            {
                writeInstace(writer,instance);
            }

        } catch (IOException e)
        {
            System.err.println(e);
        }

    }
    @Override
    public void add(E entity)
    {
        super.add(entity);
        writeToFile();
    }
    @Override
    public E delete(ID id)
    {
        E old=super.delete(id);
        writeToFile();
        return old;
    }
    @Override
    public void update(E entity)
    {
        super.update(entity);
        writeToFile();
    }
}
