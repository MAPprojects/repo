package Repositories;

import Domain.Nota;
import Exceptions.RepositoryException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public class NotaFileRepo extends NotaRepository{

    private String fileName;
    private Map<Integer, Nota> marks;

    public NotaFileRepo(String fileName) {
        super();
        this.fileName = fileName;
        marks = readFromFile(fileName);
        setStorage(marks);
    }

    @Override
    public int size() {
        return super.size();
    }

    @Override
    public Optional<Nota> save(Nota entitate) throws RepositoryException {
        Nota savedMark = super.save(entitate).get();
        saveToFile(fileName);
        return Optional.ofNullable(savedMark);
    }

    @Override
    public Optional<Nota> delete(Integer id_entitate) throws RepositoryException {
        Nota deletedMark = super.delete(id_entitate).get();
        saveToFile(fileName);
        return Optional.ofNullable(deletedMark);
    }

    @Override
    public Optional<Nota> update(Integer id_entitate, Nota entitate) throws RepositoryException {
        Nota updatedMark = super.update(id_entitate,entitate).get();
        saveToFile(fileName);
        return Optional.ofNullable(updatedMark);
    }

    @Override
    public Optional<Nota> findEntity(Integer id_entitate) throws RepositoryException {
        return super.findEntity(id_entitate);
    }

    @Override
    public Iterable<Nota> findAll() {
        return super.findAll();
    }

    @Override
    public void setStorage(Map<Integer, Nota> newStorage) {
        super.setStorage(newStorage);
    }

    protected Map<Integer, Nota> readFromFile(String fileName) {
        Map allMarks = new HashMap<Integer, Nota>();
        Stream<String> lines;
        Path filePath = Paths.get(fileName);
        try {
            lines = Files.lines(filePath);
            lines.forEach(line -> {
                String[] info = line.split(",");
                int id = Integer.valueOf(info[0]);
                int studentID = Integer.valueOf(info[1]);
                int nrTema = Integer.valueOf(info[2]);
                int vnota = Integer.valueOf(info[3]);

                Nota nota = new Nota(studentID, nrTema, vnota);

                allMarks.put(nota.getID(), nota);
            });
            /*BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String linie;
            linie = reader.readLine();

            while (linie != null) {
                String[] info = linie.split(",");
                int id = Integer.valueOf(info[0]);
                int studentID = Integer.valueOf(info[1]);
                int nrTema = Integer.valueOf(info[2]);
                int vnota = Integer.valueOf(info[3]);

                Nota nota = new Nota(studentID, nrTema, vnota);

                allMarks.put(nota.getID(), nota);

                linie = reader.readLine();
            }*/
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return allMarks;
    }

    protected void saveToFile(String fileName) {
        try {
            File file = new File(fileName);
            if(!file.exists())
                file.createNewFile();
            FileWriter fw = new FileWriter(file,false);
            BufferedWriter bw = new BufferedWriter(fw);
            for (Nota nota : findAll()) {
                bw.write(nota.getID() + "," + nota.getIdStudent() + "," + nota.getNrTema() + "," + nota.getNota());
                bw.newLine();
            }
            bw.close();
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
