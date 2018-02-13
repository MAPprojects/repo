package Repository;

import Domain.Tema;
import Repository.AbstractRepository;
import Validator.Validator;

public class RepositoryInMemoryTema extends AbstractRepository<Integer, Tema> {
    public RepositoryInMemoryTema(Validator<Tema> validator){
        super(validator);
    };
    /*
    @Override
    public void update(Integer id,Domain.Tema tema){
        if(tema.getDeadline()>findOne(id).getDeadline())
            throw new Repository.RepositoryException("Deadline-ul trebuie sa fie mai mic");
        findOne(id).setDeadline(tema.getDeadline());
    }
    */
}
