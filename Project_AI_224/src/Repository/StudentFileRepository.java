package Repository;

import Domain.Student;
import Validate.StudentValidator;
import Validate.ValidationException;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

public class StudentFileRepository extends FileRepository<UUID, Student> {
    /**
     * FileRepository Constructor
     *
     * @param filename filename
     */
    public StudentFileRepository(String filename) throws IOException, ValidationException {
        super(new StudentValidator(), filename);
        this.loadData();
    }

    @Override
    Optional<Student> buildEntity(String[] fields) {
        if(fields.length!=6){
            return Optional.empty();
        }
        return Optional.of(new Student(UUID.fromString(fields[0]), fields[1], fields[2], Integer.parseInt(fields[3]), fields[4], fields[5]));
    }
}
