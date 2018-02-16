package repository;

import domain.IValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class AbstractRepo<T> {
    protected List<T> lista;
    protected IValidator<T> validator = null;
    
    public AbstractRepo() throws Exception {
        lista = new ArrayList<T>();
        getFromFile();
    }
    public void setValidator(Optional<IValidator<T>> validator){
        if (validator.isPresent())
            this.validator=validator.get();
    }
    public void add(T obiect) throws Exception {
        if (validator!=null){
            validator.valideaza(obiect);
        }
        for (T o:lista) {
            if(obiect.equals(o)){
                throw new Exception("Obiect deja existent!");
            }
        }
        lista.add(obiect);
        updateFile();

    }

    public T findobject(Integer id,String numeMetoda) throws Exception {
        for (T n:lista){
            if (id.equals((n.getClass().getMethod(numeMetoda)).invoke(n))){
                return n;
            }
        }
        return null;
    }

    public abstract void getFromFile() throws Exception;

    public abstract void updateFile();

    public abstract void update(Integer id, T object) throws Exception;

    public void delete(Integer id, String numeMetoda) throws Exception {
        lista.remove(findobject(id,numeMetoda));
        updateFile();
    }

    public List<T> getLista() {
        return lista;
    }
    public Integer getSize(){
        return lista.size();
    }

}
