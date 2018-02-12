package Repository;

import Domain.Homework;
import Utils.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

public class HomeworkDatabaseRepository extends AbstractRepository<Integer, Homework> {

    public HomeworkDatabaseRepository()
    {
        this.loadData();
    }

    @Override
    public void save(Homework entity) {
        try (PreparedStatement cmd = Database.getConnection().prepareStatement("INSERT INTO Homework(Task, Deadline) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS))
        {
            cmd.setString(1, entity.getTask());
            cmd.setInt(2, entity.getDeadline());
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
        try (PreparedStatement cmd = Database.getConnection().prepareStatement("SELECT * FROM Homework");
             ResultSet result = cmd.executeQuery())
        {
            while (result.next())
                super.save(new Homework(result.getInt("ID"), result.getString("Task"),
                        result.getInt("Deadline")));
        }
        catch (SQLException ignored) {
        }
    }

    @Override
    public Optional<Homework> delete(Integer integer) {
        try (PreparedStatement cmd = Database.getConnection().prepareStatement("DELETE FROM Homework WHERE ID = ?"))
        {
            cmd.setInt(1, integer);
            cmd.executeUpdate();

        } catch (SQLException ignored) {
        }
        return super.delete(integer);
    }

    @Override
    public Optional<Homework> update(Integer integer, Homework entity) {
        try (PreparedStatement cmd = Database.getConnection().prepareStatement("UPDATE Homework SET Task = ?, Deadline = ? WHERE ID = ?"))
        {
            cmd.setString(1, entity.getTask());
            cmd.setInt(2, entity.getDeadline());
            cmd.setInt(3, entity.getId());
            cmd.executeUpdate();

        } catch (SQLException ignored) {
        }
        return super.update(integer, entity);
    }
}
