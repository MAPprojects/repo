package repository;

import entities.Student;
import org.hibernate.Session;
import repository.AbstractCrudRepo;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class StudentRepository extends AbstractCrudRepo<Student, String> {
    public StudentRepository() throws IOException {
        super(Student.class);
    }

    /**
     * Incarca un numar de Studenti in repo-ul din memorie;
     */
    @Override
    public void loadData() throws IOException {
    }

    @Override
    public Optional<Student> findOne(String idStudent) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Student entity = (Student) session.createNativeQuery("select * from " + typeParameterEntity.getSimpleName() + " where id_student=" + "'" + idStudent
                + "'")
                .addEntity(typeParameterEntity).uniqueResult();
        session.getTransaction().commit();
        session.close();
        return Optional.ofNullable(entity);
    }

    /**
     * Nu efectueaza nimic intrucat nu avem vreun fisier uc care sa lucram
     */
    @Override
    public void saveData() {
        //nu avem fisier cu care sa lucram
    }

    /**
     * Populeaza repo-ul din memorie cu o lista de studenti pe care noi o introducem
     *
     * @param elements Collection<E> -> o colectie de elemente de tipul generic Student
     */
    @Override
    public void populate(Collection<Student> elements) throws IOException {
        for (Student s : elements) {
            super.save(s);
        }
    }
}

