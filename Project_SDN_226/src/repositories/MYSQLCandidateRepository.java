package repositories;

import db_stuff.DBConnect;
import entities.Candidate;
import validators.RepositoryException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

public class MYSQLCandidateRepository extends AbstractRepository<Candidate, Integer> {
    private DBConnect db;


    public MYSQLCandidateRepository() {
        db = new DBConnect();

    }

    /**
     *  Get number of candidates
     * @return long
     */
    @Override
    public long size() {
        long size = -1;
        try {

            db.stmt = db.con.prepareStatement("SELECT COUNT(*) FROM candidates");
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
     * Save a candidate in repo
     * @param entity candidate
     * @return candidate
     */
    @Override
    public Candidate save(Candidate entity) {
        int count = 1;
        try {

            db.stmt = db.con.prepareStatement("SELECT COUNT(*) FROM candidates WHERE id = ?");
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


            db.stmt = db.con.prepareStatement(" INSERT INTO candidates (id, name, phone, email)  VALUES (?, ?, ?, ?)");
            db.stmt.setInt(1, entity.getId());
            db.stmt.setString(2, entity.getName());
            db.stmt.setString(3, entity.getPhone());
            db.stmt.setString(4, entity.getEmail());

            db.stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return entity;

    }

    /**
     * Delete a candidate from repository
     * @param id integer
     * @return candidate
     */
    @Override
    public Candidate delete(Integer id) {
        Candidate entity = null;
        int count = -1;
        try {
            db.stmt = db.con.prepareStatement("SELECT COUNT(*) FROM candidates WHERE id = ?");
            db.stmt.setInt(1, id);
            db.rs = db.stmt.executeQuery();

            if (db.rs.first())
                count = db.rs.getInt(1);

            if (count == 0)
                throw new RepositoryException("ID doesn't exist " + id);


            db.stmt = db.con.prepareStatement(" SELECT * FROM candidates WHERE id =?");
            db.stmt.setInt(1, id);
            db.rs = db.stmt.executeQuery();

            if (db.rs.first())
                entity = new Candidate(db.rs.getInt("id"), db.rs.getString("name"), db.rs.getString("phone"), db.rs.getString("email"));


            db.stmt = db.con.prepareStatement(" DELETE FROM candidates WHERE id =?");
            db.stmt.setInt(1, id);

            db.stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return entity;
    }

    /**
     * Update a candidate in repo
     * @param id integer
     * @param elem candidate
     */
    @Override
    public void update(Integer id, Candidate elem) {
        int count = 0;

        if (!id.equals(elem.getId())) {
            throw new RepositoryException("ID is not equal with your element's ID ");
        }

        try {

            db.stmt = db.con.prepareStatement("SELECT COUNT(*) FROM candidates WHERE id = ?");
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

            db.stmt = db.con.prepareStatement("UPDATE candidates SET name = ? , phone = ? , email = ? WHERE id = ?");
            db.stmt.setString(1, elem.getName());
            db.stmt.setString(2, elem.getPhone());
            db.stmt.setString(3, elem.getEmail());
            db.stmt.setInt(4, elem.getId());

            db.stmt.executeUpdate();


        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * Find a candidate
     * @param id integer
     * @return candidate
     */
    @Override
    public Optional<Candidate> findOne(Integer id) {
        Candidate c = null;
        try {
            db.stmt = db.con.prepareStatement("SELECT * FROM candidates WHERE id = ?");
            db.stmt.setInt(1, id);
            db.rs = db.stmt.executeQuery();


            if (!db.rs.first())
                return Optional.empty();

            c = new Candidate(db.rs.getInt("id"), db.rs.getString("name"), db.rs.getString("phone"), db.rs.getString("email"));


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.of(c);
    }

    /**
     * Get all candidates from repo
     * @return iterable
     */
    @Override
    public Iterable<Candidate> findAll() {
        ArrayList<Candidate> list = new ArrayList<>();
        Candidate c = null;

        try {
            db.stmt = db.con.prepareStatement("SELECT * FROM candidates");
            db.rs = db.stmt.executeQuery();
            while (db.rs.next()) {
                c = new Candidate(db.rs.getInt("id"), db.rs.getString("name"), db.rs.getString("phone"), db.rs.getString("email"));
                list.add(c);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return (Iterable<Candidate>) list;
    }

    /**
     *  Get all candidates in a given interval
     * @param pageNumber
     * @param limit
     * @return
     */
    @Override
    public Iterable<Candidate> nextValues(int pageNumber, int limit) {
        ArrayList<Candidate> list = new ArrayList<>();
        Candidate c = null;

        try {
            db.stmt = db.con.prepareStatement("SELECT * FROM candidates LIMIT ? OFFSET ?");
            db.stmt.setInt(1, limit);
            db.stmt.setInt(2, pageNumber * limit);

            db.rs = db.stmt.executeQuery();
            while (db.rs.next()) {
                c = new Candidate(db.rs.getInt("id"), db.rs.getString("name"), db.rs.getString("phone"), db.rs.getString("email"));
                list.add(c);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return (Iterable<Candidate>) list;
    }


    /**
     * Save data
     */
    @Override
    public void saveData() {


        // nothing to do

    }

    /**
     * Load data
     */
    @Override
    public void loadData() {
        // nothing to do
    }
}
