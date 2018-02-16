package Repository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Repository<E> implements IRepository<E> {

    private List<E> entities;
    private Validator<E> vali;

    public Repository(Validator<E> vali) {
        entities = new ArrayList<E>();
        this.vali = vali;
    }





    @Override
    public long size() {
        return entities.size();
    }

    /**
     *
     * @param entity
     * @throws ValidationException -daca entity nu e valid
     * @return null -daca entitatea a fost salvata
     */
    @Override
    public void save(E entity) throws ValidationException, SQLException {

        if(vali.validate(entity)) {
            entities.add(entity);
        }
        else {
            throw new ValidationException("ceva nu e bine");
        }
    }

    /**
     *
     * @param entity;
     * @return null daca a fost salvat
     */
    @Override
    public void delete(E entity) throws SQLException {
        entities.remove(entity);
    }

    @Override
    public List<E> getAll() {
        return entities;
    }

    @Override
    public boolean contains(E ent) {

        return entities.contains(ent);
    }

    @Override
    public  E getById(int id){return  null;};

    @Override
    public void update(E entity, E entiti1) throws ValidationException, SQLException {
        delete(entity);
        save(entiti1);
    }
}
