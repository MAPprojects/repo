package Repository;

import Domain.Project;
import Validate.ProjectValidator;
import Validate.ValidationException;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

public class ProjectFileRepository extends FileRepository<UUID, Project> {
    /**
     * ProjectFileRepository Constructor
     * @param filename file name
     */
    public ProjectFileRepository(String filename) throws IOException, ValidationException {
        super(new ProjectValidator(), filename);
        this.loadData();
    }

    @Override
    Optional<Project> buildEntity(String[] fields) {
        if(fields.length!=3) {
            return Optional.empty();
        }
        return Optional.of(new Project(UUID.fromString(fields[0]), fields[1], Integer.parseInt(fields[2])));
    }
}
