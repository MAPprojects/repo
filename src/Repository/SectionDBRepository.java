package Repository;

import Entites.Section;
import Utils.DBConnection;
import Validators.IValidator;
import Validators.ValidationException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class SectionDBRepository extends SectionRepository {
    private Connection connection = null;
    private PreparedStatement pst = null;
    private int index=0;
    public SectionDBRepository(IValidator<Section> iValidator) {
        super(iValidator);
        this.connection = DBConnection.connect();
        loadData();
    }
    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public void loadData() {
        String sql = "SELECT * FROM Sections ORDER BY id OFFSET ? ROWS FETCH NEXT 9 ROWS ONLY";
        int maximum = 0;
        try {
            pst = connection.prepareStatement(sql);
            pst.setInt(1,index);

            setIndex(index+9);
            ResultSet rs = pst.executeQuery();
            Section section=null;
            while (rs.next()) {
                int id = rs.getInt("id");
                if(id>maximum)
                    maximum=id;
                String name = rs.getString("name");
                int number = rs.getInt("number");
                section = new Section(id, name, number);
                super.save(section);
                section.setIndex(maximum);
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
//        for (Candidate candidate : findAll()) {
//            String sql1 = "DELETE FROM Candidates";
//            String sql = "INSERT INTO Candidates(name,phoneNumber) VALUES (?,?)";
//            try {
//                pst = connection.prepareStatement(sql1);
//                pst.executeUpdate();
//                pst = connection.prepareStatement(sql);
//                pst.setString(1, candidate.getName());
//                pst.setString(2, candidate.getPhoneNumber());
//                pst.executeUpdate();
//
//            } catch (SQLException e) {
//                e.printStackTrace();
//            } finally {
//                try {
//                    pst.close();
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
    }

    @Override
    public void save(Section entity) throws ValidationException {
        super.save(entity);
        try {
            String sql = "INSERT INTO Sections(id,name,number) VALUES (?,?,?)";
            pst = connection.prepareStatement(sql);
            pst.setInt(1, entity.getID());
            pst.setString(2, entity.getName());
            pst.setInt(3, entity.getNumber());
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
    public Optional<Section> delete(Integer id) {
        Optional<Section> sectionOptional = super.delete(id);
        String sql = "DELETE FROM Sections WHERE id = ?";
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


        return sectionOptional;
    }


    @Override
    public void update(Integer id, Section entity) throws ValidationException {
        super.update(id, entity);
        String sql = "UPDATE Sections SET name = ?, number=? WHERE id = ?;";
        try {
            pst = connection.prepareStatement(sql);
            pst.setString(1, entity.getName());
            pst.setInt(2, entity.getNumber());
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

