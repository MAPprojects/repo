package Repository;

import Domain.Event;
import Utils.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EventDatabaseRepository  extends AbstractRepository<Integer, Event> {
    public EventDatabaseRepository()
    {
        this.loadData();
    }

    public void saveUserEvent(Integer userId, Integer eventId)
    {
        try (PreparedStatement cmd = Database.getConnection().prepareStatement("INSERT INTO UserEvent(UID, EID) VALUES(?, ?)"))
        {
            cmd.setInt(1, userId);
            cmd.setInt(2, eventId);
            cmd.executeUpdate();

        } catch (SQLException ignored) {
        }
    }

    public void deleteUserEvent(Integer userId, Integer eventId)
    {
        try (PreparedStatement cmd = Database.getConnection().prepareStatement("DELETE FROM UserEvent WHERE UID = ? AND EID = ?"))
        {
            cmd.setInt(1, userId);
            cmd.setInt(2, eventId);
            cmd.executeUpdate();

        } catch (SQLException ignored) {
        }
    }

    @Override
    public void save(Event entity) {
        try (PreparedStatement cmd = Database.getConnection().prepareStatement("INSERT INTO Event([Name], DateEv, DateDeadline, Detalii) VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS))
        {
            cmd.setString(1, entity.getName());
            cmd.setDate(2, entity.getEventDate());
            cmd.setDate(3, entity.getDeadline());
            cmd.setString(4, entity.getDetails());
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

    private List<Integer> getUsers(Integer EventId)
    {
        List<Integer> user_list = new ArrayList<>();
        try (PreparedStatement cmd = Database.getConnection().prepareStatement("SELECT UID FROM UserEvent WHERE EID = ?"))
        {
            cmd.setInt(1, EventId);
            try (ResultSet result = cmd.executeQuery())
            {
                while (result.next())
                    user_list.add(result.getInt("UID"));
            }
        } catch (SQLException ignored) {
        }
        return user_list;
    }

    @Override
    public void loadData() {
        try (PreparedStatement cmd = Database.getConnection().prepareStatement("SELECT * FROM Event ORDER BY DateEv");
             ResultSet result = cmd.executeQuery())
        {
            while (result.next())
            {
                List<Integer> user_list = getUsers(result.getInt("ID"));
                super.save(new Event(result.getInt("ID"), result.getString("Name"),
                        result.getDate("DateEv"), result.getDate("DateDeadline"),
                        result.getString("Detalii"), user_list));
            }
        }
        catch (SQLException ignored) {
            System.out.println(ignored);
        }
    }

    @Override
    public Optional<Event> delete(Integer integer) {
        try (PreparedStatement cmd = Database.getConnection().prepareStatement("DELETE FROM Event WHERE ID = ?"))
        {
            cmd.setInt(1, integer);
            cmd.executeUpdate();

        } catch (SQLException ignored) {
        }
        return super.delete(integer);
    }

    @Override
    public Optional<Event> update(Integer integer, Event entity) {
        try (PreparedStatement cmd = Database.getConnection().prepareStatement("UPDATE Event SET [Name] = ?, DateEv = ?, DateDeadline = ?, Detalii = ? WHERE ID = ?"))
        {
            cmd.setString(1, entity.getName());
            cmd.setDate(2, entity.getEventDate());
            cmd.setDate(3, entity.getDeadline());
            cmd.setString(4, entity.getDetails());
            cmd.setInt(5, entity.getId());
            cmd.executeUpdate();
        } catch (SQLException ignored) {
        }
        return super.update(integer, entity);
    }
}
