package com.company.Repositories;

import com.company.Domain.Nota;
import com.company.Exceptions.RepositoryException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

public class FileNotaRepository extends NotaRepository {

    private String fileName;
    private Path filePath;

    public FileNotaRepository(String fileName) throws RepositoryException {
        super();
        this.fileName = fileName;
        this.filePath = Paths.get(fileName);
        //loadData();
        loadDataStream();
    }

    public void loadDataStream() throws RepositoryException {
        Path path = this.filePath;
        Stream<String> lines;
        try{
            lines =  Files.lines(path);
            lines.forEach(s-> {
                String[] nota = s.split(",");
                try {
                    super.save(new Nota(Integer.parseInt(nota[0]),Integer.parseInt(nota[1]),Integer.parseInt(nota[2])));
                } catch (RepositoryException e) {

                }
            });
        } catch (IOException e) {
            throw new RepositoryException(e.getMessage());
        }
    }

    public void loadData() throws RepositoryException {
        BufferedReader read = null;
        try {
            read = new BufferedReader(new FileReader(this.fileName));
            String line = read.readLine();
            while(line != null)
            {
                String params[] = line.split(",");
                super.save(new Nota(Integer.parseInt(params[0]),Integer.parseInt(params[1]),Integer.parseInt(params[2])));
                line = read.readLine();
            }
            read.close();
        } catch (FileNotFoundException e) {
            throw new RepositoryException("File not found!");
        } catch (IOException e) {
            throw new RepositoryException("Eroare la citire din fisier!");
        } catch (RepositoryException e) {
            throw e;
        }
    }

    public void saveData() throws RepositoryException {
        try {
            ArrayList<Nota> allEntities = new ArrayList<Nota> ((Collection<? extends Nota>) super.findAll());
            BufferedWriter write = new BufferedWriter(new FileWriter(this.fileName));
            for(Nota nota: allEntities)
            {
                String line = "";
                line = nota.getIdStudent() + "," + nota.getNrTema() + "," + nota.getNota()+ "\n";
                write.write(line);
            }
            write.close();
        } catch (IOException e) {
            throw new RepositoryException("Eroare la scriere in fisier!");
        }
    }


    @Override
    public Nota save(Nota entitate) throws RepositoryException {
        Nota nota = super.save(entitate);
        saveData();
        return nota;
    }

    @Override
    public Optional<Nota> delete(Integer id_entitate) throws RepositoryException {
        Nota nota = super.delete(id_entitate).get();
        saveData();
        return Optional.ofNullable(nota);
    }

    @Override
    public Nota update(int idStudent, int nrTema, int vnota) throws RepositoryException {
        Nota nota = super.update(idStudent, nrTema, vnota);
        saveData();
        return nota;
    }
}
