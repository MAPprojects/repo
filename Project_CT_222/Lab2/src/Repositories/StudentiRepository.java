package Repositories;

import Domain.Studenti;
import Validators.IValidator;

public class StudentiRepository extends AbstractRepository<Studenti,Integer> {

    public StudentiRepository(IValidator<Studenti> _iv){
        super(_iv);
    }
    @Override
    public void update(Integer id,String filtru,String valoare){
        if(filtru.equals("nume")){
            System.out.println("IntraNume");
            findOne(id).setNume(valoare);
        }
        else if(filtru.equals("grupa")){
            System.out.println("IntraGrupa");
            findOne(id).setGrupa(Integer.parseInt(valoare));
        }
        else if(filtru.equals("e-mail")){
            System.out.println("IntraEmail");
            findOne(id).setEmail(valoare);
        }
        else if(filtru.equals("cadru didactic")){
            System.out.println("IntraCD");
            findOne(id).setCadruDidactic(valoare);
        }
        else
            throw new RepositoryException("Acest atribut al studentului nu exista");
    }
}
