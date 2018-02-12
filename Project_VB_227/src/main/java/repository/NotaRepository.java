package repository;

import entities.Nota;
import entities.NotaPk;

import java.util.List;
import java.util.Optional;

public class NotaRepository extends AbstractCrudRepo<Nota, NotaPk> {
    public NotaRepository() {
        super(Nota.class);
    }

    @Override
    public Optional<Nota> findOne(NotaPk notaPk) {
        List<Nota> note = (List<Nota>) super.findAll();
        for (Nota n : note) {
            if (n.getIdStudent() == notaPk.getIdStudent() && n.getIdTema() == notaPk.getIdTema()) {
                return Optional.of(n);
            }
        }
        return Optional.ofNullable(null);
    }
}
