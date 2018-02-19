package repository;

import entities.Candidat;
import validator.Validator;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class CandidatRepositoryFile extends repository.CandidatRepository {
    private String numeFisier;
    public CandidatRepositoryFile(Validator<Candidat> validator, String numeFisier) {
        super(validator);
        this.numeFisier=numeFisier;
        loadFromFile();
    }

    private void loadFromFile(){
        Path path = Paths.get(numeFisier);
        Stream<String> lines;
        try {
            lines = Files.lines(path);
            lines.forEach(s -> { Candidat c=strToCandidat(s);
                if(c!=null){
                    super.save(c);
                }
            } );
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    private Candidat strToCandidat(String linie) {
        String[] s=linie.split("[|]");
        if(s.length!=4){
            System.err.println("Linie invalida "+linie);
            return null;
        }
        try{
            int id=Integer.parseInt(s[0]);
            return new Candidat(id,s[1],s[2],s[3]);
        }
        catch (NumberFormatException e){
            System.err.println("Linie invalida "+linie);
        }
        return null;
    }

    private void writeToFile(){
        try(PrintWriter pr=new PrintWriter(numeFisier)){
            for(Candidat c:super.getAll()){
                String candidatString=""+c.getID()+"|"+c.getNume()+"|"+c.getTelefon()+"|"+c.getEmail();
                pr.println(candidatString);
            }
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    @Override
    public void save(Candidat c){
        super.save(c);
        writeToFile();
    }

    @Override
    public void update(Integer id,Candidat c){
        super.update(id,c);
        writeToFile();
    }

    @Override
    public Candidat delete(Integer id){
        Candidat c=super.delete(id);
        writeToFile();
        return c;
    }

}
