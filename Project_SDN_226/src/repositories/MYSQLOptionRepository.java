package repositories;

import db_stuff.DBConnect;
import entities.Candidate;
import entities.Option;
import validators.RepositoryException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

public class MYSQLOptionRepository extends AbstractRepository<Option, String> {
    private DBConnect db;


    public MYSQLOptionRepository() {
        db = new DBConnect();

    }

    /**
     * Get number of options
     * @return long
     */
    @Override
    public long size() {
        long size = -1;
        try {

            db.stmt = db.con.prepareStatement("SELECT COUNT(*) FROM options");
            db.rs = db.stmt.executeQuery();
            size = db.rs.getRow();

            if (db.rs.first())
                size = db.rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return size;

    }

    /**
     * Save an option
     * @param entity option
     * @return option
     */
    @Override
    public Option save(Option entity) {
        int count=1;
        try {
            db.stmt = db.con.prepareStatement("SELECT COUNT(*) FROM options WHERE id =?");
            db.stmt.setString(1, entity.getId());
            db.rs = db.stmt.executeQuery();

            if (db.rs.first())
                count = db.rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (count != 0)
            throw new RepositoryException("ID already exist : " + entity.getId());

        try {


            db.stmt = db.con.prepareStatement(" INSERT INTO options (id , idCandidate , idDepartment , priority , language , name , department)  VALUES (?, ?, ?, ?, ?, ?, ?)");
            db.stmt.setString(1, entity.getId());
            db.stmt.setInt(2, entity.getIdCandidate());
            db.stmt.setInt(3, entity.getIdDepartment());
            db.stmt.setInt(4,entity.getPriority());
            db.stmt.setString(5,entity.getLanguage());
            db.stmt.setString(6,entity.getName());
            db.stmt.setString(7,entity.getDepartment());


            db.stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return entity;

    }

    /**
     * Delete an option by given id
     * @param id integer
     * @return option
     */
    @Override
    public Option delete(String id) {
        Option entity = null;
        int count =-1;

        try {
            db.stmt = db.con.prepareStatement("SELECT COUNT(*) FROM options WHERE id = ?");
            db.stmt.setString(1, id);
            db.rs = db.stmt.executeQuery();

            if (db.rs.first())
                count = db.rs.getInt(1);

            if (count == 0)
                throw new RepositoryException("ID doesn't exist " + id);


            db.stmt = db.con.prepareStatement(" SELECT * FROM options WHERE id =?");
            db.stmt.setString(1, id);
            db.rs = db.stmt.executeQuery();

            if (db.rs.first())
                entity = new Option(db.rs.getInt("idCandidate"), db.rs.getInt("idDepartment") , db.rs.getString("language") , db.rs.getInt("priority"));


            db.stmt = db.con.prepareStatement(" DELETE FROM options WHERE id =?");
            db.stmt.setString(1, id);

            db.stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return entity;
    }

    /**
     * Update an option
     * @param id integer
     * @param elem option
     */
    @Override
    public void update(String id, Option elem) {
        int count = 0;

        if (!id.equals(elem.getId())) {
            throw new RepositoryException("ID is not equal with your element's ID ");
        }

        try {

            db.stmt = db.con.prepareStatement("SELECT COUNT(*) FROM options WHERE id = ?");
            db.stmt.setString(1, id);
            db.rs = db.stmt.executeQuery();

            if (db.rs.first())
                count = db.rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (count == 0)
            throw new RepositoryException("ID doesn't exist " + id);

        try {

            db.stmt = db.con.prepareStatement("UPDATE options SET idCandidate = ? , idDepartment = ?  , language = ? , priority = ?  WHERE id = ?");
            db.stmt.setInt(1, elem.getIdCandidate());
            db.stmt.setInt(2, elem.getIdDepartment());
            db.stmt.setString(3, elem.getLanguage());
            db.stmt.setInt(4,elem.getPriority());
            db.stmt.setString(5,elem.getId());

            db.stmt.executeUpdate();


        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * Find an option  by an id
     * @param id integer
     * @return optional(option)
     */
    @Override
    public Optional<Option> findOne(String id) {
        Option o = null;
        try {
            db.stmt = db.con.prepareStatement("SELECT * FROM options WHERE id = ?");
            db.stmt.setString(1, id);
            db.rs = db.stmt.executeQuery();


            if (!db.rs.first())
                return Optional.empty();

            o = new Option(db.rs.getInt("idCandidate"), db.rs.getInt("idDepartment") , db.rs.getString("language") , db.rs.getInt("priority"));


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.of(o);
    }

    /**
     * Get all options
     * @return iterable
     */
    @Override
    public Iterable<Option> findAll() {
        ArrayList<Option> list = new ArrayList<>();
        Option o;

        try {
            db.stmt = db.con.prepareStatement("SELECT * FROM options");
            db.rs = db.stmt.executeQuery();

            while (db.rs.next()) {
                o =new Option(db.rs.getInt("idCandidate"), db.rs.getInt("idDepartment") , db.rs.getString("language") , db.rs.getInt("priority"));
                list.add(o);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    /**
     * Get all options from a given interval
     * @param pageNumber integer
     * @param limit integer
     * @return iterable
     */
    @Override
    public Iterable<Option> nextValues(int pageNumber , int limit){
        ArrayList<Option> list = new ArrayList<>();
        Option o = null;

        try {
            db.stmt = db.con.prepareStatement("SELECT * FROM options LIMIT ? OFFSET ?");
            db.stmt.setInt(1,limit);
            db.stmt.setInt(2, pageNumber*limit);

            db.rs = db.stmt.executeQuery();
            while (db.rs.next()) {
                o =new Option(db.rs.getInt("idCandidate"), db.rs.getInt("idDepartment") , db.rs.getString("language") , db.rs.getInt("priority"));
                list.add(o);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }


    @Override
    public void saveData() {


        // nothing to do

    }

    @Override
    public void loadData() {
        // nothing to do
    }
}
