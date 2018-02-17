package repository;

import domain.HasID;
import exceptii.EntityNotFoundException;
import exceptii.ValidationException;
import validator.Validator;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.stream.Stream;

public abstract class AbstractFileRepository<E extends HasID<ID>,ID> extends AbstractRepository<E,ID> {
    protected String file;

    /**
     * Constructor
     * @param file String the fileName where is the data
     * @param val -validator.Validator<E></E>
     */
    public AbstractFileRepository(Validator<E> val,String file) {
        super(val);
        this.file=file;
        loadData();
// loadDataStream();
    }

    /**
     * Creates an entity from the fields read from a row
     * @param fields String []
     * @return E
     */
    public abstract E buildEntity(String[] fields);

    private void loadDataStream(){
        Path path= Paths.get(file);
        Stream<String> lines;
        try{
            lines= Files.lines(path);
            lines.forEach(s->{
                String[] fields=s.split(";");
                E entity=buildEntity(fields);
                try {
                    super.save(entity);
                } catch (ValidationException e) {
                    System.out.println("Fisier corupt!");
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Load data from file and saves them in memory
     */
    public void loadData(){
        try(BufferedReader in=new BufferedReader(new FileReader(file))){
            String line;
            while ((line=in.readLine())!=null){
                String[] fields=line.split(";");
                E entity=buildEntity(fields);
                super.save(entity);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ValidationException e) {
            System.out.println("Fisier corupt");
        }
    }

    /**
     * Writes a row with the entity's atributes
     * @param entity E needs to have implemented the function toString
     */
    public void writeToFile(E entity){
        try(BufferedWriter out=new BufferedWriter(new FileWriter(file,true))){
            out.write(entity.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Clears the file and rewrites it with the list of entities from the memory
     */
    public void writeToFile(){
        try(BufferedWriter out=new BufferedWriter(new FileWriter(file))){
            for (E entity:super.findAll()) {
                out.write(entity.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param entity E
     * @return E if the entity was not saved in repo
     * @return null if the entity was saved in repo
     * @throws ValidationException if entity is not valid
     */
    @Override
    public Optional<E> save(E entity) throws  ValidationException{
        Optional<E> aux=super.save(entity);
        if (!aux.isPresent()) writeToFile(entity);
        return aux;
    }

    /**
     *
     * @param id ID Integer
     * @return E the deleted entity
     * @throws EntityNotFoundException if there isn't an entity in repo with iD id
     */
    @Override
    public Optional<E> delete(ID id) throws EntityNotFoundException{
        Optional<E> entity=super.delete(id);
        if (entity.isPresent()) writeToFile();
        return entity;
    }

    /**
     * Updates the entity from repo with the same id as newEntity.getId()
     * @param newEntity E
     * @throws EntityNotFoundException if the entity you want to upadate is not found in repo
     * @throws ValidationException if newEntity is not valid
     */
    @Override
    public void update(E newEntity) throws EntityNotFoundException,ValidationException {
        super.update(newEntity);
        writeToFile();
    }

}
