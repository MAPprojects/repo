package repositories;

import entities.Department;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;

public class SerializableDepartmentRepository extends AbstractRepository<Department, Integer> {
    private String filepath;

    public SerializableDepartmentRepository(String filepath) {
        super();
        this.filepath = filepath;
        loadData();
    }

    @Override
    public void loadData() {
        try (ObjectInputStream stream = new ObjectInputStream(new FileInputStream(this.filepath))) {
            Map<Integer, Department> map = (Map<Integer, Department>) stream.readObject();
            for (Department d : map.values())
                super.save(d);

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
    public Iterable<Department> nextValues(int pageNumber , int limit) {
        return null;
    }
}
