package Repositories;

import Domain.Nota;
import Validators.IValidator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
//numesugestiv
public class NoteFileRepository extends AbstractFileRepository<Nota,Integer> {
    public NoteFileRepository(String fileName, IValidator<Nota> _iv){
        super(fileName,_iv);
    }
    protected Nota Linie(String line){
        String[] s=line.split("[|]");
        if(s.length!=4){
            return null;
        }
        try{
            int id=Integer.parseInt(s[0]);
            int idStudent=Integer.parseInt(s[1]);
            int idTema=Integer.parseInt(s[2]);
            int valoare=Integer.parseInt(s[3]);
            Nota nota=new Nota(id,idStudent,idTema,valoare);
            return nota;
        }catch (NumberFormatException e){System.err.println(e);}
        return null;
    }
    protected String toLinie(Nota e){
        String s=""+e.getId()+"|"+e.getIdStudent()+"|"+e.getIdTema()+"|"+e.getValoare()+"\n";
        return s;
    }
    public void update(Integer id,String valoareS,String saptamanaPredareS){
        findOne(id).setValoare(Integer.parseInt(valoareS));
        super.update(id,valoareS,saptamanaPredareS);
    }
}