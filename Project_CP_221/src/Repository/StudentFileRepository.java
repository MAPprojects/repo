package Repository;

import Domain.Student;
import Utils.Groups;
import Utils.Names;
import Utils.Professors;
import Utils.Surname;
import Validator.Validator;
import Validator.ValidatorException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StudentFileRepository extends AbstractRepository<Integer, Student> {
    private String fileName;
    public StudentFileRepository(String fn,Validator<Student> val)
    {
        super(val);
        fileName=fn;
        //loadFromFile();
       /* try {
            populate();
        } catch (ValidatorException e) {
            e.printStackTrace();
        }*/
        streamLoading();
    }

    private void populate() throws ValidatorException {
        for(int i=1;i<=100;i++)
        {
            Random generator=new Random();
            String name1= Names.values()[generator.nextInt(Names.values().length-1)].toString();
            String name2= Surname.values()[generator.nextInt(Surname.values().length-1)].toString();
            String email=name1+"."+name2+"@yahoo.com";
            String prof= Professors.values()[generator.nextInt(Professors.values().length-1)].toString();
            String grp= Groups.values()[generator.nextInt(Groups.values().length-1)].toString();
            super.save(new Student(i,name1+" "+name2,email,grp,prof));
        }
    }
    private Student parseStudent(String line)
    {
        String[]s=line.split("[|]");
        try
        {
            int id=Integer.parseInt(s[0]);
            //double grade=Integer.parseInt(s[5]);
            //int numberGr=Integer.parseInt(s[6]);
            return new Student(id,s[1],s[2],s[3],s[4]);
        }
        catch(NumberFormatException ex)
        {
            System.err.println("Error when parsing the file "+this.fileName);
            return  null;
        }
    }

    private List<Student> loadingStudents() {
        try (Stream<String> sts= Files.lines(Paths.get(this.fileName))) {
                List<Student> l=sts.map(x->parseStudent(x)).collect(Collectors.toList());
                return l;
        }
        catch(IOException ex)
        {
            System.err.println("Error occured when parsing the file "+fileName);
            return  null;
        }
    }


    private void streamLoading()
    {
        List<Student> rez=loadingStudents();
        rez.forEach(x -> {
            try {
                save(x);
            } catch (ValidatorException e) {
                e.printStackTrace();
            }
        });
    }

    private void loadFromFile() {
        try
        {
            BufferedReader b=new BufferedReader(new FileReader(fileName));
            String line;
            while((line=b.readLine())!=null)
            {
                   String []values=line.split("[|]");
                   if(values.length!=5)
                   {
                       System.err.println("Error when parsing the file "+fileName);
                       continue;
                   }
                   try
                   {
                       int id=Integer.parseInt(values[0]);

                       //double grade=Integer.parseInt(values[5]);
                       //int numberGr=Integer.parseInt(values[6]);
                       Student st=new Student(id,values[1],values[2],values[3],values[4]);
                       super.save(st);
                   }
                   catch (NumberFormatException exc)
                   {
                       System.err.println("Error when parsing id filed from the file "+fileName);
                   }
                   catch (ValidatorException e) {
                       e.printStackTrace();
                   }

            }
        }
        catch(IOException ex)
        {
            System.err.println("Error when trying to open the file "+fileName);
        }
    }
    private void writeToFile()
    {
        try(PrintWriter pr=new PrintWriter(fileName))
        {
            for(Student s:getAll()) {
                pr.println(  "" + s.getId() + "|" + s.getName() + "|" + s.getEmail() + "|" + s.getGroup() + "|" + s.getProfessor());
            }
        }
        catch(IOException exc)
        {
            throw new RepositoryException();
        }

    }
    public void save(Student st) throws ValidatorException {
        super.save(st);
        writeToFile();
    }
    public void delete(Integer id)
    {
        super.delete(id);
        writeToFile();
    }
    public void update(Integer id,Student st)
    {
        super.update(id,st);
        writeToFile();
    }
}
