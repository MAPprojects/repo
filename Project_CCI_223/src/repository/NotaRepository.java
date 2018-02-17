package repository;

import domain.DetaliiNota;
import domain.Nota;
import exceptii.EntityNotFoundException;
import exceptii.ValidationException;
import validator.Validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class NotaRepository extends AbstractRepository<Nota,Integer> {
    /**
     * Constructor
     * @param val -validator.Validator<E></E>
     */
    public NotaRepository(Validator<Nota> val) {
        super(val);
    }

    public List<Nota> getNoteByIdStudent(Integer idStudent){
        List<Nota> note=new ArrayList<>();
        for (Nota n:findAll()){
            if (n.getIdStudent().equals(idStudent)){
                note.add(n);
            }
        }
        return note;
    }

    /**
     * Adds nota in repo just if the pair (idStudent,nrTema) is unique
     * @param nota Nota
     * @return Nota if nota was not saved in repo
     * @return null if nota is saved
     * @throws ValidationException If nota is not valid
     */
    @Override
    public Optional<Nota> save(Nota nota) throws ValidationException {
        List<Nota> note=getNoteByIdStudent(nota.getIdStudent());
        Boolean ok=true;
        for (Nota n:note){
            if (n.getNrTema().equals(nota.getNrTema()))ok=false;
        }
        if (ok==true) return super.save(nota);
        else return Optional.ofNullable(nota);
    }

    public Optional<Nota> save(Nota n, DetaliiNota det)throws ValidationException{return save(n);};

    public void update(Nota n, DetaliiNota det) throws ValidationException, EntityNotFoundException{};

}
