package Repository;

import Domain.Action;
import Domain.Note;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;

public class NoteRepository extends AbstractFileRepository<Integer, Note> {


    public NoteRepository(String filename) {
        super(filename);
    }

    @Override
    public void loadData() throws IOException {
        Arrays.stream(this.filename.toFile().listFiles()).forEach(file -> {
            if (file.canRead()) {
                String StudentId = file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf('\\') + 1);
                StudentId = StudentId.substring(0, StudentId.lastIndexOf('.'));
                try {
                    String finalStudentId = StudentId;
                    Files.lines(file.toPath()).forEach(line -> {
                        String[] attributes = line.split("#");
                        if ((attributes[0].compareTo("Add Note") == 0 || attributes[0].compareTo("Modify Note") == 0) &&
                                attributes.length >= 5 && attributes.length <= 6) {
                            Note nota = new Note(Integer.parseInt(finalStudentId),
                                    Integer.parseInt(attributes[1]), Integer.parseInt(attributes[2]),
                                    Integer.parseInt(attributes[3]), Integer.parseInt(attributes[4]),
                                    attributes.length == 6 ? attributes[5] : "", attributes[0]
                                    .compareTo("Modify Note") == 0 ? Action.MODIFY : Action.ADD);
                            this.save(nota);
                        }
                    });


                } catch (IOException ignored) {
                }
            }
        });
    }

    @Override
    public void saveData() throws IOException {
        HashSet<Integer> existence = new HashSet<>();
        for (Integer note : this.findAll()) {
            Note nota = this.findOne(note).orElse(null);
            if (nota == null)
                continue;
            Path path = Paths.get(this.filename.toAbsolutePath() + "\\" +
                    nota.getStudentID() + ".txt");
            if (!existence.contains(nota.getStudentID())) {
                existence.add(nota.getStudentID());
                Files.write(path, "".getBytes());
            }
            BufferedWriter bf = Files.newBufferedWriter(path, StandardCharsets.UTF_8, StandardOpenOption.APPEND);
            bf.write(nota.toString() + "\n");
            bf.close();
        }
    }
}