package Repository;

import Domain.HasID;
import Domain.Validator;

import java.util.Optional;

public abstract class AbstractFileRepository<E extends HasID<ID>,ID> extends AbstractRepository<E,ID>{
    protected String fileName;
    public AbstractFileRepository(String file, Validator<E> v){
        super(v);
        this.fileName=file;
        loading();
    }

    protected abstract void loading();
    protected abstract void writeToFile();

    public E save(E e){
        super.save(e);
        writeToFile();
        return e;
    }

    public void update(E e){
        super.update(e);
        writeToFile();
    }

    public Optional<E> delete(ID id){
        Optional<E> e=super.delete(id);
        writeToFile();
        return e;
    }
}
