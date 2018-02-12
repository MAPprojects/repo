package Repository;

import Domain.Student;
import Utils.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

public class StudentDatabaseRepository extends AbstractRepository<Integer, Student> {

    public StudentDatabaseRepository()
    {
        this.loadData();
    }

    @Override
    public void save(Student entity) {
        try (PreparedStatement cmd = Database.getConnection().prepareStatement("INSERT INTO Student([Name], Grupa, Email, Profesor) VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS))
        {
            cmd.setString(1, entity.getNume());
            cmd.setInt(2, entity.getGrupa());
            cmd.setString(3, entity.getEmail());
            cmd.setString(4, entity.getProfesor());
            cmd.executeUpdate();
            try (ResultSet keys = cmd.getGeneratedKeys())
            {
                if (keys.next())
                    entity.setId(keys.getInt(1));
            }
        }
        catch (SQLException ignored) {
        }
        super.save(entity);
    }

    @Override
    public void loadData() {
        try (PreparedStatement cmd = Database.getConnection().prepareStatement("SELECT * FROM Student");
            ResultSet result = cmd.executeQuery())
        {
            while (result.next())
                super.save(new Student(result.getInt("ID"), result.getString("Name"),
                            result.getInt("Grupa"), result.getString("Email"), result.getString("Profesor")));
        }
        catch (SQLException ignored) {
        }
    }

    @Override
    public Optional<Student> delete(Integer integer) {
        try (PreparedStatement cmd = Database.getConnection().prepareStatement("DELETE FROM Student WHERE ID = ?"))
        {
            cmd.setInt(1, integer);
            cmd.executeUpdate();

        } catch (SQLException ignored) {
        }
        return super.delete(integer);
    }

    @Override
    public Optional<Student> update(Integer integer, Student entity) {
        try (PreparedStatement cmd = Database.getConnection().prepareStatement("UPDATE Student SET [NAME] = ?, Email = ?, Profesor = ?, Grupa = ? WHERE ID = ?"))
        {
            cmd.setString(1, entity.getNume());
            cmd.setString(2, entity.getEmail());
            cmd.setString(3, entity.getProfesor());
            cmd.setInt(4, entity.getGrupa());
            cmd.setInt(5, entity.getId());
            cmd.executeUpdate();

        } catch (SQLException ignored) {
        }
        return super.update(integer, entity);
    }
}
