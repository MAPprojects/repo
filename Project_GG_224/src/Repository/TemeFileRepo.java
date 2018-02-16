package Repository;

import Domain.Teme;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.stream.Stream;

public class TemeFileRepo extends TemeRepo{
    String filename;

    public TemeFileRepo(Validator<Teme> vali, String filename) {
        super(vali);
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
                    super.save(new Teme(Integer.parseInt(s[0]),Integer.parseInt(s[1]),s[2]));
                } catch (ValidationException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
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
    public void saveToFile(Teme t) {
        try (BufferedWriter out = new BufferedWriter(new FileWriter(filename, true))) {
            out.write(t.toString());
            out.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param t-tema care va fi salvata
     * @throws ValidationException
     */
    @Override
    public void save(Teme t) throws ValidationException, SQLException {
        super.save(t);
        if (t != null) {
            saveToFile(t);

        }
    }

    /**
     * rescrie fisierul dupa modificari
     */
    private void writeToFile(){
        try(BufferedWriter br = new  BufferedWriter(new FileWriter(filename))){
            for(Teme x:getAll())
                br.write(x.toString()+"\n");
            br.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }


    /**
     *
     * @param t-tema care trebuie sa fie updatata
     * @param t1-tema noua care va fi salvata in memmorie
     * @throws ValidationException
     */
    public  void update(Teme t , Teme t1) throws ValidationException, SQLException {
        super.update(t,t1);
        writeToFile();
    }

    /**
     *
     * @param t-tema ce va fi stearsa din repo
     */
    public  void delete(Teme t) throws SQLException {
        super.delete(t);
        writeToFile();
    }

}
