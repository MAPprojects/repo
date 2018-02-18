package Repository;

import Domain.HasId;
import Domain.Validator;

public abstract class AbstractFileRepository<ID,E extends HasId<ID>> extends AbstractRepository<ID,E> {
    protected String numeFisier;

    public AbstractFileRepository(Validator<E> validator, String numeFisier) {
        super(validator);
        this.numeFisier=numeFisier;
    }

    abstract void incarcareFisier();
    abstract void salvareFisier();

    @Override
    public void save(E element) {
        super.save(element);
        salvareFisier();
    }

    @Override
    public E delete(ID id) {
        E s = super.delete(id);
        salvareFisier();
        return s;
    }

    @Override
    public void update(E element) {
        super.update(element);
        salvareFisier();
    }
}
