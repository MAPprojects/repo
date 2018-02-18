package Repository;

import Domain.HasId;
import Domain.ValidationException;
import Domain.Validator;


public abstract class AbstractRepositoryFile<ID,E extends HasId<ID>> extends AbstractRepository<ID,E>  {
    protected String numeFisier;

    abstract void incarcareDate() throws ValidationException;
    abstract void scrieFisier();

    public AbstractRepositoryFile(String numeFisier,Validator<E> validator) throws ValidationException{
        super(validator);
        this.numeFisier=numeFisier;
    }

    @Override
    public void save(E element) throws ValidationException{
        super.save(element);
        scrieFisier();
    }

    @Override
    public void update(E element){
        super.update(element);
        scrieFisier();
    }

    @Override
    public E delete(ID id){
        E element=super.delete(id);
        scrieFisier();
        return element;
    }

}
