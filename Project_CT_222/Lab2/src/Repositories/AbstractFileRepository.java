package Repositories;

import Domain.HasID;
import Validators.IValidator;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.stream.Stream;

public abstract class AbstractFileRepository<E extends HasID<ID>,ID> extends AbstractRepository<E,ID> {
    protected String fileName;
    public AbstractFileRepository(String file, IValidator<E> v){
        super(v);
        this.fileName=file;
        loadFromFile();
    }
    protected abstract E Linie(String line);
    protected abstract String toLinie(E elem);
    protected void loadFromFile(){
        Path p= Paths.get("./"+fileName);
        Stream<String> lines;
        try{
            lines= Files.lines(p);
            try{lines.forEach(line->{super.save(Linie(line));
            });}catch(NullPointerException e){
                System.out.println("Linie Invalida");
            }

        }
        catch(IOException e){
            System.err.println(e);
        }
    }
    protected void writeToFile(){
        try(PrintWriter pw=new PrintWriter(fileName)){
            super.findAll().forEach(e->pw.write(toLinie(e)));
        } catch (IOException ex){
            System.err.println(ex);
        }
    }

    public void save(E elem){
        super.save(elem);
        writeToFile();
    }

    public void update(ID id,String filtru,String valoare){writeToFile();}
    public Optional<E> delete(ID id){
        Optional<E> e=super.delete(id);
        writeToFile();
        return e;
    }

}
