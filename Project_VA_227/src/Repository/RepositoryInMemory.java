package Repository;


public class RepositoryInMemory extends AbstractRepository {
    private Repository repository = null;

    public RepositoryInMemory(Repository repository) {
        this.repository = repository;
    }

    @Override
    public void loadData(){

    }

    @Override
    public void saveData()
    {

    }
}
