package Repository;

import Domain.HasId;
import Domain.ValidationException;
import Domain.Validator;

public abstract class AbstractRepositoryDatabase<ID,E extends HasId<ID>> extends AbstractRepository<ID,E> {
    public String url = "jdbc:jtds:sqlserver://localhost:1433/ManagerNote;instance=SQLEXPRESS;integratedSecurity=true;";
    protected String database;
    abstract void insertIntoDatabase(E element);
    abstract void updateDatabase(E element);
    abstract void deleteFromDatabase(ID id);
    abstract void readFromDatabase() throws ValidationException;

    public AbstractRepositoryDatabase(Validator<E> validator) throws ValidationException{
        super(validator);
    }

    public void saveWithNoInsert(E element) throws ValidationException {
        super.save(element);
    }

    public void databaseConnection() {
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save(E element) throws ValidationException{
        super.save(element);
        insertIntoDatabase(element);
    }

    @Override
    public void update(E element){
        super.update(element);
        updateDatabase(element);
    }

    @Override
    public E delete(ID id){
        E element=super.delete(id);
        deleteFromDatabase(id);
        return element;
    }
}
