package Service;

import java.io.*;

public class Export {
    protected String fileName;
    public Export(String file){
        this.fileName=file;
    }
    public void export(String Operatie,Integer NrTema,Integer Nota,Integer Deadline,Integer SaptamanaPredarii,String Observatii){

        try(PrintWriter outputFile = new PrintWriter(new FileWriter(fileName, true))){
            String s="Operatie: "+Operatie+"      Numar Tema: "+NrTema+"      Domain.Nota: "+Nota+"      Deadline: "+Deadline+"      SaptamanaPredare: "+SaptamanaPredarii+"      Observatii: "+Observatii+"\n";
            outputFile.printf(s);
        }catch(IOException e){
            System.err.println("ERR"+e);
        }

    }
}
/*Incercare 1:
try{
        try(BufferedReader br=new BufferedReader(new FileReader(this.fileName))){
            String s="",line;
            while((line=br.readLine())!=null){
                s+=line+"\n";
            }
            try(PrintWriter pw=new PrintWriter(fileName)){
                String s2=""+Operatie+"|"+NrTema+"|"+Domain.Nota+"|"+Deadline+"|"+SaptamanaPredarii+"|"+Observatii;
                s+=s2;
                pw.println(s);
            }catch(IOException e){
                System.err.println("ERR"+e);
            }
        }catch (IOException ioe){
            System.err.println(ioe);
        }}catch (FileNotFoundException e){
            System.err.println(e);
        }
        try(PrintWriter pw=new PrintWriter(fileName)){
                String s=""+Operatie+"|"+NrTema+"|"+Domain.Nota+"|"+Deadline+"|"+SaptamanaPredarii+"|"+Observatii;
                pw.println(s);
        }catch(IOException e){
            System.err.println("ERR"+e);
        }*/



/*De aici am copiat:
try(BufferedReader br=new BufferedReader(new FileReader(this.fileName))){
        String line;
        while((line=br.readLine())!=null){
        String[] s=line.split("[|]");
        if(s.length!=3){
        System.out.println("Linie invalida "+line);
        continue;
        }
        try{
        int id=Integer.parseInt(s[0]);
        int deadline=Integer.parseInt(s[2]);
        Domain.Teme tema=new Domain.Teme(id,s[1],deadline);
        super.save(tema);
        }catch (NumberFormatException e){System.err.println(e);}

        }
        }catch (IOException ioe){
        System.err.println(ioe);
        }*/