package Repository;

import Domain.Studenti;


public class StudentRepo extends Repository<Studenti> {
    protected StudentRepo(Validator<Studenti> vali) {
        super(vali);
    }



    @Override
    public Studenti getById(int id) {

        Iterable<Studenti> it =getAll();
        for (Studenti st : it) {
            if (st.getIdStudent()==id)
                return st;
        }
        return null;
    }
}
