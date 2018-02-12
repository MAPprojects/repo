package com.company.Repositories;

import com.company.Domain.Tema;
import com.company.Exceptions.RepositoryException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

public class FileTemaRepository extends TemaRepository {

    private String fileName;
    private Path filePath;

    public FileTemaRepository(String fileName) throws RepositoryException {
        super();
        this.fileName = fileName;
        this.filePath = Paths.get(fileName);
        loadDataStream();
    }

    public void loadDataStream() throws RepositoryException {
        Path path = this.filePath;
        Stream<String> lines;
        try{
            lines =  Files.lines(path);
            lines.forEach(s-> {
                String[] tema = s.split(",");
                try {
                    super.save(new Tema(Integer.parseInt(tema[0]),tema[1],Integer.parseInt(tema[2])));
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
                super.save(new Tema(Integer.parseInt(params[0]),params[1],Integer.parseInt(params[2])));
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
            ArrayList<Tema> allEntities = new ArrayList<Tema> ((Collection<? extends Tema>) super.findAll());
            BufferedWriter write = new BufferedWriter(new FileWriter(this.fileName));
            for(Tema tema: allEntities)
            {
                String line = "";
                line = tema.getID() + "," + tema.getDescriere() + "," + tema.getDeadline()+ "\n";
                write.write(line);
            }
            write.close();
        } catch (IOException e) {
            throw new RepositoryException("Eroare la scriere in fisier!");
        }
    }


    @Override
    public Tema save(Tema entitate) throws RepositoryException {
        Tema tema = super.save(entitate);
        saveData();
        return tema;
    }

    @Override
    public Optional<Tema> delete(Integer id_entitate) throws RepositoryException {
        Tema tema = super.delete(id_entitate).get();
        saveData();
        return Optional.ofNullable(tema);
    }

    @Override
    public Tema update(Integer nrTema, String Descriere, int deadline) throws RepositoryException {
        Tema tema = super.update(nrTema, Descriere, deadline);
        saveData();
        return tema;
    }


}
