package Repository;

import Domain.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.stream.Stream;

public class NoteFileRepo extends NoteRepository {
    String filename;



    IRepository<Teme> repoTeme = new TemeFileRepo(new TemeValidator(), "./src/Teme.txt");

    public NoteFileRepo(Validator<Note> vali,String filename) {
        super(vali);
        this.filename=filename;
        loadData();
    };

    /**
     * incarca datele din fisierul Catalog.txt  in memorie
     */
    public void loadData() {
//        try (BufferedReader in = new BufferedReader(new FileReader(filename))) {
//            String line;
//            while ((line = in.readLine()) != null) {
//
//                Note t = new Note(line.split(";"));
//                super.save(t);
//            }
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//
//        } catch (ValidationException e) {
//            e.printStackTrace();
//        }
        Path path= Paths.get(filename);
        Stream<String> lines;
        try {
            lines= Files.lines(path);
            lines.forEach(ln->{
                String[] s=ln.split(";");
                if(s.length==3){
                try {
                    super.save(new Note(Integer.parseInt(s[0]),Integer.parseInt(s[1]),Integer.parseInt(s[2])));
                } catch (ValidationException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                }
            else
                    System.out.println("Linie corupta in fisierul catalog.txt");});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param nota-obiectul nota
     * @param observatii
     * @param saptamanaPredare-saptamana in care a fost predata tema
     * @param status-adugare sau modificare
     *  Se adauga in fisierul Catalog.txt si in fisierul separat atribuit fiecarui student dupa id-ul sau
     */
    private void saveToFile(Note nota, String observatii, int saptamanaPredare, String status){
        String filename = Integer.toString(nota.getIdStudent()) + ".txt";
        //saveToCatalaog(nota);
        try(BufferedWriter out = new BufferedWriter(new FileWriter(filename,true))){
            out.write(status+";");
            out.write(Integer.toString(nota.getIdStudent()) +";"+ Integer.toString((nota.getNrTema())) +";"+  Integer.toString(nota.getValoare())+";" +  Integer.toString(repoTeme.getById(nota.getNrTema()).getDeadline()) +";"+ Integer.toString(saptamanaPredare)+";");

            if (!observatii.isEmpty()){
                out.write("Observatii: " + observatii + ";");
                out.newLine();
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }


    /**
     *
     * @param nota-obiectul nota
     * @param observatii
     * @param saptamanaPredare -saptamana in care a fost predata tema
     * @throws ValidationException
     * Se salveaza nota atribuita studentului la o anumite tema in fisier si in memorie cu statusul: Adauga nota
     */
    public void save(Note nota, String observatii, int saptamanaPredare) throws ValidationException, SQLException {
        super.save(nota);
        writeToFile();
        saveToFile(nota,observatii,saptamanaPredare,"Adaugare nota");
    }

    /**
     *
     * @param nota-obiectul nota care urmeaza a fi modificat cu notaNou
     * @param notaNou-noul obiect nota
     * @param observatii
     * @param saptamanaPredare-saptamana in care a fost predata tema
     * @throws ValidationException
     * Se modifica nota si se salveaza in fisier nota cu statusul:Modificare nota
     */
    public void update(Note nota,Note notaNou, String observatii, int saptamanaPredare) throws ValidationException, SQLException {
        super.update(nota,notaNou);
        saveToFile(nota,observatii,saptamanaPredare,"Modificare nota");
        writeToFile();
    }

    /**
     *
     * @param t-Nota ce urmeaza a fi salvata in Catalog
     *
     * se salveaza in fisierul Catalog.txt nota impreuna cu observatiile de rigoare
     */
    public void saveToCatalaog(Note t) {
        try (BufferedWriter out = new BufferedWriter(new FileWriter("./src/Catalog.txt", true))) {

            out.write(t.toString());
            out.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeToFile(){
        try(BufferedWriter br = new  BufferedWriter(new FileWriter(filename))){
            for(Note x:getAll())
                br.write(x.toString()+"\n");
            br.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

}
