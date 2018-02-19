package repository;

import entities.Sectie;
import validator.Validator;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class SectieRepositoryFile extends SectieRepository {

    private String numeFisier;
    public SectieRepositoryFile(Validator<Sectie> validator, String numeFisier) {
        super(validator);
        this.numeFisier=numeFisier;
        loadFromFile();
    }

    private void loadFromFile(){
        Path path = Paths.get(numeFisier);
        Stream<String> lines;
        try {
            lines = Files.lines(path);
            lines.forEach(s -> { Sectie se=strToSectie(s);
                if(se!=null){
                    super.save(se);
                }
            } );
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    private Sectie strToSectie(String linie) {
        String[] s=linie.split("[|]");
        if(s.length!=3){
            System.err.println("Linie invalida "+linie);
            return null;
        }
        try{
            int id=Integer.parseInt(s[0]);
            int nrLocuri=Integer.parseInt(s[2]);
            return new Sectie(id,s[1],nrLocuri);
        }
        catch (NumberFormatException e){
            System.err.println("Linie invalida "+linie);
        }
        return null;
    }

    private void writeToFile(){
        try(PrintWriter pr=new PrintWriter(numeFisier)){
            for(Sectie c:super.getAll()){
                String sectieString=""+c.getID()+"|"+c.getNume()+"|"+c.getNrLoc();
                pr.println(sectieString);
            }
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    @Override
    public void save(Sectie s){
        super.save(s);
        writeToFile();
    }

    @Override
    public void update(Integer id, Sectie s){
        super.update(id,s);
        writeToFile();
    }

    @Override
    public Sectie delete(Integer id){
        Sectie s=super.delete(id);
        writeToFile();
        return s;
    }

}
