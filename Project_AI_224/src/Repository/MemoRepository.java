package Repository;

import Domain.HasID;
import Validate.Validator;

public class MemoRepository<ID, E  extends HasID<ID>> extends AbstractRepository<ID, E> {
    /**
     * MemoRepository Constructor
     * tests MemoRepository
     * @param validator validator
     */
    public MemoRepository(Validator<E> validator) {
        super(validator);
    }
}
