package Repositories;

import Domain.Studenti;
import Validators.IValidator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class StudentiFileRepository extends AbstractFileRepository<Studenti,Integer> {
    public StudentiFileRepository(String fileName, IValidator<Studenti> _iv){
        super(fileName,_iv);
    }
    protected Studenti Linie(String line){
                String[] s=line.split("[|]");
                if(s.length!=5){
                    return null;
                }
                try{
                    int id=Integer.parseInt(s[0]);
                    int grupa=Integer.parseInt(s[2]);
                    Studenti stud=new Studenti(id,s[1],grupa,s[3],s[4]);
                    return stud;
                }catch (NumberFormatException e){System.err.println(e);}
                return null;
        }

    protected String toLinie(Studenti e){
        String s=""+e.getId()+"|"+e.getNume()+"|"+e.getGrupa()+"|"+e.getEmail()+"|"+e.getCadruDidactic()+"\n";
        return s;
    }
    public void update(Integer id,String filtru,String valoare){
        System.out.println(filtru);
        if(filtru.equals("nume")){
            System.out.println("Intru nume");
            findOne(id).setNume(valoare);
        }
        else if(filtru.equals("grupa")){
            System.out.println("Intru grupa");
            findOne(id).setGrupa(Integer.parseInt(valoare));
        }
        else if(filtru.equals("e-mail")){
            System.out.println("Intru e-mail");
            findOne(id).setEmail(valoare);
        }
        else if(filtru.equals("cadru didactic")){
            System.out.println("Intru cd");
            findOne(id).setCadruDidactic(valoare);
        }
        else
            throw new RepositoryException("Acest atribut al studentului nu exista");

        super.update(id,filtru,valoare);
    }

}