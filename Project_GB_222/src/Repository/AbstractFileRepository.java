package Repository;

import Domain.HasID;
import Domain.Student;
import ExceptionsAndValidators.IValidator;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.stream.Stream;

public abstract class AbstractFileRepository<T extends HasID<ID>,ID> extends AbstractRepository<T,ID>
{
    private String filename;
    public AbstractFileRepository(String filename, IValidator<T> validator)
    {
        super(validator);
        this.filename=filename;
        load();
    }

    private void load()
    {
        Path path = Paths.get(filename);
        Stream<String> lines;
        try
        {
            lines = Files.lines(path); //Files – helper class
            lines.forEach(s -> { createObject(s); });

        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void writeToFile()
    {
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(filename)))
        {
            for(T type : getAll())
            {
                writeObject(writer,type);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void add(T type)
    {
        super.add(type);
        writeToFile();
    }

    @Override
    public Optional<T> delete(ID id)
    {
        Optional<T> temp = super.delete(id);
        if(temp.isPresent())
            writeToFile();
        return temp;
    }

    @Override
    public void update(T type)
    {
        super.update(type);
        writeToFile();
    }

    public abstract void createObject(String s);

    public abstract void writeObject(BufferedWriter writer,T type);
}
