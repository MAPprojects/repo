package Repository;

import Domain.HasID;
import Validate.ValidationException;
import Validate.Validator;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Optional;
import java.util.stream.Stream;


public abstract class FileRepository<ID, E extends HasID<ID>> extends AbstractRepository<ID,E> {
    private String fileName;

    public FileRepository(Validator<E> validator, String fileName) {
        super(validator);
        this.fileName=fileName;
    }

    @Override
    public Optional<E> save(E entity) throws ValidationException, FileNotFoundException, UnsupportedEncodingException {
        Optional<E> e=super.save(entity);
        if (!e.isPresent())
        {
            saveToFile(entity);
        }
        return e;
    }

    @Override
    public Optional<E> update(E entity) throws ValidationException, FileNotFoundException, UnsupportedEncodingException {
        Optional<E> e=super.update(entity);
        if (e.isPresent())
        {
            saveToFile();
        }
        return e;
    }

    @Override
    public Optional<E> delete(ID id) throws FileNotFoundException, UnsupportedEncodingException {
        Optional<E> e=super.delete(id);
        saveToFile();
        return e;
    }

    void loadData() {
        Path path = Paths.get(fileName);
        Stream<String> lines;

        try {
            lines = Files.lines(path);
            lines.forEach(line -> {
                if (line.compareTo("")!=0) {
                    String[] fields = line.split(";");
                    Optional<E> t = buildEntity(fields);
                    try {
                        if(t.isPresent()){
                            super.save(t.get());
                        }
                    } catch (ValidationException | FileNotFoundException | UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    abstract Optional<E> buildEntity(String[] fields);

    private void saveToFile(E e){
        Path path = Paths.get(fileName);
        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(path, StandardOpenOption.APPEND)) {
            bufferedWriter.write(e.toString());
            bufferedWriter.newLine();
        } catch (IOException ex) { ex.printStackTrace();}
    }

    private void saveToFile(){
        Path path = Paths.get(fileName);
        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(path))
        {
            for (E e: super.getAll()) {
                bufferedWriter.write(e.toString());
                bufferedWriter.newLine();
            }
        } catch (IOException ex) { ex.printStackTrace();}
    }
}
