package Repository;

import Domain.Studenti;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.stream.Stream;

public class StudentRepoFIle extends StudentRepo {
    String filename;

    public StudentRepoFIle(Validator<Studenti> vali, String filename) {
        super(vali);
        this.filename = filename;
        loadData();
    }

    /**
     * Se incarca datele din fisierul Studenti.txt in memorie
     */
    public void loadData() {
//        try (BufferedReader in = new BufferedReader(new FileReader(filename))) {
//            String line;
//            while ((line = in.readLine()) != null) {
//
//                Studenti t = new Studenti(line.split(";"));
//                super.save(t);
//            }
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (ValidationException e) {
//            e.printStackTrace();
//        }
        Path path= Paths.get(filename);
        Stream<String> lines;
        try {
            lines= Files.lines(path);
            lines.forEach(ln->{
                String[] s=ln.split(";");
                if(s.length==5){
                try {
                    super.save(new Studenti(Integer.parseInt(s[0]),s[1],Integer.parseInt(s[2]),s[3],s[4]));
                } catch (ValidationException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                }
            else
                    System.out.println("linie corupta in  fisierul studenti.txt!");}
            );
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     *
     * @param t-obiectul student care urmeaza a fi salvat in fisierul Studenti.txt
     */
    public void saveToFile(Studenti t) {
        try (BufferedWriter out = new BufferedWriter(new FileWriter(filename, true))) {
            out.write(t.toString());
            out.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Se rescrie fisierul dupa moddificarile facute
     */
    private void writeToFile(){
        try(BufferedWriter br = new  BufferedWriter(new FileWriter(filename))){
            for(Studenti x:getAll())
                br.write(x.toString()+"\n");
            br.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     *
     * @param t-obiectul Student care urmeaza a fi salvat
     * @throws ValidationException
     */
    @Override
    public void save(Studenti t) throws ValidationException, SQLException {
        super.save(t);
        if (t != null) {
            saveToFile(t);

        }
    }

    /**
     *
     * @param t- Stundetul care urmeaza a fi inlocuit cu Studentul t2
     * @param t1-studentul nou creat pentru update
     * @throws ValidationException
     */
    public  void update(Studenti t ,Studenti t1) throws ValidationException, SQLException {
        super.update(t,t1);
        writeToFile();
    }

    /**
     *
     * @param t-obiectul student ce urmeaza a fi sters
     */
    public  void delete(Studenti t) throws SQLException {
        super.delete(t);
        writeToFile();
    }
}
