package repository.fileRepo;

import entities.Tema;
import repository.AbstractCrudRepo;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

public class TemaFileRepo extends AbstractCrudRepo<Tema, String> {

    private static String FILE_PATH;

    /**
     * Constructor al clasei TemaFileRepo
     *
     * @param filePath - String -> path catre fisierul pe care dorim sa il utilizam
     * @throws IOException Arunca exceptie daca nu exista fisierul in care il salvam
     */
    public TemaFileRepo(String filePath) throws IOException {
        super(Tema.class);
        this.FILE_PATH = filePath;
        //loadData();
    }

    /**
     * Salveaza o tema in repo is in fisier ulterior
     *
     * @param entity E
     * @return Returneaza Entitatea pe care  asalvt-o
     * @throws IOException Arunca exceptie daca nu exista fisierul in care il salvam
     */
    @Override
    public Optional<Tema> save(Tema entity) throws IOException {
        super.save(entity);
        saveData();
        return Optional.ofNullable(entity);
    }

    /**
     * @param id Integer -> id-ul temei pe care dorim sa o stergem
     * @return Tema -> Entitatea pe care a salvat-o
     * @throws IOException Arunca exceptie daca nu exista fisierul in care il salvam
     */
    @Override
    public Optional<Tema> delete(String id) throws IOException {
        Tema t = super.delete(id).get();
        saveData();
        return Optional.ofNullable(t);
    }

    /**
     * @param id -> id-ul temei pe care dorim sa o modificam
     * @param entity  E -> the new entity to be added in the repository
     * @return Tema -> entitatea pe care a modificat-o
     * @throws IOException Arunca exceptie daca nu exista fisierul in care il salvam
     */
    @Override
    public Optional<Tema> update(String id, Tema entity) throws IOException {
        Tema t = super.update(id, entity).get();
        saveData();
        return Optional.ofNullable(t);
    }

    /**
     * Populeaza repo-ul cu o temele din lista
     *
     * @param elements Collection<E> -> o colectie de elemente de tipul generic E
     * @throws IOException Arunca exceptie daca nu exista fisierul in care il salvam
     */
    @Override
    public void populate(Collection<Tema> elements) throws IOException {
        for (Tema tema : elements) {
            this.save(tema);
        }
    }

    /**
     * Incarca din fisier temele
     *
     * @throws IOException Arunca exceptie daca nu exista fisierul in care il salvam
     */
    @Override
    public void loadData() throws IOException {
        Path path = Paths.get(FILE_PATH);
        final Boolean[] invalidFile = {false};
        Stream<String> lines;
        lines = Files.lines(path);
        lines.forEach((line) -> {
            String[] formatedString = line.split(",");
            try {
                if(formatedString.length != 3){
                    invalidFile[0] = true;
                }
                else {
                    super.save(new Tema(formatedString[0], formatedString[1],
                            Integer.parseInt(formatedString[2])));
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        });
        if(invalidFile[0] == true){
            throw new IOException("Fisierul teme este corupt!");
        }
//        BufferedReader br = new BufferedReader(new FileReader(FILE_PATH));
//        String line = br.readLine();
//        while (line != null) {
//            String[] formatedString = line.split(",");
//            super.save(new Tema(Integer.parseInt(formatedString[0]), formatedString[1],
//                    Integer.parseInt(formatedString[2])));
//            line = br.readLine();
//        }
//        br.close();
    }

    /**
     * Scrie in fisier temele
     *
     * @throws IOException Arunca exceptie daca nu exista fisierul in care il salvam
     */
    @Override
    public void saveData() throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH));
        ArrayList<Tema> teme = new ArrayList<Tema>((Collection<? extends Tema>) super.findAll());
        for (Tema tema : teme) {
            String line = tema.getId() + "," + tema.getCerinta() + "," + tema.getTermenPredare() + "\n";
            bw.write(line);
        }
        bw.close();
    }
}
