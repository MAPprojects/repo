package repositories;

import entities.Department;


public class DepartmentRepositoryInMemory extends AbstractRepository<Department, Integer> {
    public DepartmentRepositoryInMemory() {
        super();
    }

    @Override
    public Iterable<Department> nextValues(int pageNumber , int limit) {
        return null;
    }

    @Override
    public void saveData() {
        //nimic
    }

    @Override
    public void loadData() {
        //nimic
    }
}
