package repositories;

import db_stuff.DBConnect;
import entities.Candidate;
import entities.Department;
import validators.RepositoryException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

public class MYSQLDepartmentRepository extends AbstractRepository<Department, Integer> {
    private DBConnect db;


    public MYSQLDepartmentRepository() {
        db = new DBConnect();

    }

    /**
     * Get number of departments
     * @return long
     */
    @Override
    public long size() {
        long size = -1;
        try {

            db.stmt = db.con.prepareStatement("SELECT COUNT(*) FROM departments");
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
     * Save a department
     * @param entity department
     * @return department
     */
    @Override
    public Department save(Department entity) {
        int count=1;
        try {

            db.stmt = db.con.prepareStatement("SELECT COUNT(*) FROM departments WHERE id = ?");
            db.stmt.setInt(1, entity.getId());
            db.rs = db.stmt.executeQuery();

            if (db.rs.first())
                count = db.rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (count != 0)
            throw new RepositoryException("ID already exist : " + entity.getId());

        try {


            db.stmt = db.con.prepareStatement(" INSERT INTO departments (id, name, nrLoc)  VALUES (?, ?, ?)");
            db.stmt.setInt(1, entity.getId());
            db.stmt.setString(2, entity.getName());
            db.stmt.setInt(3, entity.getNrLoc());


            db.stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return entity;

    }

    /**
     * Delete a department by given id
     * @param id integer
     * @return department
     */
    @Override
    public Department delete(Integer id) {
        Department entity = null;
        int count =-1;

        try {
                db.stmt = db.con.prepareStatement("SELECT COUNT(*) FROM departments WHERE id = ?");
                db.stmt.setInt(1, id);
                db.rs = db.stmt.executeQuery();

                if (db.rs.first())
                    count = db.rs.getInt(1);

            if (count == 0)
                throw new RepositoryException("ID doesn't exist " + id);


            db.stmt = db.con.prepareStatement(" SELECT * FROM departments WHERE id =?");
            db.stmt.setInt(1, id);
            db.rs = db.stmt.executeQuery();

            if (db.rs.first())
                entity = new Department(db.rs.getInt("id"), db.rs.getString("name"), db.rs.getInt("nrLoc"));


            db.stmt = db.con.prepareStatement(" DELETE FROM departments WHERE id =?");
            db.stmt.setInt(1, id);

            db.stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return entity;
    }

    /**
     * Update a department
     * @param id integer
     * @param elem department
     */
    @Override
    public void update(Integer id, Department elem) {
        int count = 0;

        if (!id.equals(elem.getId())) {
            throw new RepositoryException("ID is not equal with your element's ID ");
        }

        try {

            db.stmt = db.con.prepareStatement("SELECT COUNT(*) FROM departments WHERE id = ?");
            db.stmt.setInt(1, id);
            db.rs = db.stmt.executeQuery();

            if (db.rs.first())
                count = db.rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (count == 0)
            throw new RepositoryException("ID doesn't exist " + id);

        try {

            db.stmt = db.con.prepareStatement("UPDATE departments SET name = ? , nrLoc = ? WHERE id = ?");
            db.stmt.setString(1, elem.getName());
            db.stmt.setInt(2, elem.getNrLoc());
            db.stmt.setInt(3, elem.getId());

            db.stmt.executeUpdate();


        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * Find a department
     * @param id integer
     * @return optional(department)
     */
    @Override
    public Optional<Department> findOne(Integer id) {
        Department d = null;
        try {
            db.stmt = db.con.prepareStatement("SELECT * FROM departments WHERE id = ?");
            db.stmt.setInt(1, id);
            db.rs = db.stmt.executeQuery();


            if (!db.rs.first())
                return Optional.empty();

            d = new Department(db.rs.getInt("id"), db.rs.getString("name"), db.rs.getInt("nrLoc"));


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.of(d);
    }

    /**
     * Get all departments .
     * @return iterable
     */
    @Override
    public Iterable<Department> findAll() {
        ArrayList<Department> list = new ArrayList<>();
        Department d;

        try {
            db.stmt = db.con.prepareStatement("SELECT * FROM departments");
            db.rs = db.stmt.executeQuery();

            while (db.rs.next()) {
                d = new Department(db.rs.getInt("id"), db.rs.getString("name"), db.rs.getInt("nrLoc"));
                list.add(d);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return (Iterable<Department>) list;
    }

    /**
     * Get all departments in a given interval
     * @param pageNumber integer
     * @param limit integer
     * @return iterable
     */
    @Override
    public Iterable<Department> nextValues(int pageNumber , int limit){
        ArrayList<Department> list = new ArrayList<>();
        Department d = null;

        try {
            db.stmt = db.con.prepareStatement("SELECT * FROM departments LIMIT ? OFFSET ?");
            db.stmt.setInt(1,limit);
            db.stmt.setInt(2, pageNumber*limit);

            db.rs = db.stmt.executeQuery();
            while (db.rs.next()) {
                d = new Department(db.rs.getInt("id"), db.rs.getString("name"), db.rs.getInt("nrLoc"));
                list.add(d);
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
