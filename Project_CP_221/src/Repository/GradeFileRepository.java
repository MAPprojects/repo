package Repository;

import Domain.Grade;
import Domain.Student;
import Validator.Validator;
import Validator.ValidatorException;
import javafx.util.Pair;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GradeFileRepository extends AbstractRepository<Pair<Integer,Integer>, Grade> {
    public String fileName;

    public GradeFileRepository(String fileName,Validator<Grade> val) {
        super(val);
        this.fileName = fileName;
        //loadFromFile();
        //loadingGrades();
        streamLoading();
    }
    private Grade parseGrade(String line)
    {
        String[]s=line.split("[|]");
        int ids,idh,grade;
        try
        {ids=Integer.parseInt(s[0]);
        idh=Integer.parseInt(s[1]);
        grade=Integer.parseInt(s[2]);
        return new Grade(ids,idh,grade);}
        catch(NumberFormatException ex)
        {
            System.err.println("Error when parsing the file "+fileName);
            return null;
        }
    }

    private List<Grade> loadingGrades()
    {
        try(Stream<String> gr= Files.lines(Paths.get(fileName)))
        {
            if(gr.toString().compareTo("")==0)
                return null;
            return gr.map(x->parseGrade(x)).collect(Collectors.toList());
        }
        catch(IOException ex)
        {
            System.err.println("Error when parsing the file "+fileName);
            return null;
        }
    }

    private void streamLoading()
    {
        List<Grade> rez=loadingGrades();
        rez.forEach(x -> {
            try {
                save(x);
            } catch (ValidatorException e) {
                e.printStackTrace();
            }
        });
    }

    private void loadFromFile()
    {
         try(BufferedReader b=new BufferedReader(new FileReader(fileName)))
         {
             String info;
             while((info=b.readLine())!=null)
             {
                 try
                 {
                     String []infos=info.split("[|]");
                     if(infos.length!=3)
                     {
                         System.err.println("Invalid number of arguments in file "+fileName);
                         continue;
                     }
                     int ids,idh,grade;
                     ids=Integer.parseInt(infos[0]);
                     idh=Integer.parseInt(infos[1]);
                     grade=Integer.parseInt(infos[2]);
                     super.save(new Grade(ids,idh,grade));
                 }
                 catch (NumberFormatException exc)
                 {
                     System.err.println("Not invalid argument for one field in file "+fileName);
                 } catch (ValidatorException e) {
                     e.printStackTrace();
                 }
             }
         }
         catch (IOException exc)
         {
             System.err.println("Error when trying to open the file "+fileName);
         }

    }

    private void writeToFile()
    {
        try(PrintWriter pw=new PrintWriter(new FileWriter(fileName)))
        {
            for (Grade g:getAll()) {
                pw.println(g.toString());
            }
        }
        catch(IOException exc)
        {
            System.err.println("Error when parsing the file "+fileName);
        }
    }
    public void save(Grade g) throws ValidatorException {
        super.save(g);
        writeToFile();
    }
    public void delete(Pair<Integer,Integer> id)
    {
        super.delete(id);
        writeToFile();
    }
    public void update(Pair<Integer,Integer> id,Grade g)
    {
        super.update(id,g);
        System.out.println("facut update repograde");
        writeToFile();
    }
}
