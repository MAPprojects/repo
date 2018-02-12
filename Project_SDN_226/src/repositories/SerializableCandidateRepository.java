package repositories;

import entities.Candidate;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;

public class SerializableCandidateRepository extends AbstractRepository<Candidate, Integer> {
    private String filepath;

    public SerializableCandidateRepository(String filepath) {
        super();
        this.filepath = filepath;
        loadData();
    }

    @Override
    public void loadData() {
        try (ObjectInputStream stream = new ObjectInputStream(new FileInputStream(this.filepath))) {
            Map<Integer, Candidate> map = (Map<Integer, Candidate>) stream.readObject();
            for (Candidate c : map.values())
                super.save(c);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveData() {
        try (ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(this.filepath))) {
            stream.writeObject(super.map);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public Iterable<Candidate> nextValues(int pageNumber , int limit) {
        return null;
    }
}
