package Repository;

import Domain.LabValidator;
import Domain.Tema;
import Domain.Validator;

public class LabRepository extends AbstractRepository<Tema,Integer>{
    public LabRepository(Validator<Tema> vt){
        super(vt);
    }
}
