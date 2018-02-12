package repository;

import entities.SystemUser;
import org.hibernate.Session;

import java.util.Optional;

public class UserRepository extends AbstractCrudRepo<SystemUser, String> {
    public UserRepository() {
        super(SystemUser.class);
    }

    @Override
    public Optional<SystemUser> findOne(String emailUser) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        SystemUser entity = (SystemUser) session.createNativeQuery("select * from " + "system_user" + " where email=" + "'" + emailUser
                + "'")
                .addEntity(typeParameterEntity).uniqueResult();
        session.getTransaction().commit();
        session.close();
        return Optional.ofNullable(entity);
    }
}
