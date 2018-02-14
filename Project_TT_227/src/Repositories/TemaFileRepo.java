package Repositories;

import Domain.Tema;
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

public class TemaFileRepo extends TemaRepository {

    private String fileName;
    private Map<Integer, Tema> homeworks;

    public TemaFileRepo(String fileName) {
        super();
        this.fileName = fileName;
        homeworks = readFromFile(fileName);
        setStorage(homeworks);
    }

    @Override
    public int size() {
        return super.size();
    }

    @Override
    public Optional<Tema> save(Tema entitate) throws RepositoryException {
        Tema savedHomework = super.save(entitate).get();
        saveToFile(fileName);
        return Optional.ofNullable(savedHomework);
    }

    @Override
    public Optional<Tema> delete(Integer id_entitate) throws RepositoryException {
        Tema deletedHomework = super.delete(id_entitate).get();
        saveToFile(fileName);
        return Optional.ofNullable(deletedHomework);
    }

    @Override
    public Optional<Tema> update(Integer id_entitate, Tema entitate) throws RepositoryException {
        Tema updatedHomework = super.update(id_entitate,entitate).get();
        saveToFile(fileName);
        return Optional.ofNullable(updatedHomework);
    }

    @Override
    public Optional<Tema> findEntity(Integer id_entitate) throws RepositoryException {
        return Optional.ofNullable(super.findEntity(id_entitate).get());
    }

    @Override
    public Iterable<Tema> findAll() {
        return super.findAll();
    }

    @Override
    public void setStorage(Map<Integer, Tema> newStorage) {
        super.setStorage(newStorage);
    }

    protected Map<Integer, Tema> readFromFile(String fileName) {
        Map allHomeworks = new HashMap<Integer, Tema>();
        Stream<String> lines;
        Path filePath = Paths.get(fileName);
        try {
            lines = Files.lines(filePath);
            lines.forEach(line -> {
                String[] info = line.split(",");
                int nrTema = Integer.valueOf(info[0]);
                String descriere = info[1];
                int deadline = Integer.valueOf(info[2]);

                Tema tema = new Tema(nrTema, descriere, deadline);

                allHomeworks.put(tema.getID(), tema);
            });
            /*BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String linie;
            linie = reader.readLine();

            while (linie != null) {
                String[] info = linie.split(",");
                int nrTema = Integer.valueOf(info[0]);
                String descriere = info[1];
                int deadline = Integer.valueOf(info[2]);

                Tema tema = new Tema(nrTema, descriere, deadline);

                allHomeworks.put(tema.getID(), tema);

                linie = reader.readLine();
            }*/
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return allHomeworks;
    }

    protected void saveToFile(String fileName) {
        try {
            File file = new File(fileName);
            if(!file.exists())
                file.createNewFile();
            FileWriter fw = new FileWriter(file,false);
            BufferedWriter bw = new BufferedWriter(fw);
            for (Tema tema : findAll()) {
                bw.write(tema.getID() + "," + tema.getDescriere() + "," + tema.getDeadline());
                bw.newLine();
            }
            bw.close();
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
