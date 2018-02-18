package Repositories;

import Domain.Nota;
import Validators.IValidator;

public class NoteRepository extends AbstractRepository<Nota,Integer> {
    public NoteRepository(IValidator<Nota> _iv){
        super(_iv);
    }
    public void update(Integer id,String valoareS,String saptamanaPredareS){
        findOne(id).setValoare(Integer.parseInt(valoareS));
    }

}
