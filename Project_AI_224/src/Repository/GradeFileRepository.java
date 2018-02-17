package Repository;

import Domain.Grade;
import Domain.Project;
import Domain.Student;
import Validate.GradeValidator;
import Validate.ValidationException;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

public class GradeFileRepository extends FileRepository<UUID, Grade>{

    private AbstractRepository<UUID, Student> studentAbstractRepository;
    private AbstractRepository<UUID, Project> projectAbstractRepository;

    public GradeFileRepository(String filename, AbstractRepository<UUID, Student> studentAbstractRepository, AbstractRepository<UUID, Project> projectAbstractRepository) throws IOException, ValidationException {
        super(new GradeValidator(), filename);
        this.studentAbstractRepository = studentAbstractRepository;
        this.projectAbstractRepository = projectAbstractRepository;
        this.loadData();
    }

    @Override
    Optional<Grade> buildEntity(String[] fields) {
        if(fields.length!=6){
            return Optional.empty();
        }
        return Optional.of(new Grade(UUID.fromString(fields[0]), studentAbstractRepository.getEntity(UUID.fromString(fields[1])).get(), projectAbstractRepository.getEntity(UUID.fromString(fields[2])).get(), Integer.parseInt(fields[3]), Integer.parseInt(fields[4]), Boolean.valueOf(fields[5])));
    }
}
