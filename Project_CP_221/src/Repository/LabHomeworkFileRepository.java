package Repository;

import Domain.LabHomework;
import Validator.Validator;
import Validator.ValidatorException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class LabHomeworkFileRepository extends AbstractRepository<Integer,LabHomework> {
    protected String fileName;
    public LabHomeworkFileRepository(String fn, Validator<LabHomework> val)
    {
        super(val);
        fileName=fn;
        //loadToFile();
        streamLoading();
        //loadingHomework();
    }
    private LabHomework parseHomework(String line)
    {
        String[] s=line.split("[|]");
        try
        {int id=Integer.parseInt(s[0]),deadline=Integer.parseInt(s[1]);
        return new LabHomework(id,deadline,s[2]);}
        catch (NumberFormatException ex)
        {
            System.err.println("Error when parsing the file "+fileName);
            return null;
        }
    }

    private List<LabHomework> loadingHomework()
    {
        try(Stream<String> labs= Files.lines(Paths.get(fileName)))
        {
            if(labs.toString().compareTo("")==0)
                return null;
            return labs.map(x->parseHomework(x)).collect(Collectors.toList());
        }
        catch(IOException ex)
        {
            System.err.println("Error when parsing the file "+fileName);
            return null;
        }
    }

    private void streamLoading()
    {
        List<LabHomework> l=loadingHomework();
        l.forEach(x -> {
            try {
                save(x);
            } catch (ValidatorException e) {
                e.printStackTrace();
            }
        });
    }
    private void loadToFile()
    {
        try(BufferedReader b=new BufferedReader(new FileReader(fileName)))
        {
            String s;
            while((s=b.readLine())!=null)
            {
                String values[]=s.split("[|]");
                if(values.length!=3)
                {
                    System.err.println("Incorrect number of argument in file "+fileName);
                    continue;
                }
                try
                {
                    int id=Integer.parseInt(values[0]);
                    int deadline=Integer.parseInt(values[1]);
                    super.save(new LabHomework(id,deadline,values[2]));
                }
                catch(NumberFormatException exc)
                {
                    System.err.println("Inconsistent integer data in file "+fileName);
                } catch (ValidatorException e) {
                    e.printStackTrace();
                }
            }
        }
        catch(IOException exc) {
            System.err.println("Error when trying to open the file "+fileName+" for reading");
        }

    }
    private void writeToFile()
    {
        try(PrintWriter b=new PrintWriter(new FileWriter(fileName)))
        {
            for(LabHomework lh:getAll())
                b.println(""+lh.getId()+"|"+lh.getDeadline()+"|"+lh.getDescription());
        }
        catch (IOException exc)
        {
            System.err.println("Error when trying to open the file "+fileName+" for writing");
        }

    }
    public void save(LabHomework lh) throws ValidatorException {
        super.save(lh);
        writeToFile();
    }

    public void delete(Integer id)
    {
        super.delete(id);
        writeToFile();
    }

    public void update(Integer id,LabHomework lh)
    {
        super.update(id,lh);
        writeToFile();
    }
}
