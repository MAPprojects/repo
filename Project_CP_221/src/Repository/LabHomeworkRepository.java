package Repository;
import Domain.LabHomework;
import Validator.Validator;
public class LabHomeworkRepository extends AbstractRepository<Integer,LabHomework> {
    public LabHomeworkRepository(Validator<LabHomework> val) {
        super(val);
    }
}
