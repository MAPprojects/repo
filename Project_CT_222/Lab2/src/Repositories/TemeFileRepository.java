package Repositories;

import Domain.Teme;
import Validators.IValidator;
import Validators.ValidatorException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;

public class TemeFileRepository extends AbstractFileRepository<Teme,Integer> {
    public TemeFileRepository(String fileName, IValidator<Teme> _iv){
        super(fileName,_iv);
    }
    protected Teme Linie(String s1){
        String[] s=s1.split("[|]");
        if(s.length!=3){
            return null;
        }
        try{
            int id=Integer.parseInt(s[0]);
            int deadline=Integer.parseInt(s[2]);
            Teme tema=new Teme(id,s[1],deadline);
            return tema;
        }catch (NumberFormatException e){System.err.println(e);}
        return null;
    }
    protected String toLinie(Teme tema){
        String s=""+tema.getId()+"|"+tema.getCerinta()+"|"+tema.getDeadline()+"\n";
        return s;
    }
    public void update(Integer id,String filtru,String valoare){
        if(filtru.equals("cerinta")){
            findOne(id).setCerinta(valoare);
        }
        else if(filtru.equals("deadline")){
            if(Integer.parseInt(valoare)<2||Integer.parseInt(valoare)>14){
                throw new ValidatorException("Tema deadline "+Integer.parseInt(valoare)+" trebuie sa fie cuprinsa intre 2 si 14");
            }
            Teme tema=findOne(id);
            Calendar calendar= Calendar.getInstance();

            if(tema.getDeadline()<=(calendar.get(calendar.WEEK_OF_YEAR)-40))
                throw new RepositoryException("S-a depasit termenul admis: "+tema.getDeadline());
            else if(tema.getDeadline()>Integer.parseInt(valoare))
                throw new RepositoryException("Nu se poate modifica deadlineul");
            else {
                tema.setDeadline(Integer.parseInt(valoare));
            }
        }
        else
            throw new RepositoryException("Acest atribut al temei nu exista");
        super.update(id,filtru,valoare);
    }
}
