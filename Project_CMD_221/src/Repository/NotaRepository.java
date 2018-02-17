package Repository;

import Entities.Nota;
import Validators.Validator;
import Validators.ValidatorException;
import javafx.util.Pair;

public class NotaRepository extends AbstractRepository<Pair<Integer,Integer>, Nota>  {
    public NotaRepository(Validator<Nota> v) { super(v); };
}
