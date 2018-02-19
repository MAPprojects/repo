package repository;

import entities.CheieOptiune;
import entities.Optiune;
import validator.Validator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static java.lang.Integer.max;

public class OptiuneRepositoryFile extends OptiuneRepository {
    private String numeFisier;

    public OptiuneRepositoryFile(Validator<Optiune> validator, String fileName) {
        super(validator);
        this.numeFisier=fileName;
        loadFromFile();
    }

    private void loadFromFile(){
        Path path = Paths.get(numeFisier);
        Stream<String> lines;
        try {
            lines = Files.lines(path);
            lines.forEach(s -> { Optiune o=strToOptiune(s);
                if(o!=null){
                    super.save(o);
                }
            } );
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    private Optiune strToOptiune(String linie) {
        String[] s=linie.split("[|]");
        if(s.length!=4){
            System.err.println("Linie invalida "+linie);
            return null;
        }
        try{
            int idOptiune=Integer.parseInt(s[0]);
            int idCandidat=Integer.parseInt(s[1]);
            int idSectie=Integer.parseInt(s[2]);
            int prioritate=Integer.parseInt(s[3]);
            Optiune o=new Optiune(idOptiune,idCandidat,idSectie);
            o.setPrioritate(prioritate);
            return o;
        }
        catch (NumberFormatException e){
            System.err.println("Linie invalida "+linie);
        }
        return null;
    }

    private void writeToFile(){
        try(PrintWriter pr=new PrintWriter(numeFisier)){
            for(Optiune o:super.getAll()){
                String optiuneString=""+o.getIdOptiune()+"|"+o.getIdCandidat()+"|"+o.getIdSectie()+"|"+o.getPrioritate();
                pr.println(optiuneString);
            }
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    private void writeToLogFile(String mesajInitial, Optiune o, String mesajFinal){
        String numeFisierCandidat=""+o.getIdCandidat()+".txt";
        try(PrintWriter pr=new PrintWriter(new FileOutputStream(new File(numeFisierCandidat), true))){

            pr.append(mesajInitial+" optiune la sectia cu ID-ul "+o.getIdSectie());
            if(mesajInitial.equals("Adaugare")){
                pr.append(" cu prioritatea "+o.getPrioritate());
                if(o.getPrioritate()==1){
                    pr.append(" aceasta fiind prima optiune a candidatului.");
                }
                else{
                    pr.append(" prioritate determinata de optiunea anterioara.");
                }
            }
            if(mesajInitial.equals("Update")){
                pr.append(" "+mesajFinal);
            }
            if(mesajInitial.equals("Stergere")){
                pr.append(" cu prioritatea "+o.getPrioritate()+".");
            }
            pr.append(System.lineSeparator());

        } catch (IOException e) {
            System.err.println(e);
        }
    }

    @Override
    public void save(Optiune o){
        super.save(o);
        writeToFile();
        writeToLogFile("Adaugare",o,"");
    }

    private void updateRec(CheieOptiune c, Optiune o, int directieUpdate){
        Optiune aux2=dict.get(c);
        for (Optiune aux : getAll()) {
            if (aux.getIdCandidat() == o.getIdCandidat() && aux.getPrioritate() == aux2.getPrioritate() - directieUpdate) {
                Optiune aux3 = new Optiune(aux.getIdOptiune(), aux.getIdCandidat(), aux.getIdSectie());
                aux3.setPrioritate(aux.getPrioritate() + directieUpdate);
                updateRec(aux3, aux3,directieUpdate);
                break;
            }
        }
        writeToLogFile("Update",o,"din prioritatea "+aux2.getPrioritate()+" in prioritatea "+o.getPrioritate()+".");
        super.update(c,o);
        writeToFile();
    }

    @Override
    public void update(CheieOptiune c, Optiune o){
        Optiune aux2=dict.get(c);
        if(aux2.getPrioritate()<o.getPrioritate()) {
            int maxim = 0;
            for (Optiune aux : getAll()) {
                if (aux.getIdCandidat() == o.getIdCandidat()) {
                    maxim = max(maxim, aux.getPrioritate());
                }
            }
            if (o.getPrioritate() > maxim)
                o.setPrioritate(maxim);
            updateRec(c, o, -1);
        }
        else if(aux2.getPrioritate()>o.getPrioritate()) {
            updateRec(c, o, 1);
        }
    }

    @Override
    public Optiune delete(CheieOptiune c){
        Optiune o=super.delete(c);
        writeToFile();
        writeToLogFile("Stergere",o,"");
        return o;
    }
}
