package Repository;

import Domain.Penalizare;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

public class RepoPenalizare {
    String filename;
    ArrayList<Penalizare> TemePenalizate=new ArrayList<>();
    public RepoPenalizare(String filename) {

        this.filename = filename;
        loadData();
    }

    /**
     * incarca datele din fisierul Teme.txt in repo din memorie
     */
    public void loadData() {

        Path path= Paths.get(filename);
        Stream<String> lines;
        try {
            lines= Files.lines(path);
            lines.forEach(ln->{
                String[] s=ln.split(";");
                if(s.length==3){
                    try {
                        save(new Penalizare(Integer.parseInt(s[0]),Integer.parseInt(s[1])));
                    } catch (ValidationException e) {
                        e.printStackTrace();
                    }
                }else
                    System.out.println("linie corupta in fisierul teme.txt");});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param t-tema care va fi scris ain fisierul Teme.txt
     */
    public void saveToFile(Penalizare t) {
        try (BufferedWriter out = new BufferedWriter(new FileWriter(filename, true))) {
            out.write(t.toString());
            out.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void save(Penalizare t) throws ValidationException {
        TemePenalizate.add(t);
        if (t != null) {
            saveToFile(t);

        }
    }

    /**
     * rescrie fisierul dupa modificari
     */
    private void writeToFile(){
        try(BufferedWriter br = new  BufferedWriter(new FileWriter(filename))){
            for(Penalizare x:TemePenalizate)
                br.write(x.toString()+"\n");
            br.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }





}
