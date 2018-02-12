package repository;

import entities.Tema;
import org.hibernate.Session;
import repository.AbstractCrudRepo;

import java.util.Optional;

public class TemaRepository extends AbstractCrudRepo<Tema, String> {
    @Override
    public Optional<Tema> findOne(String idTema) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Tema entity = (Tema) session.createNativeQuery("select * from " + typeParameterEntity.getSimpleName() + " where id_tema=" + "'" + idTema + "'")
                .addEntity(typeParameterEntity).uniqueResult();
        session.getTransaction().commit();
        session.close();
        return Optional.ofNullable(entity);
    }

    public TemaRepository() {
        super(Tema.class);
    }
}
