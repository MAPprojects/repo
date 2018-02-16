package Repository;

import Domain.Teme;

public class TemeValidator implements Validator<Teme>
{
    @Override
    public boolean validate(Teme entity) throws ValidationException {
        if(entity.getNrTema()>0 && entity.getDeadline()<14 && entity.getDeadline()>0){
            return true;
        }
        return false;
    }
}
