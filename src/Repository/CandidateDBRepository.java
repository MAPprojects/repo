package Repository;

import Entites.Candidate;
import Utils.DBConnection;
import Validators.IValidator;
import Validators.ValidationException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class CandidateDBRepository extends CandidateRepository {
    private Connection connection = null;
    private PreparedStatement pst = null;
    private int index=0;
    public void setIndex(int index) {
        this.index = index;
    }

    public CandidateDBRepository(IValidator<Candidate> iValidator) {
        super(iValidator);
        this.connection = DBConnection.connect();
        loadData();
    }

    @Override
    public void loadData() {
        String sql = "SELECT * FROM Candidates ORDER BY id OFFSET ? ROWS FETCH NEXT 9 ROWS ONLY";
        int maximum = 0;
        try {
            pst = connection.prepareStatement(sql);
            pst.setInt(1,index);

            setIndex(index+9);
            ResultSet rs = pst.executeQuery();
            Candidate candidate=null;
            while (rs.next()) {
                int id = rs.getInt("id");
                if(id>maximum)
                    maximum=id;
                String name = rs.getString("name");
                String phoneNumber = rs.getString("phoneNumber");
                candidate = new Candidate(id, name, phoneNumber);
                super.save(candidate);
                candidate.setIndex(maximum);
            }
        } catch (SQLException | ValidationException e) {
            e.printStackTrace();
        } finally {
            try {
                pst.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void saveData() {
    }

    @Override
    public void save(Candidate entity) throws ValidationException {
        super.save(entity);
        try {
            String sql = "INSERT INTO Candidates(id,name,phoneNumber) VALUES (?,?,?)";
            pst = connection.prepareStatement(sql);
            pst.setInt(1, entity.getID());
            pst.setString(2, entity.getName());
            pst.setString(3, entity.getPhoneNumber());
            pst.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                pst.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Optional<Candidate> delete(Integer id) {
        Optional<Candidate> candidateOptional = super.delete(id);
        String sql = "DELETE FROM Candidates WHERE id = ?";
        try {
            pst = connection.prepareStatement(sql);
            pst.setInt(1, id);
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                pst.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


        return candidateOptional;
    }


    @Override
    public void update(Integer id, Candidate entity) throws ValidationException {
        super.update(id, entity);
        String sql = "UPDATE Candidates SET name = ?, phoneNumber=? WHERE id = ?;";
        try {
        pst = connection.prepareStatement(sql);
        pst.setString(1, entity.getName());
        pst.setString(2, entity.getPhoneNumber());
        pst.setInt(3,id);
        pst.executeUpdate();
        } catch ( SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                pst.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}


