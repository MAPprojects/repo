package Repository;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public abstract class AbstractFileRepository<ID, E extends HasID<ID>> extends AbstractRepository<ID, E> {

    protected Path filename;
    public AbstractFileRepository(String filename)
    {
        super();
        this.filename = Paths.get(filename);

        try {
            this.loadData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveData() throws IOException {
        BufferedWriter bw = Files.newBufferedWriter(this.filename);
        for (ID item: this.findAll())
            if (this.findOne(item).isPresent())
            bw.write(this.findOne(item).get().toString() + "\n");
        bw.close();
    }
}
