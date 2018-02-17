package Repository;

import Entites.Option;
import Entites.Section;
import Utils.DBConnection;
import Validators.IValidator;
import Validators.ValidationException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

public class OptionDBRepository extends OptionRepository{
    private Connection connection = null;
    private PreparedStatement pst = null;
    private int index=0;

    public void setIndex(int index) {
        this.index = index;
    }

    public OptionDBRepository(IValidator<Option> iValidator) {
        super(iValidator);
        this.connection = DBConnection.connect();
        loadData();
    }

    @Override
    public void loadData() {

        String sql = "SELECT * FROM Options ORDER BY id OFFSET ? ROWS FETCH NEXT 9 ROWS ONLY";

        int maximum = 0;
        try {
            pst = connection.prepareStatement(sql);
            pst.setInt(1,index);

            setIndex(index+9);
            ResultSet rs = pst.executeQuery();
            //System.out.println("query executed");
            Option option=null;
            while (rs.next()) {
                Integer id=rs.getInt("id");
                if(id>maximum)
                    maximum=id;
                Integer candidateId = rs.getInt("candidateId");
                Integer sectionId = rs.getInt("sectionId");
                Integer priority = rs.getInt("priority");
                option = new Option(id,candidateId,sectionId,priority);
                super.save(option);
                option.setIndex(maximum);
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
    public void save(Option entity) throws ValidationException {
        super.save(entity);
        try {
            String sql = "INSERT INTO Options(id,candidateId,sectionId,priority) VALUES (?,?,?,?)";
            pst = connection.prepareStatement(sql);
            pst.setInt(1,entity.getID());
            pst.setInt(2, entity.getIdCandidate());
            pst.setInt(3, entity.getIdSection());
            pst.setInt(4, entity.getPriority());
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
    public Optional<Option> delete(Integer id) {
        Optional<Option> optionOptional = super.delete(id);
        String sql = "DELETE FROM Options WHERE id = ?";
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
        return optionOptional;
    }


    @Override
    public void update(Integer id, Option entity) throws ValidationException {
        super.update(id, entity);
        String sql = "UPDATE Options SET candidateId = ?, sectionId=?, priority =?  WHERE id = ?;";
        try {
            pst = connection.prepareStatement(sql);
            pst.setInt(1, entity.getIdCandidate());
            pst.setInt(2, entity.getIdSection());
            pst.setInt(3,entity.getPriority());
            pst.setInt(4,id);
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
