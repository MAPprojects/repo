package Repository;
import Domain.HasID;
import Domain.Student;
import Domain.Validator;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StudentFileRepository extends AbstractFileRepository<Student,Integer> {
    public StudentFileRepository(String numeFisier, Validator<Student> v){
        super(numeFisier,v);
    }

    @Override
    protected void loading(){
        try(Stream<String> s=Files.lines(Paths.get(fileName))){
            s.forEach(x->{
                String[] str=x.split("[|]");
                if(str.length!=5){
                    System.out.println("Linie invalida "+str);
                }
                try{
                    int id=Integer.parseInt(str[0]);
                    Student st=new Student(id,str[1],str[2],str[3],str[4]);
                    super.save(st);
                }catch(NumberFormatException e){
                    System.err.println("Id nevalid");
                }catch (RepositoryException re){
                    System.out.println(re);
                }
            });
        }catch(IOException ioe){
            System.err.println(ioe);
        }
    }

    protected void loadingStream(){
        List<Integer> list=new ArrayList<>();
        list.add(Integer.valueOf(1));
        list.add(Integer.valueOf(2));
        Map<Integer,Integer> ent=list.stream().collect(Collectors.toMap(y->y,y->y));
        for(Integer i:ent.values()){
            System.out.println(i);
        }
    }


    public static Student strToStudent(String str){
        String[] s=str.split("[|]");
        int id=Integer.parseInt(s[0]);
        return new Student(id,s[1],s[2],s[3],s[4]);
    }

    @Override
    protected void writeToFile(){
        try(PrintWriter pw=new PrintWriter(fileName)){
            for(Student st:findAll()){
                String stF=""+st.getId()+"|"+st.getNume()+"|"+st.getGrupa()+"|"+st.getEmail()+"|"+st.getCadru();
                pw.println(stF);
            }
        }catch (IOException ioe){
            System.err.println(ioe);
        }
    }

}
