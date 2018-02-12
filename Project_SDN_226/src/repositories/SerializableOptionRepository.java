package repositories;


import entities.Option;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;

public class SerializableOptionRepository extends AbstractRepository<Option, String> {
    private String filepath;

    public SerializableOptionRepository(String filepath) {
        super();
        this.filepath = filepath;
        loadData();
    }

    @Override
    public void loadData() {
        try (ObjectInputStream stream = new ObjectInputStream(new FileInputStream(this.filepath))) {
            Map<Integer, Option> map = (Map<Integer, Option>) stream.readObject();
            for (Option o : map.values())
                super.save(o);

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
    public Iterable<Option> nextValues(int pageNumber , int limit) {
        return null;
    }
}
