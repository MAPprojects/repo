package Repository;

import Domain.Teme;

public class TemeRepo extends Repository<Teme> {


    public TemeRepo(Validator<Teme> vali) {
        super(vali);
    }

    @Override
    public Teme getById(int id) {
        Iterable<Teme> it =getAll();
        for (Teme te : it) {
            if (te.getNrTema()==id)
                return te;
        }
        return null;
    }



}
