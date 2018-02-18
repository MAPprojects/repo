package Repositories;

import Domain.Teme;
import Validators.IValidator;
import Validators.ValidatorException;

import java.util.Calendar;

public class TemeRepository extends AbstractRepository<Teme,Integer> {
    public TemeRepository(IValidator<Teme> _iv){
        super(_iv);
    }
    public void update(Integer id,String filtru,String valoare){
        if(filtru.equals("cerinta")){
            findOne(id).setCerinta(valoare);
        }
        else if(filtru.equals("deadline")){
            if(Integer.parseInt(valoare)<2||Integer.parseInt(valoare)>14){
                throw new ValidatorException("Tema deadline "+Integer.parseInt(valoare)+" trebuie sa fie cuprinsa intre 2 si 14");
            }
            Teme tema=findOne(id);
            Calendar calendar= Calendar.getInstance();

            if(tema.getDeadline()<=(calendar.get(calendar.WEEK_OF_YEAR)-40))
                throw new RepositoryException("S-a depasit termenul admis: "+tema.getDeadline());
            else if(tema.getDeadline()>Integer.parseInt(valoare))
                throw new RepositoryException("Nu se poate modifica deadlineul");
            else {
                tema.setDeadline(Integer.parseInt(valoare));
            }
        }
        else
            throw new RepositoryException("Acest atribut al temei nu exista");
    }
}
