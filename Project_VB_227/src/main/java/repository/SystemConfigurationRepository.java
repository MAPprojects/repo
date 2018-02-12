package repository;

import entities.SystemConfiguration;
import org.hibernate.Session;

import java.util.Optional;

public class SystemConfigurationRepository extends AbstractCrudRepo<SystemConfiguration, String> {
    public SystemConfigurationRepository() {
        super(SystemConfiguration.class);
    }

    @Override
    public Optional<SystemConfiguration> findOne(String id) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        SystemConfiguration entity = (SystemConfiguration) session.createNativeQuery("select * from system_configuration" + " where id=" + "'" + id
                + "'")
                .addEntity(typeParameterEntity).uniqueResult();
        session.getTransaction().commit();
        session.close();
        return Optional.ofNullable(entity);
    }
}
