package repository;

import domain.DetaliiNota;
import domain.Nota;
import exceptii.EntityNotFoundException;
import exceptii.ValidationException;
import validator.Validator;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.stream.Stream;

public class NotaFileRepository extends NotaRepository {

    protected String file;

    /**
     * Constructor
     *
     * @param val  -validator.Validator<E></E>
     * @param file String the fileName where is the data
     */
    public NotaFileRepository(Validator<Nota> val, String file) {
        super(val);
        this.file=file;
        loadData();
 //       loadDataStream();
    }

    /**
     * Loads the grades from file using Stream and saves them all into memory
     */
    private void loadDataStream() {
        Path path = Paths.get(file);
        Stream<String> lines;
        try {
            lines = Files.lines(path);
            lines.forEach(s -> {
                String[] fields = s.split(";");
                Nota entity = buildEntity(fields);
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
     * Loads the grades from file and saves them into memory unsing BufferReader and FileReader
     */
    public void loadData(){
        try(BufferedReader in=new BufferedReader(new FileReader(file))){
            String line;
            while ((line=in.readLine())!=null){
                String[] fields=line.split(";");
                Nota entity=buildEntity(fields);
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
     * @param entity Nota needs to have implemented the function toString
     */
    public void writeToFile(Nota entity){
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
            for (Nota entity:super.findAll()) {
                out.write(entity.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param entity Nota
     * @return Nota if the entity was not saved in repo
     * @return null if the entity was saved in repo
     * @throws ValidationException if entity is not valid
     */
    @Override
    public Optional<Nota> save(Nota entity) throws  ValidationException{
        Optional<Nota> aux=super.save(entity);
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
    public Optional<Nota> delete(Integer id) throws EntityNotFoundException{
        Optional<Nota> entity=super.delete(id);
        if (entity.isPresent()) writeToFile();
        return entity;
    }

    /**
     * Updates the entity from repo with the same id as newEntity.getId()
     * @param newEntity Nota
     * @throws EntityNotFoundException if the entity you want to upadate is not found in repo
     * @throws ValidationException if newEntity is not valid
     */
    @Override
    public void update(Nota newEntity) throws EntityNotFoundException,ValidationException {
        super.update(newEntity);
        writeToFile();
    }

    /**
     *
     * @param nota
     * @param detaliiNota
     * @return
     * @throws ValidationException
     */
    public Optional<Nota> save(Nota nota,DetaliiNota detaliiNota) throws ValidationException {
        Optional<Nota> n=save(nota);
        if (!n.isPresent()) addNotaIstoric(detaliiNota);
        return n;
    }

    /**
     * Adds in istoric file for each student details about the grades added by the teacher
     * @param detNota DetaliiNota
     */
    private void addNotaIstoric(DetaliiNota detNota){
        String fileName = createFile(detNota);

        try(BufferedWriter out=new BufferedWriter(new FileWriter(fileName,true))) {
            String linie="Adaugare nota;"+detNota.getNota().getNrTema()+";"+detNota.getNota().getValoare()+";"+detNota.getDeadline()+";"+detNota.getSaptamana_predarii()+";";
            if (detNota.getIntarzieri()==true) {linie=linie+"cu intarziere"+";";}
            else {linie=linie+"fara intarziere"+";";}
            if (detNota.getGreseli()==true) {linie=linie+"cu greseli\n";}
            else {linie=linie+"fara greseli\n";}
            out.write(linie);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a file for each student with idStudent.txt
     * @param detNota DetaliiNota
     * @return String (the fileName created)
     */
    private String createFile(DetaliiNota detNota) {
        String fileName="D:\\MAP\\GestiuneStudentiLaboratareNote\\src\\repository\\"+detNota.getNota().getIdStudent()+".txt";
        File file=new File(fileName);
        return fileName;
    }

    public void update(Nota nota,DetaliiNota detaliiNota) throws ValidationException, EntityNotFoundException {
        update(nota);
        updateNotaIstoric(detaliiNota);
    }

    /**
     * Adds in the istoric of the student whose grade is the fact that the grade was updated and details about it
     * @param detaliiNota DetaliiNota
     */
    private void updateNotaIstoric(DetaliiNota detaliiNota){
        String fileName=createFile(detaliiNota);

        try(BufferedWriter out=new BufferedWriter(new FileWriter(fileName,true))){
            String linie="Modificare nota;"+detaliiNota.getNota().getNrTema()+";"+detaliiNota.getNota().getValoare()+";"+detaliiNota.getDeadline()+";"+detaliiNota.getSaptamana_predarii()+";";
            if (detaliiNota.getIntarzieri()==true) linie=linie+"cu intarziere;";
            else linie=linie+"fara intarziere;";
            if (detaliiNota.getGreseli()==true) linie=linie+"cu greseli\n";
            else linie=linie+"fara greseli\n";
            out.write(linie);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates an entity Nota from the string fields given as a parameter
     * @param fields String []
     * @return Nota
     */
    public Nota buildEntity(String[] fields) {
        return new Nota(Integer.parseInt(fields[0]),Integer.parseInt(fields[1]),Integer.parseInt(fields[2]),Float.parseFloat(fields[3]));
    }
}
